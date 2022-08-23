package test;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import core.Util;
import data_structure.BoundingBox;

class TestBoundingBox {
	public final static double DOUBLE_EQUALITY_EPSILON = 0.00001;
	@Test
	void testBoundingBox_two_points() {
	
		


		boolean exceptionOccured = false;
		try {
			Point2D maxCoords = null;
			Point2D minCoords = new Point2D.Double(0,0);
			
			
			BoundingBox res = new BoundingBox(minCoords, maxCoords);
	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		exceptionOccured = false;
		try {
			Point2D maxCoords = null;
			Point2D minCoords = null;
			
			
			BoundingBox res = new BoundingBox(minCoords, maxCoords);
	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		 
		exceptionOccured = false;
		try {
			Point2D maxCoords = new Point2D.Double(0,0);
			Point2D minCoords = null;
			
			
			BoundingBox res = new BoundingBox(minCoords, maxCoords);
	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;
		 


			try {

				Point2D maxCoords = new Point2D.Double(4,4);
				Point2D minCoords = new Point2D.Double(5,5);
				
				BoundingBox res = new BoundingBox(minCoords, maxCoords);	
			}catch(Throwable t) {
				exceptionOccured=true;
			}
			Assert.assertEquals(true,exceptionOccured);
			
			
			exceptionOccured = false;
			 


			try {

				Point2D maxCoords = new Point2D.Double(6,4);
				Point2D minCoords = new Point2D.Double(5,5);
				
				BoundingBox res = new BoundingBox(minCoords, maxCoords);	
			}catch(Throwable t) {
				exceptionOccured=true;
			}
			Assert.assertEquals(true,exceptionOccured);
			
			
			exceptionOccured = false;
			 


			try {

				Point2D maxCoords = new Point2D.Double(4,6);
				Point2D minCoords = new Point2D.Double(5,5);
				
				BoundingBox res = new BoundingBox(minCoords, maxCoords);	
			}catch(Throwable t) {
				exceptionOccured=true;
			}
			Assert.assertEquals(true,exceptionOccured);

	}

	@Test
	void testBoundingBox_raw_coords() {
		boolean exceptionOccured = false;
		 


		try {

			
			BoundingBox res = new BoundingBox(5,5,4,4);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;
		 


		try {

			
			BoundingBox res = new BoundingBox(5,5,6,4);		
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;
		 


		try {

			BoundingBox res = new BoundingBox(5,5,4,6);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
	}


	@Test
	void test_contains_Rectangle2D() {
		BoundingBox res = new BoundingBox(0,0,10,20);//10 x 20 rectangl;e
		
		
		
		//contains itself
		Rectangle2D rect = new Rectangle2D.Double(0, 0, 10 - 0, 20 - 0);
		Assert.assertEquals("contains itself",true,res.contains(rect));
		
		//contains smaller rectangle
		rect = new Rectangle2D.Double(0, 0, 9 - 0, 19 - 0);
		Assert.assertEquals("contains smaller rectangle",true,res.contains(rect));
		
		
		//contains much smaller rectangle
		rect = new Rectangle2D.Double(5, 5, 7 - 5, 9 - 5);
		Assert.assertEquals("contains much smaller ractangle ",true,res.contains(rect));
		
		//doesn't contain partial overlapping rectangle
		rect = new Rectangle2D.Double(-5, -5, 7 - (-5), 9 - (-5));
		Assert.assertEquals("doesn't contain partial overlapping rectangle",false,res.contains(rect));
		
		
		//doesn't contain partial overlapping rectangle
		rect = new Rectangle2D.Double(0, 0, 11-0, 20-0);
		Assert.assertEquals("doesn't contain partial overlapping rectangle",false,res.contains(rect));
		
		//doesn't contain partial overlapping rectangle
		rect = new Rectangle2D.Double(0, 0, 10-0, 21-0);
		Assert.assertEquals("doesn't contain partial overlapping rectangle",false,res.contains(rect));
		
		//doesn't contain partial overlapping rectangle
		rect = new Rectangle2D.Double(0, 0, 11-0, 21-0);
		Assert.assertEquals("doesn't contain partial overlapping rectangle",false,res.contains(rect));
		
		

		//doesn't contain non overlapping rectangle
		rect = new Rectangle2D.Double(100, 100, 115-100, 120-100);
		Assert.assertEquals("doesn't contain partial overlapping rectangle",false,res.contains(rect));
		
		

		//doesn't contain non overlapping rectangle
		rect = new Rectangle2D.Double(-100, -100, -115-(-100), -120-(-100));
		Assert.assertEquals("doesn't contain partial overlapping rectangle",false,res.contains(rect));
		
	}
	
	@Test
	void test_contains_Point2D() {
		
		
		BoundingBox res = new BoundingBox(0,0,10,20);//10 x 20 rectangl;e
		
		//center ish
		Point2D pt = new Point2D.Double(5,5);
		Assert.assertEquals(true,res.contains(pt));
		
		//borders
		pt = new Point2D.Double(0,0);
		Assert.assertEquals(true,res.contains(pt));
		pt = new Point2D.Double(10,0);
		Assert.assertEquals(true,res.contains(pt));
		pt = new Point2D.Double(10,20);
		Assert.assertEquals(true,res.contains(pt));
		pt = new Point2D.Double(0,20);
		Assert.assertEquals(true,res.contains(pt));
		
		//outside
		pt = new Point2D.Double(-5,-10);
		Assert.assertEquals(false,res.contains(pt));
		pt = new Point2D.Double(-5,10);
		Assert.assertEquals(false,res.contains(pt));
		pt = new Point2D.Double(5,-10);
		Assert.assertEquals(false,res.contains(pt));
		pt = new Point2D.Double(100,100);
		Assert.assertEquals(false,res.contains(pt));
		
	}
	
	@Test
	void test_isContainedBy() {
		BoundingBox res = new BoundingBox(0,0,10,20);//10 x 20 rectangl;e
		
		
		
		//contain within itself
		Rectangle2D rect = new Rectangle2D.Double(0, 0, 10 - 0, 20 - 0);
		Assert.assertEquals(true,res.isContainedBy(rect));
		
		
		//contained by bigger rec
		rect = new Rectangle2D.Double(0, 0, 20 - 0, 30 - 0);
		Assert.assertEquals(true,res.isContainedBy(rect));
		
		rect = new Rectangle2D.Double(-10, -10, 20 - (-10), 30 - (-10));
		Assert.assertEquals(true,res.isContainedBy(rect));
		
		rect = new Rectangle2D.Double(0, 0, 10 - 0, 25 - 0);
		Assert.assertEquals(true,res.isContainedBy(rect));
		
		rect = new Rectangle2D.Double(0, 0, 11 - 0, 20 - 0);
		Assert.assertEquals(true,res.isContainedBy(rect));
		
		rect = new Rectangle2D.Double(-1, 0, 10 - (-1), 20 - 0);
		Assert.assertEquals(true,res.isContainedBy(rect));
		
		rect = new Rectangle2D.Double(0, -1, 10 - 0, 20 - (-1));
		Assert.assertEquals(true,res.isContainedBy(rect));
		
		rect = new Rectangle2D.Double(-1, -1, 10 - (-1), 20 - (-1));
		Assert.assertEquals(true,res.isContainedBy(rect));
		
		
		//not contained by partial
		rect = new Rectangle2D.Double(0, 0, 5 - 0, 20 - 0);
		Assert.assertEquals(false,res.isContainedBy(rect));
		rect = new Rectangle2D.Double(0, 0, 10 - 0, 15 - 0);
		Assert.assertEquals(false,res.isContainedBy(rect));
		rect = new Rectangle2D.Double(5, 0, 10 - 5, 20 - 0);
		Assert.assertEquals(false,res.isContainedBy(rect));
		rect = new Rectangle2D.Double(0, 5, 10 - 0, 20 - 5);
		Assert.assertEquals(false,res.isContainedBy(rect));
		rect = new Rectangle2D.Double(5, 5, 10 - 5, 20 - 5);
		Assert.assertEquals(false,res.isContainedBy(rect));
		
		//not contained full
		rect = new Rectangle2D.Double(5, 5, 5 - 5, 10 - 5);
		Assert.assertEquals(false,res.isContainedBy(rect));
		
	}
	
	@Test
	void testGetMinCoords() {
		BoundingBox res = new BoundingBox(4,5,6,7);
		Point2D pt = res.getMinCoords();
		
		Assert.assertEquals(true,Util.almostEqual(4,pt.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(5,pt.getY(),DOUBLE_EQUALITY_EPSILON));
		
	}

	@Test
	void testSetMinCoords() {
		BoundingBox res = new BoundingBox(4,5,6,7);
		
		res.setMinCoords(1, 2);
		Point2D pt = res.getMinCoords();
		
		Assert.assertEquals(true,Util.almostEqual(1,pt.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(2,pt.getY(),DOUBLE_EQUALITY_EPSILON));
		
		res.setMinCoords(new Point2D.Double(3, 4));
		pt = res.getMinCoords();
		
		Assert.assertEquals(true,Util.almostEqual(3,pt.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(4,pt.getY(),DOUBLE_EQUALITY_EPSILON));
	}

	@Test
	void testGetMaxCoords() {
		BoundingBox res = new BoundingBox(4,5,6,7);
		Point2D pt = res.getMaxCoords();
		
		Assert.assertEquals(true,Util.almostEqual(6,pt.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(7,pt.getY(),DOUBLE_EQUALITY_EPSILON));
	}

	@Test
	void testSetMaxCoords() {
		BoundingBox res = new BoundingBox(4,5,6,7);
		
		res.setMaxCoords(8, 9);
		Point2D pt = res.getMaxCoords();
		
		Assert.assertEquals(true,Util.almostEqual(8,pt.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(9,pt.getY(),DOUBLE_EQUALITY_EPSILON));
		
		res.setMaxCoords(new Point2D.Double(10,11));
		pt = res.getMaxCoords();
		
		Assert.assertEquals(true,Util.almostEqual(10,pt.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(11,pt.getY(),DOUBLE_EQUALITY_EPSILON));
	}
	
	@Test
	void testSetMaxCoords_badargument() {
		BoundingBox res = new BoundingBox(4,5,6,7);
		
		boolean exceptionOccured = false;
		 


		try {

			
			res.setMaxCoords(1, 2);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;
		
		

		 


		try {

			
			res.setMaxCoords(8, 2);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;
		

		try {

			
			res.setMaxCoords(1, 10);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false; 
		
		
		
		exceptionOccured = false;
		

		try {

			
			res.setMaxCoords(null);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;
		 

	}
	
	@Test
	void testSetMinCoords_badargument() {
		BoundingBox res = new BoundingBox(4,5,6,7);
		
		boolean exceptionOccured = false;
		 


		try {

			
			res.setMinCoords(10, 11);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;
		
		

		 


		try {

			
			res.setMinCoords(1, 12);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;
		

		try {

			
			res.setMinCoords(13, 2);
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false; 
		
		
		
		exceptionOccured = false;
		

		try {

			
			res.setMinCoords(null);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;
		 

	}

}
