package edu.ucla.mbi.util.struts.action;
                                                                            
/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * GropuMgrSupport action
 *
 ======================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
//import java.util.concurrent.*;
//import java.io.*;

//import org.json.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;
import edu.ucla.mbi.util.struts.interceptor.*;

public abstract class GroupMgrSupport extends ManagerSupport {
        
    //---------------------------------------------------------------------
    //  Group/GroupList
    //-----------------
    
    private Group group;

    public void setGroup( Group group ) {
	this.group = group;    
    }
    
    public Group getGroup(){
	return this.group;
    }

    //---------------------------------------------------------------------
    
    public List<Group> getGroupList() {
        if (  getUserContext().getGroupDao() != null ) {
            return getUserContext().getGroupDao().getGroupList();
        } 
        return null;
    }

    //---------------------------------------------------------------------
    //  RoleAll list  
    //--------------
 
    public List<Role> getRoleAll(){
        
        if ( getUserContext().getRoleDao() != null ) {
            return getUserContext().getRoleDao().getRoleList();
        }
        return null;
    }
    
    
    //---------------------------------------------------------------------
    
    public String execute() throws Exception{
	
        if ( getUserContext().getGroupDao() != null 
             &&  getId() > 0 && group == null ) {
            group = getUserContext().getGroupDao().getGroup( getId() );                       
        }
        
        if( getOp() == null ) return SUCCESS;
        
        for ( Iterator<String> i = getOp().keySet().iterator();
              i.hasNext(); ) {

            String key = i.next();
            String val = getOp().get( key );
            
            if ( val != null && val.length() > 0 ) {

                if ( key.equalsIgnoreCase( "add" ) ) {                        
                    return addGroup( group );
                }

                //---------------------------------------------------------                

                if ( key.equalsIgnoreCase( "del" ) ) {
                    return deleteGroup( group );
                }

                //---------------------------------------------------------                

                if ( key.equalsIgnoreCase( "ldel" ) ) {
                    
                    if ( getOpp() == null ) return SUCCESS;

                    String gdel = getOpp().get( "del" );
                    if ( gdel != null ) {
                        List<Integer> gidl =
                            new ArrayList<Integer>();
                        try {
                            gdel = gdel.replaceAll("\\s","");
                            String[] gs = gdel.split(",");
                            
                            for( int ii = 0; ii <gs.length; ii++ ) {
                                gidl.add( Integer.valueOf( gs[ii] ) );
                            }
                        } catch ( Exception ex ) {
                            // should not happen
                        }
                        return deleteGroupList( gidl );
                        
                    } 
                    return SUCCESS;
                }

                //---------------------------------------------------------                

                if ( key.equalsIgnoreCase( "pup" ) ) {
                    return updateGroupProperties( getId(), group );
                }
                
                //---------------------------------------------------------                
                
                if ( key.equalsIgnoreCase( "radd" ) ) {  
                    if ( getOpp() == null ) return SUCCESS;
                    String rid = getOpp().get( "radd" );
                    try { 
                        int irid = Integer.parseInt( rid );
                        return updateGroupRoles( getId(), irid , null );
                    }  catch ( Exception ex) {
                        // ignore
                    }
                }

                //---------------------------------------------------------                

                if ( key.equalsIgnoreCase( "rdel" ) ) {
                    if ( getOpp() == null ) return SUCCESS;
                    
                    String rdel = getOpp().get( "rdel" );
                    
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
                        return updateGroupRoles( getId(), 0, ridl );
                    } else {
                        group = 
                            getUserContext().getGroupDao().getGroup( getId() );
                    }
                }
            }
        }
        return SUCCESS;
    }

    //---------------------------------------------------------------------
    // validation
    //-----------

    public void validate() {

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "GroupMgrSupport: validate" );
        
        boolean loadGroupFlag = false;
        
        if( getOp() != null ) {
            for ( Iterator<String> i = getOp().keySet().iterator();
                  i.hasNext(); ) {
                
                String key = i.next();
                String val = getOp().get(key);

                if ( val != null && val.length() > 0 ) {

                    //-----------------------------------------------------                
                    
                    if ( key.equalsIgnoreCase( "add" ) ) {
                        
                        // add group validation
                        //---------------------

                        // label: unique, upper case
                        //--------------------------

                        String newLabel = 
                            sanitizeString( group.getLabel() );
                        
                        if ( newLabel != null ) {
                            newLabel = newLabel.toUpperCase();
                            
                            // test if unique
                            //---------------
                            
                            if ( getUserContext().getGroupDao() != null && 
                                 getUserContext().getGroupDao().getGroup( newLabel ) != null ) {
                                
                                newLabel = null;
                                group.setLabel( newLabel );
                            }                                
                        }
                        
                        if ( newLabel != null ) {
                            group.setLabel( newLabel );
                        } else {
                            addFieldError( "group.label", 
                                           "Group label must be unique." );
                        }
                        
                        // name: non-empty
                        //----------------

                        String newName = 
                            sanitizeString( group.getName() );
                        
                        if ( newName != null ) {
                            group.setName( newName );
                        } else {
                            addFieldError( "group.name", 
                                           "Group name cannot be empty." );   
                        }
                    
                        break;
                    }
                    
                    //-----------------------------------------------------                
                    
                    if ( key.equalsIgnoreCase( "del" ) ) {
                        // group drop validation: NONE ?
                        break;
                    }
                    
                    //-----------------------------------------------------                
                    
                    if ( key.equalsIgnoreCase( "ldel" ) ) {
                        // group list drop validation: NONE ?
                        break;
                    }
                    
                    //-----------------------------------------------------                
                    
                    if ( key.equalsIgnoreCase( "pup" ) ) {
                        
                        if ( getOpp() == null || 
                             getUserContext().getUserDao() == null ) return;
                        
                        // group property update validation
                        //----------------------------------
                        
                        // contact user
                        //-------------
                        
                        String cLogin = 
                            sanitizeString( getOpp().get( "clogin" ) );
                        if ( cLogin != null ) {
                            User cUser = 
                                getUserContext().getUserDao().getUser( cLogin );
                            if ( cUser == null ) {
                                addFieldError( "opp.clogin", 
                                               "Login does not exists." );
                                loadGroupFlag = true;
                            } else {
                                group.setContactUser( cUser );
                            }
                        }
                        
                        
                        // admin user
                        //-----------
                        
                        String aLogin = 
                            sanitizeString( getOpp().get( "alogin" ) );
                        if ( aLogin != null ) {
                            User aUser = 
                                getUserContext().getUserDao().getUser( aLogin );
                            if ( aUser == null ) {
                                addFieldError( "opp.alogin", 
                                               "Login does not exist." );
                                loadGroupFlag = true;
                            } else {
                                group.setAdminUser( aUser );
                            }
                        }        
                        break;
                    }

                    if ( key.equalsIgnoreCase( "rdel" ) ) {
                        // role drop validation: NONE
                        
                        break;
                    }
                    
                    if ( key.equalsIgnoreCase( "radd" ) ) {
                        // role add validation: NONE

                        break;
                    }
                }
            }
        }
        
        if ( loadGroupFlag && getId() > 0 ) {
            group = getUserContext().getGroupDao().getGroup( getId() );
            setBig( false );
        }
    }


    //---------------------------------------------------------------------
    // operations
    //-----------
    
    private String addGroup( Group group ) {
        
        if( getUserContext().getGroupDao() == null || 
            group == null ) return SUCCESS;
        
        getUserContext().getGroupDao().saveGroup( group );
        Log log = LogFactory.getLog( this.getClass() );
        log.info( " new group -> id=" + group.getId() +
                  " label=" + group.getLabel() );            

        this.group = null;
        return SUCCESS;
    }
    
    //---------------------------------------------------------------------

    private String deleteGroup( Group group ) {
        
        if( getUserContext().getGroupDao() == null || 
            group == null ) return SUCCESS;
        
        if ( getUserContext().getGroupDao().getUserCount( group ) == 0 ) {
            
            Log log = LogFactory.getLog( this.getClass() );
            log.info( " delete group -> id=" + group.getId() );
            getUserContext().getGroupDao().deleteGroup( group );
            this.group = null;
            setId( 0 );
        }
        return SUCCESS;
    }

    //---------------------------------------------------------------------

    private String deleteGroupList( List<Integer> groups ) {
        
        if( getUserContext().getGroupDao() == null || 
            groups == null ) return SUCCESS;

        Log log = LogFactory.getLog( this.getClass() );
        
        for ( Iterator<Integer> ii = groups.iterator();
              ii.hasNext(); ) {
            
            int gid = ii.next();
            Group g = getUserContext().getGroupDao().getGroup( gid );
            
            if (  getUserContext().getGroupDao().getUserCount( g ) == 0 ) {
             
                log.info( " delete group -> id=" + g.getId() );
                getUserContext().getGroupDao().deleteGroup( g );                
            }
        }
        return SUCCESS;
    }
    
    //---------------------------------------------------------------------
    
    private String updateGroupProperties( int id, Group group ) {

        if( getUserContext().getGroupDao() == null ) return SUCCESS;
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "id=" + id );
        
        Group oldGroup = getUserContext().getGroupDao().getGroup( id );
        if ( oldGroup == null ) return SUCCESS;

        oldGroup.setName( group.getName() );
        oldGroup.setAdminUser( group.getAdminUser() );
        oldGroup.setContactUser( group.getContactUser() );
        oldGroup.setComments( group.getComments() );
        
        log.info( " au=" + group.getAdminUser() );
        log.info( " cu=" + group.getContactUser() );
        
        getUserContext().getGroupDao().updateGroup( oldGroup );
        this.group = getUserContext().getGroupDao().getGroup( id ); 
        
        log.info( " updated group -> id=" + id );   
        return SUCCESS;
            
    }
    
    //---------------------------------------------------------------------
    
    private String updateGroupRoles( int id, int rid, 
                                     List<Integer> rdel ) {

        if ( getUserContext().getGroupDao() == null ||
             getUserContext().getRoleDao() == null ) return SUCCESS;
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "id=" + id + " radd=" + rid );
        
        if ( rid > 0  ) {
                
            Role role = getUserContext().getRoleDao().getRole ( rid );
            Group oldGroup = getUserContext().getGroupDao().getGroup (id );
            
            log.info( "group=" + oldGroup + " role=" + role );
            
            if ( role != null && oldGroup != null) {
                
                if ( ! oldGroup.getRoles().contains( role ) ) {
                    oldGroup.getRoles().add( role );
                    getUserContext().getGroupDao().updateGroup( oldGroup ); 
                }
            }
        }
        
        if ( rdel != null ) {
                
            for ( Iterator<Integer> ii = rdel.iterator(); 
                  ii.hasNext(); ) {
                
                int drid = ii.next().intValue();
                log.info( "group=" + id + " rid=" + drid);
                
                Group oldGroup = 
                    getUserContext().getGroupDao().getGroup (id );
                Role role = 
                    getUserContext().getRoleDao().getRole ( drid );
                
                log.info( "group=" + oldGroup + " role=" + role );
                
                if ( role != null && oldGroup != null) {
                    
                    Set<Role> roles = oldGroup.getRoles();
                    
                    for ( Iterator<Role> ir = roles.iterator();
                          ir.hasNext(); ) {
                        
                        Role or = ir.next();
                        if ( or.getId() == role.getId() ) {
                            oldGroup.getRoles().remove( or );
                            break;
                        }   
                    }
                    getUserContext().getGroupDao().updateGroup( oldGroup );
                    log.info( "roles=" +oldGroup.getRoles() );
                }                        
            }
        }
        this.group = getUserContext().getGroupDao().getGroup( id );
        log.info( "roles=" +this.group.getRoles() );
        return SUCCESS;
    }
}
