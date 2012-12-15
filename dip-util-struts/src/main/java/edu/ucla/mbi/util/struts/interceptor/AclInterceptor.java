package edu.ucla.mbi.util.struts.interceptor;

/* =============================================================================
 # $HeadURL::                                                                  $
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #
 # AclInterceptor: controlls access to actions/operations
 #         
 #
 #=========================================================================== */
 
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.io.*;

import java.io.InputStream;
import javax.servlet.ServletContext;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.struts.action.*;


public class AclInterceptor implements Interceptor{

    public static final String ACL_PAGE = "acl_page";
    public static final String ACL_OPER = "acl_oper";
    public static final String ACL_TGET = "acl_tget";

    private JsonContext aclCtx;
    
    public void setAclContext( JsonContext context ) {
        aclCtx = context;
    }

    //--------------------------------------------------------------------------

    public void destroy() {}
    public void init() {}

    //--------------------------------------------------------------------------
    
    private void aclInitialize ( PortalSupport action ) {
	
        Log log = LogFactory.getLog( this.getClass() );
	      
        if ( aclCtx.getJsonConfigObject() == null ) {
	    log.info( " initilizing ACL defs..." );
            String jsonPath =
                (String) aclCtx.getConfig().get( "json-config" );
            log.info( "JsonMenuDef=" + jsonPath );
	    
            if ( jsonPath != null && jsonPath.length() > 0 ) {

                String cpath = jsonPath.replaceAll("^\\s+","" );
                cpath = jsonPath.replaceAll("\\s+$","" );
		
                try {
                    InputStream is = action.
			getServletContext().getResourceAsStream( cpath );
                    aclCtx.readJsonConfigDef( is );
		} catch ( Exception e ){
                    log.info( "JsonAclContext reading error" );
                }
            }
        }        
    }
    
    //---------------------------------------------------------------------
    
    public String intercept( ActionInvocation invocation )
        throws Exception {
        
	Log log = LogFactory.getLog( this.getClass() );
	log.info( "ACL: " );
    
        if ( ! ( invocation.getAction() instanceof PortalSupport ) ) {
            return invocation.invoke(); // no ACL control
        }

        // load menu definitions (if needed)
        //----------------------------------
            
        PortalSupport psa = (PortalSupport) invocation.getAction();
        Map sss = psa.getSession();
        
        aclInitialize( psa ); 	    
                    
        // action name
        //------------
            
        String actionName = invocation.getInvocationContext().getName();
        log.info( "ACL: action name=" + actionName );
        
        Map<String,Object> acl = 
            (Map<String,Object>) aclCtx.getJsonConfig().get( "acl" );
        
        Map<String,Object> acr = 
            (Map<String,Object>) acl.get( actionName );
        
        if ( acr == null ) return invocation.invoke(); // action not under ACL

        //----------------------------------------------------------------------
        // action-based ACL control 
        //-------------------------

        log.info( "ACL:  ruleset=" + acr );

        //----------------------------------------------------------------------
        // action-level access
        //--------------------

        List order = (List) acr.get( "order" );
        
        String login = (sss == null || (String) sss.get( "LOGIN" ) == null) ? 
            null : (String) sss.get( "LOGIN" );
            
        Set<String>  roles = sss.get( "USER_ROLE" ) == null ? null : 
            (Set<String>) ((Map) sss.get( "USER_ROLE") ).keySet();
        
        Set<String> groups = sss.get( "USER_GROUP" ) == null ? null :
            (Set<String>) ((Map) sss.get( "USER_GROUP") ).keySet();  

        Map opcAcr = null;

        for( Iterator oi = order.iterator(); oi.hasNext(); ) {
            String crt = (String) oi.next();

            if( crt.toUpperCase().equals( "ANY" ) ) {
                if ( ! (invocation.getAction() instanceof AclAware) ||
                     ((AclAware) invocation.getAction() ).getOp() == null ) {
                    break; // precedence ?
                }
            }
            
            if( crt.toUpperCase().equals( "ALLOW" ) ) {
                if ( login == null || acr.get( "allow" ) == null ) {
                    return ACL_PAGE; // no login or allow definition missing
                }
                
                Map allow = (Map) acr.get( "allow" );
                
                if ( ! aclActionVerify( allow, login, roles, groups ) ) {
                    return ACL_PAGE;
                } else {
                    if ( ! (invocation.getAction() instanceof AclAware) ) {
                        break;  // precedence ?
                    }
                }
            }

            if( crt.toUpperCase().equals( "DENY" ) ) {
                // NOTE: not implemented
            }


            if( crt.toUpperCase().equals( "OPERATION" ) ) {
                if ( ! (invocation.getAction() instanceof AclAware) || 
                     acr.get( "operation" ) == null ) {
                    return ACL_PAGE; // wrong action or missing operation acl
                }
                
                AclAware action = (AclAware) invocation.getAction();
                log.info( "ACL: AclAware action  operation=" +
                          acr.get( "operation" ) );                

                Map<String,String> op = action.getOp();
                log.info( "ACL: op=" + op );

                if ( op == null ) {
                    // or break ?
                    return invocation.invoke(); // no operation to control
                }

                Set<String> oset = op.keySet();
                String opc = "";
                if ( oset.size() == 1 ) {
                    opc = (String) oset.toArray()[0];
                } else {
                    return ACL_OPER; // operation ambiguous
                }

                opcAcr = (Map) ((Map) acr.get( "operation" )).get( opc );
                Map opcAllow = null;

                if ( opcAcr == null ) {
                    return ACL_PAGE; // missing operation acl definition
                }
                
                if ( ! aclOperationVerify( opcAcr, login, roles, groups ) ) {
                    return ACL_OPER;
                } else {
                    break;
                }              
            }
        }
        
        //----------------------------------------------------------------------
        // target-based ACL control
        //-------------------------
                        
        if ( opcAcr != null && opcAcr.get( "target") != null ) {
            
            if( ! ( invocation.getAction() instanceof AclAware ) ) {
                return ACL_TGET;
            }

            System.out.println( "target acl" );
            
            AclAware action = (AclAware) invocation.getAction();

            Set<String> owner = new HashSet<String>();
            Set<String> aul = new HashSet<String>();
            Set<String> agl = new HashSet<String>();
            
            Map target = (Map) opcAcr.get("target");
            
            // copy users
            //-----------
            List ousr = (List) target.get( "ousr" );
            
            if ( ousr != null ) {
                for ( Iterator ii = ousr.iterator(); ii.hasNext(); ) {
                    
                    String  os = (String) ii.next();
                    if ( os.equals( "__LOGIN" ) && login != null ) {
                        os = login;
                    }
                    owner.add( os );
                }
            }
            
            // copy admin users
            //-----------------

            List ausr = (List) target.get( "ausr" );
            
            if ( ausr != null ) {
                for ( Iterator ii = ausr.iterator(); ii.hasNext(); ) {
                    
                    String  os = (String) ii.next();
                    if ( os.equals( "__LOGIN" ) && login != null ) {
                        
                        os = login;
                    }
                    aul.add( os );
                }
            }

            // copy admin groups
            //------------------

            List agrp = (List) target.get( "agrp" );
            
            if ( agrp != null ) {
                for ( Iterator ii = agrp.iterator(); ii.hasNext(); ) {
                    String  os = (String) ii.next();
                    if ( os.equals( "__LOGIN" ) && login != null ) {
                        // NOTE: possibly copy user current login groups ?
                                               
                    }
                    agl.add( os );
                }
            }

            // NOTE: nulls  mean no target-level control
            //------------------------------------------
        
            action.setOwnerMatch( owner );
            action.setAdminUserMatch( aul );
            action.setAdminGroupMatch( agl );
            
        } else {
            System.out.println("NO target acl" );
        }
        
        
        String result = invocation.invoke();
        return result;
    }

    //--------------------------------------------------------------------------

    private boolean aclOperationVerify( Map acr, String login,
                                        Set<String> roles,
                                        Set<String> groups ) {

        Log log = LogFactory.getLog( this.getClass() );
        
        List order = (List) acr.get( "order" );
       
        for( Iterator oi = order.iterator(); oi.hasNext(); ) {
            String crt = (String) oi.next();

            if( crt.toUpperCase().equals( "ANY" ) ) {
                break;
            }
            
            if( crt.toUpperCase().equals( "ALLOW" ) ) {
                if ( login == null || acr.get( "allow" ) == null ) {
                    return false; // no login or allow definition missing
                }
                
                Map allow = (Map) acr.get( "allow" );
                
                if ( ! aclActionVerify( allow, login, roles, groups ) ) {
                    return false;
                } else {
                    break;
                }
            }

            if( crt.toUpperCase().equals( "DENY" ) ) {
                // NOTE: not implemented
            }

        }
        return true;
    }

    //--------------------------------------------------------------------------
    
    private boolean aclActionVerify( Map allow, String login, 
                                     Set<String> roles, 
                                     Set<String> groups ) {
        
        Log log = LogFactory.getLog( this.getClass() );

        try {
        
            // user:["lukasz97"],
            // role:["administrator"],
            // group:["ADMIN"]

            log.info( "ACL: user=" + login );
            List userl = (List) allow.get( "user" );
            if ( userl.contains( login ) ) {

                log.info( "ACL: aclVerify -> by user=" + login );               
                return true; 
            }
            
            log.info( "ACL: roles=" + roles );
            List rolel = (List) allow.get( "role" );
            for ( Iterator ii = roles.iterator(); ii.hasNext(); ) {
                Object o = ii.next();
                if ( rolel.contains( o ) ) {
                    log.info( "ACL: aclVerify -> by role=" + roles );
                    return true;
                }
            }

            log.info( "ACL: groups=" + groups );
            List groupl = (List) allow.get( "group" );
            for ( Iterator ii = groups.iterator(); ii.hasNext(); ) {
                Object o = ii.next();
                if ( groupl.contains( o ) ) {
                    log.info( "ACL: aclVerify -> by group=" + groups );
                    return true;
                }
            }
        } catch( Exception ex ) {
        }
        return false;
    }
}
