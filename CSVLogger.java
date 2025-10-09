import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CSVLogger {
    private FileWriter writer;
    private final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public CSVLogger(String filename) {
        try {
            writer = new FileWriter(filename, true);
            writer.write("timestamp,user,valence,arousal,group_valence,group_arousal\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void log(Map<String, double[]> userStates, double groupVal, double groupAro) {
        try {
            String ts = fmt.format(LocalDateTime.now());
            for (Map.Entry<String, double[]> e : userStates.entrySet()) {
                double[] emo = e.getValue();
                writer.write(String.format("%s,%s,%.3f,%.3f,%.3f,%.3f\n",
                        ts, e.getKey(), emo[0], emo[1], groupVal, groupAro));
            }
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
