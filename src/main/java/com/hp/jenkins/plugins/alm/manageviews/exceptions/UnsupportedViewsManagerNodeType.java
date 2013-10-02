package com.hp.jenkins.plugins.alm.manageviews.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/13/12
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class UnsupportedViewsManagerNodeType extends ViewsManagerException {

    public UnsupportedViewsManagerNodeType(Object view) {

        super("View '" + view + "' is of unsupported type " + view.getClass().getSimpleName());
    }
}
