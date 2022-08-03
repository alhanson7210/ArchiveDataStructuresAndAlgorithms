package graph;

/**
 * A class that represents a graph: stores the array of city nodes, the
 * adjacency list, as well as the hash table that maps city names to node ids.
 * Nodes are cities (of type CityNode); edges connect them and the cost of each edge
 * is the distance between the cities.
 * Fill in code in this class. You may add additional methods and variables.
 * You are required to implement a PriorityQueue from scratch, instead of using Java's built in.
 */

import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class Graph {
    private CityNode[] nodes; // nodes of the graph
    private Edge[] adjacencyList; // adjacency list; for each vertex stores a linked list of edges
    private int numEdges; // total number of edges
    // Add other variables as needed
    private HashMap<String, Integer> cityIds;
//    private static HashMap<Integer, String[]> edges;
    /**
     * Constructor. Reads graph info from the given file,
     * and creates nodes and edges of the graph.
     *
     * @param filename name of the file that has nodes and edges
     */
    public Graph(String filename) {
       // FILL IN CODE: load the graph from the given file
        try {
            //instance variables
            int Nodes = 0, cost, id = 0, numEdges = 0,/* lncnt = 0,*/ sourceId, destinationId;
            String line, info[], cityName, source, destination /*, l*/;
            double xcoord, ycoord;
            Edge currentEdge;
            cityIds = new HashMap<>();
//            edges = new HashMap<>();
            //counting total amount of lines for the number of edges
//            BufferedReader f = new BufferedReader(new FileReader(new File(filename)));
//            while ((l = f.readLine()) != null ) {
//                //increment number of edges
//                lncnt++;
//            }
            //reading actual graph information in from file
            BufferedReader graphInfo = new BufferedReader(new FileReader(new File(filename)));
            while((line = graphInfo.readLine()) != null) {
                if(line.startsWith("N")) {
                    //number of node or vertices on the graph
                    line = graphInfo.readLine();
                    Nodes = Integer.parseInt(line.trim());
                    //set the size of the city nodes array
                    nodes = new CityNode[Nodes];
                    //create city nodes
                    for(int i = 0; i < Nodes; i++) {
                        //next city node from file
                        line = graphInfo.readLine();
                        info = line.split(" ");
                        if (info.length == 3) {
                            //city node arguments
                            cityName = info[0];
                            xcoord = Double.parseDouble(info[1].trim());
                            ycoord = Double.parseDouble(info[2].trim());
                            //add city and id to hash map
                            cityIds.putIfAbsent(cityName, i);
                            //create city node
                            nodes[i] = new CityNode(cityName, xcoord, ycoord);
                        }
                    }
                }
                else if (line.startsWith("A")) {
                    //numEdges = lncnt - Nodes - 3;
                    adjacencyList = new Edge[Nodes];
                    while (line != null) {
                        //first line of edges to last line
                        //line = graphInfo.readLine();
                        info = line.split(" ");
                        if (info.length == 3) {
                            //edge arguments
                            source = info[0];
                            destination = info[1];
                            cost = Integer.parseInt(info[2].trim());
//                        //add edge to cost based hash. second hash is used only to make printing efficient
//                        edges.putIfAbsent(cost,new String[]{source,destination});
                            sourceId = cityIds.get(source);
                            destinationId = cityIds.get(destination);
                            if (adjacencyList[sourceId] == null && adjacencyList[destinationId] == null) {
                                //create new edge
                                adjacencyList[sourceId] = new Edge(sourceId, destinationId, cost);
                                adjacencyList[destinationId] = new Edge(destinationId, sourceId, cost);
                            } else if (adjacencyList[sourceId] != null && adjacencyList[destinationId] == null) {
                                currentEdge = adjacencyList[sourceId];
                                while (currentEdge.next() != null) currentEdge = currentEdge.next();
                                currentEdge.setNext(new Edge(sourceId, destinationId, cost));
                                adjacencyList[destinationId] = new Edge(destinationId, sourceId, cost);
                            } else if (adjacencyList[sourceId] == null && adjacencyList[destinationId] != null) {
                                adjacencyList[sourceId] = new Edge(sourceId, destinationId, cost);
                                currentEdge = adjacencyList[destinationId];
                                while (currentEdge.next() != null) currentEdge = currentEdge.next();
                                currentEdge.setNext(new Edge(destinationId, sourceId, cost));
                            } else {
                                //add edge to the source vertex's city Id
                                currentEdge = adjacencyList[sourceId];
                                while (currentEdge.next() != null) currentEdge = currentEdge.next();
                                currentEdge.setNext(new Edge(sourceId, destinationId, cost));
                                //add edge to the destination vertex's city Id
                                currentEdge = adjacencyList[destinationId];
                                while (currentEdge.next() != null) currentEdge = currentEdge.next();
                                currentEdge.setNext(new Edge(destinationId, sourceId, cost));
                            }
                            numEdges += 2;
                        }
                        //next edge from file
                        line = graphInfo.readLine();
                    }
                }
            }
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (IOException i) {
            System.out.print(i.getMessage());
        } catch (NullPointerException npe) {
            System.err.print(npe.getMessage());
        }
    }

    /**
     * Getter for the city ids' and names
     * @return hash map of the city ids and city names
     */
    public HashMap<String,Integer> getCityIds() {return cityIds;}

//    /**
//     * Getter for the two city names based off the cost as the key
//     * @return hash map of the source and destination names
//     */
//    public static HashMap<Integer, String[]> getCostHash() {return edges;}

    /**
     * Return the number of nodes in the graph
     * @return number of nodes
     */
    public int numNodes() {
        return nodes.length;
    }

    /** Return the head of the linked list that contains all edges outgoing
     * from nodeId
     * @param nodeId id of the node
     * @return head of the linked list of Edges
     */
    public Edge getFirstEdge(int nodeId) {
        return adjacencyList[nodeId];
    }

    /**
     * Return the edges of the graph as a 2D array of points.
     * Called from GUIApp to display the edges of the graph.
     *
     * @return a 2D array of Points.
     * For each edge, we store an array of two Points, v1 and v2.
     * v1 is the source vertex for this edge, v2 is the destination vertex.
     * This info can be obtained from the adjacency list
     */
    public Point[][] getEdges() {
        /*NEED TO FIX THIS METHOD*/
        Point[][] edges2D = new Point[numEdges][2];
        // FILL IN CODE
        int j = 0;
        Edge edge;
        for (int i = 0; i < adjacencyList.length && j < numEdges; i++) {
            //source vertex
            edge = adjacencyList[i];
            edges2D[j][0] = nodes[edge.getId1()].getLocation();
            //destination vertex
            edges2D[j][1] = nodes[edge.getId2()].getLocation();
            //increment to access space for next edge
            j++;
            while(edge.next() != null) {
                //next edge
                edge = edge.next();
                //source vertex
                edges2D[j][0] = nodes[edge.getId1()].getLocation();
                //destination vertex
                edges2D[j][1] = nodes[edge.getId2()].getLocation();
                //increment to access space for next edge
                j++;
            }
        }
        return edges2D;
    }

    /**
     * Get the nodes of the graph as a 1D array of Points.
     * Used in GUIApp to display the nodes of the graph.
     * @return a list of Points that correspond to nodes of the graph.
     */
    public Point[] getNodes() {
        if (nodes == null) {
            System.out.println("Graph is empty. Load the graph first.");
            return null;
        }
        Point[] nodes = new Point[this.nodes.length];
        // FILL IN CODE
        for (int i = 0; i < nodes.length; i++) nodes[i] = this.nodes[i].getLocation();
        return nodes;
    }

    /**
     * Used in GUIApp to display the names of the cities.
     * @return the list that contains the names of cities (that correspond
     * to the nodes of the graph)
     */
    public String[] getCities() {
        if (nodes == null) {
            //System.out.println("Graph is empty, load the graph from the file first");
            return null;
        }
        String[] labels = new String[nodes.length];
        // FILL IN CODE
        for(int i = 0; i < labels.length; i++) labels[i] = nodes[i].getCity();
        return labels;

    }

    /**
     * Return the CityNode for the given nodeId
     * @param nodeId id of the node
     * @return CityNode
     */
    public CityNode getNode(int nodeId) {
        return nodes[nodeId];
    }

}