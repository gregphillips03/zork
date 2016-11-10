import java.io.*;
/**
 * ReloadCommand is an abstract extension of Command Class
 * It allows the user to reload the state of the game to the
 * most recent sav state in the event that something happens that makes the
 * player unhappy
 * 
 * @author Joshua Wright
 * @version TeamBork v1
 */
public class ReloadCommand extends Command
{
    private String saveFile;
    
    /**
     * Constructor for objects of Class ReloadCommand
     * 
     * @param name   String that contains the command
     */
    ReloadCommand(String name)
    {
        
    }
    
    /**
     * Attempts to reload the game
     * 
     * @return String message to user
     * 
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * 
     */
    String execute() throws InterruptedException, FileNotFoundException
    {
        return "test";
    }
   
}