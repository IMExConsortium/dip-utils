package edu.ucla.mbi.util.data;

/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * Comment - text 
 *
 ======================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

public class Comment extends AttachedDataItem {

    public static final int TEXT = 0;
    public static final int HTML = 1;
    public static final int WIKI = 2;
    
    public Comment() {}
    
    //--------------------------------------------------------------------------
    
    String label = "";
    
    public void setLabel( String label ) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
   
    // NOTE: alias for the label field
    // -------------------------------

    public void setSubject( String subject ) {
        this.label = subject;
    }

    public String getSubject() {
        return label;
    }

    //--------------------------------------------------------------------------

    String body = "";

    public void setBody( String body ){
        this.body = body;
    }

    public String getBody(){
        return body;
    }

    //--------------------------------------------------------------------------

    int format = 0;   // 0 - TEXT; 1 - HTML; 2 - WIKI; 

    public void setFormat( int format ){
        this.format = format;
    }

    public int getFormat(){
        return this.format;
    }

    //--------------------------------------------------------------------------

    public void setText(){
        format = Comment.TEXT;
    }

    public boolean isText(){
        return (format == Comment.TEXT);
    }
        
    public void setHtml(){
        format = Comment.HTML;
    }

    public boolean isHtml(){
        return (format == Comment.HTML);
    }
    
    public void setWiki(){
        format = Comment.WIKI;
    }

    public boolean isWiki(){
        return (format == Comment.WIKI);
    }

    public String getBodyType(){

        if( format == Comment.WIKI ) return "WIKI";
        if( format == Comment.HTML ) return "HTML";

        return "TEXT";
    }

}
