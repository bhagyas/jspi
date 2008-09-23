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
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.DocFlavor;
import javax.print.MultiDoc;
import javax.print.MultiDocPrintJob;
import javax.print.PrintException;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobEvent;

import de.lohndirekt.print.attribute.AttributeHelper;
import de.lohndirekt.print.attribute.IppAttributeName;
import de.lohndirekt.print.attribute.IppStatus;
import de.lohndirekt.print.attribute.ipp.jobdesc.JobUri;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OperationsSupported;
/**
 * @author bpusch
 *
 */
class MultiDocJob extends Job implements MultiDocPrintJob {
    
	Logger log = Logger.getLogger(this.getClass().getName());
    
	/**
	 * 
	 */
	protected MultiDocJob(IppMultiDocPrintService service) {
		super(service);
	}
    
	void processMultiDocEvent(MultiDocEvent event) {
		SimpleMultiDoc doc = (SimpleMultiDoc) event.getSource();
		sendDocument(doc);
	}
    
	/**
	 *
	 */
	public void print(MultiDoc multiDoc, PrintRequestAttributeSet attributes) throws PrintException {
		SimpleMultiDoc multi = (SimpleMultiDoc) multiDoc;
		multi.addMultiDocListener(new MDListener());
		createJob(attributes);
		sendDocument(multi);
	}
    
	private void createJob(PrintRequestAttributeSet attributes) {
		IppRequest request = null;
		request = this.printService.request(OperationsSupported.CREATE_JOB);
		//add the operationn attributes
		request.addOperationAttributes(AttributeHelper.jobOperationAttributes(attributes));
		request.setJobAttributes(AttributeHelper.jobAttributes(attributes));
		IppResponse response = null;
		try {
			response = request.send();
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		if (response != null) {
			Map responseAttributes = response.getAttributes();
			if (responseAttributes.containsKey(IppAttributeName.JOB_URI.getCategory())) {
				Set jobUriSet = (Set) responseAttributes.get(IppAttributeName.JOB_URI.getCategory());
				this.jobUri = (JobUri) jobUriSet.iterator().next();
			}
			if (response.getStatus().equals(IppStatus.SUCCESSFUL_OK)
				|| response.getStatus().equals(IppStatus.SUCCESSFUL_OK_CONFLICTING_ATTRIBUTES)
				|| response.getStatus().equals(IppStatus.SUCCESSFUL_OK_CONFLICTING_ATTRIBUTES)) {
			} else {
				notifyJobListeners(PrintJobEvent.JOB_FAILED);
			}
		} else {
			notifyJobListeners(PrintJobEvent.JOB_FAILED);
		}
	}
    
	protected void sendDocument(SimpleMultiDoc multiDoc) {
		IppRequest request = null;
		AttributeSet operationAttributes = new HashAttributeSet();
		request = this.request(OperationsSupported.SEND_DOCUMENT);
		IppResponse response = null;
		try {
			operationAttributes.addAll(AttributeHelper.docOperationAttributes(multiDoc));
			request.addOperationAttributes(operationAttributes);
			if (multiDoc.getDoc().getDocFlavor() instanceof DocFlavor.INPUT_STREAM) {
				DocAttributeSet set = multiDoc.getDoc().getAttributes();
				request.setData((InputStream) multiDoc.getDoc().getPrintData());
			}
			response = request.send();
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		if (response != null) {
			Map responseAttributes = response.getAttributes();
			if (responseAttributes.containsKey(IppAttributeName.JOB_URI.getCategory())) {
				Set jobUriSet = (Set) responseAttributes.get(IppAttributeName.JOB_URI.getCategory());
				this.jobUri = (JobUri) jobUriSet.iterator().next();
			}
			if (response.getStatus().equals(IppStatus.SUCCESSFUL_OK)
				|| response.getStatus().equals(IppStatus.SUCCESSFUL_OK_CONFLICTING_ATTRIBUTES)
				|| response.getStatus().equals(IppStatus.SUCCESSFUL_OK_CONFLICTING_ATTRIBUTES)) {
			} else {
				notifyJobListeners(PrintJobEvent.JOB_FAILED);
			}
		} else {
			notifyJobListeners(PrintJobEvent.JOB_FAILED);
		}
		if (multiDoc.isLast()) {
			releaseJob();
		}
	}
    
	/**
	 * 
	 */
	private void releaseJob() {
		IppRequest request = this.request(OperationsSupported.RELEASE_JOB);
		IppResponse response = null;
		try {
			response = request.send();
			
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		if (response != null) {
			if (response.getStatus().equals(IppStatus.SUCCESSFUL_OK)
				|| response.getStatus().equals(IppStatus.SUCCESSFUL_OK_CONFLICTING_ATTRIBUTES)
				|| response.getStatus().equals(IppStatus.SUCCESSFUL_OK_CONFLICTING_ATTRIBUTES)) {
				notifyJobListeners(PrintJobEvent.JOB_COMPLETE);
			} else {
				notifyJobListeners(PrintJobEvent.JOB_FAILED);
			}
		} else {
			notifyJobListeners(PrintJobEvent.JOB_FAILED);
		}
	}
    
	private IppRequest request(OperationsSupported operation) {
		IppRequest request = IppRequestFactory.createIppRequest(this.jobUri.getURI(), operation, this.printService.getRequestingUserName(), this.printService.getRequestingUserPassword());
		AttributeSet operationAttributes = new HashAttributeSet();
		operationAttributes.add(new JobUri(this.jobUri.getURI()));
		request.addOperationAttributes(operationAttributes);
		return request;
	}
    
	private class MDListener implements MultiDocListener {
		public void processEvent(MultiDocEvent event) throws IOException {
			log.log(Level.FINEST, "MultiDocevent");
			processMultiDocEvent(event);
		}
	}
}
