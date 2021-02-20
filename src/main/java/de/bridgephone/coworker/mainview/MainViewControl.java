/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bridgephone.coworker.mainview;


import de.bridgephone.coworker.MyCallable;
import de.bridgephone.coworker.xmlconfiguration.CheckResult;
import de.bridgephone.coworker.xmlconfiguration.XmlHarmonyConfiguration;
import de.bridgephone.coworker.xmlconfiguration.XmlValueRecord;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author keesb
 */
public class MainViewControl {
    private static final String TAG="MainViewControl";
    private MainViewFx view;
    private XmlHarmonyConfiguration xmlHarmonyConfiguration;
    private Locale currentLocale;
    private final java.util.logging.Logger LOG= Logger.getLogger(TAG);

//    void changeLocale(Locale locale, int language) {
//        currentLocale=locale;
//        xmlHarmonyConfiguration.setLocale(currentLocale,language );
//    }



    public void setViewFx(MainViewFx view) {
        this.view = view;

    }

//    public Locale getLocale() {
//        currentLocale = xmlHarmonyConfiguration.getCurrentLocale();
//        return currentLocale;
//    }

    /**
     *
     * @param xmlHarmonyConfiguration Based on xmlHarmony fill the main view
     */
    public void initialFillView(XmlHarmonyConfiguration xmlHarmonyConfiguration) {
        this.xmlHarmonyConfiguration = xmlHarmonyConfiguration;
        XmlValueRecord xmlRecord = xmlHarmonyConfiguration.getXmlRecord();
        currentLocale = xmlHarmonyConfiguration.getCurrentLocale();
        checkUpdateTextFields(xmlHarmonyConfiguration, xmlRecord);

    }

    private void checkUpdateTextFields(XmlHarmonyConfiguration xmlHarmonyConfiguration1, XmlValueRecord xmlRecord) {
       xmlHarmonyConfiguration1.checkXmlRecordsOnViability() ;
            ArrayList<CheckResult> crList = xmlRecord.getCr();
            setTextFieldText(crList,XmlValueRecord.SCORINGPROGRAM);
            setTextFieldText(crList,XmlValueRecord.PCBRIDGEPHONEPROGRAM);
            setTextFieldText(crList,XmlValueRecord.BWSDIR);

            view.enableDisableStart(false);
            if (crList.get(XmlValueRecord.BWSDIR).isResult()) {
                if (crList.get(XmlValueRecord.PCBRIDGEPHONEPROGRAM).isResult()) {
                    if (crList.get(XmlValueRecord.SCORINGPROGRAM).isResult()) {
                        view.enableDisableStart(true);
                    }
                }
            }
        }


    /***
     * Set the TextField text based onthe value of the cr
     * @param crList
     * @param i
     */
    private void setTextFieldText(ArrayList<CheckResult> crList, int i ) {
        CheckResult cr;
        String s;
        cr = crList.get(i);
        if (cr.isResult()) {
            File pcBridgePhone = cr.getFile();
            if (pcBridgePhone == null) {
                s = "null";
            } else {
                s = cr.getFile().getAbsolutePath();
            }
        } else {
            s = cr.getErrorMessage();
        }
        view.setTextField(i, s, cr.isResult());
    }


    void changeLocale(Locale currentLocale, int i) {
        this.currentLocale = currentLocale;
        xmlHarmonyConfiguration.setLocale(currentLocale, i);
    }


    /**
     * Check the file value  f
     * check all fields
     * r
     * @param f the File
     * @param fieldNo
     * @return
     */
    CheckResult newFileNameEntered(File f, int fieldNo) {
        xmlHarmonyConfiguration.updateXmlRecord(f,fieldNo);
        xmlHarmonyConfiguration.updateXmlFile();
        checkUpdateTextFields(xmlHarmonyConfiguration, xmlHarmonyConfiguration.getXmlRecord());
        return xmlHarmonyConfiguration.getXmlRecord().getCr().get(fieldNo);
    }

    boolean submitButtonPressed() {
        LOG.log(Level.INFO,"Submit buton clicked");
         if (!xmlHarmonyConfiguration.updateXmlFile()){
             LOG.log(Level.WARNING,"Update of XML failed");
             return false;
         }
         boolean b= xmlHarmonyConfiguration.checkXmlRecordsOnViability();
         if (!b){
             return false;
         }
        MyCallable myCallable=new MyCallable(xmlHarmonyConfiguration);
        try {
            myCallable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
