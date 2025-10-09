import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

public class WeightOptimizer {
    private final Map<String, Double> rewardsEma = new ConcurrentHashMap<>();
    private final double decay = 0.9;
    private final double minWeight = 0.05;

    public void ensureUser(String userId) {
        rewardsEma.putIfAbsent(userId, 0.0);
    }

    public void removeUser(String userId) {
        rewardsEma.remove(userId);
    }

    public void update(String userId, double reward) {
        rewardsEma.putIfAbsent(userId, 0.0);
        double prev = rewardsEma.get(userId);
        double next = decay * prev + (1 - decay) * reward;
        rewardsEma.put(userId, next);
    }

    public synchronized Map<String, Double> getWeights() {
        Map<String, Double> scores = new HashMap<>();
        for (Map.Entry<String, Double> e : rewardsEma.entrySet()) {
            String uid = e.getKey();
            double r = e.getValue();
            double s = Math.log1p(Math.exp(r));
            scores.put(uid, s);
        }
        double total = scores.values().stream().mapToDouble(Double::doubleValue).sum();
        Map<String, Double> out = new HashMap<>();
        if (total <= 0) {
            int n = Math.max(1, scores.size());
            for (String uid : scores.keySet()) out.put(uid, 1.0 / n);
            return out;
        }
        Map<String, Double> normalized = new HashMap<>();
        for (Map.Entry<String, Double> e : scores.entrySet()) {
            normalized.put(e.getKey(), Math.max(minWeight, e.getValue() / total));
        }
        double total2 = normalized.values().stream().mapToDouble(Double::doubleValue).sum();
        for (Map.Entry<String, Double> e : normalized.entrySet()) {
            out.put(e.getKey(), e.getValue() / total2);
        }
        return out;
    }
}
