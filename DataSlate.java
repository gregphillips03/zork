import java.util.*; 
import java.io.*; 

/**
 * The DataSlate class handles special interactions with the dataslate item.
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1.2
 */
public class DataSlate
{
    static DataSlate dataSlate = new DataSlate(); 
    static final int code = 3324; 
    static boolean isLocked = true; 
    
    /**
     * Constructor for objects of class DataSlate. Singleton class controls creation. 
     */
    private DataSlate()
    {
        //exists to control object creation
    }

    /**
     * Exists to control object creation.
     * 
     * @return      DataSlate object to work with. 
     */
    public static DataSlate instance()
    {
        return dataSlate; 
    }
    
    /**
     * Interaction with user to unlock Dataslate. Prompts user for four digit code and unlocks the dataslate if guessed correctly. 
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
            s = "This dataslate is already unlocked"; 
            return s; 
        }
        System.out.println("Enter four digit code: "); 
        int userCode = scan.nextInt(); 
        if(userCode == code)
        {
            isLocked = false; 
            gs.setScore(50); 
            s = "Input Accepted...\nDataslate unlocked."; 
        }
        return s; 
    }
}
