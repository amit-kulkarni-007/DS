import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BullyAlgorithm {
    // Process class representing a node in the distributed system
    static class Process implements Runnable {
        private int id;
        private boolean isActive;
        private List<Process> processes;
        private Process leader;
        private Random random;

        public Process(int id) {
            this.id = id;
            this.isActive = true;
            this.processes = new ArrayList<>();
            this.random = new Random();
        }

        public int getId() {
            return id;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setProcesses(List<Process> processes) {
            this.processes = processes;
        }

        // Simulate sending an Election message to higher-ID processes
        private void sendElectionMessage() {
            System.out.println("Process " + id + " is sending Election messages.");
            boolean receivedOk = false;

            for (Process p : processes) {
                if (p.getId() > id && p.isActive()) {
                    System.out.println("Process " + id + " sends Election to Process " + p.getId());
                    p.receiveElectionMessage(this);
                    receivedOk = true;
                }
            }

            // If no higher-ID process responds, this process becomes the leader
            if (!receivedOk) {
                becomeLeader();
            }
        }

        // Handle receiving an Election message
        public synchronized void receiveElectionMessage(Process sender) {
            if (isActive) {
                System.out.println("Process " + id + " received Election from Process " + sender.getId());
                // Respond with OK
                sender.receiveOkMessage(this);
                // Start own election
                sendElectionMessage();
            }
        }

        // Handle receiving an OK message
        public synchronized void receiveOkMessage(Process sender) {
            System.out.println("Process " + id + " received OK from Process " + sender.getId());
            // Do nothing; wait for Coordinator message
        }

        // Declare this process as the leader
        private void becomeLeader() {
            System.out.println("Process " + id + " declares itself the leader!");
            leader = this;
            // Notify all processes
            for (Process p : processes) {
                if (p.isActive() && p.getId() != id) {
                    p.receiveCoordinatorMessage(this);
                }
            }
        }

        // Handle receiving a Coordinator message
        public synchronized void receiveCoordinatorMessage(Process newLeader) {
            System.out.println("Process " + id + " acknowledges Process " + newLeader.getId() + " as leader.");
            leader = newLeader;
        }

        // Simulate process failure
        public synchronized void fail() {
            isActive = false;
            System.out.println("Process " + id + " has failed.");
        }

        // Simulate process recovery
        public synchronized void recover() {
            isActive = true;
            System.out.println("Process " + id + " has recovered.");
            sendElectionMessage(); // Start election on recovery
        }

        @Override
        public void run() {
            try {
                // Simulate random delay before initiating election
                Thread.sleep(random.nextInt(1000));
                if (isActive) {
                    System.out.println("Process " + id + " initiates election.");
                    sendElectionMessage();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Create processes
        int numProcesses = 5;
        List<Process> processes = new ArrayList<>();
        for (int i = 1; i <= numProcesses; i++) {
            processes.add(new Process(i));
        }

        // Set the process list for each process
        for (Process p : processes) {
            p.setProcesses(processes);
        }

        // Create thread pool for concurrent execution
        ExecutorService executor = Executors.newFixedThreadPool(numProcesses);

        // Start all processes
        for (Process p : processes) {
            executor.submit(p);
        }
		System.out.println("Starting\n");
        // Simulate some process failures and recoveries
        Thread.sleep(2000); // Wait for initial election
        processes.get(4).fail(); // Process 5 fails
        System.out.println();
        Thread.sleep(2000);
        processes.get(3).fail(); // Process 4 fails
        System.out.println();
        Thread.sleep(2000);
        processes.get(4).recover(); // Process 5 recovers
		System.out.println();
        // Shutdown executor
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
