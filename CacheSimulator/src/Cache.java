 

public class Cache {
	
	private String input;
	private int address;
	private int offset;
	private int tag;
	private int slot;
	private int dirtyBit;
	
	
    Slot[] cache = new Slot[16];
    int[] mainMem = new int[2048];
	private int count = 0;
	
	
	public Cache(){
		for(int i=0; i<2048; i++)
		{
			if(count <= 0xFF)
			{
				mainMem[i] = count;
				count++;
			}
			else
			{
				count = 0;
				mainMem[i] = count;
				count++;
			}
		}
		
		for(int i=0; i<cache.length; i++)
		{
			cache[i] = new Slot();
			cache[i].setDirtyBit(0);
			cache[i].setValidBit(0);
			cache[i].setTag(0);
			cache[i].setData(i, 0);
		}
	}
	
	public int[] getBlock(int address){
		int blockBegin = address & 0xFFF0;
		int[] block = new int[16];
		int i = 0;
		
		while(i <= 0xF){
			block[i] = mainMem[blockBegin];
			blockBegin++;
			i++;
		}
		return block;
		
	}
	
	public int getSlot(int anAddress){
		int slotNum = (anAddress & 0x00F0)>>>4;
		return slotNum;
	}
	
	public int getTag(int anAddress){
		int tagNum = (anAddress & 0x0F00)>>>8;
		return tagNum;
	}
	
	public int getBlockOffset(int anAddress){
		int offset = (anAddress & 0x000F);
		return offset;
	}
	
	
}

