package aggregation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import core.Util;
import data_structure.SpatialData;
import data_structure.SpatialDataset;

public abstract class SingleSummaryStatAggregator extends AbstractSpatialDataAggregator implements SpatialDataAggregator {
	private double [] finalResultBuffer;
	private double [] attributeValueBuffer;
	
	public enum SingleSummaryStat{
		MEAN,
		MEDIAN,
		MAX,
		MIN,
		STANDARD_DEVIATION,
		BASIC,
		INLIER_DETECTION
	}
	
	public SingleSummaryStatAggregator(String sep) {
		super(sep);
		finalResultBuffer = null; //will be used to store an array that tracks mean values to avoid re-alloacting each call to applyAggregation
		attributeValueBuffer = null;
		// TODO Auto-generated constructor stub
	}
	
	
	public static SpatialDataAggregator buildAggregator(SingleSummaryStat statType,String sep) {
		switch(statType) {
			case MEAN:
				return new MeanAgg(sep,statType);
			case MEDIAN:
				return new MedianAgg(sep,statType);				
			case MAX:
				return new MaxAgg(sep,statType);
			case MIN:
				return new MinAgg(sep,statType);
			case STANDARD_DEVIATION:
				return new StdDevAgg(sep,statType);	
				
			case BASIC:
				return new BasicDataFusionAggregator(sep,statType);
			default:
				throw new IllegalArgumentException("unknownw aggregation type: "+statType);
				
			
		}
	}

	
	/**
	 * takes the average of all the CSV attributes from the points in the neighbordhood and appends the means as attribute to the given point
	 * 
	 * Requires that the attributes member of points in neighborhood be CSV doubles 
	 *  Outputs one point into outputbuffer
	 */
	public void applyAggregation(SpatialData target, SpatialDataset neighborhood,SpatialDataset outputBuffer) {
		if(target == null || neighborhood == null || outputBuffer == null) {
			throw new IllegalArgumentException("null parameters when applying spatial aggregation");			
		}

		Iterator<SpatialData> it =neighborhood.iterator();

		outputBuffer.clear();


		if (neighborhood.size()==0) {
			return;
		}

		//GET NUMBER of attributes and define a buffer to aggregate them (one double celle represent a column)		
		int numberOfAttributes =neighborhood.getNumberOfAttributes(sep);
		finalResultBuffer = Util._allocateBuffer(numberOfAttributes,finalResultBuffer,true);//we zero this array since final results may progressivly build, while attribute values simply override buffer
		attributeValueBuffer =Util._allocateBuffer(numberOfAttributes,attributeValueBuffer,false);
		int sampleIx = 0;
		while(it.hasNext()) {
			SpatialData highResDatum  = it.next();
			
			//parse attribute values into double and load the attribute value buffer
			/*
			 *  //we use this version of function instead of '_parseAttributesToDoubleArray' since we only need access 
			 *  to the attribute values temporarily and only 1 at a time. Otherwise every single point would create 
			 *  an array to store a state. Here we can manage with a single allocated array for attribute values.
			 */
			highResDatum._parseAttributesToDoubleArray(attributeValueBuffer,sep);
			
			processSpatialDataAttributes(finalResultBuffer,numberOfAttributes,sampleIx, attributeValueBuffer);
			

			sampleIx++;
			
		}
			

		String summaryString = applySummaryStat(finalResultBuffer,numberOfAttributes,neighborhood.size());
		
	

		String targetAttributes = target.getAttributes();
		String fusedAttributes;
		//empty?
		if(targetAttributes.isEmpty()) {
			//don't add comma beofre the summary string, cause empty iknput
			fusedAttributes = summaryString;//combine the attributes of low res and the entire entry/data for high res into fused data ttributes
		}else {
			fusedAttributes = target.getAttributes()+sep+summaryString;//combine the attributes of low res and the entire entry/data for high res into fused data ttributes
		}
			
		
		SpatialData fusedDatum = new SpatialData(target.getId(),target.getLocation(),fusedAttributes);
		outputBuffer.addSpatialData(fusedDatum);

	}
	

	protected abstract String applySummaryStat(double [] finalResultBuffer, int numberOfAttributes, int numSamples);
	//protected abstract void processAttributeValue(double [] attributeValues,int sampleIx,int attributeIx, double attributeValue);
	
	protected abstract void processSpatialDataAttributes(double [] finalResultBuffer,int numberOfAttributes,int sampleIx, double [] attributeValues);
	
	

	
	private static class MeanAgg extends SingleSummaryStatAggregator{

		SingleSummaryStat statType;
		public MeanAgg(String sep,SingleSummaryStat statType) {
			super(sep);
			this.statType = statType;
		}

		
		@Override
		protected String applySummaryStat(double [] _finalResultBuffer, int _numberOfAttributes, int _numSamples){
			String meanAttributes = "";
			//compute mean
			for(int i = 0;i<_numberOfAttributes;i++){
				_finalResultBuffer[i] = _finalResultBuffer[i]/ ((double)_numSamples);
				//convert the mean to stringS
				meanAttributes = meanAttributes+_finalResultBuffer[i];

				//not last element?
				if(i != _numberOfAttributes-1){
					//add comman
					meanAttributes = meanAttributes+sep;
				}
			}	
			
			return  meanAttributes;
		}
		@Override
		protected void processSpatialDataAttributes(double [] _finalResultBuffer,int _numberOfAttributes,int _sampleIx, double [] _attributeValues) {
			
			for(int attributeIx = 0;attributeIx<_numberOfAttributes;attributeIx++) {
				_finalResultBuffer[attributeIx]+= _attributeValues[attributeIx];
			}
			
		}
		
		
		
	};
	
	private static class MedianAgg extends SingleSummaryStatAggregator{

		List<List<Double>> columns;
		SingleSummaryStat statType;
		public MedianAgg(String sep,SingleSummaryStat statType) {
			super(sep);	
			this.statType = statType;
		}

		
		@Override
		protected String applySummaryStat(double [] _finalResultBuffer, int _numberOfAttributes, int _numSamples){
			String medianAttributes = "";
			
			
			
			
			 int i = 0;
			//compute median
			for(int colIx = 0;colIx <_numberOfAttributes;colIx++){
				List<Double> column =columns.get(colIx);
				Collections.sort(column);
				double median;
				if (column.size() % 2 == 0){
					//mmean of 2 middle elements
					int ix1 = Math.floorDiv(column.size(),2);
					int ix2 = Math.floorDiv(column.size(),2)-1;
				    median =  (column.get(ix1)+column.get(ix2))/2.0; 
				}else{
					int ix= Math.floorDiv(column.size(),2);
				    median = column.get(ix);
				}
				
				//convert the mean to stringS
				medianAttributes = medianAttributes+median;
				
				//not last element?
				if(i != _numberOfAttributes-1){
					//add comman
					medianAttributes = medianAttributes+sep;
				}
				
				i++;
			}
			
			return  medianAttributes;
		}
		@Override
		protected void processSpatialDataAttributes(double [] _finalResultBuffer,int _numberOfAttributes,int _sampleIx, double [] _attributeValues) {
			
			//first call?
			if( _sampleIx == 0) {
				
				allocateAttributeValueBuffer(_numberOfAttributes);
				
			}
			
			for(int attributeIx = 0;attributeIx<_numberOfAttributes;attributeIx++) {
				
				List<Double> columnI = columns.get(attributeIx);
				columnI.add(_attributeValues[attributeIx]);
				
			}
			
		}
		
		
		private void allocateAttributeValueBuffer(int _numberOfAttributes) {
			//avoid allocating twice
			if(columns == null) {
				columns = new ArrayList<List<Double>>(_numberOfAttributes);
				
				
			}else {
				
				//clear all the buffers that stored attribute raw values
				for(int i = 0;i<columns.size();i++) {
					List<Double> attributeValues = columns.get(i);
					attributeValues.clear();
				}
				
				
			}
			
			//now we add as many lists as necessary
			for(int i = columns.size();i<_numberOfAttributes;i++) {
				columns.add(new ArrayList<Double>(1024*8));
			}
			
		}
		
	
	};
	


	private static class MaxAgg extends SingleSummaryStatAggregator{

		SingleSummaryStat statType;
		public MaxAgg(String sep,SingleSummaryStat statType) {
			super(sep);
			this.statType = statType;
		}

		
		@Override
		protected String applySummaryStat(double [] _finalResultBuffer, int _numberOfAttributes, int _numSamples) {
			String attributes = "";
			//max already found in each sell, simply copy into string
			for(int i = 0;i<_numberOfAttributes;i++){			
			
				attributes = attributes+_finalResultBuffer[i];

				//not last element?
				if(i != _numberOfAttributes-1){
					//add comman
					attributes = attributes+sep;
				}
			}	
			
			return  attributes;
		}
		
		@Override
		protected void processSpatialDataAttributes(double [] _finalResultBuffer,int _numberOfAttributes,int _sampleIx, double [] _attributeValues) {
			
			for(int attributeIx = 0;attributeIx<_numberOfAttributes;attributeIx++) {
				//first row? max values
				if(_sampleIx == 0) {
					_finalResultBuffer[attributeIx] = _attributeValues[attributeIx];
				}else {
					_finalResultBuffer[attributeIx] = Math.max(_finalResultBuffer[attributeIx], _attributeValues[attributeIx]);
				}
			}
			
		}
	
		
		
	};
	
	
	
	private static class MinAgg extends SingleSummaryStatAggregator{

		SingleSummaryStat statType;
		public MinAgg(String sep,SingleSummaryStat statType) {
			super(sep);
			this.statType = statType;
		}

		
		@Override
		protected String applySummaryStat(double [] _finalResultBuffer, int _numberOfAttributes, int _numSamples) {
			String attributes = "";
			////min already found in each sell, simply copy into string
			for(int i = 0;i<_numberOfAttributes;i++){			
				
				attributes = attributes+_finalResultBuffer[i];

				//not last element?
				if(i != _numberOfAttributes-1){
					//add comman
					attributes = attributes+sep;
				}
			}	
			
			return  attributes;
		}
		
		@Override
		protected void processSpatialDataAttributes(double [] _finalResultBuffer,int _numberOfAttributes,int _sampleIx, double [] _attributeValues) {
			
			for(int attributeIx = 0;attributeIx<_numberOfAttributes;attributeIx++) {
				//first row? max values
				if(_sampleIx == 0) {
					_finalResultBuffer[attributeIx] = _attributeValues[attributeIx];
				}else {
					_finalResultBuffer[attributeIx] = Math.min(_finalResultBuffer[attributeIx], _attributeValues[attributeIx]);
				}
			}
			
		}
		
		
		
	};
	

	private static class StdDevAgg extends SingleSummaryStatAggregator{
///SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MAX);
	
		SingleSummaryStat statType;
		private SpatialDataAggregator meanAgg; 
		
		private double[]  means;
		
		private SpatialData emptyAttributePt;
		
		private SpatialDataset meanOutputBuffer;
		
		private double[]  stdDevResultBuf;
		
		/*
		 * STD DEV = SQRT(SUM( (x_i - mean)^2)/N)
		 */
		public StdDevAgg(String sep,SingleSummaryStat statType) {
			super(sep);
			meanAgg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.MEAN,sep);
			emptyAttributePt = new SpatialData(0,new Point2D.Double(0,0),"");
			means = null;
			stdDevResultBuf = null;
			meanOutputBuffer = new SpatialDataset(1);
			this.statType = statType;
		}

		
		/**
		 * computes the mean of each attribute value from a neighborhood
		 * storing result in means array
		 * @param neighborhood
		 */
		private void computeMeans(SpatialDataset _neighborhood) {
			
			meanAgg.applyAggregation(emptyAttributePt, _neighborhood, meanOutputBuffer);
			
			SpatialData meansAttrPt = meanOutputBuffer.getSpatialData(0);
			String tmpAttributes = meansAttrPt.getAttributes();
			String []tmpTokens = tmpAttributes.split(sep);
			
			int numberOfAttributes =tmpTokens.length;
			means =Util._allocateBuffer(numberOfAttributes,means,false);
			
			//avoid reallocating buffer  twice			
			//_allocateAttributeBuffer(numberOfAttributes,means);
			
			//avoid reallocating buffer  twice
		
			meansAttrPt._parseAttributesToDoubleArray(means,sep);
			
			
			
			
		}
		@Override
		public void applyAggregation(SpatialData _target, SpatialDataset _neighborhood,SpatialDataset _outputBuffer) {
			//first call to compute standard deviation. we need the mean. give empty attribute target point so
			//the single resulting points's attributes are menas for each attribute
			computeMeans(_neighborhood);
			
			//mean is embedded in the objects
			
			//compute the standard deviation
			super.applyAggregation(_target, _neighborhood, _outputBuffer);
		}
		
		@Override
		protected String applySummaryStat(double [] _finalResultBuffer, int _numberOfAttributes, int _numSamples) {
			
			
			//allocate the buffer for result (avoid alloating twice)
			stdDevResultBuf = Util._allocateBuffer(_numberOfAttributes,stdDevResultBuf,false);
			//compute standard deviation
			
			
			//create string of result
			for(int i = 0;i<_numberOfAttributes;i++){
				stdDevResultBuf[i] = Math.sqrt(_finalResultBuffer[i]/((double)_numSamples));
			}
				
			String attributes = "";
			//create string of result
			for(int i = 0;i<_numberOfAttributes;i++){			
				
				attributes = attributes+stdDevResultBuf[i];

				//not last element?
				if(i != _numberOfAttributes-1){
					//add comman
					attributes = attributes+sep;
				}
			}	
			
			return  attributes;
		}
		
		@Override
		protected void processSpatialDataAttributes(double [] _finalResultBuffer,int _numberOfAttributes,int _sampleIx, double [] _attributeValues) {
			
			//we progressivly tracking (x_i - mean)^2
			for(int attributeIx = 0;attributeIx<_numberOfAttributes;attributeIx++) {
				double diff =_attributeValues[attributeIx] - means[attributeIx];
				_finalResultBuffer[attributeIx]+=  diff*diff;
			}
			
		}


		
		
		
	};
	


}


