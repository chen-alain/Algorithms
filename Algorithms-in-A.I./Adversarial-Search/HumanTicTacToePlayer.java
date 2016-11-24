/**
 * This class implements the human tic tac toe player.
 * 
 * Alain Chen
 * 
 * Created on 9/27/2016
 */
package AdversarialGameSearch;
import java.util.Scanner;

public class HumanTicTacToePlayer implements TicTacToePlayer {
    
    private final Scanner inputScanner;
    public HumanTicTacToePlayer()
    {
        inputScanner = new Scanner(System.in);
    } 
    
    @Override
    //Returns the location of the square that will be placed.
    public Point makeNextMove(Node board)
    {    
        System.out.println("Human makes a move.");
        //Read the input from the console. The human should type in two
        //integers between 1 and 4, separated by a space.
        int a=inputScanner.nextInt(), b=inputScanner.nextInt();
        System.out.print("\n");
        return new Point(a, b);
    }
    
    @Override
    public String getName()
    {
        return "Human";
    }
}
