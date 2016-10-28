import java.util.*; 
import java.io.*; 

/**
 * Write a description of class TakeCommand here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TakeCommand extends Command
{
    private String itemName = ""; 
    
    TakeCommand(String itemName)
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
            System.out.println("Take what?"); 
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
