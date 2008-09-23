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
import java.net.URISyntaxException;

import javax.print.PrintService;

import de.lohndirekt.print.IppPrintServiceLookup;

/**
 * @author sefftinge
 *
 */
public class TestJarLookup {

    public static void main(String[] args) throws URISyntaxException {
        System.getProperties().setProperty("de.lohndirekt.print.IppPrintService.uri", "192.168.0.60:631");
        testServices();
    }

    private static void testServices() throws URISyntaxException {
        PrintService[] services = new IppPrintServiceLookup(new URI("ipp://192.168.0.60:631"),"username","password").getPrintServices();
        for (int i = 0; i < services.length; i++) {
            PrintService service = services[i];
            System.out.println(service.getName());
        }
    }
}
