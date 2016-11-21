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
    /**
     * Constructor for objects of class HealthCommand
     */
    public HealthCommand()
    {
    
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
        GameState gs = GameState.instance();
        double hp = 0;
        
        hp += gs.getHealth();
        
        if(hp <= 5)
        {
            return "You are on the brink of death my friend. Find something to help quickly!";
        }
        else if(hp > 5 && hp <= 10)
        {
            return "You are in terrible agony. Find something to subdue the pain.";
        }
        else if(hp > 10 && hp <= 15)
        {
            return "You are in considerable pain.";
        }
        else if(hp > 15 && hp <= 20)
        {
            return "You are beginning to feel bogged down.";
        }
        else if(hp > 20 && hp < 25)
        {
            return "You are feeling nearly perfect! There may however be a rock in your shoe!";
        }
        else
        {
            return "You are feeling perfect!";
        }
    }
}
