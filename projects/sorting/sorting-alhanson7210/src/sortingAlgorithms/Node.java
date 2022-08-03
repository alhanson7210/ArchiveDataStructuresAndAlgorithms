package sortingAlgorithms;

/** Represents a single node in a singly linked list */
public class Node {
    private int elem;
    private Node next;

    /**
     * Constructor
     * @param elem integer element
     */
    public Node(int elem) {
        this.elem = elem;
        this.next = null;
    }

    /**
     * Constructor with parameters
     * @param elem integer element
     * @param next reference to the next node
     */
    public Node (int elem, Node next) {
        this.elem = elem;
        this.next = next;
    }

    /**
     * Returns element
     * @return element
     */
    public int elem() {
        return elem;
    }

    /**
     * Returns next.
     * @return Reference to the next node
     */
    public Node next() {
        return next;
    }

    /**
     * Setter for the element
     * @param elem new element to store at the node
     */
    public void setElem(int elem) {
        this.elem = elem;
    }

    /**
     * Setter for next
     * @param nextNode node that should be as the next node
     */
    public void setNext(Node nextNode) {
        next = nextNode;
    }
}
