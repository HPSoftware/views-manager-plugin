package com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers;

import com.hp.jenkins.plugins.alm.manageviews.core.ViewsTree;
import hudson.model.AllView;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/14/12
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class AllViewWrapper<NI,TI> extends AbsViewWrapper<NI, TI, AllView> {

    public AllViewWrapper(AllView v, ViewsTree.ViewWrapperFactory viewWrapperFactory) {
        super(v, viewWrapperFactory);
    }

    /*
    @Override
    protected void _move(ViewGroupWrapper newParent) {
        throw new NotMovable(getWrapped(), newParent.getWrapped());
    }

    @Override
    protected void _clone(ViewGroupWrapper newParent) {
        throw new NotCloneable(getWrapped(), newParent.getWrapped());
    }

    @Override
    public void delete() {
        throw new NotDeletable(getWrapped());
    }

    @Override
    public void setName(String newName) {
        throw new NameImmutable(getWrapped(), getName(), newName);
    }
    */
}
