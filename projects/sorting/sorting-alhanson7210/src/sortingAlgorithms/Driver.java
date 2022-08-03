package sortingAlgorithms;

import java.util.Random;
import static sortingAlgorithms.ListType.*;
import static sortingAlgorithms.SortType.*;
public class Driver {
	public static void main(String[] args) {
	    //INSERTION SORT
        efficiencyTest(1,125,SORTED,INSERTION);
        efficiencyTest(1,125,REVERSED,INSERTION);
        efficiencyTest(1,125,RANDOM,INSERTION);
        //SHAKER SORT
        efficiencyTest(1,125,SORTED,SHAKER);
        efficiencyTest(1,125,REVERSED,SHAKER);
        efficiencyTest(1,125,RANDOM,SHAKER);
        //RANDOMIZED QUICK SORT
        efficiencyTest(1,125,SORTED,QUICKSORT);
        efficiencyTest(1,125,REVERSED,QUICKSORT);
        efficiencyTest(1,125,RANDOM,QUICKSORT);
        //HYBRID SORT
        efficiencyTest(1,125,SORTED,HYBRID);
        efficiencyTest(1,125,REVERSED,HYBRID);
        efficiencyTest(1,125,RANDOM,HYBRID);
        //HYBRID SORT VARIANT
        efficiencyTest(1,125,SORTED,HIBRIDVARIANT);
        efficiencyTest(1,125,REVERSED,HIBRIDVARIANT);
        efficiencyTest(1,125,RANDOM,HIBRIDVARIANT);
	}

	private static void efficiencyTest(int numberOfIterations, int listSize, ListType typeOfList, SortType sortingMethod){
        //model for testing an algorithm
        Random randomGenerator = new Random();
        SortingAlgorithms sa = new SortingAlgorithms();
        long startTime = System.currentTimeMillis();
        for(int i=0;i<numberOfIterations;i++) {
            Comparable[] list = new Comparable[listSize];
            switch (typeOfList) {
                case RANDOM:
                    for (int j = 0; j < listSize; j++) {
                        list[j] = randomGenerator.nextInt(100) + randomGenerator.nextInt(300);
                    }
                    break;
                case REVERSED:
                    int count = listSize;
                    for (int j = 0; j < listSize; j++) {
                        list[j] = count;
                        count--;
                    }
                    break;
                case SORTED:
                    for (int j = 0; j < listSize; j++) {
                        list[j] = j+1;
                    }
                    break;
            }
            switch(sortingMethod) {
                case HYBRID:
                    sa.hybridSort(list,0,list.length-1,false);
                    break;
                case SHAKER:
                    sa.shakerSort(list,0,list.length-1,false);
                    break;
                case INSERTION:
                    sa.insertionSort(list,0,list.length-1,false);
                    break;
                case QUICKSORT:
                    sa.randomizedQuickSort(list,0,list.length-1,false);
                    break;
                //non-interface hybrid sort method
                case HIBRIDVARIANT:
                    sa.hybridsort(list,0,list.length-1,false);
                    break;
            }
        }
        long endTime = System.currentTimeMillis();
        double runningTime= ((double) (endTime -startTime)) / numberOfIterations;
        System.out.println(String.format("Sorting Algorithm\tRuntime\tArray Size\tType of List\n%s\t%f\t%d\t%s",sortingMethod,runningTime,listSize,typeOfList));
        //return (int) runningTime;
    }
}
