import java.rmi.Remote; 
import java.rmi.RemoteException;  

public interface ShardRegionInterface extends Remote {
   public String getValue(String key) throws RemoteException;
   public void putvalue(String[] tup, int force) throws RemoteException;
}