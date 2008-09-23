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
package de.lohndirekt.print.examples;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.MultiDocPrintJob;
import javax.print.MultiDocPrintService;
import javax.print.PrintException;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.Sides;

import de.lohndirekt.print.IppPrintServiceLookup;
import de.lohndirekt.print.SimpleMultiDoc;

public class MultiDocExample {

    public static void main(String[] args)
        throws URISyntaxException, FileNotFoundException, PrintException, MalformedURLException {
        // setting cups properties
        System.getProperties().setProperty(IppPrintServiceLookup.URI_KEY, Messages.getString("cups.uri")); //$NON-NLS-1$
        System.getProperties().setProperty(IppPrintServiceLookup.USERNAME_KEY, Messages.getString("cups.username")); //$NON-NLS-1$
        System.getProperties().setProperty(IppPrintServiceLookup.PASSWORD_KEY, Messages.getString("cups.password")); //$NON-NLS-1$

        // get the PrintServices
        MultiDocPrintService[] services = new IppPrintServiceLookup().getMultiDocPrintServices(null, null);

        // get the first Printer
        if (services.length > 0) {
            MultiDocPrintService service = services[0];
            System.out.println("Printing on: " + service.getName());
            // create a job
            MultiDocPrintJob job = service.createMultiDocPrintJob();

            // set the job attributes
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            // we just want one copy
            Copies copies = new Copies(1);
            attributes.add(copies);
            // we want duplex printing
            Sides sides = Sides.DUPLEX;
            attributes.add(sides);
            // print it on the main media tray
            Media media = MediaTray.MAIN;
            // If you have special Mediatrays (like most printers) 
            // you can use the class LdMediaTray and give a String to the constructor like
            // new LdMediaTray("Optional2");
            attributes.add(media);

            // Now create some documents
            File testFile = new File(Messages.getString("pdfdocument.1"));
            InputStream stream = new FileInputStream(testFile);
            Doc doc = new SimpleDoc(stream, DocFlavor.INPUT_STREAM.PDF, new HashDocAttributeSet());
            File testFile2 = new File(Messages.getString("pdfdocument.2"));
            InputStream stream2 = new FileInputStream(testFile2);
            Doc doc2 = new SimpleDoc(stream2, DocFlavor.INPUT_STREAM.PDF, new HashDocAttributeSet());
            // now we need a MultiDoc
            SimpleMultiDoc multiDoc = SimpleMultiDoc.create(doc);
            // finally the print action
            try {
                // we are setting the doc and the job attributes
                job.print(multiDoc, attributes);

                // now add several docs to your mutliDoc
                // cups waits for the document with the attribute last-document = true
                // wich you can set like this:
                boolean lastDocument = true;
                multiDoc.setNext(doc2, lastDocument);

                System.out.println("printing successfull...");
            } catch (PrintException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("no Services found!");
        }
    }

}
