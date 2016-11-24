/**
 * This interface provides a uniform interface
 * for all tic tac toe players.
 * 
 * Alain Chen
 * 
 * Created on 9/27/2016
 */
package AdversarialGameSearch;

public interface TicTacToePlayer {       
    public Point makeNextMove(Node board);
    public String getName();
}
