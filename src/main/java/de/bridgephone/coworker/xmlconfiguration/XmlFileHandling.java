/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bridgephone.coworker.xmlconfiguration;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    boolean preparXmlFile(XmlValueRecord xmlRecord, String xmlDirUrl) {

        File f = new File(xmlDirUrl);
        if (!f.isDirectory()){
            LOG.log(Level.SEVERE, "Houston , we have a problem. The workdirectory is not adirectory");
            return false;
        }
        if (!f.exists()){ // if it does not exist yet, create it
            final boolean mkdir = f.mkdir();
            if (!mkdir){
                LOG.log(Level.SEVERE, "Houston , we have a problem. The workdirectory cannot be created");
                return false;
            }
            // and copy all stuf from the home directory to the workdirectory

            try {
                copyFilesFromHomeToWorkDir(f.getAbsolutePath());
                LOG.log(Level.INFO,"Neew workdirectory with content created");

            } catch (IOException ex) {
                LOG.log(Level.SEVERE,ex.getMessage());
                xmlRecord.setXmlDirUrl(xmlDirUrl);
                xmlRecord.setErrorMessage("Problem opening " + xmlDirUrl);
                return false;
            }
        }
        // xmlDirUrl is an existing directory
        xmlDirUrl = f.getAbsolutePath();
        xmlRecord.setXmlDirUrl(xmlDirUrl);


        String xmlFilePath = xmlDirUrl + "\\" + FILEXML;
        if (!new File(xmlFilePath).exists()) {
            try {
                if (!new File(xmlFilePath).createNewFile()) {
                    String s = "Could not create a configuration file ";
                    xmlRecord.setXmlFilePath(s);
                    xmlRecord.setErrorMessage(s);
                    LOG.log(Level.SEVERE,s);
                    return false;
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE,ex.getMessage());
                return false;
            }
        } else {
            xmlRecord.setXmlFilePath(xmlFilePath);
            String s = java.text.MessageFormat.format(" Xml file path set to {0}", xmlFilePath);
            LOG.log(Level.INFO,s);
//            LOG.info(s);
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
     */
    private void copyFilesFromHomeToWorkDir(String workDirUrl) throws IOException {
        File homeDir = new File(".\\");

        File[] listFiles=homeDir.listFiles();

        assert listFiles != null;
        for (File srcFile : listFiles) {
            if (!srcFile.isDirectory()) {
                if (srcFile.canRead()) {
                    if (!srcFile.isHidden()) {
                        String fileName=srcFile.getName().toLowerCase();
                        if (fileName.endsWith(".xml")) {
                            if (fileName.startsWith("bridge")) {
                                String dstFileName = workDirUrl + "\\"+fileName;
                                File dstFile = new File(dstFileName);
                                if (dstFile.exists()) {
                                    if (!dstFile.delete()){
                                        LOG.log(Level.WARNING,dstFile.getName()+ "could not be deleted");
                                    }
                                }
                                copyFile(srcFile, dstFile);
                            }
                        }
                    }
                }
            }
        }
    }

}
