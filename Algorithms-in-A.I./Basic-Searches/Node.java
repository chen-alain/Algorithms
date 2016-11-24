/* 
 * File:   Node.cpp
 * Author: Alain Chen
 * 
 * This class represents a node in the search tree/graph.
 * 
 * 
 * Created on September 7, 2016, 3:47 PM
 */
package BasicAISearches;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Node implements Comparable<Node>{

    private ArrayList<Node> children;   
    private int depth;    
    //True means dirty, false means clean. 
    private ArrayList<ArrayList<Boolean>> dirt; 
    private double currCost;
    private String action;
    private int dirtWidth;
    private int dirtHeight;
    private int robotPosX;
    private int robotPosY;
    private Node parent;
    private double hScore;//Heuristic score    

    public Node(ArrayList<ArrayList<Boolean>> dirt, int posX, int posY, 
            int depth, double cost, String act)
    {
        this.dirt = dirt;
        robotPosX = posX;
        robotPosY = posY;   
        this.depth = depth;
        parent = null;
        currCost = cost;
        action = act;
        dirtWidth= dirt.get(1).size();
        dirtHeight = dirt.size();
        hScore = heuristicFunction();
    } 

    //Expand node for IDS
    public void expandNode(int depthLimit, Problem p){
        if( depth >= depthLimit )
            return; 
        children = new ArrayList();

        //The order of children added is based on the rule given.
        //Go up.
        if( robotPosX > 1 )
        {         
            Node n = new Node(dirt, robotPosX-1, robotPosY, depth+1, 
                    currCost+1.3, "UP   ");
            n.parent = this;
            children.add(n);        
        }

        //Go left.
        if( robotPosY > 1 )
        {
            Node n = new Node(dirt, robotPosX, robotPosY-1, depth+1, 
                    currCost+1, "LEFT ");
            n.parent = this;
            children.add(n);        
        }

        //Suck.
        if(dirt.get(robotPosX).get(robotPosY))
        {
            ArrayList<ArrayList<Boolean>> dirtSuck = new ArrayList();
            for(int i=0;i<dirtHeight;i++)
            {
                dirtSuck.add(new ArrayList<Boolean>());
                for(int j=0;j<dirtWidth;j++)
                    dirtSuck.get(i).add(dirt.get(i).get(j));
            }
            (dirtSuck.get(robotPosX)).set(robotPosY, false);
            Node n = new Node(dirtSuck, robotPosX, robotPosY, depth+1, 
                    currCost, "SUCK ");
            n.parent = this;
            children.add(n);   
        }

        //Go right.
        if( robotPosY < dirtWidth-1 )
        {
            Node n = new Node(dirt, robotPosX, robotPosY+1, depth+1, 
                    currCost+1, "RIGHT");
            n.parent = this;
            children.add(n);    
        }

        //Go down.
        if( robotPosX < dirtHeight-1 )
        {
            Node n = new Node(dirt, robotPosX+1, robotPosY, depth+1, 
                    currCost+1.3, "DOWN ");
            n.parent = this;
            children.add(n);   
        }

    }
    
    //Expand node for DFGS.
    public void expandNode(Stack<Node> fringe,ArrayList<Node> visited)
    {
        //The order added is based on the rule given.
        //Go down.
        if( robotPosX < dirtHeight-1 )
        {
            Node n = new Node(dirt, robotPosX+1, robotPosY, 0, 
                    currCost+1.3, "DOWN ");
            n.parent = this;
            if(!visited.contains(n)&&!fringe.contains(n))
                fringe.add(n);  
        }
        
        //Go right.
        if( robotPosY < dirtWidth-1 )
        {
            Node n = new Node(dirt, robotPosX, robotPosY+1, 0, 
                    currCost+1,"RIGHT");
            n.parent = this;
            if(!visited.contains(n)&&!fringe.contains(n))
                fringe.add(n);  
        }

        //Suck.
        if(dirt.get(robotPosX).get(robotPosY))
        {
            ArrayList<ArrayList<Boolean>> dirtSuck = new ArrayList();
            for(int i=0;i<dirtHeight;i++)
            {
                dirtSuck.add(new ArrayList<Boolean>());
                for(int j=0;j<dirtWidth;j++)
                    dirtSuck.get(i).add(dirt.get(i).get(j));
            }
            (dirtSuck.get(robotPosX)).set(robotPosY, false);
            Node n = new Node(dirtSuck, robotPosX, robotPosY,0,
                    currCost, "SUCK ");
            n.parent = this;
            if(!visited.contains(n)&&!fringe.contains(n))
                fringe.add(n);   
        }

        //Go left.
        if( robotPosY > 1 )
        {
            Node n = new Node(dirt, robotPosX, robotPosY-1,0,
                    currCost+1,"LEFT ");
            n.parent = this;
            if(!visited.contains(n)&&!fringe.contains(n))
                fringe.add(n);       
        }
        
        //Go up.
        if( robotPosX > 1 )
        {         
            Node n = new Node(dirt, robotPosX-1, robotPosY,0,
                    currCost+1.3,"UP   ");
            n.parent = this;
            if(!visited.contains(n)&&!fringe.contains(n))
                fringe.add(n);        
        }
    }
    
    //Expand node for A*
    public void expandNode(PriorityQueue<Node> fringe)
    {
        //The order added is based on the rule given.
        //Go down.
        if( robotPosX < dirtHeight-1 )
        {
            Node n = new Node(dirt, robotPosX+1, robotPosY, 0, 
                    currCost+1.3, "DOWN ");
            n.parent = this;
            fringe.add(n);
        }     
        
        //Go right.
        if( robotPosY < dirtWidth-1 )
        {
            Node n = new Node(dirt, robotPosX, robotPosY+1, 0, 
                    currCost+1,"RIGHT");
            n.parent = this;
            fringe.add(n);
        }

        //Suck.
        if(dirt.get(robotPosX).get(robotPosY))
        {
            ArrayList<ArrayList<Boolean>> dirtSuck = new ArrayList();
            for(int i=0;i<dirtHeight;i++)
            {
                dirtSuck.add(new ArrayList<Boolean>());
                for(int j=0;j<dirtWidth;j++)
                    dirtSuck.get(i).add(dirt.get(i).get(j));
            }
            (dirtSuck.get(robotPosX)).set(robotPosY, false);
            Node n = new Node(dirtSuck, robotPosX, robotPosY,0,
                    currCost, "SUCK ");
            n.parent = this;
            fringe.add(n);
        }

        //Go left.
        if( robotPosY > 1 )
        {
            Node n = new Node(dirt, robotPosX, robotPosY-1,0,
                    currCost+1,"LEFT ");
            n.parent = this; 
            fringe.add(n);
        }
        
        //Go up.
        if( robotPosX > 1 )
        {         
            Node n = new Node(dirt, robotPosX-1, robotPosY,0,
                    currCost+1.3,"UP   ");
            n.parent = this;
                fringe.add(n);        
        }
        
        //Limit memory.
        int memorySize = 320000;
        if(fringe.size()>memorySize)
        {
            PriorityQueue<Node> temp=new PriorityQueue();
            for(int i=0;i<memorySize-5;i++)
            {
                temp.add(fringe.poll());
            }
            fringe=temp;
        }
    }
    
    private double fScore()
    {
        return currCost + hScore;
    }
    
    //The heuristic score is the distance between 
    //the leftmost position of dirt and rightmost position of dirt
    //plus the distance between the topmost and bottommost position of dirt
    //plus the minimum distance the robot needs 
    //to travel to one of those spots,
    //all of those multiplied by their step costs.
    //This heuristic is admissable as the robot needs to travel
    //at least that distance to clean up all the dirt.
    private double heuristicFunction()
    {
        int leftmost=robotPosY,rightmost=robotPosY,
                topmost=robotPosX,bottommost=robotPosX;
        for( int i = 1, n = dirtHeight; i < n; i++ )
            for( int j = 1, k = dirtWidth; j < k; j++ )
                if( dirt.get(i).get(j) )
                {
                    if(i<topmost)
                        topmost=i;
                    if(i>bottommost)
                        bottommost=i;
                    if(j<leftmost)
                        leftmost=j;
                    if(j>rightmost)
                        rightmost=j;                        
                }
        
        return 1.3*(Math.abs(topmost-bottommost)+
                Math.min(Math.abs(topmost-robotPosX), 
                        Math.abs(robotPosX-bottommost)))
                +Math.abs(leftmost-rightmost)
                +Math.min(Math.abs(leftmost-robotPosX), 
                        Math.abs(robotPosX-rightmost));
    }

    //Print all ancestors leading to this node.
    public void printPath(){

        Stack<Node> path = new Stack();
        Node traverse = this;
        while(traverse != null)
        {
            path.push(traverse);
            traverse = traverse.parent;
        }

        System.out.println( "Path: ");
        int counter = 0;
        while( !path.empty() )
        {
            //Only print ten actions in each line.
            counter=(counter+1)%10;
            Node top = path.pop();
            System.out.print( top.action +  ", " );       
            if(counter%10==0)
                System.out.print("\n");
        }
        System.out.print( "GOAL\n" );
        System.out.printf( "Path-Cost: %.1f\n", currCost);
    }

    //A goal state is if all tiles are clean.
    public Boolean goalTest()
    {
        for( int i = 1, n = dirtHeight; i < n; i++ )
            for( int j = 1, k = dirtWidth; j < k; j++ )
                if( dirt.get(i).get(j) )
                    return false;

        return true;
    }

    //Print robot location and dirt distribution of this node.
    public void printNode()
    {
        System.out.print("Robot at ("+robotPosX+","
                +robotPosY+") with dirty nodes ");
        for( int i = 1, n = dirtHeight; i < n; i++ )
            for( int j = 1, k = dirtWidth; j < k; j++ )
                if( dirt.get(i).get(j) )
                    System.out.print("("+i+","+j+") ");
        System.out.println();
    }
    
    public int getDepth()
    {
        return depth;
    }

    public ArrayList<Node> getChildren()
    {
        return children;
    }
    
    @Override
    //Two states are equal if the robot location and dirt distributions
    //are equal. This is used for the contains() for ArrayList.
    public boolean equals(Object o)
    {
        if(!( o instanceof Node) )
            return false;
        
        Node n = (Node) o;
        if(!dirt.equals(n.dirt))
            return false;
        if(robotPosX!=n.robotPosX)
            return false;
        if(robotPosY!=n.robotPosY)
            return false;
        return true;
    }
    
    @Override
    public int compareTo(Node o)
    {
        return (int)( 10*(fScore()-o.fScore()));
    }

}
