package test;

import org.junit.jupiter.api.Test;

import aggregation.MovingWindow;
import junit.framework.Assert;

class TestMovingWindow {

	@Test
	void test() {
		MovingWindow w = new MovingWindow(4);
		
		
		
		w.add(1);
		//1
		Assert.assertEquals(1,w.mean(),0.0001);
		
		w.add(2);
		//1,2
		Assert.assertEquals(1.5,w.mean(),0.0001);
		
		w.add(3);
		//1,2,3
		Assert.assertEquals(2,w.mean(),0.0001);
		
		w.add(4);
		//1,2,3,4
		Assert.assertEquals(2.5,w.mean(),0.0001);
		
		w.add(5);
		//2,3,4,5
		Assert.assertEquals(3.5,w.mean(),0.0001);
		
		
		//restart 
		w.clear();
		w.add(1);
		//1
		Assert.assertEquals(1,w.mean(),0.0001);
		
		w.add(2);
		//1,2
		Assert.assertEquals(1.5,w.mean(),0.0001);
		
		w.add(3);
		//1,2,3
		Assert.assertEquals(2,w.mean(),0.0001);
		
		w.add(4);
		//1,2,3,4
		Assert.assertEquals(2.5,w.mean(),0.0001);
		
		w.add(5);
		//2,3,4,5
		Assert.assertEquals(3.5,w.mean(),0.0001);
	}

}
