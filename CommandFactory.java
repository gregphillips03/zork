import java.util.*; 
import java.io.*; 

/**
 * Generates commands from parsed string text. Singleton Class.
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
class CommandFactory
{
    static CommandFactory commandFactory = new CommandFactory();
    public static List<String> MOVEMENT_COMMANDS = Arrays.asList("n", "w", "e", "s", "u", "d");
    public static List<String> SAVE_COMMANDS = Arrays.asList("save", "Save", "SAVE");
    public static List<String> STEAL_COMMANDS = Arrays.asList("steal", "STEAL", "Steal"); 
    public static List<String> TAKE_COMMANDS = Arrays.asList("take", "Take", "TAKE"); 
    public static List<String> DROP_COMMANDS = Arrays.asList("drop", "Drop", "DROP"); 
    public static List<String> QUIT_COMMANDS = Arrays.asList("q", "quit"); 
    public static List<String> GENERAL_COMMANDS = Arrays.asList("exits", "look");
    public static List<String> INV_COMMANDS = Arrays.asList("i", "inventory"); 
    public static List<String> SCORE_COMMANDS = Arrays.asList("score", "Score", "SCORE");
    public static List<String> HEALTH_COMMANDS = Arrays.asList("health", "Health", "HEALTH");
    public static List<String> ATTACK_COMMANDS = Arrays.asList("attack", "Attack", "ATTACK", "fight", "Fight", "FIGHT");
    public static List<String> AVAIL_VERBS = new ArrayList<>(); 
    GameState gs = GameState.instance(); 
    /**
     * Constructor for objects of class CommandFactory.
     */
    private CommandFactory()
    {
        // private to control object creation 
    }

    /**
     * Exists to control object creation.
     * 
     * @return      Returns instance of commandFactory object.
     */
    public static CommandFactory instance() 
    {
        return commandFactory; 
    }
    
    /**
     * Parses text from userInput. Handed focus from Interpreter Class.
     * 
     * @param commandString              String input from Interpreter Class as input by end user.
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * @return                           Appropriate abstract Command object.
     */
    Command parse(String commandString) throws InterruptedException, FileNotFoundException
    {
        String [] parts = commandString.split(" "); 
        for (String string : parts)
        {
            if(MOVEMENT_COMMANDS.contains(string))
            {
                MovementCommand move = new MovementCommand(string);            
                return move; 
            }
            else if(SAVE_COMMANDS.contains(string))
            {
                SaveCommand save = new SaveCommand("saveprogress.sav"); 
                return save; 
            }
            else if(INV_COMMANDS.contains(string))
            {
                InventoryCommand inv = new InventoryCommand(); 
                return inv; 
            }
            else if(QUIT_COMMANDS.contains(string))
            {
                QuitCommand q = new QuitCommand(string); 
                return q; 
            }
            else if(GENERAL_COMMANDS.contains(string))
            {
                GeneralCommand gen = new GeneralCommand(string); 
                return gen; 
            }
            else if(TAKE_COMMANDS.contains(string))
            {
                TakeCommand take = new TakeCommand(commandString); 
                return take; 
            }
            else if(DROP_COMMANDS.contains(string))
            {
                DropCommand drop = new DropCommand(commandString); 
                return drop; 
            }
            else if(SCORE_COMMANDS.contains(string) && gs.gameType.equals("new"))
            {
                ScoreCommand score = new ScoreCommand();
                return score;
            }
            else if(HEALTH_COMMANDS.contains(string) && gs.gameType.equals("new"))
            {
                HealthCommand health = new HealthCommand();
                return health;
            }
            else if(STEAL_COMMANDS.contains(string))
            {
                StealCommand steal = new StealCommand(commandString); 
                return steal; 
            }
            else if(ATTACK_COMMANDS.contains(string)){
                AttackCommand attack = new AttackCommand(commandString);
                return attack;
            }
            else if(AVAIL_VERBS.contains(string))
            {
                ItemSpecificCommand spec = new ItemSpecificCommand(string, commandString); 
                return spec; 
            }
        }
        UnknownCommand unk = new UnknownCommand(commandString); 
        return unk; 
    }
}
