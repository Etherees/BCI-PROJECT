import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Room {
    private final String roomId;
    private final EmotionSynchronizer syncer = new EmotionSynchronizer();
    private final WeightOptimizer optimizer = new WeightOptimizer();

    public Room(String roomId) {
        this.roomId = roomId;
    }

    public void registerUser(String userId) {
        optimizer.ensureUser(userId);
    }

    public void removeUser(String userId) {
        optimizer.removeUser(userId);
        syncer.removeUser(userId);
    }

    public synchronized void submitSignal(String userId, double valence, double arousal, double reward) {
        syncer.updateUserState(userId, valence, arousal);
        optimizer.update(userId, reward);
    }

    public GroupState computeGroupState() {
        Map<String, Double> weights = optimizer.getWeights();
        GroupState gs = syncer.computeGroupState(weights);
        gs.setWeights(weights);
        return gs;
    }

    public String getRoomId() { return roomId; }
}
