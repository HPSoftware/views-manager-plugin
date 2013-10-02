package com.hp.jenkins.plugins.alm.manageviews.core;

import com.hp.commons.conversion.JsonSerializer;
import com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers.ViewWrapper;
import net.sf.json.JSON;
import org.kohsuke.stapler.bind.JavaScriptMethod;


/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/10/12
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsProxyTree<T extends ViewWrapper> //Integer> //implements Tree<Integer>
    //interface not implemented because all functions must return a value due to stapler bug in bind.js
    //must state the type explicitly for serialization into ViewWrapper to work... jenkins bug.
    {

    private ViewsTree wrappedTree;
    private JsonSerializer<JSON, ViewsTree> serializer;

    public JsProxyTree(ViewsTree treeToWrap, JsonSerializer<JSON, ViewsTree> serializer) {
        wrappedTree = treeToWrap;
        this.serializer = serializer;
    }

    /*
    //@Override
    public List<Integer> getChildren(Integer data) {
        throw new RuntimeException("getChildren not exposed to ajax.");
    }

    //@Override
    public Integer getParent(Integer data) {
        throw new RuntimeException("getParent not exposed to ajax.");
    }

    @Override
    public Integer getRoot() {
        throw new RuntimeException("getRoot not exposed to ajax");
    }
    */



    @JavaScriptMethod
    //@Override
    public boolean add(T parentNode, T dataToAdd) {

        wrappedTree.add(parentNode, dataToAdd);
        return true;
    }

    @JavaScriptMethod
    //@Override
    public boolean move(T dataSubtreeToMove, T newParent) {

        wrappedTree.move(dataSubtreeToMove, newParent);
        return true;
    }

    @JavaScriptMethod
    //@Override
    public boolean clone(T dataSubtreeToClone, T newParent) {

        wrappedTree.clone(dataSubtreeToClone, newParent);
        return true;
    }

    @JavaScriptMethod
    //@Override
    public boolean delete(T subtreeToDelete) {

        wrappedTree.delete(subtreeToDelete);
        return true;
    }

    @JavaScriptMethod
    public boolean rename(T viewToRename, String newName) {

        wrappedTree.rename(viewToRename, newName);
        return true;
    }

    /*JSONArray.fromObject(
        "[\n" +
        "        {\n" +
        "            //name to display\n" +
        "            \"data\" : \"Jenkins\",\n" +
        "\n" +
        "            //attributes\n" +
        "            \"attr\" : {\n" +
        "\n" +
        "                //type attribute\n" +
        "                \"rel\" : \"Jenkins\"\n" +
        "            },\n" +
        "\n" +
        "            //child nodes\n" +
        "            \"children\" : [\n" +
        "\n" +
        "                {   \"data\" : \"job\",\n" +
        "                    \"attr\" : {\n" +
        "                        \"rel\": \"Job\"\n" +
        "                    }\n" +
        "\n" +
        "                },\n" +
        "                {   \"data\" : \"list\",\n" +
        "                    \"attr\" : {\n" +
        "                        \"rel\": \"ListView\"\n" +
        "                    }\n" +
        "\n" +
        "                },\n" +
        "                {   \"data\" : \"nested\",\n" +
        "                    \"attr\" : {\n" +
        "                        \"rel\": \"NestedView\"\n" +
        "                    }\n" +
        "                }\n" +
        "            ]\n" +
        "        }\n" +
        "    ];");
        */
    @JavaScriptMethod
    public JSON tree() {

        final JSON json = this.serializer.toJson(this.wrappedTree);
        //System.out.println(json.toString(3));
        return json;
    }

}
