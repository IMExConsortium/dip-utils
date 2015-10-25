package edu.ucla.mbi.util.data;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *
 * DataItem - a traceable unit of data
 *
 ============================================================================ */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.GregorianCalendar;
import java.util.Calendar;

public class PublicationVIP extends Publication {
        
    //--------------------------------------------------------------------------
    // volume/isse/pages added
    //------------------------

    private String volume = "";

    public void setVolume( String volume ) {
        this.volume = volume;
    }
    
    public String getVolume() {
        return volume;
    }

    //--------------------------------------------------------------------------

    private String issue = "";

    public void setIssue( String issue ) {
        this.issue = issue;
    }

    public String getIssue() {
        return issue;
    }

    //--------------------------------------------------------------------------

    private String pages = "";

    public void setPages( String pages ) {
        this.pages = pages;
    }

}
