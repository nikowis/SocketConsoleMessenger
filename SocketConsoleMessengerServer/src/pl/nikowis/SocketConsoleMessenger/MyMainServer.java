package pl.nikowis.SocketConsoleMessenger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyMainServer extends Thread {
	private ServerSocket serverSocket;
	private List<ClientButler> clients = new ArrayList<ClientButler>();
	public MyMainServer(int port) throws IOException{
		serverSocket = new ServerSocket(port);
	}
	
	public void run(){
		System.out.println("Server started at port " + serverSocket.getLocalPort());
		while(true){
			try{
				 Socket clientSocket = serverSocket.accept();
				 ClientButler client = new ClientButler(this, clientSocket);
				 client.start();
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	public synchronized void addClient(ClientButler client){
		clients.add(client);
	}
	
	public synchronized String clientListToMessage(){
		StringBuilder str = new StringBuilder("Available users : ");
		if(clients.isEmpty())
			return "No-one on the server...";
		for(ClientButler client : clients){
			str.append(client.getUserName() + ", ");
		}
		return str.toString();
	}
	
	public synchronized ClientButler getById(long id){
		for(ClientButler client : clients){
			if(client.getUserId()==id)
				return client;
		}
		return null;
	}
	
	public synchronized void sendToAll(Long id, String message){
		if(message.equals("!exit")){
			ClientButler leaving = getById(id);
			message = leaving.getUserName() + " left the conversation";
			deleteById(id);
		}
		for(ClientButler client : clients){
			if(client.getUserId()!=id)
				client.send(message);
		}
	}
	
	public synchronized void deleteById(long id) {
		Iterator<ClientButler> it = clients.iterator();
		while(it.hasNext()){
			ClientButler client = it.next();
			if(id == client.getUserId()){
				System.out.println(client.getUserName() + " disconnected...");
				it.remove();
			}
		}
	}
	
	public static void main(String[] args) {
		if(args.length==0)
			return;
		int port = Integer.parseInt(args[0]);
		try{
			Thread t = new MyMainServer(port);
			t.start();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
