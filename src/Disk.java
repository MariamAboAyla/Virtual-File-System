
public class Disk {
	
	private int []blocks;
	private int freeBlocks;
	private int allocatedBlocks;

	public Disk(int diskSize)
	{
		blocks = new int[diskSize];
		for(int i=0; i<diskSize; i++)
		{
			blocks[i] = 0; //free
		}
	
		freeBlocks = diskSize; //initially the whole disk is empty
		allocatedBlocks=0;
	}
	
	/////////generic allocate///////////
	public int allocate()
	{
		if(freeBlocks <=0 )
		{
			System.out.println("Not enough space \n");
			return -1;
		}
		for(int i=0; i<blocks.length; i++)
		{
			if(blocks[i] == 0) //free
			{
				blocks[i] = 1; //allocated
				freeBlocks--;
				allocatedBlocks++;
				return i;
			}
		}
		return -1;
	}

	///////allocate a specific block//////
	public int allocate(int i)
	{
		if(blocks[i] == 0) //free
		{
			blocks[i] = 1; //allocated
			freeBlocks--;
			allocatedBlocks++;
			return i;
		}
		return -1;
	}
	////////////deallocate a specific block//////////
	public boolean deallocate(int i)
	{
		if(blocks[i] == 0) //already free
			return false;

		blocks[i] = 0; //free the block
		allocatedBlocks--;
		freeBlocks++;
		
		return true;
	}
///// return a string of 0s and 1s >> 1 if block is allocated, 0 if block is free. ///////
	public String CheckFreeSpace()
	{
		String bitVector="";
		
		for(int i=0; i<blocks.length; i++)
		{
			if(blocks[i] == 1) //block is allocated
				bitVector += "1";
			else //block is free
				bitVector += "0";
		}

		return bitVector;
	}
///////return blocks/////////
	public int getBlocks(int i)
	{
		return blocks[i];
	}
	
	///////return disk size///////////
	public int getDiskSize()
	{
		return blocks.length;
	}
	////////return 0 >> if free, 1 >> if allocated
	public int isallocated(int i) {
		return blocks[i];
	}
/////////////////	
	public int getNumOfFreeBlocks()
	{
		return freeBlocks;
	}
////////////////
	public int getNumOfAllocatedBlocks()
	{
		return allocatedBlocks;
	}

}
