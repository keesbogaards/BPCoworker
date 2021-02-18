
package de.bridgephone.coworker;

/**
 *
 * @author Kees
 */

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import de.bridgephone.coworker.xmlconfiguration.XmlHarmonyConfiguration;
import de.bridgephone.coworker.xmlconfiguration.XmlValueRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyCallable implements Callable<String> {

    private final static String TAG = "MyCallable";
    private final static Logger LOG = LogManager.getLogger(TAG);
    private final XmlHarmonyConfiguration xmlConfiguration;
    private long timeNow;
    private final ResourceBundle bundle;

    public MyCallable(XmlHarmonyConfiguration xmlConfiguration) {
        this.xmlConfiguration = xmlConfiguration;
        bundle = ResourceBundle.getBundle("de/bridgephone/bridgephoneharmony/resources/BridgePhoneHarmonyConfigurator");
    }

    @Override
    public String call() throws Exception {
        startScoreProgram(xmlConfiguration);
        return "Klaar";
    }

    private void startScoreProgram(XmlHarmonyConfiguration xmlConfiguration) {

        ProcessBuilder pSp = new ProcessBuilder();
        String scoringProgramPath = xmlConfiguration.getScoringProgramPathFile();
        pSp.command(scoringProgramPath);

        try {
            pSp.start();
            LOG.info("scoring program " + scoringProgramPath + " started");
        } catch (IOException ex) {
//            Priority.class. ;//priority= new Priority();
            LOG.fatal("scoring program " + scoringProgramPath + " started with error", ex);
        }

        String bwsFileName = xmlConfiguration.getBwsFilePath();
        LocalDateTime now = LocalDateTime.now();
        timeNow = new Date().getTime();
        String timeNowString = convertTime(timeNow);
        LOG.info("time is " + timeNowString);

        while (!testBwsDir(bwsFileName)) {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//                timeNow = new Date().getTime();
//                timeNowString = convertTime(timeNow);
//                LOG.info("time is " + timeNowString);
//            } catch (InterruptedException ex) {
//                LOG.error("Time out interrupted");
//           }
        }
        timeNow = new Date().getTime();
        timeNowString = convertTime(timeNow);
        LOG.info("BridgePhone started at: " + timeNowString);

    }

    /**
     * *
     * Wait until the BWS file has been created
     *
     * @param bwsFileName
     * @return
     */
    private boolean testBwsDir(String bwsFileName) {

        boolean result;

        File[] listOfFiles;
        if (bwsFileName.endsWith("bws")) {
            int i = bwsFileName.lastIndexOf("\\");
            if (i != -1) {
                bwsFileName = bwsFileName.substring(0, i);

            }
        }
        File dir = new File(bwsFileName);
        listOfFiles = dir.listFiles();
        if (listOfFiles == null) {
            return false;
        }
        long time;
        long timeCoWorkerStart;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                time = listOfFile.lastModified();
                String time1 = convertTime(time);

                timeCoWorkerStart = MyCallable.this.timeNow;
                String time2 = convertTime(timeCoWorkerStart);

                long timeAfter = time - MyCallable.this.timeNow;
                if (timeAfter > 0) {
                    for (File fileForPrint : listOfFiles) {
                        LOG.info(fileForPrint.getAbsolutePath());
                    }
                    String bwsFilePath = listOfFile.getAbsolutePath().toLowerCase();
                    if ((!bwsFilePath.endsWith(".ldb"))
                            && (!bwsFilePath.endsWith("temp.bws"))) {
                        String timeString = " Modified: " + time1 + " Start: time2";
                        String s = bwsFilePath + timeString;
                        LOG.info(s);
//**  temp
                        String PCBridgePhonePath = xmlConfiguration.determineFilePath(XmlValueRecord.PCBRIDGEPHONEPROGRAM);
//        pSp.command("C:\\Users\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe"); 
//        https://docs.oracle.com/javase/6/docs/api/java/lang/ProcessBuilder.html
//        https://stackoverflow.com/questions/5604698/java-programming-call-an-exe-from-java-and-passing-parameters
//                    pBp.command(scoringProgramPath);
//                    ArrayList pBpList = new ArrayList<>();
// /f:[C:\Users\Kees\AppData\Local\NBB-Rekenprogramma\Bwsdata\z5.bws]/r/s

                        String arg = "/f:["
                                + bwsFilePath
                                + "]/r/s/";
                        ProcessBuilder pBp = new ProcessBuilder(PCBridgePhonePath, arg);

                        try {
                            pBp.start();
                            LOG.info(PCBridgePhonePath + " " + arg);
                            result = true;
                            return result;
                        } catch (IOException ex) {
                            LOG.fatal("PCBridgePhone Problem ", ex);
//                            return false;
                        }
                    }
                    String s = "File " + listOfFile.getName();
                    LOG.info(s);
                } else if (listOfFile.isDirectory()) {
                    String s = "Directory " + listOfFile.getName();
                    LOG.info(s);
                }
            }
        }
        return false;
    }

    public String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("YYYY MM DD HH:MM:SS");
        return format.format(date);
    }

}
