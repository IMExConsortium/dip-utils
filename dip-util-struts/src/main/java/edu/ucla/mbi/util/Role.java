package edu.ucla.mbi.util;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # Role
 #     
 #
 #======================================================================= */

import java.util.*;

public class Role implements Comparable {
    
    private String name; // = "USER";
    private String comments;
    private int id;

    // preset roles
    //-------------

    public static final Role USER = new Role("user");
    public static final Role ADMIN = new Role("administrator");
    public static final Role EDIT = new Role("editor");

    public Role() {}

    public Role( String name ) {
	this.name = name;
    }

    //---------------------------------------------------------------------

    public void setId( int id ) {
	this.id = id;
    } 

    public int getId() {
	return id;
    }
    
    //---------------------------------------------------------------------

    public void setName( String name ) {
	this.name = name;
    } 

    public String getName() {
	return name;
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

        sb.append( " Role(id=" + getId() );
        sb.append( " name=" + getName() );
        sb.append( " comments=" + getComments() );
        sb.append( ")" );

        return sb.toString();
    }


    //---------------------------------------------------------------------
    // Comparable interface
    //---------------------

    public int compareTo( Object obj ) {        
        return id - ((Role) obj).getId();
    }
    
    /*
    public boolean equals( Object obj ) {
        if ( ! obj.getClass().isInstance( Role.class ) ) {
            return false;
        } else {
            if( ((Role) obj).getId() != id ){
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
            ((Role) obj).getId() == this.getId() ) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        return id;
    }
    
}
