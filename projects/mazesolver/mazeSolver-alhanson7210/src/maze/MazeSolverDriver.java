package maze;

import java.io.FileNotFoundException;
import java.io.IOException;

/** The driver class for the maze solver project */
public class MazeSolverDriver {
    public static void main(String[] args) {
        MazeSolver mazeSolver = new MazeSolver();
        //System.out.println("Created maze solver.\n");
        // FILL IN CODE:
        // Call methods on the mazeSolver to load maze from the file given by
        // the first command line argument args[0],
        // draw it and then solve it using the "follow left wall" method
        if (args.length == 0){
            System.err.println("No command line argument for file name provided");
            System.exit(1);
        } else {
            try {
                //System.out.println(args[0]);

                mazeSolver.readFile(args[0]);
                //System.out.println("File read successfully.\n");
                //BufferedReader br = new BufferedReader(new FileReader(args[0]));

                mazeSolver.displayMaze();
                //System.out.println("Displayed maze successfully.\n");
//                int[][] curMaze = mazeSolver.getMaze();
//                for(int i = 0; i < mazeSolver.rowSize();i++){
//                    for(int j = 0; j < mazeSolver.colSize();j++){
//                        System.out.print(curMaze[i][j]);
//                    }
//                    System.out.print("\n");
//                }
                mazeSolver.followWall(1, 0);//stops at maze[8][1]
                //System.out.println("Finished maze successfully.\n");
            } catch (FileNotFoundException fnfe) {
                System.err.println("Could not find file");
            } catch (IOException ex) {
                System.err.println(String.format("Error loading file %s \n", args[0]));
            }
        }
        //System.out.println("End of the program ^w^!");
    }
}
