package aggregation;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;

import core.Util;
import data_structure.SpatialData;
import data_structure.SpatialDataIndex;
import data_structure.SpatialDataset;

/**
 * Method used in  M Spekken, A A Anselmi, and J P Molin. 2013. A simple method for filtering spatial data. 
 * In Precision agriculture ’13, John V Stafford (Ed.). Wageningen
Academic Publishers, 259–266. Presented at the 9th European Conference on Precision Agriculture.
to detect inlier in yield data

* outputs 2 attributes for each attribute: coeeficent of variation, and number of points where their neigbor exceed max CV (outliers/inlier are those where all their neighbors exceed maxCV)
* and 1 attribute, size of neighborhood
 */
public class InlierDetection {

	public InlierDetection() {

	}
	
	
	
	public static SpatialDataset performOutlierDetection(double maxCV,String sep, 
			SpatialDataset spatialData,
			SpatialData.DistanceMetric distMetric, 
			double neighborhoodRadius) {
		
		
		if(distMetric == SpatialData.DistanceMetric.INFINITY_NORM) {
			//we multiply by two since the sqaure neighbordhood function expects pixel width
			//but radisu is half the pixels' widht
			neighborhoodRadius = 2.0*neighborhoodRadius;
		}
		
		Iterator<SpatialData> it = spatialData.iterator();
		//go over all the points and make sure to enable buffering the attribute to double parsing 
		//to avoid parsing a point many times
		while(it.hasNext()) {
			SpatialData pt = it.next();
			pt.enableAttributeParsingBuffering();
		}
		
		SpatialDataIndex index = new SpatialDataIndex(spatialData);
		
		int numAttributes=spatialData.getNumberOfAttributes(sep);
		
		//PART1:
		InlierDetectionPart1 aggP1 = new InlierDetectionPart1(sep);
		//get the size of nieghbordhood and the coefficient of variation (CV) for each attribute
		SpatialDataset part1Res = _runPartX(maxCV,sep,spatialData,distMetric,neighborhoodRadius,index,aggP1);
		
		index = new SpatialDataIndex(part1Res);
		
		it = part1Res.iterator();
		//go over all the points and make sure to enable buffering the attribute to double parsing 
		//to avoid parsing a point many times
		while(it.hasNext()) {
			SpatialData pt = it.next();
			pt.enableAttributeParsingBuffering();
		}
		
		//the dataset should have same size as original, since were fusing it with itself. 
		//the only difference is that part1Res will have additional attributes
		
		InlierDetectionPart2 aggP2 = new InlierDetectionPart2(sep,maxCV,numAttributes);
		//run 2nd part
		return _runPartX(maxCV,sep,part1Res,distMetric,neighborhoodRadius,index,aggP2);
	}//end fuse datasetes
	
	
	public String createCSVOutputHeaderRow(String [] lowResAttributeHeaders,String [] highResAttributeHeaders,String sep) {
		
		String [] attributeHeaders;

		//in this case low and high res attributes the same
		if(lowResAttributeHeaders != null) {
			attributeHeaders = lowResAttributeHeaders;
		}else {
			attributeHeaders=highResAttributeHeaders;
		}
		
		//print all attributes

		String outputCSVHeader = "point-id";
		for(String lrAtt : attributeHeaders) {
			outputCSVHeader += sep;
			outputCSVHeader += lrAtt;
		}
		
	//we will now print columns in pairs of type for each attribute
		//skip first 2 coordinate header attributes
		for(int i = 2; i < attributeHeaders.length;i++) {
			
			
				outputCSVHeader += sep;
				outputCSVHeader = outputCSVHeader +sep+"neighborhodd_size_"+highResAttributeHeaders[i];								
			
		}
		
		
		//we will now print columns in pairs of type for each attribute
				//skip first 2 coordinate header attributes
				for(int i = 2; i < attributeHeaders.length;i++) {
					
					
						outputCSVHeader += sep;
						outputCSVHeader = outputCSVHeader +sep+"CV_"+highResAttributeHeaders[i];								
					
				}


				
				for(int i = 2; i < attributeHeaders.length;i++) {
					
					
					outputCSVHeader += sep;
					outputCSVHeader = outputCSVHeader +sep+"k_"+highResAttributeHeaders[i];								
				
			}
		
		return outputCSVHeader;
		
			
		
	}
	
	
	private static SpatialDataset _runPartX(double maxCV,String sep, 
			SpatialDataset spatialData,
			SpatialData.DistanceMetric distMetric, 
			double neighborhoodRadius,SpatialDataIndex index,SpatialDataAggregator agg) {
		
		
		SpatialDataset res = new SpatialDataset(spatialData.size());
		
		Iterator<SpatialData> it = spatialData.iterator();
		
		SpatialDataset neighborhood = new SpatialDataset(1024);
		SpatialDataset bbBuffer = new SpatialDataset(1024);//only required for circular neighborhoods
		
		
		SpatialDataset aggregationBuffer = new SpatialDataset(1024); 
		//iterate over all the low res poitns
		while(it.hasNext()) {
			
			SpatialData lowResPt = it.next();
			Point2D center = lowResPt.getLocation();
			
			//checks if the radius cshould be override by point center
			double radiusToApply = DataFusion.resolveSearchRadius(lowResPt,neighborhoodRadius,distMetric);
			
			//fetch the high res points around current point (form the neighbordhood)
			if(distMetric == SpatialData.DistanceMetric.EUCLIDEAN) {
				
				//create neighbordhood and store points in 'neighborhood' buffer
				index._getSpatialDataInCircularNeighborhood(center, radiusToApply, neighborhood,bbBuffer);
			}else if(distMetric == SpatialData.DistanceMetric.INFINITY_NORM) {
				//create neighbordhood and store points in 'neighborhood' buffer
				index._getSpatialDataInSquareNeighborhood(center, radiusToApply, neighborhood);
			}else {
				throw new IllegalArgumentException("distance metric not supported.");
			}
			
			
			//apply aggregator to neighborhood
			agg.applyAggregation(lowResPt, neighborhood, aggregationBuffer);
			
			//save all the aggregated points to result
			for(int i = 0; i < aggregationBuffer.size();i++) {
				SpatialData resultPoint =  aggregationBuffer.getSpatialData(i);
				
				
				//make sure to copy over the overrided raidus
				double overridedRad = lowResPt.getOverrideNeighborhoodSearchRadius();
				
				//did we override radius?
				if(overridedRad != SpatialData.NON_OVERRIDED_NEIGHBORHOOD_SEARCH_RADIUS) {
					
					//make sure to keep the same overide in resulting point
					resultPoint.overrideNeighborhoodSearchRadius(overridedRad);
				}
				res.addSpatialData(resultPoint);
			}
				
		}//end iterate low res poitns
		
		return res;
	}//end fuse datasetes
	



	/**
	 * Computes the coefficient of variation CV (std dev / mean) and size (number of poitns) of neighborhood 
	 * @author Not admin
	 *
	 */
	protected static class InlierDetectionPart1 extends FullNeighbordHoodAggregation {

		
		public InlierDetectionPart1(String sep) {
			super(sep,false);
			
			// TODO Auto-generated constructor stub
		}
	
		@Override
		protected String processEntireNeighborHood(SpatialData target,List<List<Double>> matrix, int numCol, int numRows) {
			 
					
	
				
			//first attribute is number of neighbors (num rows)
			String res = numRows+sep;
			//iterate over each column/attribute to compute the coefficient of determination
			for(int colIx = 0;colIx <numCol;colIx++) {

				List<Double> col = matrix.get(colIx);
				double mean = Stats.mean(col);
				double stddev = Stats.standard_deviation(col);
				double cv;
				
				//avoid divisions by 0
				if(mean == 0) {
					cv = Double.MAX_VALUE; //division by zero, it should be fine in this case to consider this infinity
				}else {
					cv = stddev/mean;
				}
				
				
				res = res + cv;
				
				//only add commma for non last attributes
				if( (colIx+1) <numCol) {
					res = res + sep;
				}
			}
			
			//format: number of neighbors, CV_1,CV_2,...CV_n, where CV_i is the CV of attribute i
			return res;
		}



	};
	
	/**
	 * Using the value computed from part 1 (CV and neighborhood size), computes a 3rd attribute, k = p' \in N(p), CV(p') > MaxCV)
	 * The number of points in the nieghoborhood of p that exceed MaxCV 
	 * @author Not admin
	 *
	 */
	protected static class InlierDetectionPart2 extends FullNeighbordHoodAggregation {

		private double maxCV;
		
		//original number of attributes before running part 1. this way we will be able to figure out
		//where the CV attributes are
		int numAttributes;
		int cvStartIx;
		public InlierDetectionPart2(String sep, double maxCV, int numAttributes) {
			super(sep,true);
			 this.maxCV=  maxCV;
			 this.numAttributes = numAttributes;
			/*
			 * so the attributes are copied over to fused result from part 1, (so yield, speed, etc),
			 * and part 1 appends neiborhood size and CV for each attribute.
			 * so +1 since add speed
			 */
			 cvStartIx=numAttributes+1;
			 
		}
	
	
		@Override
		protected String processEntireNeighborHood(SpatialData target,List<List<Double>> matrix, int numCol, int numRows) {
			
			
			String res ="";
			//iterate over each column/attribute to compute the coefficient of determination
			for(int colIx = cvStartIx;colIx <numCol;colIx++) {
				
				//number of neighbors that have their CV more than MaxCV
				int k = 0;
				List<Double> col = matrix.get(colIx);
				
				for(int rowIx = 0;rowIx<numRows;rowIx++) {
					double cv = col.get(rowIx);
					
					if (cv > maxCV) {
						k++;
					}
				}
				
				
				
				res = res + k;
				
				//only add commma for non last attributes
				if( (colIx+1) <numCol) {
					res = res + sep;
				}
			}
			
			//format: number of neighbors, CV_1,CV_2,...CV_n, k_1,k_2,...,k_n, where CV_i is the CV of attribute i, and k_i is the number of neighbors for this points that exceed maxCV for attribute i
			return target.getAttributes() + sep +res;
			
			//first attribute is number of neighbors (num rows)
		
		}



	};
	
	
	


}
