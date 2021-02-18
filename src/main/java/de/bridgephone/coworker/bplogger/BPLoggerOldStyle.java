package de.bridgephone.coworker.bplogger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

    /*
     * To change this template, choose Tools | Templates
     * and open the template in the editor.
     */
    /**
     *
     * @author kees
     */
    public class BPLoggerOldStyle {

        private static final String TAG = "    public class BPLoggerOldStyle {\n";
        private static final String LOG_FILE = "BridgePhoneFileLog";
        private static final BPLoggerOldStyle INSTANCE = new BPLoggerOldStyle();
        static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(TAG);
        private static FileHandler fh;
        private static File file;


        private BPLoggerOldStyle() {
        }

        public static BPLoggerOldStyle getInstance() {
            return INSTANCE;
        }



        public void init(String url, Level level) {
            try {
                if (url!=null){
                    url=url+"\\";
                }
                StringBuilder sb = new StringBuilder(url+LOG_FILE);
                sb.append(".txt");
                String fileNameTxt = sb.toString();
                file = new File(fileNameTxt);
                if (file.exists()) {
                    sb = new StringBuilder(url+LOG_FILE);
                    sb.append(".bak");
                    String fileNameBAK = sb.toString();
                    File bakFile = new File(fileNameBAK);
                    if (bakFile.exists()) {
                        boolean b = bakFile.delete();
                        if (!b) {
                            Logger.getLogger("").log(Level.SEVERE, "Logger File could not be deleted");
                        }
                    }
                    boolean b = file.renameTo(bakFile);
                    if (!b) {
                        Logger.getLogger("").log(Level.SEVERE, "Logger File could not be renamed to BAK");
                    }
                    file = new File(fileNameTxt);
                }
                // Add this file fandler to the java.util.logger
                // so that all messages are written to this file
                // using a simple formatter
                fh = new FileHandler(fileNameTxt);
                SimpleFormatter formatterTxt = new SimpleFormatter();
                fh.setFormatter(formatterTxt);
//            //here a filter can be placed to filter out messages coming from the
//            // a particular source
//            This filter can be attached to the handler
//            or to the logger
//            Filter filter= new Filter() {
//
//                @Override
//                public boolean isLoggable(LogRecord record) {
//                    return true;
//                }
//            };
//            fh.setFilter(filter);
                Logger rootLogger=Logger.getLogger("");
                rootLogger.addHandler(fh);
                rootLogger.setLevel(level);
                rootLogger.log(Level.CONFIG, "Start logging");
                // Prevent Restlet from setting up a new Handler by setting the
                // system property
                String setProperty = System.setProperty("java.util.logging.config.file","log.properties");
            } catch (IOException | SecurityException ex) {
                java.util.logging.Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
            }
        }

        public void stop() {
            String sb = "Stopping the logging.";
            java.util.logging.Logger.getLogger(TAG).log(Level.INFO, sb);

        }

        public String getLoggerName(){
            return file.getName();
        }

    }

