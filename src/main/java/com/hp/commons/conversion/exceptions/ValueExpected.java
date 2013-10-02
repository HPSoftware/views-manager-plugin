package com.hp.commons.conversion.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/14/12
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValueExpected extends ConversionException {

    public ValueExpected(String varName) {
        super("Expected to find a value for '" + varName + "', but there is none, or it is null.");
    }
}
