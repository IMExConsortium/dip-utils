package edu.ucla.mbi.util.struts.action;
                                                                            
/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * UserSupport action
 *                
 *
 ======================================================================== */

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.CookiesAware;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;

import javax.servlet.http.Cookie;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.json.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;
import edu.ucla.mbi.util.struts.interceptor.*;

public abstract class UserSupport extends PortalSupport {

    public final String ACTIVATE = "activate";
    public final String HOME = "home";
    public final String UEDIT = "uedit";
    public final String LOGF = "logf";
    public final String REGF = "regf";
    public final String JSON = "json";

        
    //---------------------------------------------------------------------
    // AuthenticateAware implementation
    //---------------------------------
    
    private User user = null;

    public void setUser( User user ) {
	this.user = user;
    
    }
    
    public User getUser(){
	return this.user;
    }
    
    //---------------------------------------------------------------------
    // UserSupport 
    //------------

    private String operation = "" ;
    
    public void setOp( String op ) {
	this.operation = op;
    }

    public String getOp(){
	return operation;
    }
    
    private String activationKey = "";
    
    public void setAk( String key ) {
	this.activationKey = key;
    }

    public String getAk(){
	return this.activationKey;
    }

    protected String prefs = "";
    
    public void setPrefs( String prefs ) {
        this.prefs = prefs;
    }

    protected String rurl = null;

    public void setRurl( String rurl ) {
        this.rurl = rurl;
    }

    public String getRurl() {
        return rurl;
    }

    //---------------------------------------------------------------------

    public String execute() throws Exception{
	
	Log log = LogFactory.getLog( UserSupport.class );
        log.info( " execute action: op=" + operation );

	if ( operation != null & operation.equalsIgnoreCase( "reg" ) ) {
	    return register( user );
	}
	if ( operation != null & operation.equalsIgnoreCase( "act" ) ) {
	    return activate( user );
	}
	if ( operation != null & operation.equalsIgnoreCase( "edit" ) ) {
	    return edit();
	}
	if ( operation != null & operation.equalsIgnoreCase( "login" ) ) {
	    return login( user );
	}
	if ( operation != null & operation.equalsIgnoreCase( "logout" ) ) {
	    return logout();
	}
	if ( operation != null & operation.equalsIgnoreCase( "regf" ) ) {
	    return REGF;
	}
	if ( operation != null & operation.equalsIgnoreCase( "logf" ) ) {
	    return LOGF;
	}
	if ( operation != null & operation.equalsIgnoreCase( "actf" ) ) {
	    return ACTIVATE;
	}

	if ( operation != null & operation.equalsIgnoreCase( "getprefs" ) ) {
	    edit();
            return JSON;
	}

	if ( operation != null & operation.equalsIgnoreCase( "setprefs" ) ) {
	    edit();
            return JSON;

	}
	return LOGF;
    }

    public abstract String register( User user );

    public abstract String activate( User user );

    public abstract String edit();

    public abstract String login( User user );

    public abstract String logout();

}
