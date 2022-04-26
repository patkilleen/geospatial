package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import aggregation.Stats;
import data_structure.SpatialData;
import data_structure.SpatialDataset;
import junit.framework.Assert;

class TestStats {

	@Test
	void _test_mode(){
		
		int[] arr = {1,2,3,4,1,2,3,1,2,1};
		
		int actual = Stats.mode(arr, arr.length);
		
		Assert.assertEquals(1, actual);
		
		
		int[] arr2 = {4,3,2,1,4,3,2,4,3,4};
		
		actual = Stats.mode(arr2, arr.length);
		
		Assert.assertEquals(4, actual);
	}
	@Test
	void _testFuseDatasets_mean_all() {
		
		double []res =buildTestSpatialDataSetToDoubleColumn(0);
		




		double actual =Stats.mean(res, res.length);

		Assert.assertEquals(134.4285714,actual,0.001);
		
		res =buildTestSpatialDataSetToDoubleColumn(1);
		

		actual =Stats.mean(res, res.length);
		Assert.assertEquals(4.678571429,actual,0.001);

		
		
		res =buildTestSpatialDataSetToDoubleColumn(0);
		
		int originalLength = res.length;
		
		expandArray(res,20,1000);
		
		 actual =Stats.mean(res, originalLength);

		Assert.assertEquals(134.4285714,actual,0.001);
		
		res =buildTestSpatialDataSetToDoubleColumn(1);
		

		actual =Stats.mean(res, originalLength);
		Assert.assertEquals(4.678571429,actual,0.001);

		
		


		List<Double> list = buildTestSpatialDataSetToDoubleColumnList(0);

		actual =Stats.mean(list);

		Assert.assertEquals(134.4285714,actual,0.001);
		
		list =buildTestSpatialDataSetToDoubleColumnList(1);
		

		actual =Stats.mean(list);
		Assert.assertEquals(4.678571429,actual,0.001);



		
		
			
	}

	@Test
	void _testFuseDatasets_max_all() {

		double []res =buildTestSpatialDataSetToDoubleColumn(0);

		double actual =Stats.max(res, res.length);

		Assert.assertEquals(190,actual,0.001);
		
		res =buildTestSpatialDataSetToDoubleColumn(1);
		

		actual =Stats.max(res, res.length);
		Assert.assertEquals(7.5,actual,0.001);
		


		res =buildTestSpatialDataSetToDoubleColumn(0);
		
		int originalLength = res.length;
		
		expandArray(res,20,1000);

		 actual =Stats.max(res, originalLength);

			Assert.assertEquals(190,actual,0.001);
			
			res =buildTestSpatialDataSetToDoubleColumn(1);
			

			actual =Stats.max(res, originalLength);
			Assert.assertEquals(7.5,actual,0.001);
			

			
			List<Double> list = buildTestSpatialDataSetToDoubleColumnList(0);

			actual =Stats.max(list);

			Assert.assertEquals(190,actual,0.001);
			
			list =buildTestSpatialDataSetToDoubleColumnList(1);
			

			actual =Stats.max(list);
			Assert.assertEquals(7.5,actual,0.001);
			
			
	}

	@Test
	void _testFuseDatasets_min_all() {
	
		
		double []res =buildTestSpatialDataSetToDoubleColumn(0);

		double actual =Stats.min(res, res.length);

		Assert.assertEquals(100,actual,0.001);
		
		res =buildTestSpatialDataSetToDoubleColumn(1);
		

		actual =Stats.min(res, res.length);
		Assert.assertEquals(2,actual,0.001);
		
res =buildTestSpatialDataSetToDoubleColumn(0);
		
		int originalLength = res.length;
		
		expandArray(res,20,-1000);
		
		actual =Stats.min(res, originalLength);

		Assert.assertEquals(100,actual,0.001);
		
		res =buildTestSpatialDataSetToDoubleColumn(1);
		

		actual =Stats.min(res,originalLength);
		Assert.assertEquals(2,actual,0.001);


		List<Double> list = buildTestSpatialDataSetToDoubleColumnList(0);

		actual =Stats.min(list);

		Assert.assertEquals(100,actual,0.001);
		
		list =buildTestSpatialDataSetToDoubleColumnList(1);
		

		actual =Stats.min(list);
		Assert.assertEquals(2,actual,0.001);
		
	}

	@Test
	void _testFuseDatasets_std_dev_all() {

		double []res =buildTestSpatialDataSetToDoubleColumn(0);

		double actual =Stats.standard_deviation(res, res.length);

		Assert.assertEquals(29.08046543,actual,0.001);
		
		res =buildTestSpatialDataSetToDoubleColumn(1);
		

		actual =Stats.standard_deviation(res, res.length);
		Assert.assertEquals(1.646610619,actual,0.001);
		
res =buildTestSpatialDataSetToDoubleColumn(0);
		
		int originalLength = res.length;
		
		expandArray(res,20,10000);
		actual =Stats.standard_deviation(res, originalLength);

		Assert.assertEquals(29.08046543,actual,0.001);
		
		res =buildTestSpatialDataSetToDoubleColumn(1);
		

		actual =Stats.standard_deviation(res, originalLength);
		Assert.assertEquals(1.646610619,actual,0.001);
	
		List<Double> list = buildTestSpatialDataSetToDoubleColumnList(0);

		actual =Stats.standard_deviation(list);

		Assert.assertEquals(29.08046543,actual,0.001);
		
		list =buildTestSpatialDataSetToDoubleColumnList(1);
		

		actual =Stats.standard_deviation(list);
		Assert.assertEquals(1.646610619,actual,0.001);
		

	}
	@Test
	void _testFuseDatasets_median_all() {

		double []res =buildTestSpatialDataSetToDoubleColumn(0);

		double actual =Stats.median(res, res.length);

		Assert.assertEquals(124,actual,0.001);
		
		res =buildTestSpatialDataSetToDoubleColumn(1);
		

		actual =Stats.median(res, res.length);
		Assert.assertEquals(4.375,actual,0.001);
		
		
res =buildTestSpatialDataSetToDoubleColumn(0);
		
		int originalLength = res.length;
		
		expandArray(res,1000,1000);
		
		 actual =Stats.median(res, originalLength);

			Assert.assertEquals(124,actual,0.001);
			
			res =buildTestSpatialDataSetToDoubleColumn(1);
			

			actual =Stats.median(res, originalLength);
			Assert.assertEquals(4.375,actual,0.001);
		
			List<Double> list = buildTestSpatialDataSetToDoubleColumnList(0);

			actual =Stats.median(list);

			Assert.assertEquals(124,actual,0.001);
			
			list =buildTestSpatialDataSetToDoubleColumnList(1);
			

			actual =Stats.median(list);
			Assert.assertEquals(4.375,actual,0.001);

	}
	
	static double [] expandArray(double []arr, int extensionLength,double filler) {
		double [] res = new double[arr.length +extensionLength];
		for(int i = 0;i<arr.length;i++) {
			res[i] = arr[i];
		}
		for(int i = arr.length;i< res.length;i++) {
			res[i] = filler;
		}
		
		return res;
	}
	static double[] buildTestSpatialDataSetToDoubleColumn(int colIx) {
		SpatialDataset tmp = TestDataFusion.buildHighResTestDataset();
		double [] res  = new double[tmp.size()];
		
		//build a column progressivly
		for(int i = 0;i<tmp.size();i++) {
			
			SpatialData pt = tmp.getSpatialData(i);
			double [] row = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			res[i] = row[colIx];
		}
		
		return res;
		
	}

	static List<Double> buildTestSpatialDataSetToDoubleColumnList(int colIx) {
		 
		SpatialDataset tmp = TestDataFusion.buildHighResTestDataset();
		List<Double>  res = new ArrayList<Double>(tmp.size());
		
		//build a column progressivly
		for(int i = 0;i<tmp.size();i++) {
			
			SpatialData pt = tmp.getSpatialData(i);
			double [] row = pt.parseAttributesToDoubleArray(SpatialData.COMMA);
			res.add(row[colIx]);
		}
		
		return res;
		
	}

}
