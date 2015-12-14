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

import javax.print.attribute.DocAttribute;
import javax.print.attribute.EnumSyntax;

public class LastDocument extends EnumSyntax implements DocAttribute {

	public final static LastDocument FALSE = new LastDocument(0);
	public final static LastDocument TRUE = new LastDocument(1);

	/**
	 *
	 */
	protected EnumSyntax[] getEnumValueTable() {
		return new EnumSyntax[] { FALSE, TRUE};
	}

	/**
	 *
	 */
	protected int getOffset() {
		return 0;
	}

	/**
	 *
	 */
	protected String[] getStringTable() {
		return new String[] { "false", "true" };
	}

	/**
	 * @param value
	 */
	public LastDocument(int value) {
		super(value);
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
		return LastDocument.getIppName();
	}

	/**
	 * @return
	 */
	public static String getIppName() {
		return "last-document";
	}

}
