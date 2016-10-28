import java.util.*; 
import java.io.*; 

/**
 * GeneralCommand
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
 */
public class GeneralCommand extends Command 
{
    String gen = ""; 
    
    GeneralCommand(String gen)
    {
        this.gen = gen; 
    }
    
    String execute() throws InterruptedException, FileNotFoundException
    {
        String s = ""; 
        GameState gs = GameState.instance(); 
        
        if(this.gen.equals("look"))
        {
            s = s + gs.getAdventurersCurrentRoom().getDesc() + "\n"; 
            for(Item item : gs.getAdventurersCurrentRoom().roomItems)
            {
                s = s + "There is a '" + item.getPrimaryName() + "' here." + "\n"; 
            }
            return s; 
        }
        else if(this.gen.equals("exits"))
        {
            for(Exit exit : gs.getAdventurersCurrentRoom().exitPath)
            {
                s = s + exit.describe() + "\n"; 
            }
            return s; 
        }
        return ""; 
    }
}
