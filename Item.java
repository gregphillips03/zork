import java.util.*; 
import java.io.*; 

/**
 * Item class contains Item objects that are stored in rooms, with the user, or with a NPC
 * 
 * @author William (Greg) Phillips
 * @version Zork v1
 */
public class Item
{
    private String primaryName = ""; 
    private int weight = 0; 
    private Hashtable<String, String> messages = new Hashtable<String, String>(); 
    private Hashtable<String, ArrayList<Event>> itemEvents = new Hashtable<String, ArrayList<Event>>(); 
    //private ArrayList<Event> events = new ArrayList<Event>();
    
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
                //System.out.println("Added the verb '" + verb + "', with the message, '" + message + "'."); 
            }else
            {
                ArrayList<Event> events = new ArrayList<Event>();
                String x = parts[0]; 
                String message = parts[1]; 
                CommandFactory cf = CommandFactory.instance();
                
                String[] pieces = x.split("["); 
                String verb = pieces[0];
                cf.AVAIL_VERBS.add(verb);
                this.messages.put(verb, message); 
                String evnt = pieces[1]; 
                String xx = evnt.substring(evnt.indexOf("[")+1, evnt.lastIndexOf("]"));
                //pick up here 
                String[] eventString = xx.split(","); 
                for(String event : eventString)
                {
                    Event e = new Event(event); 
                    events.add(e); 
                    this.itemEvents.put(verb, events); 
                }
            }
        }
        s.nextLine(); 
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
    
}
