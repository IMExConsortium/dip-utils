package edu.ucla.mbi.util.context;

/* =============================================================================
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #                                                                             $
 # PortalContext: JSON-based portal configuration                              $
 #                                                                             $
 #     Portal is a collection of sites. Global configuration parameters of     $
 #     the portal are set through porlamap entries. Site parameters are set    $
 #     through sitemap entries.                                                $
 #                                                                             $
 #     TO DO:                                                                  $
 #                                                                             $
 #                                                                             $
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;
import org.json.*;

public class PortalContext {

    public PortalContext() {}

    //--------------------------------------------------------------------------
    // Portal configuration
    //---------------------

    Map<String,Object> portalmap;


    public void setPortal( Map<String,Object> portal) {
	this.portalmap = portal;
    }
    
    public Map<String,Object> getPortal() {
	return portalmap;
    }

    //--------------------------------------------------------------------------

    public String getPortalSkinPath(){
        if( portalmap != null ){
            return (String) portalmap.get( "skin-path" );
        }
        return null;
    }

    public String getPortalImagePath(){
        if( portalmap != null ){
            return (String) portalmap.get( "image-path" );
        }
        return null;
    }


    //--------------------------------------------------------------------------
    // Site configuration
    //-------------------

    Map<String,Map> sitemap;

    public void setSite( Map<String,Map> site) {
	this.sitemap = site;
    }
    
    public Map<String,Map> getSite() {
	return sitemap;
    }

    //--------------------------------------------------------------------------

    public String getSiteName( String site) {

        if( sitemap!= null && sitemap.get( site ) != null ){
            return (String) sitemap.get( site ).get( "name" );
        }
        return null;
    }

    public String getSiteSkin( String site) {

        if( sitemap!= null && sitemap.get( site ) != null ){
            return (String) sitemap.get( site ).get( "skin" );
        }
        return null;
    }

    public JsonContext getSiteMenu( String site) {

        if( sitemap!= null && sitemap.get( site ) != null ){
            return (JsonContext) sitemap.get( site ).get( "menu-config" );
        }
        return null;
    }

    public JsonContext getSiteNews( String site) {
        
        if( sitemap!= null && sitemap.get( site ) != null ){
            return (JsonContext) sitemap.get( site ).get( "news-config" );
        }
        return null;
    }

    public JsonContext getSitePages( String site) {

        if( sitemap!= null && sitemap.get( site ) != null ){
            return (JsonContext) sitemap.get( site ).get( "page-config" );
        }
        return null;
    }
}
