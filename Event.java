import java.util.*; 
import java.io.*; 
import java.util.regex.*; 

/**
 * Events are actions tied to specific verbs. When triggered, they have an affect on the gamed. 
 * 
 * @author William (Greg) Phillips
 * @version Zork v1.2
 */
public class Event
{
    public static List<String> POSITIVE_MESSAGES = Arrays.asList("That felt good.", "That hit the spot.", "Oh yeah!", "Hell yeah!"); 
    public static List<String> NEGATIVE_MESSAGES = Arrays.asList("That's no good.", "That sucks.", "Ouch!", "Arggggghhh!"); 
    public static List<String> DEATH_MESSAGES    = Arrays.asList("Such a frail and feeble mind./nDeath is but mercy.", 
                                                                 "Only in death, does duty end.",
                                                                 "For every battlefield honor, a thousand heroes die, unsung - unremembered.", 
                                                                 "What is the fear of Death?\nTo die, knowing our task is undone."); 
    
    private String type = ""; 
    private String xfrm = ""; 
    private String nativeItem = ""; 
    private int health = 0; 
    private int score = 0; 
    
    /**
     * Constructor for Objects of Class Event
     * 
     * @param action        String parsed from item verb
     * @param nativeItem    String primary name of item that holds the action
     */
    public Event(String action, String nativeItem)
    {
        if(action.contains("Wound"))
        {
            String[] parts = action.split("\\(");
            this.type = parts[0]; 
            this.health = Integer.parseInt(getParenthesesContent(action)); 
            this.nativeItem = nativeItem; 
        }
        else if(action.contains("Die") |
                action.contains("Teleport") |
                action.contains("Disappear") |
                action.contains("Win")) 
        {
            this.type = action; 
            this.nativeItem = nativeItem; 
        }
        else if(action.contains("Transform"))
        {
            String[] parts = action.split("\\(");
            this.type = parts[0];    
            this.xfrm = getParenthesesContent(action); 
            this.nativeItem = nativeItem; 
        }
        else if(action.contains("Score"))
        {
            String[] parts = action.split("\\(");
            this.type = parts[0];    
            this.score = Integer.parseInt(getParenthesesContent(action)); 
            this.nativeItem = nativeItem; 
        }else if(action.contains("Unlock"))
        {
            this.type = action; 
            this.nativeItem = nativeItem; 
        }
    }
    
    /**
     * Strips out the content between parentheses.
     * 
     * @param s     Value to be parsed
     * @return      String value with parentheses stripped out
     */
    private String getParenthesesContent(String s)
    {
        return s.substring(s.indexOf('(')+1, s.indexOf(')')); 
    } 
    
    /**
     * Generate event actions based on event type, then call the appropriate method.
     * 
     * @return      String message to display to user
     * @throws InterruptedException     Pushes IO exception up the stack
     */
    public String generateEvent() throws InterruptedException
    {
        String s = ""; 
        
        switch(this.type)
        {
            case "Wound":       s = this.wound(); 
                                break; 
            case "Die":         s = this.die(); 
                                break; 
            case "Teleport":    s = this.teleport(); 
                                break;
            case "Disappear":   s = this.disappear(); 
                                break; 
            case "Win":         s = this.win(); 
                                break; 
            case "Transform":   s = this.transform(); 
                                break; 
            case "Score":       s = this.score(); 
                                break; 
            case "Unlock":      s = this.unlock(); 
                                break; 
        }
        return s; 
    }
    
    /**
     * Getter method for Event type
     * 
     * @return      String value of Event's type
     */
    public String getEventType()
    {
        return this.type; 
    }
    
    /**
     * Random number generator used to randomly pick a String message
     * 
     * @param min   Bottom limit for random number 
     * @param max   Upper limit fo random number 
     * @return      Randomly generated integer 
     */
    static int randInt(int min, int max)
    {
        Random rand = new Random(); 
        int randomNum = rand.nextInt((max - min) + 1) + min; 
        return randomNum; 
    }    
    
    /**
     * Handles item events that are wound actions. Adds or subtracts health accordingly.
     * 
     * @return                          String message to display to user 
     * @throws InterruptedException     Pushes IO exception up the stack
     */
    private String wound() throws InterruptedException
    {
        String s = ""; 
        GameState gs = GameState.instance(); 
        gs.setHealth(this.health);
        
        if(gs.getHealth() < 1)
        {
            die(); 
        }
        
        if(this.health > 0)
        {
            int i = randInt(0, NEGATIVE_MESSAGES.size() - 1); 
            //s = "\n" + NEGATIVE_MESSAGES.get(i) + "\n"; 
            return "\n" + (s = NEGATIVE_MESSAGES.get(i)) + "\n"; 
            //return s; 
        }
        
        int i = randInt(0, POSITIVE_MESSAGES.size() - 1); 
        return "\n" + (s = POSITIVE_MESSAGES.get(i)) + "\n"; 
    }
    
    /**
     * Handles item events that are die actions. Kills the thread and ends the game. 
     * 
     * @return                          Random death message to display to user
     * @throws InterruptedException     Pushes IO exception up the stack
     */
    private String die() throws InterruptedException
    {
        String s = ""; 
        int i = randInt(0, DEATH_MESSAGES.size() -1); 
        s = "\n" + (s = DEATH_MESSAGES.get(i)) + "\n"; 
        KillGame.gameKill(s); 
        return null; 
    }
    
    /**
     * Handles item events that are transform actions.
     * 
     * @return      String message to display to user 
     */
    private String transform()
    {
        //this needs to be written
        //Remove from Dungeon and/or Room + from user's inventory. Add to room or user's inventory
        return "\nTest for Transform\n"; 
    }
    
    /**
     * Handles item events that are score actions. Adjusts user's score. 
     * 
     * @return      String message to display to user 
     */
    private String score()
    {
        GameState gs = GameState.instance(); 
        gs.setScore(this.score); 
        if(this.score >0)
        {
            return "\nAdded '" + this.score + "' xp."; 
        }
        else
        {
            return "\nRemoved '" +(this.score * -1)+ "' xp."; 
        }
    }
    
    /**
     * Handles item events that are teleport actions. Teleports user to random room within dungeon. 
     * 
     * @return      String message to display to user 
     */
    private String teleport()
    {
        GameState gs = GameState.instance();
        Dungeon d = gs.getDungeon(); 
        List<String> keys = new ArrayList<String>(d.collection.keySet()); 
        int i = randInt(0, d.collection.size() - 1); 
        String randomKey = keys.get(i); 
        Room room = d.getRoom(randomKey);
        if(room != null)
        {
            gs.setAdventurersCurrentRoom(room); 
            return "\nTeleported to '" +room.getTitle()+ "'."; 
        }
        return "\nTeleportation Malfunction\n"; 
    }
    
    /**
     * Handles item events that are disappear actions. 
     * 
     * @return      String message to display to user 
     */
    private String disappear()
    {
        //this needs to be written
        //Needs to remove from Dungeon and/or Room and/or User's Inventory and/or NPC's Inventory
        //Dungeon needs to remember what items have disappeared and not restore them when restoring from save file, etc
        return "\nTest for Disappear\n"; 
    }
    
    /**
     * Handles item events that are win actions.
     * 
     * @return      String message to display to user 
     */
    private String win()
    {
        //this needs to be written
        return "\nTest for Win\n"; 
    }
    
    /**
     * Handles item events that are unlock actions. Currently, this only relates to the dataslate and holoplinth. 
     * 
     * @return      String message to display to user
     */
    private String unlock()
    {
        String s = "";
        switch(this.nativeItem)
        {
            case "dataslate":   DataSlate ds = DataSlate.instance(); 
                                s = ds.unlock(); 
                                break; 
            case "holoplinth":  HoloPlinth hs = HoloPlinth.instance(); 
                                s = hs.unlock(); 
                                break; 
        }
        return "\n" + s + "\n"; 
    }
}
