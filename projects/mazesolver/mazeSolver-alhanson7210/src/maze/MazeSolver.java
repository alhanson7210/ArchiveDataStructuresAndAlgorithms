package maze;

import direction.Directions;
import draw.StdDraw;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.awt.*;
import java.io.IOException;

import static direction.Directions.*;

/** The class that stores the maze, and has methods to read it from the
 * file, to draw it, and to solve it using a "follow left hand" method,
 * as well as helper methods.
 */
class MazeSolver {
    MazeSolver() {
        currentDirection = EAST;
    }

//    public static void main(String[] args) {
//        MazeSolver ms = new MazeSolver();
//        try {
//            System.out.println(ms);
//            ms.readFile("../../input/testMaze1");
//        } catch (IOException io) {
//            System.err.println("can't read file");
//        }
//    }
    private static Color YELLOW = new Color(255, 255, 0);
    private static Color BLACK = new Color(0, 0, 0);

    private int[][] maze; // 0 represents the space, 1 represents the wall

    private Directions currentDirection;

    private static final int WALL = 1;
    private static final int CORRIDOR = 0;

    //provided methods
    /**
     * Draw a square to represent a cell of the maze at row i and column j
     * (draw using a given color).
     *
     * @param i row id
     * @param j column id
     * @param color color
     */
    private void drawSquare(int i, int j, Color color) {
        int x = j;
        int y = i;
        int rows = maze.length;
        int cols = maze[0].length;
        double sqSizeY = 1.0 / rows;
        double sqSizeX = 1.0 / cols;
        StdDraw.setPenColor(color);
        double centerX = x * sqSizeX + sqSizeX / 2.0;
        double centerY = 1 - y * sqSizeY - sqSizeY / 2.0;
        StdDraw.filledRectangle(centerX, centerY, sqSizeX / 2.0, sqSizeY / 2.0);
        try {
            Thread.sleep(25);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Draw the maze: draw wall in black, and passages in white. */
    void displayMaze() {
        int rows = maze.length;
        int cols = maze[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // draw a black square at (i,j) if there is a wall at that cell
                // don't draw anything otherwise (equivalent to drawing a "white
                // square")
                int res = maze[i][j];
                if (res == 1) {
                    drawSquare(i, j, BLACK);

                }
            }
        }
    }

    //Instructions
    // FILL IN CODE: add a method to read the maze from the file;
    // add a method solve the maze and add helper methods as needed.

    //Added methods
    /**
     * render the maze from a text file into a 2d integer array.
     * @param fileName path to the maze text file required to render the maze.
     * @throws IOException catches the file not found exception if there is an invalid path; this includes normal io errors that may arise.
     */
    void readFile(String fileName) throws IOException {
        //create a buffered reader to read from a given file
        File file = new File(fileName);
        //System.out.println("made file");
        //System.out.println(file.getAbsolutePath());
        FileReader fileReader = new FileReader(file.getAbsolutePath());
        //System.out.println("made file reader");
        BufferedReader br = new BufferedReader(fileReader);
        //System.out.println("made buffered reader");
        //keep track of each line in file
        int lineCount = 0;
        int threshold = 0;
        //int startOfMaze = 0;
        //read each line of the file until there are no more
        //System.out.println("Start reading from file");
        String line = br.readLine();
        while(line != null && lineCount <= threshold) {
            //first line should not contain a # which can be checked for to obtain the maze's size
            if (!line.contains("#")){
                String[] mazeSize = line.split(" ");
                //System.out.println(mazeSize[0] + mazeSize[1]);
                try {
                    //assign the size of the maze
                    maze = new int[Integer.parseInt(mazeSize[0])][Integer.parseInt(mazeSize[1])];
                    threshold = Integer.parseInt(mazeSize[0]);
                } catch (NumberFormatException nfe) {
                    //problem reading the size of the maze
                    System.err.println(nfe.getMessage());
                }

            }
            else {
                //reading the rows of the maze to determine if we have a wall or a corridor
                for (int i = 0; i < maze.length; i++) {
                    //System.out.println(line.charAt(i));
                    if (line.charAt(i) == '#')
                        maze[lineCount-1][i] = WALL;
                    else
                        maze[lineCount-1][i] = CORRIDOR;
                }
                //increment the row when finished with a line
                //lineCount++;
                //read next line for the row
                //line = br.readLine();
            }
            lineCount++;
            line = br.readLine();
        }
        //System.out.println("finished reading file");
    }

    /**
     * Draw a square for the current position, test for the base case of reaching the exit, and recursively call the follow wall method updating the current direction and position as needed.
     * @param row row of the new/starting position.
     * @param col column of the new/starting position.
     */
    void followWall(int row, int col){
        //return if we've found our way to the exit
        if (row == maze.length - 2 && col == maze[0].length - 1){
            drawSquare(row,col,YELLOW);
        }
        //recursively call the follow the wall method
        else {
            //first draw the current square
            drawSquare(row,col,YELLOW);
            //according to our current direction, update where we are facing to the appropriate direction
            switch (currentDirection) {
                //adapt the left,forwards,right,and backwards according to our current direction
                case EAST:
                    //if we can turn to the left, update the direction we are facing, then 'move' to that square; left is row - 1, col.
                    if(maze[row-1][col] == CORRIDOR){
                        updateDirection(LEFT);
                        followWall(row-1,col);
                    }
                    //if we can go forwards, update the direction we are facing, then 'move' to that square; forwards is row, col + 1
                    else if(maze[row][col+1] == CORRIDOR){
                        updateDirection(FORWARDS);
                        followWall(row,col+1);
                    }
                    //if we can turn to the right, update the direction we are facing, then 'move' to that square; right is row + 1, col
                    else if(maze[row+1][col] == CORRIDOR){
                        updateDirection(RIGHT);
                        followWall(row+1,col);
                    }
                    //if we can go backwards, update the direction we are facing, then 'move' to that square; backwards is row, col - 1
                    else if(maze[row][col-1] == CORRIDOR) {
                        updateDirection(BACKWARDS);
                        followWall(row, col - 1);
                    }
                    break;
                case NORTH:
                    //if we can turn to the left, update the direction we are facing, then 'move' to that square; left is row - 1, col.
                    if(maze[row][col-1] == CORRIDOR){
                        updateDirection(LEFT);
                        followWall(row,col-1);
                    }
                    //if we can go forwards, update the direction we are facing, then 'move' to that square; forwards is row, col + 1
                    else if(maze[row-1][col] == CORRIDOR){
                        updateDirection(FORWARDS);
                        followWall(row-1,col);
                    }
                    //if we can turn to the right, update the direction we are facing, then 'move' to that square; right is row + 1, col
                    else if(maze[row][col+1] == CORRIDOR){
                        updateDirection(RIGHT);
                        followWall(row,col+1);
                    }
                    //if we can go backwards, update the direction we are facing, then 'move' to that square; backwards is row, col - 1
                    else if(maze[row+1][col] == CORRIDOR) {
                        updateDirection(BACKWARDS);
                        followWall(row+1, col);
                    }
                    break;
                case WEST:
                    //if we can turn to the left, update the direction we are facing, then 'move' to that square; left is row - 1, col.
                    if(maze[row+1][col] == CORRIDOR){
                        updateDirection(LEFT);
                        followWall(row+1,col);
                    }
                    //if we can go forwards, update the direction we are facing, then 'move' to that square; forwards is row, col + 1
                    else if(maze[row][col-1] == CORRIDOR){
                        updateDirection(FORWARDS);
                        followWall(row,col-1);
                    }
                    //if we can turn to the right, update the direction we are facing, then 'move' to that square; right is row + 1, col
                    else if(maze[row-1][col] == CORRIDOR){
                        updateDirection(RIGHT);
                        followWall(row-1,col);
                    }
                    //if we can go backwards, update the direction we are facing, then 'move' to that square; backwards is row, col - 1
                    else if(maze[row][col+1] == CORRIDOR) {
                        updateDirection(BACKWARDS);
                        followWall(row, col + 1);
                    }
                    break;
                case SOUTH:
                    //if we can turn to the left, update the direction we are facing, then 'move' to that square; left is row - 1, col.
                    if(maze[row][col+1] == CORRIDOR){
                        updateDirection(LEFT);
                        followWall(row,col+1);
                    }
                    //if we can go forwards, update the direction we are facing, then 'move' to that square; forwards is row, col + 1
                    else if(maze[row+1][col] == CORRIDOR){
                        updateDirection(FORWARDS);
                        followWall(row+1,col);
                    }
                    //if we can turn to the right, update the direction we are facing, then 'move' to that square; right is row + 1, col
                    else if(maze[row][col-1] == CORRIDOR){
                        updateDirection(RIGHT);
                        followWall(row,col-1);
                    }
                    //if we can go backwards, update the direction we are facing, then 'move' to that square; backwards is row, col - 1
                    else if(maze[row-1][col] == CORRIDOR) {
                        updateDirection(BACKWARDS);
                        followWall(row-1, col);
                    }
                    break;
            }
        }
    }

    /**
     * Depending on if we change from left, forwards, right, or backwards: change the current direction to reflect the appropriate change in direction.
     * @param newDirection change in direction we want to change to: left, forwards, right, or backwards.
     */
    private void updateDirection(Directions newDirection){
        switch (newDirection) {
            //if we are changing our direction to the left
            case LEFT:
                switch (currentDirection) {
                    //according to our current direction, update where we are facing to the appropriate direction
                    case EAST:
                        currentDirection = NORTH;
                        break;
                    case NORTH:
                        currentDirection = WEST;
                        break;
                    case WEST:
                        currentDirection = SOUTH;
                        break;
                    case SOUTH:
                        currentDirection = EAST;
                        break;
                }
                break;
            //if we are changing our direction so that we move forwards
            case FORWARDS:
                switch (currentDirection) {
                    //according to our current direction, update where we are facing to the appropriate direction
                    case EAST:
                        break;
                    case NORTH:
                        break;
                    case WEST:
                        break;
                    case SOUTH:
                        break;
                }
                break;
            //if we are changing our direction to the right
            case RIGHT:
                switch (currentDirection) {
                    //according to our current direction, update where we are facing to the appropriate direction
                    case EAST:
                        currentDirection = SOUTH;
                        break;
                    case NORTH:
                        currentDirection = EAST;
                        break;
                    case WEST:
                        currentDirection = NORTH;
                        break;
                    case SOUTH:
                        currentDirection = WEST;
                        break;
                }
                break;
            //if we are changing our direction so that we move backwards
            case BACKWARDS:
                switch (currentDirection) {
                    //according to our current direction, update where we are facing to the appropriate direction
                    case EAST:
                        currentDirection = WEST;
                        break;
                    case NORTH:
                        currentDirection = SOUTH;
                        break;
                    case WEST:
                        currentDirection = EAST;
                        break;
                    case SOUTH:
                        currentDirection = NORTH;
                        break;
                }
                break;
        }
    }
//    int rowSize(){
//        return maze.length;
//    }
//    int colSize(){
//        return maze[0].length;
//    }
//    int[][] getMaze(){
//        return maze;
//    }
}
