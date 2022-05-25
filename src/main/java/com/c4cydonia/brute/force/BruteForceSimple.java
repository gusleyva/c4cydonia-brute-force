/*
 * Copyright (c) 2022 Nextiva, Inc. to Present.
 * All rights reserved.
 */

package com.c4cydonia.brute.force;

import com.c4cydonia.brute.force.utils.Utils;

/**
 * Brute force algorithm implementation to crack a password that is represented in a String.
 * Code references
 * - https://www.geeksforgeeks.org/print-all-combinations-of-given-length/
 * - https://www.vogella.com/tutorials/JavaPerformance/article.html
 */
public class BruteForceSimple {
    private static int DELTA = 7;
    private static String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    private String TMP_PASSWORD = "zzzzzz";//"ggf";
    private boolean isCracked = false;

    public static void main(String[] args) {
        BruteForceSimple bruteForce = new BruteForceSimple();
        char[] input = ALPHABET.toCharArray();
        bruteForce.orchestrator(DELTA, input);
    }

    /**
     * Iterates from 1 character to DELTA:
     * - a, b, c... x, y, z.
     * - aa, ab, ac, ba, bb, bc, ca... zy, zz.
     * - aaa, aab, aac, aba, abb, abc, aca, acb, acc... zzy, zzz.
     * Additionally, there is code to calculate the memory and execution time.
     * @param delta maximum password length.
     * @param input charset to generate our passwords.
     * - lower case, upper case, digits, symbols.
     */
    public void orchestrator(int delta, char[] input) {
        Utils utils = new Utils();
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        for(int tmpLenPassword = 1; tmpLenPassword <= delta; tmpLenPassword++) {
            iterateAllInputCombinations(input, "", input.length, tmpLenPassword);
        }
        utils.calculateMemoryUsed(runtime);
        utils.calculateExecutionTime(startTime);
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
            isCracked = isCracked(prefix);
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

    private boolean isCracked (String tmpPassword) {
        if(TMP_PASSWORD.equals(tmpPassword)) {
            System.out.printf("Password: [%s]\n", tmpPassword);
            return true;
        }
        System.out.printf("Password incorrect: [%s]\n", tmpPassword);
        return false;
    }
}
