import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
public class RMIServer extends RMIServImpl{
    public RMIServer() throws RemoteException
    {

    }
    public static void main(String args[]) throws RemoteException{
     
    try
    {
      Registry registry= LocateRegistry.createRegistry(5000);
      RMIServImpl serv = new RMIServImpl();
      registry.rebind("rmiServer",serv);
      System.out.println("SERVER operation completed!");
    }
    catch(Exception e)
    {
      System.out.println(e);
    }

  }
}