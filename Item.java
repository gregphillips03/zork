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
            // need to account for the brackets
            String verb = parts[0]; 
            String message = parts[1]; 
            CommandFactory cf = CommandFactory.instance(); 
            cf.AVAIL_VERBS.add(verb); 
            this.messages.put(verb, message); 
            //System.out.println("Added the verb '" + verb + "', with the message, '" + message + "'."); 
        }
        s.nextLine(); 
    }
    
    /**
     * getPrimaryName
     * Returns the item's primary name
     * 
     * @ return         Returns this objects primary name as a String
     **/
    public String getPrimaryName()
    {
        return this.primaryName; 
    }
    
    /**
     * getMessageForVerb
     * Returns corresponding message string that accompanies a verb
     * 
     * @param verb                      Verb as String to look for in messages hashtable
     * @return this.messages.get(verb)  Returns the corresponding message for the verb as a String
     * @return null                     Returns null if verb is not part of the hashtable key
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
     * toString
     * Displays the Item's available verbs as a String
     * 
     * @return s        Return's each verb as a String, wokka encased for legibility
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
     * getWeight
     * Getter method for item weight
     * 
     * @return this.weight      Returns weight as integer
     */
    public int getWeight()
    {
        return this.weight; 
    }
}
