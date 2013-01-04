package edu.ucla.mbi.util.context;

/* =============================================================================
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #                                                                             $
 # ActionContext: global action configration                                   $
 #                                                                             $
 #     TO DO:                                                                  $
 #                                                                             $
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.Map;
import java.util.HashMap;

import edu.ucla.mbi.util.cache.CacheClient;

public class ActionContext {

    private boolean isCacheOn = true;
    private boolean isAgentOn = true;

    private Map config = null;
    private CacheClient cacheClient = null;
    
    public ActionContext() {}
    
    public void setCache( String cache ) {
       
        Log log = LogFactory.getLog( this.getClass() );
       
        if ( cache != null ) {
            if ( cache.equalsIgnoreCase( "true" ) ||
                 cache.equalsIgnoreCase( "on" ) ||
                 cache.equalsIgnoreCase( "yes" )){
                isCacheOn = true;
            } else if ( cache.equalsIgnoreCase( "false" ) ||
                        cache.equalsIgnoreCase( "off" ) ||
                        cache.equalsIgnoreCase( "no" ) ) {
                isCacheOn = false;
            } else {
                log.info( "  cache=" + isCacheOn +
                          " (unrecognized format)" );
            }
            log.info( "  cache=" + isCacheOn );
        } else {
            log.info( "  cache=" + isCacheOn +
                      " (default)");
        }
    }
    
    public String getCache() {
	return Boolean.toString( isCacheOn );
    }

    public boolean isCacheOn() {
	return isCacheOn;
    }

    //--------------------------------------------------------------------------
       
    public void setAgent( String agent ) {

        Log log = LogFactory.getLog( this.getClass() );

        if ( agent != null ) {
            if ( agent.equalsIgnoreCase( "true" ) ||
                 agent.equalsIgnoreCase( "on" ) ||
                 agent.equalsIgnoreCase( "yes" ) ) {
                isAgentOn = true;
            } else if ( agent.equalsIgnoreCase( "false" ) ||
                        agent.equalsIgnoreCase( "off" ) ||
                        agent.equalsIgnoreCase( "no" ) ) {
                isAgentOn = false;
            } else {
                log.info( "  agent=" + isAgentOn +
                          " (unrecognized format)" );
            }
            log.info( "  agent=" + isAgentOn );
        } else {
            log.info( "  agent=" + isAgentOn +
                      " (default)");
        }
    }
    
    public String getAgent() {
	return Boolean.toString( isAgentOn );
    }

    public boolean isAgentOn() {
	return isAgentOn;
    }


    //--------------------------------------------------------------------------

    public Map getConfig(){
        if( config == null ){
            config = new HashMap();
        }
        return config;
    }

    public void setConfig( Map config ){
        this.config = config;
    }


    //--------------------------------------------------------------------------
    
    public void setCacheClient( CacheClient cacheClient ){
        this.cacheClient = cacheClient;
    }
    
    public CacheClient getCacheClient() throws Exception{
        return cacheClient;
    }
}
