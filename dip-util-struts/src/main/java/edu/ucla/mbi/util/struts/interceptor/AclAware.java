package edu.ucla.mbi.util.struts.interceptor;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # AclAware interface: must be implemented by classes interceptable 
 #           by AclInterceptor
 #
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.*;

import javax.servlet.ServletContext;

public interface AclAware {
    
    public void setOp( Map<String,String> op );
    public Map<String,String> getOp();

    public void setOwnerMatch( Set<String> owner );
    public void setAdminUserMatch( Set<String> aul );
    public void setAdminGroupMatch( Set<String> agl );
}

