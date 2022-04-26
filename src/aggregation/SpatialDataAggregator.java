package aggregation;

import data_structure.SpatialData;
import data_structure.SpatialDataset;

public interface SpatialDataAggregator {

	public void applyAggregation(SpatialData target, SpatialDataset neighborhood,SpatialDataset outputBuffer);
	

	
}
