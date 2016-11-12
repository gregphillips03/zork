import java.util.*; 
import java.io.*; 
import java.util.regex.*; 

/**
 * Events are actions tied to specific verbs. When triggered, they have an affect on the gamed. 
 * 
 * @author William (Greg) Phillips
 * @version Zork v1.1
 */
public class Event
{
    public static List<String> POSITIVE_MESSAGES = Arrays.asList("That felt good.", "That hit the spot.", "Oh yeah!", "Hell yeah!"); 
    public static List<String> NEGATIVE_MESSAGES = Arrays.asList("That's no good.", "That sucks.", "Ouch!", "Arggggghhh!"); 
    public static List<String> DEATH_MESSAGES    = Arrays.asList("Such a frail and feeble mind.", 
                                                                 "Only in death, does duty end.",
                                                                 "For every battlefield honor, a thousand heroes die, unsung - unremembered.", 
                                                                 "What is the fear of Death? \n To die, knowing our task is undone."); 
    
    private String type = ""; 
    private String xfrm = ""; 
    private int health = 0; 
    private int score = 0; 
    
    /**
     * Constructor for Objects of Class Event
     * 
     * @param action        String parsed from item verb
     */
    public Event(String action)
    {
        if(action.contains("Wound"))
        {
            String[] parts = action.split("\\(");
            this.type = parts[0]; 
            this.health = Integer.parseInt(getParenthesesContent(action)); 
        }
        else if(action.contains("Die") |
                action.contains("Teleport") |
                action.contains("Disappear") |
                action.contains("Win")) 
        {
            this.type = action; 
        }
        else if(action.contains("Transform"))
        {
            String[] parts = action.split("\\(");
            this.type = parts[0];    
            this.xfrm = getParenthesesContent(action); 
        }
        else if(action.contains("Score"))
        {
            String[] parts = action.split("\\(");
            this.type = parts[0];    
            this.score = Integer.parseInt(getParenthesesContent(action)); 
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
     * Random number generator / used to randomly pick a String message
     * 
     * @param min   Bottom limit for random number 
     * @param max   Upper limit fo random number 
     * @return      randomly generated integer 
     */
    static int randInt(int min, int max)
    {
        Random rand = new Random(); 
        int randomNum = rand.nextInt((max - min) + 1) + min; 
        return randomNum; 
    }    
    
    /**
     * Handles events that are wound actions.
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
        
        if(this.health >0)
        {
            int i = randInt(0, NEGATIVE_MESSAGES.size() - 1); 
            return "\n" + (s = NEGATIVE_MESSAGES.get(i)) + "\n"; 
        }
        
        int i = randInt(0, POSITIVE_MESSAGES.size() - 1); 
        return "\n" + (s = POSITIVE_MESSAGES.get(i)) + "\n"; 
    }
    
    /**
     * Handles events that are die actions.
     * 
     * @return                          String message to display to user 
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
     * Handles events that are transform actions.
     * 
     * @return      String message to display to user 
     */
    private String transform()
    {
        return "\nTest for Transform\n"; 
    }
    
    /**
     * Handles events that are score actions.
     * 
     * @return      String message to display to user 
     */
    private String score()
    {
        GameState gs = GameState.instance(); 
        gs.setScore(this.score); 
        return "\nAdded '" + this.score + "' XP."; 
    }
    
    /**
     * Handles events that are teleport actions.
     * 
     * @return      String message to display to user 
     */
    private String teleport()
    {
        return "\nTest for Teleport\n"; 
    }
    
    /**
     * Handles events that are disappear actions. 
     * 
     * @return      String message to display to user 
     */
    private String disappear()
    {
        return "\nTest for Disappear\n"; 
    }
    
    /**
     * Handles events that are win actions.
     * 
     * @return      String message to display to user 
     */
    private String win()
    {
        return "\nTest for Win\n"; 
    }
}
