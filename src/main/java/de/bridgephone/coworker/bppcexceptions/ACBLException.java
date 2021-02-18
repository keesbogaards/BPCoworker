/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.bridgephone.coworker.bppcexceptions;



/**
 *
 * @author kees bogaards met nog iets
 */

public class ACBLException extends BridgePhoneException {

   public ACBLException(String moduleName, int errorCode) {
        super(moduleName, errorCode);
   }

    public ACBLException(String moduleName, int errorCode, Throwable rootCause) {
        super(moduleName, errorCode, rootCause);
    }

    public ACBLException(String moduleName, String message) {
        super(moduleName, message);
    }

    public ACBLException(String moduleName, String message, int errorCode) {
        super(moduleName, message, errorCode);
    }

    public ACBLException(String moduleName, String message, Throwable rootCause) {
        super(moduleName, message, rootCause);
    }

    public ACBLException(String moduleName, String message, int errorCode, Throwable cause) {
        super(moduleName, message, errorCode, cause);
    }
}

