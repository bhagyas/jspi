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

import java.util.HashMap;
import java.util.Map;

public final class IppStatus {

	public static final IppStatus SUCCESSFUL_OK = new IppStatus("successful-ok", 0x0000);
	public static final IppStatus SUCCESSFUL_OK_IGNORED_OR_SUBSTITUTED_ATTRIBUTES =
		new IppStatus("successful-ok-ignored-or-substituted-attributes", 0x0001);
	public static final IppStatus SUCCESSFUL_OK_CONFLICTING_ATTRIBUTES =
		new IppStatus("successful-ok-conflicting-attributes", 0x0002);
	public static final IppStatus CLIENT_ERROR_BAD_REQUEST = new IppStatus("client-error-bad-request", 0x0400);
	public static final IppStatus CLIENT_ERROR_FORBIDDEN = new IppStatus("client-error-forbidden", 0x0401);
	public static final IppStatus CLIENT_ERROR_NOT_AUTHENTICATED = new IppStatus("client-error-not-authenticated", 0x0402);
	public static final IppStatus CLIENT_ERROR_NOT_AUTHORIZED = new IppStatus("client-error-not-authorized", 0x0403);
	public static final IppStatus CLIENT_ERROR_NOT_POSSIBLE = new IppStatus("client-error-not-possible", 0x0404);
	public static final IppStatus CLIENT_ERROR_TIMEOUT = new IppStatus("client-error-timeout", 0x0405);
	public static final IppStatus CLIENT_ERROR_NOT_FOUND = new IppStatus("client-error-not-found", 0x0406);
	public static final IppStatus CLIENT_ERROR_GONE = new IppStatus("client-error-gone", 0x0407);
	public static final IppStatus CLIENT_ERROR_REQUEST_ENTITY_TOO_LARGE =
		new IppStatus("client-error-request-entity-too-large", 0x0408);
	public static final IppStatus CLIENT_ERROR_REQUEST_VALUE_TOO_LONG =
		new IppStatus("client-error-request-value-too-long", 0x0409);
	public static final IppStatus CLIENT_ERROR_DOCUMENT_FORMAT_NOT_SUPPORTED =
		new IppStatus("client-error-document-format-not-supported", 0x040A);
	public static final IppStatus CLIENT_ERROR_ATTRIBUTES_OR_VALUES_NOT_SUPPORTED =
		new IppStatus("client-error-attributes-or-values-not-supported", 0x040B);
	public static final IppStatus CLIENT_ERROR_URI_SCHEME_NOT_SUPPORTED =
		new IppStatus("client-error-uri-scheme-not-supported", 0x040C);
	public static final IppStatus CLIENT_ERROR_CHARSET_NOT_SUPPORTED =
		new IppStatus("client-error-charset-not-supported", 0x040D);
	public static final IppStatus CLIENT_ERROR_CONFLICTING_ATTRIBUTES =
		new IppStatus("client-error-conflicting-attributes", 0x040E);
	public static final IppStatus CLIENT_ERROR_COMPRESSION_NOT_SUPPORTED =
		new IppStatus("client-error-compression-not-supported", 0x040F);
	public static final IppStatus CLIENT_ERROR_COMPRESSION_ERROR = new IppStatus("client-error-compression-error", 0x0410);
	public static final IppStatus CLIENT_ERROR_DOCUMENT_FORMAT_ERROR =
		new IppStatus("client-error-document-format-error", 0x0411);
	public static final IppStatus CLIENT_ERROR_DOCUMENT_ACCESS_ERROR =
		new IppStatus("client-error-document-access-error", 0x0412);
	public static final IppStatus SERVER_ERROR_INTERNAL_ERROR = new IppStatus("server-error-internal-error", 0x0500);
	public static final IppStatus SERVER_ERROR_OPERATION_NOT_SUPPORTED =
		new IppStatus("server-error-operation-not-supported", 0x0501);
	public static final IppStatus SERVER_ERROR_SERVICE_UNAVAILABLE = new IppStatus("server-error-service-unavailable", 0x0502);
	public static final IppStatus SERVER_ERROR_VERSION_NOT_SUPPORTED =
		new IppStatus("server-error-version-not-supported", 0x0503);
	public static final IppStatus SERVER_ERROR_DEVICE_ERROR = new IppStatus("server-error-device-error", 0x0504);
	public static final IppStatus SERVER_ERROR_TEMPORARY_ERROR = new IppStatus("server-error-temporary-error", 0x0505);
	public static final IppStatus SERVER_ERROR_NOT_ACCEPTING_JOBS = new IppStatus("server-error-not-accepting-jobs", 0x0506);
	public static final IppStatus SERVER_ERROR_BUSY = new IppStatus("server-error-busy", 0x0507);
	public static final IppStatus SERVER_ERROR_JOB_CANCELED = new IppStatus("server-error-job-canceled", 0x0508);
	public static final IppStatus SERVER_ERROR_MULTIPLE_DOCUMENT_JOBS_NOT_SUPPORTED =
		new IppStatus("server-error-multiple-document-jobs-not-supported", 0x0509);

	private static Map stati;

	private static void init() {
		stati = new HashMap();
		put(SUCCESSFUL_OK);
		put(SUCCESSFUL_OK_IGNORED_OR_SUBSTITUTED_ATTRIBUTES);
		put(SUCCESSFUL_OK_CONFLICTING_ATTRIBUTES);
		put(CLIENT_ERROR_BAD_REQUEST);
		put(CLIENT_ERROR_FORBIDDEN);
		put(CLIENT_ERROR_NOT_AUTHENTICATED);
		put(CLIENT_ERROR_NOT_AUTHORIZED);
		put(CLIENT_ERROR_NOT_POSSIBLE);
		put(CLIENT_ERROR_TIMEOUT);
		put(CLIENT_ERROR_NOT_FOUND);
		put(CLIENT_ERROR_GONE);
		put(CLIENT_ERROR_REQUEST_ENTITY_TOO_LARGE);
		put(CLIENT_ERROR_REQUEST_VALUE_TOO_LONG);
		put(CLIENT_ERROR_DOCUMENT_FORMAT_NOT_SUPPORTED);
		put(CLIENT_ERROR_ATTRIBUTES_OR_VALUES_NOT_SUPPORTED);
		put(CLIENT_ERROR_URI_SCHEME_NOT_SUPPORTED);
		put(CLIENT_ERROR_CHARSET_NOT_SUPPORTED);
		put(CLIENT_ERROR_CONFLICTING_ATTRIBUTES);
		put(CLIENT_ERROR_COMPRESSION_NOT_SUPPORTED);
		put(CLIENT_ERROR_COMPRESSION_ERROR);
		put(CLIENT_ERROR_DOCUMENT_FORMAT_ERROR);
		put(CLIENT_ERROR_DOCUMENT_ACCESS_ERROR);
		put(SERVER_ERROR_INTERNAL_ERROR);
		put(SERVER_ERROR_OPERATION_NOT_SUPPORTED);
		put(SERVER_ERROR_SERVICE_UNAVAILABLE);
		put(SERVER_ERROR_VERSION_NOT_SUPPORTED);
		put(SERVER_ERROR_DEVICE_ERROR);
		put(SERVER_ERROR_TEMPORARY_ERROR);
		put(SERVER_ERROR_NOT_ACCEPTING_JOBS);
		put(SERVER_ERROR_BUSY);
		put(SERVER_ERROR_JOB_CANCELED);
		put(SERVER_ERROR_MULTIPLE_DOCUMENT_JOBS_NOT_SUPPORTED);

	}

	/**
	 * @param status2
	 */
	private static void put(IppStatus status) {
		stati.put(new Integer(status.getStatus()), status);
	}

	public static IppStatus get(int statusCode) {
		if (stati == null) {
			init();
		}
		return (IppStatus)stati.get(new Integer(statusCode));
	}

	/*
	 * End of static part
	 */

	private String text;
	private int status;

	private IppStatus(String statusText, int statusCode) {
		this.status = statusCode;
		this.text = statusText;
	}

	public String getText() {
		return this.text;
	}

	public int getStatus() {
		return this.status;
	}

}
