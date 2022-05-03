package test;

import java.awt.geom.Point2D;
//import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.imaging.ImageReadException;
import org.junit.jupiter.api.Test;

import data_structure.MyRaster;
import data_structure.SpatialData;
import data_structure.SpatialDataset;
import io.FileHandler;
import junit.framework.Assert;

class TestMyRaster {

	private static final double DOUBLE_COMPARE_EPSILON=0.000001;
	private static final String TEST_RASTER_FILE_PATH = "test-data/example-raster.tif";
	private static final String TEST_RASTER_CSV_FILE_PATH = "test-data/example-raster.csv";
	private static final String PATH_TO_R_SCRIPT_EXE = "/path/to/R/R-4.1.3/bin/Rscript.exe";//environment dependent!
	private static final String PATH_TO_R_RASTER_INFO_SCRIPT = "r/raster-info.r";
	
	private static final int X_IX=0;
	private static final int Y_IX=1;
	private static final int PIXEL_VALUE_IX=2;
	 
	@Test
	void test_project_directory() {
		
		//testing to make sure the files necessary for testing exist
		File f = new File(TEST_RASTER_FILE_PATH);
		Assert.assertEquals(true,f.exists());
		
		f = new File(TEST_RASTER_CSV_FILE_PATH);
		Assert.assertEquals(true,f.exists());
		
		f = new File(PATH_TO_R_RASTER_INFO_SCRIPT);
		Assert.assertEquals(true,f.exists());
	}

	@Test
	void test_r_installation() {
		File f = new File(PATH_TO_R_SCRIPT_EXE);
		Assert.assertEquals(true,f.exists());
	}
	
	@Test
	void test_adjustAreaToPointCenters() throws ImageReadException, IOException {
		//public Rectangle2D adjustAreaToPointCenters(Rectangle2D rec) {
		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);
		double xmin = 438510.9;
		double xmax = 438511.76;
		double ymin = 5019647.6;
		double ymax = 5019649.35;
		Rectangle2D rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax);
		
		Assert.assertEquals(438511.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438511.75, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);
		
		Assert.assertEquals(5019647.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019649.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);
		
		
		
		
		xmin = 438508;
		xmax = 438511.76;
		ymin = 5019647.6;
		ymax = 5019649.35;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax);
		
		Assert.assertEquals(438510.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438511.75, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);
		
		Assert.assertEquals(5019647.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019649.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);
		
		
		xmin = 438510.9;
		xmax = 438520;
		ymin = 5019647.6;
		ymax = 5019649.35;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax);
		
		Assert.assertEquals(438511.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438514.25, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);
		
		Assert.assertEquals(5019647.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019649.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);
		
		
		xmin = 438510.9;
		xmax = 438511.76;
		ymin = 5019643;
		ymax = 5019649.35;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax);
		
		Assert.assertEquals(438511.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438511.75, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);
		
		Assert.assertEquals(5019645.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019649.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);
		
		
		
		
		xmin = 438510.9;
		xmax = 438511.76;
		ymin = 5019647.6;
		ymax = 5019655;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax);
		
		Assert.assertEquals(438511.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438511.75, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);
		
		Assert.assertEquals(5019647.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019650.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);
		
		

		xmin = 438510.4;
		xmax = 438513.7;
		ymin = 5019646.8;
		ymax = 5019648.4;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax);
		
		Assert.assertEquals(438510.75, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438513.25, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);
		
		Assert.assertEquals(5019647.25, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019648.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);
		
		
		xmin = 438508;
		xmax = 438520;
		ymin = 5019643;
		ymax = 5019655;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax);
		
		Assert.assertEquals(438510.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438514.25, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);
		
		Assert.assertEquals(5019645.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019650.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);
		
		
	}
	@Test
	void test_getPixelValue() throws ImageReadException, IOException {
	
		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);
		List<double[]> imgData = loadPixelsValues();
		
		for(int i = 0;i<imgData.size();i++) {
			double [] triple = imgData.get(i);
			
			double easting = triple[X_IX];
			double northing = triple[Y_IX];
			double expectedPixelValue = triple[PIXEL_VALUE_IX];
			
			double actualValue = r.getPixelValue(easting, northing, 0);
			
			Assert.assertEquals(expectedPixelValue,actualValue,DOUBLE_COMPARE_EPSILON);
		}
	}
	
	
	@Test
	void test_load_image_attributes() throws ImageReadException, IOException {
		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);
		Assert.assertEquals(10,r.getHeight());
		Assert.assertEquals(9,r.getWidth());
		Assert.assertEquals(0.5,r.getPixelSpatialScaleX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(0.5,r.getPixelSpatialScaleY(),DOUBLE_COMPARE_EPSILON);
		
		Rectangle2D rec = r.getBoundingBox();
		Assert.assertEquals(438510,rec.getMinX(),DOUBLE_COMPARE_EPSILON);		
		Assert.assertEquals(438514.5,rec.getMaxX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019645.5,rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019650.5,rec.getMaxY(),DOUBLE_COMPARE_EPSILON);
	}
	
	@Test
	void test_mean() throws ImageReadException, IOException {
		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);
		
		
		double radius_halfPixelAway = r.getPixelSpatialScaleX()/2.0;
		double radius_1PixelAway = r.getPixelSpatialScaleX();
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway = r.getPixelSpatialScaleX()*2;
		double radius_3PixelAway = r.getPixelSpatialScaleX()*3;
		
		
		Rectangle2D bb = r.getBoundingBox();
		//(28) 28 ...
		// 28 28 ...
		// ...
		
		Assert.assertEquals(28.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(28.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//...
		// 128 191 ...
		// (128) 191 ...
		
		Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		//         ....
		// ... 13 23
		// ... 13 (13)
		
		Assert.assertEquals(13,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(13,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);

		//... 191 (82)
		//    82   82
		//        ...
		
		Assert.assertEquals(82,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		
		
		// (28) (28) 28 ...
		// (28) (28) 28  ...
		//  28   82  222
		
		Assert.assertEquals((28.0+28.0+28.0+28.0)/4.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((28.0+28.0+28.0)/3.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//...
		// 128  191 191 ...
		// (128) (191) 28 ...
		// (128) (191) 191  ...
		
		Assert.assertEquals((128.0 + 128.0 + 191.0+191.0)/4.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((128.0 + 128.0 + 191.0)/3.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//...                    ...
		// ...  13      23     23
		// ...    13   (13)    (23)   
		// ...  13   (13) (13)
		
		Assert.assertEquals((13.0+13.0+13.0+23.0)/4.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((13.0+13.0+23.0)/3.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		

		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222
		
		Assert.assertEquals((82.0+82.0+82.0+191.0)/4.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((82.0+82.0+191.0)/3.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//... 222 (191) (82)
				// ...  82  (82)   (82)
				//  ...  222 82 222
				
				Assert.assertEquals((82.0+82.0+82.0+191.0)/4.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
				Assert.assertEquals((82.0+82.0+191.0)/3.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
				
				
				/*
				 * 		222	 159	159		159
				 * 		82	(222)	(159)	159
				 * 		82	(82)	(222)	159
				 * 		191	82		222	159
				 */
				Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
				Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
				
				
				
				

				
				Assert.assertEquals((222.0+159+159+159+82+222+159+159+82+82+222+159+191+82+222+159)/16.0,r.mean(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
				Assert.assertEquals((159+159+82+222+159+159+82+82+222+159+82+222)/12.0,r.mean(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
				
		
	}
	
	
	
	@Test
	void test_max() throws ImageReadException, IOException {
		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);
		
		
		double radius_halfPixelAway = r.getPixelSpatialScaleX()/2.0;
		double radius_1PixelAway = r.getPixelSpatialScaleX();
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway = r.getPixelSpatialScaleX()*2;
		double radius_3PixelAway = r.getPixelSpatialScaleX()*3;
		
		
		Rectangle2D bb = r.getBoundingBox();
		//(28) 28 ...
		// 28 28 ...
		// ...
		
		Assert.assertEquals(28.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(28.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//...
		// 128 191 ...
		// (128) 191 ...
		
		Assert.assertEquals(128,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(128,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		//         ....
		// ... 13 23
		// ... 13 (13)
		
		Assert.assertEquals(13,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(13,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);

		//... 191 (82)
		//    82   82
		//        ...
		
		Assert.assertEquals(82,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		
		
		// (28) (28) 28 ...
		// (28) (28) 28  ...
		//  28   82  222
		
		Assert.assertEquals(28.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(28.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//...
		// 128  191 191 ...
		// (128) (191) 28 ...
		// (128) (191) 191  ...
		
		Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//...                    ...
		// ...  13      23     23
		// ...    13   (13)    (23)   
		// ...  13   (13) (13)
		
		Assert.assertEquals(23.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(23.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		

		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222
		
		Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//... 222 (191) (82)
				// ...  82  (82)   (82)
				//  ...  222 82 222
				
				Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
				Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
				
				
				/*
				 * 		222	 159	159		159
				 * 		82	(222)	(159)	159
				 * 		82	(82)	(222)	159
				 * 		191	82		222	159
				 */
				Assert.assertEquals(222.0,r.max(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
				Assert.assertEquals(222.0,r.max(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
				
				
				
				

				
				Assert.assertEquals(222.0,r.max(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
				Assert.assertEquals(222.0,r.max(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
				
		
	}
	
	
	@Test
	void test_min() throws ImageReadException, IOException {
		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);
		
		
		double radius_halfPixelAway = r.getPixelSpatialScaleX()/2.0;
		double radius_1PixelAway = r.getPixelSpatialScaleX();
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway = r.getPixelSpatialScaleX()*2;
		double radius_3PixelAway = r.getPixelSpatialScaleX()*3;
		
		
		Rectangle2D bb = r.getBoundingBox();
		//(28) 28 ...
		// 28 28 ...
		// ...
		
		Assert.assertEquals(28.0,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(28.0,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//...
		// 128 191 ...
		// (128) 191 ...
		
		Assert.assertEquals(128,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(128,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		//         ....
		// ... 13 23
		// ... 13 (13)
		
		Assert.assertEquals(13,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(13,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);

		//... 191 (82)
		//    82   82
		//        ...
		
		Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		
		
		// (28) (28) 28 ...
		// (28) (28) 28  ...
		//  28   82  222
		
		Assert.assertEquals(28,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(28,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//...
		// 128  191 191 ...
		// (128) (191) 28 ...
		// (128) (191) 191  ...
		
		Assert.assertEquals(128,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(128,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//...                    ...
		// ...  13      23     23
		// ...    13   (13)    (23)   
		// ...  13   (13) (13)
		
		Assert.assertEquals(13,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(13,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		

		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222
		
		Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
		
		
		//... 222 (191) (82)
				// ...  82  (82)   (82)
				//  ...  222 82 222
				
				Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
				Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
				
				
				/*
				 * 		222	 159	159		159
				 * 		82	(222)	(159)	159
				 * 		82	(82)	(222)	159
				 * 		191	82		222	159
				 */
				Assert.assertEquals(82,r.min(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
				Assert.assertEquals(82,r.min(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
				
				
				
				

				
				Assert.assertEquals(82,r.min(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0),DOUBLE_COMPARE_EPSILON);
				Assert.assertEquals(82,r.min(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0),DOUBLE_COMPARE_EPSILON);
				
		
	}

	/**
	 * loads the pixel values from the test CSV file
	 * @return list of triples <easting/x,northing/y,pixel value>
	 * @throws IOException
	 */
	List<double[]> loadPixelsValues() throws IOException{
		
		SpatialDataset ds = FileHandler.readCSVIntoSpatialDataset(TEST_RASTER_CSV_FILE_PATH,null,",");
		
		List<double[]> res = new ArrayList<double[]>(ds.size());
		
		Iterator<SpatialData> it = ds.iterator();
		while(it.hasNext()) {
			SpatialData pt = it.next();
			
			double [] triple = new double[3];
			triple[X_IX]=pt.getLocation().getX();
			triple[Y_IX]=pt.getLocation().getY();
			String pixelValueStr = pt.getAttributes();
			triple[PIXEL_VALUE_IX]=Double.parseDouble(pixelValueStr);
			res.add(triple);
		}
		
		return res;
	}
}
