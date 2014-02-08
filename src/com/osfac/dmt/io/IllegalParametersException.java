package com.osfac.dmt.io;

/**
 * Simple class to report missing/bad {@link DriverProperties} problems.
 */
public class IllegalParametersException extends java.lang.Exception {

    /**
     * Creates new
     * <code>IllegleParametersException</code> without detail message.
     */
    public IllegalParametersException() {
    }

    /**
     * Constructs an
     * <code>IllegleParametersException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public IllegalParametersException(String msg) {
        super(msg);
    }
}
