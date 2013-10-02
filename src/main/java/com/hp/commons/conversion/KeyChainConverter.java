package com.hp.commons.conversion;

import com.hp.commons.core.collection.keymapped.KeyedFactory;
import com.hp.commons.conversion.exceptions.ConversionFailure;
import com.hp.commons.conversion.exceptions.KeyedFactoryExpected;
import com.hp.commons.conversion.exceptions.WrongConversionInputType;
import net.sf.json.JSONArray;
import org.apache.commons.beanutils.Converter;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class KeyChainConverter implements Converter {

    private KeyedFactory factoryChain;

    public KeyChainConverter(KeyedFactory factoryChain) {
        this.factoryChain = factoryChain;
    }

    @Override
    public Object convert(Class type, Object value) {
        /*
        if (value instanceof JSONObject) {

            JSONObject obj = (JSONObject)value;
            final Object keychain = obj.get("keychain");

            if (keychain == null) {
                throw new ValueExpected("keychain");
            }
            else if (keychain instanceof JSONArray) {
                return convert(type, keychain);
            }
            else {
                throw new WrongConversionInputType(keychain, JSONArray.class);
            }
        }

        throw new WrongConversionInputType(value, JSONObject.class);
        */
        if (value instanceof String) {
            value = ((String) value).substring(1, ((String) value).length()-1);
            JSONArray keychain = JSONArray.fromObject(value);
            return convert(type,keychain);
        }
        throw new WrongConversionInputType(value, String.class);
    }

    public Object convert(Class type, JSONArray keys) {

        KeyedFactory factory = factoryChain;

        final int lastKeyIndex = keys.size() - 1;
        for (int keyIndex = 0 ; keyIndex < lastKeyIndex; keyIndex++) {

            Object key = keys.get(keyIndex);
            Object potentialFactory = factory.get(key);

            if (potentialFactory instanceof KeyedFactory) {
                factory = (KeyedFactory)potentialFactory;
            }
            else {
                throw new KeyedFactoryExpected(keyIndex, potentialFactory);
            }
        }

        Object result = factory.get(keys.get(lastKeyIndex));

        if (!type.isAssignableFrom(result.getClass())) {
            throw new ConversionFailure(result, type);
        }

        return result;
    }
}
