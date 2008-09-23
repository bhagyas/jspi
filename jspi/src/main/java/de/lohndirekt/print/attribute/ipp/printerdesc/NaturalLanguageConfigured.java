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
package de.lohndirekt.print.attribute.ipp.printerdesc;

import java.util.Locale;

import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.TextSyntax;

public class NaturalLanguageConfigured extends TextSyntax implements PrintServiceAttribute {

	/**
	 * @param value
	 */
	public NaturalLanguageConfigured(String name, Locale locale) {
		super(name, locale);
	}

	/**
	 *
	 */
	public Class getCategory() {
		return NaturalLanguageConfigured.class;
	}

	/**
	 *
	 */
	public static String getIppName() {
		return "natural-language-configured";
	}

	public String getName() {
		return NaturalLanguageConfigured.getIppName();
	}

}
