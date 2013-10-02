package com.hp.jenkins.plugins.alm.manageviews.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/26/12
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class NameImmutable extends ViewsManagerException {

    public NameImmutable(Object wrapped, String name, String newName) {
        this("instances of type '" + wrapped.getClass().getSimpleName() + "' cannot be renamed.", wrapped, name, newName);
    }

    public NameImmutable(String reason, Object wrapped, String name, String newName) {
        super("failed to rename '" + name + "' to '" + newName + "': " + reason);
    }
}
