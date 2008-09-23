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
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.print.CancelablePrintJob;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.RequestingUserName;
import javax.print.attribute.standard.Sides;

import de.lohndirekt.print.IppPrintServiceLookup;
import de.lohndirekt.print.attribute.auth.RequestingUserPassword;

/**
 * @author sefftinge
 * 
 *  
 */
public class SimpleDocExample {

    /**
     * @param args
     * @throws URISyntaxException
     * @throws PrintException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException,
            PrintException, IOException {

        // setting cups properties
        System.getProperties().setProperty(IppPrintServiceLookup.URI_KEY,
                Messages.getString("cups.uri")); //$NON-NLS-1$
        System.getProperties().setProperty(IppPrintServiceLookup.USERNAME_KEY,
                Messages.getString("cups.username")); //$NON-NLS-1$
        System.getProperties().setProperty(IppPrintServiceLookup.PASSWORD_KEY,
                Messages.getString("cups.password")); //$NON-NLS-1$

        // get the PrintServices
        PrintService[] services = new IppPrintServiceLookup().getPrintServices();

        // get the first Printer
        if (services.length > 0) {
            PrintService service = null;
            System.out.println("Please choose a print service:");
            for (int i = 0; i < services.length; i++) {
                PrintService service2 = services[i];
                System.out.println("[" + i + "] : " + service2.getName());
            }
            // let the user choose a service
            while (true) {
                int serviceToUse = Integer.valueOf(readStdIn()).intValue();
                if (serviceToUse < 0 || serviceToUse >= services.length) {
                    System.out.println("Bitte eine Zahl zwischen 0 und "
                            + (services.length - 1) + " eingeben.");
                } else {
                    service = services[serviceToUse];
                    break;
                }
            }

            // ask for username
            System.out.print("Username : ");
            String username = readStdIn().trim();
            String password = null;
            if (username != null && username.trim().length() > 0) {
                System.out.print("Password : ");
                password = readStdIn().trim();
            }

            System.out.println("Printing on: " + service.getName());
            // create a job
            CancelablePrintJob job = (CancelablePrintJob) service
                    .createPrintJob();

            // set the job attributes
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

            if (username != null && username.trim().length() > 0) {
                RequestingUserName usernameAttr = new RequestingUserName(
                        username, Locale.GERMANY);
                RequestingUserPassword userpassAttr = new RequestingUserPassword(
                        password, Locale.GERMANY);
                attributes.add(usernameAttr);
                attributes.add(userpassAttr);
            }
            // we just want one copy
            Copies copies = new Copies(1);
            attributes.add(copies);
            // we want duplex printing
            Sides sides = Sides.DUPLEX;
            attributes.add(sides);
            // print it on the main media tray
			// Media media = MediaTray.MAIN;
            // If you have special Mediatrays (like most printers)
            // you can use the class LdMediaTray and give a String to the
            // constructor like
            // new LdMediaTray("Optional2");
			// attributes.add(media);

            // Now create a document
            File testFile = new File(Messages.getString("pdfdocument.1"));
            InputStream stream = new FileInputStream(testFile);
            Doc doc = new SimpleDoc(stream, DocFlavor.INPUT_STREAM.PDF,
                    new HashDocAttributeSet());

            // finally the print action
            try {
                // we are setting the doc and the job attributes
                job.print(doc, attributes);
                System.out.println("printing successfull...");
            } catch (PrintException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("no Services found!");
        }
    }

    /**
     * @return
     */
    private static String readStdIn() {
        StringBuffer sb = new StringBuffer();
        int ch;
        try {
            while ((ch = System.in.read()) != 10) {
                sb.append((char) ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}