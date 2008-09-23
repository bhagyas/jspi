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
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.attribute.Attribute;
import javax.print.attribute.URISyntax;
import javax.print.attribute.standard.RequestingUserName;

import de.lohndirekt.print.attribute.IppAttributeName;
import de.lohndirekt.print.attribute.auth.RequestingUserPassword;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OperationsSupported;

/**
 * @author bpusch
 *
 */
class IppServer {

	private final Logger log = Logger.getLogger(this.getClass().getName());
	private URI uri;
	private RequestingUserName user;
	private RequestingUserPassword passwd;
	
	IppServer(URI uri, RequestingUserName user, RequestingUserPassword passwd) {
		this.uri = uri;
		this.user = user;
		this.passwd = passwd;
	}

	/**
	 * @return a list of all PrintServices (printers as well as classes)
	 * 			known to the CUPS server  
	 */
	List getPrintServices() {
		List services = new ArrayList();
		services.addAll(getServices(OperationsSupported.CUPS_GET_PRINTERS,user,passwd));
		services.addAll(getServices(OperationsSupported.CUPS_GET_CLASSES,user,passwd));
		return services;
	}
	
	private List getServices(OperationsSupported operation,RequestingUserName user, RequestingUserPassword passwd){
		if (!(operation==OperationsSupported.CUPS_GET_CLASSES || operation == OperationsSupported.CUPS_GET_PRINTERS)){
			throw new IllegalArgumentException("Operation not applicable");
		}
		IppResponse response = null;
		IppRequest request = IppRequestFactory.createIppRequest(this.uri, operation,user,passwd);
		try {
			response = request.send();
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(),e);
		}
		List services = new ArrayList();
		if (response != null) {
			Set uriAttributes = (Set)response.getAttributes().get(IppAttributeName.PRINTER_URI_SUPPORTED.getCategory());
			if (uriAttributes != null) {
				for (Iterator iter = uriAttributes.iterator(); iter.hasNext();) {
					Attribute attribute = (Attribute)iter.next();
					URI printerUri = ((URISyntax)attribute).getURI();
					IppPrintService service = new IppPrintService(printerUri);
					service.setRequestingUserName(user);
					service.setRequestingUserPassword(passwd);
					services.add(service);
				}
			}
		}
		return services;
	}

}
