import java.util.*; 
import java.io.*; 

/**
 * InventoryCommand is an abstract extension of the Command Class. It allows the user to view items they are currently carrying. 
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
     * Displays user's items in inventory as String if user is carrying items
     * 
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * 
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
