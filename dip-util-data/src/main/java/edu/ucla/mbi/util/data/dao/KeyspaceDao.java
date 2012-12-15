package edu.ucla.mbi.util.data.dao;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # WorkflowDAO - workflow control persistence
 #
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.data.*;

public interface KeyspaceDao {

    public Keyspace getKeyspace( String name );
    public List<Keyspace> getKeyspaceList();
    public long getKeyspaceCount();

    public void saveKeyspace( Keyspace ksp );
    public void updateKeyspace( Keyspace ksp );
    public void deleteKeyspace( Keyspace  ksp );

    public void nextKey( Key sub );
    public Key nextKey( String keyspace );
    public Key findKey( Long id );
    public List findKeyAll();
    public Key getKey( String keyspace, String accession ); 
    public Key getMaxKey( String keyspace ); 

}
