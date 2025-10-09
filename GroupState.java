import java.util.Map;

public class GroupState {
    private double valence;
    private double arousal;
    private int members;
    private Map<String, Double> weights;

    public GroupState() {}

    public GroupState(double valence, double arousal, int members) {
        this.valence = valence;
        this.arousal = arousal;
        this.members = members;
    }

    public double getValence() { return valence; }
    public void setValence(double valence) { this.valence = valence; }
    public double getArousal() { return arousal; }
    public void setArousal(double arousal) { this.arousal = arousal; }
    public int getMembers() { return members; }
    public void setMembers(int members) { this.members = members; }
    public Map<String, Double> getWeights() { return weights; }
    public void setWeights(Map<String, Double> weights) { this.weights = weights; }

    @Override
    public String toString() {
        return String.format("GroupState[valence=%.3f, arousal=%.3f, members=%d, weights=%s]",
                valence, arousal, members, weights);
    }
}
