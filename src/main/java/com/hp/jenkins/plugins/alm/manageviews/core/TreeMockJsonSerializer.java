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
public class TreeMockJsonSerializer<T> implements JsonSerializer<JSON, Tree<T>> {


    private void dfsJsonify(Tree<T> element, T treeRoot, JSONObject root) {

        root.put("data", "" + treeRoot);

        JSONObject attributes = new JSONObject();
        attributes.put("rel", "NestedView");
        attributes.put("id", treeRoot);
        root.put("attr", attributes);

        final List<T> treeChildren = element.getChildren(treeRoot);
        final JSONArray children = new JSONArray();
        for (T treeChild: treeChildren) {

            JSONObject child = new JSONObject();
            dfsJsonify(element, treeChild, child);
            children.add(child);
        }

        root.put("children", children);
    }


    @Override
    public JSON toJson(Tree<T> element) {

        final JSONObject ret = new JSONObject();

        final JSONArray wrapper = new JSONArray();
        final JSONObject root = new JSONObject();

        final T treeRoot = element.getRoot();

        dfsJsonify(element, treeRoot, root);

        wrapper.add(root);
        ret.put("data", wrapper);

        return ret;
    }
}
