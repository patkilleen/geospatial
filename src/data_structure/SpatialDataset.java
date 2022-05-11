package data_structure;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SpatialDataset {

	private List<SpatialData> data;
	

	public SpatialDataset() {
		data = new ArrayList<SpatialData>();
	}
	public SpatialDataset(int capacity) {
		data = new ArrayList<SpatialData>(capacity);
	}
	
	public void addSpatialData(SpatialData datum){
		
		if(datum == null) {
			throw new IllegalArgumentException("can't add null spatial datapoint to dataset");
		}
		data.add(datum);
	}

	public SpatialData getSpatialData(int ix){
		
		if(ix < 0 || ix >= this.size()) {
			throw new IndexOutOfBoundsException("cannot access spatial data (of size "+this.size()+") at index "+ix+", index out of bounds");
		}
		return data.get(ix);
	}
	
	/**
	 * returns data list. shouldn't be used. Use the other api functiosn instead (required for testing)
	 * @return
	 */
	public List<SpatialData> getData(){
		return data;
	}
	
	public int size(){
		return data.size();
	}

	public void clear(){
		data.clear();
	}
	public Iterator<SpatialData> iterator(){
		return data.iterator();
	}
	

	/**
	 * Partions the dataset into a series of non-overlapping subsets
	 * The subsets are deepcopies, so manipulating the datasets won't affect this dataset (of course manipulating the spatialdata points
	 * will affect them as only the list of points is deepcopied, not the content of list)
	 * The smallest subset size is 1, so if the number of slices provided is larger than the number of elements, each elemenet of the dataset will be split into its own list
	 * 
	 * Example:
	 * example. suppose we want 13 slices from dataset of size 100. dividing 100 by 13 would lead to  subsets of size 7.69. This doesn't work
	 *		//since we would need an 8th subset to fill the 9 remaining (100 module 13 is 9, since 13 * 7 is 91). That means subsets need to be of size 7 (floor(7.69)
	 *	// and the last subset will not be filled (8*13 = 104, so the first 12 subsets of size 8 will lead to 96 elements,8 each, and the 4 remaining will be in 13th sbuset)
	 *	 * 
	 * @param desiredSubsetSize desired size of subsetst that dataset is bein broken into 
	 * @return list of spatail dataset subset
	 */
	public List<SpatialDataset> split(int desiredSubsetSize){
		
		//can't have 0 or negative number of subset.
		//smallest is 1, the dataset itself
		if(desiredSubsetSize <=0) {
			throw new IllegalArgumentException("cannot partition the spatial dataset into subsets of non positive size");
		}
		
		//empty dataset returns an empty list of subset
		if(this.size() == 0) {
			return new ArrayList<SpatialDataset>(0);
		}
		
		
		
		//subset sizes greater than num elements in list?
		if(desiredSubsetSize > this.size()) {
			desiredSubsetSize=this.size();
		}
		
		int numberSubsets = (int) Math.ceil(((double)this.size())/((double)desiredSubsetSize));
		
		List<SpatialDataset> res = new ArrayList<SpatialDataset>(numberSubsets);
		
		//easy case where the subset is this dataset itself (make deep copy, a new list)
		if(desiredSubsetSize==this.size()) {
			SpatialDataset copy = new SpatialDataset(this.size());
			
			Iterator<SpatialData> it = this.iterator();
			
			while(it.hasNext()) {
				SpatialData pt = it.next();
				copy.addSpatialData(pt);
			}
			
			res.add(copy);
			
			return res;
		}
		

		Iterator<SpatialData> it = this.iterator();
		
		int i = 1;
		int sliceIx = 0;
		
		int elementsAdded = 0;
		//size slice in ideal case is the size of dataset split evenly into desiredNumSlices exactly
		//but usual case will be desiredNumSlices doesn't divie the size exactly, so the lasat slice will have fewer entries in it
		//int sliceSize = (int) Math.ceil((double)this.size() / (double)desiredNumSlices);
		
		//next dataset to fill
		SpatialDataset slice = new SpatialDataset(desiredSubsetSize);
		while(it.hasNext()){
			
		
			SpatialData datum = it.next();
			slice.addSpatialData(datum);
			
			//next slice?
			if(i == desiredSubsetSize){
				
				res.add(slice);
				elementsAdded += slice.size();
				slice = new SpatialDataset(desiredSubsetSize);
				i=0;
			}
			
			i++;
		}
		
		//didn't get a chance to add the lasat slice?
		if(elementsAdded < this.size()){
			res.add(slice);
		}
		
		return res;
	}
	
	/**
	 * Sorts the dataset by location 
	 */
	public void sort(){
		Collections.sort(data);
	}
	
	/**
	 * Finds the maximum coordinates in the dataset and returns a Point2D where its
	 * x and y values contain the maximum values of the dataset's x and y coordinates, respectively. 
	 * @return point holding the maximum x and y values from the dataset or null if the dataset is empty
	 */
	public Point2D findMaxCoordinates() {
		
		if(this.size() ==0) {
			return null;
		}
		
		double max_X = -1 *Double.MAX_VALUE;
		double max_Y = -1 *Double.MAX_VALUE;
		
		Iterator<SpatialData> it = this.iterator();
		
		while(it.hasNext()) {
			SpatialData pt = it.next();
			
			Point2D coord = pt.getLocation();
			if(max_X < coord.getX()) {
				max_X=coord.getX();
			}
			if(max_Y < coord.getY()) {
				max_Y=coord.getY();
			}
			
		}
		
		return new Point2D.Double(max_X,max_Y);
	}
	
	/**
	 * Finds the minimum coordinates in the dataset and returns a Point2D where its
	 * x and y values contain the minimum values of the dataset's x and y coordinates, respectively. 
	 * @return point holding the minimum x and y values from the dataset or null if the dataset is empty
	 */
	public Point2D findMinCoordinates() {
		
		if(this.size() ==0) {
			return null;
		}
		
		double min_X = Double.MAX_VALUE;
		double min_Y = Double.MAX_VALUE;
		
		Iterator<SpatialData> it = this.iterator();
		
		while(it.hasNext()) {
			SpatialData pt = it.next();
			
			Point2D coord = pt.getLocation();
			if(min_X > coord.getX()) {
				min_X=coord.getX();
			}
			if(min_Y > coord.getY()) {
				min_Y=coord.getY();
			}
			
		}
		
		return new Point2D.Double(min_X,min_Y);
	}
	
	/**
	 * Computes the bounding box around the dataset 
	 * @return smallest box that covers all the poitns in dataset
	 */
	public BoundingBox computeBoundingBox() {
		

		if(this.size() ==0) {
			return null;
		}
		
		double min_X = Double.MAX_VALUE;
		double min_Y = Double.MAX_VALUE;
		
		double max_X = -1 *Double.MAX_VALUE;
		double max_Y = -1 *Double.MAX_VALUE;
		
		Iterator<SpatialData> it = this.iterator();
		
		while(it.hasNext()) {
			SpatialData pt = it.next();
			
			Point2D coord = pt.getLocation();
			if(min_X > coord.getX()) {
				min_X=coord.getX();
			}
			if(min_Y > coord.getY()) {
				min_Y=coord.getY();
			}
			if(max_X < coord.getX()) {
				max_X=coord.getX();
			}
			if(max_Y < coord.getY()) {
				max_Y=coord.getY();
			}
			
		}
		
		return new BoundingBox(min_X,min_Y,max_X,max_Y);
		
	}
	
	/**
	 * Returns the number of attributes points have in this dataset
	 * It is assumed that all points have the same number of attributes
	 * @param sep seperator character used to deliniate attribute values
	 * @return number of attributes (0 for empty dataset or empty attribute string)
	 */
	public int getNumberOfAttributes(String sep) {
		
		if(this.size()==0) {
			return 0;
		}
		String tmpAttributes = getSpatialData(0).getAttributes();
		
		if(tmpAttributes == null || tmpAttributes.isEmpty()) {
			return 0;
		}
		String []tmpTokens = tmpAttributes.split(sep);
		return tmpTokens.length;
		
	}
	
	/**
	 * Parses and returns the string attribute value of a point
	 * @param ptIx index of the point
	 * @param attributeIx index of the attribute
	 * @param sep seperator to parse CSV attributes
	 * @return the string value of the attribute
	 */
	public String getAttributeStringValue(int ptIx, int attributeIx,String sep) {
		double res = 0;
		SpatialData pt = this.getSpatialData(ptIx);

		return pt.getAttributeStringValue(attributeIx,sep);
		//String ptAtt = pt.getAttributes();
//		String [] tokens = ptAtt.split(sep);
		
	//	return tokens[attributeIx];
	}
	
	/**
	 * Parses and returns the attribute value of a point
	 * @param ptIx index of the point
	 * @param attributeIx index of the attribute
	 * @param sep seperator to parse CSV attributes
	 * @return the value of the attribute
	 */
	public double getAttributeValue(int ptIx, int attributeIx,String sep) {
		double res = 0;
		SpatialData pt = this.getSpatialData(ptIx);
		
		double []values = pt.parseAttributesToDoubleArray(sep);
		
		return values[attributeIx];
	}
	
	
	/**
	 * Parses and returns the attribute value of a point
	 * @param ptIx index of the point
	 * @param attributeIx index of the attribute
	 * @param sep seperator to parse CSV attributes
	 * @param buffer buffer to store the attribute parsing result temporarily to avoid reallocating buffer
	 * @return the value of the attribute
	 */
	public double _getAttributeValue(int ptIx, int attributeIx,String sep,double []buffer) {
		double res = 0;
		SpatialData pt = this.getSpatialData(ptIx);
		
		pt._parseAttributesToDoubleArray(buffer,sep);
		
		return buffer[attributeIx];
	}
	
	
	/**
	 * Slice the dataset vertically by returns the values for a single attribute for all points
	 * @param attributeIx the index of the attribute
	 * @param sep seperator string to split the CSV attributes
	 * @return list of attriute values for each point
	 */
	public List<Double> getAttributeValues(int attributeIx,String sep){
		List<Double> res = new ArrayList<Double>(this.size());
		
		for(int i = 0;i<this.size();i++) {
			double attValue = this.getAttributeValue(i, attributeIx, sep);
			res.add(attValue);
			
		}
		
		return res;
	}
	
	/**
	 * Slice the dataset vertically by returns the values for a single attribute for all points
	 * @param attributeIx the index of the attribute
	 * @param sep seperator string to split the CSV attributes
	 * @param outBuffer the list to store results in to avoid allocating another buffer
	 * @return list of attriute values for each point
	 */
	public void _getAttributeValues(int attributeIx,String sep,List<Double> outBuffer ){
		outBuffer.clear();
		for(int i = 0;i<this.size();i++) {
			double attValue = this.getAttributeValue(i, attributeIx, sep);
			outBuffer.add(attValue);
			
		}		
		
	}
	
	/**
	 * Converts this spatial dataset into a Polygon2D where each vertex is point in the dataset 
	 * and vertices are connected by edges by the  order the points are found in the dataset
	 * 
	 * 
	 * The last point in the dataset does not necessarily need to close the polygon's path.
	 * 
	 * Note that some precision in the coordinates may be lost as floating point
	 * coordinates are used.
	 * @return A polygon2D or <code>null</code> when the dataset is empty
	 */
	public Polygon2D toPolygon2D() {
		
		//empty dataset
		if(this.size()==0) {
			return null;
		}
		
		Iterator<SpatialData> it = this.iterator();
		
		Polygon2D res = new Polygon2D();
		
		while(it.hasNext()) {
			SpatialData data = it.next();
			Point2D pt = data.getLocation();
			
			res.addPoint(pt);
		}
		
		return res;
		
	}
}
