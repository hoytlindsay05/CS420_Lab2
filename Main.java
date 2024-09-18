import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a new registry on port 1100
            Registry registry = LocateRegistry.createRegistry(1100);
            System.out.println("RMI Registry created on port 1100.");

            // Create the BSSManager instance
            BSSManagerImpl bssManager = new BSSManagerImpl(3); // Example with 3 processes
            Naming.rebind("//localhost:1100/BSSManager", bssManager);
            
            // Create and bind Process instances
            Process process0 = new ProcessImpl(0, bssManager);
            Process process1 = new ProcessImpl(1, bssManager);
            Process process2 = new ProcessImpl(2, bssManager);

            bssManager.addProcess(0, process0);
            bssManager.addProcess(1, process1);
            bssManager.addProcess(2, process2);

            Naming.rebind("//localhost:1100/Process0", process0);
            Naming.rebind("//localhost:1100/Process1", process1);
            Naming.rebind("//localhost:1100/Process2", process2);
            
            System.out.println("BSS Manager and Processes bound to RMI registry on port 1100.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
