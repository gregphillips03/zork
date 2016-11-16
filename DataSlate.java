import java.util.*; 
import java.io.*; 

/**
 * The DataSlate class handles special interactions with the dataslate item.
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1.2
 */
public class DataSlate
{
    static DataSlate dataSlate = new DataSlate(); 
    static final int code = 3324; 
    static boolean isLocked = true; 
    
    /**
     * Constructor for objects of class DataSlate. Singleton class controls creation. 
     */
    private DataSlate()
    {
        //exists to control object creation
    }

    /**
     * Exists to control object creation.
     * 
     * @return      DataSlate object to work with. 
     */
    public static DataSlate instance()
    {
        return dataSlate; 
    }
    
    /**
     * Interaction with user to unlock Dataslate. Prompts user for four digit code and unlocks the dataslate if guessed correctly. 
     * 
     * @return      String message to user
     */
    public String unlock()
    {
        Scanner scan = new Scanner(System.in);
        GameState gs = GameState.instance();         
        String s = "Invalid Input"; 
        
        if(!isLocked)
        {
            s = "This dataslate is already unlocked"; 
            return s; 
        }
        System.out.println("Enter four digit code: "); 
        int userCode = scan.nextInt(); 
        if(userCode == code)
        {
            isLocked = false; 
            gs.setScore(50);
            updateSlate(); 
            s = "Input Accepted...\nDataslate unlocked."; 
        }
        return s; 
    }
    
    /**
     * Interaction with user to access Dataslate. Allows user to explore various funcitons of dataslate interface. 
     * 
     * @return      String message to user
     */
    public String access()
    {
        String s = "|-------------------------|\n" +
                   "|+++Available Functions+++|\n" +
                   "|+ No Available Function +|\n" +
                   "|-------------------------|\n";
        //need to write
        return s; 
    }
    
    /**
     * Updates the dataslate so the user no longer sees the "unlock" event. Adds in the access functionality instead. 
     * Physically removes the object from the dungeon and room/or inventory and then replaces with like object including differing functionality. 
     */
    private void updateSlate()
    {
        GameState gs = GameState.instance(); 
        CommandFactory cf = CommandFactory.instance(); 
        Dungeon d = gs.getDungeon(); 
        Item item = d.getItem("dataslate");
        Room room = gs.getAdventurersCurrentRoom();
        boolean userHad = false; 
        boolean roomHad = false; 
      
        //determine if the dataslate is in inventory or in the room
        //remove and add item accordingly 
        if(gs.getItemInVicinityNamed("dataslate") != null)
        {
            room.roomItems.remove(item); 
            roomHad = true; 
        }
        else if(gs.getItemFromInventory("dataslate") != null)
        {
            gs.removeFromInventory(item); 
            userHad = true; 
        }
        
        //remove from dungeon
        d.itemsInDungeon.remove(item.getPrimaryName());      
        
        //create a new dataslate
        Item slate = new Item("dataslate", 1);
        //add verb message combo, ensure it's within the program's known verbs
        slate.addVerbMessage("access", "++++DataSlate Interface++++"); 
        cf.AVAIL_VERBS.add("access"); 
        //create access event and add to item's array of events
        Event e = new Event("Access", "dataslate"); 
        ArrayList<Event> al = new ArrayList<Event>(); 
        al.add(e); 
        slate.addEvent("access", al); 
        
        if(roomHad)
        {
            d.itemsInDungeon.put(slate.getPrimaryName(), slate); 
            room.roomItems.add(slate); 
        }
        else if(userHad)
        {
            d.itemsInDungeon.put(slate.getPrimaryName(), slate); 
            gs.addToInventory(slate); 
        }
    }
    
    /**
     * Persistence method to write to save file
     * 
     * @param pw        PrintWriter object to work with / handed focus from GameState Class
     */
    void storeState(PrintWriter pw)
    {
        if(isLocked)
        {
            pw.write("isLocked=true\n"); 
        }
        else
        {
            pw.write("isLocked=false\n"); 
        }        
    }
    
    /**
     * Hydration method to read from save file
     * 
     * @param scan      Scanner object to work with/ handed focus from GameState Class 
     */
    void restoreState(Scanner scan)
    {
        if(!scan.nextLine().equals("isLocked=true"))
        {
            isLocked = false; 
            updateSlate(); 
        }
    }        
}
