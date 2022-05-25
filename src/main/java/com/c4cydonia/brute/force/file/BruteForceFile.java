/*
 * Copyright (c) 2022 Nextiva, Inc. to Present.
 * All rights reserved.
 */

package com.c4cydonia.brute.force.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;

/**
 * Brute force algorithm implementation to crack Excel file with password.
 */
public class BruteForceFile {
    private final static int DELTA = 4;
    // abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
    private final static String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private final static String FILE_NAME = "hola.xls";
    private final static String DUMMY_PASS = "zzt"; // "zzt" This password can be used for initial testing
    private final static boolean IS_XLSX = false;

    private static NPOIFSFileSystem FILE_SYSTEM;
    private static Decryptor DECRYPTOR;

    private boolean isCracked = false;

    public BruteForceFile() throws IOException {
        FILE_SYSTEM = new NPOIFSFileSystem(getResourceFile(FILE_NAME), true);
        // DECRYPTOR = loadDecryptor(FILE_SYSTEM);
    }

    public static void main(String[] args) throws IOException {
        BruteForceFile bruteForceFile = new BruteForceFile();
        // bruteForceFile.readXlsFile(DUMMY_PASS);
        char[] input = ALPHABET.toCharArray();
        bruteForceFile.orchestrator(DELTA, input);
    }

    /**
     * Iterates from 1 character to DELTA:
     * - a, b, c... x, y, z.
     * - aa, ab, ac, ba, bb, bc, ca... zy, zz.
     * - aaa, aab, aac, aba, abb, abc, aca, acb, acc... zzy, zzz.
     * @param delta maximum password length.
     * @param input charset to generate our passwords.
     * - Lower case, upper case, digits, symbols.
     * - For this example we are using only lower case.
     */
    private void orchestrator(int delta, char[] input) {
        for(int tmpLenPassword = 1; tmpLenPassword <= delta; tmpLenPassword++) {
            iterateAllInputCombinations(input, "", input.length, tmpLenPassword);
        }
    }

    /**
     * The main recursive method to print all possible strings of temporal length password.
     * @param input characters from the input string.
     * @param prefix string combination.
     * @param size input size.
     * @param tmpLenPassword length of the string
     */
    public void iterateAllInputCombinations(char[] input,
                                            String prefix,
                                            int size,
                                            int tmpLenPassword) {
        // Base case: tmpLenPassword is 0, print prefix
        if (tmpLenPassword == 0) {
            isCracked = bruteForceFile(IS_XLSX, prefix);
            // System.out.println(prefix);
            return;
        }

        // One by one add all characters, from input and recursively call for tmpLenPassword equals to size -1
        for (int i = 0; i  < size && !isCracked; ++i) {
            // Next character of input added
            String newPrefix = prefix + input[i];
            // tmpLenPassword is decreased, because we have added a new character
            iterateAllInputCombinations(input, newPrefix, size, tmpLenPassword - 1);
        }
    }

    private boolean bruteForceFile(boolean isXlsx,  String tmpPassword) {
        if (isXlsx) {
            return readXlsxFile(tmpPassword, DECRYPTOR);
        }
        return readXlsFile(tmpPassword);
    }

    /**
     * Read from Excel files (xls) before 2006.
     * @param tmpPassword generated string to open the file.
     * @return boolean to define if password was correct or not.
     */
    private boolean readXlsFile(String tmpPassword) {
        try {
            Biff8EncryptionKey.setCurrentUserPassword(tmpPassword);
            new HSSFWorkbook(FILE_SYSTEM);
            System.out.printf("Password: [%s]\n", tmpPassword);
            return true;
        } catch (Exception e) {
            System.out.printf("Exception opening the file, password incorrect: [%s]\n", tmpPassword);
        }
        return false;
    }

    /**
     * Read from Excel files (xlsx) after 2006.
     * @param tmpPassword generated string to open the file.
     * @param decryptor to verify the password.
     * @return boolean to define if password was correct or not.
     */
    private boolean readXlsxFile(String tmpPassword, Decryptor decryptor) {
        try {
            if (!decryptor.verifyPassword(tmpPassword)) {
                System.out.println("Unable to process: document is encrypted.");
            } else {
                System.out.println("Password: " + tmpPassword);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Decryptor loadDecryptor(NPOIFSFileSystem fileSystem) throws IOException {
        EncryptionInfo info = new EncryptionInfo(fileSystem);
        return Decryptor.getInstance(info);
    }

    private File getResourceFile(final String fileName) {
        URL url = this.getClass()
                .getClassLoader()
                .getResource(fileName);

        if(url == null) {
            throw new IllegalArgumentException(fileName + " is not found 1");
        }

        return new File(url.getFile());
    }
}
