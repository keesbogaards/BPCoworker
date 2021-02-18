package de.bridgephone.coworker.bppcexceptions;

import java.util.logging.Level;


/**
 *
 * @author kees
 */
public class BridgePhoneException extends Exception {
     private static final String TAG= "BridgePhoneException";
    String moduleName;
    int errorCode = -1;

    public BridgePhoneException(String moduleName, int errorCode) {
        super();
        this.errorCode = errorCode;
        this.moduleName = moduleName;
        logException(null, errorCode, null);
    }

    public BridgePhoneException(String moduleName, int errorCode, Throwable rootCause) {
        super(rootCause);
        this.errorCode = errorCode;
        this.moduleName = moduleName;
        logException(null, errorCode, rootCause);
    }

    public BridgePhoneException(String moduleName, String message) {
        super(message, null);
        this.moduleName = moduleName;
        logException(message, errorCode, null);
    }

    public BridgePhoneException(String moduleName, String message, int errorCode) {
        super(message, null);
        this.errorCode = errorCode;
        this.moduleName = moduleName;
        logException(message, errorCode, null);
    }

    public BridgePhoneException(String moduleName, String message, Throwable rootCause) {
        super(message, rootCause);
        this.moduleName = moduleName;
        logException(message, errorCode, rootCause);
    }

    public BridgePhoneException(String moduleName, String message, int errorCode, Throwable rootCause) {
        super(message, rootCause);
        this.errorCode = errorCode;
        this.moduleName = moduleName;
        logException(message, errorCode, rootCause);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getModuleName() {
        return moduleName;
    }

     @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder("Module: ");
        sb.append(moduleName);
        if (errorCode >= 0) {
            sb.append(" error: ");
            sb.append(errorCode);
        }
        sb.append(" ");
        String s= super.getMessage();
        sb.append(s==null?"*":s);

        return sb.toString();
    }

    private void logException(String message, int errorCode, Throwable rootCause) {
        StringBuilder sb = new StringBuilder(moduleName);
        sb.append(" ");
        if (message != null) {
            sb.append(message);
            sb.append(" ");
        }
        sb.append(errorCode);
        sb.append("\n");
        sb.append("Rootcause ");
        if (rootCause != null) {
            String s= rootCause.getMessage()==null?"+":rootCause.getMessage();
            sb.append(s);
            sb.append("\n");
        }
        
        java.util.logging.Logger.getLogger(TAG).log(Level.SEVERE, sb.toString());

    }
}
