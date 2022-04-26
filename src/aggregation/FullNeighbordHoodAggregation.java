package aggregation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.Util;
import data_structure.SpatialData;
import data_structure.SpatialDataset;

public abstract class FullNeighbordHoodAggregation extends AbstractSpatialDataAggregator implements SpatialDataAggregator {

	private List<List<Double>> dataMatrix;
//	private List<double[]> rows;
	private int numberOfAttributes;
	
	private boolean ignoreSelfAttributes;
	public FullNeighbordHoodAggregation(String sep,boolean ignoreSelfAttributes) {
		super(sep);
		
		dataMatrix = null;
		this.ignoreSelfAttributes = ignoreSelfAttributes;
		numberOfAttributes=-1;
		//rows = new ArrayList<double[]>(16);
		
	}

	@Override
	/**
	 * aggreagates the attributes of the neighborhood into a matrix to make it easier to process
	 * We ignored
	 */
	public void applyAggregation(SpatialData target, SpatialDataset neighborhood,SpatialDataset outputBuffer) {
		if(target == null || neighborhood == null || outputBuffer == null) {
			throw new IllegalArgumentException("null parameters when applying spatial aggregation");			
		}
		outputBuffer.clear();
		
		
	
		//not initialized yet?
		if(numberOfAttributes == -1) {
			numberOfAttributes =neighborhood.getNumberOfAttributes(sep);
		}
		
		//create matrix first time calling this func
		if(dataMatrix == null) {			 
			dataMatrix = new ArrayList<List<Double>>(numberOfAttributes);		
			
			//create empty columns
			for(int attributeIx = 0; attributeIx < numberOfAttributes; attributeIx++) {
				List<Double> column = new ArrayList<Double>(neighborhood.size());
				dataMatrix.add(column);
			}
		}
				
		
		//populate the matrix with columns or attribute values
		for(int attributeIx = 0; attributeIx < numberOfAttributes; attributeIx++) {
			List<Double> column = dataMatrix.get(attributeIx);
			neighborhood._getAttributeValues(attributeIx, sep, column);
		}
		
	

		
		
		String summaryString = processEntireNeighborHood(target,dataMatrix,numberOfAttributes,neighborhood.size());
		
		

		String targetAttributes = target.getAttributes();
		String fusedAttributes;
		//empty or not printing self attributes?
		if(ignoreSelfAttributes || targetAttributes.isEmpty()) {
			//don't add comma beofre the summary string, cause empty iknput
			fusedAttributes = summaryString;//combine the attributes of low res and the entire entry/data for high res into fused data ttributes
		}else {
			fusedAttributes = target.getAttributes()+sep+summaryString;//combine the attributes of low res and the entire entry/data for high res into fused data ttributes
		}
			
		
		SpatialData fusedDatum = new SpatialData(target.getId(),target.getLocation(),fusedAttributes);
		outputBuffer.addSpatialData(fusedDatum);
		
	}
	protected abstract String processEntireNeighborHood(SpatialData target,List<List<Double>>matrix, int numCol, int numRows);
	//
	

	

}
