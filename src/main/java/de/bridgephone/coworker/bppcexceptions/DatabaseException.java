/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bridgephone.coworker.bppcexceptions;



/**
 *
 * @author kees
 */
public class DatabaseException extends BridgePhoneException{
    
    public DatabaseException(String moduleName, String message) {
        super(moduleName, message);
    }

    public DatabaseException(String moduleName,  String message, int code, Exception ex) {
        super(moduleName, message, code, ex);
    }
}
