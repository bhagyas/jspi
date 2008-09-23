package com.google.code.jspi;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintJobAttributeSet;
import javax.print.attribute.PrintJobAttributeSet;

import de.lohndirekt.print.attribute.AttributeHelper;
import de.lohndirekt.print.attribute.AttributeWriter;
import de.lohndirekt.print.attribute.IppDelimiterTag;
import de.lohndirekt.print.attribute.IppStatus;
import de.lohndirekt.print.attribute.ipp.Charset;
import de.lohndirekt.print.attribute.ipp.NaturalLanguage;

public class IppResponseIppImpl {
	private final Logger log = Logger.getLogger(this.getClass().getName());

	private final IppStatus status;

    // Id wird in der Cups-API zwar Uebergeben, ist aber auch immer 1.
	private final int id = 1;

	private final PrintJobAttributeSet jobAttributes = new HashPrintJobAttributeSet();

	private final AttributeSet operationAttributes = new HashAttributeSet();

	private final AttributeSet printerAttributes = new HashAttributeSet();
    
    private static final NaturalLanguage NATURAL_LANGUAGE_DEFAULT = NaturalLanguage.EN;

	private static final Charset CHARSET_DEFAULT = Charset.UTF_8;
	
	public IppResponseIppImpl(IppStatus status) {
		super();
		this.status = status;
        operationAttributes.add(CHARSET_DEFAULT);
		operationAttributes.add(NATURAL_LANGUAGE_DEFAULT);
	}
	
	/**
	 * @return
	 */
	public IppStatus getStatus() {
		return status;
	}
	
    private byte[] ippFooter() {
		byte[] footer = new byte[1];
		footer[0] = (byte) IppDelimiterTag.END_ATTRIBUTES.getValue();
		return footer;
	}

	private byte[] ippHeader() {
		// Ipp header data according to http://www.ietf.org/rfc/rfc2910.txt
		ByteArrayOutputStream out = new ByteArrayOutputStream(8);
		// The first 2 bytes represent the IPP version number (1.1)
		// major version-number
		out.write((byte) 1);
		// minor version-number
		out.write((byte) 1);
		// 2 byte status id
		AttributeWriter.writeInt2(this.status.getStatus(), out);
		// 4 byte request id
		AttributeWriter.writeInt4(this.id, out);
		return out.toByteArray();
	}
	
    private byte[] ippAttributes() throws UnsupportedEncodingException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		operationAttributes(out);
		printerAttributes(out);
		jobAttributes(out);
		byte[] body = out.toByteArray();
		return body;
	}

	/**
	 * @param out
	 * @return
	 */
	private void jobAttributes(ByteArrayOutputStream out)
			throws UnsupportedEncodingException {
		if (!jobAttributes.isEmpty()) {
			out.write((byte) IppDelimiterTag.BEGIN_JOB_ATTRIBUTES.getValue());
			for (int i = 0; i < jobAttributes.toArray().length; i++) {
				Attribute attribute = jobAttributes.toArray()[i];
				AttributeWriter.attributeBytes(attribute, out);
			}
		}
	}

	/**
	 * 
	 * @param out
	 * @return
	 */
	private void printerAttributes(ByteArrayOutputStream out)
			throws UnsupportedEncodingException {
		if (!printerAttributes.isEmpty()) {
			out.write((byte) IppDelimiterTag.BEGIN_PRINTER_ATTRIBUTES
					.getValue());
			Attribute[] attributes = printerAttributes.toArray();
			for (int i = 0; i < attributes.length; i++) {
				AttributeWriter.attributeBytes(attributes[i], out);
			}
		}
	}

	/**
	 * 
	 * @param out
	 * @return
	 */
	private void operationAttributes(ByteArrayOutputStream out)
			throws UnsupportedEncodingException {
		if (!operationAttributes.isEmpty()) {
			out.write((byte) IppDelimiterTag.BEGIN_OPERATION_ATTRIBUTES
					.getValue());
			Attribute[] attributes = AttributeHelper
					.getOrderedOperationAttributeArray(operationAttributes);
			for (int i = 0; i < attributes.length; i++) {
				AttributeWriter.attributeBytes(attributes[i], out);
			}
		}
	}
    
	public void write(OutputStream os) throws IOException {
		os.write(this.ippHeader());
		os.write(this.ippAttributes());
		os.write(this.ippFooter());
	}

	public PrintJobAttributeSet getJobAttributes() {
		return jobAttributes;
	}

	public AttributeSet getOperationAttributes() {
		return operationAttributes;
	}

	public AttributeSet getPrinterAttributes() {
		return printerAttributes;
	}

}
