public class Treasure {
    private Hunter hunter;
    private String treasure;


    public Treasure(Hunter hunter){
        this.hunter = hunter;
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
            treasure = "sword";
        }


    }




}
