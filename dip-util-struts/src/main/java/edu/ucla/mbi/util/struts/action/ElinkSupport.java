package edu.ucla.mbi.util.struts.action;
                                                                            
/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * ElinkSupport action - external links support
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
import java.io.*;
import org.json.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.struts.interceptor.*;

public abstract class ElinkSupport extends ActionSupport 
    implements SessionAware, CookiesAware, ServletContextAware {
    
    
    private JsonContext context;

    public void setElinkContext( JsonContext context ) {
	this.context = context;
    }    
    public JsonContext getElinkContext() {
	return this.context;
    }
        
    //---------------------------------------------------------------------
    // SessionAware interface implementation
    //--------------------------------------
    private Map session;

    private static final int BUFFER_SIZE = 4096;

    public void setSession(Map session){
	this.session=session;
    }

    public Map getSession(){
	return session;
    }

    //---------------------------------------------------------------------
    // CookiesAware interface implementation
    //--------------------------------------
    private Map  cookies;

    public void setCookiesMap(Map cookies){
	this.cookies=cookies;
    }

    public Map getCookiesMap(){
	return cookies;
    }

    //---------------------------------------------------------------------
    // ServletContextAware interface implementation
    //---------------------------------------------
    private ServletContext servletContext;

    public void setServletContext( ServletContext context){
	this.servletContext = context;
    }

    public ServletContext getServletContext(){
	return servletContext;
    }
    
    //---------------------------------------------------------------------

    public abstract String execute() throws Exception;

}
