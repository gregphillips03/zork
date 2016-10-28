import java.util.*; 
import java.io.*; 

/**
 * GameState Holds the current state of the game
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
 */

class GameState
{
    static Room currentRoom; 
    static Dungeon currentDungeon; 
    static GameState gameState = new GameState(); 
    static ArrayList<Item> carriedItems = new ArrayList<Item>(); 
    
    /**
     * Constructor for objects of class GameState
     */
    private GameState()
    {
        //exists to control object creation
    }
    
    /**
     * Exists to control object creation
     */
    public static GameState instance()
    {
        return gameState; 
    }
    
    /**
     * 
     */
    public void initialize(Dungeon dungeon)
    {
        currentDungeon = dungeon; 
        setAdventurersCurrentRoom(dungeon.getEntry()); 
    }

    /**
     * Returns the player's current Dungeon
     * 
     * @return      returns the current Dungeon object.
     */
    public Dungeon getDungeon()
    {
        return currentDungeon; 
    }
    
    /**
     * Sets the player's current location
     * 
     * @param room      Room location of current player.
     */
    public void setAdventurersCurrentRoom(Room room)
    {
       currentRoom = room; 
    }
    
    /**
     * Returns the player's current room location
     * 
     * @return      returns the currentRoom object.
     */
    public Room getAdventurersCurrentRoom()
    {
        return currentRoom; 
    }
    
    /**
     *
     */
    public void store(String saveName) throws FileNotFoundException
    {
        String pattern3 = "Bork v3.0 save data"; 
        GameState gs = GameState.instance(); 
        PrintWriter pw = new PrintWriter(saveName); 
        
        pw.write(pattern3 + "\n"); 
        gs.getDungeon().storeState(pw);
        // all that follows if after the pw comes back from writing dungeon and room stuff
        pw.write("===\n"); 
        pw.write("Adventurer:\n"); 
        pw.write("Current room: " + gs.getAdventurersCurrentRoom().getTitle());
        if(!carriedItems.isEmpty())
        {
            String s = ""; 
            String r = ""; 
            pw.write("\n"); 
            pw.write("Inventory: "); 
            for(Item item : gs.carriedItems)
            {
                s = s + item.getPrimaryName() + ","; 
            }
            r = s.substring(0, s.lastIndexOf(",")); 
            pw.write(r); 
        }
        pw.close(); 
    }    

    /**
     *
     */
    public void restore(String filename) throws FileNotFoundException
    {
        String pattern2 = "==="; 
        GameState gs = GameState.instance();         
        Scanner scan = new Scanner(new File(filename)); 

        while(!scan.nextLine().equals("Room states:"))
        {
            scan.nextLine(); 
        } 
        
        while(!scan.hasNext(pattern2))
        {
            String s = scan.nextLine(); 
            String sub = ":"; 
            String[] parts = s.split(sub); 
            String roomText = parts[0]; 
            gs.getDungeon().getRoom(roomText).restoreState(scan); 
        }
        scan.nextLine(); // advances token
        while(!scan.nextLine().equals("Adventurer:"))
        {
            scan.nextLine(); 
        }
        gs.getDungeon().restoreState(scan); 
        if(scan.hasNextLine())
        {
            String sub = scan.nextLine(); 
            String [] parts = sub.split(" "); 
            String sub2 = parts[1]; 
            String [] pieces = sub2.split(","); 
            for(String string : pieces)
            {
                Item item = getDungeon().getItem(string); 
                carriedItems.add(item); 
            }               
        }
    }
    
    /**
     *
     */
    ArrayList<String> getInventoryNames()
    {
        return null; 
    } 
    
    /**
     *
     */
    void addToInventory(Item item)
    {
        carriedItems.add(item); 
    }
    
    /**
     *
     */
    void removeFromInventory(Item item)
    {
        carriedItems.remove(item); 
    }
    
    /**
     *
     */
    Item getItemInVicinityNamed(String name)
    {
        GameState gs = GameState.instance(); 
        Room room = gs.getAdventurersCurrentRoom(); 
        Dungeon d = gs.getDungeon(); 
        Item item = d.getItem(name); 
        if(room.roomItems.contains(item))
        {
            return item; 
        }
        return null; 
    }
    
    /**
     *
     */
    Item getItemFromInventory(String name)
    {
        GameState gs = GameState.instance(); 
        Dungeon d = gs.getDungeon(); 
        Item item = d.getItem(name); 
        if(carriedItems.contains(item))
        {
            return item; 
        }
        return null; 
    }
    
}
