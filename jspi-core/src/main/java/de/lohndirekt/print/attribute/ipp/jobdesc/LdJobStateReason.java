/*
 * Created on 22.01.2004
 *
 * 
 */
package de.lohndirekt.print.attribute.ipp.jobdesc;

import java.util.Locale;

import javax.print.attribute.standard.JobStateReason;

/**
 * @author bpusch
 *
 * 
 */
public class LdJobStateReason extends JobStateReason {
    
    private String stringValue;

    public static LdJobStateReason NONE = new LdJobStateReason("none", Locale.getDefault(), -1);

    /**
     * @param value
     */
    private LdJobStateReason(String stringValue, Locale locale, int value) {
        super(value);
        this.stringValue = stringValue;
    }

   public String toString(){
       if (this.stringValue != null){
           return this.stringValue;
       } else {
           return super.toString();
       }
   }

    

}
