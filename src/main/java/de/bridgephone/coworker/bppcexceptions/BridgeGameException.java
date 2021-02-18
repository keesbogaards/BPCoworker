package de.bridgephone.coworker.bppcexceptions;

/**
 *
 * @author Kees Bogaards Blue from server
 *
 */
public class BridgeGameException extends BridgePhoneException {

   public BridgeGameException(String moduleName, int errorCode) {
        super(moduleName, errorCode);
   }

    public BridgeGameException(String moduleName, int errorCode, Throwable rootCause) {
        super(moduleName, errorCode, rootCause);
    }

    public BridgeGameException(String moduleName, String message) {
        super(moduleName, message);
    }

    public BridgeGameException(String moduleName, String message, int errorCode) {
        super(moduleName, message, errorCode);
    }

    public BridgeGameException(String moduleName, String message, Throwable rootCause) {
        super(moduleName, message, rootCause);
    }

    public BridgeGameException(String moduleName, String message, int errorCode, Throwable cause) {
        super(moduleName, message, errorCode, cause);
    }
}
