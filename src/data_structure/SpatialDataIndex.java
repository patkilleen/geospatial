package data_structure;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SpatialDataIndex {

	enum AxisType{
		LONG_EASTING,
		LAT_NORTHING
	};

	static final int DEFAULT_BUCKET_SIZE = 32;

	static final boolean LOWER_BOUND_SEARCH =true;
	static final boolean UPPER_BOUND_SEARCH =false;
	//stores all the points with a given esting/longitude coordinate buckets
	private HashMap<Double,SpatialDataset> coordinateMap;
	private SpatialDataset dataset;
	private HashMap<SpatialData,SpatialData> indexBuffer;

	
	
	private SpatialDataIndex(HashMap<Double,SpatialDataset> coordinateMap, SpatialDataset dataset,HashMap<SpatialData,SpatialData> indexBuffer) {
		this.coordinateMap = coordinateMap;
		this.dataset = dataset;
		this.indexBuffer = indexBuffer;
	}
	/**
	 * Places index on spatial dataset to make it easy to query for points in range
	 * Note, dataset is sorted (by easting/longitude, then by latitude/northing)to avoid duplicating data
	 * This function will sort the dataset given.
	 * @param _dataset
	 */
	public SpatialDataIndex(SpatialDataset _dataset) {
		
	
		
		if(_dataset == null) {
			throw new IllegalArgumentException("cannot create index on null data set");
		}
		
		if(_dataset.size() == 0) {
			throw new IllegalArgumentException("cannot create index on empty data set");
		}
		
	
		dataset = _dataset;


		this.indexBuffer = new HashMap<SpatialData,SpatialData>();
		
		dataset.sort();
		coordinateMap = new HashMap<Double,SpatialDataset>();
		//populate hashmap for easting/longitude
		Iterator<SpatialData> it = dataset.iterator();


		SpatialData datum = null;
		Point2D coord = null;
		Double eastLongKey = null;
		SpatialDataset bucket = null;
		//iteratre each datum
		while(it.hasNext()){
			datum = it.next();

			coord = datum.getLocation();

			eastLongKey = coord.getX();

			//eastin/long already in map
			if (coordinateMap.containsKey(eastLongKey)){
				//bucket alreay exists
				bucket = coordinateMap.get(eastLongKey);
			}else{

				//need create new buck
				bucket = new SpatialDataset(DEFAULT_BUCKET_SIZE);
				coordinateMap.put(eastLongKey, bucket);
			}

			//add the datum to bucket
			bucket.addSpatialData(datum);

		}//finished populating hashmap

		//now we sort each bucket by northing/latitude for binary search lookup after searching via hashmap
		//since each bucket has same easting/longitude, then sorting will thus sort by northing/latitude
		//
		Set<Double> keySet = coordinateMap.keySet();
		Iterator<Double> keyIt = keySet.iterator();
		while(keyIt.hasNext()){
			Double key = keyIt.next();

			bucket = coordinateMap.get(eastLongKey);

			bucket.sort();
			//System.out.println("bucket size: "+bucket.size());
		}
		
	//	System.out.println("index info");
		
		keySet = coordinateMap.keySet();
		
		//System.out.println("number buckets: "+keySet.size());
		
	}

	
	public void setIndexBuffer(HashMap<SpatialData, SpatialData> indexBuffer) {
		this.indexBuffer = indexBuffer;
	}

	/**
	 * Fetch all the points the fall inside a circular neighborhood using the Euclidean distance in an efficient manner.
	 * @param center center of the neighbordhood
	 * @param radius the maximum distance neighbors can have from the center to be included 
	 * @return the set of points found inside the neighborhood
	 */
	public SpatialDataset getSpatialDataInCircularNeighborhood(Point2D center, double radius) {
		
		SpatialDataset neighborhood = new SpatialDataset(dataset.size());
		SpatialDataset bbBuffer = new SpatialDataset(128);
		_getSpatialDataInCircularNeighborhood(center,radius,neighborhood,bbBuffer);
		 
		 return neighborhood;
	}

	/**
	 * Fetch all the points the fall inside a circular neighborhood using the Euclidean distance in an efficient manner.
	 * @param center center of the neighbordhood
	 * @param radius the maximum distance neighbors can have from the center to be included
	 * @param neighborhood output buffer containing the set of points found inside the neighborhood
	 * @param bbBuffer buffer to store temporary results from bounding box filtering 
	 */
	public void _getSpatialDataInCircularNeighborhood(Point2D center, double radius,SpatialDataset neighborhood,SpatialDataset bbBuffer) {

		if(center == null) {
			throw new IllegalArgumentException("can't compute circular neighbordhood. null center provided");
		}
		
		if(radius < 0.0) {
			throw new IllegalArgumentException("can't compute circular neighbordhood. non-negative radius required");
		}
		 
		
		Point2D minPt = new Point2D.Double(center.getX() - radius,center.getY()-radius);
		Point2D maxPt = new Point2D.Double(center.getX() + radius,center.getY()+radius);
		
		BoundingBox bb = new BoundingBox(minPt,maxPt);
		
		//since the neighborhood is circular, it won't perfectly contain a block of points.
		//filter the points that area outstide the smallest bounding box thhat encapsulates the circular radius (efficient)
		//before iterating over each point (brute force)
		SpatialDataset filteredBlock;
		if(bbBuffer == null) {
			filteredBlock = getSpatialDataInBoundingBox(bb);
		}else {
			bbBuffer.clear();
			//the bounding box buffer was given, so don't have to re create it
			_getSpatialDataInBoundingBox(bb,bbBuffer);
			filteredBlock =bbBuffer;
		}
		
		SpatialDataIndex._getSpatialDataInNeighborhood(filteredBlock,center,radius,SpatialData.DistanceMetric.EUCLIDEAN,neighborhood);
		//__getSpatialDataInCircularNeighborhood(center,radius,neighborhood,this.indexBuffer);
	}
	
	
	/**
	 * Fetch all the points the fall inside a block/square neighborhood in an efficient manner.
	 * @param center center of the neighbordhood/pixel/block
	 * @param squareWidth width/length of the block
	 * #return neighborhood the set of points found inside the neighborhood
	 */
	public SpatialDataset getSpatialDataInSquareNeighborhood(Point2D center, double squareWidth) {
		SpatialDataset neighborhood = new SpatialDataset(dataset.size());
		_getSpatialDataInSquareNeighborhood(center,squareWidth,neighborhood);
		 
		 return neighborhood;
	}
	/**
	 * Fetch all the points the fall inside a block/square neighborhood in an efficient manner.
	 * @param center center of the neighbordhood/pixel/block
	 * @param squareWidth width/length of the block
	 * @param neighborhood output buffer containing the set of points found inside the neighborhood
	 */
	public void _getSpatialDataInSquareNeighborhood(Point2D center, double squareWidth,SpatialDataset neighborhood) {
		

		if(center == null) {
			throw new IllegalArgumentException("can't compute circular neighbordhood. null center provided");
		}
		
		if(squareWidth < 0.0) {
			throw new IllegalArgumentException("can't compute circular neighbordhood. non-negative radius required");
		}
		double halfWidth = squareWidth/2.0;
		
		Point2D minPt = new Point2D.Double(center.getX() - halfWidth,center.getY()-halfWidth);
		Point2D maxPt = new Point2D.Double(center.getX() + halfWidth,center.getY()+halfWidth);
		
		BoundingBox bb = new BoundingBox(minPt,maxPt);

		_getSpatialDataInBoundingBox(bb,neighborhood);
		

		
		
	}
	/**
	 * Fetch all the points the fall inside a neighborhood using some user-defined distance metric
	 * by using a exaustive search approach.
	 * @param dataset the dataset to search through
	 * @param center center of the neighbordhood
	 * @param radius the maximum distance neighbors can have from the center to be included
	 * @param distMetric the user-defined distanced metric
	 * @return   set of points found inside the neighborhood
	 */
	public static SpatialDataset getSpatialDataInNeighborhood(SpatialDataset dataset, Point2D center, double radius,SpatialData.DistanceMetric distMetric) {
		if (dataset == null ) {
			throw new IllegalArgumentException("can't compute neighbordhood. null input dataset");
		}
		
		SpatialDataset neighborhood = new SpatialDataset(dataset.size());
		 _getSpatialDataInNeighborhood(dataset,center,radius,distMetric,neighborhood);
		 
		 return neighborhood;
	}
	/**
	 * Fetch all the points the fall inside a neighborhood using some user-defined distance metric
	 * by using a exaustive search approach.
	 * @param dataset the dataset to search through
	 * @param center center of the neighbordhood
	 * @param radius the maximum distance neighbors can have from the center to be included
	 * @param distMetric the user-defined distanced metric
	 * @param neighborhood output buffer containing the set of points found inside the neighborhood
	 * @return 
	 */
	public static void _getSpatialDataInNeighborhood(SpatialDataset dataset, Point2D center, double radius,SpatialData.DistanceMetric distMetric,SpatialDataset neighborhood) {
		
		if(center == null) {
			throw new IllegalArgumentException("can't compute neighbordhood. null center provided");
		}
		
		if(radius < 0.0) {
			throw new IllegalArgumentException("can't compute neighbordhood. non-negative radius required");
		}
		
		if(neighborhood == null) {
			throw new IllegalArgumentException("can't compute neighbordhood. null output buffer");
		}
		if (dataset == null ) {
			throw new IllegalArgumentException("can't compute neighbordhood. null input dataset");
		}
		
		neighborhood.clear();
		
		if(dataset.size() == 0) {
			return;
		}
		
		
		Iterator<SpatialData> it = dataset.iterator();
		
		
		//go over each point
		while(it.hasNext()){
			
			SpatialData pt = it.next();
			
			//does points fall into neigbordhoor?
			if(pt.distanceTo(center,distMetric) <= radius){
													
				neighborhood.addSpatialData(pt);
				
			}
	
		}//end looping candidates
		
	}
	/**
	 * Fetch all points found inside given bounding box using an efficient search. ignores memory management and creates new buffer
	 * @param min minimum coordinates of bounding box
	 * @param max maximum coordinates of bounding box
	 * @return points inside bouding box
	 */
	public SpatialDataset getSpatialDataInBoundingBox(BoundingBox bb){
		
		SpatialDataset res = new SpatialDataset(128);
			
		_getSpatialDataInBoundingBox(bb,res);
		
		return res;
	}

	/**
	 * Fetch all points found inside given bounding box using an efficient search and puts it into results buffer
	 * @param min minimum coordinates of bounding box
	 * @param max maximum coordinates of bounding box
	 * @param resultBuffer buffer to be used to avoid reallocating buffers if uneccessary  
	 */
	public void _getSpatialDataInBoundingBox(BoundingBox bb, SpatialDataset resultBuffer){
		__getSpatialDataInBoundingBox(bb,resultBuffer,null);//null for no black list. all points considered
	}
	
	/**
	 * Fetch all points found inside given bounding box using an efficient search and puts it into results buffer
	 * @param min minimum coordinates of bounding box
	 * @param max maximum coordinates of bounding box
	 * @param resultBuffer buffer to be used to avoid reallocating buffers if uneccessary  
	 * @param exclusionAreas areas represented as bounding boxes where points in the dataset are ignored if they fall inside these area. Null or empty list means no exclusion (it behaves exactly like _getSpatialDataInBoundingBox in this case)
	 */
	public void __getSpatialDataInBoundingBox(BoundingBox bb, SpatialDataset resultBuffer, List<BoundingBox> exclusionAreas){

		
		if(bb == null) {
			throw new IllegalArgumentException("cannot fetch data points in null boudning box");
		}
		Point2D min = bb.getMinCoords();
		Point2D max = bb.getMaxCoords();
		indexBuffer.clear();
		resultBuffer.clear();
		//get the longitude/easting index bounds for coordinates in desired range
		IndexPair longEastIxPair = _getSpatialPointIndicesInRange(min.getX(),max.getX(),dataset,AxisType.LONG_EASTING);
		
		//make sure long/easting points  exist inside range,
		if(longEastIxPair.indicesExist()){
			
			SpatialData outterCandidate = null;
			SpatialDataset candidates = null;
			IndexPair latNorthingIxPair = null;
			SpatialData innerCandidate = null;
			//iterate through the indices to check for outter candidates 
			for(int longEastIx = longEastIxPair.lowerIx; longEastIx <= longEastIxPair.upperIx;longEastIx++){
				
				outterCandidate = dataset.getSpatialData(longEastIx);
				
				//fetch the datset of all poitns with given longitude/eastig coordinate
				candidates = coordinateMap.get(outterCandidate.getLocation().getX());
				
				//find the boudning indices to only search trhough points that are possibly within range
				latNorthingIxPair = _getSpatialPointIndicesInRange(min.getY(),max.getY(),candidates,AxisType.LAT_NORTHING);
				
				
				//make sure lat/northing points  exist inside range
				if(latNorthingIxPair.indicesExist()){
					
					//iterate through all data points, they inside bounding box
					for(int latNorthIx = latNorthingIxPair.lowerIx; latNorthIx <= latNorthingIxPair.upperIx;latNorthIx++){
						
						innerCandidate =candidates.getSpatialData(latNorthIx);
						if(!indexBuffer.containsKey(innerCandidate)){
							indexBuffer.put(innerCandidate,innerCandidate);
						}
						
						
					}
					
				}
			}
			
		}
		
		//now make sure no duplicates were added by making indices unique
		Set<SpatialData> keySet = indexBuffer.keySet();
		Iterator<SpatialData> keyit = keySet.iterator();
		while(keyit.hasNext()){
			SpatialData datumInBoundary = keyit.next();
			
			//the areas to exclude points don't exist?
			if(exclusionAreas== null || exclusionAreas.size() ==0) {
				//add it unconditionally
				resultBuffer.addSpatialData(datumInBoundary);
			}else {
				
				boolean blacklisted=false;
				//we must make sure the point doesn't fall into any of the blacklist areas
				for(BoundingBox blackListedBB : exclusionAreas) {
					
					//point in a blacklisted area?
					if ((blackListedBB != null) && (blackListedBB.contains(datumInBoundary.getLocation()))) {
						blacklisted=true;
						break;
					}
				}
				
				//only add the point if it wasn't in a blacklisted area
				if(!blacklisted) {
					
					resultBuffer.addSpatialData(datumInBoundary);
				}
				
				
			}//end check for blackist areas
			
		}
		
		
		
		
	}


	/**
	 * Fetches a pair indices that represent the lower and upper bound indices of points within range in the sorted and indexed spatial dataset
	 * @param minValue minimum coordinate value of points
	 * @param maxValue maximum coordinate value of points
	 * @param type the axis type (either going to be longitude/easting or latitude/northing)
	 * @return first element is lowest index of points in range, 2nd elelement is upper bound index of points in range
	 */
	static private IndexPair _getSpatialPointIndicesInRange(double minValue, double maxValue, SpatialDataset sortedDataset,AxisType type){
		
		//this method is to just filter out datasets. for cases with 1, easy enough to simly check its distance without anything fancy
		if(sortedDataset.size() ==1){
			 new IndexPair(0,1);
		}
		IndexPair res = null;

		//find lower bound index
		int lowerix = findLowerBoundaryIx(minValue,sortedDataset,type);

		//find upper bound index
		int upperix = findUpperBoundaryIx(maxValue,sortedDataset,type);

		res = new IndexPair(lowerix,upperix);
		return res;
	}



	static private int findLowerBoundaryIx(double minValue,SpatialDataset sortedDataset,AxisType type){
		return findBoundaryIxHelper(minValue,0,sortedDataset.size()-1,LOWER_BOUND_SEARCH,sortedDataset,type);
	}

	static private int findUpperBoundaryIx(double maxValue,SpatialDataset sortedDataset,AxisType type){
		return findBoundaryIxHelper(maxValue,0,sortedDataset.size()-1,UPPER_BOUND_SEARCH,sortedDataset,type);
	}


	/*#lowerBoundaryFlag = true when lower boundary, and false when upper boundary
			#we assume the boundary value can split the sorted data
			#when lowerBoundaryFlag = TRUE, returns an element index j such that for all i in range(j,length(sortedData)),sortedData[i] >= boudaryValue, 
			#	and for all i in range(1,j-1) sortedData[i] < boudaryValue
			#when lowerBoundaryFlag = FALSE, returns an element index j such that or all i in range(1,j),sortedData[i] <= boudaryValue, 
			#	and for all i in range(j+1,length(sortedData)) sortedData[i] > boudaryValue
			#-1 is returned if the lower boundary is outside the dataset (all data is smaller than boundary )
			#-1 is returned if the upper boundary is outside the dataset (all data is larger than boundary )*/
	static private int findBoundaryIxHelper (double boudaryValue,int ixLeft, int ixRight, boolean lowerBoundaryFlag,SpatialDataset sortedDataset,AxisType type){
		
		
		if(sortedDataset == null || sortedDataset.size() ==0){
			System.out.println("warning, empty/null sorted spatial dataset (hashmap buckets not properly initialized or dataset given to index was empty)");
			return -1;
		}

		/*if(length(sortedData) == 0){
					return(-1)
				}*/
		int res = -1;

		//#BASE CASE (single cell, double cell, or the middle cell is the boundary)
		//#single cell:
		if(ixLeft == ixRight){


			if (lowerBoundaryFlag){
				
				
					//#boundary is right of this cell?
					if(getCoordinate(ixLeft,sortedDataset,type) < boudaryValue){
						return(ixLeft+1); //#elements from index (ixLeft+1) to length(sortedData)-1 are >= boundary value
					}
				
				//#cell is the value or is greater tha boundary, in any case #elements from index (ixLeft) to length(sortedData) are >= boundary value	
					return(ixLeft); 		

			}else{

			//	#boundary is left of this cell?
				if(getCoordinate(ixLeft,sortedDataset,type) > boudaryValue){
					return(ixLeft-1); //#elements from index 1 to (ixLeft-1) to length(sortedData) are <= boundary value
				}

				//#cell is the value or is smaller than boundary, in any case #elements from index 1 to (ixLeft) are <= boundary value	
				return(ixLeft); 		

			}


		//#double cell?
		}else if ((ixLeft+1) == ixRight ){

			//#finding left boundary ix?
			if (lowerBoundaryFlag){

				//#right cell smaller than boundary?
				if(getCoordinate(ixRight,sortedDataset,type) < boudaryValue){
					return(ixRight+1);// #all elements from index (ixRight+1) to length(sortedData) are >= boundary
				}

				//#right cell is the boundary
				if(getCoordinate(ixRight,sortedDataset,type) == boudaryValue){

					//#it may be the case that left cell is also same value
					if(getCoordinate(ixLeft,sortedDataset,type) == boudaryValue){
						return(ixLeft); //#all elements from index (ixLeft) (including ixRight) to length(sortedData) are >= boundary
					}else{
						return(ixRight);//#all elements from index (ixRight) (excluding ixLeft) to length(sortedData) are >= boundary
					}

				}
				//#it may be the case that the boundary is between these two cells
				if((getCoordinate(ixRight,sortedDataset,type) > boudaryValue) && (getCoordinate(ixLeft,sortedDataset,type) < boudaryValue)){
					return(ixRight);// #all elements from index (ixRight) (excluding ixLeft) to length(sortedData) are >= boundary
				}

				//#left cell is the boundary?
				if(getCoordinate(ixLeft,sortedDataset,type) >= boudaryValue){
					return(ixLeft);// #all elements from index (ixLeft) (including ixRight) to length(sortedData) are >= boundary
				}else{
					return(ixLeft-1);// #all elements from index (ixLeft-1) to length(sortedData) are >= boundary
				}
			}else{

				//#boundary is smaller than left cell?
				if(getCoordinate(ixLeft,sortedDataset,type) > boudaryValue){
					return(ixLeft-1);// #all elements from index 1 to ixLeft -1  are <= boundary
				}

				//#left cell is the boundary
				if(getCoordinate(ixLeft,sortedDataset,type) == boudaryValue){

					//#it may be the case that right cell is also same value
					if(getCoordinate(ixRight,sortedDataset,type) == boudaryValue){
						return(ixRight);// #all elements from index 1 to ixRight (including ixLeft) are <= boundary
					}else{
						return(ixLeft);// #all elements from index 1 to ixLeft are <= boundary (from ixRight onwards, it's > boundary)
					}

				}
				//#it may be the case that the boundary is between these two cells
				if((getCoordinate(ixLeft,sortedDataset,type) < boudaryValue) && (getCoordinate(ixRight,sortedDataset,type) > boudaryValue)){
					return(ixLeft);// #all elements from index 1 to ixLeft are <= boundary (from ixRight onwards, it's > boundary)
				}

				//#right cell is the boundary?
				if(getCoordinate(ixRight,sortedDataset,type) <= boudaryValue){
					return(ixRight);// #all elements from index 1 to ixRight (including ixLeft) are <= boundary
				}else{
					return(ixRight+1);// #all elements from index 1 to (iRight +1) are <= boundary 
				}

			}

		}

		//#middle ix computation between left and right index
		int ixMid = Math.floorDiv(ixRight - ixLeft,2) +ixLeft;

		//#does the boundary lie in the middle? 
		if(boudaryValue == getCoordinate(ixMid,sortedDataset,type)){
			//#have to search left and right, but luckily we can linearily search both sides 
			//#until a value changes, sicne the data is sorted (no need for recursion)

			return scanForBoundaryFromIndex(ixMid, lowerBoundaryFlag,sortedDataset,type);

		}//#end of case where the middle value is boundary value

		//#end BASE CASE

		if(boudaryValue < getCoordinate(ixMid,sortedDataset,type)){

			//#go search left
			res = findBoundaryIxHelper(boudaryValue,ixLeft, ixMid-1, lowerBoundaryFlag,sortedDataset,type);
		}else{
			//#go search right
			res = findBoundaryIxHelper(boudaryValue,ixMid+1,ixRight, lowerBoundaryFlag,sortedDataset,type);
		}

		//#boundary not inside sorted data
		if(res < 0 || res >= sortedDataset.size()){
			res = -1;
		}

		return res;


	}

	static private double getCoordinate(int pointIx,SpatialDataset sortedDataset,AxisType type){
		if(type==AxisType.LONG_EASTING){
			return sortedDataset.getSpatialData(pointIx).getLocation().getX();
		}else{//NORTHING/LATITUDE
			return sortedDataset.getSpatialData(pointIx).getLocation().getY();	
		}
		
	}
	
	
	/*#will scan left or right to find the smallest or largest index of a cell boundary
	#i is the idnex to start scanning from
	#e.g.: scan right boundary for 2 of elements [1 , 1 , 2 , 2, 2 ,3 ,4] would be  index 4 while scan left would be index 2 (boundary 3 would be index 5 for any scan direction)*/
	static private int scanForBoundaryFromIndex(int i, boolean scanLeftFlag,SpatialDataset sortedDataset,AxisType type){

		//#error checking
		
		/*if(is.null(sortedData) || length(sortedData) == 0){
			print("empty sorted ddata in boundary scan")
			return(-1)
		}*/
		
		if((i < 0) || (0 >= sortedDataset.size())){
			System.out.println("index out of bounds in scanning for boundaries");
			return -1;
		}
		
		
		double boudaryValue=  getCoordinate(i,sortedDataset,type);
		
		if(scanLeftFlag){
			i=i-1;// # by definition, we know i won't be -1 since i is atleast 1 since we in 3 cells or bigger case
			//#search left for smallest index boundary
			//#search until find boundary
			while((i >= 0) && getCoordinate(i,sortedDataset,type) == boudaryValue){
			
				i = i -1;
			}
			
			return(i+1);// #boundary ix 1 cell right of i
		}else{
			i=i+1;
			//#search right for largest index boundary
			
			
			int ixUpperBound =sortedDataset.size()-1;
			//#search right for largest index boundary
			//#search until find boundary
			while(i <= ixUpperBound  && getCoordinate(i,sortedDataset,type) == boudaryValue){
			
				i = i +1;
			}
			
			return i-1;// #boundary ix 1 cell left of i
		}
	}
	
	public SpatialDataIndex copy() {
		return new SpatialDataIndex(coordinateMap,dataset,indexBuffer); 
	}

}


