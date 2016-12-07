import java.util.*; 
import java.io.*; 

/**
 * RandomEvent class is responsible for initiating random events throughout the game
 * each turn goes through 
 * 
 * @author Joshua Wright
 * @version TeamBork v1
 */
public class RandomEvent {
    
    private int randomEvent;
    GameState gs = GameState.instance();
    public static List<Integer> FALL_EVENT = Arrays.asList(68, 99);
    public static List<Integer> RAT_EVENT = Arrays.asList(10, 20, 30, 40);
    public static List<Integer> ROCK_EVENT = Arrays.asList(5, 43, 61);
    public static List<Integer> GETLOST_EVENT = Arrays.asList(74, 32);
    public static List<Integer> TRAGEDY_EVENT = Arrays.asList(88);
    /**
     * Constructor for RandomEvent generator
     */
    RandomEvent()
    {
        randomEvent = (int)(Math.random() * ((100 - 0) + 1));
        initEvents(randomEvent);
    }
    /**
     * 
     * @param event tells the function initEvents 
     * 
     */
    public void initEvents(int event) 
    {
        int playerHealth = (int)gs.getHealth();
        if (FALL_EVENT.contains(event)) {
            System.out.println("+++YOU TRIP AND FALL CAUSING YOU TO LOSE 1 HEALTH POINT+++");
            gs.setHealth(1);
        }
        else if (RAT_EVENT.contains(event)) {
            if (event == 10)
                System.out.println("+++A RAT SCURRYS PAST YOUR FEET STARTLING YOU+++");
            else if (event == 20)
                System.out.println("+++TWO LARGE RATS RUN UNDER YOU BRUSHING YOUR LEGS+++");
            else if (event == 30 || event == 40)
                System.out.println("+++YOU ACCIDENTALLY STEP ON A RAT THAT RUNS UNDER YOU+++");
        }
        else if (ROCK_EVENT.contains(event)) {
            if (event == 5) {
                System.out.println("+++A SMALL SIZED OBJECT FALLS FROM THE CEILING, YOU LOSE 2 HEALTH POINTS+++");
                gs.setHealth(2);
            }
            else if (event == 43) {
                System.out.println("+++A MEDIUM SIZED OBJECT FALLS FROM THE CEILING, YOU LOSE 4 HEALTH POINTS+++");
                gs.setHealth(4);
            }
            else if (event == 61) {
                int deathChance = (int)(Math.random() * ((10 - 0) + 1));
                if (deathChance == 5) {
                    System.out.println("+++THE CEILING CAVES IN ON YOU AND YOU DIE+++");
                    gs.setHealth(250);
                }
            }
        }
        else if (GETLOST_EVENT.contains(event)) {
            System.out.println("+++YOU SEEM TO GET LOST FOR A FEW TURNS AND SOMEHOW FIND YOURSELF BACK AT THE ENTRY OF THE DUNGEON+++");
            gs.setAdventurersCurrentRoom(gs.entryRoom);
        }
        else if (TRAGEDY_EVENT.contains(event)) {
            int whichTragedy = (int)(Math.random() * ((50 - 0) + 1));
            if (whichTragedy == 7) {
                    System.out.println("+++YOU SUFFER FROM A HEART ATTACK AND DIE+++");
                    gs.setHealth(250);
            }
            if (whichTragedy == 41) {
                    System.out.println("+++SOME CHEMICALS SPILL INTO YOUR EYE FROM ABOVE AND YOU LOSE YOUR VISION+++");
                    System.out.println("+++OVER TIME YOU STARVE TO DEATH FROM YOUR INABILITY TO FIND FOOD+++");
                    gs.setHealth(250);
            }
        }
    } 
}
