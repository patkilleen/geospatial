package core;

import java.awt.geom.Point2D;

import org.apache.lucene.spatial.util.GeoDistanceUtils;

import data_structure.SpatialData;

public class Util {

	public Util() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Compares two floats for equality, given a margin of error
	 * @param a a number
	 * @param b a number
	 * @param epsilon precision of how close a and b need to be to be considered equal
	 * @return true when a and b are almost equal and false otherwise
	 */
	public static boolean almostEqual(double a, double b, double epsilon) {
		return Math.abs(a-b)<=epsilon;
	}
	
	/**
	 * Allocates memory for a new buffer, does nothing for buffer of desired size, and extends buffer if too small
	 * @param size the desired size of buffer
	 * @param buffer reference to (potentially previously allocated) buffer (null means we need create new instance)
	 * @return a buffer of desired size
	 */
	public  static double [] _allocateBuffer(int size,double [] buffer, boolean zeroMemory) {
		
		double [] res = null;
		//not allocated yet or not big enough?
		if(buffer== null || buffer.length < size) {
			res = new double[size];
			
		}else {
			res= buffer;//already have allocated a buffer of desired seize
		}
		
		if(zeroMemory) {
		//zero the parts of buffer that will be used
			for(int i =0;i<res.length;i++) {
				res[i]=0.0;
			}
		}
		
		return res;
	}
	
	
	public static double angleTo(Point2D from, Point2D to) {
		double angle = Math.toDegrees(Math.atan2(to.getY() - from.getY(), to.getX() - from.getX()));
		
		 if(angle < 0){
		        angle += 360;
		    }

		    return angle;
		
		
	}
	
	public static double distanceTo(Point2D from,Point2D to,SpatialData.DistanceMetric distMe){
		
		if(to==null || from == null) {
			throw new IllegalArgumentException("location was null when comparing point distances");
		}
		
		double res = 0.0;
		double deltaX=0;
		double deltaY=0;
		switch(distMe) {
		
			case EUCLIDEAN:
				//euclidean distance
				deltaY = from.getY()-to.getY();
				deltaX = from.getX()-to.getX();
				
				res = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
				break;
			case INFINITY_NORM:
				//infinity norm distance (basically it acts like checking if a point inside a block instead of inside a spehere)
				deltaY = Math.abs(from.getY()-to.getY());
				deltaX =  Math.abs(from.getX()-to.getX());
				
				res = Math.max(deltaY, deltaX);
				break;
			case HAVERSIN:
				//TODO: this call doesn't work since the library linking is having issues find binary class that implemtns the function
				//probably i library referenceing issue. But skeleton of code is herre for when library issues fixed
				res= GeoDistanceUtils.haversin(from.getY(), from.getX(),to.getY(), to.getX());
				break;
				
			default:
				throw new IllegalArgumentException("unknown distance metric"); 
		}
		
		return res;
		

	}

}
	
		