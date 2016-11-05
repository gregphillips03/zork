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
     * @param s     Scanner object for hydration.
     * @param d     Dungeon object for hydration.
     * @param b     Initialization parameter. Tells constructor whether or not to set NPC current room and add object or avoid duplicaton of objects in dungeon.
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
     * Getter method for NPC mood
     * 
     * @return      NPC object's mood as boolean. 
     */
    public boolean getMood()
    {
        return this.isAngered; 
    }
    
    /**
     * Setter method for NPC hit points
     * 
     * @param h  Hitpoints of NPC object as integer.
     */
    private void setHealth(int h)
    {
        health = h; 
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
                        int i = randInt(0, npcRoom.exitPath.size() -1); 
                        npc.goToAdjacentRoom(npcRoom, i, npc); 
                    } 
                }
                else if(npc.isMobile)
                {
                    Room npcRoom = npc.getNpcRoom(); 
                    int i = randInt(0, npcRoom.exitPath.size() - 1); 
                    npc.goToAdjacentRoom(npcRoom, i, npc); 
                }
            }
        }
    }

    /**
     * Random number generator / used to randomly pick an exit for the NPC to walk through
     * 
     * @param min   Bottom limit for random number / currently only 0 to match bottom of Array index.
     * @param max   Upper limit fo random number / maxes out at the size of the ArrayList of exits, -1 for 0-based.
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
     * @ param room     The room the NPC is currently sitting in
     * @ param npc      The current denizen object to work on
     * @ return         Boolean value based on failure or success where user is or isn't located in an adjacent or same room as the NPC.
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
             System.out.println("tryFollowUser move '" +npc.getName()+ "' to '" +tempRoom.getTitle()+ "', from " + room.getTitle() +"."); 
             return true;              
         }
         for(Exit exit : tempRoom.exitPath)
         {
             if(exit.getDest() == room)
             {
                 npc.setRoom(tempRoom); 
                 room.removeNpc(npc); 
                 tempRoom.addNpc(npc);
                 System.out.println("tryFollowUser move '" +npc.getName()+ "' to '" +tempRoom.getTitle()+ "', from " + room.getTitle() +"."); 
                 return true; 
             }
         }
         return false; 
    }
    
    /**
     * This moves the NPC to a random room
     * 
     * @ param room     The room the NPC is currently sitting in.
     * @ param i        Random number to index in room's array of exits.
     * @ param npc      The current denizen object to work on.
     */
    static void goToAdjacentRoom(Room room, int i, Denizen npc)
    {
        Exit exit = room.exitPath.get(i);
        Room tempRoom = exit.getDest(); 
        npc.setRoom(tempRoom); 
        room.removeNpc(npc); 
        tempRoom.addNpc(npc); 
        System.out.println("goToAdjacentRoom move '" +npc.getName()+ "' to '" +tempRoom.getTitle()+ "'."); 
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
