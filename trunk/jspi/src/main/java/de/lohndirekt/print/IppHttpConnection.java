/**
 * Copyright (C) 2003 <a href="http://www.lohndirekt.de/">lohndirekt.de</a>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *  
 */
package de.lohndirekt.print;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import de.lohndirekt.print.exception.AuthenticationException;


/**
 * simple facade / abstraction layer to "commons httpclient"
 * 
 * @author sefftinge
 *  
 */
class IppHttpConnection implements IppConnection {

    private Logger log = Logger.getLogger(this.getClass().getName());

    private HttpClient httpConn;

    private PostMethod method;

    /**
     * @param uri
     * @param user
     * @param passwd
     * @param useStream
     */
    public IppHttpConnection(URI uri, String user, String passwd) {
        try {
            httpConn = new HttpClient();
            method = new PostMethod(toHttpURI(uri).toString());
            method.addRequestHeader("Content-type", "application/ipp");
            method.addRequestHeader("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
            method.setRequestContentLength(EntityEnclosingMethod.CONTENT_LENGTH_AUTO);
            // authentication
            if (user != null && user.trim().length() > 0) {
                if (log.isLoggable(Level.FINER)) {
                    log.log(Level.SEVERE, "Using username: "+user+" , passwd.length "+passwd.length());
                }
                method.setDoAuthentication(true);
                Credentials creds = new UsernamePasswordCredentials(user,
                        passwd);
                httpConn.getState().setCredentials(null,
                        toHttpURI(uri).getHost(), creds);
                
            }
            
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    
    /**
     * @return content of the response
     * @throws IOException
     */
    public InputStream getIppResponse() throws IOException {
        return method.getResponseBodyAsStream();
    }

    /**
     * @return the statuscode of last request
     * @throws IOException
     */
    public int getStatusCode() throws IOException {
        return method.getStatusCode();
    }

    private static URI toHttpURI(URI uri) {
        if (uri.getScheme().equals("ipp")) {
            String uriString = uri.toString().replaceFirst("ipp", "http");
            // TODO remove this hack!
//            uriString = uriString.replaceAll("(\\d{1,3}\\.){3}\\d{1,3}:\\d{1,}", "127.0.0.1:631");
            // endof hack
            try {
                uri = new URI(uriString);
            } catch (URISyntaxException e) {
                throw new RuntimeException("toHttpURI buggy? : uri was " + uri);
            }
        }
        return uri;
    }

    /**
     * @param stream
     */
    public void setIppRequest(InputStream stream) {
        method.setRequestBody(stream);
    }
    
    public boolean execute() throws HttpException, IOException {
        if (this.method.validate()) {
            httpConn.executeMethod(method);
            if (this.getStatusCode()==HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new AuthenticationException(method.getStatusText());
            }
            return true;
        } else {
            return false;
        }
    }

}