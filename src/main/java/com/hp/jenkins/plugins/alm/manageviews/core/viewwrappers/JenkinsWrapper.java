package com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers;

import com.hp.commons.core.tree.exceptions.NotCloneable;
import com.hp.commons.core.tree.exceptions.NotDeletable;
import com.hp.commons.core.tree.exceptions.NotMovable;
import com.hp.commons.core.tree.exceptions.RootHasNoParent;
import com.hp.jenkins.plugins.alm.manageviews.core.ViewsTree;
import com.hp.jenkins.plugins.alm.manageviews.exceptions.NameImmutable;
import hudson.model.ViewGroup;
import jenkins.model.Jenkins;

public class JenkinsWrapper<NI,TI> extends ViewGroupWrapper<NI,TI,ViewGroup> {

    public JenkinsWrapper(ViewsTree.ViewWrapperFactory wrapperFactory) {

        super(Jenkins.getInstance(), wrapperFactory);
    }

    @Override
    protected void _move(ViewGroupWrapper newParent) {

        throw new NotMovable(getWrapped(), (ViewGroup)newParent.getWrapped());
    }

    @Override
    protected void _clone(ViewGroupWrapper newParent) {

        throw new NotCloneable(getWrapped(), newParent);
    }


    @Override
    protected ViewGroup _getParent() {
        throw new RootHasNoParent(getWrapped());
    }


    @Override
    public void delete() {

        throw new NotDeletable(getWrapped());
    }

    @Override
    public String getName() {
        return "Jenkins";
    }

    @Override
    public void setName(String newName) {
        throw new NameImmutable(getWrapped(), getName(), newName);
    }

    @Override
    public String getViewType() {
        return "Jenkins";
    }
}