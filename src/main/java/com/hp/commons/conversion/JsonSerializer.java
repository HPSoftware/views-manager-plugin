package com.hp.commons.conversion;

import net.sf.json.JSON;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/14/12
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public interface JsonSerializer<R extends JSON ,T> {

    public R toJson(T serializationSource);
}
