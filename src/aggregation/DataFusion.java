package aggregation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import data_structure.SpatialData;
import data_structure.SpatialDataIndex;
import data_structure.SpatialDataset;

public class DataFusion {

	public DataFusion() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * public enum DistanceMetric{
		EUCLIDEAN,
		INFINITY_NORM,//aka UNIRFORM norm, Chebyshev norm, max norm,  supremum  norm
	 */
	public static SpatialDataset fuseDatasets(String sep,SpatialDataset lowResPoints, 
			SpatialDataset highResPoints,
			SingleSummaryStatAggregator.SingleSummaryStat aggOp,
			SpatialData.DistanceMetric distMetric, 
			double neighborhoodRadius) {

		//build index over high res points
		SpatialDataIndex index = new SpatialDataIndex(highResPoints);
		return _fuseDatasets(sep,lowResPoints,highResPoints,aggOp,distMetric,neighborhoodRadius,index);
		
	}
	
	 
	public static SpatialDataset _fuseDatasets(String sep,SpatialDataset lowResPoints, 
			SpatialDataset highResPoints,
			SingleSummaryStatAggregator.SingleSummaryStat aggOp,
			SpatialData.DistanceMetric distMetric, 
			double neighborhoodRadius,SpatialDataIndex index) {
		
		
		if(distMetric == SpatialData.DistanceMetric.INFINITY_NORM) {
			//we multiply by two since the sqaure neighbordhood function expects pixel width
			//but radisu is half the pixels' widht
			neighborhoodRadius = 2.0*neighborhoodRadius;
		}
		
		Iterator<SpatialData> it = highResPoints.iterator();
		//go over all the points and make sure to enable buffering the attribute to double parsing 
		//to avoid parsing a point many times
		while(it.hasNext()) {
			SpatialData pt = it.next();
			pt.enableAttributeParsingBuffering();
		}
		
		
		//aggregator to apply to neighborhood
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(aggOp,sep);
		
		SpatialDataset res = new SpatialDataset(lowResPoints.size());
		
		it = lowResPoints.iterator();
		
		SpatialDataset neighborhood = new SpatialDataset(lowResPoints.size());
		SpatialDataset bbBuffer = new SpatialDataset(lowResPoints.size());//only required for circular neighborhoods
		
		
		SpatialDataset aggregationBuffer = new SpatialDataset(lowResPoints.size()); 
		//iterate over all the low res poitns
		while(it.hasNext()) {
			
			
			SpatialData lowResPt = it.next();
			
			//checks if the radius cshould be override by point center
			double radiusToApply = resolveSearchRadius(lowResPt,neighborhoodRadius,distMetric);
			
			Point2D center = lowResPt.getLocation();
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
				res.addSpatialData(resultPoint);
			}
				
		}//end iterate low res poitns
		
		return res;
	}//end fuse datasetes
	
	public static SpatialDataset multiThreaded_FuseDatasets(String sep, SpatialDataset lowResPoints, 
			SpatialDataset highResPoints,
			SingleSummaryStatAggregator.SingleSummaryStat aggOp,
			SpatialData.DistanceMetric distMetric, 
			double neighborhoodRadius, int numberOfThreads) {
		
		if (numberOfThreads <=0) {
			return null;
		}
		
		
		if (numberOfThreads ==1) {
			return DataFusion.fuseDatasets(sep,lowResPoints, highResPoints, aggOp, distMetric,neighborhoodRadius);
		}
		
		//build index over high res points
		SpatialDataIndex index = new SpatialDataIndex(highResPoints);
		
		double division = ((double)lowResPoints.size())  / ((double)numberOfThreads);
		//chunk sizes 
		int subsetSize = (int)Math.ceil(division);
		
		List<SpatialDataset> subsets = lowResPoints.split(subsetSize);
		
		//have each thread process niegibord hoods for a subset of poitns (threads will acess all the high res points)
		//to seperate the work
		List<DataFusionWorker> workers = new ArrayList<DataFusionWorker>(subsets.size());
		
		//create thread for each subset and get the tor un
		for(int i = 0;i<subsets.size();i++) {
			SpatialDataset subset = subsets.get(i);
			DataFusionWorker worker = new DataFusionWorker(index);
			workers.add(worker);
			
			worker.fuseDatasets(sep,subset, highResPoints,aggOp,distMetric, neighborhoodRadius);
			
		}
		
		SpatialDataset res = new SpatialDataset(lowResPoints.size());
		for(int i = 0;i<workers.size();i++) {
			
			DataFusionWorker worker = workers.get(i);
			
			SpatialDataset tmpResult = worker.pollResults();
			
			
			//iterattre over all points aggregated by worker
			Iterator<SpatialData> it =tmpResult.iterator();
			
			while(it.hasNext()) {
				SpatialData pt = it.next();
				
				res.addSpatialData(pt);
			}
			
		}//end ewait for worker
		
		return res;
	}
	
	/**
	 * Decides whether to apply globally the user-defined search radius or 
	 * use a data-defined raidus found in spatial data point acting as neighborhood center
	 * @param lowResPt spatial data point acting as neighborhood center
	 * @param neighborhoodRadius globally applied user-defined search radius
	 * @param distMetric distace metric used for searching
	 * @return radius to apply to search for neighborhood
	 */
	protected static double resolveSearchRadius(SpatialData lowResPt,double neighborhoodRadius,SpatialData.DistanceMetric distMetric) {
		double overridedRadius = lowResPt.getOverrideNeighborhoodSearchRadius();
		
		double radiusToApply=-1;
		
		//no radius override for this specific point center?
		if(overridedRadius == SpatialData.NON_OVERRIDED_NEIGHBORHOOD_SEARCH_RADIUS) {
			
			//use the parameter defined
			radiusToApply=neighborhoodRadius;
		}else {
			
			//theis point center defines it's own search radius
			
			
			if(distMetric == SpatialData.DistanceMetric.INFINITY_NORM) {
				//we multiply by two since the sqaure neighbordhood function expects pixel width
				//but radisu is half the pixels' widht
				radiusToApply = 2.0*overridedRadius;
			}else {
				radiusToApply = overridedRadius;
			}
			
		}
		
		return radiusToApply;
	}
	private static class DataFusionWorker implements Runnable{
		
		
		private SpatialDataset fusionResult;
		
		private Thread thread;
		
		SpatialDataset lowResPoints;
		SpatialDataset highResPoints;
		SingleSummaryStatAggregator.SingleSummaryStat aggOp;
		SpatialData.DistanceMetric distMetric;
		double neighborhoodRadius;
		SpatialDataIndex index;
		String sep;
		public DataFusionWorker(SpatialDataIndex index) {
			thread = new Thread(this);
			this.index = index.copy();//make a copy so the threads share ponly part of the the index (we compute index once, and create temporary buffers for each thread)
									
			//want to make sure each thread has it's own index buffer 
			this.index.setIndexBuffer(new HashMap<SpatialData,SpatialData>()) ;
		}
		
		public void fuseDatasets(String sep,SpatialDataset lowResPoints, 
				SpatialDataset highResPoints,
				SingleSummaryStatAggregator.SingleSummaryStat aggOp,
				SpatialData.DistanceMetric distMetric, 
				double neighborhoodRadius
				) {
			this.lowResPoints=lowResPoints; 
			this.highResPoints=highResPoints;
			this.aggOp=aggOp;
			this.distMetric=distMetric; 
			this.neighborhoodRadius=neighborhoodRadius;
			this.sep = sep;
			thread.start();
		}

		@Override
		public void run() {
			// 
			fusionResult= DataFusion._fuseDatasets(sep,lowResPoints, highResPoints,aggOp,distMetric, neighborhoodRadius,index);
		}
		
		public SpatialDataset pollResults() {
			
			//wait for worked to finish aggregating it's subset of points
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			return fusionResult;
		}
		
	}
}
