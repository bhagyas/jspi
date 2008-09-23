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
package de.lohndirekt.print.attribute;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.Doc;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintJobAttributeSet;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.DocumentName;

import de.lohndirekt.print.SimpleMultiDoc;
import de.lohndirekt.print.attribute.ipp.DocumentFormat;
import de.lohndirekt.print.attribute.ipp.LastDocument;

/**
 * @author ld-development
 *
 * 
 */
public final class AttributeHelper {

    private final static Logger log = Logger.getLogger(AttributeHelper.class.getName());
    /**
     * filters the given attributes
     * 
     * @param attributes
     * @return only the attributes wich are of type <code>PrintJobAttribute</code> and not operation attributes 
     */
    public final static PrintJobAttributeSet jobAttributes(PrintRequestAttributeSet attributes) {
        PrintJobAttributeSet jobAttributes = new HashPrintJobAttributeSet();
        if (attributes != null) {
            AttributeSet invalidAttributes = jobOperationAttributes(attributes);
            Object[] attributeArray = attributes.toArray();
            for (int i = 0; i < attributeArray.length; i++) {
                Attribute attribute = (Attribute) attributeArray[i];
                //attributes-charset, attributes-natural-language etc. are not set by the user
                if (attribute instanceof PrintJobAttribute && !(invalidAttributes.containsValue(attribute))) {
                    jobAttributes.add(attribute);
                }
            }
        }
        return jobAttributes;
    }

    /**
     * filters the given attributes
     * 
     * @param attributes
     * @return only job-operation attributes
     */
    public final static AttributeSet jobOperationAttributes(PrintRequestAttributeSet attributes) {
        AttributeSet operationAttributes = new HashAttributeSet();
        if (attributes != null) {
            Object[] attributeArray = attributes.toArray();
            for (int i = 0; i < attributeArray.length; i++) {
                Attribute attribute = (Attribute) attributeArray[i];
                //attributes-charset, attributes-natural-language etc. are not set by the user
                if (attribute.getCategory().equals(IppAttributeName.JOB_NAME.getCategory())
                    || attribute.getCategory().equals(IppAttributeName.FIDELITY.getCategory())
                    || attribute.getCategory().equals(IppAttributeName.JOB_IMPRESSIONS.getCategory())
                    || attribute.getCategory().equals(IppAttributeName.JOB_K_OCTETS.getCategory())
                    || attribute.getCategory().equals(IppAttributeName.JOB_MEDIA_SHEETS.getCategory())
                    || attribute.getCategory().equals(IppAttributeName.JOB_NAME.getCategory())
                    || attribute.getCategory().equals(IppAttributeName.COMPRESSION.getCategory())
                    || attribute.getCategory().equals(IppAttributeName.REQUESTING_USER_NAME.getCategory())
                    || attribute.getCategory().equals(IppAttributeName.REQUESTING_USER_PASSWD.getCategory())) {
                    operationAttributes.add(attribute);
                }
            }
        }
        return operationAttributes;
    }

    /**
     * @param multiDoc
     * @return a <code>List</code> with the document-format, document-name and last-document
     */
    public final static AttributeSet docOperationAttributes(SimpleMultiDoc multiDoc) {
        AttributeSet operationAttributes = new HashAttributeSet();
        try {
            operationAttributes = docOperationAttributes(multiDoc.getDoc());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Could not get Doc from multiDoc", e);
        }
        LastDocument lastDocument;
        if (multiDoc.isLast()) {
            lastDocument = LastDocument.TRUE;
        } else {
            lastDocument = LastDocument.FALSE;
        }
        operationAttributes.add(lastDocument);
        return operationAttributes;
    }

    /**
     * @param doc
     * @return a <code>List</code> with the document-format and document-name
     */
    public final static AttributeSet docOperationAttributes(Doc doc) {
        AttributeSet operationAttributes = new HashAttributeSet();
        if (doc.getAttributes() != null) {
            DocumentName docName = (DocumentName) doc.getAttributes().get(IppAttributeName.DOCUMENT_NAME.getCategory());
            if (docName != null) {
                operationAttributes.add(docName);
            }
        }
        operationAttributes.add(new DocumentFormat(doc.getDocFlavor().getMimeType(), Locale.getDefault()));
        return operationAttributes;
    }

    /**
     * @param operationAttributes2
     * @return
     */
    public final static Attribute[] getOrderedOperationAttributeArray(AttributeSet operationAttributes2) {
        AttributeSet copy = new HashAttributeSet(operationAttributes2);
        Attribute[] attributes = new Attribute[copy.size()];
        int i = 0;
        // start with Charset
        Class category = IppAttributeName.CHARSET.getCategory();
        if (copy.containsKey(category)) {
            attributes[i] = copy.get(category);
            copy.remove(category);
            i++;
        }
        // start with Charset
        category = IppAttributeName.NATURAL_LANGUAGE.getCategory();
        if (copy.containsKey(category)) {
            attributes[i] = copy.get(category);
            copy.remove(category);
            i++;
        }
        // add the rest
        Attribute[] remaining = copy.toArray();
        for (int j = 0; j < remaining.length; j++) {
            attributes[i+j] = remaining[j];
        }
        return attributes;
    }
    
    

}
