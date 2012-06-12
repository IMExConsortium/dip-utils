package edu.ucla.mbi.util.data;

/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * DataAclAware - access control interface
 *
 ======================================================================== */

import java.util.*;

public interface DataAclAware {

    public boolean testAcl( Set<String> ownerMatch, 
                            Set<String> adminUserMatch, 
                            Set<String> adminGroupMatch );   
}