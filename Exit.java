import java.util.*; 

/**
 * Exit Class holds onto Rooms and their exit paths. 
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class Exit
{
    private String dir = ""; 
    private Room src; 
    private Room dest; 
    private boolean locked = false;
    private String lockedObject = "";
    /*Customizations: Keys you must be in posetion of to get the locked cleared*/
    ArrayList<Item> keys = new ArrayList<Item>(); 
    private String door;
    /**
     * Constructor for objects of class Exit
     * 
     * @param dir   Direction to move as String.
     * @param src   Room source as Room object.
     * @param dest  Room destination as Room object.
     */
    public Exit(String dir, Room src, Room dest)
    {
        this.dir = dir; 
        this.src = src; 
        this.dest = dest; 
    }
    
    /**
     * Constructor for object of Class Exit.
     * 
     * @param s     Scanner object handed focus from Dungeon Class.
     * @param d     Dungeon object to work with.
     */
    public Exit(Scanner s, Dungeon d)
    {
        String pattern1 = "---"; 
        
        while(!s.hasNext(pattern1)) // does not advance token
        {
            Room tempRoom = d.getRoom(s.nextLine()); 
            String direction = s.nextLine(); 
            
            String[] parts = s.nextLine().split(":"); 
            Room tempDest = d.getRoom(parts[0]);
            
            Exit tempExit = new Exit(direction, tempRoom, tempDest); 
            /* Customizations: check if the exit is locked, if it is grab what item(s) can unlock it */
            if (parts.length > 1) 
            {
                if (parts[1].equals("locked"))
                {                    
                    tempExit.lock();
                    tempExit.lockedObject = parts[2];
                    
                    /* Now split the last part as items that are keys */
                    String[] pieces = parts[3].split(",");
                    GameState gs = GameState.instance(); 
                    for(String itemName : pieces)
                    {
                        Item item = d.getItem(itemName); 
                        tempExit.keys.add(item);
                    }
                }
            }
            
            tempRoom.addExit(tempExit); 
        }
        s.nextLine(); 
    }

    /**
     * Describes the exit.
     * 
     * @return     Exit description as a String.
     */
    String describe()
    {
        String s = "You can go '" + getDir() + "' to '" + getDest().getTitle()+ "' from '" +getSrc().getTitle()+ "'."; 
        return s; 
    }
    
    /**
     * Get the Direction of this exit object.
     * 
     * @return     returns direction to exit as a String
     */
    public Boolean isLocked()
    {
        return this.locked; 
    }
    
    /**
     * Get the Direction of this exit object.
     * 
     * @return     returns direction to exit as a String
     */
    public String getLockedObject()
    {        
        return this.lockedObject; 
    }
    
    /**
     * Get the Direction of this exit object.
     * 
     * @return     returns direction to exit as a String
     */
    public ArrayList<Item> getKeys()
    {
        return this.keys; 
    }
    
    /**
     * Get the Direction of this exit object.
     * 
     * @return     returns direction to exit as a String
     */
    public String getDir()
    {
        return this.dir; 
    }
    
    /**
     * Get the Source room FROM which to exit.
     * 
     * @return     The FROM room as a Room Object.
     */
    public Room getSrc()
    {
        return this.src; 
    }
    
    /**
     * Get the Destination room TO exit to. 
     * 
     * @return     The DESTINATION room as Room object. 
     */
    public Room getDest()
    {
        return this.dest; 
    }
    
    /**
     * Changes boolean field "locked" value to false
     * 
     */
    public void unlock()
    {
        locked = false;
    }
    /**
     * Changes boolean field "locked" value to true
     * 
     */
    public void lock()
    {
        locked = true;
    }
}
