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
import data_structure.Polygon2D;
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
		Rectangle2D rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax,null);

		Assert.assertEquals(438511.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438511.75, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);

		Assert.assertEquals(5019647.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019649.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);




		xmin = 438508;
		xmax = 438511.76;
		ymin = 5019647.6;
		ymax = 5019649.35;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax,rec);

		Assert.assertEquals(438510.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438511.75, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);

		Assert.assertEquals(5019647.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019649.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);


		xmin = 438510.9;
		xmax = 438520;
		ymin = 5019647.6;
		ymax = 5019649.35;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax,rec);

		Assert.assertEquals(438511.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438514.25, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);

		Assert.assertEquals(5019647.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019649.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);


		xmin = 438510.9;
		xmax = 438511.76;
		ymin = 5019643;
		ymax = 5019649.35;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax,rec);

		Assert.assertEquals(438511.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438511.75, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);

		Assert.assertEquals(5019645.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019649.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);




		xmin = 438510.9;
		xmax = 438511.76;
		ymin = 5019647.6;
		ymax = 5019655;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax,rec);

		Assert.assertEquals(438511.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438511.75, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);

		Assert.assertEquals(5019647.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019650.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);



		xmin = 438510.4;
		xmax = 438513.7;
		ymin = 5019646.8;
		ymax = 5019648.4;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax,rec);

		Assert.assertEquals(438510.75, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438513.25, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);

		Assert.assertEquals(5019647.25, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019648.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);


		xmin = 438508;
		xmax = 438520;
		ymin = 5019643;
		ymax = 5019655;
		rec = r.adjustAreaToPointCenters(xmin,xmax,ymin,ymax,rec);

		Assert.assertEquals(438510.25, rec.getMinX(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(438514.25, rec.getMaxX(),DOUBLE_COMPARE_EPSILON);

		Assert.assertEquals(5019645.75, rec.getMinY(),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(5019650.25, rec.getMaxY(),DOUBLE_COMPARE_EPSILON);


	}
	@Test
	void test_getPixelValue() throws ImageReadException, IOException {


		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);

		float [] pixelValueBuffer = new float[r.getNumBands()];
		List<double[]> imgData = loadPixelsValues();

		for(int i = 0;i<imgData.size();i++) {
			double [] triple = imgData.get(i);

			double easting = triple[X_IX];
			double northing = triple[Y_IX];
			double expectedPixelValue = triple[PIXEL_VALUE_IX];

			double actualValue = r.getPixelValue(easting, northing, 0,pixelValueBuffer);

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
		float [] pixelValueBuffer = new float[r.getNumBands()];

		double radius_halfPixelAway = r.getPixelSpatialScaleX()/2.0;
		double radius_1PixelAway = r.getPixelSpatialScaleX();
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway = r.getPixelSpatialScaleX()*2;
		double radius_3PixelAway = r.getPixelSpatialScaleX()*3;


		Rectangle2D bb = r.getBoundingBox();
		//(28) 28 ...
		// 28 28 ...
		// ...

		Assert.assertEquals(28.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(28.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//...
		// 128 191 ...
		// (128) 191 ...

		Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

		//         ....
		// ... 13 23
		// ... 13 (13)

		Assert.assertEquals(13,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(13,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

		//... 191 (82)
		//    82   82
		//        ...

		Assert.assertEquals(82,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);




		// (28) (28) 28 ...
		// (28) (28) 28  ...
		//  28   82  222

		Assert.assertEquals((28.0+28.0+28.0+28.0)/4.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((28.0+28.0+28.0)/3.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//...
		// 128  191 191 ...
		// (128) (191) 28 ...
		// (128) (191) 191  ...

		Assert.assertEquals((128.0 + 128.0 + 191.0+191.0)/4.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((128.0 + 128.0 + 191.0)/3.0,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//...                    ...
		// ...  13      23     23
		// ...    13   (13)    (23)   
		// ...  13   (13) (13)

		Assert.assertEquals((13.0+13.0+13.0+23.0)/4.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((13.0+13.0+23.0)/3.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);




		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222

		Assert.assertEquals((82.0+82.0+82.0+191.0)/4.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((82.0+82.0+191.0)/3.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222

		Assert.assertEquals((82.0+82.0+82.0+191.0)/4.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((82.0+82.0+191.0)/3.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		/*
		 * 		222	 159	159		159
		 * 		82	(222)	(159)	159
		 * 		82	(82)	(222)	159
		 * 		191	82		222	159
		 */
		Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);






		Assert.assertEquals((222.0+159+159+159+82+222+159+159+82+82+222+159+191+82+222+159)/16.0,r.mean(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((159+159+82+222+159+159+82+82+222+159+82+222)/12.0,r.mean(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


	}



	@Test
	void test_max() throws ImageReadException, IOException {
		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);

		float [] pixelValueBuffer = new float[r.getNumBands()];

		double radius_halfPixelAway = r.getPixelSpatialScaleX()/2.0;
		double radius_1PixelAway = r.getPixelSpatialScaleX();
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway = r.getPixelSpatialScaleX()*2;
		double radius_3PixelAway = r.getPixelSpatialScaleX()*3;


		Rectangle2D bb = r.getBoundingBox();
		//(28) 28 ...
		// 28 28 ...
		// ...

		Assert.assertEquals(28.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(28.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//...
		// 128 191 ...
		// (128) 191 ...

		Assert.assertEquals(128,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(128,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

		//         ....
		// ... 13 23
		// ... 13 (13)

		Assert.assertEquals(13,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(13,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

		//... 191 (82)
		//    82   82
		//        ...

		Assert.assertEquals(82,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);




		// (28) (28) 28 ...
		// (28) (28) 28  ...
		//  28   82  222

		Assert.assertEquals(28.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(28.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//...
		// 128  191 191 ...
		// (128) (191) 28 ...
		// (128) (191) 191  ...

		Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//...                    ...
		// ...  13      23     23
		// ...    13   (13)    (23)   
		// ...  13   (13) (13)

		Assert.assertEquals(23.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(23.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);




		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222

		Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222

		Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(191.0,r.max(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		/*
		 * 		222	 159	159		159
		 * 		82	(222)	(159)	159
		 * 		82	(82)	(222)	159
		 * 		191	82		222	159
		 */
		Assert.assertEquals(222.0,r.max(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(222.0,r.max(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);






		Assert.assertEquals(222.0,r.max(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(222.0,r.max(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


	}


	@Test
	void test_min() throws ImageReadException, IOException {
		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);

		float [] pixelValueBuffer = new float[r.getNumBands()];
		double radius_halfPixelAway = r.getPixelSpatialScaleX()/2.0;
		double radius_1PixelAway = r.getPixelSpatialScaleX();
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway = r.getPixelSpatialScaleX()*2;
		double radius_3PixelAway = r.getPixelSpatialScaleX()*3;


		Rectangle2D bb = r.getBoundingBox();
		//(28) 28 ...
		// 28 28 ...
		// ...

		Assert.assertEquals(28.0,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(28.0,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//...
		// 128 191 ...
		// (128) 191 ...

		Assert.assertEquals(128,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(128,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

		//         ....
		// ... 13 23
		// ... 13 (13)

		Assert.assertEquals(13,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(13,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

		//... 191 (82)
		//    82   82
		//        ...

		Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);




		// (28) (28) 28 ...
		// (28) (28) 28  ...
		//  28   82  222

		Assert.assertEquals(28,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(28,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//...
		// 128  191 191 ...
		// (128) (191) 28 ...
		// (128) (191) 191  ...

		Assert.assertEquals(128,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(128,r.min(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//...                    ...
		// ...  13      23     23
		// ...    13   (13)    (23)   
		// ...  13   (13) (13)

		Assert.assertEquals(13,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(13,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);




		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222

		Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222

		Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.min(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		/*
		 * 		222	 159	159		159
		 * 		82	(222)	(159)	159
		 * 		82	(82)	(222)	159
		 * 		191	82		222	159
		 */
		Assert.assertEquals(82,r.min(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.min(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);






		Assert.assertEquals(82,r.min(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82,r.min(new Point2D.Double(438512,5019648), radius_2PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


	}


	@Test
	void test_aggregation_outside_raster_extent() throws ImageReadException, IOException {
		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);

		float [] pixelValueBuffer = new float[r.getNumBands()];
		double radius_halfPixelAway = r.getPixelSpatialScaleX()/2.0;
		double radius_1PixelAway = r.getPixelSpatialScaleX();
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway = r.getPixelSpatialScaleX()*2;
		double radius_3PixelAway = r.getPixelSpatialScaleX()*3;


		Rectangle2D bb = r.getBoundingBox();
		//(28) 28 ...
		// 28 28 ...
		// ...

		Assert.assertEquals(MyRaster.NO_PIXEL_VALUES,r.mean(new Point2D.Double(bb.getMinX()-100*radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(MyRaster.NO_PIXEL_VALUES,r.mean(new Point2D.Double(bb.getMinX()-100*radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

		Assert.assertEquals(MyRaster.NO_PIXEL_VALUES,r.max(new Point2D.Double(bb.getMinX()-100*radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(MyRaster.NO_PIXEL_VALUES,r.max(new Point2D.Double(bb.getMinX()-100*radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

		Assert.assertEquals(MyRaster.NO_PIXEL_VALUES,r.min(new Point2D.Double(bb.getMinX()-100*radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(MyRaster.NO_PIXEL_VALUES,r.min(new Point2D.Double(bb.getMinX()-100*radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), 0, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
	}

	@Test
	void test_aggregation_with_boundary_() throws ImageReadException, IOException {



		double radius_halfPixelAway =0.5/2.0;
		double radius_1PixelAway = 0.5;
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway =0.5*2;
		double radius_3PixelAway = 0.5*3;

		Polygon2D pixelBoundary = new Polygon2D();
		pixelBoundary.addPoint((float)(438511.0f-radius_1PixelAway), 5019650.0f);//top left inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438514.0f, 5019650.0f);//top right inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438514.0f, 5019646.0f);//bottom right inner corner (1 pixel away from border)
		pixelBoundary.addPoint((float)(438511.0f-radius_1PixelAway), 5019646.0f);//bottom left inner corner (1 pixel away from border)
		pixelBoundary.addPoint((float)(438511.0f-radius_1PixelAway), 5019650.0f);//close path of polygon


		MyRaster r = new MyRaster(pixelBoundary);
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);


		float [] pixelValueBuffer = new float[r.getNumBands()];

		Rectangle2D bb = r.getBoundingBox();



		//TOP LEFT CORNER

		//[28] [28] [28] 191 ....
		// [28] (28) (28) 191 ...
		// [28] (82) (222) 222 ... 
		// 128 82 222   159 ...
		//...


		Assert.assertEquals((28.0+28.0+82.0+222.0)/4.0,r.mean(new Point2D.Double(438511-radius_halfPixelAway,5019650 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((28.0+28.0+82.0)/3.0,r.mean(new Point2D.Double(438511-radius_halfPixelAway,5019650 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//TOP RIGHT CORNER
		/*
		 * ... 		222 	[222]	[191]	[82]
		 * ... 		222		(82)	(82)	[82]
		 * ...		159		(222)	(82)	[222]
		 * ...		159		159		222		222
		 *....								...
		 */

		Assert.assertEquals((82.0 +82.0 + 222.0 + 82.0)/4.0,r.mean(new Point2D.Double(438514-radius_halfPixelAway,5019650 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((82.0+82.0+82.0)/3.0,r.mean(new Point2D.Double(438514-radius_halfPixelAway,5019650 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);



		//BOT RIGHT CORNER
		/*
		 * ...								...
		 * ... 		159 	99		13		13
		 * ... 		99		(13)	(23)	[23]
		 * ...		99		(13)	(13)	[23]
		 * ...		159		[13]	[13]	[13]
		 */

		Assert.assertEquals((13.0 +13.0 + 13.0 + 23.0)/4.0,r.mean(new Point2D.Double(438514-radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((13.0+13.0+23.0)/3.0,r.mean(new Point2D.Double(438514-radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//BOT LEFT CORNER
		/*
		 *			...						...
		 *  		128 		28		191		82	...
		 *  		[128]		(191)	(191)	82	...
		 * 			[128]		(191)	(28)	82	...
		 * 			[128]		[191]	[191]	82	...
		 */

		Assert.assertEquals((191.0 +191.0 + 191.0 + 28.0)/4.0,r.mean(new Point2D.Double(438511-radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((191.0+191.0+28.0)/3.0,r.mean(new Point2D.Double(438511-radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//BOT CENTER
		/*
		 * 		191		82		222		159		99 ...
		 * ...	191		(82)	(222)	(99	)	13			...
		 * ... 28		(82)	(222)	(99) 	13...	
		 *... 191		[82]	[222]	[159]	13		...
		 */

		Assert.assertEquals((82.0+82.0+222.0+222.0+99.0+99.0)/6.0,r.mean(new Point2D.Double(438512+radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((82.0+222.0+222.0+99.0)/4.0,r.mean(new Point2D.Double(438512+radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);





		pixelBoundary = new Polygon2D();
		pixelBoundary.addPoint(438512.0f, (float)(5019648+radius_1PixelAway));//top left inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438513.0f, (float)(5019648+radius_1PixelAway));//top right inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438513.0f, (float)(5019647.0f+radius_1PixelAway));//bottom right inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438512.0f, (float)(5019647.0f+radius_1PixelAway));//bottom left inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438512.0f, (float)(5019648+radius_1PixelAway));//close the polygon
		r.setPixelBoundary(pixelBoundary);
		r.loadPixelInsideBoundaryMatrix();
		//CENTER
		/*
		 * 		159		159			159		159		222 ...
		 * ...	222		(159)		(159)	[159]		159			...
		 * ... 	82		(222)		(159)	[99] 		99...	
		 *... 	82		[222]		[159]	[99]		13		...
		 */

		Assert.assertEquals((159.0+159.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438513-radius_halfPixelAway,5019648 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((159.0+159.0+222.0)/3.0,r.mean(new Point2D.Double(438513-radius_halfPixelAway,5019648 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);





	}


	@Test
	void test_aggregation_with_boundary_with_predfined_pixel_boundary_matrix() throws ImageReadException, IOException {



		double radius_halfPixelAway =0.5/2.0;
		double radius_1PixelAway = 0.5;
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway =0.5*2;
		double radius_3PixelAway = 0.5*3;

		Polygon2D pixelBoundary = new Polygon2D();
		pixelBoundary.addPoint((float)(438511.0f-radius_1PixelAway), 5019650.0f);//top left inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438514.0f, 5019650.0f);//top right inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438514.0f, 5019646.0f);//bottom right inner corner (1 pixel away from border)
		pixelBoundary.addPoint((float)(438511.0f-radius_1PixelAway), 5019646.0f);//bottom left inner corner (1 pixel away from border)
		pixelBoundary.addPoint((float)(438511.0f-radius_1PixelAway), 5019650.0f);//close path of polygon


		MyRaster r = new MyRaster(pixelBoundary);

		//this initially creates the pixel boundary flag matrix 
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);

		boolean [][]flagMatrix =r.getPixelInsideBoundaryFlags();


		//reload it to simulate all ready available matrix of pixel boundary flags 
		r = new MyRaster(flagMatrix);

		//this initially creates the pixel boundary flag matrix 
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);


		Rectangle2D bb = r.getBoundingBox();

		float [] pixelValueBuffer = new float[r.getNumBands()];

		//TOP LEFT CORNER

		//[28] [28] [28] 191 ....
		// [28] (28) (28) 191 ...
		// [28] (82) (222) 222 ... 
		// 128 82 222   159 ...
		//...


		Assert.assertEquals((28.0+28.0+82.0+222.0)/4.0,r.mean(new Point2D.Double(438511-radius_halfPixelAway,5019650 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((28.0+28.0+82.0)/3.0,r.mean(new Point2D.Double(438511-radius_halfPixelAway,5019650 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//TOP RIGHT CORNER
		/*
		 * ... 		222 	[222]	[191]	[82]
		 * ... 		222		(82)	(82)	[82]
		 * ...		159		(222)	(82)	[222]
		 * ...		159		159		222		222
		 *....								...
		 */

		Assert.assertEquals((82.0 +82.0 + 222.0 + 82.0)/4.0,r.mean(new Point2D.Double(438514-radius_halfPixelAway,5019650 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((82.0+82.0+82.0)/3.0,r.mean(new Point2D.Double(438514-radius_halfPixelAway,5019650 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);



		//BOT RIGHT CORNER
		/*
		 * ...								...
		 * ... 		159 	99		13		13
		 * ... 		99		(13)	(23)	[23]
		 * ...		99		(13)	(13)	[23]
		 * ...		159		[13]	[13]	[13]
		 */

		Assert.assertEquals((13.0 +13.0 + 13.0 + 23.0)/4.0,r.mean(new Point2D.Double(438514-radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((13.0+13.0+23.0)/3.0,r.mean(new Point2D.Double(438514-radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//BOT LEFT CORNER
		/*
		 *			...						...
		 *  		128 		28		191		82	...
		 *  		[128]		(191)	(191)	82	...
		 * 			[128]		(191)	(28)	82	...
		 * 			[128]		[191]	[191]	82	...
		 */

		Assert.assertEquals((191.0 +191.0 + 191.0 + 28.0)/4.0,r.mean(new Point2D.Double(438511-radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((191.0+191.0+28.0)/3.0,r.mean(new Point2D.Double(438511-radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//BOT CENTER
		/*
		 * 		191		82		222		159		99 ...
		 * ...	191		(82)	(222)	(99	)	13			...
		 * ... 28		(82)	(222)	(99) 	13...	
		 *... 191		[82]	[222]	[159]	13		...
		 */

		Assert.assertEquals((82.0+82.0+222.0+222.0+99.0+99.0)/6.0,r.mean(new Point2D.Double(438512+radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((82.0+222.0+222.0+99.0)/4.0,r.mean(new Point2D.Double(438512+radius_halfPixelAway,5019646 + radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);





		pixelBoundary = new Polygon2D();
		pixelBoundary.addPoint(438512.0f, (float)(5019648+radius_1PixelAway));//top left inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438513.0f, (float)(5019648+radius_1PixelAway));//top right inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438513.0f, (float)(5019647.0f+radius_1PixelAway));//bottom right inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438512.0f, (float)(5019647.0f+radius_1PixelAway));//bottom left inner corner (1 pixel away from border)
		pixelBoundary.addPoint(438512.0f, (float)(5019648+radius_1PixelAway));//close the polygon
		r.setPixelBoundary(pixelBoundary);
		r.loadPixelInsideBoundaryMatrix();
		//CENTER
		/*
		 * 		159		159			159		159		222 ...
		 * ...	222		(159)		(159)	[159]		159			...
		 * ... 	82		(222)		(159)	[99] 		99...	
		 *... 	82		[222]		[159]	[99]		13		...
		 */

		Assert.assertEquals((159.0+159.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438513-radius_halfPixelAway,5019648 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((159.0+159.0+222.0)/3.0,r.mean(new Point2D.Double(438513-radius_halfPixelAway,5019648 - radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);





	}


	@Test
	void test_fuseAll() throws ImageReadException, IOException {




		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);
		float [] pixelValueBuffer = new float[r.getNumBands()];


		Rectangle2D bb = r.getBoundingBox();
		double radius_halfPixelAway = r.getPixelSpatialScaleX()/2.0;
		double radius_1PixelAway = r.getPixelSpatialScaleX();
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway = r.getPixelSpatialScaleX()*2;
		double radius_3PixelAway = r.getPixelSpatialScaleX()*3;

		Rectangle2D preAllocatedRect = new Rectangle2D.Double();



		int bandIx = 0;
		int meanIx = 2;
		int maxIx = 3;
		int minIx = 4;

		String sep = ",";

		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(0,new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway),"85,12"));//point 0 
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(438512,5019648),"192,52"));//point 1
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway),"150,45"));//point 2

		r.fuseAll(ds,",",radius_1PixelAway,SpatialData.DistanceMetric.INFINITY_NORM,preAllocatedRect ,bandIx,pixelValueBuffer);

		//bottom left corner: point 0
		//...
		// 128 191 191
		// (128) (191) 28
		// (128) (191) 191
		//mean (128 + 128 + 191 + 191)/4
		//min 128
		//max 191

		Assert.assertEquals(85.0,ds.getAttributeValue(0,0, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(12.0,ds.getAttributeValue(0,1, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((128.0+128.0+191.0+191.0)/4.0,ds.getAttributeValue(0,meanIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(191.0,ds.getAttributeValue(0,maxIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(128.0,ds.getAttributeValue(0,minIx, sep),DOUBLE_COMPARE_EPSILON);



		//Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		//Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//center : point 1
		/*
		 * 		222	 159	159		159
		 * 		82	(222)	(159)	159
		 * 		82	(82)	(222)	159
		 * 		191	82		222	159
		 */


		Assert.assertEquals(192.0,ds.getAttributeValue(1,0, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(52.0,ds.getAttributeValue(1,1, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,ds.getAttributeValue(1,meanIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(222.0,ds.getAttributeValue(1,maxIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82.0,ds.getAttributeValue(1,minIx, sep),DOUBLE_COMPARE_EPSILON);



		//Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		//Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

		//top right: point 2
		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222


		Assert.assertEquals(150.0,ds.getAttributeValue(2,0, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(45.0,ds.getAttributeValue(2,1, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((82.0+82.0+82.0+191.0)/4.0,ds.getAttributeValue(2,meanIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(191.0,ds.getAttributeValue(2,maxIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82.0,ds.getAttributeValue(2,minIx, sep),DOUBLE_COMPARE_EPSILON);

		//Assert.assertEquals((82.0+82.0+82.0+191.0)/4.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		//Assert.assertEquals((82.0+82.0+191.0)/3.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(0,new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway),"85,12"));//point 0 
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(438512-radius_halfPixelAway,5019648+radius_halfPixelAway),"192,52"));//point 1
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway),"150,45"));//point 2

		r.fuseAll(ds,",",radius_1PixelAway,SpatialData.DistanceMetric.EUCLIDEAN,preAllocatedRect ,bandIx,pixelValueBuffer);

		//bottom left corner: point 0
		//...
		// 128 191 191
		// (128) 191 28
		// (128) (191) 191
		//mean (128 + 128 + 191 + 191)/4
		//min 128
		//max 191

		Assert.assertEquals(85.0,ds.getAttributeValue(0,0, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(12.0,ds.getAttributeValue(0,1, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((128.0+128.0+191.0)/3.0,ds.getAttributeValue(0,meanIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(191.0,ds.getAttributeValue(0,maxIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(128.0,ds.getAttributeValue(0,minIx, sep),DOUBLE_COMPARE_EPSILON);



		//Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		//Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		//center : point 1
		/*	159
		 *82 222	159
		 *	82
		 */


		Assert.assertEquals(192.0,ds.getAttributeValue(1,0, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(52.0,ds.getAttributeValue(1,1, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((222.0+82.0+82.0+159.0+159)/5.0,ds.getAttributeValue(1,meanIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(222.0,ds.getAttributeValue(1,maxIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82.0,ds.getAttributeValue(1,minIx, sep),DOUBLE_COMPARE_EPSILON);



		//Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		//Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

		//top right: point 2
		//... 222 (191) (82)
		// ...  82  (82)   (82)
		//  ...  222 82 222


		Assert.assertEquals(150.0,ds.getAttributeValue(2,0, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(45.0,ds.getAttributeValue(2,1, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals((82.0+82.0+191.0)/3.0,ds.getAttributeValue(2,meanIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(191.0,ds.getAttributeValue(2,maxIx, sep),DOUBLE_COMPARE_EPSILON);
		Assert.assertEquals(82.0,ds.getAttributeValue(2,minIx, sep),DOUBLE_COMPARE_EPSILON);

		//Assert.assertEquals((82.0+82.0+82.0+191.0)/4.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
		//Assert.assertEquals((82.0+82.0+191.0)/3.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


	}
	@Test
	void test_multithreaded_fuseAll() throws ImageReadException, IOException {




		MyRaster r = new MyRaster();
		r.load(PATH_TO_R_SCRIPT_EXE, PATH_TO_R_RASTER_INFO_SCRIPT, TEST_RASTER_FILE_PATH);
		float [] pixelValueBuffer = new float[r.getNumBands()];
		int bandIx = 0;
		//multithreadted for rgb not supported, so convert to different storeage forma
		//r.convertRGBToMSData(0);

		Rectangle2D bb = r.getBoundingBox();
		double radius_halfPixelAway = r.getPixelSpatialScaleX()/2.0;
		double radius_1PixelAway = r.getPixelSpatialScaleX();
		//double radius_1andHalfPixelAway =radius_1PixelAway +radius_halfPixelAway;
		double radius_2PixelAway = r.getPixelSpatialScaleX()*2;
		double radius_3PixelAway = r.getPixelSpatialScaleX()*3;

		Rectangle2D preAllocatedRect = new Rectangle2D.Double();

		int numberOfThreads = 3; //only 3 thrads since 3 points

		
		int meanIx = 2;
		int maxIx = 3;
		int minIx = 4;

		String sep = ",";


		//make sure there are no race conditions, so runn 100 times
		for(int i = 0;i<100;i++) {

			SpatialDataset ds = new SpatialDataset(2);
			ds.addSpatialData(new SpatialData(0,new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway),"85,12"));//point 0 
			ds.addSpatialData(new SpatialData(1,new Point2D.Double(438512,5019648),"192,52"));//point 1
			ds.addSpatialData(new SpatialData(2,new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway),"150,45"));//point 2

			r.fuseAll(ds,",",radius_1PixelAway,SpatialData.DistanceMetric.INFINITY_NORM,preAllocatedRect ,bandIx,pixelValueBuffer);
			r.multiThreaded_fuseAll(numberOfThreads,ds,sep,radius_1PixelAway,SpatialData.DistanceMetric.INFINITY_NORM , bandIx);

			//bottom left corner: point 0
			//...
			// 128 191 191
			// (128) (191) 28
			// (128) (191) 191
			//mean (128 + 128 + 191 + 191)/4
			//min 128
			//max 191

			Assert.assertEquals(85.0,ds.getAttributeValue(0,0, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(12.0,ds.getAttributeValue(0,1, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals((128.0+128.0+191.0+191.0)/4.0,ds.getAttributeValue(0,meanIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(191.0,ds.getAttributeValue(0,maxIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(128.0,ds.getAttributeValue(0,minIx, sep),DOUBLE_COMPARE_EPSILON);



			//Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
			//Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


			//center : point 1
			/*
			 * 		222	 159	159		159
			 * 		82	(222)	(159)	159
			 * 		82	(82)	(222)	159
			 * 		191	82		222	159
			 */


			Assert.assertEquals(192.0,ds.getAttributeValue(1,0, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(52.0,ds.getAttributeValue(1,1, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,ds.getAttributeValue(1,meanIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(222.0,ds.getAttributeValue(1,maxIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(82.0,ds.getAttributeValue(1,minIx, sep),DOUBLE_COMPARE_EPSILON);



			//Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
			//Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

			//top right: point 2
			//... 222 (191) (82)
			// ...  82  (82)   (82)
			//  ...  222 82 222


			Assert.assertEquals(150.0,ds.getAttributeValue(2,0, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(45.0,ds.getAttributeValue(2,1, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals((82.0+82.0+82.0+191.0)/4.0,ds.getAttributeValue(2,meanIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(191.0,ds.getAttributeValue(2,maxIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(82.0,ds.getAttributeValue(2,minIx, sep),DOUBLE_COMPARE_EPSILON);

			//Assert.assertEquals((82.0+82.0+82.0+191.0)/4.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
			//Assert.assertEquals((82.0+82.0+191.0)/3.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


			ds = new SpatialDataset(2);
			ds.addSpatialData(new SpatialData(0,new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway),"85,12"));//point 0 
			ds.addSpatialData(new SpatialData(1,new Point2D.Double(438512-radius_halfPixelAway,5019648+radius_halfPixelAway),"192,52"));//point 1
			ds.addSpatialData(new SpatialData(2,new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway),"150,45"));//point 2

			r.multiThreaded_fuseAll(numberOfThreads,ds,sep,radius_1PixelAway,SpatialData.DistanceMetric.EUCLIDEAN , bandIx);

			//bottom left corner: point 0
			//...
			// 128 191 191
			// (128) 191 28
			// (128) (191) 191
			//mean (128 + 128 + 191 + 191)/4
			//min 128
			//max 191

			Assert.assertEquals(85.0,ds.getAttributeValue(0,0, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(12.0,ds.getAttributeValue(0,1, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals((128.0+128.0+191.0)/3.0,ds.getAttributeValue(0,meanIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(191.0,ds.getAttributeValue(0,maxIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(128.0,ds.getAttributeValue(0,minIx, sep),DOUBLE_COMPARE_EPSILON);



			//Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
			//Assert.assertEquals(128,r.mean(new Point2D.Double(bb.getMinX()+radius_halfPixelAway,bb.getMinY()+radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


			//center : point 1
			/*	159
			 *82 222	159
			 *	82
			 
			 	*/
			Assert.assertEquals(192.0,ds.getAttributeValue(1,0, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(52.0,ds.getAttributeValue(1,1, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals((222.0+82.0+82.0+159.0+159)/5.0,ds.getAttributeValue(1,meanIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(222.0,ds.getAttributeValue(1,maxIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(82.0,ds.getAttributeValue(1,minIx, sep),DOUBLE_COMPARE_EPSILON);



			//Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
			//Assert.assertEquals((222.0+82.0+222.0+159.0)/4.0,r.mean(new Point2D.Double(438512,5019648), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);

			//top right: point 2
			//... 222 (191) (82)
			// ...  82  (82)   (82)
			//  ...  222 82 222


			Assert.assertEquals(150.0,ds.getAttributeValue(2,0, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(45.0,ds.getAttributeValue(2,1, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals((82.0+82.0+191.0)/3.0,ds.getAttributeValue(2,meanIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(191.0,ds.getAttributeValue(2,maxIx, sep),DOUBLE_COMPARE_EPSILON);
			Assert.assertEquals(82.0,ds.getAttributeValue(2,minIx, sep),DOUBLE_COMPARE_EPSILON);

			//Assert.assertEquals((82.0+82.0+82.0+191.0)/4.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.INFINITY_NORM, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);
			//Assert.assertEquals((82.0+82.0+191.0)/3.0,r.mean(new Point2D.Double(bb.getMaxX()-radius_halfPixelAway,bb.getMaxY()-radius_halfPixelAway), radius_1PixelAway, SpatialData.DistanceMetric.EUCLIDEAN, 0,null,pixelValueBuffer),DOUBLE_COMPARE_EPSILON);


		}
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
