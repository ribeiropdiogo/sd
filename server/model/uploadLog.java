package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class uploadLog {
    private int Nlogs;
    private ArrayList<String> logs;
    private ReentrantLock lock;
    private Condition wakeUpOnUpdate;

    public uploadLog() {
        this.Nlogs = 0;
        this.logs = new ArrayList<String>();
        this.lock = new ReentrantLock();
        this.wakeUpOnUpdate = lock.newCondition();
    }

    public int getNlogs() {
        return this.Nlogs;
    }

    // n ha problema em n ser sincrono já que uploader só atualiza
    // o Nlogs no fim
    public String getLogN(int logNIn) {
        return this.logs.get(logNIn);
    }

    public synchronized void addLog(String newLog) {
        this.logs.add(newLog);
        this.Nlogs++;
        this.lock.lock();
        this.wakeUpOnUpdate.signalAll();
        this.lock.unlock();
    }

    public void sleepIfUpdated(int lastLogUpdated) {
        if (lastLogUpdated == this.Nlogs) {
            try {
                this.lock.lock();
                wakeUpOnUpdate.await();
                System.out.println("Wake me up inside");
                this.lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Update Thread was unexpectedly woken");
            }
        }
    }

}