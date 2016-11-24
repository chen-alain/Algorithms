/**
 * This program solves Latin Square Puzzles.
 * 
 * Alain Chen
 * 
 * Created on 10/19/2016
 */
package CSP;

public class Main {

    private static long time;//Timer to keep track of program run time.
    
    public static void main(String[] args) {
        
        //example();
        instance1();
        instance2();
        instance3();
        instance4();
    }
    
    public static void example()
    {
        time = System.nanoTime();
        System.out.println("Example");  
        LatinSquaresPuzzleSolver lsps =  new LatinSquaresPuzzleSolver(5,5);
        lsps.addNumberedTile(1, 4, 1);
        lsps.addNumberedTile(3, 0, 2);
        
        int count = lsps.backTrack();        
        System.out.println("Partial Assignments: " + count );
        
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
        System.out.print("\n\n\n");
        
    }
    
    public static void instance1()
    {
        time = System.nanoTime();
        System.out.println("Instance 1: ");  
        
        LatinSquaresPuzzleSolver lsps =  new LatinSquaresPuzzleSolver(5,5);
        lsps.addNumberedTile(0, 4, 0);
        lsps.addNumberedTile(2, 3, 1);
        
        int count = lsps.backTrack();        
        System.out.println("Partial Assignments: " + count );
        
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
        System.out.print("\n\n\n");
    }
    
    public static void instance2()
    {
        time = System.nanoTime();
        System.out.println("Instance 2: ");  
        
        LatinSquaresPuzzleSolver lsps =  new LatinSquaresPuzzleSolver(6,6);
        lsps.addNumberedTile(1, 5, 0);
        lsps.addNumberedTile(3, 4, 2);
        
        int count = lsps.backTrack();        
        System.out.println("Partial Assignments: " + count );
        
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
        System.out.print("\n\n\n");
    }
    
    public static void instance3()
    {
        time = System.nanoTime();
        System.out.println("Instance 3: ");  
        
        LatinSquaresPuzzleSolver lsps =  new LatinSquaresPuzzleSolver(6,6);
        lsps.addNumberedTile(0, 5, 1);
        lsps.addNumberedTile(3, 2, 1);
        lsps.addNumberedTile(5, 3, 2);
        
        int count = lsps.backTrack();        
        System.out.println("Partial Assignments: " + count );
        
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
        System.out.print("\n\n\n");
    }
    
    public static void instance4()
    {
        time = System.nanoTime();
        System.out.println("Instance 4: ");  
        
        LatinSquaresPuzzleSolver lsps =  new LatinSquaresPuzzleSolver(7,7);
        lsps.addNumberedTile(0, 6, 1);
        lsps.addNumberedTile(3, 5, 0);
        lsps.addNumberedTile(4, 0, 2);
        
        int count = lsps.backTrack();        
        System.out.println("Partial Assignments: " + count );
        
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
        System.out.print("\n\n\n");
    }
    
}
