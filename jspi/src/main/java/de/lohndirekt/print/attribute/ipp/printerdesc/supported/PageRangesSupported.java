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
 */package de.lohndirekt.print.attribute.ipp.printerdesc.supported;

import javax.print.attribute.EnumSyntax;
import javax.print.attribute.SupportedValuesAttribute;


public class PageRangesSupported extends EnumSyntax implements SupportedValuesAttribute {

	public static final PageRangesSupported NOT_SUPPORTED = new PageRangesSupported(0);
	public static final PageRangesSupported SUPPORTED = new PageRangesSupported(1);
	
	/**
	 *
	 */

	protected EnumSyntax[] getEnumValueTable() {
		return new EnumSyntax[]{
			NOT_SUPPORTED,
			SUPPORTED
			
		};
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
		return new String[]{
			"supported" ,
			"not supported"
		};
	}

	/**
	 * @param value
	 */
	public PageRangesSupported(int value) {
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
		return PageRangesSupported.getIppName();
	}

	/**
	 * 
	 */
	public static String getIppName() {
		return "page-ranges-supported";
	}

}
