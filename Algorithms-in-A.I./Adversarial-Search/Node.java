/**
 * This class represents the state of a board.
 * 
 * Alain Chen
 * 
 * Created on 9/27/2016
 */
package AdversarialGameSearch;

import java.util.ArrayList;

public class Node {
    
    /*
        We set the boardSize to 5x5 so we don't have to deal with 0s.
        1 means player1 has placed its mark there,
        0 means no one has placed at the position,
        -1 means player2 has placed its mark there.
    */
    private int[][] boardState;
    private int depth;
    public ArrayList<Node> children;
    
    public Node(int depth)
    {
        children=null;
        boardState = new int[5][5];
        
        //Clear board state.
        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                boardState[i][j]=0;
        this.depth=depth;       
    }
    
    public Node(Node b)
    {
        boardState = new int[5][5];
        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                boardState[i][j]=b.boardState[i][j];
        depth=b.depth+1;//increase depth
        children=b.children;
    }
    
    //Player=1 is Max, player=-1 if min.
    //returns whether move is successful or not.
    public boolean makeMove(Point p, int player)
    {
        //Out of bounds.
        if( p.x < 1 )
            return false;
        if( p.y < 1 )
            return false;
        if( p.x > 4 )
            return false;
        if( p.y > 4 )
            return false;
        
        if(boardState[p.x][p.y]==0)
        {
            boardState[p.x][p.y]=player;
            return true;
        }       
        return false;
    }
    
    //Returns 1 if player1 has won, 
    //0 if no one has won, -1 if player2 has won.
    public int winCondition()
    {
        int sum;
        //Check for row win conditions.
        for(int i=1;i<=4;i++)
        {
            sum=boardState[i][1]+boardState[i][2]+boardState[i][3];
            if(Math.abs(sum)>=3)
                return (int)Math.signum(sum);//Return the sign of the sum.
            
            sum=boardState[i][2]+boardState[i][3]+boardState[i][4];
            if(Math.abs(sum)>=3)
                return (int)Math.signum(sum);//Return the sign of the sum.                
        }
        
        //Check for column win conditions.
        for(int i=1;i<=4;i++)
        {
            sum=boardState[1][i]+boardState[2][i]+boardState[3][i];
            if(Math.abs(sum)>=3)
                return (int)Math.signum(sum);//Return the sign of the sum.
            
            sum=boardState[2][i]+boardState[3][i]+boardState[4][i];
            if(Math.abs(sum)>=3)
                return (int)Math.signum(sum);//Return the sign of the sum.               
        }
        
        //Check for diagonal win conditions, from topleft to bottomright.
        for(int i=1;i<=2;i++)
        {
            for(int j=1;j<=2;j++)
            {
                sum=boardState[i][j]+boardState[i+1][j+1]
                        +boardState[i+2][j+2];
                if(Math.abs(sum)>=3)
                    return (int)Math.signum(sum);//Return the sign of the sum.
            }                
        }
        
        //Check for diagonal win conditions, from topright to bottomleft.
        for(int i=1;i<=2;i++)
        {
            for(int j=3;j<=4;j++)
            {
                sum=boardState[i][j]+boardState[i+1][j-1]
                        +boardState[i+2][j-2];
                if(Math.abs(sum)>=3)
                    return (int)Math.signum(sum);//Return the sign of the sum.
            }                
        }
        //No one has won
        return 0;
    }
    
    //Returns a list of points containing possible moves that will 
    //make three in a row, which is the same as
    //number of two in rows for the given player.
    public ArrayList<Point> numOpenTwo(int player)
    {
        ArrayList<Point> list = new ArrayList();
        //Check for open two's in the same row.
        for(int i=1;i<=4;i++)
        {
            for(int j=1;j<=2;j++)
            {
                if( boardState[i][j]==0 && boardState[i][j+1]==player
                        && boardState[i][j+2]==player)
                    list.add(new Point(i,j));
                
                if( boardState[i][2+j]==0 && boardState[i][j]==player
                    && boardState[i][j+1]==player)
                    list.add(new Point(i,2+j));
            }           
        }
        
        //Check for open two's in the same column.
        for(int i=1;i<=4;i++)
        {
            for(int j=1;j<=2;j++)
            {
                if( boardState[j][i]==0 && boardState[j+1][i]==player
                        && boardState[j+2][i]==player)
                    list.add(new Point(j,i));
                
                 if( boardState[2+j][i]==0 && boardState[j][i]==player
                    && boardState[1+j][i]==player)
                    list.add(new Point(2+j,i));
            }          
        }
        
        //Check for open two's in the diagonals, topleft to bottomright.
        for(int i=1;i<=2;i++)
        {
            for(int j=1;j<=2;j++)
            {
                if( boardState[i][j]==0 && boardState[i+1][j+1]==player
                        && boardState[i+2][j+2]==player)
                    list.add(new Point(i,j));
                
                if( boardState[i+2][j+2]==0 && boardState[i+1][j+1]==player
                    && boardState[i][j]==player)
                    list.add(new Point(i+2,j+2));
            }
        }
        
        //Check for open two's in the diagonals, topright to bottomleft.
        for(int i=3;i<=4;i++)
        {
            for(int j=1;j<=2;j++)
            {
                if( boardState[j][i]==0 && boardState[j+1][i-1]==player
                        && boardState[j+2][i-2]==player)
                    list.add(new Point(j,i));
                
                if( boardState[i][j]==0 && boardState[i-1][j+1]==player
                    && boardState[i-2][j+2]==player)
                list.add(new Point(i,j));
            }                        
        }
        return list;
    }
    
    //Print out the state of the board
    public void printBoard()
    {
        char temp;
        System.out.println(" 1234");
        for(int i=1;i<=4;i++)
        {
            System.out.print(i);
            for(int j=1;j<=4;j++)
            {
                switch(boardState[i][j])
                {
                    case 1:
                        temp='X';
                        break;                    
                    case -1:
                        temp = 'O';
                        break;
                    case 0:
                    default:
                        temp=' ';
                        break;                        
                }
                System.out.print(temp);
            }
            System.out.print("\n");
        }
    }
    
    //Returns all possible moves, which are the empty spaces.
    public ArrayList<Point> emptySpaces()
    {
        ArrayList<Point> list = new ArrayList();
        for(int i=1;i<=4;i++)
        {
            for(int j=1;j<=4;j++)
            {
                if(boardState[j][i]==0)
                {
                    list.add(new Point(j,i));
                }
            }
        }
        return list;
    }
    
    //Return the first open space, which is used in the beginner player.
    //Row has a higher priority than column.
    public Point firstOpenSpace()
    {
        for(int i=1;i<=4;i++)
        {
            for(int j=1;j<=4;j++)
            {
                if(boardState[j][i]==0)
                {
                    return new Point(j,i);
                }
            }
        }
        return null;
    }
    
    public boolean getIsTied()
    {
        return firstOpenSpace()==null;
    }
    
    public int getDepth()
    {
        return depth;
    }
    
    //Generate all possible successor nodes.
    public void generateChildren(int isMax)
    {
        children=new ArrayList();
        ArrayList<Point> empty = emptySpaces();
        for( int i=0,n=empty.size();i<n;i++)
        {
            children.add(new Node(this));
            children.get(i).makeMove(empty.get(i),isMax);
        }        
    }
}
