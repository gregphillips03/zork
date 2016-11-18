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
        GameState gs = GameState.instance();
        int score = 0;
        String message = "";

        score += gs.getScore();

        if(score == 0)
        {
            message = "Explore the dungeon and acquire some points!";
            return score + " " + message;
        }
        else if(score > 0 && score <= 5)
        {
            message = "Good start. Keep going!";
            return score + " " + message;
        }
        else if(score > 5 && score <= 10)
        {
            message = "Good job! You've reached rank I.";
            return score + " " + message;
        }
        else if(score > 10 && score <= 15)
        {
            message = "Wow! You've reached rank II.";
            return score + " " + message;
        }
        else
        {
            message = "This is a test case";
            return score + " " + message;
        }
    }
}
        
