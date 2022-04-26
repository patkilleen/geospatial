package test;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import aggregation.YieldProcessor;
import data_structure.Polygon2D;
import data_structure.SpatialData;
import data_structure.SpatialDataset;
import io.FileHandler;
import junit.framework.Assert;

class TestYieldProcessor {

	SpatialDataset createInputDatasaet() {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15"));
		ds.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17"));
		ds.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18"));		
		ds.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19"));
		ds.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20"));
		
		ds.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26"));
		ds.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27"));
		ds.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29"));
		ds.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30"));
		ds.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32"));		
		ds.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33"));
		ds.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34"));
		
		ds.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38"));		
		ds.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39"));
		ds.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40"));
		
		ds.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44"));		
		ds.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45"));
		ds.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46"));
		return ds;
	}
	@Test
	void test_toHarvestPassSet() {
		
		SpatialDataset ds=createInputDatasaet();
		int timestampIx =0;
		List<SpatialDataset> segments = YieldProcessor.toHarvestPassSet(ds, timestampIx,",");
		
		
		
		List<SpatialDataset> expected = new ArrayList<SpatialDataset>(4);
		
		SpatialDataset tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
		tmp.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14"));
		tmp.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15"));
		tmp.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17"));
		tmp.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18"));		
		tmp.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19"));
		tmp.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26"));
		tmp.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27"));
		tmp.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29"));
		tmp.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30"));
		tmp.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32"));		
		tmp.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33"));
		tmp.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38"));		
		tmp.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39"));
		tmp.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		
		tmp.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44"));		
		tmp.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45"));
		tmp.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46"));
		expected.add(tmp);
		
		Assert.assertEquals(expected.size(),segments.size());
		
		for(int i= 0;i<expected.size();i++) {
			
			SpatialDataset expectedDs = expected.get(i);
			SpatialDataset actualDs = segments.get(i);
			Assert.assertEquals(expectedDs.size(),actualDs.size());
			
			for(int j  =0;j<expectedDs.size();j++) {
				SpatialData expectedPt = expectedDs.getSpatialData(j);
				SpatialData actualPt = actualDs.getSpatialData(j);
				
				Assert.assertTrue(expectedPt.toString().equals(actualPt.toString()));
				
			}
		}
		
	}
	
	@Test
	void test_toHarvestPassDataset() throws IOException {
		
		SpatialDataset ds=createInputDatasaet();
		int timestampIx =0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		
		
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15,0"));
		expected.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17,0"));
		expected.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18,0"));		
		expected.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19,0"));
		expected.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20,0"));
		
		
		
		expected.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26,1"));
		expected.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27,1"));
		expected.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29,1"));
		expected.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30,1"));
		expected.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32,1"));		
		expected.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33,1"));
		expected.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34,1"));
		
		
		
		expected.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38,2"));		
		expected.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39,2"));
		expected.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40,2"));
		
		
		
		
		expected.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44,3"));		
		expected.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45,3"));
		expected.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46,3"));
		
		
		testTwoDataSetsEqual(expected,actual);
		
	}
	
	@Test
	void test_toHarvestPassDataset_1pass() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:20"));
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:20,0"));
		int timestampIx = 0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}


	@Test
	void test_toHarvestPassDataset_1_reading() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
		
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13,0"));
		
		int timestampIx = 0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}
	
	@Test
	void test_toHarvestPassDataset_2_readings() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14"));
		
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14,0"));
		
		int timestampIx = 0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}
	
	
	@Test
	void test_toHarvestPassDataset_2_readings_big_dif() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:25"));
		
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:25,0"));
		
		int timestampIx = 0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}

	@Test
	void test_toHarvestPassDataset_3_readings_big_dif() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:25"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:47:55"));
		
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:25,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:47:55,0"));
		
		int timestampIx = 0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}

	

	@Test
	void test_toHarvestPassDataset_3_readings() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:25"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:26"));
		
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:25,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:26,0"));
		
		int timestampIx = 0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}


	@Test
	void test_toHarvestPassDataset_3_readings_big_diff_2() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:55"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:56"));
		
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:55,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:56,0"));
		
		int timestampIx = 0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}

	@Test
	void test_toHarvestPassDataset_4_readings_big_diff() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:55"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:56"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:57"));
		
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:55,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:56,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:57,0"));
		
		int timestampIx = 0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}

	@Test
	void test_toHarvestPassDataset_4_readings_big_diff2() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:25"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:56"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:47:12"));
		
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:25,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:56,1"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:47:12,1"));
		
		int timestampIx = 0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}
	

	@Test
	void test_toHarvestPassDataset_4_readings_big_diff3() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:25"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:26"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:47:12"));
		
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:24,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:25,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:26,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:47:12,0"));
		
		int timestampIx = 0;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}






	
	@Test
	void test_toHarvestPassDataset_multiple_attributes() throws IOException {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"hey,2021-11-06T23:46:13,190"));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"hi,2021-11-06T23:46:14,132"));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"hello,2021-11-06T23:46:20,59"));
		
		SpatialDataset expected = new SpatialDataset(2);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"hey,2021-11-06T23:46:13,190,0"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"hi,2021-11-06T23:46:14,132,0"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"hello,2021-11-06T23:46:20,59,0"));
		int timestampIx = 1;
		SpatialDataset actual = YieldProcessor.toHarvestPassDataset(ds, timestampIx,",");
		
		testTwoDataSetsEqual(expected,actual);
		
	}
	
	

	@Test
	void test_cleanHarvestDatasetSegments() throws IOException {
		//public static List<SpatialDataset> cleanHarvestDatasetSegments(SpatialDataset inputDataset, int timestampIx,String sep,int numFillSamplesError, int numFinishSamplesError,int shortSegmentSize) throws IOException{
		SpatialDataset ds=createInputDatasaet();

		int timestampIx = 0;
		
		List<SpatialDataset> segments = YieldProcessor.toHarvestPassSet(ds, timestampIx,",");
		
		List<SpatialDataset> actual = YieldProcessor.cleanHarvestDatasetSegments(segments, timestampIx,",",2,1,4);
		
		//testTwoDataSetsEqual(expected,actual);
		
		//starterror, finish error, short seg

		
		List<SpatialDataset> expected = new ArrayList<SpatialDataset>(4);
		
		SpatialDataset tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13,1,0,0"));
		tmp.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14,1,0,0"));
		tmp.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15,0,0,0"));
		tmp.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17,0,0,0"));
		tmp.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18,0,0,0"));
		tmp.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19,0,0,0"));
		tmp.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20,0,1,0"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26,1,0,0"));
		tmp.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27,1,0,0"));
		tmp.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29,0,0,0"));
		tmp.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30,0,0,0"));
		tmp.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32,0,0,0"));		
		tmp.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33,0,0,0"));
		tmp.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34,0,1,0"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38,1,0,1"));		
		tmp.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39,1,0,1"));
		tmp.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40,0,1,1"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		
		tmp.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44,1,0,1"));		
		tmp.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45,1,0,1"));
		tmp.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46,0,1,1"));
		expected.add(tmp);
		
		
		Assert.assertEquals(expected.size(),actual.size());
		
		for(int i= 0;i<expected.size();i++) {
			
			SpatialDataset expectedDs = expected.get(i);
			SpatialDataset actualDs = actual.get(i);
			testTwoDataSetsEqual(expectedDs,actualDs);
		}
		
		
	}
	
	

	@Test
	void test_cleanHarvestDatasetSegments2() throws IOException {
		//public static List<SpatialDataset> cleanHarvestDatasetSegments(SpatialDataset inputDataset, int timestampIx,String sep,int numFillSamplesError, int numFinishSamplesError,int shortSegmentSize) throws IOException{
		SpatialDataset ds=createInputDatasaet();

		int timestampIx = 0;
		List<SpatialDataset> segments = YieldProcessor.toHarvestPassSet(ds, timestampIx,",");
		List<SpatialDataset> actual = YieldProcessor.cleanHarvestDatasetSegments(segments, timestampIx,",",1,1,2);
		
		//testTwoDataSetsEqual(expected,actual);
		

		
		List<SpatialDataset> expected = new ArrayList<SpatialDataset>(4);
		
		SpatialDataset tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13,1,0,0"));
		tmp.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14,0,0,0"));
		tmp.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15,0,0,0"));
		tmp.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17,0,0,0"));
		tmp.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18,0,0,0"));
		tmp.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19,0,0,0"));
		tmp.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20,0,1,0"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26,1,0,0"));
		tmp.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27,0,0,0"));
		tmp.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29,0,0,0"));
		tmp.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30,0,0,0"));
		tmp.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32,0,0,0"));		
		tmp.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33,0,0,0"));
		tmp.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34,0,1,0"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38,1,0,0"));		
		tmp.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39,0,0,0"));
		tmp.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40,0,1,0"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		
		tmp.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44,1,0,0"));		
		tmp.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45,0,0,0"));
		tmp.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46,0,1,0"));
		expected.add(tmp);
		
		
		
		Assert.assertEquals(expected.size(),actual.size());
		
		for(int i= 0;i<expected.size();i++) {
			
			SpatialDataset expectedDs = expected.get(i);
			SpatialDataset actualDs = actual.get(i);
			testTwoDataSetsEqual(expectedDs,actualDs);
		}
		
		
	}
	
	
	@Test
	void test_cleanHarvestDatasetSegments3() throws IOException {
		//public static List<SpatialDataset> cleanHarvestDatasetSegments(SpatialDataset inputDataset, int timestampIx,String sep,int numFillSamplesError, int numFinishSamplesError,int shortSegmentSize) throws IOException{
		SpatialDataset ds=createInputDatasaet();

		int timestampIx = 0;
		List<SpatialDataset> segments = YieldProcessor.toHarvestPassSet(ds, timestampIx,",");
		List<SpatialDataset> actual = YieldProcessor.cleanHarvestDatasetSegments(segments, timestampIx,",",1,2,3);
		
		//testTwoDataSetsEqual(expected,actual);
		

		
		List<SpatialDataset> expected = new ArrayList<SpatialDataset>(4);
		
		SpatialDataset tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13,1,0,0"));
		tmp.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14,0,0,0"));
		tmp.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15,0,0,0"));
		tmp.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17,0,0,0"));
		tmp.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18,0,0,0"));
		tmp.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19,0,1,0"));
		tmp.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20,0,1,0"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26,1,0,0"));
		tmp.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27,0,0,0"));
		tmp.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29,0,0,0"));
		tmp.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30,0,0,0"));
		tmp.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32,0,0,0"));		
		tmp.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33,0,1,0"));
		tmp.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34,0,1,0"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38,1,0,0"));		
		tmp.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39,0,1,0"));
		tmp.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40,0,1,0"));
		expected.add(tmp);
		
		tmp = new SpatialDataset(2);
		
		tmp.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44,1,0,0"));		
		tmp.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45,0,1,0"));
		tmp.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46,0,1,0"));
		expected.add(tmp);
		

		
		
		Assert.assertEquals(expected.size(),actual.size());
		
		for(int i= 0;i<expected.size();i++) {
			
			SpatialDataset expectedDs = expected.get(i);
			SpatialDataset actualDs = actual.get(i);
			testTwoDataSetsEqual(expectedDs,actualDs);
		}
		
		
	}
	
	@Test
	void test_identifyValueExceedingThreshold1() throws IOException {
		
		SpatialDataset tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"250"));
		tmp.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"190"));
		tmp.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"222"));
		tmp.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"75"));
		tmp.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"130"));
		tmp.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"320"));
		tmp.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"210"));
		
	
		SpatialDataset actual =  YieldProcessor.identifyExcedingBounds(tmp, 0,",", 300,true);
		
		SpatialDataset exptected = new SpatialDataset(2);
		exptected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"250,0"));
		exptected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"190,0"));
		exptected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"222,0"));
		exptected.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"75,0"));
		exptected.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"130,0"));
		exptected.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"320,1"));
		exptected.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"210,0"));
		
		testTwoDataSetsEqual(exptected,actual);
	
	}
	
	
	@Test
	void test_identifyValueExceedingThreshold2() throws IOException {
		
		SpatialDataset tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"0,250"));
		tmp.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"1,190"));
		tmp.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2,222"));
		tmp.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"0,75"));
		tmp.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2,130"));
		tmp.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"3,320"));
		tmp.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"5,210"));
		
	
		SpatialDataset actual =  YieldProcessor.identifyExcedingBounds(tmp, 1,",", 300,true);
		
		SpatialDataset exptected = new SpatialDataset(2);
		exptected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"0,250,0"));
		exptected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"1,190,0"));
		exptected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2,222,0"));
		exptected.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"0,75,0"));
		exptected.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2,130,0"));
		exptected.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"3,320,1"));
		exptected.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"5,210,0"));
		
		testTwoDataSetsEqual(exptected,actual);
	
	}
	
	
	@Test
	void test_identifyValueExceedingThreshold3() throws IOException {
		
		SpatialDataset tmp = new SpatialDataset(2);
		tmp.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"0,250"));
		tmp.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"1,190"));
		tmp.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2,222"));
		tmp.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"0,75"));
		tmp.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2,130"));
		tmp.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"3,320"));
		tmp.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"5,210"));
		
	
		SpatialDataset actual =  YieldProcessor.identifyExcedingBounds(tmp, 1,",", 140,false);
		
		SpatialDataset exptected = new SpatialDataset(2);
		exptected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"0,250,0"));
		exptected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"1,190,0"));
		exptected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2,222,0"));
		exptected.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"0,75,1"));
		exptected.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2,130,1"));
		exptected.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"3,320,0"));
		exptected.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"5,210,0"));
		
		testTwoDataSetsEqual(exptected,actual);
	
	}
	
	@Test
	void  test_identifyCo_LocatedPoints() {
		
		SpatialDataset inputData = new SpatialDataset(5);
		inputData.addSpatialData(new SpatialData(1,new Point2D.Double(1,1),""));
		inputData.addSpatialData(new SpatialData(2,new Point2D.Double(1,2),""));
		inputData.addSpatialData(new SpatialData(3,new Point2D.Double(2,1),""));
		inputData.addSpatialData(new SpatialData(4,new Point2D.Double(2,2),""));
		inputData.addSpatialData(new SpatialData(5,new Point2D.Double(2,1),""));
		
	
		
		SpatialDataset actual = YieldProcessor.identifyCo_LocatedPoints( inputData,"");
		
		SpatialDataset exptected = new SpatialDataset(5);
		exptected.addSpatialData(new SpatialData(1,new Point2D.Double(1,1),"0"));
		exptected.addSpatialData(new SpatialData(2,new Point2D.Double(1,2),"0"));
		exptected.addSpatialData(new SpatialData(3,new Point2D.Double(2,1),"1"));
		exptected.addSpatialData(new SpatialData(4,new Point2D.Double(2,2),"0"));
		exptected.addSpatialData(new SpatialData(5,new Point2D.Double(2,1),"1"));
		testTwoDataSetsEqual(exptected,actual);
		
	}
	@Test
	void test__findTimeBasedNearbySamples()  {
		
		SpatialDataset ds=createInputDatasaet();
		SpatialDataset outputBuffer = new SpatialDataset(16);
		boolean forwardFlag =true;
		YieldProcessor._findTimeBasedNearbySamples(0 , ds,0,",",3,outputBuffer,forwardFlag);
		
		
		SpatialDataset expected = new SpatialDataset(2);
		//expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14"));
		expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15"));
		//expected.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17"));
		
		
		/*expected.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18"));		
		expected.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19"));
		expected.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20"));
		
		expected.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26"));
		expected.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27"));
		expected.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29"));
		expected.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30"));
		expected.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32"));		
		expected.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33"));
		expected.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34"));
		
		expected.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38"));		
		expected.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39"));
		expected.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40"));
		
		expected.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44"));		
		expected.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45"));
		expected.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46"));*/
		
		testTwoDataSetsEqual(expected,outputBuffer);
		
		 ds=createInputDatasaet();
		//	 outputBuffer = new SpatialDataset(16);
			forwardFlag =true;
			YieldProcessor._findTimeBasedNearbySamples(0 , ds,0,",",5,outputBuffer,forwardFlag);
			
				

			expected = new SpatialDataset(2);
			//expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
			expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14"));
			expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15"));
			expected.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17"));
			
			
			expected.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18"));		
			/*expected.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19"));
			expected.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20"));
			
			expected.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26"));
			expected.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27"));
			expected.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29"));
			expected.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30"));
			expected.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32"));		
			expected.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33"));
			expected.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34"));
			
			expected.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38"));		
			expected.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39"));
			expected.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40"));
			
			expected.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44"));		
			expected.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45"));
			expected.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46"));*/
			
			testTwoDataSetsEqual(expected,outputBuffer);
			
			
			// outputBuffer = new SpatialDataset(16);
				forwardFlag =true;
				YieldProcessor._findTimeBasedNearbySamples(4 , ds,0,",",5,outputBuffer,forwardFlag);
				
					

				expected = new SpatialDataset(2);
				//expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
				//expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14"));
				//expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15"));
				//expected.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17"));
				
				
				//expected.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18"));		
				expected.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19"));
				expected.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20"));
				
				/*expected.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26"));
				expected.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27"));
				expected.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29"));
				expected.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30"));
				expected.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32"));		
				expected.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33"));
				expected.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34"));
				
				expected.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38"));		
				expected.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39"));
				expected.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40"));
				
				expected.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44"));		
				expected.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45"));
				expected.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46"));*/
				testTwoDataSetsEqual(expected,outputBuffer);
				
				forwardFlag =false;
				YieldProcessor._findTimeBasedNearbySamples(11 , ds,0,",",5,outputBuffer,forwardFlag);
				
					

				expected = new SpatialDataset(2);
				//expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
				//expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14"));
				//expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15"));
				//expected.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17"));
				
				
				//expected.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18"));		
				//expected.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19"));
				//expected.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20"));
				
				//expected.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26"));
				
				expected.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30"));
				expected.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29"));
				expected.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27"));
								
				/*expected.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32"));-		
				expected.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33"));
				expected.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34"));
				
				expected.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38"));		
				expected.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39"));
				expected.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40"));
				
				expected.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44"));		
				expected.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45"));
				expected.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46"));*/
				testTwoDataSetsEqual(expected,outputBuffer);
				
				
				forwardFlag =false;
				YieldProcessor._findTimeBasedNearbySamples(19 , ds,0,",",5,outputBuffer,forwardFlag);
				
					

				expected = new SpatialDataset(2);
				//expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"2021-11-06T23:46:13"));
				//expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0),"2021-11-06T23:46:14"));
				//expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,0),"2021-11-06T23:46:15"));
				//expected.addSpatialData(new SpatialData(4,new Point2D.Double(0,0),"2021-11-06T23:46:17"));
				
				
				//expected.addSpatialData(new SpatialData(5,new Point2D.Double(0,0),"2021-11-06T23:46:18"));		
				//expected.addSpatialData(new SpatialData(6,new Point2D.Double(0,0),"2021-11-06T23:46:19"));
				//expected.addSpatialData(new SpatialData(7,new Point2D.Double(0,0),"2021-11-06T23:46:20"));
				
				//expected.addSpatialData(new SpatialData(8,new Point2D.Double(0,0),"2021-11-06T23:46:26"));
				
				/*expected.addSpatialData(new SpatialData(11,new Point2D.Double(0,0),"2021-11-06T23:46:30"));
				expected.addSpatialData(new SpatialData(10,new Point2D.Double(0,0),"2021-11-06T23:46:29"));
				expected.addSpatialData(new SpatialData(9,new Point2D.Double(0,0),"2021-11-06T23:46:27"));
								
				expected.addSpatialData(new SpatialData(12,new Point2D.Double(0,0),"2021-11-06T23:46:32"));-		
				expected.addSpatialData(new SpatialData(13,new Point2D.Double(0,0),"2021-11-06T23:46:33"));
				expected.addSpatialData(new SpatialData(14,new Point2D.Double(0,0),"2021-11-06T23:46:34"));
				
				expected.addSpatialData(new SpatialData(15,new Point2D.Double(0,0),"2021-11-06T23:46:38"));		
				expected.addSpatialData(new SpatialData(16,new Point2D.Double(0,0),"2021-11-06T23:46:39"));
				expected.addSpatialData(new SpatialData(17,new Point2D.Double(0,0),"2021-11-06T23:46:40"));*/
				
				expected.addSpatialData(new SpatialData(19,new Point2D.Double(0,0),"2021-11-06T23:46:45"));
				expected.addSpatialData(new SpatialData(18,new Point2D.Double(0,0),"2021-11-06T23:46:44"));		
				
				//expected.addSpatialData(new SpatialData(20,new Point2D.Double(0,0),"2021-11-06T23:46:46"));-
				testTwoDataSetsEqual(expected,outputBuffer);
			
				
				forwardFlag =true;
				YieldProcessor._findTimeBasedNearbySamples(19 , ds,0,",",5,outputBuffer,forwardFlag);
				
					

				expected = new SpatialDataset(2);
				testTwoDataSetsEqual(expected,outputBuffer);
				
				forwardFlag =false;
				YieldProcessor._findTimeBasedNearbySamples(0 , ds,0,",",5,outputBuffer,forwardFlag);
				
					

				expected = new SpatialDataset(2);
				testTwoDataSetsEqual(expected,outputBuffer);
				
			
	
	}
	
	@Test
	void test_harvestPointPairToHarvestArea() {
		
		SpatialData from = new SpatialData(0,new Point2D.Double(0,0),"20");
		SpatialData to = new SpatialData(1,new Point2D.Double(0,5),"10");
		boolean swathWidthInFeet=false;
		Polygon2D harvestArea = YieldProcessor.harvestPointPairToHarvestArea(from, to,",",0,1,swathWidthInFeet);
		
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(0,1)));
		
		
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(-10,0)));
		Assert.assertEquals(false,harvestArea.contains(new Point2D.Double(10,0)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(9.99,0)));
		Assert.assertEquals(false,harvestArea.contains(new Point2D.Double(5,5)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(5,4.99)));
		Assert.assertEquals(false,harvestArea.contains(new Point2D.Double(0,5)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(0,4.99)));
		Assert.assertEquals(false,harvestArea.contains(new Point2D.Double(10,5)));
		Assert.assertEquals(false,harvestArea.contains(new Point2D.Double(9.99,4.99)));
		Assert.assertEquals(false,harvestArea.contains(new Point2D.Double(5,5)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(4.99,4.99)));
		Assert.assertEquals(false,harvestArea.contains(new Point2D.Double(0,5)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(0,4.99)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(5,0)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(0,0)));

		//https://www.mathsisfun.com/geometry/polygons-interactive.html
		
		from = new SpatialData(0,new Point2D.Double(2.5,2.5),""+Math.sqrt(50));
		to = new SpatialData(1,new Point2D.Double(8.5,8.5),""+Math.sqrt(18));
		
		harvestArea = YieldProcessor.harvestPointPairToHarvestArea(from, to,",",0,1,swathWidthInFeet);
		
		//inside
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(5,5)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(7,5)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(5,7)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(8,7)));
		
		
		//on border
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(0,5)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(5,0.5)));
		Assert.assertEquals(false,harvestArea.contains(new Point2D.Double(7,10)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(7,9.99)));
		Assert.assertEquals(false,harvestArea.contains(new Point2D.Double(10,7)));
		Assert.assertEquals(true,harvestArea.contains(new Point2D.Double(9.99,7)));
		
		
		
		
		
	}
	
	@Test
	void test_removeGPSPositionErrors() {	
	int timestampIx=0;			
	int speedIx=1;
	double maxGPSError=2;	
	String sep = ",";
	boolean mphFlag = true;

	
	SpatialDataset inputDataset = new SpatialDataset();
	//the speed may not be representative of position change, but it's just made to determine the max speed allowable (max speed 3)
	//so max acceptable distance is 
	//1 second harvest samplign max distance is  1 sec * 3m/s + 2 * 2 = 3 + 4 = 7
	inputDataset.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),"2021-11-06T23:46:45,1"));
	inputDataset.addSpatialData(new SpatialData(1,new Point2D.Double(0,1),"2021-11-06T23:46:46,2")); 
	inputDataset.addSpatialData(new SpatialData(2,new Point2D.Double(0,0.5),"2021-11-06T23:46:47,1"));
	inputDataset.addSpatialData(new SpatialData(3,new Point2D.Double(0,2),"2021-11-06T23:46:48,3"));
	inputDataset.addSpatialData(new SpatialData(4,new Point2D.Double(0,3),"2021-11-06T23:46:49,1"));
	inputDataset.addSpatialData(new SpatialData(5,new Point2D.Double(0,4),"2021-11-06T23:46:50,2"));
	//remove gps outliers first. this is important for computeing the area harvested
	SpatialDataset actual=YieldProcessor.removeGPSPositionErrors( inputDataset, timestampIx,  speedIx,  sep, maxGPSError,mphFlag);
	
	SpatialDataset expected = new SpatialDataset();
	
	expected.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),"2021-11-06T23:46:45,1"));
	expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,1),"2021-11-06T23:46:46,2")); 
	expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0.5),"2021-11-06T23:46:47,1"));
	expected.addSpatialData(new SpatialData(3,new Point2D.Double(0,2),"2021-11-06T23:46:48,3"));
	expected.addSpatialData(new SpatialData(4,new Point2D.Double(0,3),"2021-11-06T23:46:49,1"));
	expected.addSpatialData(new SpatialData(5,new Point2D.Double(0,4),"2021-11-06T23:46:50,2"));
	
	testTwoDataSetsEqual(expected,actual);
	
	
	inputDataset = new SpatialDataset();
	//the speed may not be representative of position change, but it's just made to determine the max speed allowable (max speed 3)
	//so max acceptable distance is 
	//1 second harvest samplign max distance is  1 sec * 3m/s + 2 * 2 = 3 + 4 = 7
	inputDataset.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),"2021-11-06T23:46:45,1"));
	inputDataset.addSpatialData(new SpatialData(1,new Point2D.Double(0,1),"2021-11-06T23:46:46,2")); 
	inputDataset.addSpatialData(new SpatialData(2,new Point2D.Double(0,0.5),"2021-11-06T23:46:47,1"));
	inputDataset.addSpatialData(new SpatialData(3,new Point2D.Double(15,2),"2021-11-06T23:46:48,3"));//bad
	inputDataset.addSpatialData(new SpatialData(4,new Point2D.Double(0,3),"2021-11-06T23:46:49,1"));
	inputDataset.addSpatialData(new SpatialData(5,new Point2D.Double(0,4),"2021-11-06T23:46:50,2"));
	//remove gps outliers first. this is important for computeing the area harvested
	actual=YieldProcessor.removeGPSPositionErrors( inputDataset, timestampIx,  speedIx,  sep, maxGPSError,mphFlag);
	
	expected = new SpatialDataset();
	
	expected.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),"2021-11-06T23:46:45,1"));
	expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,1),"2021-11-06T23:46:46,2")); 
	expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0.5),"2021-11-06T23:46:47,1"));
	//expected.addSpatialData(new SpatialData(3,new Point2D.Double(15,2),"2021-11-06T23:46:48,3"));
	expected.addSpatialData(new SpatialData(4,new Point2D.Double(0,3),"2021-11-06T23:46:49,1"));
	expected.addSpatialData(new SpatialData(5,new Point2D.Double(0,4),"2021-11-06T23:46:50,2"));
	
	testTwoDataSetsEqual(expected,actual);
	
	
	
	inputDataset = new SpatialDataset();
	//the speed may not be representative of position change, but it's just made to determine the max speed allowable (max speed 3)
	//so max acceptable distance is 
	//1 second harvest samplign max distance is  1 sec * 3m/s + 2 * 2 = 3 + 4 = 7
	inputDataset.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),"2021-11-06T23:46:45,1"));
	inputDataset.addSpatialData(new SpatialData(1,new Point2D.Double(0,1),"2021-11-06T23:46:46,2")); 
	inputDataset.addSpatialData(new SpatialData(2,new Point2D.Double(0,0.5),"2021-11-06T23:46:47,1"));
	inputDataset.addSpatialData(new SpatialData(3,new Point2D.Double(8,2),"2021-11-06T23:46:48,3"));//bad
	inputDataset.addSpatialData(new SpatialData(4,new Point2D.Double(0,3),"2021-11-06T23:46:49,1"));
	inputDataset.addSpatialData(new SpatialData(5,new Point2D.Double(0,4),"2021-11-06T23:46:50,2"));
	//remove gps outliers first. this is important for computeing the area harvested
	actual=YieldProcessor.removeGPSPositionErrors( inputDataset, timestampIx,  speedIx,  sep, maxGPSError,mphFlag);
	
	expected = new SpatialDataset();
	
	expected.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),"2021-11-06T23:46:45,1"));
	expected.addSpatialData(new SpatialData(1,new Point2D.Double(0,1),"2021-11-06T23:46:46,2")); 
	expected.addSpatialData(new SpatialData(2,new Point2D.Double(0,0.5),"2021-11-06T23:46:47,1"));
	//expected.addSpatialData(new SpatialData(3,new Point2D.Double(8,2),"2021-11-06T23:46:48,3"));
	expected.addSpatialData(new SpatialData(4,new Point2D.Double(0,3),"2021-11-06T23:46:49,1"));
	expected.addSpatialData(new SpatialData(5,new Point2D.Double(0,4),"2021-11-06T23:46:50,2"));
	
	testTwoDataSetsEqual(expected,actual);
	
	
	
	}
	public void testTwoDataSetsEqual(SpatialDataset expected, SpatialDataset actual) {
		Assert.assertEquals(expected.size(),actual.size());
		
		
		for(int j  =0;j<expected.size();j++) {
			SpatialData expectedPt = expected.getSpatialData(j);
			SpatialData actualPt = actual.getSpatialData(j);
			
			Assert.assertTrue(expectedPt.toString().equals(actualPt.toString()));
			
		}
	
	}
	
	
}
