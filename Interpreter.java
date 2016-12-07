import java.util.*; 
import java.io.*; 
import java.util.regex.*; 

/**
 * Contains main function of game. Initializes Dungeon. Prompts User for input. 
 * 
 * @author William (Greg) Phillips
 * @version Zork v1.2
 */
public class Interpreter
{
       
    /**
     * Main program to intialize game sequence. Creates buffered reader object to manipulate in promptUser() method. 
     * Cycles until it receives the letter q from the promptUser() method. 
     * Initiates instances of Singleton GameState and CommandFactory objects
     * 
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * @param args                       Commandline string arguments
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
        int count = 0;
        do
        {
            System.out.println(gs.getAdventurersCurrentRoom().describe()); 
            s = promptUser(commandLine); 
            r = cf.parse(s).execute(); 
            System.out.println(r);
            if ( count % 2 == 0) {
                new RandomEvent();
            }
            count += 1;
         }
        while(!s.equals("q") && gs.playerHealth > 0); 
        KillGame.endGame(); 
    }    
    
    /**
     * Gets input from the system user.
     * 
     * @param commandLine   BufferedReader object handed focus from main. 
     * @return userInput    Text that user types into keyboard.
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
     * Begins game by either starting a new dungeon, or hydrating from save file. Helper method to declutter main
     * 
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     */
    public static void beginGame() throws InterruptedException, FileNotFoundException
    {
        Scanner in = new Scanner(System.in); 
        String userFile = ""; 
        String pattern1 = ".+bork$"; 
        String pattern2 = ".+sav$";
        String pattern3 = "Group Bork v1.0 save data"; 
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
     * Welcomes user to the dungeon by the dungeon's name, and provides user with initial description of entry room. Helper method to declutter main
     * 
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack
     */
    public static void introMessage() throws InterruptedException
    {
       GameState gs = GameState.instance(); 
       
       System.out.println(
                                    "+++Incoming Transmission+++ \n\n" +
                                    "Battle Brother, your assistance is needed!\n" +
                                    "You have teleported to '" + gs.getDungeon().getName() + "'.\n" +
                                    gs.getAdventurersCurrentRoom().describe()+ "\n" + 
                                    "The Emperor Protects!\n\n" +
                                    "+++Transmission End+++\n"
                          );  
    }
}
