package com.vue.logs;

public class Main {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // get the flag values from the server.
        new Main().pupulateLogFlags();
        
        String message = "AddImage has failed";
        boolean emergencyFlag = false;
        Logging.e("VueCardsActivity", message, emergencyFlag);
        Logging.i(LogTags.LAUNCH_ACTIVITY, message, emergencyFlag);
    }
    
    private void pupulateLogFlags() {
        // TODO: call network api to poplate the logging flags.
        // LoggingConstants.sTurnOfLogsPrint =
        // LoggingConstants.sTurnOfSdcardLogs =
        // LoggingConstants.sTurnOfNetworkLogs =
    }
    
}
