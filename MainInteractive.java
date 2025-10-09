import java.util.*;

public class MainInteractive {
    public static void main(String[] args) throws Exception {
        int numClients = args.length > 0 ? Integer.parseInt(args[0]) : 5;
        int duration = args.length > 1 ? Integer.parseInt(args[1]) : 0;

        EmotionNetwork network = new EmotionNetwork(numClients);
        CSVLogger logger = new CSVLogger("emotion_log.csv");

        System.out.println("Starting Brain-to-Brain Simulation with " + numClients + " clients...");
        System.out.println("Type 'quit' to exit.");

        Scanner sc = new Scanner(System.in);
        long start = System.currentTimeMillis();
        boolean running = true;

        while (running) {
            Thread.sleep(500);
            network.update();

            double[] group = network.getGroupState();
            System.out.printf("Group State -> Valence: %.3f | Arousal: %.3f | Members: %d\n",
                    group[0], group[1], network.getUserStates().size());

            logger.log(network.getUserStates(), group[0], group[1]);

            if (duration > 0 && (System.currentTimeMillis() - start) / 1000 > duration) break;
            if (System.in.available() > 0) {
                String cmd = sc.nextLine().trim();
                if (cmd.equalsIgnoreCase("quit")) running = false;
            }
        }

        logger.close();
        System.out.println("Simulation ended. Data saved to emotion_log.csv.");
    }
}
