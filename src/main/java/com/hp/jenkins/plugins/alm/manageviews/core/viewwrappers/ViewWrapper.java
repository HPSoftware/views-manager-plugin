package com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers;

import com.hp.commons.core.tree.node.TreeNode;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/13/12
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ViewWrapper extends TreeNode<ViewWrapper> {

    public Object getWrapped();

    @Override
    public ViewGroupWrapper getParent();

    String getName();

    public void setName(String newName);

    Object getId();

    Object getTreeId();

    String getViewType();
}
