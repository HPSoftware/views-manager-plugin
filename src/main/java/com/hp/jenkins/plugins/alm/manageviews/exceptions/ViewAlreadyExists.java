package com.hp.jenkins.plugins.alm.manageviews.exceptions;

import hudson.model.View;
import hudson.model.ViewGroup;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/15/12
 * Time: 10:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewAlreadyExists extends ViewsManagerException {

    public ViewAlreadyExists(ViewGroup vg, View v) {
        super("can't associate view '" + v + "' of type '" + v.getClass().getSimpleName() +
                "' to view group '" + vg + "' of type '" + vg.getClass().getSimpleName() +
                "' because it already contains a view by the name of '" + v.getViewName() + "'.");
    }
}
