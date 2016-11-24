/**
 * This class represents each tile in the puzzle.
 * 
 * Alain Chen
 * 
 * Created on 10/19/2016
 */
package CSP;

public class Tile {
    
    private int numNeighborsLeft;
    private int originalNumber;
    private boolean isNumbered;
    public Tile()
    {
        numNeighborsLeft = 10;//No number is assigned.
        isNumbered = false;
    }
    
    public void setNumNeighborsLeft(int n)
    {
        numNeighborsLeft = n;
        originalNumber = n;
        isNumbered = true;
    }
    
    public void decrementNumNeighborsLeft()
    {
        numNeighborsLeft--;
    }
    
    public void incrementNumNeighborsLeft()
    {
        numNeighborsLeft++;
    }
    
    public int getNumNeighborsLeft()
    {
        return numNeighborsLeft;
    }
    
    public boolean isNumbered()
    {
        return isNumbered;
    }
    
    //Returns the original assigned number
    //For printing the puzzle.
    public int getOrigNum()
    {
        return originalNumber;
    }    
}
