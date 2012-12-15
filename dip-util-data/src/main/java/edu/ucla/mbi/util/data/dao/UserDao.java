package edu.ucla.mbi.util.data.dao;

/* =========================================================================
 # $HeadURL:: https://lukasz@imex.mbi.ucla.edu/svn/dip-ws/dip-util/trunk/d#$
 # $Id:: UserDao.java 497 2009-08-29 22:21:45Z lukasz                      $
 # Version: $Rev:: 497                                                     $
 #==========================================================================
 #
 # UserDao 
 #     
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.data.*;

public interface UserDao {

    public User getUser( int id );
    public User getUser( String login );
    public List<User> getUserList();
    public List<User> getUserList( int firstRecord, int blockSize );
    public long getUserCount();

    public void saveUser( User user );
    public void updateUser( User user );
    public void deleteUser( User user );
    
}
