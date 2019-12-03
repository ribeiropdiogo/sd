package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class model implements Serializable {
    private Map<Integer, user> users;
    private Map<String, mediaFile> files;
    private uploadLog uploadLog;
    private loadManager ldManager;

    public model() {
        this.users = new HashMap<Integer,user>();
        this.files = null;
        this.uploadLog = new uploadLog();
        this.ldManager= new loadManager();
    }

    public int getNLogs(){
        return this.uploadLog.getNlogs();
    }

    public String[] newLogs(int lastLogUpdated){
        int newLogsNumber=this.uploadLog.getNlogs()-lastLogUpdated;
        String [] newLogs= new String[newLogsNumber];
        
        for(int i=0;i<newLogsNumber;i++){
          newLogs[i]=this.uploadLog.getLogN(i+lastLogUpdated);
        }
        return newLogs;
    }


}