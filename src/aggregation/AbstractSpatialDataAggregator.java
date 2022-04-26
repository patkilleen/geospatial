package aggregation;

import data_structure.SpatialData;
import data_structure.SpatialDataset;

abstract public class AbstractSpatialDataAggregator implements SpatialDataAggregator{
	protected String sep;
	
	/**
	 * @param sep string to deliniate the point attributes
	 */
	public AbstractSpatialDataAggregator(String sep) {
		this.sep = sep;
	}

	@Override
	abstract public void applyAggregation(SpatialData target, SpatialDataset neighborhood, SpatialDataset outputBuffer);


}
