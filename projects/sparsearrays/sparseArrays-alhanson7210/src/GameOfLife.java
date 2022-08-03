import sparseArray.*;

public class GameOfLife {
    public static void main(String[] args) {
        if(args.length < 1 || args.length > 1)
            System.exit(1);
        //filename
        System.out.print(args[0]);
        //initialize the sparse array
        MySparseArray CurrentGeneration = new MySparseArray(0);
        //read nodes into the first generation
        CurrentGeneration.readFromFile(args[0]);
        //get two boards back with the current number of neighbors and the next generation
        for (int i = 0; i < 4; i++) {
            CurrentGeneration.printSparseArray();
            CurrentGeneration = CurrentGeneration.gameOfLife();
        }
        CurrentGeneration.printToFile("../../files/outputFile.txt");
    }
}
