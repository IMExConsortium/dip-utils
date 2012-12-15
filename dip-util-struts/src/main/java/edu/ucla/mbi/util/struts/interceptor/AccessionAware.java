package edu.ucla.mbi.util.struts.interceptor;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # AccessionAware interface: must be implemented by classes interceptable 
 #           by AccessionInterceptor
 #
 #======================================================================= */

import java.util.*;

public interface AccessionAware {

    public void setNs( String namespace );
    public String getNs();

    public void setAc( String accession );
    public String getAc();
    
}

