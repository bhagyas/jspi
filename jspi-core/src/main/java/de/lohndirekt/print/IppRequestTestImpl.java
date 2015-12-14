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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintJobAttributeSet;
import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.Compression;
import javax.print.attribute.standard.CopiesSupported;
import javax.print.attribute.standard.JobImpressionsSupported;
import javax.print.attribute.standard.JobKOctetsSupported;
import javax.print.attribute.standard.JobMediaSheetsSupported;
import javax.print.attribute.standard.JobPrioritySupported;
import javax.print.attribute.standard.JobSheets;
import javax.print.attribute.standard.MultipleDocumentHandling;
import javax.print.attribute.standard.NumberUpSupported;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PDLOverrideSupported;
import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import javax.print.attribute.standard.Sides;

import de.lohndirekt.print.IppRequest;
import de.lohndirekt.print.IppResponse;
import de.lohndirekt.print.attribute.IppAttributeName;
import de.lohndirekt.print.attribute.IppStatus;
import de.lohndirekt.print.attribute.ipp.Charset;
import de.lohndirekt.print.attribute.ipp.NaturalLanguage;
import de.lohndirekt.print.attribute.ipp.jobtempl.LdJobHoldUntil;
import de.lohndirekt.print.attribute.ipp.printerdesc.PrinterDriverInstaller;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.CompressionSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.FinishingsSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.JobHoldUntilSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.JobSheetsSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.MediaSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.MultipleDocumentHandlingSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OperationsSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OrientationRequestedSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.PageRangesSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.PrinterUriSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.SidesSupported;

public class IppRequestTestImpl implements IppRequest {
	private Logger log = Logger.getLogger(this.getClass().getName());
	
    class IppResponseTestImpl implements IppResponse {
        
        IppStatus status;
        Map attributes;

        IppResponseTestImpl() {

        }

        /**
         * @return
         */
        public Map getAttributes() {
            return attributes;
        }

        /**
         * @return
         */
        public IppStatus getStatus() {
            return status;
        }

    }

    private IppResponseTestImpl response;
    private Object data;
    private OperationsSupported operation;
    //Id wird in der Cups-API zwar �bergeben, ist aber auch immer 1.
    private PrintJobAttributeSet jobAttributes = new HashPrintJobAttributeSet();
    private AttributeSet operationAttributes = new HashAttributeSet();
    private AttributeSet printerAttributes = new HashAttributeSet();

    /**
     * @param printerAttributes
     */
    public void setPrinterAttributes(AttributeSet attrs) {
        this.printerAttributes = attrs;
    }

    /**
     * @param operation
     */
    public IppRequestTestImpl(URI path, OperationsSupported operation) {
        this.operation = operation;
        init();
    }

    /**
     * 
     */
    private void init() {
        setStandardAttributes();
    }

    /**
     * 
     */
    private void setStandardAttributes() {
        operationAttributes.add(Charset.ISO_8859_1);
        operationAttributes.add(NaturalLanguage.EN);
    }

    /**
     * @param attributes
     */
    public void addOperationAttributes(AttributeSet attributes) {
        this.operationAttributes.addAll(attributes);
    }

    /**
     * @param data the data as input stream
     * @param the size of the data
     */
    public void setData(InputStream data) {
        this.data = data;
    }
    /**
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @param attributes
     */
    public void setJobAttributes(PrintJobAttributeSet attributes) {
        this.jobAttributes = attributes;
    }

    public IppResponse send() throws IOException {
    	try {
    	this.response = new IppResponseTestImpl();
        this.send(this.operation);
    	} catch (Exception e){
    		throw new RuntimeException(e);
    	}
    	return response;
    }

    /**
     * @param supported
     */
    private void send(OperationsSupported operation) throws URISyntaxException {
    	if (operation.equals(OperationsSupported.GET_PRINTER_ATTRIBUTES)) {
           sendGetPrinterAttributes();
        } else if (operation.equals(OperationsSupported.CUPS_GET_PRINTERS)) {
			sendCupsGetPrinter();
        } else{
        	log.warning("Call to request " + operation.toString() + " not implemented");
        	this.response.attributes = new HashMap();
        }
		response.status = IppStatus.SUCCESSFUL_OK;
    }

    /**
     * 
     */
    private void sendCupsGetPrinter() throws URISyntaxException {
    	Map attributes = new HashMap();
    	Set printerUris = new HashSet();
    	printerUris.add(new PrinterUriSupported(new URI("http://127.0.0.1")));
    	attributes.put(IppAttributeName.PRINTER_URI_SUPPORTED.getCategory(),printerUris);
    	this.response.attributes = attributes;
    }

    /**
     * 
     */
    private void sendGetPrinterAttributes() throws URISyntaxException {
        //    	
        //		- Printer attributes that are Job Template attributes ("xxx-
        //				default" "xxx-supported", and "xxx-ready" in the Table in
        //				Section 4.2),
        //			  - "pdl-override-supported",
        //			  - "compression-supported",
        //			  - "job-k-octets-supported",
        //			  - "job-impressions-supported",
        //			  - "job-media-sheets-supported",
        //			  - "printer-driver-installer",
        //			  - "color-supported", and
        //			  - "reference-uri-schemes-supported"

        Map attributes = new HashMap();
        Set pdlOverride = new HashSet();
        pdlOverride.add(PDLOverrideSupported.ATTEMPTED);
        pdlOverride.add(PDLOverrideSupported.NOT_ATTEMPTED);
        attributes.put(IppAttributeName.PDL_OVERRIDE_SUPPORTED.getCategory(), pdlOverride);
        Set compression = new HashSet();
        compression.add(new CompressionSupported(Compression.GZIP.toString(), Locale.getDefault()));
        compression.add(new CompressionSupported(Compression.NONE.toString(), Locale.getDefault()));
        attributes.put(IppAttributeName.COMPRESSION_SUPORTED.getCategory(), compression);
        Set jobKOctets = new HashSet();
        jobKOctets.add(new JobKOctetsSupported(1, 10));
        attributes.put(IppAttributeName.JOB_K_OCTETS_SUPPORTED.getCategory(), jobKOctets);
        Set jobImpressions = new HashSet();
        jobImpressions.add(new JobImpressionsSupported(1, 10));
        attributes.put(IppAttributeName.JOB_IMPRESSIONS_SUPPORTED.getCategory(), jobImpressions);
        Set jobMediaSheets = new HashSet();
        jobMediaSheets.add(new JobMediaSheetsSupported(1, 10));
        attributes.put(IppAttributeName.JOB_MEDIA_SHEETS_SUPPORTED.getCategory(), jobMediaSheets);
        Set printerDriverInst = new HashSet();
        printerDriverInst.add(new PrinterDriverInstaller(new URI("http://127.0.0.1")));
        attributes.put(IppAttributeName.PRINTER_DRIVER_INSTALLER.getCategory(), printerDriverInst);
        Set color = new HashSet();
        color.add(ColorSupported.SUPPORTED);
        attributes.put(IppAttributeName.COLOR_SUPPORTED.getCategory(), color);
        Set refUriScheme = new HashSet();
        refUriScheme.add(ReferenceUriSchemesSupported.HTTP);
        refUriScheme.add(ReferenceUriSchemesSupported.FTP);
        attributes.put(IppAttributeName.REFERENCE_URI_SCHEMES_SUPPORTED.getCategory(), refUriScheme);

        //Attributes named in 4.2 of rfc2911
        Set jobPrio = new HashSet();
        jobPrio.add(new JobPrioritySupported(99));
        attributes.put(IppAttributeName.JOB_PRIORIY_SUPPORTED.getCategory(), jobPrio);
        Set jobHoldUntil = new HashSet();
        jobHoldUntil.add(new JobHoldUntilSupported(new LdJobHoldUntil("12:00:00",Locale.getDefault()).toString(),Locale.getDefault()));
        jobHoldUntil.add(new JobHoldUntilSupported(LdJobHoldUntil.THIRD_SHIFT.toString(),Locale.getDefault()));
        attributes.put(IppAttributeName.JOB_HOLD_UNTIL_SUPPORTED.getCategory(), jobHoldUntil);
        Set jobSheets = new HashSet();
        jobSheets.add(new JobSheetsSupported(JobSheets.NONE.toString(), Locale.getDefault()));
        jobSheets.add(new JobSheetsSupported(JobSheets.STANDARD.toString(), Locale.getDefault()));
        attributes.put(IppAttributeName.JOB_SHEETS_SUPORTED.getCategory(), jobSheets);
        Set multipleDocumentHandling = new HashSet();
        multipleDocumentHandling.add(
            new MultipleDocumentHandlingSupported(
                MultipleDocumentHandling.SEPARATE_DOCUMENTS_COLLATED_COPIES.toString(),
                Locale.getDefault()));
        multipleDocumentHandling.add(
            new MultipleDocumentHandlingSupported(
                MultipleDocumentHandling.SINGLE_DOCUMENT.toString(),
                Locale.getDefault()));
        attributes.put(
            IppAttributeName.MULTIPLE_DOCUMENT_HANDLING_SUPPORTED.getCategory(),
            multipleDocumentHandling);
        Set copies = new HashSet();
        copies.add(new CopiesSupported(1, 100));
        attributes.put(IppAttributeName.COPIES_SUPPORTED.getCategory(), copies);
        Set finishings = new HashSet();
        finishings.add(new FinishingsSupported(1));
        finishings.add(new FinishingsSupported(2));
        attributes.put(IppAttributeName.FINISHINGS_SUPPORTED.getCategory(), finishings);
        Set pageRanges = new HashSet();
        pageRanges.add(PageRangesSupported.SUPPORTED);
        attributes.put(IppAttributeName.PAGE_RANGES_SUPPORTED.getCategory(), pageRanges);
        Set sides = new HashSet();
        sides.add(new SidesSupported(Sides.DUPLEX.toString(), Locale.getDefault()));
        sides.add(new SidesSupported(Sides.TWO_SIDED_SHORT_EDGE.toString(), Locale.getDefault()));
        attributes.put(IppAttributeName.SIDES_SUPPORTED.getCategory(), sides);
        Set numberUp = new HashSet();
        numberUp.add(new NumberUpSupported(1, 10));
        numberUp.add(new NumberUpSupported(100));
        attributes.put(IppAttributeName.NUMBER_UP_SUPPORTED.getCategory(), numberUp);
        Set orientationReq = new HashSet();
        orientationReq.add(new OrientationRequestedSupported(OrientationRequested.LANDSCAPE.getValue()));
        orientationReq.add(new OrientationRequestedSupported(OrientationRequested.PORTRAIT.getValue()));
        attributes.put(IppAttributeName.ORIENTATION_REQUESTED_SUPPORTED.getCategory(), orientationReq);
        Set media = new HashSet();
        media.add(new MediaSupported("test", Locale.getDefault()));
        media.add(new MediaSupported("test2", Locale.getDefault()));
        attributes.put(IppAttributeName.MEDIA_SUPPORTED.getCategory(), media);
        //media-ready not implemented
        //printer-resolution-supported not implemented
		//print-quality-supported not implemented
		response.attributes = attributes;
		
    }

    private IppResponse getResponse() throws IOException {
        return this.response;
    }
}
