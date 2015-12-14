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
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.print.attribute.Attribute;
import javax.print.attribute.DateTimeSyntax;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.print.attribute.TextSyntax;
import javax.print.attribute.URISyntax;

import de.lohndirekt.print.attribute.ipp.Charset;
import de.lohndirekt.print.attribute.ipp.NaturalLanguage;

public final class AttributeWriter {
    
    private final static int MILIS_IN_MINUTE = 1000*60;
    private final static int MILIS_IN_HOUR =MILIS_IN_MINUTE*60;
    private static Map encodings = new HashMap();
    
    public final static Charset DEFAULT_CHARSET = Charset.ISO_8859_1;
    
	/**
	 * @param bytes
	 * @param byteNr
	 * @param attribute
	 * @return
	 */
	private static void fillName(ByteArrayOutputStream out, Attribute attribute) {
		String name = attribute.getName();
		fillName(out, name);
	}

	private static void fillName(ByteArrayOutputStream out, String name) {
		//name length
        writeInt2(name.length(), out);
		//name of attribute
		writeString(name, out);
        
	}

	/**
	 * @param attribute
	 * @param bytes
	 * @return
	 */
	private static void attributeBytes(IntegerSyntax attribute, ByteArrayOutputStream out)   {
		//value tag
		out.write((byte) IppValueTag.INTEGER.getValue());
		//name
		fillName(out, (Attribute) attribute);
		//value length (always 4 bytes)
        writeInt2(4, out);
        //value
        writeInt4(attribute.getValue(), out);
	}

	/**
	 * @param attribute
	 * @param bytes
	 * @return
	 */
	private static void attributeBytes(SetOfIntegerSyntax attribute, ByteArrayOutputStream out)   {
		int[][] members = attribute.getMembers();
		for (int i = 0; i < members.length; i++) {
			int[] range = members[i];
			int lowerBound = range[0];
			int upperBound = range[1];

			//value tag
			out.write((byte) IppValueTag.RANGE.getValue());
			//name
			if (i == 0) {
				fillName(out, (Attribute) attribute);
			} else {
				fillName(out, "");
			}
			//value length (always 8 bytes)
            writeInt2(8, out);

			//(Lower bound first)Integer in 4 bytes
            writeInt4(lowerBound, out);
			//(Upper bound)Integer in 4 bytes
            writeInt4(upperBound, out);
		}
	}
	/**
	 * @param attribute
	 * @param bytes
	 * @return
	 */
	private static void attributeBytes(TextSyntax attribute, ByteArrayOutputStream out)  {
		// value tag
		if (attribute instanceof Charset) {
			out.write((byte) IppValueTag.CHARSET.getValue());
		} else if (attribute instanceof NaturalLanguage) {
			out.write((byte) IppValueTag.LANGUAGE.getValue());
		} else {
			out.write((byte) IppValueTag.NAME.getValue());
		}
		//name
		fillName(out, (Attribute) attribute);
		//value length 
        writeInt2(attribute.toString().length(), out);
        writeNameAndTextString(attribute.toString(), out);
	}

	/**
	 * @param attribute
	 * @param bytes
	 * @return
	 */
	private static void attributeBytes(EnumSyntax attribute, ByteArrayOutputStream out)  {
		// Value tag
		out.write((byte) IppValueTag.TEXT.getValue());
		// Name
		fillName(out, (Attribute) attribute);
		// value length
        writeInt2(attribute.toString().length(), out);
        writeString(attribute.toString(), out);
	}
    
	/**
     * 
     * uses UTC time, not local time zone to encode the date
     * 
	 * @param attribute
	 * @param bytes
	 * @return
	 */
	private static void attributeBytes(DateTimeSyntax attribute, ByteArrayOutputStream out)  {
        
        
		//Value tag
		out.write((byte) IppValueTag.DATE.getValue());
		//Name
		fillName(out, (Attribute) attribute);
		//Value length (always 11 bytes)
        writeInt2(11, out);
        
		Date date = attribute.getValue();
        TimeZone zone = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(zone);
		cal.setTime(date);
        
        int year = cal.get(GregorianCalendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int deci = cal.get(Calendar.MILLISECOND) / 100;
        int offset = zone.getOffset(cal.getTimeInMillis());
		int hoursFromUtc = offset / MILIS_IN_HOUR;
		int minutesFromUtc = offset % MILIS_IN_HOUR / MILIS_IN_MINUTE;
		char direction = '+';
		if (hoursFromUtc < 0) {
			direction = '-';
			hoursFromUtc *= -1;
		}
        writeInt2(year, out);
		out.write((byte) month);
		out.write((byte) day);
		out.write((byte) hour);
		out.write((byte) minute);
		out.write((byte) second);
		out.write((byte) deci);
		out.write((byte) direction);
		out.write((byte) hoursFromUtc);
		out.write((byte) minutesFromUtc);
	}
	/**
	 * @param attribute
	 * @param bytes
	 * @return
	 */
	private static void attributeBytes(URISyntax attribute, ByteArrayOutputStream out)  {
		//Value tag
		out.write((byte) IppValueTag.URI.getValue());
		//Name
		fillName(out, (Attribute) attribute);
		//value length
        writeInt2(attribute.toString().length(), out);
		writeString(attribute.toString(), out);
	}

	/**
	 * @param attribute
	 * @param bytes
	 * @return
	 */
	private static void attributeBytes(ResolutionSyntax attribute, ByteArrayOutputStream out)  {
		//Value tag
		out.write((byte) IppValueTag.INTEGER.getValue());
		//Name
		fillName(out, (Attribute) attribute);
        //Value length (always 9)
        out.write((byte) 0);
        out.write((byte) 9);
		int feedResolution = attribute.getFeedResolution(ResolutionSyntax.DPI);
		int crossFeedResolution = attribute.getCrossFeedResolution(ResolutionSyntax.DPI);
        //(Lower bound first)Integer in 4 bytes
		writeInt4(crossFeedResolution, out);
		//(Upper bound)Integer in 4 bytes
		writeInt4(feedResolution, out);
		out.write((byte) ResolutionSyntax.DPI);
	}
    
    public static void writeInt4(int value, ByteArrayOutputStream out){
        out.write((byte) ((value & 0xff000000) >> 24));
        out.write((byte) ((value & 0xff0000) >> 16));
        out.write((byte) ((value & 0xff00) >> 8));
        out.write((byte) (value & 0xff));     
    }
    
    public static void writeInt2(int value, ByteArrayOutputStream out){
        out.write((byte) ((value & 0xff00) >> 8));
        out.write((byte) (value & 0xff));     
    }
    
    public static void writeString(String value, ByteArrayOutputStream out, String charsetName) {
        byte[] bytes;
		try {
			bytes = value.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(charsetName + " encoding not supported by JVM");
		}
        out.write(bytes, 0, bytes.length);
    }
    
    public static void writeString(String value, ByteArrayOutputStream out){
        writeString(value, out, Charset.US_ASCII.getValue());
    }
    
    public static void writeNameAndTextString(String value, ByteArrayOutputStream out) {
        writeString(value, out, DEFAULT_CHARSET.getValue());
    }

	/**
     * @param attribute
     * @return
     */
	public static void attributeBytes(Attribute attribute, ByteArrayOutputStream out) {
        if (attribute instanceof IntegerSyntax) {
			attributeBytes((IntegerSyntax) attribute, out);
		} else if (attribute instanceof TextSyntax) {
			attributeBytes((TextSyntax) attribute, out);
		} else if (attribute instanceof DateTimeSyntax) {
			attributeBytes((DateTimeSyntax) attribute, out);
		} else if (attribute instanceof ResolutionSyntax) {
			attributeBytes((ResolutionSyntax) attribute, out);
		} else if (attribute instanceof SetOfIntegerSyntax) {
			attributeBytes((SetOfIntegerSyntax) attribute, out);
		} else if (attribute instanceof EnumSyntax) {
			attributeBytes((EnumSyntax) attribute, out);
		} else if (attribute instanceof URISyntax) {
			attributeBytes((URISyntax) attribute, out);
		} else {
			throw new IllegalArgumentException("The given attribute is of an unknown SubType (only IntegerSyntax, TextSyntax, DateTimeSyntax, ResolutionSyntax, SetOfIntegerSyntax, EnumSyntax and URISyntax supported)");
		}
	}

}