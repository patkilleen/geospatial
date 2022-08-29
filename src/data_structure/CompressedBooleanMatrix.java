package data_structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Compresses a boolean matrix of flags by a factor of 8 by using bits in unsigned 8-bit words
 * instead of a matrix of booleans
 * @author Not admin
 *
 */
public class CompressedBooleanMatrix {

	final static int NUMBER_BITS_PER_BYTE = 8;
	private CompressedBooleanArray [] booleanArrayRows;
	private int nCols;
	private int nRows;
	public CompressedBooleanMatrix(int nrow, int ncol) {
		nCols= ncol;
		nRows = nrow;
		booleanArrayRows = new CompressedBooleanArray[nrow];
		
		for(int i = 0;i<nrow;i++) {
			CompressedBooleanArray boolArr = new CompressedBooleanArray(ncol);
			booleanArrayRows[i]=boolArr;
		}
		
	}
	
	public void setFlag(int rIx, int cIx, boolean flag) {
		
		CompressedBooleanArray boolArr = booleanArrayRows[rIx];
		boolArr.setFlag(cIx, flag);		
		
		
	}
	
	public boolean getFlag(int rIx, int cIx) {
		CompressedBooleanArray boolArr = booleanArrayRows[rIx];
		return boolArr.getFlag(cIx);
	}

	
	 
	public int getNumberOfRows() {
		return nRows;
	}
	public int getNumberOfColumns() {
		return nCols;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		CompressedBooleanMatrix other = (CompressedBooleanMatrix) obj;
		
		if(nCols != other.nCols || nRows != other.nRows) {
			return false;
		}
		
		if(booleanArrayRows.length != other.booleanArrayRows.length) {
			return false;
		}
		for(int i =0;i<booleanArrayRows.length;i++) {
			CompressedBooleanArray otherArr = other.booleanArrayRows[i];
			CompressedBooleanArray thisArr = booleanArrayRows[i];
			if (! otherArr.equals(thisArr)){
				return false;
			}
			
		}
		return true;
	}
	 
	 
	
	 public static class CompressedBooleanArray {

	 	private byte [] bitmap;
	 	public CompressedBooleanArray(int size) {
	 		int nCellsCompressed=Math.floorDiv(size,NUMBER_BITS_PER_BYTE)+1; //any excess stored in the bits of last byte if not perfectly divisible. +1 since if fewer than 8 elements, then it rounds down to 0, but we need at least one bye
	 		
	 		bitmap = new byte[nCellsCompressed];
	 	}
	 	
	 	public void setFlag(int ix, boolean flag) {
	 		
	 		int wordIx=Math.floorDiv(ix,NUMBER_BITS_PER_BYTE)  ;
	 		
	 		byte targetWord = bitmap[wordIx];
	 		
	 		//use byte since index of flag stored as bit (at most ix 7)
	 		int  flagIx =ix % NUMBER_BITS_PER_BYTE;
	 		//determine wehther the appropriate bit is 1 or 0 (true of false)
	 		byte mask = 1;
	 		mask = (byte) (mask << flagIx);//shift the 1 to align it with target bit (e..g, 0001 0000 would be index flagIndex = 4)
	 		
	 		if(!flag) {//set target bit to 0 
	 			bitmap[wordIx]= (byte) (targetWord & (~mask));//we flip all bits in mask, so the target bit is 0 in mask, and rest is 1, (e..g, 1110 1111) so bit-wise-and will zero the target bit without affecting other bits
	 		}else {//set target bith to 1
	 			bitmap[wordIx]= (byte) (targetWord | mask);//the target bit is 1 in mask, and rest is 0 so bit-wise-or will set 1 to the target bit without affecting other bits
	 		}
	 		
	 		
	 		
	 	}
	 	
	 	public boolean getFlag(int ix) {

	 		int wordIx=Math.floorDiv(ix,NUMBER_BITS_PER_BYTE) ;  
	 		
	 		
	 		//use byte since index of flag stored as bit (at most ix 7)
	 		int  flagIx =ix % NUMBER_BITS_PER_BYTE;
	 		//determine wehther the appropriate bit is 1 or 0 (true of false)
	 		byte mask = 1;
	 		mask = (byte) (mask << flagIx);//shift the 1 to align it with target bit (e..g, 0001 0000 would be index flagIndex = 4)
	 		
	 	
	 		//returns true when target bit is 1, and false when it is 0
	 		return (bitmap[wordIx] & mask) == mask;  
	 	}
	 	
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			
			CompressedBooleanArray other = (CompressedBooleanArray) obj;

			if(bitmap.length != other.bitmap.length) {
				return false;
			}
			
			for(int i =0;i<bitmap.length;i++) {
				byte b1 = bitmap[i];
				byte b2 = other.bitmap[i];
				if (b1!=b2) {
				return false;
				}
			}
			return true;
		}
		 

	 }


	 
}
