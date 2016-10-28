import java.util.*; 
import java.io.*; 

/**
 * SaveCommand
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
 */
public class SaveCommand extends Command
{
    private String saveFileName = ""; 
    
    SaveCommand(String f)
    {
        this.saveFileName = f; 
    }
    
    String execute() throws InterruptedException, FileNotFoundException
    {
        GameState gs = GameState.instance(); 
        gs.store(saveFileName); 
        return "\n+++Actions Saved+++\n"; 
    }
}
