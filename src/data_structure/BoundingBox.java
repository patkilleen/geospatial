package data_structure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

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
	
	/**
	 * Returns true when this bounding box fully contains a given rectangle.
	 * The largest rectangle considered as contained inside the bounding box is
	 * the bounding box itself.
	 * @param r Rectangle2D to check if its inside this bounding box
	 * @return true when the rectangle is inside this bounding box, false otherwise
	 */
	public boolean contains(Rectangle2D r) {

		return 	(this.minCoords.getX() <= r.getMinX()) && //left border of r is the same or inside this BB
				(this.minCoords.getY() <= r.getMinY()) && //top border of r is the same or inside this BB
				(this.maxCoords.getX() >= r.getMaxX()) && //right border of r is the same or inside this BB
				(this.maxCoords.getY() >= r.getMaxY()); //bottom border of r is the same or inside this BB
	
		
	}
	

	/**
	 * Returns true when this bounding box  contains a poiont. A point inside the boundingbox can be on the border
	 * 
	 * @param pt Point2D to check if its inside this bounding box
	 * @return true when the point is inside this bounding box, false otherwise
	 */
	public boolean contains(Point2D pt) {

		return 	(this.minCoords.getX() <= pt.getX()) &&
				(this.minCoords.getY() <= pt.getY()) && 
				(this.maxCoords.getX() >= pt.getX()) && 
				(this.maxCoords.getY() >= pt.getY()); 
	}
	/**
	 * Returns true when this bounding box is fully contained by a given rectangle.
	 * The largest bounding box considered as contained inside the rectangle
	 * the rectangle itself.
	 * @param r Rectangle2D to check if this bounding box is inside it
	 * @return true when the boudning box is inside the rectangle, false otherwise
	 */
	public boolean isContainedBy(Rectangle2D r) {

		return 	(r.getMinX() <= this.minCoords.getX()  ) && //left border of r is the same or inside this BB
				(r.getMinY() <= this.minCoords.getY()) && //top border of r is the same or inside this BB
				(r.getMaxX() >= this.maxCoords.getX()) && //right border of r is the same or inside this BB
				(r.getMaxY() >= this.maxCoords.getY()); //bottom border of r is the same or inside this BB
	
		
	}
	
}
