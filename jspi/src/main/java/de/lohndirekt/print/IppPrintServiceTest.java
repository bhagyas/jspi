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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.print.PrintService;
import javax.print.attribute.Attribute;

import junit.framework.TestCase;
import de.lohndirekt.print.attribute.IppAttributeName;
import de.lohndirekt.print.test.IppRequestTestImpl;

/**
 * @author bpusch
 *
 */
public class IppPrintServiceTest extends TestCase {

    PrintService service;
    Logger log;

    /**
     * Constructor for IppPrintServiceTest.
     * @param arg0
     */
    public IppPrintServiceTest(String arg0) {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(IppRequestFactory.IPP_REQUEST_IMPL_KEY, IppRequestTestImpl.class.getName());
        IppPrintServiceLookup lookup = new IppPrintServiceLookup(new URI("http://127.0.0.1"), "", "");
        PrintService[] services = lookup.getPrintServices();
        this.service = services[0];
        this.log = Logger.getLogger(this.getName());
    }

    public void testGetSupportedAttributeCategories() {
        Class[] categories = this.service.getSupportedAttributeCategories();
        assertNotNull(categories);
        List cats = Arrays.asList(categories);
        assertTrue(cats.contains(IppAttributeName.JOB_PRIORIY.getCategory()));
        assertTrue(cats.contains(IppAttributeName.JOB_HOLD_UNTIL.getCategory()));
        assertTrue(cats.contains(IppAttributeName.JOB_SHEETS.getCategory()));
        assertTrue(cats.contains(IppAttributeName.MULTIPLE_DOCUMENT_HANDLING.getCategory()));
        assertTrue(cats.contains(IppAttributeName.COPIES.getCategory()));
        assertTrue(cats.contains(IppAttributeName.FINISHINGS.getCategory()));
        assertTrue(cats.contains(IppAttributeName.SIDES.getCategory()));
        assertTrue(cats.contains(IppAttributeName.NUMBER_UP.getCategory()));
        assertTrue(cats.contains(IppAttributeName.ORIENTATION_REQUESTED.getCategory()));
        assertTrue(cats.contains(IppAttributeName.MEDIA.getCategory()));
        assertTrue(cats.contains(IppAttributeName.COMPRESSION.getCategory()));
        assertTrue(cats.contains(IppAttributeName.JOB_K_OCTETS.getCategory()));
        assertTrue(cats.contains(IppAttributeName.JOB_IMPRESSIONS.getCategory()));
        assertTrue(cats.contains(IppAttributeName.JOB_MEDIA_SHEETS.getCategory()));
    }

    public void testIsAttributeCategorySupported() {
        Class category = IppAttributeName.JOB_SHEETS.getCategory();
        boolean supported = this.service.isAttributeCategorySupported(category);
        assertEquals(Arrays.asList(this.service.getSupportedAttributeCategories()).contains(category), supported);
        
    }

    public void testGetSupportedAttributeValues() {
        Class category = IppAttributeName.MEDIA.getCategory();
        Attribute[] attrs = (Attribute[]) this.service.getSupportedAttributeValues(category, null, null);
        assertEquals("Should be 2 media-supported attributes", 2, attrs.length);
        for (int j = 0; j < attrs.length; j++) {
            Attribute attribute = attrs[j];
            assertEquals(IppAttributeName.MEDIA_SUPPORTED.getCategory(), attribute.getCategory());
        }
        category = IppAttributeName.COMPRESSION.getCategory();
        attrs = (Attribute[]) this.service.getSupportedAttributeValues(category, null, null);
        for (int j = 0; j < attrs.length; j++) {
            Attribute attribute = attrs[j];
            assertEquals(IppAttributeName.COMPRESSION_SUPORTED.getCategory(), attribute.getCategory());
        }
        category = IppAttributeName.JOB_PRIORIY.getCategory();
        attrs = (Attribute[]) this.service.getSupportedAttributeValues(category, null, null);
        assertEquals("Should be 1 job-priority-supported attribute", 1, attrs.length);
        for (int j = 0; j < attrs.length; j++) {
            Attribute attribute = attrs[j];
            assertEquals(IppAttributeName.JOB_PRIORIY_SUPPORTED.getCategory(), attribute.getCategory());
        }
        category = IppAttributeName.SIDES.getCategory();
        attrs = (Attribute[]) this.service.getSupportedAttributeValues(category, null, null);
        assertEquals("Should be 2 sides-supported attributes", 2, attrs.length);
        for (int j = 0; j < attrs.length; j++) {
            Attribute attribute = attrs[j];
            assertEquals(IppAttributeName.SIDES_SUPPORTED.getCategory(), attribute.getCategory());
        }

    }
   
}