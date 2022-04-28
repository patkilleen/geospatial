package test;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import aggregation.DataFusion;
import aggregation.SingleSummaryStatAggregator;
import data_structure.SpatialData;
import data_structure.SpatialDataIndex;
import data_structure.SpatialDataset;
import junit.framework.Assert;

class TestDataFusion {
	private static final int NUM_TEST_ITERATION_FOR_THREAD_RACE_CONDITION_TEST=50;
	
	@Test
	void testFuseDatasets_empty_neighborhoods() {
		
		
		SpatialDataset lowResDS = new SpatialDataset(32);
		//x, y, yield, speed

		//group 1

		lowResDS.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),""));


		//group 2
		lowResDS.addSpatialData(new SpatialData(2,new Point2D.Double(50,100),"")); //super far away, no nearby points
		
		
		
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5);
		
		
		
		Assert.assertEquals(1, res.size());


		
		SpatialData pt = res.getSpatialData(0);

		double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		Assert.assertEquals(1,pt.getId());
							
		Assert.assertEquals(104.5,actual[0],0.001);
		Assert.assertEquals(4.575,actual[1],0.001);	
		
		
		
		
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5);
		
		
		pt = res.getSpatialData(0);

		actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		Assert.assertEquals(1,pt.getId());
							
		Assert.assertEquals(104.5,actual[0],0.001);
		Assert.assertEquals(4.575,actual[1],0.001);	
		
		

	}
	
	@Test
	void testFuseDatasets_empty_neighborhoods2() {
		
		
		SpatialDataset lowResDS = new SpatialDataset(32);
		//x, y, yield, speed

		//group 1

		lowResDS.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),""));

		//group 2
		lowResDS.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),""));
		//group 3
		lowResDS.addSpatialData(new SpatialData(3,new Point2D.Double(50,100),"")); //super far away, no nearby points
		
		
		
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5);
		
		
		Assert.assertEquals(2, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(104.5,actual[0],0.001);
				Assert.assertEquals(4.575,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(125,actual[0],0.001);
				Assert.assertEquals(3.0666667,actual[1],0.001);
			}else if(pt.getId() == 3) {
				fail("expected empty neghborhdood but had results");

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
		
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5);
		
		Assert.assertEquals(2, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(104.5,actual[0],0.001);
				Assert.assertEquals(4.575,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(125,actual[0],0.001);
				Assert.assertEquals(3.0666667,actual[1],0.001);
			}else if(pt.getId() == 3) {
				fail("expected empty neghborhdood but had results");

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
		

	}

	
	
	@Test
	void testFuseDatasets_basic() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2.5);


		_testFuseDatasets_basic_2_5m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2.5);
		_testFuseDatasets_basic_2_5m_Euclidean(res);

		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2);
		_testFuseDatasets_basic_2m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2);
		_testFuseDatasets_basic_2_5m_Euclidean(res);
	}


	@Test
	void testFuseDatasets_mean() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5);
		_testFuseDatasets_mean_2_5m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5);
		_testFuseDatasets_mean_2_5m_Euclidean(res);

		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2);
		_testFuseDatasets_mean_2m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2);
		_testFuseDatasets_mean_2_5m_Euclidean(res);
	}



	@Test
	void testFuseDatasets_max() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2.5);
		_testFuseDatasets_max_2_5m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2.5);
		_testFuseDatasets_max_2_5m_Euclidean(res);

		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2);
		_testFuseDatasets_max_2m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2);
		_testFuseDatasets_max_2_5m_Euclidean(res);
	}


	@Test
	void testFuseDatasets_min() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5);
		_testFuseDatasets_min_2_5m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5);
		_testFuseDatasets_min_2_5m_Euclidean(res);

		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2);
		_testFuseDatasets_min_2m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2);
		_testFuseDatasets_min_2_5m_Euclidean(res);
	}

	@Test
	void testFuseDatasets_std_dev() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2.5);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2.5);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);

		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2);
		_testFuseDatasets_std_dev_2m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);
	}

	@Test
	void testFuseDatasets_median() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5);
		_testFuseDatasets_median_2_5m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5);
		_testFuseDatasets_median_2_5m_Euclidean(res);

		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2);
		_testFuseDatasets_median_2m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2);
		_testFuseDatasets_median_2_5m_Euclidean(res);
	}


	@Test
	void testFuseDatasets_mean_ALL() {
		SpatialDataset lowResDS = new SpatialDataset (1);
		lowResDS.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),""));
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 20);
		_testFuseDatasets_mean_all(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 20);
		_testFuseDatasets_mean_all(res);
	}

	@Test
	void testFuseDatasets_max_ALL() {
		SpatialDataset lowResDS = new SpatialDataset (1);
		lowResDS.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),""));
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 20);
		_testFuseDatasets_max_all(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 20);
		_testFuseDatasets_max_all(res);
	}

	@Test
	void testFuseDatasets_min_ALL() {
		SpatialDataset lowResDS = new SpatialDataset (1);
		lowResDS.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),""));
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 20);
		_testFuseDatasets_min_all(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 20);
		_testFuseDatasets_min_all(res);
	}

	@Test
	void testFuseDatasets_std_dev_ALL() {
		SpatialDataset lowResDS = new SpatialDataset (1);
		lowResDS.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),""));
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 20);
		_testFuseDatasets_std_dev_all(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 20);
		_testFuseDatasets_std_dev_all(res);
	}

	@Test
	void testFuseDatasets_median_ALL() {
		SpatialDataset lowResDS = new SpatialDataset (1);
		lowResDS.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),""));
		SpatialDataset highResDS = buildHighResTestDataset();

		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 20);
		_testFuseDatasets_median_all(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 20);
		_testFuseDatasets_median_all(res);
	}
	
	
	@Test
	void testFuseDatasets_basic_subsetsize_split() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		int subsetsize = 1;
		SpatialDataset res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_basic_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);


		subsetsize = 2;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_basic_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);

		subsetsize = 3;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_basic_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);

		subsetsize = 4;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_basic_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);


		subsetsize = 5;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_basic_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_basic_2_5m_Euclidean(res);

	}

	
	@Test
	void testFuseDatasets_mean_subsetsize_split() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		int subsetsize = 1;
		SpatialDataset res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_mean_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);


		subsetsize = 2;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_mean_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);

		subsetsize = 3;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_mean_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);

		subsetsize = 4;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_mean_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);


		subsetsize = 5;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_mean_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_mean_2_5m_Euclidean(res);

	}

	@Test
	void testFuseDatasets_max_subsetsize_split() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		int subsetsize = 1;
		SpatialDataset res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_max_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);


		subsetsize = 2;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_max_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);


		subsetsize = 3;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_max_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);


		subsetsize = 4;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_max_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);


		subsetsize = 5;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_max_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_max_2_5m_Euclidean(res);

	}

	@Test
	void testFuseDatasets_min_subsetsize_split() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		int subsetsize = 1;
		SpatialDataset res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_min_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);

		subsetsize = 2;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_min_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);

		subsetsize = 3;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_min_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);

		subsetsize = 4;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_min_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);

		subsetsize = 5;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_min_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MIN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_min_2_5m_Euclidean(res);

	}

	@Test
	void testFuseDatasets_std_dev_subsetsize_split() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		int subsetsize = 1;
		SpatialDataset res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_std_dev_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);

		subsetsize = 2;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_std_dev_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);

		subsetsize = 3;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);


		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_std_dev_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);

		subsetsize = 4;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_std_dev_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);

		subsetsize = 5;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_std_dev_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_std_dev_2_5m_Euclidean(res);

	}

	@Test
	void testFuseDatasets_median_subsetsize_split() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		int subsetsize = 1;
		SpatialDataset res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_median_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_median_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);

		subsetsize = 2;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_median_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);

		subsetsize = 3;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);


		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_median_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);

		subsetsize = 4;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_median_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);

		subsetsize = 5;
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);

		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,subsetsize);
		_testFuseDatasets_median_2m_Euclidean(res);
		res = splitAndAggregate(lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,subsetsize);
		_testFuseDatasets_median_2_5m_Euclidean(res);


	}

	@Test
	void testMultiThreaded_FuseDatasets_basic() {

		//do it 1000 times to check for race condidtion 
		for(int i = 0;i<NUM_TEST_ITERATION_FOR_THREAD_RACE_CONDITION_TEST;i++) {
			SpatialDataset lowResDS = buildLowResTestDataset();
			SpatialDataset highResDS = buildHighResTestDataset();

			SpatialDataset res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,4);
			_testFuseDatasets_basic_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,4);
			_testFuseDatasets_basic_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2,4);
			_testFuseDatasets_basic_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2,4);
			_testFuseDatasets_basic_2_5m_Euclidean(res);


			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,3);			
			_testFuseDatasets_basic_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,3);
			_testFuseDatasets_basic_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2,3);
			_testFuseDatasets_basic_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2,3);
			_testFuseDatasets_basic_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,2);			
			_testFuseDatasets_basic_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,2);
			_testFuseDatasets_basic_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2,2);
			_testFuseDatasets_basic_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2,2);
			_testFuseDatasets_basic_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,1);			
			_testFuseDatasets_basic_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,1);
			_testFuseDatasets_basic_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.EUCLIDEAN, 2,1);
			_testFuseDatasets_basic_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.BASIC, SpatialData.DistanceMetric.INFINITY_NORM, 2,1);
			_testFuseDatasets_basic_2_5m_Euclidean(res);
		}
	}


	
	@Test
	void testMultiThreaded_FuseDatasets_mean() {

		//do it 1000 times to check for race condidtion 
		for(int i = 0;i<NUM_TEST_ITERATION_FOR_THREAD_RACE_CONDITION_TEST;i++) {
			SpatialDataset lowResDS = buildLowResTestDataset();
			SpatialDataset highResDS = buildHighResTestDataset();

			SpatialDataset res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,4);
			_testFuseDatasets_mean_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,4);
			_testFuseDatasets_mean_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,4);
			_testFuseDatasets_mean_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,4);
			_testFuseDatasets_mean_2_5m_Euclidean(res);


			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,3);			
			_testFuseDatasets_mean_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,3);
			_testFuseDatasets_mean_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,3);
			_testFuseDatasets_mean_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,3);
			_testFuseDatasets_mean_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,2);			
			_testFuseDatasets_mean_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,2);
			_testFuseDatasets_mean_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,2);
			_testFuseDatasets_mean_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,2);
			_testFuseDatasets_mean_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,1);			
			_testFuseDatasets_mean_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,1);
			_testFuseDatasets_mean_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,1);
			_testFuseDatasets_mean_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,1);
			_testFuseDatasets_mean_2_5m_Euclidean(res);
		}
	}

	@Test
	void testMultiThreaded_FuseDatasets_max() {

		//do it 1000 times to check for race condidtion 
		for(int i = 0;i<NUM_TEST_ITERATION_FOR_THREAD_RACE_CONDITION_TEST;i++) {
			SpatialDataset lowResDS = buildLowResTestDataset();
			SpatialDataset highResDS = buildHighResTestDataset();

			SpatialDataset res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,4);
			_testFuseDatasets_max_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,4);
			_testFuseDatasets_max_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2,4);
			_testFuseDatasets_max_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2,4);
			_testFuseDatasets_max_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,3);			
			_testFuseDatasets_max_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,3);
			_testFuseDatasets_max_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2,3);
			_testFuseDatasets_max_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2,3);
			_testFuseDatasets_max_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,2);			
			_testFuseDatasets_max_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,2);
			_testFuseDatasets_max_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2,2);
			_testFuseDatasets_max_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2,2);
			_testFuseDatasets_max_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,1);			
			_testFuseDatasets_max_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,1);
			_testFuseDatasets_max_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.EUCLIDEAN, 2,1);
			_testFuseDatasets_max_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MAX, SpatialData.DistanceMetric.INFINITY_NORM, 2,1);
			_testFuseDatasets_max_2_5m_Euclidean(res);

		}
	}

	@Test
	void testMultiThreaded_FuseDatasets_std_dev() {

		//do it 1000 times to check for race condidtion 
		for(int i = 0;i<NUM_TEST_ITERATION_FOR_THREAD_RACE_CONDITION_TEST;i++) {
			SpatialDataset lowResDS = buildLowResTestDataset();
			SpatialDataset highResDS = buildHighResTestDataset();

			SpatialDataset res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,4);
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,4);
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2,4);
			_testFuseDatasets_std_dev_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2,4);
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);


			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,3);			
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,3);
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2,3);
			_testFuseDatasets_std_dev_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2,3);
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);


			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,2);			
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,2);
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2,2);
			_testFuseDatasets_std_dev_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2,2);
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,1);			
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,1);
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.EUCLIDEAN, 2,1);
			_testFuseDatasets_std_dev_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION, SpatialData.DistanceMetric.INFINITY_NORM, 2,1);
			_testFuseDatasets_std_dev_2_5m_Euclidean(res);

		}
	}


	@Test
	void testMultiThreaded_FuseDatasets_median() {

		//do it 1000 times to check for race condidtion 
		for(int i = 0;i<NUM_TEST_ITERATION_FOR_THREAD_RACE_CONDITION_TEST;i++) {
			SpatialDataset lowResDS = buildLowResTestDataset();
			SpatialDataset highResDS = buildHighResTestDataset();

			SpatialDataset res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,4);
			_testFuseDatasets_median_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,4);
			_testFuseDatasets_median_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,4);
			_testFuseDatasets_median_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,4);
			_testFuseDatasets_median_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,3);			
			_testFuseDatasets_median_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,3);
			_testFuseDatasets_median_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,3);
			_testFuseDatasets_median_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,3);
			_testFuseDatasets_median_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,2);			
			_testFuseDatasets_median_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,2);
			_testFuseDatasets_median_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,2);
			_testFuseDatasets_median_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,2);
			_testFuseDatasets_median_2_5m_Euclidean(res);

			lowResDS = buildLowResTestDataset();
			highResDS = buildHighResTestDataset();
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2.5,1);			
			_testFuseDatasets_median_2_5m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2.5,1);
			_testFuseDatasets_median_2_5m_Euclidean(res);

			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.EUCLIDEAN, 2,1);
			_testFuseDatasets_median_2m_Euclidean(res);
			res = DataFusion.multiThreaded_FuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN, SpatialData.DistanceMetric.INFINITY_NORM, 2,1);
			_testFuseDatasets_median_2_5m_Euclidean(res);
		}
	}


	@Test
	void testFuseDatasets_data_defined_search_radius() {
		SpatialDataset lowResDS = buildLowResTestDataset();
		SpatialDataset highResDS = buildHighResTestDataset();

		Iterator<SpatialData> it = lowResDS.iterator();
		
		while(it.hasNext()) {
			SpatialData pt = it.next();
			pt.overrideNeighborhoodSearchRadius(2.5);
		}
		SpatialDataset res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 50);
		_testFuseDatasets_mean_2_5m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 50);
		_testFuseDatasets_mean_2_5m_Euclidean(res);

		it = lowResDS.iterator();
		
		while(it.hasNext()) {
			SpatialData pt = it.next();
			pt.overrideNeighborhoodSearchRadius(2);
		}
		
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 50);
		_testFuseDatasets_mean_2m_Euclidean(res);
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 50);
		_testFuseDatasets_mean_2_5m_Euclidean(res);
		
		
		

		it = lowResDS.iterator();
		
		while(it.hasNext()) {
			SpatialData pt = it.next();
			
			if(pt.getId() == 1) {
				pt.overrideNeighborhoodSearchRadius(1000);//alll points in neighbordhood
			}
			
		}
		
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.EUCLIDEAN, 50);
		
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				

				Assert.assertEquals(134.4285714,actual[0],0.001);//all points mean
				Assert.assertEquals(4.678571429,actual[1],0.001);//all points mean
			}else if(pt.getId() == 2) {

				Assert.assertEquals(127.5,actual[0],0.001);
				Assert.assertEquals(3.1,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(177,actual[0],0.001);
				Assert.assertEquals(7.25,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(139.5,actual[0],0.001);
				Assert.assertEquals(4.0625,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
		
		res = DataFusion.fuseDatasets(SpatialData.COMMA,lowResDS, highResDS, SingleSummaryStatAggregator.SingleSummaryStat.MEAN, SpatialData.DistanceMetric.INFINITY_NORM, 50);
		
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				

				Assert.assertEquals(134.4285714,actual[0],0.001);//all points mean
				Assert.assertEquals(4.678571429,actual[1],0.001);//all points mean
			}else if(pt.getId() == 2) {

				Assert.assertEquals(125,actual[0],0.001);
				Assert.assertEquals(3.0666667,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(177,actual[0],0.001);
				Assert.assertEquals(7.25,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(139.5,actual[0],0.001);
				Assert.assertEquals(4.0625,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
		
		
	}


	/**
	 * builds dataset of the points in the data/test/testing.xlsx:data-fusion
	 * @return
	 */
	public static SpatialDataset buildHighResTestDataset() {
		SpatialDataset ds = new SpatialDataset(32);
		//x, y, yield, speed

		//the max width of all neieghobrhoods is 3 distance
		//group 1
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(1,2),"100,5"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,1),"105,4.5"));
		ds.addSpatialData( new SpatialData(3,new Point2D.Double(1,-1),"103,4.8"));
		ds.addSpatialData( new SpatialData(4,new Point2D.Double(0.5,-0.5),"110,4"));


		//group 2
		ds.addSpatialData(new SpatialData(5,new Point2D.Double(10,5),"120,3"));
		ds.addSpatialData(new SpatialData(6,new Point2D.Double(9,6),"125,3.2"));
		ds.addSpatialData(new SpatialData(7,new Point2D.Double(8,7),"130,3"));


		//group 3
		ds.addSpatialData(new SpatialData(8,new Point2D.Double(-5,-4),"190,7"));
		ds.addSpatialData(new SpatialData(9,new Point2D.Double(-4,-4),"186,7.5"));
		ds.addSpatialData(new SpatialData(10,new Point2D.Double(-5,-5),"155,7.25"));


		//group 4
		ds.addSpatialData(new SpatialData(11,new Point2D.Double(-5,9),"145,2"));
		ds.addSpatialData(new SpatialData(12,new Point2D.Double(-4,7),"120,4"));
		ds.addSpatialData(new SpatialData(13,new Point2D.Double(-5,7),"170,6"));
		ds.addSpatialData(new SpatialData(14,new Point2D.Double(-4,7),"123,4.25"));

		return ds;
	}


	public static SpatialDataset splitAndAggregate(SpatialDataset lowResPoints, 
			SpatialDataset highResPoints,
			SingleSummaryStatAggregator.SingleSummaryStat aggOp,
			SpatialData.DistanceMetric distMetric, 
			double neighborhoodRadius, int subsetSize) {

		SpatialDataset res =  new SpatialDataset(lowResPoints.size());
		//build index over high res points
		SpatialDataIndex index = new SpatialDataIndex(highResPoints);



		List<SpatialDataset> subsets = lowResPoints.split(subsetSize);				

		for(int i = 0;i<subsets.size();i++) {
			SpatialDataset subset = subsets.get(i);

			SpatialDataset tmpResult = DataFusion._fuseDatasets(SpatialData.COMMA,subset, highResPoints,aggOp,distMetric, neighborhoodRadius,index);



			//iterattre over all points aggregated by worker
			Iterator<SpatialData> it =tmpResult.iterator();

			while(it.hasNext()) {
				SpatialData pt = it.next();

				res.addSpatialData(pt);
			}

		}//end ewait for worker

		return res;
	}
	/**
	 * builds dataset of the points in the data/test/testing.xlsx:data-fusion
	 * @return
	 */
	public static SpatialDataset buildLowResTestDataset() {
		SpatialDataset ds = new SpatialDataset(32);
		//x, y, yield, speed

		//group 1

		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),""));


		//group 2
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),""));


		//group 3
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),""));


		//group 4
		ds.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),""));

		return ds;
	}



	public static void _testFuseDatasets_basic_2_5m_Euclidean(SpatialDataset actual) {


		SpatialDataset expectedId_1 = new SpatialDataset();
		SpatialDataset expectedId_2 = new SpatialDataset();
		SpatialDataset expectedId_3 = new SpatialDataset();
		SpatialDataset expectedId_4 = new SpatialDataset();

		SpatialDataset expected = new SpatialDataset();
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"1,1.0,2.0,100,5"));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2,0.0,1.0,105,4.5"));
		expected.addSpatialData( new SpatialData(1,new Point2D.Double(0,0),"3,1.0,-1.0,103,4.8"));
		expected.addSpatialData( new SpatialData(1,new Point2D.Double(0,0),"4,0.5,-0.5,110,4"));


		expectedId_1.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"1,1.0,2.0,100,5"));
		expectedId_1.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2,0.0,1.0,105,4.5"));
		expectedId_1.addSpatialData( new SpatialData(1,new Point2D.Double(0,0),"3,1.0,-1.0,103,4.8"));
		expectedId_1.addSpatialData( new SpatialData(1,new Point2D.Double(0,0),"4,0.5,-0.5,110,4"));

		//group 2
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"5,10.0,5.0,120,3"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"6,9.0,6.0,125,3.2"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"7,8.0,7.0,130,3"));

		expectedId_2.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"5,10.0,5.0,120,3"));
		expectedId_2.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"6,9.0,6.0,125,3.2"));
		expectedId_2.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"7,8.0,7.0,130,3"));


		//group 3
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"8,-5.0,-4.0,190,7"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"9,-4.0,-4.0,186,7.5"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"10,-5.0,-5.0,155,7.25"));


		expectedId_3.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"8,-5.0,-4.0,190,7"));
		expectedId_3.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"9,-4.0,-4.0,186,7.5"));
		expectedId_3.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"10,-5.0,-5.0,155,7.25"));


		//group 4
		expected.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"11,-5.0,9.0,145,2"));
		expected.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"12,-4.0,7.0,120,4"));
		expected.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"13,-5.0,7.0,170,6"));
		expected.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"14,-4.0,7.0,123,4.25"));


		expectedId_4.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"11,-5.0,9.0,145,2"));
		expectedId_4.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"12,-4.0,7.0,120,4"));
		expectedId_4.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"13,-5.0,7.0,170,6"));
		expectedId_4.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"14,-4.0,7.0,123,4.25"));

		Assert.assertEquals(expected.size(), actual.size());
		actual.sort();
		expected.sort();

		for(int i = 0; i < expected.size();i++) {
			//SpatialData pt1 = expected.getSpatialData(i);
			SpatialData pt = actual.getSpatialData(i);

			//Assert.assertEquals(pt1.getId(),pt2.getId());
			//Assert.assertEquals(pt1.getLocation().getX(),pt2.getLocation().getX(),0.0001);
			//Assert.assertEquals(pt1.getLocation().getY(),pt2.getLocation().getY(),0.0001);

			boolean samePt = false;
			SpatialDataset expectedPts = null;
			if(pt.getId()==1) {
				expectedPts=expectedId_1;

			}else if(pt.getId()==2) {

				expectedPts=expectedId_2;
			}else if(pt.getId()==3) {

				expectedPts=expectedId_3;

			}else if(pt.getId()==4) {

				expectedPts=expectedId_4;
			}	
			
			for(int j = 0; j < expectedPts.size();j++) {
				SpatialData pt2 = expectedPts.getSpatialData(j);
				
				if(pt2.getId() == pt.getId()) {
					if(pt2.getLocation().getX() == pt.getLocation().getX()) {
						if(pt2.getLocation().getY() == pt.getLocation().getY()) {
							if(pt2.getAttributes().equals(pt.getAttributes())) {
								samePt=true;
							}
						}
					}
				}
			}

			Assert.assertTrue(samePt);
		}


	}


	public static void _testFuseDatasets_basic_2m_Euclidean(SpatialDataset actual) {


		SpatialDataset expectedId_1 = new SpatialDataset();
		SpatialDataset expectedId_2 = new SpatialDataset();
		SpatialDataset expectedId_3 = new SpatialDataset();
		SpatialDataset expectedId_4 = new SpatialDataset();

		SpatialDataset expected = new SpatialDataset();
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2,0.0,1.0,105,4.5"));
		expected.addSpatialData( new SpatialData(1,new Point2D.Double(0,0),"3,1.0,-1.0,103,4.8"));
		expected.addSpatialData( new SpatialData(1,new Point2D.Double(0,0),"4,0.5,-0.5,110,4"));


		expectedId_1.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2,0.0,1.0,105,4.5"));
		expectedId_1.addSpatialData( new SpatialData(1,new Point2D.Double(0,0),"3,1.0,-1.0,103,4.8"));
		expectedId_1.addSpatialData( new SpatialData(1,new Point2D.Double(0,0),"4,0.5,-0.5,110,4"));

		//group 2
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"6,9.0,6.0,125,3.2"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"7,8.0,7.0,130,3"));

		expectedId_2.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"6,9.0,6.0,125,3.2"));
		expectedId_2.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"7,8.0,7.0,130,3"));


		//group 3
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"8,-5.0,-4.0,190,7"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"9,-4.0,-4.0,186,7.5"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"10,-5.0,-5.0,155,7.25"));


		expectedId_3.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"8,-5.0,-4.0,190,7"));
		expectedId_3.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"9,-4.0,-4.0,186,7.5"));
		expectedId_3.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"10,-5.0,-5.0,155,7.25"));


		//group 4
		expected.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"11,-5.0,9.0,145,2"));
		expected.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"12,-4.0,7.0,120,4"));
		expected.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"13,-5.0,7.0,170,6"));
		expected.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"14,-4.0,7.0,123,4.25"));


		expectedId_4.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"11,-5.0,9.0,145,2"));
		expectedId_4.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"12,-4.0,7.0,120,4"));
		expectedId_4.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"13,-5.0,7.0,170,6"));
		expectedId_4.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"14,-4.0,7.0,123,4.25"));

		Assert.assertEquals(expected.size(), actual.size());
		actual.sort();
		expected.sort();

		for(int i = 0; i < expected.size();i++) {
			//SpatialData pt1 = expected.getSpatialData(i);
			SpatialData pt = actual.getSpatialData(i);

			//Assert.assertEquals(pt1.getId(),pt2.getId());
			//Assert.assertEquals(pt1.getLocation().getX(),pt2.getLocation().getX(),0.0001);
			//Assert.assertEquals(pt1.getLocation().getY(),pt2.getLocation().getY(),0.0001);

			boolean samePt = false;
			SpatialDataset expectedPts = null;
			if(pt.getId()==1) {
				expectedPts=expectedId_1;

			}else if(pt.getId()==2) {

				expectedPts=expectedId_2;
			}else if(pt.getId()==3) {

				expectedPts=expectedId_3;

			}else if(pt.getId()==4) {

				expectedPts=expectedId_4;
			}	
			
			for(int j = 0; j < expectedPts.size();j++) {
				SpatialData pt2 = expectedPts.getSpatialData(j);
				
				if(pt2.getId() == pt.getId()) {
					if(pt2.getLocation().getX() == pt.getLocation().getX()) {
						if(pt2.getLocation().getY() == pt.getLocation().getY()) {
							if(pt2.getAttributes().equals(pt.getAttributes())) {
								samePt=true;
							}
						}
					}
				}
			}

			Assert.assertTrue(samePt);
		}


	}



	public static void _testFuseDatasets_mean_2_5m_Euclidean(SpatialDataset res) {
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(104.5,actual[0],0.001);
				Assert.assertEquals(4.575,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(125,actual[0],0.001);
				Assert.assertEquals(3.0666667,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(177,actual[0],0.001);
				Assert.assertEquals(7.25,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(139.5,actual[0],0.001);
				Assert.assertEquals(4.0625,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
	}

	public 	static void _testFuseDatasets_max_2_5m_Euclidean(SpatialDataset res) {
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(110,actual[0],0.001);
				Assert.assertEquals(5,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(130,actual[0],0.001);
				Assert.assertEquals(3.2,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(190,actual[0],0.001);
				Assert.assertEquals(7.5,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(170,actual[0],0.001);
				Assert.assertEquals(6,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
	}


	public static void _testFuseDatasets_min_2_5m_Euclidean(SpatialDataset res) {
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(100,actual[0],0.001);
				Assert.assertEquals(4,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(120,actual[0],0.001);
				Assert.assertEquals(3,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(155,actual[0],0.001);
				Assert.assertEquals(7,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(120,actual[0],0.001);
				Assert.assertEquals(2,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
	}


	public static void _testFuseDatasets_std_dev_2_5m_Euclidean(SpatialDataset res) {
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(3.640054945,actual[0],0.001);
				Assert.assertEquals(0.376662979,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(4.082482905,actual[0],0.001);
				Assert.assertEquals(0.094280904,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(15.6418242,actual[0],0.001);
				Assert.assertEquals(0.204124145,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(20.08108563,actual[0],0.001);
				Assert.assertEquals(1.418350715,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
	}

	public 	static void _testFuseDatasets_median_2_5m_Euclidean(SpatialDataset res) {
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(104,actual[0],0.001);
				Assert.assertEquals(4.65,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(125,actual[0],0.001);
				Assert.assertEquals(3,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(186,actual[0],0.001);
				Assert.assertEquals(7.25,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(134,actual[0],0.001);
				Assert.assertEquals(4.125,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
	}

	public static void _testFuseDatasets_mean_2m_Euclidean(SpatialDataset res) {
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(106,actual[0],0.001);
				Assert.assertEquals(4.433333333,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(127.5,actual[0],0.001);
				Assert.assertEquals(3.1,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(177,actual[0],0.001);
				Assert.assertEquals(7.25,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(139.5,actual[0],0.001);
				Assert.assertEquals(4.0625,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
	}

	public static void _testFuseDatasets_max_2m_Euclidean(SpatialDataset res) {
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(110,actual[0],0.001);
				Assert.assertEquals(4.8,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(130,actual[0],0.001);
				Assert.assertEquals(3.2,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(190,actual[0],0.001);
				Assert.assertEquals(7.5,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(170,actual[0],0.001);
				Assert.assertEquals(6,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
	}


	public static void _testFuseDatasets_min_2m_Euclidean(SpatialDataset res) {
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(103,actual[0],0.001);
				Assert.assertEquals(4,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(125,actual[0],0.001);
				Assert.assertEquals(3,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(155,actual[0],0.001);
				Assert.assertEquals(7,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(120,actual[0],0.001);
				Assert.assertEquals(2,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
	}


	public static void _testFuseDatasets_std_dev_2m_Euclidean(SpatialDataset res) {
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(2.943920289,actual[0],0.001);
				Assert.assertEquals(0.329983165,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(2.5,actual[0],0.001);
				Assert.assertEquals(0.1,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(15.6418242,actual[0],0.001);
				Assert.assertEquals(0.204124145,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(20.08108563,actual[0],0.001);
				Assert.assertEquals(1.418350715,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
	}

	public static void _testFuseDatasets_median_2m_Euclidean(SpatialDataset res) {
		Assert.assertEquals(4, res.size());


		for(int i = 0; i < res.size();i++) {
			SpatialData pt = res.getSpatialData(i);

			double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			if(pt.getId() == 1) {								
				Assert.assertEquals(105,actual[0],0.001);
				Assert.assertEquals(4.5,actual[1],0.001);
			}else if(pt.getId() == 2) {

				Assert.assertEquals(127.5,actual[0],0.001);
				Assert.assertEquals(3.1,actual[1],0.001);
			}else if(pt.getId() == 3) {
				Assert.assertEquals(186,actual[0],0.001);
				Assert.assertEquals(7.25,actual[1],0.001);
			}else if(pt.getId() == 4) {
				Assert.assertEquals(134,actual[0],0.001);
				Assert.assertEquals(4.125,actual[1],0.001);

			}else {
				fail("incorrect id of "+pt.getId());
			}
		}
	}


	public static void _testFuseDatasets_mean_all(SpatialDataset res) {
		Assert.assertEquals(1, res.size());



		SpatialData pt = res.getSpatialData(0);

		double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);

		Assert.assertEquals(134.4285714,actual[0],0.001);
		Assert.assertEquals(4.678571429,actual[1],0.001);


	}


	public static void _testFuseDatasets_max_all(SpatialDataset res) {
		Assert.assertEquals(1, res.size());



		SpatialData pt = res.getSpatialData(0);

		double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);

		Assert.assertEquals(190,actual[0],0.001);
		Assert.assertEquals(7.5,actual[1],0.001);


	}


	public static void _testFuseDatasets_min_all(SpatialDataset res) {
		Assert.assertEquals(1, res.size());



		SpatialData pt = res.getSpatialData(0);

		double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);

		Assert.assertEquals(100,actual[0],0.001);
		Assert.assertEquals(2,actual[1],0.001);


	}


	public static void _testFuseDatasets_std_dev_all(SpatialDataset res) {
		Assert.assertEquals(1, res.size());



		SpatialData pt = res.getSpatialData(0);

		double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);

		Assert.assertEquals(29.08046543,actual[0],0.001);
		Assert.assertEquals(1.646610619,actual[1],0.001);


	}

	public static void _testFuseDatasets_median_all(SpatialDataset res) {
		Assert.assertEquals(1, res.size());



		SpatialData pt = res.getSpatialData(0);

		double [] actual = pt.parseAttributesToDoubleArray(SpatialData.COMMA);

		Assert.assertEquals(124,actual[0],0.001);
		Assert.assertEquals(4.375,actual[1],0.001);


	}

}
