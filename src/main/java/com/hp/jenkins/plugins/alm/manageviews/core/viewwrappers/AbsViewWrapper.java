package com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers;

import com.hp.jenkins.plugins.alm.manageviews.core.ViewsTree;
import com.hp.jenkins.plugins.alm.manageviews.exceptions.TreeModificationFailedDueToIO;
import com.hp.jenkins.plugins.alm.manageviews.exceptions.ViewsManagerException;
import hudson.model.Descriptor;
import hudson.model.View;
import hudson.model.ViewGroup;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/26/12
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbsViewWrapper<NI,TI,T extends View> extends AbsWrapper<NI,TI,T> {

    public AbsViewWrapper(T wrapped, ViewsTree.ViewWrapperFactory wrapperFactory) {
        super(wrapped, wrapperFactory);
    }

    @Override
    public T getWrapped() {
        return super.getWrapped();
    }

    @Override
    protected ViewGroup _getParent() {
        return getWrapped().getOwner();
    }

    @Override
    public String getName() {
        return getWrapped().getViewName();
    }

    @Override
    public void setName(String newName) {
        try {
            getWrapped().rename(newName);
        }
        catch (Descriptor.FormException e) {
            throw new ViewsManagerException("renaming of view " + getName() + " to " + newName + " failed:", e);
        }
    }

    @Override
    public void delete() {

        try {
            getWrapped().getOwner().deleteView(getWrapped());
        }

        catch (IOException ioe) {

            throw new TreeModificationFailedDueToIO(ioe);
        }
    }

    @Override
    protected void _move(ViewGroupWrapper newParent) {
        move(this.getWrapped(), (ViewGroup)newParent.getWrapped());
    }

    @Override
    protected List<View> _getChildren() {
        return Collections.emptyList();
    }
}
