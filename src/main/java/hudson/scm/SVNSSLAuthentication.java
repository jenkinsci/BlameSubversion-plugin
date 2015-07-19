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

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.wc.admin.SVNTranslator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author schristou88
 */
public class SVNSSLAuthentication extends org.tmatesoft.svn.core.auth.SVNSSLAuthentication {

    public SVNSSLAuthentication(File certFile, String password, boolean storageAllowed, SVNURL url, boolean isPartial) throws IOException {
        super(ISVNAuthenticationManager.SSL, null, storageAllowed, url, isPartial);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(certFile);
        try {
            SVNTranslator.copy(in, baos);
        } finally {
            in.close();
        }
        myCertificate = baos.toByteArray();
        myPassword = password;
    }

    public SVNSSLAuthentication(File certFile, String password, boolean storageAllowed) throws IOException {
        this(certFile,password,storageAllowed,null,false);
    }

    public SVNSSLAuthentication(byte[] certFile, String password, boolean storageAllowed) {
        this(certFile,password,storageAllowed,null,false);
    }

    public SVNSSLAuthentication(byte[] certFile, String password, boolean storageAllowed, SVNURL url, boolean isPartial) {
        super(ISVNAuthenticationManager.SSL, null, storageAllowed, url, isPartial);
        myCertificate = certFile;
        myPassword = password;
        mySSLKind = SSL;
    }

    /**
     *
     * @return cetificate file as a byte array
     */
    public byte[] getCertificateByteArray() {
        return myCertificate;
    }

    @Override
    public void setCertificatePath(String path) {
        super.setCertificatePath(path);

        myCertificatePath = path;
    }

    @Override
    public String getSSLKind() {
        return mySSLKind;
    }

    @Override
    public String getPassword() {
        return myPassword;
    }

    @Override
    public String getCertificatePath() {
        return myCertificatePath;
    }

    private String myPassword;
    private String mySSLKind;
    private byte[] myCertificate;
    private String myCertificatePath;
}