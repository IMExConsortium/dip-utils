package edu.ucla.mbi.util.cache;

/* =============================================================================
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * CacheClient interface                                                       $
 *                                                                             $
 *     implemented by cache clients, supports generic object store/fetch       $
 *     operations                                                              $
 *                                                                             $
 *=========================================================================== */

import edu.ucla.mbi.fault.Fault;

public interface CacheClient {
    public Object fetch(String id) throws Fault;
    public void store(String id, Object obj)throws Fault;
}

