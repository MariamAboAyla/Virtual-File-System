import java.util.ArrayList;

public interface AllocationMethod
{

	public boolean allocate(VFile file);
	public boolean deAllocate(String filePath);
	public ArrayList<Integer> getBlocks();

}
