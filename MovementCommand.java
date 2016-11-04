import java.util.*; 
import java.io.*; 

/**
 * MovementCommand is an abstract extension of the Command Class
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class MovementCommand extends Command
{
    private String dir = ""; 
    
    /**
     * Constructor for objects of Class MovementCommand
     * 
     * @param dir   Direction as String to move user
     */
    MovementCommand(String dir)
    {
        this.dir = dir; 
    }
    
    /**
     * execute
     * Attempts to move user between Room objects
     * 
     * @return      String for successful movement
     * @return      String for unsuccessful movement
     */
    String execute() throws InterruptedException, FileNotFoundException
    {
        GameState gs = GameState.instance(); 
        Room room = gs.getAdventurersCurrentRoom().leaveBy(dir); 
        if(room != null)
        {
            gs.setAdventurersCurrentRoom(room);
            Denizen.moveDenizens(); 
            return "Movement Complete"; 
        }
        return "You can't go '" + this.dir + "' from " + gs.getAdventurersCurrentRoom().getTitle() + "."; 
    }
}
