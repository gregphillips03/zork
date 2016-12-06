
/**
 * Write a description of class AttackCommand here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*; 
import java.io.*; 
public class AttackCommand extends Command
{
    private String npcName = ""; 
    Denizen target;
    /**
     * Constructor for objects of Class StealCommand
     * 
     * @param f     Filename of save file
     */
    AttackCommand(String npcName)
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
    /**
     * Takes String input and splits it, asking for more input if more is needed to complete attack command
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
            return "There's no one here to attack."; 
        }
        
        else if(parts.length == 1){
            Scanner scan = new Scanner(System.in); 
            String s = ""; 
            System.out.println("Attack who?");
            s = scan.nextLine();
            if(room.npcHere.isEmpty() == false){
                for(Denizen d : room.npcHere){
                    if(d.getName().equals(s)){
                        target = d;
                        break;
                    }
                }
                if(target.getName().equals(s)){
                System.out.println("With what?");
                boolean validItem = false;
                while(validItem == false){
                String w = "";
                w = scan.nextLine();
                if(gs.getItemFromInventory(w) == null)
                {
                    System.out.println("You do not have item " + w + ".");
                }
                else if(w.equals("cancel")){
                    break;
                }
                else if(gs.getItemFromInventory(w) != null){
                    Item weapon = gs.getItemFromInventory(w);
                    validItem = true;
                    Combat c = new Combat(target, weapon);
                    c.execute();
               }
            }
           }
          }
          else{
               System.out.println("Room is empty.");
            }
        }
        else if(parts.length == 2){
           Scanner scan = new Scanner(System.in);
           if(room.npcHere.isEmpty() == false){
               for(Denizen d : room.npcHere){
                    if(d.getName().equals(parts[1])){
                        target = d;
                        break;
                    }
                }
               if(target.getName().equals(parts[1])){
                System.out.println("With what?");
                boolean validItem = false;
                while(validItem == false){
                String w = "";
                w = scan.nextLine();
               if(gs.getItemFromInventory(w) == null)
                {
                    System.out.println("You do not have item " + w + ".");
                }
                else if(w.equals("cancel")){
                    break;
                }
                else if(gs.getItemFromInventory(w) != null){
                    Item weapon = gs.getItemFromInventory(w);
                    validItem = true;
                    Combat c = new Combat(target, weapon);
                    c.execute();
               }
            }
           }
         }
            else{
               System.out.println("Room is empty.");
            }
       }
       return "";
    }
}
