package data_structure;

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
import java.util.Iterator;

import org.apache.commons.imaging.FormatCompliance;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.formats.tiff.TiffContents;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffRasterData;
import org.apache.commons.imaging.formats.tiff.TiffReader;

import io.FileHandler;

/**
 * //https://commons.apache.org/proper/commons-imaging/index.html
 * @author Not admin
 *
 */
public class MyRaster {


	private static final int NO_DATA_FILLER_VALUE_TAG_ID =0xa481; 
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
	/**
	 * creates a raster object from a file path to a geotiff. 
	 * Avoids reading anything into memory until other API functions are called
	 * @param inputTiffFile
	 */
	public MyRaster() {


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

		if(!boundingBox.contains(easting, northing)) {
			throw new IndexOutOfBoundsException("the coordinates are outsude the bounding box. Cannot get pixel");
		}
		//note that the top left corner of an image is index (0,0), and as you increase y you go south
		int x = (int) Math.floor((easting-boundingBox.getMinX())/pixelSpatialScaleX);
		

		int y = (int) Math.floor((boundingBox.getMaxY()-northing)/pixelSpatialScaleY);

		return getPixelValue(x,y,bandIx);
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
		
		rasterToCSV(rExecutablePath,rasterInfoRScriptPath,inputTiffFile,outputCSV);
	
		
	}
	
	public static void rasterToCSV(String rExecutablePath, String rasterInfoRScriptPath, String inputTiffFile, String outputCSV) throws ImageReadException, IOException {
		MyRaster r = new MyRaster();
		r.load(rExecutablePath,rasterInfoRScriptPath,inputTiffFile);
		
		
		System.out.println("converting following raster to csv: "+outputCSV);
		System.out.println(r.toString());
		
		double xmin = r.boundingBox.getMinX();
		double ymin = r.boundingBox.getMinY();
		double xmax = r.boundingBox.getMaxX();
		double ymax = r.boundingBox.getMaxY();
		
		double xStep = r.pixelSpatialScaleX;
		double yStep = r.pixelSpatialScaleX;
		
		
		//at this point the user decided to delete output file or were safely creating a new one
		FileHandler.createFile(outputCSV);
	


	try(FileWriter fw = new FileWriter(outputCSV, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw))
	{
		
		String output;
		//we start at center of a pixel (hence the pixel scale /2), top left corner (maxy, min x)
		for(double northing = ymax- (r.pixelSpatialScaleY/2.0);northing>=ymin;northing-=yStep) {
			for(double easting = xmin+ (r.pixelSpatialScaleX/2.0);easting<=xmax;easting+=xStep) {
			
		//for(double northing = ymin+ (r.pixelSpatialScaleY/2.0);northing<=ymax;northing+=yStep) {
			//for(double easting = xmin+ (r.pixelSpatialScaleX/2.0);easting<=xmax;easting+=xStep) {
				
				float f = r.getPixelValue(easting, northing, 2);//index not used for MS data
				if(r.isRGBRaster()) {
					output = easting+" "+northing+" "+((int)f);
				}else {
					output = easting+" "+northing+" "+f;
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


}
