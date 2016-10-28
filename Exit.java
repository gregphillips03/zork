import java.util.*; 

/**
 * Exit Class holds onto Rooms and their exit paths
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v2
 */
public class Exit
{
    private String dir = ""; 
    private Room src; 
    private Room dest; 

    /**
     * Constructor for objects of class Exit
     */
    public Exit(String dir, Room src, Room dest)
    {
        this.dir = dir; 
        this.src = src; 
        this.dest = dest; 
    }
    
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
     * Describes the exit
     * 
     * Package visibility
     * @return     returns exit description
     */
    String describe()
    {
        String s = "You can go '" + getDir() + "' to '" + getDest().getTitle()+ "' from '" +getSrc().getTitle()+ "'."; 
        return s; 
    }
    
    /**
     * Get the Direction of this exit object
     * 
     * Public visibility
     * @return     returns direction to exit
     */
    public String getDir()
    {
        return this.dir; 
    }
    
    /**
     * Get the Source room FROM which to exit
     * 
     * Public visibility
     * @return     returns the FROM room
     */
    public Room getSrc()
    {
        return this.src; 
    }
    
    /**
     * Get the Destination room TO exit to
     * 
     * Public visibility
     * @return     returns the DESTINATION room
     */
    public Room getDest()
    {
        return this.dest; 
    }
}
