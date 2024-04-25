
import javafx.util.Pair;

import java.util.Random;

public class AI {
    public Pair<Integer,Integer> attack(){
        Random rand = new Random();
        int randomX = rand.nextInt((9 - 0) + 1) + 0;
        int randomY = rand.nextInt((9 - 0) + 1) + 0;
        return new Pair<>(randomX,randomY);
    }
}
