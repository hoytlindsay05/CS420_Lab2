import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Process extends Remote {
    void send(Message message) throws RemoteException;
    void deliver(Message message) throws RemoteException;
    void getToken() throws RemoteException;
    void receiveToken() throws RemoteException; // Added method
}
