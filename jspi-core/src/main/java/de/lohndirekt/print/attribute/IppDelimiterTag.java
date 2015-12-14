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
 *Bezeichnungen bernommen aus <link>www.ietf.org/rfc/rfc2910.txt</link>
 *Tags ohne Bezeichnung sind dort nicht aufgefhrt, werden aber in der Cups-API verwendet
 */
public class IppDelimiterTag {
	
	private String description = "";
	private int value = 0;

	public static final IppDelimiterTag ZERO = new IppDelimiterTag(0x00, "");
	public static final IppDelimiterTag BEGIN_OPERATION_ATTRIBUTES = new IppDelimiterTag(0x01, "operation-attributes-tag");
	public static final IppDelimiterTag BEGIN_JOB_ATTRIBUTES = new IppDelimiterTag(0x02, "job-attributes-tag");
	public static final IppDelimiterTag END_ATTRIBUTES = new IppDelimiterTag(0x03, "end-of-attributes-tag");
	public static final IppDelimiterTag BEGIN_PRINTER_ATTRIBUTES = new IppDelimiterTag(0x04, "printer-attributes-tag");
	public static final IppDelimiterTag UNSUPPORTED_GROUP = new IppDelimiterTag(0x05, "unsupported-attributes-tag");
	public static final IppDelimiterTag SUBSCRIPTION = new IppDelimiterTag(0x06, "");
	public static final IppDelimiterTag EVENT_NOTIFICATION = new IppDelimiterTag(0x07, "");
	
	private IppDelimiterTag(int value, String description) {
			this.value = value;
			this.description = description;
		}
	
		public int getValue(){
			return this.value;
		}
		
		public String toString(){
			return this.description;
		}
	
}