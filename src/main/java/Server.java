import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
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

			ClientThread opp;

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
					catch(Exception e) {}
				}
			}

			public void updateClient(ClientThread to,String message) {
				try {
					to.out.writeObject(message);
				}
				catch(Exception e) {}
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
							if (data instanceof Message){
								Message clientMessage = (Message)data;
								if (clientMessage.isAttacking){ // Checks pair to see if hit
									Pair<Integer,Integer> coord = clientMessage.getAttackCoord();
									for (int i = 0; i < battleShips.size(); i++){
										if (battleShips.get(i).checkHit(coord)){
											//TODO ALERT HIT, SWITCH TURNS, CHECK FOR SUNKEN SHIP

										}else{
											//TODO ALERT MISS, SWITCH TURNS

										}

									}
								}
								if (clientMessage.finishedPlacingShips){
									battleShips.add(clientMessage.newBoat);

								}
							}
//					    	callback.accept("client: " + count + " sent: " + data);
//					    	updateClients("client #"+count+" said: "+data);

					    	}
					    catch(Exception e) {
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					    	updateClients("Client #"+count+" has left the server!");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run


		}//end of client thread
}






