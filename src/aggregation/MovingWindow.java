package aggregation;

public class MovingWindow {

	private double [] window;
	
	private int cursor;
	
	private int numAddedValues;
	public MovingWindow(int windowCapacity) {
		window = new double[windowCapacity];
		cursor = 0;
		numAddedValues = 0;
	}
	
	public void add(double value) {
		window[cursor] = value;
		
		cursor = (cursor+1)%window.length;
		numAddedValues++;
	}

	public double mean() {
		if(getWindowSize()==0) {
			throw new IllegalStateException("cannot take the mean of an empty moving window");
		}
		return Stats.mean(window,getWindowSize());
	}
	
	
	
	/**
	 * returns the number of elements that can be stored in the window
	 * @return
	 */
	public int getWindowCapacity() {
		return window.length;
	}
	/**
	 * returns the current size of window (equivalent to getWindowCapacity if the window has been filled)
	 * @return
	 */
	public int getWindowSize() {
		
		int capacity = getWindowCapacity();
		//haven't filled window yet?
		if(numAddedValues <capacity) {
			return numAddedValues;
		}else {// window is filled
			return capacity;
		}
				
	}
	
	public void clear() {
		cursor = 0;
		numAddedValues = 0;
		
	}
}
