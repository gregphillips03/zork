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
    /**
     * key   String name of item that unlocks the lockedObject
     * lockedObject  String name of the object that is unlocked by key
     */
    private String key;
    private String lockedObject;
    private String command;

    /**
     * Constructor for objects of Class UnlockCommand
     * 
     * @param command   String that the user entered that will be parsed to find the key and locked object
     */
    UnlockCommand(String command)
    {
        this.command = command;
    }

    /**
     * Attempts to unlock door with given parameters
     * 
     * @return String message to user
     * 
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * 
     */
    String execute() throws InterruptedException, FileNotFoundException
    {
        String response = "Exit is now unlocked...proceed.";
        String [] parts = this.command.split(" "); 
        if(parts.length == 1)
        {
            return "test";
        }
        // parts[0] is the word "unlock"...that is how we got here!
        //parts[1] is the locked object
        this.lockedObject = parts[1];

        //parts[2] is 'with'

        //parts[3] is the key needed
        this.key = parts[3];

        //Make sure the object requesting to be unlocked is here
        GameState gs = GameState.instance(); 
        Exit lockedExit = null; 

        for(Exit exit : gs.getAdventurersCurrentRoom().roomExits)
        {
            if (exit.getLockedObject().equals(lockedObject)) 
            {
                lockedExit = exit;
            }
        }

        if (lockedExit == null)
        {
            response = "I don't know what '" + lockedObject + "' means.";
        }
        else
        {
            if (lockedExit.isLocked())
            {
                //Make sure the key needed to unlock the object is in the users inventory as well as is an actual key!
                if (gs.getItemFromInventory(key) == null)
                {
                    response = "You are not in possesion of a '" + key + "'";
                }
                else
                { 
                    //unlock the exit 
                    for(String key: gs.getDungeon().collection.keySet())
                    {
                        Room rm = gs.getDungeon().getRoom(key);
                        for(Exit exit : rm.roomExits)
                        {
                            if (exit.getLockedObject().equals(lockedObject)) 
                            {
                                exit.unlock();
                            }
                        }
                    }
                }
            }
            else
            {
                response = "I don't know what '" + lockedObject + "' means.";
            }

        }

        return response;
    }

}
