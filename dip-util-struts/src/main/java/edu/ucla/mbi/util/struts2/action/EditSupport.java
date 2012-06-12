package edu.ucla.mbi.util.struts2.action;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * EditSupport - editing of pages/menus                                        $
 *                                                                             $
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

public abstract class EditSupport extends PortalSupport {

    public static final String STORE ="STORE";

    //--------------------------------------------------------------------------
    // configuration
    //---------------

    JsonContext pageContext;

    public JsonContext getPageContext() {

        if( getPortalContext() == null ){
            return this.pageContext;
        }
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "PageSupport: site=" + this.getSite()
                  + " siteDef="+this.getSiteDef() );
        return getPortalContext().getSitePages( this.getSite() );
    }

    public void setPageContext( JsonContext context ) {
        this.pageContext = context;
    }

    //--------------------------------------------------------------------------
    // edited page 
    //------------
    
    private String pageId;

    public void setPageid( String pageId ) {
        this.pageId = pageId;
    }

    public String getPageid() {
        return pageId;
    }

    private String newId;

    public void setNewid( String newId ) {
        this.newId = newId;
    }

    public String getNewid() {
        return newId;
    }

    private Map page = new HashMap();
    
    public void setPage( Map page ) {
        this.page = page;
    }
    
    public Map getPage() {
        return page;
    }
    
    private String pageSource;
    
    public void setSource( String source ) {
        this.pageSource = source;
    }
    
    public String getSource() {
        return pageSource;
    }
    
    //--------------------------------------------------------------------------
    // edited item
    //------------

    private Map item = new HashMap();

    public void setItem( Map item ) {
        this.item = item;
    }

    public Map getItem() {
        return item;
    }

    //--------------------------------------------------------------------------
    // operation
    //----------

    private Map  operation;

    public void setOpr( Map operation ) {
        this.operation = operation;
    }

    public Map getOpr() {
        return operation;
    }

    //--------------------------------------------------------------------------
    // item to drop
    //-------------

    private List<String> dropid;

    public void setDropid( List<String> id ) {
        dropid = id;
    }
    public List<String> getDropid() {
        return dropid;
    }

    //--------------------------------------------------------------------------
    // return ???
    //-----------

    //String page;
    //
    //public void setPage( String page ) {
    //    this.page =  page;
    //}
    //
    //public String getPage(){
    //    return this.page;
    //}

    String ret = ActionSupport.SUCCESS;

    public void setRet( String ret ) {
        this.ret = ret;
    }

    public String getRet(){
        return this.ret;
    }

    //--------------------------------------------------------------------------

    public abstract String execute() throws Exception;
}
