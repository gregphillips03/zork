import java.util.*; 

/**
 * Exit Class holds onto Rooms and their exit paths
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class Exit
{
    private String dir = ""; 
    private Room src; 
    private Room dest; 

    /**
     * Constructor for objects of class Exit
     * 
     * @param dir   Direction to move as String
     * @param src   Room source as Room object
     * @param dest  Room destination as Room object
     */
    public Exit(String dir, Room src, Room dest)
    {
        this.dir = dir; 
        this.src = src; 
        this.dest = dest; 
    }
    
    /**
     * Constructor for object of Class Exit
     * 
     * @param s     Scanner object handed focus from Dungeon Class
     * @param d     Dungeon object to work with
     */
    public Exit(Scanner s, Dungeon d)
    {
        String pattern1 = "---"; 
        
        while(!s.hasNext(pattern1)) // does not advance token
        {
            Room tempRoom = d.getRoom(s.nextLine()); 
            String direction = s.nextLine(); 
            Room tempDest = d.getRoom(s.nextLine());
            
            Exit tempExit = new Exit(direction, tempRoom, tempDest); 
            tempRoom.addExit(tempExit); 
        }
        s.nextLine(); 
    }

    /**
     * describe
     * Describes the exit / Package visibility
     * 
     * @return     Returns exit description as a String
     */
    String describe()
    {
        String s = "You can go '" + getDir() + "' to '" + getDest().getTitle()+ "' from '" +getSrc().getTitle()+ "'."; 
        return s; 
    }
    
    /**
     * getDir
     * 
     * Get the Direction of this exit object / Public visibility
     * @return     returns direction to exit as a String
     */
    public String getDir()
    {
        return this.dir; 
    }
    
    /**
     * getSrc
     * Get the Source room FROM which to exit / Public visibility
     * 
     * @return     returns the FROM room as Room Object
     */
    public Room getSrc()
    {
        return this.src; 
    }
    
    /**
     * getDest
     * Get the Destination room TO exit to / Public visibility
     * 
     * @return     returns the DESTINATION room as Room object
     */
    public Room getDest()
    {
        return this.dest; 
    }
}
