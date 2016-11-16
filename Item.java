import java.util.*; 
import java.io.*; 

/**
 * Item class contains Item objects that are stored in rooms, with the user, or with a NPC
 * 
 * @author William (Greg) Phillips
 * @version Zork v1.2
 */
public class Item
{
    private String primaryName = ""; 
    private int weight = 0; 
    private Hashtable<String, String> messages = new Hashtable<String, String>(); 
    private Hashtable<String, ArrayList<Event>> itemEvents = new Hashtable<String, ArrayList<Event>>(); 
    
    /**
     * Constructor for objects of class Item
     * 
     * @param s     Scanner object handed focus by Dungeon Class
     */
    public Item(Scanner s)
    {
        String pattern1 = "---"; 
        
        this.primaryName = s.next(); 
        this.weight = s.nextInt(); 
        s.nextLine(); // needed to advance token to beginning of next line        
        while(!s.hasNext(pattern1))
        {
            String sub = s.nextLine(); 
            String[] parts = sub.split(":"); 
            if(!parts[0].contains("[")) //checks to see if there are events associated with the verb
            {
                String verb = parts[0]; 
                String message = parts[1]; 
                CommandFactory cf = CommandFactory.instance(); 
                cf.AVAIL_VERBS.add(verb); 
                this.messages.put(verb, message); 
                //System.out.println("Added the verb '" + verb + "', with the message, '" + message + "'."); //debugging
            }else
            {
                ArrayList<Event> events = new ArrayList<Event>();
                String x = parts[0]; 
                String message = parts[1]; 
                CommandFactory cf = CommandFactory.instance();
                
                String[] pieces = x.split("\\["); 
                String verb = pieces[0];
                cf.AVAIL_VERBS.add(verb);
                this.messages.put(verb, message); 
                String xx = getBracketContent(pieces[1]); 
                String[] eventString = xx.split(","); 
                for(String event : eventString)
                {
                    Event e = new Event(event, this.primaryName); 
                    events.add(e); 
                    //System.out.println("Added '" + e.getEventType() + "' to Array List for Item '" +this.getPrimaryName() +"'."); //debugging
                    this.itemEvents.put(verb, events); 
                }
            }
        }
        s.nextLine(); 
    }
    
    /**
     * Constructor for objects of Class Item
     * 
     * @param name      Name of item as String
     * @param weight    Weight of item as integer
     */
    public Item(String name, int weight)
    {
        this.primaryName = name; 
        this.weight = weight; 
    }
    
    /**
     * Returns the item's primary name
     * 
     * @return         Returns this objects primary name as a String
     **/
    public String getPrimaryName()
    {
        return this.primaryName; 
    }
    
    /**
     * Returns corresponding message string that accompanies a verb
     * 
     * @param verb      Verb as String to look for in messages hashtable
     * @return          String message pair for specified verb. 
     */
    public String getMessageForVerb(String verb)
    {
        if(this.messages.containsKey(verb))
        {
            return this.messages.get(verb); 
        }
        //System.out.println(verb + " not found.\n"); 
        return null; 
    }
    
    /**
     * Access the hashtable that contains the verb and event arraylist combination
     * 
     * @param verb      Verb as String to look for in itemEvents hashtable
     * @return          Arraylist of verbs available actions
     */
    public ArrayList getEventsForVerb(String verb)
    {
        if(this.itemEvents.containsKey(verb))
        {
            return this.itemEvents.get(verb); 
        }
        //System.out.println(verb + " not found.\n"); 
        return null;         
    }
    
    /**
     * Displays the Item's available verbs as a String
     * 
     * @return      Returns each verb as a String, wokka encased for legibility. 
     */    
    public String toString()
    {
        Enumeration en = this.messages.keys();
        String s = ""; 
        while(en.hasMoreElements())
        {
            s = s + "<" + en.nextElement() + "> "; 
        }    
        return s; 
    }
    
    /**
     * Getter method for item weight
     * 
     * @return      Item weight as an integer. 
     */
    public int getWeight()
    {
        return this.weight; 
    }
    
    /**
     * Strips out the content between brackets.
     * 
     * @param s     Value to be parsed
     * @return      String value with brackets stripped out
     */
    private String getBracketContent(String s)
    {
        return s.substring(s.indexOf('[')+1, s.indexOf(']')); 
    } 
    
    /**
     * Add message to item verb
     * 
     * @param verb      Verb to pair with message as String
     * @param message   Message to display to user with verb combo
     */
    public void addVerbMessage(String verb, String message)
    {
        this.messages.put(verb, message); 
    }
    
    /**
     * 
     */
    public void addEvent(String key, ArrayList al)
    {
        this.itemEvents.put(key, al); 
    }
}
