/**
 * This class provides the necessary functionality to solve
 * a Latin Squares constraint satisfaction problem,
 * with the least remaining values heuristic, degree heuristic,
 * and forward checking.
 * 
 * Alain Chen
 * 
 * Created on 10/19/2016
 */
package CSP;

import java.util.ArrayList;
import java.util.Random;

public class LatinSquaresPuzzleSolver {
    private final int width;//width of the puzzle.
    private final int height;//height of the puzzle.
    private final int MAX_DIMENSION = 7+1;//Maximum width/height.
    private final Tile[][] tiles = new Tile[MAX_DIMENSION][MAX_DIMENSION];
    private final int[] solution;
    private int count;
    Random random;
    /*
        The assumption is that the width and height are 0-indexed,
        and that tiles array is tiles[xPos][yPos]
    
        so the * below is at (2, 1).
        ....
        ..*.
        ....
        ....
    */
    
    public LatinSquaresPuzzleSolver(int width, int height)
    {
        this.width = width;
        this.height = height;
        solution = new int[width];
        count = 0;
        
        for(int i=0;i<width+1;i++)
            for(int j=0;j<height+1;j++)
                tiles[i][j]=new Tile();
        
        for(int i=0;i<width;i++)
            solution[i]=-1;
        
        random = new Random();
        
    }

    //Returns the column with the minimum remaining values.
    public int heuristicMRV(boolean[][] validMoves)
    {
        int min = Integer.MAX_VALUE;
        ArrayList<Integer> columnIndexes = new ArrayList();
        for(int i=0;i<width;i++)
        {
            if(solution[i]==-1)//Only consider columns not assigned yet
            {
                int numRemaingValues = 0;
                for(int j=0;j<height;j++)
                {
                   if( validMoves[i][j] )
                       numRemaingValues++;
                }

                if(numRemaingValues < min)
                {
                    min = numRemaingValues;
                    columnIndexes.clear();
                    columnIndexes.add(i);
                }
                
                if(numRemaingValues == min)
                {
                    columnIndexes.add(i);
                }
            }
        }  
        
        //All values have been assigned.
        if(columnIndexes.isEmpty())
            return -1;
        
        //There is only one minimum, so return that index.
        if(columnIndexes.size()==1)
            return columnIndexes.get(0);
        
        //If there is a tie, use degree heuristic.
        return heuristicDegree(columnIndexes);
    }
    
    //Returns the column (of the list) with the greatest degree.
    //Since the constraint that all columns and rows can only contain
    //one colored square equally affects all columns, we do not need
    //to consider that in calculating the degree heuristic.
    //If all variables are assigned, -1 is returned.
    //Thus, we only need to consider the number of
    //numbered tiles in the current columns and neighboring columns.    
    public int heuristicDegree( ArrayList<Integer> columns )
    {
        int max = Integer.MIN_VALUE;
        ArrayList<Integer> columnIndexes = new ArrayList();
        
        //Maintain list of columns 
        //with maximum number of constraints on other rows.
        for(int i=0, n=columns.size(); i<n; i++)
        {
            int j = columns.get(i);
            
            //Gets number of constraints of the previous row,
            //this row, and the next row, since neighboring
            //tiles will have a constraint on one another.
            int num = numberTilesAffecting(j-1)+numberTilesAffecting(j)
                    +numberTilesAffecting(j+1);
            
            if(num>max)
            {
                max=num;
                columnIndexes.clear();
                columnIndexes.add(j);
            }
            
            if(num==max)
                columnIndexes.add(j);
        }
        
        //All values have been assigned.
        if(columnIndexes.isEmpty())
            return -1;
        
        //There is only one minimum, so return that index.
        if(columnIndexes.size()==1)
            return columnIndexes.get(0);
        
        //If there is a still a tie, then return a random index.
        return columnIndexes.get(random.nextInt(columnIndexes.size()));
    }
    
    //Returns the number of tiles in a column that is in a constraint
    //relation with the given column.
    public int numberTilesAffecting(int column)
    {
        //Make sure the column is within the puzzle.
        if(column<0||column>=height)
            return 0;
        
        int num = 0;
        for(int i=0;i<height;i++)
        {
            if(tiles[column][i].isNumbered()
                &&tiles[column][i].getNumNeighborsLeft()==0)
            {
                //If it is on the top or bottom, only two tiles
                //in the row of interest will be affected.
                if( i == 0 || i == height )
                    num+=2;
                else
                    num+=3;
            }
        }

        return num;
    }

    //Starting of backtrack search.
    //Returns the number of partial assignments.
    public int backTrack()
    {
        //keep track of values left that are valid.
        boolean[][] validMoves = new boolean[width+1][height+1];
        
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++)
                validMoves[i][j]=true;               
        
        removeInconsistentForwardCheck(validMoves);
        recursiveBackTracking(heuristicMRV(validMoves),validMoves);
            
        return count;
    }
    
    //Recursive step of the backtracking algorithm.
    public boolean recursiveBackTracking( int column, 
            boolean[][] validMoves ) 
    {               
        //All values has been assigned.
        if (column == -1) 
        {                 
            printPuzzle();
            return true;
        }           
        else 
        {    
            //Increment number of partial assignments.
            count++;  
            int i = 0;
            boolean solFound = false;
            
            //Go over each possible value.  
            //The HW specification does not require
            //a heuristic for selecting a value.
            while( !solFound && i < height )
            {
                if(validMoves[column][i])
                {
                    boolean[][] temp = new boolean[width+1][height+1];

                    //Copy valid moves into new temporary array
                    //that will be passed recursively.
                    for(int k=0;k<width;k++)
                        System.arraycopy(validMoves[k], 0,
                                temp[k], 0, height);

                    //Assign partial solution.
                    solution[column]=i;                                                
                    
                    //Decrement neighbors
                    if(column!=0)
                    {                      
                        if(i!=0)
                            tiles[column-1][i-1].decrementNumNeighborsLeft();
                        tiles[column-1][i+1].decrementNumNeighborsLeft();
                        tiles[column-1][i].decrementNumNeighborsLeft();
                    }
                    if(i!=0)
                    {                        
                        tiles[column][i-1].decrementNumNeighborsLeft();
                        tiles[column+1][i-1].decrementNumNeighborsLeft();
                    }
                    if(i!=height-1)
                    {
                        tiles[column][i+1].decrementNumNeighborsLeft();
                        tiles[column+1][i+1].decrementNumNeighborsLeft();
                        tiles[column+1][i].decrementNumNeighborsLeft();
                    }
                    
                    //Forwarding checking, remove inconsistent values.
                    removeInconsistentForwardCheck(temp);
                    
                    //Only continue search if there are legal values in all 
                    //domains.
                    if(!hasEmptyDomain(temp)&&!cannotPlaceNeighbors(temp))
                    {
                        solFound = 
                            recursiveBackTracking(heuristicMRV(temp), temp);
                        if(solFound)
                            return true;
                    }
                    
                    //Remove assignment.
                    solution[column]=-1;
              
                    if(column!=0)
                    {                      
                        if(i!=0)
                            tiles[column-1][i-1].incrementNumNeighborsLeft();
                        tiles[column-1][i+1].incrementNumNeighborsLeft();
                        tiles[column-1][i].incrementNumNeighborsLeft();
                    }
                    if(i!=0)
                    {                        
                        tiles[column][i-1].incrementNumNeighborsLeft();
                        tiles[column+1][i-1].incrementNumNeighborsLeft();
                    }

                    if(i!=height-1)
                    {
                        tiles[column][i+1].incrementNumNeighborsLeft();
                        tiles[column+1][i+1].incrementNumNeighborsLeft();
                        tiles[column+1][i].incrementNumNeighborsLeft();
                    }
                }
                i++;
            }
        }       
        return false;
    } 
    
    //Add a number constraint to puzzle.
    public void addNumberedTile( int xPos, int yPos, int number )
    {
        tiles[xPos][yPos].setNumNeighborsLeft(number);
    }
    
     //Returns true if one the columns has an empty domain.
    public boolean hasEmptyDomain(boolean[][] validMoves)
    {
        boolean hasValue;
        for(int i=0;i<width;i++)
        {
            hasValue=false;
            if(solution[i]!=-1)
                continue;
            
            for(int j=0;j<height;j++)
            {
                hasValue = validMoves[i][j] || hasValue;
            }
            
            if(!hasValue)
                return true;
        }  
        
        return false;
    }
    
    //Returns true if one the numbered tiles cannot be satisfied.
    public boolean cannotPlaceNeighbors(boolean[][] validMoves)
    {
        for(int i=0;i<width;i++)
        {
            for(int j=0;j<height;j++)
            {
                if(tiles[i][j].isNumbered() 
                        && tiles[i][j].getNumNeighborsLeft() > 0)
                {
                    int numValidNeighbors = 0;
                    if(i!=0)//Check for leftmost colum.
                    {
                        if(j!=0)//Check for top row.
                            if(validMoves[i-1][j-1])
                                numValidNeighbors++;
                        
                        if(validMoves[i-1][j])
                            numValidNeighbors++;
                        
                        if(j!=height-1)//Check for bottom row.
                            if(validMoves[i-1][j+1])
                                numValidNeighbors++;
                    }
                    
                    if(j!=0)//Check for top row.
                        if(validMoves[i][j-1])
                            numValidNeighbors++;
                    
                    if(j!=height-1)//Check for bottom row.
                        if(validMoves[i][j+1])
                            numValidNeighbors++;
                    
                    if(i!=height-1)//Check for rightmost colum.
                    {
                        if(j!=0)//Check for top row.
                            if(validMoves[i+1][j-1])
                                numValidNeighbors++;
                        
                        if(validMoves[i+1][j])
                            numValidNeighbors++;
                        
                        if(j!=height-1)//Check for bottom row.
                            if(validMoves[i+1][j+1])
                                numValidNeighbors++;
                    }
                        
                    //Number of neighbors left does not meet
                    //requirement of neighbors needed to be colored.
                    if(numValidNeighbors<tiles[i][j].getNumNeighborsLeft())
                        return true;                    
                }
            }
        }          
        return false;
    }
    
    //Prints the solution of the puzzle.
    public void printPuzzle()
    {
        String s;
        for(int i = 0; i < height; i++ )
        {
            for(int j = 0; j < width; j++ )
            {
                if(tiles[j][i].isNumbered())//Tile contains a number.
                    s=""+tiles[j][i].getOrigNum();
                else if( solution[j]==i )//Tiles is filled.
                    s="*";
                else//Tile is empty
                    s=".";
                
                System.out.print(s);
            }
            System.out.print("\n");
        }
    }
    
    //Removes inconsistent values.
    //This is the forwarding checking step.
    public void removeInconsistentForwardCheck(boolean[][] validMoves)
    { 
        for(int i=0;i<width;i++)
        {
            //If the current column has been assigned.
            if(solution[i]!=-1)
            {
                //Remove values that are in the same row as a colored tile.
                for(int j=0;j<width;j++)
                {
                    validMoves[j][solution[i]]=false;
                }
                
                //Remove values to the neighbors of 
                //an assigned tile.
                if(i!=0)//Checks for the leftmost column.
                { 
                    if(solution[i]!=0)//Checks for top row.
                        validMoves[i-1][solution[i]-1]=false;
                    if(solution[i]!=height-1)//Checks for bottom row.
                        validMoves[i-1][solution[i]+1]=false;
                }
                
                if(i!=width-1)//Check for rightmost column
                {
                    if(solution[i]!=0)//Checks for top row.
                        validMoves[i+1][solution[i]-1]=false;
                    if(solution[i]!=height-1)//Checks for bottom row.
                        validMoves[i+1][solution[i]+1]=false;
                }
            }
        }             
        
        for(int i = 0; i < width; i++ )
        {
            for(int j = 0; j < height; j++ )  
            {
                //Remove neighbors of a numbered tile that
                //cannot have any more neighbors.
                if(tiles[i][j].isNumbered() 
                        && tiles[i][j].getNumNeighborsLeft()==0)
                {
                    //Remove tiles that contain a number.
                    validMoves[i][j]=false;
                    
                    if( i != 0 )//Check for leftmost column
                    {
                        if( j != 0 )//Check for top row.
                            validMoves[i-1][j-1]=false;

                        validMoves[i-1][j]=false;

                        if( j != height-1 )//Check for bottom row.
                            validMoves[i-1][j+1]=false;
                    }
                    
                    if(j!=0) //Check for top row.
                        validMoves[i][j-1]=false;
                    
                    if(j!=height-1)//Check for bottom row.
                        validMoves[i][j+1]=false;

                    if( i != width-1 )//Check for rightmost column.
                    {
                        if( j != 0 )//Check for top row.
                            validMoves[i+1][j-1]=false;

                        validMoves[i+1][j]=false;

                        if( j != height-1 )//Check for bottom row.
                            validMoves[i+1][j+1]=false;
                    }
                }
            }
        }
    }
}
