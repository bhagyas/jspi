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

/**
 *Bezeichnungen übernommen aus <link>www.ietf.org/rfc/rfc2910.txt</link>
 *Tags ohne Bezeichnung sind dort nicht aufgeführt, werden aber in der Cups-API verwendet
 */
public class IppValueTag {
	
	private String description;
	private int value = 0;

	public static final IppValueTag UNSUPPORTED_VALUE = new IppValueTag(0x10, "unsupported");
	public static final IppValueTag DEFAULT = new IppValueTag(0x11, "'default' ");
	public static final IppValueTag UNKNOWN = new IppValueTag(0x12, "unknown");
	public static final IppValueTag NOVALUE = new IppValueTag(0x13, "no-value");
	public static final IppValueTag NOTSETTABLE = new IppValueTag(0x15, "");
	public static final IppValueTag DELETEATTR = new IppValueTag(0x16, "");
	public static final IppValueTag ADMINDEFINE = new IppValueTag(0x17, "");
	public static final IppValueTag INTEGER = new IppValueTag(0x21, "integer");
	public static final IppValueTag BOOLEAN = new IppValueTag(0x22, "boolean");
	public static final IppValueTag ENUM = new IppValueTag(0x23, "enum");
	public static final IppValueTag STRING = new IppValueTag(0x30, "octetString");
	public static final IppValueTag DATE = new IppValueTag(0x31, "dateTime");
	public static final IppValueTag RESOLUTION = new IppValueTag(0x32, "resolution");
	public static final IppValueTag RANGE = new IppValueTag(0x33, "rangeOfInteger");
	public static final IppValueTag BEGIN_COLLECTION = new IppValueTag(0x34, "");
	public static final IppValueTag TEXTLANG = new IppValueTag(0x35, "resolution");
	public static final IppValueTag NAMELANG = new IppValueTag(0x36, "nameWithLanguage");
	public static final IppValueTag END_COLLECTION = new IppValueTag(0x37, "");
	public static final IppValueTag TEXT = new IppValueTag(0x41, "textWithoutLanguage");
	public static final IppValueTag NAME = new IppValueTag(0x42, "nameWithoutLanguage");
	public static final IppValueTag KEYWORD = new IppValueTag(0x44, "keyword");
	public static final IppValueTag URI = new IppValueTag(0x45, "uri");
	public static final IppValueTag URISCHEME = new IppValueTag(0x46, "uriScheme");
	public static final IppValueTag CHARSET = new IppValueTag(0x47, "charset");
	public static final IppValueTag LANGUAGE = new IppValueTag(0x48, "naturalLanguage");
	public static final IppValueTag MIMETYPE = new IppValueTag(0x49, "mimeMediaType");
	public static final IppValueTag MEMBERNAME = new IppValueTag(0x4A, "");
	public static final IppValueTag MASK = new IppValueTag(0x7FFFFFFF, "");
	public static final IppValueTag COPY = new IppValueTag(0x80000001, "");

	private IppValueTag(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return this.value;
	}
	
	
	public String toString() {
		return this.description;
	}

}
