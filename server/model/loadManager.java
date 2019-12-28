package model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class loadManager{
    
    public int MAXSIZE;
    public int currentDownloads;
    public final int MAXDOWN=10;
    public ReentrantLock lock;
    public Condition canDownload;

public loadManager(){
    this.MAXSIZE=1000000;
    this.currentDownloads=0;
    this.lock=new ReentrantLock();
    this.canDownload=this.lock.newCondition();
}    

//falta o Fifo
    public synchronized void downloadQueue(){
       while(this.currentDownloads>=this.MAXDOWN){
        try{
          this.canDownload.await();
        }catch(InterruptedException e){
            
        }
       }
    }

    public synchronized void freeDownload(){
        this.currentDownloads--;
        this.canDownload.signalAll();
    }



}