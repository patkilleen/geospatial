package test;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import data_structure.SpatialData;
import data_structure.SpatialDataset;
import io.FileHandler;
import junit.framework.Assert;

class TestFileHandler {

	@Test
	void testReadCSVIntoSpatialDataset() throws IOException {
		
		File inputFile = File.createTempFile("java-geo", ".csv");
		inputFile.deleteOnExit();
		
		String fileContent = 	"x,y,yield,speed\n"
								+ "0,0,100,5.5\n"
								+ "2,5,150,6.5\n"
								+ "-2.5,-6.9,250,0.3";
		
		FileHandler.append(inputFile.getAbsolutePath(), fileContent.getBytes());
		
		SpatialDataset res =FileHandler.readCSVIntoSpatialDataset(inputFile.getAbsolutePath(),null,",");
		
		Assert.assertEquals(3,res.size());
		
		Assert.assertEquals(0.0,res.getSpatialData(0).getLocation().getX(),0.0001); 
		Assert.assertEquals(0.0,res.getSpatialData(0).getLocation().getY(),0.0001);
		Assert.assertEquals(100,res.getAttributeValue(0, 0, ","),0.0001);
		Assert.assertEquals(5.5,res.getAttributeValue(0, 1, ","),0.0001);
		Assert.assertEquals(SpatialData.NON_OVERRIDED_NEIGHBORHOOD_SEARCH_RADIUS,res.getSpatialData(0).getOverrideNeighborhoodSearchRadius(),0.0001);
		
		Assert.assertEquals(2,res.getSpatialData(1).getLocation().getX(),0.0001); 
		Assert.assertEquals(5,res.getSpatialData(1).getLocation().getY(),0.0001);
		Assert.assertEquals(150,res.getAttributeValue(1, 0, ","),0.0001);
		Assert.assertEquals(6.5,res.getAttributeValue(1, 1, ","),0.0001);
		Assert.assertEquals(SpatialData.NON_OVERRIDED_NEIGHBORHOOD_SEARCH_RADIUS,res.getSpatialData(1).getOverrideNeighborhoodSearchRadius(),0.0001);
		
		Assert.assertEquals(-2.5,res.getSpatialData(2).getLocation().getX(),0.0001); 
		Assert.assertEquals(-6.9,res.getSpatialData(2).getLocation().getY(),0.0001);
		Assert.assertEquals(250,res.getAttributeValue(2, 0, ","),0.0001);
		Assert.assertEquals(0.3,res.getAttributeValue(2, 1, ","),0.0001);
		Assert.assertEquals(SpatialData.NON_OVERRIDED_NEIGHBORHOOD_SEARCH_RADIUS,res.getSpatialData(2).getOverrideNeighborhoodSearchRadius(),0.0001);
	}
	
	@Test
	void testReadCSVIntoSpatialDataset_with_overrided_radius_column() throws IOException {
		
		File inputFile = File.createTempFile("java-geo", ".csv");
		inputFile.deleteOnExit();
		
		String fileContent = 	"x,y,yield,speed,radius\n"
								+ "0,0,100,5.5,10\n"
								+ "2,5,150,6.5,100\n"
								+ "-2.5,-6.9,250,0.3,0.5";
		
		FileHandler.append(inputFile.getAbsolutePath(), fileContent.getBytes());
		
		SpatialDataset res =FileHandler.readCSVIntoSpatialDataset(inputFile.getAbsolutePath(),"radius",",");
		
		Assert.assertEquals(3,res.size());
		
		Assert.assertEquals(0.0,res.getSpatialData(0).getLocation().getX(),0.0001); 
		Assert.assertEquals(0.0,res.getSpatialData(0).getLocation().getY(),0.0001);
		Assert.assertEquals(100,res.getAttributeValue(0, 0, ","),0.0001);
		Assert.assertEquals(5.5,res.getAttributeValue(0, 1, ","),0.0001);
		Assert.assertEquals(10,res.getSpatialData(0).getOverrideNeighborhoodSearchRadius(),0.0001);
		
		Assert.assertEquals(2,res.getSpatialData(1).getLocation().getX(),0.0001); 
		Assert.assertEquals(5,res.getSpatialData(1).getLocation().getY(),0.0001);
		Assert.assertEquals(150,res.getAttributeValue(1, 0, ","),0.0001);
		Assert.assertEquals(6.5,res.getAttributeValue(1, 1, ","),0.0001);
		Assert.assertEquals(100,res.getSpatialData(1).getOverrideNeighborhoodSearchRadius(),0.0001);
		
		Assert.assertEquals(-2.5,res.getSpatialData(2).getLocation().getX(),0.0001); 
		Assert.assertEquals(-6.9,res.getSpatialData(2).getLocation().getY(),0.0001);
		Assert.assertEquals(250,res.getAttributeValue(2, 0, ","),0.0001);
		Assert.assertEquals(0.3,res.getAttributeValue(2, 1, ","),0.0001);
		Assert.assertEquals(0.5,res.getSpatialData(2).getOverrideNeighborhoodSearchRadius(),0.0001);
	}
	
	
	@Test
	void testWriteSpatialDatasetToFile() throws IOException {
		File outputFile = File.createTempFile("java-geo", ".csv");
		
		SpatialDataset highResDS = TestDataFusion.buildHighResTestDataset();
		
		//make sure id's start from 0
		for(int i = 0;i<highResDS.size();i++) {
			SpatialData pt = highResDS.getSpatialData(i);
			
			pt.setId(i);
		}
		FileHandler.writeSpatialDatasetToFile(outputFile.getAbsolutePath(),"x,y,yield,speed", highResDS,true,SpatialData.COMMA,false);
		SpatialDataset res = FileHandler.readCSVIntoSpatialDataset(outputFile.getAbsolutePath(), null, SpatialData.COMMA);
		
		
		Assert.assertEquals(highResDS.size(),res.size());
		//make sure after sort all items in same order
		for(int j = 0;j<highResDS.size();j++) {
			SpatialData pt1 = highResDS.getSpatialData(j);
			SpatialData pt2 = res.getSpatialData(j);
			
			Assert.assertEquals(pt1.getLocation().getX(), pt2.getLocation().getX(),0.00001);
			Assert.assertEquals(pt1.getLocation().getY(), pt2.getLocation().getY(),0.00001);
			Assert.assertEquals(pt1.getAttributes(), pt2.getAttributes());
			Assert.assertEquals(pt1.toString(), pt2.toString());
		}
		
		
		//reuse same output file
			
		// highResDS = TestDataFusion.buildHighResTestDataset();
		FileHandler.writeSpatialDatasetToFile(outputFile.getAbsolutePath(),"x,y,yield,speed", highResDS,true,SpatialData.COMMA,false);
		 res = FileHandler.readCSVIntoSpatialDataset(outputFile.getAbsolutePath(), null, SpatialData.COMMA);
		
		
		Assert.assertEquals(highResDS.size(),res.size());
		//make sure after sort all items in same order
		for(int j = 0;j<highResDS.size();j++) {
			SpatialData pt1 = highResDS.getSpatialData(j);
			SpatialData pt2 = res.getSpatialData(j);
			
			Assert.assertEquals(pt1.getLocation().getX(), pt2.getLocation().getX(),0.00001);
			Assert.assertEquals(pt1.getLocation().getY(), pt2.getLocation().getY(),0.00001);
			Assert.assertEquals(pt1.getAttributes(), pt2.getAttributes());
			Assert.assertEquals(pt1.toString(), pt2.toString());
		}
			
		
		
		SpatialDataset ds = new SpatialDataset(32);
		//x, y, yield, speed

		//group 1

		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),"100,5"));


		//group 2
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),"105,6"));


		//group 3
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),"110,7"));


		//group 4
		ds.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),"105,5"));

		FileHandler.writeSpatialDatasetToFile(outputFile.getAbsolutePath(),"id,x,y,yield,speed", ds,true,SpatialData.COMMA,true);
		
		res = FileHandler.readCSVIntoSpatialDataset(outputFile.getAbsolutePath(), null, SpatialData.COMMA);
		
		Assert.assertEquals(4,res.size());
		
		
		Assert.assertEquals(1,res.getSpatialData(0).getLocation().getX(),0.0001); 
		Assert.assertEquals(0.0,res.getSpatialData(0).getLocation().getY(),0.0001);
		Assert.assertEquals(0,res.getAttributeValue(0, 0, ","),0.0001);
		Assert.assertEquals(100,res.getAttributeValue(0, 1, ","),0.0001);
		Assert.assertEquals(5,res.getAttributeValue(0, 2, ","),0.0001);
		
		

		Assert.assertEquals(2,res.getSpatialData(1).getLocation().getX(),0.0001); 
		Assert.assertEquals(8,res.getSpatialData(1).getLocation().getY(),0.0001);
		Assert.assertEquals(6,res.getAttributeValue(1, 0, ","),0.0001);
		Assert.assertEquals(105,res.getAttributeValue(1, 1, ","),0.0001);
		Assert.assertEquals(6,res.getAttributeValue(1, 2, ","),0.0001);
		
		Assert.assertEquals(3,res.getSpatialData(2).getLocation().getX(),0.0001); 
		Assert.assertEquals(-4.5,res.getSpatialData(2).getLocation().getY(),0.0001);
		Assert.assertEquals(-4.5,res.getAttributeValue(2, 0, ","),0.0001);
		Assert.assertEquals(110,res.getAttributeValue(2, 1, ","),0.0001);
		Assert.assertEquals(7,res.getAttributeValue(2, 2, ","),0.0001);
		
		Assert.assertEquals(4,res.getSpatialData(3).getLocation().getX(),0.0001); 
		Assert.assertEquals(-4.5,res.getSpatialData(3).getLocation().getY(),0.0001);
		Assert.assertEquals(8,res.getAttributeValue(3, 0, ","),0.0001);
		Assert.assertEquals(105,res.getAttributeValue(3, 1, ","),0.0001);
		Assert.assertEquals(5,res.getAttributeValue(3, 2, ","),0.0001);
		
		
		
		
		
		ds = new SpatialDataset(32);
		//x, y, yield, speed

		//group 1

		ds.addSpatialData(new SpatialData(1,new Point2D.Double(0,0),""));


		//group 2
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(8,6),""));


		//group 3
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(-4.5,-4.5),""));


		//group 4
		ds.addSpatialData(new SpatialData(4,new Point2D.Double(-4.5,8),""));

		
		
		FileHandler.writeSpatialDatasetToFile(outputFile.getAbsolutePath(),"x,y", ds,true,SpatialData.COMMA,false);
		
		res = FileHandler.readCSVIntoSpatialDataset(outputFile.getAbsolutePath(), null, SpatialData.COMMA);
		
		
		Assert.assertEquals(4,res.size());
		
		
		Assert.assertEquals(0.0,res.getSpatialData(0).getLocation().getX(),0.0001); 
		Assert.assertEquals(0.0,res.getSpatialData(0).getLocation().getY(),0.0001);
		Assert.assertEquals("",res.getSpatialData(0).getAttributes());

		Assert.assertEquals(8,res.getSpatialData(1).getLocation().getX(),0.0001); 
		Assert.assertEquals(6,res.getSpatialData(1).getLocation().getY(),0.0001);
		Assert.assertEquals("",res.getSpatialData(1).getAttributes());

		Assert.assertEquals(-4.5,res.getSpatialData(2).getLocation().getX(),0.0001); 
		Assert.assertEquals(-4.5,res.getSpatialData(2).getLocation().getY(),0.0001);
		Assert.assertEquals("",res.getSpatialData(2).getAttributes());
		
		Assert.assertEquals(-4.5,res.getSpatialData(3).getLocation().getX(),0.0001); 
		Assert.assertEquals(8,res.getSpatialData(3).getLocation().getY(),0.0001);
		Assert.assertEquals("",res.getSpatialData(3).getAttributes());
		
		
	}

}
