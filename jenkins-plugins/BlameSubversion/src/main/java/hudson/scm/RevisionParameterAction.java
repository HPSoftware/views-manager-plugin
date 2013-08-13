package hudson.scm;

import hudson.model.InvisibleAction;
import hudson.scm.BlameSubversionSCM.SvnInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Action containing a list of SVN revisions that should be checked out. Used for parameterized builds.
 * 
 * Modify: changed by tang
 * 
 * @author tang,Tom Huybrechts
 */
public class RevisionParameterAction extends InvisibleAction implements Serializable {
	
	private final List<SvnInfo> revisions;

	public RevisionParameterAction(List<SvnInfo> revisions) {
		super();
		this.revisions = revisions;
	}
	
	public RevisionParameterAction(SvnInfo... revisions) {
		this.revisions = new ArrayList<SvnInfo>(Arrays.asList(revisions));
	}

	public List<SvnInfo> getRevisions() {
		return revisions;
	}
	
	public SVNRevision getRevision(String url) {
		for (SvnInfo revision: revisions) {
			if (revision.url.equals(url)) {
				return SVNRevision.create(revision.revision);
			}
		}
		return null;
	}

}
