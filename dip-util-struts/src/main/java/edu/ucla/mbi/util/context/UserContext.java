package edu.ucla.mbi.util.context;

/* ========================================================================
 # $Id::                                                                  $
 # Version: $Rev::                                                        $
 #=========================================================================
 #                                                                        $
 # JsonContext: JSON-based configuration                                  $
 #                                                                        $
 #     TO DO:                                                             $
 #                                                                        $
 #======================================================================= */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;
import org.json.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;

public class UserContext extends JsonContext {

    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao( UserDao dao ) {
        userDao = dao;
    }

    //---------------------------------------------------------------------        

    private GroupDao groupDao;

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao( GroupDao dao ) {
        groupDao = dao;
    }
        
    //---------------------------------------------------------------------        
    
    private RoleDao roleDao;

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao( RoleDao dao ) {
        roleDao = dao;
    }
        
    //---------------------------------------------------------------------        
    
    public void initialize() { 

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "UserContext: initializing" );
        
        FileResource fr = (FileResource) getConfig().get("json-source");
        if ( fr == null ) return;

        try {
            readJsonConfigDef( fr.getInputStream() );
        } catch ( Exception e ){
            log.info( "UserContext: json-source error" );
            return;
        }

        //-----------------------------------------------------------------        
        // initialize roles
        //-----------------

        if ( roleDao != null ) {
            try {
                if( getJsonConfigObject().getJSONArray("role") != null ) {
                    
                    // "role":[{"name":"user",
                    //             "comments":"default user role"}]    
                    
                    JSONArray roleArray = 
                        getJsonConfigObject().getJSONArray( "role" );
            
                    for ( int i = 0; i < roleArray.length(); i++ ) {
                        JSONObject role = roleArray.getJSONObject( i );
                        if ( role != null ) {
                            String name = role.getString( "name" );
                            String comments = role.getString( "comments" );

                            log.info( "role: name=" + name +
                                      " comments=" + comments );
                            
                            Role oldRole = roleDao.getRole( name );
                            if ( oldRole == null ) {
                                Role newRole = new Role();
                                newRole.setName( name );
                                newRole.setComments( comments );
                                roleDao.saveRole( newRole );
                            }
                        }
                    }

                }
            } catch ( Exception e ){
                e.printStackTrace();
                log.info( "UserContext: json-source error" );
                return;
            }   
        }

        //-----------------------------------------------------------------        
        // initialize groups
        //------------------

        if ( groupDao != null ) {
            try {
                if( getJsonConfigObject()
                    .getJSONArray("group") != null ) {
                    
                    //"group":[{label:"USER",name:"users",roles:["user"],
                    //          comments:"user group"}]

                    log.info( "UserContext: initializing groups" );
                    
                    JSONArray groupArray = 
                        getJsonConfigObject().getJSONArray( "group" );
                    
                    for ( int i = 0; i < groupArray.length(); i++ ) {
                        JSONObject group = groupArray.getJSONObject( i );
                        if ( group != null ) {

                            String label = group.getString( "label" );
                            String name = group.getString( "name" );
                            String comments = 
                                group.getString( "comments" );
                            
                            Group oldGroup = groupDao.getGroup( label );
                            if ( oldGroup == null ) {
                                
                                Group newGroup  = new Group();

                                newGroup.setLabel( label );
                                newGroup.setName( name );
                                newGroup.setComments( comments );
                                

                                log.info( "label=" + label + 
                                          " name=" + name);
                                JSONArray roleArray = 
                                    group.getJSONArray( "roles" );
                                if ( roleArray != null && 
                                     roleDao != null ) {
                                    
                                    for ( int r = 0; 
                                          r < roleArray.length(); 
                                          r++ ) {
                                        
                                        String role  = 
                                            roleArray.getString( r );
                                        
                                        
                                        if ( roleDao
                                             .getRole( role ) != null ) {
                                            newGroup.getRoles().
                                                add( roleDao
                                                     .getRole( role ) );
                                        }
                                    }
                                }
                                groupDao.saveGroup( newGroup );
                            }
                        }
                    }

                }
            } catch ( Exception e ){
                e.printStackTrace();
                log.info( "UserContext: json-source error" );
                return;
            }

        }

        //-----------------------------------------------------------------        
        // initialize users
        //-----------------
        
        if ( userDao != null ) {
            try {
                if( getJsonConfigObject()
                    .getJSONArray("user") != null ) {
                    
                    //"user":[{login:"ADMIN",
                    //         roles:["user"], groups:["users"]}]

                    log.info( "UserContext: initializing users" );
                    
                    JSONArray userArray = 
                        getJsonConfigObject().getJSONArray( "user" );
                    
                    for ( int i = 0; i < userArray.length(); i++ ) {
                        JSONObject user = userArray.getJSONObject( i );
                        if ( user != null ) {
                            
                            String login = user.getString( "login" );
                            log.info( "NEW LOGIN =" + login);
                            User oldUser = userDao.getUser( login );
                            if ( oldUser == null ) {
                                
                                User newUser  = new User();
                                newUser.setLogin( login );

                                log.info( "login=" + login );
                             
                                // roles
                                
                                JSONArray roleArray = 
                                    user.getJSONArray( "roles" );
                                if ( roleArray != null && 
                                     roleDao != null ) {
                                    
                                    for ( int r = 0; 
                                          r < roleArray.length(); 
                                          r++ ) {
                                        
                                        String role  = 
                                            roleArray.getString( r );
                                        
                                        
                                        if ( roleDao
                                             .getRole( role ) != null ) {
                                            newUser.getRoles().
                                                add( roleDao
                                                     .getRole( role ) );
                                        }
                                    }
                                }

                                // groups 
                                //-------
                                

                                JSONArray groupArray = 
                                    user.getJSONArray( "groups" );
                                if ( groupArray != null && 
                                     groupDao != null ) {
                                    
                                    for ( int g = 0; 
                                          g < groupArray.length(); 
                                          g++ ) {
                                        
                                        String group  = 
                                            groupArray.getString( g );
                                        
                                        
                                        if ( groupDao
                                             .getGroup( group ) != null ) {

                                            newUser.getGroups().
                                                add( groupDao
                                                     .getGroup( group ) );
                                            log.info( "grp added" );
                                        }
                                    }
                                }                                
                                userDao.saveUser( newUser );
                            }
                        }
                    }

                }
            } catch ( Exception e ){
                e.printStackTrace();
                log.info( "UserContext: json-source error" );
                return;
            }
        }
    }
}
