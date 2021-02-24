/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bridgephone.coworker.xmlconfiguration;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.bridgephone.coworker.xmlconfiguration.XmlHarmonyConfiguration.*;
import static org.apache.commons.io.FileUtils.copyFile;


/**
 *
 * @author Kees
 */
public class XmlFileHandling {
   private static final String TAG = "XmlFileHandling";
//    private final static Logger LOG = LogManager.getLogger(TAG);
    private final java.util.logging.Logger LOG= Logger.getLogger(TAG);
    private static final String TEMP_UC = "TEMP";
    private static final String TEMP_LC = "temp";
    private static final String BRIDGEPHONEHARMONYDIR = "bridgephoneharmony";
    public static final String FILEXML = "bridgephoneharmony.xml";


    /**
     * Make sure an XML File exists.
     * Fill the Xml Value Record with the found
     * values In case of error, set the error message in the record
     *
     * @param xmlRecord containing the current values
     * @return the boolean
     */
    boolean prepareXmlFile(XmlValueRecord xmlRecord, String xmlDirUrl) {

        File f = new File(xmlDirUrl);
        if (!f.exists()){ // if it does not exist yet, create it
            final boolean mkdir = f.mkdir();
            if (!mkdir){
                LOG.log(Level.SEVERE, "Houston , we have a problem. The workdirectory cannot be created");
                return false;
            }
            // and copy all stuff from the home directory to the workdirectory

             int i=   copyFilesFromHomeToWorkDir(f.getAbsolutePath());
                if (i == 3) {

                    LOG.log(Level.INFO, "New workdirectory with content created");
                }else {
                String s="Problem opening " + xmlDirUrl;
                LOG.log(Level.SEVERE,s);
                xmlRecord.setXmlDirUrl(xmlDirUrl);
                return false;
            }
        }
        // xmlDirUrl is an existing directory
        xmlDirUrl = f.getAbsolutePath();
        xmlRecord.setXmlDirUrl(xmlDirUrl);


        String xmlFilePath = xmlDirUrl + "\\" + FILEXML;
        if (!new File(xmlFilePath).exists()) {
                int i=   copyFilesFromHomeToWorkDir(f.getAbsolutePath());
                if (i == 3) {

                    LOG.log(Level.INFO, "New workdirectory with content created");
                }else {
                    String s="Problem opening " + xmlDirUrl;
                    LOG.log(Level.SEVERE,s);
                    xmlRecord.setXmlDirUrl(xmlDirUrl);
                    return false;
                }
        } else {
            xmlRecord.setXmlFilePath(xmlFilePath);
            String s = java.text.MessageFormat.format(" Xml file path set to {0}", xmlFilePath);
            LOG.log(Level.INFO,s);
            return true;
        }
        return false;
    }

    /**
     *
     * @param test boolean true is test version
     * @return working directory in the appdata
     */
    public String getWorkDir(boolean test) {
        String xmlDirUrl;
        Map<String, String> env = System.getenv();
        xmlDirUrl = env.get(TEMP_UC);
        xmlDirUrl = xmlDirUrl.toLowerCase();
        if (!test){
         xmlDirUrl = xmlDirUrl.replaceFirst(TEMP_LC, BRIDGEPHONEHARMONYDIR) + "\\";
        } else{
          xmlDirUrl = xmlDirUrl.replaceFirst(TEMP_LC, BRIDGEPHONEHARMONYDIR) + "test\\";
        }
        return xmlDirUrl;
    }

    /**
     * *
     * copy all files from the home directory to the work directory except jar
     * an exe files
     *
     * @throws IOException an unexpected IO file while copying
     * @return number of files copied or -1nif exception
     */
    public int copyFilesFromHomeToWorkDir(String workDirUrl)  {
        URL homeDirUrl=getClass().getResource("/bridgephoneharmony.xml");
        String path= homeDirUrl.getPath();
        int index=path.indexOf("bridgephoneharmony.xml");
        String homeDir=path.substring(0,index);
        File homeDirFile = new File(homeDir);


        File[] listFiles=homeDirFile.listFiles();
        int i=0;

        assert listFiles != null;
        for (File srcFile : listFiles) {
            if (!srcFile.isDirectory()) {
                if (srcFile.canRead()) {
                    if (!srcFile.isHidden()) {
                        String fileName=srcFile.getName().toLowerCase();
                        if (fileName.endsWith(".xml")) {
                            if (fileName.startsWith("bridgephoneharmony")) {
                                String dstFileName = workDirUrl + "\\"+fileName;
                                File dstFile = new File(dstFileName);
                                if (dstFile.exists()) {
                                    if (!dstFile.delete()){
                                        LOG.log(Level.WARNING,dstFile.getName()+ "could not be deleted");
                                        i=10;
                                    }
                                }
                                try {
                                    copyFile(srcFile, dstFile);
                                } catch (IOException e) {
                                    i=-1;
                                    return i;
                                }
                                LOG.log(Level.INFO,srcFile.getName()+ " copied");
                                i++;
                            }
                        }
                    }
                }
            }
        }
        return i;
    }


    /***
     * Read the path's from the XMl file
     * @param xmlRecord is initialized for the working directory
     8 The xmlRecord is updated with the values read
     */
    public void readPathsFromXmlFile(XmlValueRecord xmlRecord){
        XmlDocHandling xmlDocHandling = new XmlDocHandling(xmlRecord.getXmlFilePath());

        // read language
        String language = xmlDocHandling.readFilePath(LANGUAGE_TAG);
        xmlRecord.setLanguage(language);
        // read score program path
        String scoreProgramPath = xmlDocHandling.readFilePath(SCORE_PROGRAMM_PATH_TAG);
        xmlRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM).setFile(new File(scoreProgramPath));
        //read Bws path
        String bwsPath = xmlDocHandling.readFilePath(BWS_PATH_TAG);
        xmlRecord.getCr().get(XmlValueRecord.BWSDIR).setFile(new File(bwsPath));
        // read PCBridgePhone path
        String pcBridgePhonePath = xmlDocHandling.readFilePath(BRIDGEPHONE_PATH_TAG);
        xmlRecord.getCr().get(XmlValueRecord.PCBRIDGEPHONEPROGRAM).setFile(new File(pcBridgePhonePath));

    }

}
