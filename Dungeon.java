import java.util.*; 
import java.io.*;

/**
 * Dungeon 
 * 
 * Container for rooms that includes a key searchable hashtable. Dungeon knows what items are in it and what NPCs are in it. 
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class Dungeon
{
    private String name = ""; 
    private Room entry;
    private String filename = "";     
    static Hashtable<String, Room> collection = new Hashtable<String, Room>();
    static Hashtable<String, Item> itemsInDungeon = new Hashtable<String, Item>(); 
    static Hashtable<String, Denizen> npcInDungeon = new Hashtable<String, Denizen>(); 
    
    /**
     * Constructor for objects of class Dungeon
     * 
     * @param entry     Adventurers room entry as a Room object
     * @param name      Dungeon's name as a String
     */
    public Dungeon(Room entry, String name)
    {
        this.name = name; 
        this.entry = entry; 
    }
    
    /**
     * Constructor for objects of class Dungeon
     * 
     * @param filename      Filename to read dungeon file from
     * @param initState     Tells the dungeon's subsequent components if they should hydrate at creation or what for informaiton from save file
     */
    public Dungeon(String filename, boolean initState) throws FileNotFoundException, InterruptedException
    {
        this.filename = filename; 
        boolean b = initState; 
        String pattern1 = "---"; 
        String pattern2 = "==="; 
        String pattern3 = "Bork v3.0"; 
        
        try
        {
            Scanner scan = new Scanner(new File(filename)); 
            this.name = scan.nextLine();
            if(!scan.nextLine().equals(pattern3))
            {
                String s = "+++This data file is incompatible with the current version of Bork+++"; 
                KillGame.gameKill(s); 
            } 
            do
            {
                scan.nextLine(); 
            }
            while(!scan.nextLine().equals("Items:")); 
            while(!scan.hasNext(pattern2))
            {
                Item item = new Item(scan); 
                this.addItem(item); 
            }           
            do
            {
                scan.nextLine(); 
            }
            while(!scan.nextLine().equals("Rooms:"));  
            Room r = new Room(scan, this, b);  
            this.add(r); 
            this.setEntry(r);         
            while(!scan.hasNext(pattern2))
            {
                Room rm = new Room(scan, this, b); 
                this.add(rm); 
            }
            
            do
            {
                scan.nextLine(); 
            }
            while(!scan.nextLine().equals("Exits:")); 
            
            while(!scan.hasNext(pattern2))
            {
                Exit xt = new Exit(scan, this); 
            }
            
            do
            {
                scan.nextLine(); 
            }
            while(!scan.nextLine().equals("NPC:")); 
            
            while(!scan.hasNext(pattern2))
            {
                Denizen npc = new Denizen(scan, this, b); 
                this.addNpc(npc); 
                //System.out.println("Added '" + npc.getName() + "' to dungeon list."); 
            }

         }
        catch(FileNotFoundException e)
        {
            System.out.println("+++Caught FileNotFoundException+++"); 
            String s =         "+++Data file not located in current data loom+++ \n" +
                               "+++Verify noosphere link+++ \n"; 
            KillGame.gameKill(s); 
        } 
    }
    

    /**
     * getEntry
     * Getter method for Dungeon's entry room / Public visibility
     * 
     * @return this.entry   Returns entry room as a Room object
     */
    public Room getEntry()
    {
        return this.entry; 
    }
    
    /**
     * setEntry
     * Setter method for Dungeon entry / Public visibility
     * 
     * @param rm    Room object to set as the Dungeon's entry room
     */
    public void setEntry(Room rm)
    {
        this.entry = rm;  
    }    
    
    /**
     * getName
     * Getter method for Dungeon name / Public visibility
     * 
     * @return this.name    Returns name of the dungeon as a String
     */
    public String getName()
    {
        return this.name; 
    }    
    
    /**
     * getRoom
     * Searches hashtable by room title as key / Public visibility
     * 
     * @param roomTitle     Title of room to look up in hashtable 
     * @return collection.get(roomTitle)    Returns Room object if found
     * @return null                         Returns null if object is not found
     */
    public static Room getRoom(String roomTitle)
    {
        if(collection.containsKey(roomTitle))
        //Checks to see if roomTitle is a key in the hashtable
        {
            //System.out.println("Found Room : '" + roomTitle + "'."); 
            return collection.get(roomTitle);
            //Gets the room object from the hashtable 
        }
        System.out.println(roomTitle + " not found.\n");
        return null; 
    }    
    
    /**
     * add
     * Adds a Room object to the hashtable / Public visibility
     * 
     * @param room      Room object to be added to hashtable as Room object
     */
    public static void add(Room room)
    {
        collection.put(room.getTitle(), room);
        //System.out.println("Added Room: " + room.getTitle()); 
    }
    
    /**
     * storeState
     * Persistence method to store dungeon information in save file / Package Visibility 
     * 
     * @param w     PrintWriter object handed focus by GameState Class
     */
    void storeState(PrintWriter w)
    {
        GameState gs = GameState.instance(); 
        w.write("Dungeon file: /" + gs.getDungeon().filename + "\n"); 
        w.write("Room states:\n"); 
        for(String key: gs.getDungeon().collection.keySet())
        {
            Room rm = gs.getDungeon().getRoom(key);
            rm.storeState(w); 
        }
    }
    
    /**
     * restoreState
     * Hydration method to read information from save file / Package Visibility 
     * 
     * @param r     Scanner object handed focus from Interpreter Class
     */
    void restoreState(Scanner r)
    {
        GameState gs = GameState.instance(); 
        String cr = r.nextLine();
        int index = cr.lastIndexOf(":"); 
        String currentRoom = cr.substring(index +2);
        Room tempRoom = getRoom(currentRoom);
        gs.setAdventurersCurrentRoom(tempRoom); 
    }
    
    /**
     * getItem
     * Searches for item in dungeon's known items
     *
     * @param primaryName                        Primary name of item to search for as key to hashtable
     * @return itemsInDungeon.get(primaryName)   Returns item as Item object
     * @return null                              Returns null if item not found in dungeon
     */
    public Item getItem(String primaryName)
    {
        if(itemsInDungeon.containsKey(primaryName))
        {
            return itemsInDungeon.get(primaryName);
        }
        //System.out.println("Item '" + primaryName + "' not found."); 
        return null; 
    }
    
    /**
     * addItem
     * Add's item to the dungeon's hashtable
     * 
     * @param item      Item object to add to dungeon
     */
    public void addItem(Item item)
    {
        itemsInDungeon.put(item.getPrimaryName(), item); 
        //System.out.println("Added: '" +item.getPrimaryName()+ "' to Dungeon.");
    }
    
    /**
     * getNPC
     * Searches for NPC in dungeon's known Denizen objects
     *
     * @param npcName                        Name of NPC to search for
     * @return npcInDungeon.get(npcName)     Returns NPC as a Denizen object if found
     * @return null                          Returns null of NPC is not in dungeon
     */    
    public Denizen getNPC(String npcName)
    {
        if(npcInDungeon.containsKey(npcName))
        {
            return npcInDungeon.get(npcName);
        }
        return null; 
    }
    
    /**
     * addNpc
     * Add's NPC as Denizen object to dungeon's hashtable
     *
     * @param npc    Denizen object to add to dungeon's hashtable
     */    
    public void addNpc(Denizen npc)
    {
        npcInDungeon.put(npc.getName(), npc); 
    }
}
