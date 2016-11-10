import java.io.*;
/**
 * UnlockCommand is an abstract extension of Command Class
 * It allows the user to interact with locked doors
 * 
 * @author Shane McSally
 * @version Zork v1
 */
public class Combat 
{
    private int playerHealth;
    private int attackerName;
    private String winText;
    private String lossText;
    private Boolean victory;
    private boolean testValue = true;
    /**
     * Constructor for objects of Class Combat
     * 
     * @param d   Denizen player has encountered
     */
    boolean Combat(Denizen d)
    {
        //Insert Combat logic here
        return true;
    }
    
    /**
     * @return winText or lossText depending on results of Combat
     */
    String resultText(){
        //logic for win or loss text (based on Combat boolean)
        return "";
    }
    /**
     * @return Player health for comparison
     */
    int getPlayerHealth(){
        return 100; //temp value
    }
   
}
