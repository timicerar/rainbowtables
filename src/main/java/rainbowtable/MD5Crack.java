package rainbowtable;

import rainbowtable.hash.Bytes;
import rainbowtable.hash.HashTable;
import utils.CommonHelper;
import utils.FileHelper;
import utils.Reducer;
import utils.UIHelper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Crack {

    private final String charset;
    private final int minPasswordLength;
    private final int maxPasswordLength;
    private final int chainLength;
    private final UIHelper uiHelper;
    private final CommonHelper commonHelper;
    private final Reducer reducer;
    private final MessageDigest messageDigest;
    private final HashTable hashTable;

    public MD5Crack(String charset, int minPasswordLength, int maxPasswordLength, int chainsPerTable, int chainLength, String filename) throws NoSuchAlgorithmException, IOException {
        this.charset = charset;
        this.minPasswordLength = minPasswordLength;
        this.maxPasswordLength = maxPasswordLength;
        this.chainLength = chainLength;

        this.uiHelper = new UIHelper();
        this.commonHelper = new CommonHelper();
        this.reducer = new Reducer(charset, minPasswordLength, maxPasswordLength);
        this.messageDigest = this.commonHelper.getMD5digester();

        FileHelper fileHelper = new FileHelper();
        BufferedInputStream dis = fileHelper.openFile(filename);
        hashTable = fileHelper.readTable(dis, chainsPerTable, minPasswordLength, maxPasswordLength);
    }

    public boolean crackHash(String hashString) {
        final byte[] hash = commonHelper.hexStringToByteArray(hashString);

        HashTable foundEndpoints = searchEndpoints(hash, hashTable);
        uiHelper.printEndpointCount(foundEndpoints.size());

        return eliminateFalseAlarms(foundEndpoints, hashTable, hash);
    }

    private HashTable searchEndpoints(byte[] hash, HashTable table) {
        HashTable foundEndpoints = new HashTable(table.size() / 11, minPasswordLength, maxPasswordLength);

        byte[] reducedEndpoint = null;

        for (int pwLength = minPasswordLength; pwLength <= maxPasswordLength; pwLength++) {
            for (int i = chainLength - 1; i >= 0; i--) {
                byte[] possibleEndpoint = hash;
                for (int j = i; j < chainLength; j++) {
                    reducedEndpoint = reducer.reduce(possibleEndpoint, j, (byte) pwLength);
                    possibleEndpoint = messageDigest.digest(reducedEndpoint);
                }

                Bytes bytes = new Bytes(reducedEndpoint);

                if (table.contains(bytes)) {
                    foundEndpoints.insert(bytes);
                }

            }
        }
        return foundEndpoints;
    }

    private boolean eliminateFalseAlarms(HashTable foundEndpoints, HashTable table, byte[] hash) {
        for (Bytes endpoint : foundEndpoints) {
            Bytes currentPlaintext = table.search(endpoint);
            byte pwLength = (byte) endpoint.getBytes().length;
            byte[] currentHash;

            for (int i = 0; i < chainLength; i++) {
                currentHash = messageDigest.digest(commonHelper.bytesToString(currentPlaintext.getBytes(), charset).getBytes());

                if (commonHelper.equalBytes(hash, currentHash)) {
                    uiHelper.hashCracked(commonHelper.bytesToString(currentPlaintext.getBytes(), charset));
                    return true;
                }

                currentPlaintext = new Bytes(reducer.reduce(currentHash, i, pwLength));
            }
        }

        uiHelper.crackFailed();

        return false;
    }
}
