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
package de.lohndirekt.print.attribute.ipp;

import java.util.Locale;

import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.TextSyntax;

public class Charset extends TextSyntax implements PrintRequestAttribute {

	public final static Charset ISO_8859_1 = new Charset("iso-8859-1", Locale.getDefault());
	public final static Charset ISO_8859_15 = new Charset("iso-8859-15", Locale.getDefault());
	public final static Charset UTF_8 = new Charset("utf-8", Locale.getDefault());
    public final static Charset US_ASCII = new Charset("us-ascii", Locale.getDefault());

	/**
	 * @param value
	 */
	public Charset(String name, Locale locale) {
		super(name, locale);
	}

	/**
	 *
	 */

	public Class getCategory() {
		return Charset.class;
	}

	/**
	 *
	 */

	public static String getIppName() {
		return "attributes-charset";
	}

	public String getName() {
		return Charset.getIppName();
	}

}
