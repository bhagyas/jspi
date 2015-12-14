/**
 * Copyright (C) 2004 <a href="http://www.lohndirekt.de/">lohndirekt.de</a>
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintJobAttributeSet;
import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.TextSyntax;

import de.lohndirekt.print.attribute.AttributeHelper;
import de.lohndirekt.print.attribute.AttributeParser;
import de.lohndirekt.print.attribute.AttributeWriter;
import de.lohndirekt.print.attribute.IppAttributeName;
import de.lohndirekt.print.attribute.IppDelimiterTag;
import de.lohndirekt.print.attribute.IppStatus;
import de.lohndirekt.print.attribute.ipp.Charset;
import de.lohndirekt.print.attribute.ipp.NaturalLanguage;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OperationsSupported;

/**
 * @author bpusch, speters, sefftinge
 *  
 */
class IppRequestCupsImpl implements IppRequest {
    class IppResponseIppImpl implements IppResponse {
        private Logger log = Logger.getLogger(this.getClass().getName());

        private IppStatus status;

        private Map attributes;

        IppResponseIppImpl(InputStream response) {
            try{
                parseResponse(response);
            } catch (IOException e) {
                log.log(Level.SEVERE, e.getMessage(), e);
                throw new RuntimeException(e);
            }
            
        }

        private void parseResponse(InputStream response) throws IOException {
            byte[] header = new byte[8];
            response.read(header);
            this.status = IppStatus.get((int) (header[2] << 8)
                    + (int) header[3]);
            if (response.available() != 0) {
                this.attributes = AttributeParser.parseResponse(response);
            } else {
                this.attributes = new HashMap();
            }
            if (log.isLoggable(Level.FINEST)) {
                log.finest("Status: " + status.getText());
            }
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

    private IppConnection conn;

    private boolean sent = false;

    private Object data;

    private URI path;

    private OperationsSupported operation;

    //Id wird in der Cups-API zwar bergeben, ist aber auch immer 1.
    private int id = 1;

    private PrintJobAttributeSet jobAttributes = new HashPrintJobAttributeSet();

    private AttributeSet operationAttributes = new HashAttributeSet();

    private AttributeSet printerAttributes = new HashAttributeSet();

    private Logger log = Logger.getLogger(this.getClass().getName());

    private static final int SEND_REQUEST_COUNT = 3;

    private static final int SEND_REQUEST_TIMEOUT = 50;
    
    private static final NaturalLanguage NATURAL_LANGUAGE_DEFAULT = NaturalLanguage.EN;
    
    private static final Charset CHARSET_DEFAULT = Charset.UTF_8;
    

    /**
     * @param operation
     */
    IppRequestCupsImpl(URI path, OperationsSupported operation) {
        this.path = path;
        this.operation = operation;
        init();
    }

    /**
     * @param printerAttributes
     */
    public void setPrinterAttributes(AttributeSet attrs) {
        this.printerAttributes = attrs;
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
        operationAttributes.add(CHARSET_DEFAULT);
        operationAttributes.add(NATURAL_LANGUAGE_DEFAULT);
    }

    /**
     *  
     */
    private byte[] ippFooter() {
        byte[] footer = new byte[1];
        footer[0] = (byte) IppDelimiterTag.END_ATTRIBUTES.getValue();
        return footer;
    }

    /**
     *  
     */
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
    private void jobAttributes(ByteArrayOutputStream out) throws UnsupportedEncodingException {
        if (!jobAttributes.isEmpty()) {
            out.write((byte) IppDelimiterTag.BEGIN_JOB_ATTRIBUTES
                            .getValue());
            for (int i = 0; i < jobAttributes.toArray().length; i++) {
                Attribute attribute = (Attribute) jobAttributes.toArray()[i];
                AttributeWriter.attributeBytes(attribute, out);
            }
        }
    }

    /**
     *
     * @param out
     * @return
     */
    private void printerAttributes(ByteArrayOutputStream out) throws UnsupportedEncodingException {
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
    private void operationAttributes(ByteArrayOutputStream out) throws UnsupportedEncodingException {
        if (!operationAttributes.isEmpty()) {
            out.write((byte) IppDelimiterTag.BEGIN_OPERATION_ATTRIBUTES
                            .getValue());
            Attribute[] attributes = AttributeHelper.getOrderedOperationAttributeArray(operationAttributes);
            for (int i = 0; i < attributes.length; i++) {
                AttributeWriter.attributeBytes(attributes[i], out);
            }
        }
    }

    /**
     *  
     */
    private byte[] ippHeader() {
        //Ipp header data according to http://www.ietf.org/rfc/rfc2910.txt
        ByteArrayOutputStream out = new ByteArrayOutputStream(8);
        //The first 2 bytes represent the IPP version number (1.1)
        //major version-number
        out.write((byte) 1);
        //minor version-number
        out.write((byte) 1);
        //2 byte operation id
        AttributeWriter.writeInt2(this.operation.getValue(), out);
        //4 byte request id
        AttributeWriter.writeInt4(this.id, out);
        return out.toByteArray();
    }

    /**
     * @param attributes
     */
    public void addOperationAttributes(AttributeSet attributes) {
        this.operationAttributes.addAll(attributes);
    }

    /**
     * @param stream
     */
    public void setData(InputStream data) {
        this.data = data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @param attributes
     */
    public void setJobAttributes(PrintJobAttributeSet attributes) {
        this.jobAttributes = attributes;
    }

    /**
     * @see de.lohndirekt.print.IppRequest#send()
     * @throws IllegalArgumentException
     *             when called twice
     */
    public IppResponse send() throws IOException {
        if (sent) {
            throw new IllegalStateException("Send must not be called twice");
        }

        String username = findUserName(this.operationAttributes);
        String password = findPassword(this.operationAttributes);
        boolean ok = false;
        int tries = 0;
        while (!ok && tries < SEND_REQUEST_COUNT) {
            tries++;

            this.conn = new IppHttpConnection(this.path, username, password);

            Vector v = new Vector();
            v.add(new ByteArrayInputStream(this.ippHeader()));
            v.add(new ByteArrayInputStream(this.ippAttributes()));
            v.add(new ByteArrayInputStream(this.ippFooter()));
            if (this.data != null) {
                v.add(this.getDataAsInputStream());
            }
            SequenceInputStream stream = new SequenceInputStream(v.elements());
            conn.setIppRequest(stream);
            conn.execute();

            if (conn.getStatusCode() != HttpURLConnection.HTTP_OK) {
                if (log.isLoggable(Level.INFO)) {
                    String msg = "Cups seems to be busy - STATUSCODE "+conn.getStatusCode();
                    if (tries < SEND_REQUEST_COUNT) {
                        msg += " - going to retry in " + SEND_REQUEST_TIMEOUT
                                + " ms";
                    }
                    log.warning(msg);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    if (log.isLoggable(Level.INFO)) {
                        log.info(e.getMessage());
                    }
                }
            } else {
                ok = true;
            }

        }
        this.sent = true;
        return getResponse();
    }

    /**
     * @param list
     * @return
     */
    private String findUserName(AttributeSet list) {
        if (list != null) {
            TextSyntax attr = (TextSyntax) list.get(IppAttributeName.REQUESTING_USER_NAME
                    .getCategory());
            if (attr != null) {
                return attr.getValue();
            }
        }
        return null;
    }

    /**
     * @param list
     * @return
     */
    private String findPassword(AttributeSet list) {
        if (list != null) {
            TextSyntax attr = (TextSyntax) list.get(IppAttributeName.REQUESTING_USER_PASSWD
                    .getCategory());
            if (attr != null) {
                return attr.getValue();
            }
        }
        return null;
    }

    /**
     * @return
     */
    private InputStream getDataAsInputStream() {
        if (data == null) {
            return null;
//        } else if (data instanceof FileInputStream) {
//            FileInputStream in = (FileInputStream) data;
//            try {
//				if (in.getFD().valid()){
//                    //TODO remove this hack
//				    in=new FileInputStream("./testfiles/test.pdf");
//				}
//			} catch (IOException e) {
//				log.log(Level.WARNING, "", e);
//			}
//            return in;
        } else if (data instanceof InputStream) {
            InputStream in = (InputStream) data;
            return in;
        } else if (data instanceof byte[]) {
            return new ByteArrayInputStream((byte[]) data);
        } else {
            throw new IllegalStateException("unknown data format : "
                    + data.getClass());
        }
    }

    private IppResponse getResponse() throws IOException {
        if (this.conn.getStatusCode()==HttpURLConnection.HTTP_OK) {
            return new IppResponseIppImpl(conn.getIppResponse());
        } else {
            return null;
        }
    }

}