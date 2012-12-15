package edu.ucla.mbi.util;

/* =============================================================================
 # $HeadURL::                                                                  $
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #
 # AclVerify: controlls access to actions/operations
 #
 #
 #=========================================================================== */

import java.io.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.context.*;

public class AclValidator{
    
    private JsonContext aclContext;

    public void setAclContext( JsonContext context ) {
        aclContext = context;
    }

    public JsonContext getAclContext(){
        return aclContext;
    }
    
    public void initialize(){

        Log log = LogFactory.getLog( this.getClass() );

        if ( aclContext.getJsonConfigObject() == null ) {
            
            log.info( " initilizing ACL context..." );

            String jsonPath =
                (String) aclContext.getConfig().get( "json-config" );
            log.info( "JsonMenuDef=" + jsonPath );
            
            if ( jsonPath != null && jsonPath.length() > 0 ) {

                String cpath = jsonPath.replaceAll("^\\s+","" );
                cpath = jsonPath.replaceAll("\\s+$","" );

                try {
                    InputStream is = ApplicationContextProvider
                        .getResourceAsStream(  cpath );
                    aclContext.readJsonConfigDef( is );
                    
                } catch ( Exception e ){
                    e.printStackTrace();
                    log.info( "JsonAclContext reading error" );
                }
            }
        }

        log.info( " initilizing validator..." );

    }
    
    //--------------------------------------------------------------------------
    // action-based control
    //---------------------
    
    public boolean verify( String action, String op,
                           String login, Set<String> role, Set<String> group ){

        return verify( action, op, login, role, group, null, null, null);
        
    }

    //--------------------------------------------------------------------------
    // target-based ACL control
    //-------------------------
    
    public boolean verify( String action, String op, 
                           String login, Set<String> role, Set<String> group,
                           String town, Set<String> tadm, Set<String> tgrp ){
        
        //target:{
        //    ousr:["__LOGIN"],
        //        ausr:["__LOGIN","DIP"],
        //        agrp:["USR","DIP"]
        //        }

        Set<String> tol = null;
        if(town !=null ){
            tol = new HashSet<String>();
            tol.add( town );
        }

        Map<String,Object> acl =
            (Map<String,Object>) aclContext.getJsonConfig().get( "acl" );

        Map<String,Object> aca = (Map<String,Object>) acl.get( action );
        if ( aca == null ) return true; // action not under ACL

        Map<String,Object> aco = (Map<String,Object>) aca.get( op );
        if ( aco == null ) return true; // operation not under ACL

        List order = (List) aco.get( "order" );
        
        for( Iterator oi = order.iterator(); oi.hasNext(); ) {
            String crt = (String) oi.next();

            if( crt.equalsIgnoreCase( "ANY" ) ){
                return true;
            }

            if( crt.equalsIgnoreCase( "DENY" ) ){
                Map denyAcr = (Map) aco.get( crt );
                
                if( actionMatch( denyAcr, login, role,  group ) ){
                    return false;
                }
            }

            if( crt.equalsIgnoreCase( "ALLOW" ) ){
                Map allowAcr = (Map) aco.get( crt );
                
                return actionMatch( allowAcr, login, role,  group );
            }

            if( crt.equalsIgnoreCase( "TARGET-DENY" ) ){
                Map dtgtAcr = (Map) aco.get( crt );

                if( tol != null 
                    && targetMatch( dtgtAcr, login, role, group,
                                     tol, tadm, tgrp ) ){
                    return false;
                }
            }

            if( crt.equalsIgnoreCase( "TARGET" ) ){
                Map tgtAcr = (Map) aco.get( crt );
                
                return targetMatch( tgtAcr, login, role, group,
                                     tol, tadm, tgrp );
            }
        }

        return false;
    }

    //--------------------------------------------------------------------------
    
    private boolean actionMatch( Map allow, String login, 
                                 Set<String> roles, 
                                 Set<String> groups ) {
        
        Log log = LogFactory.getLog( this.getClass() );
        
        try {
        
            // user:["lukasz97"],
            // role:["administrator"],
            // group:["ADMIN"]

            log.info( "ACL: user=" + login );
            List userl = (List) allow.get( "user" );
            if ( userl.contains( login ) ) {

                log.info( "ACL: aclVerify -> by user=" + login );               
                return true; 
            }
            
            log.info( "ACL: roles=" + roles );
            List rolel = (List) allow.get( "role" );
            for ( Iterator ii = roles.iterator(); ii.hasNext(); ) {
                Object o = ii.next();
                if ( rolel.contains( o ) ) {
                    log.info( "ACL: aclVerify -> by role=" + roles );
                    return true;
                }
            }

            log.info( "ACL: groups=" + groups );
            List groupl = (List) allow.get( "group" );
            for ( Iterator ii = groups.iterator(); ii.hasNext(); ) {
                Object o = ii.next();
                if ( groupl.contains( o ) ) {
                    log.info( "ACL: aclVerify -> by group=" + groups );
                    return true;
                }
            }
        } catch( Exception ex ) {
        }
        return false;
    }

    //--------------------------------------------------------------------------

    private boolean operationVerify( Map acr, String login,
                                     Set<String> roles,
                                     Set<String> groups ) {
        
        Log log = LogFactory.getLog( this.getClass() );
        
        List order = (List) acr.get( "order" );
        
        for( Iterator oi = order.iterator(); oi.hasNext(); ) {
            String crt = (String) oi.next();

            if( crt.toUpperCase().equals( "ANY" ) ) {
                break;
            }
            
            if( crt.toUpperCase().equals( "ALLOW" ) ) {
                if ( login == null || acr.get( "allow" ) == null ) {
                    return false; // no login or allow definition missing
                }
                
                Map allow = (Map) acr.get( "allow" );
                
                if ( ! actionMatch( allow, login, roles, groups ) ) {
                    return false;
                } else {
                    break;
                }
            }

            if( crt.toUpperCase().equals( "DENY" ) ) {
                // NOTE: not implemented
            }

        }
        return true;
    }

    //--------------------------------------------------------------------------

    private boolean targetMatch( Map target, String login, 
                                 Set<String> roles, Set<String> groups,
                                 Set<String> town, 
                                 Set<String> tadm, 
                                 Set<String> tgrp ){
        
        Log log = LogFactory.getLog( this.getClass() );
        log.debug("LOGIN="+login+ " r="+roles+" g="+groups);
        log.debug("TGT="+town+ " au="+tadm+" ag="+tgrp);

        if( town == null ) return true;

        if( target.get("ousr") != null ){
            for( Iterator icou = ((List) target.get("ousr")).iterator();
                 icou.hasNext(); ){
                
                String cou = (String) icou.next();
        
                if( cou.equals("__LOGIN") && login != null ){
                    if( town.contains( login ) ) return true; 
                } else {
                    if( town.contains( cou ) ) return true;
                }
            }
        }

        if( target.get("ausr") != null ){
            for( Iterator icau = ((List)target.get("ausr")).iterator();
                 icau.hasNext(); ){
                
                String cau = (String) icau.next();
        
                if( cau.equals("__LOGIN") && login != null ){
                    if( tadm.contains( login ) ) return true;
                } else {
                    if( tadm.contains( cau ) ) return true;
                }
            }
        }

        if( target.get("agrp") != null ){
            for( Iterator icag = ((List)target.get("agrp")).iterator();
                 icag.hasNext(); ){
                
                String cag = (String) icag.next();
                
                if( cag.equals("__LOGIN") && groups != null ){
                    for( Iterator<String> igrp = groups.iterator();
                         igrp.hasNext(); ){

                        String cgrp = igrp.next();
                        log.debug("acl(agrp)="+ cgrp + " tgrp=" + tgrp);
                        if( tgrp.contains( cgrp ) ) return true;
                    }
                } else {

                    log.debug("acl(grp)="+ cag + " tgrp=" + tgrp);
                    if( tgrp.contains( cag ) ) return true;
                }
            }
        }
        return false;      
    }
}
