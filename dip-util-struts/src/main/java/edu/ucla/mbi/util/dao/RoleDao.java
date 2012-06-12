package edu.ucla.mbi.util.dao;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # RoleDao
 #     
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.*;

public interface RoleDao {

    public Role getRole( int id );
    public Role getRole( String name );
    public List<Role> getRoleList();

    public long getUserCount( Role role );
    public List<User> getUserList( Role role );
    
    public long getGroupCount( Role role );
    public List<Group> getGroupList( Role role );
    
    public void saveRole( Role role );
    public void updateRole( Role role );
    public void deleteRole( Role role );
    
}
