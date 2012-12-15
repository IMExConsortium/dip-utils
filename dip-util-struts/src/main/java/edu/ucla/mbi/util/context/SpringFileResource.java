package edu.ucla.mbi.util.context;

/* ========================================================================
 # $Id::                                                                  $
 # Version: $Rev::                                                        $
 #=========================================================================
 #                                                                        $
 # SpringFileResource: Spring-based filesystem access                     $
 #                                                                        $
 #     TO DO:                                                             $
 #                                                                        $
 #======================================================================= */

import java.io.*;
import org.springframework.core.io.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringFileResource 
    implements FileResource, ApplicationContextAware {
    
    String fileName;
    
    public void setFile( String file ){
        fileName = file;
    }
    
    ApplicationContext context;
    
    public void setApplicationContext( ApplicationContext context ) {
        this.context = context;
    }
    
    //---------------------------------------------------------------------
    
    public InputStream  getInputStream() throws IOException {
        if ( context == null ) return null;
        return context.getResource( fileName ).getInputStream();
    } 
}

