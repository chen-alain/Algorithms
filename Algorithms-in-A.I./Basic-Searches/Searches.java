package BasicAISearches;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
/* 
 * File:   main.cpp
 * Author: Alain Chen
 * 
 * This program implements 
 * a) iterative deepening tree search
 * b) depth-first graph search
 * and c) A*
 * for a vacuum cleaner world.
 * 
 * Created on September 7, 2016, 3:47 PM
 */
enum ProblemState { SOL_FOUND, FAILURE, CUTOFF };
public class Searches {

    private static long nodesExpanded=0;
    private static long time;//Timer to keep track of program run time.
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        
        time = System.nanoTime();
        System.out.println("DFGS with 4x4");        
        depthFirstGraphSearch(Problem.FOUR_BY_FOUR);
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
        
        time = System.nanoTime();
        System.out.println("DFGS with 5x6");        
        depthFirstGraphSearch(Problem.FIVE_BY_SIX);
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
        
        time = System.nanoTime();
        System.out.println("A* with 4x4");        
        aStarSearch(Problem.FOUR_BY_FOUR);
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
        
        time = System.nanoTime();
        System.out.println("A* with 5x6");        
        aStarSearch(Problem.FIVE_BY_SIX);
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
        
        nodesExpanded = 0;
        time = System.nanoTime();
        System.out.println("IDS with 4x4");        
        iterativeDeepeningTreeSearch(Problem.FOUR_BY_FOUR);
        System.out.println("Nodes expanded in one hour: " + nodesExpanded );
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
        
        nodesExpanded = 0;
        time = System.nanoTime();
        System.out.println("IDS with 5x6");        
        iterativeDeepeningTreeSearch(Problem.FIVE_BY_SIX);
        System.out.println("Nodes expanded in one hour: " + nodesExpanded );
        System.out.printf("Execution Time: %.2f ms\n\n\n", 
                (double)(System.nanoTime() - time)/1000000 );
    }   
    
    //Performs an aStarSearch with heuristics
    public static ProblemState aStarSearch(Problem p)
    {
        System.out.println("First 10 nodes expanded:");
        nodesExpanded = 0;
        
        Node root;
        ArrayList<ArrayList<Boolean>> dirtDistribution;   
        if( p == Problem.FOUR_BY_FOUR )   
            dirtDistribution=generateFourByFour();
        else
            dirtDistribution=generateFiveBySix();

        //Initialize root node.
        root = new Node(dirtDistribution,3,2,0,0,"START");
        PriorityQueue<Node> fringe = new PriorityQueue();
        fringe.add(root);
        
        while(!fringe.isEmpty())
        {
            Node node = fringe.poll();
            
            
            if(nodesExpanded<10)
            {                
                System.out.print((nodesExpanded+1)+". ");
                node.printNode();
            }
            nodesExpanded++;
            
            //Check for goal test.
            if(node.goalTest())
            {
                node.printPath();
                return ProblemState.SOL_FOUND;
            }
            
            //Expand node.
            node.expandNode(fringe);           
        }
        return ProblemState.FAILURE;
    }

    public static ProblemState recursiveDLS(Node node, Problem p, int limit)
    {
        //Timeout if solution not found after an hour.
        if((System.nanoTime()-time)/1000000000>60*60)
        {                        
            return ProblemState.FAILURE;
        }
        boolean reachedCutoff = false;
        int lastIndex;
        Node n;
        //Check whether goal test is reached.
        if( node.goalTest() ) 
        {
            node.printPath();
            return ProblemState.SOL_FOUND;
        }
        //Check if depth limit is reached.
        else if( node.getDepth() == limit )
        {
            return ProblemState.CUTOFF;
        }
        else
        {
            //Expand nodes.
            node.expandNode(limit,p);           
            
            if(nodesExpanded<10)
            {             
                System.out.print((nodesExpanded+1)+". ");
                node.printNode();
            }   
            nodesExpanded++;
            
            while(!node.getChildren().isEmpty())
            {
                //System.out.println(limit);
                lastIndex=node.getChildren().size()-1;
                n=node.getChildren().get(lastIndex);
                node.getChildren().remove(n);
                ProblemState result = recursiveDLS(n, p, limit);
                //Remove node to converse memory.
                if( result == ProblemState.CUTOFF )
                    reachedCutoff = true;
                else if( result != ProblemState.FAILURE )
                    return result;
            }
        }

        if( reachedCutoff )
            return ProblemState.CUTOFF;

        return ProblemState.FAILURE;
    }

    public static ProblemState depthLimitedSearch(Problem p, int limit)
    {
        Node n;
        ArrayList<ArrayList<Boolean>> dirtDistribution;   
        if( p == Problem.FOUR_BY_FOUR )   
            dirtDistribution=generateFourByFour();
        else
            dirtDistribution=generateFiveBySix();

        n = new Node(dirtDistribution,3,2,0,0, "START");//Make node.
        ProblemState s = recursiveDLS(n,p,limit);
        return s;
    }

    public static ProblemState iterativeDeepeningTreeSearch(Problem p)
    {       
        for( int depth = 0; depth < 10000; depth++ )
        {
            System.out.println("At depth " + depth);
            ProblemState s = depthLimitedSearch(p, depth);
            if( s != ProblemState.CUTOFF )
                return s;
        }
        return ProblemState.FAILURE;
    }
    
    public static ProblemState depthFirstGraphSearch(Problem p)
    {
        System.out.println("First 10 nodes expanded:");
        nodesExpanded = 0;
        
        Node root;
        ArrayList<ArrayList<Boolean>> dirtDistribution;   
        ArrayList<Node> visited = new ArrayList();
        if( p == Problem.FOUR_BY_FOUR )   
            dirtDistribution=generateFourByFour();
        else
            dirtDistribution=generateFiveBySix();

        //Initialize root node.
        root = new Node(dirtDistribution,3,2,0,0,"START");
        Stack<Node> fringe = new Stack();
        fringe.push(root);
        
        while(!fringe.empty())
        {
            Node node = fringe.pop();
            
            if(nodesExpanded<10)
            {
                nodesExpanded++;
                System.out.print(nodesExpanded+". ");
                node.printNode();
            }
            
            //Check for goal test.
            if(node.goalTest())
            {
                node.printPath();
                return ProblemState.SOL_FOUND;
            }
            
            //Expand node.
            node.expandNode(fringe, visited);
            
            visited.add(node);
            
        }
        return ProblemState.FAILURE;
    }
    
     public static ArrayList<ArrayList<Boolean>> generateFourByFour()
    {
        ArrayList<ArrayList<Boolean>> dirtDistribution = new ArrayList();
        //I am increasing the dimensions of the rooms by 1 
        //so I do not need to deal with 0s.
        for( int i=0; i < 5; i++ )
        {
            dirtDistribution.add(new ArrayList(5));
            for( int j=0; j < 5; j++ )
                dirtDistribution.get(i).add(false);
        }

        dirtDistribution.get(1).set(2,true);
        dirtDistribution.get(1).set(4,true);
        dirtDistribution.get(2).set(2,true);
        dirtDistribution.get(2).set(3,true);
        dirtDistribution.get(3).set(1,true);
        dirtDistribution.get(4).set(2,true);
        dirtDistribution.get(4).set(4,true);
        return dirtDistribution;
    }

    public static ArrayList<ArrayList<Boolean>> generateFiveBySix()
    {
        ArrayList<ArrayList<Boolean>> dirtDistribution= new ArrayList();
        //I am increasing the dimensions of the rooms by 1 
        //so I do not need to deal with 0s.
        for( int i=0; i < 6; i++ )
        {
            dirtDistribution.add(new ArrayList(7));
            for( int j=0; j < 7; j++ )
                 dirtDistribution.get(i).add(false);
        }

        dirtDistribution.get(1).set(2,true);
        dirtDistribution.get(1).set(4,true);
        dirtDistribution.get(1).set(6,true);
        dirtDistribution.get(2).set(1,true);
        dirtDistribution.get(2).set(3,true);
        dirtDistribution.get(2).set(4,true);
        dirtDistribution.get(3).set(1,true);
        dirtDistribution.get(3).set(5,true);
        dirtDistribution.get(3).set(6,true);
        dirtDistribution.get(4).set(2,true);
        dirtDistribution.get(4).set(4,true);
        dirtDistribution.get(5).set(3,true);
        dirtDistribution.get(5).set(4,true);
        dirtDistribution.get(5).set(6,true);
        return dirtDistribution;
    }
}
