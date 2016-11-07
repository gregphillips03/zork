import java.io.*;
/**
 * UnlockCommand is an abstract extension of Command Class
 * It allows the user to interact with locked doors
 * 
 * @author Shane McSally
 * @version Zork v1
 */
public class UnlockCommand extends Command
{
    private String key;
    private String door;
    
    /**
     * Constructor for objects of Class UnlockCommand
     * 
     * @param key   String name of item that unlocks the door
     * @param door  String name of the exit that is unlocked by item
     */
    UnlockCommand(String key, String door)
    {
        
    }
    
    /**
     * Attempts to unlock door with given parameters
     * 
     * @return String message to user
     * 
     * @throw InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throw FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * 
     */
    String execute() throws InterruptedException, FileNotFoundException
    {
        return "test";
    }
}
