package com.hp.jenkins.plugins.alm.manageviews.core;

import com.hp.commons.core.collection.keymapped.KeyedFactory;
import com.hp.commons.core.collection.keymapped.KeyMappedSet;
import com.hp.commons.core.collection.keymapped.KeyMappedSetImpl;
import com.hp.commons.conversion.KeyChainConverter;
import com.hp.commons.core.tree.Tree;
import com.hp.commons.core.tree.node.NodeDelegatingTree;
import com.hp.commons.core.unique.SequentialIntegerKeysFactory;
import com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers.*;
import com.hp.jenkins.plugins.alm.manageviews.exceptions.UnsupportedViewsManagerNodeType;
import com.hp.jenkins.plugins.alm.manageviews.exceptions.ViewsManagerException;
import hudson.model.AllView;
import hudson.model.ListView;
import hudson.model.View;
import hudson.model.ViewGroup;
import hudson.plugins.nested_view.NestedView;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.Stapler;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/12/12
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class ViewsTree implements Tree<ViewWrapper>,
        KeyedFactory<Integer, ViewWrapper> {

    /*
    * static class fields
    * */

    private static KeyMappedSet<Integer, ViewsTree> treeMappedSet =
            new KeyMappedSetImpl<Integer, ViewsTree>(
                    new SequentialIntegerKeysFactory<ViewsTree>(0));
    static {
        Stapler.CONVERT_UTILS.register(
                new KeyChainConverter(treeMappedSet),
                ViewWrapper.class);
    }

    /*
    * instance fields
    * */

    private NodeDelegatingTree<ViewWrapper> tree;
    private Integer treeId;

    //object because can contain an instance of Jenkins, which is not a view...
    private KeyMappedSet<Integer, Object> nodeMappedSet;
    private ViewWrapperFactory viewWrapperFactory;


    public ViewsTree(View v) {

        this((Object) v);
    }

    public ViewsTree(ViewGroup vg) {

        this((Object) vg);
    }

    private ViewsTree(Object v) {

        this.nodeMappedSet =
                new KeyMappedSetImpl<Integer, Object>(
                        new SequentialIntegerKeysFactory<Object>(0));

        treeId = treeMappedSet.insert(this);
        this.viewWrapperFactory = new ViewWrapperFactory();

        final ViewWrapper root = viewWrapperFactory.newViewWrapper(v);

        this.tree = new NodeDelegatingTree<ViewWrapper>(root);
    }

    /*
    * tree ops
    * */

    @Override
    public ViewWrapper getRoot() {

        return this.tree.getRoot();
    }

    @Override
    public List<ViewWrapper> getChildren(ViewWrapper data) {

        return this.tree.getChildren(data);
    }

    @Override
    public ViewWrapper getParent(ViewWrapper data) {

        return this.tree.getParent(data);
    }

    @Override
    public void add(ViewWrapper parentNode, ViewWrapper dataToAdd) {

        this.tree.add(parentNode, dataToAdd);

        save();
    }

    @Override
    public void move(ViewWrapper dataSubtreeToMove, ViewWrapper newParent) {

        this.tree.move(dataSubtreeToMove, newParent);

        save();
    }

    @Override
    public void clone(ViewWrapper dataSubtreeToClone, ViewWrapper newParent) {

        this.tree.clone(dataSubtreeToClone, newParent);

        save();
    }

    @Override
    public void delete(ViewWrapper subtreeToDelete) {

        this.tree.delete(subtreeToDelete);

        //if it already existed, we get it's existing key.. otherwise, add and remove is acceptable
        this.nodeMappedSet.remove(this.nodeMappedSet.insert(subtreeToDelete.getWrapped()));

        save();
    }

    public void rename(ViewWrapper viewToRename, String newName) {

        viewToRename.setName(newName);

        save();
    }

    private void save() {
        try {
        Jenkins.getInstance().save();
        }
        catch (Exception ioe) {
            throw new ViewsManagerException("failed to persist jenkins configuration of views.", ioe);
        }
    }

    /*
    * key factory ops
    * */
    @Override
    public ViewWrapper get(Integer key) {
        return viewWrapperFactory.newViewWrapper(this.nodeMappedSet.get(key));
    }


    public class ViewWrapperFactory {

        public ViewWrapper newViewWrapper(Object v) {

            final Integer nodeKey = ViewsTree.this.nodeMappedSet.insert(v);

            final AbsWrapper ret = _newViewWrapper(v);
            ret.setId(nodeKey);
            ret.setTreeId(ViewsTree.this.treeId);

            return ret;
        }

        /*
        * make this extensible, with a map that takes the View implementation and bind it to a handler,
        * also after initializaiton if there exists a view implementation that is not a key in the map, fail startup.
        * */
        private AbsWrapper _newViewWrapper(Object v) {

            //we check for a specific instance, not "instanceof" because for example dashboard extends listview...
            if (v.getClass().equals(NestedView.class)) {
                return new NestedViewWrapper((NestedView)v, this);
            }

            if (v == Jenkins.getInstance()) {
                return new JenkinsWrapper(this);
            }

            else if (v.getClass().equals(ListView.class)) {
                return new ListViewWrapper((ListView)v, this);
            }

            else if (v.getClass().equals(AllView.class)) {
                return new AllViewWrapper((AllView)v, this);
            }

            else if (v instanceof View) {
                return new GenericViewWrapper((View)v, this);
            }

            else {
                throw new UnsupportedViewsManagerNodeType(v);
            }
        }

    }



}
