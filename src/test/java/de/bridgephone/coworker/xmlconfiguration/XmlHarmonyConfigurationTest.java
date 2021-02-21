package de.bridgephone.coworker.xmlconfiguration;

import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;


class XmlHarmonyConfigurationTest {
    private static  boolean test;

    @BeforeEach
    void setUp() {
        test=true;
    }

    @AfterEach
    void tearDown() {
    }





    @Test
    void checkOutXmlRecordTest() {
        XmlHarmonyConfiguration instance= new XmlHarmonyConfiguration(test);
        String workdir=instance.defineWorkdir();
        instance.initialize(workdir);
        String s=instance.checkOutXmlRecord();
        Assertions.assertTrue(s.isEmpty());

    }



    @Test
    void checkXmlRecordsOnViability() {
        XmlHarmonyConfiguration instance= new XmlHarmonyConfiguration(test);
        String workdir=instance.defineWorkdir();
        instance.initialize(workdir);
        String s=instance.checkOutXmlRecord();
        boolean b;
        if (s.isEmpty()){
            b=instance.checkXmlRecordsOnViability();
        }else{
            Assert.isEmpty(s);
            b=false;
        }
        Assertions.assertTrue(b);

    }

    @Test
    void checkViabilityScoringProgram() {
        XmlHarmonyConfiguration instance= new XmlHarmonyConfiguration(test);
        String workdir=instance.defineWorkdir();
        instance.initialize(workdir);
        String s=instance.checkOutXmlRecord();
        Assertions.assertTrue(s.isEmpty());
        boolean result=instance.checkXmlRecordsOnViability();
        Assertions.assertTrue(result);


        instance.checkViabilityScoringProgram();
        XmlValueRecord xmlValueRecord=instance.getXmlRecord();
        CheckResult cr=xmlValueRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM);
        Assertions.assertTrue(cr.isResult());
        Assertions.assertTrue(cr.getErrorMessage().isEmpty());


        File f=new File("");
        xmlValueRecord=instance.getXmlRecord();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM);
        cr.setFile(f);
        instance.checkViabilityScoringProgram();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM);
        Assertions.assertFalse(cr.isResult());
        String expectedErrorMessage="File  does not exist";
        Assertions.assertEquals(cr.getErrorMessage(),expectedErrorMessage);

        CheckResult crhelp=xmlValueRecord.getCr().get(XmlValueRecord.BWSDIR);
        File fHelp= crhelp.getFile();//an existing file but not a exe
        xmlValueRecord=instance.getXmlRecord();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM);
        cr.setFile(fHelp);
        instance.checkViabilityScoringProgram();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.SCORINGPROGRAM);
        Assertions.assertFalse(cr.isResult());
        String partialExpectedErrorMessage="is not executable";
        boolean b=cr.getErrorMessage().endsWith(partialExpectedErrorMessage);
        Assertions.assertTrue(b);

    }

    @Test
    void checkViabilityBwsDir() {

        XmlHarmonyConfiguration instance= new XmlHarmonyConfiguration(test);
        String workdir=instance.defineWorkdir();
        instance.initialize(workdir);
        String s=instance.checkOutXmlRecord();
        Assertions.assertTrue(s.isEmpty());


        instance.checkViabilityBwsDir();
        XmlValueRecord xmlValueRecord=instance.getXmlRecord();
        CheckResult cr=xmlValueRecord.getCr().get(XmlValueRecord.BWSDIR);
        Assertions.assertTrue(cr.isResult());
        Assertions.assertTrue(cr.getErrorMessage().isEmpty());


        File f=new File("");
        xmlValueRecord=instance.getXmlRecord();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.BWSDIR);
        cr.setFile(f);
        instance.checkViabilityBwsDir();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.BWSDIR);
        Assertions.assertFalse(cr.isResult());
        String expectedErrorMessage="File  does not exist";
        Assertions.assertEquals(cr.getErrorMessage(),expectedErrorMessage);

        CheckResult crhelp=xmlValueRecord.getCr().get(XmlValueRecord.PCBRIDGEPHONEPROGRAM);
        File fHelp= crhelp.getFile();//an existing file but not a bws
        xmlValueRecord=instance.getXmlRecord();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.BWSDIR);
        cr.setFile(fHelp);
        instance.checkViabilityBwsDir();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.BWSDIR);
        Assertions.assertFalse(cr.isResult());
        String partialExpectedErrorMessage="does not end with .bws";
        boolean b= cr.getErrorMessage().endsWith(partialExpectedErrorMessage);
        Assertions.assertTrue(b);


    }

    @Test
    void checkViabilityPCBridgePhoneProgram() {
        XmlHarmonyConfiguration instance= new XmlHarmonyConfiguration(test);
        String workdir=instance.defineWorkdir();
        instance.initialize(workdir);
        String s=instance.checkOutXmlRecord();
        Assertions.assertTrue(s.isEmpty());


        instance.checkViabilityPCBridgePhoneProgram();
        XmlValueRecord xmlValueRecord=instance.getXmlRecord();
        CheckResult cr=xmlValueRecord.getCr().get(XmlValueRecord.PCBRIDGEPHONEPROGRAM);
        Assertions.assertTrue(cr.isResult());
        Assertions.assertTrue(cr.getErrorMessage().isEmpty());


        File f=new File("");
        xmlValueRecord=instance.getXmlRecord();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.PCBRIDGEPHONEPROGRAM);
        cr.setFile(f);
        instance.checkViabilityPCBridgePhoneProgram();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.PCBRIDGEPHONEPROGRAM);
        Assertions.assertFalse(cr.isResult());
        String expectedErrorMessage="File  does not exist";
        Assertions.assertEquals(cr.getErrorMessage(),expectedErrorMessage);

        CheckResult crhelp=xmlValueRecord.getCr().get(XmlValueRecord.BWSDIR);
        File fHelp= crhelp.getFile();//an existing file but not a exe
        xmlValueRecord=instance.getXmlRecord();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.PCBRIDGEPHONEPROGRAM);
        cr.setFile(fHelp);
        instance.checkViabilityPCBridgePhoneProgram();
        cr=xmlValueRecord.getCr().get(XmlValueRecord.PCBRIDGEPHONEPROGRAM);
        Assertions.assertFalse(cr.isResult());
        String errorMessage=cr.getErrorMessage();
        Assertions.assertFalse(errorMessage.isEmpty());
    }







    @Test
    void setLocale() {
    }

    @Test
    void getCurrentLocale() {
    }
}