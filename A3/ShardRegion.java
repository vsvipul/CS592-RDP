import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry;
import java.io.BufferedReader;
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject; 

public class ShardRegion extends ShardRegionImpl {
    public static void main(String[] args) {
        String regionId = args[0];
        myshardId = regionId;
        System.out.println("Started shardRegion" + regionId);
        try { 
            // Instantiate the implementation class 
            ShardRegionImpl obj = new ShardRegion(); 
            // Export the remote object to the stub 
            ShardRegionInterface stub = (ShardRegionInterface) UnicastRemoteObject.exportObject(obj, 0);  
            // Bind the remote object (stub) in the registry 
            Registry registry = LocateRegistry.getRegistry(); 
            registry.bind(regionId, stub);  
        } catch (Exception e) { 
            System.err.println("Server exception: " + e.toString()); 
            e.printStackTrace(); 
        }
    }
}