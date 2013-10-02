package com.hp.commons.conversion.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/10/12
 * Time: 12:34 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ConversionException extends RuntimeException {

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConversionException(Throwable cause) {
        super(cause);
    }
}
