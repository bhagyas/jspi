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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.DocFlavor;
import javax.print.MultiDocPrintService;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.standard.RequestingUserName;

import de.lohndirekt.print.attribute.auth.RequestingUserPassword;

/** 
 * 
 * @author bpusch
 * 
 * <p>
 * This is an implementation of the Java Print Service API for IPP.<p>
 * To use this implementation do one of the following:
 * <p>
 * 	1. Set the system properties
 * <ul>
 * <li><code>de.lohndirekt.print.IppPrintService.uri</code></li>
 * <li><code>de.lohndirekt.print.IppPrintService.username</code></li>
 * <li><code>de.lohndirekt.print.IppPrintService.password</code></li>
 * </ul>
 * and use the default constructor
 * <p>
 *  2. use the 3 parameter constructor
 * <p>
 *  then register the instance by invoking javax.print.PrintServiceLookup.registerServiceProvider<br>
 * <br>
 *  @see javax.print.PrintServiceLookup#registerServiceProvider
 */
public class IppPrintServiceLookup extends PrintServiceLookup {
    public final static String URI_KEY = "de.lohndirekt.print.IppPrintService.uri";
    public final static String USERNAME_KEY = "de.lohndirekt.print.IppPrintService.username";
    public final static String PASSWORD_KEY = "de.lohndirekt.print.IppPrintService.password";
    private List cupsServers = new ArrayList();
    private Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * <p>
     * default constructor uses the values set in
     * <p>
     * <code>de.lohndirekt.print.IppPrintService.uri</code><br>
     * <code>de.lohndirekt.print.IppPrintService.username</code><br>
     * <code>de.lohndirekt.print.IppPrintService.password</code><br>
     */
    public IppPrintServiceLookup() {
        String myUri = (String) System.getProperties().get(URI_KEY);
        String user = (String) System.getProperties().get(USERNAME_KEY);
        String password = (String) System.getProperties().get(PASSWORD_KEY);
        URI uri = null;
        if (myUri == null) {
            throw new NullPointerException("System property " + URI_KEY + " not set!");
        } else if (user == null) {
            throw new NullPointerException("System property " + USERNAME_KEY + " not set!");
        } else if (password == null) {
            throw new NullPointerException("System property " + PASSWORD_KEY + " not set!");
        }
        try {
            uri = new URI(myUri);
        } catch (URISyntaxException e) {
            throw new NullPointerException("System property " + URI_KEY + " - " + myUri + " is not a valid Uri!");
        }
        cupsServers.add(new IppServer(uri, new RequestingUserName(user,Locale.getDefault()), new RequestingUserPassword(password,Locale.getDefault())));
    }

    /**
     * This constructor uses the given parameters to connect to an IPP service
     * 
     * @param uri the uri of the ipp service
     * @param username used for authentication
     * @param password used for authentication
     */
    public IppPrintServiceLookup(URI uri, String username, String password) {
        cupsServers.add(new IppServer(uri, new RequestingUserName(username,Locale.getDefault()),new RequestingUserPassword(password,Locale.getDefault())));
    }

    /* (non-Javadoc)
     * @see javax.print.PrintServiceLookup#getPrintServices(javax.print.DocFlavor, javax.print.attribute.AttributeSet)
     */
    public PrintService[] getPrintServices(DocFlavor flavor, AttributeSet attributes) {
        PrintService[] services = getPrintServices();
        List fittingServices = new ArrayList();
        for (int i = 0; i < services.length; i++) {
            IppPrintService service = (IppPrintService) services[i];
            if (checkService(service, flavor, attributes)) {
                fittingServices.add(service);
            }
        }
        PrintService[] serviceArray = new PrintService[fittingServices.size()];
        return (PrintService[]) fittingServices.toArray(serviceArray);
    }

    /* (non-Javadoc)
     * @see javax.print.PrintServiceLookup#getPrintServices()
     */
    public PrintService[] getPrintServices() {
        List services = new ArrayList();
        try {
            for (Iterator iter = cupsServers.iterator(); iter.hasNext();) {
                IppServer server = (IppServer) iter.next();
                services.addAll(server.getPrintServices());
            }
        } catch (RuntimeException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        PrintService[] serviceArray = new PrintService[services.size()];
        return (PrintService[]) services.toArray(serviceArray);
    }

    /**
     * @param service
     * @param flavor
     * @param attributes
     * @return
     */
    private boolean checkService(IppPrintService service, DocFlavor flavor, AttributeSet attributes) {
        if (flavor == null || service.isDocFlavorSupported(flavor)) {
            if (attributes != null) {
                Attribute[] attrArray = attributes.toArray();
                for (int j = 0; j < attrArray.length; j++) {
                    Attribute attribute = attrArray[j];
                    if (service.isAttributeValueSupported(attribute, flavor, null)) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see javax.print.PrintServiceLookup#getMultiDocPrintServices(javax.print.DocFlavor[], javax.print.attribute.AttributeSet)
     */
    public MultiDocPrintService[] getMultiDocPrintServices(DocFlavor[] flavors, AttributeSet attributes) {
        Set multiDocServices = new HashSet();
        PrintService[] services = getPrintServices(null, attributes);
        for (int i = 0; i < services.length; i++) {
            PrintService service = services[i];
            if (flavors != null) {
                for (int j = 0; j < flavors.length; j++) {
                    DocFlavor flavor = flavors[j];
                    if (!service.isDocFlavorSupported(flavor)) {
                        break;
                    }
                }
            }
            multiDocServices.add(new IppMultiDocPrintService(((IppPrintService) service).getUri()));
        }
        return (MultiDocPrintService[]) multiDocServices.toArray(new MultiDocPrintService[multiDocServices.size()]);

    }

    /* (non-Javadoc)
     * @see javax.print.PrintServiceLookup#getDefaultPrintService()
     */
    public PrintService getDefaultPrintService() {
        PrintService[] services = this.getPrintServices();
        if (services.length > 0) {
            return services[0];
        }
        return null;
    }

}
