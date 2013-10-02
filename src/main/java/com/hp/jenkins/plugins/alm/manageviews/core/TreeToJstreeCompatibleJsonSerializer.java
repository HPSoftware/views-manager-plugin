package com.hp.jenkins.plugins.alm.manageviews.core;

import com.hp.commons.conversion.JsonSerializer;
import com.hp.commons.core.tree.Tree;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/14/12
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class TreeToJstreeCompatibleJsonSerializer<D,T extends Tree<D>> implements JsonSerializer<JSON, T> {

    private JsonSerializer<JSONObject, D> dataSerializer;

    public TreeToJstreeCompatibleJsonSerializer(
            JsonSerializer<JSONObject, D> dataSerializer) {

        this.dataSerializer = dataSerializer;
    }

    private JSON dfsJsonify(T element, D node) {

        JSONObject root = dataSerializer.toJson(node);

        final List<D> treeChildren = element.getChildren(node);
        final JSONArray children = new JSONArray();
        for (D treeChild: treeChildren) {

            JSON child = dfsJsonify(element, treeChild);
            children.add(child);
        }

        root.put("children", children);

        return root;
    }


    @Override
    public JSON toJson(T serializationSource) {

        final JSONObject ret = new JSONObject();

        final JSONArray wrapper = new JSONArray();

        final D treeRoot = serializationSource.getRoot();

        wrapper.add(dfsJsonify(serializationSource, treeRoot));

        ret.put("data", wrapper);

        return ret;
    }
}
