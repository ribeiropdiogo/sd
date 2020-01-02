package model;

import exceptions.noSuchUserException;
import exceptions.duplicateUserException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class model {
    private Map<String, user> users;
    private Map<Integer, mediaFile> files;
    private uploadLog uploadLog;
    public loadManager ldManager;
    private ReentrantLock usersLock;
    private ReentrantLock filesLock;

    public model() {
        this.users = new HashMap<String, user>();
        this.files = new HashMap<Integer, mediaFile>();
        this.uploadLog = new uploadLog();
        this.ldManager = new loadManager();
        this.usersLock = new ReentrantLock();
        this.filesLock = new ReentrantLock();
    }

    public void addUser(String nameIn, String passwordIn) throws duplicateUserException {
        this.usersLock.lock();
        if(this.users.get(nameIn)!=null){
            this.usersLock.unlock();
            throw new duplicateUserException("");
        }
        this.users.put(nameIn, new user(nameIn, passwordIn));
        this.usersLock.unlock();
    }

    public boolean login(String nameIn, String passwordIn) throws noSuchUserException{
        this.usersLock.lock();
        user user2Log=this.users.get(nameIn);
        if(user2Log==null){
            this.usersLock.unlock();
            throw new noSuchUserException("");
        }
        boolean result= user2Log.checkPassword(passwordIn);
        this.usersLock.unlock();
        return result;
    }

    public void addFile(String tituloIn, String InterpreteIn, String ano, String[] tagsIn) {
        this.filesLock.lock();
        int newFileId = this.files.size() + 1;
        mediaFile newFile = new mediaFile(newFileId, tituloIn, InterpreteIn, ano, tagsIn);
        this.files.put(newFileId, newFile);
        this.uploadLog.addLog(newFile.toString());
        this.filesLock.unlock();
    }

    public void addDownloadToFile(int fileNumber){
        this.filesLock.lock();
        mediaFile fileDownloaded=this.files.get(fileNumber);
        if(fileDownloaded==null){
            this.filesLock.unlock();
        }
        fileDownloaded.addDownload();
        this.filesLock.unlock();
    }

    
    //Já que não prevemos retirar ficheiros, consultas n precisam ser sincronas
    public String getNextFileNString() {
        return Integer.toString(this.files.size() + 1);
    }

    public String getFileTitle(int fileId) {
        mediaFile file=this.files.get(fileId);
        if(file==null){
            return null;
        }
        return file.getFileTitle();
    }

    public String getFileArtist(int fileId) {
        return this.files.get(fileId).getFileArtist();
    }

    public ArrayList<String> SearchOnTag(String searchTagIn) {
        ArrayList<String> songList = new ArrayList<>();

        Iterator it = this.files.values().iterator();
        while (it.hasNext()) {

            mediaFile fileAt = (mediaFile) it.next();

            if (fileAt.containsTag(searchTagIn)) {
                songList.add(fileAt.toString());
            }
        }
        return songList;
    }

    public int getLastLogNumber() {
        return this.uploadLog.getNlogs();
    }

    public void sleepIfUpdated(int lastLogUpdated) {
        this.uploadLog.sleepIfUpdated(lastLogUpdated);
    }

    /*
     * public Condition getUpdateCondition(){ return
     * this.uploadLog.getUpdateCondition(); }
     * 
     * public boolean isUpdated(int lastLogUpdated){ return
     * this.uploadLog.isUpdated(lastLogUpdated); }
     */

    public String[] newLogs(int lastLogUpdated) {
        System.out.println("Dude had " + lastLogUpdated + " im at " + this.uploadLog.getNlogs());
        int newLogsNumber = this.uploadLog.getNlogs() - lastLogUpdated;
        String[] newLogs = new String[newLogsNumber];

        for (int i = 0; i < newLogsNumber; i++) {
            newLogs[i] = this.uploadLog.getLogN(i + lastLogUpdated);
        }
        return newLogs;
    }

    public int getMAXSIZE(){
         return this.ldManager.MAXSIZE;
    }

}