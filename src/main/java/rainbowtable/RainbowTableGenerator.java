package rainbowtable;

import utils.CommonHelper;
import utils.FileHelper;
import utils.Reducer;
import utils.UIHelper;

import java.io.BufferedOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class RainbowTableGenerator {
    private final String charset;
    private final int minPasswordLength;
    private final int maxPasswordLength;
    private final int chainsPerTable;
    private final int chainLength;

    private final Random random;
    private final Reducer reducer;
    private final CommonHelper commonHelper;
    private final UIHelper uiHelper;
    private final MessageDigest messageDigest;
    private final FileHelper fileHelper;

    public RainbowTableGenerator(String charset, int minPasswordLength, int maxPasswordLength, int chainsPerTable, int chainLength) throws NoSuchAlgorithmException {
        this.charset = charset;
        this.minPasswordLength = minPasswordLength;
        this.maxPasswordLength = maxPasswordLength;
        this.chainsPerTable = chainsPerTable;
        this.chainLength = chainLength;

        this.random = new Random(System.currentTimeMillis());
        this.reducer = new Reducer(charset, minPasswordLength, maxPasswordLength);
        this.fileHelper = new FileHelper();
        this.commonHelper = new CommonHelper();
        this.uiHelper = new UIHelper();
        this.messageDigest = this.commonHelper.getMD5digester();
    }

    public boolean generateTable() {
        BufferedOutputStream dos = fileHelper.createTableFile(this.charset.length(), this.minPasswordLength, this.maxPasswordLength, this.chainsPerTable, this.chainLength);
        uiHelper.printTableGenerationStartStats();

        int keyspaceID = 0;
        int[] keyspaceRatio = commonHelper.calculateKeyspaceRatios(this.charset, this.minPasswordLength, this.maxPasswordLength, this.chainsPerTable);

        for (int i = 0; i < chainsPerTable; i++) {
            byte pwLength = (byte) (keyspaceID + this.minPasswordLength);

            byte[] startingPoint = createRandomStartingPoint(random, pwLength);

            byte[] endpoint = calculateChain(startingPoint, pwLength);

            if (i > keyspaceRatio[keyspaceID] && keyspaceID < keyspaceRatio.length - 1) {
                keyspaceID++;
            }

            fileHelper.writeToFile(dos, startingPoint, endpoint);

            if (i != 0 && i % (chainsPerTable / 20) == 0) {
                uiHelper.printTableGenerationProgress(i, chainsPerTable);
            }
        }

        uiHelper.printTableGenerationProgress(chainsPerTable, chainsPerTable);

        fileHelper.closeFile(dos);

        return true;
    }

    private byte[] createRandomStartingPoint(Random random, byte pwLength) {
        byte[] startingPoint = new byte[pwLength];
        random.nextBytes(startingPoint);

        for (int a = 0; a < startingPoint.length; a++) {
            startingPoint[a] = (byte) (Math.abs(startingPoint[a] % charset.length()));
        }

        return startingPoint;
    }

    private byte[] calculateChain(byte[] currentEndpoint, byte pwLength) {
        int j;
        byte[] hash;

        for (j = 0; j < chainLength; j++) {
            hash = this.messageDigest.digest(currentEndpoint);
            currentEndpoint = this.reducer.reduce(hash, j, pwLength);
        }

        return currentEndpoint;
    }
}
