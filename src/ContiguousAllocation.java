import java.util.ArrayList;


public class ContiguousAllocation implements AllocationManager  {

	private ArrayList<Integer> blocks = new ArrayList<Integer>();
	private int fileStartIndex = blocks.indexOf(-1);

//=====================================constructor======================================//	
	public ContiguousAllocation(int size) 
	{	
		for(int i = 0 ; i < size ; i++)
		{
			blocks.add(-1);
		}
	}
	
//======================================Allocate blocks for file======================================//
	@Override
	public int allocate(VFile file) {
        
			int start = fileStartIndex;

			int smallestSize=0;
			int size = file.get_Size();
			boolean validBlocks = false; //initialize
			for(int i = 0 ; i < blocks.size() ; i++)
			{
				if (smallestSize==size)
				{
					validBlocks = true;
					break;
				}
				if(blocks.get(i)!= -1) //allocated
				{
					smallestSize=0;
				}
				else
				{
					if(smallestSize==0){
						start = i;
					}
					smallestSize++;

				}
			}
			if(smallestSize==size)
			{
				validBlocks = true;
			}
			if (validBlocks) { //free
				for(int i = start ; i < size+start ; i++)
				{
					blocks.set( i, 1); //allocate

				}
				file.set_StartIndex(start);
				return start;
			}
			else
			{
				System.out.println("Couldn't allocate block.");
				return -1;
			}
				

         
//			ArrayList<Integer> StartIndexes = new ArrayList<Integer>();
//			ArrayList<Integer> blocksSize = new ArrayList<Integer>();
//
//			getFreeBlocks(StartIndexes , blocksSize);
//
//			int startIndx = GetStartIndex(StartIndexes , blocksSize , file.get_Size());
//
//			if(startIndx !=-1) {
//				fileStartIndex = startIndx;
//				for(int i = 0 ; i < file.get_Size() ; i++)
//				{
//					blocks.set(startIndx+i,1);
//					file.get_AllocatedBlocks().add(startIndx+i);
//				}
//			}
//

}
		
//=============================Free Space Manager=========================================//
	@Override
	public void displayDiskStatus()
	{
		
		int allocatedSpace = 0 , emptySpace = 0;
		String bitVector="";
		for(int i = 0 ; i < blocks.size() ; i++)
		{
			
			
       		if(blocks.get(i)!=-1)
			{
       			allocatedSpace++;
				bitVector += "1";
			}
			else
			{
				emptySpace++;
				bitVector += "0";
			}
		}
	
		System.out.println("Empty space: " + emptySpace);
		System.out.println("Allocated space: " + allocatedSpace);
	
		System.out.print("Disk space: ");
		System.out.println(bitVector + " ");
		
	}
//=============================get free blocks=========================================//
	private void getFreeBlocks(ArrayList<Integer> startIndexes, ArrayList<Integer> blocksSize) 
	{
		int counter = 0; 
		for(int i = 0 ; i < blocks.size() ; i++)
		{
			if(blocks.get(i) == -1) // if free block
			{
				counter++;
			}
			else if(counter > 0 && blocks.get(i)!= -1) // block is free
			{
				startIndexes.add(i - counter); //add a potential start index (block)
				blocksSize.add(counter); //add its size
				
				counter =0;
			}
		}
		if(counter > 0)
		{
			startIndexes.add(blocks.size() - counter);
			blocksSize.add(counter);
			
			counter =0;
		}
	}
//===============get start index using best fit approach==================//
	private int GetStartIndex(ArrayList<Integer> startIndexes, ArrayList<Integer> blocksSize, int size) 
	{

	int minSize = Integer.MAX_VALUE;
		int minIndex = -1;
		for(int i = 0 ; i < startIndexes.size() ; i++)
		{
			if(blocksSize.get(i) >= size)
			{
				if(blocksSize.get(i) < minSize)
				{
					minSize = blocksSize.get(i);
					minIndex = startIndexes.get(i);
				}
			}
		}
		if(minIndex != -1) {
			return minIndex;
		}
		else
		{
			return -1;
		}}
	
//====================represent the allocated blocks ============================//
	public void WriteFiles(VFile file) 
	{
		String Lines="";
		for(int i = 0 ; i < file.get_Size() ; i++)
		{
			
	       Lines = file.get_FileName()+" "+fileStartIndex+" "+file.get_Size()+"\n";
			
		}
		System.out.println(Lines);
	}
//====================deallocate file blocks ============================//
	@Override
	public void deAllocate(VFile file) {
     	
		for(int i = file.get_StartIndex() ; i < file.get_Size()+file.get_StartIndex() ; i++)
		{
			blocks.set(i, -1);
		}

	}
//=============================================================================//
}


