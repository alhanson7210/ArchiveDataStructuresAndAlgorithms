package heap;

/** A priority queue: represented by the min heap.
 *  Used in Prim's algorithm. */
public class PriorityQueue {
    // FILL IN CODE as needed
    private HeapElement[] heap;
    private Integer[] heapPositionArray;
    private static int MAXSIZE;
    private int size;
    private static Integer INFINITY = Integer.MAX_VALUE, NEGATIVEINFINITY = Integer.MIN_VALUE;

    public PriorityQueue(int maxsize) {
        MAXSIZE = maxsize + 1;
        size = 0;
        heap = new HeapElement[MAXSIZE];
        heapPositionArray = new Integer[maxsize];
        heap[0] = new HeapElement(-1, NEGATIVEINFINITY);
    }

    private class HeapElement {
        private int id, cost;
        /**
         * heap element containing an id and its
         * @param i vertex index
         * @param c cost to destination index
         */
        private HeapElement(int i, int c) {
            /*
             * source vertex id or city Id
             */
            id = i;
            /*
             * Cost to travel between an edges id1 and id2
             */
            cost = c;
        }
    }

    public void print() {
        for (int i = 1; i <= size; i++)
            System.out.print(heap[i] + " ");
        System.out.println();
    }

    private void swap(int firstPosition, int secondPosition) {
        int fpCityId = getCityId(firstPosition);
        int spCityId = getCityId(secondPosition);
        if(fpCityId == -1 || spCityId == -1) return;
        HeapElement temporaryStore;
        temporaryStore = heap[firstPosition];
        heap[firstPosition] = heap[secondPosition];
        heapPositionArray[fpCityId] = secondPosition;
        heap[secondPosition] = temporaryStore;
        heapPositionArray[spCityId] = firstPosition;
    }

    private int parent(int position) {
        return position/2;
    }

    public void insert(int id, int cost) {
        size++;
        heap[size] = new HeapElement(id+1,cost);
        heapPositionArray[id] = size;
        int currentElement = size, parentElement = parent(currentElement);
        while(heap[currentElement].cost < heap[parentElement].cost) {
            swap(currentElement, parentElement);
            currentElement = parentElement;
            parentElement = parent(currentElement);
        }
    }

    private boolean isALeaf(int position) {
        int lastParent = size/2, lastElement = size;
        return (position > lastParent) && (position < lastElement);
    }

    private int leftChild(int position) {
        return position * 2;
    }

    private int rightChild(int position) {
        return position * 2 + 1;
    }

    public void reduceKey(int cityId, int newCost) {
        //int smallestChild;

        int heapPositionIdx = heapPositionArray[cityId];
        //System.out.print("\nthe heap position is " + heapPositionIdx + "and has a new priority of " + newCost + " and the old priority cost is ");
        if(heapPositionIdx <= 0 || heapPositionIdx > size) return;
        //reposition the newly changed cityid based off of its new cost
        heap[heapPositionIdx].cost = newCost;
        int currentElement = heapPositionIdx, parentElement = parent(currentElement);
        while(heap[currentElement].cost < heap[parentElement].cost) {
            swap(currentElement, parentElement);
            currentElement = parentElement;
            parentElement = parent(currentElement);
        }
//
//        heapPositionIdx = heapIdx(cityId);
//
//        while (!isALeaf(heapPositionIdx)) {
//            // set the smallest child to left child
//            smallestChild = leftChild(heapPositionIdx);
//            if ((smallestChild < size) && (heap[smallestChild].cost > heap[smallestChild + 1].cost))
//                /* right child was smaller, so smallest child = right child */
//                smallestChild = smallestChild + 1;
//            // the value of the smallest child is less than value of current,
//            if (heap[heapPositionIdx].cost <= heap[smallestChild].cost)
//                /* the heap is already valid */
//                return;
//            swap(heapPositionIdx, smallestChild);
//            heapPositionIdx = smallestChild;
//        }
    }

    private int getCityId(int heapIdx) {
        if(heapIdx == 0) return -1;

        for (int i = 0; i < heapPositionArray.length; i++) {
            if(heapPositionArray[i] == heapIdx)
                return i;
        }

        return NEGATIVEINFINITY;
    }

    public boolean neighborKnown(int neighbor) {
        return heapIdx(neighbor) == NEGATIVEINFINITY;
    }

    private int heapIdx(int cityId) {return (cityId == -1)? NEGATIVEINFINITY: heapPositionArray[cityId];}

    public boolean heapIsFull() {
        for (HeapElement element : heap) if (element.id != -1 && neighborKnown(element.id)) return true;
        return false;
    }

    public int removeMin() {
        // swap the end of the heap into the root
        swap(1, size);
        // removed the end of the heap
        size--;
        // fix the heap property - push down as needed
        if (size != 0)
            reduceKey(size, heap[size].cost);
        int cityId = getCityId(size+1);
        heapPositionArray[cityId] = NEGATIVEINFINITY;
        //remove the min value
        return cityId;
    }
}

