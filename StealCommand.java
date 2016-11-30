import java.util.*; 
import java.io.*; 

/**
 * StealCommand is an abstract extension of the Command Class. It handles all instances where a user attempts to steal items from NPCs. 
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1.2
 */
public class StealCommand extends Command
{
    private String npcName = ""; 
    
    /**
     * Constructor for objects of Class StealCommand
     * 
     * @param f     Filename of save file
     */
    StealCommand(String npcName)
    {
        this.npcName = npcName; 
    }
    
    /**
     * Random number generator
     * 
     * @param min   Bottom limit for random number
     * @param max   Upper limit fo random number
     * @return      randomly generated integer
     */
    static int randInt(int min, int max)
    {
        Random rand = new Random(); 
        int randomNum = rand.nextInt((max - min) + 1) + min; 
        return randomNum; 
    }    
        
    /**
     * Takes String input and splits it. If String array is less than 1, it calls Cogitate. If not, it asks for more input before calling cogitate
     * 
     * @throws InterruptedException      Pushes thread sleep disruptions up the stack 
     * @throws FileNotFoundException     Pushes IO exception up the stack where not explicitly handled
     * 
     * @return  String message to user. 
     */
    String execute() throws InterruptedException, FileNotFoundException
    {
        String[] parts = npcName.split(" "); 
        GameState gs = GameState.instance(); 
        Room room = gs.getAdventurersCurrentRoom(); 
        
        if(room.npcHere.isEmpty())
        {
            gs.setScore(-10); 
            return "There's no one here to steal from."; 
        }
        
        if(parts.length > 1)
        {
            return cogitate(parts, room); 
        }
        else if(parts.length == 1)
        {
            Scanner scan = new Scanner(System.in); 
            String s = ""; 
            System.out.println("Steal from who?"); 
            s = scan.nextLine(); 
            String[] pieces = s.split(" "); 
            return cogitate(pieces, room); 
        }
        return "";
    }
    
    /**
     * Determines if there is a valid NPC to steal from
     * 
     * @param sa    String Array of input split by execute method
     * @param rm    Room location of user
     * @return      String message to user based on success of take action. 
     */
    private static String cogitate(String [] sa, Room rm)
    {   
        GameState gs = GameState.instance(); 
        String s = "Could not locate the NPC you specified."; 
        
        for(Denizen npc : rm.npcHere)
        {
            for(String string : sa)
            {
                if(sa.equals(npc.getName()))
                {
                    s = tryStealFromNPC(npc); 
                }
            }
        }

        return s; 
    }
    
    /**
     * Trys to steal from NPC in room
     * 
     * @param npc       NPC object to attempt to steal from
     * @return          String message to user
     */
    private static String tryStealFromNPC(Denizen npc)
    {
        String s = "";
        GameState gs = GameState.instance(); 
        
        if(npc.carriedItems.isEmpty() && npc.getMood() == false)
        {
            int i = randInt(0, 9);
            switch(i)
            {
                case 0:                     npc.setMood(true); 
                                            s = "Attempt to steal from '" + npc.getName() + "' failed. You made it mad.\n";
                                            gs.setHealth(gs.getHealth()/2); 
                                            s = s + "The '" + npc.getName() + "' strikes you hard in the face.\n";
                                            s = s + "Check your health!\n"; 
                                            gs.setScore(-10); 
                                            break; 
                
                case 1:                     npc.setMood(true);  
                                            s = "Attempt to steal from '" + npc.getName() + "' failed. You made it mad.\n";
                                            gs.setHealth(gs.getHealth()/3); 
                                            s = s + "The '" + npc.getName() + "' gives you a solid blow to the ribs.\n";
                                            s = s + "Check your health!\n";
                                            gs.setScore(-10); 
                                            break; 
                                    
                case 2:                     npc.setMood(true);  
                                            s = "Attempt to steal from '" + npc.getName() + "' failed. You made it mad.\n";
                                            gs.setHealth(gs.getHealth()/4); 
                                            s = s + "The '" + npc.getName() + "' gives you a glancing blow to the side.\n";
                                            s = s + "Check your health!\n";
                                            gs.setScore(-10); 
                                            break; 
                
                case 3: case 4: case 5:     npc.setMood(true);  
                                            s = "Attempt to steal from '" + npc.getName() + "' failed. You made it mad.\n";
                                            s = s + "The '" + npc.getName() + "' swings at you, but you dodge out of the way.\n"; 
                                            gs.setScore(-10); 
                                            break;  
                
                case 6: case 7:             npc.setMood(true); 
                                            s = "Attempt to steal from '" + npc.getName() + "' failed. You made it mad.\n";
                                            s = s + "The '" + npc.getName() + "' swings at you, but you dodge out of the way.\n"; 
                                            s = s + "The '" + npc.getName() + "' wasn't carrying anything to steal.\n"; 
                                            break; 
                                    
                default:                    s = "The '" + npc.getName() + "' wasn't carrying anything to steal.\n";
                                            s = s + "The '" + npc.getName() + "' doesn't appear to have noticed you.\n"; 
                                            break;              
            }
            return s; 
        }
        else if(npc.carriedItems.isEmpty() && npc.getMood() == true)
        {
            int i = randInt(0, 9);
            switch(i)
            {
                case 0:                     s = "Attempt to steal from '" + npc.getName() + "'\n";
                                            s = s + "The '" + npc.getName() + "' gives you a devastating blow, cracking your skull in two!\n";
                                            gs.setHealth(gs.getHealth() -1);                                             
                                            s = s + "Check your health!\n";
                                            gs.setScore(-20); 
                                            break; 
                                            
                case 1: case 2: case 3:     s = "Attempt to steal from '" + npc.getName() + "' failed.\n";
                                            gs.setHealth(gs.getHealth()/2); 
                                            s = s + "The '" + npc.getName() + "' strikes you hard in the face.\n";
                                            s = s + "Check your health!\n"; 
                                            gs.setScore(-20); 
                                            break;
                                            
                case 4: case 5: case 6:     s = "Attempt to steal from '" + npc.getName() + "' failed.\n";
                                            gs.setHealth(gs.getHealth()/3); 
                                            s = s + "The '" + npc.getName() + "' gives you a solid blow to the ribs.\n";
                                            s = s + "Check your health!\n";
                                            gs.setScore(-20); 
                                            break;
                                            
                case 7: case 8:             s = "Attempt to steal from '" + npc.getName() + "' failed.\n";
                                            s = s + "The '" + npc.getName() + "' wasn't carrying anything to steal.\n"; 
                                            gs.setHealth(gs.getHealth()/4); 
                                            s = s + "The '" + npc.getName() + "' gives you a glancing blow to the side.\n";
                                            s = s + "Check your health!\n";
                                            gs.setScore(-20); 
                                            break;
                
                default:                    s = "Attempt to steal from '" + npc.getName() + "' failed.\n";
                                            s = s + "The '" + npc.getName() + "' swings at you, but you dodge out of the way.\n"; 
                                            s = s + "The '" + npc.getName() + "' wasn't carrying anything to steal.\n";
                                            break; 
            }
        }
        else if(!npc.carriedItems.isEmpty() && npc.getMood() == false)
        {
            int i = randInt(0, 9); 
            switch(i)
            {
                case 0:                     npc.setMood(true); 
                                            s = "Attempt to steal from '" + npc.getName() + "' failed. You made it mad.\n";
                                            gs.setHealth(gs.getHealth()/4); 
                                            s = s + "The '" + npc.getName() + "' strikes you hard in the face.\n";
                                            s = s + "Check your health!\n"; 
                                            gs.setScore(-10); 
                                            break; 
                
                case 1:                     npc.setMood(true);  
                                            s = "Attempt to steal from '" + npc.getName() + "' failed. You made it mad.\n";
                                            gs.setHealth(gs.getHealth()/5); 
                                            s = s + "The '" + npc.getName() + "' gives you a solid blow to the ribs.\n";
                                            s = s + "Check your health!\n";
                                            gs.setScore(-10); 
                                            break;
                                            
                case 2: case 3: case 4:     npc.setMood(true); 
                                            s = "Attempt to steal from '" + npc.getName() + "' failed. You made it mad.\n"; 
                                            gs.setScore(-10); 
                                            break; 
                                            
                case 5: case 6: case 7:     npc.setMood(true); 
                                            s = "Attempt to steal from '" + npc.getName() + "' successful!. It's mad now!\n";
                                            gs.setScore(20);
                                            Item item = stealItem(npc);
                                            gs.addToInventory(item); 
                                            s = s + "You stole a '" + item.getPrimaryName() + "' from the '" + npc.getName() + "'."; 
                                            break;
                
                case 8:                     npc.setMood(true);
                                            npc.setMobile(false); 
                                            s = "Attempt to steal from '" + npc.getName() + "' successful!. It's mad now!\n";
                                            gs.setScore(30);
                                            Item item2 = stealItem(npc);
                                            gs.addToInventory(item2);
                                            s = s + "You broke the '" + npc.getName() + "'s legs. It can't move!"; 
                                            s = s + "You stole a '" + item2.getPrimaryName() + "' from the '" + npc.getName() + "'."; 
                                            break;
                                            
                default:                    npc.setMobile(false);
                                            npc.setHealth(npc.getHealth());
                                            gs.setHealth(gs.getHealth()*-.5);
                                            gs.setScore(50);
                                            ArrayList<Item> al = epicSteal(npc); 
                                            s = "Attempt to steal from '" + npc.getName() + "' successful!.!\n";
                                            s = s + "You drive your combat blade through the Ork's throat, killing it with a straight prejudice.\n"; 
                                            s = s + "The Emperor smiles upon you with his beneficient glory, restoring your health.\n";
                                            for(Item item3 : al)
                                            {
                                                gs.addToInventory(item3); 
                                                s = s + "You stole a '" + item3.getPrimaryName() + "' from the '" + npc.getName() + "'."; 
                                            }
                                            
            }
        }
        else if(!npc.carriedItems.isEmpty() && npc.getMood() == true)
        {
            int i = randInt(0, 9); 
            switch(i)
            {
                case 0:                     s = "Attempt to steal from '" + npc.getName() + "'\n";
                                            s = s + "The '" + npc.getName() + "' gives you a devastating blow, cracking your skull in two!\n";
                                            gs.setHealth(gs.getHealth() -1);                                             
                                            s = s + "Check your health!\n";
                                            gs.setScore(-30); 
                                            break;
                                            
                case 1: case 2: case 3:     s = "Attempt to steal from '" + npc.getName() + "'\n";
                                            s = s + "The '" + npc.getName() + "' gives you a glancing blow to the side.\n";
                                            gs.setHealth(gs.getHealth()/5);                                             
                                            s = s + "Check your health!\n";
                                            gs.setScore(-30); 
                                            break;
                                            
                case 4: case 5: case 6:     s = "Attempt to steal from '" + npc.getName() + "' successful!.";
                                            gs.setScore(20);
                                            gs.setHealth(gs.getHealth()/4); 
                                            Item item = stealItem(npc);
                                            gs.addToInventory(item); 
                                            s = s + "You stole a '" + item.getPrimaryName() + "' from the '" + npc.getName() + "'.";
                                            s = s + "You paid for that with a blow to the face.\n"; 
                                            break;
                                            
                case 7: case 8:             s = "Attempt to steal from '" + npc.getName() + "' successful!.";
                                            gs.setScore(40);
                                            Item item4 = stealItem(npc);
                                            gs.addToInventory(item4); 
                                            s = s + "You stole a '" + item4.getPrimaryName() + "' from the '" + npc.getName() + "'.";
                                            break;
                
                default:                    npc.setMobile(false);
                                            npc.setHealth(npc.getHealth());
                                            gs.setHealth(gs.getHealth()*-1);
                                            gs.setScore(60); 
                                            ArrayList<Item> al = epicSteal(npc); 
                                            s = "Attempt to steal from '" + npc.getName() + "' successful!.!\n";
                                            s = s + "You drive your combat blade through the '" + npc.getName() + "''s throat, killing it with a epic badassery.\n";
                                            s = s + "A veritable gore of viscera and blood cover your armor. Let none mistake you are a warrior without equal.\n"; 
                                            s = s + "The Emperor smiles upon you with his beneficient glory, restoring your health beyond capacity.\n";
                                            for(Item item5 : al)
                                            {
                                                gs.addToInventory(item5); 
                                                s = s + "You stole a '" + item5.getPrimaryName() + "' from the '" + npc.getName() + "'."; 
                                            }
                                   
            }
        }
        
        return s; 
    }
    
    /**
     * Removes the item from the NPCs inventory during a successful steal action. 
     * 
     * @param npc       NPC to steal item from.
     * @return          Random item stolen from NPC.
     */
    private static Item stealItem(Denizen npc)
    {
        int i = randInt(0, npc.carriedItems.size() -1);
        Item item = npc.carriedItems.get(i);
        npc.removeFromInventory(item); 
        return item; 
    }
    
    /**
     * Removes all items from NPC inventory during epic steal action.
     * 
     * @param npc       NPC to steal from. 
     * @return          ArrayList of Items stolen from NPC
     */
    private static ArrayList<Item> epicSteal(Denizen npc)
    {
        ArrayList<Item> al = new ArrayList<Item>(); 
        for(Item item : npc.carriedItems)
        {
            npc.removeFromInventory(item); 
            al.add(item); 
        }
        return al; 
    }
}
