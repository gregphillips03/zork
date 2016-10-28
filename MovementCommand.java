import java.util.*; 
import java.io.*; 

/**
 * MovementCommand
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
 */
public class MovementCommand extends Command
{
    private String dir = ""; 
    
    MovementCommand(String dir)
    {
        this.dir = dir; 
    }
    
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
