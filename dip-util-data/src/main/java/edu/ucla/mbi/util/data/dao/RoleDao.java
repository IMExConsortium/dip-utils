package edu.ucla.mbi.util.data.dao;

/* =========================================================================
 # $HeadURL:: https://lukasz@imex.mbi.ucla.edu/svn/dip-ws/dip-util/trunk/d#$
 # $Id:: RoleDao.java 497 2009-08-29 22:21:45Z lukasz                      $
 # Version: $Rev:: 497                                                     $
 #==========================================================================
 #
 # RoleDao
 #     
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.data.*;

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
