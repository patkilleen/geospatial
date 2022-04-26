package test;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import core.Util;
import data_structure.BoundingBox;
import data_structure.SpatialData;
import data_structure.SpatialDataIndex;
import data_structure.SpatialDataset;


class TestSpatialDataIndex {

	public final static double DOUBLE_EQUALITY_EPSILON = 0.00001;
	
	@Test
	void testget_constructor_badargument() {
		
		boolean exceptionOccured = false;


		try {
			SpatialDataIndex si = new SpatialDataIndex(null);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		 exceptionOccured = false;


			try {

				SpatialDataset ds = new SpatialDataset(0);
					

				SpatialDataIndex si = new SpatialDataIndex(ds);	
			}catch(Throwable t) {
				exceptionOccured=true;
			}


			Assert.assertEquals(true,exceptionOccured);
			
			
	}
	
	@Test
	void testgetSpatialDataInRange_bad_arguments() {
		

		SpatialDataset ds = buildTestDataset();
		

		SpatialDataIndex si = new SpatialDataIndex(ds);

		boolean exceptionOccured = false;
	
		
		exceptionOccured = false;
		try {

			
			//bounding box is entire dataset
			SpatialDataset res = si.getSpatialDataInBoundingBox(null);
	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
	}
	@Test
	void testgetSpatialDataInRange_entire_dataset1() {
	
		
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		Point2D maxCoords = ds.findMaxCoordinates();
		Point2D minCoords = ds.findMinCoordinates();
		
		//bounding box is entire dataset
		SpatialDataset res = si.getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords));
		
		//_testgetSpatialDataInRange_entire_dataset(ds,res);
		assertSpatialDatasetsEqual(ds,res);
		
	
	}
	@Test
	void testgetSpatialDataInRange_entire_dataset2() {
	
		SpatialDataset res = new SpatialDataset(32);
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		Point2D maxCoords = ds.findMaxCoordinates();
		Point2D minCoords = ds.findMinCoordinates();
		
		//bounding box is entire dataset
		si._getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords),res);
		
		//_testgetSpatialDataInRange_entire_dataset(ds,res);
		assertSpatialDatasetsEqual(ds,res);
		
	
	}
	
	/*void _testgetSpatialDataInRange_entire_dataset(SpatialDataset ds,SpatialDataset res) {
		
		Assert.assertEquals(res.size(), ds.size());
		
		//sort both datasets so points align
		res.sort();
		ds.sort();
		
		for(int i = 0;i<ds.size();i++) {
			SpatialData pt1 = ds.getSpatialData(i);
			SpatialData pt2 = res.getSpatialData(i);
			
			Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getX(), pt2.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
			Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getY(), pt2.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));			
		}
	}*/
	@Test
	void testgetSpatialDataInRange_left_half_points1() {
	
		
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		
		
		
		
		
		Point2D maxCoords = ds.findMaxCoordinates();
		Point2D minCoords = ds.findMinCoordinates();
		
		//max max x be 0, so all points with more than 0 x excluded
		maxCoords.setLocation(0, maxCoords.getY());
		
		
		
		SpatialDataset res = si.getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords));
		
		_testgetSpatialDataInRange_left_half_points(ds,res);
		
	}
	
	@Test
	void testgetSpatialDataInRange_left_half_points2() {
	
		SpatialDataset res = new SpatialDataset(32);
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		
		
		
		
		
		Point2D maxCoords = ds.findMaxCoordinates();
		Point2D minCoords = ds.findMinCoordinates();
		
		//max max x be 0, so all points with more than 0 x excluded
		maxCoords.setLocation(0, maxCoords.getY());
		
		
		
		si._getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords),res);
		
		_testgetSpatialDataInRange_left_half_points(ds,res);
		
	}
				
	void _testgetSpatialDataInRange_left_half_points(SpatialDataset ds,SpatialDataset res) {
		SpatialDataset expected = new SpatialDataset(ds.size());
		
		//dynamically populate expected points
		for(int i = 0;i<ds.size();i++) {
			SpatialData pt1 = ds.getSpatialData(i);
		
			if(pt1.getLocation().getX() <=0) {
				expected.addSpatialData(pt1);
			}
		}
		
		assertSpatialDatasetsEqual(res,expected);
		/*Assert.assertEquals(res.size(), expected.size());
		//sort both datasets so points align
		res.sort();
		expected.sort();
		
		for(int i = 0;i<expected.size();i++) {
			SpatialData pt1 = ds.getSpatialData(i);
			SpatialData pt2 = res.getSpatialData(i);
			
			Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getX(), pt2.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
			Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getY(), pt2.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));			
		}*/
	}
	
	

	@Test
	void testgetSpatialDataInRange_right_half_points1() {
	
		
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		
		
		
		
		
		Point2D maxCoords = ds.findMaxCoordinates();
		Point2D minCoords = ds.findMinCoordinates();
		
		//max max x be 0, so all points with more than 0 x excluded
		minCoords.setLocation(0, minCoords.getY());
		
		
		
		SpatialDataset res = si.getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords));
		
		_testgetSpatialDataInRange_right_half_points(ds,res);
		}
	
	

	@Test
	void testgetSpatialDataInRange_right_half_points2() {
	
		SpatialDataset res = new SpatialDataset(32);
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		
		
		
		
		
		Point2D maxCoords = ds.findMaxCoordinates();
		Point2D minCoords = ds.findMinCoordinates();
		
		//max max x be 0, so all points with more than 0 x excluded
		minCoords.setLocation(0, minCoords.getY());
		
		
		
		si._getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords),res);
		
		_testgetSpatialDataInRange_right_half_points(ds,res);
		}
		
	
	void _testgetSpatialDataInRange_right_half_points(SpatialDataset ds,SpatialDataset res) {
		SpatialDataset expected = new SpatialDataset(ds.size());
		
		//dynamically populate expected points
		for(int i = 0;i<ds.size();i++) {
			SpatialData pt1 = ds.getSpatialData(i);
		
			if(pt1.getLocation().getX() >=0) {
				expected.addSpatialData(pt1);
			}
		}
		
		assertSpatialDatasetsEqual(expected,res);
		/*Assert.assertEquals(res.size(), expected.size());
		//sort both datasets so points align
		res.sort();
		expected.sort();
		
		for(int i = 0;i<expected.size();i++) {
			SpatialData pt1 = expected.getSpatialData(i);
			SpatialData pt2 = res.getSpatialData(i);
			
			Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getX(), pt2.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
			Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getY(), pt2.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));			
		}*/
	}
	
		
	
	
	@Test
	void testgetSpatialDataInRange_center_points1() {
	
		
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		
		
		
		
		
		Point2D maxCoords = new Point2D.Double(4,6);
		Point2D minCoords = new Point2D.Double(0,2);
		
		
		
		
		SpatialDataset res = si.getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords));
		_testgetSpatialDataInRange_center_points(ds,res);
	}
	
	@Test
	void testgetSpatialDataInRange_center_points2() {
	
		SpatialDataset res = new SpatialDataset(32);
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		
		
		
		
		
		Point2D maxCoords = new Point2D.Double(4,6);
		Point2D minCoords = new Point2D.Double(0,2);
		
		
		
		
		si._getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords),res);
		_testgetSpatialDataInRange_center_points(ds,res);
	}
	
	
	void _testgetSpatialDataInRange_center_points(SpatialDataset ds,SpatialDataset res) {
		SpatialDataset expected = new SpatialDataset(ds.size());
	
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(2,4),null));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(2,5),null));
		expected.addSpatialData(new SpatialData(2,new Point2D.Double(4,2),null));
		
		assertSpatialDatasetsEqual(expected,res);
		/*Assert.assertEquals(res.size(), expected.size());
		//sort both datasets so points align
		res.sort();
		expected.sort();
		
		for(int i = 0;i<expected.size();i++) {
			SpatialData pt1 = expected.getSpatialData(i);
			SpatialData pt2 = res.getSpatialData(i);
			
			Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getX(), pt2.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
			Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getY(), pt2.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));			
		}*/
	}
	
		

	
	@Test
	void testgetSpatialDataInRange_empty_region1() {
	
		
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);

		
		Point2D maxCoords = new Point2D.Double(6,0);
		Point2D minCoords = new Point2D.Double(2,-6);
		
		
		
		
		SpatialDataset res = si.getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords));		
	
		Assert.assertEquals(0,res.size());
		
		
		maxCoords = new Point2D.Double(6,0);
		minCoords = new Point2D.Double(2,-6);
		
		
		
		
		res = si.getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords));
				
			
		Assert.assertEquals(0,res.size());
		
	}
	
	
	@Test
	void testgetSpatialDataInRange_empty_region2() {
	
		SpatialDataset res = new SpatialDataset(32);
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);

		
		Point2D maxCoords = new Point2D.Double(6,0);
		Point2D minCoords = new Point2D.Double(2,-6);
		
		
		
		
		si._getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords),res);		
	
		Assert.assertEquals(0,res.size());
		
		
		maxCoords = new Point2D.Double(6,0);
		minCoords = new Point2D.Double(2,-6);
		
		
		
		
		si._getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords),res);
				
			
		Assert.assertEquals(0,res.size());
		
	}
	
	@Test
	void testgetSpatialDataInRange_single_point1() {
	
		
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		
		
		
		
		
		Point2D maxCoords = ds.getSpatialData(0).getLocation();
		Point2D minCoords = maxCoords;
		
		
		
		
		SpatialDataset res = si.getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords));
				
			
		Assert.assertEquals(1,res.size());
		
		
		SpatialData pt1 = res.getSpatialData(0);
		SpatialData pt2 = ds.getSpatialData(0);
		
		Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getX(), pt2.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getY(), pt2.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
		
	}


	@Test
	void testgetSpatialDataInRange_single_point2() {
	
		SpatialDataset res = new SpatialDataset(32);
		SpatialDataset ds = buildTestDataset();
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		
		
		
		
		
		Point2D maxCoords = ds.getSpatialData(0).getLocation();
		Point2D minCoords = maxCoords;
		
		
		
		
		si._getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords),res);
				
			
		Assert.assertEquals(1,res.size());
		
		
		SpatialData pt1 = res.getSpatialData(0);
		SpatialData pt2 = ds.getSpatialData(0);
		
		Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getX(), pt2.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getY(), pt2.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
		
	}
	
	@Test
	void testgetSpatialDataInRange_colocated_points() {
	
		
		SpatialDataset ds = new SpatialDataset(5);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(1,1),""));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(1,2),""));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(2,1),""));
		ds.addSpatialData(new SpatialData(4,new Point2D.Double(2,2),""));
		ds.addSpatialData(new SpatialData(5,new Point2D.Double(2,1),""));
		
		
		SpatialDataset res = new SpatialDataset(32);
		
			

		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		
		
		
		
		//bounding box is the location of the two points, (2,1)
		Point2D maxCoords = ds.getSpatialData(2).getLocation();
		Point2D minCoords = maxCoords;
		
		
		
		
		si._getSpatialDataInBoundingBox(new BoundingBox(minCoords, maxCoords),res);
				
			
		Assert.assertEquals(2,res.size());
		
		
		SpatialData pt1 = res.getSpatialData(0);
		SpatialData pt2 = new SpatialData(3,new Point2D.Double(2,1),"");
		
		Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getX(), pt2.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getY(), pt2.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
		
		pt1 = res.getSpatialData(1);
		pt2 = new SpatialData(5,new Point2D.Double(2,1),"");
		
		Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getX(), pt2.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getY(), pt2.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
		
	}
	

	
	@Test
	void getSpatialDataInNeighborhood_bad_arguments() {
		SpatialDataset ds=buildTestDataset();
		
		
				
		
		
		boolean exceptionOccured = false;


		try {
			SpatialDataset res = SpatialDataIndex.getSpatialDataInNeighborhood(null, new Point2D.Double(0,0), 0, SpatialData.DistanceMetric.EUCLIDEAN);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;


		try {
			SpatialDataset res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, null, 0, SpatialData.DistanceMetric.EUCLIDEAN);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		exceptionOccured = false;


		try {
			SpatialDataset res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), -1, SpatialData.DistanceMetric.EUCLIDEAN);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		
		SpatialDataset tmp = new SpatialDataset(3);
		
		exceptionOccured = false;


		try {
			SpatialDataIndex._getSpatialDataInNeighborhood(null, new Point2D.Double(0,0), 0, SpatialData.DistanceMetric.EUCLIDEAN,tmp);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;


		try {
			SpatialDataIndex._getSpatialDataInNeighborhood(ds, null, 0, SpatialData.DistanceMetric.EUCLIDEAN,tmp);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		exceptionOccured = false;


		try {
			SpatialDataIndex._getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), -1, SpatialData.DistanceMetric.EUCLIDEAN,tmp);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		exceptionOccured = false;


		try {
			SpatialDataIndex._getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 1, SpatialData.DistanceMetric.EUCLIDEAN,null);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
	
	
	}
	@Test
	void getSpatialDataInNeighborhood() {
		SpatialDataset ds=buildTestDataset();
		
		
		SpatialDataset res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 0, SpatialData.DistanceMetric.EUCLIDEAN);		
		Assert.assertEquals(0,res.size());
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 0, SpatialData.DistanceMetric.INFINITY_NORM);		
		Assert.assertEquals(0,res.size());
		
		
		ds = new SpatialDataset(0);
		
		SpatialDataset expected= new SpatialDataset(1);
		
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 0, SpatialData.DistanceMetric.EUCLIDEAN);		
		assertSpatialDatasetsEqual(expected,res);
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 0, SpatialData.DistanceMetric.INFINITY_NORM);		
		assertSpatialDatasetsEqual(expected,res);
	
		ds=buildTestDataset();
		
		expected= new SpatialDataset(1);
		
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 1, SpatialData.DistanceMetric.EUCLIDEAN);		
		assertSpatialDatasetsEqual(expected,res);
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 1, SpatialData.DistanceMetric.INFINITY_NORM);		
		assertSpatialDatasetsEqual(expected,res);
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 2, SpatialData.DistanceMetric.EUCLIDEAN);		
		assertSpatialDatasetsEqual(expected,res);
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 2, SpatialData.DistanceMetric.INFINITY_NORM);		
		assertSpatialDatasetsEqual(expected,res);
		
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 2.5, SpatialData.DistanceMetric.EUCLIDEAN);		
		assertSpatialDatasetsEqual(expected,res);
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 2.5, SpatialData.DistanceMetric.INFINITY_NORM);		
		assertSpatialDatasetsEqual(expected,res);
		
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 3, SpatialData.DistanceMetric.EUCLIDEAN);		
		assertSpatialDatasetsEqual(expected,res);
		
		expected= new SpatialDataset(3);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 3, SpatialData.DistanceMetric.INFINITY_NORM);		
		assertSpatialDatasetsEqual(expected,res);
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 4.3, SpatialData.DistanceMetric.EUCLIDEAN);		
		assertSpatialDatasetsEqual(expected,res);
		
		expected= new SpatialDataset(3);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-4),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(4,2),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(2,4),null));
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 4.3, SpatialData.DistanceMetric.INFINITY_NORM);		
		assertSpatialDatasetsEqual(expected,res);
		
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(2,4),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(4,2),null));
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 4.5, SpatialData.DistanceMetric.EUCLIDEAN);		
		assertSpatialDatasetsEqual(expected,res);
		
		
		
		
		
		expected= new SpatialDataset(3);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-4),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(4,2),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(2,4),null));
		SpatialDataIndex._getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 4.3, SpatialData.DistanceMetric.INFINITY_NORM,res);		
		assertSpatialDatasetsEqual(expected,res);
		
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(2,4),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(4,2),null));
		SpatialDataIndex._getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 4.5, SpatialData.DistanceMetric.EUCLIDEAN,res);		
		assertSpatialDatasetsEqual(expected,res);
		
	}
	
	@Test
	void getSpatialDataInCircularNeighborhood() {
		SpatialDataset ds=buildTestDataset();
		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		SpatialDataset bbBuffer =new SpatialDataset(4);
		
		SpatialDataset res = si.getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 0);		

		SpatialDataset expected= new SpatialDataset(1);
		
		res = si.getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 1);		
		assertSpatialDatasetsEqual(expected,res);
		
		si._getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 0,res,bbBuffer);
		assertSpatialDatasetsEqual(expected,res);
		
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		res = si.getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 2);		
		assertSpatialDatasetsEqual(expected,res);
		
		si._getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 2,res,bbBuffer);
		assertSpatialDatasetsEqual(expected,res);
		
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		res = si.getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 2.5);		
		assertSpatialDatasetsEqual(expected,res);
		
		si._getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 2.5,res,bbBuffer);
		assertSpatialDatasetsEqual(expected,res);
		
		
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		res = si.getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 3);		
		assertSpatialDatasetsEqual(expected,res);
		
		si._getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 3,res,bbBuffer);
		assertSpatialDatasetsEqual(expected,res);
		
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		res = si.getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 4.3);		
		assertSpatialDatasetsEqual(expected,res);
		
		si._getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 4.3,res,bbBuffer);
		assertSpatialDatasetsEqual(expected,res);
		
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(2,4),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(4,2),null));
		res = si.getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 4.5);		
		assertSpatialDatasetsEqual(expected,res);
		
		si._getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 4.5,res,bbBuffer);
		assertSpatialDatasetsEqual(expected,res);
		

	}

	
	@Test
	void getSpatialDataInSquareNeighborhood() {
		SpatialDataset ds=buildTestDataset();
		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		SpatialDataset res = si.getSpatialDataInSquareNeighborhood(new Point2D.Double(0,0), 0);		
		
		Assert.assertEquals(0,res.size());
		

		SpatialDataset expected= new SpatialDataset(1);
		
		
		res = si.getSpatialDataInSquareNeighborhood(new Point2D.Double(0,0), 1*2);		
		assertSpatialDatasetsEqual(expected,res);
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		
		res =si.getSpatialDataInSquareNeighborhood(new Point2D.Double(0,0), 2*2);		
		assertSpatialDatasetsEqual(expected,res);
		
		
		expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		
		res =si.getSpatialDataInSquareNeighborhood(new Point2D.Double(0,0), 2.5*2);		
		assertSpatialDatasetsEqual(expected,res);
		
		
		
		expected= new SpatialDataset(3);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		res =si.getSpatialDataInSquareNeighborhood(new Point2D.Double(0,0), 3*2);		
		assertSpatialDatasetsEqual(expected,res);
		
		
		expected= new SpatialDataset(3);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-4),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(4,2),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(2,4),null));
		res = si.getSpatialDataInSquareNeighborhood(new Point2D.Double(0,0), 4.3*2);		
		assertSpatialDatasetsEqual(expected,res);
		
		
		
		
		
		expected= new SpatialDataset(3);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-2,0),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(3,3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-4),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(4,2),null));
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(2,4),null));
		si._getSpatialDataInSquareNeighborhood(new Point2D.Double(0,0), 4.3*2,res);		
		assertSpatialDatasetsEqual(expected,res);
		

		
	}
	
	

	
	@Test
	void getSpatialDataInNeighborhood_all_dataset() {
		SpatialDataset ds=buildTestDataset();
		
		
		SpatialDataset res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 1000, SpatialData.DistanceMetric.EUCLIDEAN);		
		assertSpatialDatasetsEqual(ds,res);
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), 1000, SpatialData.DistanceMetric.INFINITY_NORM);		
		assertSpatialDatasetsEqual(ds,res);
		
	}
	
	
	@Test
	void getSpatialDataInCircularNeighborhood_all_dataset() {
		SpatialDataset ds=buildTestDataset();
		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		SpatialDataset res = si.getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 1000);		
		assertSpatialDatasetsEqual(ds,res);
		
		SpatialDataset bbBuffer =new SpatialDataset(4);
		
		si._getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 1000,res,bbBuffer);		
		assertSpatialDatasetsEqual(ds,res);
		
		si._getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), 1000,res,bbBuffer);		
		assertSpatialDatasetsEqual(ds,res);
		
		
	}
	
	@Test
	void getSpatialDataInSquareNeighborhood_all_dataset() {
		SpatialDataset ds=buildTestDataset();
		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		SpatialDataset res = si.getSpatialDataInSquareNeighborhood(new Point2D.Double(0,0), 1000);		
		assertSpatialDatasetsEqual(ds,res);
		
	}
	@Test
	void getSpatialDataInNeighborhood_0_radius_single_point() {
		SpatialDataset ds=buildTestDataset();
		
		SpatialDataset expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(4,2),null));
		
		SpatialDataset res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(4,2), 0, SpatialData.DistanceMetric.EUCLIDEAN);		
		assertSpatialDatasetsEqual(expected,res);
		res = SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(4,2), 0, SpatialData.DistanceMetric.INFINITY_NORM);		
		assertSpatialDatasetsEqual(expected,res);
		
	}
	
	@Test
	void getSpatialDataInCircularNeighborhood_0_radius_single_point() {
		 
		
		SpatialDataset ds=buildTestDataset();
		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		SpatialDataset expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(4,2),null));
		
		SpatialDataset res = si.getSpatialDataInCircularNeighborhood(new Point2D.Double(4,2), 0);		
		assertSpatialDatasetsEqual(expected,res);
				
		
		SpatialDataset bbBuffer =new SpatialDataset(4);
		
		si._getSpatialDataInCircularNeighborhood(new Point2D.Double(4,2), 0,res,bbBuffer);		
		assertSpatialDatasetsEqual(expected,res);
		
		si._getSpatialDataInCircularNeighborhood(new Point2D.Double(4,2), 0,res,bbBuffer);		
		assertSpatialDatasetsEqual(expected,res);
				
	}
	
	
	@Test
	void getSpatialDataInSquareNeighborhood_0_radius_single_point() {
		SpatialDataset ds=buildTestDataset();
		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		SpatialDataset expected= new SpatialDataset(1);
		expected.addSpatialData(new SpatialData(1,new Point2D.Double(4,2),null));
		
		SpatialDataset res = si.getSpatialDataInSquareNeighborhood(new Point2D.Double(4,2), 0);		
		assertSpatialDatasetsEqual(expected,res);
		
		
		
	}
	
	
	//this test compares the 2 different ways of fetching points indide a block (one efficient way, and another innefficenit)
	//they should return identical point sets fro same center and radius
	@Test
	void getSpatialDataInSquareNeighborhood_vs_raw_dist() {
		SpatialDataset ds=buildTestDataset();
		SpatialDataIndex si = new SpatialDataIndex(ds);
		
		SpatialDataset bbBuffer =new SpatialDataset(4);
		//for center (0,0)
		for(double radius = 0;radius < 15;radius+=0.1) {
			SpatialDataset res1= si.getSpatialDataInSquareNeighborhood(new Point2D.Double(0,0), radius*2.0);
			SpatialDataset res2= SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), (radius*2.0)/2.0, SpatialData.DistanceMetric.INFINITY_NORM);
			assertSpatialDatasetsEqual(res2,res1);
			
			
			res1= si.getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), radius);
			res2= SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), radius, SpatialData.DistanceMetric.EUCLIDEAN);
			assertSpatialDatasetsEqual(res2,res1);
			
			
			si._getSpatialDataInCircularNeighborhood(new Point2D.Double(0,0), radius,res1,bbBuffer);
			res2= SpatialDataIndex.getSpatialDataInNeighborhood(ds, new Point2D.Double(0,0), radius, SpatialData.DistanceMetric.EUCLIDEAN);
			assertSpatialDatasetsEqual(res2,res1);
			
			
		}
		
		
		Iterator<SpatialData> it = ds.iterator();
		while(it.hasNext()) {
			
			SpatialData pt = it.next();
			Point2D center = pt.getLocation();
			//for all the points to act as center 
			for(double radius = 0;radius < 15;radius+=0.5) {
				SpatialDataset res1= si.getSpatialDataInSquareNeighborhood(center, radius*2.0);
				SpatialDataset res2= SpatialDataIndex.getSpatialDataInNeighborhood(ds, center, (radius*2.0)/2.0, SpatialData.DistanceMetric.INFINITY_NORM);
				
				if(res1.size() != res2.size()){
					System.out.println("degub");
				}
				assertSpatialDatasetsEqual(res2,res1);
				
				res1= si.getSpatialDataInCircularNeighborhood(center, radius);
				res2= SpatialDataIndex.getSpatialDataInNeighborhood(ds, center, radius, SpatialData.DistanceMetric.EUCLIDEAN);
				
				assertSpatialDatasetsEqual(res2,res1);
			}
				
		}
	
	
		
				
	}
	
	public void assertSpatialDatasetsEqual(SpatialDataset  ds1,SpatialDataset ds2) {

		if(ds1== null) {
			if(ds2 != null) {
				Assert.fail("datasets not the same. one was null and the other wasn't");
			}else {
				return; //they both null
			}
		}
		if(ds2== null) {
			if(ds1 != null) {
				Assert.fail("datasets not the same. one was null and the other wasn't");
			}else {
				return; //they both null
			}
		}
		Assert.assertEquals(ds1.size(), ds2.size());
		//sort both datasets so points align
		ds1.sort();
		ds2.sort();
		
		for(int i = 0;i<ds1.size();i++) {
			SpatialData pt1 = ds1.getSpatialData(i);
			SpatialData pt2 = ds2.getSpatialData(i);
			
			Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getX(), pt2.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
			Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getY(), pt2.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));			
		}
	}
	/**
	 * builds dataset of the points in the data/test/testing.xlsx:spatial-index
	 * @return
	 */
	public static SpatialDataset buildTestDataset() {
		SpatialDataset ds = new SpatialDataset(32);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(-3,-3),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(-3,-4),null));
		ds.addSpatialData( new SpatialData(3,new Point2D.Double(-4,-5),null));
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(-2,-5),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(-2,0),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(-3,6),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(-3,7),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(-4,8),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(-2,6),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(-2,7),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(11,8),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(8,7),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(9,8),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(11,-4),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(8,-3),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(9,-4),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(2,4),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(3,3),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(2,5),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(4,2),null));
		return ds;
	}
	
}
