package hudson.scm;

import hudson.model.AbstractModelObject;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.scm.BlameSubversionSCM.ModuleLocation;
import hudson.triggers.SCMTrigger;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.tmatesoft.svn.core.SVNException;

import javax.servlet.ServletException;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;

/**
 * Per repository status.
 * 
 * Modify: changed by tang
 * 
 * @author tang,Kohsuke Kawaguchi
 * @see BlameSubversionStatus
 */
public class BlameSubversionRepositoryStatus extends AbstractModelObject {
    public final UUID uuid;

    public BlameSubversionRepositoryStatus(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDisplayName() {
        return uuid.toString();
    }

    public String getSearchUrl() {
        return uuid.toString();
    }

    /**
     * Notify the commit to this repository.
     *
     * <p>
     * Because this URL is not guarded, we can't really trust the data that's sent to us. But we intentionally
     * don't protect this URL to simplify <tt>post-commit</tt> script set up.
     */
    public void doNotifyCommit(StaplerRequest req, StaplerResponse rsp) throws ServletException, IOException {
        requirePOST();

        // compute the affected paths
        Set<String> affectedPath = new HashSet<String>();
        String line;
        BufferedReader r = new BufferedReader(req.getReader());
        while((line=r.readLine())!=null) {
            LOGGER.finer("Reading line: "+line);
            affectedPath.add(line.substring(4));
            if (line.startsWith("svnlook changed --revision ")) {
                String msg = "Expecting the output from the svnlook command but instead you just sent me the svnlook invocation command line: " + line;
                LOGGER.warning(msg);
                throw new IllegalArgumentException(msg);
            }
        }
        if(LOGGER.isLoggable(FINE))
            LOGGER.fine("Change reported to Subversion repository "+uuid+" on "+affectedPath);
        boolean scmFound = false, triggerFound = false, uuidFound = false, pathFound = false;

        OUTER:
        for (AbstractProject<?,?> p : Hudson.getInstance().getItems(AbstractProject.class)) {
            try {
                SCM scm = p.getScm();
                if (scm instanceof BlameSubversionSCM) scmFound = true; else continue;

                SCMTrigger trigger = p.getTrigger(SCMTrigger.class);
                if (trigger!=null) triggerFound = true; else continue;

                BlameSubversionSCM sscm = (BlameSubversionSCM) scm;
                for (ModuleLocation loc : sscm.getLocations()) {
                    if (loc.getUUID(p).equals(uuid)) uuidFound = true; else continue;

                    String m = loc.getSVNURL().getPath();
                    String n = loc.getRepositoryRoot(p).getPath();
                    if(!m.startsWith(n))    continue;   // repository root should be a subpath of the module path, but be defensive

                    String remaining = m.substring(n.length());
                    if(remaining.startsWith("/"))   remaining=remaining.substring(1);
                    String remainingSlash = remaining + '/';

                    for (String path : affectedPath) {
                        if(path.equals(remaining) /*for files*/ || path.startsWith(remainingSlash) /*for dirs*/) {
                            // this project is possibly changed. poll now.
                            // if any of the data we used was bogus, the trigger will not detect a chaange
                            LOGGER.fine("Scheduling the immediate polling of "+p);
                            trigger.run();
                            pathFound = true;

                            continue OUTER;
                        }
                    }
                }
            } catch (SVNException e) {
                LOGGER.log(WARNING,"Failed to handle Subversion commit notification",e);
            }
        }

        if (!scmFound)          LOGGER.warning("No subversion jobs found");
        else if (!triggerFound) LOGGER.warning("No subversion jobs using SCM polling");
        else if (!uuidFound)    LOGGER.warning("No subversion jobs using repository: " + uuid);
        else if (!pathFound)    LOGGER.fine("No jobs found matching the modified files");

        rsp.setStatus(SC_OK);
    }

    private static final Logger LOGGER = Logger.getLogger(BlameSubversionRepositoryStatus.class.getName());
}
