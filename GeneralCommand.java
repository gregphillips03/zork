import java.util.*; 
import java.io.*; 

/**
 * GeneralCommand is an abstract extension of Command Class. Takes generalized input from user and determines how to implement. Currently "look" and "exits" input.
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class GeneralCommand extends Command 
{
    String gen = ""; 
    
    /**
     * Constructor for objects of Class GeneralCommand
     * 
     * @param gen       String "look" or "exits" from CommandFactory parsed input
     */
    GeneralCommand(String gen)
    {
        this.gen = gen; 
    }
    
    /**
     * If "look" displays to user item and npcs in room as well as the rooms description. 
     * If "exits" displays to user available room exits. 
     * 
     * @return String message to user. 
     * 
     * @throw InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throw FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     */
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
            for(Denizen den: gs.getAdventurersCurrentRoom().npcHere)
            {
                s = s + "A '" + den.getName() + "' is here." + "\n"; 
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
