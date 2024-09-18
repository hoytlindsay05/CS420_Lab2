import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;

public class BSSManagerImpl extends UnicastRemoteObject implements BSSManager {
    private static final long serialVersionUID = 1L;
    private int currentTokenHolder; // Process ID that currently holds the token
    private Queue<Message> waitingQueue; // Messages waiting for delivery
    private Map<Integer, Process> processMap; // Mapping of process IDs to Process objects
    private int[] vectorClock;

    protected BSSManagerImpl(int numProcesses) throws RemoteException {
        super();
        this.currentTokenHolder = 0; // Initial token holder (e.g., process 0)
        this.waitingQueue = new LinkedList<>();
        this.processMap = new HashMap<>();
        this.vectorClock = new int[numProcesses];
    }

    @Override
    public synchronized void send(Message message) throws RemoteException {
        // Update vector clock
        for (int i = 0; i < vectorClock.length; i++) {
            vectorClock[i] = Math.max(vectorClock[i], message.getVectorClock()[i]);
        }

        if (currentTokenHolder == message.getTargetProcessId()) {
            // Deliver messages in causal order if this process holds the token
            deliverMessages();
        } else {
            // Add message to the waiting queue
            waitingQueue.add(message);
        }
    }

    @Override
    public synchronized void releaseToken() throws RemoteException {
        // Pass the token to the next process (round-robin for simplicity)
        currentTokenHolder = (currentTokenHolder + 1) % processMap.size();
        System.out.println("Token passed to process " + currentTokenHolder);

        // Deliver messages in causal order if the current process is now the token holder
        deliverMessages();
    }

    @Override
    public synchronized void requestToken(int processId) throws RemoteException {
        // Check if the requesting process is the current token holder
        if (currentTokenHolder == processId) {
            // The process already holds the token, so grant it immediately
            Process process = processMap.get(processId);
            if (process != null) {
                process.receiveToken();
            }
        } else {
            // Add process to request queue or handle request as needed
            System.out.println("Process " + processId + " requested the token but doesn't hold it.");
        }
    }

    private synchronized void deliverMessages() throws RemoteException {
        // Deliver messages from the waiting queue in causal order
        while (!waitingQueue.isEmpty()) {
            Message message = waitingQueue.poll();
            int targetProcessId = message.getTargetProcessId();
            Process targetProcess = processMap.get(targetProcessId);
            if (targetProcess != null) {
                targetProcess.deliver(message);
            }
        }
    }

    public synchronized void addProcess(int processId, Process process) {
        processMap.put(processId, process);
    }
}
