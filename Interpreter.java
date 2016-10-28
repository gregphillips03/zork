import java.util.*; 
import java.io.*; 
import java.util.regex.*; 

/**
 * Interpreter - Contains main function of game
 * Initializes Dungeon
 * Prompts User
 * 
 * @ Author William (Greg) Phillips
 * @ Version Bork v3.0
 */
public class Interpreter
{
       
    /**
     * Main program to intialize game sequence. 
     * Creates buffered reader object to manipulate in promptUser() method.
     * Cycles until it receives the letter q from the promptUser() method. 
     * Initiates instances of Singleton GameState and CommandFactory objects
     */
    public static void main(String[] args) throws InterruptedException, FileNotFoundException
    {
        BufferedReader commandLine = new BufferedReader(new InputStreamReader(System.in));
        String s = "";
        String r = ""; 
        beginGame();     
        CommandFactory cf = CommandFactory.instance(); 
        GameState gs = GameState.instance(); 
        introMessage(); 

        do
        {
            System.out.println(gs.getAdventurersCurrentRoom().describe()); 
            s = promptUser(commandLine); 
            r = cf.parse(s).execute(); 
            System.out.println(r); 
         }
        while(!s.equals("q")); 
        KillGame.endGame(); 
    }    
    
    /**
     * Gets input from the system user.
     * @return userInput    returns text that user types into keyboard.
     */
    static String promptUser(BufferedReader commandLine)
    {
        try
        {
            String userInput = "";
            System.out.println("Brother, Enter a Command!: ");
            userInput = commandLine.readLine(); 
            return userInput; 
        }
        catch (IOException e)
        {
            System.err.println("Caught IOException: " + e.getMessage()); 
            return ""; 
        }
    }
    
    /**
     * beginGame() attempts to reduce the overhead load placed on the interpreter main method
     * De-clutters the main method by moving aesthetics and startup information
     * 
     */
    public static void beginGame() throws InterruptedException, FileNotFoundException
    {
        Scanner in = new Scanner(System.in); 
        String userFile = ""; 
        String pattern1 = ".+bork$"; 
        String pattern2 = ".+sav$";
        String pattern3 = "Bork v3.0 save data"; 
        GameState gs = GameState.instance(); 
        Dungeon d;
        
        System.out.print("Enter Data Location: ");
        userFile = in.nextLine(); 
        System.out.println(); 
        in.close(); 
        if(Pattern.matches(pattern1, userFile))
        {
           d = new Dungeon(userFile, true);
           gs.initialize(d); 
        }else if (Pattern.matches(pattern2, userFile))
        {
           Scanner scanN = new Scanner(new File(userFile)); 
           String s = scanN.nextLine(); 
           if(!s.equals(pattern3))
           {
               KillGame.gameKill(s + "/n" +
                                 "+++Data file not within System Allowable Parameters+++ \n" +
                                 "+++Check Data Type+++ \n"
                                 ); 
           }
           String whichDungeon = scanN.nextLine(); 
           int index = whichDungeon.lastIndexOf("/"); 
           String fileName = whichDungeon.substring(index +1); 
           d = new Dungeon(fileName, false); 
           scanN.close(); 
           gs.initialize(d); 
           gs.restore(userFile);            
        }
        else
        {
            String s = "+++Data file not within System Allowable Parameters+++ \n" +
                       "+++Check Data Type+++ \n"; 
            KillGame.gameKill(s); 
        } 
     }  
    
    /** 
     * Welcomes user to the dungeon by the dungeon's name
     * Provides user with initial description of entry room
     */
    public static void introMessage() throws InterruptedException
    {
       GameState gs = GameState.instance(); 
       
       System.out.println(
                                    "+++Incoming Transmission+++ \n\n" +
                                    "Battle Brother, your assistance is needed!\n" +
                                    "You have teleported to '" + gs.getDungeon().getName() + "'.\n" +
                                    "You notice the text '" + gs.getAdventurersCurrentRoom().getTitle()+ "' scrawled into the wall...\n" +
                                    "...the remnants of bloody nails linger in cermamite and plasteel...\n" +
                                    gs.getAdventurersCurrentRoom().describe()+ "\n" + 
                                    "The Emperor Protects!\n\n" +
                                    "+++Transmission End+++\n"
                          );  
    }
}
