// RDP CS592 Assignment 2
// by Vipul B17069

import java.util.concurrent.*;

public class Main {
    // Sum of all Primes between 1 and MAX.
    private static final int MAX = 1000000;

    // Checks if n is prime or not
    // Returns a boolean
    private static boolean isPrime(int n){
        if (n<2) return false;
        for (int i=2 ; i*i <= n ; i++){
            if (n%i==0){
                return false;
            }
        }
        return true;
    }

    // A callable findSumPrimes which calculates sum of all primes in [start, start+step, start+2*step, start+3*step ...... upto MAX]
    public static class findSumPrimes implements Callable<Long> {
        int start, step, max;
        public findSumPrimes(int start, int step, int max){
            this.start = start;
            this.step = step;
            this.max = max;
        }
        @Override
        public Long call() {
            long sum = 0;
            // Start from "start" and go up to "max" with steps of "step"
            // This is to ensure that work divided between the threads equally.
            for (int i=start ; i<=max ; i+=step){
                if (isPrime(i)){
                    sum += i;
                }
            }
            return sum;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // Single Threaded Version to verify
        long sumPrimes = 0;
        System.out.println("Case: SingleThreaded");
        long startTime = System.nanoTime();
        for (int i=1;i<=MAX;i++){
            if (isPrime(i)) {
                sumPrimes += i;
            }
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("Execution time: "+elapsedTime/1000000 + " ms");
        System.out.println();
        System.out.println("Final Sum of Primes: "+ sumPrimes);
        System.out.println();

        // Fixed Thread Pool Experiment with different values of N and nThreads
        // nvals = N = Number of Tasks or Subranges
        int nvals[] = {100, 200, 500, 1000};

        // nThreads = Number of Threads created in FixedThreadPool
        for (int nThreads = 2; nThreads<=64; nThreads*=2){
            System.out.println("FixedThreadPool with nThreads=" + nThreads + " :");
            for (int i=0; i<nvals.length ; i++){
                System.out.println("Case: N=" + nvals[i]);
                startTime = System.nanoTime();
                ExecutorService executor = Executors.newFixedThreadPool(nThreads);
                CompletionService<Long> completionService = new ExecutorCompletionService<Long>(executor);
                for (int j=1; j<=nvals[i] ; j++) {
                    completionService.submit(new findSumPrimes(j, nvals[i], MAX));
                }
                int received = 0;
                boolean errors = false;
                long totalSum = 0;

                // Wait for each Future to generate a result and aggregate the results in a single variable totalSum
                while (received< nvals[i] && !errors) {
                    Future <Long> resultFuture = completionService.take();
                    try {
                        long curResult = resultFuture.get();
                        received++;
                        totalSum += curResult;
                    } catch (Exception e) {
                        errors = true;
                    }
                }
                executor.shutdown();
                endTime = System.nanoTime();
                elapsedTime = endTime - startTime;
                System.out.println("Execution time: "+elapsedTime/1000000 + " ms");
                System.out.println();
                System.out.println("Final Sum of Primes: "+ totalSum);
                System.out.println();
            }
        }
    }
}
