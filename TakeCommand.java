import java.util.*; 
import java.io.*; 

/**
 * TakeCommand is an abstract extension of the Command Class. 
 * It allows the user to take items and add them to their inventory. 
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class TakeCommand extends Command
{
    private String itemName = ""; 
    
    /**
     * Constructor for objects of Class TakeCommand
     * 
     * @param itemName      Name of item as String
     */
    TakeCommand(String itemName)
    {
        this.itemName = itemName; 
    }
    
    /**
     * Takes String input and splits it. If String array is >1, it calls Cogitate. If not, it asks for more input before calling cogitate
     * 
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
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
            System.out.println("Take what?"); 
            s = scan.nextLine(); 
            String[] pieces = s.split(" "); 
            return cogitate(pieces); 
        }
        return "";
    }
    
    /**
     * Determines if the user can take an item based on whether it's in the room, how heavy it is, and whether or not they already have it. 
     * 
     * @param sa    String Array of input split by execute method
     * @return      String message to user based on success of take action. 
     */
    private static String cogitate(String [] sa)
    {   
        GameState gs = GameState.instance(); 
        for(String string : sa)
        {
            Item item = gs.getItemInVicinityNamed(string); 
            Item carr = gs.getItemFromInventory(string); 
            if(item != null && item.getWeight() != 99999)
            {
                String s = ""; 
                gs.addToInventory(item); 
                gs.getAdventurersCurrentRoom().remove(item); 
                s = s + "Added '" +item.getPrimaryName() + "' to your inventory.\n"; 
                s = s + "You can " + item.toString() + "the '" + item.getPrimaryName() + "'."; 
                return s; 
            }
            else if(item != null && item.getWeight() == 99999)
            {
                String r = "You can't take the '" + item.getPrimaryName() + "' with you.\n" +
                           "It's too heavy.\n" +
                           "You can interact with it instead."; 
                return r; 
            }
            else if(carr != null)
            {
                return "You already have the '" + carr.getPrimaryName() +"'."; 
            }
        } 
        return "That item isn't here"; 
    }
}
