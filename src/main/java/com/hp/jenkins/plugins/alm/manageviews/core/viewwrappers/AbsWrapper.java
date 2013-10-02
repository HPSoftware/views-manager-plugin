package com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers;

import com.hp.commons.core.collection.CollectionUtils;
import com.hp.commons.core.handler.Handler;
import com.hp.jenkins.plugins.alm.manageviews.core.ViewsTree;
import com.hp.jenkins.plugins.alm.manageviews.exceptions.TreeModificationFailedDueToIO;
import com.hp.jenkins.plugins.alm.manageviews.exceptions.ViewAlreadyExists;
import com.hp.jenkins.plugins.alm.manageviews.exceptions.ViewGroupExpected;
import com.hp.jenkins.plugins.alm.manageviews.plugin.ManageViewsLink;
import hudson.model.View;
import hudson.model.ViewGroup;
import hudson.plugins.nested_view.NestedView;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/13/12
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbsWrapper<NI, TI, T> implements ViewWrapper {

    protected T wrapped;
    protected ViewsTree.ViewWrapperFactory wrapperFactory;
    private NI id;
    private TI treeId;

    public AbsWrapper(T wrapped, ViewsTree.ViewWrapperFactory wrapperFactory) {
        this.wrapped = wrapped;
        this.wrapperFactory = wrapperFactory;
    }

    public void setId(NI id) {
        this.id = id;
    }

    public void setTreeId(TI treeId) {
        this.treeId = treeId;
    }

    public NI getId() {
        return this.id;
    }


    public TI getTreeId() {
        return this.treeId;
    }

    @Override
    public T getWrapped() {
        return wrapped;
    }

    @Override
    public void move(ViewWrapper newParent) {

        if (newParent instanceof ViewGroupWrapper) {
            _move((ViewGroupWrapper) newParent);
        }
        else throw new ViewGroupExpected(newParent.getWrapped());
    }

    @Override
    public void clone(ViewWrapper newParent) {
        if (newParent instanceof ViewGroupWrapper) {
            _clone((ViewGroupWrapper) newParent);
        }
        else throw new ViewGroupExpected(newParent.getWrapped());
    }

    @Override
    public List<ViewWrapper> getChildren() {
        return CollectionUtils.map(
                _getChildren(),
                new Handler<ViewWrapper, View>() {

                    @Override
                    public ViewWrapper apply(View input) {

                        return wrapperFactory.newViewWrapper(input);
                    }
                }
        );
    }

    @Override
    public ViewGroupWrapper getParent() {
        return (ViewGroupWrapper)wrapperFactory.newViewWrapper(_getParent());
    }

    @Override
    public String getViewType() {
        return getWrapped().getClass().getSimpleName();
    }

    protected abstract ViewGroup _getParent();

    protected abstract List<View> _getChildren();

    protected abstract void _move(ViewGroupWrapper newParent);

    protected void _clone(ViewGroupWrapper newParent) {

        //Jenkins wrapper never reaches here, there's an override of the clone method.
        final View clone = (View)ManageViewsLink.clone(getWrapped());
        ManageViewsLink.setOwner(clone, null);
        AbsWrapper.move(clone, (ViewGroup) newParent.getWrapped());
    }

    protected static void move(View v, ViewGroup vg) {

        try {

            if (vg.getView(v.getViewName()) != null) {
                throw new ViewAlreadyExists(vg,v);
            }

            //if copy, or for some other reason there is no parent...
            if (v.getOwner() != null && v.getOwner().getView(v.getViewName()) != null) {
                v.getOwner().deleteView(v);
            }

            if (vg instanceof NestedView) {
                ManageViewsLink.AddView((NestedView)vg, v);
            }

            else if (vg == Jenkins.getInstance()) {
                Jenkins.getInstance().addView(v);
            }
        }

        catch (IOException ioe) {

            throw new TreeModificationFailedDueToIO(ioe);
        }

        ManageViewsLink.setOwner(v, vg);
    }

}
