package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonHelper {

    public String bytesToString(byte[] bytes, String charset) {
        StringBuilder result = new StringBuilder();

        for (byte aByte : bytes) {
            result.append(charset.charAt(aByte % charset.length()));
        }

        return result.toString();
    }

    public boolean equalBytes(byte[] byteArray1, byte[] byteArray2) {
        if (byteArray1.length != byteArray2.length) {
            return false;
        }

        for (int i = 0; i < byteArray1.length; i++) {
            if (byteArray1[i] != byteArray2[i]) {
                return false;
            }
        }

        return true;
    }

    public byte calculatePasswordLength(int chainNumber, int minPasswordLength, int maxPasswordLength) {
        return (byte) (chainNumber % (maxPasswordLength - minPasswordLength + 1) + minPasswordLength);
    }

    public MessageDigest getMD5digester() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD5");
    }

    public long calculateKeyspace(String charset, int minPasswordLength, int maxPasswordLength) {
        long keyspace = 0;

        for (int i = minPasswordLength; i <= maxPasswordLength; i++) {
            keyspace += (long) Math.pow(charset.length(), i);
        }

        return keyspace;
    }

    public int[] calculateKeyspaceRatios(String charset, int minPasswordLength, int maxPasswordLength, int chainsPerTable) {
        long total = calculateKeyspace(charset, minPasswordLength, maxPasswordLength);
        int[] ratios = new int[maxPasswordLength - minPasswordLength + 1];

        for (int i = 0; i <= maxPasswordLength - minPasswordLength; i++) {
            ratios[i] = (int) (chainsPerTable / (total / calculateKeyspace(charset, i, i)));
        }

        return ratios;
    }

    public int calculatePrime(int size) {
        int i = size / 2;

        while (true) {
            boolean isPrime = true;

            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime) {
                return i;
            }

            i++;
        }
    }

    public byte[] hexStringToByteArray(String s) {
        int length = s.length();
        byte[] data = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }
}