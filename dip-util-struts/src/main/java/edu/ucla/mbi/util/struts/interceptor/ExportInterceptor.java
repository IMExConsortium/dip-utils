package edu.ucla.mbi.util.struts.interceptor;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * ExportInterceptor: data export                                              $
 *                                                                             $
 *=========================================================================== */

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

public abstract class ExportInterceptor implements Interceptor {

    protected abstract String buildExport( ValueStack stack,
                                           ExportAware action );        
    
    //--------------------------------------------------------------------------    
    
    public void destroy() { }
    public void init() { }
    
    public String intercept( ActionInvocation invocation ) 
        throws Exception {
        
        invocation.addPreResultListener( new PreResultListener() {
                public void beforeResult( ActionInvocation invocation, 
                                          String resultCode ) {
                    
                    if ( invocation.getAction() instanceof 
                         ExportAware ) {
                        build( invocation );
                    } else {
                        return; // abort if not ExportAware
                    }
                }
            } );
        
        return invocation.invoke();
    }
    
    protected void build( ActionInvocation invocation ) {
     
        Log log = LogFactory.getLog( this.getClass() );
        ValueStack stack = invocation.getStack();
        
        ExportAware action = (ExportAware) invocation.getAction();
        
        TableViewContext tableContext = action.getTableContext();
        String table = action.getTableName();
        List tableData = action.getTableData();

        String expString =  buildExport( stack, action);
        
        try { 
            byte[] bytes = expString.getBytes("UTF-8");
            action.setExportStream( new ByteArrayInputStream( bytes ) );
        } catch( UnsupportedEncodingException uee ) {
            // NOTE: should not happen
        }
        action.setContentType( "text/xml; charset=ISO-8859-1" );
    }
}
