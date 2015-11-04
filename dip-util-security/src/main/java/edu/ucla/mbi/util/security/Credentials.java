package edu.ucla.mbi.util.security;

/*==============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *------------------------------------------------------------------------------
 *
 * Credentials: test user password and authority
 *
 *============================================================================*/

import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.handler.MessageContext;

import org.apache.commons.codec.binary.Base64;
import org.vps.crypt.Crypt;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

public class Credentials {
        
        String login = null;
        String pass = null;

        public String getLogin() {
            return login;
        }

        public String getPass() {
            return pass;
        }

        public Credentials( MessageContext context ) {

            try {
                Map requestHeaders =
                    (Map) context.get(MessageContext.HTTP_REQUEST_HEADERS ) ;

                String b64str = (String)
                    ( (List) requestHeaders.get( "Authorization" ) ).get( 0 );

                String lpString =
                    new String( Base64.decodeBase64( b64str.substring( 6 ) ) );


                login = lpString.substring( 0, lpString.indexOf( ":" ) );
                pass = lpString.substring( lpString.indexOf( ":" ) + 1 );
            } catch ( Exception e ) {
                //*** ignore: login/pass left at null
            }

            Log log = LogFactory.getLog( Credentials.class );
            log.debug( "login: " + login + " pass: " + pass );
        }

        public boolean test( DaoAuthenticationProviderImpl dapi) {

            if( pass == null || pass.length() == 0 ) {
                return false;
            }

            if( login != null ) {
                Log log = LogFactory.getLog( Credentials.class );
                
                String passAfterCrypt = Crypt.crypt( "ab", pass );
                
                log.debug( "Credentials test: login=" + login 
                           + " and passAfterCrypt=" + passAfterCrypt );

                if( dapi.authorize(login, passAfterCrypt) ){
                    return true;
                }
            }
            return false;
        }
}

