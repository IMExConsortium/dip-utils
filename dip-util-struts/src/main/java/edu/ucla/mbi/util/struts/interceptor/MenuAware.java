package edu.ucla.mbi.util.struts.interceptor;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # MenuAware interface: must be implemented by classes interceptable 
 #           by MenuInterceptor
 #
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.context.*;

import javax.servlet.ServletContext;

public interface MenuAware {

    public void setMst( String state );
    public String getMst();

    public void setMmd( String mode );
    public String getMmd();

    public void setMenuSel( List<Integer> sel );
    public List<Integer> getMenuSel();
    
    public void setUserOn(boolean on );
    public boolean isUserOn();
    public boolean getUserOn();

    public void setSearchOn(boolean on );
    public boolean isSearchOn();
    public boolean getSearchOn();
    
    public void setMenuContext( JsonContext context );
    public JsonContext getMenuContext();

    public void setServletContext( ServletContext context );
    public ServletContext getServletContext();

}

