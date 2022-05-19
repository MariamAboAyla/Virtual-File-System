import java.util.ArrayList;
import java.util.HashMap;

public class IndexedAllocation{
    private final ArrayList<Integer> blocks = new ArrayList<>();
    private final HashMap<Integer, ArrayList<Integer>> IndexBlocks = new HashMap<>();
    private final ArrayList<BlockDetails> StoredFiles  = new ArrayList<>();
    private final HashMap<String , Integer> StoredFilesToIndexFiles = new HashMap<>();

    String VFS = "VFIndexed.vfs";

    IndexedAllocation(int size){
        for(int i = 0 ;i < size ; i++) {
            blocks.add(-1);
        }
    }

    public int allocate(VFile file) {

        int startIndex = blocks.indexOf(-1); // find first free block
        ArrayList<Integer> blocksForFile = new ArrayList<>(); // create array of blocks for file

        if(startIndex != -1) { // if there is free block
            blocks.set(startIndex, 1); // set block to used

            for(int i = 0 ;i < file.getSize() ; i++) { // allocate blocks for file
                int freeSpace = blocks.indexOf(-1); // find first free block
                if(freeSpace > -1) { // if there is free block
                    blocksForFile.add(freeSpace); // add block to array of blocks for file
                    blocks.set(freeSpace, 1); // set block to used
                }
                else {
                    System.out.println("No enough space for file, please delete some files");
                    blocks.set(startIndex, -1); // set block to free
                    for(Integer e :  blocksForFile) // deallocate
                        blocks.set(e, -1); // set block to free
                    return -1;
                }
            }
            // StoredFiles.add(new Pair(file.getFilePath() , startIndex));
            StoredFilesToIndexFiles.put(file.getFilePath() ,startIndex ); // add file to list of files
            file.setAllocatedBlocks(blocksForFile); // set blocks for file
            file.setStartIndex(startIndex); // set start index for file
            IndexBlocks.put(startIndex, blocksForFile); // add blocks for file to index

        }
        else {

            System.out.println("No enough space for file, please delete some files");
            return -1;
        }
        return startIndex;
    }

    public void deAllocate(VFile file) {
        int i;
        for(i = 0 ;i < StoredFiles.size();i++){ // find file in list of files
            if(StoredFiles.get(i).path.equals(file.getFilePath())){ // if file found
                ArrayList<Integer> bl = IndexBlocks.get(StoredFiles.get(i).indexBlock); // get blocks for file
                IndexBlocks.remove(StoredFiles.get(i).indexBlock); // remove blocks from index
                StoredFiles.remove(i); // remove file from list of files
                for (Integer integer : bl) { // deallocate blocks
                    blocks.set(integer, -1); // set block to free
                }
            }
        }
    }



}
