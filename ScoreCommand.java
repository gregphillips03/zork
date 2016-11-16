import java.io.*;
/**
 * ScoreCommand is an abstract extension of Command Class
 * It allows the user to ask for their current score
 * 
 * @author Shane McSally
 * @version Zork v1
 */
public class ScoreCommand extends Command
{
    /**
     * Constructor for objects of class ScoreCommand
     */
    public ScoreCommand()
    {
       
    }

    /**
     * Gets player's current score and returns it to the user
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
