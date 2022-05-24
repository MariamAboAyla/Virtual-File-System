import java.util.ArrayList;

public class VFile {
    private String fileName;
    private String filePath;
    private ArrayList<Integer> allocatedBlocks = new ArrayList<Integer>();
    private boolean deleted;
    private int startIndex = -1;
    private int size;

    public VFile(String fileName , int size) {
        this.fileName=fileName;
        this.size = size;
    }

    public String get_FileName() {
        return fileName;
    }

    public void set_AllocatedBlocks(ArrayList<Integer> allocatedBlocks) {
        this.allocatedBlocks = allocatedBlocks;
    }
    public ArrayList<Integer> get_AllocatedBlocks() {
        return allocatedBlocks;
    }

    public void set_Deleted(boolean deleted) {
        this.deleted = deleted;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public int get_Size() {return size;}
    public void set_ٍSize(int size) {this.size = size;}

    public String get_FilePath() {
        return filePath;
    }

    public void set_FilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
