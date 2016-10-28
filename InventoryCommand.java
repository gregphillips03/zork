import java.util.*; 
import java.io.*; 

/**
 * InventoryCommand
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
 */
public class InventoryCommand extends Command
{
    InventoryCommand()
    {
    
    }
    
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
