package rainbowtable.hash;

public class HashFunction {

    private final int firstBytes;
    private final int tableSize;

    public HashFunction(int tableSize, int minPwLength, int maxPwLength) {
        firstBytes = minPwLength + (maxPwLength - minPwLength) / 2;
        this.tableSize = tableSize;
    }

    public int hash(Bytes bytes2) {
        int hash = 0;

        byte[] bytes = bytes2.getBytes();

        for (int i = 1; i < firstBytes; i++) {
            hash += Math.pow(bytes[i - 1], i);
        }

        return hash % tableSize;
    }
}
