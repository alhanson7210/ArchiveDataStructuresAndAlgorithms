package sortingAlgorithms;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/** Test file for external sort.
 * You are responsible for thoroughly testing external sort on your own.
 * This tester provides only the most basic testing. */
public class ExternalSortTester {
    public final static String INPUT_FILE = "inputFile";
    public final static String SORTED_FILE = "sortedFile";

    /** Performs basic testing for external sort. */
    @Test
    public void testExternalSort() {
            SortingInterface listSorter = new SortingAlgorithms();
            try {
                Files.deleteIfExists(Paths.get(INPUT_FILE));
                Files.deleteIfExists(Paths.get(SORTED_FILE));
                for (int i = 0; i < 100; i++) {
                    Files.deleteIfExists(Paths.get("temp" + i + ".txt"));
                    Files.deleteIfExists(Paths.get("tmp" + i + ".txt"));
                }

            } catch (IOException e1) {
                System.out.println("inputFile did not exist, no need to remove");
            }
            generateLargeFile(INPUT_FILE, 1000000);
            listSorter.externalSort(INPUT_FILE, SORTED_FILE, 10000, 100);
            try (BufferedReader br = new BufferedReader(new FileReader(SORTED_FILE))) {
                String line;
                int num = 0;
                int lineNum = 0;
                while ((line = br.readLine()) != null) {
                    int nextNum = Integer.parseInt(line);
                    lineNum++;
                    if ((nextNum < num) || (nextNum == Integer.MAX_VALUE)) {
                        Assert.fail("External file is not sorted properly. See line # " + lineNum);
                    }
                    num = nextNum;
                }
                // Delete temp files
                for (int i = 0; i < 100; i++) {
                    Files.deleteIfExists(Paths.get("temp" + i + ".txt"));
                    Files.deleteIfExists(Paths.get("tmp" + i + ".txt"));
                }

            } catch (FileNotFoundException e) {
                System.out.println("Could not open file: " + e);
                Assert.fail();
            } catch (IOException e) {
                System.out.println("Could not read from file: " + e);
                Assert.fail();
            }
    }


    /** Used for testing external sort. Generates a large file of ints.
     *
     * @param filename name of the file
     * @param n number of elements in the file (one per line)
     */
    public static void generateLargeFile(String filename, int n) {
            Random rand = new Random();
            try (PrintWriter pw = new PrintWriter(filename)) {
                for (int i = 0; i < n; i++)
                    pw.println(rand.nextInt(1000) + rand.nextInt(100));
            } catch (IOException e) {
                System.out.println("Error writing to a file " + filename);
            }

        }


}
