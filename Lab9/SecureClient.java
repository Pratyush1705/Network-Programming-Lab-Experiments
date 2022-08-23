import java.util.*;
import java.io.*;
import java.net.*;

public class SecureClient {
	public static void main(String args[]) throws IOException {
		Scanner scan = new Scanner(System.in);
		System.out.println("Connecting to the server...");
		Socket clientSocket = new Socket("localhost", 7777);
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		DataInputStream din=new DataInputStream(clientSocket.getInputStream());  
		DataOutputStream dout=new DataOutputStream(clientSocket.getOutputStream());  
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
		
		// Client enters ID. This will be used by the program for verifying who
		// is communicating as well as check the OTP against the ID, on the
		// server side
		System.out.println("Enter your ID:");
		String id = scan.nextLine();
		System.out.println("Contacting server...");
		out.println(id);
		System.out.println("Server has sent the OTP. Please enter it here:");
		String otp = scan.nextLine();
		System.out.println("Verifying...");
		out.println(id);
		out.println(otp);
                din.readUTF();
		System.out.println(in.readLine());
		String str="",str2=""; 
		while(!str.equals("stop")){  
			str=br.readLine();  
			dout.writeUTF(str);  
			dout.flush();  
			str2=din.readUTF();  
			System.out.println("Server says: "+str2);  
			}  
			  
			dout.close();
		
		in.close();
		out.close();
		clientSocket.close();
	}
}