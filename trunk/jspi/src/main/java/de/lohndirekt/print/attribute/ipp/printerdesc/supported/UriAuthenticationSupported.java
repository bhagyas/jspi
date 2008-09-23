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

public class UriAuthenticationSupported extends TextSyntax implements SupportedValuesAttribute {

	public static UriAuthenticationSupported NONE = new UriAuthenticationSupported("none", Locale.getDefault());
	public static UriAuthenticationSupported REQUESTING_USER_NAME =
		new UriAuthenticationSupported("requesting-user-name", Locale.getDefault());
	public static UriAuthenticationSupported BASIC = new UriAuthenticationSupported("basic", Locale.getDefault());
	public static UriAuthenticationSupported DIGEST = new UriAuthenticationSupported("digest", Locale.getDefault());
	public static UriAuthenticationSupported CERTIFICATION = new UriAuthenticationSupported("certification", Locale.getDefault());

	/**
	 * @param value
	 * @param locale
	 */
	public UriAuthenticationSupported(String value, Locale locale) {
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
		return UriAuthenticationSupported.getIppName();
	}

	/**
	 * @return
	 */
	public static String getIppName() {
		return "uri-authentication-supported";
	}

}
