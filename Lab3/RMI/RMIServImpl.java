import java.rmi.*;
import java.rmi.server.*;
public class RMIServImpl extends UnicastRemoteObject implements RMIServInt{
  public RMIServImpl() throws RemoteException
  {
    super();
  }
  public double sum(double n1, double n2) throws RemoteException{
    return n1+n2;
  }
}