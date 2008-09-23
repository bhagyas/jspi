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

import java.lang.reflect.Constructor;
import java.net.URI;

import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.RequestingUserName;

import de.lohndirekt.print.attribute.auth.RequestingUserPassword;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OperationsSupported;

/**
 * @author bpusch
 *  
 */
public final class IppRequestFactory {

    final static String IPP_REQUEST_IMPL_KEY = "de.lohndirekt.print.IppRequest.Impl";

    final static IppRequest createIppRequest(URI uri,
            OperationsSupported operation, RequestingUserName user,
            RequestingUserPassword passwd) {
        String requestClassName = System.getProperty(IPP_REQUEST_IMPL_KEY);
        IppRequest request = null;
        if (requestClassName == null) {
            request = new IppRequestCupsImpl(uri, operation);
        } else {
            Class clazz = null;
            try {
                clazz = Class.forName(requestClassName);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Class " + requestClassName
                        + " does not exist.");
            }

            try {
                Constructor constructor = clazz
                        .getDeclaredConstructor(new Class[] { URI.class,
                                OperationsSupported.class });
                request = (IppRequest) constructor.newInstance(new Object[] {
                        uri, operation });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (user != null && passwd != null) {
            AttributeSet set = new HashAttributeSet();
            set.add(user);
            set.add(passwd);
            request.addOperationAttributes(set);
        }
        return request;
    }
}