package data_structure;

import java.awt.geom.Point2D;

public class BoundingBox {

	private Point2D minCoords;
	private Point2D maxCoords;
	
	/**
	 * Creates a bounding box. The maximum coordinates must be equal or larger to minimum coordinates
	 * @param minX minimum x coordinate of box 
	 * @param minY minimum y coordinate of box
	 * @param maxX maximum x coordinate of box
	 * @param maxY maximum y coordinate of box
	 */
	public BoundingBox(double minX, double minY, double maxX, double maxY) {
		init(new Point2D.Double(minX,minY),new Point2D.Double(maxX,maxY));
	}
	
	
	/**
	 * Creates a bounding box. The maximum coordinates must be equal or larger to minimum coordinates
	 * @param minPt the coordinate with minimum x and y values
	 * @param maxPt the coordinate with maximum x and y values
	 */
	public BoundingBox(Point2D minPt, Point2D maxPt) {
		
		init(minPt,maxPt);
		///init(minPt.getX(),minPt.getY(),maxPt.getX(),maxPt.getY());
	}
	
	private void init(Point2D minPt, Point2D maxPt) {
		
		validityCheck(minPt,maxPt);
		minCoords = minPt;
		maxCoords = maxPt;
		
		
	}
	
	
	/**
	 * Makes sure the given points meet the requirements of max being greater than min and no null points
	 * @param minPt the coordinate with minimum x and y values
	 * @param maxPt the coordinate with maximum x and y values
	 * 
	 * @throws IllegalArgumentException thrown when validity check failed on arguments
	 */
	private static void validityCheck(Point2D minPt, Point2D maxPt) {
		if(minPt== null || maxPt == null) {
			throw new IllegalArgumentException("cannot create bounding box, null coordinates");
		}
		if((maxPt.getX() < minPt.getX()) || (maxPt.getY() < minPt.getY())){
			throw new IllegalArgumentException("cannot create bounding box. Bounding box incorrectly defined (some maximum x or y coordinate smaller than minimum coordinates)");
		}
	}
	public Point2D getMinCoords() {
		return minCoords;
	}
	public void setMinCoords(Point2D minPt) {
		
		validityCheck(minPt,maxCoords);
		this.minCoords = minPt;
	}
	
	public void setMinCoords(double x, double y) {
		
		Point2D newMinCoords = new Point2D.Double(x,y);
		validityCheck(newMinCoords,maxCoords);
		minCoords = newMinCoords;
		
	}
	
	public Point2D getMaxCoords() {
		return maxCoords;
	}
	public void setMaxCoords(Point2D maxPt) {
		
		validityCheck(minCoords,maxPt);
		
		this.maxCoords = maxPt;
	}
	
	public void setMaxCoords(double x, double y) {
		
		Point2D newMaxCoords = new Point2D.Double(x,y);
		validityCheck(minCoords,newMaxCoords);
		maxCoords = newMaxCoords;
	}
	

	

}
