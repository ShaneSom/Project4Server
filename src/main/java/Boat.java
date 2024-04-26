import javafx.util.Pair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Boat implements Serializable {

    int lives;
    ArrayList<Pair<Integer, Integer>> coords = new ArrayList<>();
    Set<Pair<Integer,Integer>> life = new HashSet<>();

    public Boat(ArrayList<Pair<Integer, Integer>> coords) {
        this.coords = coords;
        lives = coords.size();
    }

    public boolean checkHit(Pair<Integer,Integer> check){

        for (Pair<Integer, Integer> x :
                coords) {
            System.out.println(x.getValue() + " to " + check.getValue() + " and " + x.getKey() + " to " + check.getKey());
            if (Objects.equals(x.getValue(), check.getValue()) && Objects.equals(x.getKey(), check.getKey())){
                if (life.contains(check)){
                    System.out.println("ALREADY HIT");
                    return false;
                }else{
                    System.out.println("HIT");
                    life.add(check);
                    lives--;
                    return true;

                }
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
