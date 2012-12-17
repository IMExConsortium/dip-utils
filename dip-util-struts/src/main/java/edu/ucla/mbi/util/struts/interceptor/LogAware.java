package edu.ucla.mbi.util.struts.interceptor;

/* =========================================================================
 # $HeadURL:: https://imex.mbi.ucla.edu/svn/central/trunk/icentral/src/mai#$
 # $Id:: LogAware.java 216 2011-06-25 21:18:52Z lukasz                     $
 # Version: $Rev:: 216                                                     $
 #==========================================================================
 #
 # AclAware interface: must be implemented by classes interceptable 
 #           by AclInterceptor
 #
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.context.*;

import javax.servlet.ServletContext;

public interface LogAware {
    
    public void setOp( Map<String,String> op );
    public Map<String,String> getOp();
    public ServletContext getServletContext();
    public Map getSession();
    //public void setOwnerMatch( Set<String> owner );
    //public void setAdminUserMatch( Set<String> aul );
    //public void setAdminGroupMatch( Set<String> agl );
}

