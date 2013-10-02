package com.hp.jenkins.plugins.alm.manageviews.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/13/12
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class ViewGroupExpected extends ViewsManagerException {

    public ViewGroupExpected(Object view) {

        super("Expected view '" + view + "' to be of type ViewGroup, but was " +
                view.getClass().getSimpleName());
    }
}
