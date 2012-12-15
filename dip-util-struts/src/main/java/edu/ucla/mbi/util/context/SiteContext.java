package edu.ucla.mbi.util.context;

/* =============================================================================
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #                                                                             $
 # SiteContext: JSON-based configuration od sites                              $
 #                                                                             $
 #     TO DO:                                                                  $
 #                                                                             $
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;
import org.json.*;

public class SiteContext {

    public SiteContext() {}

    Map<String,Map> sitemap;
    
    //--------------------------------------------------------------------------

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
