import java.util.*;
import java.io.*;
import java.net.*;

class TimeOutTask extends TimerTask {
	boolean isTimedOut = false;
	
	public void run() {
		isTimedOut = true;
	}
}

public class SecureServer {
	public static void main(String args[]) throws IOException {
		ServerSocket serverSocket = new ServerSocket(7777);
		System.out.println("Server running and waiting for client...");
		Socket clientSocket = serverSocket.accept();
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
		DataInputStream din=new DataInputStream(clientSocket.getInputStream());  
		DataOutputStream dout=new DataOutputStream(clientSocket.getOutputStream());  
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
		
		// Server waits for a client to send its user ID
		String id = in.readLine();
		
		// Server generates an OTP and waits for client to send this
		Random r = new Random();
		String otp = new String();
		for(int i=0 ; i < 8 ; i++) {
			otp += r.nextInt(10);
		}
		System.out.println(otp);
                dout.writeUTF(otp);
		
		// Server starts a timer of 10 seconds during which the OTP is valid.
		TimeOutTask task = new TimeOutTask();
		Timer t = new Timer();
		t.schedule(task, 100000L);
		
		// Server listens for client to send its ID and OTP to check if it is
		// valid
		String newId = in.readLine();
		String newOtp = in.readLine();
		String str="",str2="";
		if(newId.equals(id)) {
			// User ID is verified
			if(task.isTimedOut) {
				// User took more than 100 seconds and hence the OTP is invalid
				out.println("Time out!");
			} else if(!newOtp.equals(otp)) {
				out.println("Incorrect OTP!");
			} else {
				out.println("Logged In!");
				while(!str.equals("stop")){  
					str=din.readUTF();  
					System.out.println("client says: "+str);  
					str2=br.readLine();  
					dout.writeUTF(str2);  
					dout.flush();  
					}  
					din.close(); 
			}
		}
		clientSocket.close();
		serverSocket.close();
		System.exit(0);
	}
}