import java.util.*; 
import java.io.*; 

/**
 * SaveCommand is an abstract extension of the Command Class
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class SaveCommand extends Command
{
    private String saveFileName = ""; 
    
    /**
     * Constructor for objects of Class SaveCommand
     * 
     * @param f     Filename of save file
     */
    SaveCommand(String f)
    {
        this.saveFileName = f; 
    }
    
    /**
     * Method call to begin persistence sequence / hands focus to GameState Class
     * 
     * @throw InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throw FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * 
     * @return  Returns string message to user "+++Actions Saved+++"
     */
    String execute() throws InterruptedException, FileNotFoundException
    {
        GameState gs = GameState.instance(); 
        gs.store(saveFileName); 
        return "\n+++Actions Saved+++\n"; 
    }
}
