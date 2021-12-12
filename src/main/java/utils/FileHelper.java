package utils;

import rainbowtable.hash.Bytes;
import rainbowtable.hash.HashTable;

import java.io.*;

public class FileHelper {

    private final UIHelper uihelper = new UIHelper();

    public BufferedOutputStream createTableFile(int charsetLength, int minPasswordLength, int maxPasswordLength, int chainsPerTable, int chainLength) {
        File file = new File(charsetLength + "-" + minPasswordLength + "-" + maxPasswordLength + "-" + chainsPerTable + "-" + chainLength + ".tbl");
        BufferedOutputStream dos;

        try {
            dos = new BufferedOutputStream(new FileOutputStream(file));
        } catch (Exception e) {
            uihelper.writeError();
            return null;
        }

        return dos;
    }

    public void writeToFile(BufferedOutputStream dos, byte[] startingPoint, byte[] endpoint) {
        try {
            dos.write(startingPoint);
            dos.write(endpoint);
        } catch (Exception e) {
            uihelper.writeError();
        }
    }

    public void closeFile(BufferedOutputStream dos) {
        try {
            dos.close();
        } catch (Exception e) {
            uihelper.closeError();
        }
    }

    public BufferedInputStream openFile(String filename) {
        File file = new File(filename);
        BufferedInputStream dis;
        try {
            dis = new BufferedInputStream(new FileInputStream(file));
        } catch (Exception e) {
            uihelper.readError();
            return null;
        }

        return dis;
    }

    public HashTable readTable(BufferedInputStream dis, int chainsPerTable, int minPasswordLength, int maxPasswordLength) throws IOException {
        HashTable table = new HashTable(chainsPerTable / 5, minPasswordLength, maxPasswordLength);
        int i = 0;

        int availableIterations = dis.available();
        uihelper.startFileRead();

        while (true) {
            int pwLength = i % (maxPasswordLength - minPasswordLength + 1) + minPasswordLength;
            byte[] startingPoint = new byte[pwLength];
            byte[] endpoint = new byte[pwLength];

            try {
                availableIterations -= (pwLength * 2);

                if (availableIterations < 50) {
                    if (dis.available() < startingPoint.length + endpoint.length) {
                        break;
                    }
                }


                dis.read(startingPoint);
                dis.read(endpoint);
                table.insert(new Bytes(endpoint), new Bytes(startingPoint));
                i++;
            } catch (EOFException e) {
                uihelper.endFileRead(i);
                return table;
            } catch (Exception e) {
                uihelper.readError();
                return null;
            }
        }

        uihelper.endFileRead(i);

        return table;
    }
}