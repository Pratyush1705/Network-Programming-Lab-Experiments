import java.net.*;
import java.util.*;
import java.io.*;
//Client side program 
public class VChatClient{
    
   private String not= "***!!";
   //Making using of Object Input and Output streams to transfer data through sockets.
   private ObjectInputStream in;
   private ObjectOutputStream out;
   //Client Socket
   private Socket soc;
   // Server address, username and port variables
   private String server,uname;
   private int portno;
   //Client constructor
   VChatClient(String server,int portno,String uname)
   {
       this.server=server;
       this.portno=portno;
       this.uname=uname;
   }
  //Setter and getter  Method to set and retrieve username 
   public void setUname(String uname)
   {
       this.uname=uname;
   }
   public String getUname()
   {
       return uname;
   }
   //A display method to display strings
   private void show(String Msg)
   {
       System.out.println(Msg);
   }
    //Sends Message to server
   void sendMsg(Chat msg) {
     try 
     {
        out.writeObject(msg);
     }
     catch(IOException e) 
     {
        show("Exception arised while writing to server: " + e);
     }
   }
    //A method to close Socket and the streams when user logs out or any issue arises.
    private void disconnect() {
    try
     { 
        if(in != null) 
        {
            in.close();
        }
        
     }
     catch(Exception e) {}
     try 
     {
        if(out != null)
        {
            out.close();
        } 
     }
     catch(Exception e) {}
     try
     {
        if(soc != null) 
        {
            soc.close();
        }
       
     }
    catch(Exception e) {}
        
   }

   //the start() method starts the chat service
   public boolean start(){
       try 
       {
           //Establishing client socket by passing server add and port no
           soc=new Socket(server,portno);
           System.out.println("Client Connection");
       } 
       catch (Exception e) {
           //TODO: handle exception
           show("OOPS!Facing issues connecting to server: "+e);
           return false;
       }
        String Message = "Connection established successfully " + soc.getInetAddress() + ":" + soc.getPort();
        show(Message);
        //Creating both input and output Data streams and passing the input and output stream into the created object streams
       try{
           in=new ObjectInputStream(soc.getInputStream());
           out=new ObjectOutputStream(soc.getOutputStream());
       }
       catch(IOException ex)
       {
           show("Exception arised while creating new I/O Streams: "+ex);
           return false;
       }
       //starts a thread to listen to server
       new LFromServer().start();
       //Sending username to Server in string format
       try
       {
            out.writeObject(uname);
       }
       catch(IOException ex)
       {
           show("Exception caused while logging in : "+ex);
           disconnect();
           return false;
       }
       return true;
    }
    //Main Method
    public static void main(String[] args)
    {
        //Default port used here is 5000 if user doesnt specify port to start server.
        int portnum=5000;
        //Default parameters 
        String serverAdd="localhost";
        String user= "anonymous";
        Scanner sin=new Scanner(System.in);

        System.out.println("Enter a name as a username:");
        user=sin.nextLine();
        switch(args.length)
        {
            case 3:
               //Passes the username portno and server address as arguements
               serverAdd=args[2];
            case 2:
               try
               {
                 //Passing username and portno as args
                 portnum=Integer.parseInt(args[1]);
               }
               catch(Exception e)
               {
                   System.out.println("Wrong Port number");
                   System.out.println("Wrong Format of taking arguements");
                   return;
               }
            case 1:
             //Passes only username as args
              user = args[0];
            case 0:
               break;
            default:
               System.out.println("Correct Format is java VChatClient user portnum serverAdd");
            return;
        }
        //Client object 
        VChatClient vcl= new VChatClient(serverAdd,portnum,user);
        //using the client object to start client service and returns back when server instance isnt running
        if(!vcl.start())
            return;
        System.out.println("\nHOLAAA! Welcome To VCHATT!!! CHAT ROOM.");
        System.out.println("Select Service of your choice:");
        System.out.println("1) Send a broadcast message to all clients by just typing the message");
        System.out.println("2) Type '$username<space>message' to send a  private message to a desired client");
        System.out.println("3) Type 'WHOISIN' to find out the active users present in chat room");
        System.out.println("4) Type 'U' to display the number of users connected to server");
        System.out.println("5) Type 'LOGOUT' to logoff from server");

        //Infinite loop which runs as long as the client wishes to stay and exits on LOGOUT
        while(true)
        {
            System.out.print("> ");
            //Users Choice or action
            String msg=sin.nextLine();
            //Compares the entered choice by user and sends passes to the Chat class method on match
            if(msg.equalsIgnoreCase("LOGOUT")) {
				vcl.sendMsg(new Chat(Chat.Logout, ""));
				break;
			}
			
			else if(msg.equalsIgnoreCase("WHOISIN")) {
				vcl.sendMsg(new Chat(Chat.WHOISIN, ""));				
			}
			
			else if(msg.equalsIgnoreCase("U")){
				vcl.sendMsg(new Chat(Chat.UserCount, ""));
			}
			else {
				vcl.sendMsg(new Chat(Chat.MSG, msg));
			}
		}
		//Closing scanner class
		sin.close();
		//Disconnects the client
		vcl.disconnect();
        
        
    }
    //A class which extends Thread and waits for a response from server
    class LFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					
					String msg = (String) in.readObject();
					System.out.println(msg);
					System.out.print("> ");
				}
				catch(IOException e) {
					show(not + "User logged out successfully! closing service!: " + e + not);
					break;
				}
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}

}