import java.rmi.*;
import java.rmi.server.*;
public interface RMIServInt extends Remote{
 public double sum(double n1, double n2) throws RemoteException;
}