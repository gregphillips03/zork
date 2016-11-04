import java.util.*; 
import java.io.*; 

/**
 * CommandFactory generates commands from parsed string text
 * Singleton Class
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
class CommandFactory
{
    static CommandFactory commandFactory = new CommandFactory();
    public static List<String> MOVEMENT_COMMANDS = Arrays.asList("n", "w", "e", "s", "u", "d");
    public static List<String> SAVE_COMMANDS = Arrays.asList("save", "Save", "SAVE"); 
    public static List<String> TAKE_COMMANDS = Arrays.asList("take", "Take", "TAKE"); 
    public static List<String> DROP_COMMANDS = Arrays.asList("drop", "Drop", "DROP"); 
    public static List<String> QUIT_COMMANDS = Arrays.asList("q", "quit"); 
    public static List<String> GENERAL_COMMANDS = Arrays.asList("exits", "look");
    public static List<String> INV_COMMANDS = Arrays.asList("i", "inventory"); 
    public static List<String> AVAIL_VERBS = new ArrayList<>(); 
    
    /**
     * Constructor for objects of class CommandFactory
     */
    private CommandFactory()
    {
        // private to control object creation 
    }

    /**
     * Exists to control object creation 
     * 
     * @return commandFactory   returns commandFactory object
     */
    public static CommandFactory instance() 
    {
        return commandFactory; 
    }
    
    /**
     * parse
     * Parses text from userInput / handed focus from Interpreter Class
     * 
     * @return move     Returns abstract Command in the form of a MovementCommand object
     * @return save     Returns abstract Command in the form of a SaveCommand object
     * @return inv      Returns abstract Command in the form of a InventoryCommand object 
     * @return q        Returns abstract Command in the form of a QuitCommand object
     * @return gen      Returns abstract Command in the form of a GeneralCommand object
     * @return take     Returns abstract Command in the form of a TakeCommand object
     * @return drop     Returns abstract Command in the form of a DropCommand object
     * @return spec     Returns abstract Command in the form of a ItemSpecificCommand object
     * @return unk      Returns abstract Command in the form of a UnknownCommand object
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
