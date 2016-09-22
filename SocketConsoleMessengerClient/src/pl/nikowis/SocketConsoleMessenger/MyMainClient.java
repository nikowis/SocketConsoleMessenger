package pl.nikowis.SocketConsoleMessenger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class MyMainClient {
	private BufferedReader buf;
	private String name;
	private DataInputStream in;
	private DataOutputStream out;
	private ServerButler serverButler;
	private Socket socket;
	private boolean connectionAvailable=false;
	
	public void setConnectionAvailable(boolean x){
		connectionAvailable=x;
	}
	
	public boolean getConnectionAvailable(){
		return connectionAvailable;
	}
	
	public MyMainClient(int port, String serverName){
		try{
		buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Connecting to " + serverName +" on port " + port);
		socket = new Socket(serverName, port);
		in =  new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		System.out.println("Just connected to " + socket.getRemoteSocketAddress()); 
		System.out.println("Input your chat nickname :");
		connectionAvailable=true;
		name = buf.readLine();
		out.writeUTF(name);
		out.flush();
		connectionAvailable=true;
		serverButler = new ServerButler(this, in);
		serverButler.start();
		writeMessages();
		
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void writeMessages(){
		System.out.println("Press enter to send message, '!exit' to quit: ");
		String message="";
		try{
			while(!message.equals("!exit") && connectionAvailable){
	        	message = buf.readLine();
	        	out.writeUTF(message);
	        	out.flush();
	        }
			this.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void close(){
		System.out.println("Disconnected...");
		setConnectionAvailable(false);
        try{
			serverButler.interrupt();
			in.close();
			out.close();
	        socket.close();
	        System.exit(0);
        }catch(Exception e){}
	}
	
	public static void main(String[] args) {
		if(args.length==0)
			return;
		String serverName = args[0];
		int port= Integer.parseInt(args[1]);
		new MyMainClient(port, serverName);
	}

	

}
