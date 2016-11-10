import java.util.*; 
import java.io.*; 

/**
 * UnknownCommand is an abstract extension of Command Class
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class UnknownCommand extends Command
{
    private String bogusCommand = ""; 
    
    /**
     * Constructor for objects of Class UnknownCommand
     * 
     * @param bogusCommand      String input from CommandFactory where input could not be parsed into correct abstract Command object
     */
    UnknownCommand(String bogusCommand)
    {
        this.bogusCommand = bogusCommand; 
    }
    
    /**
     * Default message to System out
     * 
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * 
     * @return    String message to user that programs does not understand the input
     */
    String execute() throws InterruptedException, FileNotFoundException
    {
        String s = "This AI does not comprehend '" + bogusCommand + "'."; 
        return s; 
    }
}
