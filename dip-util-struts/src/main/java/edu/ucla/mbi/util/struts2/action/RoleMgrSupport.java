package edu.ucla.mbi.util.struts2.action;
                                                                            
/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * RoleMgrSupport action
 *
 ======================================================================== */

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.CookiesAware;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;

import javax.servlet.http.Cookie;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import org.json.*;

import edu.ucla.mbi.util.*;
import edu.ucla.mbi.util.dao.*;
import edu.ucla.mbi.util.struts2.interceptor.*;

public abstract class RoleMgrSupport extends ManagerSupport {
    
    //---------------------------------------------------------------------
    // Role/RoleList 
    //--------------
    
    private Role role = null;
    
    public void setRole( Role role ) {
	this.role = role;
    }
    
    public Role getRole(){
	return this.role;
    }

    //---------------------------------------------------------------------

    public List<Role> getRoleList(){
        if ( getUserContext().getRoleDao() != null ) {
            return getUserContext().getRoleDao().getRoleList();
        }
        return null;
    }
        
    //---------------------------------------------------------------------

    public String execute() throws Exception{
	
        if ( getUserContext().getRoleDao() != null && 
             getId() > 0 && role == null ) {
            role = getUserContext().getRoleDao().getRole( getId() );
        }
        
        if( getOp() != null ) {
            for ( Iterator<String> i =  getOp().keySet().iterator();
                  i.hasNext(); ) {
                
                String key = i.next();
                String val =  getOp().get( key );
                
                if ( val != null && val.length() > 0 ) {
                    
                    if ( key.equalsIgnoreCase( "add" ) ) {
                        return addRole( role );
                    }

                    //-----------------------------------------------------                   

                    if ( key.equalsIgnoreCase( "del" ) ) {
                        return deleteRole( role );
                    }
                    
                    //-----------------------------------------------------

                    if ( key.equalsIgnoreCase( "ldel" ) ) {

                        if ( getOpp() == null ) return SUCCESS;

                        String rdel = getOpp().get( "del" );
                        if ( rdel != null ) {
                            List<Integer> ridl =
                                new ArrayList<Integer>();
                            try {
                                rdel = rdel.replaceAll("\\s","");
                                String[] rs = rdel.split(",");
                                
                                for( int ii = 0; ii <rs.length; ii++ ) {
                                    ridl.add( Integer.valueOf( rs[ii] ) );
                                }
                            } catch ( Exception ex ) {
                                // should not happen
                            }
                            return deleteRoleList( ridl );                            
                        }                        
                        return SUCCESS;
                    }
                    
                    if ( key.equalsIgnoreCase( "pup" ) ) {
                        return updateRoleProperties( getId(), role );
                    }
                }
            }
        }
        return SUCCESS;
    }


    //---------------------------------------------------------------------
    
    public void validate() {

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "RoleMgrSupport: validate" );

        boolean errorFlag = false;
        
        if(  getOp() != null ) {
            for ( Iterator<String> i =  getOp().keySet().iterator();
                  i.hasNext(); ) {

                String key = i.next();
                String val =  getOp().get(key);

                log.info( "RoleMgrSupport: op=" + key );

                if ( val != null && val.length() > 0 ) {
                    if ( key.equalsIgnoreCase( "add" ) ) {
                        
                        // add validation
                        //---------------
                        String newName = role.getName();
                        if ( newName != null ) {
                            try {
                                newName = newName.replaceAll("^\\s+","");
                                newName = newName.replaceAll("\\s+$","");
                            } catch ( Exception ex ){
                                // should not happen
                            }
                            if ( newName.length() == 0 ){
                                newName = null;
                            } else {

                                // test if unique
                                //---------------
                                
                                if ( getUserContext().getRoleDao() != null ) {
                                    Role oldRole = 
                                        getUserContext().getRoleDao().getRole( newName );
                                    if ( oldRole != null ) {
                                        newName = null;
                                        role.setName( newName );
                                    }
                                }
                            }                                
                        }
                        
                        if ( newName != null ) {
                            role.setName( newName );
                        } else {
                            addFieldError( "role.name", 
                                           "Please, provide a unique, " +
                                           "non-empty role name." );   
                        }
                    }
                    
                    if ( key.equalsIgnoreCase( "del" ) ) {
                        // drop validation:
                        
                    
                    }

                    if ( key.equalsIgnoreCase( "ldel" ) ) {
                        // drop list validation: NONE ?
                    }
                    
                    if ( key.equalsIgnoreCase( "pup" ) ) {
                        // property update validation: NONE ?
                    }
                }
            }
        }

        log.info( "RoleMgrSupport: ec=" + errorFlag );

        if ( errorFlag ) {
            if ( getUserContext().getRoleDao() != null ) {
                if( getId() > 0 ){
                    role = getUserContext().getRoleDao().getRole( getId() );
                }
            }
            setBig( false );
        }
    }


    //---------------------------------------------------------------------    
    
    public String addRole( Role role ) {

        if( getUserContext().getRoleDao() == null || 
            role == null ) return SUCCESS;
        
        getUserContext().getRoleDao().saveRole( role );
        Log log = LogFactory.getLog( this.getClass() );
        log.info( " new role -> id=" + role.getId() +
                  " name=" + role.getName() );
        this.role = null;
        return SUCCESS;
    }

    
    //---------------------------------------------------------------------

    public String deleteRole( Role role ) {

        Log log = LogFactory.getLog( this.getClass() );
        if( getUserContext().getRoleDao() == null || 
            role == null ) return SUCCESS;

        Role dr = getUserContext().getRoleDao().getRole( role.getId() );
        if ( dr == null ) return SUCCESS;
        
        log.info( " delete role=" + dr );
        log.info( " group count=" +  
                  getUserContext().getRoleDao().getGroupCount( dr ) );
        log.info( " user count=" +  
                  getUserContext().getRoleDao().getUserCount( dr ) );

        if ( getUserContext().getRoleDao().getGroupCount( dr ) == 0 && 
             getUserContext().getRoleDao().getUserCount( dr ) == 0 ) {
            

            log.info( " delete role -> id=" + dr.getId() );
            getUserContext().getRoleDao().deleteRole( dr );
            this.role = null;
            setId( 0 ); 
        }
       
        return SUCCESS;
    }
    
    
    //---------------------------------------------------------------------

    private String deleteRoleList( List<Integer> roles ) {

        if( getUserContext().getRoleDao() == null || 
            roles == null ) return SUCCESS;
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "delete role list=" + roles );

        for ( Iterator<Integer> ii = roles.iterator();
              ii.hasNext(); ) {
            
            int rid = ii.next();
            Role r = getUserContext().getRoleDao().getRole( rid );
            
            if ( getUserContext().getRoleDao().getGroupCount( r ) == 0 &&
                 getUserContext().getRoleDao().getUserCount( r ) == 0 ) {
                
                log.info( " delete group -> id=" + role.getId() );
                getUserContext().getRoleDao().deleteRole( r );
            }
        }
        return SUCCESS;
    }

    
    //---------------------------------------------------------------------
    
    public String updateRoleProperties( int id, Role role ) {
        
        if( getUserContext().getRoleDao() == null ) return SUCCESS;

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "id=" + id + "role=" + role );
        
        Role oldRole = getUserContext().getRoleDao().getRole( id );
        if ( oldRole == null ) return SUCCESS;

        oldRole.setComments( role.getComments() );
        
        getUserContext().getRoleDao().updateRole( oldRole );
        this.role = getUserContext().getRoleDao().getRole( id );

        log.info( " updated role -> id=" + id );
        return SUCCESS;
    }
}
