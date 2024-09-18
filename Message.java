import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private int[] vectorClock;
    private int targetProcessId;

    public Message(String content, int[] vectorClock, int targetProcessId) {
        this.content = content;
        this.vectorClock = vectorClock;
        this.targetProcessId = targetProcessId;
    }

    public String getContent() {
        return content;
    }

    public int[] getVectorClock() {
        return vectorClock;
    }

    public int getTargetProcessId() {
        return targetProcessId;
    }
}
