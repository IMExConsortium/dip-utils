package edu.ucla.mbi.util.struts2.action;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * AclMgrSupport - access control list manager                                 $
 *                                                                             $
 ============================================================================ */

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

import edu.ucla.mbi.util.*;
import edu.ucla.mbi.util.struts2.interceptor.*;

public class AclMgrSupport extends ManagerSupport {

    private static final int BUFFER_SIZE = 4096;

    //--------------------------------------------------------------------------
    // configuration
    //---------------
    
    private JsonContext aclContext;

    public void setAclContext( JsonContext context ) {
        aclContext = context;
    }

    public JsonContext getAclContext() {
        return aclContext;
    }
    
    //--------------------------------------------------------------------------
    // action parameters
    //------------------

    
    //--------------------------------------------------------------------------

    Log log = LogFactory.getLog( this.getClass() );
    
    private void initialize() {
       
	Map<String,Object> jpd = aclContext.getJsonConfig();
	
	if ( jpd == null ) {
	    log.info( " initilizing ACL list..." );
	    String jsonPath = 
		(String) aclContext.getConfig().get( "json-config" );
	    log.info( "JsonACLDef=" + jsonPath );
	
	    if ( jsonPath != null && jsonPath.length() > 0 ) {
		
		String cpath = jsonPath.replaceAll("^\\s+","" );
		cpath = jsonPath.replaceAll("\\s+$","" );

		try {
		    InputStream is = 
			getServletContext().getResourceAsStream( cpath );
		    aclContext.readJsonConfigDef( is );
		    
		    jpd = aclContext.getJsonConfig();
		    
		} catch ( Exception e ){
		    log.info( "JsonConfig (ACL) reading error" );
		}
	    }
	}
    }

    //---------------------------------------------------------------------
    //---------------------------------------------------------------------

    public String execute() throws Exception{
        
        if( getOp() != null ) {
            for ( Iterator<String> i =  getOp().keySet().iterator();
                  i.hasNext(); ) {

                String key = i.next();
                String val =  getOp().get( key );

                if ( val != null && val.length() > 0 ) {

                    if ( key.equalsIgnoreCase( "rin" ) ) {
                        initialize();
                        return SUCCESS;
                    }
                }
            }
        }
        return SUCCESS;
    }


    //--------------------------------------------------------------------------

    public void validate() {
	/*
	// missing id
	//-----------
	
	String id = getId();

	if ( id == null ) {
	    addActionError("No page id");
	} else {
	    
	    try {
		id = id.replaceAll("\\s","");
	    } catch (Exception e ){
		// none
	    }
	    
	    if ( id.length() == 0 ) {
		addActionError("No page id");
	    }
	    
	    initialize();
	    Map<String,Object> jpd = pageContext.getJsonConfig();
	    Map pages = (Map) ((Map) jpd.get("pageConfig") ).get( "pages" );
            page = (Map) pages.get( id );
	    if ( page == null ) {
		addActionError("Page not defined.");
	    }

	}
        */
    }
}
