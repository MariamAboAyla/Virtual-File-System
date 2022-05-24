import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class IndexedAllocation implements AllocationManager{
    private final ArrayList<Integer> blocks = new ArrayList<>(); // the blocks that are allocated to this file
    private final HashMap<Integer, ArrayList<Integer>> IndexBlocks = new HashMap<>(); // <block, [indexes]>
    private final ArrayList<BlockDetails> StoredFiles  = new ArrayList<>(); // this is the list of files that are stored in the disk
    private final HashMap<String , Integer> StoredFilesToIndexFiles = new HashMap<>(); // <fileName, indexFile>

    public final String VFS = "VFIndexed.vfs";

    IndexedAllocation(int size){
        for(int i = 0 ;i < size ; i++) {
            blocks.add(-1); // -1 means empty block
        }
    }
    @Override
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
            StoredFilesToIndexFiles.put(file.get_FileName() ,startIndex ); // add file to list of files
            file.set_AllocatedBlocks(blocksForFile); // set blocks for file
            IndexBlocks.put(startIndex, blocksForFile); // add blocks for file to index

        }
        else {

            System.out.println("No enough space for file, please delete some files");
            return -1; // no free block
        }
        return startIndex; // return start index
    }
    @Override
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

    public void LoadHardDisk(VDirectory root) {
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
                        root.addNewDirectory(String.valueOf(new VDirectory(filesFromSize[0]))); // add directory to root
                    }
                }
                else {
                    VDirectory cur = root;
                    StringBuilder CurPath = new StringBuilder(root.get_DirectoryName() + '/'); // create path for directory
                    for(int i = 1 ; i < files.length - 1 ; i++) { // find directory in root
                        cur = cur.getSubDirectory(CurPath + files[i]); // get directory
                        CurPath.append(files[i]).append("/"); // add directory to path
                    }
                    if(files[files.length - 1].contains(".")) { // if file
                        cur.addFile(new VFile(filesFromSize[0], sizes.length)); // add file to directory
                        allocateFileFromDisk(sizes, filesFromSize[0]); // allocate file from disk
                    }
                    else {
                        cur.addSubDirectory(new VDirectory(filesFromSize[0])); // add directory to directory class
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        for (BlockDetails storedFile : StoredFiles) {
            StoredFilesToIndexFiles.put(storedFile.path, storedFile.indexBlock); // add file to list of files
        }

    }
    void allocateFileFromDisk(String []arr , String name) {
        int startIndex = Integer.parseInt(arr[0]); // get start index
        ArrayList<Integer> blocksForFile= new ArrayList<>(); // create array of blocks for file

        blocks.set(startIndex, 0);
        for(int i = 1 ; i < arr.length ; i++) {
            blocksForFile.add(Integer.parseInt(arr[i])); // add block to array of blocks for file
            blocks.set(Integer.parseInt(arr[i]), 1); // set block to used
        }
        StoredFiles.add(new BlockDetails(name , startIndex)); // add file to list of files
        IndexBlocks.put(startIndex, blocksForFile); // add blocks for file to index

    }

    
    public void SaveHardDisk(VDirectory root) {
        if(root.get_DirectoryName().equals("root")){     // if root
            clearFile(); // clear file
        }
        else
            appendOnFile(root.get_DirectoryName() + " | -1"); // add directory to file

        ArrayList<VDirectory> rootDirectories = root.get_SubDirectories(); // get sub directories

        saveFiles(root.get_Files()); // save files
        for(int i = 0 ; i < root.get_SubDirectories().size();i++)
            SaveHardDisk(rootDirectories.get(i)); // save sub directories

    }
    void saveFiles(ArrayList<VFile> files){
        for(int i = 0 ;i < files.size();i++){ // for each file
            saveFile(files.get(i).get_FileName()); // save file
        }
    }
    void saveFile(String path){
        Integer indexFile =  StoredFilesToIndexFiles.get(path); // get index of file
        path = path  + " | "  + indexFile + " "; // add index to path
        ArrayList<Integer> allocatedBlocks = IndexBlocks.get(indexFile); // get blocks for file
        StringBuilder pathBuilder = new StringBuilder(path); // create path for file
        for (Integer allocatedBlock : allocatedBlocks) {
            pathBuilder.append(allocatedBlock).append(" "); // add block to path
        }
        path = pathBuilder.toString();
        appendOnFile(path); // append path to file
        path = "";
    }
    void clearFile() {
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(VFS, false)); // clear file
            out.close(); // close file
        }
        catch (IOException e) {
            System.out.println("EXCEPTION!" + e);
        }
    }
    void appendOnFile(String str ){
        try{

            BufferedWriter out = new BufferedWriter(new FileWriter(VFS, true)); // append to file
            out.write(str+"\n"); // write to file
            out.close(); // close file
        }
        catch (IOException e) {
            System.out.println("EXCEPTION!" + e);
        }
    }
    @Override
    public void displayDiskStatus() {
        int allocated = 0 , freeSpace = 0;
        for(int i = 0 ; i < blocks.size() ; i++)
        {
            if(i%10 == 0) { // if new line
                System.out.println(); // new line
            }
            System.out.print(blocks.get(i) + " "); // print block
            if(blocks.get(i)!=-1) // if block is allocated
                allocated++; // increase allocated blocks
            else
                freeSpace++; // increase free space

        }
        System.out.println("\n\nNumber of allocated BLocks = " + allocated);
        System.out.println("Number of Free BLocks = " + freeSpace);
        System.out.println("Total number of blocks = " + blocks.size()  + "\n");
    }

    public void saveStatues(String path) {
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(VFS, true)); // clear file
            out.write(path); // write to file
            String [] arr= path.split("/");
            String name = arr[arr.length-1];
            int ind=StoredFilesToIndexFiles.get(name);

            out.write(" "+(ind+1)+"\n");
            out.write((ind+1)+" ");
            ArrayList<Integer> blocksForFile= IndexBlocks.get(ind);
            for(int i = 0 ; i < blocksForFile.size() ; i++)
            {
                out.write(blocksForFile.get(i)+" ");
            }
            out.write("\n");
            out.close(); // close file
        }
        catch (IOException e) {
            System.out.println("EXCEPTION!" + e);
        }


    }
    public VDirectory loadStatues(){
        VDirectory root = new VDirectory("root");
        VFile file;
        try{
            BufferedReader in = new BufferedReader(new FileReader(VFS)); // clear file
            String line,line2;
            while((line = in.readLine()) != null) {
               line2=in.readLine();
               String [] arr= line.split(" ");
                String [] name= arr[0].split("/");
                String [] blocks= line2.split(" ");
                String name2=name[name.length-1];
                int ind=Integer.parseInt(arr[1]);
                String path=name[0];
                for(int i=1;i<name.length-1;i++)
                {
                    path=path+"/"+name[i];
                    root.addNewDirectory(path);
                }
                path=path+"/"+name[name.length-1];
                int counter=blocks.length-1;
                root.addNewFile(path,counter);

//                ArrayList<Integer> blocksForFile= new ArrayList<>();
//                for(int i=1;i<blocks.length;i++)
//                {
//                    blocksForFile.add(Integer.parseInt(blocks[i]));
//                }
//                StoredFilesToIndexFiles.put(name2,ind-1);
//                IndexBlocks.put(ind-1,blocksForFile);
                file=root.checkFilePath(path);
                allocate(file);


            }
            in.close(); // close file
        }
        catch (IOException e) {
            System.out.println("EXCEPTION!" + e);
        }
        return root;
    }


}
