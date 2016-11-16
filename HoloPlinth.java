import java.util.*; 
import java.io.*; 

/**
 * The HoloPlinth class handles special interactions with the holoplinth item. 
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1.2
 */
public class HoloPlinth
{
    static HoloPlinth holoPlinth = new HoloPlinth();
    static final String passPhrase = "golgotha"; 
    static boolean isLocked = true; 
    
    /**
     * Constructor for objects of class HoloPlinth. Singleton class controls creation
     */
    public HoloPlinth()
    {
        //exists to control object creation
    }

    /**
     * Exists to control object creation.
     * 
     * @return      HoloPlinth object to work with. 
     */
    public static HoloPlinth instance()
    {
        return holoPlinth; 
    }
    
    /**
     * Interaction with user to unlock Holoplinth. Prompts user for passphrase and unlocks the holoplinth if guessed correctly. 
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
            s = "This holoplinth is already unlocked"; 
            return s; 
        }
        System.out.println("Enter passphrase: "); 
        String userCode = scan.nextLine().toLowerCase(); 
        if(userCode.equals(passPhrase))
        {
            isLocked = false; 
            gs.setScore(25);
            updatePlinth(); 
            s = "Input Accepted...\nHoloplinth unlocked."; 
        }
        return s; 
    }
    
    /**
     * Interaction with user to access HoloPlinth. Allows user to explore various funcitons of plinth interface. 
     * 
     * @return      String message to user
     */
    public String access()
    {
        String s = "|-------------------------|\n" +
                   "|+++Available Functions+++|\n" +
                   "| <BioScan>    <Teleport> |\n" +
                   "|-------------------------|\n";
        //need to write
        return s; 
    }
    
    /**
     * Updates the holoplinth so the user no longer sees the unlock event. Adds in the access functionality instead.
     * Physically removes the object from the dungeon and room and then replaces with like object including differing functionality.
     */
    private void updatePlinth()
    {
        GameState gs = GameState.instance(); 
        CommandFactory cf = CommandFactory.instance(); 
        Dungeon d = gs.getDungeon();
        Item item = d.getItem("holoplinth"); 
        Room room = d.getRoom("Engine Control");
        
        //remove from dungeon and room
        d.itemsInDungeon.remove(item.getPrimaryName()); 
        room.roomItems.remove(item);  
        
        //create new holoplinth item
        Item plinth = new Item("holoplinth", 99999); 
        
        //add verb message combo, ensure it's within the program's known verbs
        plinth.addVerbMessage("access", "++++Holoplinth Interface+++"); 
        cf.AVAIL_VERBS.add("access"); 
        
        //create access event and add to item's array of events
        Event e = new Event("Access", "holoplinth"); 
        ArrayList<Event> al = new ArrayList<Event>(); 
        al.add(e); 
        plinth.addEvent("access", al); 
        
        //add item to dungeon and room
        d.itemsInDungeon.put(plinth.getPrimaryName(), plinth); 
        room.roomItems.add(plinth);       
        
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
     * @param scan      Scanner object to work with / handed focus from GameState Class
     */
    void restoreState(Scanner scan)
    {
        if(!scan.nextLine().equals("isLocked=true"))
        {
            isLocked = false; 
            updatePlinth(); 
        }
    }
}
