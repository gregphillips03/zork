import java.util.*; 
import java.io.*; 
import java.util.regex.*; 

/**
 * Room Class contains room objects. Each room has a description, and knows if it has been visited or not. Each room can contain items and NPCs.
 * 
 * @author     William (Greg) Phillips
 * @version    Zork v1
 */
public class Room
{
    private String title = ""; 
    private String desc = ""; 
    private boolean beenHere = false; 
    ArrayList<Exit> roomExits = new ArrayList<Exit>(); 
    ArrayList<Item> roomItems = new ArrayList<Item>(); 
    ArrayList<Denizen> npcHere = new ArrayList<Denizen>(); 
    
    public static class ExitIsLockedException extends Exception {
        public ExitIsLockedException(String e) {
            super(e);
        }
    }

    /**
     * Constructor for objects of class Room
     * 
     * @param title     Title of the Room object as a String.
     */
    public Room(String title)
    {
        this.title = title; 
    }
    
    /**
     * Constructor for objects of class Room
     * 
     * @param s          Scanner object to work with / handed focus from Dungeon Class.
     * @param d          Dungeon object to work with.
     * @param initState  Tells room whether to add Items to Room object's ArrayList, or to hold off until hydration from save file.
     */
    public Room(Scanner s, Dungeon d, boolean initState)
    {      
        String pattern1 = "---"; 
        String pattern2 = "Contents:";
        Pattern p = Pattern.compile(pattern2);  
        this.title = s.nextLine(); 
        while(!s.hasNext(pattern1)) // this does not advance the token
        {
            if(s.hasNext(p) && initState)
            {
                String sub = s.nextLine(); 
                String [] parts = sub.split(" "); 
                String sub2 = parts[1]; 
                String [] pieces = sub2.split(","); 
                for(String string : pieces)
                {
                    Item item = d.getItem(string); 
                    this.add(item); 
                }
            }
            else if (s.hasNext(p) && !initState)
            {
                s.nextLine(); 
            } 
            this.desc = this.desc + "\n" + s.nextLine(); 
        }
        s.nextLine(); //advances token
    }
    
    /**
     * Returns room title.
     * 
     * @return      Returns Room title as a String.
     */
    public String getTitle()
    {
        return this.title; 
    }
    
    /**
     * Sets the room's description.
     * 
     * @param desc      Description of Room object as a String.
     */ 
    public void setDesc(String desc)
    {
        this.desc = desc; 
    }
    
    /**
     * Gets the room's description.
     * 
     * @return      Room object's description as a String.
     */ 
    public String getDesc()
    {
        return this.desc; 
    }
    
    /**
     * Attempts to move from one room to another room. If a direction is found in the room's arraylist of exits, then a Room objet is sent back. Else, null is returned
     * 
     * @param dir                       Direction to attempt to exit
     * @return                          Destination to exit to as a Room object. Null if there isn't an exit in the specified direction
     * @throws InterruptedException     Pushes IO exception up the stack
     */ 
    Room leaveBy(String dir) throws InterruptedException, ExitIsLockedException
    {
        GameState gs = GameState.instance(); 
        for(Exit exit : gs.getAdventurersCurrentRoom().roomExits)
        {
            if(exit.getDir().equals(dir))
            {
                if (exit.isLocked())
                {                    
                    String items = "";
                    for(Item item : exit.getKeys())
                    {
                        items = item.getPrimaryName(); 
                    }
                    throw new ExitIsLockedException("This exit is locked, you need to use a '" + items + "' to unlock it.");                    
                }                
                else
                {
                    //If we are accessing a room through a locked object, close the door behind us!!! 
                    /*if (exit.getLockedObject().equals("door"))
                    {
                        exit.lock();
                    }*/
                    
                    System.out.println("Moving to Room '" +exit.getDest().getTitle()+ "'.");
                    System.out.print("."); 
                    Thread.sleep(150); 
                    System.out.print("."); 
                    Thread.sleep(150); 
                    System.out.print(".\n"); 
                    Thread.sleep(150); 
                }
                return exit.getDest(); 
            }
        }
        return null; 
    }

    
    /**
     * Describes the room.
     * 
     * @return      Only room title is printed to screen, if room has been visited. If room has not been visited, both title and description are returned. Current room is marked as visited.
     */ 
    String describe()
    {
        if(this.beenHere)
        {
            return "\nYou are now in room, '" + this.getTitle() + "'.\n"; 
        }
        this.beenHere = true;
        String s = ""; 
        String r = "";
        String d = ""; 
        GameState gs = GameState.instance(); 
        for(Exit exit : gs.getAdventurersCurrentRoom().roomExits)
        {
           s = s + exit.describe() + "\n"; 
        }
        for(Item item : gs.getAdventurersCurrentRoom().roomItems)
        {
           r = r + "There is a '" + item.getPrimaryName() + "' here." + "\n"; 
        }
        for(Denizen den : gs.getAdventurersCurrentRoom().npcHere)
        {
           d = d + "There is a '" + den.getName() + "' here." + "\n"; 
        }
        return ("You are now in room, '" + this.getTitle() + "'.\n" + "Description:" + this.desc + "\n" + s + r + d); 
    }
    
    /**
     * Adds an Exit to the current room.
     * 
     * @param exit      Exit object to be added to current room.
     */ 
    public void addExit(Exit exit)
    {
        this.roomExits.add(exit); 
    }
    
    /**
     * Persistence method to write to save file. Currently checks if the room has been visited, if there are items in the room, and if there are NPCs in the room
     * 
     * @param w      PrintWriter object to work with / handed focus from Dungeon Class.
     */ 
    void storeState(PrintWriter w)
    {
        if(this.beenHere && this.roomItems.isEmpty() && this.npcHere.isEmpty()) //have been here and there are no items or npc
        {
            w.write(this.getTitle() + ":\n"); 
            w.write("beenHere=true\n"); 
            w.write("---\n");           
        }
        else if (!this.beenHere && !this.roomItems.isEmpty() && this.npcHere.isEmpty()) // have NOT been here and does have items but not NPCs
        {
            String s = ""; 
            w.write(this.getTitle() + ":\n"); 
            w.write("beenHere=false\n"); 
            w.write("Contents: "); 
            for(Item item : this.roomItems)
            {
                s = s + item.getPrimaryName() + ","; 
            }
            String r = s.substring(0, s.lastIndexOf(",")); 
            w.write(r + "\n"); 
            w.write("---\n"); 
        }
        else if (this.beenHere && !this.roomItems.isEmpty() && this.npcHere.isEmpty()) // have been here and has items and does not have NPC
        {
            String s = ""; 
            w.write(this.getTitle() + ":\n"); 
            w.write("beenHere=true\n"); 
            w.write("Contents: "); 
            for(Item item : this.roomItems)
            {
                s = s + item.getPrimaryName() + ","; 
            }
            String r = s.substring(0, s.lastIndexOf(",")); 
            w.write(r + "\n"); 
            w.write("---\n");        
        }
        else if (this.beenHere && this.roomItems.isEmpty() && !this.npcHere.isEmpty()) // have been here and does NOT have items but has NPC
        {
            String s = ""; 
            w.write(this.getTitle() + ":\n"); 
            w.write("beenHere=true\n");
            w.write("NPCs: "); 
            for(Denizen npc : this.npcHere)
            {
                s = s + npc.getName() + ","; 
            }
            String r = s.substring(0, s.lastIndexOf(",")); 
            w.write(r + "\n"); 
            w.write("---\n"); 
        }
        else if(!this.beenHere && !this.roomItems.isEmpty() && !this.npcHere.isEmpty()) // have NOT been here and does have items and NPCs
        {
            String s = ""; 
            String x = ""; 
            w.write(this.getTitle() + ":\n"); 
            w.write("beenHere=false\n"); 
            w.write("Contents: "); 
            for(Item item : this.roomItems)
            {
                s = s + item.getPrimaryName() + ","; 
            }
            String r = s.substring(0, s.lastIndexOf(",")); 
            w.write(r + "\n");
            w.write("NPCs: "); 
            for(Denizen npc : this.npcHere)
            {
                x = x + npc.getName() + ","; 
            }
            String z = x.substring(0, x.lastIndexOf(",")); 
            w.write(z + "\n"); 
            w.write("---\n");         
        }
        else if(!this.beenHere && this.roomItems.isEmpty() && !this.npcHere.isEmpty()) // have not been here, no items, but does have NPC
        {
            String s = ""; 
            w.write(this.getTitle() + ":\n"); 
            w.write("beenHere=false\n");
            w.write("NPCs: "); 
            for(Denizen npc : this.npcHere)
            {
                s = s + npc.getName() + ","; 
            }
            String r = s.substring(0, s.lastIndexOf(",")); 
            w.write(r + "\n"); 
            w.write("---\n");             
        }
        else if(this.beenHere && !this.roomItems.isEmpty() && !this.npcHere.isEmpty()) //visited, has items, has npcs
        {
            String s = ""; 
            String x = ""; 
            w.write(this.getTitle() + ":\n"); 
            w.write("beenHere=true\n"); 
            w.write("Contents: "); 
            for(Item item : this.roomItems)
            {
                s = s + item.getPrimaryName() + ","; 
            }
            String r = s.substring(0, s.lastIndexOf(",")); 
            w.write(r + "\n");
            w.write("NPCs: "); 
            for(Denizen npc : this.npcHere)
            {
                x = x + npc.getName() + ","; 
            }
            String z = x.substring(0, x.lastIndexOf(",")); 
            w.write(z + "\n"); 
            w.write("---\n");          
        }
    }
    
    /**
     * Hydration method to restore game from save file. 
     * 
     * @param r      Scanner Object to work with / handed focus from GameState Class.
     */ 
    void restoreState(Scanner r)
    {
        String pattern2 = "Contents:";
        String pattern3 = "NPCs:"; 
        Pattern p = Pattern.compile(pattern2);
        Pattern pp = Pattern.compile(pattern3); 
        GameState gs = GameState.instance(); 
        
        if(r.nextLine().equals("beenHere=true"))
        {
            this.beenHere = true;
            //System.out.println("Updated Room: " + this.getTitle() + "."); 
        }
        else
        {
            this.beenHere = false; 
        }
        if(r.hasNext(p))
        {
            String sub = r.nextLine(); 
            String [] parts = sub.split(" "); 
            String sub2 = parts[1]; 
            String [] pieces = sub2.split(","); 
            for(String string : pieces)
            {
                Item item = gs.getDungeon().getItem(string); 
                if(item != null) // this handles the damn burrito
                {
                    this.add(item); 
                }
            }            
        }
        if(r.hasNext(pp))
        {
            String sub = r.nextLine();
            String [] parts = sub.split(" "); 
            String sub2 = parts[1]; 
            String [] pieces = sub2.split(","); 
            for(String string : pieces)
            {
                Denizen den = gs.getDungeon().getNPC(string); 
                if(den != null)
                {
                    this.addNpc(den); 
                    den.setRoom(this); 
                }
            }
        }
        r.nextLine(); 
    }
    
    /**
     * Adds an item to the roomItems array list.
     * 
     * @param item      Item object to add to Room's ArrayList as a Item Object.
     */
    void add(Item item)
    {
        this.roomItems.add(item); 
    }
    
    /**
     * Removes an item from the roomItems array list.
     * 
     * @param item      Item object to remove from Room's ArrayList as a Item Object.
     */
    void remove(Item item)
    {
        this.roomItems.remove(item); 
    }
    
    /**
     * 
     */
    public ArrayList<Item> getRoomItems()
    {
        return this.roomItems; 
    }

    /**
     * Checks to see if there is a NPC object in the room.
     * 
     * @return      False if no NPC in the Room's ArrayList. True if NPC found in room.
     */
    public boolean containsNpc()
    {
        if(this.npcHere.isEmpty())
        {
            return false; 
        }
        return true; 
    }

    /**
     * Add Denizen object to this Room's ArrayList.
     * 
     * @param npc       Denizen object to add to ArrayList as Denizen object.
     */    
    void addNpc(Denizen npc)
    {
        this.npcHere.add(npc); 
    }

    /**
     * Remove Denizen object from this Room's ArrayList
     * 
     * @param npc       Denizen object to remove from Room's ArrayList as Denizen Object. 
     */   
    void removeNpc(Denizen npc)
    {
        this.npcHere.remove(npc); 
    }
    
}
