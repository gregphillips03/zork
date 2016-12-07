import java.util.*; 
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
    private String reload = "";
    GameState gs = GameState.instance();
    /**
     * Constructor for objects of Class ReloadCommand
     * 
     * @param name   String that contains the command
     */
    ReloadCommand(String reload)
    {
        this.reload = reload;
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
       if((this.reload.equals("reload") || this.reload.equals("restart"))  && gs.saveFile != "")
        { 
           String pattern = "Group Bork v1.0 save data";
           Scanner scanN = new Scanner(new File(gs.saveFile)); 
           String s = scanN.nextLine(); 
           System.out.println("+++Atempting to reload state+++");
           if(!s.equals(pattern))
           {
               KillGame.gameKill(s + "/n" +
                                 "+++Data file not within System Allowable Parameters+++ \n" +
                                 "+++Check Data Type+++ \n"
                                 ); 
           }
           String whichDungeon = scanN.nextLine(); 
           int index = whichDungeon.lastIndexOf("/"); 
           String fileName = whichDungeon.substring(index +1); 
           Dungeon d = new Dungeon(fileName, false); 
           scanN.close(); 
           gs.initialize(d); 
           gs.restore(gs.saveFile);
           System.out.println("+++Current session returned to more recent saved state+++");
            return ""; 
        }
        else
        {
            System.out.println("+++Reload state failed+++");
            return ""; 
        }
    }
   
}