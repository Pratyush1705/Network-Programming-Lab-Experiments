import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
public class VChatServer {
	//Unique id for every new connection
	private static int uId;
	//an arraylist which stores the users 
	private ArrayList<Client> al;
	// timestamp
	private SimpleDateFormat datefmt;
	//port no
	private int port;
	//a flag to check if server is running or not
	private boolean continueflw;
	private String not = " ***!! ";
	
	//Server constructor
	public VChatServer(int port) {
		this.port = port;
		//Setting the date format
		datefmt = new SimpleDateFormat("HH:mm:ss");
		al = new ArrayList<Client>();
	}
	// a method to start server service
	public void start() {
		//Setting the flag true to mark the continuation of service
		continueflw= true;
		try 
		{
			//Creating a server socket for server
			ServerSocket ss = new ServerSocket(port);

			while(continueflw) 
			{
				show("Server waiting for Clients on port " + port + ".");
				//Accepts request from client via accept() method
				Socket soc= ss.accept();
				//breaks the flow when server is terminated
				if(!continueflw)
					break;
			    // Client thread created on successfull connection
				Client client= new Client(soc);
				// Add the connected user to arraylist
				al.add(client);
                //Start the client thread
				client.start();
			}
			//If intentionally stopping the server
			try {
				//Closing Server scoket 
				 ss.close();
				for(int i = 0; i < al.size(); ++i) {
					Client tc = al.get(i);
					try {
						//Closing all data streams
					tc.in.close();
					tc.out.close();
					tc.soc.close();
					}
					catch(IOException e) {
					}
				}
			}
			catch(Exception e) {
				show("Exception arised due to interruption!Closing the server and clients: " + e);
			}
		}
		catch (IOException e) {
            String msg = datefmt.format(new Date()) + " Exception arised on new ServerSocket: " + e + "\n";
			show(msg);
		}
	}
	
	//Method to stop server
	protected void stopserver() {
		continueflw = false;
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			System.out.println("See ya! Server is stopped!!"+ e);
		}
	}
	
	//a method to display a message along with timestamp
	private void show(String msg) {
		String time = datefmt.format(new Date()) + " " + msg;
		System.out.println(time);
	}
	
	//Method to broadcast message to all clients
	private synchronized boolean broadcast(String message) {
		//Timestamp
		String time = datefmt.format(new Date());
		//Using this string array for private message operation
		String[] w = message.split(" ",3);
		//Flag to set up to true  private message option is selected
		boolean isPrivate = false;
		//When the first character matches the dollar sign the private flag is set to true
		if(w[1].charAt(0)=='$') 
			isPrivate=true;
		if(isPrivate==true)
		{
			//Separates the symbol and username by using the substring function
			String tocheck=w[1].substring(1, w[1].length());
			
			message=w[0]+w[2];
			String messagefinal = time + " " + message + "\n";
			boolean found=false;
			//finding the username from the list and match with the string
			for(int y=al.size(); --y>=0;)
			{
				Client ct1=al.get(y);
				String check=ct1.getUsername();
				if(check.equals(tocheck))
				{
					//Writing to client if server fails to remove it from list
					if(!ct1.writeMsg(messagefinal)) {
						al.remove(y);
						show("Disconnected Client " + ct1.username + " removed from list.");
					}
					//Username match found and msg sent
					found=true;
					break;
				}
				
				
				
			}
			//If match not found , return
			if(found!=true)
			{
				return false; 
			}
		}
		//Broadcast message case
		else
		{
			String messagefinal = time + " " + message + "\n";
			//broadcast message sent by client to all other clients
			System.out.print(messagefinal);
			
			for(int i = al.size(); --i >= 0;) {
				Client ct = al.get(i);
				if(!ct.writeMsg(messagefinal)) {
					al.remove(i);
					show("Disconnected Client is " + ct.username + " removed from list.");
				}
			}
		}
		return true;
		
		
	}

	synchronized void removeClient(int id) {
		
		String disconnectedClient = "";
		for(int i = 0; i < al.size(); ++i) {
			Client ct = al.get(i);
			if(ct.id == id) {
				disconnectedClient = ct.getUsername();
				al.remove(i);
				break;
			}
		}
		broadcast(not + disconnectedClient + " has left the chat room." + not);
	}
	
	public static void main(String[] args) { 
		//Default server parameters
		int portNumber = 5000;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Wrong arguement format");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Correct format is: > java VChatServer portNumber");
				return;
				
		}
		//Server object
		VChatServer server = new VChatServer(portNumber);
		//Starting server service
		server.start();
	}
    //Client class used to create client threads
	class Client extends Thread {
		//client socket
		Socket soc;
		//Object streams
		ObjectInputStream in;
		ObjectOutputStream out;
		//unique id
		int id;
		//username , chat object to get type of option selected,timestamp
		String username;
		Chat chat;
		String date;

		// Client Constructor
		Client(Socket soc) {
			
			id = ++uId;
			this.soc = soc;
			System.out.println("Client Thread trying to create Object I/O Streams");
			//Creating the data object streams
			try
			{
				out = new ObjectOutputStream(soc.getOutputStream());
				in = new ObjectInputStream(soc.getInputStream());
				//reading username from socket input stream
				username = (String) in.readObject();
				//Broadcast message sent to all when a new user joins
				broadcast(not + username + " has joined the chat room." + not);
			}
			catch (IOException e) {
				show("Exception arised while creating new I/O Streams: " + e);
				return;
			}
			catch (ClassNotFoundException e) {
			}
            date = new Date().toString() + "\n";
		}
		
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
        //run() method to the run the client thread continuously
		public void run() {
			boolean continueflw = true;
			//Counter to count the users
			int count=0;
			while(continueflw) {
				try {
					//Other than username rest all data entered are passed to chat class for setting and getting them
					//Hence reading the data from data input streams as chat objects
					chat = (Chat) in.readObject();
				}
				catch (IOException e) {
					show(username + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				//Getting the message from the created chat object
				String message = chat.getMSG();
                //Switch case method which performs the option selected by user
				switch(chat.getType()) {
                //When it is a message type
				case Chat.MSG:
					boolean confirmation =  broadcast(username + ": " + message);
					if(confirmation==false){
						String msg = not + "I am afraid there is no such user in the room." + not;
						writeMsg(msg);
					}
					break;
					//Logout case
				case Chat.Logout:
					show(username + " disconnected with a LOGOUT message.");
					continueflw = false;
					break;
					//names of active users in chat room
				case Chat.WHOISIN:
					writeMsg("List of the users connected at " + datefmt.format(new Date()) + "\n");
					
					
					for(int i = 0; i < al.size(); ++i) {
						Client ct = al.get(i);
						writeMsg((i+1) + ") " + ct.username + " since " + ct.date);
					}
					break;
					//user count case
				case Chat.UserCount:
				    writeMsg("Number of users connected at" + datefmt.format(new Date()) + "\n");
				    for(int i = 1; i < al.size()+1; ++i) {
					   count++;
				    }
					writeMsg("Total numbers of users connected to server: "+(count)+"\n");
					count=0;
				    break;
				}
			}
			//Out of this loops means client is disconnected and client is removed from list
			removeClient(id);
			close();
		}
		
		// closes socket and streams
		private void close() {
			try {
				if(out != null)
				{
					out.close();
				} 
			}
			catch(Exception e) {}
			try {
				if(in != null)
				{
					in.close();
				} 
			}
			catch(Exception e) {};
			try {
				if(soc != null)
				{
					soc.close();
				} 
			}
			catch (Exception e) {}
		}

		//method to Write to Client output stream
		private boolean writeMsg(String msg) {
			//Checks if socket is connected or not,if not then it closes socket and streams
			if(!soc.isConnected()) {
				close();
				return false;
            }
			try {
				out.writeObject(msg);
			}
			catch(IOException e) {
				show(not + "Err!!! Failed to send message to " + username + not);
				show(e.toString());
			}
			return true;
		}
	}
}