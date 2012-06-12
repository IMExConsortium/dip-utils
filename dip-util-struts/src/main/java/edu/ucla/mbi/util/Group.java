package edu.ucla.mbi.util;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # Group of users 
 #     
 #======================================================================= */

import java.util.*;

public class Group implements Comparable {

    private int id = -1;

    private String label = "";
    private String name = "";
    private String comments = "";
    
    private User contactUser;
    private User adminUser;

    private Set<Role> roles;    

    //---------------------------------------------------------------------

    public int getId(){
	return id;
    }
    
    public void setId( int id ) {
	this.id = id;
    }
    
    //---------------------------------------------------------------------

    public String getLabel() {
        return label;
    }
    
    public void setLabel( String label ) {
        this.label = label;
    }

    //---------------------------------------------------------------------

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    //---------------------------------------------------------------------
    
    public User getContactUser() {
        return contactUser;
    }
    
    public void setContactUser( User user ) {
        this.contactUser = user;
    }

    //---------------------------------------------------------------------

    public User getAdminUser() {
        return adminUser;
    }
    
    public void setAdminUser( User user ) {
        this.adminUser = user;
    }
    
    //---------------------------------------------------------------------

    public Set<Role> getRoles() {
        if ( roles == null ) {
            roles = new TreeSet();
        }
        return roles;
    }

    public void setRoles( Set<Role> roles ) {
        this.roles = roles;
    }

    //---------------------------------------------------------------------

    public void setComments( String comments ) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }


    //---------------------------------------------------------------------

    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append( " Group(id=" + getId() );
        sb.append( " label=" + getLabel() );
        sb.append( " comments=" + getComments() );
        sb.append( ")" );

        return sb.toString();
    }


    //---------------------------------------------------------------------
    // Comparable interface
    //---------------------

    public int compareTo( Object obj ) {
        return id - ((Group) obj).getId();
    }

    /*

    public boolean equals( Object obj ) {
        if ( ! obj.getClass().isInstance( Group.class ) ) {
            return false;
        } else {
            if( ((Group) obj).getId() != id ){
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
            ((Group) obj).getId() == this.getId() ) {
            return true;
        } 
        return false;
    }

    public int hashCode() {
        return id;
    }

}
