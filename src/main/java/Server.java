import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.util.Pair;

public class Server{

	BattleshipGameLogic battleshipGame;

	int count = 1;
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;


	Server(Consumer<Serializable> call){

		callback = call;
		server = new TheServer();
		server.start();
	}


	public class TheServer extends Thread{

		public void run() {

			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");


		    while(true) {

				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();

				count++;

			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}


		class ClientThread extends Thread{


			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;

			boolean lookingForPlayer;

			boolean playAI;

			String username;
			ArrayList<Boat> battleShips = new ArrayList<>();


			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;
			}

			public void updateClients(String message) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.writeObject(message);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}

			public void updateClient(ClientThread to, String message) {
				try {
					to.out.writeObject(message);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}

			public void lookForPlayer(){ // Set client with client
				for (ClientThread client : clients
				) {
					if (client.lookingForPlayer && !(client.username.equals(username))){
						System.out.println("MATCH FOUND");
						lookingForPlayer = false;
						client.lookingForPlayer = false;
						battleshipGame = new BattleshipGameLogic(this, client);
						updateClient(this, "MATCH FOUND");
						updateClient(client, "MATCH FOUND");
						updateClient(battleshipGame.p1, "turn");
						battleshipGame.currentPTurn = battleshipGame.p1;
						updateClient(battleshipGame.p2, "wait");
					}
				}
			}

			public void run(){

				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}

				updateClients("new client on server: client #"+count);

				 while(true) {
					    try {
					    	Object data = in.readObject();
							System.out.println(data.toString());
							if (data instanceof Message){
								Message clientMessage = (Message)data;
								if (clientMessage.isAttacking){ // Checks pair to see if hit
									if (battleshipGame.attack(clientMessage.attackCoord)){
										if (battleshipGame.currentPTurn == battleshipGame.p1){
											updateClient(battleshipGame.p1,"hit" + clientMessage.attackCoord.getKey() + "," + clientMessage.attackCoord.getValue());
											updateClient(battleshipGame.p2,"hO"  + clientMessage.attackCoord.getKey() + "," + clientMessage.attackCoord.getValue());
											updateClient(battleshipGame.p2, "turn");
											battleshipGame.currentPTurn = battleshipGame.p2;
											updateClient(battleshipGame.p1, "wait");
											if (battleshipGame.checkOut(battleshipGame.p2)){
												System.out.println("PLAYER ONE WIN");
												updateClient(battleshipGame.p1, "win");
												updateClient(battleshipGame.p2, "lose");
											}

										}else{
											updateClient(battleshipGame.p2,"hit" + clientMessage.attackCoord.getKey() + "," + clientMessage.attackCoord.getValue());
											updateClient(battleshipGame.p1,"hO" + clientMessage.attackCoord.getKey() + "," + clientMessage.attackCoord.getValue());
											updateClient(battleshipGame.p1, "turn");
											battleshipGame.currentPTurn = battleshipGame.p1;
											updateClient(battleshipGame.p2, "wait");
											if (battleshipGame.checkOut(battleshipGame.p1)){
												System.out.println("PLAYER TWO WIN");
												updateClient(battleshipGame.p1, "win");
												updateClient(battleshipGame.p2, "lose");
											}
										}
									}else{
										if (battleshipGame.currentPTurn == battleshipGame.p1){
											updateClient(battleshipGame.p1,"miss" + clientMessage.attackCoord.getKey() + "," + clientMessage.attackCoord.getValue());
											updateClient(battleshipGame.p2,"mO" + clientMessage.attackCoord.getKey() + "," + clientMessage.attackCoord.getValue());
											updateClient(battleshipGame.p2, "turn");
											battleshipGame.currentPTurn = battleshipGame.p2;

											updateClient(battleshipGame.p1, "wait");
										}else{
											updateClient(battleshipGame.p2,"miss"  + clientMessage.attackCoord.getKey() + "," + clientMessage.attackCoord.getValue());
											updateClient(battleshipGame.p1,"mO"  + clientMessage.attackCoord.getKey() + "," + clientMessage.attackCoord.getValue());
											updateClient(battleshipGame.p1, "turn");
											battleshipGame.currentPTurn = battleshipGame.p1;

											updateClient(battleshipGame.p2, "wait");
										}
									}
								}
								if (clientMessage.finishedPlacingShips){
									battleShips.add(clientMessage.newBoat);

								}
								if (clientMessage.isUsername){
									username = clientMessage.msg;
								}
								if (clientMessage.updateBoats){
									battleShips = clientMessage.updateBoat;
								}
							}else{
								String msg = data.toString();
								if (msg.equals("matchmake")){
									System.out.println(username + "IS LOOKING");
									lookingForPlayer = true;
									lookForPlayer();
								}
							}
//					    	callback.accept("client: " + count + " sent: " + data);
//					    	updateClients("client #"+count+" said: "+data);

					    	}
					    catch(Exception e) {
							e.printStackTrace();
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					    	updateClients("Client #"+count+" has left the server!");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run


		}//end of client thread
}






