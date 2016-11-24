/**
 * This class implements a minimax tic tac toe player, which includes 
 * Advanced and Master levels.
 * 
 * These players will look 2 (advanced) or 4 (master) moves ahead
 * and makes a decision based on a node's minimax value.
 * 
 * Alain Chen
 * 
 * Created on 9/27/2016
 */
package AdversarialGameSearch;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author L
 */
public class MinimaxTicTacToePlayer implements TicTacToePlayer {
    
    //Minimax value of all successor nodes.
    private ArrayList<Integer> minimaxValue;
    private int nodesExpanded;
    private long time;
    private final int isMax;
    private final String name;
    private final int ply;//number of moves that will be looked ahead.
    private final Random random;
    
    public MinimaxTicTacToePlayer(int isMax, String name)
    {
        this.isMax = isMax;
        this.name = name;
        if(name.equals("Advanced"))
            ply=2;//Advanced level
        else
            ply=4;//For master level.
        nodesExpanded=0;
        random = new Random();
    }
     
    @Override
    //Returns the location of the square that will be placed.
    //This function is the minimax decision function.
    public Point makeNextMove(Node board)
    {               
        minimaxValue= new ArrayList();
        time = System.nanoTime();
        nodesExpanded=0;
        int max = maxValue(board);
        
        int numMaxAppears=0;//How many times the best evaluation occurs.
        
        for(int j=0, n=minimaxValue.size();j<n;j++)
        {
            if(minimaxValue.get(j)==max)
                numMaxAppears++;
        }
        
        //Generate a random number between 1 and count.
        //Randomize node selection.
        int numSelected = random.nextInt(numMaxAppears)+1;
        
        //Return the random numSelected-to-last appearance of the max.
        for(int i = 1; i<numSelected;i++ )
            minimaxValue.remove((int)minimaxValue.lastIndexOf(max));       
               
        System.out.print(name + " has expanded " + nodesExpanded + 
                " nodes in " );
        System.out.printf("Execution Time: %.2f ms\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
                
        return board.emptySpaces().get(minimaxValue.lastIndexOf(max));
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    //h value for a cutoff node.
    public int eval(Node board)
    {        
        //Terminal nodes.
        if(board.winCondition()==isMax)
            return Integer.MAX_VALUE;
        if(board.winCondition()==isMax*(-1))
            return Integer.MIN_VALUE;
        if(board.getIsTied())
            return 0;
        
        // h(n) = # open two in row for me - # open two in row for opponent
        return board.numOpenTwo(isMax).size()
                - board.numOpenTwo(isMax*(-1)).size();
    }
    
    //Min value function for the minimax decision
    public int minValue(Node board)
    {        
        nodesExpanded++;
        if(board.getDepth()>=ply)
        {
            return(eval(board));
        }
        
        //Terminal nodes
        if(board.winCondition()==isMax)
            return (Integer.MAX_VALUE);
        
        if(board.winCondition()==isMax*(-1))
            return (Integer.MIN_VALUE);
        
        if(board.getIsTied())
           return 0;
    
        //Successors.
        board.generateChildren(isMax*(-1));
        
        //Find the minimum of the maximum values of its successors.
        int min = Integer.MAX_VALUE;
        for(int i=0, n=board.children.size();i<n;i++)
        {
            int e = maxValue(board.children.get(i));
            if(e<min)
                min=e;
            
            if(board.getDepth()==0)
                minimaxValue.add(e);
        }

        return min;
    }
    
    //Max value function for the minimax decision
    public int maxValue(Node board)
    {        
        nodesExpanded++;
        if(board.getDepth()>=ply)
        {
            return(eval(board));
        }
        
        //Terminal nodes
        if(board.winCondition()==isMax)
            return (Integer.MAX_VALUE);
        
        if(board.winCondition()==isMax*(-1))
            return (Integer.MIN_VALUE);
        
        if(board.getIsTied())
           return 0;
        
        //Successors.
        board.generateChildren(isMax);
        
        
        //Find the maximum of the minimum values of its successors.
        int max = Integer.MIN_VALUE;
        for(int i=0, n=board.children.size();i<n;i++)
        {
            int e = minValue(board.children.get(i));
            if(e>max)
                max=e;
            
            if(board.getDepth()==0)
                minimaxValue.add(e);
        }

        return max;
    }   
}
