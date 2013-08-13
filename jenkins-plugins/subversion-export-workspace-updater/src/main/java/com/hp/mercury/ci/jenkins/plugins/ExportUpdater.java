/*
 * The MIT License
 *
 * Copyright (c) 2004-2010, Sun Microsystems, Inc., Kohsuke Kawaguchi, Fulvio Cavarretta,
 * Jean-Baptiste Quenot, Luca Domenico Milanesio, Renaud Bruyeron, Stephen Connolly,
 * Tom Huybrechts, Yahoo! Inc., Manufacture Francaise des Pneumatiques Michelin,
 * Romain Seguy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.hp.mercury.ci.jenkins.plugins;

import hudson.Extension;
import hudson.scm.SubversionSCM.External;

import hudson.scm.subversion.WorkspaceUpdater;
import hudson.scm.subversion.WorkspaceUpdaterDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link WorkspaceUpdater} that uses "svn update" as much as possible.
 * 
 * @author Kohsuke Kawaguchi
 */
public class ExportUpdater extends WorkspaceUpdater {
    private static final long serialVersionUID = 1451258464864424355L;
    private static boolean overwrite = true;

    @DataBoundConstructor
    public ExportUpdater() {
    }

    @Override
    public UpdateTask createTask() {
        return new TaskImpl();
    }

    public static class TaskImpl extends UpdateTask {
        /**
         * 
         */
        private static final long serialVersionUID = 23470934706979L;


        @Override
        public List<External> perform() throws IOException, InterruptedException {

            final SVNUpdateClient svnuc = manager.getUpdateClient();
            final List<External> externals = new ArrayList<External>(); // store discovered externals to here

            try {
                File local = new File(ws, location.getLocalDir());
                svnuc.setEventHandler(new SubversionUpdateEventHandler(listener.getLogger(), externals, local, location.getLocalDir()));

                SVNRevision r = getRevision(location);

                listener.getLogger().println("Exporting " + location.remote);

                svnuc.doExport(location.getSVNURL(), local.getCanonicalFile(), SVNRevision.HEAD, r, System.getProperty("line.separator","\n"), overwrite, SVNDepth.INFINITY);

            }
            catch (final SVNException e) {

                e.printStackTrace(listener.error("Failed to update " + location.remote));

                return null;
            }

            return externals;
        }

    }

    @Extension
    public static class DescriptorImpl extends WorkspaceUpdaterDescriptor {
        @Override
        public String getDisplayName() {
            return "Export From Subversion";
        }
    }
}