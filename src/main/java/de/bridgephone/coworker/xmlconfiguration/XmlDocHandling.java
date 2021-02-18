/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bridgephone.coworker.xmlconfiguration;


import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import de.bridgephone.coworker.bppcexceptions.BridgePhoneException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class handles the bridgephone.xml actions
 *
 * @author Kees
 */
public class XmlDocHandling {
    private final static Logger LOG = LogManager.getLogger(XmlDocHandling.class);
    
        private final ResourceBundle bundle;

//    public static final String ACBL_PROGRAM_TYPE = "ACBL";
//    public static final String NBB_PROGRAM_TYPE = "NBB";
//    public static final String TOURNOIA_PROGRAM_TYPE = "TOURNOIA";
//    public static final String RUDERSYV_PROGRAM_TYPE = "RUDERSYV";
//    public static final String PAIRSSCORER_PROGRAM_TYPE = "PAIRSCORER";
//    public static final String BRIDGEPHONE_PROGRAM_TYPE = "BRIDGEPHONESCORER";
//    public static final String MESSERBRIDGE_PROGRAM_TYPE = "MESSERBRIDGE";
//    public static final String BRIDGEIT_PROGRAM_TYPE = "BRIDGEIT";
//    public static final String COWORKERR_PROGRAM_TYPE = "COWORKER";
//    public static final String NO_PROGRAM_TYPE = "NONE";

//    public static final String WINDOWS = "WINDOWS";
//    public static final String LINUX = "LINUX";
//    public static final String MAC_OS = "MAC_OS";

    public static final String ACCESS96 = "ACCESS_96";
    public static final String ACCESS2000 = "ACCESS_2000";

//    public static final String DB_URL_TAG = "db_url";
//    public static final String PROGRAM_TYPE_TAG = "program_type";
//    public static final String START_RESTART_TAG = "start_restart";
//    public static final String MEMBERS_DB_TAG = "member_db";
//    public static final String SCORE_DB_TAG = "score_db";
//    public static final String OS_TAG = "os_type";
//    public static final String ACCESS_TAG = "access_version";
//    public static final String MOVEMENTLIB_TAG = "movement_library";
//    public static final String LANGUAGE_TAG = "language";    
//   public static final String SESSION_TAG = "session_enable";
   
//   public static final String SESSION_ENABLED = "session_table_enabled";
//   public static final String SESSION_DISABLED = "session_table_disabled";
   
//   public static final String START=  "start";
//   public static final String RESTART=  "restart";
   
   

    public static final String LANGUAGE_EN = "EN";
    public static final String LANGUAGE_NL = "NL";
    public static final String LANGUAGE_DE = "DE";
    public static final String LANGUAGE_F = "F";

    private static final String TAG = "XmlConfiguration";

//    public static final String FILEXML = "bridgephone.xml";

//    private final ArrayList<String> ul = new ArrayList<>(); // in case more nodes are found
    private final String fileName;
//    private final String localAppData;


    /**
     * *
     * Just used for testing purposes
     *
     * @param xmlFileUrl
     */
    public XmlDocHandling(String xmlFileUrl) {
        bundle=ResourceBundle.getBundle("BridgePhoneHarmonyConfigurator");
        fileName = xmlFileUrl;
//        String[] dir = fileName.split(FILEXML, 2);
//        localAppData = dir[0];

    }

//    /**
//     * *
//     * For restart purposes
//     *
//     * @param db_url
//     * @param tag
//     * @throws de.bridgephone.BPPCExceptions.BridgePhoneException
//     */
//    public void insertDirectory(String db_url, String tag) throws BridgePhoneException {
//        try {
//            NodeList groupNodes = getNodeList(fileName, tag);
//            Node url = groupNodes.item(0);
//            Document docu = url.getOwnerDocument();
//            // create a new node elelment
//            Element newDir = docu.createElement(tag);
//            Text newText = docu.createTextNode(db_url);
//            newDir.appendChild(newText);
//            url.getParentNode().insertBefore(newDir, url);//  appendChild(newDir);
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer;
//            transformer = transformerFactory.newTransformer();
//            DOMSource source = new DOMSource(docu);
//            StreamResult fileToStore = new StreamResult(fileName);
//            transformer.transform(source, fileToStore);
//        } catch (TransformerException | ParserConfigurationException | SAXException | IOException ex) {
//            String s = "insertDirectory parsing or IO problem";
//            LOG.error ( s, ex);
//            throw new BridgePhoneException(TAG, s);
//        }
//    }

//    public boolean removeDirectory(int nodeNum, String tag) {
//        boolean result = false;
//        try {
//            NodeList groupNodes = getNodeList(fileName, tag);
//            Node url = groupNodes.item(nodeNum);
//            if (url == null) {
//                return false;
//            }
//            url.getParentNode().removeChild(url);
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            Document docu = url.getOwnerDocument();
//            DOMSource source = new DOMSource(docu);
//            StreamResult fileToStore = new StreamResult(fileName);
//            transformer.transform(source, fileToStore);
//            result = true;
//            System.out.println("File removed");
//
//        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
//            LOG.fatal (  ex);
//        }
//        return result;
//    }

    /**
     *
     * @param elementText the value of elementText
     * @param tag the value of tid
     * @throws BridgePhoneException
     */
    public void changeElement(String elementText, String tag) throws BridgePhoneException {
        try {
            NodeList nList = getNodeList(fileName, tag);
            if (nList.getLength() < 1) {
                String s = " no facility for " + tag;
                LOG.warn ( s);
                JOptionPane.showMessageDialog(null, s);
                throw new SAXException(s);
            }
            Node nNode = nList.item(0);// there should be only 1
            if (nNode.getNodeType()
                    == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                if (tag.equals(eElement.getNodeName())) {
                    eElement.setTextContent(elementText);
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            Node url = nList.item(0);
            Document docu = url.getOwnerDocument();
            DOMSource source = new DOMSource(docu);
            StreamResult fileToStore = new StreamResult(fileName);
            transformer.transform(source, fileToStore);
        } catch (TransformerException | ParserConfigurationException | SAXException | IOException ex) {
            String s = "problem parsing xml file" + ex.getMessage();
            LOG.warn ( s, ex);
            JOptionPane.showMessageDialog(null, s);
            throw new BridgePhoneException(TAG, s);
        }
    }

//    /**
//     * Read the name of the work directory from the Xml file This name must be
//     * an existing directory If no valid directory found, a bridgephone
//     * directory is created in the default directory that contains the TEMP
//     * directory
//     *
//     * @return the work directory as a string
//     */
//    public String determineWorkDirectory() {
//        String url = "empty";
//        boolean found = false;
//        try {
//            File bridgephoneDb;
//            NodeList groupNodes = getNodeList(fileName, DB_URL_TAG);
//            for (int i = 0; i < groupNodes.getLength(); i++) {
//                Node groupNode = groupNodes.item(i);
//                groupNode.normalize();
//                url = groupNode.getTextContent().trim();
//                bridgephoneDb = new File(url);
//                url = bridgephoneDb.getAbsolutePath();
//                bridgephoneDb = new File(url);
//                if (bridgephoneDb.isDirectory()) {
//                    if (bridgephoneDb.exists()) {
//                        found = true;
//                        break;
//                    }
//                }
//            }
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
//        }
//        if (!found) {
//            url = localAppData;
//        }
//        return url;
//    }

    /**
     * *
     * The getenv() function provides a.o the temp directory for temporary use
     * of internal data By replacing in the absolute path temp with bridgephone,
     * a temporary directory is created that can be used by PCBridgePhone If it
     * does not (yet) exist, it is created
     *
     * @param tag
     * @return temporary PCBridgePhone directory
     */
//    public String getDefaultWindowsWorkDirectory() {
//        String os_type;
//        Map<String, String> env = System.getenv();
//        os_type = env.get(TEMP_UC);
//        os_type = os_type.toLowerCase();
//        os_type = os_type.replaceFirst(TEMP_LC, BRIDGEPHONEDIR) + "\\";
//        File bridgephoneWorkDir = new File(os_type);
//        os_type = bridgephoneWorkDir.getAbsolutePath();
//        bridgephoneWorkDir = new File(os_type);
//        if (!bridgephoneWorkDir.exists()) {
//            bridgephoneWorkDir.mkdir();
//        }
//        return os_type;
//    }
//
//    private String getDefaultLinuxWorkDirectory() {
//        String os_type;
//        Map<String, String> env = System.getenv();
//        os_type = env.get(HOME);
//
//        StringBuilder sb = new StringBuilder(os_type);
//        sb.append("/");
//        sb.append(BRIDGEPHONEDIR);
//        os_type = sb.toString();
//        File bridgephoneWorkDir = new File(os_type);
//        os_type = bridgephoneWorkDir.getAbsolutePath();
//        bridgephoneWorkDir = new File(os_type);
//        if (!bridgephoneWorkDir.exists()) {
//            bridgephoneWorkDir.mkdir();
//        }
//        return os_type;
//    }
//    public String getDefaultWindowsWorkDirectory(String directoryName) {
//        String os_type;
//        Map<String, String> env = System.getenv();
//        os_type = env.get(TEMP_UC);
//        os_type = os_type.toLowerCase();
//        os_type = os_type.replaceFirst(TEMP_LC, BRIDGEPHONEDIR) + "\\";
//        File bridgephoneWorkDir = new File(os_type);
//        os_type = bridgephoneWorkDir.getAbsolutePath();
//        bridgephoneWorkDir = new File(os_type);
//        if (!bridgephoneWorkDir.exists()) {
//            bridgephoneWorkDir.mkdir();
//        }
//        return os_type;
//    }
//    /*S*
//     * *
//     * Read the default program type from the Xml file
//     *
//     * @return os_type of the work directory
//     */
//    public String readDefaultProgramType() {
//        String url = NO_PROGRAM_TYPE;
//        try {
//            NodeList groupNodes = getNodeList(fileName, PROGRAM_TYPE_TAG);
//            if (groupNodes.getLength() <= 0) {
//                return url;
//            }
//            Node groupNode = groupNodes.item(0);
//            groupNode.normalize();
//            url = groupNode.getTextContent().trim();
//
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
//        }
//        return url;
//    }

    /*
     * *
     * Read the score db (bws) os_type from the Xml file
     * @return os_type of the work directory
     */

//    /**
//     *
//     * @param programType the value of ramType
//     * @throws BridgePhoneException
//     * @return the java.lang.String
//     */
//
//    public String readScoreDB(int programType) throws BridgePhoneException {
//        String url;
//        try {
//            NodeList groupNodes = getNodeList(fileName, SCORE_DB_TAG);
//            if (groupNodes.getLength() > 0) {
//                Node groupNode = groupNodes.item(0);
//                groupNode.normalize();
//                url = groupNode.getTextContent().trim();
//                Pattern urlPattern = Pattern.compile("\\$LOCALAPPDATA\\\\");
//                Matcher matcher = urlPattern.matcher(url);
//                if (matcher.find()) {
//                    String adaptLocalAppData = localAppData.replaceAll("\\\\", "\\\\\\\\");
//                    url = url.replaceAll("\\$LOCALAPPDATA", adaptLocalAppData);
//                }
//                File scoreDb = new File(url);
//                if (scoreDb.exists()) {
//                    return url;
//                }
//            }
////            }else{
////                if (programType==ProgramTypes.BRIDGEPHONESCORER){
////                    return "";
////                }
////            }
//            String s = "No valid ScoreDB found";
//            Logger.getLogger(TAG + ".readScoreDB").log(Level.SEVERE, s);
//            throw new BridgePhoneException(TAG + ".readScoreDB", s);
//
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            String s = "parser error";
//            Logger.getLogger(TAG + ".readScoreDB").log(Level.SEVERE, s, ex);
//            throw new BridgePhoneException(TAG + ".readScoreDB ", s + ex.getMessage());
//        }
//    }

//    /*
//     * *
//     * Read the score db (bws) os_type from the Xml file
//     * @return os_type of the work directory
//     */
//    public String readMovementLibDir() throws BridgePhoneException {
//        String url=null;
//        try {
//
//            NodeList groupNodes = getNodeList(fileName, MOVEMENTLIB_TAG);
//            if (groupNodes.getLength() > 0) {
//                Node groupNode = groupNodes.item(0);
//                groupNode.normalize();
//                url = groupNode.getTextContent().trim();
//                Pattern urlPattern = Pattern.compile("\\$LOCALAPPDATA\\\\");
//                Matcher matcher = urlPattern.matcher(url);
//                if (matcher.find()) {
//                    String s = matcher.group();
//                    String adaptLocalAppData = localAppData.replaceAll("\\\\", "\\\\\\\\");
//                    url = url.replaceAll("\\$LOCALAPPDATA", adaptLocalAppData);
//
//                }
//
//                File movementLibDir = new File(url);
//                if (movementLibDir.exists()) {
//                    return url;
//                } else {
//                    String adaptLocalAppData = localAppData.replaceAll("\\\\", "\\\\\\\\");
//                    url = adaptLocalAppData + "movementLib";
//                    return url;
//                }
//            }
//            String s = "No valid Movement Library Found found";
//            Logger.getLogger(TAG + ".readScoreDB").log(Level.SEVERE, s);
//            return url;
//
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            String s = "parser error";
//            Logger.getLogger(TAG + ".readMovementLibDir").log(Level.SEVERE, s, ex);
//            return null;
//        }
//    }

    /*
     * *
     * Read the score db (bws) os_type from the Xml file
     * @return os_type of the work directory
     */

//    /**
//     *
//     * @throws BridgePhoneException
//     * @return the java.lang.String
//     */
//
//    public String readMembersDB() throws BridgePhoneException {
//        String url = null;
//        try {
//            NodeList groupNodes = getNodeList(fileName, MEMBERS_DB_TAG);
//            if (groupNodes.getLength() > 0) {
//                Node groupNode = groupNodes.item(0);
//                groupNode.normalize();
//                url = groupNode.getTextContent().trim();
//                Pattern urlPattern = Pattern.compile("\\$LOCALAPPDATA\\\\");
//                Matcher matcher = urlPattern.matcher(url);
//                if (matcher.find()) {
//                    String adaptLocalAppData = localAppData.replaceAll("\\\\", "\\\\\\\\");
//                    url = url.replaceAll("\\$LOCALAPPDATA", adaptLocalAppData);
//                }
//                File memberDb = new File(url);
//                if (!memberDb.isDirectory()) {
//                    if (memberDb.exists()) {
//                        return url;
//                    }
//                }
//            }else{
//                
//            }
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            String s = "parser error";
//            Logger.getLogger(TAG).log(Level.SEVERE, s, ex);
//            throw new BridgePhoneException(TAG + "readMembersDB ", s + ex.getMessage());
//        }
//        return url;
//    }

//    /*
//     * Read the score db (bws) os_type from the Xml file
//     * @return os_type of the work directory
//     */
//    public String readStartRestart() throws BridgePhoneException {
//        String startRestart = RESTART;
//        try {
//            NodeList groupNodes = getNodeList(fileName, START_RESTART_TAG);
//            if (groupNodes.getLength() <= 0) {
//                return startRestart;
//            }
//            Node groupNode = groupNodes.item(0);
//            groupNode.normalize();
//            startRestart = groupNode.getTextContent().trim();
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            String s = "parser error";
//            Logger.getLogger(TAG).log(Level.SEVERE, s, ex);
//            throw new BridgePhoneException(TAG + "readMembersDB ", s + ex.getMessage());
//        }
//        return startRestart;
//    }

//    /*
//     * Read the score db (bws) os_type from the Xml file
//     * @return os_type of the work directory
//     */
//    public String readLanguage() throws BridgePhoneException {
//        Locale locale= Locale.getDefault();       
//        String language = locale.getLanguage().toUpperCase();
//        try {
//            NodeList groupNodes = getNodeList(fileName, LANGUAGE_TAG);
//            if (groupNodes.getLength() <= 0) {
//                return language;
//            }
//            Node groupNode = groupNodes.item(0);
//            groupNode.normalize();
//            language = groupNode.getTextContent().trim().toUpperCase();
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            String s = "parser error";
//            Logger.getLogger(TAG).log(Level.SEVERE, s, ex);
//            throw new BridgePhoneException(TAG + "readLanguage ", s + ex.getMessage());
//        }
//        return language;
//    }
    
//        /*
//     * Read the session Enable setting (ENABLE, DISABLE( (bws)from the Xml file
//     * ifr not set, consider false
//     * @return os_type of the work directory
//     */
//    public boolean readSessionEnable() throws BridgePhoneException {
//        boolean session_enable=false;
//        try {
//
//            NodeList groupNodes = getNodeList(fileName, SESSION_TAG);
//            if (groupNodes.getLength() <= 0) {
//                return session_enable;
//            }
//            Node groupNode = groupNodes.item(0);
//            groupNode.normalize();
//            String session_text = groupNode.getTextContent().trim();
//            if (session_text.equalsIgnoreCase(SESSION_ENABLED)){
//                session_enable= true;
//            }
//
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            String s = "parser error";
//            Logger.getLogger(TAG).log(Level.SEVERE, s, ex);
//            throw new BridgePhoneException(TAG + "readSessionEnable ", s + ex.getMessage());
//        }
//        return session_enable;
//    }

//    /**
//     * *
//     * Read Operation System type
//     *
//     * @return String default is WINDOWS; others LINUX, MAC_OS Store type 
//     */
//    public String readOS() {
//        String os_type = WINDOWS;
//        try {
//            NodeList groupNodes = getNodeList(fileName, OS_TAG);
//            if (groupNodes.getLength() <= 0) {
//            } else {
//                Node groupNode = groupNodes.item(0);
//                groupNode.normalize();
//                os_type = groupNode.getTextContent().trim();
//            }
//
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
//            os_type = WINDOWS;
//        }
//        return os_type;
//    }

//    /**
//     * *
//     * Read Operation System type
//     *
//     * @return String default is WINDOWS; others LINUX, MAC_OS Store type in
//     * os_type
//     */
//    public String readAccessType() {
//        String url = ACCESS96;
//        try {
//            NodeList groupNodes = getNodeList(fileName, ACCESS_TAG);
//            if (groupNodes.getLength() <= 0) {
//                return url;
//            }
//            Node groupNode = groupNodes.item(0);
//            groupNode.normalize();
//            url = groupNode.getTextContent().trim();
//
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
//            url = ACCESS96;
//        }
//        return url;
//    }
    
    
    

    
        
        /*S*
     * *
     * Read the default program type from the Xml file
     *
     * @return programmPath of the work directory
     */
    public String readFilePath(String tag) {
        String url = "";
        try {
            NodeList groupNodes = getNodeList(fileName, tag);
            if (groupNodes.getLength() <= 0) {
                return url;
            }
            Node groupNode = groupNodes.item(0);
            groupNode.normalize();
            url = groupNode.getTextContent().trim();

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOG.fatal (ex);
            String s=" An error occurred reading tag: "+tag+ " "+ex.getMessage();
            JOptionPane.showMessageDialog(null, s);
            url = "";
        }
        return url;
    }
    
    

    private NodeList getNodeList(String fileName, String tag) throws ParserConfigurationException, SAXException, IOException {
        // openn the xml file and determine the work directory 
        Document doc;
        doc = getDoc(fileName);
        NodeList groupNodes = doc.getElementsByTagName(tag);
        return groupNodes;
    }

    private Document getDoc(String fileName) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document docu = db.parse(fileName);
        docu.getDocumentElement().normalize();
        return docu;
    }

//    public XmlRecord readAll() throws BridgePhoneException {
//
//        XmlRecord xmlRecord = new XmlRecord();
//        String workDirUrl = determineWorkDirectory();
//        xmlRecord.setWorkDbdir(workDirUrl);
//        String programTypeString = readDefaultProgramType();
//        int pt =1;// determineprogramType(programTypeString);
//        xmlRecord.setProgramTypeString(programTypeString);
//        xmlRecord.setProgramType(pt);
//        String scoreDb = readScoreDB(pt);
//        xmlRecord.setScoreDB(scoreDb);
//        boolean sessionTableEnabled= readSessionEnable();
//        xmlRecord.setSessionTableEnabled(sessionTableEnabled);
////        String movementLibDir=readMovementLibDir();
////        xmlRecord.setMovementLibDir(movementLibDir);
//        String language=readLanguage();
//        xmlRecord.setLanguage(language);
//        String accessType=readAccessType();
//        xmlRecord.setAccessType(accessType);
//        String startRestart=readStartRestart();
//        xmlRecord.setStartRestart(startRestart);
//        String os=readOS();
//        xmlRecord.setOs(os);
//        return xmlRecord;
//
//    }

//        public static final int NONE = 10;
//    public static final int RUDERSYV = 11;
//    public static final int PAIRSCORER = 12;
//    public static final int NBB = 13;
//    public static final int COWORKER=14;
//    public static final int ACBL = 15;
//    public static final int TOURNOIA = 16;
//    public static final int BRIDGEPHONESCORER = 17;
//    public static final int BRIDGEIT = 18;
//    public static final int GENERICACCESS96 = 19;
//    public static final int GENERICACCESS2000 = 20;
//    public static final int MESSER = 21;
//    private int determineprogramType(String programTypeString) {
//        int programType;
//        switch (programTypeString.toUpperCase()) {
//            case "PAIRSCORER": {
//                programType = ProgramTypes.PAIRSCORER;
//                break;
//            }
//            case "RUDERSYV": {
//                programType = ProgramTypes.RUDERSYV;
//                break;
//            }
//            case "NBB": {
//                programType = ProgramTypes.NBB;
//                break;
//            }
//            case "COWORKER": {
//                programType = ProgramTypes.COWORKER;
//                break;
//            }
//            case "ACBL": {
//                programType = ProgramTypes.ACBL;
//                break;
//            }
//            case "TOURNOIA": {
//                programType = ProgramTypes.TOURNOIA;
//                break;
//            }
//            case "BRIDGEPHONESCORER": {
//                programType = ProgramTypes.BRIDGEPHONESCORER;
//                break;
//            }
//            case "BRIDGEIT": {
//                programType = ProgramTypes.BRIDGEIT;
//                break;
//            }
//            case "GENERICACCESS96": {
//                programType = ProgramTypes.GENERICACCESS96;
//                break;
//            }
//            case "GENERICACCESS2000": {
//                programType = ProgramTypes.GENERICACCESS2000;
//                break;
//            }
//            case "MESSER": {
//                programType = ProgramTypes.MESSER;
//                break;
//            }
//            default: {
//                programType = ProgramTypes.NONE;
//                break;
//            }
//        }
//        return programType;
//    }
    
     public  void wite() throws ParserConfigurationException,
            TransformerException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        
        Element root = doc.createElementNS("zetcode.com", "users");
        doc.appendChild(root);

        root.appendChild(createUser(doc, "1", "Robert", "Brown", "programmer"));
        root.appendChild(createUser(doc, "2", "Pamela", "Kyle", "writer"));
        root.appendChild(createUser(doc, "3", "Peter", "Smith", "teacher"));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transf = transformerFactory.newTransformer();
        
        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");
        transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        DOMSource source = new DOMSource(doc);

        File myFile = new File("src/main/resources/users.xml");
        
        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(myFile);

        transf.transform(source, console);
        transf.transform(source, file);
    }

    private static Node createUser(Document doc, String id, String firstName, 
            String lastName, String occupation) {
        
        Element user = doc.createElement("user");

        user.setAttribute("id", id);
        user.appendChild(createUserElement(doc, "firstname", firstName));
        user.appendChild(createUserElement(doc, "lastname", lastName));
        user.appendChild(createUserElement(doc, "occupation", occupation));

        return user;
    }
    
     private static Node createUserElement(Document doc, String name, 
            String value) {

        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));

        return node;
    }
}
