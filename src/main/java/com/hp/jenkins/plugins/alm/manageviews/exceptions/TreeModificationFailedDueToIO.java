package com.hp.jenkins.plugins.alm.manageviews.exceptions;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/14/12
 * Time: 12:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class TreeModificationFailedDueToIO extends ViewsManagerException {
    public TreeModificationFailedDueToIO(IOException ioe) {
        super("Tree modification failed due to IO operation.", ioe);
    }
}
