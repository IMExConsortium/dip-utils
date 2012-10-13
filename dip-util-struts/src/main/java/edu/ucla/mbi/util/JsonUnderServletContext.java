package edu.ucla.mbi.util;

/*==============================================================================
 * $HeadURL::                                                                  $ 
 * $Id::                                                                       $
 * Version:: $Rev$
 *==============================================================================
 *
 * JsonUnderServletContext
 *
 *==============================================================================
 */

import java.io.*;
import javax.servlet.ServletContext;

public class JsonUnderServletContext {
    
    public static String getPathAfterReadJson ( JsonContext jsonContext,
                                                ServletContext servletContext 
                                                ) throws Exception 
    {

        String jsonPath =
                (String) jsonContext.getConfig().get( "json-config" );

        if ( jsonPath != null && jsonPath.length() > 0 ) {

            String cpath = jsonPath.replaceAll("^\\s+","" );
            cpath = jsonPath.replaceAll("\\s+$","" );

            
            try {
                InputStream is = servletContext.getResourceAsStream( cpath );
                jsonContext.readJsonConfigDef( is );
            } catch ( Exception e ){
                throw e;
            }

            return servletContext.getRealPath( cpath );
        } else {
            throw new Exception( "json configuration" ); 
        }
    }
}

