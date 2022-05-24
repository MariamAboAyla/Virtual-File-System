import java.util.*;


public class LinkedAllocation implements AllocationMethod {

    private ArrayList<Integer> blocks = new ArrayList<>(); // of blocks size
    private final HashMap<String, ArrayList<String>> directories = new HashMap <>(); // directories -> directory name points to file name; could acess ile from files map
    private final HashMap<VFile, LinkedList<Integer> > files = new HashMap<> (); // file -> linked-list of assigned blocks

    public final String VFS = "VFIndexed.vfs";

    LinkedAllocation(int size)
    {
        for(int i = 0 ;i < size ; i++)
        {
            blocks.add(0);
        }
    }

    public void setBlocks (ArrayList<Integer> blocks)
    {
        this.blocks = blocks;
    }

    public ArrayList<Integer> getBlocks ( )
    {
        return this.blocks;
    }


    public boolean allocate (VFile file)
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
            return false;
        }

        for (int i=0; i<fileSize; i++)
        {
            nextBlockIndex = blocks.indexOf ( - 1 );// first available block
            blocks.set ( nextBlockIndex , 1 );
            fileBlocks.add ( nextBlockIndex );
        }

        files.put ( file, fileBlocks); // add the file and its assigned blocks to list

        String directoryName = getDirectoryName(file);
        ArrayList<String> tmpDirectoryFiles = directories.get ( directoryName );
        tmpDirectoryFiles.add ( file.get_FileName () );
        directories.put ( directoryName, tmpDirectoryFiles );
                //      directories.put ( directoryName, file.get_FileName () );
        // add the file to "files" and add the file to the directory

        return true;

    }

    public boolean deAllocate(String filePath)
    {
        //if delete it -> will delete from: directory list - file list - deallocate blocks
        VFile vFile = null;
        LinkedList<Integer> list = new LinkedList<> ();

        for (Map.Entry<VFile, LinkedList<Integer> > iterator: files.entrySet ())
        {
            if( Objects.equals ( iterator.getKey ( ).get_FilePath ( )   ,    filePath ) )
            {
                vFile = iterator.getKey ();

                //deallocates the blocks
                for (Integer allocatedBlocks: iterator.getValue () )
                {
                    blocks.set ( allocatedBlocks, 0 ); // deallocate blocks - mark free
                }

                // empties the blocks linked list of the file
                iterator.getValue ().clear ();

                // empties the file itself from the directory list
                String fileName = vFile.get_FileName ();
                String directoryName = filePath.substring ( 0, (filePath.length () - fileName.length () ) ); // to get the directory name from path
                ArrayList<String> directoryContent = directories.get ( directoryName );
                directoryContent.remove ( fileName );
                directories.remove ( directoryName );
                directories.put ( directoryName, directoryContent );

                // empties the file from the file list
                files.remove ( iterator.getKey (), iterator.getValue () ); // delete it from files list


                return true;

            }
        }

        return false;

    }

    public void displayAllocatedBlocks(String filePath)
    {
        VFile vFile = null;
        LinkedList<Integer> list = new LinkedList<> ();
        int startIndex = 0, endIndex = 0;
        for (Map.Entry<VFile, LinkedList<Integer> > iterator: files.entrySet ())
        {
            if( Objects.equals ( iterator.getKey ( ).get_FilePath ( )   ,    filePath ) )
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
        String filePath = file.get_FilePath ();

        for ( Map.Entry<String, ArrayList<String>> currentDirectory: directories.entrySet ())
        {
            if(filePath.contains ( currentDirectory.getKey () ))
            {

            }
        }

        return "";
    }

    public int getSpaceManager()
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

    public StringBuilder getFreeSpaceManager()
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
