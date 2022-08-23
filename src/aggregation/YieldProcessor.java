package aggregation;

import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.io.FilenameUtils;

import data_structure.BoundingBox;
import data_structure.MyRaster;
import data_structure.Polygon2D;
import data_structure.SpatialData;
import data_structure.SpatialDataIndex;
import data_structure.SpatialDataset;
import io.Configuration;
import io.FileHandler;
import io.IConfig;

/**
 * Most of the cleaning steps mentionned by : G Lyle, Brett Anthony Bryan, and B Ostendorf. 2014. Post-processing methods to eliminate erroneous grain yield measurements: review and directions
for future development. Precision agriculture 15, 4 (2014), 377–402

* for removing geps coordinates outside acceptable range: Simon Blackmore. 1999. Remedial correction of yield map data. Precision agriculture 1, 1 (1999), 53–66
 * @author Not admin
 *
 */
public class YieldProcessor {

	final public static double MILES_PER_HOUR_TO_METERS_PER_SECOND_DIVISOR=2.237;
	final public static double FEET_PER_METERS=3.28084;
	final public static int DEFAULT_SEGMENT_MODE_SECONDS_MOD = 3;
	public YieldProcessor() {
		// TODO Auto-generated constructor stub
	}




	//args:[input csv, output csv, degrees epsilon, window size]
	public static void main(String [] args) throws IOException, ImageReadException {

		String operation = args[0];

		if(operation.equals("-turn")) {
			String inputCSV =args[1];
			String outputCSV = args[2];
			double angleEp = Double.parseDouble(args[3]);
			int windowSize = Integer.parseInt(args[4]);
			int timestampIx = Integer.parseInt(args[5]);


			SpatialDataset res2;
			try {
				res2 = YieldProcessor.identifyTurns(FileHandler.readCSVIntoSpatialDataset(inputCSV,null,","),angleEp,windowSize,timestampIx,",",DEFAULT_SEGMENT_MODE_SECONDS_MOD);

				String inputCSVHeader = FileHandler.readCSVHeader(inputCSV);

				//FileHandler.writeSpatialDatasetToFile(outputCSV,inputCSVHeader+",isTurning", res2,true,",",false);
				FileHandler.writeSpatialDatasetToFile(outputCSV,inputCSVHeader+",isTurning", res2,true,",",false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}else if(operation.equals("-segment")) {

			String inputCSV =args[1];
			String outputCSV = args[2];
			int timestampIx=Integer.parseInt(args[3]);


			SpatialDataset res2;
			try {


				toHarvestPassesCSV( inputCSV, outputCSV,  timestampIx,",",DEFAULT_SEGMENT_MODE_SECONDS_MOD);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


		}else if(operation.equals("-filter")) {
			String inputCSV =args[1];
			String outputCSV = args[2];
			int timestampIx=Integer.parseInt(args[3]);
			int movingAverageWindowSizeInSeconds=Integer.parseInt(args[4]);
			int attributeIx=Integer.parseInt(args[5]);
			String sep = ",";
			double minMaxBoundsMod = Double.parseDouble(args[6]);

			SpatialDataset inputDataset = FileHandler.readCSVIntoSpatialDataset(inputCSV,null,sep);

			SpatialDataset res = applyForwardBackwardFilter( inputDataset, timestampIx,  movingAverageWindowSizeInSeconds, attributeIx, sep,  minMaxBoundsMod,DEFAULT_SEGMENT_MODE_SECONDS_MOD);


			String inputCSVHeader = FileHandler.readCSVHeader(inputCSV);

			String attributeOfInterrest = null;
			String [] tokens = inputCSVHeader.split(sep);
			attributeOfInterrest=tokens[attributeIx+2];//+2 since the two first x-y coordiantes don't count
			FileHandler.writeSpatialDatasetToFile(outputCSV,inputCSVHeader+","+attributeOfInterrest+"_isOutlier", res,true,",",false);
			//FileHandler.writeSpatialDatasetToFile(outputCSV,inputCSVHeader+",color", res,true,",",false);
		

		
	}else if(operation.equals("-overlap")) {
		String inputCSV =args[1];
		String outputCSV = args[2];
		int timestampIx=Integer.parseInt(args[3]);		
		int swathWidthIx=Integer.parseInt(args[4]);
		int speedIx=Integer.parseInt(args[5]);
		double maxGPSError=Double.parseDouble(args[6]);
		int preventFilterSegNum=Integer.parseInt(args[7]);
		double widthMod=Double.parseDouble(args[8]);
		String sep = ",";
		boolean mphFlag = true;
		boolean usingSwathWidthInFeet = true;
		SpatialDataset inputDataset = FileHandler.readCSVIntoSpatialDataset(inputCSV,null,sep);
		
		//remove gps outliers first. this is important for computeing the area harvested
		SpatialDataset res=removeGPSPositionErrors( inputDataset, timestampIx,  speedIx,  sep, maxGPSError,mphFlag,DEFAULT_SEGMENT_MODE_SECONDS_MOD);
			
		
		res = identifyOverlap( res, timestampIx,  swathWidthIx, sep, preventFilterSegNum,widthMod,usingSwathWidthInFeet,DEFAULT_SEGMENT_MODE_SECONDS_MOD);


		String inputCSVHeader = FileHandler.readCSVHeader(inputCSV);

		FileHandler.writeSpatialDatasetToFile(outputCSV,inputCSVHeader+",overlapping", res,true,",",false);
		
		
	}else if(operation.equals("-clean_all")) {
		IConfig config = new Configuration(args[1]);
		
		String inputCSV = config.getProperty(IConfig.PROPERTY_YP_INPUT_CSV);
		String sep = config.getProperty(IConfig.PROPERTY_YP_CSV_SEPERATOR);
		
		
		System.out.println("reading data from :"+inputCSV);
		String inputCSVHeader = FileHandler.readCSVHeader(inputCSV);
		SpatialDataset inputDataset = FileHandler.readCSVIntoSpatialDataset(inputCSV,null,sep);
		
		int modeSegMod = config.getIntProperty(IConfig.PROPERTY_YP_SEGMENT_DETERMINATION_MODE_SECS_MOD);
		
		
		int timestampIx = config.getIntProperty(IConfig.PROPERTY_YP_TIMESTAMP_IX);
		List<SpatialDataset> harvestSegements=toHarvestPassSet(inputDataset,timestampIx,sep,modeSegMod);
		
		
		int numSecsFillSamplesError = config.getIntProperty(IConfig.PROPERTY_YP_NUMBER_SECONDS_HARVEST_FILL_MODE);
		int numSecsFinishSamplesError=config.getIntProperty(IConfig.PROPERTY_YP_NUMBER_SECONDS_HARVEST_FINISH_MODE);
		int shortSegmentSize=config.getIntProperty(IConfig.PROPERTY_YP_SHORT_SEGMENT_SIZE);
		

		System.out.println("identifying start/finish harvest pass errors and short segements");
		/*identify harvest fill and finish mode errors and short segments 
		 * 
		 */
		List<SpatialDataset> segments = cleanHarvestDatasetSegments(harvestSegements,  timestampIx, sep, numSecsFillSamplesError,  numSecsFinishSamplesError, shortSegmentSize);
		
		
		System.out.println("applying moisture filter");
		//apply moisture filter to each segment
		int moistureIx=config.getIntProperty(IConfig.PROPERTY_YP_MOISTURE_IX);
		double moisoutreLowerBoundThreshold=config.getDoubleProperty(IConfig.PROPERTY_YP_MOISTURE_LOWER_BOUND_THRESHOLD);
		
		for(int i=0;i<segments.size();i++) {
			SpatialDataset seg = segments.get(i);
			
			SpatialDataset tmpRes = identifyExcedingBounds(seg, moistureIx, sep,moisoutreLowerBoundThreshold ,false);
			segments.set(i, tmpRes);
			
		}
		
		
		System.out.println("applying biological yield maximum filter");
		
		/*
		 * apply biological yield maximum filter
		 */
		int yieldIx=config.getIntProperty(IConfig.PROPERTY_YP_YIELD_IX);
		double yieldUpperBoundThreshold=config.getDoubleProperty(IConfig.PROPERTY_YP_YIELD_UPPER_BOUND_THRESHOLD);
		
		for(int i=0;i<segments.size();i++) {
			SpatialDataset seg = segments.get(i);
			
			SpatialDataset tmpRes = identifyExcedingBounds(seg, yieldIx, sep,yieldUpperBoundThreshold ,true);
			segments.set(i, tmpRes);
			
		}
		
		System.out.println("applying forward-backware speed harvest pass filter");
		
		/*
		 * apply forward-backward pass speed filter
		 */
		int spdMovingAverageWindowSizeInSeconds=config.getIntProperty(IConfig.PROPERTY_YP_SPEED_MOVING_AVG_WINDOW_SIZE);
		int speedIx=config.getIntProperty(IConfig.PROPERTY_YP_SPEED_IX);		
		double spdMinMaxBoundsMod=config.getDoubleProperty(IConfig.PROPERTY_YP_SPEED_FILTER_BOUNDS_MODIFIER);


		for(int i=0;i<segments.size();i++) {
			SpatialDataset seg = segments.get(i);
			
			SpatialDataset tmpRes = applyForwardBackwardFilter( seg, timestampIx,  spdMovingAverageWindowSizeInSeconds, speedIx, sep,  spdMinMaxBoundsMod,modeSegMod);
			segments.set(i, tmpRes);
			
		}
		
		System.out.println("identifying co-located points");
		
		/*
		 * identify coolocated points		
		 */
		for(int i=0;i<segments.size();i++) {
			SpatialDataset seg = segments.get(i);
			
			SpatialDataset tmpRes = identifyCo_LocatedPoints(seg, sep);
			segments.set(i, tmpRes);
			
		}
		
		System.out.println("revmoing points with gps errors");
		
		/*
		 * remove points that suffer from GPS errors
		 */
		
		double maxGPSError = config.getDoubleProperty(IConfig.PROPERTY_YP_GPS_NOISE_MAX_ERROR);
		boolean mphFlag =  config.getBooleanProperty(IConfig.PROPERTY_YP_SPEED_MPH_FLAG);
		
		for(int i=0;i<segments.size();i++) {
			SpatialDataset seg = segments.get(i);
			
			SpatialDataset tmpRes = removeGPSPositionErrors( seg,timestampIx, speedIx,  sep,maxGPSError, mphFlag,modeSegMod);
			segments.set(i, tmpRes);
			
		}
		
		System.out.println("identifying overlaps");
		
		/*
		 * identify overlaps
		 */
		


		int swathWidthIx=config.getIntProperty(IConfig.PROPERTY_YP_WIDTH_IX);
		int preventFilterSegNum=config.getIntProperty(IConfig.PROPERTY_YP_OVERLAP_PREVENT_FILTER_IN_PASS_NUM);
		double widthMod=config.getDoubleProperty(IConfig.PROPERTY_YP_OVERLAP_HARVEST_AREA_SIZE_MODIFIER);
		boolean usingFeetFlag=config.getBooleanProperty(IConfig.PROPERTY_YP_OVERLAP_SWATH_WIDTH_FEET_FLAG);
		
		
		boolean [] outlier = __identifyOverlap(segments,timestampIx,  swathWidthIx,sep,preventFilterSegNum,widthMod,usingFeetFlag);

		int ptIx = 0;
		for(SpatialDataset seg : segments) {
			
			for(int i = 0;i<seg.size();i++) {
				
				SpatialData pt = seg.getSpatialData(i);
				
				String newAttributes = pt.getAttributes();

				if(outlier[ptIx]) {
					newAttributes = newAttributes + sep + "1";
				}else {
					newAttributes = newAttributes + sep + "0";
				}

				pt.setAttributes(newAttributes);
				
				ptIx++;
				
			}//end iterate over points in segment
			
		}//end iterate over the segments


		System.out.println("identifying sharp turns");
		/*
		 * identify turns
		 */
		double turnAngleEpsilon=config.getDoubleProperty(IConfig.PROPERTY_YP_TURNING_ANGLE_EPSILON);
		int turnMovingAverageWindowSize=config.getIntProperty(IConfig.PROPERTY_YP_TURNING_ANGLE_MOVING_AVG_WINDOW_SIZE);

		for(int i=0;i<segments.size();i++) {
			SpatialDataset seg = segments.get(i);
			
			boolean [] partOfTurn = _identifyTurns(seg, turnAngleEpsilon,  turnMovingAverageWindowSize,timestampIx,sep);

			for(int j = 0;j<seg.size();j++) {
				SpatialData inputPt = seg.getSpatialData(j);

				String newAttributes = inputPt.getAttributes();

				if(partOfTurn[j]) {
					newAttributes = newAttributes + sep + "1";
				}else {
					newAttributes = newAttributes + sep + "0";
				}
				
				//update attribute of point
				inputPt.setAttributes(newAttributes);;
				
			
			}//end iterate the points in segment

			
		}//end iterate segment
		
		System.out.println("applying forward-backware yield harvest pass filter");
				
		/*
		 * apply forward-backward pass yield filter
		 */
		int yieldMovingAverageWindowSizeInSeconds=config.getIntProperty(IConfig.PROPERTY_YP_YIELD_MOVING_AVG_WINDOW_SIZE);
		
		double yieldMinMaxBoundsMod=config.getDoubleProperty(IConfig.PROPERTY_YP_YIELD_FILTER_BOUNDS_MODIFIER);


		
		for(int i=0;i<segments.size();i++) {
			SpatialDataset seg = segments.get(i);
			
			SpatialDataset tmpRes = applyForwardBackwardFilter( seg, timestampIx,  yieldMovingAverageWindowSizeInSeconds, yieldIx, sep,  yieldMinMaxBoundsMod,modeSegMod);
			segments.set(i, tmpRes);
			
		}
		
		
		
		String outputCSV=config.getProperty(IConfig.PROPERTY_YP_OUTPUT_CSV);
		
		String outputHeader = inputCSVHeader + sep +
							  "harvest_fill_error" + sep +
							  "harvest_finish_error" + sep +
							  "short_segment" + sep +
							  "moisture_outlier" + sep +
							  "biological_yield_exceeded" + sep +
							  "speed_outlier" + sep +							 
							  "co-located_point" + sep +
							  "harvest-overlap" + sep +
							  "harvester_turning" + sep +
							  "yield_outlier";
		
		int resSize=0;
		for(int i=0;i<segments.size();i++) {
			SpatialDataset seg = segments.get(i);
			
			resSize+= seg.size();
			
		}
		
		//create output dataset
		SpatialDataset res = new SpatialDataset(resSize);
		for(int i=0;i<segments.size();i++) {
			SpatialDataset seg = segments.get(i);
			for(int j=0;j<seg.size();j++) {
				res.addSpatialData(seg.getSpatialData(j));
			}			
			
		}
		

		System.out.println("writing results to:" +outputCSV);
		
		FileHandler.writeSpatialDatasetToFile(outputCSV,outputHeader, res,true,",",false);
			
		
		
	}else if(operation.equals("-imagery-fusion")) {//fuse yield with GeoTIFF imagery
		
		String inputCSV =args[1];
		String outputCSV = args[2];
		String inputGeotiff =args[3];
		String imageryColName =args[4];//only required for MS data as we don't know what band it is. name of bnad column		
		double radius =Double.parseDouble(args[5]);		
		SpatialData.DistanceMetric distMetric =SpatialData.DistanceMetric.valueOf(args[6]);
		//String aggOppStr =args[7];
		String rExecutablePath=args[7];
		String rasterInfoRScriptPath = args[8];
		String fieldBoundaryPath = args[9];
		int numberOfThreads = Integer.parseInt(args[10]);
		String sep = ",";
		
		System.out.println("reading yield data: "+inputCSV);
		
		SpatialDataset inputDataset = FileHandler.readCSVIntoSpatialDataset(inputCSV,null,sep);
		
		
		String inputCSVHeader = FileHandler.readCSVHeader(inputCSV);

		//read boundary file
		SpatialDataset boundaryPts = FileHandler.readCSVIntoSpatialDataset(fieldBoundaryPath, null, sep);
		
		//convert field boundary to polygon 2d (used to avoid including raster pixels outside the field)
		Polygon2D fieldBoundaryPoly =boundaryPts.toPolygon2D();

		
		MyRaster r = new MyRaster(fieldBoundaryPoly);
		System.out.print("reading raster: "+inputGeotiff);
		
		long startTime =  System.currentTimeMillis();   
		r.load(rExecutablePath, rasterInfoRScriptPath, inputGeotiff);
		long endTime =  System.currentTimeMillis();   

		long ellapsedtime = Math.floorDiv(endTime-startTime, 1000);
		System.out.println("... took approx. "+ellapsedtime+" seconds.");
		
		Iterator<SpatialData> it = inputDataset.iterator();
		
		//int aggOpp=-1;
		
		int [] aggOperators = {MyRaster.MEAN_AGG,MyRaster.MAX_AGG,MyRaster.MIN_AGG};
		String [] aggOperatorNames = {"mean","max","min"};

		int bandFromIx=0;
		int bandToIx=0;
		
		if(r.isRGBRaster()) {
			bandFromIx=0;
			bandToIx=2;
		}
		
	
		
		System.out.print("fusiong yield data and imagery ");
		
		Rectangle2D preAllocatedRect = new Rectangle2D.Double();
		
		startTime =  System.currentTimeMillis();   
		for(int bandIx =bandFromIx; bandIx <=bandToIx;bandIx++ ) {
			r.multiThreaded_fuseAll(numberOfThreads,inputDataset, sep, radius, distMetric, bandIx); 
			
		}
		endTime =  System.currentTimeMillis();
		ellapsedtime = Math.floorDiv(endTime-startTime, 1000);
		System.out.println("Fusion took approx. "+ellapsedtime+" seconds.");
		String header = inputCSVHeader;
		
		for(String aggOpName : aggOperatorNames) {
			header = header+",";
			if(r.isMSRaster()) {
				header = header + "\"MS_" + imageryColName + "_"+aggOpName+"\"";
			}else {
				header = header + "\"RGB_Red_"+aggOpName+"\",\""+ "RGB_Green_"+aggOpName+"\",\""+ "RGB_Blue_"+aggOpName+"\"";
			}
		}
		
		System.out.println("writing fusion results to "+outputCSV);
		FileHandler.writeSpatialDatasetToFile(outputCSV,header, inputDataset,true,",",false);
		

		
		
	}else if(operation.equals("-multi-image-fusion")) {//fuse yield with many smaller  GeoTIFFs tiles of a larger GeoTIff (they won't share the same extent)
		
		String inputCSV =args[1];
		String outputCSV = args[2];
		String inputGeotiffDir =args[3]; //directory where all the Big GeoTIFFs tiles are 
		String imageryColName =args[4];//only required for MS data as we don't know what band it is. name of bnad column		
		double radius =Double.parseDouble(args[5]);		
		SpatialData.DistanceMetric distMetric =SpatialData.DistanceMetric.valueOf(args[6]);
		//String aggOppStr =args[7];
		String rExecutablePath=args[7];
		String rasterInfoRScriptPath = args[8];
		String fieldBoundaryPath = args[9];
		int numberOfThreads = Integer.parseInt(args[10]);
		//int inputCSVRowSplitNum =Integer.parseInt(args[11]);//number of rows to split the input CSV datsaet into
		//int inputCSVColSplitNum =Integer.parseInt(args[12]);//number of columsn to split the input CSV datsaet into
		
		String sep = ",";
		
		
		System.out.println("reading yield data: "+inputCSV);
		
		SpatialDataset _inputDataset = FileHandler.readCSVIntoSpatialDataset(inputCSV,null,sep);
		SpatialDataIndex index = new SpatialDataIndex(_inputDataset);
		
		//used to store results of index lookup for yield sbuareas
		SpatialDataset _inputDatasetBuffer = new SpatialDataset(_inputDataset.size()); 
		
		SpatialDataset outputDataset = new SpatialDataset(_inputDataset.size());
		
		
		String inputCSVHeader = FileHandler.readCSVHeader(inputCSV);

		
		//
		String header = inputCSVHeader;
		boolean computedHeader=false;
		
		//read boundary file
		SpatialDataset boundaryPts = FileHandler.readCSVIntoSpatialDataset(fieldBoundaryPath, null, sep);
		
		//convert field boundary to polygon 2d (used to avoid including raster pixels outside the field)
		Polygon2D fieldBoundaryPoly =boundaryPts.toPolygon2D();

		
		
		
		//all the input GeoTIFFs in  directory specified by inputGeotiffDir
		File[] inputTiffs = FileHandler.getFilesInDirectory(inputGeotiffDir, ".tif");
		
		//the list of bounding boxes of yield points already process
		List<BoundingBox> bbBlackList = new ArrayList<BoundingBox>(inputTiffs.length);
		
		//iterate over every tiff and load them individually into memory for fusion
		//the GeoTiffs are assumed to fully contain the bounding box of each cell in
		//the grid of the inputCSV that was split (exactly one geotiff will contain the cell) 
		for(File inputTiffFile :inputTiffs) {
			MyRaster r = new MyRaster(fieldBoundaryPoly);
			String inputTiff = inputTiffFile.getAbsolutePath();
			System.out.print("reading raster: "+inputTiff);
			
			long startTime =  System.currentTimeMillis();   
			r.load(rExecutablePath, rasterInfoRScriptPath, inputTiff);
			long endTime =  System.currentTimeMillis();   
	
			long ellapsedtime = Math.floorDiv(endTime-startTime, 1000);
			System.out.println("... took approx. "+ellapsedtime+" seconds.");
			
			Rectangle2D rasterBB = r.getBoundingBox();
			
			//this blackbox raster will have margin's trimmed to not fused yiel at very edge of image (-2 * radisu to make sure yield  in fusion be in it))
			BoundingBox _rasterBB=new BoundingBox(rasterBB.getMinX()+(radius*2),rasterBB.getMinY()+(radius*2),rasterBB.getMaxX()-(2*radius),rasterBB.getMaxY()-(2*radius));
			
			//lookup all yield points inside the raster via the index (exlcude those already processed)
			 index.__getSpatialDataInBoundingBox(_rasterBB, _inputDatasetBuffer, bbBlackList);
			 
			 //take note of area already processed
			 bbBlackList.add(_rasterBB);
			 
			//avoid including the cell twice in fusion
			

			
			
//			Iterator<SpatialData> it = targetCell.iterator();
//			
			//int aggOpp=-1;
			
			//int [] aggOperators = {MyRaster.MEAN_AGG,MyRaster.MAX_AGG,MyRaster.MIN_AGG};
			String [] aggOperatorNames = {"mean","max","min"};
	
			int bandFromIx=0;
			int bandToIx=0;
			
			if(r.isRGBRaster()) {
				bandFromIx=0;
				bandToIx=2;
			}
			
			if(!computedHeader) {

				for(String aggOpName : aggOperatorNames) {
					header = header+",";
					if(r.isMSRaster()) {
						header = header + "\"MS_" + imageryColName + "_"+aggOpName+"\"";
					}else {
						header = header + "\"RGB_Red_"+aggOpName+"\",\""+ "RGB_Green_"+aggOpName+"\",\""+ "RGB_Blue_"+aggOpName+"\"";
					}
				}
				computedHeader=true;
			}
			
			System.out.print("fusiong yield data and imagery ");
			
			Rectangle2D preAllocatedRect = new Rectangle2D.Double();
			
			startTime =  System.currentTimeMillis();   
			for(int bandIx =bandFromIx; bandIx <=bandToIx;bandIx++ ) {
				r.multiThreaded_fuseAll(numberOfThreads,_inputDatasetBuffer, sep, radius, distMetric, bandIx); 
				
			}
			endTime =  System.currentTimeMillis();
			ellapsedtime = Math.floorDiv(endTime-startTime, 1000);
			System.out.println("Fusion took approx. "+ellapsedtime+" seconds.");
			
			
			//append the result to final dataset
			for(int i = 0;i <_inputDatasetBuffer.size();i++) {
				SpatialData _pt= _inputDatasetBuffer.getSpatialData(i);
				outputDataset.addSpatialData(_pt);
			}
			
			_inputDatasetBuffer.clear();
			System.out.println("writing fusion results to "+outputCSV);
			
		}//end iterate each tiff to read and fuse
		FileHandler.writeSpatialDatasetToFile(outputCSV,header, outputDataset,true,",",false);
		
	
	}else if(operation.equals("-imagery-fusion-direction-of-images")) {//fuse yield with all GeoTIFF imagery in a directory
		//assumes all the geotiffs have the same extend and represent different bands/vegetation indices
		//so the in-boundary matrix is only computed once
		
		
	
		String inputCSV =args[1];
		String outputCSV = args[2];
		String inputGeotiffDir =args[3];// directory that holds all raster files to process
		//String imageryColName =args[4];//only required for MS data as we don't know what band it is. name of bnad column		
		double radius =Double.parseDouble(args[4]);		
		SpatialData.DistanceMetric distMetric =SpatialData.DistanceMetric.valueOf(args[5]);
		//String aggOppStr =args[7];
		String rExecutablePath=args[6];
		String rasterInfoRScriptPath = args[7];
		String fieldBoundaryPath = args[8];
		int numberOfThreads = Integer.parseInt(args[9]);
		String sep = ",";
		
		System.out.println("reading yield data: "+inputCSV);
		
		SpatialDataset inputDataset = FileHandler.readCSVIntoSpatialDataset(inputCSV,null,sep);
		
		
		String inputCSVHeader = FileHandler.readCSVHeader(inputCSV);
		
		String header = inputCSVHeader;
		

		//read boundary file
		SpatialDataset boundaryPts = FileHandler.readCSVIntoSpatialDataset(fieldBoundaryPath, null, sep);
		
		//convert field boundary to polygon 2d (used to avoid including raster pixels outside the field)
		Polygon2D fieldBoundaryPoly =boundaryPts.toPolygon2D();

		
		
		//need to load first image to create this matrix
		//that will then be used for all sbusequent geotiffs
		boolean [][] pixelInsideBoundaryMatrix = null;
		
		
		//used for aggregating pixels in neighbordhoods
		Rectangle2D preAllocatedRect = new Rectangle2D.Double(); 
		
		//get all the '.tif' files in given directiory
		File [] rasterFiles = FileHandler.getFilesInDirectory(inputGeotiffDir,".tif");
		
		if(rasterFiles == null) {
			System.out.println("No fusion performed. No geotiffs with '.tif' extension in directory "+inputGeotiffDir);
			System.exit(0);
		}
		System.out.println("Fusing yield data with "+rasterFiles.length+" rasters in direcotry "+inputGeotiffDir);
		
		for(File f : rasterFiles) {
			
			MyRaster r=null;
			//first time loading a raster into memory (no pixel boundary flag matrix)?
			if (pixelInsideBoundaryMatrix == null)
				r = new MyRaster(fieldBoundaryPoly);//matrix will be created from the field boundary polygon file
			else {
				r = new MyRaster(pixelInsideBoundaryMatrix);//matrix already created will be used to avoid creating it more than once (a lenghty process)
			}
			
			
			
			long startTime =  System.currentTimeMillis();   
			
			//System.out.println("...processing raster: "+f.getAbsolutePath());
			r.load(rExecutablePath, rasterInfoRScriptPath, f.getAbsolutePath());
			long endTime =  System.currentTimeMillis();   

			long ellapsedtime = Math.floorDiv(endTime-startTime, 1000);
			System.out.println("... took approx. "+ellapsedtime+" seconds.");
			
			
			//first image loaded?
			if (pixelInsideBoundaryMatrix == null) {
				pixelInsideBoundaryMatrix = r.getPixelInsideBoundaryFlags(); //save the matrix for future imagaes
			}
			
			Iterator<SpatialData> it = inputDataset.iterator();
			
			//int aggOpp=-1;
			
			
			String [] aggOperatorNames = {"mean","max","min"};
		
			
			int bandFromIx=0;
			int bandToIx=0;
			
			if(r.isRGBRaster()) {
				bandFromIx=0;
				bandToIx=2;
			}
			
	
			
			//System.out.println("fusiong yield data and imagery ");
			
			startTime =  System.currentTimeMillis();
			for(int bandIx =bandFromIx; bandIx <=bandToIx;bandIx++ ) {
				r.multiThreaded_fuseAll(numberOfThreads,inputDataset, sep, radius, distMetric, bandIx); 
				
			}
			
			endTime =  System.currentTimeMillis();
			ellapsedtime = Math.floorDiv(endTime-startTime, 1000);
			System.out.println("Fusion took approx. "+ellapsedtime+" seconds.");
			
			//use the name of the file as the band name for column band identification, and remove extension
			String imageryColName = FilenameUtils.removeExtension(f.getName());
			for(String aggOpName : aggOperatorNames) {
				header = header+",";
				if(r.isMSRaster()) {
					header = header + "\"MS_" + imageryColName + "_"+aggOpName+"\"";
				}else {
					header = header + "\"RGB_Red_"+aggOpName+"\",\""+ "RGB_Green_"+aggOpName+"\",\""+ "RGB_Blue_"+aggOpName+"\"";
				}
			}
			
			
	
		}//end iterate each raster file
		System.out.println("writing fusion results to "+outputCSV);
		FileHandler.writeSpatialDatasetToFile(outputCSV,header, inputDataset,true,",",false);
		
	}//end cmd arguemnt for processing all images in directory

		System.exit(0);
	}

	/**
	 * Identifies points involved in a turn based on average angle of point pairs
	 * @param inputData spatial data assumed to be sorted by timestamp
	 * @param angleEpsilon the threshodl angle in degrees before considering a pair of points not part of same segment
	 * @param movingAverageWindowSize the number of angles kept in history to compute the average angle
	 * @param sep attribute seperator
	 * @param modeSegMod number of times duration ellapsed mode before considrered outleir (3 is recommended)
	 * @return a SpatialDataset with boolean flag appended to attribute where element i is true when the ith point is part of turn, and false otherwise 
	 */
	public static SpatialDataset identifyTurns(SpatialDataset inputData, double angleEpsilon, int movingAverageWindowSize,int timestampIx, String sep,int modeSegMod) {



		SpatialDataset res = new SpatialDataset(inputData.size());


		List<SpatialDataset> segments = toHarvestPassSet(inputData,timestampIx,sep,modeSegMod);
		//make sure to splite the dataset into harvest passes/segmenets and apply turn detection only for the points within segements
		//iterate over every segmgent and apply turn detection
		for(SpatialDataset segment : segments) {
			boolean [] partOfTurn = _identifyTurns(segment, angleEpsilon,  movingAverageWindowSize,timestampIx,sep);

			for(int i = 0;i<segment.size();i++) {
				SpatialData inputPt = segment.getSpatialData(i);

				String newAttributes = inputPt.getAttributes();

				if(partOfTurn[i]) {
					newAttributes = newAttributes + sep + "1";
				}else {
					newAttributes = newAttributes + sep + "0";
				}

				SpatialData newPt = new SpatialData(inputPt.getId(),inputPt.getLocation(),newAttributes);
				res.addSpatialData(newPt);
			}

		}

		return res;
	}

	/**
	 * Identifies points involved in a turn based on average angle of point pairs
	 * @param inputData spatial data assumed to be sorted by timestamp and is a harvest pass/segment
	 * @param angleEpsilon the threshodl angle in degrees before considering a pair of points not part of same segment
	 * @param movingAverageWindowSizeInSeconds points withing the number of seconds defined by this parameter have angles kept in history to compute the average angle
	 * @param timestampIx index of the timestamp attribute
	 * @param attribute seperator
	 * @return a boolean array where element i is true when the ith point is part of turn, and false otherwise 
	 * 
	 */
	public static boolean [] _identifyTurns(SpatialDataset inputData, double angleEpsilon, int movingAverageWindowSizeInSeconds,int timestampIx, String sep) {


		//make a deep copy of the dataset, since we will be altering the datset to identify turns
		SpatialDataset copy = new SpatialDataset(inputData.size());
		for(int i = 0;i<inputData.size();i++) {
			SpatialData pt1 = inputData.getSpatialData(i);
			//use i as id too make sure the id's can be used as indices to lookup the point
			copy.addSpatialData(new SpatialData(i,pt1.getLocation(),pt1.getAttributes()));

		}
		inputData=copy;
		//stores angles
		//MovingWindow window = new MovingWindow(movingAverageWindowSizeInSeconds);

		boolean [] isPartOfTurnForward = new boolean[inputData.size()];

		List<Double> angleBuffer = new ArrayList<Double>(movingAverageWindowSizeInSeconds);
		SpatialDataset windowBuffer = new SpatialDataset(movingAverageWindowSizeInSeconds);


		//make sure to enable the attribute parse buffering
		//for(int i = 0;i<inputData.size();i++) {
		//SpatialData pt1 = inputData.getSpatialData(i);
		//	pt1.enableAttributeParsingBuffering(); // can't enable buffering cause timestamp isn't a double



		//}


		//compute all the point angles 
		_appendNeighbortAngleToAttributes(inputData, sep);


		int forwardnAngleIx = inputData.getNumberOfAttributes(sep)-2;
		int backAngleIx = forwardnAngleIx+1;

		//forward pass: iterate over each point (end at before last point since there is no next point to check after last point)
		//start at 1 and end before last since end points can't be part of turn by design
		for(int i = 1;i<inputData.size()-1;i++) {


			//SpatialData pt1 = inputData.getSpatialData(i);

			//compute window left (as we move forward trhough points)	
			_findTimeBasedNearbySamples(i , inputData, timestampIx,sep, movingAverageWindowSizeInSeconds,windowBuffer,false);

			Iterator<SpatialData> it = windowBuffer.iterator();
			angleBuffer.clear();
			//compute all the angles
			while(it.hasNext()) {
				SpatialData neighbor = it.next();
				//SpatialData pt2 = inputData.getSpatialData(i+1);

				int neighborIx = neighbor.getId();

				//double angle = neighbor.angleTo(pt1);
				double angle = Double.parseDouble(inputData.getAttributeStringValue(neighborIx, forwardnAngleIx, sep)); 

				angleBuffer.add(angle);


			}


			//neighbors are found in time window?
			if(windowBuffer.size()>0) { 
				double angleHistory = Stats.mean(angleBuffer);

				double nextPtAngle = Double.parseDouble(inputData.getAttributeStringValue(i, forwardnAngleIx, sep));
				//started turning (a different of more than acceptable angle tolerance occured)?
				if (Math.abs(angleHistory-nextPtAngle) > angleEpsilon) {
					isPartOfTurnForward[i]=true;			
				}else {
					isPartOfTurnForward[i]=false;
				}
			}else {
				//no neighbors, so can't exclude it for being different than neighbors
				isPartOfTurnForward[i]=false;
			}
		}

		isPartOfTurnForward[0] = false;
		isPartOfTurnForward[inputData.size()-1] = false;

		boolean [] isPartOfTurnBackward = new boolean[inputData.size()];

	
		//back pass: iterate over each point (end at before last point since there is no next point to check after last point)
		//start and before last and go until the 2nd since end points can't be part of turn
		for(int i = inputData.size()-2;i>0;i--) {


			//SpatialData pt1 = inputData.getSpatialData(i);

			//compute window left (as we move forward trhough points)	
			_findTimeBasedNearbySamples(i , inputData, timestampIx,sep, movingAverageWindowSizeInSeconds,windowBuffer,true);

			Iterator<SpatialData> it = windowBuffer.iterator();
			angleBuffer.clear();
			//compute all the angles
			while(it.hasNext()) {
				SpatialData neighbor = it.next();
				//SpatialData pt2 = inputData.getSpatialData(i+1);

				int neighborIx = neighbor.getId();

				//double angle = neighbor.angleTo(pt1);
				double angle = Double.parseDouble(inputData.getAttributeStringValue(neighborIx, backAngleIx, sep)); 

				angleBuffer.add(angle);


			}


			//neighbors are found in time window?
			if(windowBuffer.size()>0) { 
				double angleHistory = Stats.mean(angleBuffer);

				double nextPtAngle = Double.parseDouble(inputData.getAttributeStringValue(i, backAngleIx, sep));
				//started turning (a different of more than acceptable angle tolerance occured)?
				if (Math.abs(angleHistory-nextPtAngle) > angleEpsilon) {
					isPartOfTurnBackward[i]=true;			
				}else {
					isPartOfTurnBackward[i]=false;
				}
			}else {
				//no neighbors, so can't exclude it for being different than neighbors
				isPartOfTurnBackward[i]=false;
			}
		}


		isPartOfTurnBackward[0] = false;
		isPartOfTurnBackward[inputData.size()-1] = false;

		boolean [] result = new boolean[inputData.size()];

		//by considering both past and future history, a turn is identified
		for(int i = 0; i < inputData.size();i++) {
			result[i]=isPartOfTurnBackward[i]&&isPartOfTurnForward[i];
		}

		return result;
	}


	public static void _appendNeighbortAngleToAttributes(SpatialDataset inputData, String sep) {

		//iterate forward over points, and append the compute angle to attributes

		for(int i =0 ; i< inputData.size()-1;i++) {

			/*
			 * TODO: CLEAR THE moving window average very time a new pass/segment occurs. will need to know what column ix is that. 
			 */
			SpatialData pt1 = inputData.getSpatialData(i);
			SpatialData pt2 = inputData.getSpatialData(i+1);

			double angle = pt1.angleTo(pt2);

			String pt1Attributes = pt1.getAttributes();

			pt1.setAttributes(pt1Attributes + sep + angle); 

		}

		//last point angle is 0 since no other point
		SpatialData lastPt = inputData.getSpatialData(inputData.size()-1);
		lastPt.setAttributes(lastPt.getAttributes() + sep + "0.0");

		//now iterate backward to comppute the angles

		for(int i = inputData.size()-1;i>0;i--) {

			SpatialData pt1 = inputData.getSpatialData(i);
			SpatialData pt2 = inputData.getSpatialData(i-1);

			double angle = pt1.angleTo(pt2);

			String pt1Attributes = pt1.getAttributes();

			pt1.setAttributes(pt1Attributes + sep + angle);

		}

		//last point angle is 0 since no other point
		SpatialData firstPt = inputData.getSpatialData(0);
		firstPt.setAttributes(firstPt.getAttributes() + sep + "0.0");
	}

	/**
	 * Converts a yield dataset into a list of harvest-passes/segments based on timestamps
	 * assumes the dataste is sorted by time
	 * @param inputDataset
	 * @param timestampIx index of the tiemstamp attribute
	 * @param sep attribute seperator
	 * @return list of harvest passes/segments
	 * @throws IOException 
	 */
	public static void toHarvestPassesCSV(String inputCSV, String outputCSV, int timestampIx,String sep, int modeSegMod) throws IOException{

		SpatialDataset inputDataset = FileHandler.readCSVIntoSpatialDataset(inputCSV,null,sep);

		String inputCSVHeader = FileHandler.readCSVHeader(inputCSV);



		SpatialDataset outputDataset = toHarvestPassDataset(inputDataset,timestampIx,sep,modeSegMod);


		FileHandler.writeSpatialDatasetToFile(outputCSV,inputCSVHeader+",segmentId", outputDataset,true,sep,false);

	}



	/**
	 * Converts a yield dataset into a dataset where an attribute, the harvest-passes/segments based on timestamps, is added to dataset
	 * assumes the dataste is sorted by time
	 * @param inputDataset
	 * @param timestampIx index of the tiemstamp attribute
	 * @param sep attribute seperator
	 * @return spatialdataset with segmenet/harvest-pass id identified in the attributes
	 * @param modeSegMod number of times duration ellapsed mode before considrered outleir (3 is recommended)
	 * @throws IOException 
	 */
	public static SpatialDataset toHarvestPassDataset(SpatialDataset inputDataset, int timestampIx,String sep,int modeSegMod) throws IOException{


		List<SpatialDataset> segments = toHarvestPassSet(inputDataset,timestampIx,sep,modeSegMod);

		SpatialDataset outputDataset = new SpatialDataset(inputDataset.size());


		//iterate over each harvest pass
		for(int segmentId = 0;segmentId<segments.size();segmentId++) {
			SpatialDataset segDataset = segments.get(segmentId);

			Iterator<SpatialData> it = segDataset.iterator();

			while(it.hasNext()) {
				SpatialData pt = it.next();

				//add the se3gement id to attributes
				String attributes = pt.getAttributes();
				attributes = attributes+sep+segmentId;
				pt.setAttributes(attributes);

				//add poitn to result
				outputDataset.addSpatialData(pt);
			}
		}

		return outputDataset;

	}

	/**
	 * Converts a yield dataset into a list of harvest-passes/segments based on timestamps
	 * assumes the dataste is sorted by time
	 * @param inputDataset
	 * @param timestampIx index of the tiemstamp attribute
	 * @param sep attribute seperator
	 * @param modeSegMod number of times duration ellapsed mode before considrered outleir (3 is recommended)
	 * @return list of harvest passes/segments
	 */
	public static List<SpatialDataset> toHarvestPassSet(SpatialDataset inputDataset, int timestampIx,String sep, int modeSegMod){

		List<SpatialDataset> res = new ArrayList<SpatialDataset>(128);

		if(inputDataset.size()==0) {
			return res;
		}


		if(inputDataset.size()==1 ||inputDataset.size()==2) {
			SpatialDataset ds = new SpatialDataset(inputDataset.size());

			for(int i=0;i<inputDataset.size();i++) {
				ds.addSpatialData(inputDataset.getSpatialData(i));
			}

			res.add(ds);
			return res;
		}


		//compute the time difference between everypoint (-1 since were compring each pair of timestamp to each other, so last element will have nothing to compare to)
		int [] deltaTimes = new int[inputDataset.size()-1];

		String [] tokens = null;
		//populate the delta time (ingore last eleement since nothing to compare to)
		for(int i = 0;i<inputDataset.size()-1;i++) {
			SpatialData pt1 = inputDataset.getSpatialData(i);
			SpatialData pt2 = inputDataset.getSpatialData(i+1);


			//time difference (in seconds) between the samples
			int deltaT = secondsBetweenPoints(pt1,pt2,timestampIx,sep);

			//store compute time difference
			deltaTimes[i]=deltaT;
		}


		//compute frequency of the samples using mode (most frequent time difference)
		int sampleFreq = Stats.mode(deltaTimes, deltaTimes.length);

		//harvest pass /segement time threshold. the amount of time between samples to consider a new pass
		int timeThresh=modeSegMod*sampleFreq;

		//start creating the datastes for each harvest pass by identifying start and end of passes
		//passes tend to have a minimum of 2 poitns (start and end). there is a small chance the last point is an outlier and it's not part of previous pass
		//but we will add it to final pass anyways

		boolean startingNewPass = true;
		SpatialDataset hPass=null;
		for(int i = 0;i<inputDataset.size();i++) {


			SpatialData pt1 = inputDataset.getSpatialData(i);


			//last point?
			if(i == inputDataset.size()-1) {

				//we only add the previous pass to result when were not creating the first pass
				if(hPass == null) {
					hPass = new SpatialDataset(1);
				}

				hPass.addSpatialData(pt1);
				res.add(hPass);
				//nothing more to do
				break;
			}




			//when we start a new pass, the next point is always added to the dataset
			//since an end of pass was created
			if(startingNewPass) {

				//we only add the previous pass to result when were not creating the first pass
				if(hPass != null) {
					res.add(hPass);
				}
				hPass = new SpatialDataset(64);
				hPass.addSpatialData(pt1);
				startingNewPass=false;
				continue;
			}

			//we add point to the pass
			hPass.addSpatialData(pt1);



			/*
			 * we now check the time diffrence between samples to identify different passes
			 */
			int deltaT = deltaTimes[i];

			//this identifies a new segment is being created
			startingNewPass = deltaT > timeThresh;


		}


		return res;
	}


	/**
	 * Adds 3 new boolean attributes to each point in the given harvest segments, namely:  start_of_segement_error,end_of_segment_error, short_segement
	 * @param inputDataset
	 * @param timestampIx
	 * @param sep
	 * @param numSecsFillSamplesError
	 * @param numSecsFinishSamplesError
	 * @param shortSegmentSize
	 * @return
	 * @throws IOException
	 */
	public static List<SpatialDataset> cleanHarvestDatasetSegments(List<SpatialDataset> harvestSegements, int timestampIx,String sep,int numSecsFillSamplesError, int numSecsFinishSamplesError,int shortSegmentSize) {
		//List<SpatialDataset> segments =  toHarvestPassSet(inputDataset, timestampIx,sep);

		List<SpatialDataset> res = new ArrayList<SpatialDataset>(harvestSegements.size()); 


		int ptIx=0;
		//remove any segments that are considered too short
		for(SpatialDataset ds : harvestSegements) {

			SpatialDataset newSegment = new SpatialDataset(ds.size());

			Iterator<SpatialData> it = ds.iterator();


			//short segement?
			boolean shortSegFlag =ds.size()<shortSegmentSize;

			/*
			 * compute start time of segment. we assume that 1 reading is in segement, since by definition atleast 2 readings in it
			 */
			SpatialData startPt = ds.getSpatialData(0);

			String [] tokens = startPt.getAttributes().split(sep);
			String ptTimeStr = tokens[timestampIx];
			LocalDateTime segStartTime = LocalDateTime.parse(ptTimeStr);

			SpatialData endPt = ds.getSpatialData(ds.size()-1);

			tokens = endPt.getAttributes().split(sep);
			ptTimeStr = tokens[timestampIx];
			LocalDateTime segEndTime = LocalDateTime.parse(ptTimeStr);



			while(it.hasNext()) {
				SpatialData  pt = it.next();
				//shortSeg[ptIx] = shortSegFlag;

				boolean isStartFillError=false;
				boolean isFinishFillError=false;
				int deltaT = secondsBetweenPoints(segStartTime,pt, timestampIx, sep); 

				//skip the start filling readings
				if(deltaT<numSecsFillSamplesError) {
					isStartFillError=true;
				}

				deltaT = secondsBetweenPoints(pt,segEndTime, timestampIx, sep);
				//skip the finish filling readings
				if(deltaT<numSecsFinishSamplesError) {
					isFinishFillError=true;
				}

				SpatialData  newPoint = new SpatialData(pt.getId(),pt.getLocation(),pt.getAttributes()+sep+(isStartFillError?"1":"0")+sep+(isFinishFillError?"1":"0")+sep+(shortSegFlag?"1":"0"));
				newSegment.addSpatialData(newPoint);
				ptIx++;
			}

			res.add(newSegment);

		}


		return res;


	}


	
	/**
	 * appends a flag to dataset attribute  (1 when attribute value exceeds threshold, 0 otherwise) to indicate if value exceeded a maximum/miniumum
	 * @param inputData dataset to check for value exceeding bounds
	 * @param attributeIx attribute index to check bounds
	 * @param sep seperator
	 * @param threshold threshold used to determine boundary exceeding
	 * @param maxFlag true when checking for excedding maximum threshold, flase when checking for mlower than minimum threshold
	 * @return dataset with "1" appe3nded to attributes whos desired value exccede threshodl (0 when inside bound)
	 */
	public static SpatialDataset identifyExcedingBounds(SpatialDataset inputData, int attributeIx,String sep, double threshold,boolean maxFlag) {

		SpatialDataset res = new SpatialDataset(inputData.size());

		Iterator<SpatialData> it = inputData.iterator();

		while(it.hasNext()) {
			SpatialData pt = it.next();

			String [] tokens = pt.getAttributes().split(sep);
			String valueStr = tokens[attributeIx];

			double value =  Double.parseDouble(valueStr);

			SpatialData newPt =null;
			if(maxFlag) {
				newPt= new SpatialData(pt.getId(),pt.getLocation(),pt.getAttributes()+sep+(value >threshold ? "1":"0"));
			}else {
				newPt= new SpatialData(pt.getId(),pt.getLocation(),pt.getAttributes()+sep+(value <threshold ? "1":"0"));
			}
			res.addSpatialData(newPt);
		}

		return res;


	}
	
	/**
	 * Identifies points that share the same coordinates
	 * @param inputData the datsaet to check fo co-located points
	 * @param sep seperator of attributes
	 * @return the dataset with "1" append to attributes of points that share a location with another point and "0" for a point in a unique location
	 */
	public static SpatialDataset identifyCo_LocatedPoints(SpatialDataset inputData, String sep) {
		
		//make a deep copy of the dataset, since we will be sorting the alter input data points
		 
		SpatialDataset copy = new SpatialDataset(inputData.size());
		for(int i = 0;i<inputData.size();i++) {
			SpatialData pt1 = inputData.getSpatialData(i);
			//use i as id too make sure the id's can be used as indices to lookup the point
			copy.addSpatialData(new SpatialData(i,pt1.getLocation(),pt1.getAttributes()));

		}
		
	
		SpatialDataset res = new SpatialDataset(inputData.size());
		
		
		SpatialDataIndex index = new SpatialDataIndex(copy);
		
		
		Iterator<SpatialData> it = inputData.iterator();
		
		SpatialDataset buffer = new SpatialDataset(16);
		
		
		//keeps track of what point we added the attribute to to avoid adding twice
		//boolean [] processed = new boolean[copy.size()];
		
		while(it.hasNext()) {
			SpatialData pt = it.next();
			
			//get all points in 0 radius neghborhodd (any point with same location)
			index._getSpatialDataInSquareNeighborhood(pt.getLocation(),0,buffer);//0 width, since only points on same location will be returned
			
			String attributes = "";
			if(buffer.size() ==1) {
				//only 1 point, so add "0" to indicate point shares the location with no other point
				attributes= pt.getAttributes();
				attributes = attributes + sep + "0";
//				pt.setAttributes(attribute);
				//processed[pt.getId()] = true;
			}else {
				
				attributes= pt.getAttributes();
				attributes = attributes + sep + "1";
			
			}//end there are no colocated points
			
			//SpatialData originalPt = inputData.getSpatialData(pt.getId());
			res.addSpatialData(new SpatialData(pt.getId(),pt.getLocation(),attributes));
			
		}//end iteratee all points
		
		return res;
		
		 
	}

	public static SpatialDataset applyForwardBackwardFilter(SpatialDataset inputData,int timestampIx, int movingAverageWindowSizeInSeconds,int attributeIx,String sep, double minMaxBoundsMod,int modeSegMod) {



		SpatialDataset res = new SpatialDataset(inputData.size());


		List<SpatialDataset> segments = toHarvestPassSet(inputData,timestampIx,sep,modeSegMod);
		//make sure to splite the dataset into harvest passes/segmenets and apply the back-forward filter detection only for the points within segements
		//iterate over every segmgent and apply  filter
		for(SpatialDataset segment : segments) {
			boolean [] outlier = _applyForwardBackwardFilter(segment, timestampIx,  movingAverageWindowSizeInSeconds,attributeIx,sep,minMaxBoundsMod);

			for(int i = 0;i<segment.size();i++) {
				SpatialData inputPt = segment.getSpatialData(i);

				String newAttributes = inputPt.getAttributes();

				if(outlier[i]) {
					newAttributes = newAttributes + sep + "1";
				}else {
					newAttributes = newAttributes + sep + "0";
				}

				SpatialData newPt = new SpatialData(inputPt.getId(),inputPt.getLocation(),newAttributes);
				res.addSpatialData(newPt);
			}

		}

		return res;
	}


	public static boolean [] _applyForwardBackwardFilter(SpatialDataset inputData,int timestampIx, int movingAverageWindowSizeInSeconds,int attributeIx,String sep, double minMaxBoundsMod) {


		//we require the ids to be from 0 to size -1


		//make a deep copy of the dataset, since we will be altering the datset to identify turns
		SpatialDataset copy = new SpatialDataset(inputData.size());
		for(int i = 0;i<inputData.size();i++) {
			SpatialData pt1 = inputData.getSpatialData(i);
			//use i as id too make sure the id's can be used as indices to lookup the point
			copy.addSpatialData(new SpatialData(i,pt1.getLocation(),pt1.getAttributes()));

		}
		inputData=copy;


		boolean [] forwardOutlier = new boolean[inputData.size()];


		List<Double> meanValueBuffer = new ArrayList<Double>(movingAverageWindowSizeInSeconds);
		SpatialDataset windowBuffer = new SpatialDataset(movingAverageWindowSizeInSeconds);




		//forward pass: iterate over each point (end at before last point since there is no next point to check after last point)
		//start at 1 and end before last since end points can't be part of turn by design
		for(int i = 0;i<inputData.size();i++) {

			forwardOutlier[i]=_applyForwardBackwardFilterHelper(meanValueBuffer,i,inputData,timestampIx,sep,movingAverageWindowSizeInSeconds,windowBuffer,true,attributeIx,  minMaxBoundsMod);
			//


		}

		//forwardOutlier[0] = false;
		forwardOutlier[inputData.size()-1] = false;//no points ahead of last poitn in forward pass, so not an outlier

		boolean [] backwardOutlier = new boolean[inputData.size()];

	

		//back pass: iterate over each point (end at before last point since there is no next point to check after last point)
		//start and before last and go until the 2nd since end points can't be part of turn
		for(int i = inputData.size()-1;i>=0;i--) {

			backwardOutlier[i]=_applyForwardBackwardFilterHelper(meanValueBuffer,i,inputData,timestampIx,sep,movingAverageWindowSizeInSeconds,windowBuffer,false,attributeIx,  minMaxBoundsMod);
			//SpatialData pt1 = inputData.getSpatialData(i);

		}


		backwardOutlier[0] = false;


		boolean [] result = new boolean[inputData.size()];

		//by considering both past and future history, a turn is identified
		for(int i = 0; i < inputData.size();i++) {
			result[i]=backwardOutlier[i]&&forwardOutlier[i];
		}

		return result;
	}



	private static boolean _applyForwardBackwardFilterHelper(List<Double> meanValueBuffer,int i, SpatialDataset inputData, int timestampIx,
			String sep, int movingAverageWindowSizeInSeconds, SpatialDataset windowBuffer, boolean fowardPass,int attributeIx, double minMaxBoundsMod) {
		//compute window left (as we move forward trhough points)	
		_findTimeBasedNearbySamples(i , inputData, timestampIx,sep, movingAverageWindowSizeInSeconds,windowBuffer,fowardPass);

		Iterator<SpatialData> it = windowBuffer.iterator();
		meanValueBuffer.clear();
		//compute all the angles
		while(it.hasNext()) {
			SpatialData neighbor = it.next();
			//SpatialData pt2 = inputData.getSpatialData(i+1);

			int neighborIx = neighbor.getId();


			double value = Double.parseDouble(inputData.getAttributeStringValue(neighborIx, attributeIx, sep)); 

			meanValueBuffer.add(value);


		}


		//neighbors are found in time window?
		if(windowBuffer.size()>0) { 
			double neighbordHoodMean = Stats.mean(meanValueBuffer);

			//compute minimun and max boundaries with respect to neighborhood mean
			double maxBound  = neighbordHoodMean + minMaxBoundsMod*neighbordHoodMean;
			double minBound  = neighbordHoodMean - minMaxBoundsMod*neighbordHoodMean;
			double currPtValue = Double.parseDouble(inputData.getAttributeStringValue(i, attributeIx, sep));
			//started turning (a different of more than acceptable angle tolerance occured)?
			if ((currPtValue < minBound) || (currPtValue >  maxBound)) {
				return true;			
			}else {
				return false;
			}
		}else {
			//no neighbors, so can't exclude it for being different than neighbors
			return false;
		}
	}





	public static int secondsBetweenPoints(SpatialData pt1,SpatialData pt2, int timestampIx, String sep) {


		String [] tokens = pt1.getAttributes().split(sep);
		String ptTimeStr = tokens[timestampIx];
		LocalDateTime pt1Time = LocalDateTime.parse(ptTimeStr);

		tokens = pt2.getAttributes().split(sep);
		ptTimeStr = tokens[timestampIx];
		LocalDateTime pt2Time = LocalDateTime.parse(ptTimeStr);

		return (int) ChronoUnit.SECONDS.between(pt1Time, pt2Time);  //time difference (in seconds) between the start of segment and current sample

	}

	public static int secondsBetweenPoints(LocalDateTime pt1Time,SpatialData pt2, int timestampIx, String sep) {



		String [] tokens = pt2.getAttributes().split(sep);
		String ptTimeStr = tokens[timestampIx];
		LocalDateTime pt2Time = LocalDateTime.parse(ptTimeStr);

		return (int) ChronoUnit.SECONDS.between(pt1Time, pt2Time);  //time difference (in seconds) between the start of segment and current sample

	}

	public static int secondsBetweenPoints(SpatialData pt1,LocalDateTime pt2Time, int timestampIx, String sep) {



		String [] tokens = pt1.getAttributes().split(sep);
		String ptTimeStr = tokens[timestampIx];
		LocalDateTime pt1Time = LocalDateTime.parse(ptTimeStr);

		return (int) ChronoUnit.SECONDS.between(pt1Time, pt2Time);  //time difference (in seconds) between the start of segment and current sample

	}

	/**
	 * finds all samples taken within a short period of time from a traget sample (either on left or right of sample)
	 * @param targetIx the index of target point in inputData to check for points around time this point samples
	 * @param inputData data set to search that is sorted by timestamps
	 * @param timestampIx index of timersptamp attribute
	 * @param sep seperator
	 * @param lenInSecs the window length in seconds to search for
	 * @param outputBuffer buffer to store resulting points inside the neighborhood
	 * @param forwardFlag
	 */
	public static void _findTimeBasedNearbySamples(int targetIx , SpatialDataset inputData, int timestampIx,String sep,int lenInSecs,SpatialDataset outputBuffer,boolean forwardFlag) {


		outputBuffer.clear();

		if(targetIx < 0 || targetIx >= inputData.size()) {

			return;
		}

		SpatialData target = inputData.getSpatialData(targetIx);


		String [] tokens = target.getAttributes().split(sep);
		String ptTimeStr = tokens[timestampIx];
		LocalDateTime targetTime = LocalDateTime.parse(ptTimeStr);


		//searching to right of target point (in future)
		if(forwardFlag) {

			//iterate over every point right of target and keep adding them to result until we reach a 
			//point that's timestamp is outside the window
			for(int i = targetIx + 1; i <inputData.size();i++) {
				SpatialData pt = inputData.getSpatialData(i);

				int deltaT = secondsBetweenPoints(targetTime,pt,timestampIx,sep);

				//still in neighborhood/window?
				if(deltaT<=lenInSecs) {
					outputBuffer.addSpatialData(pt);
				}else {
					//any pooint passed this point won't be inside time window
					break;
				}
			}
		}else {//searching left

			//iterate over every point right of target and keep adding them to result until we reach a 
			//point that's timestamp is outside the window
			for(int i = targetIx - 1; i >=0;i--) {
				SpatialData pt = inputData.getSpatialData(i);

				int deltaT = secondsBetweenPoints(pt,targetTime,timestampIx,sep);

				//still in neighborhood/window?
				if(deltaT<=lenInSecs) {
					outputBuffer.addSpatialData(pt);
				}else {
					//any pooint passed this point won't be inside time window
					break;
				}
			}
		}
	}
	
	//
	/**
	 * Removes any withing-harvest-pass pairs that distance is not physically posible given max speed
	 * @param inputData data to remove points that have noisy GPS coordniates
	 * @param timestampIx attribute  index of timestamp
	 * @param speedIx attribute index of speed used to determine max speed (speed should be in m/s with mphFlag = false, or mph with mphFlag = true)
	 * @param sep seperator of attribute
	 * @param maxGPSError maximum error the GPS reading should have (e.g., 2, meaning readings can be at most 2 m away from location recorded)
	 * @param mphFlag flag indicating whether speed is in miles per hour
	 * @param modeSegMod number of times duration ellapsed mode before considrered outleir (3 is recommended)
	 * @return dataset with points that have coordinate issues removed
	 */
	public static SpatialDataset removeGPSPositionErrors(SpatialDataset inputData,int timestampIx, int speedIx, String sep,double maxGPSError, boolean mphFlag, int modeSegMod) {
		
	List<SpatialDataset> segments = toHarvestPassSet(inputData,timestampIx,sep,modeSegMod);
	
	
	
	double maxSpd = -1;
	
	Iterator<SpatialData> it = inputData.iterator();
	
	while(it.hasNext()) {
		SpatialData pt = it.next();
		
		double spd = Double.parseDouble(pt.getAttributeStringValue(speedIx, sep));
		
		if(!mphFlag) {
			//convert from mph to m/s
			spd = spd/MILES_PER_HOUR_TO_METERS_PER_SECOND_DIVISOR;
		}
		if(maxSpd < spd) {
			maxSpd = spd;
		}
	}
	
	SpatialDataset res = new SpatialDataset(inputData.size());
	//int forwardnAngleIx = inputData.getNumberOfAttributes(sep);
	
	boolean foundLocationError = false;
	//make sure to splite the dataset into harvest passes/segmenets (want to avoid creating a harvest area polygon from two different passes
	//since they may not be spatially sequentiall)
	for(SpatialDataset segment : segments) {
		
		
		foundLocationError=false;
		//
		for(int i = 0;i<segment.size();i++) {
			
			//always add 1st point, nothing to compare to
			if(i==0) {
				res.addSpatialData(segment.getSpatialData(i));
				continue;
			}
			SpatialData pt1 = segment.getSpatialData(i-1);
			SpatialData pt2 = segment.getSpatialData(i);
			
			double dist = pt1.distanceTo(pt2, SpatialData.DistanceMetric.EUCLIDEAN);
			
			
			int deltaT = secondsBetweenPoints(pt1,pt2,timestampIx,sep);
			
			double threshold = (maxSpd*deltaT) + 2 *maxGPSError;
			//gps error?
			//so we aply below condition since we want to avoid ingoring a good
			//point just because its adjacent to error in forward direction.
			if(dist >threshold && ! foundLocationError) {
				foundLocationError=true;
				//outlier, don't add it
			}else {
				foundLocationError=false;
				res.addSpatialData(pt2);
			}
		}
		
		
		
	}
	return res;

}
	/**
	 * Identifies the overlap harvest points who were sampled in areas already harvested
	 * by creating a harvest area as the readings are iterated over timestamp.
	 * @param inputData entire filed harvest data that has been assumed to have GPS error revmoed (points whos distance is way to far are removed).  assumed sorted by timestamp 
	 * @param swathWidthIx index of the swath width attribute of harvested
	 * @param sep seperator of attribute values
	 * @param maxGPSError max number of meters the gps can be off
	 * @param modeSegMod number of times duration ellapsed mode before considrered outleir (3 is recommended)
	 * @return
	 */
	
	public static SpatialDataset identifyOverlap(SpatialDataset inputData,int timestampIx, int swathWidthIx,String sep,int preventFilterSegNum,double widthMod,boolean usingSwathWidthFeetFlag,int modeSegMod) {



		SpatialDataset res = new SpatialDataset(inputData.size());


		
		
		
			boolean [] outlier = _identifyOverlap(inputData, timestampIx,  swathWidthIx,sep,preventFilterSegNum,widthMod,usingSwathWidthFeetFlag,modeSegMod);

			for(int i = 0;i<inputData.size();i++) {
				SpatialData inputPt = inputData.getSpatialData(i);

				String newAttributes = inputPt.getAttributes();

				if(outlier[i]) {
					newAttributes = newAttributes + sep + "1";
				}else {
					newAttributes = newAttributes + sep + "0";
				}

				SpatialData newPt = new SpatialData(inputPt.getId(),inputPt.getLocation(),newAttributes);
				res.addSpatialData(newPt);
			

		}

		return res;
	}

	
		
	
	
	/**
	 * Identifies the overlap harvest points who were sampled in areas already harvested
	 * by creating a harvest area as the readings are iterated over timestamp
	 * @param inputData entire filed harvest data that has been assumed to have GPS error revmoed (points whos distance is way to far are removed).  assumed sorted by timestamp 
	 * @param swathWidthIx index of the swath width attribute of harvested
	 * @param sep seperator of attribute values
	 * @param preventFilterSegNum number of overlaps that can occur whitint a segement. Example, 2 means the next two points from point i will not be considered overlapping in point i
	 * @param widthMod the modifier to apply to the harvester's width
	 * @param usingSwathWidthFeetFlag flag indicating that we are using feet for swath width and have to convert from feet to meter
	 * @param modeSegMod number of times duration ellapsed mode before considrered outleir (3 is recommended) 
	 * @return
	 */
	public static boolean [] _identifyOverlap(SpatialDataset inputData,int timestampIx, int swathWidthIx,String sep,int preventFilterSegNum,double widthMod,boolean usingSwathWidthFeetFlag,int modeSegMod) {
	
	
		
				
		List<SpatialDataset> segments = toHarvestPassSet(inputData,timestampIx,sep,modeSegMod);
		return  __identifyOverlap(segments,timestampIx, swathWidthIx, sep, preventFilterSegNum, widthMod,usingSwathWidthFeetFlag);
		
	}
		
	public static boolean [] __identifyOverlap(List<SpatialDataset> segments,int timestampIx, int swathWidthIx,String sep,int preventFilterSegNum,double widthMod,boolean usingSwathWidthFeetFlag) {
		
		int numberPoints=0;
		for(SpatialDataset seg : segments) {
			numberPoints+= seg.size();
		}
		boolean [] res = new boolean[numberPoints];
				
		int sampleIx=0;
		

		//list of areas harvested (their union form the entrie harvest area)
		List<Polygon2D> harvestAreas = new ArrayList<Polygon2D>(numberPoints);
		
		List<Polygon2D> tmpSegmentHarvestAreas = new ArrayList<Polygon2D>(32);
		
				
	//	List<SpatialDataset> segments = toHarvestPassSet(inputData,timestampIx,sep);
		
		
		//int forwardnAngleIx = inputData.getNumberOfAttributes(sep);
		
		//make sure to splite the dataset into harvest passes/segmenets (want to avoid creating a harvest area polygon from two different passes
		//since they may not be spatially sequentiall)
		for(SpatialDataset segment : segments) {
			
			
			
			//boolean insideSamePolygonLock = true;
			//
			for(int i = 0;i<segment.size();i++) {
				
				int areaPreventOverlapBufferCounter = 0;
				SpatialData pt = segment.getSpatialData(i);
				
				//don't compute the harevest area for 1st point. we just check if valid vpoint
				boolean alreadyHarvested = false;
				
				//is the harvest point inside an area already harvestd?
				//check every area
				//for(Polygon2D a: harvestAreas) {
				for(int j = 0; j < harvestAreas.size();j++) {
					Polygon2D a = harvestAreas.get(j);
					Point2D coord = pt.getLocation();
										
										
					if(a.contains(coord)) {
						
						//there is a special case where we ignore the overlap if we just adde d
						//the area from last sample in sampe row
					/*	if(i>0 && j >= harvestAreas.size()-preventFilterSegNum) {
							break;					
						}*/
						
						alreadyHarvested=true;
						break;
					}else {
						//insideSamePolygonLock=false;
					}
					
				}
				
				res[sampleIx]=alreadyHarvested;
				//compute the harvest area of this point and add union
				//we can't compute harvest area of first pt. need atleast 2 points
				//dont' comptue harvest area if ignoring the reading
				if(i>0 /*&& !alreadyHarvested*/) {
					
					SpatialData from = segment.getSpatialData(i-1);
					SpatialData to = pt;//just renaming current point for clairty
					//double dist = from.distanceTo(to, SpatialData.DistanceMetric.EUCLIDEAN);
					
					Polygon2D harvestPoly =harvestPointPairToHarvestArea(from,to,sep,swathWidthIx,widthMod,usingSwathWidthFeetFlag);
					//harvestAreas.add(harvestPoly);
					
					if(areaPreventOverlapBufferCounter>= preventFilterSegNum) {
						areaPreventOverlapBufferCounter=0;
						harvestAreas.addAll(tmpSegmentHarvestAreas);
						tmpSegmentHarvestAreas.clear();
						
					}else {
						areaPreventOverlapBufferCounter++;
						tmpSegmentHarvestAreas.add(harvestPoly);
					}
				    
				}
				
				
				
				sampleIx++;
			}//end iterate over samples from segement

			if(tmpSegmentHarvestAreas.size() > 0) {
				
				harvestAreas.addAll(tmpSegmentHarvestAreas);
				
			}
			tmpSegmentHarvestAreas.clear();

		}//end iterate each segment

		
		
		
		
		return res;
		
	}
	
	
	public static Polygon2D harvestPointPairToHarvestArea(SpatialData from, SpatialData to,String sep,int swathWidthIx,double widthMod,boolean usingSwathWidthFeetFlag) {
		double angle = from.angleTo(to);
		

		double rotationAngle = angle-90;// - 90 cause the angle between swath width and points connection segement is right angle
		//create a line segment aligned in direction of the line connecting both points
		Line2D alignedFromSwathWidthSegment = toSwathWidthSegment(from,swathWidthIx,sep,angle,widthMod,usingSwathWidthFeetFlag);
	    Line2D alignedToSwathWidthSegment =toSwathWidthSegment(to,swathWidthIx,sep,angle,widthMod,usingSwathWidthFeetFlag);
	    
	    //create harvest polygon
	    Polygon2D harvestPoly = new Polygon2D();
	    harvestPoly.addPoint(new Point2D.Double(alignedFromSwathWidthSegment.getX1(), alignedFromSwathWidthSegment.getY1()));
	    harvestPoly.addPoint(new Point2D.Double(alignedFromSwathWidthSegment.getX2(), alignedFromSwathWidthSegment.getY2()));
	    harvestPoly.addPoint(new Point2D.Double(alignedToSwathWidthSegment.getX2(), alignedToSwathWidthSegment.getY2()));
	    harvestPoly.addPoint(new Point2D.Double(alignedToSwathWidthSegment.getX1(), alignedToSwathWidthSegment.getY1()));
	    harvestPoly.addPoint(new Point2D.Double(alignedFromSwathWidthSegment.getX1(), alignedFromSwathWidthSegment.getY1()));//close path
	    
	    
	    return harvestPoly;
	}
	private static Line2D toSwathWidthSegment(SpatialData pt, int swathWidthIx, String sep, double angle,double widthMod,boolean usingSwathWidthFeetFlag) {
		
		double swathWidth = Double.parseDouble(pt.getAttributeStringValue(swathWidthIx, sep));
		
		//SWATH width in feeet? 
		if(usingSwathWidthFeetFlag) {
			//convert to meters
			swathWidth = swathWidth/FEET_PER_METERS;
		}
		
		
		
		//apply modifier
		swathWidth = swathWidth* widthMod;
		Point2D coord = pt.getLocation();
		
		
		//line that represents the extend of swath width around the point (it's verticale)
		Line2D verticalSawthWidthLine = new Line2D.Double(coord.getX(),coord.getY()-swathWidth/2.0,coord.getX(),coord.getY()+swathWidth/2.0);
		

		
		
		 // Obtain an AffineTransform that describes a rotation
	    // about the angle (typically between the line segment connecting the 2 harvest points (given in radians!), around
	    // the center ot the line (the harvest sampel point). 
	    AffineTransform at = 
	        AffineTransform.getRotateInstance(
	            Math.toRadians(angle), coord.getX(), coord.getY());
	    
	    Path2D tmp = (Path2D)at.createTransformedShape(verticalSawthWidthLine);
	    
	    double [][] swathWidthPts = getPoints(tmp);
	    
	    Line2D res = new Line2D.Double(swathWidthPts[0][0],swathWidthPts[0][1],swathWidthPts[1][0],swathWidthPts[1][1]);
	    return res;
	    
	    
	}
	
	static double[][] getPoints(Path2D path) {
	    List<double[]> pointList = new ArrayList<double[]>();
	    double[] coords = new double[6];
	    int numSubPaths = 0;
	    for (PathIterator pi = path.getPathIterator(null);
	         ! pi.isDone();
	         pi.next()) {
	        switch (pi.currentSegment(coords)) {
	        case PathIterator.SEG_MOVETO:
	            pointList.add(Arrays.copyOf(coords, 2));
	            ++ numSubPaths;
	            break;
	        case PathIterator.SEG_LINETO:
	            pointList.add(Arrays.copyOf(coords, 2));
	            break;
	        case PathIterator.SEG_CLOSE:
	            if (numSubPaths > 1) {
	                throw new IllegalArgumentException("Path contains multiple subpaths");
	            }
	            return pointList.toArray(new double[pointList.size()][]);
	        default:
	            throw new IllegalArgumentException("Path contains curves");
	        }
	    }
	    //throw new IllegalArgumentException("Unclosed path");
	    return pointList.toArray(new double[pointList.size()][]);
	}

}
