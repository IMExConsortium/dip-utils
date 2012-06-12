package edu.ucla.mbi.orm;

/*==============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *
 * HibernateCSCUtil: HibernateCurrentSessionContextUtil
 *  
 *=========================================================================== */                                                                                              
import org.apache.commons.logging.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class HibernateCSCUtil extends HibernateUtil implements TxAware {  

    private final String
        TRANSACTION_STRATEGY = "hibernate.transaction.factory_class";
    private final String
        JDBC_TX = "org.hibernate.transaction.JDBCTransactionFactory";
    private final String
        CONTEXT_CLASS = "hibernate.current_session_context_class";
    private final String
        CONTEXT_CLASS_VAL = "thread";

    public HibernateCSCUtil() {
        Log log = LogFactory.getLog(HibernateCSCUtil.class);
        log.info( "CurrentSessionContextUtil constructor started..." );

        try {
            configuration = new Configuration();

            //** Read not only hibernate.properties, but also hibernate.cfg.xml
            configuration.configure();

            String transaction_strategy = configuration
                            .getProperty( TRANSACTION_STRATEGY );

            if( transaction_strategy == null
                    || !transaction_strategy.equals( JDBC_TX ) )
            {
                log.info( "transaction_strategy=" + transaction_strategy );
                throw new UnsupportedOperationException (
                            "useCurrentSession warning: " +
                            TRANSACTION_STRATEGY + " have to set to " +
                            JDBC_TX + ".");
            }
        
            String context_class = configuration.getProperty( CONTEXT_CLASS );
               
            if( context_class == null
                    || !context_class.equals(CONTEXT_CLASS_VAL ) )
            {
                    
                throw new UnsupportedOperationException (
                            "useCurrentSession warning: " +
                            CONTEXT_CLASS + " have to set to thread." );
            }

            //** or use static variable handling
            sessionFactory = configuration.buildSessionFactory();

        } catch (Throwable ex) {
            /** 
             * We have to catch Throwable, otherwise we will miss
             * NoClassDefFoundError and other subclasses of Error
             */
            log.error("Building SessionFactory failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }

    public void beginTx() {
    
        Log log = LogFactory.getLog(HibernateCSCUtil.class);
        log.info( "beginTx start..." );

        try{
            getCurrentSession().beginTransaction();
        }catch(HibernateException ex){
            throw ex;
        }
    }    

    public void commitTx() {  

        Log log = LogFactory.getLog(HibernateCSCUtil.class);
        log.info( "commtTx start..." );

        try{
            getCurrentSession().getTransaction().commit();
        }catch(RuntimeException ex){
            rollbackTx();
            throw ex;
        } finally {
            closeSession();
        }
    }

    public void rollbackTx(){

        Log log = LogFactory.getLog(HibernateCSCUtil.class);
        log.info( "rollbackTx start..." );

        if( getCurrentSession().getTransaction().isActive() ) {
            log.info( "rollbackTx: transaction is active. ");
            try {
                getCurrentSession().getTransaction().rollback();
            } catch ( RuntimeException ex ) {
                log.info( "rollbackTx: got runtimeException ex=" + ex.toString() );
                throw ex;
            } finally {
                closeSession();
            }
        } else {
            log.info( "rollbackTx: transaction is not active. ");
        }
    }

    private void closeSession() {

        if( getCurrentSession() != null && getCurrentSession().isOpen()){
            getCurrentSession().close();
        }
    }

}                                       
