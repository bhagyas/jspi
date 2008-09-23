/*
 * Created on 10.09.2004
 *
 * 
 */
package de.lohndirekt.print.exception;

/**
 * @author sefftinge
 *
 * 
 */
public class AuthenticationException extends RuntimeException {

    /**
     * 
     */
    public AuthenticationException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public AuthenticationException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public AuthenticationException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
