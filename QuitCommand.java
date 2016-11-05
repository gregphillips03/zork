import java.util.*; 
import java.io.*; 

/**
 * QuitCommand is an abstract extension of the Command Class. Quits the game.
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class QuitCommand extends Command
{
    private String quit = ""; 
    
    /**
     * Constructor for objects of Class QuitCommand
     * 
     * @param q     String from CommandFactory indicating to quit game
     */
    QuitCommand(String q)
    {
        this.quit = q; 
    }
    
    /**
     * Quits the game and calls KillGame to kill the thread
     * 
     * @throw InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throw FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * 
     * @return      Returns empty string
     */
    String execute() throws InterruptedException, FileNotFoundException
    {
        if(this.quit.equals("q"))
        {
            return ""; 
        }
        else if(this.quit.equals("quit"))
        {
            KillGame.endGame(); 
            return ""; 
        }
        return ""; 
    }
}
