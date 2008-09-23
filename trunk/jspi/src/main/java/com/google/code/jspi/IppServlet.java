package com.google.code.jspi;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.lohndirekt.print.attribute.IppStatus;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OperationsSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.PrinterUriSupported;

/**
 * Servlet implementation class IppServlet
 */
public class IppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private Map attributes;
	private OperationsSupported operationId;
	private byte[] jobdata;
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("");
        System.out.println("H E A D E R S");
        System.out.println("");
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			System.out
					.println(headerName + ":" + request.getHeader(headerName));
		}
		System.out.println("");
		System.out.println("B O D Y");
		System.out.println("");
		ServletInputStream inputStream = request.getInputStream();
		parseRequest(inputStream);
		System.out.println("Operazione richiesta : 0x"
				+ Integer.toHexString(this.operationId.getValue()));
		if (this.operationId.getValue() == OperationsSupported.CUPS_GET_PRINTERS
				.getValue()) {
			IppResponseIppImpl ippresponse = new IppResponseIppImpl(
					IppStatus.SUCCESSFUL_OK);
			try {
				ippresponse.getPrinterAttributes().add(
						new PrinterUriSupported(new URI(
								"http://127.0.0.1:9090/ipp/IppServlet")));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			ippresponse.write(response.getOutputStream());
		}
		
		if (this.operationId.getValue() == OperationsSupported.PRINT_JOB
				.getValue()) {
			IppResponseIppImpl ippresponse = new IppResponseIppImpl(
					IppStatus.SUCCESSFUL_OK);
			ippresponse.write(response.getOutputStream());
			java.io.File tempFile = java.io.File
					.createTempFile("unijob", "tmp");
			FileOutputStream faos = new FileOutputStream(tempFile);
			faos.write(this.jobdata);
			faos.close();
			System.out.println("Scritti : [" + this.jobdata.length
					+ "] bytes in : [" + tempFile.getAbsolutePath() + "]");
		}

		response.getOutputStream().close();
	}
	
    private void parseRequest(InputStream request) throws IOException {
		byte[] header = new byte[8];
		request.read(header);
		this.operationId = new OperationsSupported((header[2] << 8) + header[3]);
		if (request.available() != 0) {
			this.attributes = AttributeParser.parseRequest(request);
		} else {
			this.attributes = new HashMap();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (request.available() != 0) {
			baos.write(request.read());
		}
		baos.close();
		this.jobdata = baos.toByteArray();
		request.close();
	}

}
