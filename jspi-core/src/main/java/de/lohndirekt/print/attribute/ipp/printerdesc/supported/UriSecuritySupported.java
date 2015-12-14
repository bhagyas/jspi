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
package de.lohndirekt.print.attribute.ipp.printerdesc.supported;

import java.util.Locale;

import javax.print.attribute.SupportedValuesAttribute;
import javax.print.attribute.TextSyntax;

public class UriSecuritySupported extends TextSyntax implements SupportedValuesAttribute {

	public static UriSecuritySupported NONE = new UriSecuritySupported("none", Locale.getDefault());
	public static UriSecuritySupported SSl3 =
		new UriSecuritySupported("ssl3", Locale.getDefault());
	public static UriSecuritySupported TSL = new UriSecuritySupported("tsl", Locale.getDefault());

	/**
	 * @param value
	 * @param locale
	 */
	public UriSecuritySupported(String value, Locale locale) {
		super(value, locale);
	}

	/**
	 *
	 */

	public Class getCategory() {
		return this.getClass();
	}

	/**
	 *
	 */

	public String getName() {
		return UriSecuritySupported.getIppName();
	}

	/**
	 * @return
	 */
	public static String getIppName() {
		return "uri-security-supported";
	}

}
