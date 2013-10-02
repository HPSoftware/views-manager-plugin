package com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers;

import com.hp.jenkins.plugins.alm.manageviews.core.ViewsTree;
import com.hp.jenkins.plugins.alm.manageviews.exceptions.TreeModificationFailedDueToIO;
import com.hp.jenkins.plugins.alm.manageviews.exceptions.ViewsManagerException;
import hudson.model.Descriptor;
import hudson.model.ViewGroup;
import hudson.plugins.nested_view.NestedView;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/13/12
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class NestedViewWrapper<NI,TI> extends ViewGroupWrapper<NI,TI,NestedView> {

    public NestedViewWrapper(NestedView wrapped, ViewsTree.ViewWrapperFactory wrapperFactory) {
        super(wrapped, wrapperFactory);
    }

    /*
    * TODO: all of the below logic is copy paste from AbsViewWrapper!
    * this logic should be removed to some external class, and delegated by both AbsViewWrapper and this class,
    * to maintain DRY.
    * */
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

}
