package test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import data_structure.CompressedBooleanMatrix;
import junit.framework.Assert;

class TestCompressedBooleanMatrix {

	@Test
	void testSettersGetters() {
		CompressedBooleanMatrix m = new CompressedBooleanMatrix(35,45);
		
		//all false bye default
		for(int i = 0;i<m.getNumberOfRows();i++) {
			for(int j = 0;j<m.getNumberOfColumns();j++) {
				
				Assert.assertEquals(false,m.getFlag(i, j));
				
			}
		}
		
		
		for(int i = 0;i<m.getNumberOfRows();i++) {
			for(int j = 0;j<m.getNumberOfColumns();j++) {
				m.setFlag(i, j, false);
				Assert.assertEquals(false,m.getFlag(i, j));
				m.setFlag(i, j, true);
				Assert.assertEquals(true,m.getFlag(i, j));
			}
		}
		
		//set odd numbered indices to 0, and even to 1
		for(int i = 0;i<m.getNumberOfRows();i++) {
			for(int j = 0;j<m.getNumberOfColumns();j++) {
				m.setFlag(i, j, j % 2 == 0);
				
			}
		}
		//check odd numbered indices to 0, and even to 1
		for(int i = 0;i<m.getNumberOfRows();i++) {
			for(int j = 0;j<m.getNumberOfColumns();j++) {
				Assert.assertEquals(j % 2 == 0,m.getFlag(i, j));
				
				
			}
		}
		
		//set odd numbered indices to 1, and even to 0
				for(int i = 0;i<m.getNumberOfRows();i++) {
					for(int j = 0;j<m.getNumberOfColumns();j++) {
						m.setFlag(i, j, j % 2 == 1);
						
					}
				}
				//check odd numbered indices to 1, and even to 0
				for(int i = 0;i<m.getNumberOfRows();i++) {
					for(int j = 0;j<m.getNumberOfColumns();j++) {
						Assert.assertEquals(j % 2 == 1,m.getFlag(i, j));
						
						
					}
				}
	}

	@Test
	void testEquals() {
		CompressedBooleanMatrix m1 = new CompressedBooleanMatrix(35,45);
		CompressedBooleanMatrix m2 = new CompressedBooleanMatrix(35,45);
		Assert.assertEquals(true, m1.equals(m1));
		Assert.assertEquals(true, m1.equals(m2));
		
		m2.setFlag(14, 15, true);
		
		Assert.assertEquals(false, m1.equals(m2));
		
		m2.setFlag(8,8, true);
		
		Assert.assertEquals(false, m1.equals(m2));
		
		m1.setFlag(14, 15, true);
		m1.setFlag(8,8, true);
		
		Assert.assertEquals(true, m1.equals(m2));
		
	}

}
