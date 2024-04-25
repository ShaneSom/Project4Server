public class BattleshipGameLogic {
    Server.ClientThread p1;
    Server.ClientThread p2;

    AI aiPlay;

    int turn = 0;

    public BattleshipGameLogic(Server.ClientThread p1, Server.ClientThread p2){ // Playing client to client
        this.p1 = p1;
        this.p2 = p2;
    }

    public BattleshipGameLogic(Server.ClientThread p1){ // Playing against Ai


    }

    public void setP1(Server.ClientThread p){
        p1 = p;
    }

    public void setP2(Server.ClientThread p){
        p2 = p;
    }


    public Server.ClientThread whoPlayFirst(){
        if (Math.round( Math.random() )  == 0){
            return p1;
        }else{
            return p2;
        }
    }
}
