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

import javax.print.attribute.EnumSyntax;
import javax.print.attribute.SupportedValuesAttribute;

public class OperationsSupported extends EnumSyntax implements SupportedValuesAttribute {

	public static final OperationsSupported CREATE_PRINTER_SUBSCRIPTION = new OperationsSupported(0x16);
	public static final OperationsSupported CREATE_JOB_SUBSCRIPTION = new OperationsSupported(0x17);

	public static final OperationsSupported CANCEL_SUBSCRIPTION = new OperationsSupported(0x1B);
	public static final OperationsSupported ACTIVATE_PRINTER = new OperationsSupported(0x28);

	/* 
	 *  IPP operations... as defined in section 4.4.15 in rfc 2911
	 */
	public static final OperationsSupported PRINT_JOB = new OperationsSupported(0x02);
	public static final OperationsSupported PRINT_URI = new OperationsSupported(0x03);
	public static final OperationsSupported VALIDATE_JOB = new OperationsSupported(0x04);
	public static final OperationsSupported CREATE_JOB = new OperationsSupported(0x05);
	public static final OperationsSupported SEND_DOCUMENT = new OperationsSupported(0x06);
	public static final OperationsSupported SEND_URI = new OperationsSupported(0x07);
	public static final OperationsSupported CANCEL_JOB = new OperationsSupported(0x08);
	public static final OperationsSupported GET_JOB_ATTRIBUTES = new OperationsSupported(0x09);
	public static final OperationsSupported PAUSE_PRINTER = new OperationsSupported(0x10);
	public static final OperationsSupported GET_JOBS = new OperationsSupported(0x0A);
	public static final OperationsSupported GET_PRINTER_ATTRIBUTES = new OperationsSupported(0x0B);
	public static final OperationsSupported HOLD_JOB = new OperationsSupported(0x0C);
	public static final OperationsSupported RELEASE_JOB = new OperationsSupported(0x0D);
	public static final OperationsSupported RESTART_JOB = new OperationsSupported(0x0E);
	public static final OperationsSupported RESUME_PRINTER = new OperationsSupported(0x11);
	public static final OperationsSupported PURGE_JOBS = new OperationsSupported(0x12);

	/*
	 * More standard IPP operations?
	 */
	public static final OperationsSupported SET_PRINTER_ATTRIBUTES = new OperationsSupported(0x13);
	public static final OperationsSupported SET_JOB_ATTRIBUTES = new OperationsSupported(0x14);
	public static final OperationsSupported GET_PRINTER_SUPPORTED_VALUES = new OperationsSupported(0x15);
	public static final OperationsSupported GET_SUBSCRIPTION_ATTRIBUTES = new OperationsSupported(0x18);
	public static final OperationsSupported GET_SUBSCRIPTIONS = new OperationsSupported(0x19);
	public static final OperationsSupported RENEW_SUBSCRIPTION = new OperationsSupported(0x1A);
	public static final OperationsSupported GET_NOTIFICATIONS = new OperationsSupported(0x1C);
	public static final OperationsSupported SEND_NOTIFICATIONS = new OperationsSupported(0x1D);
	public static final OperationsSupported GET_PRINT_SUPPORT_FILES = new OperationsSupported(0x21);
	public static final OperationsSupported ENABLE_PRINTER = new OperationsSupported(0x22);
	public static final OperationsSupported DISABLE_PRINTER = new OperationsSupported(0x23);
	public static final OperationsSupported PAUSE_PRINTER_AFTER_CURRENT_JOB = new OperationsSupported(0x24);
	public static final OperationsSupported HOLD_NEW_JOBS = new OperationsSupported(0x25);
	public static final OperationsSupported RELEASE_HELD_NEW_JOBS = new OperationsSupported(0x26);
	public static final OperationsSupported DEACTIVATE_PRINTER = new OperationsSupported(0x27);
	public static final OperationsSupported SHUTDOWN_PRINTER = new OperationsSupported(0x2A);
	public static final OperationsSupported STARTUP_PRINTER = new OperationsSupported(0x2B);
	public static final OperationsSupported REPROCESS_JOB = new OperationsSupported(0x2C);
	public static final OperationsSupported CANCEL_CURRENT_JOB = new OperationsSupported(0x2D);
	public static final OperationsSupported SUSPEND_CURRENT_JOB = new OperationsSupported(0x2E);
	public static final OperationsSupported RESTART_PRINTER = new OperationsSupported(0x29);
	public static final OperationsSupported RESUME_JOB = new OperationsSupported(0x2F);
	public static final OperationsSupported PROMOTE_JOB = new OperationsSupported(0x30);
	public static final OperationsSupported SCHEDULE_JOB_AFTER = new OperationsSupported(0x31);

	//Cups-specific values
	public static final OperationsSupported PRIVATE = new OperationsSupported(0x4000);
	public static final OperationsSupported CUPS_GET_DEFAULT = new OperationsSupported(0x4001);
	public static final OperationsSupported CUPS_GET_PRINTERS = new OperationsSupported(0x4002);
	public static final OperationsSupported CUPS_ADD_PRINTER = new OperationsSupported(0x4003);
	public static final OperationsSupported CUPS_DELETE_PRINTER = new OperationsSupported(0x4004);
	public static final OperationsSupported CUPS_GET_CLASSES = new OperationsSupported(0x4005);
	public static final OperationsSupported CUPS_ADD_CLASS = new OperationsSupported(0x4006);
	public static final OperationsSupported CUPS_DELETE_CLASS = new OperationsSupported(0x4007);
	public static final OperationsSupported CUPS_ACCEPT_JOBS = new OperationsSupported(0x4008);
	public static final OperationsSupported CUPS_REJECT_JOBS = new OperationsSupported(0x4009);
	public static final OperationsSupported CUPS_SET_DEFAULT = new OperationsSupported(0x400A);
	public static final OperationsSupported CUPS_GET_DEVICES = new OperationsSupported(0x400B);
	public static final OperationsSupported CUPS_GET_PPDS = new OperationsSupported(0x400C);
	public static final OperationsSupported CUPS_MOVE_JOB = new OperationsSupported(0x400D);
	public static final OperationsSupported CUPS_ADD_DEVICE = new OperationsSupported(0x400E);
	public static final OperationsSupported CUPS_DELETE_DEVICE = new OperationsSupported(0x400F);

	/**
	 * @param value
	 */
	public OperationsSupported(int value) {
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
		return OperationsSupported.getIppName();
	}

	public final static String getIppName() {
		return "operations-supported";
	}

}
