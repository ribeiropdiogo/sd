import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.concurrent.locks.Condition;

import model.model;

public class notificationWorker implements Runnable {

    private model serverInfo;
    private int lastLogUpdated;
    private PrintWriter socketWriter;

    public notificationWorker(model serverInfoIn, PrintWriter sockerwWriterIn) {
        this.serverInfo = serverInfoIn;
        this.lastLogUpdated = serverInfoIn.getLastLogNumber();
        this.socketWriter = sockerwWriterIn;

    }

    public void run() {

        while (true) {
            this.serverInfo.sleepIfUpdated(this.lastLogUpdated);
            String newLogs[] = this.serverInfo.newLogs(lastLogUpdated);
            for (String log : newLogs) {
                socketWriter.println("NEW "+log);
            }
            this.lastLogUpdated += newLogs.length;
        }

    }

}