import java.util.*; 
import java.io.*; 

/**
 * The HoloPlinth class handles special interactions with the holoplinth item. 
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1.2
 */
public class HoloPlinth
{
    static HoloPlinth holoPlinth = new HoloPlinth();
    static final String passPhrase = "golgotha"; 
    static boolean isLocked = true; 
    
    /**
     * Constructor for objects of class HoloPlinth. Singleton class controls creation
     */
    public HoloPlinth()
    {
        //exists to control object creation
    }

    /**
     * Exists to control object creation.
     * 
     * @return      HoloPlinth object to work with. 
     */
    public static HoloPlinth instance()
    {
        return holoPlinth; 
    }
    
    /**
     * Interaction with user to unlock Holoplinth. Prompts user for passphrase and unlocks the holoplinth if guessed correctly. 
     * 
     * @return      String message to user
     */
    public String unlock()
    {
        Scanner scan = new Scanner(System.in);
        GameState gs = GameState.instance(); 
        String s = "Invalid Input"; 
        
        if(!isLocked)
        {
            s = "This holoplinth is already unlocked"; 
            return s; 
        }
        System.out.println("Enter passphrase: "); 
        String userCode = scan.nextLine().toLowerCase(); 
        if(userCode.equals(passPhrase))
        {
            isLocked = false; 
            gs.setScore(25); 
            s = "Input Accepted...\nHoloplinth unlocked."; 
        }
        return s; 
    }
}
