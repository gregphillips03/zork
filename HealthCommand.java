import java.io.*;
/**
 * HealthCommand is an abstract extension of Command Class
 * It allows the user to ask for their current health
 * 
 * @author Shane McSally
 * @version Zork v1
 */
public class HealthCommand extends Command
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class HealthCommand
     */
    public HealthCommand()
    {
        // initialise instance variables
        x = 0;
    }

    /**
     * Gets player's current health and returns it to the user
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
