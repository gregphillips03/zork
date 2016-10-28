import java.util.*; 
import java.io.*;

/**
 * Dungeon 
 * 
 * Container for rooms that includes a key searchable hashtable. 
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
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
     */
    public Dungeon(Room entry, String name)
    {
        this.name = name; 
        this.entry = entry; 
    }
    
    /**
     * Constructor for objects of class Dungeon
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
                Denizen npc = new Denizen(scan, this); 
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
     * 
     * Public visibility
     * 
     * @return this.entry returns entry room. 
     */
    public Room getEntry()
    {
        return this.entry; 
    }
    
    /**
     * Setter method for Dungeon entry
     * Public visibility
     * 
     */
    public void setEntry(Room rm)
    {
        this.entry = rm;  
    }    
    
    /**
     * Getter method for Dungeon name
     * Public visibility
     * 
     * @return this.name - returns name of the dungeon
     */
    public String getName()
    {
        return this.name; 
    }    
    
    /**
     * Searches hashtable by room title
     * Public visibility
     * 
     * @parameter roomTitle - title of room to look up in hashtable 
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
     * Adds a Room object to the hashtable
     * Public visibility
     * 
     * @parameter room - room object to be added to hashtable
     */
    public static void add(Room room)
    {
        collection.put(room.getTitle(), room);
        //System.out.println("Added Room: " + room.getTitle()); 
    }
    
    /**
     * Desc
     * 
     * Package Visibility 
     * 
     * @parameter w - PrintWriter object
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
     * Desc
     * 
     * Package Visibility 
     * 
     * @parameter r - Scanner object
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
     *
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
     *
     */
    public void addItem(Item item)
    {
        itemsInDungeon.put(item.getPrimaryName(), item); 
        //System.out.println("Added: '" +item.getPrimaryName()+ "' to Dungeon.");
    }
    
    /**
     *
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
     *
     */    
    public void addNpc(Denizen npc)
    {
        npcInDungeon.put(npc.getName(), npc); 
    }
}
