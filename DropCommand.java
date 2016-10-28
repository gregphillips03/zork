import java.util.*; 
import java.io.*; 

/**
 * DropCommand
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
 */
public class DropCommand extends Command
{
    private String itemName = ""; 
    
    DropCommand(String itemName)
    {
        this.itemName = itemName; 
    }
    
    String execute() throws InterruptedException, FileNotFoundException
    {
        String[] parts = itemName.split(" "); 
        GameState gs = GameState.instance(); 
        if(parts.length > 1)
        {
            return cogitate(parts); 
        }
        else if(parts.length == 1)
        {
            Scanner scan = new Scanner(System.in); 
            String s = ""; 
            System.out.println("Drop what?"); 
            s = scan.nextLine(); 
            String[] pieces = s.split(" "); 
            return cogitate(pieces); 
        }
        return "";
    }
   
    private static String cogitate(String [] sa)
    {   
        GameState gs = GameState.instance(); 
        for(String string : sa)
        {
            Item item = gs.getItemFromInventory(string); 
            Item onFloor = gs.getItemInVicinityNamed(string); 
            if(item != null)
            {
                gs.removeFromInventory(item); 
                gs.getAdventurersCurrentRoom().add(item); 
                return "Removed '" +item.getPrimaryName() + "' from your inventory.\n"; 
            }
            else if(onFloor != null)
            {
                return "The '" + onFloor.getPrimaryName() + "' is already in the room."; 
            }
        } 
        return "You're not carrying that item."; 
    }    
    
}
