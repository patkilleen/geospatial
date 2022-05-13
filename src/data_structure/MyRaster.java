package data_structure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.commons.imaging.FormatCompliance;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.formats.tiff.TiffContents;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffRasterData;
import org.apache.commons.imaging.formats.tiff.TiffReader;

import core.Util;
import io.FileHandler;

/**
 * //https://commons.apache.org/proper/commons-imaging/index.html
 * @author Not admin
 *
 */
public class MyRaster {


	private static final int NO_DATA_FILLER_VALUE_TAG_ID =0xa481;
	public static final double NO_PIXEL_VALUES = -9999.0;
	
	public static final int MEAN_AGG=0;
	public static final int MAX_AGG=1;
	public static final int MIN_AGG=2;
	private boolean loaded;
	private boolean isRGBRaster;

	private Rectangle2D boundingBox;

	private double pixelSpatialScaleX;
	private double pixelSpatialScaleY;

	private int height;
	private int width;
	private double missingDataFillerValue;

	private int numBands;

	private float [] pixelValueBuffer;

	Raster r = null;//used to store RGB raster data
	TiffRasterData rasterData=null;//used to store MS raster data
	
	boolean [][] pixelInsideBoundaryFlags;
	
	Polygon2D pixelBoundary;
	/**
	 * creates a raster object from a file path to a geotiff. 
	 * Avoids reading anything into memory until other API functions are called
	 * @param pixelBoundary pixel boundary that limits which pixels to process in raster aggregations. No pixel will be included in the aggregation that is outside this boundary.
	 */
	public MyRaster(Polygon2D pixelBoundary) {

		this.pixelBoundary = pixelBoundary;
		loaded=false;
	}
	
	/**
	 * creates a raster object from a file path to a geotiff. 
	 * Avoids reading anything into memory until other API functions are called
	 */
	public MyRaster( ) {
		loaded=false;

	}
	
	/**
	 * creates a raster object from a file path to a geotiff. 
	 * Avoids reading anything into memory until other API functions are called
	 * @param pixelInsideBoundaryFlags a predifined matrix of flags indicating what pixel falls outside the proessing boundarnyc
	 */
	public MyRaster(boolean [][] pixelInsideBoundaryFlags ) {
		loaded=false;
		this.pixelInsideBoundaryFlags=pixelInsideBoundaryFlags;

	}
	
	

	public Polygon2D getPixelBoundary() {
		return pixelBoundary;
	}

	/**
	 * @param pixelBoundary pixel boundary that limits which pixels to process in raster aggregations. No pixel will be included in the aggregation that is outside this boundary.
	 * 
	 */
	public void setPixelBoundary(Polygon2D pixelBoundary) {
		this.pixelBoundary = pixelBoundary;
	}

	
	
	public boolean[][] getPixelInsideBoundaryFlags() {
		return pixelInsideBoundaryFlags;
	}

	public void setPixelInsideBoundaryFlags(boolean[][] pixelInsideBoundaryFlags) {
		this.pixelInsideBoundaryFlags = pixelInsideBoundaryFlags;
	}

	public Rectangle2D getBoundingBox() {
		return boundingBox;
	}

	

	public double getPixelSpatialScaleX() {
		return pixelSpatialScaleX;
	}


	public double getPixelSpatialScaleY() {
		return pixelSpatialScaleY;
	}



	public int getHeight() {
		return height;
	}

	

	public int getWidth() {
		return width;
	}


	public void load(String rExecutablePath, String rasterInfoRScriptPath, String inputTiffFile) throws IOException, ImageReadException {
		if(inputTiffFile==null) {
			throw new IllegalArgumentException("expected non null geotiff file path ");
		}


		loadRasterAttributes( rExecutablePath,  rasterInfoRScriptPath,  inputTiffFile);

		readRaster(inputTiffFile);


		loaded=true;
	}

	public boolean isRGBRaster() {
		return isRGBRaster;
	}

	public boolean isMSRaster() {
		return !isRGBRaster();
	}

	/**
	 * returns a pixel value at given image cell/tile
	 * @param x tile x index
	 * @param y tile y index
	 * @param bandIx the band index (0 for MS, and 0-2 for RGB)
	 * @return
	 */
	public float getPixelValue(int x, int y, int bandIx) {
		if(!loaded) {
			throw new IllegalStateException("raster not loaded, cannot get pixel value");
		}

		if(bandIx < 0) {
			throw new IndexOutOfBoundsException("band index expected to be(0 for MS, and 0-2 for RGB) but was : "+bandIx);
		}

		if(isRGBRaster) {
			if(bandIx > 2) {
				throw new IndexOutOfBoundsException("band index expected to be (0 for MS, and 0-2 for RGB) but was : "+bandIx);
			}

		}
		
		float f;
		if(!isRGBRaster) {
			f = rasterData.getValue(x, y);
		}else {
			pixelValueBuffer = r.getPixel(x, y, pixelValueBuffer);
			f = pixelValueBuffer[bandIx];
		}

		return f;
	}


	/**
	 * returns a pixel value of the cell the given UTM coordinates falls into 
	 * @param easting UTM x coordinate
	 * @param northing UTM y coordinate
	 * @param bandIx the band index (0 for MS, and 0-2 for RGB)
	 * @return
	 */
	public float getPixelValue(double easting, double northing, int bandIx) {

		//if(!boundingBox.contains(easting, northing)) { avoid using "contains" since some boundary cases are considered not in image
		if(easting < boundingBox.getMinX() || easting > boundingBox.getMaxX() || northing < boundingBox.getMinY() || northing > boundingBox.getMaxY()) {
			throw new IndexOutOfBoundsException("the coordinates are outsude the bounding box. Cannot get pixel");
		}
		//note that the top left corner of an image is index (0,0), and as you increase y you go south (so norting decreases)
		int x = eastingToXIndex(easting);		
		int y = northingToYIndex(northing);

		return getPixelValue(x,y,bandIx);
	}
	
	private int eastingToXIndex(double easting){
		 return (int) Math.floor((easting-boundingBox.getMinX())/pixelSpatialScaleX);	
	}
	
	private int northingToYIndex(double northing){
		 return  (int) Math.floor((boundingBox.getMaxY()-northing)/pixelSpatialScaleY);	
	}


	public void loadRasterAttributes(String rExecutablePath, String rasterInfoRScriptPath, String inputTiffFile) throws IOException {

		//we create a temporary file to output the raster information results from R Cran Script
		File resFile = File.createTempFile("java-geo", ".csv");
		resFile.deleteOnExit();


		ProcessBuilder pb = new ProcessBuilder(rExecutablePath,rasterInfoRScriptPath,inputTiffFile , resFile.getAbsolutePath());


		//we run the process in the temporary file's direction. I
		//pb.directory(simulationOutputDir.toFile());

		Process p;
		try {
			p = pb.start();
			p.waitFor();


		} catch (IOException | InterruptedException e) {

			e.printStackTrace();
			throw new RuntimeException("failed to output the raster spatial information: "+e.getMessage());
		}

		double xmin=-1;
		double xmax=-1;
		double ymin=-1;
		double ymax=-1;

		//read output file and parse the rast3er infromation into this object's attributes
		try (BufferedReader br = new BufferedReader(new FileReader(resFile))) {
			String line;

			//ietate over every line
			while ((line = br.readLine()) != null) {





				//parse line into spatial data point

				String [] tokens = line.split(",");

				String attributeName = tokens[0];
				String value = tokens[1];
				if(attributeName.equals("\"nrows\"")) {

					//remove the double quotes before parsing into a number
					value = value.replace("\"", "");
					height = Integer.parseInt(value);					
				}else if(attributeName.equals("\"ncols\"")) {

					//remove the double quotes before parsing into a number
					value = value.replace("\"", "");
					width = Integer.parseInt(value);
				}else if(attributeName.equals("\"xmin\"")) {

					//remove the double quotes before parsing into a number
					value = value.replace("\"", "");
					xmin = Double.parseDouble(value);
				}else if(attributeName.equals("\"xmax\"")) {

					//remove the double quotes before parsing into a number
					value = value.replace("\"", "");
					xmax = Double.parseDouble(value);
				}else if(attributeName.equals("\"ymax\"")) {

					//remove the double quotes before parsing into a number
					value = value.replace("\"", "");
					ymax = Double.parseDouble(value);
				}else if(attributeName.equals("\"ymin\"")) {

					//remove the double quotes before parsing into a number
					value = value.replace("\"", "");
					ymin = Double.parseDouble(value);

				}



			}//end iterate each line
			br.close();
		}//end reading the temoporary file with raster properties

		//altnough the pixels should be square, this computation is 
		//flexible enough to enable rectangular pixels
		pixelSpatialScaleX=(xmax-xmin)/((double)width);
		pixelSpatialScaleY=(ymax-ymin)/((double)height);



		boundingBox = new Rectangle2D.Double(xmin, ymin, xmax - xmin, ymax - ymin);
		
		//avoid re-creating the pixel boundary matrix if 
		//it was already provided
		if(pixelInsideBoundaryFlags == null) {
			loadPixelInsideBoundaryMatrix();
		}

	}


	private void readRaster(String inputTiffFile) throws ImageReadException, IOException {
		/*
		 * TODO: implement below
		 */
		isRGBRaster=false; 




		File target = new File(inputTiffFile);

		ByteSourceFile byteSource = new ByteSourceFile(target);


		TiffReader tiffReader = new TiffReader(true);

		// read the tiff directory data
		TiffContents contents = tiffReader.readDirectories(byteSource,true,FormatCompliance.getDefault());

		isRGBRaster  =false; 

		TiffDirectory directory=null;



		//were assuming there is only 1 layer in the MS imagery
		for(int i = 0;i<contents.directories.size();i++) {


			TiffDirectory tmpDirectory = contents.directories.get(i);


			if (tmpDirectory.hasTiffFloatingPointRasterData()) {
				directory=tmpDirectory;
			}

		}

		//no directory with floating point data? Therefore, has RGB data
		if (directory==null) {              
			isRGBRaster=true;
		}


		if(isRGBRaster) {
			//TiffDirectory directory=null;
			BufferedImage img = null;
			for(int i = 0;i<contents.directories.size();i++) {


				TiffDirectory tmpDirectory = contents.directories.get(i);



				img = tmpDirectory.getTiffImage();
				if (img!=null) {					
					//directory=tmpDirectory;
					break;
				}

			}//end iterate all driectoryies in image in search for a tiff image data


			if(img==null) {
				System.err.println("Cannot read buffered tiff image from Specified RGB file "+inputTiffFile+"");
				System.exit(-1);
			}
			System.out.println("Reading RGB GeoTIFF file: "+inputTiffFile+"...");

			r = img.getRaster();

			numBands = r.getNumBands();

			pixelValueBuffer = new float[numBands];

		}else {
			//MS DATA

			numBands=1;

			//look for the missing value attribute
			//
			for(int i = 0;i<contents.directories.size();i++) {


				TiffDirectory tmpDirectory = contents.directories.get(i);


				for(TiffField tf : tmpDirectory.getDirectoryEntries()) {

					int tag = tf.getTag();

					if(tag == NO_DATA_FILLER_VALUE_TAG_ID) {
						String missingValueStr = tf.getStringValue();
						missingDataFillerValue= Double.parseDouble(missingValueStr);
						break;
					}
				}


			}

			HashMap<String, Object> params = new HashMap<>();
			System.out.println("Reading MS GeoTIFF file: "+inputTiffFile+"...");
			rasterData = directory.getFloatingPointRasterData(params);


		}



	}
	
	/**
	 * Convert a given bounding rectangle to a result rectangle that makes
	 * iterating over the pixel center coordinates more straight forward.
	 * The the dimension of the result rectangle specifies the minimum and maximum
	 * X/easting and Y/northing coordinates representing pixel centers
	 * 
	 * 
	 * For example, to iterate over all pixel-center locations inside a bounding box
	 * the following for loop can be used using the dimensions of the resulting rectangle
	 * <code>
	 	for(double northing = ymax;northing>=ymin;northing-=pixelSpatialScaleY) {
			for(double easting = xmin;easting<=xmax;easting+=pixelSpatialScaleX) {
			//process each pixel-center coordiate here as (easting,northing)
			}
		}

	 * </code>
	 * 
	 * 
	 * @param xmin
	 * @param xmax
	 * @param ymin
	 * @param ymax
	 * @param boundingSearchRecBuffer an optional pre-allocated Rectangle2D object used to store internal boundary computations and save memory allocations
	 * @return
	 */
	public Rectangle2D adjustAreaToPointCenters(Rectangle2D rec,Rectangle2D boundingSearchRecBuffer) {
		
		
		
		
		
		double xmin = rec.getMinX();
		double xmax = rec.getMaxX();
		double ymin = rec.getMinY();
		double ymax = rec.getMaxY();
		return adjustAreaToPointCenters(xmin,xmax,ymin,ymax,boundingSearchRecBuffer);
		
	}
	/**
	 * Convert a given bounding rectangle to a result rectangle that makes
	 * iterating over the pixel center coordinates more straight forward.
	 * The the dimension of the result rectangle specifies the minimum and maximum
	 * X/easting and Y/northing coordinates representing pixel centers
	 * 
	 * 
	 * For example, to iterate over all pixel-center locations inside a bounding box
	 * the following for loop can be used using the dimensions of the resulting rectangle
	 * <code>
	 	for(double northing = ymax;northing>=ymin;northing-=pixelSpatialScaleY) {
			for(double easting = xmin;easting<=xmax;easting+=pixelSpatialScaleX) {
			//process each pixel-center coordiate here as (easting,northing)
			}
		}

	 * </code>
	 * 
	 * 
	 * @param xmin
	 * @param xmax
	 * @param ymin
	 * @param ymax
	 * @param boundingSearchRecBuffer an optional pre-allocated Rectangle2D object used to store internal boundary computations and save memory allocations
	 * @return
	 */
	public Rectangle2D adjustAreaToPointCenters(double xmin,double xmax,double ymin,double ymax,Rectangle2D boundingSearchRecBuffer) {		
		double halfXPixelWidth = pixelSpatialScaleX/2.0;
		double halfYPixelWidth = pixelSpatialScaleY/2.0;
		
		
		double ptCenterLeftLimit=boundingBox.getMinX()+halfXPixelWidth;
		double ptCenterRightLimit= boundingBox.getMaxX()-halfXPixelWidth;
		double ptCenterBottomLimit=boundingBox.getMinY()+halfYPixelWidth;
		double ptCenterUpperLimit=boundingBox.getMaxY()-halfYPixelWidth;
		
		//make sure the  area is limited to be insided the raster's extent
		xmin = Math.max(xmin,ptCenterLeftLimit );		
		xmax = Math.min(xmax,ptCenterRightLimit);
		ymin = Math.max(ymin, ptCenterBottomLimit);
		ymax = Math.min(ymax,ptCenterUpperLimit );

		
		
		int pixelCount = (int) Math.ceil((xmin-ptCenterLeftLimit)/pixelSpatialScaleX);
		xmin = ptCenterLeftLimit + (pixelCount*pixelSpatialScaleX);
		
		pixelCount = (int) Math.floor((xmax-ptCenterLeftLimit)/pixelSpatialScaleX);
		xmax = ptCenterLeftLimit + (pixelCount*pixelSpatialScaleX);
		
		
		pixelCount = (int) Math.ceil((ymin-ptCenterBottomLimit)/pixelSpatialScaleY);
		ymin = ptCenterBottomLimit + (pixelCount*pixelSpatialScaleY);
		
		pixelCount = (int) Math.floor((ymax-ptCenterBottomLimit)/pixelSpatialScaleY);
		ymax = ptCenterBottomLimit + (pixelCount*pixelSpatialScaleY);
		
		
		//no pre-allocated rectange2d
		if(boundingSearchRecBuffer == null) {
			return new Rectangle2D.Double(xmin,ymin,xmax-xmin,ymax-ymin);
		}else {
			boundingSearchRecBuffer.setFrame(xmin, ymin, xmax-xmin,ymax-ymin);
			return boundingSearchRecBuffer;
		}
	}
	
	/**
	 * Computes the mean value of pixels who's location (pixel center) fall within a radius around a point center.
	 * @param center the center of the neighbordhood to compute the mean
	 * @param radius radius around center to decide what pixels to be included
	 * @param distMetric distance metric used to define neighborhodd shape
	 * @param bandIx the band index to use in mean (ignored for multispectral data that store data in floating point format). For RGB 0= red, 1 = green, 2 = blue.
	 * @param boundingSearchRecBuffer an optional pre-allocated Rectangle2D object used to store internal boundary computations and save memory allocations
	 * @return the mean of pixel values in neighborhood or -9999 if nieghborhood empty
	 */
	public double mean(Point2D center, double radius, SpatialData.DistanceMetric distMetric, int bandIx,Rectangle2D boundingSearchRecBuffer) {
		return _aggregate(center,radius,distMetric,bandIx,MEAN_AGG,boundingSearchRecBuffer);
	}
	/**
	 * Computes the max value of pixels who's location (pixel center) fall within a radius around a point center.
	 * @param center the center of the neighbordhood to compute the max
	 * @param radius radius around center to decide what pixels to be included
	 * @param distMetric distance metric used to define neighborhodd shape
	 * @param bandIx the band index to use in max (ignored for multispectral data that store data in floating point format). For RGB 0= red, 1 = green, 2 = blue.
	 * @param boundingSearchRecBuffer an optional pre-allocated Rectangle2D object used to store internal boundary computations and save memory allocations
	 * @return the max of pixel values in neighborhood or -9999 if nieghborhood empty
	 */
	public double max(Point2D center, double radius, SpatialData.DistanceMetric distMetric, int bandIx,Rectangle2D boundingSearchRecBuffer) {
		return _aggregate(center,radius,distMetric,bandIx,MAX_AGG,boundingSearchRecBuffer);
	}
	
	/**
	 * Computes the min value of pixels who's location (pixel center) fall within a radius around a point center.
	 * @param center the center of the neighbordhood to compute the min
	 * @param radius radius around center to decide what pixels to be included
	 * @param distMetric distance metric used to define neighborhodd shape
	 * @param bandIx the band index to use in min (ignored for multispectral data that store data in floating point format). For RGB 0= red, 1 = green, 2 = blue.
	 * @param boundingSearchRecBuffer an optional pre-allocated Rectangle2D object used to store internal boundary computations and save memory allocations
	 * @return the min of pixel values in neighborhood or -9999 if nieghborhood empty
	 */
	public double min(Point2D center, double radius, SpatialData.DistanceMetric distMetric, int bandIx,Rectangle2D boundingSearchRecBuffer) {
		return _aggregate(center,radius,distMetric,bandIx,MIN_AGG,boundingSearchRecBuffer);
	}
	
	
	/**
	 * Computes an aggregation value of pixels who's location (pixel center) fall within a radius around a point center.
	 * Pixels outside a given sub-area of the raster are not considered (e.g., outside the boundaries of a farmer's field 
	 * where crops do not grow).
	 * @param center the center of the neighbordhood to compute the aggregate
	 * @param radius radius around center to decide what pixels to be included
	 * @param distMetric distance metric used to define neighborhodd shape
	 * @param bandIx the band index to use in aggregation (ignored for multispectral data that store data in floating point format). For RGB 0= red, 1 = green, 2 = blue.
	 * @param aggOp aggregation operator  (maximum: <code>MAX_AGG</code>, minimum: <code>MIN_AGG</code>, or MEAN: <code>MEAN_AGG</code>)
	 * @param pixelBoundary boundary to apply to pixel search. No pixel will be processed that is outside this boundary.
	 * @param boundingSearchRecBuffer an optional pre-allocated Rectangle2D object used to store internal boundary computations and save memory allocations 
	 * @return the aggregation result of pixel values in neighborhood or <code>NO_PIXEL_VALUES </code> if nieghborhood is empty
	 */
	public double _aggregate(Point2D center, double radius, SpatialData.DistanceMetric distMetric, int bandIx, int aggOp,Rectangle2D boundingSearchRecBuffer) {
		
		if(!loaded) {
			throw new IllegalStateException("cannot perform aggregations on Raster that hasn't been loaded into memory");
		}
		
		double aggRes = 0;
		
		int numPixelsInArea = 0;
		
		if (aggOp == MAX_AGG) {
			aggRes=Float.MIN_VALUE;
		}else if (aggOp == MIN_AGG) {
			aggRes=Double.MAX_VALUE;
		} 
		//SpatialData.DistanceMetric.EUCLIDEAN
		
		boolean ignoringNODATAValues=false;
		
		//multispectral imagery?
		if(!isRGBRaster) {
			ignoringNODATAValues=true;			
		}
		
		double cx = center.getX();
		double cy = center.getY();
		double xmin =cx - radius;
		double xmax = cx + radius;
		double ymin = cy - radius;
		double ymax = cy+radius;
		
		Rectangle2D boundingSearchRec =adjustAreaToPointCenters(xmin,xmax,ymin,ymax,boundingSearchRecBuffer);
		
		xmin = boundingSearchRec.getMinX();
		xmax=boundingSearchRec.getMaxX();
		ymin = boundingSearchRec.getMinY();
		ymax = boundingSearchRec.getMaxY();
		
				 
		
		Point2D pt = new Point2D.Double(0,0);
		//iterate over every point inside this area, ignoring NO_DATA values
		//we start at top left corner of area (maxy, min x)
		for(double northing = ymax;northing>=ymin;northing-=pixelSpatialScaleY) {
			for(double easting = xmin;easting<=xmax;easting+=pixelSpatialScaleX) {
				
				int x = eastingToXIndex(easting);		
				int y = northingToYIndex(northing);

				
				//non null matrix of  means boundary defined to determine wheat poitn to include in aggregations
				if(pixelInsideBoundaryFlags != null) {
					//pixel location falls outside the boundary? 
					if(!pixelInsideBoundaryFlags[x][y]) {
						continue;
					}
				}
			
				boolean insideNeighborhoodFlag = false;
				if(distMetric == SpatialData.DistanceMetric.INFINITY_NORM) {
					insideNeighborhoodFlag=true;//by definition, the bounding box is the nieghborhood
				}else {
					//check if point inside neighborhood					
					pt.setLocation(easting,northing);
					double dist = Util.distanceTo(center, pt, distMetric);
					
					//anything withing the radius around center is considered a neighbor
					insideNeighborhoodFlag =dist <= radius;
					 
				}
				
				//do we include pixel value in mean
				if(insideNeighborhoodFlag) {
					float f = getPixelValue(easting, northing, bandIx);
					
					//we ignoreing missing values?
					if(ignoringNODATAValues) {
						if(f == missingDataFillerValue) {
							continue;//a NO_DATA value, skip to next pixel
						}
					}
					numPixelsInArea++;
					
					if(aggOp ==MEAN_AGG) {
						aggRes += f;
					}else if(aggOp ==MAX_AGG) {
						
						//found bigger pixel value?
						if(f>aggRes) {
							aggRes = f;	
						}
					}else if(aggOp ==MIN_AGG) {
						
						//found smaller pixel value?
						if(f<aggRes) {
							aggRes = f;	
						}
					}
				}
				
				
			}	//end iterate over X axis
		}//end iterate over y axis
				
		//there weren't any pixels in neighborhood?
		if(numPixelsInArea==0) {
			aggRes=NO_PIXEL_VALUES;
		}else {
			
			//apply final step to aggregation
			if(aggOp ==MEAN_AGG) {
				aggRes = aggRes/((double)numPixelsInArea);
			}
			//nothing to do for min and max,  final result already stored in aggRes
		}
		

		return aggRes;
	}

	public String toString() {
		String res = "width: "+width+"\n";
		res += "height: "+height+"\n";
		res += "extent(xmin,ymin,xmax,ymax): ("+boundingBox.getMinX()+","+boundingBox.getMinY()+","+boundingBox.getMaxX()+","+boundingBox.getMaxY()+")\n";
		res += "pixel scale:  "+pixelSpatialScaleX+"m x "+pixelSpatialScaleY+" m\n";
		if(isRGBRaster) {
			res += "imagery type: RGB\n";
			res += "number of bands: "+numBands;
		}else {
			res += "imagery type: MS\n";
			res += "NO_DATA filler value: "+missingDataFillerValue;
		}
		
		return res;
		
	}
	public static void main(String [] args) throws ImageReadException, IOException {
		String rExecutablePath=args[0];
		String rasterInfoRScriptPath = args[1];
		String inputTiffFile=args[2];
		String outputCSV = args[3];
		int bandIx = Integer.parseInt(args[4]);
		rasterToCSV(rExecutablePath,rasterInfoRScriptPath,inputTiffFile,outputCSV,bandIx);
	
		
	}
	
	
	/**
	 * Reads a raster file and converts it to CSV (note that the output file will be approx. 40 times larger than 
	 * the Raster since 2 coordinates will be stored for each pixel)
	 * @param rExecutablePath path to R Cran programming executable. E.g.: "/home/user/R/bin/Rscript.exe
	 * @param rasterInfoRScriptPath path to the R script that will summarize the raster's info: <project root>/r/raster-info.r
	 * @param inputTiffFile path to GeoTIFF raster file to convert to CSV
	 * @param outputCSV output CSV file path
	 * @param bandIx the band index to include in the CSV file (ignored for multispectral data that store data in floating point format). For RGB 0= red, 1 = green, 2 = blue. 
	 * @throws ImageReadException
	 * @throws IOException
	 */
	public static void rasterToCSV(String rExecutablePath, String rasterInfoRScriptPath, String inputTiffFile, String outputCSV, int bandIx) throws ImageReadException, IOException {
		MyRaster r = new MyRaster();
		r.load(rExecutablePath,rasterInfoRScriptPath,inputTiffFile);
		
		
		System.out.println("converting following raster to csv: "+outputCSV);
		System.out.println(r.toString());
		
		double xmin = r.boundingBox.getMinX();
		double ymin = r.boundingBox.getMinY();
		double xmax = r.boundingBox.getMaxX();
		double ymax = r.boundingBox.getMaxY();
		
		double xStep = r.pixelSpatialScaleX;
		double yStep = r.pixelSpatialScaleY;
		
		
		//at this point the user decided to delete output file or were safely creating a new one
		FileHandler.createFile(outputCSV);
	


	try(FileWriter fw = new FileWriter(outputCSV, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw))
	{
		
		out.println("X,Y,band_"+bandIx);
		String output;
		//we start at center of a pixel (hence the pixel scale /2), top left corner (maxy, min x)
		for(double northing = ymax- (r.pixelSpatialScaleY/2.0);northing>=ymin;northing-=yStep) {
			for(double easting = xmin+ (r.pixelSpatialScaleX/2.0);easting<=xmax;easting+=xStep) {
			
		//for(double northing = ymin+ (r.pixelSpatialScaleY/2.0);northing<=ymax;northing+=yStep) {
			//for(double easting = xmin+ (r.pixelSpatialScaleX/2.0);easting<=xmax;easting+=xStep) {
				
				float f = r.getPixelValue(easting, northing, bandIx);//index not used for MS data
				if(r.isRGBRaster()) {
					output = easting+","+northing+","+((int)f);
				}else {
					output = easting+","+northing+","+f;
				}
				
				out.println(output);
			}	
		}
		//out.println(outputCSVHeader);
			
	
		
		out.close();
	} catch (IOException e) {
		//exception handling left as an exercise for the reader
		throw e;
	}

	}


	/**
	 * Loads the internal buffer that holds boolean flags
	 * to track which pixel falls inside the given (if any provided) pix3el boundary <code>pixelBoundary</code>
	 * which is required when choosing which picxel is processed in aggregations
	 * 
	 * 
	 * 
	 * When no boundary is provided (<code>pixelBoundary=null</code>), all pixels are processed
	 * When a boundary is provided (via constructor or  <code>setPixelBoundary</code> then
	 *  points inside this boundary determined by this function indicated which 
	 *  pixel to exlucde from aggregations
	 *  
	 *  
	 *  Note that this function can take a considerable amount of time for large rasters.
	 *  TODO: spead up using mutliple threads
	 */
	public void loadPixelInsideBoundaryMatrix() {
		
		if(pixelBoundary== null) {
			//all points consider inside boundary of entire raster
			pixelInsideBoundaryFlags=null;
			return;
		}
		
		System.out.println("creating pixel inside boundary flag matrix...");
		
		double xmax = boundingBox.getMaxX();
		double xmin = boundingBox.getMinX();
		double ymax = boundingBox.getMaxY();
		double ymin = boundingBox.getMinY();

		
		Rectangle2D boundingSearchRec =adjustAreaToPointCenters(xmin,xmax,ymin,ymax,null);
		
		xmin = boundingSearchRec.getMinX();
		xmax=boundingSearchRec.getMaxX();
		ymin = boundingSearchRec.getMinY();
		ymax = boundingSearchRec.getMaxY();
		

		
		//create matrix of flags to hold booleans that indicate whether a pixel fallsindise given boundary
		pixelInsideBoundaryFlags = new boolean[width][height];

		//we start at top left corner of area (maxy, min x)
		//iterate over every pixel location 
		for(double northing = ymax;northing>=ymin;northing-=pixelSpatialScaleY) {
			for(double easting = xmin;easting<=xmax;easting+=pixelSpatialScaleX) {

				int x = eastingToXIndex(easting);		
				int y = northingToYIndex(northing);


				//we have to check whether pixel location falls inside the boundary and log result 
				pixelInsideBoundaryFlags[x][y] = pixelBoundary.contains(easting, northing);
				



			}//end inner loop over esting
		}//end northing loop
	}//end func


	

}
