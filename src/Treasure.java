public class Treasure {
    private String treasure;
    private String treasureKit = "";

    public Treasure(){
    }

    public String getTreasureKit(){
        return treasureKit;
    }


    public void generateTreasure(){
        int x = (int)(Math.random() * 100);

        if (x < 33){
            treasure = "Sword";
        } else if (x <66) {
            treasure = "Shield";
        } else if (x < 100) {
            treasure = "Spellbook";
        }
        else {
            treasure = "Sword";
        }
    }


    public String findTreasure(){
        if (Town.ifNewTown == 0 ) {
            int x = (int) (Math.random() * 100);
            if (x < 50 && !(treasureKit.contains(treasure))) {
                Town.ifNewTown++;
                treasureKit+= treasure;
                return "You have found the " + treasure + "!";

            } else if (x< 50 && (treasureKit.contains(treasure))) {
                return "You already have this item in your inventory";
            } else {
                Town.ifNewTown++;
                return "You have not found the treasure. You can no longer look for treasure ";
            }
        }
        else {
            return "You can't search for treasure in this town anymore anymore";
        }
    }

    public String checkInventory(){
        if (getTreasureKit().contains("Sword") && getTreasureKit().contains("Shield") && getTreasureKit().contains("Spellbook")){
            TreasureHunter.isGameOver = true;
            return "You have obtained all the treasure! The game is now over.";
        }
        return "";
    }





}
