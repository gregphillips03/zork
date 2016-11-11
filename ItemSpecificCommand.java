
/**
 * ItemSpecificCommand is an abstract extension of the Command Class. It allows the user to perform specific actions with items. 
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
     * Searches user's inventory and room for item. If found it attempts to pair it with the corresponding verb.
     * 
     * @return      String message to user. 
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
                    //possible area to check for event and verb combo
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
                    //possible area to check for event and verb combo                
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
