import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

public class EmotionSynchronizer {
    private static class UserState {
        double valence;
        double arousal;
        long t;
    }

    private final Map<String, UserState> users = new ConcurrentHashMap<>();
    private double lastValence = 0.0;
    private double lastArousal = 0.0;
    private long lastT = System.currentTimeMillis();
    private final double smoothingTauSec = 2.0;

    public void updateUserState(String userId, double valence, double arousal) {
        UserState st = new UserState();
        st.valence = valence;
        st.arousal = arousal;
        st.t = System.currentTimeMillis();
        users.put(userId, st);
    }

    public void removeUser(String userId) {
        users.remove(userId);
    }

    public synchronized GroupState computeGroupState(Map<String, Double> weights) {
        if (users.isEmpty()) return new GroupState(0.0, 0.0, 0);

        double valenceAcc = 0.0;
        double arousalAcc = 0.0;
        double totalW = 0.0;
        int members = 0;
        for (Map.Entry<String, UserState> e : users.entrySet()) {
            String uid = e.getKey();
            UserState st = e.getValue();
            double w = 1.0;
            if (weights != null && weights.containsKey(uid)) w = weights.get(uid);
            valenceAcc += w * st.valence;
            arousalAcc += w * st.arousal;
            totalW += w;
            members++;
        }
        if (totalW <= 0.0) totalW = Math.max(1, members);
        double valence = valenceAcc / totalW;
        double arousal = arousalAcc / totalW;

        long now = System.currentTimeMillis();
        double dtSec = Math.max(0.0, (now - lastT) / 1000.0);
        double alpha = 1.0 - Math.exp(-dtSec / Math.max(1e-6, smoothingTauSec));
        double smoothedValence = (1 - alpha) * lastValence + alpha * valence;
        double smoothedArousal = (1 - alpha) * lastArousal + alpha * arousal;

        lastValence = smoothedValence;
        lastArousal = smoothedArousal;
        lastT = now;

        GroupState gs = new GroupState(smoothedValence, smoothedArousal, members);
        return gs;
    }
}
