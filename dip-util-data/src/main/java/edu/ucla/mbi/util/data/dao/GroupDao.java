package edu.ucla.mbi.util.data.dao;

/* =========================================================================
 # $HeadURL:: https://lukasz@imex.mbi.ucla.edu/svn/dip-ws/dip-util/trunk/d#$
 # $Id:: GroupDao.java 497 2009-08-29 22:21:45Z lukasz                     $
 # Version: $Rev:: 497                                                     $
 #==========================================================================
 #
 # GroupDao 
 #     
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.data.*;

public interface GroupDao {

    public Group getGroup( int id );
    public Group getGroup( String label );
    public List<Group> getGroupList();
    
    public long getUserCount( Group group );
    public List<User> getUserList( Group group );
    
    public void saveGroup( Group group );
    public void updateGroup( Group group );
    public void deleteGroup( Group group );
    
}
