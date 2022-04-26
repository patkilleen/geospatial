package aggregation;

import java.util.Collections;
import java.util.List;

public class Stats {

	public Stats() {
		// TODO Auto-generated constructor stub
	}


	
	public static double mean(double [] values) {
		return mean(values,values.length);
	}
	public static double mean(double [] values, int length) {
		if(values== null || values.length == 0) {
			throw new IllegalArgumentException("empty values given");
		}
		
		if(length >values.length || length <=0) {
			throw new IllegalArgumentException("ill-formed length: "+length);
		}
		
		double mean = 0;
		
		for(int i = 0;i<length;i++) {
			mean += values[i];
		}
		
		mean = mean/((double)length);
		
		return mean;
	}
	
	public static double mean(List<Double> values) {
		if(values== null || values.size() == 0) {
			throw new IllegalArgumentException("empty values given");
		}
		
		
		double mean = 0;
		
		for(int i = 0;i<values.size();i++) {
			mean += values.get(i);
		}
		
		mean = mean/((double)values.size());
		
		return mean;
	}
	
	
	public static double median(double [] values) {
		return median(values,values.length);
	}
	
	public static double median(double [] values, int length) {
		
		if(values== null || values.length == 0) {
			throw new IllegalArgumentException("empty values given");
		}
		
		if(length >values.length || length <=0) {
			throw new IllegalArgumentException("ill-formed length: "+length);
		}
		double median=0;
		
		//sort the values
		bubbleSort(values,length);
		
		//compute median
		for(int i = 0;i <length;i++){

			
			
			if (length % 2 == 0){
				//mmean of 2 middle elements
				int ix1 = Math.floorDiv(length,2);
				int ix2 = Math.floorDiv(length,2)-1;
			    median =  (values[ix1]+values[ix2])/2.0; 
			}else{
				int ix= Math.floorDiv(length,2);
			    median = values[ix];
			}
			
			//convert the mean to stringS
		}
		
		return median;
		
	}
	
	public static double median(List<Double> values) {
		
		int length = values.size();
		if(values== null || length == 0) {
			throw new IllegalArgumentException("empty values given");
		}
		
	
		double median=0;

		Collections.sort(values);
		
		//compute median
		for(int i = 0;i <length;i++){

			
			
			if (length % 2 == 0){
				//mmean of 2 middle elements
				int ix1 = Math.floorDiv(length,2);
				int ix2 = Math.floorDiv(length,2)-1;
			    median =  (values.get(ix1)+values.get(ix2))/2.0; 
			}else{
				int ix= Math.floorDiv(length,2);
			    median = values.get(ix);
			}
			
			//convert the mean to stringS
		}
		
		return median;
		
	}
	
	public static double max(double [] values) {
		return max(values,values.length);
	}
	
	public static double max(double [] values, int length) {
		if(values== null || values.length == 0) {
			throw new IllegalArgumentException("empty values given");
		}
		
		if(length >values.length || length <=0) {
			throw new IllegalArgumentException("ill-formed length: "+length);
		}
		
		double max = values[0];
		
		for(int i = 0;i<length;i++) {
			
			//found new max?
			if(max < values[i]) {
				max = values[i];
			}
		}
		
		return max;
	}
	public static double max(List<Double> values) {
		
		int length = values.size();
		if(values== null || length == 0) {
			throw new IllegalArgumentException("empty values given");
		}
		
		
		
		double max = values.get(0);
		
		for(int i = 0;i<length;i++) {
			
			//found new max?
			if(max < values.get(i)) {
				max = values.get(i);
			}
		}
		
		return max;
	}
	
	public static double min(double [] values) {
		return min(values,values.length);
	}
	public static double min(double [] values, int length) {
		if(values== null || values.length == 0) {
			throw new IllegalArgumentException("empty values given");
		}
		
		if(length >values.length || length <=0) {
			throw new IllegalArgumentException("ill-formed length: "+length);
		}
		
		double min = values[0];
		
		for(int i = 0;i<length;i++) {
			
			//found new min?
			if(min > values[i]) {
				min = values[i];
			}
		}
		
		return min;
	}
	
	public static double min(List<Double> values) {
		int length = values.size();
		if(values== null || length == 0) {
			throw new IllegalArgumentException("empty values given");
		}
			
		
		double min = values.get(0);
		
		for(int i = 0;i<length;i++) {
			
			//found new min?
			if(min > values.get(i)) {
				min = values.get(i);
			}
		}
		
		return min;
	}
	
	public static double standard_deviation(double [] values) {
		return standard_deviation(values,values.length);
	}
	public static double standard_deviation(List<Double> values) {
		
		int length = values.size();
		if(values== null || length == 0) {
			throw new IllegalArgumentException("empty values given");
		}
		
	
		
		double res=0;
		double mean = mean(values);
		//we progressivly tracking (x_i - mean)^2
		for(int i = 0; i < length;i++) {
			double diff =values.get(i) - mean;
			res+=  diff*diff;
		}
		
		//create string of result
		
		res = Math.sqrt(res/((double)length));
		
		return res;
		
	}
	
	public static double standard_deviation(double [] values, int length) {
		if(values== null || values.length == 0) {
			throw new IllegalArgumentException("empty values given");
		}
		
		if(length >values.length || length <=0) {
			throw new IllegalArgumentException("ill-formed length: "+length);
		}
		
		double res=0;
		double mean = mean(values,length);
		//we progressivly tracking (x_i - mean)^2
		for(int i = 0; i < length;i++) {
			double diff =values[i] - mean;
			res+=  diff*diff;
		}
		
		//create string of result
		
		res = Math.sqrt(res/((double)length));
		
		return res;
		
	}
	
	/**
	 * returns the most frequent integer 
	 * @param a list of integer
	 * @param n size of array
	 * @return most frequent integer in array
	 */
	public static int mode(int a[],int n) {
	      int maxValue = 0;
	      int maxCount = 0;
	      int i=0;
	      int j=0;

	      for (i = 0; i < n; ++i) {
	         int count = 0;
	         for (j = 0; j < n; ++j) {
	            if (a[j] == a[i])
	            ++count;
	         }

	         if (count > maxCount) {
	            maxCount = count;
	            maxValue = a[i];
	         }
	      }
	      return maxValue;
	   }
	
	static void bubbleSort(double arr[], int n)
    {
     
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (arr[j] > arr[j+1])
                {
                    // swap arr[j+1] and arr[j]
                    double temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
    }
}
