import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BSSManager extends Remote {
    void send(Message message) throws RemoteException;
    void releaseToken() throws RemoteException;
    void requestToken(int processId) throws RemoteException;
}
