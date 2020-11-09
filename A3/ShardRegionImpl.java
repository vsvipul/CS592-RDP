import java.util.*;  
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  

public class ShardRegionImpl implements ShardRegionInterface {
    private HashMap<String, String> m = new HashMap<String, String>();
    public static String myshardId = "";
    public String getValue(String key){
        if (m.containsKey(key)){
            return m.get(key);
        }
        try {
            // Get the registry 
            Registry registry = LocateRegistry.getRegistry(); 
            // Look up the registry for the remote object 
            ShardCoordInterface stub = (ShardCoordInterface) registry.lookup("Coord"); 
            String shardId = stub.getShardId(key);

            if (shardId.equals(myshardId)) {
                return "";
            }
            ShardRegionInterface stub2 = (ShardRegionInterface) registry.lookup(shardId);
            return stub2.getValue(key);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString()); 
            e.printStackTrace(); 
        }
        return "";
    }

    public void putvalue(String[] tup, int force){
        String key = tup[0];
        String val = tup[1];
        if (force == 1) {
            m.put(key, val);
        } else {
            try {
                // Get the registry 
                Registry registry = LocateRegistry.getRegistry(); 
                // Look up the registry for the remote object 
                ShardCoordInterface stub = (ShardCoordInterface) registry.lookup("Coord"); 
                String shardId = stub.getShardId(key);
                if (shardId.equals(myshardId)) {
                    m.put(key, val);
                    return;
                }
                ShardRegionInterface stub2 = (ShardRegionInterface) registry.lookup(shardId);
                stub2.putvalue(tup, 1);
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString()); 
                e.printStackTrace(); 
            } 
        }
    }
}