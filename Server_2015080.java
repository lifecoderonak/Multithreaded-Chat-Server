// Ronak Kumar (2015080)

import java.io.*;
import java.util.*;
import java.net.*;

public class Server_2015080 {

	public static Vector<ClientHandler> connections = new Vector<ClientHandler>();
	public static int counter = 0;

	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(3333);
		Socket s;
		for(;;) {
			s = ss.accept();
			System.out.println("Client " + connections.size() + " Connected");
			DataInputStream dataInput = new DataInputStream(s.getInputStream());
			DataOutputStream dataOutput = new DataOutputStream(s.getOutputStream());
			ClientHandler handler = new ClientHandler(s, "Client " + counter, dataInput, dataOutput);
			Thread t = new Thread(handler);
			connections.add(handler);
			t.start();
			counter++;
		}
	}
}

class ClientHandler implements Runnable {
	Scanner scanner = new Scanner(System.in);
	public String name = null;
	public DataInputStream dataInput = null;
	public DataOutputStream dataOutput = null;
	public Socket s = null;
	public Boolean islogged = null;

	public ClientHandler(Socket s, String name, DataInputStream dataInput, DataOutputStream dataOutput) {
		initialize(dataInput, dataOutput);
		setSocket(s);
		setName(name);
		this.islogged = true;
	}

	public void initialize(DataInputStream dataInput, DataOutputStream dataOutput) {
		this.dataInput = dataInput;
		this.dataOutput = dataOutput;
	}
	
	public void setSocket(Socket s) {
		this.s = s;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void run() {
		String received;
		for(;;) {
			try {
				received = dataInput.readUTF();
				if(received.equals("quit")) {
					this.islogged = false;
					this.s.close();
					break;
				}
				String messageToSend;
				String rec = "";
				List<String> clients = new ArrayList<String>();
				String[] words = received.split(" ");
				String message = "";
				if(words[0].equals("Client")) {
					String temp = words[1];
					if(temp.indexOf(',') == -1) {
						clients.add(temp);
					}
					else {
						String[] cl = temp.split(",");
						clients.add(cl[0]);
						clients.add(cl[1]);
					}
					int colon = -1;
					for(int i = 0; i < words.length; i++) {
						if(words[i] == ":") {
							colon = i;
							break;
						}
					}
					for(int i = colon+1; i < words.length; i++) {
						message += words[i] + " ";
					}
					messageToSend = message;
					if(clients.size() == 1) {
						rec += "Client ";
						rec += clients.get(0);
						for(ClientHandler mc : Server_2015080.connections) {
							if(mc.name.equals(rec)) {
								if(mc.islogged == true) {
									mc.dataOutput.writeUTF(this.name + " : " + messageToSend);
								}	
							}
							else {
								System.out.println("Client not connected or does not exist");
								break;
							}
						}
						rec = "";
					}
					else if(clients.size() == 2) {
						for(int i = 0; i < 2; i++) {
							rec += "Client ";
							rec += clients.get(i);
							for(ClientHandler mc : Server_2015080.connections) {
								if(mc.name.equals(rec)) {
									if(mc.islogged == true) {
										mc.dataOutput.writeUTF(this.name + " : " + messageToSend);
									}
								}	
							}
							rec = "";
						}
					}
				}
				else if(words[0].equals("All")) {
					for(int i = 2; i < words.length; i++) {
						message += words[i] + " ";
					}
					messageToSend = message;
					for(int i = 0; i < Server_2015080.connections.size(); i++) {
						rec += Server_2015080.connections.get(i).name;
						for(ClientHandler mc : Server_2015080.connections) {
							if(mc.name.equals(rec)) {
								if(mc.islogged == true) {
									mc.dataOutput.writeUTF(this.name + " : " + messageToSend);
								}
							}
						}
						rec = "";
					}	
				}
				else {
					System.out.println("Number of connections are :-");
					for(int i = 0; i < Server_2015080.connections.size(); i++) {
						System.out.println(Server_2015080.connections.get(i).name);
					}
				}
			} 
			catch(IOException exception) {
				exception.printStackTrace();
			}
		}
		try {
			this.dataInput.close();
			this.dataOutput.close();
		}
		catch(IOException exception) {
			exception.printStackTrace();
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}
}