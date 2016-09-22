package pl.nikowis.SocketConsoleMessenger;

import java.io.*;
import java.net.*;

public class ClientButler extends Thread {
	private Socket socket;
	private MyMainServer server;
	private String name;
	private DataInputStream in ;
	private DataOutputStream out;
	private long userId;
	private static int nextId=1;
	
	public ClientButler(MyMainServer server,Socket socket){
		this.socket=socket;
		this.server=server;
		userId=nextId;
		nextId++;
		try{
		in  = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public String getUserName(){
		return name;
	}
	
	public long getUserId(){
		return userId;
	}
	
	public void send(String msg){
		try {
			out.writeUTF(msg);
			out.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void run(){
		String message="";
		try{
			 name=in.readUTF();
			 System.out.println(name+ " connected from " + socket.getRemoteSocketAddress());
			 server.sendToAll(getUserId(),name +" just connected...");
			 out.writeUTF(server.clientListToMessage());
			 server.addClient(this);
			 while(!message.equals("!exit")){
				 message=in.readUTF();
				 if(!message.equals("!exit")){
					 server.sendToAll(getUserId(),name+ " says :" + message);
					 System.out.println(name+ " says : " +message);
				 }
				 else{
					 server.sendToAll(getUserId(), message);
				 }
			 }
			 in.close();
			 out.close();
			 socket.close();
		}catch(Exception e){
			server.sendToAll(getUserId(),"!exit");
			System.out.println(e.getMessage());
		}
	}
}
