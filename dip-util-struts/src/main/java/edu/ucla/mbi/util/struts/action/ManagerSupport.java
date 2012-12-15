package edu.ucla.mbi.util.struts.action;
                                                                            
/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * ManagerSupport action
 *
 ======================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.struts.interceptor.*;

public abstract class ManagerSupport extends PortalSupport 
    implements AclAware {

    public final String EDIT = "edit";
     
    //---------------------------------------------------------------------
    // operations 
    //-----------

    private Map<String,String> opm;

    public void setOp( Map<String,String> op ) {
        this.opm = op;
    }

    public Map<String,String> getOp(){
        return opm;
    }

    //---------------------------------------------------------------------
    
    private Map<String,String> opp;  // params

    public void setOpp( Map<String,String> opp ) {
        this.opp = opp;
    }

    public Map<String,String> getOpp(){
        return opp;
    }

    //---------------------------------------------------------------------
    
    protected Set<String> ownerMatch;
    public void setOwnerMatch( Set<String> owner ) {
        ownerMatch = owner;
    }

    //---------------------------------------------------------------------

    protected Set<String> adminUserMatch;
    public void setAdminUserMatch( Set<String> user ) {
        adminUserMatch = user;
    }

    //---------------------------------------------------------------------

    protected Set<String> adminGroupMatch;
    public void setAdminGroupMatch( Set<String> group ) {
        adminGroupMatch = group;
    }

    //---------------------------------------------------------------------

    private int id;

    public int  getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }
   
    
    //---------------------------------------------------------------------

    public abstract String execute() throws Exception;
    
    
    //---------------------------------------------------------------------
    // validaton
    //----------

    public boolean testPass( String pass ) {
        
        if (pass != null ) {
            try {
                String tstr = pass.replaceAll( "^\\s+", "" );
                tstr = tstr.replaceAll( "\\s+$", "" );
                if ( tstr.length() < 4 || tstr.length() > 16 ) {
                    return false;
                }
                
                return true;
                
            } catch( Exception ex ) {
                // shoud not happen
            }
        }
        return false;
    }
    
    public String sanitizeString( String str ) {

        if ( str == null ) return null;

        String sstr = str.replaceAll( "^\\s+", "" );
        sstr = sstr.replaceAll( "\\s+$", "" );
        if ( sstr.length() ==  0 ) return null;
        
        return sstr;
    }

    public String sanitizeEmail( String email ) {

        if ( email != null ) {
            try {
                email = email.replaceAll("^\\s+","");
                email = email.replaceAll("\\s+$","");
            } catch( Exception ex ) {
                // shoud not happen
            }
        }
        return email;
    }
}
