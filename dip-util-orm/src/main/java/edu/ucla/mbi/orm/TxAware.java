package edu.ucla.mbi.orm;

/*==============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *
 * TxAware interface: 
 *
 *=========================================================================== */

public interface TxAware extends HibernateOrmUtil {
    void beginTx();
    void commitTx();
    void rollbackTx();
}
