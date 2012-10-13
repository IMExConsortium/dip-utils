package edu.ucla.mbi.util.struts2.action;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * PageSupport - returns a page composed from a predefned file wrapped         $
 *               within a standard layout/menu                                 $
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

public class PageSupport extends PortalSupport {

    private static final int BUFFER_SIZE = 4096;

    //--------------------------------------------------------------------------
    // configuration
    //---------------

    JsonContext pageContext;
    
    public JsonContext getPageContext() {
        
        if( getPortalContext() == null ){
            return this.pageContext;
        }

        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "PageSupport: site=" + this.getSite()
                   + " siteDef="+this.getSiteDef() );
        return getPortalContext().getSitePages( this.getSite() );
    }
    
    public void setPageContext( JsonContext context ) {
	this.pageContext = context;
    }

    //--------------------------------------------------------------------------
    // action parameters
    //------------------

    private String pageId;

    public void setId( String id ) {
        this.pageId = id;
    }

    public String getId() {
        return pageId;
    }
    
    public Map page;
    public void setPage( Map page ) {
        this.page =  page;
    }

    public Map getPage(){
        return this.page;
    }
    
    private String pageSource;

    public void setSource( String source ) {
        this.pageSource = source;
    }

    public String getSource() {
        return pageSource;
    }
    
    //--------------------------------------------------------------------------


    private void initialize() {
	
	Map<String,Object> jpd = getPageContext().getJsonConfig();
	
	if ( jpd == null ) {
                
            Log log = LogFactory.getLog( this.getClass() ); 
	    log.info( " initilizing page defs..." );
	    String jsonPath = 
		(String) getPageContext().getConfig().get( "json-config" );
	    log.debug( "JsonPageDef=" + jsonPath );
	
	    if ( jsonPath != null && jsonPath.length() > 0 ) {
		
		String cpath = jsonPath.replaceAll("^\\s+","" );
		cpath = jsonPath.replaceAll("\\s+$","" );

                log.debug( "ServletContext =" + getServletContext() );

		try {
		    InputStream is = 
			getServletContext().getResourceAsStream( cpath );
		    getPageContext().readJsonConfigDef( is );
		    
		    jpd = getPageContext().getJsonConfig();
		    
		} catch ( Exception e ){
		    log.info( "JsonConfig reading error" );
		}
	    }
	}
    }
   
    public void findMenuPage() {

        Log log = LogFactory.getLog( this.getClass() );

        Map<String,Object> jpd = getPageContext().getJsonConfig();

        if ( getId() == null || getId().length() <= 0 ) {
            addActionError( "No page id" );
        } else {

            Map pages = (Map) ((Map) jpd.get("pageConfig") ).get( "pages" );
            page = (Map) pages.get( getId() );

            log.info("PageSupport: id=" + getId() );

            if ( page != null ) {
                log.info( "page=" + page );

                log.info(" PageAction: title=" + page.get( "title" ) +
                         " menusel=" + page.get( "menusel" ) +
                         " menudef=" + page.get( "menudef" ) );

                // default tab selection
                //----------------------

                if ( getMst() == null || getMst().length() == 0 ) {
                    setMst( (String) page.get( "menusel" ) );
                    if ( getMst() == null || getMst().length() == 0 ) {
                        setMst( "" );
                    }
                }
            } else {
                addActionError( "No page found" );
            }
        }
    }

    public boolean doJsonFileUpdate( JsonContext jsContext, String jsonFilePath) {

        Log log = LogFactory.getLog( this.getClass() );
        
        PrintWriter pw = null;

        try {

            log.info( " write: jfPath=" + jsonFilePath );
            pw = new PrintWriter ( new File ( jsonFilePath ) );

            synchronized(this) {
                jsContext.writeJsonConfigDef ( pw );
                log.info( " doUpdate: after write json context. " );
            }

            pw.flush();
        } catch ( Exception e ) {
            log.info ( "JSON printting error: " + e.toString() );
            return false;
        } finally {
            if( pw != null ){
                pw.close();
            }
        }

        return true;
    }
 
    public String execute() throws Exception {
	
	initialize();
	
	Map<String,Object> jpd = getPageContext().getJsonConfig();
	
       	if ( getId() != null && getId().length() > 0 ) {

	    Map pages = (Map) ((Map) jpd.get("pageConfig") ).get( "pages" );
	    page = (Map) pages.get( getId() );

            Log log = LogFactory.getLog( this.getClass() );
    	    log.info("PageSupport: id=" + getId() + 
                     " path=" + getPage().get("viewpath") );
            
	    if ( page != null ) {
		log.info( "page=" + page );

		log.info(" PageAction: title=" + page.get( "title" ) + 
			 " menusel=" + page.get( "menusel" ) +
			 " menudef=" + page.get( "menudef" ) );
		
		// default tab selection
		//----------------------

		if ( getMst() == null || getMst().length() == 0 ) {
		    setMst( (String) page.get( "menusel" ) );
		    if ( getMst() == null || getMst().length() == 0 ) {
                        setMst( "" );
		    }			
		}
		
		// file path
		//----------

		String path = (String) page.get( "viewpath" );		
		StringBuffer sb = new StringBuffer();
		
		try{
		    char[] buffer = new char[BUFFER_SIZE];

		    InputStream is = 
                        getServletContext().getResourceAsStream( path ); 
		    InputStreamReader ir = new InputStreamReader( is ); 

		    int len =0;
		    while ( (len = ir.read( buffer, 0, 
                                            BUFFER_SIZE ) ) >= 0 ) {
			sb.append( buffer , 0, len); 
		    }
		} catch(Exception ioe) {
		    // thrown if no file ?
		}

		// page content
		//-------------

		String  vt = (String) page.get( "viewtype" );
		
		if ( vt == null || vt.length() == 0 || 
		     vt.equals( "file-html" ) ) {

		    // default format: html - pass through
		    //------------------------------------
		   		    
		    //pageSource = 
                    //    be.devijver.wikipedia.Parser.toHtml( sb.toString(), 
                    //                                        null );
		    
		    pageSource = sb.toString();
		} else {
		    // alternate formats: convert if needed
		    //-------------------------------------
		    pageSource = sb.toString();

		}
	    }
	}
	
	if ( getRet() != null && getRet().equals( "jqmenu" ) ) {
	    return "json";
	} 

	if ( getRet() != null && getRet().equals( "body" ) ) {
	    return "body";
	} 

	return SUCCESS;
    }

    //--------------------------------------------------------------------------

    public void validate() {
	
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
	    Map<String,Object> jpd = getPageContext().getJsonConfig();
	    Map pages = (Map) ((Map) jpd.get("pageConfig") ).get( "pages" );
            page = (Map) pages.get( id );
	    if ( page == null ) {
		addActionError("Page not defined.");
	    }

	}
    }
}
