package edu.ucla.mbi.util.struts.interceptor;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * JqTableViewInterceptor: generates jqGrid-compatible data for classes        $
 *  implementing TableViewAware interface                                      $
 *                                                                             $
 *=========================================================================== */

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public abstract class TableViewInterceptor implements Interceptor {
    
    public void destroy() { }
    public void init() { }
    
    public String intercept( ActionInvocation invocation ) 
        throws Exception {
        
        invocation.addPreResultListener( new PreResultListener() {
                public void beforeResult( ActionInvocation invocation, 
                                          String resultCode ) {
                    
                    if ( invocation.getAction() instanceof 
                         TableViewAware ) {
                        build( invocation );
                    } else {
                        return; // abort if not TableViewAware
                    }
                }
            } );
        
        return invocation.invoke();
    }

    protected abstract void build( ActionInvocation invocation );        
}
