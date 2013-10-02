package com.hp.commons.conversion.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/10/12
 * Time: 12:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConversionFailure extends ConversionException {

    public ConversionFailure(Object value, Class type) {
        super("failed to convert '" + value + "' of type '" + value.getClass() + "' to type '" + type.getClass() + "'");
    }
}
