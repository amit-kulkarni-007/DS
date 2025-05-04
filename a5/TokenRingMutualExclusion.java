import java.util.Scanner;

public class TokenRingMutualExclusion {

    private int numProcesses;
    private boolean[] requestingCriticalSection;
    private int tokenHolder;

    public TokenRingMutualExclusion(int numProcesses) {
        this.numProcesses = numProcesses;
        this.requestingCriticalSection = new boolean[numProcesses];
        this.tokenHolder = 0; // Start with process 0 holding the token
    }

    public void requestCriticalSection(int processId) {
        requestingCriticalSection[processId] = true;
    }

    public void releaseCriticalSection(int processId) {
        requestingCriticalSection[processId] = false;
    }

    public void passToken() {
        int next = (tokenHolder + 1) % numProcesses;
        tokenHolder = next;
    }

    public void runTokenRing() {
        Scanner sc = new Scanner(System.in);
        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\nCurrent token holder: Process " + tokenHolder);
            if (requestingCriticalSection[tokenHolder]) {
                System.out.println("Process " + tokenHolder + " ENTERS critical section.");
                // Simulate work in critical section
                System.out.println("Process " + tokenHolder + " is WORKING in critical section...");
                System.out.println("Process " + tokenHolder + " EXITS critical section.");
                releaseCriticalSection(tokenHolder);
            } else {
                System.out.println("Process " + tokenHolder + " does NOT need critical section.");
            }

            System.out.print("Pass token? (yes/no): ");
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("yes")) {
                passToken();
            } else {
                continueRunning = false;
            }
        }

        sc.close();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int numProcesses = sc.nextInt();
        sc.nextLine(); // Consume newline

        TokenRingMutualExclusion ring = new TokenRingMutualExclusion(numProcesses);

        boolean setupDone = false;
        while (!setupDone) {
            System.out.print("Enter process IDs requesting critical section (comma-separated, e.g., 0,2): ");
            String input = sc.nextLine();
            if (!input.isEmpty()) {
                String[] processIds = input.split(",");
                for (String pidStr : processIds) {
                    int pid = Integer.parseInt(pidStr.trim());
                    if (pid >= 0 && pid < numProcesses) {
                        ring.requestCriticalSection(pid);
                    }
                }
            }
            setupDone = true;
        }

        ring.runTokenRing();
        sc.close();
    }
}

