
public interface AllocationMethod {
	public boolean allocate(int Size);
	public void deallocate();
	public int [] getBlocks();

}
