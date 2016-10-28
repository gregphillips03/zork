import java.util.*; 
import java.io.*; 

/**
 * QuitCommand
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
 */
public class QuitCommand extends Command
{
    private String quit = ""; 
    
    QuitCommand(String q)
    {
        this.quit = q; 
    }
    
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
