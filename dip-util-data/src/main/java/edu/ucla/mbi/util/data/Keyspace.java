package edu.ucla.mbi.util.data;

/* =============================================================================
 # $HeadURL::                                                                  $
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #                                                                             $
 # Keyspace                                                                    $
 #                                                                             $
 #=========================================================================== */

public class Keyspace {
    
    private long id = -1;
    private String name = "";
    private String comments = "";
    private String prefix = "";
    private String postfix = "";
    private boolean active = false;

    // setters
    //--------
    
    private Keyspace setId( long id ) {
	this.id = id;
	return this;
    }

    public Keyspace setName( String name ) {
	this.name = name;
	return this;
    }

    public Keyspace setComments( String comments ) {
	this.comments = comments;
	return this;
    }

    public Keyspace setPrefix( String name ) {
	this.prefix = name;
	return this;
    }

    public Keyspace setPostfix( String name ) {
	this.postfix = name;
	return this;
    }
    
    public Keyspace setActive( boolean active ) {
	this.active = active;
	return this;
    }

    public Keyspace setActive() {
	return this.setActive( true );
    }

    public Keyspace setInActive() {
	return this.setActive( false );
    }
    
    // getters
    //--------

    public long getId() {
       	return this.id;
    }

    public String getName() {
       	return this.name;
    }

    public String getComments() {
       	return this.comments;
    }

    public String getPrefix() {
	return this.prefix;
    }
    
    public String getPostfix() {
       	return this.postfix;
    }

    public boolean isActive() {
       	return this.active;
    }

    public boolean getActive() {
       	return this.active;
    }

}
