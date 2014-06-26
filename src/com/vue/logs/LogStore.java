package com.vue.logs;

import java.util.ArrayList;

/**
 * 
 * object to save all the logs for batch upload.
 * 
 */
public class LogStore implements Cloneable {
    private static LogStore logStore;
    private ArrayList<String> logsList = new ArrayList<String>();
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    
    public static LogStore getInstance() {
        if (logStore == null) {
            logStore = new LogStore();
        }
        return logStore;
    }
    
    public void addLogToList(String logMessage) {
        logsList.add(logMessage);
    }
    
    public ArrayList<String> getLogList() {
        return logsList;
    }
    
    public int getLogsCount() {
        if (logsList != null) {
            return logsList.size();
        } else {
            logsList = new ArrayList<String>();
            return logsList.size();
        }
    }
}
