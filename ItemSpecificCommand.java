
/**
 * ItemSpecificCommand
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
 */
public class ItemSpecificCommand extends Command
{
    private String verb = ""; 
    private String noun = ""; 
    
    ItemSpecificCommand(String verb, String noun)
    {
        this.verb = verb; 
        this.noun = noun; 
    }
    
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
