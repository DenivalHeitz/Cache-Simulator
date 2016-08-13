
public class Slot {
	
	private int validBit;
	private int tag;
	private int[] data = new int[16];
	private int dirtyBit;
	
	
	public int getValidBit(){
		return validBit;
	}
	
	public void setValidBit(int aValidBit){
		this.validBit = aValidBit;
	}
	
	public int getTag(){
		return tag;
	}
	
	public void setTag(int aTag){
		this.tag = aTag;
	}
	
	public int[] getData(){
		return data;
	}
	
	public int getDataAt(int index){
		return data[index];
	}
	
	public void setData(int index, int aData){
		this.data[index] = aData;
	}
	
	public int getDirtyBit(){
		return dirtyBit;
	}
	
	public void setDirtyBit(int aDirtyBit){
		this.dirtyBit = aDirtyBit;
	}

}
