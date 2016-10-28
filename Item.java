import java.util.*; 
import java.io.*; 

/**
 * Item
 * 
 * @author William (Greg) Phillips
 * @version Bork v3.0
 */
public class Item
{
    private String primaryName = ""; 
    private int weight = 0; 
    private Hashtable<String, String> messages = new Hashtable<String, String>(); 
    
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
     * goesBy
     * @ param name     item name to look up
     * @ return         returns true if item is known by a certain name
     **/
    public boolean goesBy(String name)
    {
        return false; 
    }
    
    /**
     * getPrimaryName
     * @ return         returns this objects primary name
     **/
    public String getPrimaryName()
    {
        return this.primaryName; 
    }
    
    public String getMessageForVerb(String verb)
    {
        if(this.messages.containsKey(verb))
        {
            return this.messages.get(verb); 
        }
        //System.out.println(verb + " not found.\n"); 
        return null; 
    }
    
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
    
    public int getWeight()
    {
        return this.weight; 
    }
}
