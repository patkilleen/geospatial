package test;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import core.Util;
import data_structure.SpatialData;


class TestSpatialDatas {

	public final static double DOUBLE_EQUALITY_EPSILON = 0.00001;
	@Test
	void testSpatialData_constructor() {
		SpatialData sd = null;
		
		//public SpatialData(int id, Point2D location, String attributes)
		sd = new SpatialData(1,new Point2D.Double(2,3),"4,5");
		
		Assert.assertEquals(1,sd.getId());
		Assert.assertEquals(true,Util.almostEqual(2,sd.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(3,sd.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(false,Util.almostEqual(2,sd.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(false,Util.almostEqual(3,sd.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,"4,5".equals(sd.getAttributes()));
		
		
		sd = new SpatialData(-1,new Point2D.Double(2,3),"");
		Assert.assertEquals(-1,sd.getId());
		Assert.assertEquals(true,"".equals(sd.getAttributes()));
		
		sd = new SpatialData(-1000,new Point2D.Double(2,3),null);
		Assert.assertEquals(-1000,sd.getId());
		Assert.assertEquals(null, sd.getAttributes());
	}

	@Test
	void testSpatialData_constructor_badagruments() {
		
		boolean exceptionOccured = false;
		SpatialData sd = null;
		
		
		try {
			sd = new SpatialData(1,null,"4,5");	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		
		
		Assert.assertEquals(true,exceptionOccured);
		
	}
	
	@Test
	void testSpatialData_setters_getters() {
		SpatialData sd = null;
		
		//public SpatialData(int id, Point2D location, String attributes)
		sd = new SpatialData(0,new Point2D.Double(0,0),"0,0");
		
		sd.setId(1);
		sd.setLocation(new Point2D.Double(1,2));
		sd.setAttributes("3,4");
		
		Assert.assertEquals(1,sd.getId());
		Assert.assertEquals(true,Util.almostEqual(1,sd.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(2,sd.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(false,Util.almostEqual(1,sd.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(false,Util.almostEqual(2,sd.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,"3,4".equals(sd.getAttributes()));
		
		
		sd.setId(-1000);
		sd.setLocation(new Point2D.Double(-10,-1));
		sd.setAttributes("");
		
		Assert.assertEquals(-1000,sd.getId());
		Assert.assertEquals(true,Util.almostEqual(-10,sd.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(-1,sd.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(false,Util.almostEqual(-10,sd.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(false,Util.almostEqual(-1,sd.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,"".equals(sd.getAttributes()));
		
		sd.setAttributes(null);
		
		Assert.assertEquals(null, sd.getAttributes());
	}
	
	@Test
	void testSpatialData_setters_getters_badargument() {
		SpatialData sd = null;
		
		//public SpatialData(int id, Point2D location, String attributes)
		sd = new SpatialData(0,new Point2D.Double(0,0),"0,0");		
		
		boolean exceptionOccured = false;
		
		
		
		try {
			sd.setLocation(null);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		
		
		Assert.assertEquals(true,exceptionOccured);
		
	}
	
	@Test
	void testEuclideanDistanceTo_bad_arg() {
		SpatialData sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		SpatialData sd2 = null;
		
		boolean exceptionOccured = false;
		
		
		Point2D tmp = null;
		try {
			double res = sd1.distanceTo(tmp,SpatialData.DistanceMetric.EUCLIDEAN);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		
		
		Assert.assertEquals(true,exceptionOccured);
		
		exceptionOccured = false;
		
		
		
		try {
			double res = sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		
		
		Assert.assertEquals(true,exceptionOccured);
	}
	@Test
	void testEuclideanDistanceTo() {
		SpatialData sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		SpatialData sd2 = new SpatialData(0,new Point2D.Double(0,10),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(10,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(-10,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,-10),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(10,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(-10,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,-10),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,10),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,0),"");
		Assert.assertEquals(true,Util.almostEqual(0,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(10,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(10,0),"");
		Assert.assertEquals(true,Util.almostEqual(0,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(10,10),"");
		sd2 = new SpatialData(0,new Point2D.Double(10,10),"");
		Assert.assertEquals(true,Util.almostEqual(0,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),DOUBLE_EQUALITY_EPSILON));
		
		// sqrt {(0-3)^2 + (0-10)^2}
		//== sqrt(9 + 100) = sqrt(109)~ 10.44
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(3,10),"");
		Assert.assertEquals(true,Util.almostEqual(10.44,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),0.01));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(-3,-10),"");
		Assert.assertEquals(true,Util.almostEqual(10.44,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),0.01));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(100,100),"");
		sd2 = new SpatialData(0,new Point2D.Double(103,110),"");
		Assert.assertEquals(true,Util.almostEqual(10.44,sd1.distanceTo(sd2,SpatialData.DistanceMetric.EUCLIDEAN),0.01));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(-3,-10),"");
		Assert.assertEquals(true,Util.almostEqual(10.44,sd1.distanceTo(sd2.getLocation(),SpatialData.DistanceMetric.EUCLIDEAN),0.01));
	}
	
	
	@Test
	void testInfinityNormDistanceTo() {
		SpatialData sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		SpatialData sd2 = new SpatialData(0,new Point2D.Double(0,10),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(10,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(-10,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,-10),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(10,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(-10,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,-10),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,10),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,0),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(0,0),"");
		Assert.assertEquals(true,Util.almostEqual(0,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(10,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(10,0),"");
		Assert.assertEquals(true,Util.almostEqual(0,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));
		
		sd1 = new SpatialData(0,new Point2D.Double(10,10),"");
		sd2 = new SpatialData(0,new Point2D.Double(10,10),"");
		Assert.assertEquals(true,Util.almostEqual(0,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),DOUBLE_EQUALITY_EPSILON));

		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(3,10),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),0.01));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(-3,-10),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),0.01));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(100,100),"");
		sd2 = new SpatialData(0,new Point2D.Double(103,110),"");
		Assert.assertEquals(true,Util.almostEqual(10,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),0.01));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,100),"");
		sd2 = new SpatialData(0,new Point2D.Double(103,110),"");
		Assert.assertEquals(true,Util.almostEqual(103,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),0.01));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,100),"");
		sd2 = new SpatialData(0,new Point2D.Double(-103,110),"");
		Assert.assertEquals(true,Util.almostEqual(103,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),0.01));
		
		sd1 = new SpatialData(0,new Point2D.Double(100,0),"");
		sd2 = new SpatialData(0,new Point2D.Double(110,-103),"");
		Assert.assertEquals(true,Util.almostEqual(103,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),0.01));
		
		sd1 = new SpatialData(0,new Point2D.Double(1,2),"");
		sd2 = new SpatialData(0,new Point2D.Double(5,8),"");
		Assert.assertEquals(true,Util.almostEqual(6,sd1.distanceTo(sd2,SpatialData.DistanceMetric.INFINITY_NORM),0.01));
		
		sd1 = new SpatialData(0,new Point2D.Double(1,2),"");
		sd2 = new SpatialData(0,new Point2D.Double(5,8),"");
		Assert.assertEquals(true,Util.almostEqual(6,sd1.distanceTo(sd2.getLocation(),SpatialData.DistanceMetric.INFINITY_NORM),0.01));
		
	}
	/*@Test
	void testHaversinDistanceTo() {
		fail("haven't fixed library issues yet.");
	}*/
	@Test
	void testCompareTo_pair() {
		
		/* * @return 0 when these points share the same coordinate. -1 when this point's  X coordinate is smaller, +1 when this point's x coordinate is larger. 
		In case when both points share the same x coordinate, -1 when this point's  Y coordinate is smaller, +1 when this point's y coordinate is larger.
		*/
		SpatialData sd1 = new SpatialData(0,new Point2D.Double(0,0),null);
		SpatialData sd2 = new SpatialData(0,new Point2D.Double(0,0),null);
		
		Assert.assertEquals(0,sd1.compareTo(sd2));
		Assert.assertEquals(0,sd2.compareTo(sd1));
		
		Assert.assertEquals(0,sd1.compareTo(sd1));
		Assert.assertEquals(0,sd2.compareTo(sd2));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),null);
		sd2 = new SpatialData(1,new Point2D.Double(0,0),null);
		
		Assert.assertEquals(0,sd1.compareTo(sd2));
		Assert.assertEquals(0,sd2.compareTo(sd1));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),null);
		sd2 = new SpatialData(1,new Point2D.Double(0,0),"hello");
		
		Assert.assertEquals(0,sd1.compareTo(sd2));
		Assert.assertEquals(0,sd2.compareTo(sd1));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),null);
		sd2 = new SpatialData(0,new Point2D.Double(10,0),null);
		
		Assert.assertEquals(-1,sd1.compareTo(sd2));
		Assert.assertEquals(1,sd2.compareTo(sd1));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(10,0),null);
		sd2 = new SpatialData(0,new Point2D.Double(0,0),null);
		
		Assert.assertEquals(-1,sd2.compareTo(sd1));
		Assert.assertEquals(1,sd1.compareTo(sd2));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(-1,0),null);
		sd2 = new SpatialData(0,new Point2D.Double(0,0),null);
		
		Assert.assertEquals(-1,sd1.compareTo(sd2));
		Assert.assertEquals(1,sd2.compareTo(sd1));
		
		sd1 = new SpatialData(0,new Point2D.Double(-1.5,0),null);
		sd2 = new SpatialData(0,new Point2D.Double(0,0),null);
		
		Assert.assertEquals(-1,sd1.compareTo(sd2));
		Assert.assertEquals(1,sd2.compareTo(sd1));
		
		sd1 = new SpatialData(0,new Point2D.Double(-1.5,0),null);
		sd2 = new SpatialData(0,new Point2D.Double(-1.4,0),null);
		
		Assert.assertEquals(-1,sd1.compareTo(sd2));
		Assert.assertEquals(1,sd2.compareTo(sd1));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(0,-10),null);
		sd2 = new SpatialData(0,new Point2D.Double(0,10),null);
		
		Assert.assertEquals(-1,sd1.compareTo(sd2));
		Assert.assertEquals(1,sd2.compareTo(sd1));
		
		sd1 = new SpatialData(0,new Point2D.Double(0,0),null);
		sd2 = new SpatialData(0,new Point2D.Double(0,-10),null);
		
		Assert.assertEquals(-1,sd2.compareTo(sd1));
		Assert.assertEquals(1,sd1.compareTo(sd2));
		
		

		sd1 = new SpatialData(0,new Point2D.Double(0,50),null);
		sd2 = new SpatialData(0,new Point2D.Double(10,60),null);
		
		Assert.assertEquals(-1,sd1.compareTo(sd2));
		Assert.assertEquals(1,sd2.compareTo(sd1));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(10,-10),null);
		sd2 = new SpatialData(0,new Point2D.Double(0,-100),null);
		
		Assert.assertEquals(-1,sd2.compareTo(sd1));
		Assert.assertEquals(1,sd1.compareTo(sd2));
		
		
		sd1 = new SpatialData(0,new Point2D.Double(-1,22.5),null);
		sd2 = new SpatialData(0,new Point2D.Double(0,11.123),null);
		
		Assert.assertEquals(-1,sd1.compareTo(sd2));
		Assert.assertEquals(1,sd2.compareTo(sd1));
		
		sd1 = new SpatialData(0,new Point2D.Double(-1.5,123.456),null);
		sd2 = new SpatialData(0,new Point2D.Double(0,123.456),null);
		
		Assert.assertEquals(-1,sd1.compareTo(sd2));
		Assert.assertEquals(1,sd2.compareTo(sd1));
		
		sd1 = new SpatialData(0,new Point2D.Double(-1.5,50),null);
		sd2 = new SpatialData(0,new Point2D.Double(-1.4,-50),null);
		
		Assert.assertEquals(-1,sd1.compareTo(sd2));
		Assert.assertEquals(1,sd2.compareTo(sd1));
	}

	@Test
	void testCompareTo_badarguments() {
		
		//public SpatialData(int id, Point2D location, String attributes)
		SpatialData sd = new SpatialData(0,new Point2D.Double(0,0),"0,0");		
		
		boolean exceptionOccured = false;
		
				
		try {
			int res = sd.compareTo(null);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		
		
		Assert.assertEquals(true,exceptionOccured);
	
	}
	@Test
	void testToString() {
	
	
		SpatialData sd = null;
		
		//public SpatialData(int id, Point2D location, String attributes)
		sd = new SpatialData(0,new Point2D.Double(0,0),"0,0");
		//id,x_coord,y_coord,attributes
		Assert.assertEquals("0,0.0,0.0,0,0",sd.toString());
		
		sd = new SpatialData(10,new Point2D.Double(1,2),"hello");
		Assert.assertEquals("10,1.0,2.0,hello",sd.toString());
		
		sd = new SpatialData(5000,new Point2D.Double(1.55,53.123),"1,2,3");
		Assert.assertEquals("5000,1.55,53.123,1,2,3",sd.toString());
		
		sd = new SpatialData(5000,new Point2D.Double(1.55,53.123),null);
		Assert.assertEquals("5000,1.55,53.123,null",sd.toString());
		
		sd = new SpatialData(5000,new Point2D.Double(-1.55,0.53),null);
		Assert.assertEquals("5000,-1.55,0.53,null",sd.toString());
	}
	
	@Test
	void testParseAttributesToDoubleArray() {
		SpatialData sd = new SpatialData(0,new Point2D.Double(0,0),null);
		
		double [] res = sd.parseAttributesToDoubleArray(SpatialData.COMMA);
		Assert.assertEquals(null,res);
		
		sd.setAttributes("");
		
		res = sd.parseAttributesToDoubleArray(SpatialData.COMMA);
								
		Assert.assertEquals(0, res.length);
		
		
		sd.setAttributes("1.0");
		
		res = sd.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		Assert.assertEquals(1, res.length);
		
		Assert.assertEquals(true,Util.almostEqual(1.0, res[0],DOUBLE_EQUALITY_EPSILON));
		
		
		sd.setAttributes("1.0,2.0");
		
		res = sd.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		Assert.assertEquals(2, res.length);
		
		Assert.assertEquals(true,Util.almostEqual(1.0, res[0],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(2.0, res[1],DOUBLE_EQUALITY_EPSILON));
		
		sd.setAttributes("1.0,2.0,-4");
		
		res = sd.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		Assert.assertEquals(3, res.length);
		
		Assert.assertEquals(true,Util.almostEqual(1.0, res[0],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(2.0, res[1],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(-4.0, res[2],DOUBLE_EQUALITY_EPSILON));
		
		sd.setAttributes("1.0,2.0,-4,3.1415");
		
		res = sd.parseAttributesToDoubleArray(SpatialData.COMMA);
		
		Assert.assertEquals(4, res.length);
		
		Assert.assertEquals(true,Util.almostEqual(1.0, res[0],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(2.0, res[1],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(-4.0, res[2],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(3.1415, res[3],DOUBLE_EQUALITY_EPSILON));
		 
		
		sd._parseAttributesToDoubleArray(res,SpatialData.COMMA);
		
		Assert.assertEquals(4, res.length);
		
		Assert.assertEquals(true,Util.almostEqual(1.0, res[0],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(2.0, res[1],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(-4.0, res[2],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(3.1415, res[3],DOUBLE_EQUALITY_EPSILON));
		
		res = new double[10];
		
		sd._parseAttributesToDoubleArray(res,SpatialData.COMMA);
		
		Assert.assertEquals(10, res.length);
		
		Assert.assertEquals(true,Util.almostEqual(1.0, res[0],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(2.0, res[1],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(-4.0, res[2],DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(3.1415, res[3],DOUBLE_EQUALITY_EPSILON));
		
	}
	
	@Test
	void testParseAttributesToDoubleArray_badargs() {
		SpatialData sd = new SpatialData(0,new Point2D.Double(0,0),null);
		
		
		
		
		
		
		
		
		boolean exceptionOccured = false;
		
		
		try {
			sd.setAttributes("");
			sd._parseAttributesToDoubleArray(null,SpatialData.COMMA);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		
		
		Assert.assertEquals(true,exceptionOccured);
	
		
		exceptionOccured = false;
		
		
		try {
			double [] res = new double[0];
			sd.setAttributes("1,2");
			sd._parseAttributesToDoubleArray(res,SpatialData.COMMA);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		
		
		Assert.assertEquals(true,exceptionOccured);
		
		

		exceptionOccured = false;
		
		
		try {
			double [] res = new double[0];
			sd.setAttributes("1");
			sd._parseAttributesToDoubleArray(res,SpatialData.COMMA);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		
		
		Assert.assertEquals(true,exceptionOccured);
		

		
	}
	
	
	@Test
	void testRadiusOverride() {
		SpatialData sd = new SpatialData(0,new Point2D.Double(0,0),null);
		Assert.assertEquals(sd.NON_OVERRIDED_NEIGHBORHOOD_SEARCH_RADIUS,sd.getOverrideNeighborhoodSearchRadius(),0.0001);
		
		sd.overrideNeighborhoodSearchRadius(0);
		Assert.assertEquals(0.0,sd.getOverrideNeighborhoodSearchRadius(),0.0001);
		
		sd.overrideNeighborhoodSearchRadius(1.55);
		Assert.assertEquals(1.55,sd.getOverrideNeighborhoodSearchRadius(),0.0001);
		
		sd.overrideNeighborhoodSearchRadius(1000);
		Assert.assertEquals(1000,sd.getOverrideNeighborhoodSearchRadius(),0.0001);
		
		sd.clearNeighborhoodSearchRadiusOverride();
		Assert.assertEquals(sd.NON_OVERRIDED_NEIGHBORHOOD_SEARCH_RADIUS,sd.getOverrideNeighborhoodSearchRadius(),0.0001);
	}
	

	
	@Test
	void testRadiusOverride_bad_arg() {
		SpatialData sd = new SpatialData(0,new Point2D.Double(0,0),null);
		
		
		
		
		boolean exceptionOccured = false;
		
		
		try {
			
			sd.overrideNeighborhoodSearchRadius(-1000);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		
		
		Assert.assertEquals(true,exceptionOccured);
		
		exceptionOccured = false;
		
		
		try {
			
			sd.overrideNeighborhoodSearchRadius(-1);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}
		
		
		Assert.assertEquals(true,exceptionOccured);
	
	}
	
	@Test
	void testAngleTo() {
		SpatialData pt1 = new SpatialData(0,new Point2D.Double(0,0),null);
		SpatialData pt2 = new SpatialData(0,new Point2D.Double(1,0),null);
		
		Assert.assertEquals(0.0,pt1.angleTo(pt2), DOUBLE_EQUALITY_EPSILON);
		
		pt1 = new SpatialData(0,new Point2D.Double(0,0),null);
		pt2 = new SpatialData(0,new Point2D.Double(1,1),null);
		
		Assert.assertEquals(45,pt1.angleTo(pt2), DOUBLE_EQUALITY_EPSILON);
		
		pt1 = new SpatialData(0,new Point2D.Double(0,0),null);
		pt2 = new SpatialData(0,new Point2D.Double(0,1),null);
		
		Assert.assertEquals(90,pt1.angleTo(pt2), DOUBLE_EQUALITY_EPSILON);
		
		pt1 = new SpatialData(0,new Point2D.Double(0,0),null);
		pt2 = new SpatialData(0,new Point2D.Double(-1,0),null);
		
		Assert.assertEquals(180,pt1.angleTo(pt2), DOUBLE_EQUALITY_EPSILON);
		
		pt1 = new SpatialData(0,new Point2D.Double(0,0),null);
		pt2 = new SpatialData(0,new Point2D.Double(0,-1),null);
		
		Assert.assertEquals(270,pt1.angleTo(pt2), DOUBLE_EQUALITY_EPSILON);
	}
}

