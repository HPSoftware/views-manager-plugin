package com.hp.jenkins.plugins.alm.manageviews.core;

import com.hp.commons.conversion.JsonSerializer;
import com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers.ViewWrapper;
import net.sf.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/14/12
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class ViewWrapperToJstreeCompatibleJsonSerializer implements JsonSerializer<JSONObject, ViewWrapper> {

    @Override
    public JSONObject toJson(ViewWrapper serializationSource) {

        JSONObject ret = new JSONObject();

        ret.put("data", serializationSource.getName());

        JSONObject attributes = new JSONObject();
        attributes.put("rel", serializationSource.getViewType());
        attributes.put("id", "tid_" + serializationSource.getTreeId() + "_nid_" + serializationSource.getId());
        ret.put("attr", attributes);

        JSONObject metadata = new JSONObject();
        metadata.put("tree_id", serializationSource.getTreeId());
        metadata.put("node_id", serializationSource.getId());
        ret.put("metadata", metadata);

        return ret;
    }
}
