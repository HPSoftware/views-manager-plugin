package com.hp.jenkins.plugins.alm.manageviews.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/13/12
 * Time: 1:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class ViewsManagerException extends RuntimeException {

    public ViewsManagerException(String message) {
        super(message);
    }

    public ViewsManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewsManagerException(Throwable cause) {
        super(cause);
    }
}
