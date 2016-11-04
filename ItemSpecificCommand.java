
/**
 * ItemSpecificCommand is an abstract extension of the Command Class
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class ItemSpecificCommand extends Command
{
    private String verb = ""; 
    private String noun = ""; 
    
    /**
     * Constructor for objects of Class ItemSpecificCommand
     * 
     * @param verb      Item's action verb as a String
     * @param noun      Item's pairing message sent from CommandFactory as complete input
     */
    ItemSpecificCommand(String verb, String noun)
    {
        this.verb = verb; 
        this.noun = noun; 
    }
    
    /**
     * execute
     * Searches user's inventory and room for item. If found it attempts to pair it with the corresponding verb.
     * 
     * @return s    Returns paired message for specified verb / from inventory
     * @return s    String message indicating user cannot perform indicated verb on said object / from inventory
     * @return s    Returns paired message for specified verb / from room
     * @return s    String message indicating user cannot perform indicated verb on said object / from room
     */
    String execute()
    {
        String [] parts = noun.split(" "); 
        GameState gs = GameState.instance(); 
        Room room = gs.getAdventurersCurrentRoom();
        String s = "";
        for(String string : parts)
        {
            Item item = gs.getItemFromInventory(string); 
            if(item != null)
            {
                s = item.getMessageForVerb(verb); 
                if(s != null)
                {
                    return s; 
                }
                else
                {
                    s = "You can't '" + verb + "' that."; 
                    return s; 
                }
            }
        }
        
        for(String string: parts)
        {
            Item item = gs.getItemInVicinityNamed(string); 
            if(item != null)
            {
                s = item.getMessageForVerb(verb); 
                if(s != null)
                {
                    return s; 
                }
                else
                {
                    s = "You can't '" + verb + "' that."; 
                    return s; 
                }
            }            
        }
        
        return ""; 
    }
}
