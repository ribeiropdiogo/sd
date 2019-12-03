package model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class loadManager{
    
    private int MAXSIZE;
    private int currentDownloads;
    private final int MAXDOWN=10;
    private ReentrantLock lock;
    private Condition canDownload;

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