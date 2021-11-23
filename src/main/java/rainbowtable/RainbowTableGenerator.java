package rainbowtable;

import java.util.Random;

public class RainbowTableGenerator {
    private int charset;
    private int minPasswordLength;
    private int maxPasswordLength;
    private int chainsPerTable;
    private int chainLength;

    private Random random;

    private static final int ALPHA_NUMERIC_LOWER_CASE = 0;
    private static final int ALPHA_NUMERIC_LOWER_UPPER_CASE = 1;
    private static final int ALPHA_LOWER_CASE = 2;
    private static final int ALPHA_LOWER_UPPER_CASE = 3;

    public RainbowTableGenerator(int charsetMode, int minPasswordLength, int maxPasswordLength, int chainsPerTable, int chainLength) {
        if (charsetMode < 0 || charsetMode > 3) {
            throw new IllegalArgumentException("Invalid charset mode!");
        }

        this.charset = charsetMode;
        this.minPasswordLength = minPasswordLength;
        this.maxPasswordLength = maxPasswordLength;
        this.chainsPerTable = chainsPerTable;
        this.chainLength = chainLength;

        this.random = new Random(System.currentTimeMillis());
    }

    public void generateTable() {

    }
}
