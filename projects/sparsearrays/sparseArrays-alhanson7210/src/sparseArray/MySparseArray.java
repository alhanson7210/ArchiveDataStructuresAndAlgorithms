package sparseArray;

import java.io.*;

/** Implementation of the SparseArray Interface.  */
public class MySparseArray implements SparseArray {
    // Stores default value
    // Stores two heads (of type MyNode): rowHead and columnHead
    // FILL IN CODE
    private MyNode dummyHead;
    private Object value;

    /**
     * creates new dummy head
     * Sets the default value for the sparse array
     * @param defaultValue default value
     */
    public MySparseArray(Object defaultValue) {
        // FILL IN CODE
        dummyHead = new MyNode(-1,-1,defaultValue);
        value = defaultValue;
    }

    /**
     * Getter for the default value
     * @return Returns the default value
     */
    @Override
    public Object getDefaultValue() {
        // FILL IN CODE
        return value; // change
    }


    /**
     * Gets element at the given row and column
     * if the node does not exist return default value
     * @param row row
     * @param col column
     * @return default value
     */
    @Override
    public Object elementAt(int row, int col) {
        // FILL IN CODE
        //out of range
        if(row <= -1 || col <= -1)
            return value;
        //only the dummy head
        if(dummyHead.rowNext() == null || dummyHead.colNext() == null)
            return value;
        //at least one node is present
        MyNode columnNode = dummyHead;
        while(columnNode.colNext() != null) {
            if(columnNode.colNext().columnIndex() == col) {
                MyNode rowNode = columnNode.colNext();
                while(rowNode.rowNext() != null){
                    if(rowNode.rowNext().rowIndex() == row)
                        return rowNode.rowNext().value();
                    rowNode = rowNode.rowNext();
                }
                return value;
            }
            columnNode = columnNode.colNext();
        }
        return value; // change
    }

    /**
     * Modifies the value at a given row, column,
     * or inserts the node for this row, column in the sparse array
     * if it did not exist before.
     * If value is the default value, then the node should be deleted from
     * the sparse array
     * @param row row
     * @param col column
     * @param value value of the element
     */
    @Override
    public void setValue(int row, int col, Object value) {
        //A lot of things are off in my set value method where I should have used node instead of columnNode/rowNode in a lot of places that I wouldn't have the time to fix
        //dummy nodes that are out of bounds
        if(row <= -1 || col <= -1)
            return;
        //elements that exist already
        if(!this.value.equals(elementAt(row,col))) {
            MyNode columnNode = dummyHead;
            while(columnNode.colNext() != null) {
                if (columnNode.colNext().columnIndex() == col) {
                    MyNode rowNode = columnNode.colNext();
                    while (rowNode.rowNext() != null) {
                        if(rowNode.rowNext().rowIndex() == row && value.equals(this.value)) {
                            if (rowNode.rowNext().rowNext() == null) {
                                rowNode.setRowNext(null);
                                return;
                            } else {
                                rowNode.setRowNext(rowNode.rowNext().rowNext());
                                return;
                            }
                        }
                        else if (rowNode.rowNext().rowIndex() == row) { //assuming default values are handled by the previous, this should catch non defalut values
                            rowNode.rowNext().setValue(value);
                            return;
                        }
                        rowNode = rowNode.rowNext();
                    }
                }
                columnNode = columnNode.colNext();
            }
        }
        //element is nonexistent but the value is the same as the default value
        else {/*(value.equals(this.value) may not need && this.value.equals(elementAt(row,col)))*/
            if(value.equals(this.value))
                return;
        }
        //set the first node with non-default value
        MyNode node = new MyNode(row,col,value);
        if(dummyHead.rowNext() == null && dummyHead.colNext() == null) {
            dummyHead.setRowNext(new MyNode(row,-1, this.value));
            dummyHead.setColNext(new MyNode(-1, col, this.value));
            dummyHead.rowNext().setColNext(node);
            dummyHead.colNext().setRowNext(node);
            return;
        }
        //setting elements after the first node that are nonexistent
        MyNode columnNode = dummyHead;
        //finding the correct column
        while (columnNode.colNext() != null) {
            //column that exists
            if (columnNode.colNext().columnIndex() == col) {
                MyNode rowNode = columnNode.colNext();
                //connecting the new node in the correct row position within the column
                while (rowNode.rowNext() != null) {
                    if (rowNode.rowIndex() < row && rowNode.rowNext().rowIndex() > row) {
                        node.setRowNext(rowNode.rowNext());
                        rowNode.setRowNext(node);
                        break;
                    }
                    rowNode = rowNode.rowNext();
                }
                //case where node belongs at the end
                if (rowNode.rowIndex() < row && rowNode.rowNext() == null) {
                    rowNode.setRowNext(node);
                }
                //the row doesn't exist which means we must create a new row node for this case
                rowNode = dummyHead;
                //restarting from the dummyHead since there is no
                //connecting the new node in the correct column position within the row
                //finding the correct row
                MyNode dummyRow;
                while (rowNode.rowNext() != null) {
                    //exception row exists as well but the cell itself is empty at row and col
                    if (rowNode.rowNext().rowIndex() == row) {
                        columnNode = rowNode.rowNext();
                        while(columnNode.colNext() != null) {
                            if(columnNode.columnIndex() < col && columnNode.colNext().columnIndex() > col) {
                                node.setColNext(columnNode.colNext());
                                columnNode.setColNext(node);
                                return;
                            }
                            else if (columnNode.colNext().columnIndex() < col && columnNode.colNext().colNext() == null) {
                                columnNode.colNext().setColNext(node);
                                return;
                            }
                            columnNode = columnNode.colNext();
                        }
                    }
                    else if (rowNode.rowIndex() < row && rowNode.rowNext().rowIndex() > row) {
                        dummyRow = new MyNode(row,rowNode.columnIndex(),this.value);
                        dummyRow.setRowNext(rowNode.rowNext());
                        rowNode.setRowNext(dummyRow);
                        dummyRow.setColNext(node);
                        return;
                    }
                    rowNode = rowNode.rowNext();
                }

                if (rowNode.rowIndex() < row && rowNode.rowNext() == null) {
                    dummyRow = new MyNode(row,rowNode.columnIndex(),this.value);
                    rowNode.setRowNext(dummyRow);
                    dummyRow.setColNext(node);
                    return;
                }
            }





            //new column between existing columns
            else if (columnNode.columnIndex() < col && columnNode.colNext().columnIndex() > col) {
                //new column
                MyNode dummyColumn = new MyNode(columnNode.rowIndex(),col,this.value);
                //save the current columns neighbor to the new columns neighbor
                dummyColumn.setColNext(columnNode.colNext());
                //change the current columns neighbor to the new column
                columnNode.setColNext(dummyColumn);
                //only element present in the new column
                dummyColumn.setRowNext(node);
                MyNode dummyRow;
                //start over from the dummyHead to traverse the same way but going down the rows instead
                MyNode rowNode = dummyHead;
                while (rowNode.rowNext() != null) {
                    //traversing the dummy nodes
                    if (rowNode.rowIndex() == row) {
                        //setting the nodes correct column position
                        columnNode = rowNode.rowNext();
                        while(columnNode.colNext() != null) {
                            //position between nodes
                            if(columnNode.columnIndex() < col && columnNode.colNext().columnIndex() > col) {
                                node.setColNext(columnNode.colNext());
                                columnNode.setColNext(node);
                                return;
                            }
                            //last element
                            else if (columnNode.colNext().columnIndex() < col && columnNode.colNext().colNext() == null) {
                                columnNode.colNext().setColNext(node);
                                return;
                            }
                            columnNode = columnNode.colNext();
                        }

                    }
                    //create new dummy row and set column position
                    else if (rowNode.rowIndex() < row && rowNode.rowNext().rowIndex() > row) {
                        dummyRow = new MyNode(row,rowNode.columnIndex(),this.value);
                        dummyRow.setRowNext(rowNode.rowNext());
                        rowNode.setRowNext(dummyRow);
                        dummyRow.setColNext(node);
                        return;
                    }
                    rowNode = rowNode.rowNext();
                }
                if (rowNode.rowIndex() < row && rowNode.rowNext() == null) {
                    dummyRow = new MyNode(row,rowNode.columnIndex(),this.value);
                    rowNode.setRowNext(dummyRow);
                    dummyRow.setColNext(node);
                    return;
                }
            }

            columnNode = columnNode.colNext();
        }






        //new column at the end
        if(columnNode.columnIndex() < col && columnNode.colNext() == null) {
            MyNode dummyColumn = new MyNode(columnNode.rowIndex(),col,this.value);
            //change the current columns neighbor to the new column
            columnNode.setColNext(dummyColumn);
            dummyColumn.setRowNext(node);
            MyNode dummyRow;
            MyNode rowNode = dummyHead;
            while (rowNode.rowNext() != null) {
                if (rowNode.rowIndex() == row) {
                    //setting the nodes correct column position
                    columnNode = rowNode.rowNext();
                    while(columnNode.colNext() != null) {
                        //position between nodes
                        if(columnNode.columnIndex() < col && columnNode.colNext().columnIndex() > col) {
                            node.setColNext(columnNode.colNext());
                            columnNode.setColNext(node);
                            return;
                        }
                        //last element
                        else if (columnNode.colNext().columnIndex() < col && columnNode.colNext().colNext() == null) {
                            columnNode.colNext().setColNext(node);
                            return;
                        }
                        columnNode = columnNode.colNext();
                    }
                }
                else if (rowNode.rowIndex() < row && rowNode.rowNext().rowIndex() > row) {
                    dummyRow = new MyNode(row,rowNode.columnIndex(),this.value);
                    dummyRow.setRowNext(rowNode.rowNext());
                    rowNode.setRowNext(dummyRow);
                    dummyRow.setColNext(node);
                    return;
                }
                rowNode = rowNode.rowNext();
            }
            if (rowNode.rowIndex() < row && rowNode.rowNext() == null) {
                dummyRow = new MyNode(row,rowNode.columnIndex(),this.value);
                rowNode.setRowNext(dummyRow);
                dummyRow.setColNext(node);
            }
        }
    }


    /** Read the sparse array from the file with the given filename
     *
     * @param filename name of the input file
     */
    @Override
    public void readFromFile(String filename) {
        // FILL IN CODE
        int row, col, val;
        try {
            File file = new File(filename);
            FileReader reader = new FileReader(file);
            BufferedReader sparsereader = new BufferedReader(reader);
            String newNode;
            while((newNode = sparsereader.readLine()) != null) {
                String[] nodeData = newNode.split(",");
                try {
                    if(Integer.parseInt(nodeData[2].trim()) != 0) {
                        row = Integer.parseInt(nodeData[0].trim());
                        col = Integer.parseInt(nodeData[1].trim());
                        val = Integer.parseInt(nodeData[2].trim());
                        setValue(row, col, val);
                    }
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.err.print(fnfe.getMessage());
        } catch (IOException io) {
            io.printStackTrace();
        }
    }


    /**
     * Outputs the sparse array to the file with the given filename.
     * Prints only row, col on each line.
     *
     */
    @Override
    public void printToFile(String filename) {
        // FILL IN CODE
        if(dummyHead.rowNext() == null || dummyHead.colNext() == null)
            return;

        try {
            File file = new File(filename);
            FileWriter writer = new FileWriter(file);
            MyNode columnNode = dummyHead;
            while(columnNode.colNext() != null) {
                MyNode rowNode = columnNode.colNext();
                while(rowNode.rowNext() != null) {
                    if(!this.value.equals(rowNode.rowNext().value())) {
                        String rowColumnAndValue = String.format("%d,%d,%d\n", rowNode.rowIndex(), rowNode.columnIndex(), (int) rowNode.value());
                        writer.write(rowColumnAndValue);
                    }
                    rowNode = rowNode.rowNext();
                }
                columnNode = columnNode.colNext();
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    private int NeighborCount(int row, int col) {
        if(row == -1 || col == -1)
            return 0;

        int neighborCount = 0;
        for (int x = row-1; x <= row +1; x++) {
            for (int y = col-1; y <= col; y++) {
                if(!value.equals(elementAt(x,y)) && x != row && y != col) {
                    neighborCount++;
                }
            }
        }
        return neighborCount;
    }

    private int numberOfRows(){
        int NumberOfRows = 0;
        MyNode rowNode = dummyHead;
        while (rowNode.rowNext() != null) {
            NumberOfRows++;
            rowNode = rowNode.rowNext();
        }
        return NumberOfRows;
    }

    private int numberOfColumns() {
        int NumberOfRows = 0;
        MyNode columnNode = dummyHead;
        while (columnNode.rowNext() != null) {
            NumberOfRows++;
            columnNode = columnNode.rowNext();
        }
        return NumberOfRows;
    }
    public MySparseArray gameOfLife() {
        MySparseArray nextGeneration = numberOfNeighbors();
        int NumberOfRows = numberOfRows(), NumberOfColumns = numberOfColumns();
        return nextGeneration;
    }

    private MySparseArray numberOfNeighbors() {
        if(dummyHead.rowNext() == null || dummyHead.colNext() == null)
            return this;

        MySparseArray nextGeneration = new MySparseArray(0);
        MyNode rowNode = dummyHead;
        while (rowNode.rowNext() != null) {
            MyNode columnNode = rowNode.rowNext();
            int thisCellsNumberOfNeighbors = 0;
            while(columnNode.colNext() != null) {
                for (int y = columnNode.rowIndex()-1; y <= columnNode.rowIndex()+1; y++) {
                    for (int x = columnNode.columnIndex()-1; x <= columnNode.columnIndex()+1; x++) {
                        if(y != columnNode.rowIndex() && x != columnNode.columnIndex()) {
                            if (elementAt(y, x).equals(value)) {
                                if(NeighborCount(y,x) == 3) nextGeneration.setValue(y,x,1);
                            } else {
                                if ((NeighborCount(y, x) == 3)) {
                                    nextGeneration.setValue(y, x, elementAt(y, x));
                                } else {
                                    nextGeneration.setValue(y, x, value);
                                }
                            }
                        }
                        else{
                            thisCellsNumberOfNeighbors = NeighborCount(columnNode.rowIndex(),columnNode.columnIndex());
                            if (thisCellsNumberOfNeighbors < 2)
                                nextGeneration.setValue(columnNode.rowIndex(),columnNode.columnIndex(), value);
                            else if (thisCellsNumberOfNeighbors < 4)
                                nextGeneration.setValue(columnNode.rowIndex(),columnNode.columnIndex(),1);
                            else
                                nextGeneration.setValue(columnNode.rowIndex(),columnNode.columnIndex(),value);
                        }
                    }
                }
                columnNode = columnNode.colNext();
            }
            rowNode = rowNode.rowNext();
        }
        return nextGeneration;
    }

    public void printSparseArray() {
        System.out.print("\n");
        MyNode rowNode = dummyHead;
        while (rowNode.rowNext() != null) {
            MyNode columnNode = rowNode;
            while(columnNode.colNext() != null) {
                if(rowNode.rowIndex() != -1 || columnNode.columnIndex() != -1) {
                    System.out.print("{r = " + columnNode.rowIndex() + ", c = " + columnNode.columnIndex() + ", v = " + columnNode.value() + "}\t");
                }
                columnNode = columnNode.colNext();
            }
            rowNode = rowNode.rowNext();
            System.out.print("\n");
        }
    }
    // Add other methods as needed - like the method that counts neighbors of the cell etc.
}