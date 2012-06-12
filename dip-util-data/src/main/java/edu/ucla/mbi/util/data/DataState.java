package edu.ucla.mbi.util.data;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * DataState - status of a data item                                           $
 *                                                                             $
 ============================================================================ */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

import edu.ucla.mbi.util.*;

public class DataState {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    //---------------------------------------------------------------------

    private String name = "";

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    //---------------------------------------------------------------------

    private String comments = "";

    public void setComments( String comments ) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }    


    //--------------------------------------------------------------------------
    // equality
    //---------

    public int hashCode() {
        if( id != null ) {
            return id.intValue();
        } else {
            return 0;
        }
    }

    public boolean equals( Object obj ) {

        if( obj.getClass() != this.getClass() ){
            return false;
        }

        if( this.getId() == null || ((DataState) obj).getId() == null ) {
            return false;
        }

        if( this.getId().intValue() == ((DataState) obj).getId().intValue() ) {
            return true;
        }
        return false;
    }
    
}