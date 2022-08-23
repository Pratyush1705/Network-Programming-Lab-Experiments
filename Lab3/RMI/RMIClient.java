import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class RMIClient{
   public static void main(String[]args) throws RemoteException{
     try
     {
        double n1 = Double.parseDouble(args[0]);
        double n2 = Double.parseDouble(args[1]);
        Registry registry = LocateRegistry.getRegistry("127.0.0.1",5000);
        RMIServInt a = (RMIServInt)registry.lookup("rmiServer");
        System.out.println("First number value: "+n1);
        System.out.println("Second number value: "+n2);
        System.out.println("Sum of the two numbers:\t"+a.sum(n1,n2));
     }
     catch(Exception e)
     {
        System.out.println(e);
     }

  }

}