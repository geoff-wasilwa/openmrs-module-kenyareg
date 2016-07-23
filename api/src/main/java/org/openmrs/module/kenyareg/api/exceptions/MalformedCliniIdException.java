package org.openmrs.module.kenyareg.api.exceptions;

/**
 *
 * @author Gitahi Ng'ang'a
 */
public class MalformedCliniIdException extends Exception {

    /**
     * Creates a new instance of <code>MalformedCliniIdException</code> without detail message.
     */
    public MalformedCliniIdException() {
        super("The Clinic ID you entered is in the wrong format. Please use the "
                + "format '12345-00001' for Universal Clinic IDs and '00001/2005' "
                + "for Local Clinic IDs");
    }

    /**
     * Constructs an instance of <code>MalformedCliniIdException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MalformedCliniIdException(String msg) {
        super(msg);
    }
}
