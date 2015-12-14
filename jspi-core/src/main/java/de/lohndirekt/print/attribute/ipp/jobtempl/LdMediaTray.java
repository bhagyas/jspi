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
package de.lohndirekt.print.attribute.ipp.jobtempl;

import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.standard.Media;

public class LdMediaTray extends Media implements PrintRequestAttribute, PrintJobAttribute {

	private String name;

	/**
	 * @param trayName
	 * @throws NullPointerException if trayName is null
	 */
	public LdMediaTray(String trayName) {
		super(0);
		if (trayName == null) {
			throw new NullPointerException("TrayName must not be null");
		}
		this.name = trayName;
	}

	public String toString() {
		return this.name;
	}

}
