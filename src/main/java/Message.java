
import java.io.Serializable;
import javafx.util.Pair;


public class Message implements Serializable {
    static final long serialVersionUID = 42L;

    boolean finishedPlacingShips = false;
    boolean isAttacking = false;
    boolean didHit = false;

    String msgTo;
    String msgFrom;
    Pair<Integer,Integer> attackCoord;

    Boat newBoat;

    public void setNewBoat(Boat t){
        newBoat = t;
    }

    public Boat getNewBoat(){
        return newBoat;
    }



    public void setAttackCoord(Pair<Integer,Integer> coords){
        attackCoord = coords;
    }

    public Pair<Integer,Integer> getAttackCoord(){
        return attackCoord;
    }

    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgTo(String msgTo) {
        this.msgTo = msgTo;
    }

    public String getMsgTo() {
        return msgTo;
    }


}
