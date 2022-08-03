package algo;

import graph.*;
import heap.PriorityQueue;

/** Subclass of MSTAlgorithm. Uses Prim's algorithm to compute MST of the graph. */
public class PrimAlgorithm extends MSTAlgorithm {

    private int sourceVertex;
    private int[][] table;
    private PriorityQueue minHeap;
    private static int INFINITY = Integer.MAX_VALUE;
    private static int DEFAULTPARENT = -1;

    /**
     * Constructor for PrimAlgorithm. Takes the graph
     * @param graph input graph
     * @param sourceVertex the first vertex of MST
     */
    public PrimAlgorithm(Graph graph, int sourceVertex) {
        super(graph);
        this.sourceVertex = sourceVertex;
    }
    /**
     * Compute minimum spanning tree for this graph using Prim's algorithm.
     * Add edges of MST to edgesMST list.
     * */
    @Override
    public void computeMST() {
        // FILL IN CODE
        // Note: must use a PriorityQueue and be efficient
        //INITIALIZE TABLE
        int numberOfNodes = numNodes();
        table = new int[numberOfNodes][2];
        minHeap = new PriorityQueue(numberOfNodes);
        for (int i = 0; i < numberOfNodes; i++) {
            if(i == sourceVertex) {
                table[i][0] = INFINITY-1;
                table[i][1] = DEFAULTPARENT;
                minHeap.insert(i,INFINITY-1);
            } else {
                table[i][0] = INFINITY;
                table[i][1] = DEFAULTPARENT;
                minHeap.insert(i,INFINITY);
            }
        }

        int knownVertex, j = 0;
        Edge neighbor;
        //REPEAT PROCESS FOR THE NUMBER OF VERTICES
        while(j != numberOfNodes) {
            //FIND THE MINIMUM UNKNOWN VERTEX TO MARK AS KNOWN
            knownVertex = minHeap.removeMin();
            //LOOP THROUGH NEIGHBORS TO FIX COST IF NECESSARY
            neighbor = getFirstEdge(knownVertex);
            //addMSTEdge(neighbor);
            while (neighbor != null) {
                if (!minHeap.neighborKnown(neighbor.getId2())) {
                    if (table[neighbor.getId2()][0] > neighbor.getCost()) {
                        table[neighbor.getId2()][0] = neighbor.getCost();
                        table[neighbor.getId2()][1] = neighbor.getId1();
                        //System.out.print("\n" + neighbor.getId2() + " had its cost changed to " + neighbor.getCost() + " in the table");
                        minHeap.reduceKey(neighbor.getId2(), neighbor.getCost());
                    }
                }
                neighbor = neighbor.next();
            }
            j++;
        }

        for (int i = 0; i < table.length; i++) {
            if(table[i][1] != -1) addMSTEdge(new Edge(table[i][1],i,table[i][0]));
        }
    }
}
