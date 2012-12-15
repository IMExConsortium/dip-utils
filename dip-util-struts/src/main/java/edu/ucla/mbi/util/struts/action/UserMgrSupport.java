package edu.ucla.mbi.util.struts.action;
                                                                            
/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * UserMgrSupport action
 *
 ======================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;
import edu.ucla.mbi.util.struts.interceptor.*;

public abstract class UserMgrSupport extends ManagerSupport {
    
    //---------------------------------------------------------------------
    //  User/UserList
    //---------------
    
    private User user = null;

    public void setUser( User user ) {
	this.user = user;    
    }
    
    public User getUser(){
	return this.user;
    }
    
    //---------------------------------------------------------------------
    
    public List<User> getUserList(){
        if ( getUserContext().getUserDao() == null ) return null;
        
        if( firstRecord > 0 &&  blockSize > 0 ) {
            return getUserContext().getUserDao().getUserList( firstRecord, 
                                                              blockSize );
        } 
        return getUserContext().getUserDao().getUserList();
    }


    //---------------------------------------------------------------------
    // GroupAll list
    //--------------
    
    public List<Group> getGroupAll(){

        if ( getUserContext().getGroupDao() != null ) {
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
    // user paging 
    //------------

    int firstRecord = -1;
    
    public int getFirstRecord() {
        return firstRecord;
    }
    
    public void setFirstRecord( int firstRecord ) {
        this.firstRecord = firstRecord;
    }

    //---------------------------------------------------------------------

    
    int blockSize = -1;
    
    public int getBlockSize() {
        return blockSize;
    }
    
    public void setBlockSize( int blockSize ) {
        this.blockSize = blockSize;
    }

    //---------------------------------------------------------------------
    
    public long getTotalRecords() {
        if ( getUserContext().getUserDao() == null ) return 0;
        return getUserContext().getUserDao().getUserCount();
    }
    
    //---------------------------------------------------------------------

    public String execute() throws Exception{
        
        if ( getUserContext().getUserDao() != null && 
             getId() > 0 && user == null ) {
            user = getUserContext().getUserDao().getUser( getId() );
        }
        
        if( getOp() == null ) return SUCCESS;
        
        for ( Iterator<String> i = getOp().keySet().iterator();
              i.hasNext(); ) {
            
            String key = i.next();
            String val = getOp().get(key);
            
            if ( val != null && val.length() > 0 ) {
                if ( key.equalsIgnoreCase( "add" ) ) {
                    return addUser( user );
                }

                //---------------------------------------------------------

                if ( key.equalsIgnoreCase( "del" ) ) {
                    return deleteUser( user );
                }

                //---------------------------------------------------------

                if ( key.equalsIgnoreCase( "ldel" ) ) {

                    if ( getOpp() == null ) return SUCCESS;
                    
                    String udel = getOpp().get( "del" );

                    if ( udel != null ) {
                        List<Integer> uidl =
                            new ArrayList<Integer>();
                        try {
                            udel = udel.replaceAll("\\s","");
                            String[] us = udel.split(",");

                            for( int ii = 0; ii <us.length; ii++ ) {
                                uidl.add( Integer.valueOf( us[ii] ) );
                            }
                        } catch ( Exception ex ) {
                            // should not happen
                        }
                        return deleteUserList( uidl );
                    }
                    return SUCCESS;
                }

                //---------------------------------------------------------
                
                if ( key.equalsIgnoreCase( "pup" ) ) {
                    return updateUserProperties( getId(), user );
                }
                
                //---------------------------------------------------------
                
                if ( key.equalsIgnoreCase( "sup" ) ) {
                    return updateUserStatus( getId(), user );
                }

                //---------------------------------------------------------
                
                if ( key.equalsIgnoreCase( "prs" ) ) {

                    String pass1 = getOpp().get( "pass1" );
                    String pass2 = getOpp().get( "pass2" );
                  
                    return updateUserPassword( getId(), pass1, pass2 ); 
                }
                
                //---------------------------------------------------------

                if ( key.equalsIgnoreCase( "radd" ) ) {  
                    if ( getOpp() == null ) return SUCCESS;
                    String rid = getOpp().get( "radd" );
                    try { 
                        int irid = Integer.parseInt( rid );
                        return updateUserRoles( getId(), irid , null );
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
                        return updateUserRoles( getId(), 0, ridl );
                    } else {
                        user = 
                            getUserContext().getUserDao().getUser( getId() );
                    }
                }
            
                //---------------------------------------------------------

                if ( key.equalsIgnoreCase( "gadd" ) ) {  
                    if ( getOpp() == null ) return SUCCESS;
                    String gid = getOpp().get( "gadd" );
                    try { 
                        int igid = Integer.parseInt( gid );
                        return updateUserGroups( getId(), igid , null );
                    }  catch ( Exception ex) {
                        // ignore
                    }
                }

                //---------------------------------------------------------                
                
                if ( key.equalsIgnoreCase( "gdel" ) ) {
                    if ( getOpp() == null ) return SUCCESS;
                    
                    String gdel = getOpp().get( "gdel" );
                    
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
                        return updateUserGroups( getId(), 0, gidl );
                    } else {
                        user = 
                            getUserContext().getUserDao().getUser( getId() );
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
        log.info( "UserMgrSupport: validate" );
        
        boolean loadUserFlag = false;
        
        if( getOp() != null ) {
            for ( Iterator<String> i = getOp().keySet().iterator();
                  i.hasNext(); ) {

                String key = i.next();
                String val = getOp().get(key);

                if ( val != null && val.length() > 0 ) {
                  
                    //-----------------------------------------------------
                    
                    if ( key.equalsIgnoreCase( "add" ) ) {
                                            
                        // add user validation
                        //--------------------
                        
                        // login: unique
                        //---------------
                        
                        String newLogin = 
                            sanitizeString( user.getLogin() );
                        
                        if ( newLogin != null ) {

                            // test if unique
                            //---------------
                            
                            if ( getUserContext().getUserDao() != null &&
                                 getUserContext().getUserDao().getUser( newLogin ) != null ) {
                                
                                newLogin = null;
                                user.setLogin( newLogin );
                            }
                        }                                
                        
                        if ( newLogin != null ) {
                            user.setLogin( newLogin );
                        } else {
                            addFieldError( "user.login", 
                                           "User Login must be unique." );
                        }


                        // first name: non-empty
                        //-----------------------

                        String newFirstName = 
                            sanitizeString( user.getFirstName() );
                        
                        if ( newFirstName != null ) {
                            user.setFirstName( newFirstName );
                        } else {
                            addFieldError( "user.firstName", 
                                           "First name field" +
                                           " cannot be empty." );
                        }   

                        // last name: non-empty
                        //---------------------

                        String newLastName = 
                            sanitizeString( user.getLastName() );
                        
                        if ( newLastName != null ) {
                            user.setLastName( newLastName );
                        } else {
                            addFieldError( "user.lastName", 
                                           "Last name field" +
                                           " cannot be empty." );
                        }   
                        
                        // email: non-empty
                        //-----------------

                        String newEmail = 
                            sanitizeString( user.getEmail() );
                        
                        if ( newEmail != null ) {
                            user.setEmail( newEmail );
                        } else {
                            addFieldError( "user.email", 
                                           "E-mail field" +
                                           " cannot be empty." );
                        }
                        break;
                    }
                    
                    //-----------------------------------------------------

                    if ( key.equalsIgnoreCase( "del" ) ) {
                        // user drop validation: NONE ?                        
                        break;
                    }

                    //-----------------------------------------------------

                    if ( key.equalsIgnoreCase( "ldel" ) ) {
                        // user list drop validation: NONE ?                        
                        break;
                    }

                    //-----------------------------------------------------

                    if ( key.equalsIgnoreCase( "pup" ) ) {

                        if ( user == null || 
                             getUserContext().getUserDao() == null )  return;
                        
                        // user property update validation                       
                        //---------------------------------
                       
                        // first name: non-empty
                        //-----------------------

                        String newFirstName = 
                            sanitizeString( user.getFirstName() );
                        
                        if ( newFirstName != null ) {
                            user.setFirstName( newFirstName );
                        } else {
                            addFieldError( "user.firstName", 
                                           "First name field" +
                                           " cannot be empty." );
                        }   

                        // last name: non-empty
                        //---------------------

                        String newLastName = 
                            sanitizeString( user.getLastName() );
                        
                        if ( newLastName != null ) {
                            user.setLastName( newLastName );
                        } else {
                            addFieldError( "user.lastName", 
                                           "Last name field" +
                                           " cannot be empty." );
                        }   
                        
                        // email: non-empty
                        //-----------------

                        String newEmail = 
                            sanitizeString( user.getEmail() );
                        
                        if ( newEmail != null ) {
                            user.setEmail( newEmail );
                        } else {
                            addFieldError( "user.email", 
                                           "E-mail field" +
                                           " cannot be empty." );
                        }
                        
                        break;
                    }
                    
                    //-----------------------------------------------------

                    if ( key.equalsIgnoreCase( "sup" ) ) {
                        // user status update validation                       
                        break;
                    }

                    //-----------------------------------------------------

                    if ( key.equalsIgnoreCase( "prs" ) ) {
                        // user password reset validation                       
                        break;
                    }
                    
                    //-----------------------------------------------------

                    if ( key.equalsIgnoreCase( "radd" ) ) {
                        // user role add validation: NONE
                        break;
                    }
                    
                    //-----------------------------------------------------
                    
                    if ( key.equalsIgnoreCase( "rdel" ) ) {
                        // user role drop validation: NONE
                        break;
                    }

                    //-----------------------------------------------------

                    if ( key.equalsIgnoreCase( "gadd" ) ) {
                        // user group add validation: NONE
                        break;
                    }
                    
                    //-----------------------------------------------------
                    
                    if ( key.equalsIgnoreCase( "gdel" ) ) {
                        // user group drop validation: NONE                                              
                        break;
                    }
                }
            }
        }
        
        if ( loadUserFlag && getId() > 0 ) {
            user = getUserContext().getUserDao().getUser( getId() );
            setBig( false );
        }
    }
    
    
    //---------------------------------------------------------------------
    // operations
    //-----------

    public String addUser( User user ) {

        if( getUserContext().getUserDao() == null || 
            user == null ) return SUCCESS;

        getUserContext().getUserDao().saveUser( user );
        Log log = LogFactory.getLog( this.getClass() );
        log.info( " new group -> id=" + user.getId() +
                  " login=" + user.getLogin() );

        this.user = null;
        return SUCCESS;
    }


    //---------------------------------------------------------------------

    public String deleteUser( User user ) {

        if( getUserContext().getUserDao() == null || 
            user == null ) return SUCCESS;
        
        User oldUser = getUserContext().getUserDao().getUser( user.getId() );
        if ( oldUser == null ) return SUCCESS;
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( " delete user -> id=" + oldUser.getId() );
        getUserContext().getUserDao().deleteUser( oldUser );        

        this.user = null;
        setId( 0 );
        return SUCCESS;
    }

    //---------------------------------------------------------------------

    private String deleteUserList( List<Integer> users ) {
        
        if( getUserContext().getUserDao() == null || 
            users == null ) return SUCCESS;

        Log log = LogFactory.getLog( this.getClass() );
        
        for ( Iterator<Integer> ii = users.iterator();
              ii.hasNext(); ) {
            
            int gid = ii.next();
            User u = getUserContext().getUserDao().getUser( gid );
                                     
            log.info( " delete group -> id=" + u.getId() );
            getUserContext().getUserDao().deleteUser( u );                
        }
        return SUCCESS;
    }

    //---------------------------------------------------------------------
    
    public String updateUserProperties( int id, User user ) {

        if( getUserContext().getUserDao() == null ) return SUCCESS;
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "id=" + id );

        User oldUser = getUserContext().getUserDao().getUser( id );
        if ( oldUser == null ) return SUCCESS;
        
        // ACL control
        //------------
        //protected Set<String> ownerMatch;
        //protected Set<String> groupMatch;

        // - test user


        if ( ownerMatch != null ) {
            if ( ! ownerMatch.contains( oldUser.getLogin() ) ) {
            
                // - test group
            
                if ( oldUser.getGroups() == null ) return SUCCESS;
                
                boolean groupOk = false;
                
                for ( Iterator ig = oldUser.getGroups().iterator();
                      ig.hasNext(); ) {
                    Group g = (Group) ig.next();
                    
                    if ( ownerMatch.contains(  g.getName() ) ) {
                        groupOk = true;
                    }
                }
                if ( ! groupOk ) return SUCCESS;
            }
        }
        
        oldUser.setFirstName( user.getFirstName() );
        oldUser.setLastName( user.getLastName() );
        oldUser.setAffiliation( user.getAffiliation() );
        oldUser.setEmail( user.getEmail() );
        
        oldUser.setActivated( user.getActivated() );
        oldUser.setEnabled( user.getEnabled() );
        
        getUserContext().getUserDao().updateUser( oldUser );
        this.user = getUserContext().getUserDao().getUser( id );

        log.info( " updated user(props) -> id=" + id );
        return SUCCESS;
    }


    //---------------------------------------------------------------------
    
    public String updateUserStatus( int id, User user ) {
        
        if( getUserContext().getUserDao() == null ) return SUCCESS;
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "id=" + id );

        User oldUser = getUserContext().getUserDao().getUser( id );
        if ( oldUser == null ) return SUCCESS;

        oldUser.setActivated( user.getActivated() );
        oldUser.setEnabled( user.getEnabled() );
        
        getUserContext().getUserDao().updateUser( oldUser );
        this.user = getUserContext().getUserDao().getUser( id );

        log.info( " updated user(status) -> id=" + id );
        return SUCCESS;
    }
    
    //---------------------------------------------------------------------
    
    private String updateUserRoles( int id, int rid, 
                                    List<Integer> rdel ) {
        
        if ( getUserContext().getUserDao() == null || 
             getUserContext().getRoleDao() == null ) return SUCCESS;
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "id=" + id + " radd=" + rid );
        
        if ( rid > 0  ) {
                
            Role role = getUserContext().getRoleDao().getRole( rid );
            User oldUser = getUserContext().getUserDao().getUser( id );
            
            log.info( "user=" + oldUser + " role=" + role );
            
            if ( role != null && oldUser != null) {
                
                if ( ! oldUser.getRoles().contains( role ) ) {
                    oldUser.getRoles().add( role );
                    getUserContext().getUserDao().updateUser( oldUser ); 
                }
            }
        }
        
        if ( rdel != null ) {
                
            for ( Iterator<Integer> ii = rdel.iterator(); 
                  ii.hasNext(); ) {
                
                int drid = ii.next().intValue();
                log.info( "group=" + id + " rid=" + drid);
                
                User oldUser = getUserContext().getUserDao().getUser(id );
                Role role = getUserContext().getRoleDao().getRole( drid );
                
                log.info( "user=" + oldUser + " role=" + role );
                
                if ( role != null && oldUser != null) {
                    
                    Set<Role> roles = oldUser.getRoles();
                    
                    for ( Iterator<Role> ir = roles.iterator();
                          ir.hasNext(); ) {
                        
                        Role or = ir.next();
                        if ( or.getId() == role.getId() ) {
                            oldUser.getRoles().remove( or );
                            break;
                        }   
                    }
                    getUserContext().getUserDao().updateUser( oldUser );
                    log.info( "roles=" +oldUser.getRoles() );
                }                        
            }
        }
        this.user = getUserContext().getUserDao().getUser( id );
        log.info( "roles=" +this.user.getRoles() );
        return SUCCESS;
    }
    
    //---------------------------------------------------------------------
    
    private String updateUserGroups( int id, int gid, 
                                     List<Integer> gdel ) {
        
        if ( getUserContext().getUserDao() == null || 
             getUserContext().getGroupDao() == null ) return SUCCESS;
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "id=" + id + " gadd=" + gid );
        
        if ( gid > 0  ) {
                
            Group group = getUserContext().getGroupDao().getGroup( gid );
            User oldUser = getUserContext().getUserDao().getUser( id );
            
            log.info( "user=" + oldUser + " group=" + group );
            
            if ( group != null && oldUser != null ) {
                if ( ! oldUser.getGroups().contains( group ) ) {
                    oldUser.getGroups().add( group );
                    getUserContext().getUserDao().updateUser( oldUser ); 
                }
            }
        }
        
        if ( gdel != null ) {
            
            for ( Iterator<Integer> ii = gdel.iterator(); 
                  ii.hasNext(); ) {
                
                int dgid = ii.next().intValue();
                log.info( "user=" + id + " gid=" + dgid);
                
                User oldUser = getUserContext().getUserDao().getUser( id );
                Group group = getUserContext().getGroupDao().getGroup( dgid );
                
                log.info( "user=" + oldUser + " group=" + group );
                
                if ( group != null && oldUser != null) {
                    Set<Group> groups = oldUser.getGroups();
                    
                    for ( Iterator<Group> ig = groups.iterator();
                          ig.hasNext(); ) {
                        
                        Group og = ig.next();
                        if ( og.getId() == group.getId() ) {
                            oldUser.getGroups().remove( og );
                            break;
                        }   
                    }
                    getUserContext().getUserDao().updateUser( oldUser );
                    log.info( "groups=" +oldUser.getGroups() );
                }                        
            }
        }
        
        this.user = getUserContext().getUserDao().getUser( id );
        log.info( "groups=" +this.user.getGroups() );
        return SUCCESS;
    }

    
    //---------------------------------------------------------------------

    public String updateUserPassword( int id, 
                                      String pass1, String pass2 ) {
        
        if( getUserContext().getUserDao() == null ) return SUCCESS;

        Log log = LogFactory.getLog( this.getClass() );
        log.info( " id(pass)=" + id );
        
        User oldUser = getUserContext().getUserDao().getUser( id );
        if ( oldUser == null ) return SUCCESS;
        

        // ACL control
        //------------
        //protected Set<String> ownerMatch;
        //protected Set<String> groupMatch;
        
        // - test user
        
        if ( ownerMatch != null ) {

            if ( ! ownerMatch.contains( oldUser.getLogin() ) ) {
            
                // - test group
            
                if ( oldUser.getGroups() == null ) return SUCCESS;
            
                boolean groupOk = false;
            
                for ( Iterator ig = oldUser.getGroups().iterator();
                      ig.hasNext(); ) {
                    Group g = (Group) ig.next();
                    
                    if ( ownerMatch.contains(  g.getName() ) ) {
                        groupOk = true;
                    }
                }                     
                if ( ! groupOk ) return SUCCESS;
            }
        }
        
        if ( sanitizeString( pass1 ) != null && 
             sanitizeString( pass2 ) != null && 
             sanitizeString( pass1 ).equals( sanitizeString( pass2 ) ) ) {
            
            // set password
            //-------------
            
            oldUser.encryptPassword( sanitizeString( pass1 ) );
            getUserContext().getUserDao().updateUser( oldUser );
            this.user = getUserContext().getUserDao().getUser( id );
        }
        
        return SUCCESS;
    }
    
}
