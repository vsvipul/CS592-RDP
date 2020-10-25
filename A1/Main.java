import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int MAX = 100000;
    private static int[] is_prime = new int[MAX+1];

    private static boolean isPrime(int n){
        if (n<2) return false;
        for (int i=2 ; i*i <= n ; i++){
            if (n%i==0){
                return false;
            }
        }
        return true;
    }

    public static class findAllPrimes implements Runnable {
        int start, end;
        public findAllPrimes(int start, int end){
            this.start = start;
            this.end = end;
        }
        @Override
        public void run() {
//            System.out.println("starting"+this.start);
            for (int i=start ; i<=end ; i++){
                if (isPrime(i)){
                    is_prime[i]=1;
                }
            }
//            System.out.println("starting"+this.end);
        }
    }

    public static void printPrimesFound(){
        int res=0;
        for (int i=1;i<=MAX;i++){
            if (is_prime[i]==1){
                res++;
            }
        }
        System.out.println("Number of primes found: " + res);
    }
    public static void main(String[] args) {
	// write your code here
        int nvals[] = {100, 200, 500, 1000};

        System.out.println("SingleThreadExecutor:");
        for (int i=0; i<nvals.length ; i++){
            System.out.println("Case: N=" + nvals[i]);
            long startTime = System.nanoTime();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            int perthread=MAX/nvals[i];
            for (int j=0; j<nvals[i] ; j++) {
                executor.execute(new findAllPrimes((j*perthread)+1, (j+1)*perthread));
            }
            executor.shutdown();
            try {
                executor.awaitTermination(10, TimeUnit.SECONDS);
            } catch (Exception e){
                System.out.println("done");
            }
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            System.out.println("Execution time: "+elapsedTime/1000000 + " ms");
            printPrimesFound();
            System.out.println();
        }

        for (int nThreads = 2; nThreads<=8; nThreads*=2){
            System.out.println("FixedThreadPool with nThreads=" + nThreads + " :");
            for (int i=0; i<nvals.length ; i++){
                System.out.println("Case: N=" + nvals[i]);
                long startTime = System.nanoTime();
                ExecutorService executor = Executors.newFixedThreadPool(nThreads);
                int perthread=MAX/nvals[i];
                for (int j=0; j<nvals[i] ; j++) {
                    executor.execute(new findAllPrimes((j*perthread)+1, (j+1)*perthread));
                }
                executor.shutdown();
                try {
                    executor.awaitTermination(10, TimeUnit.SECONDS);
                } catch (Exception e){
                    System.out.println("done");
                }
                long endTime = System.nanoTime();
                long elapsedTime = endTime - startTime;
                System.out.println("Execution time: "+elapsedTime/1000000 + " ms");
                printPrimesFound();
                System.out.println();
            }
        }

        System.out.println("CachedThreadPool:");
        for (int i=0; i<nvals.length ; i++){
            System.out.println("Case: N=" + nvals[i]);
            long startTime = System.nanoTime();
            ExecutorService executor = Executors.newCachedThreadPool();
            int perthread=MAX/nvals[i];
            for (int j=0; j<nvals[i] ; j++) {
                executor.execute(new findAllPrimes((j*perthread)+1, (j+1)*perthread));
            }
            executor.shutdown();
            try {
                executor.awaitTermination(10, TimeUnit.SECONDS);
            } catch (Exception e){
                System.out.println("done");
            }
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            System.out.println("Execution time: "+elapsedTime/1000000 + " ms");
            printPrimesFound();
            System.out.println();
        }

    }
}
