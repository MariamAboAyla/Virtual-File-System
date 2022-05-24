import java.util.ArrayList;

public class Directory {
    private String directoryPath;

    private ArrayList<VFile> files = new ArrayList<VFile>();

    private ArrayList<Directory> subDirectories = new ArrayList<Directory>();

    private boolean deleted = false;

    /*	this method prints the directory name and its files
    then makes recursion to loop on the subDirectories to print their structure too.
    The level parameter can be used to print spaces before the directory name is
     printed to show its level in the structure
     */
    public void printDirectoryStructure(int level)
    {
        System.out.println(directoryPath);
        printDirectory(level+1,this);
    }

    private void printDirectory(int level , Directory Dir)
    {
        printFiles(Dir.files , level);
        ArrayList<Directory> Directories = Dir.getSubDirectories();
        for(int i = 0 ; i < Directories.size() ; i++)
        {
            Directory temp = Directories.get(i);
            for(int j = 0 ; j < level*2 ;j++) {
                System.out.print("   ");
            }
            System.out.println(temp.getDirectoryPath());
            level++;
            printDirectory(level , temp);
            level--;
        }
    }


    private void printFiles(ArrayList<VFile> files, int level)
    {
        for(int i = 0 ; i < files.size() ; i++)
        {
            for(int j = 0 ; j < level*2 ;j++) {
                System.out.print("   ");
            }
            System.out.println(files.get(i).get_FilePath());
        }
    }

    public Directory(){}

    public Directory(String name ){
        directoryPath =  name;
    }
    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public ArrayList<VFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<VFile> files) {
        this.files = files;
    }

    public ArrayList<Directory> getSubDirectories() {
        return subDirectories;
    }

    public void setSubDirectories(ArrayList<Directory> subDirectories) {
        this.subDirectories = subDirectories;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void addDirectory(Directory D)
    {
        subDirectories.add(D);
    }

    public void addFile(VFile D)
    {
        files.add(D);
    }

    public Directory getSubDirectory(String DirectoryName) {
        for (Directory subDirectory : subDirectories) {
            if (subDirectory.directoryPath.equals(DirectoryName))
                return subDirectory;
        }
        System.out.println("No such Directory");
        return null;
    }

    @Override
    public String toString() {
        return directoryPath;
    }
}
