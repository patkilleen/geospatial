package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aggregation.DataFusion;
import aggregation.InlierDetection;
import aggregation.SingleSummaryStatAggregator;
import aggregation.YieldProcessor;
import data_structure.SpatialData;
import data_structure.SpatialDataset;
import io.FileHandler;

public class Main {


	final static double EARTH_RADIUS = 6378137.0;// # from https://cran.r-project.org/web/packages/geosphere/geosphere.pdf

	final static String LOW_RES_INPUT_CSV_FILE_OPTION = "-inL";
	final static String HIGH_RES_INPUT_CSV_FILE_OPTION = "-inH";
	final static String DISTANCE_MEASURE_OPTION = "-d";
	final static String AGGREGATOR_OPERATOR_OPTION = "-agg";
	final static String RADIUS_OPTION = "-r";
	final static String OUTPUT_CSV_FILE_OPTION = "-o";
	final static String NUMBER_OF_THREADS_OPTION = "-t";
	final static String ATTRIBUTE_CSV_SEPERATOR_OPTION = "-s";
	final static String INLIER_MAX_CV_OPTION = "-maxCV";
	final static String DATA_DEFINED_RADIUS_OVERIDE_COLUMN_NAME_OPTION= "-rO";
	final static String INCLUDE_ROW_NAMES_OPTION= "-rname";
	final static String FORCE_OUTPUT_FILE_OVERRIDE_OPTION= "-of";
	
	final static String DISTANCE_MEASURE_EUCLIDEAN = "EUCLIDEAN";
	final static String DISTANCE_MEASURE_INFINITY_NORM = "INFINITY_NORM";

	final static String AGG_OP_BASIC="BASIC";
	final static String AGG_OP_MEAN="MEAN";
	final static String AGG_OP_MEDIAN="MEDIAN";
	final static String AGG_OP_MAX="MAX";
	final static String AGG_OP_MIN="MIN";
	final static String AGG_OP_STANDARD_DEVIATION="STANDARD_DEVIATION";
	final static String AGG_OP_INLIER_DETECTION="INLIER_DETECTION";
	

	final static int OPTION_CMD_IX =0;
	final static int OPTION_DESCRIPTION_IX =1;
	final static String [][] CMD_LINE_OPTIONS = {			
			{LOW_RES_INPUT_CSV_FILE_OPTION,"<lower spatial resolution input CSV file path. Points in this file act as centers of neighborhoods>"},
			{HIGH_RES_INPUT_CSV_FILE_OPTION,"<higher spatial resolution input CSV file path. Points in this file will be merged to nieghborhood centers>"},
			{DISTANCE_MEASURE_OPTION," ["+DISTANCE_MEASURE_EUCLIDEAN+"|"+DISTANCE_MEASURE_INFINITY_NORM+"] <distance measure used to define neighborhoods>"},
			{AGGREGATOR_OPERATOR_OPTION,"[BASIC|MEAN|MEDIAN|MAX|MIN|STANDARD_DEVIATION|INLIER_DETECTION]<data fusion aggregation operation to apply to points inside neighbordhoods"},
			{RADIUS_OPTION,"<radius (or pixel width for blocked-based fusion) of neighbordhoods>"},
			{OUTPUT_CSV_FILE_OPTION,"<output CSV path of data fusion>"}
			
	} ;

	final static String [][] OPTIONAL_CMD_LINE_OPTIONS = {
			{ATTRIBUTE_CSV_SEPERATOR_OPTION,"<the seperator for the CSV file :  ','  by default>"},
			{NUMBER_OF_THREADS_OPTION,"< number of threads : 1 by default>"},
			{INLIER_MAX_CV_OPTION,"<maximum coefficient of varition for inlier detection: 0.2 by default>"},
			{DATA_DEFINED_RADIUS_OVERIDE_COLUMN_NAME_OPTION,"<column name whose values will be used as search radius for neighborhoods when that point is center: no override by default>"},
			{INCLUDE_ROW_NAMES_OPTION,"<flag to include a row names/IDS column in the output CSV: false by default>"},
			{FORCE_OUTPUT_FILE_OVERRIDE_OPTION,"flag when true means use prompted to override outputfile, false overrides without prompt: false by default>"},			
	} ;
	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static List<SpatialData> processNeighborhoodResultBuffer = new ArrayList<SpatialData>(1024);



	public static void main(String [] args) {
		/*
		 * SpatialDataset lowResPoints : -inL
	SpatialDataset highResPoints : -inH
	SingleSummaryStatAggregator.SingleSummaryStat aggOp : -gg
	SpatialData.DistanceMetric distMetric : -d
	double neighborhoodRadius : -r

		 */

		/*
		SpatialDataset res2;
		try {
			res2 = YieldProcessor.identifyTurns(FileHandler.readCSVIntoSpatialDataset(args[0],null,","),15,10,",");
			FileHandler.writeSpatialDatasetToFile(args[1],"X,Y,timestamp,moisture,speed,width,yield,color", res2,true,",",false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		System.exit(0);*/
		
		if(args.length == 0 || args.length %2  != 0 ){

			printCmdLineUsage();
			System.exit(0);
		}
		String sep = SpatialData.COMMA;
		String lowResFilePath= null;
		String highResFilePath= null;
		SpatialDataset lowResPoints=null; // from -inL
		SpatialDataset highResPoints=null;// from -inH
		SingleSummaryStatAggregator.SingleSummaryStat aggOp=null; //from -agg
		SpatialData.DistanceMetric distMetric=null;// from -d
		String radiusOverrideColName = null;
		double maxCV = 0.2;
		double radius=-1; //from -r
		boolean includeRowNames = false;
		boolean overrideOutputFile = false;
		int numThreads = 1; //1 by default
		String outputFile=null;


		//parse all the command line arguments into program parameters
		for(int i = 0; i<args.length;i = i +2) {

			String op  = args[i];

			if (!isValidOption(op)){
				System.out.println("unrecognised option: '"+op+"'");
				printCmdLineUsage();
				System.exit(0);
			}




			if(op.equals(ATTRIBUTE_CSV_SEPERATOR_OPTION)) {
				sep=args[i+1];
				
				//remove any ENDING and starting quotes
				if(sep.length() > 1) {
					sep = sep.replace("\'","");
					sep = sep.replace("\"","");
				}

			}else if(op.equals(DATA_DEFINED_RADIUS_OVERIDE_COLUMN_NAME_OPTION)) {
					radiusOverrideColName=args[i+1];
					
					
					
					
			}else if(op.equals(INCLUDE_ROW_NAMES_OPTION)) {
			
				

				String includeRowNameStr = args[i+1];


				try {
					includeRowNames =Boolean.parseBoolean(includeRowNameStr);

					
				}catch(NumberFormatException e){
					System.out.println("failed to parse the include row names flag. Expected a boolean but was: "+includeRowNameStr);
					printCmdLineUsage();
					System.exit(0);
				}

					
			
			}else if(op.equals(FORCE_OUTPUT_FILE_OVERRIDE_OPTION)) {
			
				

				String outputOverrideStr = args[i+1];


				try {
					overrideOutputFile =Boolean.parseBoolean(outputOverrideStr);

					
				}catch(NumberFormatException e){
					System.out.println("failed to parse the output file override flag. Expected a boolean but was: "+outputOverrideStr);
					printCmdLineUsage();
					System.exit(0);
				}

					
			
			
			}else if(op.equals(INLIER_MAX_CV_OPTION)) {
			
				

				String maxCVStr = args[i+1];


				try {
					maxCV =Double.parseDouble(maxCVStr);

					
				}catch(NumberFormatException e){
					System.out.println("failed to parse the inlier maxCV. Expected a reel number but was: "+maxCVStr);
					printCmdLineUsage();
					System.exit(0);
				}

			
			}else if(op.equals(LOW_RES_INPUT_CSV_FILE_OPTION)) {
				lowResFilePath=args[i+1];

				
			}else if(op.equals(HIGH_RES_INPUT_CSV_FILE_OPTION)) {
				highResFilePath=args[i+1];
				
			}else if(op.equals(DISTANCE_MEASURE_OPTION)) {

				String distStr = args[i+1];


				try {
					distMetric =SpatialData.DistanceMetric.valueOf(distStr);
				}catch(IllegalArgumentException e){
					System.out.println("failed to parse distance measure. Expected 'EUCLIDEAN' or 'INFINITY_NORM' but was '"+distStr+"'");
					printCmdLineUsage();
					System.exit(0);
				}

			}else if(op.equals(AGGREGATOR_OPERATOR_OPTION)) {

				String aggStr = args[i+1];


				try {
					aggOp =SingleSummaryStatAggregator.SingleSummaryStat.valueOf(aggStr);

				}catch(IllegalArgumentException e){
					System.out.println("failed to aggregation operator. Expected ['"+AGG_OP_BASIC+"','"+AGG_OP_MEAN+"','"+AGG_OP_MEDIAN+"','"+AGG_OP_MAX+"','"+AGG_OP_MIN+"','"+AGG_OP_STANDARD_DEVIATION+"','"+AGG_OP_INLIER_DETECTION+"']"
							+ " but was '"+aggStr+"'");
					printCmdLineUsage();
					System.exit(0);
				}

			}else if(op.equals(RADIUS_OPTION)) {

				String radisuStr = args[i+1];


				try {
					radius =Double.parseDouble(radisuStr);

					if(radius <0) {
						throw new NumberFormatException("negative number");
					}
				}catch(NumberFormatException e){
					System.out.println("failed to parse the neighborhood radius. Expected a non-negative number but was: "+radisuStr);
					printCmdLineUsage();
					System.exit(0);
				}


			}else if(op.equals(OUTPUT_CSV_FILE_OPTION)) {

				outputFile = args[i+1];


			}else if(op.equals(NUMBER_OF_THREADS_OPTION)) {

				String numThreadStr = args[i+1];


				try {
					numThreads =Integer.parseInt(numThreadStr);

					if(numThreads <=0) {
						throw new NumberFormatException("non-positive integer");
					}
				}catch(NumberFormatException e){
					System.out.println("failed to parse the number of threads . Expected a positive number but was: "+numThreadStr);
					printCmdLineUsage();
					System.exit(0);
				}

			}//end checking options



		}//end iterate arguments


			
		try {				
			lowResPoints = FileHandler.readCSVIntoSpatialDataset(lowResFilePath,radiusOverrideColName,sep);
		}catch(IOException e) {
			System.out.println("failed to create low resolution spatial data by reading '"+lowResFilePath +"' due to "+e.getMessage());
			printCmdLineUsage();
			System.exit(0);
		}
		
		try {
			highResPoints = FileHandler.readCSVIntoSpatialDataset(highResFilePath,radiusOverrideColName,sep);
		}catch(IOException e) {
			System.out.println("failed to create high resolution spatial data by reading '"+highResFilePath+"' due to "+e.getMessage());
			printCmdLineUsage();
			System.exit(0);
		}
		
		System.out.println("merging higher resolution data from "+highResFilePath+"\n to lower resolution data from "+lowResFilePath);
		System.out.println("The "+aggOp+" operator is being applied to each neighborhood of radius "+radius+" using the "+distMetric+" dsitance measure.");

		SpatialDataset res= null;
		
		if(aggOp==SingleSummaryStatAggregator.SingleSummaryStat.INLIER_DETECTION) {
			res = InlierDetection.performOutlierDetection(maxCV,sep,highResPoints, distMetric,radius);
		}else {
			res = DataFusion.multiThreaded_FuseDatasets(sep,lowResPoints, highResPoints, aggOp, distMetric,radius,numThreads);
		}
		
		

		System.out.println("Outputing tresult to : "+outputFile);
		String highResCSVHeader= null;
		String lowResCSVHeader=null;

		boolean addCSVHeader=true;


		try {
			//	compute the combined CSV header of output file
			highResCSVHeader = FileHandler.readCSVHeader(highResFilePath);


		}catch(IOException e) {
			addCSVHeader =false;
			System.out.println("failed to read CSV header from "+highResFilePath+". output file won't have a 1st row header");
		}

		try {
			//	compute the combined CSV header of output file
			lowResCSVHeader = FileHandler.readCSVHeader(lowResFilePath);


		}catch(IOException e) {
			System.out.println("failed to read CSV header from "+lowResFilePath+". output file won't have a 1st row header");

			addCSVHeader = false;
		}

		//create the CSV header
		String outputCSVHeader;
		if(addCSVHeader) {
			
			
			String []lowresAttHeaders = null;
			if (lowResCSVHeader != null) {
				lowresAttHeaders=lowResCSVHeader.split(sep);
			}
			String []highResAttHeaders = null;
			if (highResCSVHeader != null) {
				highResAttHeaders=highResCSVHeader.split(sep);
			}
			outputCSVHeader =createCSVHeader(lowresAttHeaders,highResAttHeaders,sep,aggOp,includeRowNames); 
		}else {
			outputCSVHeader="";
		}
		
		FileHandler.writeSpatialDatasetToFile(outputFile,outputCSVHeader, res,overrideOutputFile,sep,includeRowNames);
		
		System.out.println("Finished data fusion.");
		//it
	}//end main

	public static boolean isValidOption(String op) {

		boolean res = false;
		//search list of predefined options and makei sure given option is among them
		for(int i = 0; i<CMD_LINE_OPTIONS.length;i++) {
			String[] cmdDescPair = CMD_LINE_OPTIONS[i];

			if(op.equals(cmdDescPair[OPTION_CMD_IX])) {
				res=true;
				break;
			}

		}

		//search optional argument option if normal option not found
		if(!res) {

			for(int i = 0; i<OPTIONAL_CMD_LINE_OPTIONS.length;i++) {
				String[] cmdDescPair = OPTIONAL_CMD_LINE_OPTIONS[i];

				if(op.equals(cmdDescPair[OPTION_CMD_IX])) {
					res=true;
					break;
				}

			}

		}

		return res;


	}




	private static void printCmdLineUsage() {
		System.out.println("******************\nThis program merges spatial dataset of varying resolution by offering nieghborhood-based aggregation data fusion. \nUsage: ");

		//print options
		for(int i = 0; i<CMD_LINE_OPTIONS.length;i++) {
			String[] cmdDescPair = CMD_LINE_OPTIONS[i];
			System.out.println("\t"+cmdDescPair[OPTION_CMD_IX]+": \t"+cmdDescPair[OPTION_DESCRIPTION_IX]);
		}

		System.out.println("optional arguments:");
		//print options
		for(int i = 0; i<OPTIONAL_CMD_LINE_OPTIONS.length;i++) {
			String[] cmdDescPair = OPTIONAL_CMD_LINE_OPTIONS[i];
			System.out.println("\t"+cmdDescPair[OPTION_CMD_IX]+": \t"+cmdDescPair[OPTION_DESCRIPTION_IX]);
		}

		System.out.println("******************\n");
	}


	public static String createCSVHeader(String [] lowResAttributeHeaders,String [] highResAttributeHeaders,String sep,SingleSummaryStatAggregator.SingleSummaryStat agg,boolean includeRowNames) {

		String outputCSVHeader = "";
		switch(agg) {



		case MEAN:
		case MEDIAN:
		case MAX:
		case MIN:
		case STANDARD_DEVIATION:

			
			if(includeRowNames) {
				outputCSVHeader = "LR_point-id";
			}else {
				if(lowResAttributeHeaders.length> 0) {
					outputCSVHeader="LR_"+lowResAttributeHeaders[0];
				}
			}
			
			int startIx = 0;
			if(! includeRowNames) {
				startIx=1;
			}
			for(int i = startIx;i <lowResAttributeHeaders.length;i++) {
				String lrAtt=lowResAttributeHeaders[i];
				outputCSVHeader += sep;
				outputCSVHeader += "LR_"+lrAtt;
			}
		



			//skip first 2, since they are coordinates
			for(int i = 2;i<highResAttributeHeaders.length;i++) {

				outputCSVHeader = outputCSVHeader +sep+"HR_"+agg+"_"+highResAttributeHeaders[i];
			}


			break;
		case BASIC:	
			
			if(includeRowNames) {
				outputCSVHeader = "LR_point-id"+sep;
			}else {
				if(lowResAttributeHeaders.length> 0) {
					outputCSVHeader="LR_"+lowResAttributeHeaders[0]+sep;
				}
			}
			
			startIx = 0;
			if(! includeRowNames) {
				startIx=1;
			}
			for(int i = startIx;i <lowResAttributeHeaders.length;i++) {
				String lrAtt=lowResAttributeHeaders[i];
				outputCSVHeader += "LR_"+lrAtt+sep;
			}

			outputCSVHeader += "HR_point-id";
			for(String hrAtt : highResAttributeHeaders) {
				outputCSVHeader +=sep;
				outputCSVHeader += "HR_"+hrAtt;
			}

			break;



		case INLIER_DETECTION:	
			String [] attributeHeaders;

			//in this case low and high res attributes the same
			if(lowResAttributeHeaders != null) {
				attributeHeaders = lowResAttributeHeaders;
			}else {
				attributeHeaders=highResAttributeHeaders;
			}

			//print all attributes


			if(includeRowNames) {
				outputCSVHeader = "point-id";
			}else {
				if(attributeHeaders.length> 0) {
					outputCSVHeader=attributeHeaders[0];
				}
			}
					
			startIx = 0;
			if(! includeRowNames) {
				startIx=1;
			}
			for(int i = startIx;i <attributeHeaders.length;i++) {
				String lrAtt=attributeHeaders[i];
				outputCSVHeader += sep;
				outputCSVHeader += lrAtt;
			}


			outputCSVHeader = outputCSVHeader +sep+"neighborhodd_size";					

			


			//we will now print columns in pairs of type for each attribute
			//skip first 2 coordinate header attributes
			for(int i = 2; i < attributeHeaders.length;i++) {


				outputCSVHeader = outputCSVHeader +sep+"CV_"+highResAttributeHeaders[i];								

			}



			for(int i = 2; i < attributeHeaders.length;i++) {


				outputCSVHeader = outputCSVHeader +sep+"k_"+highResAttributeHeaders[i];								

			}

			break;

		}

		return	 outputCSVHeader;

	}
}
