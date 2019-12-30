package model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class loadManager{
    
    public int MAXSIZE;
    private int ticketsOut;
    private int downloadsDone;
    private final int MAXDOWN=2;
    private ReentrantLock lock;
    private Condition canDownload;

public loadManager(){
    this.MAXSIZE=1000000;
    this.ticketsOut=0;
    this.downloadsDone=0;
    this.lock=new ReentrantLock();
    this.canDownload=this.lock.newCondition();
}    

public int getTicket(){
    this.lock.lock();
    this.ticketsOut++;
    this.lock.unlock();
    return this.ticketsOut;
}

    public void waitDownload(int ticket){
        this.lock.lock();
       while(ticket>(this.downloadsDone+this.MAXDOWN)){
        try{
          this.canDownload.await();
        }catch(Exception e){
            e.printStackTrace();
        }
       }
       this.lock.unlock();
    }

    public void freeDownload(){
        this.lock.lock();
        this.downloadsDone++;
        this.canDownload.signalAll();
        this.lock.unlock();
    }



}