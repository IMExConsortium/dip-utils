package edu.ucla.mbi.util;

/* ========================================================================
 # $HeadURL::                                                             $
 # $Id::                                                                  $
 # Version: $Rev::                                                        $
 #=========================================================================
 #
 # User      
 #
 #====================================================================== */


import java.util.*;
import org.vps.crypt.Crypt;

public class User implements Comparable {

    private int id = -1;
    private String firstName = "";
    private String lastName = "";
    private String affiliation = "";
    private String title = "";
    private String email = "";

    private String login = "";
    private String password = "";
    private String activationKey = "";
    
    private boolean active = false;
    private boolean enabled = true;
       
    Set<Role> roles;
    Set<Group> groups;

    //---------------------------------------------------------------------

    public User() {
	//roles = new TreeSet<Role>();
    }

    public User ( String login, String pass ){
	this.login = login;
	setPassword( pass );
	//roles = new HashSet<Role>();
    }

    //---------------------------------------------------------------------

    public int getId(){
	return id;
    }

    public void setId( int id ) {
	this.id = id;
    }

    //---------------------------------------------------------------------

    public void setLogin( String login ) {
	this.login = login;
    }
   
    public String getLogin() {
	return login;
    }

    //---------------------------------------------------------------------

    public void setPassword( String pass ) {

	// set entcypted password
	//-----------------------
	
	this.password =  pass;
    }
   
    public String getPassword() {
	return password;
    }

    public void encryptPassword( String pass ) {
	
        // set entcypted password
        //-----------------------

        this.password = Crypt.crypt( "ab", pass );
    }
    
    public boolean testPassword( String pass ) {
	
        if( password == null || password.equals( "" )  ) {
            return true;
        }
        
        if( pass == null || pass.length() == 0 ) {
            return false;
        }
        
	if( password.equals( Crypt.crypt( "ab", pass ) ) ) {
	    return true;
	} 
        
	return false;
    }

    //---------------------------------------------------------------------
    
    public void setFirstName( String name ) {
	this.firstName = name;
    }

    public String getFirstName() {
	return firstName;
    }

    //---------------------------------------------------------------------

    public void setLastName( String name ) {
	this.lastName = name;
    }

    public String getLastName() {
        return lastName;
    }

    //---------------------------------------------------------------------

    public void setTitle( String title ) {
	this.title = title;
    }

    public String getTitle() {
        return title;
    }

    //---------------------------------------------------------------------

    public void setEmail( String email ) {
	this.email = email;
    }

    public String getEmail() {
        return email;
    }

    //---------------------------------------------------------------------

    public void setAffiliation( String affiliation ) {
        this.affiliation = affiliation;
    }

    public String getAffiliation() {
        return affiliation;
    }

    //---------------------------------------------------------------------
    // enable/disable account
    //-----------------------

    public void setEnabled( boolean enabled ) {
	this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    //---------------------------------------------------------------------
    // acount activation
    //------------------

    public void setActivationKey() {

      	String key = "::" + UUID.randomUUID().toString() + "::";
	this.activationKey = key;
    }

    public void setActivationKey( String key ) {
	this.activationKey = key;
    }

    public boolean testActivationKey( String key ) {
	
	if ( key != null && activationKey != null &&
	     key.equals( activationKey ) ) {
	    return true;
	}
	
	return false;
    }

    public String getActivationKey() {
        return this.activationKey;
    }

    public void setActivated( boolean active ) {
	this.active = active;
    }

    public boolean getActivated() {
	return active;
    }

    public boolean isActivated() {
	return active;
    }
    
    //---------------------------------------------------------------------
    // user roles
    //-----------

    public Set<Role> getRoles() {

        if ( roles == null ) {
            roles = new TreeSet<Role>();
        }
	return roles;
    }    

    public void setRoles( Set<Role> roles ) {
	this.roles = roles;
    }


    public Set<String> getRoleNames(){

        Set<String> rnl = new HashSet<String>();

        if( roles != null ){
            for( Iterator<Role> ir = roles.iterator();
                 ir.hasNext(); ) {
                rnl.add( ir.next().getName());
            }
        }

        return rnl;

    }
    
    //---------------------------------------------------------------------

    public Set<Role> getAllRoles() {

        Set<Role> all = new HashSet();

        if( roles != null ){
            all.addAll( roles );
        }
        
        if( groups != null ) {
            for( Iterator<Group> ig = groups.iterator(); 
                 ig.hasNext(); ) {
                Group g = ig.next();

                if ( g.getRoles() != null ) {
                    all.addAll( g.getRoles() );
                }
            }
        }
        return all;
    }

    public Set<String> getAllRoleNames(){

        Set<Role> rl = getAllRoles();
        Set<String> rnl = new HashSet<String>();

        if( rl != null ){
            for( Iterator<Role> ir = rl.iterator();
                 ir.hasNext(); ) {
                rnl.add( ir.next().getName());
            }
        }

        return rnl;
    }

    
    //---------------------------------------------------------------------
    // user groups
    //------------

    public Set<Group> getGroups() {
        
        if ( groups == null ) {
            groups = new TreeSet<Group>();
        }
	return groups;
    }    

    public void setGroups( Set<Group> groups ) {
	this.groups = groups;
    }
    

    public Set<String> getGroupNames(){

        Set<String> gnl = new HashSet<String>();

        if( groups != null ){
            for( Iterator<Group> ig = groups.iterator();
                 ig.hasNext(); ) {
                gnl.add( ig.next().getName() );
            }
        }

        return gnl;
    }
    
    //---------------------------------------------------------------------

    public int compareTo( Object obj ) {
        return id - ((User) obj).getId();
    }


    /*
    public boolean equals( Object obj ) {
        if ( ! obj.getClass().isInstance( User.class ) ) {
            return false;
        } else {
            if( ((User) obj).getId() != id ){
                return false;
            }
        }
        return true;
    }
    */
    
    public boolean equals( Object obj ) {

        if( obj.getClass() != this.getClass() ) {
            return false;
        }

        if( this.getId() != -1  &&
            ((User) obj).getId() == this.getId() ) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        return id;
    }

}
