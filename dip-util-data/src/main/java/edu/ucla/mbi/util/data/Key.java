package  edu.ucla.mbi.util.data;

/* =============================================================================
 # $HeadURL::                                                                  $
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #                                                                             $
 # Key                                                                         $
 #                                                                             $
 #=========================================================================== */

import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

public class Key{
    
    private long id = -1;
    private Keyspace kspace = null;
    private long kval = -1;
    private Timestamp created = null;

    //--------------------------------------------------------------------------
    // setters
    //--------
    
    private Key setId( long id ) {
	this.id = id;
	return this;
    }

    public Key setKeyspace( Keyspace kspace ) {
	this.kspace = kspace;
	return this;
    }

    public Key setValue( long value ) {
	this.kval = value;
	return this;
    }
    
    public Key setCreated( Timestamp time ) {
        this.created = time;
	return this;
    }    
    
    //--------------------------------------------------------------------------
    // getters
    //--------

    public long getId() {
       	return this.id;
    }

    public Keyspace getKeyspace() {
       	return this.kspace;
    }
    
    public long getValue() {
       	return this.kval;
    }
    
    public Timestamp getCreated() {
	return this.created;
    }
    
    //--------------------------------------------------------------------------
    // accession
    
    public String getAccession() {
        return kspace.getPrefix() + kval + kspace.getPostfix();
    }
    
}
