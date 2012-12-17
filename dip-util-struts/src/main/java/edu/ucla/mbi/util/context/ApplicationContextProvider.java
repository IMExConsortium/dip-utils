package edu.ucla.mbi.util.context;

/* =============================================================================
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #
 # ApplicationContextProvider: Spring-based ApplicationContext access
 #
 #=========================================================================== */

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.InputStream;
import java.io.IOException;

public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static InputStream getResourceAsStream( String location ) 
        throws IOException {
        return applicationContext.getResource( location ).getInputStream() ;
    }
    
    public void setApplicationContext( ApplicationContext applicationContext ) 
        throws BeansException {
        // Assign the ApplicationContext into a static variable     
        this.applicationContext = applicationContext;
    }
}
