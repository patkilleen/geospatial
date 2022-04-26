package aggregation;

import java.util.Iterator;

import data_structure.SpatialData;
import data_structure.SpatialDataset;

public class BasicDataFusionAggregator extends AbstractSpatialDataAggregator implements SpatialDataAggregator {

	SingleSummaryStatAggregator.SingleSummaryStat statType;
	
	/**
	 * 
	 * @param sep string to deliniate the point attributes
	 */
	public BasicDataFusionAggregator(String sep,SingleSummaryStatAggregator.SingleSummaryStat statType) {
		super(sep);
		// TODO Auto-generated constructor stub
		this.statType = statType;
	}

	@Override
	/**
	 * fuses all the points in the neigbordhood to the target point
	 *  Outputs as many points as those in neighborhood into outputbuffer
	 */
	public void applyAggregation(SpatialData target, SpatialDataset neighborhood,SpatialDataset outputBuffer) {
		if(target == null || neighborhood == null || outputBuffer == null) {
			throw new IllegalArgumentException("null parameters when applying spatial aggregation");			
		}
		
		Iterator<SpatialData> it =neighborhood.iterator();
		
		outputBuffer.clear();
		
		while(it.hasNext()) {
			SpatialData highResDatum  = it.next();
		
			
			
			String targetAttributes = target.getAttributes();
			String fusedAttributes;
			//empty?
			if(targetAttributes.isEmpty()) {
				//don't add comma beofre the summary string, cause empty iknput
				fusedAttributes = highResDatum.toCSVString(sep);//combine the attributes of low res and the entire entry/data for high res into fused data ttributes
			}else {
				fusedAttributes = target.getAttributes()+sep+highResDatum.toCSVString(sep);//combine the attributes of low res and the entire entry/data for high res into fused data ttributes
			}
			
			
			
			SpatialData fusedDatum = new SpatialData(target.getId(),target.getLocation(),fusedAttributes);
			outputBuffer.addSpatialData(fusedDatum);
		}
		
	}


	

}
