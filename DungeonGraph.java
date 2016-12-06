import java.util.*; 
import java.io.*;

/**
 * DungeonGraph holds a respresentation of the dungeon rooms as nodes, and the exits between them as edges. 
 * 
 * @author          William (Greg) Phillips
 * @version         Zork v1.2
 */
public class DungeonGraph
{
    static DungeonGraph dungeonGraph = new DungeonGraph();
    static ArrayList<String> rooms = new ArrayList<String>(); 
    static int vertices = 0; 
    static int[][] adjMatrix;     
    
    /**
     * Constructor for objects of class DungeonGraph. Singleton class controls creation. 
     */
    private DungeonGraph()
    {
        //exists to control object creation
    }

    /**
     * Exists to control object creation. 
     * 
     * @return      DungeonGraph object. 
     */
    public static DungeonGraph instance()
    {
        return dungeonGraph; 
    }
    
    /**
     * Creates the nodes (rooms) for dungeon representation as graph. 
     */
    public void makeNodes()
    {
        GameState gs = GameState.instance();
        Dungeon d = gs.getDungeon();
        rooms.clear(); 
        for(String key: d.collection.keySet())
        {
            rooms.add(key);
            vertices ++; 
            System.out.print(key); //testing but doesn't reach
            System.out.print(vertices); //testing but doesn't reach
        }
        adjMatrix = new int[vertices][vertices];
    }
    
    /**
     * Creates edges (exits) between nodes (rooms) for representation as a graph.
     */
    public void makeEdges()
    {
        GameState gs = GameState.instance(); 
        Dungeon d = gs.getDungeon(); 
        
        for(String key: d.collection.keySet())
        {
            Room room = d.getRoom(key); 
            if(!room.roomExits.isEmpty())
            {
                for(Exit exit : room.roomExits)
                {
                    Room dest = exit.getDest();
                    String i = room.getTitle(); 
                    String j = dest.getTitle(); 
                    try
                    {
                        adjMatrix[rooms.indexOf(i)][rooms.indexOf(j)] = 1;
                    }
                    catch(ArrayIndexOutOfBoundsException index)
                    {
                        System.out.println("The vertices do not exist at make edge creation"); 
                    }
                }
            }
        }
    } 
    
    /**
     * Used for testing
     * 
     * @param to        Destination node
     * @param from      Origin node
     */
    static int getEdge(int from, int to)
    {
        try
        {
            return adjMatrix[from][to]; 
        }
        catch(ArrayIndexOutOfBoundsException index)
        {
            return 0; 
        }
    }
    
    /**
     * Used for testing
     */
    public static void main(String args[])
    {
        DungeonGraph dg = DungeonGraph.instance();
        dg.makeNodes(); 
        dg.makeEdges(); 
        
        System.out.println("The adjacency matrix for the given graph is: ");
        System.out.print("  ");
        for (int i = 0; i <= dg.vertices; i++)
        {
            System.out.print(i + " ");
            System.out.println();             
        }

        for (int i = 0; i <= dg.vertices; i++) 
        {
            System.out.print(i + " ");
            for (int j = 0; j <= dg.vertices; j++)
            {
                System.out.print(dg.getEdge(i, j) + " ");
                System.out.println();                
            } 
        }
    }
}
