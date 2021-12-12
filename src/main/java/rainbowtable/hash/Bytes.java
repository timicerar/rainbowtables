package rainbowtable.hash;

public class Bytes {
    private final byte[] bytes;
    public Bytes next;
    public Bytes value;

    public Bytes(byte[] bytes, Bytes next) {
        this.bytes = bytes;
        this.next = next;
    }

    public Bytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public int hashCode() {
        int hash = 0;

        for (byte b : bytes) {
            hash += b;
        }

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Bytes other = (Bytes) obj;
        byte[] bytes1 = this.bytes;
        byte[] bytes2 = other.bytes;

        if (bytes.length != bytes2.length) {
            return false;
        }

        for (int i = 0; i < bytes1.length; i++) {
            if (bytes1[i] != bytes2[i]) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();

        for (byte aByte : bytes) {
            ret.append(aByte).append(", ");
        }

        return ret.toString();
    }
}