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

import java.util.Locale;

import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.TextSyntax;

/**
 * The job-hold-until attribute specifies a hold time. 
 * In addition to the standard IPP/1.1 keyword names, 
 * CUPS supports name values of the form "HH:MM" and "HH:MM:SS" 
 * that specify a hold time.<br> 
 * The hold time is in Greenwich Mean Time (GMT) and not in the 
 * local time zone. If the specified time is less than the 
 * current time, the job is held until the next day.
 *
 * @author bpusch
 */
public class LdJobHoldUntil extends TextSyntax implements PrintRequestAttribute, PrintJobAttribute {
    
    public final static LdJobHoldUntil NO_HOLD = new LdJobHoldUntil("no-hold",Locale.getDefault());
    public final static LdJobHoldUntil INDEFINITE = new LdJobHoldUntil("indefinite",Locale.getDefault());
    public final static LdJobHoldUntil DAY_TIME = new LdJobHoldUntil("day-time",Locale.getDefault());
    public final static LdJobHoldUntil EVENING = new LdJobHoldUntil("evening",Locale.getDefault());
    public final static LdJobHoldUntil NIGHT = new LdJobHoldUntil("night",Locale.getDefault());
    public final static LdJobHoldUntil WEEKEND = new LdJobHoldUntil("weekend",Locale.getDefault());
    public final static LdJobHoldUntil SECOND_SHIFT = new LdJobHoldUntil("second-shift",Locale.getDefault());
    public final static LdJobHoldUntil THIRD_SHIFT = new LdJobHoldUntil("third-shift",Locale.getDefault());

	/**
     * @param value
     * @param locale
     */
    public LdJobHoldUntil(String value, Locale locale) {
		super(value, locale);
	}

	public Class getCategory() {
		return LdJobHoldUntil.class;
	}

	public String getName() {
		return LdJobHoldUntil.getIppName();
	}

	
    public final static String getIppName() {
		return "job-hold-until";
	}

}
