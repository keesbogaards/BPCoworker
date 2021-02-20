package de.bridgephone.coworker.xmlconfiguration;


import de.bridgephone.coworker.bppcexceptions.BridgePhoneException;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Kees
 */
public class XmlValueRecord {

    private static final String TAG = "XmlValueRecord";

    public static final int SCORINGPROGRAM = 0;
    public static final int PCBRIDGEPHONEPROGRAM = 1;
    public static final int BWSDIR = 2;

    private String xmlDirUrl;
    private String xmlFilePath;
    private String language;
    private final ArrayList<CheckResult> crList;

    public XmlValueRecord() {
        crList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CheckResult cr = new CheckResult();
            cr.setResult(false);
            cr.setErrorMessage("not initialized");
            cr.setFile(new File("not initialized"));
            crList.add(cr);
        }
    }

    /**
     * @return the xmlDirUrl
     */
    public String getXmlDirUrl() {
        return xmlDirUrl;
    }

    /**
     * @param xmlDirUrl the xmlDirUrl to set
     */
    public void setXmlDirUrl(String xmlDirUrl) {
        this.xmlDirUrl = xmlDirUrl;
    }

    /**
     * @return the xmlFilePath
     */
    public String getXmlFilePath() {
        return xmlFilePath;
    }

    /**
     * @param xmlFilePath the xmlFilePath to set
     */
    public void setXmlFilePath(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }


    /**
     * @return the bwsFilePath
     */
    public String getBwsFilePath() {
        return crList.get(BWSDIR).getFile().getAbsolutePath();
    }

    /**
     * @param bwsFilePath the bwsFilePath to set
     */
    public void setBwsFilePath(String bwsFilePath) {
        crList.get(BWSDIR).setFile(new File(bwsFilePath));
//        this.bwsFilePath = bwsFilePath;
    }

    /**
     * *
     * Update xml value
     *
     * @param index
     * @param f
     * @throws BridgePhoneException
     */
    public void updateXmlRecordFilePath(int index, File f)  {

        crList.get(index).setFile(f);
        }

        public File readXmlRecordFilePath(int index){
          return crList.get(index).getFile();
        }

    /**
     *
     * @return the pcbridgephoneFilePath
     */
    public String getPcbridgephoneFilePath() {
        return crList.get(PCBRIDGEPHONEPROGRAM).getFile().getAbsolutePath();
    }

    /**
     *
     * @param pcbridgephoneFilePath the pcbridgephoneFilePath to set
     */
    public void setPcbridgephoneFilePath(String pcbridgephoneFilePath) {
        crList.get(PCBRIDGEPHONEPROGRAM).setFile(new File(pcbridgephoneFilePath));
    }


    /**
     * @return the crList
     */
    public ArrayList<CheckResult> getCr() {
        return crList;
    }

    public void setCr(int textFieldId, CheckResult ccheckResult) {
        crList.set(textFieldId, ccheckResult);

    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
      
    }

 

}
