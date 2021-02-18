/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bridgephone.coworker.xmlconfiguration;


import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.bridgephone.coworker.bppcexceptions.BridgePhoneException;



/**
 * This class handles the bridgephone.xml actions
 *
 * @author Kees
 */
public class XmlHarmonyConfiguration {

    private final boolean test;
    private Locale currentLocale=Locale.ENGLISH;
    private static final String TAG = "XmlConfiguration";
    private final java.util.logging.Logger LOG= Logger.getLogger(TAG);

    private final XmlValueRecord xmlRecord;
    private final XmlFileHandling xmlFileHandling;

    public static final String LANGUAGE_TAG = "language";

    public static final String LANGUAGE_EN = "EN";
    public static final String LANGUAGE_NL = "NL";
    public static final String LANGUAGE_DE = "DE";
    public static final String LANGUAGE_F = "F";
    public static final String LANGUAGE_SLK = "SLK";
    
    private  final String [] languages = {
       LANGUAGE_EN , LANGUAGE_F ,LANGUAGE_DE,LANGUAGE_NL, LANGUAGE_SLK};

    public static final String SCORE_PROGRAMM_PATH_TAG = "score_programm_path";
    public static final String BWS_PATH_TAG = "bws_directory";
    public static final String BRIDGEPHONE_PATH_TAG = "bridgephone_path";
    private ResourceBundle bundle;

    /**
     * *
     * Just used for testing purposes
     *
     * @param test the value of test
     */
    public XmlHarmonyConfiguration(boolean test) {
        xmlFileHandling = new XmlFileHandling();
        xmlRecord = new XmlValueRecord();
        this.test=test;

    }

    public String getStatus() {
        String s = xmlRecord.getErrorMessage();
        if (s == null) {
            s = "";
        }
        return s;
    }

    public String defineWorkdir() {
        return xmlFileHandling.getWorkDir(test);
    }

    /***
     * initialize xmlRecord with the work directory
     * @param workDirUrl established work directory appdata/local/bridgephoneharmony
     */
    public boolean initialize(String workDirUrl) {
        return xmlFileHandling.preparXmlFile(xmlRecord, workDirUrl);
    }

    /**
     * @return the xmlRecord
     */
    public XmlValueRecord getXmlRecord() {
        return xmlRecord;
    }

    /**
     * *
     * Read all the values from the xml file Test on feasibility. If not feasile
     * , set error messages and return
     */
    public void readAllXmlValues() {
//        Read scoring program file path
        try {

            XmlDocHandling xmlDocHandling = new XmlDocHandling(xmlRecord.getXmlFilePath());

            // read language
            String language = xmlDocHandling.readFilePath(LANGUAGE_TAG);
            xmlRecord.setLanguage(language);
            currentLocale=readLocale();
            bundle = ResourceBundle.getBundle("BridgePhoneHarmonyConfigurator", currentLocale);

            String scoreProgrammFilePath = xmlDocHandling.readFilePath(SCORE_PROGRAMM_PATH_TAG);
            if (scoreProgrammFilePath.isEmpty()) {
                String s = bundle.getString("score_program_path_is_not_set");
                xmlRecord.setErrorMessage(s);
                LOG.log(Level.SEVERE,TAG+s);
//                LOG.fatal(TAG+s);
            }
            setScoringProgramPathFile(new File(scoreProgrammFilePath));
//            xmlRecord.setScoringProgramFilePath(scoreProgrammFilePath);

//        Read bws file path
            String bwsFilePath = xmlDocHandling.readFilePath(BWS_PATH_TAG);
            if (scoreProgrammFilePath.isEmpty()) {
                String s = TAG+ bundle.getString("bws_file_path_is_not_set");
                xmlRecord.setErrorMessage(s);
                LOG.log(Level.SEVERE,TAG+s);
//                LOG.error(s);
            }
            xmlRecord.setBwsFilePath(bwsFilePath);

//      Read BridgePhone Path
            String bridgePhoneFilePath = xmlDocHandling.readFilePath(BRIDGEPHONE_PATH_TAG);
            if (bridgePhoneFilePath.isEmpty()) {
                String s = bundle.getString("bridgePhone_fil_path_is_not_set");
                xmlRecord.setErrorMessage(s);
                LOG.log(Level.WARNING,TAG+s);

            }
            xmlRecord.setPcbridgephoneFilePath(bridgePhoneFilePath);


        } catch (Throwable t) {
            String s = "Exception";
            xmlRecord.setErrorMessage(s);
            LOG.log(Level.SEVERE,TAG+s+t.getMessage());

        }
    }

    /**
     * *
     * SCORE_PROGRAMM_PATH_TAG ; BWS_PATH_TAG BRIDGEPHONE_PATH_TAG;
     * BM_PRO_PATH_TAG
     *
     */
    public boolean updateXmlFile(/*XmlValueRecord xmlRecord*/) {

        XmlDocHandling xmlDocHandling = new XmlDocHandling(xmlRecord.getXmlFilePath());
        try {
            xmlDocHandling.changeElement(getScoringProgramPathFile() , XmlHarmonyConfiguration.SCORE_PROGRAMM_PATH_TAG);

        xmlDocHandling.changeElement(xmlRecord.getPcbridgephoneFilePath(), XmlHarmonyConfiguration.BRIDGEPHONE_PATH_TAG);
        xmlDocHandling.changeElement(xmlRecord.getBwsFilePath(), XmlHarmonyConfiguration.BWS_PATH_TAG);
        } catch (BridgePhoneException e) {
            String s="could not update xml record";
            LOG.log(Level.WARNING,TAG+s);
            return false;
        }
        LOG.info(TAG+"XML file updated");
        return true;

    }

    /**
     * **
     * Check entries in XmlValueRecord on viability
     *
     * @return false in case of at least one error
     */
    public boolean checkXmlRecordsOnViability() {
        try {
            String filePath;// = "WTF WTF";
            boolean b1 = true;
            boolean b2 = true;
            for (int i = 0; i < 3; i++) {
                switch (i) {
                    case XmlValueRecord.BWSDIR: {
                        filePath = xmlRecord.getBwsFilePath();
                        b1 = b1 & checkOnExistance(filePath, XmlValueRecord.BWSDIR);
                        b2 = b2 & checkOnBws(filePath);
                        break;
                    }
                    case XmlValueRecord.PCBRIDGEPHONEPROGRAM: {
                        filePath = xmlRecord.getPcbridgephoneFilePath();
                        b1 = b1 & checkOnExistance(filePath, XmlValueRecord.PCBRIDGEPHONEPROGRAM);
                        b2 = b2 & checkOnExecutability(filePath);
                        if (!b2) {
                            filePath = WindowsReqistry.readRegistry("HKEY_LOCAL_MACHINE\\SOFTWARE\\WOW6432Node\\BridgePhone", "install_Dir");
                            b1 = b1 & checkOnExistance(filePath, XmlValueRecord.PCBRIDGEPHONEPROGRAM);
//                            b2 = b2 & checkOnExecutability(filePath);
                        }
                        break;
                    }
                    case XmlValueRecord.SCORINGPROGRAM: {
                        filePath = getScoringProgramPathFile() ;
                        b1 = checkOnExistance(filePath, XmlValueRecord.SCORINGPROGRAM);
                        b2 = checkOnExecutability(filePath);
                        break;
                    }
                    default: {
                        b1 = false;
                        break;
                    }

                }
            }
            StringBuilder sb= new StringBuilder();
            for (int i=0;i<3;i++){
                CheckResult cr=xmlRecord.getCr().get(1);
                if (cr.isResult()){
                    sb.append (" ok ");
                    sb.append(cr.getFile().getAbsolutePath());
                }else{
                    sb.append( "error ");
                    sb.append(cr.getErrorMessage());
                }
            }
            LOG.log(Level.INFO,TAG+sb.toString());
            return b1 && b2;
        } catch (Throwable t) {
            String s = " checkXmlRecordsOnViability ";
            LOG.log(Level.SEVERE,TAG+s+t.getMessage());
//            LOG.error(s, t);
            return false;
        }
    }

//    public CheckResult checkPCBridgePhoneFilePath() {
//        try {
//            int index = XmlValueRecord.PCBRIDGEPHONEPROGRAM;
//            CheckResult cr = xmlRecord.getCr().get(index);
//            String pcBridgePhoneFilePath = determineFilePath(index);
//            return evaluatePCBridgePhonePath(pcBridgePhoneFilePath, cr);
//        } catch (Throwable t) {
//            String s = " checkPCBridgePhoneFilePath ";
//            LOG.log(Level.SEVERE,TAG+s+ t.getMessage());
////            LOG.fatal(s, t);
//            CheckResult cr1 = new CheckResult();
//            String formatted = MessageFormat.format("Exception in {0}", s);
//            cr1.setErrorMessage(formatted);
//            cr1.setResult(false);
//            return cr1;
//        }
//    }

//    private CheckResult evaluatePCBridgePhonePath(String pcBridgePhoneFilePath, CheckResult cr) {
//        try {
//            if (!checkOnExistance(pcBridgePhoneFilePath, 0)) {
//                cr.setErrorMessage(bundle.getString("no_existant_pcbridgephone_path_set"));
//                cr.setResult(false);
//            } else if (!checkOnExecutability(pcBridgePhoneFilePath)) {
//                cr.setErrorMessage(bundle.getString("no_executable_pcbridgephone_path_set"));
//                cr.setResult(false);
//            } else {
//                cr.setResult(true);
//                File f = new File(pcBridgePhoneFilePath);
//                cr.setFile(f);
//            }
//            return cr;
//        } catch (Throwable t) {
//            String s = " evaluatePCBridgePhonePath";
//            LOG.log(Level.SEVERE,TAG+s+" "+t.getMessage());
////            LOG.fatal(s, t);
//            CheckResult cr1 = new CheckResult();
//            cr1.setErrorMessage(MessageFormat.format(bundle.getString("exception_in_0"), s));
//            cr1.setResult(false);
//            return cr1;
//        }
//    }

//    public CheckResult checkScoringProgramFilePath() {
//        try {
//            int index = XmlValueRecord.SCORINGPROGRAM;
//            CheckResult cr = xmlRecord.getCr().get(index);
//            String scoringProgramFilePath = determineFilePath(index);
//            if (!checkOnExistance(scoringProgramFilePath, 0)) {
//                cr.setErrorMessage(bundle.getString("no_existant_scoring_program_path_set"));
//                cr.setResult(false);
//            } else if (!checkOnExecutability(scoringProgramFilePath)) {
//                cr.setErrorMessage(bundle.getString("no_executable_scoring_program_path_set"));
//                cr.setResult(false);
//            } else {
//                cr.setResult(true);
//                File f = new File(scoringProgramFilePath);
//                cr.setFile(f);
//            }
//            return cr;
//        } catch (Throwable t) {
//            String s = "checkScoringProgramFilePath";
////            LOG.fatal(s, t);
//            LOG.log(Level.WARNING,TAG +" " + s+" "+t.getMessage());
//            CheckResult cr1 = new CheckResult();
//            cr1.setResult(false);
//            cr1.setErrorMessage("Exception in " + s);
//            return cr1;
//        }
//    }

//    public CheckResult checkBWSFilePath() {
//        try {
//            int index = XmlValueRecord.BWSDIR;
//            CheckResult cr = xmlRecord.getCr().get(index);
//            String bwsFilePath = determineFilePath(index);
//            if (!checkOnExistance(bwsFilePath, 0)) {
//                cr.setErrorMessage(bundle.getString("no_existant_bws_file_path_set"));
//                cr.setResult(false);
//            } else if (!checkOnBws(bwsFilePath)) {
//                cr.setErrorMessage(bundle.getString("no_bws_file_detected"));
//                cr.setResult(false);
//            } else {
//                cr.setResult(true);
//                File f = new File(bwsFilePath);
//                cr.setFile(f);
//            }
//            return cr;
//        } catch (Throwable t) {
//            String s = "checkBWSFilePath";
//            LOG.log(Level.SEVERE,TAG+s+" "+t.getMessage());
////            LOG.fatal(s, t);
//            CheckResult cr1 = new CheckResult();
//            cr1.setResult(false);
//            cr1.setErrorMessage(MessageFormat.format(bundle.getString("exception_in_0"), s));
//            return cr1;
//        }
//
//    }

    public String getScoringProgramPathFile() {
        return xmlRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM).getFile().getAbsolutePath();
//        return xmlRecord.getScoringProgramFilePath();
    }

    public void setScoringProgramPathFile(File f) {
        xmlRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM).setFile(f);
//        xmlRecord.setScoringProgramFilePath(f.getAbsolutePath());
    }

//    public String getPcbridgephoneFilePath() {
//        return xmlRecord.getPcbridgephoneFilePath();
//    }

//    public void setPCBridgePhonePathFile(File f) {
//        xmlRecord.setPcbridgephoneFilePath(f.getAbsolutePath());
//    }

    public String getBwsFilePath() {
        return xmlRecord.getBwsFilePath();
    }

    public void setBwsPathFile(File f) {
        xmlRecord.setBwsFilePath(f.getAbsolutePath());
    }

    /**
     * *
     *
     * @param i =SCORINGPROGRAM, BWSDIR or PCBRIDGEPHONEPROGRAM
     * @return file path
     */
    public String determineFilePath(int i) {
        switch (i) {
            case XmlValueRecord.SCORINGPROGRAM:
                return getScoringProgramPathFile() ;
//                return xmlRecord.getScoringProgramFilePath();
            case XmlValueRecord.BWSDIR:
                return xmlRecord.getBwsFilePath();
            case XmlValueRecord.PCBRIDGEPHONEPROGRAM:
                return xmlRecord.getPcbridgephoneFilePath();
            default:
                return "WTF";

        }
    }

    /**
     *  Check the existence of te file
     * Update the xml record
     * @param filePath to exe or bws file
     * @param textFieldId 0..2 scorecalcuation program, bridgephone.exe , bws directory
     * @return true if exists
     */
    public boolean checkOnExistance(String filePath, int textFieldId) {
        CheckResult cr = xmlRecord.getCr().get(textFieldId);
//        cr.setResult(CheckResult.NOK);
        try {
            if (filePath == null) {
                String s = MessageFormat.format(bundle.getString("file_not_defined"), filePath);
                xmlRecord.setErrorMessage(s);
                cr.setErrorMessage(s);
                cr.setFile(null);
                xmlRecord.setCr(textFieldId, cr);
                LOG.log(Level.WARNING,TAG +" TextFileds " + textFieldId + " " + s);
//                LOG.error("TextFileds " + textFieldId + " " + s);
                return false;
            }
            File f = new File(filePath);
            if (!f.exists()) {
                String s = MessageFormat.format(bundle.getString("file_0_does_not_exist"), filePath);
                xmlRecord.setErrorMessage(s);
                cr.setErrorMessage(s);
                cr.setFile(null);
                xmlRecord.setCr(textFieldId, cr);
                LOG.log(Level.WARNING,TAG + s);
//                LOG.error(s);
                return false;
            }
            cr.setFile(f);
            cr.setResult(CheckResult.OK);
            xmlRecord.setCr(textFieldId, cr);
            return true;
        } catch (Throwable t) {
            String s = MessageFormat.format(bundle.getString("file_0_does_not_exist"), new Object[]{filePath});
            xmlRecord.setErrorMessage(s);
            cr.setErrorMessage(s);
            cr.setFile(null);
            xmlRecord.setCr(textFieldId, cr);
//            LOG.error(s);
            LOG.log(Level.WARNING,TAG +" " + s+" "+t.getMessage());
//            LOG.error(s, t);
            return false;
        }
    }

    /**
     * Check whether file is executable
     * Presume file exists
     *
     * @param filePath to the file to be checked on executability
     * @return true if executable
     */
    public boolean checkOnExecutability(String filePath) {
        File f=new File(filePath);
        if (!f.exists()){
            //programming error checkOnExecutability should be called after  checkOnExistance
            String s="WTF file "+filePath+" should exists";
            LOG.log(Level.SEVERE,TAG +" " + s);
            return false;
        }

        if (!filePath.trim().toLowerCase().endsWith(".exe")) {
            String s = MessageFormat.format(bundle.getString("file_0_is_not_executable"), filePath);
            xmlRecord.setErrorMessage(s);
            LOG.log(Level.WARNING,TAG +" " + s);
//            LOG.fatal(s);
            return false;
        }

        return true;
    }

    public boolean checkOnBws(String filePath) {

        if (!filePath.trim().toLowerCase().endsWith(".bws")) {
            String s = bundle.getString("file_0_does_not_end_with_bws");
            s = MessageFormat.format(s, filePath);
            xmlRecord.setErrorMessage(s);
            LOG.log(Level.WARNING,TAG +" " + s);
//            LOG.fatal(s);
            return false;
        }

        return true;
    }

//    public String getXmlDirUrl() {
//        return xmlRecord.getXmlDirUrl();
//    }

    /**
     * *
     *  read and set the current locale
     * @return the current locale
     */
    private  Locale readLocale() {
        String language = xmlRecord.getLanguage().toUpperCase();
        switch (language) {
            case XmlHarmonyConfiguration.LANGUAGE_EN:
                currentLocale = Locale.ENGLISH;
                break;
            case XmlHarmonyConfiguration.LANGUAGE_DE:
                currentLocale = Locale.GERMAN;
                break;
            case XmlHarmonyConfiguration.LANGUAGE_F:
                currentLocale = Locale.FRENCH;
                break;
            case XmlHarmonyConfiguration.LANGUAGE_NL:
                currentLocale = Locale.forLanguageTag("NL");
                break;
            default:
                currentLocale = Locale.getDefault();
                break;
        }
        return getCurrentLocale();
    }

    public void setLocale(Locale newLocale,int language) {
        currentLocale = newLocale;
        bundle = ResourceBundle.getBundle("de/bridgephone/bridgephonecoworker/resources/BridgePhoneHarmonyConfigurator", currentLocale);
        xmlRecord.setLanguage(languages[language]);
    }

    /**
     * @return the currentLocale
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }


}
