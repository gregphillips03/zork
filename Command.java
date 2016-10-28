import java.util.*; 
import java.io.*; 

/**
 * Command
 * 
 * @author      William (Greg) Phillips
 * @version     Bork v3.0
 */
abstract class Command
{
    abstract String execute() throws InterruptedException, FileNotFoundException; 
}
