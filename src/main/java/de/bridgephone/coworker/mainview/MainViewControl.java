/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bridgephone.coworker.mainview;


import de.bridgephone.coworker.xmlconfiguration.CheckResult;
import de.bridgephone.coworker.xmlconfiguration.XmlHarmonyConfiguration;
import de.bridgephone.coworker.xmlconfiguration.XmlValueRecord;
import org.apache.logging.log4j.LogManager;

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
    private java.util.logging.Logger LOG= Logger.getLogger(TAG);

    static void changeLocale(Locale currentLocale) {

    }



    public void setViewFx(MainViewFx view) {
        this.view = view;

    }

    public Locale getLocale() {
        currentLocale = xmlHarmonyConfiguration.getCurrentLocale();
        return currentLocale;
    }

    /**
     *
     * @param xmlHarmonyConfiguration Based on xmlHarmony fill the main view
     */
    public void initialFillView(XmlHarmonyConfiguration xmlHarmonyConfiguration) {
        this.xmlHarmonyConfiguration = xmlHarmonyConfiguration;
        XmlValueRecord xmlRecord = xmlHarmonyConfiguration.getXmlRecord();
        getLocale();

        checkUpdateTextFields(xmlHarmonyConfiguration, xmlRecord);

    }

    private void checkUpdateTextFields(XmlHarmonyConfiguration xmlHarmonyConfiguration1, XmlValueRecord xmlRecord) {
        if (xmlHarmonyConfiguration1.checkXmlRecordsOnViability()) {
            ArrayList<CheckResult> crList = xmlRecord.getCr();
            // Scoring program
            int i = XmlValueRecord.SCORINGPROGRAM;
            String s;
            CheckResult cr = crList.get(XmlValueRecord.SCORINGPROGRAM);
            if (cr.isResult()) {
                File scoringProgram = cr.getFile();
                if (scoringProgram == null) {
                    s = "null";
                } else {
                    s = cr.getFile().getAbsolutePath();
                }
            } else {
                s = cr.getErrorMessage();
            }
            view.setTextField(i, s, cr.isResult());
            i = XmlValueRecord.PCBRIDGEPHONEPROGRAM;
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
            i = XmlValueRecord.BWSDIR;
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


            view.enableDisableStart(false);
            if (crList.get(XmlValueRecord.BWSDIR).isResult()) {
                if (crList.get(XmlValueRecord.PCBRIDGEPHONEPROGRAM).isResult()) {
                    if (crList.get(XmlValueRecord.SCORINGPROGRAM).isResult()) {
                        view.enableDisableStart(true);
                    }
                }
            }
        }
    }


    void changeLocale(Locale currentLocale, int i) {
        this.currentLocale = currentLocale;
        xmlHarmonyConfiguration.setLocale(currentLocale, i);
    }

    void check(File f, int fieldNo) {
        String path = f.getAbsolutePath();
        if (fieldNo == XmlValueRecord.BWSDIR) {
            xmlHarmonyConfiguration.getXmlRecord().setBwsFilePath(path);
        }
        if (fieldNo == XmlValueRecord.PCBRIDGEPHONEPROGRAM) {
            xmlHarmonyConfiguration.getXmlRecord().setPcbridgephoneFilePath(path);
        }
        if (fieldNo == XmlValueRecord.PCBRIDGEPHONEPROGRAM) {
            xmlHarmonyConfiguration.getXmlRecord().setScoringProgramFilePath(path);
        }
        checkUpdateTextFields(xmlHarmonyConfiguration, xmlHarmonyConfiguration.getXmlRecord());
    }

    void submitButtonPressed() {
        LOG.log(Level.INFO,"Submit buton clicked");
         if (!xmlHarmonyConfiguration.updateXmlFile()){
             LOG.log(Level.WARNING,"Update of XML failed");
         };
    }

}
