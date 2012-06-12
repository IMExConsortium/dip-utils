package edu.ucla.mbi.util.data;

/* =============================================================================
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #                                                                             $
 # JsonContext: JSON-based workflow configuration                              $
 #                                                                             $
 #     TO DO:                                                                  $
 #                                                                             $
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import edu.ucla.mbi.util.data.*;

import java.util.*;
import java.io.*;
import org.json.*;

import edu.ucla.mbi.util.*;
import edu.ucla.mbi.util.data.dao.*;

public class KeyspaceContext extends JsonContext {
    
    private KeyspaceDAO keyspaceDao;
    
    public KeyspaceDAO getKeyspaceDao() {
        return keyspaceDao;
    }

    public void setKeyspaceDao( KeyspaceDAO dao ) {
        keyspaceDao = dao;
    }

    //--------------------------------------------------------------------------
    
    public void initialize() { 

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "KeyspaceContext: initializing" );
        
        FileResource fr = (FileResource) getConfig().get("json-source");
        if ( fr == null ) return;

        try {
            readJsonConfigDef( fr.getInputStream() );
        } catch ( Exception e ){
            log.info( "KeyspaceContext: json-source error" );
            return;
        }

        log.info( "KeyspaceContext: json-source OK " );
        System.out.println( "config="+ getJsonConfigObject() );
        
        if ( keyspaceDao != null ) {
        
            log.info( "KeyContext: KeyspaceDAO OK " );

            //------------------------------------------------------------------
            // initialize keyspaces
            //---------------------
            
            try {
                if( getJsonConfigObject().getJSONArray("keyspace") != null ) {
                    
                    // "keyspace":[{"name":"imex",
                    //              "prefix":"IM-",
                    //              "postfix":"NP"
                    //              "comments":"IMEx keyassinger"}]    
                    
                    JSONArray keyspaceArray = 
                        getJsonConfigObject().getJSONArray("keyspace");
                    
                    for ( int i = 0; i < keyspaceArray.length(); i++ ) {
                        JSONObject ksp = keyspaceArray.getJSONObject( i );
                        if ( ksp == null ) continue;
                        
                        String name = ksp.getString( "name" );
                        String prefix = ksp.getString( "prefix" );
                        String postfix = ksp.getString( "postfix" );
                        String comments = ksp.getString( "comments" );
                        
                        log.info( "keyspace: name=" + name +
                                  " comments=" + comments );
                        log.info( "keyspace: prefix=" + prefix +
                                  " postfix=" + postfix );
                        
                        Keyspace oldKsp = keyspaceDao.getKeyspace( name );
                        log.info( "keyspace: old=" + oldKsp );
                        if ( oldKsp != null ) continue;
                        
                        log.info( "keyspace: creating new");
                        Keyspace newKsp = new Keyspace();
                        newKsp.setName( name );
                        newKsp.setPrefix( prefix);
                        newKsp.setPostfix( postfix );
                        newKsp.setComments( comments );
                        keyspaceDao.saveKeyspace( newKsp );
                        log.info( "keyspace: creating DONE");
                    }

                }
            } catch ( Exception e ){
                log.info( "KeyContext: json-source error (status)" );
                e.printStackTrace();
                return;
            }   
        }
    }
}
