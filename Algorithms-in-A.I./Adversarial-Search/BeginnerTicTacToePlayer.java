/**
 * This class implements the beginner tic tac toe player.
 * 
 * Beginner will place its marks sequentially in a blank square
 * in increasing order of row number and then column number
 * unless it or its opponent will win via an open two-in-a-row.
 * 
 * Alain Chen
 * 
 * Created on 9/27/2016
 */
package AdversarialGameSearch;

import java.util.ArrayList;

public class BeginnerTicTacToePlayer implements TicTacToePlayer {
    
    private final int isMax;
    public BeginnerTicTacToePlayer(int isMax)
    {
        this.isMax = isMax;
    }
    
    @Override
    //Returns the location of the square that will be placed.
    public Point makeNextMove(Node board)
    {
        System.out.print("Beginner makes a move.\n\n");
        
        //If there is a move that allows this player to win via 2-in-row
        ArrayList<Point> list = board.numOpenTwo(this.isMax);
        if( !list.isEmpty() )
            return list.get(0);
        
        //If there is a move that allows the other player to win via 2-in-row.
        list.clear();
        list = board.numOpenTwo(this.isMax*(-1));
        if( !list.isEmpty() )
            return list.get(0);
        
        return board.firstOpenSpace();
    }

    @Override
    public String getName()
    {
        return "Beginner";
    }
}
