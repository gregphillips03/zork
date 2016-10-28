import java.util.*; 
import java.io.*; 

/**
 * UnknownCommand 
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
 */
public class UnknownCommand extends Command
{
    private String bogusCommand = ""; 
    
    UnknownCommand(String bogusCommand)
    {
        this.bogusCommand = bogusCommand; 
    }
    
    String execute() throws InterruptedException, FileNotFoundException
    {
        String s = "This AI does not comprehend '" + bogusCommand + "'."; 
        return s; 
    }
}
