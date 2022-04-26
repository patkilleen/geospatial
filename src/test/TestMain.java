package test;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import aggregation.DataFusion;
import aggregation.SingleSummaryStatAggregator;
import aggregation.SpatialDataAggregator;
import core.Main;
import core.Util;
import data_structure.SpatialData;
import data_structure.SpatialDataset;
import io.FileHandler;
import junit.framework.Assert;

class TestMain {

	File lowResInputFile;
	File highResInputFile;
	File outFile;
	
	/*
	 * mean
	 */
	@Test
	void test_2_5_mean_euclidean() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "MEAN",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_mean_2_5m_Euclidean(res);
	}
	
	@Test
	void test_2_5_mean_infinity_norm() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "MEAN",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_mean_2_5m_Euclidean(res);
	}


	@Test
	void test_2_mean_euclidean() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "MEAN",
									"-r", "2",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_mean_2m_Euclidean(res);
	}
	
	@Test
	void test_2_mean_infinity_norm() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "MEAN",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_mean_2_5m_Euclidean(res);
	}
	
	/*
	 * min
	 */
	@Test
	void test_2_5_min_euclidean() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "MIN",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_min_2_5m_Euclidean(res);
	}
	
	@Test
	void test_2_5_min_infinity_norm() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "MIN",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_min_2_5m_Euclidean(res);
	}


	@Test
	void test_2_min_euclidean() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "MIN",
									"-r", "2",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_min_2m_Euclidean(res);
	}
	
	@Test
	void test_2_min_infinity_norm() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "MIN",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_min_2_5m_Euclidean(res);
	}
	
	
	/*
	 * max
	 */
	
	@Test
	void test_2_5_max_euclidean() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "MAX",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_max_2_5m_Euclidean(res);
	}
	
	@Test
	void test_2_5_max_infinity_norm() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "MAX",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_max_2_5m_Euclidean(res);
	}


	@Test
	void test_2_max_euclidean() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "MAX",
									"-r", "2",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_max_2m_Euclidean(res);
	}
	
	@Test
	void test_2_max_infinity_norm() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "MAX",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_max_2_5m_Euclidean(res);
	}
	
	
	/*
	 * median
	 */
	
	@Test
	void test_2_5_median_euclidean() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "MEDIAN",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_median_2_5m_Euclidean(res);
	}
	
	@Test
	void test_2_5_median_infinity_norm() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "MEDIAN",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_median_2_5m_Euclidean(res);
	}


	@Test
	void test_2_median_euclidean() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "MEDIAN",
									"-r", "2",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_median_2m_Euclidean(res);
	}
	
	@Test
	void test_2_median_infinity_norm() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "MEDIAN",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_median_2_5m_Euclidean(res);
	}
	
	
	/*
	 * standard_deviation
	 */
	
	@Test
	void test_2_5_standard_deviation_euclidean() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "STANDARD_DEVIATION",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_std_dev_2_5m_Euclidean(res);
	}
	
	@Test
	void test_2_5_standard_deviation_infinity_norm() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "STANDARD_DEVIATION",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_std_dev_2_5m_Euclidean(res);
	}


	@Test
	void test_2_standard_deviation_euclidean() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "STANDARD_DEVIATION",
									"-r", "2",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_std_dev_2m_Euclidean(res);
	}
	
	@Test
	void test_2_standard_deviation_infinity_norm() throws IOException {
		File _lowResInputFile = getLowResInputFile();
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "STANDARD_DEVIATION",
									"-r", "2.5",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		TestDataFusion._testFuseDatasets_std_dev_2_5m_Euclidean(res);
	}
	
	@Test
	void testApplyAggregation() throws IOException {
		
		
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1.0,2.0");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"-5.0,4.0");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),"Grain,NE");
		
		
	
		
		SpatialDataset _highResDS = new SpatialDataset(3);
		
		_highResDS.addSpatialData(sd1);
		_highResDS.addSpatialData(sd2);		
		_highResDS.addSpatialData(sd3);
			
		
		
		File _highResInputFile = File.createTempFile("java-geo", ".csv");
		_highResInputFile.deleteOnExit();
		FileHandler.writeSpatialDatasetToFile(_highResInputFile.getAbsolutePath(),"x,y,att1,att2", _highResDS,true,SpatialData.COMMA,false);
		
		
		SpatialDataset _lowResDS = new SpatialDataset(1);
		SpatialData target = new SpatialData(7,new Point2D.Double(100,150),"10.0,20.0");
		_lowResDS.addSpatialData(target);
		

		File _lowResInputFile = File.createTempFile("java-geo", ".csv");
		_lowResInputFile.deleteOnExit();
		FileHandler.writeSpatialDatasetToFile(_lowResInputFile.getAbsolutePath(),"x,y,att1,att2", _lowResDS,true,SpatialData.COMMA,false);
		
		
		
		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "INFINITY_NORM",
									"-agg", "BASIC",
									"-r", "1000",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true"};
		
		SpatialDataset output = testRunMain(cmdLineArgs);
		
	
				
		
		SpatialData []expectedPts={
				new SpatialData(7,new Point2D.Double(100,150),"10.0,20.0,0,-10.0,5.0,1.0,2.0"),
				new SpatialData(7,new Point2D.Double(100,150),"10.0,20.0,1,-5.0,-1.0,-5.0,4.0"),
				new SpatialData(7,new Point2D.Double(100,150),"10.0,20.0,2,0.0,15.0,Grain,NE"),
				
				};
			
		
		Assert.assertEquals(3, output.size());
		
		int expectedContainedInRes = 0;
		
		for(SpatialData expected : expectedPts) {
			for(int i = 0;i< output.size();i++) {
				//SpatialData expected = expectedPts[i];
				SpatialData actual = output.getSpatialData(i);
				
				if( Math.abs(expected.getLocation().getX() -actual.getLocation().getX())< 0.0001) {
					if( Math.abs(expected.getLocation().getY() -actual.getLocation().getY())< 0.0001) {
						if(expected.getAttributes().equals(actual.getAttributes())) {
							expectedContainedInRes++;
						}
					}
				}
				
				
				
			}
		}
		
		Assert.assertEquals(3, expectedContainedInRes);
		
	}
	
	@Test
	void testFuseDatasets_data_defined_search_radius() throws IOException {
		

		SpatialDataset _lowResDS = TestDataFusion.buildLowResTestDataset();
		

		Iterator<SpatialData> it = _lowResDS.iterator();
		
		while(it.hasNext()) {
			SpatialData pt = it.next();									
			pt.setAttributes("2.5");			
			
		}
		
		File _lowResInputFile = File.createTempFile("java-geo", ".csv");
		_lowResInputFile.deleteOnExit();
			FileHandler.writeSpatialDatasetToFile(_lowResInputFile.getAbsolutePath(),"x,y,radius", _lowResDS,true,SpatialData.COMMA,false);
		
		
		//
		File _highResInputFile = getHighResInputFile();		
		File _outFile = getOutFile();

		
		String [] cmdLineArgs = {	"-inH",_highResInputFile.getAbsolutePath(),
									"-inL", _lowResInputFile.getAbsolutePath(),
									"-d", "EUCLIDEAN",
									"-agg", "MEAN",
									"-r", "50",
									"-o", _outFile.getAbsolutePath(),
									"-t", "4" ,
									"-rname","false",
									"-of", "true",
									"-rO", "radius"};
		
		SpatialDataset res = testRunMain(cmdLineArgs);
		removeAttributeX(res,0);//remove the radius attribute
		TestDataFusion._testFuseDatasets_mean_2_5m_Euclidean(res);
		
		String []cmdLineArgs2 = {	"-inH",_highResInputFile.getAbsolutePath(),
				"-inL", _lowResInputFile.getAbsolutePath(),
				"-d", "INFINITY_NORM",
				"-agg", "MEAN",
				"-r", "50",
				"-o", _outFile.getAbsolutePath(),
				"-t", "4" ,
				"-rname","false",
				"-of", "true",
				"-rO", "radius"};

		res = testRunMain(cmdLineArgs2);
		removeAttributeX(res,0);//remove the radius attribute
		TestDataFusion._testFuseDatasets_mean_2_5m_Euclidean(res);

		it = _lowResDS.iterator();
		
		
		while(it.hasNext()) {
			SpatialData pt = it.next();									
			pt.setAttributes("2");			
			
		}
		
		FileHandler.writeSpatialDatasetToFile(_lowResInputFile.getAbsolutePath(),"x,y,radius", _lowResDS,true,SpatialData.COMMA,false);
		
		String []cmdLineArgs3 = {	"-inH",_highResInputFile.getAbsolutePath(),
				"-inL", _lowResInputFile.getAbsolutePath(),
				"-d", "EUCLIDEAN",
				"-agg", "MEAN",
				"-r", "50",
				"-o", _outFile.getAbsolutePath(),
				"-t", "4" ,
				"-rname","false",
				"-of", "true",
				"-rO", "radius"};

		res = testRunMain(cmdLineArgs3);
		removeAttributeX(res,0);//remove the radius attribute
		TestDataFusion._testFuseDatasets_mean_2m_Euclidean(res);
		
		
		String []cmdLineArgs4 = {	"-inH",_highResInputFile.getAbsolutePath(),
				"-inL", _lowResInputFile.getAbsolutePath(),
				"-d", "INFINITY_NORM",
				"-agg", "MEAN",
				"-r", "50",
				"-o", _outFile.getAbsolutePath(),
				"-t", "4" ,
				"-rname","false",
				"-of", "true",
				"-rO", "radius"};

		res = testRunMain(cmdLineArgs4);
		removeAttributeX(res,0);//remove the radius attribute
		TestDataFusion._testFuseDatasets_mean_2_5m_Euclidean(res);


		
		it = _lowResDS.iterator();
		
		while(it.hasNext()) {
			SpatialData pt = it.next();
			if(pt.getId() == 1) {				
			
				pt.setAttributes("1000");
			
			}else {
				
				pt.setAttributes("2");
				
			}
			
		}
		
		FileHandler.writeSpatialDatasetToFile(_lowResInputFile.getAbsolutePath(),"x,y,radius", _lowResDS,true,SpatialData.COMMA,false);
		
		String []cmdLineArgs5 = {	"-inH",_highResInputFile.getAbsolutePath(),
				"-inL", _lowResInputFile.getAbsolutePath(),
				"-d", "EUCLIDEAN",
				"-agg", "MEAN",
				"-r", "50",
				"-o", _outFile.getAbsolutePath(),
				"-t", "4" ,
				"-rname","false",
				"-of", "true",
				"-rO", "radius"};

		res = testRunMain(cmdLineArgs5);
	
		removeAttributeX(res,0);//remove the radius attribute
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
		
		
		String []cmdLineArgs6 = {	"-inH",_highResInputFile.getAbsolutePath(),
				"-inL", _lowResInputFile.getAbsolutePath(),
				"-d", "INFINITY_NORM",
				"-agg", "MEAN",
				"-r", "50",
				"-o", _outFile.getAbsolutePath(),
				"-t", "4" ,
				"-rname","false",
				"-of", "true",
				"-rO", "radius"};

		res = testRunMain(cmdLineArgs6);
		removeAttributeX(res,0);//remove the radius attribute
						
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



	public SpatialDataset testRunMain(String[] args) throws IOException {

		File _outFile = getOutFile();
		Main.main(args);
		
		SpatialDataset res = FileHandler.readCSVIntoSpatialDataset(_outFile.getAbsolutePath(),null,SpatialData.COMMA);
		
		//make sure id's start from 1
		for(int i = 0;i<res.size();i++) {
			SpatialData pt = res.getSpatialData(i);
			
			pt.setId(i+1);
		}
		
		return res;
	}
	public File getLowResInputFile() throws IOException {
		
		SpatialDataset lowResDS = TestDataFusion.buildLowResTestDataset();
		
		if(lowResInputFile == null) {
			lowResInputFile = File.createTempFile("java-geo", ".csv");
			lowResInputFile.deleteOnExit();
			FileHandler.writeSpatialDatasetToFile(lowResInputFile.getAbsolutePath(),"x,y", lowResDS,true,SpatialData.COMMA,false);
		}
		
		return lowResInputFile;
	}
	
	public File getHighResInputFile() throws IOException {
		
		SpatialDataset highResDS = TestDataFusion.buildHighResTestDataset();
		
		if(highResInputFile == null) {
			highResInputFile = File.createTempFile("java-geo", ".csv");
			highResInputFile.deleteOnExit();
			FileHandler.writeSpatialDatasetToFile(highResInputFile.getAbsolutePath(),"x,y,yield,speed", highResDS,true,SpatialData.COMMA,false);
		}
		
		return highResInputFile;
	}
	
	public File getOutFile() throws IOException {
		if(outFile == null) {
			outFile = File.createTempFile("java-geo", ".csv");
			outFile.deleteOnExit();
		}
		return outFile;
	}
	
	public static void removeAttributeX(SpatialDataset ds,int attributeIx) {
		Iterator<SpatialData> it = ds.iterator();
		while(it.hasNext()) {
			SpatialData pt = it.next();
			
			
			String attribute =pt.getAttributes();
			String [] tokens = attribute.split(",");
			String newatt = "";
			
			for(int i = 0;i<tokens.length;i++) {
				if(i!=attributeIx) {
					
					if(!newatt.equals("")){
						newatt = newatt + ",";
					}
					newatt = newatt +tokens[i];
				}
			}
			 
			pt.setAttributes(newatt);
		}
	}

}
