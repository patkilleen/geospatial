package core;

import java.awt.geom.Point2D;

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
}
	
		