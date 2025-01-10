import java.util.Scanner;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    public static int ifNewTown = 0;


    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param s The town's shoppe.
     * @param t The surrounding terrain.
     */
    public Town(Shop shop, double toughness)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        if (!TreasureHunter.hardMode){
            toughTown = false;
        } else {
            toughTown = (Math.random() < toughness);
        }
    }

    public String getLatestNews()
    {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param h The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            ifNewTown = 0;
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance = 0;
        if (toughTown && !(TreasureHunter.secret)) {
            noTroubleChance = 0.66;
        } else if (!(toughTown)  && !(TreasureHunter.secret)){
            noTroubleChance = 0.33;
        }
        else if (TreasureHunter.secret) {
            noTroubleChance = 1;
        }

        double random = (Math.random());
        if (TreasureHunter.secret){
            random = 2;
        }
        if (!TreasureHunter.hardMode && !toughTown){
        random +=  0.25;
        }
        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (TreasureHunter.secret){
                goldDiff = 100;
            }

            if (random > noTroubleChance) {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " + goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            } else {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " + goldDiff + " gold.";
                hunter.changeGold(-1 * goldDiff);
            }
        }
    }


    public void luckyDice()
    {
        int wager = 0;
        boolean test = false;
        boolean test2= false;
        int numb = 0;

        if (hunter.getGold()==0){
            System.out.println("You have 0 gold. You can't enter my casino. Get out of here!");
        }
        else {
            System.out.println("The game is called \"Lucky Dice\".");
            while (!test){

                Scanner scanner = new Scanner(System.in);
                System.out.println("What are you wagering today?");
                String answer = scanner.nextLine();
                wager = Integer.parseInt(answer);
                if (wager> hunter.getGold()){
                    System.out.println("You can't wager what you don't have. Choose again.");
                }
                else
                    test = true;
            }

            System.out.println("Choose a number from 1 - 12");
           while (!test2){
               Scanner dices = new Scanner(System.in);
               String num = dices.nextLine();
               numb = Integer.parseInt(num);

               if (!(1<=numb&&numb <=12)){
                   System.out.println("That number is not within 1-12. Choose again.");

               }
               else {
                   test2 = true;
               }
           }

            int dice = (int) ((Math.random()*6 +1) + (Math.random()*6+1));
            System.out.println("Rolling two dice.... and the number is..."+ dice +"!");
            if (numb<=(dice+2)&& numb>= dice|| numb>= (dice-2) &&numb <=dice){
                System.out.println("You are within the target, however you have not guessed the right number\nYou get your gold back!");
            }
            else if (numb ==dice){
                System.out.println("You guessed the right target! You get double your gold!");
                hunter.changeGold(wager*2);
                int gold = wager/10;
                Treasure.changeLuck(gold);
                System.out.println("You now have "+hunter.getGold()+" gold!");

            }
            else{
                System.out.println("This is why you don't gamble, you are way off target and lost all your gold");
                hunter.changeGold(0-wager);
                int gold = wager/10;
                Treasure.changeLuck((0-gold));
                System.out.println("You now have "+hunter.getGold()+" gold!");

            }

        }





    }


    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .2)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .4)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .6)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .8)
        {
            return new Terrain("Desert", "Water");
        }
        else
        {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        double rand = Math.random();
        if(!(TreasureHunter.hardMode) && !(TreasureHunter.secret)){
            rand = 1;
        }
        return (rand < 0.5);
    }


}