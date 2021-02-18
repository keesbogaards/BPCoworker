/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bridgephone.coworker.xmlconfiguration;

import java.io.File;
//import java.util.ResourceBundle;

/**
 *
 * @author Kees
 */
/**
 * *
 * result of validation of xmlValue
 */
public class CheckResult {
    
    public final static boolean OK=true;
        public final static boolean NOK=false;

    private boolean result = true;
    private File fileName = null;

    private String errorMessage = "";
//    private final ResourceBundle bundle;

//    public CheckResult() {
////        bundle = java.util.ResourceBundle.getBundle("de/bridgephone/bridgephoneharmony/resources/BridgePhoneHarmonyConfigurator");
//    }

    /**
     * @return the ok
     */
    public boolean isResult() {
        return result;
    }

    /**
     * @param ok the ok to set
     */
    public void setResult(boolean ok) {
        this.result = ok;
    }

    /**
     * @return the name
     */
    public File getFile() {
        return fileName;
    }

    /**
     * @param name the name to set
     */
    public void setFile(File name) {
        this.fileName = name;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
