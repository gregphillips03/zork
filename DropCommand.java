import java.util.*; 
import java.io.*; 

/**
 * DropCommand Class is an abstract extension of the Command Class. The user can attempt to drop items they have in inventory. 
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class DropCommand extends Command
{
    private String itemName = ""; 
    
    /**
     * Constructor for objects of Class DropCommand
     * 
     * @param itemName      String sent from CommandFactory to parse
     */
    DropCommand(String itemName)
    {
        this.itemName = itemName; 
    }
    
    /**
     * Takes String input and splits it. If String array is >1, it calls Cogitate. If not, it asks for more input before calling cogitate
     * 
     * @throw InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throw FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * 
     * @return  String message to user.
     */
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
   
    /**
     * Determines if the user is carrying an item, or if it is in the room. If item can be droppped, it's removed from the user's inventory and added to the room. 
     * 
     * @param sa    String Array of input split by execute method.
     * @return      String message to user based on success or failure when dropping an item. 
     */
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
