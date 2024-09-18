import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;

public class ProcessImpl extends UnicastRemoteObject implements Process {
    private static final long serialVersionUID = 1L;
    private int processId;
    private int[] vectorClock;
    private Queue<Message> messageQueue;
    private BSSManager bssManager;

    protected ProcessImpl(int processId, BSSManager bssManager) throws RemoteException {
        super();
        this.processId = processId;
        this.vectorClock = new int[3]; // Adjust size based on the number of processes
        this.messageQueue = new LinkedList<>();
        this.bssManager = bssManager;
    }

    @Override
    public synchronized void send(Message message) throws RemoteException {
        // Increment local vector clock
        vectorClock[processId]++;
        
        // Send message to BSSManager
        bssManager.send(new Message(message.getContent(), vectorClock.clone(), processId));
    }

    @Override
    public synchronized void deliver(Message message) throws RemoteException {
        // Deliver the message to the application layer
        System.out.println("Process " + processId + " delivered: " + message.getContent());
    }

    @Override
    public synchronized void getToken() throws RemoteException {
        // Request the token from the BSSManager
        System.out.println("Process " + processId + " requesting the token.");
        bssManager.requestToken(processId);
    }

    @Override
    public synchronized void receiveToken() throws RemoteException {
        System.out.println("Process " + processId + " received the token.");
        // Deliver messages if the token is received
        deliverMessages();
    }

    private synchronized void deliverMessages() throws RemoteException {
        // Deliver messages from the queue
        while (!messageQueue.isEmpty()) {
            Message message = messageQueue.poll();
            deliver(message);
        }
    }
}
