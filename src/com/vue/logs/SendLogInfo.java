package com.vue.logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Environment;

public class SendLogInfo {
    private static final String MAIN_DIRECTORY = "VueLogs";
    private static final String FORMATER_STRING = "MMM_dd_yyyy";
    
    public static void sendToNetwork(String tag, String message) {
        // TODO network calls to upload message to server
    }
    
    /**
     * send batch logs to server
     */
    public static void flush() {
        if (LogStore.getInstance().getLogsCount() > 0) {
            ArrayList<String> logsList = LogStore.getInstance().getLogList();
            // TODO: network calls to send all the logs at once.
        }
    }
    
    /**
     * MainDirectory is VueLogs SubDirectory may be ERROR,INFO or WARNINGS. Text
     * file will be saving with current date.
     */
    public static void writeToSdcard(String subDirectory, String message) {
        String path = Environment.getExternalStorageDirectory().toString();
        File dir = new File(path + "/" + MAIN_DIRECTORY + "/");
        if (!dir.isDirectory()) {
            dir.mkdir();
        }
        File subdir = new File(dir.getAbsoluteFile(), subDirectory);
        if (!subdir.isDirectory()) {
            subdir.mkdir();
        }
        String fileName = getFileName();
        File file = new File(subdir, "/" + fileName + ".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(file, true)));
            out.write("\n" + message + "\n");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @return String gives the specifed date formate.
     */
    private static String getFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMATER_STRING);
        Date date = new Date(System.currentTimeMillis());
        String fileName = formatter.format(date);
        return fileName;
    }
}
