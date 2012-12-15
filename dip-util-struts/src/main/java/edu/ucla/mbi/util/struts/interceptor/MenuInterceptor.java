package edu.ucla.mbi.util.struts.interceptor;

/* =============================================================================
 # $HeadURL::                                                                  $
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #                                                                             $
 # MenuInterceptor: pre/postprocesses menu selection                           $
 #                                                                             $
 #=========================================================================== */

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

import java.io.InputStream;
import javax.servlet.ServletContext;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.context.*;

public class MenuInterceptor implements Interceptor{

    public void destroy() { }

    public void init() { 
	
    }
    
    private static List<Integer> selDefault;
    
    static {
	selDefault = new ArrayList();
	selDefault.add( 0 ); selDefault.add( 0 ); selDefault.add( 0 );
    }

    private void menuInitialize ( MenuAware action ) {
	
	Log log = LogFactory.getLog( this.getClass() );
	
	JsonContext menuContext = action.getMenuContext();        
        log.info( "MenuInterceptor: menuContext="+menuContext);

	if ( menuContext.getJsonConfigObject() == null ) {
	    log.info( " initilizing menu defs..." );
            String jsonPath =
                (String) menuContext.getConfig().get( "json-config" );
            log.info( "JsonMenuDef=" + jsonPath );
	    
            if ( jsonPath != null && jsonPath.length() > 0 ) {

                String cpath = jsonPath.replaceAll("^\\s+","" );
                cpath = jsonPath.replaceAll("\\s+$","" );
		
                try {
                    InputStream is = action.
			getServletContext().getResourceAsStream( cpath );
                    menuContext.readJsonConfigDef( is );
		} catch ( Exception e ){
                    log.info( "JsonMenuDef reading error" );
                }
            }
        }
    }
    
    public String intercept( ActionInvocation invocation )
        throws Exception {
	
	Log log = LogFactory.getLog( this.getClass() );

       	if ( invocation.getAction() instanceof MenuAware ) {
	    
	    MenuAware action = (MenuAware) invocation.getAction();
            
            //------------------------------------------------------------------
	    // load menu definitions (if needed)
	    //----------------------------------
	    
	    menuInitialize( action ); 	    

	    //------------------------------------------------------------------
	    // menu selection
	    //---------------
            
	    String mst = action.getMst();
            
	    List<Integer> sel = new ArrayList();
	    if( mst != null ) {
		String selsplit[] = mst.split( ":" );

		for( int i = 0; i < selsplit.length; i++ ) {
		    try {
			int val = Integer.parseInt( selsplit[ i ] );
			sel.add( val );
		    } catch ( NumberFormatException e ){
			sel.add( 0 );
		    }
		}
		action.setMenuSel( sel );
	    }
	}
	
        invocation.addPreResultListener( new PreResultListener() {
                public void beforeResult( ActionInvocation invocation,
                                          String resultCode ) {
                    menuProcess( invocation );
                }
            } );
	
        String result = invocation.invoke();
        return result;
    }

    public void menuProcess( ActionInvocation invocation ) {
	
	Log log = LogFactory.getLog( this.getClass() );
	
	if ( invocation.getAction() instanceof MenuAware ) {
            
	    ValueStack stack = invocation.getStack();		
	    MenuAware action = (MenuAware) invocation.getAction();
    
	    // post-process menu selection
	    //----------------------------
            
	    String mst = action.getMst();  // menu selection	                
	    List<Integer> sel = new ArrayList();
	    if( mst != null ) {
		String selsplit[] = mst.split( ":" );

		for( int i = 0; i < selsplit.length; i++ ) {
		    try {
			int val = Integer.parseInt( selsplit[ i ] );
			sel.add( val );
		    } catch ( NumberFormatException e ){
			sel.add( 0 );
		    }
		}
	    }
	    if ( sel.size() == 0) {
		sel = selDefault;
	    }
            action.setMenuSel( sel );
	}
    }
}
