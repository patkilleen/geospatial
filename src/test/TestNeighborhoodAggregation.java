package test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

import aggregation.SingleSummaryStatAggregator;
import aggregation.SpatialDataAggregator;
import aggregation.SingleSummaryStatAggregator.SingleSummaryStat;
import core.Util;
import data_structure.SpatialData;
import data_structure.SpatialDataIndex;
import data_structure.SpatialDataset;
import junit.framework.Assert;

class TestNeighborhoodAggregation {
	public final static double DOUBLE_EQUALITY_EPSILON = 0.00001;
	@Test
	void testMax() {
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MAX,SpatialData.COMMA);
		
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1.0,2.0");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"-5.0,4.0");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),"10.0,3.0");
		SpatialData sd4 = new SpatialData(4,new Point2D.Double(5,23),"0.0,9.0");
		SpatialData sd5 = new SpatialData(5,new Point2D.Double(10,-15),"-3.0,-4.0");
		
		SpatialDataset ds = new SpatialDataset(2);

		SpatialDataset output = new SpatialDataset(1);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	
		SpatialData target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,20.0");
		
		String expectedAttributes = "10.0,20.0,10.0,9.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		SpatialData actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,15),"15.0,3.0"));
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		expectedAttributes = "10.0,20.0,15.0,9.0";
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		/*
		 * NOW WITH 1 MORE ATTRIBUTE
		 */
		

		sd1.setAttributes("1.0,2.0,1.0");
		sd2.setAttributes("-5.0,4.0,2.0");
		sd3.setAttributes("10.0,3.0,3.0");
		sd4.setAttributes("0.0,9.0,4.0");
		sd5.setAttributes("-3.0,-4.0,5.0");
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,40.0");
		
		expectedAttributes = "10.0,40.0,10.0,9.0,5.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		
		
		/*
		 * NOW WITH only 1 ATTRIBUTE
		 */
		

		sd1.setAttributes("1.0");
		sd2.setAttributes("-5.0");
		sd3.setAttributes("10.0");
		sd4.setAttributes("52.0");
		sd5.setAttributes("-3.0");
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"11.0,20.0");
		
		expectedAttributes = "11.0,20.0,52.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
	}
	
	@Test
	void testAgg_empty_dataset() {
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MAX,SpatialData.COMMA);
					
		SpatialDataset ds = new SpatialDataset(2);

		SpatialDataset output = new SpatialDataset(1);
			
		SpatialData target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,20.0");
		
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(0, output.size());
		
		
		agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MIN,SpatialData.COMMA);
		
		ds = new SpatialDataset(2);

		output = new SpatialDataset(1);
			
		target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,20.0");
		
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(0, output.size());
		

		agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MEAN,SpatialData.COMMA);
		
		ds = new SpatialDataset(2);

		output = new SpatialDataset(1);
			
		target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,20.0");
		
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(0, output.size());


	}
	
	@Test
	void testAgg_empty_target_attributes() {
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MIN,SpatialData.COMMA);
		
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1.0,2.0");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"-5.0,4.0");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),"10.0,3.0");
		SpatialData sd4 = new SpatialData(4,new Point2D.Double(5,23),"0.0,9.0");
		SpatialData sd5 = new SpatialData(5,new Point2D.Double(10,-15),"-3.0,-4.0");
		
		SpatialDataset ds = new SpatialDataset(2);

		SpatialDataset output = new SpatialDataset(1);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	
		SpatialData target = new SpatialData(7,new Point2D.Double(10,-15),"");
		
		String expectedAttributes = "-5.0,-4.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		SpatialData actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
	}
	

	

	@Test
	void testAgg_non_numeric_target_attributes() {
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MIN,SpatialData.COMMA);
		
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1.0,2.0");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"-5.0,4.0");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),"10.0,3.0");
		SpatialData sd4 = new SpatialData(4,new Point2D.Double(5,23),"0.0,9.0");
		SpatialData sd5 = new SpatialData(5,new Point2D.Double(10,-15),"-3.0,-4.0");
		
		SpatialDataset ds = new SpatialDataset(2);

		SpatialDataset output = new SpatialDataset(1);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	
		SpatialData target = new SpatialData(7,new Point2D.Double(10,-15),"helloworld!");
		
		String expectedAttributes = "helloworld!,-5.0,-4.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		SpatialData actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
	}
	@Test
	void testAgg_NULL_args() {
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MAX,SpatialData.COMMA);
					
		SpatialDataset ds = new SpatialDataset(2);

		SpatialDataset output = new SpatialDataset(1);
			
		SpatialData target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,20.0");
		
		
		
		boolean exceptionOccured = false;


		try {
			agg.applyAggregation(null, ds, output);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;


		try {
			agg.applyAggregation(target, null, output);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;


		try {
			agg.applyAggregation(target, ds, null);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
	}
	@Test
	void testMin() {
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MIN,SpatialData.COMMA);
		
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1.0,2.0");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"-5.0,4.0");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),"10.0,3.0");
		SpatialData sd4 = new SpatialData(4,new Point2D.Double(5,23),"0.0,9.0");
		SpatialData sd5 = new SpatialData(5,new Point2D.Double(10,-15),"-3.0,-4.0");
		
		SpatialDataset ds = new SpatialDataset(2);

		SpatialDataset output = new SpatialDataset(1);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	
		SpatialData target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,20.0");
		
		String expectedAttributes = "10.0,20.0,-5.0,-4.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		SpatialData actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		
		/*
		 * NOW WITH 1 MORE ATTRIBUTE
		 */
		

		sd1.setAttributes("1.0,2.0,1.0");
		sd2.setAttributes("-5.0,4.0,2.0");
		sd3.setAttributes("10.0,3.0,3.0");
		sd4.setAttributes("0.0,9.0,4.0");
		sd5.setAttributes("-3.0,-4.0,5.0");
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,40.0");
		
		expectedAttributes = "10.0,40.0,-5.0,-4.0,1.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		
		
		/*
		 * NOW WITH only 1 ATTRIBUTE
		 */
		

		sd1.setAttributes("1.0");
		sd2.setAttributes("-5.0");
		sd3.setAttributes("10.0");
		sd4.setAttributes("-52.0");
		sd5.setAttributes("-3.0");
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"11.0,20.0");
		
		expectedAttributes = "11.0,20.0,-52.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
	}
		
	
	@Test
	void testMedian() {
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MEDIAN,SpatialData.COMMA);
		
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1.0,2.0");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"-5.0,4.0");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),"10.0,3.0");
		SpatialData sd4 = new SpatialData(4,new Point2D.Double(5,23),"0.0,9.0");
		SpatialData sd5 = new SpatialData(5,new Point2D.Double(10,-15),"-3.0,-4.0");
		SpatialData sd6 = new SpatialData(5,new Point2D.Double(10,-15),"15,-2.5");
		SpatialData sd7 = new SpatialData(5,new Point2D.Double(10,-15),"-2.5,9");
		
		SpatialDataset ds = new SpatialDataset(2);

		SpatialDataset output = new SpatialDataset(1);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);
		ds.addSpatialData(sd6);	
		ds.addSpatialData(sd7);	
		SpatialData target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,20.0");
		
		String expectedAttributes = "10.0,20.0,0.0,3.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		SpatialData actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		
		/*
		 * NOW WITH 1 MORE ATTRIBUTE
		 */
		

		sd1.setAttributes("1.0,2.0,1.0");
		sd2.setAttributes("-5.0,4.0,2.0");
		sd3.setAttributes("10.0,3.0,3.0");
		sd4.setAttributes("0.0,9.0,4.0");
		sd5.setAttributes("-3.0,-4.0,5.0");
		sd6.setAttributes("15,-2.5,5.0");
		sd7.setAttributes("-2.5,9,-2.0");
		
		SpatialData sd8 = new SpatialData(5,new Point2D.Double(10,-15),"9,8,7");
		
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);
		ds.addSpatialData(sd6);	
		ds.addSpatialData(sd7);
		ds.addSpatialData(sd8);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,40.0,22.0");
		
		expectedAttributes = "10.0,40.0,22.0,0.5,3.5,3.5";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		
		
		/*
		 * NOW WITH only 1 ATTRIBUTE
		 */
		

		sd1.setAttributes("1.0");
		sd2.setAttributes("-15.0");
		sd3.setAttributes("7");
		sd4.setAttributes("-9");
		
		
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"11.0,20.0");
		
		expectedAttributes = "11.0,20.0,-4.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
	}
	
	
	@Test
	void testMean() {
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MEAN,SpatialData.COMMA);
		
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"3,3");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"-5.0,8");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),"10.0,-3");
		SpatialData sd4 = new SpatialData(4,new Point2D.Double(5,23),"2,4");
		SpatialData sd5 = new SpatialData(5,new Point2D.Double(10,-15),"5,8");
		
		SpatialDataset ds = new SpatialDataset(2);

		SpatialDataset output = new SpatialDataset(1);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	
		SpatialData target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,20.0");
		
		String expectedAttributes = "10.0,20.0,3.0,4.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		SpatialData actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		
		/*
		 * NOW WITH 1 MORE ATTRIBUTE
		 */
		

		sd1.setAttributes("3,3,1.0");
		sd2.setAttributes("-5.0,8,2.0");
		sd3.setAttributes("10.0,-3.0,3.0");
		sd4.setAttributes("2,4,4.0");
		sd5.setAttributes("5,8,5.0");
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,40.0");
		
		expectedAttributes = "10.0,40.0,3.0,4.0,3.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		
		
		/*
		 * NOW WITH only 1 ATTRIBUTE
		 */
		

		sd1.setAttributes("-1.0");
		sd2.setAttributes("-2.0");
		sd3.setAttributes("-3.0");
		sd4.setAttributes("5.0");
		sd5.setAttributes("6.0");
		
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"11.0,20.0");
		
		expectedAttributes = "11.0,20.0,1.0";
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		Assert.assertEquals(true,expectedAttributes.equals(actual.getAttributes()));
		
		
		
		
		
		sd1.setAttributes("-10.53");
		sd2.setAttributes("-22.0");
		sd3.setAttributes("-0.3");
		sd4.setAttributes("5.2");
		sd5.setAttributes("6.35");
		
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"11.123,-0.45");
		
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		double res [] = actual.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		
		
		Assert.assertEquals(true,Util.almostEqual(11.123,res[0],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(-0.45,res[1],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(-4.256,res[2],DOUBLE_EQUALITY_EPSILON));
		
		
	}
		
	@Test
	void testStdDev() {
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.STANDARD_DEVIATION,SpatialData.COMMA);
		
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"3,3");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"-5.0,8");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),"10.0,-3");
		SpatialData sd4 = new SpatialData(4,new Point2D.Double(5,23),"2,4");
		SpatialData sd5 = new SpatialData(5,new Point2D.Double(10,-15),"5,8");
		
		SpatialDataset ds = new SpatialDataset(2);

		SpatialDataset output = new SpatialDataset(1);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	
		SpatialData target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,20.0");
		
		
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		SpatialData actual = output.getSpatialData(0);
		
		
		double res [] = actual.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		
		
		Assert.assertEquals(true,Util.almostEqual(4.85793,res[2],0.001));
		Assert.assertEquals(true,Util.almostEqual(4.04969,res[3],0.001));
		
		
		
		
		
		/*
		 * NOW WITH 1 MORE ATTRIBUTE
		 */
		

		sd1.setAttributes("3,3,1.0");
		sd2.setAttributes("-5.0,8,2.0");
		sd3.setAttributes("10.0,-3.0,3.0");
		sd4.setAttributes("2,4,4.0");
		sd5.setAttributes("5,8,5.0");
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"10.0,40.0");
		
		
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
	
		res = actual.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		
		
		Assert.assertEquals(true,Util.almostEqual(4.85793,res[2],0.001));
		Assert.assertEquals(true,Util.almostEqual(4.04969,res[3],0.001));
		Assert.assertEquals(true,Util.almostEqual(1.41421,res[4],0.001));
		
		/*
		 * NOW WITH only 1 ATTRIBUTE
		 */
		

		sd1.setAttributes("-1.0");
		sd2.setAttributes("-2.0");
		sd3.setAttributes("-3.0");
		sd4.setAttributes("5.0");
		sd5.setAttributes("6.0");
		
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"11.0,20.0");
		

		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		res = actual.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		
		
		Assert.assertEquals(true,Util.almostEqual(3.741657,res[2],0.001));
				
		
		
		
		sd1.setAttributes("-10.53");
		sd2.setAttributes("-22.0");
		sd3.setAttributes("-0.3");
		sd4.setAttributes("5.2");
		sd5.setAttributes("6.35");
		
		
		ds.clear();
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);	

		//output = new SpatialDataset(1);
	
		target = new SpatialData(7,new Point2D.Double(10,-15),"11.123,-0.45");
		
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(1, output.size());
		
		actual = output.getSpatialData(0);
		
		res = actual.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		
		
		Assert.assertEquals(true,Util.almostEqual(10.6936,res[2],0.001));		
		
	}
		

	

}
