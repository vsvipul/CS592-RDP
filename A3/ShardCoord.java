import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry;
import java.io.BufferedReader;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.Random; 
import java.util.Scanner; 

public final class ShardCoord extends ShardCoordImpl {
    private static final ShardCoord instance = new ShardCoord();
    private ShardCoord() {}

    private static ShardCoord getInstance() {
        return instance;
    }

    private void init(int numShards) {
        this.numShards = numShards;
        this.startShardRegions();
        this.checkShardRegionsWorking();
        try {
            String csvFile = "data.csv";
            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            String[] tempArr;
            int i=0;
            System.out.println("Started inserting records from data.csv into " + numShards + " shardRegions.");
            while ((line = br.readLine()) != null) {
                tempArr = line.split(",");
                String key = tempArr[0];
                String value = tempArr[1];
                insertIntoShard(key, value);
                i++;
            }
            br.close();
            System.out.println("Inserted " + Integer.toString(i)+" records into " + numShards + " shardRegions.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void startShardRegions() {
        for (int i = 0; i < this.numShards; i++) {
            String[] args = new String[] { "java", "ShardRegion", Integer.toString(i) };
            ProcessBuilder pb = new ProcessBuilder(args);
            try {
                pb.start();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private void checkShardRegionsWorking() {
        for (int i = 0; i < this.numShards; i++) {
            Boolean tryAgain = true;
            while (tryAgain){
                try {
                    // Get the registry
                    Registry registry = LocateRegistry.getRegistry();
                    // Look up the registry for the remote object
                    ShardRegionInterface stub = (ShardRegionInterface) registry.lookup(Integer.toString(i));
                    System.out.println("shardRegion" +Integer.toString(i)+" started.");
                    tryAgain = false;
                } catch (Exception e) {
                    System.err.println("Still waiting for shardRegion" +Integer.toString(i));
                    tryAgain = true;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException er) {
                        System.err.println("Cannot sleep.");
                    }
                }
            }
        }
    }

    private void insertIntoShard(String key, String value) {
        String shardId = getShardId(key);
        //System.out.println(shardId);
        try {  
            // Get the registry 
            Registry registry = LocateRegistry.getRegistry(); 
            // Look up the registry for the remote object 
            ShardRegionInterface stub = (ShardRegionInterface) registry.lookup(shardId);
            // Pass a message to the remote object
            stub.putvalue(new String[]{key, value}, 1);
         } catch (Exception e) {
            System.err.println("Client exception: " + e.toString()); 
            e.printStackTrace(); 
         } 
    }

    public static void main(String args[]) {
        int numShards = Integer.parseInt(args[0]);
        ShardCoord insta = ShardCoord.getInstance();
        insta.init(numShards);
        try {
            ShardCoordImpl obj = ShardCoord.getInstance();
            // Export the remote object to the stub
            ShardCoordInterface stub = (ShardCoordInterface) UnicastRemoteObject.exportObject(obj, 0);
            // Bind the remote object (stub) in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Coord", stub);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
        }
    }
}