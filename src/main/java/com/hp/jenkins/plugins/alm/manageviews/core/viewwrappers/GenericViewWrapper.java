package com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers;

import com.hp.jenkins.plugins.alm.manageviews.core.ViewsTree;
import hudson.model.View;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/15/12
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class GenericViewWrapper<NI,TI> extends AbsViewWrapper<NI,TI,View> {

    public GenericViewWrapper(View v, ViewsTree.ViewWrapperFactory viewWrapperFactory) {
        super(v, viewWrapperFactory);
    }

    /*
    @Override
    protected void _move(ViewGroupWrapper newParent) {
        throw new NotMovable("The view type " + getWrapped().getClass().getSimpleName() +
                " is not known by the views manager plugin, and moving it may be dangerous.",
                getWrapped(), newParent.getWrapped());
    }

    @Override
    public void delete() {
        throw new NotDeletable("The view type " + getWrapped().getClass().getSimpleName() +
                " is not known by the views manager plugin, and deleting it may be dangerous.",
                getWrapped());
    }

    @Override
    public void setName(String newName) {
        throw new NameImmutable("The view type " + getWrapped().getClass().getSimpleName() +
                " is not known by the views manager plugin, and renaming it may be dangerous.", getWrapped(), getName(), newName);
    }
    */

    @Override
    public String getViewType() {
        return "UnknownViewType";
    }
}
