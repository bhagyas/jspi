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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.print.attribute.standard.CopiesSupported;
import javax.print.attribute.standard.JobHoldUntil;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.JobPriority;
import javax.print.attribute.standard.JobState;
import javax.print.attribute.standard.PrinterResolution;
import javax.print.attribute.standard.PrinterURI;

import junit.framework.TestCase;
import de.lohndirekt.print.attribute.cups.JobPageLimit;
import de.lohndirekt.print.attribute.ipp.Charset;
import de.lohndirekt.print.attribute.ipp.jobdesc.JobUri;
import de.lohndirekt.print.attribute.ipp.printerdesc.PrinterCurrentTime;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.PageRangesSupported;

/**
 * @author ld-development
 * 
 * 
 */
public class AttributeWriterTest extends TestCase {
    
    ByteArrayOutputStream out;
    
    protected void setUp() throws Exception {
            super.setUp();
            out = new ByteArrayOutputStream();
        }


    private Date date(String timeZoneId){
        TimeZone zone = TimeZone.getTimeZone(timeZoneId);
        Calendar cal = Calendar.getInstance(zone);

        cal.set(Calendar.YEAR, 2003);
        cal.set(Calendar.MONTH, Calendar.JUNE);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MINUTE, 48);
        cal.set(Calendar.SECOND, 9);
        cal.set(Calendar.MILLISECOND, 47);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        Date date = cal.getTime();
        
        return date;
    }
    /**
     * 
     */
    public void testAttributeWriterForDateTimeSyntax() throws IOException {
    	
        //testing for TimeZone Asia/Omsk
        //1:48:09 P.M. in Greenwich obviously means 1:48:09 P.M. in Greenwich
        Date d = date("UTC");
        
        // testing lohndirekt attribute
        AttributeWriter.attributeBytes(new PrinterCurrentTime(d), out);
        assertEquals(new byte[] { 49, 0, 20, 112, 114, 105, 110, 116, 101, 114, 45, 99, 117, 114, 114, 101, 110, 116, 45, 116, 105, 109, 101, 0, 11, 7, -45, 6, 1, 13, 48, 9, 0, 43, 0, 0 }, out.toByteArray());
        
        //testing javax.print attribute
        out.reset();
        AttributeWriter.attributeBytes(new JobHoldUntil(d), out);
        assertEquals(new byte[] { 49, 0, 14, 106, 111, 98, 45, 104, 111, 108, 100, 45, 117, 110, 116, 105, 108, 0, 11, 7, -45, 6, 1, 13, 48, 9, 0, 43, 0, 0 }, out.toByteArray());
        
        //testing for TimeZone Asia/Omsk
        //1:48:09 P.M. in Omsk means 6:48:09 A.M. in Greenwich
        d = date("Asia/Omsk");
        
        // testing lohndirekt attribute
        out.reset();
        AttributeWriter.attributeBytes(new PrinterCurrentTime(d), out);
        assertEquals(new byte[] { 49, 0, 20, 112, 114, 105, 110, 116, 101, 114, 45, 99, 117, 114, 114, 101, 110, 116, 45, 116, 105, 109, 101, 0, 11, 7, -45, 6, 1, 6, 48, 9, 0, 43, 0, 0 }, out.toByteArray());

        //testing javax.print attribute
        out.reset();
        AttributeWriter.attributeBytes(new JobHoldUntil(d), out);
        assertEquals(new byte[] { 49, 0, 14, 106, 111, 98, 45, 104, 111, 108, 100, 45, 117, 110, 116, 105, 108, 0, 11, 7, -45, 6, 1, 6, 48, 9, 0, 43, 0, 0 }, out.toByteArray());
    }

    /**
     * 
     */
    public void testAttributeWriterForEnumSyntax() throws IOException {
        // testing lohndirekt attribute
        AttributeWriter.attributeBytes(PageRangesSupported.SUPPORTED, out);
        assertEquals(
            new byte[] { 65, 0, 21, 112, 97, 103, 101, 45, 114, 97, 110, 103, 101, 115, 45, 115, 117, 112, 112, 111, 114, 116, 101, 100, 0, 13, 110, 111, 116, 32, 115, 117, 112, 112, 111, 114, 116, 101, 100 },
            out.toByteArray());

        // testing javax.print attribute
        out.reset();
        AttributeWriter.attributeBytes(JobState.CANCELED, out);
        assertEquals(new byte[] { 65, 0, 9, 106, 111, 98, 45, 115, 116, 97, 116, 101, 0, 8, 99, 97, 110, 99, 101, 108, 101, 100 }, out.toByteArray());

    }
    /**
     * 
     */
    public void testAttributeWriterForIntegerSyntax() throws IOException {
        // testing lohndirekt attribute
        AttributeWriter.attributeBytes(new JobPageLimit(3), out);
        assertEquals(new byte[] { 33, 0, 14, 106, 111, 98, 45, 112, 97, 103, 101, 45, 108, 105, 109, 105, 116, 0, 4, 0, 0, 0, 3 }, out.toByteArray());

        // testing javax.print attribute
        out.reset();
        AttributeWriter.attributeBytes(new JobPriority(3), out);
        assertEquals(new byte[] { 33, 0, 12, 106, 111, 98, 45, 112, 114, 105, 111, 114, 105, 116, 121, 0, 4, 0, 0, 0, 3 }, out.toByteArray());
    }
    /**
     * 
     */
    public void testAttributeWriterForResolutionSyntax() throws IOException {
        // testing javax.print attribute
        AttributeWriter.attributeBytes(new PrinterResolution(800, 800, PrinterResolution.DPI), out);
        assertEquals(
            new byte[] { 33, 0, 18, 112, 114, 105, 110, 116, 101, 114, 45, 114, 101, 115, 111, 108, 117, 116, 105, 111, 110, 0, 9, 0, 0, 3, 32, 0, 0, 3, 32, 100},
        out.toByteArray());
    }
    /**
     * 
     */
    public void testAttributeWriterForSetOfIntegerSyntax() throws IOException {
        // testing javax.print attribute
        AttributeWriter.attributeBytes(new CopiesSupported(1,5), out);
        assertEquals(
            new byte[] { 51, 0, 16, 99, 111, 112, 105, 101, 115, 45, 115, 117, 112, 112, 111, 114, 116, 101, 100, 0, 8, 0, 0, 0, 1, 0, 0, 0, 5 },
        out.toByteArray());
    }
    /**
     * 
     */
    public void testAttributeWriterForTextSyntax() throws IOException {
        // testing lohndirekt attribute
        AttributeWriter.attributeBytes(Charset.ISO_8859_15, out);
        assertEquals(
            new byte[] { 71, 0, 18, 97, 116, 116, 114, 105, 98, 117, 116, 101, 115, 45, 99, 104, 97, 114, 115, 101, 116, 0, 11, 105, 115, 111, 45, 56, 56, 53, 57, 45, 49, 53 },
        out.toByteArray());

        // testing javax.print attribute
        out.reset();
        AttributeWriter.attributeBytes(new JobName("testJob",Locale.GERMANY), out);
        assertEquals(
            new byte[] { 66, 0, 8, 106, 111, 98, 45, 110, 97, 109, 101, 0, 7, 116, 101, 115, 116, 74, 111, 98 },
        out.toByteArray());
    }
    /**
     * 
     */
    public void testAttributeWriterForURISyntax() throws URISyntaxException, IOException {
        URI uri = new URI("http://www.google.de/");
        // testing lohndirekt attribute
        AttributeWriter.attributeBytes(new JobUri(uri), out);
        assertEquals(
            new byte[] { 69, 0, 7, 106, 111, 98, 45, 117, 114, 105, 0, 21, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 103, 111, 111, 103, 108, 101, 46, 100, 101, 47 },
        out.toByteArray());

        // testing javax.print attribute
        out.reset();
        AttributeWriter.attributeBytes(new PrinterURI(uri), out);
        assertEquals(
            new byte[] { 69, 0, 11, 112, 114, 105, 110, 116, 101, 114, 45, 117, 114, 105, 0, 21, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 103, 111, 111, 103, 108, 101, 46, 100, 101, 47 },
        out.toByteArray());
    }

    /**
     * @param expected
     * @param actual
     */
    private void assertEquals(byte[] expected, byte[] actual) {
        assertEquals(new String(expected), new String(actual));
    }


	
}
