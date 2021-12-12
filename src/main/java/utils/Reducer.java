package utils;

import java.util.Random;

public class Reducer {
    private final String charset;
    private final int maxPwLength;
    private final int minPwLength;
    private Random random;

    public Reducer(String charset, int minPwLength, int maxPwLength) {
        this.charset = charset;
        this.maxPwLength = maxPwLength;
        this.minPwLength = minPwLength;
    }

    public byte[] reduce(byte[] hash, int functionNr) {
        byte pwLength = (byte) ((functionNr) % (maxPwLength - minPwLength + 1) + minPwLength);
        return reduce(hash, functionNr, pwLength);
    }

    public byte[] reduce(byte[] hash, int functionNr, byte passwordLength) {
        byte[] result = new byte[passwordLength];

        for (int i = 0; i < result.length; i++) {
            hash[i] = (byte) (hash[i] ^ functionNr);
            result[i] = (byte) (Math.abs(hash[i]) % charset.length());
            hash[i] = (byte) (hash[i] ^ functionNr);
        }

        return result;
    }
}
