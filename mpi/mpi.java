import java.util.concurrent.atomic.AtomicInteger;

public class DistributedSumOpenMP {

    static final int N = 10; // Number of elements in the array
    static final int NUM_THREADS = 4; // Number of threads/processes

    public static void main(String[] args) {
        int[] array = new int[N];
        AtomicInteger globalSum = new AtomicInteger(0);

        // Initialize array with values
        for (int i = 0; i < N; i++) {
            array[i] = i + 1; // Example: 1, 2, 3, ..., N
        }

        // Create and start worker threads
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                int start = threadId * (N / NUM_THREADS);
                int end = (threadId == NUM_THREADS - 1) ? N : (threadId + 1) * (N / NUM_THREADS);
                int localSum = 0;
                for (int j = start; j < end; j++) {
                    localSum += array[j];
                }
                globalSum.addAndGet(localSum); // Add local sum to global sum atomically
                System.out.println("Thread " + threadId + " calculated sum: " + localSum);
            });
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Total sum: " + globalSum.get());
    }
}

