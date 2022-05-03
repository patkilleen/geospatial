package data_structure;

import java.awt.geom.Point2D;

import org.apache.lucene.spatial.util.GeoDistanceUtils;

import core.Util;


public class SpatialData implements Comparable<SpatialData>{

	final public static double NON_OVERRIDED_NEIGHBORHOOD_SEARCH_RADIUS=-1.0;
	public enum DistanceMetric{
		EUCLIDEAN,
		INFINITY_NORM,//aka UNIRFORM norm, Chebyshev norm, max norm,  supremum  norm
		HAVERSIN,
	}
	
	static public final String COMMA = ","; 
	static public final String NULL_STRING = "null";
	private int id;
	private Point2D location;
	private String attributes;
	
	private double overridedRadius; 
	// flag to indicate if we should remember the double parsed attributes to avoid
	//re parsing every time
	private boolean enabledBufferedAttributes;
	private double [] bufferedParsedAttributes;
	
	public SpatialData(int id, Point2D location, String attributes) {
		
		super();
		
		if(location==null) {
			throw new IllegalArgumentException("location was null");
		}		
		
		//we don't hold a state (remember parsed attributes) by default
		enabledBufferedAttributes = false;
		bufferedParsedAttributes = null;
		
		this.id = id;
		this.location = location;
		this.attributes = attributes;
		overridedRadius=NON_OVERRIDED_NEIGHBORHOOD_SEARCH_RADIUS;
	}
		
	
	public Point2D getLocation() {
		return location;
	}
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void setLocation(Point2D location) {
		if(location==null) {
			throw new IllegalArgumentException("location was null");
		}
		this.location = location;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {

		this.attributes = attributes;
	}


	public void enableAttributeParsingBuffering() {
		enabledBufferedAttributes=true;
	}
	
	public void disableAttributeParsingBuffering() {
		enabledBufferedAttributes=false;
	}

	/**
	 * Computes the distance to point depending on coordinate reference system.
	 * When longlat = true: haversine distance used (WGT84 or w/e degrees)
	 * When longlat = false: euclidean distances useds (UTM in meters)
	 * @param that point to compute distance to
	 * @param distMe the distance metric to use to measure the distance between points
	 * @return distance from this point to that point
	 */
	public double distanceTo(SpatialData that,DistanceMetric distMe){
		if(that==null) {
			throw new IllegalArgumentException("location was null when comparing point distances");
		}
		
		return  distanceTo(that.getLocation(),distMe);
	}
	/**
	 * Computes the distance to point depending on coordinate reference system.
	 * When longlat = true: haversine distance used (WGT84 or w/e degrees)
	 * When longlat = false: euclidean distances useds (UTM in meters)
	 * @param that point to compute distance to
	 * @param distMe the distance metric to use to measure the distance between points
	 * @return distance from this point to that point
	 */
	public double distanceTo(Point2D that,DistanceMetric distMe){
		return Util.distanceTo(this.getLocation(),that,distMe);
	
	}

	
	
	/**
	 * compares two points to enable sorting points
	 * @param that point to compare to
	 * @return 0 when these points share the same coordinate. -1 when this point's  X coordinate is smaller, +1 when this point's x coordinate is larger. In case when both points share the same x coordinate, -1 when this point's  Y coordinate is smaller, +1 when this point's y coordinate is larger.
	 */
	@Override
	public int compareTo(SpatialData that) {
		if(that==null) {
			throw new IllegalArgumentException("location was null when comparing points");
		}
		
		if(that == this) {
			return 0;
		}
		
		//sort by longitude/easting first
		if (this.location.getX() < that.location.getX()) return -1;
        if (this.location.getX() > that.location.getX()) return +1;
        //then poitns with same long/east sorted by latitude
		if (this.location.getY() < that.location.getY()) return -1;
        if (this.location.getY() > that.location.getY()) return +1;
        
        return 0;
	}

	
	
	/**
	 * Converts the object to string in CSV format:  id,x_coord,y_coord,attributes
	 * @return string with following format: "id,x_coord,y_coord,attributes"
	 */
	@Override
	public String toString(){
		
		//as the attributes are assumed to be coma seperate attrributes, to string is the attributes appended to CSV-coordinates
		//null is output if attribute string is null
		return this.id+COMMA+this.location.getX()+COMMA+this.location.getY()+COMMA+(this.attributes==null?NULL_STRING:attributes);
		
	}
	
	
	/**
	 * Converts the object to string in CSV format:  id,x_coord,y_coord,attributes
	 * @param sep seperator string to deliniate CSV attributes 
	 * @return string with following format: "id,x_coord,y_coord,attributes"
	 */
	public String toCSVString(String sep){
		
		//as the attributes are assumed to be coma seperate attrributes, to string is the attributes appended to CSV-coordinates
		//null is output if attribute string is null
		return this.id+sep+_toCSVString(sep);
		
	}
	
	/**
	 * Converts the object to string in CSV format without the id:  x_coord,y_coord,attributes
	 * @param sep seperator string to deliniate CSV attributes 
	 * @return string with following format: ",x_coord,y_coord,attributes"
	 */
	public String _toCSVString(String sep){
		
		//as the attributes are assumed to be coma seperate attrributes, to string is the attributes appended to CSV-coordinates
		//null is output if attribute string is null
		return this.location.getX()+sep+this.location.getY()+sep+(this.attributes==null?NULL_STRING:attributes);
		
	}
	
	/**
	 * Parses the attributes (numbers seperated by commands) to a double array.
	 * 
	 * Affected by enabledBufferedAttributes: where when enabledBufferedAttributes is true, calling this
	 * will store a state in this object that saves a reference to returned array that was created
	 * returning same every every subsequent call. when enabledBufferedAttributes is false, a new array is created each call
	 * 
	 * @param sep seperator string to deliniate CSV attributes
	 * @return Double array
	 */
	public double [] parseAttributesToDoubleArray(String sep) {
		//did we enabled buffereding the parsed attributes?
		if(enabledBufferedAttributes) {
			
			//not the first tiem parsing? (ie we have a result from last time called?
			if (bufferedParsedAttributes != null) {
				return  bufferedParsedAttributes;
			}
		}
						
		
		if(attributes== null) {
			return null;
		}
		if(attributes.isEmpty()) {
			return new double[0];
		}
		//parse the attribute into tokens seperate by comma. parse each mean to double
		String []tmpTokens = attributes.split(sep);
		
		double [] res = new double[tmpTokens.length];
		
		_parseAttributesToDoubleArray(res,sep);
		
		//did we enabled buffereding the parsed attributes?
		if(enabledBufferedAttributes) {
			//first tiem parsing? (
			if (bufferedParsedAttributes == null) {
				
				//save the result
				bufferedParsedAttributes=res;
			}
		}
		return res;
		
	}
	
	/**
	 * Will fille (or partially fill) parsed attribute values into a buffer
	 * Not affected by enabledBufferedAttributes: the calling function managers its own memory
	 * @param outputBuffer buffer to store parsed attribute values that needs to be big enought to store all values
	 * @param sep seperator string to deliniate CSV attributes
	 */
	public void _parseAttributesToDoubleArray(double [] outputBuffer,String sep) {
		if(outputBuffer == null) {
			throw new IllegalArgumentException("double output buffer is null, can't convert attributes to doubles");
		}
					
		
		//parse the attribute into tokens seperate by comma. parse each mean to double
		String []tmpTokens = attributes.split(sep);
		
		if(tmpTokens.length > outputBuffer.length) {
			throw new IllegalArgumentException("double output buffer not large engouth  to store all attribute values");
		}
		
		//iterate over every attribute seperated by comma
		for(int i = 0 ; i < tmpTokens.length;i++) {
			
			//parse the attribute to Double
			String attributeiStr =tmpTokens[i];
			outputBuffer[i] = Double.parseDouble(attributeiStr);
		}
		
	}
	
	/**
	 * Pasrses the attribute and returns the string value
	 * @param attributeIx attribute index
	 * @param sep seperator
	 * @return the value at given index of attribute string
	 */
	public String getAttributeStringValue(int attributeIx,String sep) {
		
	
		String [] tokens = attributes.split(sep);
		
		return tokens[attributeIx];
	}
	
	
	/**
	 * returns the angle between the given point and this point
	 * @param target given point
	 * @return angle between points
	 */
	public double angleTo(SpatialData target) {
		
		
		
	    //double angle = Math.toDegrees(Math.atan2(target.location.getY() - location.getY(), target.location.getX() - location.getX()));
		return Util.angleTo(this.getLocation(),target.getLocation());

	   /*if(angle < 0){
	        angle += 360;
	    }

	    return angle;*/
	}
	
	/**
	 * Overrides the search radius
	 * @param r
	 */
	public void overrideNeighborhoodSearchRadius(double r) {
		if(r<0) {
			throw new IllegalArgumentException("exptect non-negative radius but was "+r);
		}
		overridedRadius=r;
	}
	
	/**
	 * Overrides the search radius
	 * @param r
	 */
	public void clearNeighborhoodSearchRadiusOverride() {
		overridedRadius=NON_OVERRIDED_NEIGHBORHOOD_SEARCH_RADIUS;
	}
	/**
	 * Returns a non-negative radius when this spatial point should
	 * have a point-specific search radius when used as nieghborhood center  
	 * 
	 * @return -1 when no radius is override, >= 0 when radius is overrided
	 */
	public double getOverrideNeighborhoodSearchRadius() {
		//to be override by subclassees
		return overridedRadius;
		
	}
}
