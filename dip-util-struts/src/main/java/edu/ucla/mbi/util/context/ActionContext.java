package edu.ucla.mbi.util.context;

/* =============================================================================
 # $Id:: ActionContext.java 2189 2012-04-21 17:59:45Z lukasz                   $
 # Version: $Rev:: 2189                                                        $
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

import net.spy.memcached.spring.MemcachedClientFactoryBean;
import net.spy.memcached.MemcachedClient;

public class ActionContext {

    private boolean isCacheOn = true;
    private boolean isAgentOn = true;

    private Map config = null;
    private MemcachedClientFactoryBean mcf = null;
    
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
    
    MemcachedClient mcc;

    /*
    public void setMemcachedClientFactory( MemcachedClientFactoryBean mcf ){
        this.mcf = mcf;
    }

    public MemcachedClientFactoryBean getMemcachedClientFactory(){
        return mcf;
    }
    */

    public void setMemcachedClient( MemcachedClient mcc ){
        this.mcc = mcc;
    }


    public MemcachedClient getMemcachedClient() throws Exception{
        return mcc; // (MemcachedClient) mcf.getObject();
    }

}
