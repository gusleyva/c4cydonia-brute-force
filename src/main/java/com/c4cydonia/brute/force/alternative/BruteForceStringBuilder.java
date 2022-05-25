/*
 * Copyright (c) 2022 Nextiva, Inc. to Present.
 * All rights reserved.
 */

package com.c4cydonia.brute.force.alternative;

import com.c4cydonia.brute.force.utils.Utils;

/**
 * Brute force algorithm implementation to crack a password that is represented in a String using String Builder.
 * Code references.
 * - https://www.geeksforgeeks.org/print-all-combinations-of-given-length/
 */
public class BruteForceStringBuilder {
    private static int DELTA = 7;
    private static String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    private String TMP_PASSWORD = "zzzzzz";//"ggf";
    private boolean isCracked = false;

    public static void main(String[] args) {
        BruteForceStringBuilder bruteForce = new BruteForceStringBuilder();
        char[] input = ALPHABET.toCharArray();
        bruteForce.orchestrator(DELTA, input);
    }

    private void orchestrator(int delta, char[] input) {
        Utils utils = new Utils();
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        for(int tmpLenPassword = 1; tmpLenPassword <= delta; tmpLenPassword++) {
            iterateAllInputCombinations(input, new StringBuilder(), input.length, tmpLenPassword);
        }
        utils.calculateMemoryUsed(runtime);
        utils.calculateExecutionTime(startTime);
    }

    public void iterateAllInputCombinations(char[] input,
                                              StringBuilder tmpPassword,
                                              int size,
                                              int lenPassword) {
        // Base case: tmpLenPassword is 0, print prefix
        if (lenPassword == 0) {
            isCracked = isCracked(tmpPassword.toString());
            return;
        }

        for (int pos = 0; pos < size && !isCracked; ++pos) {
            tmpPassword.append(input[pos]);
            iterateAllInputCombinations(input, tmpPassword, size, lenPassword - 1);
            tmpPassword.deleteCharAt(tmpPassword.length() - 1);
        }
    }

    protected boolean isCracked (String tmpPassword) {
        if(TMP_PASSWORD.equals(tmpPassword)) {
            System.out.printf("Password: [%s]\n", tmpPassword);
            return true;
        }
        System.out.printf("Password incorrect: [%s]\n", tmpPassword);
        return false;
    }
}
