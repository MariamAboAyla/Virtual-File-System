
public class Main {
	 public static void main(String[] args)
    {
    	Disk disk = new Disk(10);
    	int []blocks;
    	String st;
    	
    	blocks = new int[5];
    
    	for (int i=0; i<disk.getDiskSize(); i++)
    	{
    		disk.allocate(i);
    	
    	}
    	for (int i=0; i<blocks.length; i++)
    	{
    		disk.deallocate(i);
    	
    	}
    	
    	
      /* for(int i=0; i<disk.getDiskSize(); i++)
    	{
    	  
    	   int d = disk.getBlocks(i);
    		System.out.println(d);
    	}*/
    	
    	st = disk.CheckFreeSpace();
    	System.out.println(st);
       System.out.println(disk.getNumOfFreeBlocks());
       System.out.println(disk.getNumOfAllocatedBlocks());

     
    	
    
    }
}
