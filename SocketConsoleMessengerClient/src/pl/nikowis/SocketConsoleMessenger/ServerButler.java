package pl.nikowis.SocketConsoleMessenger;

import java.io.*;

public class ServerButler extends Thread {
	private MyMainClient client;
	private DataInputStream in ;
	
	public ServerButler(MyMainClient client, DataInputStream in){
		this.client= client;
		this.in=in;
	}
	
	public void run(){
			String message="";
			while(client.getConnectionAvailable()){
				try {
					message = in.readUTF();
					System.out.println(message);
				} 
				catch (IOException e) {
					client.close();
				}
			}
	}
}
