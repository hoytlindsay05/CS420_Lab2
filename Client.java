import java.rmi.Naming;

public class Client {
    public static void main(String[] args) {
        try {
            // Lookup the process objects on port 1100
            Process process0 = (Process) Naming.lookup("//localhost:1100/Process0");
            Process process1 = (Process) Naming.lookup("//localhost:1100/Process1");
            Process process2 = (Process) Naming.lookup("//localhost:1100/Process2");

            // Create messages
            int[] vectorClock = {0, 0, 0}; // Initial vector clock for simplicity
            Message message1 = new Message("Message 1 from Process 0", vectorClock, 1);
            Message message2 = new Message("Message 2 from Process 1", vectorClock, 2);
            Message message3 = new Message("Message 3 from Process 2", vectorClock, 0);

            // Send messages
            process0.send(message1);
            process1.send(message2);
            process2.send(message3);

            // Request the token
            process0.getToken();
            process1.getToken();
            process2.getToken();

            // Wait to see the output
            Thread.sleep(10000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
