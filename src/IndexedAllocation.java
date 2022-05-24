import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class IndexedAllocation{
    private final ArrayList<Integer> blocks = new ArrayList<>();
    private final HashMap<Integer, ArrayList<Integer>> IndexBlocks = new HashMap<>();
    private final ArrayList<BlockDetails> StoredFiles  = new ArrayList<>();
    private final HashMap<String , Integer> StoredFilesToIndexFiles = new HashMap<>();

    public final String VFS = "VFIndexed.vfs";

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

            for(int i = 0 ;i < file.get_Size() ; i++) { // allocate blocks for file
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
            StoredFilesToIndexFiles.put(file.get_FileName() ,startIndex ); // add file to list of files
            file.set_AllocatedBlocks(blocksForFile); // set blocks for file
            //file.setStartIndex(startIndex); // set start index for file
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
            if(StoredFiles.get(i).path.equals(file.get_FileName())){ // if file found
                ArrayList<Integer> bl = IndexBlocks.get(StoredFiles.get(i).indexBlock); // get blocks for file
                IndexBlocks.remove(StoredFiles.get(i).indexBlock); // remove blocks from index
                StoredFiles.remove(i); // remove file from list of files
                for (Integer integer : bl) { // deallocate blocks
                    blocks.set(integer, -1); // set block to free
                }
            }
        }
    }

    public void LoadHardDisk(Directory root) {
        File file = new File(VFS);

        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String st1 = sc.nextLine(); // read line
                String[] filesFromSize = st1.split(" \\| "); // split file name and size
                String[] files =filesFromSize[0].split("\\/");; // split file name and path
                String[] sizes = filesFromSize[1].split(" "); // split size
                if(files.length == 2) {
                    if(files[1].contains(".")) {
                        root.addFile(new VFile(filesFromSize[0], sizes.length)); // add file to root
                        allocateFileFromDisk(sizes, filesFromSize[0]); // allocate file from disk
                    }
                    else {
                        root.addDirectory(new Directory(filesFromSize[0])); // add directory to root
                    }
                }
                else {
                    Directory cur = root;
                    StringBuilder CurPath = new StringBuilder(root.getDirectoryPath() + '/'); // create path for directory
                    for(int i = 1 ; i < files.length - 1 ; i++) { // find directory in root
                        cur = cur.getSubDirectory(CurPath + files[i]); // get directory
                        CurPath.append(files[i]).append("/"); // add directory to path
                    }
                    if(files[files.length - 1].contains(".")) { // if file
                        cur.addFile(new VFile(filesFromSize[0], sizes.length)); // add file to directory
                        allocateFileFromDisk(sizes, filesFromSize[0]); // allocate file from disk
                    }
                    else {
                        cur.addDirectory(new Directory(filesFromSize[0]));
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        for (BlockDetails storedFile : StoredFiles) {
            StoredFilesToIndexFiles.put(storedFile.path, storedFile.indexBlock);
        }

    }
    void allocateFileFromDisk(String []arr , String name) {
        int startIndex = Integer.parseInt(arr[0]);
        ArrayList<Integer> blocksForFile= new ArrayList<>();

        blocks.set(startIndex, 0);
        for(int i = 1 ; i < arr.length ; i++) {
            blocksForFile.add(Integer.parseInt(arr[i]));
            blocks.set(Integer.parseInt(arr[i]), 1);
        }
        StoredFiles.add(new BlockDetails(name , startIndex));
        IndexBlocks.put(startIndex, blocksForFile);

    }

    
    public void SaveHardDisk(Directory root) {
        String path;
        if(root.getDirectoryPath().equals("root"))
            clearFile();
        else
            appendOnFile(root.getDirectoryPath() + " | -1");

        ArrayList<Directory> rootDirectories = root.getSubDirectories();

        saveFiles(root.getFiles());
        for(int i = 0 ; i < root.getSubDirectories().size();i++)
            SaveHardDisk(rootDirectories.get(i));

    }
    void saveFiles(ArrayList<VFile> files){
        for(int i = 0 ;i < files.size();i++){
            saveFile(files.get(i).get_FilePath());
        }
    }
    void saveFile(String path){
        Integer indexFile =  StoredFilesToIndexFiles.get(path);
        path = path  + " | "  + indexFile + " ";
        ArrayList<Integer> allocatedBlocks = IndexBlocks.get(indexFile);
        StringBuilder pathBuilder = new StringBuilder(path);
        for (Integer allocatedBlock : allocatedBlocks) {
            pathBuilder.append(allocatedBlock).append(" ");
        }
        path = pathBuilder.toString();
        appendOnFile(path);
        path = "";
    }
    void clearFile() {
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(VFS, false));
            out.close();
        }
        catch (IOException e) {
            System.out.println("EXCEPTION!" + e);
        }
    }
    void appendOnFile(String str ){
        try{

            BufferedWriter out = new BufferedWriter(new FileWriter(VFS, true));
            out.write(str+"\n");
            out.close();
        }
        catch (IOException e) {
            System.out.println("EXCEPTION!" + e);
        }
    }
    
    public void DisplayDiskStatus() {
        int allocated = 0 , freeSpace = 0;
        for(int i = 0 ; i < blocks.size() ; i++)
        {
            if(i%10 == 0) {
                System.out.println();

            }
            System.out.print(blocks.get(i) + " ");
            if(blocks.get(i)!=-1)
                allocated++;
            else
                freeSpace++;

        }
        System.out.println("\n\nNumber of allocated BLocks = " + allocated);
        System.out.println("Number of Free BLocks = " + freeSpace);
        System.out.println("Total number of blocks = " + blocks.size()  + "\n");
    }
}
