package edu.ucla.mbi.util.data.dao;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
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
