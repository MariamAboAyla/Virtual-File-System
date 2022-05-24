import java.util.*;


public class LinkedAllocation implements AllocationManager {

    private ArrayList<Integer> blocks = new ArrayList<>(); // of blocks size
    private final HashMap<String, String> directories = new HashMap <>(); // directories -> directory name points to file name; could acess ile from files map
    private final HashMap<VFile, LinkedList<Integer> > files = new HashMap<> (); // file -> linked-list of assigned blocks

    public final String VFS = "VFIndexed.vfs";

    LinkedAllocation(int size)
    {
        for(int i = 0 ;i < size ; i++)
        {
            blocks.add(0);
        }
    }

    void setBlocks (ArrayList<Integer> blocks)
    {
        this.blocks = blocks;
    }

    ArrayList<Integer> getBlocks ()
    {
        return this.blocks;
    }

    @Override
    public int allocate (VFile file)
    {
        int nextBlockIndex = blocks.indexOf ( -1 ); // first available block
        blocks.set ( nextBlockIndex , 1 );
        int fileSize = file.get_Size ();
        LinkedList<Integer> fileBlocks = new LinkedList<> ( ) ;
        fileBlocks.add ( nextBlockIndex );

        int emptySpaceSize = getSpaceManager();
        if((file.get_Size () > blocks.size ())  ||  emptySpaceSize<file.get_Size () )
        {
            System.out.println ("Unable to allocate file! No Enough Space !" );
            return -1;
        }

        for (int i=0; i<fileSize; i++)
        {
            nextBlockIndex = blocks.indexOf ( - 1 );// first available block
            blocks.set ( nextBlockIndex , 1 );
            fileBlocks.add ( nextBlockIndex );
        }

        files.put ( file, fileBlocks); // add the file and its assigned blocks to list

        String directoryName = getDirectoryName(file);
        directories.put ( directoryName, file.get_FileName () );
        // add the file to "files" and add the file to the directory

        return 1;

    }

    @Override
    public void displayDiskStatus() {

    }

    @Override
    public void deAllocate(VFile file)
    {
        ///

    }

    public void displayAllocatedBlocks(String filePath)
    {
        VFile vFile = null;
        LinkedList<Integer> list = new LinkedList<> ();
        int startIndex = 0, endIndex = 0;
        for (Map.Entry<VFile, LinkedList<Integer> > iterator: files.entrySet ())
        {
            if( Objects.equals ( iterator.getKey ( ).get_FileName ( )   ,    filePath ) )
            {
                vFile = iterator.getKey ();
                list = iterator.getValue ( );

            }
        }
        if(vFile == null)
        {
            System.out.println ("No such a file !" );
            return;
        }

        System.out.println ( filePath + "   "+startIndex+"  "+endIndex);
        int prev = startIndex;
        for (Integer iterator: list)
        {
            if(iterator==startIndex) continue;
            System.out.println ( prev + "   " + iterator);
            prev = iterator;
        }
        System.out.println (prev + "   nil" );


    }

    String getDirectoryName(VFile file)
    {

            return "";
    }

    int getSpaceManager()
    {
        // get the size of the empty blocks remaining
        int emptySize = 0;
        StringBuilder freeSpace = getFreeSpaceManager();
        for (int i=0; i<freeSpace.length (); i++)
        {
            if (freeSpace.charAt ( i ) == '0')
            {
                emptySize++;
            }
        }

        return emptySize;
    }

    StringBuilder getFreeSpaceManager()
    {
        // get the blocks in  a string of 0's and 1's ->
        // if 0 means block at ith item is empty, while if 1 means block at ith item is accompained
        StringBuilder result = new StringBuilder ( );
        for (Integer block : blocks) {
            result.append ( Integer.toString ( block ) );
        }

        return result;
    }



}
