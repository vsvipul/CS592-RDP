import java.rmi.Remote; 
import java.rmi.RemoteException;  

public interface ShardCoordInterface extends Remote {
    public String getShardId(String key) throws RemoteException;
}