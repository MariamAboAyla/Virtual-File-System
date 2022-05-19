import java.util.ArrayList;

public class VFile {
    private String filePath;
    private ArrayList<Integer> allocatedBlocks = new ArrayList<Integer>();
    private boolean deleted;

    private int startIndex = -1;
    private int size;

    public VFile(int s) {
        setSize(s);
    }

    public VFile(String Path , int s) {
        filePath = Path;
        size = s;
    }

    public ArrayList<Integer> getAllocatedBlocks() {
        return allocatedBlocks;
    }

    public void setAllocatedBlocks(ArrayList<Integer> allocatedBlocks) {
        this.allocatedBlocks = allocatedBlocks;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return filePath;
    }
}
