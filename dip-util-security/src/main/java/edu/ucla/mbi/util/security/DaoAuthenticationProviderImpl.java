package edu.ucla.mbi.util.security;

/*==============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *------------------------------------------------------------------------------
 *
 *  DaoAuthenticationProviderImpl: 
 *      implementation of 
 *         org.springframework.security.providers.dao.DaoAuthenticationProvider
 *
 *============================================================================*/

import java.util.List;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;

//import org.springframework.security.providers.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
//import  org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import  org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DaoAuthenticationProviderImpl extends DaoAuthenticationProvider {

    private static List<String> authorityRoleList;

    //*** constructor
    public DaoAuthenticationProviderImpl() {
        Log log = LogFactory.getLog( DaoAuthenticationProviderImpl.class );
        log.info( "DaoAuthenticationProviderImpl constructor initializing ..." );
    }

    //*** setter
    public void setAuthorityRoleList(List<String> roleList) {
        this.authorityRoleList = roleList;
    }

    //*** getter
    public List<String> getAuthorityRoleList() {
        return authorityRoleList;
    }

    public boolean authorize( String login, String passwordCrypted ) {
        Log log = LogFactory.getLog( DaoAuthenticationProviderImpl.class );
        
        UsernamePasswordAuthenticationToken 
            userPassAuthenToken = 
                new UsernamePasswordAuthenticationToken( login, 
                                                         passwordCrypted );
        
        try { 
            Authentication 
                authentication = super.authenticate( userPassAuthenToken ); 
    
            Collection<?> authoGranted = authentication.getAuthorities();

            for( Iterator i = authoGranted.iterator(); i.hasNext();){
                GrantedAuthority authority = (GrantedAuthority) i.next();;
                log.info( "authorize: role = " + authority.getAuthority() );
                if( authorityRoleList.contains( authority.getAuthority() ) ) {
                    return true;
                }
            }
            
        } catch( AuthenticationException ex ) {
            log.info( "authorize: get exception=" + ex.toString() );
            return false;
        }

        return false;
    }
}
