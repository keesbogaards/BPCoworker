package de.bridgephone.coworker.bppcexceptions;

/**
 *
 * @author kees
 */

public class CommunicationException extends BridgePhoneException {

   public CommunicationException(String moduleName, int errorCode) {
        super(moduleName, errorCode);
   }

    public CommunicationException(String moduleName, int errorCode, Throwable rootCause) {
        super(moduleName, errorCode, rootCause);
    }

    public CommunicationException(String moduleName, String message) {
        super(moduleName, message);
    }

    public CommunicationException(String moduleName, String message, int errorCode) {
        super(moduleName, message, errorCode);
    }

    public CommunicationException(String moduleName, String message, Throwable rootCause) {
        super(moduleName, message, rootCause);
    }

    public CommunicationException(String moduleName, String message, int errorCode, Throwable cause) {
        super(moduleName, message, errorCode, cause);
    }
}
