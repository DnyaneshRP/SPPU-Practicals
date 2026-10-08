/*

    ===================== DAA MINI PROJECT =====================

    TITLE:
    Comparison of Naive String Matching and Rabin-Karp Algorithms


    AIM:
    Implement the Naive String Matching algorithm and Rabin-Karp
    algorithm for string matching and observe the difference in
    their working and execution time for the same input.


    DESCRIPTION:
    String matching is the process of finding all occurrences
    of a given pattern inside a text.

    This project implements two string matching algorithms:

    1. Naive String Matching
       - Compares the pattern with the text at every possible
         starting position.
       - Directly compares characters one by one.

    2. Rabin-Karp Algorithm
       - Uses hashing to compare the pattern with portions
         (windows) of the text.
       - Uses a rolling hash to efficiently calculate the hash
         value of the next window.
       - If the hash values match, characters are compared to
         confirm the actual match and avoid hash collisions.


    ALGORITHM 1: NAIVE STRING MATCHING

    1. Start from the first position of the text.
    2. Compare the pattern with the corresponding characters
       of the text.
    3. If all characters match, store the current position.
    4. If a mismatch occurs, shift the pattern by one position.
    5. Repeat until all possible positions are checked.


    ALGORITHM 2: RABIN-KARP

    1. Calculate the hash value of the pattern.
    2. Calculate the hash value of the first text window having
       the same length as the pattern.
    3. Compare the hash values.
    4. If the hash values are equal, compare the actual characters
       to verify the match.
    5. Calculate the hash value of the next text window using
       the rolling hash technique.
    6. Repeat until all possible windows are checked.


    TIME COMPLEXITY:

    Naive String Matching:
    Best Case     : O(n)
    Average Case  : O(n * m)
    Worst Case    : O(n * m)

    Rabin-Karp:
    Best/Expected : O(n + m)
    Average       : O(n + m)
    Worst Case    : O(n * m)

    where:
    n = length of text
    m = length of pattern


    SPACE COMPLEXITY:

    Naive String Matching : O(1) auxiliary space
    Rabin-Karp            : O(1) auxiliary space


    KEY DIFFERENCE:

    Naive String Matching directly compares characters at every
    possible position.

    Rabin-Karp first compares hash values and performs character
    comparison only when the hash values are equal.


    EXECUTION TIME:
    The program uses System.nanoTime() to measure and compare
    the execution time of both algorithms for the same input.

    Note:
    Execution time may vary between different runs because of
    JVM optimization, system load, and the size of the input.


    ===== SAMPLE INPUT =====

    Enter text: AABAACAADAABAABA
    Enter pattern: AABA


    ===== SAMPLE OUTPUT =====

    ----- Naive String Matching -----
    Pattern found at positions (0-based): [0, 9, 12]
    Execution Time:  ....

    ----- Rabin-Karp String Matching -----
    Pattern found at positions (0-based): [0, 9, 12]
    Execution Time:  ....

    ----- Comparison -----
    Rabin-Karp was faster for this input.


    IMPORTANT:
    Positions are displayed using 0-based indexing.

    For the sample input, the pattern "AABA" occurs at positions
    0, 9 and 12.

    
    =============================================================

*/

import java.util.*;

public class StringMatching {

    // ---------------------------------------------------------
    // Naive String Matching
    // ---------------------------------------------------------
    static ArrayList<Integer> naiveSearch(String text, String pattern) {

        ArrayList<Integer> positions = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        // Compare pattern with every possible position in text
        for (int i = 0; i <= n - m; i++) {

            int j = 0;

            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }

            // If entire pattern matched
            if (j == m) {
                positions.add(i);
            }
        }

        return positions;
    }


    // ---------------------------------------------------------
    // Rabin-Karp String Matching
    // ---------------------------------------------------------
    static ArrayList<Integer> rabinKarp(String text, String pattern) {

        ArrayList<Integer> positions = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        int d = 256;       // Number of possible characters
        int q = 101;       // Prime number used for hashing

        long patternHash = 0;
        long textHash = 0;
        long h = 1;

        // Calculate h = d^(m-1) % q
        for (int i = 0; i < m - 1; i++) {
            h = (h * d) % q;
        }

        // Calculate initial hash values
        for (int i = 0; i < m; i++) {
            patternHash = (d * patternHash + pattern.charAt(i)) % q;
            textHash = (d * textHash + text.charAt(i)) % q;
        }

        // Slide pattern over text
        for (int i = 0; i <= n - m; i++) {

            // If hash values match, compare characters
            // to avoid hash collision
            if (patternHash == textHash) {

                int j = 0;

                while (j < m &&
                       text.charAt(i + j) == pattern.charAt(j)) {
                    j++;
                }

                if (j == m) {
                    positions.add(i);
                }
            }

            // Calculate hash for next window
            if (i < n - m) {

                textHash = (d * (textHash
                        - text.charAt(i) * h)
                        + text.charAt(i + m)) % q;

                // Make hash positive
                if (textHash < 0) {
                    textHash += q;
                }
            }
        }

        return positions;
    }


    // ---------------------------------------------------------
    // Main Method
    // ---------------------------------------------------------
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("\nEnter text: ");
        String text = sc.nextLine();

        System.out.print("\nEnter pattern: ");
        String pattern = sc.nextLine();

        // Check for invalid input
        if (pattern.length() == 0) {
            System.out.println("\nPattern cannot be empty.");
            sc.close();
            return;
        }

        if (pattern.length() > text.length()) {
            System.out.println("\nPattern is longer than text.");
            sc.close();
            return;
        }


        // -----------------------------------------------------
        // Naive String Matching
        // -----------------------------------------------------

        long startTime = System.nanoTime();

        ArrayList<Integer> naivePositions =
                naiveSearch(text, pattern);

        long endTime = System.nanoTime();

        long naiveTime = endTime - startTime;


        // -----------------------------------------------------
        // Rabin-Karp String Matching
        // -----------------------------------------------------

        startTime = System.nanoTime();

        ArrayList<Integer> rabinPositions =
                rabinKarp(text, pattern);

        endTime = System.nanoTime();

        long rabinTime = endTime - startTime;


        // -----------------------------------------------------
        // Display Results
        // -----------------------------------------------------

        System.out.println("\n----- Naive String Matching -----");

        if (naivePositions.isEmpty()) {
            System.out.println("Pattern not found.");
        } else {
            System.out.println("Pattern found at positions (0-based): "
                    + naivePositions);
        }

        System.out.println("Execution Time: "
                + naiveTime + " ns");


        System.out.println("\n----- Rabin-Karp String Matching -----");

        if (rabinPositions.isEmpty()) {
            System.out.println("Pattern not found.");
        } else {
            System.out.println("Pattern found at positions (0-based): "
                    + rabinPositions);
        }

        System.out.println("Execution Time: "
                + rabinTime + " ns");


        // -----------------------------------------------------
        // Comparison
        // -----------------------------------------------------

        System.out.println("\n----- Comparison -----");

        if (naiveTime < rabinTime) {
            System.out.println("Naive String Matching was faster for this input.");
        } else if (rabinTime < naiveTime) {
            System.out.println("Rabin-Karp was faster for this input.");
        } else {
            System.out.println("Both algorithms took approximately the same time.");
        }

        sc.close();
    }
}