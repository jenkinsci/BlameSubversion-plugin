package hudson.scm;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

/**
 * {@link SCMRevisionState} for {@link BlameSubversionSCM}. {@link Serializable} since we compute
 * this remote.
 * 
 * Modify: changed by tang
 */
final class BlameSVNRevisionState extends SCMRevisionState implements Serializable {
    /**
     * All the remote locations that we checked out. This includes those that are specified
     * explicitly via {@link BlameSubversionSCM#getLocations()} as well as those that
     * are implicitly pulled in via svn:externals, but it excludes those locations that
     * are added via svn:externals in a way that fixes revisions.
     */
    final Map<String,Long> revisions;

    BlameSVNRevisionState(Map<String, Long> revisions) {
        this.revisions = revisions;
    }

//    public PartialOrder compareTo(SCMRevisionState rhs) {
//        BlameSVNRevisionState that = (BlameSVNRevisionState)rhs;
//        return PartialOrder.from(that.hasNew(this), this.hasNew(that));
//    }
//
//    /**
//     * Does this object has something newer than the given object?
//     */
//    private boolean hasNew(BlameSVNRevisionState that) {
//        for (Entry<String,Long> e : revisions.entrySet()) {
//            Long rhs = that.revisions.get(e.getKey());
//            if (rhs==null || e.getValue().compareTo(rhs)>0)
//                return true;
//        }
//        return false;
//    }

    private static final long serialVersionUID = 1L;
}
