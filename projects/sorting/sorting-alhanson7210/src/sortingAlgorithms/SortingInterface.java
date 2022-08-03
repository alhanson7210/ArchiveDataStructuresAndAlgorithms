package sortingAlgorithms;

/** Interface containing sorting methods of project 2.
 *  Write class SortingAlgorithms that implements this interface. */
interface SortingInterface {

   void insertionSort(Comparable[] array, int lowindex, int highindex, boolean reversed);

   void shakerSort(Comparable[] array, int lowindex, int highindex, boolean reversed);

   void randomizedQuickSort(Comparable[] array, int lowindex, int highindex, boolean reversed);

   void hybridSort(Comparable[] array, int lowindex, int highindex, boolean reversed);

   void externalSort(String inputFile, String outputFile, int n, int k);

   Node mergeSortLL(Node list, boolean reversed);

}
