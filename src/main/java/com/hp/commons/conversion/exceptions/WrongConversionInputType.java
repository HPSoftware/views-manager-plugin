package com.hp.commons.conversion.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class WrongConversionInputType extends ConversionException {

    public WrongConversionInputType(Object value, Class<?> c) {

        super("converter expected input value " + value + " to be of type " + c +
                ", but was " + value.getClass().getSimpleName());
    }
}
