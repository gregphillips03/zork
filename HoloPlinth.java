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
            updatePlinth(); 
            s = "Input Accepted...\nHoloplinth unlocked."; 
        }
        return s; 
    }
    
    /**
     * Interaction with user to access HoloPlinth. Allows user to explore various funcitons of plinth interface. 
     * 
     * @throws InterruptedException     Pushes IO exception up the stack
     * @return                          String message to user
     */
    public String access() throws InterruptedException
    {
        Scanner scan = new Scanner(System.in);  
        String user = ""; 
        String s = "|-------------------------|\n" +
                   "|+++Available Functions+++|\n" +
                   "| <BioScan>    <Teleport> |\n" +
                   "|-------------------------|\n" +
                   "Choose a function:\n";
        System.out.println(s); 
        user = scan.nextLine().toLowerCase(); 
        
        switch(user)
        {
            case "bioscan":     s = getBio();
                                break; 
            case "teleport":    s = doTeleport(); 
                                break; 
        }
               
        return s + "+++Disconnected from Holoplinth+++"; 
    }
    
    /**
     * Updates the holoplinth so the user no longer sees the unlock event. Adds in the access functionality instead.
     * Physically removes the object from the dungeon and room and then replaces with like object including differing functionality.
     */
    private void updatePlinth()
    {
        GameState gs = GameState.instance(); 
        CommandFactory cf = CommandFactory.instance(); 
        Dungeon d = gs.getDungeon();
        Item item = d.getItem("holoplinth"); 
        Room room = d.getRoom("Engine Control");
        
        //remove from dungeon and room
        d.itemsInDungeon.remove(item.getPrimaryName()); 
        room.roomItems.remove(item);  
        
        //create new holoplinth item
        Item plinth = new Item("holoplinth", 99999); 
        
        //add verb message combo, ensure it's within the program's known verbs
        plinth.addVerbMessage("access", "++++Holoplinth Interface+++"); 
        cf.AVAIL_VERBS.add("access"); 
        
        //create access event and add to item's array of events
        Event e = new Event("Access", "holoplinth"); 
        ArrayList<Event> al = new ArrayList<Event>(); 
        al.add(e); 
        plinth.addEvent("access", al); 
        
        //add item to dungeon and room
        d.itemsInDungeon.put(plinth.getPrimaryName(), plinth); 
        room.roomItems.add(plinth);       
        
    }
    
    /**
     * Persistence method to write to save file
     * 
     * @param pw        PrintWriter object to work with / handed focus from GameState Class
     */
    void storeState(PrintWriter pw)
    {
        if(isLocked)
        {
            pw.write("isLocked=true\n"); 
        }
        else
        {
            pw.write("isLocked=false\n"); 
        }
    }
    
    /**
     * Hydration method to read from save file
     * 
     * @param scan      Scanner object to work with / handed focus from GameState Class
     */
    void restoreState(Scanner scan)
    {
        if(!scan.nextLine().equals("isLocked=true"))
        {
            isLocked = false; 
            updatePlinth(); 
        }
    }
    
    /**
     * Scans the dungeon and returns the locations of where NPCs are located
     * 
     * @throws InterruptedException         Pushes thread sleep disruptions up the stack 
     * @return                              Location of NPCs as String
     */
    private String getBio() throws InterruptedException
    {
        String s = "";
        int count = 0; 
        GameState gs = GameState.instance(); 
        
        System.out.println("Initiating Scan Sequence"); 
        Thread.sleep(200);
        System.out.print("."); 
        Thread.sleep(200);
        System.out.print("."); 
        Thread.sleep(200);
        System.out.print("."); 
        Thread.sleep(200);
        System.out.print("."); 
        Thread.sleep(200);        
        System.out.print(".\n");
        Thread.sleep(200);   
        
        for(String key: gs.getDungeon().collection.keySet())
        {
            Room rm = gs.getDungeon().getRoom(key); 
            if(!rm.npcHere.isEmpty())
            {
                for(Denizen npc : rm.npcHere)
                {
                    s = s + "Room '" + rm.getTitle() + "' contains a '" + npc.getName() + "'.\n"; 
                    System.out.println("++++BIOFORM DETECTED++++"); 
                }
            }
            System.out.print("Scanning " + rm.getTitle()); 
            Thread.sleep(150);
            System.out.print("."); 
            Thread.sleep(150);
            System.out.print("."); 
            Thread.sleep(150);
            System.out.print(".\n"); 
            count ++; 
        }        
        return "++++" + count + " AREAS SCANNED++++\n" + s; 
    } 
    
    /**
     * Teleports the user to a room
     * 
     * @throws InterruptedException         Pushes thread sleep disruptions up the stack  
     * @return                              String message to user
     */
    private String doTeleport() throws InterruptedException
    {
        String s = "Teleportation Malfunction"; 
        String user = ""; 
        int userint = 0;
        int random = 0; 
        Scanner scan = new Scanner(System.in); 
        GameState gs = GameState.instance();        
        Item item = gs.getItemFromInventory("powerpack"); 
        ArrayList<String> availRooms; 
        
        if(item != null)
        {
            System.out.println("To teleport, please insert powerpack.");
            System.out.println("It will be consumed during the process."); 
            System.out.println("Insert now? (Y / N): "); 
            user = scan.next().toLowerCase(); 
            if(user.equals("y"))
            {
                gs.removeFromInventory(item); 
                System.out.println("++++Powerpack accepted++++"); 
                Thread.sleep(350); 
                System.out.println("++++Powering Tessaract Coils++++"); 
                System.out.print("."); 
                Thread.sleep(250);
                System.out.print("."); 
                Thread.sleep(250);
                System.out.print(".\n");
                Thread.sleep(250); 
                System.out.println("++++Coils Hot++++"); 
                Thread.sleep(500); 
                
                System.out.println(s = formatRooms(availRooms = availRooms())); 
                System.out.println("Choose Teleport Location"); 
                System.out.print("Enter numeric identifier: ");
                userint = scan.nextInt(); 
                Room room = teleportLocation(userint, availRooms); 
                if(room == null)
                {
                    s = "Teleport Malfunction @ Room Location";
                    return s; 
                }
                
                if((random = randInt(0, 10)) < 4)
                {
                    s = teleportMalfunction(random, room); 
                    return s; 
                }
                
                gs.setAdventurersCurrentRoom(room);
                gs.setScore(random*5); 
                teleportFluff(); 
                
                s = "Teleported to '" + room.getTitle() + "'.\n"; 
                return s; 
            }
            else
            {
                s = "Sequence Aborted.\nDisconnected from Holoplinth.\n"; 
                return s; 
            }
        }
        else
        {
            s = "You need additional power to teleport.\n" +
                "Please try again with power pack.\n"; 
            return s; 
        }
    }
    
    /**
     * Gets the available rooms within the Dungeon
     * 
     * @return      ArrayList populated with the room titles within the dungeon
     */
    private ArrayList<String> availRooms()
    {
        ArrayList<String> sa = new ArrayList<String>(); 
        GameState gs = GameState.instance(); 
        
        for(String key: gs.getDungeon().collection.keySet())
        {
            sa.add(key); 
        }
        return sa; 
    }
    
    /**
     * Formats the available rooms for output to the user
     * 
     * @param al        ArrayList of room titles as String
     * @return          Formatted string for display to user
     */
    private String formatRooms(ArrayList<String> al)
    {
        String s = "";
        int i = 1; 
        
        for(String room : al)
        {
            s = s + room + " " + "<" + i + ">" + "\n"; 
            i ++; 
        }
        return s; 
    }
    
    /**
     * Parses the the user's input to determine which room to teleport to
     * 
     * @param i     Integer value as taken from the user / paired with the desired room
     * @param al    ArrayList of room titles
     * @return      Room to teleport to
     */
    private Room teleportLocation(int i, ArrayList<String> al)
    {
        GameState gs = GameState.instance(); 
        Dungeon d = gs.getDungeon(); 
        
        Room room = d.getRoom(al.get(i -1)); 
        return room; 
    }
    
    /**
     * Logic for teleportation malfunctioning
     * 
     * @throws InterruptedException     Pushes IO exception up the stack
     * 
     * @param room                      Target location for teleportation sequence. 
     * @param i                         Randomly generated number from teleportation initiation
     * @return                          String message to user
     * 
     */
    String teleportMalfunction(int i, Room room) throws InterruptedException
    {
        String s = "";
        GameState gs = GameState.instance(); 
        Room rm = gs.getAdventurersCurrentRoom();         
        Dungeon d = gs.getDungeon(); 
        double j = gs.getHealth();
        
        switch(i)
        {
            case 0:     gs.setHealth(j);
                        s = "++++Teleportation ripped you apart++++\n"; 
                        System.out.println(s); 
                        KillGame.endGame(); 
                        break; 
                        
            case 1:     gs.setHealth(j/2); 
                        s = "++++Teleportation Malfunction++++\n++++Check Your Health!++++\n";
                        teleportFluff(); 
                        gs.setAdventurersCurrentRoom(room); 
                        break;
                        
            case 2:     s = "++++Teleportation Malfunction++++\n++++Holoplinth has Teleported Instead!++++\n";
                        teleportFluff(); 
                        rm.remove(d.getItem("holoplinth"));
                        room.add(d.getItem("holoplinth")); 
                        break;
            
            case 3:     s = "++++Teleportation Malfunction++++\n++++Unable to Transport Your Equipment++++\n";
                        teleportFluff(); 
                        if(!gs.carriedItems.isEmpty())
                        {
                            for(Item item : gs.carriedItems)
                            {
                                gs.removeFromInventory(item); 
                                rm.add(item); 
                            }
                        }
                        gs.setAdventurersCurrentRoom(room); 
                        break; 
        }
        return s; 
    }
    
    /**
     * Random number generator / used to determine teleportation malfunction
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
     * Fluff for teleportation sequence
     * 
     * @throws InterruptedException         Pushes thread sleep disruptions up the stack  
     */
    private void teleportFluff() throws InterruptedException
    {
        System.out.println("++++Sequence Initiated++++");
        Thread.sleep(250);
        for(int i = 0; i < 4; i++)
        {
            System.out.print("."); 
            Thread.sleep(250); 
        }
        System.out.print(".\n");
        Thread.sleep(250); 
        System.out.println("++++Discharging Coil Energy++++"); 
        Thread.sleep(200); 
        
        for(int i = 0; i < 10; i++)
        {
            System.out.print("."); 
            Thread.sleep(200); 
        }        
        System.out.print(".\n");
        Thread.sleep(100);

        for(int i = 0; i < 10; i++)
        {
            System.out.print("."); 
            Thread.sleep(100); 
        }         
        System.out.print(".\n");
        Thread.sleep(50); 
        
        for(int i = 0; i < 10; i++)
        {
            System.out.print("."); 
            Thread.sleep(50); 
        }         
        System.out.print(".\n");
        Thread.sleep(20); 

        for(int i = 0; i < 10; i++)
        {
            System.out.print("."); 
            Thread.sleep(20); 
        }         
        System.out.print(".\n");
        Thread.sleep(10); 
        
        for(int i = 0; i < 10; i++)
        {
            System.out.print("."); 
            Thread.sleep(10); 
        }         
        System.out.print(".\n");        
        Thread.sleep(10); 

        for(int i = 0; i < 10; i++)
        {
            System.out.print("."); 
            Thread.sleep(10); 
        }           
        System.out.print(".\n"); 
        Thread.sleep(50); 
        
        for(int i = 0; i < 10; i++)
        {
            System.out.print("."); 
            Thread.sleep(10); 
        }           
        System.out.print(".\n"); 
        Thread.sleep(50); 
        
        for(int i = 0; i < 10; i++)
        {
            System.out.print("."); 
            Thread.sleep(10); 
        }           
        System.out.print(".\n"); 
        Thread.sleep(50);         
        
        System.out.println("+++++++++++++++++++");
        Thread.sleep(50);         
        for(int i = 0; i < 10; i++)
        {
            System.out.print("+"); 
            Thread.sleep(10); 
        } 
        System.out.print("\n"); 
        
        System.out.println("+++++++++++++++++++");
        Thread.sleep(50);
        for(int i = 0; i < 10; i++)
        {
            System.out.print("+"); 
            Thread.sleep(10); 
        } 
        System.out.print("\n"); 
        
        System.out.println("+++++++++++++++++++");
        Thread.sleep(50); 
        for(int i = 0; i < 10; i++)
        {
            System.out.print("+"); 
            Thread.sleep(10); 
        } 
        System.out.print("\n"); 
        
        System.out.println("+++++++++++++++++++"); 
        Thread.sleep(50); 
        for(int i = 0; i < 10; i++)
        {
            System.out.print("+"); 
            Thread.sleep(10); 
        }  
        System.out.print("\n"); 
        
        Thread.sleep(1000); 
        System.out.println("++++Sequence Complete++++"); 
    }
}
