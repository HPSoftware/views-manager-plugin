package com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers;

import com.hp.jenkins.plugins.alm.manageviews.core.ViewsTree;
import hudson.model.View;
import hudson.model.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ViewGroupWrapper<NI,TI,VG extends ViewGroup> extends AbsWrapper<NI,TI,VG> {

    ViewGroupWrapper(VG wrapped, ViewsTree.ViewWrapperFactory wrapperFactory) {

        super(wrapped, wrapperFactory);
    }


    @Override
    protected List<View> _getChildren() {

        final Collection<View> views = this.wrapped.getViews();

        return new ArrayList<View>(views);
    }

}