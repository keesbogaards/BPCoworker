package de.bridgephone.coworker.xmlconfiguration;

import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class XmlFileHandlingTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }
    @org.junit.jupiter.api.Test
    void prepareXmlFileTest() {
        XmlFileHandling instance= new XmlFileHandling();
        String xmlDirUrl=instance.getWorkDir(true);

        XmlValueRecord xmlValueRecord= new XmlValueRecord();

        instance.prepareXmlFile(xmlValueRecord,xmlDirUrl);
        String xmlFileDirPathExpected="c:\\users\\gebrui~1\\appdata\\local\\bridgephoneharmonytest";
        Assertions.assertEquals(xmlValueRecord.getXmlDirUrl(),xmlFileDirPathExpected);
        ArrayList<CheckResult> crList=xmlValueRecord.getCr();
        int crListSizeExpected=3;
        Assertions.assertEquals(crList.size(),crListSizeExpected);
        String errorMessageExpected="not initialized";
        for (int i=0;i<3;i++){
            Assertions.assertEquals(crList.get(i).getErrorMessage(),errorMessageExpected);
        }
        boolean resultExpected=false;
        for (int i=0;i<3;i++){
            Assertions.assertEquals(crList.get(i).isResult(),resultExpected);
        }
        File fileExpected= new File("not initialized");
        for (int i=0;i<3;i++){
            Assertions.assertEquals(crList.get(i).getFile(),fileExpected);
        }
    }

    @org.junit.jupiter.api.Test
    void getWorkDirTest() {
        XmlFileHandling instance= new XmlFileHandling();
        String appDataDir="c:\\users\\gebrui~1\\appdata\\local\\bridgephoneharmonytest\\";
        String xmlDirUrl=instance.getWorkDir(true);
        Assert.isNonEmpty(xmlDirUrl);
        Assertions.assertEquals(appDataDir,xmlDirUrl);
    }

    @org.junit.jupiter.api.Test
    void copyFilesFromHomeToWorkDirTest() {
        XmlFileHandling instance= new XmlFileHandling();
        String xmlDirUrl=instance.getWorkDir(true);
        int expectedNrFiles=3;
        int i=    instance.copyFilesFromHomeToWorkDir(xmlDirUrl);
        Assertions.assertEquals(i,expectedNrFiles);


    }

}