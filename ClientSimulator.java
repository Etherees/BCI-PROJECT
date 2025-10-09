import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ClientSimulator implements Runnable {
    private final String userId;
    private final Room room;
    private volatile boolean running = true;

    public ClientSimulator(String userId, Room room) {
        this.userId = userId;
        this.room = room;
        room.registerUser(userId);
    }

    public void stop() { running = false; room.removeUser(userId); }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            while (running) {
                double t = (System.currentTimeMillis() - start) / 1000.0;
                double valence = Math.tanh(Math.sin(t * 0.6 + ThreadLocalRandom.current().nextDouble(-0.2, 0.2)));
                double arousal = 0.5 + 0.5 * Math.sin(t * 0.9 + ThreadLocalRandom.current().nextDouble(-0.2, 0.2));
                // Send to room
                double reward = valence > 0.3 ? 1.0 : 0.0;
                room.submitSignal(userId, valence, arousal, reward);
                Thread.sleep(300 + ThreadLocalRandom.current().nextInt(0,200));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
