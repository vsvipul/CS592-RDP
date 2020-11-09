import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry;
import java.io.FileWriter;  

public class Client {
    public static void main(String[] args) {
        String fileName = args[0];
        String shardRegion = args[1];
        try {
            FileWriter outWriter = new FileWriter("./cur_output/"+fileName);
            File infile = new File("./input/" + fileName);
            FileReader fr = new FileReader(infile);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            String line2 = "";
            String[] tempArr;
            System.out.println("Started reading test cases from "+fileName);
            try {  
                // Get the registry 
                Registry registry = LocateRegistry.getRegistry(); 
                // Look up the registry for the remote object 
                ShardRegionInterface stub = (ShardRegionInterface) registry.lookup(shardRegion); 
                while ((line = br.readLine()) != null) {
                    tempArr = line.split(" ");
                    String action = tempArr[0];
                    String key = tempArr[1];
                    if (action.equals("P")) {
                        String value = tempArr[2];
                        stub.putvalue(new String[]{key, value}, 0);
                    } else if (action.equals("G")){
                        String res = stub.getValue(key);
                        outWriter.write(res +"\n");
                    }
                }
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString()); 
                e.printStackTrace(); 
            } 
            br.close();
            outWriter.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}