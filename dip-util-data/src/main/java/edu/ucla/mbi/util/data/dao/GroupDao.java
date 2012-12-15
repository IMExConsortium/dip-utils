package edu.ucla.mbi.util.data.dao;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
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
