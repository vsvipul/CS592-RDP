import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;  
import java.util.Random; 
import java.util.*;  
import java.io.BufferedReader;
import java.io.FileReader;


public class CreateTestCases {
    public static void main(String[] args) {
        HashMap<String, String> m = new HashMap<String, String>();
        try {
            String csvFile = "data.csv";
            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            String[] tempArr;
            int i=0;
            System.out.println("Started reading records from data.csv.");
            while ((line = br.readLine()) != null) {
                tempArr = line.split(",");
                String key = tempArr[0];
                String value = tempArr[1];
                m.put(key, value);
                i++;
            }
            br.close();
            System.out.println("Inserted " + Integer.toString(i)+" records into map m.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        for (int tc = 0; tc<50;tc++) {
            String fileName = Integer.toString(tc);
            File inObj = new File("./input/"+fileName);
            File outObj = new File("./output/"+fileName);
            try {
                FileWriter inWriter = new FileWriter("./input/"+fileName);
                FileWriter outWriter = new FileWriter("./output/"+fileName);
                String[] stateCodes = new String[]{"KL", "UK", "HR", "MH", "HP", "MP", "CG", "RJ", "DL", "UP", "AP", "KA", "JK"};
                for (int i=0;i<1000;i++){     
                    Random rand = new Random(); 
                    String randState = stateCodes[rand.nextInt(stateCodes.length)];
                    String num7 = Integer.toString(rand.nextInt(9000000) + 1000000);
                    int randAction = rand.nextInt(2);
                    String key = randState + num7;
                    String value = Integer.toString(rand.nextInt(90000) + 10000);
                    if (randAction == 0){
                        inWriter.write("P "+ key + " " + value +"\n");
                        m.put(key, value);
                    } else {
                        inWriter.write("G "+ randState + num7 +"\n");
                        String res = "";
                        if (m.containsKey(key)){
                            res = m.get(key);
                        }
                        outWriter.write(res +"\n");
                    }
                }
                inWriter.close();
                outWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}