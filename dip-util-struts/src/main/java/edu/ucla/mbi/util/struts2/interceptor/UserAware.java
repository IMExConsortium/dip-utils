package edu.ucla.mbi.util.struts2.interceptor;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # UserAware interface: must be implemented by classes interceptable 
 #           by UserInterceptor (not imlemented yet)
 #
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.*;

import javax.servlet.ServletContext;

public interface UserAware {

    public void setUserContext( UserContext context );
    public UserContext getUserContext();
    
}

