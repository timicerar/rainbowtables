package rainbowtable.hash;

import utils.CommonHelper;

import java.util.Iterator;

public class HashTable implements Iterable<Bytes> {

    private final Bytes[] bytesTable;
    private final HashFunction hashFunction;
    private int size;

    public HashTable(int size, int minPwLength, int maxPwLength) {
        int prime = new CommonHelper().calculatePrime(size);
        this.bytesTable = new Bytes[prime];
        this.hashFunction = new HashFunction(prime, minPwLength, maxPwLength);
    }

    public void insert(Bytes key) {
        int index = hashFunction.hash(key);

        if (bytesTable[index] == null) {
            bytesTable[index] = key;
        } else {
            Bytes currentBytes = bytesTable[index];

            if (currentBytes.equals(key)) {
                return;
            }

            while (currentBytes.next != null) {
                if (currentBytes.equals(key)) {
                    return;
                }
                currentBytes = currentBytes.next;
            }

            currentBytes.next = key;
        }

        size++;
    }

    public void insert(Bytes key, Bytes value) {
        key.value = value;
        insert(key);
    }

    public boolean contains(Bytes key) {
        Bytes found = findKey(key);
        return found != null;
    }

    public Bytes search(Bytes key) {
        Bytes found = findKey(key);

        if (found == null) {
            return null;
        }

        return found.value;
    }

    public int size() {
        return size;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[ ");

        for (int i = 0; i < bytesTable.length; i++) {
            if (i + 1 == bytesTable.length) {
                sb.append(bytesTable[i]);
            } else {
                sb.append(bytesTable[i]);
                sb.append(", ");
            }
        }

        sb.append(" ]");

        return sb.toString();
    }

    public Bytes[] getBytes() {
        return bytesTable;
    }

    @Override
    public Iterator<Bytes> iterator() {
        return new HashIterator(bytesTable, size);
    }

    private Bytes findKey(Bytes key) {
        int index = hashFunction.hash(key);
        Bytes otherBytes = bytesTable[index];

        while (otherBytes != null && !key.equals(otherBytes)) {
            if (otherBytes.next != null) {
                otherBytes = otherBytes.next;
            } else {
                break;
            }
        }

        return otherBytes;
    }
}
