package primeSieve;


public class multithreadedPrimes {

    public static void main(String[] args) {

        final long startTime = System.currentTimeMillis();

        Integer primeMax = 1_000_000;
        int numOfPrimes = 100_000;
        // its the number of primes - 1 (because this algorithm doesnt worry about 2 or
        // multiples of it)
        // if primeMax = 100, numOfPrimes = 24
        // if 1,000 then 167
        // if 10,000 then 1228
        // if 1,000,000, then  78,497
        // if 100,000,000, then 5,761,454
        // if primeMax = 1,000,000,000 then numOfPrimes = 50,847,533
        // --------------------------------------------------------------------------------------------------

        sieveMethods erat = new sieveMethods(primeMax);

        int[] primeArray = erat.sieveOfEratosthenes(numOfPrimes);

        // for(int i =0; i<primeArray.length; i++){
        // System.out.println(primeArray[i]);
        // }

        System.out.println("Base prime array counted");

        final long firstEndTime = System.currentTimeMillis();
        System.out.println("Initial execution time: " + (firstEndTime - startTime) + " milliseconds");

        long start = 0;
        int add = 1_000_000;
        int newPrimeLength = 100_000;
        // optimal numOfThreads ~ the amount of cores you CPU has (for my laptop I used 5 threads)
        int numOfThreads = 1;
        long startDifference = 200_000_000L;

        multithreadMethods threadi = new multithreadMethods(start, 1, primeArray,
                newPrimeLength, 1, numOfThreads, startDifference);
        Thread t1 = new Thread(threadi);

        for (int i = 0; i < numOfThreads; i++) {
            threadi = new multithreadMethods(start + (startDifference * i),
                    add, primeArray, newPrimeLength, i, numOfThreads, startDifference);
            t1 = new Thread(threadi);
            t1.start();
        }

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Total time elapsed: " + (endTime - startTime) + " milliseconds");
    }

}