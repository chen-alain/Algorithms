/**
 * This program implements three different levels of 
 * tic-tac-toe players using an adversarial search algorithm, Minimax.
 * 
 * Alain Chen
 * 
 * Created on 9/27/2016
 */
package AdversarialGameSearch;

public class GameSearch {

    public static void main(String[] args) {
        //Beginner vs human, Beginner goes first.
        playTicTacToe(new BeginnerTicTacToePlayer(1), 
                new HumanTicTacToePlayer());
        //Beginner vs Advanced, Beginner goes first.
        playTicTacToe(new BeginnerTicTacToePlayer(1), 
                new MinimaxTicTacToePlayer(-1, "Advanced"));
        //Beginner vs Advanced, Advanced goes first.
        playTicTacToe(new MinimaxTicTacToePlayer(1, "Advanced"), 
                new BeginnerTicTacToePlayer(-1));
        //Master vs Advanced, Advanced goes first.
        playTicTacToe(new MinimaxTicTacToePlayer(1, "Advanced"), 
                new MinimaxTicTacToePlayer(-1, "Master"));
        //Master vs Advanced, Master goes first.
        playTicTacToe(new MinimaxTicTacToePlayer(1, "Master"), 
                new MinimaxTicTacToePlayer(-1, "Advanced"));
    }        
    
    //Play the tic tac toe game. max will always go first.
    public static void playTicTacToe(TicTacToePlayer max, TicTacToePlayer min)
    {
        System.out.println("===" + max.getName() + "(X) v.s. " 
                + min.getName() + "(O)");
        Node b = new Node(0);     
        
        int currPlayer=1;
        
        int numTurns = 0;
        
        boolean successfulMove;
        
        //Play until a terminal state is reached.
        while(!b.getIsTied()&&b.winCondition()==0)
        {
            numTurns++;
            System.out.print(numTurns + ". ");
            if(currPlayer==1)
                successfulMove=b.makeMove(max.makeNextMove(b), currPlayer);
            else
                successfulMove=b.makeMove(min.makeNextMove(b), currPlayer);
            
            b.printBoard();
            System.out.print("\n");
            
            if(successfulMove)    
            {
                //It is now the other player's turn.
                currPlayer*=(-1);
            }
            else
            {
                System.out.println("INVALID MOVE -- PLACE ANOTHER MOVE.");
                numTurns--;
            }
        }
        
        String winner;
        if(currPlayer==-1)
            winner=max.getName()+"(X)";
        else
            winner=min.getName()+"(O)";
        
        if(b.getIsTied())
             System.out.println("The game is a draw!\n\n");
        else
            System.out.println(winner + " has won the game!\n\n");
    }
}
