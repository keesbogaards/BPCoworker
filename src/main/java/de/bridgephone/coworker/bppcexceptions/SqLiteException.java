/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.bridgephone.coworker.bppcexceptions;



/**
 *
 * @author kees
 */

public class SqLiteException extends BridgePhoneException {

   public SqLiteException(String moduleName, int errorCode) {
        super(moduleName, errorCode);
   }

    public SqLiteException(String moduleName, int errorCode, Throwable rootCause) {
        super(moduleName, errorCode, rootCause);
    }

    public SqLiteException(String moduleName, String message) {
        super(moduleName, message);
    }

    public SqLiteException(String moduleName, String message, int errorCode) {
        super(moduleName, message, errorCode);
    }

    public SqLiteException(String moduleName, String message, Throwable rootCause) {
        super(moduleName, message, rootCause);
    }

    public SqLiteException(String moduleName, String message, int errorCode, Throwable cause) {
        super(moduleName, message, errorCode, cause);
    }
}

