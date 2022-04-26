package test;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

import aggregation.BasicDataFusionAggregator;
import aggregation.SingleSummaryStatAggregator;
import aggregation.SpatialDataAggregator;
import core.Util;
import data_structure.SpatialData;
import data_structure.SpatialDataset;
import junit.framework.Assert;

class TestBasicDataFusionAggregator {
	public final static double DOUBLE_EQUALITY_EPSILON = 0.00001;
	@Test
	void testApplyAggregation() {
		//SpatialDataAggregator agg = new BasicDataFusionAggregator();
		SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.BASIC,SpatialData.COMMA);
		
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1.0,2.0");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"-5.0,4.0");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),"Grain,NE");
		
		
		SpatialDataset ds = new SpatialDataset(2);

		SpatialDataset output = new SpatialDataset(1);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
			
		SpatialData target = new SpatialData(7,new Point2D.Double(100,150),"10.0,20.0");
		
		SpatialData []expectedPts={
				new SpatialData(7,new Point2D.Double(100,150),"10.0,20.0,1,-10.0,5.0,1.0,2.0"),
				new SpatialData(7,new Point2D.Double(100,150),"10.0,20.0,2,-5.0,-1.0,-5.0,4.0"),
				new SpatialData(7,new Point2D.Double(100,150),"10.0,20.0,3,0.0,15.0,Grain,NE"),
				
				};
		
		agg.applyAggregation(target, ds, output);
		
		Assert.assertEquals(3, output.size());
		
		for(int i = 0;i< output.size();i++) {
			SpatialData expected = expectedPts[i];
			SpatialData actual = output.getSpatialData(i);
			
			Assert.assertEquals(expected.getId(),actual.getId());
			Assert.assertEquals(expected.getId(),actual.getId());
			
			Assert.assertEquals(true,Util.almostEqual(expected.getLocation().getX(), actual.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
			Assert.assertEquals(true,Util.almostEqual(expected.getLocation().getY(), actual.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
			
			Assert.assertEquals(expected.getAttributes(),actual.getAttributes());
			
			
		}
		
	}
		@Test
		void testApplyAggregation_empty_target_attributes() {
			SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.BASIC,SpatialData.COMMA);
			
			SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1.0,2.0");
			SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"-5.0,4.0");
			SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),"Grain,NE");
			
			
			SpatialDataset ds = new SpatialDataset(2);

			SpatialDataset output = new SpatialDataset(1);
			ds.addSpatialData(sd1);
			ds.addSpatialData(sd2);		
			ds.addSpatialData(sd3);
				
			SpatialData target = new SpatialData(7,new Point2D.Double(100,150),"");
			
			SpatialData []expectedPts={
					new SpatialData(7,new Point2D.Double(100,150),"1,-10.0,5.0,1.0,2.0"),
					new SpatialData(7,new Point2D.Double(100,150),"2,-5.0,-1.0,-5.0,4.0"),
					new SpatialData(7,new Point2D.Double(100,150),"3,0.0,15.0,Grain,NE"),
					
					};
			
			agg.applyAggregation(target, ds, output);
			
			Assert.assertEquals(3, output.size());
			
			for(int i = 0;i< output.size();i++) {
				SpatialData expected = expectedPts[i];
				SpatialData actual = output.getSpatialData(i);
				
				Assert.assertEquals(expected.getId(),actual.getId());
				Assert.assertEquals(expected.getId(),actual.getId());
				
				Assert.assertEquals(true,Util.almostEqual(expected.getLocation().getX(), actual.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
				Assert.assertEquals(true,Util.almostEqual(expected.getLocation().getY(), actual.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));
				
				Assert.assertEquals(expected.getAttributes(),actual.getAttributes());
				
				
			}
			

		
		

	}
		
		
		@Test
		void testApplyAggregation_empty_dataset() {
			SpatialDataAggregator agg = SingleSummaryStatAggregator.buildAggregator(SingleSummaryStatAggregator.SingleSummaryStat.BASIC,SpatialData.COMMA);
			
			
			
			SpatialDataset ds = new SpatialDataset(2);

			SpatialDataset output = new SpatialDataset(1);
		
			SpatialData target = new SpatialData(7,new Point2D.Double(100,150),"");
			
			SpatialData []expectedPts={
					new SpatialData(7,new Point2D.Double(100,150),"1,-10.0,5.0,1.0,2.0"),
					new SpatialData(7,new Point2D.Double(100,150),"2,-5.0,-1.0,-5.0,4.0"),
					new SpatialData(7,new Point2D.Double(100,150),"3,0.0,15.0,Grain,NE"),
					
					};
			
			agg.applyAggregation(target, ds, output);
			
			Assert.assertEquals(0, output.size());

		}

}
