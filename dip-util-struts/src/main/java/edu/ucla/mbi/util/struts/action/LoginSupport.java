package edu.ucla.mbi.util.struts.action;
                                                                            
/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * LoginSupport action
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
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.struts.interceptor.*;

public abstract class LoginSupport extends PortalSupport {
    
    //---------------------------------------------------------------------
    // AuthenticateAware implementation
    //---------------------------------
    
    User user = null;

    public void setUser( User user ) {
	this.user = user;
    
    }
    
    public User getUser(){
	return this.user;
    }
    
    
    //---------------------------------------------------------------------

    public abstract String execute() throws Exception;

}
