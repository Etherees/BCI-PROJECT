import java.util.*;

public class EmotionNetwork {
    private Map<String, double[]> userStates = new HashMap<>();
    private Random rand = new Random();

    public EmotionNetwork(int numUsers) {
        for (int i = 1; i <= numUsers; i++) {
            userStates.put("user" + i, new double[]{rand.nextDouble() * 2 - 1, rand.nextDouble()});
        }
    }

    public void update() {
        for (double[] state : userStates.values()) {
            state[0] += rand.nextGaussian() * 0.05;
            state[1] += rand.nextGaussian() * 0.05;
            state[0] = Math.max(-1, Math.min(1, state[0]));
            state[1] = Math.max(0, Math.min(1, state[1]));
        }
    }

    public double[] getGroupState() {
        double v = 0, a = 0;
        for (double[] state : userStates.values()) {
            v += state[0];
            a += state[1];
        }
        int n = userStates.size();
        return new double[]{v / n, a / n};
    }

    public Map<String, double[]> getUserStates() {
        return userStates;
    }
}
