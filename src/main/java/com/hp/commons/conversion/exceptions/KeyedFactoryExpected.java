package com.hp.commons.conversion.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class KeyedFactoryExpected extends ConversionException {

    public KeyedFactoryExpected(int index, Object potentialFactory) {

        super("the " + index + " key chain result was expected to have been a KeyedFactory, " +
                "but was actually " + potentialFactory.getClass().getSimpleName() + ": " + potentialFactory);
    }
}
