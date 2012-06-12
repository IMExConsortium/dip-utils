package edu.ucla.mbi.util.struts2.action;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * PortalSupport action                                                        $
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

public abstract class PortalSupport extends ActionSupport 
    implements MenuAware, UserAware, SessionAware, 
               CookiesAware, ServletContextAware {
    
    //--------------------------------------------------------------------------
    // MenuAware implementation
    //-------------------------
        
    private String mst;
    
    public void setMst( String status ){
	this.mst = status;
    }
    
    public String getMst() {
	return this.mst;
    }

    private String mmd;

    public void setMmd( String mode ){
        this.mmd = mode;
    }

    public String getMmd() {

        if ( mmd == null ) {
            mmd = "big";
        }
        return this.mmd;
    }

    private List<Integer> menuSel;

    public void setMenuSel( List<Integer> sel ) {
	this.menuSel = sel;
    }
    public List<Integer> getMenuSel() {
	return this.menuSel;
    }

    private boolean userOn = true;

    public void setUserOn( boolean on ) {
        userOn = on;
    }

    public boolean isUserOn() {
        return userOn;
    }

    public boolean getUserOn() {
        return userOn;
    }

    private boolean searchOn = true;

    public void setSearchOn(boolean on ) {
        searchOn = on;
    }

    public boolean isSearchOn() {
        return searchOn;
    }

    public boolean getSearchOn() {
        return searchOn;
    }


    //--------------------------------------------------------------------------
    // Configuration
    //--------------
    
    // global action configuration - ActionContext

    private ActionContext context;

    public void setContext( ActionContext context ) {
        this.context = context;
    }

    public ActionContext getContext() {
        return this.context;
    }

    //--------------------------------------------------------------------------
    
    private JsonContext menuContext;

    public void setMenuContext( JsonContext context ) {
	this.menuContext = context;
    }    

    public JsonContext getMenuContext() {

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "PortalSupport: portalContext=" + portalContext );


        if( portalContext == null ){
            return this.menuContext;
        } 
        
        log.info( "PortalSupport: site=" + this.getSite() 
                  + " siteDef="+this.siteDef );

        log.info( "PortalSupport:MenuContext: " 
                  + getPortalContext().getSiteMenu( this.getSite() ) );
        return getPortalContext().getSiteMenu( this.getSite() );
    }

    //--------------------------------------------------------------------------

    private boolean bigOn = true; // header is on by default

    public void setBig( boolean big ) {
        this.bigOn = big;
    }

    public  boolean isBigOn() {
        return bigOn;
    }

    public  boolean getBig() {
        return bigOn;
    }


    public void setDummy(){}


    //--------------------------------------------------------------------------
    // Portal configuration
    //---------------------

    private PortalContext portalContext;

    public void setPortalContext( PortalContext context ) {
        this.portalContext = context;
    }

    public PortalContext getPortalContext() {        
        return this.portalContext;
    }
    
    private String site;

    public void setSite( String site ) {
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "set site=" + site );

        this.site = site;
    }

    public String getSite() {
        
        if( site!=null && site.length()>0 ){
            return this.site;
        } 
        return this.siteDef;
    }

    //--------------------------------------------------------------------------
    
    private String siteDef;

    public void setSiteDef( String site ) {
        this.siteDef = site;
    }

    public String getSiteDef() {
        return this.siteDef;
    }

    //--------------------------------------------------------------------------
    // SKIN
    //-----

    private String skin;

    public void setSkn( String skin ) {
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "set skn=" + skin);

        this.skin = skin;
    }

    public String getSkn() {
        
        if( skin!=null && skin.length()>0 ){
            return this.skin;
        } 
        
        if( portalContext != null && this.getSite() != null ){
            return portalContext.getSiteSkin( this.getSite() );
        }
        
        return this.skinDef;
    }

    //--------------------------------------------------------------------------
    
    private String skinDef;

    public void setSknDef( String skin ) {
        this.skinDef = skin;
    }

    public String getSknDef() {
        return this.skinDef;
    }
    
    //--------------------------------------------------------------------------

    String ret;
    public void setRet( String ret) {
        this.ret = ret;
    }

    public String getRet() {
        return this.ret;
    }
    
    //---------------------------------------------------------------------
    // UserAware interface implementation
    //--------------------------------------

    private UserContext userContext;

    public void setUserContext( UserContext context ) {
	this.userContext = context;
    }    
    public UserContext getUserContext() {
	return this.userContext;
    }
        
    //---------------------------------------------------------------------
    // SessionAware interface implementation
    //--------------------------------------

    private Map session;

    //private static final int BUFFER_SIZE = 4096;

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

}
