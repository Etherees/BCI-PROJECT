import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        int numClients = 5;
        if (args.length >= 1) {
            try { numClients = Integer.parseInt(args[0]); } catch (Exception e) {}
        }
        System.out.println("Starting Brain-to-Brain Java Console Simulation\n" +
                           "Clients: " + numClients);

        Room room = new Room("room1");
        ExecutorService exec = Executors.newCachedThreadPool();
        List<ClientSimulator> sims = new ArrayList<>();
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < numClients; ++i) {
            String uid = "user" + (i+1);
            ClientSimulator sim = new ClientSimulator(uid, room);
            sims.add(sim);
            futures.add(exec.submit(sim));
        }

        // Printer loop: print group state every 500ms
        ScheduledExecutorService printer = Executors.newSingleThreadScheduledExecutor();
        printer.scheduleAtFixedRate(() -> {
            GroupState gs = room.computeGroupState();
            System.out.println(java.time.LocalTime.now() + " - " + gs.toString());
        }, 0, 500, TimeUnit.MILLISECONDS);

        // Run for 30 seconds then shut down
        Thread.sleep(30000);
        System.out.println("Shutting down...");
        for (ClientSimulator s : sims) s.stop();
        exec.shutdownNow();
        printer.shutdownNow();
        System.out.println("Done."); 
    }
}
