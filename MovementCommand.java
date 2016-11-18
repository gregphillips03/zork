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
     * Attempts to move user between Room objects
     * 
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * 
     * @return      String based on movement success or fail
     */
    String execute() throws InterruptedException, FileNotFoundException
    {
        GameState gs = GameState.instance(); 
        Room room = gs.getAdventurersCurrentRoom().leaveBy(dir); 
        if(room != null)
        {
            gs.setAdventurersCurrentRoom(room);
            Denizen.moveDenizens(); 
            
            gs.setHealth(0.125);
            return "Movement Complete"; 
        }
        return "You can't go '" + this.dir + "' from " + gs.getAdventurersCurrentRoom().getTitle() + "."; 
    }
}
