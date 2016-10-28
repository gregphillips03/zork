import java.util.*; 
import java.io.*; 
import java.util.regex.*; 

/**
 * Denizen
 * 
 * The Denizen class relates to the dungeon's NPCs. 
 * It creats the Denizens themselves and moves around according to predefined rules 
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
    private ArrayList<Item> carriedItems = new ArrayList<Item>(); 
    
    public Denizen(String name, String desc, int health, boolean mobile)
    {
        this.name = name; 
        this.desc = desc; 
        this.health = health; 
        this.isMobile = mobile; 
    }
    
    public Denizen(Scanner s, Dungeon d)
    {
        String pattern1 = "---"; 
        String pattern2 = "Carries:"; 
        Pattern p = Pattern.compile(pattern2); 
        
        this.name = s.nextLine();
        String subRoom = s.nextLine();
        Room tempRoom = d.getRoom(subRoom);   
        this.currentRoom = tempRoom; 
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
                    this.add(item);
                    //System.out.println("Added " +item.getPrimaryName()+ " to " + this.getName() + " inventory."); 
                }
            }
            this.desc = this.desc + "\n" + s.nextLine(); 
        }
        tempRoom.addNpc(this); 
        //System.out.println("Added " +this.name+ " to room " +tempRoom.getTitle() + ".");
        //System.out.println("Mobile: " + this.isMobile); 
        //System.out.println("Angry: " + this.isAngered);
        //System.out.println("Desc: " + this.desc); 
        s.nextLine(); //advances token
    }
    
    // store state needs to go in here
    
    public String getName()
    {
        return this.name; 
    }
    
    public String getDesc()
    {
        return this.desc; 
    }
    
    public int getHealth()
    {
        return this.health; 
    }
    
    private void setHealth(int h)
    {
        health = h; 
    }
    
    private void setRoom(Room rm)
    {
        currentRoom = rm; 
    }
    
    void add(Item item)
    {
        this.carriedItems.add(item); 
    }
    
    public Room getNpcRoom()
    {
        return this.currentRoom; 
    }

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
     * 
     */
    static int randInt(int min, int max)
    {
        Random rand = new Random(); 
        int randomNum = rand.nextInt((max - min) + 1) + min; 
        return randomNum; 
    }
    
    /**
     * tryFollowUser
     * This is called if the NPC is currently pissed off. It attempts to follow the user if it is located in an adjacent room to the user's current room
     * 
     * @ param room     the room the NPC is currently sitting in
     * @ param npc      the current denizen object to work on
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
     * goToAdjacentRoom
     * This moves the NPC to a random room
     * @ param room     the room the NPC is currently sitting in
     * @ param i        random number to index in room's array of exits
     * @ param npc      the current denizen object to work on
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
}
