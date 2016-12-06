import java.util.*; 
import java.io.*; 
import java.util.regex.*; 

/**
 * The Denizen class relates to the dungeon's NPCs. It creates the Denizens themselves and moves around according to predefined rules for each NPC.
 * 
 * @author William (Greg) Phillips
 * @version Zork v1
 */
public class Denizen
{
    private Room currentRoom; 
    private String name = ""; 
    private String desc = ""; 
    private int health = 0; 
    private boolean isMobile = false; 
    private boolean isAngered = false; 
    ArrayList<Item> carriedItems = new ArrayList<Item>(); 
    
    /**
     * Constructor for Denizen objects
     * 
     * @param name      Name of NPC.
     * @param desc      Description of NPC.
     * @param health    Hit points of NPC.
     * @param mobile    Whether or not the NPC can move.
     */
    
    public Denizen(String name, String desc, int health, boolean mobile)
    {
        this.name = name; 
        this.desc = desc; 
        this.health = health; 
        this.isMobile = mobile; 
    }
    
    /**
     * Constructor for Denizen objects
     * 
     * @param s             Scanner object for hydration.
     * @param d             Dungeon object for hydration.
     * @param initState     Initialization parameter. Tells constructor whether or not to set NPC current room and add object or avoid duplicaton of objects in dungeon.
     */
    public Denizen(Scanner s, Dungeon d, boolean initState)
    {
        String pattern1 = "---"; 
        String pattern2 = "Carries:"; 
        Pattern p = Pattern.compile(pattern2); 
        
        this.name = s.nextLine();
        String subRoom = s.nextLine();
        Room tempRoom = d.getRoom(subRoom);   
        if(initState)
        {
            this.currentRoom = tempRoom; 
        }
        this.health = s.nextInt();
        s.nextLine(); //needed to advance token past npc's health int
        if(s.nextLine().equals("mobile"))
        {
            this.isMobile = true; 
        }else
        {
            this.isMobile = false; 
        }
        if(s.nextLine().equals("angry"))
        {
            this.isAngered = true; 
        }
        else
        {
            this.isAngered = false; 
        }
        while(!s.hasNext(pattern1)) // does not advance token
        {
            if(s.hasNext(p))
            {
                String sub = s.nextLine(); 
                String [] parts = sub.split(" "); 
                String sub2 = parts[1]; 
                String [] pieces = sub2.split(",");
                for(String string : pieces)
                {
                    Item item = d.getItem(string); 
                    if(initState)
                    {
                        this.add(item);
                    }
                    //System.out.println("Added " +item.getPrimaryName()+ " to " + this.getName() + " inventory."); 
                }
            }
            this.desc = this.desc + "\n" + s.nextLine(); 
        }
        if(initState)
        {
            tempRoom.addNpc(this); 
        }
        //System.out.println("Added " +this.name+ " to room " +tempRoom.getTitle() + ".");
        //System.out.println("Mobile: " + this.isMobile); 
        //System.out.println("Angry: " + this.isAngered);
        //System.out.println("Desc: " + this.desc); 
        s.nextLine(); //advances token
    }
    
    /**
     * Getter method for NPC name
     * 
     * @return       Name of NPC as string.
     */
    public String getName()
    {
        return this.name; 
    }
    /*
     * Checks to see if the Denizen has a Weapon. Used for later Combat.
     * Can be expanded with switch to ArrayList
     */
    public boolean hasWeapon(){
        for(Item i : carriedItems){
            if(i.getPrimaryName().equals("boltpistol") || i.getPrimaryName().equals("chainsword")){
                return true;
            }
        }
        return false;
    }
    /**
     * Getter method for NPC description
     *  
     * @return      Description of NPC as string.
     */
    public String getDesc()
    {
        return this.desc; 
    }
    
    /**
     * Getter method for NPC hit points
     * 
     * @return      Current hit points of NPC as integer.
     */
    public int getHealth()
    {
        return this.health; 
    }
    
    /**
     * Getter method for NPC mobility
     * 
     * @return      NPC object's mobility as boolean.
     */
    public boolean getMobility()
    {
        return this.isMobile; 
    }
    
    /**
     * Setter method for NPC mobility
     * 
     * @param mobile     Boolean value to set NPC's mobility to
     */
    public void setMobile(boolean mobile)
    {
        this.isMobile = mobile; 
    }
    
    /**
     * Getter method for NPC mood
     * 
     * @return      NPC object's mood as boolean. 
     */
    public boolean getMood()
    {
        return this.isAngered; 
    }
    
    /**
     * Setter method for NPC mood
     * 
     * @param mood      Boolean value to set NPC's mood to
     */
    public void setMood(boolean mood)
    {
        this.isAngered = mood; 
    }
    
    /**
     * Setter method for NPC hit points
     * 
     * @param h  Hitpoints of NPC object as integer.
     */
    public void setHealth(int h)
    {
        health = health - h; 
    }
    
    /**
     * Setter method for NPC current room
     * 
     * @param rm    Room to set the NPC in as Room object.
     */

    void setRoom(Room rm)
    {
        currentRoom = rm; 
    }
    
    /**
     * Adds item to NPC inventory
     * 
     * @param item Item object to add to NPC carriedItems ArrayList. 
     */
    void add(Item item)
    {
        this.carriedItems.add(item); 
    }
    
    /**
     * Removes item from NPC inventory.
     * 
     * @param item      Item to remove from NPC inventory ArrayList as Item object.
     */
    void removeFromInventory(Item item)
    {
        carriedItems.remove(item); 
    }
    
    /**
     * Getter method for NPC currentRoom
     * 
     * @return      The room the NPC is currently located.
     */
    public Room getNpcRoom()
    {
        return this.currentRoom; 
    }

    /**
     * Moves denizens through dungeon rooms / method call triggered by successful MoveCommand object
     */
    public static void moveDenizens()
    {
        GameState gs = GameState.instance(); 
        Set<String> keys = gs.getDungeon().npcInDungeon.keySet();
        for(String key : keys)
        {
            Denizen npc = gs.getDungeon().getNPC(key); 
            if(npc != null)
            {
                if(npc.isAngered && npc.isMobile)
                {
                    Room npcRoom = npc.getNpcRoom(); 
                    if(!tryFollowUser(npcRoom, npc))
                    {
                        int i = randInt(0, npcRoom.roomExits.size() -1); 
                        npc.goToAdjacentRoom(npcRoom, i, npc);
                        leaveItemInRoom(npc); 
                    } 
                }
                else if(npc.isMobile)
                {
                    Room npcRoom = npc.getNpcRoom(); 
                    int i = randInt(0, npcRoom.roomExits.size() - 1); 
                    npc.goToAdjacentRoom(npcRoom, i, npc); 
                    takeItemFromRoom(npc);
                    leaveItemInRoom(npc); 
                }
            }
        }
    }

    /**
     * Random number generator / used to randomly pick an exit for the NPC to walk through
     * 
     * @param min   Bottom limit for random number / currently only 0 to match bottom of Array index.
     * @param max   Upper limit fo random number / maxes out at the size of the ArrayList of exits, -1 for 0-based.
     * @return      randomly generated integer within a range defined by the number of available exits
     */
    static int randInt(int min, int max)
    {
        Random rand = new Random(); 
        int randomNum = rand.nextInt((max - min) + 1) + min; 
        return randomNum; 
    }
    
    /**
     * This is called if the NPC is currently pissed off. It attempts to follow the user if it is located in an adjacent room to the user's current room
     * 
     * @param room     The room the NPC is currently sitting in
     * @param npc      The current denizen object to work on
     * @return         Boolean value based on failure or success where user is or isn't located in an adjacent or same room as the NPC.
     */
    static boolean tryFollowUser(Room room, Denizen npc)
    {
         GameState gs = GameState.instance(); 
         Room tempRoom = gs.getAdventurersCurrentRoom(); 
         // if npc and user in the same room
         if(room == tempRoom)
         {
             npc.setRoom(tempRoom); 
             room.removeNpc(npc); 
             tempRoom.addNpc(npc);
             //System.out.println("tryFollowUser move '" +npc.getName()+ "' to '" +tempRoom.getTitle()+ "', from " + room.getTitle() +"."); 
             return true;              
         }
         for(Exit exit : tempRoom.roomExits)
         {
             if(exit.getDest() == room)
             {
                 npc.setRoom(tempRoom); 
                 room.removeNpc(npc); 
                 tempRoom.addNpc(npc);
                 //System.out.println("tryFollowUser move '" +npc.getName()+ "' to '" +tempRoom.getTitle()+ "', from " + room.getTitle() +"."); 
                 return true; 
             }
         }
         return false; 
    }
    
    /**
     * This moves the NPC to a random room
     * 
     * @param room     The room the NPC is currently sitting in.
     * @param i        Random number to index in room's array of exits.
     * @param npc      The current denizen object to work on.
     */
    static void goToAdjacentRoom(Room room, int i, Denizen npc)
    {
        Exit exit = room.roomExits.get(i);
        Room tempRoom = exit.getDest(); 
        npc.setRoom(tempRoom); 
        room.removeNpc(npc); 
        tempRoom.addNpc(npc); 
        //System.out.println("goToAdjacentRoom move '" +npc.getName()+ "' to '" +tempRoom.getTitle()+ "'."); 
    }
    
    /**
     * NPC takes item from room
     * 
     * @param npc       NPC object to work with. 
     */
    static void takeItemFromRoom(Denizen npc)
    {
        Room room = npc.getNpcRoom(); 
        if(!room.roomItems.isEmpty())
        {
            ArrayList<Item> al = room.getRoomItems();
            ArrayList<Item> toDrop = new ArrayList<Item>();
            Iterator<Item> iter = al.iterator(); 
            while(iter.hasNext())
            {
                Item item = iter.next(); 
                int i = randInt(0, 9);
                if(item.getWeight() != 99999)
                {
                    switch(i)
                    {
                        //case 0: case 1: case 2: case 3: case 4: // testing
                        case 5: case 6: case 7: case 8: case 9:         toDrop.add(item);  
                                                                        npc.add(item);
                                                                        //System.out.println("'" + npc.getName() + "' grabbed '" + item.getPrimaryName() + "' from '" + room.getTitle() + "'."); //testing 
                                                                        break; 
                    
                    }
                }
            }
            
            for(Item item : toDrop)
            {
                room.remove(item); 
            }
        }
    }
    
    /**
     * NPC leaves item in room
     * 
     * @param npc       NPC object to work with
     */
    static void leaveItemInRoom(Denizen npc)
    {
        Room room = npc.getNpcRoom(); 
        ArrayList<Item> al = npc.carriedItems; 
        if(al.size() > 3)
        {
            int i = randInt(0, al.size() -1); 
            Item item = al.get(i); 
            npc.removeFromInventory(item); 
            room.add(item); 
            //System.out.println("'" + npc.getName() + "' left '" + item.getPrimaryName() + "' in '" + room.getTitle() + "'."); //testing
        }
        
    }
    
    /**
     * Persistence method to store NPC states in save file
     * 
     * @param pw PrintWriter object handed focus from GameState Class.
     */
    void storeState(PrintWriter pw)
    {
            pw.write(this.name + "\n"); 
            pw.write(this.health + "\n"); 
            pw.write("isMobile=" + this.isMobile + "\n"); 
            pw.write("isAngered=" + this.isAngered + "\n"); 
            if(!this.carriedItems.isEmpty())
            {
                String s = ""; 
                String r = ""; 
                pw.write("Carries: "); 
                for(Item item : this.carriedItems)
                {
                    s = s + item.getPrimaryName() + ","; 
                }
                r = s.substring(0, s.lastIndexOf(",")); 
                pw.write(r + "\n");  
                pw.write("---\n"); 
            }            
    } 
    
    /**
     * Hydration method to upload NPC states from save file.
     * 
     * @param s         Scanner object handed focus from GameState Class.
     * @param den       Denizen object to work on.
     */
    void restoreState(Scanner s, Denizen den)
    {
        String pattern1 = "---"; 
        GameState gs = GameState.instance(); 
        this.health = s.nextInt(); 
        s.nextLine(); // needed to advance the token
        if(s.nextLine().equals("isMobile=true"))
        {
            this.isMobile = true; 
        }else
        {
            this.isMobile = false; 
        }
        if(s.nextLine().equals("isAngered=true"))
        {
            this.isAngered = true; 
        }else
        {
            this.isAngered = false; 
        }
        if(!s.hasNext(pattern1))
        {
            String sub = s.nextLine(); 
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
        s.nextLine(); 
    }
}
