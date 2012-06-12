package edu.ucla.mbi.util.data.dao;

/* =========================================================================
 # $HeadURL:: https://lukasz@imex.mbi.ucla.edu/svn/dip-ws/trunk/dip-util-s#$
 # $Id:: UserDao.java 429 2009-08-14 01:44:32Z lukasz                      $
 # Version: $Rev:: 429                                                     $
 #==========================================================================
 #
 # PublicationDAO 
 #     
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.User;
import edu.ucla.mbi.util.Group;
import edu.ucla.mbi.util.data.*;

public interface PublicationDAO {

    public Publication getPublication( int id );
    public Publication getPublication( String title );
    public Publication getPublicationByPmid( String pmid );
    public Publication getPublicationByKey( String key );
    
    public List<Publication> getPublicationList();
    public List<Publication> getPublicationList( int firstRecord, 
                                                 int blockSize,
                                                 String sortKey, boolean asc );

    public List<Publication> getPublicationList( int firstRecord, 
                                                 int blockSize,
                                                 String sortKey, boolean asc,
                                                 Map<String,String> flt);

    public List<Publication> getPublicationList( int firstRecord, int blockSize );

    public long getPublicationCount();
    public long getPublicationCount( Map<String,String> flt );

    public List<User> getOwners( String query);
    public List<User> getAdminUsers( String query );
    public List<Group> getAdminGroups( String query ); 
    public List<DataState> getStates( String query );
    
    //public void savePublication( Publication publication );
    //public void updatePublication( Publication publication );
    public Publication savePublication( Publication publication );
    public Publication updatePublication( Publication publication );
    public void deletePublication( Publication publication );
    
}
