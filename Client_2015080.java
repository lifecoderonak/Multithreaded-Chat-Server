// Ronak Kumar (2015080)

import java.io.*;
import java.net.*;
import java.util.*;

public class Client_2015080 {

	public static DataInputStream dataInput;
	public static DataOutputStream dataOutput;
	public static Socket s;
	public String hostName = "localhost";
	public static InetAddress ip;
	public static InputStream streamIn;
	public static OutputStream streamOut;
	
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		try {
			ip = InetAddress.getByName("localhost");
		}
		catch(UnknownHostException exception) {
			exception.printStackTrace();
		}
		try {
			s = new Socket(ip, 3333);
		} 
		catch(IOException exception) {
			exception.printStackTrace();
		}
		try {
			streamIn = s.getInputStream();
			streamOut = s.getOutputStream();
		} 
		catch(IOException exception) {
			exception.printStackTrace();
		}
		dataInput = new DataInputStream(streamIn);
		dataOutput = new DataOutputStream(streamOut);
		Thread sendMessage = new Thread(new Runnable() {
			public void run() {
				for(;;) {
					String msg = scanner.nextLine();
					try {
						dataOutput.writeUTF(msg);
					}
					catch(IOException exception) {
						exception.printStackTrace();
					}
					catch(Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		});
		Thread readMessage = new Thread(new Runnable() {
			public void run() {
				for(;;) {
					try {
						String msg = dataInput.readUTF();
						System.out.println(msg);
					} 
					catch(IOException exception) {
						exception.printStackTrace();
					}
					catch(Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		});
		sendMessage.start();
		readMessage.start();
	}
}