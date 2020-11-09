import java.util.Scanner; 
import java.util.Random; 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class RunTestCases {
    private static void runTestCases(int numShards, int tc) {
        Random rand = new Random();
        for (int i=0;i<tc;i++) {
            String curShard = Integer.toString(rand.nextInt(numShards));
            String args = "java Client " + Integer.toString(i) + " " + curShard;
            ProcessBuilder pb = new ProcessBuilder("/usr/bin/bash", "-c", args);
            try {
                pb.start();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private static void checkTestCasesOutput(int numShards, int tc) {
        boolean allTestsPassed = true;
        for (int i=0;i<tc;i++) {
            try {
                File f1 = new File("./output/"+Integer.toString(i));
                File f2 = new File("./cur_output/"+Integer.toString(i));
                FileReader fR1 = new FileReader(f1);
                FileReader fR2 = new FileReader(f2);
                BufferedReader reader1 = new BufferedReader(fR1);
                BufferedReader reader2 = new BufferedReader(fR2);
                String line1 = null;
                String line2 = null;
                int flag = 1;
                try {
                    while ((flag == 1) && ((line1 = reader1.readLine()) != null)
                            && ((line2 = reader2.readLine()) != null)) {
                        if (!line1.equals(line2))
                            flag = 0;
                    }
                    reader1.close();
                    reader2.close();
                } catch(IOException e) {
                    System.out.println(e);
                }
                if (flag==0){
                    allTestsPassed = false;
                }
            } catch(FileNotFoundException e){
                System.out.println(e);
            }
        }
        if (allTestsPassed){
            System.out.println("All test cases passed. :)");
        } else {
            System.out.println("Test cases did not pass. :(");
        }
    }

    public static void main(String args[]) {
        int numShards = Integer.parseInt(args[0]);
        runTestCases(numShards, 50);
        System.out.println("Running done.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException er) {
            System.err.println("Cannot sleep.");
        }
        System.out.println("Checking diff.");
        checkTestCasesOutput(numShards, 50);
    }
}