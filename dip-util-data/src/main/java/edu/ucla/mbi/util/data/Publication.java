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

public class Publication extends SourcedDataItem {
    
    public Publication() {}
    
    //--------------------------------------------------------------------------
    // identifiers
    //------------

    private String doi = "";

    public void setDoi( String doi ) {
        this.doi = doi;
    }
    
    public String getDoi() {
        return doi;
    }

    //--------------------------------------------------------------------------

    private String pmid = "";

    public void setPmid( String pmid ) {
        this.pmid = pmid;
    }

    public String getPmid() {
        return pmid;
    }

    //--------------------------------------------------------------------------

    private String journalSpecific = "";

    public void setJournalSpecific( String journalSpecific ) {
        this.journalSpecific = journalSpecific;
    }

    public String getJournalSpecific() {
        return journalSpecific;
    }
    
    //--------------------------------------------------------------------------

    private String contactEmail = "";

    public void setContactEmail( String email ) {
        this.contactEmail = email;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    //--------------------------------------------------------------------------
    // publication data
    //-----------------

    private String title = "";

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    //--------------------------------------------------------------------------

    private String author = "";

    public void setAuthor( String author ) {
        this.author = author; 
    }
    
    public String getAuthor() {
        return author; 
    }

    //--------------------------------------------------------------------------
    private String publicationAbstract = "";
    
    public void setAbstract( String abst ) {
        this.publicationAbstract = abst;
    }

    public String getAbstract() {
        return publicationAbstract;
    }

    //--------------------------------------------------------------------------
    // release data
    //--------------
    
    private GregorianCalendar pubDate;

    public void setPubDate( GregorianCalendar date ) {
        this.pubDate = date;
    }
    
    public GregorianCalendar getPubDate() {
        return pubDate;
    }
    
    public String getPubDateStr() {

        if( pubDate != null ) {
            return dateStr( pubDate.get(Calendar.YEAR),
                            pubDate.get(Calendar.MONTH),
                            pubDate.get(Calendar.DAY_OF_MONTH) );
        } else {
            return "0000/00/00";
        }
    }
    
    public void setPubDateStr( String date ) {
        //this.releaseDate = date;
    }

    //--------------------------------------------------------------------------
    
    private GregorianCalendar expectedPubDate;
    
    public void setExpectedPubDate( GregorianCalendar date ) {
        this.expectedPubDate = date;
    }

    public GregorianCalendar getExpectedPubDate() {
        return expectedPubDate;
    }

    public String getExpectedPubDateStr() {

        if( expectedPubDate != null ) {
            return dateStr( expectedPubDate.get(Calendar.YEAR), 
                            expectedPubDate.get(Calendar.MONTH),
                            expectedPubDate.get(Calendar.DAY_OF_MONTH) );
        } else {
            return "0000/00/00";
        }
    }   

    public void setExpectedPubDateStr( String date ) {
        //this.releaseDate = date;
    }


    //--------------------------------------------------------------------------

    private GregorianCalendar releaseDate;

    public void setReleaseDate( GregorianCalendar date ) {
        this.releaseDate = date;
    }

    public GregorianCalendar getReleaseDate() {
        return releaseDate;
    }
    
    public String getReleaseDateStr() {
        if( releaseDate != null ) {
            return dateStr( releaseDate.get(Calendar.YEAR),
                            releaseDate.get(Calendar.MONTH),
                            releaseDate.get(Calendar.DAY_OF_MONTH) );
        }  else {
            return "0000/00/00";
        }
    }
    
    public void setReleaseDateStr( String date ) {
        //this.releaseDate = date;
    }

    //--------------------------------------------------------------------------

    private String dateStr( int year, int month, int day ) {
        String date = Integer.toString( year );
        
        date = month < 9 ? date + "/0" : date + "/";
        date = date + Integer.toString( month + 1 );
        
        date = day < 10 ? date + "/0" : date + "/";
        date = date + Integer.toString( day );
        return date;
    }

}
