package sortingAlgorithms;

import java.util.Random;
import java.io.*;

public class SortingAlgorithms implements SortingInterface {

	public void insertionSort(Comparable[] array, int lowindex, int highindex, boolean reversed) {
        int i, j;
        Comparable cur;
        for (i = lowindex + 1; i <= highindex; i++) {
            cur = array[i];
            for (j = i - 1; j >= lowindex; j--) {
                if ((reversed & cur.compareTo(array[j]) > 0)  || (!reversed & cur.compareTo(array[j]) <= 0)) {
                    array[j + 1] = cur;
                    break;
                }

                else {
                    array[j + 1] = array[j];
                    array[j] = cur;
                }
            }
        }

    }

	public void shakerSort(Comparable[] array, int lowindex, int highindex, boolean reversed) {
        for(int position = lowindex; position <= highindex; position++){
            Comparable temp;
            for(int k = position; k < highindex; k++) {
                if((reversed & array[k].compareTo(array[k+1]) < 0) || (!reversed & array[k].compareTo(array[k+1]) > 0)){
                    temp = array[k+1];
                    array[k+1] = array[k];
                    array[k] = temp;
                }
            }
            //largest should be at the end already
            highindex--;
            //therefore, the range starts from the assumed second to largest and stops at the number before the position -since it is checked
            for(int j = highindex; j > position; j--) {
                if((reversed & array[j].compareTo(array[j-1]) > 0) || (!reversed & array[j].compareTo(array[j-1]) < 0)) {
                    temp = array[j-1];
                    array[j-1] = array[j];
                    array[j] = temp;
                }
            }
        }
	}

    private int partition(Comparable[] array, int lowindex, int highindex, boolean reversed) {
	    //initialization
        if (highindex <= lowindex+1) return lowindex;
        int i = lowindex, j = highindex - 1;
        Comparable temp, pivot;
        Random randInt = new Random();
        //random pivot index
        int pivotIdx = randInt.nextInt(highindex+1);
        while (pivotIdx < lowindex)
            pivotIdx = randInt.nextInt(highindex+1);
        //swapping elements at high index and the random pivot index
        temp = array[highindex];
        array[highindex] = array[pivotIdx];
        array[pivotIdx] = temp;
        pivot = array[highindex];

        while(i <= j) {
            while((!reversed & array[i].compareTo(pivot) < 0) || (reversed & array[i].compareTo(pivot) > 0))
                i++;

            while((!reversed & array[j].compareTo(pivot) > 0 & j> 0) || (reversed & array[j].compareTo(pivot) < 0 & j> 0))
                j--;

            if (i > j | i == j)
                break;
            else{
                temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        temp = array[i];
        array[i] = pivot;
        array[highindex] = temp;
        return i;
    }

	public void randomizedQuickSort(Comparable[]   array, int lowindex, int highindex, boolean reversed) {
	    if(lowindex <= highindex) {
	        int separation = partition(array,lowindex,highindex,reversed);
	        randomizedQuickSort(array,lowindex,separation-1,reversed);
	        randomizedQuickSort(array,separation+1,highindex,reversed);
        }
    }

	public void hybridSort(Comparable[] array, int lowindex, int highindex, boolean reversed) {
        if(Math.floor(array.length/.25) <= highindex-lowindex)
            insertionSort(array,0,array.length-1,reversed);
        else
            randomizedQuickSort(array,lowindex,highindex,reversed);
//        This commented code also in the non-interface hybrid sort was slower so it was replaced by this current code
//        if(Math.floor(array.length/.1) <= highindex-lowindex) {
//            insertionSort(array,lowindex,highindex,reversed);
//        }
//        else {
//            randomizedQuickSort(array,lowindex,highindex,reversed);
//        }
	}

    public void hybridsort(Comparable[] array, int lowindex, int highindex, boolean reversed) {
        if(Math.floor(array.length/.1) <= highindex-lowindex)
            insertionSort(array,lowindex,highindex,reversed);

        else
            randomizedQuickSort(array,lowindex,highindex,reversed);

    }

	public void externalSort(String inputFile, String outputFile, int n, int k) {
        int numberOfChunks = (int) Math.ceil((double)(n/k));
        try {
            //initializing the readers
            BufferedReader inputreader = new BufferedReader(new FileReader(inputFile));
            PrintWriter outputreader = new PrintWriter(outputFile);
            BufferedReader[] tempreader = new BufferedReader[numberOfChunks];
            //sorting the chunks into temp files
            String integer = inputreader.readLine();
            for(int i = 0; i < numberOfChunks; i++){
                int count = 0;
                Comparable[] temp = new Comparable[k];
                while (integer != null & count < k) {
                    temp[count] = Integer.parseInt(integer);
                    count++;
                    integer = inputreader.readLine();
                }
                randomizedQuickSort(temp,0,temp.length-1,false);
                String file = "temp"+i+1+".txt";
                PrintWriter tempfile = new PrintWriter(file);
                for (Comparable aTemp : temp) {
                    tempfile.println(aTemp);
                }
                tempreader[i] = new BufferedReader(new FileReader(file));
            }
            //initial values into array
            int[] tempFileValueStore = new int[numberOfChunks];
            for(int i = 0; i < tempreader.length; i++){
                String val = tempreader[i].readLine();
                if (val != null)
                    tempFileValueStore[i] = Integer.parseInt(val);
                else
                    tempFileValueStore[i] = -1;
            }
            //merging values #note -1 is target value to know that a given temp file is closed
            for (int v = 0; v < n; v++){
                int minIndex = 0;
                //initialized val to 0 instead of 1 because if there is only one temp file, the code can break here only if val starts at 1
                for(int val = 0; val < tempFileValueStore.length; val++){
                    if(tempFileValueStore[val] < tempFileValueStore[minIndex] & tempFileValueStore[minIndex] != -1 & tempFileValueStore[val] != -1)
                        minIndex = val;

                    else if (tempFileValueStore[minIndex] == -1 & tempFileValueStore[val] != -1)
                        minIndex = val;
                }
                //if statement below might be unnecessary
                if(tempFileValueStore[minIndex] != -1) {
                    outputreader.println(tempFileValueStore[minIndex]);
                    String nextVal = tempreader[minIndex].readLine();
                    if(nextVal != null) {
                        tempFileValueStore[minIndex] = Integer.parseInt(nextVal);
                    } else {
                        tempreader[minIndex].close();
                        tempFileValueStore[minIndex] = -1;
                    }
                }
            }
        } catch (IOException | NumberFormatException io) {
            io.printStackTrace();
        }
    }

    private Node middleNode(Node list) {
	    if (list == null) return null;

	    Node slow = list;
	    Node fast = list;

	    while (slow.next() != null && fast.next().next() != null){
	        slow = slow.next();
	        fast = fast.next().next();
        }

	    return slow;
    }

	private Node merge(Node leftside, Node rightSide, boolean reversed) {
        Node result;
        if (leftside == null)
            return rightSide;

        if (rightSide == null)
            return leftside;

        if ((!reversed & leftside.elem() <= rightSide.elem()) || (reversed & leftside.elem() > rightSide.elem())) {
            result = leftside;
            result.setNext(merge(leftside.next(),rightSide,reversed));
        } else {
            result = rightSide;
            result.setNext(merge(rightSide.next(),leftside,reversed));
        }

        return result;
    }

	public Node mergeSortLL(Node list, boolean reversed) {
        if (list.next() == null)
            return list;

        Node middleNode = middleNode(list);
        Node nextAfterMiddle = middleNode.next();
        middleNode.setNext(null);
        Node leftSide = mergeSortLL(list,reversed);
        Node rightside = mergeSortLL(nextAfterMiddle,reversed);
        return merge(leftSide,rightside,reversed);
	}
}
