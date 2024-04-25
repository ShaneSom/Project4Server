
import java.io.Serializable;
import javafx.util.Pair;


public class Message implements Serializable {
    static final long serialVersionUID = 42L;

    boolean isPlacingShips = false;
    boolean isAttacking = false;
    boolean didHit = false;
    int[][] updateGrid;
    Pair<Integer,Integer> attackCoord;


    public void setUpdateGrid(int[][] updatedGrid){
        updateGrid = updatedGrid;

    }

    public int[][] getUpdateGrid(){
        return updateGrid;
    }

    public void setAttackCoord(Pair<Integer,Integer> coords){
        attackCoord = coords;
    }

    public Pair<Integer,Integer> getAttackCoord(){
        return attackCoord;
    }



}
