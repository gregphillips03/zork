import java.util.*; 
import java.io.*; 

/**
 * Command
 * Abstract class for formation of objects based on parsed user input
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
abstract class Command
{
    abstract String execute() throws InterruptedException, FileNotFoundException; 
}
