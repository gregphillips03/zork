import java.util.*; 
import java.io.*; 

/**
 * InventoryCommand is an abstract extension of the Command Class
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class InventoryCommand extends Command
{
    /**
     * Constructor for object of Class InventoryCommand
     */
    InventoryCommand()
    {
        //no necessary implementation
    }
    
    /**
     * execute
     * Displays user's items in inventory as String if user is carrying items
     * 
     * @return      Indicates user isn't carrying items
     * @return s    Returns items in user's inventory as String
     */
    String execute() throws InterruptedException, FileNotFoundException
    {
        GameState gs = GameState.instance(); 
        String s = ""; 
        for(Item item : gs.carriedItems)
        {
            s = s + "You have a '" + item.getPrimaryName() + "'.\n"; 
        }
        if(s.equals(""))
        {
            return "You have no items."; 
        }
        return s; 
    }
}
