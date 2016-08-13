import java.util.Scanner;
import java.util.Arrays;

public class Simulator {
	
	Cache myCache;
	Slot aSlot;
	
	private static String input;
	private int address;
	private int prevAddress;
	private int blockOffset;
	private int slot;
	private int tag;
	private int data;
	private int valid;
	private int dirty;
	
	static Scanner keyBoard = new Scanner(System.in);
	static Simulator mySim = new Simulator();
	
	public Simulator(){
		myCache = new Cache();
		aSlot = new Slot();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Put cursor in console and press enter to start.");
		while(keyBoard.hasNextLine()){
			System.out.println("(R)ead, (W)rite, or (D)isplay cache?");
			input = keyBoard.next();
			if(input.equalsIgnoreCase("R")){
				mySim.readAddress();
			}
			else if(input.equalsIgnoreCase("W")){
				mySim.write();
			}
			else if(input.equalsIgnoreCase("D")){
				mySim.display();
			}
			continue;
		}
		
		
		
	}
	
	public void readAddress(){
		System.out.println("What address would you like to read? ");
		
		String temp = keyBoard.next();
		address = Integer.parseInt(temp, 16);
		blockOffset = mySim.myCache.getBlockOffset(address);
		slot = mySim.myCache.getSlot(address);
		tag = mySim.myCache.getTag(address);
		data = mySim.myCache.cache[slot].getDataAt(blockOffset);
	    dirty = mySim.myCache.cache[slot].getDirtyBit();
		valid = mySim.myCache.cache[slot].getValidBit();
		String hexAddress = Integer.toHexString(address);
		
		
		if(valid == 0){
			int[] tempBlock = mySim.myCache.getBlock(address);
			System.out.println("Address " + hexAddress + " not found in cache(cache miss)");
			for(int i=0; i<16; i++)
			{
				mySim.myCache.cache[slot].setData(i, tempBlock[i]);
			}
			mySim.myCache.cache[slot].setValidBit(1);
			mySim.myCache.cache[slot].setTag(tag);
		}
//		else if ((valid == 1) && !(tag == mySim.myCache.cache[slot].getTag())){
//			int[] tempBlock = mySim.myCache.getBlock(address);
//			System.out.println("Address " + hexAddress + " not found in cache(cache miss)");
//			for(int i=0; i<16; i++)
//			{
//				mySim.myCache.cache[slot].setData(i, tempBlock[i]);
//			}
//			//mySim.myCache.cache[slot].setValidBit(1);
//			mySim.myCache.cache[slot].setTag(tag);
//		}
//		else if ((valid == 1) && (dirty == 1) && !(tag == mySim.myCache.cache[slot].getTag())){
//			
//		}
		else if(tag == mySim.myCache.cache[slot].getTag()){
			System.out.println("At that byte there is the value " + Integer.toHexString(data) + "(Cache Hit).");
		}
	}
	
	public void write(){
		System.out.println("What address would you like to write to?.");
		
		String temp1 = keyBoard.next();
		address = Integer.parseInt(temp1, 16);
		
		System.out.println("What data would you like to write at that address?");
		
		String temp2 = keyBoard.next();
		int tempData = Integer.parseInt(temp2, 16);
		String hexTempData = Integer.toHexString(tempData);
		String hexAddress = Integer.toHexString(address);
		
		blockOffset = mySim.myCache.getBlockOffset(address);
		slot = mySim.myCache.getSlot(address);
		tag = mySim.myCache.getTag(address);
		data = mySim.myCache.cache[slot].getDataAt(blockOffset);
	    dirty = mySim.myCache.cache[slot].getDirtyBit();
		valid = mySim.myCache.cache[slot].getValidBit();
		
		if((valid == 0) && (dirty ==0) ){
			int[] tempBlock = mySim.myCache.getBlock(address);
			for(int i=0; i<16; i++)
			{
				mySim.myCache.cache[slot].setData(i, tempBlock[i]);
			}
			mySim.myCache.cache[slot].setValidBit(1);
			mySim.myCache.cache[slot].setDirtyBit(1);
			mySim.myCache.cache[slot].setTag(tag);
			mySim.myCache.cache[slot].setData(blockOffset, tempData);
			System.out.println("Value " + hexTempData + " has been written to address " + hexAddress + "(Cache Miss)");
		}
		else if((valid == 1) && (dirty == 0) && tag == mySim.myCache.cache[slot].getTag()){
			mySim.myCache.cache[slot].setData(blockOffset, tempData);
			mySim.myCache.cache[slot].setDirtyBit(1);
			System.out.println("Value " + hexTempData + " has been written to address " + hexAddress + "(Cache Hit)");
		}
		else if((valid == 1) && (dirty == 1) && tag == mySim.myCache.cache[slot].getTag()){
			mySim.myCache.cache[slot].setData(blockOffset, tempData);
			//mySim.myCache.cache[slot].setDirtyBit(1);
			System.out.println("Value " + hexTempData + " has been written to address " + hexAddress + "(Cache Hit)");
		}
		else if((valid == 1) && (dirty == 1) && !(tag == mySim.myCache.cache[slot].getTag())){
			int[] writeBack = mySim.myCache.cache[slot].getData();
			int[] tempBlock = mySim.myCache.getBlock(address);
			String prevValue = Integer.toHexString(writeBack[blockOffset]);
			int temp = prevAddress & 0xFFF0;
			for(int i=0; i<16; i++)
			{
				mySim.myCache.mainMem[temp] = writeBack[i];
				temp++;
			}
			for(int i=0; i<16; i++)
			{
				mySim.myCache.cache[slot].setData(i, tempBlock[i]);
			}
			mySim.myCache.cache[slot].setDirtyBit(1);
			mySim.myCache.cache[slot].setTag(tag);
			mySim.myCache.cache[slot].setData(blockOffset, tempData);
			System.out.println("The block at slot " + Integer.toHexString(slot) + " has been replaced and "
					+ hexTempData + " has been written to address " + hexAddress + "(Cache Miss)");
			
		}
		prevAddress = address;
	}
	
	public void display(){
		
		
		System.out.println("valid dirty tag   slot");
		
		for(int i=0; i<16; i++)
		{
			String tempValid = Integer.toHexString(mySim.myCache.cache[i].getValidBit());
			String tempDirty = Integer.toHexString(mySim.myCache.cache[i].getDirtyBit());
			String tempTag = Integer.toHexString(mySim.myCache.cache[i].getTag());
			String tempSlot = Integer.toHexString(i);
			System.out.print(tempValid + "     " + tempDirty + "      " + tempTag + "      " + tempSlot + "    ");
			for(int j=0; j<16; j++)
			{
				String tempData = Integer.toHexString(mySim.myCache.cache[i].getDataAt(j));
				System.out.print( tempData + "  ");
			}
			System.out.println();
		}
	}
	
	public String convertStringToHex(String str){
		  
		  char[] chars = str.toCharArray();
		  
		  StringBuffer hex = new StringBuffer();
		  for(int i = 0; i < chars.length; i++){
		    hex.append(Integer.toHexString((int)chars[i]));
		  }
		  
		  return hex.toString();
	  }


}
