public class IndexedAllocation implements AllocationMethod {
    @Override
    public boolean allocate(int Size) {
        return false;
    }

    @Override
    public void deallocate() {

    }

    @Override
    public int[] getBlocks() {
        return new int[0];
    }
}
