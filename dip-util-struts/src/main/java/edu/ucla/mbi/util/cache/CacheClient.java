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

public interface CacheClient {
    public Object fetch(String id) throws Exception;
    public void store(String id, Object obj)throws Exception;
}

