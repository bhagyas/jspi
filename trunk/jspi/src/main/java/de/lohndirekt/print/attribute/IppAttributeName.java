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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.print.attribute.Attribute;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.Compression;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.CopiesSupported;
import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.print.attribute.standard.DateTimeAtCreation;
import javax.print.attribute.standard.DateTimeAtProcessing;
import javax.print.attribute.standard.DocumentName;
import javax.print.attribute.standard.Fidelity;
import javax.print.attribute.standard.Finishings;
import javax.print.attribute.standard.JobImpressions;
import javax.print.attribute.standard.JobImpressionsCompleted;
import javax.print.attribute.standard.JobImpressionsSupported;
import javax.print.attribute.standard.JobKOctets;
import javax.print.attribute.standard.JobKOctetsProcessed;
import javax.print.attribute.standard.JobKOctetsSupported;
import javax.print.attribute.standard.JobMediaSheets;
import javax.print.attribute.standard.JobMediaSheetsCompleted;
import javax.print.attribute.standard.JobMediaSheetsSupported;
import javax.print.attribute.standard.JobMessageFromOperator;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.JobOriginatingUserName;
import javax.print.attribute.standard.JobPriority;
import javax.print.attribute.standard.JobPrioritySupported;
import javax.print.attribute.standard.JobSheets;
import javax.print.attribute.standard.JobState;
import javax.print.attribute.standard.JobStateReason;
import javax.print.attribute.standard.JobStateReasons;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MultipleDocumentHandling;
import javax.print.attribute.standard.NumberOfDocuments;
import javax.print.attribute.standard.NumberOfInterveningJobs;
import javax.print.attribute.standard.NumberUp;
import javax.print.attribute.standard.NumberUpSupported;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.OutputDeviceAssigned;
import javax.print.attribute.standard.PDLOverrideSupported;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PagesPerMinute;
import javax.print.attribute.standard.PagesPerMinuteColor;
import javax.print.attribute.standard.PresentationDirection;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterInfo;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterLocation;
import javax.print.attribute.standard.PrinterMakeAndModel;
import javax.print.attribute.standard.PrinterMessageFromOperator;
import javax.print.attribute.standard.PrinterMoreInfo;
import javax.print.attribute.standard.PrinterMoreInfoManufacturer;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.PrinterResolution;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReason;
import javax.print.attribute.standard.PrinterStateReasons;
import javax.print.attribute.standard.PrinterURI;
import javax.print.attribute.standard.QueuedJobCount;
import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import javax.print.attribute.standard.RequestingUserName;
import javax.print.attribute.standard.Severity;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;

import de.lohndirekt.print.attribute.auth.RequestingUserPassword;
import de.lohndirekt.print.attribute.cups.DeviceClass;
import de.lohndirekt.print.attribute.cups.DeviceUri;
import de.lohndirekt.print.attribute.cups.JobKLimit;
import de.lohndirekt.print.attribute.cups.JobPageLimit;
import de.lohndirekt.print.attribute.cups.JobQuotaPeriod;
import de.lohndirekt.print.attribute.cups.MemberNames;
import de.lohndirekt.print.attribute.cups.MemberUris;
import de.lohndirekt.print.attribute.cups.PrinterType;
import de.lohndirekt.print.attribute.ipp.Charset;
import de.lohndirekt.print.attribute.ipp.DetailedStatusMessage;
import de.lohndirekt.print.attribute.ipp.DocumentFormat;
import de.lohndirekt.print.attribute.ipp.NaturalLanguage;
import de.lohndirekt.print.attribute.ipp.StatusMessage;
import de.lohndirekt.print.attribute.ipp.UnknownAttribute;
import de.lohndirekt.print.attribute.ipp.jobdesc.JobId;
import de.lohndirekt.print.attribute.ipp.jobdesc.JobMoreInfo;
import de.lohndirekt.print.attribute.ipp.jobdesc.JobOriginatingHostName;
import de.lohndirekt.print.attribute.ipp.jobdesc.JobPrinterUpTime;
import de.lohndirekt.print.attribute.ipp.jobdesc.JobPrinterUri;
import de.lohndirekt.print.attribute.ipp.jobdesc.JobUri;
import de.lohndirekt.print.attribute.ipp.jobdesc.TimeAtCompleted;
import de.lohndirekt.print.attribute.ipp.jobdesc.TimeAtCreation;
import de.lohndirekt.print.attribute.ipp.jobdesc.TimeAtProcessing;
import de.lohndirekt.print.attribute.ipp.jobtempl.LdJobHoldUntil;
import de.lohndirekt.print.attribute.ipp.printerdesc.MultipleOperationTimeout;
import de.lohndirekt.print.attribute.ipp.printerdesc.NaturalLanguageConfigured;
import de.lohndirekt.print.attribute.ipp.printerdesc.PrinterCurrentTime;
import de.lohndirekt.print.attribute.ipp.printerdesc.PrinterDriverInstaller;
import de.lohndirekt.print.attribute.ipp.printerdesc.PrinterStateMessage;
import de.lohndirekt.print.attribute.ipp.printerdesc.PrinterUpTime;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.CharsetConfigured;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.CopiesDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.DocumentFormatDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.FinishingsDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.JobHoldUntilDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.JobPriorityDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.JobSheetsDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.MediaDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.MultipleDocumentHandlingDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.NumberUpDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.OrientationRequestedDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.defaults.SidesDefault;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.CharsetSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.CompressionSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.DocumentFormatSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.FinishingsSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.GeneratedNaturalLanguageSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.IppVersionsSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.JobHoldUntilSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.JobSheetsSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.MediaSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.MultipleDocumentHandlingSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.MultipleDocumentJobsSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OperationsSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OrientationRequestedSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.OutputBinSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.PageRangesSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.PrinterUriSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.SidesSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.UriAuthenticationSupported;
import de.lohndirekt.print.attribute.ipp.printerdesc.supported.UriSecuritySupported;
import de.lohndirekt.print.attribute.undocumented.PrinterStateTime;

/**
 * @author bpusch
 *
 */
public final class IppAttributeName {
	private final static Logger log = Logger.getLogger(AttributeParser.class.getName());
	
	/*
	 * Attributes defined in javax.print.attribute.standard
	 */
	public static final IppAttributeName COLOR_SUPPORTED = new IppAttributeName(ColorSupported.SUPPORTED);
	public static final IppAttributeName COMPRESSION = new IppAttributeName(Compression.NONE);
	public static final IppAttributeName COPIES = new IppAttributeName(new Copies(1));
	public static final IppAttributeName COPIES_SUPPORTED = new IppAttributeName(new CopiesSupported(1));
	public static final IppAttributeName DATE_TIME_AT_COMPLETION = new IppAttributeName(new DateTimeAtCompleted(new Date()));
	public static final IppAttributeName DATE_TIME_AT_CREATION = new IppAttributeName(new DateTimeAtCreation(new Date()));
	public static final IppAttributeName DATE_TIME_AT_PROCESSING = new IppAttributeName(new DateTimeAtProcessing(new Date()));
	public static final IppAttributeName DOCUMENT_NAME = new IppAttributeName(new DocumentName("", Locale.CANADA));
	public static final IppAttributeName FIDELITY = new IppAttributeName(Fidelity.FIDELITY_TRUE);
	public static final IppAttributeName FINISHINGS = new IppAttributeName(Finishings.BIND);
	public static final IppAttributeName JOB_IMPRESSIONS = new IppAttributeName(new JobImpressions(1));
	public static final IppAttributeName JOB_IMPRESSIONS_COMPLETED = new IppAttributeName(new JobImpressionsCompleted(1));
	public static final IppAttributeName JOB_IMPRESSIONS_SUPPORTED = new IppAttributeName(new JobImpressionsSupported(1, 1));
	public static final IppAttributeName JOB_K_OCTETS = new IppAttributeName(new JobKOctets(1));
	public static final IppAttributeName JOB_K_OCTETS_PROCESSED = new IppAttributeName(new JobKOctetsProcessed(1));
	public static final IppAttributeName JOB_K_OCTETS_SUPPORTED = new IppAttributeName(new JobKOctetsSupported(1, 1));
	public static final IppAttributeName JOB_MEDIA_SHEETS = new IppAttributeName(new JobMediaSheets(1));
	public static final IppAttributeName JOB_MEDIA_SHEETS_COMPLETED = new IppAttributeName(new JobMediaSheetsCompleted(1));
	public static final IppAttributeName JOB_MEDIA_SHEETS_SUPPORTED = new IppAttributeName(new JobMediaSheetsSupported(1, 1));
	public static final IppAttributeName JOB_MESSAGE_FROM_OPERATOR =
		new IppAttributeName(new JobMessageFromOperator("", Locale.CANADA));
	public static final IppAttributeName JOB_NAME = new IppAttributeName(new JobName("", Locale.CANADA));
	public static final IppAttributeName JOB_ORIGINATING_USER_NAME =
		new IppAttributeName(new JobOriginatingUserName("", Locale.CANADA));
	public static final IppAttributeName JOB_PRIORIY = new IppAttributeName(new JobPriority(1));
	public static final IppAttributeName JOB_PRIORIY_SUPPORTED = new IppAttributeName(new JobPrioritySupported(1));
	public static final IppAttributeName JOB_SHEETS = new IppAttributeName(JobSheets.NONE);
	public static final IppAttributeName JOB_STATE = new IppAttributeName(JobState.ABORTED);
	public static final IppAttributeName JOB_STATE_REASON = new IppAttributeName(JobStateReason.ABORTED_BY_SYSTEM);
	public static final IppAttributeName JOB_STATE_REASONS = new IppAttributeName(new JobStateReasons());
	//Could be MediaName,MediaSizeName or MediaTray
	public static final IppAttributeName MEDIA = new IppAttributeName(MediaSizeName.A);
	public static final IppAttributeName MULTIPLE_DOCUMENT_HANDLING =
		new IppAttributeName(MultipleDocumentHandling.SEPARATE_DOCUMENTS_COLLATED_COPIES);
	public static final IppAttributeName NUMBER_OF_DOCUMENTS = new IppAttributeName(new NumberOfDocuments(1));
	public static final IppAttributeName NUMBER_OF_INTERVENING_JOBS = new IppAttributeName(new NumberOfInterveningJobs(1));
	public static final IppAttributeName NUMBER_UP = new IppAttributeName(new NumberUp(1));
	public static final IppAttributeName NUMBER_UP_SUPPORTED = new IppAttributeName(new NumberUpSupported(1));
	public static final IppAttributeName ORIENTATION_REQUESTED = new IppAttributeName(OrientationRequested.LANDSCAPE);
	public static final IppAttributeName OUTPUT_DEVICE_ASSIGNED =
		new IppAttributeName(new OutputDeviceAssigned("", Locale.CANADA));
	public static final IppAttributeName PAGE_RANGES = new IppAttributeName(new PageRanges(1));
	public static final IppAttributeName PAGES_PER_MINUTE = new IppAttributeName(new PagesPerMinute(1));
	public static final IppAttributeName PAGES_PER_MINUTE_COLOR = new IppAttributeName(new PagesPerMinuteColor(1));
	public static final IppAttributeName PDL_OVERRIDE_SUPPORTED = new IppAttributeName(PDLOverrideSupported.ATTEMPTED);
	public static final IppAttributeName PRESENTATION_DIRECTION = new IppAttributeName(PresentationDirection.TOBOTTOM_TOLEFT);
	public static final IppAttributeName PRINTER_INFO = new IppAttributeName(new PrinterInfo("", Locale.CANADA));
	public static final IppAttributeName PRINTER_IS_ACCEPTING_JOBS = new IppAttributeName(PrinterIsAcceptingJobs.ACCEPTING_JOBS);
	public static final IppAttributeName PRINTER_LOCATION = new IppAttributeName(new PrinterLocation("", Locale.CANADA));
	public static final IppAttributeName PRINTER_MAKE_AND_MODEL =
		new IppAttributeName(new PrinterMakeAndModel("", Locale.CANADA));
	public static final IppAttributeName PRINTER_MESSAGE_FROM_OPERATOR =
		new IppAttributeName(new PrinterMessageFromOperator("", Locale.CANADA));
	public static final IppAttributeName PRINTER_MORE_INFO = new IppAttributeName(new PrinterMoreInfo(IppAttributeName.getURI()));
	public static final IppAttributeName PRINTER_MORE_INFO_MANUFACTURER =
		new IppAttributeName(new PrinterMoreInfoManufacturer(IppAttributeName.getURI()));
	public static final IppAttributeName PRINTER_NAME = new IppAttributeName(new PrinterName("", Locale.CANADA));
	public static final IppAttributeName PRINTER_RESOLUTION = new IppAttributeName(new PrinterResolution(1, 1, 1));
	public static final IppAttributeName PRINTER_STATE = new IppAttributeName(PrinterState.IDLE);
	public static final IppAttributeName PRINTER_STATE_REASON = new IppAttributeName(PrinterStateReason.CONNECTING_TO_DEVICE);
	public static final IppAttributeName PRINTER_STATE_REASONS = new IppAttributeName(new PrinterStateReasons());
	public static final IppAttributeName PRINTER_URI = new IppAttributeName(new PrinterURI(IppAttributeName.getURI()));
	public static final IppAttributeName PRINT_QUALITY = new IppAttributeName(PrintQuality.DRAFT);
	public static final IppAttributeName QUEUED_JOB_COUNT = new IppAttributeName(new QueuedJobCount(1));
	public static final IppAttributeName REFERENCE_URI_SCHEMES_SUPPORTED =
		new IppAttributeName(ReferenceUriSchemesSupported.FILE);
	public static final IppAttributeName REQUESTING_USER_NAME = new IppAttributeName(new RequestingUserName("", Locale.CANADA));
	public static final IppAttributeName REQUESTING_USER_PASSWD = new IppAttributeName(new RequestingUserPassword("", Locale.CANADA));
	public static final IppAttributeName SEVERITY = new IppAttributeName(Severity.ERROR);
	public static final IppAttributeName SHEET_COLLATE = new IppAttributeName(SheetCollate.COLLATED);
	public static final IppAttributeName SIDES = new IppAttributeName(Sides.DUPLEX);

	/*
	 * IPP standard attributes defined in de.lohndirekt.attribute.ipp
	 */
	public static final IppAttributeName CHARSET = new IppAttributeName(new Charset("x", Locale.getDefault()));
	public static final IppAttributeName CHARSET_CONFIGURED =
		new IppAttributeName(new CharsetConfigured("x", Locale.getDefault()));
	public static final IppAttributeName CHARSET_SUPORTED = new IppAttributeName(new CharsetSupported("x", Locale.getDefault()));
	public static final IppAttributeName COMPRESSION_SUPORTED =
		new IppAttributeName(new CompressionSupported("x", Locale.getDefault()));
	public static final IppAttributeName COPIES_DEFAULT = new IppAttributeName(new CopiesDefault(1));
    public static final IppAttributeName DETAILED_STATUS_MESSAGE = new IppAttributeName(new DetailedStatusMessage("x", Locale.getDefault()));
    public static final IppAttributeName DOCUMENT_FORMAT =
		new IppAttributeName(new DocumentFormat("x", Locale.getDefault()));
	public static final IppAttributeName DOCUMENT_FORMAT_SUPORTED =
		new IppAttributeName(new DocumentFormatSupported("x", Locale.getDefault()));
	public static final IppAttributeName DOCUMENT_FORMAT_DEFAULT =
		new IppAttributeName(new DocumentFormatDefault("x", Locale.getDefault()));
	public static final IppAttributeName FINISHINGS_DEFAULT = new IppAttributeName(new FinishingsDefault(1));
	public static final IppAttributeName FINISHINGS_SUPPORTED = new IppAttributeName(new FinishingsSupported(1));
	public static final IppAttributeName IPP_VERSIONS_SUPPORTED =
		new IppAttributeName(new IppVersionsSupported("x", Locale.getDefault()));
    public static final IppAttributeName JOB_HOLD_UNTIL = new IppAttributeName(new LdJobHoldUntil("x", Locale.getDefault()));    
	public static final IppAttributeName JOB_HOLD_UNTIL_DEFAULT = new IppAttributeName(new JobHoldUntilDefault("x", Locale.getDefault()));
	public static final IppAttributeName JOB_HOLD_UNTIL_SUPPORTED = new IppAttributeName(new JobHoldUntilSupported("x", Locale.getDefault()));
	public static final IppAttributeName JOB_ID = new IppAttributeName(new JobId(1));
	public static final IppAttributeName JOB_MORE_INFO = new IppAttributeName(new JobMoreInfo(IppAttributeName.getURI()));
	public static final IppAttributeName JOB_ORIGINATING_HOST_NAME =
		new IppAttributeName(new JobOriginatingHostName("x", Locale.getDefault()));
	public static final IppAttributeName JOB_PRINTER_UP_TIME = new IppAttributeName(new JobPrinterUpTime(1));
	public static final IppAttributeName JOB_PRINTER_URI = new IppAttributeName(new JobPrinterUri(getURI()));
	public static final IppAttributeName JOB_PRIORITY_DEFAULT = new IppAttributeName(new JobPriorityDefault(1));
	public static final IppAttributeName JOB_SHEETS_DEFAULT =
		new IppAttributeName(new JobSheetsDefault("x", Locale.getDefault()));
	public static final IppAttributeName JOB_SHEETS_SUPORTED =
		new IppAttributeName(new JobSheetsSupported("x", Locale.getDefault()));
	public static final IppAttributeName JOB_URI =
		new IppAttributeName(new JobUri(IppAttributeName.getURI()));
	public static final IppAttributeName GENERATED_NATURAL_LANGUAGE_SUPPORTED =
		new IppAttributeName(new GeneratedNaturalLanguageSupported("x", Locale.getDefault()));
	public static final IppAttributeName MEDIA_DEFAULT = new IppAttributeName(new MediaDefault("x", Locale.getDefault()));
	public static final IppAttributeName MEDIA_SUPPORTED = new IppAttributeName(new MediaSupported("x", Locale.getDefault()));
    public static final IppAttributeName MULTIPLE_DOCUMENT_HANDLING_DEFAULT =
        new IppAttributeName(new MultipleDocumentHandlingDefault("x", Locale.getDefault()));
    public static final IppAttributeName MULTIPLE_DOCUMENT_HANDLING_SUPPORTED =
        new IppAttributeName(new MultipleDocumentHandlingSupported("x", Locale.getDefault()));
    public static final IppAttributeName MULTIPLE_DOCUMENT_JOBS_SUPPORTED =
		new IppAttributeName(new MultipleDocumentJobsSupported(1));
	public static final IppAttributeName MULTIPLE_OPERATION_TIMEOUT = new IppAttributeName(new MultipleOperationTimeout(1));
	public static final IppAttributeName NATURAL_LANGUAGE = new IppAttributeName(new NaturalLanguage("x", Locale.getDefault()));
	public static final IppAttributeName NATURAL_LANGUAGE_CONFIGURED =
		new IppAttributeName(new NaturalLanguageConfigured("x", Locale.getDefault()));
	public static final IppAttributeName NUMBER_UP_DEFAULT = new IppAttributeName(new NumberUpDefault(1));
	public static final IppAttributeName OPERATIONS_SUPPORTED = new IppAttributeName(new OperationsSupported(1));
	public static final IppAttributeName ORIENTATION_REQUESTED_DEFAULT = new IppAttributeName(new OrientationRequestedDefault(1));
	public static final IppAttributeName ORIENTATION_REQUESTED_SUPPORTED =
		new IppAttributeName(new OrientationRequestedSupported(1));
	public static final IppAttributeName PAGE_RANGES_SUPPORTED = new IppAttributeName(new PageRangesSupported(1));
	public static final IppAttributeName PRINTER_CURRENT_TIME = new IppAttributeName(new PrinterCurrentTime(new Date()));
	public static final IppAttributeName PRINTER_DRIVER_INSTALLER =
		new IppAttributeName(new PrinterDriverInstaller(IppAttributeName.getURI()));
	public static final IppAttributeName PRINTER_STATE_MESSAGE =
		new IppAttributeName(new PrinterStateMessage("x", Locale.getDefault()));
	public static final IppAttributeName PRINTER_TYPE = new IppAttributeName(new PrinterType(1));
	public static final IppAttributeName PRINTER_UP_TIME = new IppAttributeName(new PrinterUpTime(1));
	public static final IppAttributeName PRINTER_URI_SUPPORTED =
		new IppAttributeName(new PrinterUriSupported(IppAttributeName.getURI()));
	public static final IppAttributeName SIDES_DEFAULT = new IppAttributeName(new SidesDefault("x", Locale.getDefault()));
	public static final IppAttributeName SIDES_SUPPORTED = new IppAttributeName(new SidesSupported("x", Locale.getDefault()));
	public static final IppAttributeName STATUS_MESSAGE = new IppAttributeName(new StatusMessage("x", Locale.getDefault()));
	public static final IppAttributeName TIME_AT_COMPLETED = new IppAttributeName(new TimeAtCompleted(1));
	public static final IppAttributeName TIME_AT_CREATION = new IppAttributeName(new TimeAtCreation(1));
	                                                                                              public static final IppAttributeName TIME_AT_PROCESSING = new IppAttributeName(new TimeAtProcessing(1));
	public static final IppAttributeName URI_AUTHENTICATION_SUPPORTED =
		new IppAttributeName(new UriAuthenticationSupported("x", Locale.getDefault()));
	public static final IppAttributeName URI_SECURITY_SUPPORTED =
		new IppAttributeName(new UriSecuritySupported("x", Locale.getDefault()));
	
	
	/*
	 * CUPS IPP extension attributes defined in de.lohndirekt.attribute.cups
	 */
	public static final IppAttributeName DEVICE_CLASS = new IppAttributeName(new DeviceClass("x", Locale.getDefault()));
	public static final IppAttributeName DEVICE_URI = new IppAttributeName(new DeviceUri(IppAttributeName.getURI()));
	public static final IppAttributeName Job_K_LIMIT = new IppAttributeName(new JobKLimit(1));
	public static final IppAttributeName JOB_PAGE_LIMIT = new IppAttributeName(new JobPageLimit(1));
	public static final IppAttributeName JOB_QUOTA_PERIOD = new IppAttributeName(new JobQuotaPeriod(1));
	public static final IppAttributeName MEMBER_NAMES = new IppAttributeName(new MemberNames("x", Locale.getDefault()));
	public static final IppAttributeName MEMBER_URIS = new IppAttributeName(new MemberUris(IppAttributeName.getURI()));
	public static final IppAttributeName PRINTER_STATE_TIME = new IppAttributeName(new PrinterStateTime(1));
	
	/*
	 * undocumented IPP attributes used by CUPS
	 */
	public static final IppAttributeName OUTPUT_BIN_SUPPORTED =
		new IppAttributeName(new OutputBinSupported("x", Locale.getDefault()));
	
	
	private static Map attributesByName = new HashMap();;
	private static Map attributesByCategory = new HashMap();;

	/**
		 * 
		 */
	private static URI getURI() {
		try {
			return new URI("http://www.lohndirekt.de");
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

	}

	private static void put(IppAttributeName attr) {
		attributesByName.put(attr.getName(), attr);
		attributesByCategory.put(attr.getCategory(), attr);
	}

	/**
	 * 
	 */
	static {
		put(IppAttributeName.CHARSET);
		put(IppAttributeName.CHARSET_CONFIGURED);
		put(IppAttributeName.CHARSET_SUPORTED);
		put(IppAttributeName.COLOR_SUPPORTED);
		put(IppAttributeName.COMPRESSION);
		put(IppAttributeName.COMPRESSION_SUPORTED);
		put(IppAttributeName.COPIES);
		put(IppAttributeName.COPIES_DEFAULT);
		put(IppAttributeName.COPIES_SUPPORTED);
		put(IppAttributeName.DATE_TIME_AT_COMPLETION);
		put(IppAttributeName.DATE_TIME_AT_CREATION);
		put(IppAttributeName.DATE_TIME_AT_PROCESSING);
		put(IppAttributeName.DEVICE_CLASS);
		put(IppAttributeName.DEVICE_URI);
        put(IppAttributeName.DETAILED_STATUS_MESSAGE);
		put(IppAttributeName.DOCUMENT_NAME);
		put(IppAttributeName.DOCUMENT_FORMAT);
		put(IppAttributeName.DOCUMENT_FORMAT_DEFAULT);
		put(IppAttributeName.DOCUMENT_FORMAT_SUPORTED);
		put(IppAttributeName.FIDELITY);
		put(IppAttributeName.FINISHINGS);
		put(IppAttributeName.FINISHINGS_DEFAULT);
		put(IppAttributeName.FINISHINGS_SUPPORTED);
		put(IppAttributeName.GENERATED_NATURAL_LANGUAGE_SUPPORTED);
		put(IppAttributeName.IPP_VERSIONS_SUPPORTED);
        put(IppAttributeName.JOB_HOLD_UNTIL);
		put(IppAttributeName.JOB_HOLD_UNTIL_DEFAULT);
		put(IppAttributeName.JOB_HOLD_UNTIL_SUPPORTED);
		put(IppAttributeName.JOB_ID);
		put(IppAttributeName.JOB_IMPRESSIONS);
		put(IppAttributeName.JOB_IMPRESSIONS_COMPLETED);
		put(IppAttributeName.JOB_IMPRESSIONS_SUPPORTED);
		put(IppAttributeName.Job_K_LIMIT);
		put(IppAttributeName.JOB_K_OCTETS);
		put(IppAttributeName.JOB_K_OCTETS_PROCESSED);
		put(IppAttributeName.JOB_K_OCTETS_SUPPORTED);
		put(IppAttributeName.JOB_MEDIA_SHEETS);
		put(IppAttributeName.JOB_MEDIA_SHEETS_COMPLETED);
		put(IppAttributeName.JOB_MEDIA_SHEETS_SUPPORTED);
		put(IppAttributeName.JOB_MESSAGE_FROM_OPERATOR);
		put(IppAttributeName.JOB_MORE_INFO);
		put(IppAttributeName.JOB_NAME);
		put(IppAttributeName.JOB_ORIGINATING_HOST_NAME);
		put(IppAttributeName.JOB_ORIGINATING_USER_NAME);
		put(IppAttributeName.JOB_PAGE_LIMIT);
		put(IppAttributeName.JOB_PRINTER_UP_TIME);
		put(IppAttributeName.JOB_PRINTER_URI);
		put(IppAttributeName.JOB_PRIORIY);
		put(IppAttributeName.JOB_PRIORITY_DEFAULT);
		put(IppAttributeName.JOB_PRIORIY_SUPPORTED);
		put(IppAttributeName.JOB_QUOTA_PERIOD);
		put(IppAttributeName.JOB_SHEETS);
		put(IppAttributeName.JOB_SHEETS_DEFAULT);
		put(IppAttributeName.JOB_SHEETS_SUPORTED);
		put(IppAttributeName.JOB_STATE);
		put(IppAttributeName.JOB_STATE_REASON);
		put(IppAttributeName.JOB_STATE_REASONS);
		put(IppAttributeName.JOB_URI);
		put(IppAttributeName.MEDIA);
		put(IppAttributeName.MEDIA_DEFAULT);
		put(IppAttributeName.MEDIA_SUPPORTED);
		put(IppAttributeName.MEMBER_NAMES);
		put(IppAttributeName.MEMBER_URIS);
		put(IppAttributeName.MULTIPLE_DOCUMENT_HANDLING);
        put(IppAttributeName.MULTIPLE_DOCUMENT_HANDLING_DEFAULT);
		put(IppAttributeName.MULTIPLE_DOCUMENT_HANDLING_SUPPORTED);
		put(IppAttributeName.MULTIPLE_DOCUMENT_JOBS_SUPPORTED);
		put(IppAttributeName.MULTIPLE_OPERATION_TIMEOUT);
		put(IppAttributeName.NATURAL_LANGUAGE);
		put(IppAttributeName.NATURAL_LANGUAGE_CONFIGURED);
		put(IppAttributeName.NUMBER_OF_DOCUMENTS);
		put(IppAttributeName.NUMBER_OF_INTERVENING_JOBS);
		put(IppAttributeName.NUMBER_UP);
		put(IppAttributeName.NUMBER_UP_DEFAULT);
		put(IppAttributeName.NUMBER_UP_SUPPORTED);
		put(IppAttributeName.OPERATIONS_SUPPORTED);
		put(IppAttributeName.ORIENTATION_REQUESTED);
		put(IppAttributeName.ORIENTATION_REQUESTED_DEFAULT);
		put(IppAttributeName.ORIENTATION_REQUESTED_SUPPORTED);
		put(IppAttributeName.OUTPUT_BIN_SUPPORTED);
		put(IppAttributeName.OUTPUT_DEVICE_ASSIGNED);
		put(IppAttributeName.PAGE_RANGES);
		put(IppAttributeName.PAGE_RANGES_SUPPORTED);
		put(IppAttributeName.PAGES_PER_MINUTE);
		put(IppAttributeName.PAGES_PER_MINUTE_COLOR);
		put(IppAttributeName.PDL_OVERRIDE_SUPPORTED);
		put(IppAttributeName.PRESENTATION_DIRECTION);
		put(IppAttributeName.PRINT_QUALITY);
		put(IppAttributeName.PRINTER_CURRENT_TIME);
		put(IppAttributeName.PRINTER_DRIVER_INSTALLER);
		put(IppAttributeName.PRINTER_INFO);
		put(IppAttributeName.PRINTER_IS_ACCEPTING_JOBS);
		put(IppAttributeName.PRINTER_LOCATION);
		put(IppAttributeName.PRINTER_MAKE_AND_MODEL);
		put(IppAttributeName.PRINTER_MESSAGE_FROM_OPERATOR);
		put(IppAttributeName.PRINTER_MORE_INFO);
		put(IppAttributeName.PRINTER_MORE_INFO_MANUFACTURER);
		put(IppAttributeName.PRINTER_NAME);
		put(IppAttributeName.PRINTER_RESOLUTION);
		put(IppAttributeName.PRINTER_STATE);
		put(IppAttributeName.PRINTER_STATE_MESSAGE);
		put(IppAttributeName.PRINTER_STATE_REASON);
		put(IppAttributeName.PRINTER_STATE_REASONS);
		put(IppAttributeName.PRINTER_STATE_TIME);
		put(IppAttributeName.PRINTER_TYPE);
		put(IppAttributeName.PRINTER_UP_TIME);
		put(IppAttributeName.PRINTER_URI);
		put(IppAttributeName.PRINTER_URI_SUPPORTED);
		put(IppAttributeName.QUEUED_JOB_COUNT);
		put(IppAttributeName.REFERENCE_URI_SCHEMES_SUPPORTED);
		put(IppAttributeName.REQUESTING_USER_NAME);
		put(IppAttributeName.REQUESTING_USER_PASSWD);
		put(IppAttributeName.SEVERITY);
		put(IppAttributeName.SHEET_COLLATE);
		put(IppAttributeName.SIDES);
		put(IppAttributeName.SIDES_DEFAULT);
		put(IppAttributeName.SIDES_SUPPORTED);
		put(IppAttributeName.STATUS_MESSAGE);
		put(IppAttributeName.TIME_AT_COMPLETED);
		put(IppAttributeName.TIME_AT_CREATION);
		put(IppAttributeName.TIME_AT_PROCESSING);
		put(IppAttributeName.URI_AUTHENTICATION_SUPPORTED);
		put(IppAttributeName.URI_SECURITY_SUPPORTED);
	}

	public static IppAttributeName get(String attributeName) {
		IppAttributeName attrib =(IppAttributeName)attributesByName.get(attributeName);
		if (attrib == null) {
			log.warning("Unknown Attribute " + attributeName + ".");
			attrib = new IppAttributeName(new UnknownAttribute(attributeName, Locale.getDefault()));
		}
		return attrib;
	}

	public static IppAttributeName get(Class category) {
	    IppAttributeName attrib =(IppAttributeName)attributesByCategory.get(category);
		if (attrib == null) {
			log.warning("Unknown Category " + category + ".");
			attrib = new IppAttributeName(new UnknownAttribute(category.getName(), Locale.getDefault()));
		}
		return attrib;
	}
	// End of static part

	private String methodName;
	private Class attributeClass;
	private Class category;

	private IppAttributeName(Attribute attribute) {
		this.methodName = attribute.getName();
		this.attributeClass = attribute.getClass();
		this.category = attribute.getCategory();
	}

	/**
	 * @return
	 */
	public Class getAttributeClass() {
		return attributeClass;
	}

	public String getName() {
		return this.methodName;
	}

	/**
	 * 
	 */
	public Class getCategory() {
		return this.category;
	}

}
