import java.io.*;
/**
 * Combat handles logic of Player/NPC combat encounters
 * 
 * @author Corey Staier
 * @version Zork v1 7 December 2016
 */
public class Combat 
{
    private int playerHealth;
    private int attackerName;
    private Boolean victory;
    private boolean testValue = true;
    /**
     * Constructor for objects of Class Combat
     * 
     * @param d   Denizen player has encountered
     * @param i   Item player is using as a weapon
     */
    Denizen target;
    Item weapon;
    int damageDoable = 0;
    GameState gs = GameState.instance(); 
    Combat(Denizen d, Item i)
    {
        //Insert Combat logic here
        this.target = d;
        this.weapon = i;      
        /*return true;*/
    }
    String execute() throws InterruptedException{ 
        int enemyHealth = this.target.getHealth();
        System.out.println("You attack " + this.target.getName() + "(" + enemyHealth + " health) " + " with " + weapon.getPrimaryName());
        if(this.isWeapon(this.weapon)){ //If Item is a weapon, damage possible is 15
            damageDoable = 15;
        }
        else{ //If Item is not a weapon, damage is relative to its weight
            damageDoable = this.weapon.getWeight();
        }
        
        this.target.setHealth(enemyHealth - damageDoable);
        if(this.target.getHealth() <= 0){
            gs.getAdventurersCurrentRoom().removeNpc(this.target);
            System.out.println("You have done " + damageDoable + " damage to your target.");
            System.out.println("You have killed " + this.target.getName());
        }
        else{
            int playerHealth = (int)gs.getHealth();
            System.out.println("You have done " + damageDoable + " damage to your target.");
            gs.setHealth(playerHealth - 5);
            System.out.println("The " + this.target.getName() + " attacks, dealing 5 damage.");
            if(gs.getHealth() <= 0){
                Event e = new Event("die", this.weapon.getPrimaryName());
                e.generateEvent();
                return "";
            }
            
        }
        return "";
    }
    /**
     * @return Player health for comparison
     */
    int getPlayerHealth(){
        return 100; //temp value
    }
   
    boolean isWeapon(Item chosen){ //Checks if item used is a weapon
        if(chosen.getPrimaryName().equals("boltpistol") || chosen.getPrimaryName().equals("chainsword")){
            return true;
        }
        else{
            return false;
        }
    }
}
