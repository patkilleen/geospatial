package test;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import core.Util;
import data_structure.BoundingBox;
import data_structure.SpatialData;
import data_structure.SpatialDataset;

class TestSpatialDataset {
	public final static double DOUBLE_EQUALITY_EPSILON = 0.00001;
	@Test
	void testSpatialDataset_constructor() {

		SpatialDataset ds = new SpatialDataset();

		Assert.assertEquals(0,ds.size());

		//SpatialData sd1 = new SpatialData(0,new Point2D.Double(0,0),null);
	}

	@Test
	void testSpatialDataset_constructor_int() {
		SpatialDataset ds = new SpatialDataset(0);

		Assert.assertEquals(0,ds.size());

		ds = new SpatialDataset(10);

		Assert.assertEquals(0,ds.size());

	}

	@Test
	void testAddSpatialData() {

		SpatialDataset ds = new SpatialDataset(2);

		ds.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),null));

		Assert.assertEquals(1,ds.size());

		ds.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),null));

		Assert.assertEquals(2,ds.size());

		ds.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),null));		

		Assert.assertEquals(3,ds.size());



		ds = new SpatialDataset(10);

		ds.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),null));

		Assert.assertEquals(1,ds.size());

		ds.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),null));

		Assert.assertEquals(2,ds.size());

		ds.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),null));

		Assert.assertEquals(3,ds.size());



		ds = new SpatialDataset();

		ds.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),null));

		Assert.assertEquals(1,ds.size());

		ds.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),null));

		Assert.assertEquals(2,ds.size());

		ds.addSpatialData(new SpatialData(0,new Point2D.Double(0,0),null));

		Assert.assertEquals(3,ds.size());


	}

	@Test
	void testAddSpatialData_badargument() {
		SpatialDataset ds = new SpatialDataset(2);



		boolean exceptionOccured = false;


		try {
			ds.addSpatialData(null);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
	}
	@Test
	void testGetSpatialData() {


		SpatialData sd1 = new SpatialData(1,new Point2D.Double(3,-1),null);
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(4,-2),null);
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(5,-3),null);

		SpatialDataset ds = new SpatialDataset(2);

		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);

		Assert.assertSame(sd1, ds.getSpatialData(0));
		Assert.assertSame(sd2, ds.getSpatialData(1));
		Assert.assertSame(sd3, ds.getSpatialData(2));



	}

	@Test
	void testGetSpatialData_bad_argument() {

		SpatialDataset ds = new SpatialDataset(2);


		boolean exceptionOccured = false;


		try {
			ds.getSpatialData(0);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);


		SpatialData sd1 = new SpatialData(1,new Point2D.Double(3,-1),null);

		ds = new SpatialDataset(2);

		ds.addSpatialData(sd1);


		exceptionOccured = false;


		try {
			ds.getSpatialData(-1);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);

		exceptionOccured = false;


		try {
			ds.getSpatialData(1);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);


		ds = new SpatialDataset(2);

		ds.addSpatialData(sd1);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd1);
		
		
		exceptionOccured = false;


		try {
			ds.getSpatialData(3);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
		
		
		exceptionOccured = false;


		try {
			ds.getSpatialData(1000);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);

		exceptionOccured = false;


		try {
			ds.getSpatialData(-1000);	
		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);


	}

	@Test
	void testSize() {

		SpatialData sd1 = new SpatialData(1,new Point2D.Double(3,-1),null);
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(4,-2),null);
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(5,-3),null);

		SpatialDataset ds = new SpatialDataset(2);
		Assert.assertEquals(0, ds.size());
		
		ds = new SpatialDataset(0);
		Assert.assertEquals(0, ds.size());
		
		ds = new SpatialDataset();
		Assert.assertEquals(0, ds.size());
		
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		ds.addSpatialData(sd3);

		Assert.assertEquals(3, ds.size());
		
		ds = new SpatialDataset(2);

		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		ds.addSpatialData(sd3);

		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		ds.addSpatialData(sd3);
		
		Assert.assertEquals(6, ds.size());
		
		
	}

	@Test
	void testClear() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(3,-1),null);
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(4,-2),null);
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(5,-3),null);

		SpatialDataset ds = new SpatialDataset(2);
		ds.clear();
		Assert.assertEquals(0, ds.size());
		
		ds = new SpatialDataset(0);
		ds.clear();
		Assert.assertEquals(0, ds.size());
		
		ds = new SpatialDataset();
		ds.clear();
		Assert.assertEquals(0, ds.size());
		
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		ds.addSpatialData(sd3);
		ds.clear();
		
		
		Assert.assertEquals(0, ds.size());
		
		ds = new SpatialDataset(2);

		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		ds.addSpatialData(sd3);

		ds.clear();
		
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		ds.addSpatialData(sd3);
		
		Assert.assertEquals(3, ds.size());
		
	}

	@Test
	void testIterator() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(3,-1),null);
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(4,-2),null);
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(5,-3),null);

		SpatialDataset ds = new SpatialDataset(2);
		
		Iterator<SpatialData> it = ds.iterator();
		
		
		Assert.assertEquals(false,it.hasNext());
		
		ds.addSpatialData(sd1);
		
		it = ds.iterator();
		
		
		Assert.assertEquals(true,it.hasNext());		
		Assert.assertSame(sd1,it.next());
		Assert.assertEquals(false,it.hasNext());
		
		
		it = ds.iterator();
		
		
		Assert.assertEquals(true,it.hasNext());		
		Assert.assertSame(sd1,it.next());
		Assert.assertEquals(false,it.hasNext());
		
		
		ds.addSpatialData(sd2);
		ds.addSpatialData(sd3);
		
		it = ds.iterator();
		
		Assert.assertEquals(true,it.hasNext());		
		Assert.assertSame(sd1,it.next());
		Assert.assertEquals(true,it.hasNext());
		Assert.assertSame(sd2,it.next());
		Assert.assertEquals(true,it.hasNext());
		Assert.assertSame(sd3,it.next());
		Assert.assertEquals(false,it.hasNext());
		
		it = ds.iterator();
		
		Assert.assertEquals(true,it.hasNext());		
		Assert.assertSame(sd1,it.next());
		Assert.assertEquals(true,it.hasNext());
		Assert.assertSame(sd2,it.next());
		Assert.assertEquals(true,it.hasNext());
		Assert.assertSame(sd3,it.next());
		Assert.assertEquals(false,it.hasNext());
		
		ds.clear();
		it = ds.iterator();
		Assert.assertEquals(false,it.hasNext());
	}

	@Test
	void testSplit_empty_dataset() {

		SpatialDataset ds = new SpatialDataset(2);
		
		
		List<SpatialDataset> subsets = ds.split(1);
		
		Assert.assertTrue(subsets.isEmpty());
		
		
		subsets = ds.split(100);
		
		Assert.assertTrue(subsets.isEmpty());
		
	}
	@Test
	void testSplit_max_number_subsets() {
		SpatialDataset ds = new SpatialDataset(2);
		
		//fill the list with 100 points
		for(int i = 0;i<100;i++) {
			SpatialData sd1 = new SpatialData(i,new Point2D.Double(0,0),null);
			ds.addSpatialData(sd1);
		}
		
		
		List<SpatialDataset> subsets = ds.split(1);
		
		Assert.assertEquals(100,subsets.size());
		
		for(int i = 0;i<subsets.size();i++) {
			SpatialDataset subset = subsets.get(i);
			Assert.assertEquals(1,subset.size());
			SpatialData pt = subset.getSpatialData(0);
			Assert.assertEquals(i,pt.getId());
		}
		
		//run twice to show slice doesn't affect dataset
		subsets = ds.split(1);
		
		Assert.assertEquals(100,subsets.size());
		
		for(int i = 0;i<subsets.size();i++) {
			SpatialDataset subset = subsets.get(i);
			Assert.assertEquals(1,subset.size());
			SpatialData pt = subset.getSpatialData(0);
			Assert.assertEquals(i,pt.getId());
		}
		
		//make sure slicing didn't affect eelments in original dataset
		Assert.assertEquals(100,ds.size());
		//
		for(int i = 0;i<100;i++) {
			SpatialData pt = ds.getSpatialData(i);
			Assert.assertEquals(i,pt.getId());
		}

	}

	
	@Test
	void testSplit_max_subset_size_overflow() {
		SpatialDataset ds = new SpatialDataset(2);
		
		//fill the list with 100 points
		for(int i = 0;i<100;i++) {
			SpatialData sd1 = new SpatialData(i,new Point2D.Double(0,0),null);
			ds.addSpatialData(sd1);
		}
		
		
		List<SpatialDataset> subsets = ds.split(100+1);
		
		Assert.assertEquals(1,subsets.size());
		
		SpatialDataset subset = subsets.get(0);
		Assert.assertEquals(100,subset.size());
		
		for(int i = 0;i<subset.size();i++) {
//			SpatialDataset subset = subsets.get(i);
	//		Assert.assertEquals(1,subset.size());
			SpatialData pt = subset.getSpatialData(i);
			Assert.assertEquals(i,pt.getId());
		}
		
		subsets = ds.split(100*100);
		
		Assert.assertEquals(1,subsets.size());
		
		subset = subsets.get(0);
		Assert.assertEquals(100,subset.size());
		
		for(int i = 0;i<subset.size();i++) {
//			SpatialDataset subset = subsets.get(i);
	//		Assert.assertEquals(1,subset.size());
			SpatialData pt = subset.getSpatialData(i);
			Assert.assertEquals(i,pt.getId());
		}


	}
	
	@Test
	void testSplit_subsets_of_all_sizes() {
		SpatialDataset ds = new SpatialDataset(2);
		
		//fill the list with 100 points
		for(int i = 0;i<100;i++) {
			SpatialData sd1 = new SpatialData(i,new Point2D.Double(0,0),null);
			ds.addSpatialData(sd1);
		}
		
		for(int subSetSize = 100;subSetSize>=1;subSetSize--) {
			
			List<SpatialDataset> subsets = ds.split(subSetSize);
			
			
			//example. suppose we want 13 slices from dataset of size 100. dividing 100 by 13 would lead to  subsets of size 7.69. This doesn't work
			//since we would need an 8th subset to fill the 9 remaining (100 module 13 is 9, since 13 * 7 is 91). That means subsets need to be of size 8 (ceiling(7.69)
			// and the last subset will not be filled (8*13 = 104, so the first 12 subsets of size 8 will lead to 96 elements,8 each, and the 4 remaining will be in 13th sbuset)
			int expectedNumSubsets = (int)Math.ceil(100.0/((double)subSetSize));
			
			Assert.assertEquals(expectedNumSubsets, subsets.size());
			//double div =((double)ds.size())/((double)numSubSets);
			int expectedSizeOfEachSubset =subSetSize;			
			
			
			//the last subset will have varying size depedning on if we can divide data perfectly or not
			int expectedSizeOfLastSubset;
			if( 100 % subSetSize ==0) {
				expectedSizeOfLastSubset =expectedSizeOfEachSubset;
			}else {
				expectedSizeOfLastSubset =100 % subSetSize;
			}
			
			//
			//Assert.assertEquals(expectedNumSubsets,subsets.size());
			
			int idCounter = 0;
			
			for(int i = 0;i<subsets.size();i++) {
				SpatialDataset subset = subsets.get(i);
				
				//last subset?
				if(i == subsets.size() -1) {
					Assert.assertEquals(expectedSizeOfLastSubset,subset.size());
				}else {
					Assert.assertEquals(expectedSizeOfEachSubset,subset.size());
				}
				
				//ieterate the subset to make sure each point found inside
				for(int j = 0;j<subset.size();j++) {
					SpatialData pt = subset.getSpatialData(j);
					Assert.assertEquals(idCounter,pt.getId());
					idCounter++;
				}
				
				
			}	
		}
		
		
		
	}
	
	@Test
	void testSplit_subsets_deepcopy() {
		SpatialDataset ds = new SpatialDataset(2);
		
		//fill the list with 100 points
		for(int i = 0;i<100;i++) {
			SpatialData sd1 = new SpatialData(i,new Point2D.Double(0,0),null);
			ds.addSpatialData(sd1);
		}
		
		
		List<SpatialDataset> subsets = ds.split(1);
		
		Assert.assertEquals(100,subsets.size());
		
		for(int i = 0;i<subsets.size();i++) {
			SpatialDataset subset = subsets.get(i);
			
			
			SpatialData pt = ds.getSpatialData(i);
			subset.addSpatialData(pt);
			
		}
		
//	make sure slicing didn't affect eelments in original dataset
		Assert.assertEquals(100,ds.size());
		//
		for(int i = 0;i<100;i++) {
			SpatialData pt = ds.getSpatialData(i);
			Assert.assertEquals(i,pt.getId());
		}
		
		
		
		ds = new SpatialDataset(2);
		
		//fill the list with 100 points
		for(int i = 0;i<100;i++) {
			SpatialData sd1 = new SpatialData(i,new Point2D.Double(0,0),null);
			ds.addSpatialData(sd1);
		}
		
		
		subsets = ds.split(100);
		
		Assert.assertEquals(100,ds.size());
		Assert.assertEquals(1,subsets.size());
		
		for(int i = 0;i<subsets.size();i++) {
			SpatialDataset subset = subsets.get(i);
			
			
			SpatialData pt = ds.getSpatialData(i);
			subset.addSpatialData(pt);
			
		}
		
//	make sure slicing didn't affect eelments in original dataset
		Assert.assertEquals(100,ds.size());
		//
		for(int i = 0;i<100;i++) {
			SpatialData pt = ds.getSpatialData(i);
			Assert.assertEquals(i,pt.getId());
		}
		
	}
	
	@Test
	void testSplit_1_subset() {
		SpatialDataset ds = new SpatialDataset(2);
		
		//fill the list with 100 points
		for(int i = 0;i<100;i++) {
			SpatialData sd1 = new SpatialData(i,new Point2D.Double(0,0),null);
			ds.addSpatialData(sd1);
		}
		
		
		List<SpatialDataset> subsets = ds.split(100);
		
		Assert.assertEquals(1,subsets.size());
		SpatialDataset subset =subsets.get(0);

		for(int i = 0;i<100;i++) {
			SpatialData pt1 = ds.getSpatialData(i);
			SpatialData pt2 = subset.getSpatialData(i);
			
			Assert.assertEquals(pt1.getId(),pt2.getId());
		}
			
		
		
//	
	}
	@Test
	void testSplit_bad_arg() {
	
		
	
	
	SpatialDataset ds = new SpatialDataset(2);


	boolean exceptionOccured = false;


	try {
		List<SpatialDataset> subsets = ds.split(0);

	}catch(Throwable t) {
		exceptionOccured=true;
	}


	Assert.assertEquals(true,exceptionOccured);


	
	 	exceptionOccured = false;


		try {
			List<SpatialDataset> subsets = ds.split(-1);

		}catch(Throwable t) {
			exceptionOccured=true;
		}


		Assert.assertEquals(true,exceptionOccured);
	}
	@Test
	void testSort_xcoord_diff_only() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,0),null);
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,0),null);
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,0),null);
		SpatialData sd4 = new SpatialData(1,new Point2D.Double(5,0),null);
		SpatialData sd5 = new SpatialData(2,new Point2D.Double(10,0),null);
		
		SpatialDataset ds = new SpatialDataset(2);

		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);		
		
		
		ds.sort();
		
		Assert.assertSame(sd1, ds.getSpatialData(0));
		Assert.assertSame(sd2, ds.getSpatialData(1));
		Assert.assertSame(sd3, ds.getSpatialData(2));
		Assert.assertSame(sd4, ds.getSpatialData(3));
		Assert.assertSame(sd5, ds.getSpatialData(4));
		
		
		ds = new SpatialDataset(2);

		ds.addSpatialData(sd3);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd5);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd4);		
		
		
		ds.sort();
		
		Assert.assertSame(sd1, ds.getSpatialData(0));
		Assert.assertSame(sd2, ds.getSpatialData(1));
		Assert.assertSame(sd3, ds.getSpatialData(2));
		Assert.assertSame(sd4, ds.getSpatialData(3));
		Assert.assertSame(sd5, ds.getSpatialData(4));
		
		
		ds = new SpatialDataset(2);

		ds.addSpatialData(sd4);
		ds.addSpatialData(sd1);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd2);
		ds.addSpatialData(sd5);		
		
		
		ds.sort();
		
		Assert.assertSame(sd1, ds.getSpatialData(0));
		Assert.assertSame(sd2, ds.getSpatialData(1));
		Assert.assertSame(sd3, ds.getSpatialData(2));
		Assert.assertSame(sd4, ds.getSpatialData(3));
		Assert.assertSame(sd5, ds.getSpatialData(4));
		
		
	}

	@Test
	void testSort_ycoord_diff_only() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(0,-10),null);
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(0,-5),null);
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,0),null);
		SpatialData sd4 = new SpatialData(1,new Point2D.Double(0,5),null);
		SpatialData sd5 = new SpatialData(2,new Point2D.Double(0,10),null);
		
		SpatialDataset ds = new SpatialDataset(2);

		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);		
		
		
		ds.sort();
		
		Assert.assertSame(sd1, ds.getSpatialData(0));
		Assert.assertSame(sd2, ds.getSpatialData(1));
		Assert.assertSame(sd3, ds.getSpatialData(2));
		Assert.assertSame(sd4, ds.getSpatialData(3));
		Assert.assertSame(sd5, ds.getSpatialData(4));
		
		
		ds = new SpatialDataset(2);

		ds.addSpatialData(sd3);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd5);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd4);		
		
		
		ds.sort();
		
		Assert.assertSame(sd1, ds.getSpatialData(0));
		Assert.assertSame(sd2, ds.getSpatialData(1));
		Assert.assertSame(sd3, ds.getSpatialData(2));
		Assert.assertSame(sd4, ds.getSpatialData(3));
		Assert.assertSame(sd5, ds.getSpatialData(4));
		
		
		ds = new SpatialDataset(2);

		ds.addSpatialData(sd4);
		ds.addSpatialData(sd1);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd2);
		ds.addSpatialData(sd5);		
		
		
		ds.sort();
		
		Assert.assertSame(sd1, ds.getSpatialData(0));
		Assert.assertSame(sd2, ds.getSpatialData(1));
		Assert.assertSame(sd3, ds.getSpatialData(2));
		Assert.assertSame(sd4, ds.getSpatialData(3));
		Assert.assertSame(sd5, ds.getSpatialData(4));
		
		
	}

	@Test
	void testSort() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),null);
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),null);
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),null);
		SpatialData sd4 = new SpatialData(1,new Point2D.Double(5,23),null);
		SpatialData sd5 = new SpatialData(2,new Point2D.Double(10,-15),null);
		
		SpatialDataset ds = new SpatialDataset(2);

		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);		
		
		
		ds.sort();
		
		Assert.assertSame(sd1, ds.getSpatialData(0));
		Assert.assertSame(sd2, ds.getSpatialData(1));
		Assert.assertSame(sd3, ds.getSpatialData(2));
		Assert.assertSame(sd4, ds.getSpatialData(3));
		Assert.assertSame(sd5, ds.getSpatialData(4));
		
		
		ds = new SpatialDataset(2);

		ds.addSpatialData(sd3);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd5);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd4);		
		
		
		ds.sort();
		
		Assert.assertSame(sd1, ds.getSpatialData(0));
		Assert.assertSame(sd2, ds.getSpatialData(1));
		Assert.assertSame(sd3, ds.getSpatialData(2));
		Assert.assertSame(sd4, ds.getSpatialData(3));
		Assert.assertSame(sd5, ds.getSpatialData(4));
		
		
		ds = new SpatialDataset(2);

		ds.addSpatialData(sd4);
		ds.addSpatialData(sd1);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd2);
		ds.addSpatialData(sd5);		
		
		
		ds.sort();
		
		Assert.assertSame(sd1, ds.getSpatialData(0));
		Assert.assertSame(sd2, ds.getSpatialData(1));
		Assert.assertSame(sd3, ds.getSpatialData(2));
		Assert.assertSame(sd4, ds.getSpatialData(3));
		Assert.assertSame(sd5, ds.getSpatialData(4));
		
		
	}
	
	@Test
	void testSort_multi_dataset_compare() {
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(-10,5),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(-5,-1),null));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,15),null));
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(5,23),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(10,-15),null));		
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,10),null));
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(5,35),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(10,-10),null));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(0,-5),null));
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(5,-1),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(10,15),null));
		ds.addSpatialData(new SpatialData(3,new Point2D.Double(3,-5),null));
		ds.addSpatialData(new SpatialData(1,new Point2D.Double(3,-1),null));
		ds.addSpatialData(new SpatialData(2,new Point2D.Double(3,-3),null));
		
		
		SpatialDataset copy =  new SpatialDataset(2);
		
		Iterator<SpatialData> it =ds.iterator();
		while(it.hasNext()) {
			
			copy.addSpatialData(it.next());
		}
		
		//1000 random permutations
		for(int i = 0;i<1000;i++)
			
		{	
			
			//randomly sort the dataste
			List<SpatialData> data1 = ds.getData();
			List<SpatialData> data2 = copy.getData();
			
			Collections.shuffle(data1);
			Collections.shuffle(data2);
			
			ds.sort();
			copy.sort();
			
			//make sure after sort all items in same order
			for(int j = 0;j<ds.size();j++) {
				SpatialData pt1 = ds.getSpatialData(j);
				SpatialData pt2 = copy.getSpatialData(j);
				
				Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getX(), pt2.getLocation().getX(),DOUBLE_EQUALITY_EPSILON));
				Assert.assertEquals(true,Util.almostEqual(pt1.getLocation().getY(), pt2.getLocation().getY(),DOUBLE_EQUALITY_EPSILON));			
			}
			
		}

	}
	
	@Test
	void testFindMinCoordinates() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),null);
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),null);
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),null);
		SpatialData sd4 = new SpatialData(1,new Point2D.Double(5,23),null);
		SpatialData sd5 = new SpatialData(2,new Point2D.Double(10,-15),null);
		
		SpatialDataset ds = new SpatialDataset(2);

		
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);		
		
		Point2D res = ds.findMinCoordinates();
		
		Assert.assertEquals(true,Util.almostEqual(-10, res.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(-15, res.getY(),DOUBLE_EQUALITY_EPSILON));
		
		
		ds = new SpatialDataset(2);
		res = ds.findMinCoordinates();
		Assert.assertNull(res);
		
		ds = new SpatialDataset(2);
		ds.addSpatialData(sd1);
		res = ds.findMinCoordinates();
		
		Assert.assertEquals(true,Util.almostEqual(-10, res.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(5, res.getY(),DOUBLE_EQUALITY_EPSILON));
	}
	
	@Test
	void testFindMaxCoordinates() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),null);
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),null);
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),null);
		SpatialData sd4 = new SpatialData(1,new Point2D.Double(5,23),null);
		SpatialData sd5 = new SpatialData(2,new Point2D.Double(10,-15),null);
		
		SpatialDataset ds = new SpatialDataset(2);

		
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);		
		
		Point2D res = ds.findMaxCoordinates();
		
		Assert.assertEquals(true,Util.almostEqual(10, res.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(23, res.getY(),DOUBLE_EQUALITY_EPSILON));
		
		
		ds = new SpatialDataset(2);
		res = ds.findMaxCoordinates();
		Assert.assertNull(res);
		
		ds = new SpatialDataset(2);
		ds.addSpatialData(sd1);
		res = ds.findMaxCoordinates();
		
		Assert.assertEquals(true,Util.almostEqual(-10, res.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(5, res.getY(),DOUBLE_EQUALITY_EPSILON));
	}
	
	@Test
	void testgetAttributeValue() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(100,53),"-10,5");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(10,-2),"-5,-1");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(12,-5),"0,15");
	
		
		SpatialDataset ds = new SpatialDataset(2);
		
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
			
		
		Assert.assertEquals(-10,ds.getAttributeValue(0, 0, SpatialData.COMMA),0.0001); Assert.assertEquals(5,ds.getAttributeValue(0, 1, SpatialData.COMMA),0.0001);
		Assert.assertEquals(-5,ds.getAttributeValue(1, 0, SpatialData.COMMA),0.0001); Assert.assertEquals(-1,ds.getAttributeValue(1, 1, SpatialData.COMMA),0.0001);
		Assert.assertEquals(0,ds.getAttributeValue(2, 0, SpatialData.COMMA),0.0001); Assert.assertEquals(15,ds.getAttributeValue(2, 1, SpatialData.COMMA),0.0001);
		
		double [] buf = new double[2];
		
		Assert.assertEquals(-10,ds._getAttributeValue(0, 0, SpatialData.COMMA,buf),0.0001); Assert.assertEquals(5,ds._getAttributeValue(0, 1, SpatialData.COMMA,buf),0.0001);
		Assert.assertEquals(-5,ds._getAttributeValue(1, 0, SpatialData.COMMA,buf),0.0001); Assert.assertEquals(-1,ds._getAttributeValue(1, 1, SpatialData.COMMA,buf),0.0001);
		Assert.assertEquals(0,ds._getAttributeValue(2, 0, SpatialData.COMMA,buf),0.0001); Assert.assertEquals(15,ds._getAttributeValue(2 ,1, SpatialData.COMMA,buf),0.0001);
		
	}
	
	@Test
	void testgetAttributeValues() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(100,53),"-10,5");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(10,-2),"-5,-1");
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(12,-5),"0,15");
	
		
		SpatialDataset ds = new SpatialDataset(2);
		
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
			
		List<Double> re1 = ds.getAttributeValues(0, SpatialData.COMMA);
		List<Double> re2 = ds.getAttributeValues(1, SpatialData.COMMA);
		
		Assert.assertEquals(3,re1.size());
		Assert.assertEquals(3,re2.size());
		
		Assert.assertEquals(-10,re1.get(0),0.0001);Assert.assertEquals(5,re2.get(0),0.0001);
		Assert.assertEquals(-5,re1.get(1),0.0001);Assert.assertEquals(-1,re2.get(1),0.0001);
		Assert.assertEquals(0,re1.get(2),0.0001);Assert.assertEquals(15,re2.get(2),0.0001);
		
		ds._getAttributeValues(0, SpatialData.COMMA,re1);
		ds._getAttributeValues(1, SpatialData.COMMA,re2);
		
		Assert.assertEquals(3,re1.size());
		Assert.assertEquals(3,re2.size());
		
		Assert.assertEquals(-10,re1.get(0),0.0001);Assert.assertEquals(5,re2.get(0),0.0001);
		Assert.assertEquals(-5,re1.get(1),0.0001);Assert.assertEquals(-1,re2.get(1),0.0001);
		Assert.assertEquals(0,re1.get(2),0.0001);Assert.assertEquals(15,re2.get(2),0.0001);
		
		
	}
	
	
	@Test
	void testFindBoudningBox() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),null);
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),null);
		SpatialData sd3 = new SpatialData(3,new Point2D.Double(0,15),null);
		SpatialData sd4 = new SpatialData(1,new Point2D.Double(5,23),null);
		SpatialData sd5 = new SpatialData(2,new Point2D.Double(10,-15),null);
		
		SpatialDataset ds = new SpatialDataset(2);

		
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);		
		ds.addSpatialData(sd3);
		ds.addSpatialData(sd4);
		ds.addSpatialData(sd5);		
		
		BoundingBox bb = ds.computeBoundingBox();
		
		Point2D res = bb.getMaxCoords();
		
		Assert.assertEquals(true,Util.almostEqual(10, res.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(23, res.getY(),DOUBLE_EQUALITY_EPSILON));
		
		res = bb.getMinCoords();
	
		Assert.assertEquals(true,Util.almostEqual(-10, res.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(-15, res.getY(),DOUBLE_EQUALITY_EPSILON));
		
		
		
		ds = new SpatialDataset(2);
		bb = ds.computeBoundingBox();
		Assert.assertNull(bb);
		
		ds = new SpatialDataset(2);
		ds.addSpatialData(sd1);
		bb = ds.computeBoundingBox();
		
		res = ds.findMaxCoordinates();
		
		Assert.assertEquals(true,Util.almostEqual(-10, res.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(5, res.getY(),DOUBLE_EQUALITY_EPSILON));
		
		res = ds.findMinCoordinates();
		
		Assert.assertEquals(true,Util.almostEqual(-10, res.getX(),DOUBLE_EQUALITY_EPSILON));
		Assert.assertEquals(true,Util.almostEqual(5, res.getY(),DOUBLE_EQUALITY_EPSILON));
	}
	
	@Test
	void testgetAttributeStringValue() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1,2,3");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"5,6,7");
		SpatialDataset ds = new SpatialDataset(2);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		
		Assert.assertEquals("1",ds.getAttributeStringValue(0, 0,SpatialData.COMMA));
		Assert.assertEquals("2",ds.getAttributeStringValue(0, 1,SpatialData.COMMA));
		Assert.assertEquals("3",ds.getAttributeStringValue(0, 2,SpatialData.COMMA));
		Assert.assertEquals("5",ds.getAttributeStringValue(1, 0,SpatialData.COMMA));
		Assert.assertEquals("6",ds.getAttributeStringValue(1, 1,SpatialData.COMMA));
		Assert.assertEquals("7",ds.getAttributeStringValue(1, 2,SpatialData.COMMA));
	}
	
	
	@Test
	void testGetNumberOfAttributes() {
		SpatialData sd1 = new SpatialData(1,new Point2D.Double(-10,5),"");
		SpatialData sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"");
		
		SpatialDataset ds = new SpatialDataset(2);
		
		Assert.assertEquals(0,ds.getNumberOfAttributes(SpatialData.COMMA));
		
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		
		
		Assert.assertEquals(0,ds.getNumberOfAttributes(SpatialData.COMMA));
		
		sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1,2,3");
		sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"1,2,3");
		
		ds = new SpatialDataset(2);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		
		Assert.assertEquals(3,ds.getNumberOfAttributes(SpatialData.COMMA));
		
		sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1 2 3");
		sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"1 2 3");
		
		ds = new SpatialDataset(2);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		
		Assert.assertEquals(3,ds.getNumberOfAttributes(" "));
		
		sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1,2");
		sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"1,2");
		
		ds = new SpatialDataset(2);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		
		Assert.assertEquals(2,ds.getNumberOfAttributes(SpatialData.COMMA));
		
		sd1 = new SpatialData(1,new Point2D.Double(-10,5),"1");
		sd2 = new SpatialData(2,new Point2D.Double(-5,-1),"1");
		
		ds = new SpatialDataset(2);
		ds.addSpatialData(sd1);
		ds.addSpatialData(sd2);
		
		Assert.assertEquals(1,ds.getNumberOfAttributes(SpatialData.COMMA));
		

	}
}
