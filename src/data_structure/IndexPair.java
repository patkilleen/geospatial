package data_structure;

public class IndexPair {

	public int lowerIx;
	public int upperIx;
	public int getLowerIx() {
		return lowerIx;
	}
	public void setLowerIx(int lowerIx) {
		this.lowerIx = lowerIx;
	}
	public int getUpperIx() {
		return upperIx;
	}
	public void setUpperIx(int upperIx) {
		this.upperIx = upperIx;
	}
	public IndexPair(int lowerIx, int upperIx) {
		super();
		this.lowerIx = lowerIx;
		this.upperIx = upperIx;
	}
	
	public boolean isLowerBoundOutsideDataset(){
		return lowerIx == -1;
	}

	public boolean isUpperBoundOutsideDataset(){
		return upperIx == -1;
	}
	
	public boolean indicesExist(){
		return ((upperIx!= -1) &&(lowerIx != -1));
	}
}
