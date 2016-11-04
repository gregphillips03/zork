import java.util.*; 
import java.io.*; 

/**
 * GameState Holds the current state of the game
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */

class GameState
{
    static Room currentRoom; 
    static Dungeon currentDungeon; 
    static GameState gameState = new GameState(); 
    static ArrayList<Item> carriedItems = new ArrayList<Item>(); 
    
    /**
     * Constructor for objects of class GameState
     * Singleton class controls creation
     */
    private GameState()
    {
        //exists to control object creation
    }
    
    /**
     * instance
     * Exists to control object creation
     * 
     * @return      returns state of game as in instace of the object
     */
    public static GameState instance()
    {
        return gameState; 
    }
    
    /**
     * initialize
     * Places user in current dungeon and sets their entry room
     * 
     * @param dungeon       Dungeon object to work with
     */
    public void initialize(Dungeon dungeon)
    {
        currentDungeon = dungeon; 
        setAdventurersCurrentRoom(dungeon.getEntry()); 
    }

    /**
     * getDungeon
     * Getter method to return the player's current Dungeon
     * 
     * @return      returns the current dungeon as a Dungeon object.
     */
    public Dungeon getDungeon()
    {
        return currentDungeon; 
    }
    
    /**
     * setAdventurersCurrentRoom
     * Setter method to set the player's current location
     * 
     * @param room      Room location of current player as a room object
     */
    public void setAdventurersCurrentRoom(Room room)
    {
       currentRoom = room; 
    }
    
    /**
     * getAdventurersCurrenRoom
     * Returns the player's current room location
     * 
     * @return      returns the current room as a room object
     */
    public Room getAdventurersCurrentRoom()
    {
        return currentRoom; 
    }
    
    /**
     * store
     * Persistence method to write information to save file
     * 
     * @param saveName      name file to write to / focus handed from SaveCommand Class
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
        pw.write("\n"); 
        pw.write("===\n"); 
        pw.write("NPC States:\n");
        Set<String> keys = gs.getDungeon().collection.keySet();
        for(String key : keys)
        {
            Room room = gs.getDungeon().getRoom(key); 
            if(!room.npcHere.isEmpty())
            {
               for(Denizen den : room.npcHere)
               {
                  den.storeState(pw); 
               }
            }
        }   
        pw.write("===\n"); 
        pw.close(); 
    }    

    /**
     * restore
     * Hydration method to restore game state
     * 
     * @param filename      name of file to restore from / handed focus from Interpreter Class
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
        if(!scan.hasNext(pattern2))
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
        scan.nextLine(); // advances token
        while(!scan.nextLine().equals("NPC States:"))
        {
            scan.nextLine(); 
        } 
        
        while(!scan.hasNext(pattern2))
        {
            String s = scan.nextLine(); 
            Denizen den = gs.getDungeon().getNPC(s); 
            den.restoreState(scan, den); 
        }
    }
    
    /**
     * addToInventory
     * Adds item to user's inventory
     * 
     * @param item      Item to add to inventory ArrayList as Item object
     */
    void addToInventory(Item item)
    {
        carriedItems.add(item); 
    }
    
    /**
     * removeFromInventory
     * Removes item from user's inventory
     * 
     * @param item      Item to remove from user's inventory ArrayList as Item object
     */
    void removeFromInventory(Item item)
    {
        carriedItems.remove(item); 
    }
    
    /**
     * getItemInVicinityNamed
     * Searches room for item of specified name
     * 
     * @param name      Primary name of item to search for
     * @return item     Returns Item object if item if found in room
     * @return null     Returns null if Item cannot be found
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
     * getItemFromInventory
     * Searches user inventory for item of specified name
     * 
     * @param name      Primary name of item to search for
     * @return item     Returns Item object if item if found in room
     * @return null     Returns null if Item cannot be found
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
