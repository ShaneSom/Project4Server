import javafx.util.Pair;

import java.util.ArrayList;

public class Boat {

    int lives;
    ArrayList<Pair<Integer, Integer>> coords = new ArrayList<>();
    public Boat(ArrayList<Pair<Integer, Integer>> coords) {
        this.coords = coords;
        lives = coords.size();
    }

    public boolean checkHit(Pair<Integer,Integer> check){
        for (Pair<Integer, Integer> x :
                coords) {
            if (x.getValue() == check.getValue() && x.getKey() == check.getKey()){
                return true;
            }
        }
        return false;
    }

    public void hit(){
        lives--;
    }

    public int getLives(){
        return lives;
    }


}
