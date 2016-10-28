import java.util.*; 
import java.io.*; 
import java.util.regex.*; 

/**
 * Room class contains room objects. Each room has a description, and knows if it has been visited or not. 
 * 
 * @ Author     William (Greg) Phillips
 * @ Version    Bork v3
 */
public class Room
{
    private String title = ""; 
    private String desc = ""; 
    private boolean beenHere = false; 
    ArrayList<Exit> exitPath = new ArrayList<Exit>(); 
    ArrayList<Item> roomItems = new ArrayList<Item>(); 
    ArrayList<Denizen> npcHere = new ArrayList<Denizen>(); 
    
    /**
     * Constructor for objects of class Room
     */
    public Room(String title)
    {
        this.title = title; 
    }
    
    /**
     * Constructor for objects of class Room
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
     * Returns room title
     * Public visibility
     * @return      returns Room title 
     */
    public String getTitle()
    {
        return this.title; 
    }
    
    /**
     * Sets the room's description
     * Public visibility
     */ 
    public void setDesc(String desc)
    {
        this.desc = desc; 
    }
    
    /**
     * Gets the room's description / primarily for command objects
     * Public visibility
     */ 
    public String getDesc()
    {
        return this.desc; 
    }
    
    /**
     * Attempts to move from one room to another room
     * If a direction is found in the room's arraylist of exits, then a Room objet is sent back
     * Else, null is returned
     * Package visibility
     * 
     * @param dir       direction to attempt to exit. 
     */ 
    Room leaveBy(String dir) throws InterruptedException
    {
        GameState gs = GameState.instance(); 
        for(Exit exit : gs.getAdventurersCurrentRoom().exitPath)
        {
            if(exit.getDir().equals(dir))
            {
                System.out.println("Moving to Room '" +exit.getDest().getTitle()+ "'.");
                System.out.print("."); 
                Thread.sleep(150); 
                System.out.print("."); 
                Thread.sleep(150); 
                System.out.print(".\n"); 
                Thread.sleep(150); 
                return exit.getDest(); 
            }
        }
        return null; 
    }
    
    /**
     * Describes the room
     * Package visibility
     * If room has been visited:
     * @return      this.getTitle() Only room title is printed to screen
     * If room has not been visited:
     * @return      this.title +\n+ this.desc both title and description are returned. Current room is marked as visited.
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
        for(Exit exit : gs.getAdventurersCurrentRoom().exitPath)
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
     * Adds an Exit to the current room
     * Public visibility
     * 
     * @param exit      Exit object to be added to current room.
     */ 
    public void addExit(Exit exit)
    {
        this.exitPath.add(exit); 
    }
    
    /**
     * Desc
     * Package visibility
     * 
     * @param w      PrintWriter Object
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
     * Desc
     * Package visibility
     * 
     * @param r      Scanner Object
     */ 
    void restoreState(Scanner r)
    {
        String pattern2 = "Contents:";
        Pattern p = Pattern.compile(pattern2); 
        
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
            GameState gs = GameState.instance(); 
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
        r.nextLine(); 
    }
    
    /**
     * add
     * adds an item to the roomItems array list
     */
    void add(Item item)
    {
        this.roomItems.add(item); 
    }
    
    /**
     * remove
     * removes an item from the roomItems array list
     */
    void remove(Item item)
    {
        this.roomItems.remove(item); 
    }
    
    /**
     * 
     */
    Item getItemName(String name)
    {
        return null; 
    }
    
    /**
     * 
     */
    ArrayList<Item> getContents()
    {
        return null; 
    }
    
    public boolean containsNpc()
    {
        if(this.npcHere.isEmpty())
        {
            return false; 
        }
        return true; 
    }

    /**
     * 
     */    
    void addNpc(Denizen npc)
    {
        this.npcHere.add(npc); 
    }

    /**
     * 
     */   
    void removeNpc(Denizen npc)
    {
        this.npcHere.remove(npc); 
    }
    
}
