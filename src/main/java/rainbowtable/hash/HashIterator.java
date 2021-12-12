package rainbowtable.hash;

import java.util.Iterator;

public class HashIterator implements Iterator<Bytes> {

    private int position;
    private final int size;
    private int iterations;
    private Bytes currentBytes;
    private final Bytes[] bytesTable;

    public HashIterator(Bytes[] bytesTable, int size) {
        this.bytesTable = bytesTable;
        this.size = size;
        this.position = -1;
    }

    @Override
    public boolean hasNext() {
        return iterations < size;
    }

    @Override
    public Bytes next() {
        if (currentBytes == null || currentBytes.next == null) {
            while (bytesTable[++position] == null) ;
            currentBytes = bytesTable[position];
        } else {
            currentBytes = currentBytes.next;
        }

        iterations++;

        return currentBytes;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
