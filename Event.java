
/**
 * Events are actions tied to specific verbs. When triggered, they have an affect on the gamed. 
 * 
 * @author William (Greg) Phillips
 * @version Zork v1.1
 */
public class Event
{
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
            wound(action); 
        }
        else if(action.contains("Die")) 
        {
            die(action); 
        }
        else if(action.contains("Transform"))
        {
            transform(action); 
        }
        else if(action.contains("Score"))
        {
            score(action); 
        }
        else if(action.contains("Teleport"))
        {
            teleport(action); 
        }
        else if(action.contains("Disappear"))
        {
            disappear(action); 
        }
    }
    
    /**
     * Handles events that are wound actions.
     * 
     * @param s     Action to be parsed
     */
    private void wound(String s)
    {
        String[] parts = s.split("\\(");
        this.type = parts[0]; 
        this.health = Integer.parseInt(getParenthesesContent(s)); 
    }
    
    /**
     * Handles events that are die actions.
     * 
     * @param s     Action to be parsed
     */
    private void die(String s)
    {
        this.type = s; 
    }
    
    /**
     * Handles events that are transform actions.
     * 
     * @param s     Action to be parsed
     */
    private void transform(String s)
    {
        String[] parts = s.split("\\(");
        this.type = parts[0];    
        this.xfrm = getParenthesesContent(s); 
    }
    
    /**
     * Handles events that are score actions.
     * 
     * @param s     Action to be parsed
     */
    private void score(String s)
    {
        String[] parts = s.split("\\(");
        this.type = parts[0];    
        this.score = Integer.parseInt(getParenthesesContent(s)); 
    }
    
    /**
     * Handles events that are teleport actions.
     * 
     * @param s     Action to be parsed
     */
    private void teleport(String s)
    {
        this.type = s; 
    }
    
    /**
     * Handles events that are disappear actions. 
     * 
     * @param s     Action to be parsed
     */
    private void disappear(String s)
    {
        this.type = s; 
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
}
