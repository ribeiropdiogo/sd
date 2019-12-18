package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Condition;

public class model implements Serializable {
    private Map<String, user> users;
    private Map<Integer, mediaFile> files;
    private uploadLog uploadLog;
    private loadManager ldManager;

    public model() {
        this.users = new HashMap<String, user>();
        this.files = new HashMap<Integer, mediaFile>();
        this.uploadLog = new uploadLog();
        this.ldManager = new loadManager();
    }

    public void addUser(String nameIn, String passwordIn) {
        this.users.put(nameIn, new user(nameIn, passwordIn));
    }

    public boolean login(String nameIn, String passwordIn) {
        return this.users.get(nameIn).checkPassword(passwordIn);
    }

    public void addFile(String tituloIn, String InterpreteIn, String ano, String[] tagsIn) {
        int newFileId = this.files.size() + 1;
        mediaFile newFile = new mediaFile(newFileId, tituloIn, InterpreteIn, ano, tagsIn);
        this.files.put(newFileId, newFile);
        this.uploadLog.addLog(newFile.toString());
    }

    public String getNextFileNString(){
        return Integer.toString(this.files.size()+1);
    }

    public String getFileTitle(int fileId) {
        return this.files.get(fileId).getFileTitle();
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

    public int getLastLogNumber(){
        return this.uploadLog.getNlogs();
    }

    public void sleepIfUpdated(int lastLogUpdated) {
        this.uploadLog.sleepIfUpdated(lastLogUpdated);
    }

    /*public Condition getUpdateCondition(){
        return this.uploadLog.getUpdateCondition();
    }

    public boolean isUpdated(int lastLogUpdated){
        return this.uploadLog.isUpdated(lastLogUpdated);
    }*/

    public String[] newLogs(int lastLogUpdated) {
        System.out.println("Dude had "+lastLogUpdated+" im at "+this.uploadLog.getNlogs());
        int newLogsNumber = this.uploadLog.getNlogs() - lastLogUpdated;
        String[] newLogs = new String[newLogsNumber];

        for (int i = 0; i < newLogsNumber; i++) {
            newLogs[i] = this.uploadLog.getLogN(i + lastLogUpdated);
        }
        return newLogs;
    }

}