package com.hp.jenkins.plugins.alm.manageviews.core.viewwrappers;

import com.hp.jenkins.plugins.alm.manageviews.core.ViewsTree;
import hudson.model.ListView;

public class ListViewWrapper<NI,TI> extends AbsViewWrapper<NI,TI,ListView> {


    public ListViewWrapper(ListView wrapped, ViewsTree.ViewWrapperFactory wrapperFactory) {
        super(wrapped, wrapperFactory);
    }



}