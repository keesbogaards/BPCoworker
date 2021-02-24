/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bridgephone.coworker.xmlconfiguration;


import de.bridgephone.coworker.bppcexceptions.BridgePhoneException;

import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;



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


    public String defineWorkdir() {
        return xmlFileHandling.getWorkDir(test);
    }

    /***
     * initialize xmlRecord with the work directory
     * @param workDirUrl established work directory appdata/local/bridgephoneharmony
     */
    public boolean initialize(String workDirUrl) {
        return xmlFileHandling.prepareXmlFile(xmlRecord, workDirUrl);
    }

    /**
     * @return the xmlRecord
     */
    public XmlValueRecord getXmlRecord() {
        return xmlRecord;
    }

    /**  TODO check completely
     * *
     * Read all the values from the xml file Test on existence. If null
     * , set error messages and return
     * @return empty message if ok, else all problems specified
     */
    public String checkOutXmlRecord() {

        StringBuilder sb= new StringBuilder("");
        try {
            xmlFileHandling.readPathsFromXmlFile(xmlRecord);
            String language =xmlRecord.getLanguage();
            xmlRecord.setLanguage(language);
            currentLocale=readLocale();
            bundle = ResourceBundle.getBundle("BridgePhoneHarmonyConfigurator", currentLocale);

            String scoreProgrammFilePath =xmlRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM).getFile().getAbsolutePath();
            if (scoreProgrammFilePath.isEmpty()) {
                String s = bundle.getString("score_program_path_is_not_set");
                xmlRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM).setErrorMessage(s);
                LOG.log(Level.SEVERE,TAG+s);
                sb.append(s);
            }
//        Read bws file path
            String bwsFilePath =xmlRecord.getCr().get(XmlValueRecord.BWSDIR).getFile().getAbsolutePath();
            if (bwsFilePath.isEmpty()) {
                String s = TAG+ bundle.getString("bws_file_path_is_not_set");
                LOG.log(Level.SEVERE,TAG+s);
                sb.append(" "+s);
            }

//      Read BridgePhone Path
            String bridgePhoneFilePath =xmlRecord.getCr().get(XmlValueRecord.PCBRIDGEPHONEPROGRAM).getFile().getAbsolutePath();
            if (bridgePhoneFilePath.isEmpty()) {
                String s = bundle.getString("bridgePhone_fil_path_is_not_set");
                LOG.log(Level.WARNING,TAG+s);
                sb.append(" "+s);

            }
            return sb.toString();

        } catch (Throwable t) {
            String s = "Exception";
            sb.append(" "+s);
            LOG.log(Level.SEVERE,TAG+s+t.getMessage());
            return sb.toString();

        }
    }

    /**
     * *
     * SCORE_PROGRAMM_PATH_TAG ; BWS_PATH_TAG BRIDGEPHONE_PATH_TAG;
     * BM_PRO_PATH_TAG
     *
     */
    public boolean updateXmlFile(/*XmlValueRecord xmlRecord*/) {
//        xmlFileHandling.readPathsFromXmlFile(xmlRecord);
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

    public void updateXmlRecord(File f, int fieldNo) {
        xmlRecord.getCr().get(fieldNo).setFile(f);
    }

    /**
     * **
     * Check entries in XmlValueRecord on viability
     *
     * @return false in case of at least one error
     */
    public boolean checkXmlRecordsOnViability() {
        try {
            File f;
            boolean errorFree=true;
            StringBuilder sb= new StringBuilder();
            for (int i = 0; i < 3; i++) {
                switch (i) {
                    case XmlValueRecord.BWSDIR: {
                        checkViabilityBwsDir();
                        break;
                    }
                    case XmlValueRecord.PCBRIDGEPHONEPROGRAM: {
                        checkViabilityPCBridgePhoneProgram();
                        break;
                    }
                    case XmlValueRecord.SCORINGPROGRAM: {
                        checkViabilityScoringProgram();
                        break;
                    }
                    default: {
                        errorFree = false;
                        break;
                    }
                }
                errorFree = isErrorFree(i,errorFree, sb);
            }

            LOG.log(Level.INFO,TAG+sb.toString());
            return errorFree;
        } catch (Throwable t) {
            String s = " checkXmlRecordsOnViability ";
            LOG.log(Level.SEVERE,TAG+s+t.getMessage());
            return false;
        }
    }

    public void checkViabilityScoringProgram() {
        File f;
        f=xmlRecord.readXmlRecordFilePath(XmlValueRecord.SCORINGPROGRAM);
        if(checkOnExistence(f, XmlValueRecord.SCORINGPROGRAM)){
            checkOnExecutability(f, XmlValueRecord.SCORINGPROGRAM) ;
        }
    }

    public void checkViabilityBwsDir() {
        File f;
        f=xmlRecord.readXmlRecordFilePath(XmlValueRecord.BWSDIR);
        if( checkOnExistence(f, XmlValueRecord.BWSDIR)){
            checkOnBws(f);
        }
    }

    public void checkViabilityPCBridgePhoneProgram() {
        File f;
        f=xmlRecord.readXmlRecordFilePath(XmlValueRecord.PCBRIDGEPHONEPROGRAM);
        if(checkOnExistence(f, XmlValueRecord.PCBRIDGEPHONEPROGRAM) ){
             if (!checkOnExecutability(f, XmlValueRecord.PCBRIDGEPHONEPROGRAM)){
                 String bridgePhoneRegistryFilePath = WindowsReqistry.readRegistry("HKEY_LOCAL_MACHINE\\SOFTWARE\\WOW6432Node\\BridgePhone", "install_Dir");
                 if (bridgePhoneRegistryFilePath!=null) {
                     File fNex = new File(bridgePhoneRegistryFilePath);
                     checkOnExistence(fNex, XmlValueRecord.PCBRIDGEPHONEPROGRAM);
                 }
             }
         }
    }

    private boolean isErrorFree(int i,boolean errorNotFound, StringBuilder sb) {
        CheckResult cr=xmlRecord.getCr().get(i);
        if (cr.isResult()){
            sb.append (" ok ");
            sb.append(cr.getFile().getAbsolutePath());
        }else{
            errorNotFound =false;
            sb.append( "error ");
            sb.append(cr.getErrorMessage());
        }
        return errorNotFound;
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
    }

    public void setScoringProgramPathFile(File f) {
        xmlRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM).setFile(f);
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
     *  Update the xml record crList
     * @param  f exe or bws file
     * @param textFieldId 0..2 scorecalcuation program, bridgephone.exe , bws directory
     * @return true if exists
     */
    public boolean checkOnExistence(File f, int textFieldId) {
        CheckResult cr = xmlRecord.getCr().get(textFieldId);
        try {
            if (f.getAbsolutePath() == null) {
                String s = bundle.getString("file_not_defined");
                CheckResult crNew=new CheckResult(f,CheckResult.NOK,s);
                xmlRecord.setCr(textFieldId, crNew);
                LOG.log(Level.WARNING,TAG +" TextFileds " + textFieldId + " " + s);
                return false;
            }
            if (!f.exists()) {
                String s = MessageFormat.format(bundle.getString("file_0_does_not_exist"), f.getName());
                CheckResult crNew=new CheckResult(f,CheckResult.NOK,s);
                xmlRecord.setCr(textFieldId, crNew);
                LOG.log(Level.WARNING,TAG + s);
                return false;
            }
            CheckResult crNew=new CheckResult(f,CheckResult.OK,"");
            xmlRecord.setCr(textFieldId, crNew);
            return true;
        } catch (Throwable t) {
            String filePath=f.getAbsolutePath();
            String s = MessageFormat.format(bundle.getString("file_0_does_not_exist"), filePath);
            CheckResult crNew=new CheckResult(f,CheckResult.NOK,s);
            xmlRecord.setCr(textFieldId, crNew);
            LOG.log(Level.WARNING,TAG +" " + s+" "+t.getMessage());
            return false;
        }
    }

    /**
     * Check whether file is executable
     * Presume file exists
     *
     * @param f file to be checked on executability
     * @param textFiledId number in the testFieldArray
     * @return true if executable
     */
    public boolean checkOnExecutability(File f, int textFiledId) {
        if (!f.exists()){
            //programming error checkOnExecutability should be called after  checkOnExistance
            String s="WTF file "+f.getAbsolutePath()+" should exists";
            LOG.log(Level.SEVERE,TAG +" " + s);
            CheckResult crNew=new CheckResult(f,CheckResult.NOK,s);
            xmlRecord.setCr(textFiledId,crNew);
            return false;
        }

        if (!f.getAbsolutePath().trim().toLowerCase().endsWith(".exe")) {
            String s = MessageFormat.format(bundle.getString("file_0_is_not_executable"), f.getAbsoluteFile());
            CheckResult crNew=new CheckResult(f,CheckResult.NOK,s);
            xmlRecord.setCr(textFiledId,crNew);
            LOG.log(Level.WARNING,TAG +" " + s);
            return false;
        }
        CheckResult crNew=new CheckResult(f);
        xmlRecord.setCr(textFiledId,crNew);
        return true;
    }

    public void checkOnBws(File f) {
        int textFiledId=XmlValueRecord.BWSDIR;
        if (!f.getAbsolutePath().trim().toLowerCase().endsWith(".bws")) {
            String s = bundle.getString("file_0_does_not_end_with_bws");
            s = MessageFormat.format(s, f.getAbsolutePath());
            CheckResult crNew=new CheckResult(f,CheckResult.NOK,s);
            xmlRecord.setCr(textFiledId,crNew);
            LOG.log(Level.WARNING,TAG +" " + s);
//            LOG.fatal(s);
            return;
        }else {
            CheckResult crNew = new CheckResult(f);
            xmlRecord.setCr(textFiledId, crNew);
        }
//        return true;
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
