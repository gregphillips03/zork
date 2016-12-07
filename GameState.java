import java.util.*; 
import java.io.*; 

/**
 * GameState Holds the current state of the game. 
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1.2
 */

class GameState
{
    static double playerHealth = 25; 
    static int playerScore = 0; 
    static Room currentRoom;
    static Room entryRoom; 
    static Dungeon currentDungeon; 
    static GameState gameState = new GameState(); 
    static ArrayList<Item> carriedItems = new ArrayList<Item>();
    static String gameType = "";
    static String saveFile = "";
    /**
     * Constructor for objects of class GameState. Singleton class controls creation.
     */
    private GameState()
    {
        //exists to control object creation
    }
    
    /**
     * Exists to control object creation. 
     * 
     * @return      State of game as in instace of the object.
     */
    public static GameState instance()
    {
        return gameState; 
    }
    
    /**
     * Places user in current dungeon and sets their entry room.
     * 
     * @param dungeon       Dungeon object to work with.
     */
    public void initialize(Dungeon dungeon)
    {
        currentDungeon = dungeon; 
        setAdventurersCurrentRoom(dungeon.getEntry()); 
    }

    /**
     * Getter method to return the player's current Dungeon.
     * 
     * @return      Current dungeon as a Dungeon object.
     */
    public Dungeon getDungeon()
    {
        return currentDungeon; 
    }
    
    /**
     * Setter method to set the player's current location.
     * 
     * @param room      Room location of current player as a room object.
     */
    public void setAdventurersCurrentRoom(Room room)
    {
       currentRoom = room; 
    }
    
    /**
     * Returns the player's current room location.
     * 
     * @return      Current room as a room object.
     */
    public Room getAdventurersCurrentRoom()
    {
        return currentRoom; 
    }
    
    /**
     * Persistence method to write information to save file.
     * 
     * @param saveName      Name file to write to / focus handed from SaveCommand Class.
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     */
    public void store(String saveName) throws FileNotFoundException
    {
        String pattern3 = "Group Bork v1.0 save data"; 
        GameState gs = GameState.instance(); 
        PrintWriter pw = new PrintWriter(saveName); 
        
        pw.write(pattern3 + "\n"); 
        gs.getDungeon().storeState(pw);
        // all that follows if after the pw comes back from writing dungeon and room stuff
        pw.write("===\n"); 
        pw.write("Adventurer:\n"); 
        pw.write("Current room: " + gs.getAdventurersCurrentRoom().getTitle() + "\n");
        pw.write("Health: " + gs.getHealth() + "\n"); 
        pw.write("Score: " + gs.getScore()); 
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
        pw.write("Holoplinth State:\n"); 
        HoloPlinth hp = HoloPlinth.instance(); 
        hp.storeState(pw); 
        pw.write("===\n");
        pw.write("DataSlate State:\n"); 
        DataSlate ds = DataSlate.instance(); 
        ds.storeState(pw); 
        pw.write("===\n"); 
        pw.close(); 
    }    

    /**
     * Hydration method to restore game state.
     * 
     * @param filename      Name of file to restore from / handed focus from Interpreter Class.
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
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
        scan.nextLine(); 
        while(!scan.nextLine().equals("Holoplinth State:"))
        {
            scan.nextLine(); 
        }
        HoloPlinth hp = HoloPlinth.instance(); 
        hp.restoreState(scan); 
        scan.nextLine(); 
        while(!scan.nextLine().equals("DataSlate State:"))
        {
            scan.nextLine(); 
        }
        DataSlate ds = DataSlate.instance(); 
        ds.restoreState(scan); 
    }
    
    /**
     * Adds item to user's inventory.
     * 
     * @param item      Item to add to inventory ArrayList as Item object.
     */
    void addToInventory(Item item)
    {
        carriedItems.add(item); 
    }
    
    /**
     * Removes item from user's inventory.
     * 
     * @param item      Item to remove from user's inventory ArrayList as Item object.
     */
    void removeFromInventory(Item item)
    {
        carriedItems.remove(item); 
    }
    
    /**
     * Getter method for player's score
     * 
     * @return      Player's score as integer
     */
    int getScore()
    {
        return playerScore; 
    }
    
    /**
     * Setter method for player's score
     * 
     * @param i     Integer to add to score
     */
    void setScore(int i)
    {
        playerScore = playerScore + i; 
    }
    
    /**
     * Getter method for player's health
     * 
     * @return      Player's health as integer
     */    
    double getHealth()
    {
        return playerHealth; 
    }
    
    /**
     * Setter method for player's health
     * 
     * @param i     Integer to subtract from health (is positive) or add to health (if negative)
     */
    void setHealth(double i)
    {
        playerHealth = playerHealth - i;
    }
    
    
    /**
     * Searches room for item of specified name.
     * 
     * @param name      Primary name of item to search for.
     * @return          Item object of item if found in room. Null if Item cannot be found
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
     * Searches user inventory for item of specified name.
     * 
     * @param name      Primary name of item to search for
     * @return          Item object if found in inventory. Null if Item cannot be found.
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
