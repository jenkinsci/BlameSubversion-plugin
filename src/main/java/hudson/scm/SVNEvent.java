/*
 * The MIT License
 * 
 * Copyright (c) 2015 schristou88
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
package hudson.scm;

import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.internal.wc.SVNExternal;
import org.tmatesoft.svn.core.wc.SVNEventAction;

/**
 * @author schristou88
 */
public class SVNEvent extends org.tmatesoft.svn.core.wc.SVNEvent {
    private SVNExternal myExternalInfo;
    private SVNExternal myPreviousExternalInfo;

    public SVNEvent(SVNErrorMessage errorMessage, SVNEventAction expected) {
        super(errorMessage, expected);
    }

    public SVNExternal getExternalInfo() {
        return myExternalInfo;
    }

    public SVNExternal getPreviousExternalInfo() {
        return myPreviousExternalInfo;
    }

    public SVNEvent setExternalInfo(SVNExternal prev, SVNExternal _new) {
        this.myPreviousExternalInfo = prev;
        this.myExternalInfo = _new;
        return this;
    }
}
