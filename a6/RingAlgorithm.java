import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RingAlgorithm {
    // Process class representing a node in the ring
    static class Process implements Runnable {
        private int id;
        private boolean isActive;
        private List<Process> ring; // List of all processes in the ring
        private int position; // Position in the ring
        private Process leader; // Current leader
        private Random random;

        public Process(int id, int position) {
            this.id = id;
            this.isActive = true;
            this.position = position;
            this.ring = new ArrayList<>();
            this.random = new Random();
        }

        public int getId() {
            return id;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setRing(List<Process> ring) {
            this.ring = ring;
        }

        // Get the next active process in the ring
        private Process getNextActiveProcess() {
            int nextPos = (position + 1) % ring.size();
            Process next = ring.get(nextPos);
            // Skip inactive processes
            while (!next.isActive()) {
                nextPos = (nextPos + 1) % ring.size();
                next = ring.get(nextPos);
                if (nextPos == position) {
                    return this; // Only this process is active
                }
            }
            return next;
        }

        // Initiate an election
        private void startElection() {
            if (!isActive) return;
            System.out.println("Process " + id + " initiates election.");
            List<Integer> electionList = new ArrayList<>();
            electionList.add(id);
            sendElectionMessage(electionList);
        }

        // Send Election message to the next process
        private void sendElectionMessage(List<Integer> electionList) {
            Process next = getNextActiveProcess();
            if (next == this) {
                // Only this process is active, declare self as leader
                becomeLeader();
            } else {
                System.out.println("Process " + id + " sends Election message to Process " + next.getId() + " with list: " + electionList);
                next.receiveElectionMessage(electionList, this);
            }
        }

        // Receive Election message
        public synchronized void receiveElectionMessage(List<Integer> electionList, Process sender) {
            if (!isActive) return;
            System.out.println("Process " + id + " received Election message from Process " + sender.getId() + " with list: " + electionList);

            if (electionList.contains(id)) {
                // Message has completed the ring, determine leader
                int maxId = electionList.stream().max(Integer::compare).orElse(id);
                System.out.println("Process " + id + " determines leader ID: " + maxId);
                sendCoordinatorMessage(maxId);
            } else {
                // Add own ID and forward the message
                electionList.add(id);
                sendElectionMessage(electionList);
            }
        }

        // Send Coordinator message to announce the leader
        private void sendCoordinatorMessage(int leaderId) {
            Process next = getNextActiveProcess();
            if (next == this) {
                // Only this process is active
                becomeLeader();
            } else {
                System.out.println("Process " + id + " sends Coordinator message to Process " + next.getId() + " with leader ID: " + leaderId);
                next.receiveCoordinatorMessage(leaderId, this);
            }
        }

        // Receive Coordinator message
        public synchronized void receiveCoordinatorMessage(int leaderId, Process sender) {
            if (!isActive) return;
            System.out.println("Process " + id + " received Coordinator message from Process " + sender.getId() + " with leader ID: " + leaderId);
            // Set leader to the process with leaderId
            for (Process p : ring) {
                if (p.getId() == leaderId) {
                    leader = p;
                    System.out.println("Process " + id + " acknowledges Process " + leaderId + " as leader.");
                    break;
                }
            }
            // Forward Coordinator message
            if (getNextActiveProcess() != this) {
                sendCoordinatorMessage(leaderId);
            }
        }

        // Declare this process as the leader
        private void becomeLeader() {
            System.out.println("Process " + id + " declares itself the leader!");
            leader = this;
            sendCoordinatorMessage(id);
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
            startElection(); // Start election on recovery
        }

        @Override
        public void run() {
            try {
                // Simulate random delay before initiating election
                Thread.sleep(random.nextInt(1000));
                if (isActive) {
                    startElection();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Create processes in a ring
        int numProcesses = 5;
        List<Process> ring = new ArrayList<>();
        for (int i = 0; i < numProcesses; i++) {
            ring.add(new Process(i + 1, i)); // IDs 1 to 5
        }

        // Set the ring for each process
        for (Process p : ring) {
            p.setRing(ring);
        }

        // Create thread pool for concurrent execution
        ExecutorService executor = Executors.newFixedThreadPool(numProcesses);

        // Start all processes
        for (Process p : ring) {
            executor.submit(p);
        }

        // Simulate some process failures and recoveries
        Thread.sleep(2000); // Wait for initial election
        ring.get(4).fail(); // Process 5 fails
        Thread.sleep(2000);
        ring.get(3).fail(); // Process 4 fails
        Thread.sleep(2000);
        ring.get(4).recover(); // Process 5 recovers

        // Shutdown executor
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
}
