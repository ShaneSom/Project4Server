import javafx.util.Pair;

public class BattleshipGameLogic {
    Server.ClientThread p1;
    Server.ClientThread p2;

    Server.ClientThread currentPTurn;

    AI aiPlay;

    int turn = 0;

    public BattleshipGameLogic(Server.ClientThread p1, Server.ClientThread p2){ // Playing client to client
        this.p1 = p1;
        this.p2 = p2;
    }

    public BattleshipGameLogic(Server.ClientThread p1){ // Playing against Ai


    }

    public boolean checkOut(Server.ClientThread p){
        for (Boat s:
             p.battleShips) {
            if (s.lives != 0){
                return false;
            }
        }
        return true;
    }



    public void setP1(Server.ClientThread p){
        p1 = p;
    }

    public void setP2(Server.ClientThread p){
        p2 = p;
    }

    public Server.ClientThread whoTurn(){
        if (turn  == 0){
            currentPTurn = p1;
            return p1;
        }else{
            currentPTurn = p2;
            return p2;
        }
    }

    public boolean attack(Pair<Integer,Integer> t){
        Pair<Integer,Integer> coord = t;
        if (currentPTurn.equals(p1)){ // attacks p2
            System.out.println("BATTLESHIPS SIZE: "+ p2.battleShips.size());
            for (int i = 0; i < p2.battleShips.size(); i++){
                if (p2.battleShips.get(i).checkHit(coord)){
                    //TODO ALERT HIT, SWITCH TURNS, CHECK FOR SUNKEN SHIP
                    return true;

                }
            }
        } else{
            System.out.println("BATTLESHIPS SIZE: "+ p1.battleShips.size());
            for (int i = 0; i < p1.battleShips.size(); i++){
                if (p1.battleShips.get(i).checkHit(coord)) {
                    //TODO ALERT HIT, SWITCH TURNS, CHECK FOR SUNKEN SHIP
                    return true;

                }
            }
        }
        //TODO ALERT MISS, SWITCH TURNS
        return false;
    }
}
