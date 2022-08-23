import java.io.IOException;
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.util.*;
public class udpServer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		DatagramSocket ds = new DatagramSocket(1234);
		 
		byte[] receive = new byte[65535];

		DatagramPacket DpReceive = null; 
		while (true)
		{
		 
		 DpReceive = new DatagramPacket(receive,receive.length);
		 ds.receive(DpReceive);
                 System.out.println("Client Says:-" + data(receive));
                 Arrays.fill(receive,(byte)0);
		 if (data(receive).toString().equals("bye"))
		 {
		    System.out.println("Client sent Bye!!EXITING");
		    break;
		 }

		
		}
		ds.close();
		
	 }
		

		public static StringBuilder data(byte[] a)
		{
		  if (a == null)
		  return null;
		  StringBuilder r = new StringBuilder(); 
		  int i = 0;

		  while (a[i]!= 0)
		  {
		    r.append((char)a[i]); 
		    i++;
		  }
		  return r;
      
	   }

}
