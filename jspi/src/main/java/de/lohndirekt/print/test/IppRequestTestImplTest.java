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
package de.lohndirekt.print.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.Attribute;

import junit.framework.TestCase;
import de.lohndirekt.print.IppRequest;
import de.lohndirekt.print.attribute.IppAttributeName;
import de.lohndirekt.print.attribute.ipp.UnknownAttribute;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OperationsSupported;

/**
 * @author bpusch
 *
 * 
 */
public class IppRequestTestImplTest extends TestCase {

    public void testSendGetPrinterAttributes() throws URISyntaxException, IOException {
        Class unknownAttributeCategory = new UnknownAttribute("x", Locale.getDefault()).getCategory();
        IppRequest request = new IppRequestTestImpl(new URI("http://127.0.0.1"), OperationsSupported.GET_PRINTER_ATTRIBUTES);
        Map attributes = request.send().getAttributes();
        for (Iterator iter = attributes.keySet().iterator(); iter.hasNext();) {
            Class category = (Class) iter.next();
            //Should not return any unknown Attributes
            assertFalse(category.equals(unknownAttributeCategory));
            Set attrs = (Set) attributes.get(category);
            assertNotNull(attrs);
            for (Iterator iterator = attrs.iterator(); iterator.hasNext();) {
                Attribute element = (Attribute) iterator.next();
                assertEquals(category, element.getCategory());
            }
        }
    }

    public void testSendCupsGetPrinter() throws URISyntaxException, IOException {
        IppRequest request = new IppRequestTestImpl(new URI("http://127.0.0.1"), OperationsSupported.CUPS_GET_PRINTERS);
        Map attributes = request.send().getAttributes();
        assertTrue("Response must contain an attribute of category printer-uri-spported",attributes.containsKey(IppAttributeName.PRINTER_URI_SUPPORTED.getCategory()));
    }

    public void testGetResponse() throws URISyntaxException, IOException {
        //response is now returned by send()
    }

}
