
/**
 * KillGame terminates the program.
 * 
 * @author      William (Greg) Phillips
 * @version     Zork v1
 */
public class KillGame
{
    /**
     * Forces the program to quit with a termination code of 1 (something unexpectedly went wrong)
     * 
     * @param s String to be added to System output
     */
    public static void gameKill(String s) throws InterruptedException
    {   
            //aesthetics
            System.out.println("+++Critical ERROR+++"); 
            Thread.sleep(1000); 
            System.out.println("+++Scrapcode Evident in Data Loom+++"); 
            Thread.sleep(500);
            System.out.println(s); 
            Thread.sleep(200); 
            System.out.print("+++Transmission Degrading+++"); 
            System.out.println(); 
            Thread.sleep(500); 
            System.out.print("."); 
            Thread.sleep(500); 
            System.out.print("."); 
            Thread.sleep(500); 
            System.out.print("."); 
            Thread.sleep(200); 
            System.out.println(); 
            System.out.println("+++Transmission Lost+++"); 
            Thread.sleep(3000); 
            //aesthetics
            
            //meat
            System.exit(1); 
            //meat
    }
    
    /**
     * Forces the program to quit with a termination code of 0 (expected termination)
     * 
     */    
    public static void endGame() throws InterruptedException
    {
            //aesthetics
            System.out.println("+++Data Link Malfunction+++"); 
            Thread.sleep(1000); 
            System.out.println("+++Transmission Degrading+++"); 
            Thread.sleep(500);
            System.out.print("+++Transmission Degrading+++"); 
            System.out.println(); 
            Thread.sleep(500); 
            System.out.print("."); 
            Thread.sleep(500); 
            System.out.print("."); 
            Thread.sleep(500); 
            System.out.print("."); 
            Thread.sleep(200); 
            System.out.println(); 
            System.out.println("+++Transmission Lost+++"); 
            Thread.sleep(3000); 
            //aesthetics
            
            //meat
            System.exit(0); 
            //meat   
    }
}
