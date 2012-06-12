package edu.ucla.mbi.orm;

/*==============================================================================
 * $HeadURL:: https://wyu@imex.mbi.ucla.edu/svn/dip-ws/trunk/dip-util-orm-temp/$
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *
 * HibernateTLUtil: HibernateThreadLocalUtil
 *  
 *=========================================================================== */                                                                                              
import org.hibernate.*;
import org.hibernate.cfg.*;

import org.apache.commons.logging.*;

public class HibernateTLUtil extends HibernateUtil implements TxAware {  

    private final String
        TRANSACTION_STRATEGY = "hibernate.transaction.factory_class";
    private final String
        JDBC_TX = "org.hibernate.transaction.JDBCTransactionFactory";

    private ThreadLocal threadSession = new ThreadLocal();
    private ThreadLocal threadTransaction = new ThreadLocal();

    public HibernateTLUtil () {
        Log log = LogFactory.getLog(HibernateTLUtil.class);
        log.info( " HibernateTLUtil constructor started..." );

        //** Create the initial SessionFactory from default configuration files
        try {
            configuration = new Configuration();

            //** Read not only hibernate.properties, but also hibernate.cfg.xml
            configuration.configure();

            String transaction_strategy = configuration
                            .getProperty( TRANSACTION_STRATEGY );

            if( transaction_strategy == null
                        || !transaction_strategy.equals( JDBC_TX ) )
            {
                throw new UnsupportedOperationException (
                                "useThreadLocal warning: " +
                                TRANSACTION_STRATEGY + " have to set to " +
                                JDBC_TX + ".");
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
        Session s = (Session) threadSession.get();
        if (s == null) {
            Log log = LogFactory.getLog(HibernateUtil.class);
            log.debug("Opening new Session for this thread.");
            s = getSessionFactory().openSession();
            threadSession.set(s);
        }
        return s;
    }

    public void beginTx(){
        
        Transaction tx = (Transaction) threadTransaction.get();
     
        try {
            if (tx == null) {
                Log log = LogFactory.getLog(HibernateTLUtil.class);
                log.debug("Starting new database transaction in this thread.");
                tx = getCurrentSession().beginTransaction();
                threadTransaction.set(tx);
            } 
        } catch ( HibernateException ex) {
            throw ex;
        }
    }

    public void commitTx(){
        Transaction tx = (Transaction) threadTransaction.get();

        try {
            if ((tx != null) && !tx.wasCommitted() && !tx.wasRolledBack()) {
                Log log = LogFactory.getLog(HibernateUtil.class);
                log.debug("Committing database transaction of this thread.");
                tx.commit();
            } 

            threadTransaction.set(null);
        } catch (RuntimeException ex) {
            Log log = LogFactory.getLog(HibernateUtil.class);
            log.error(ex);
            rollbackTx();
            throw ex;
        }
    }

    public void rollbackTx(){

        Transaction tx = (Transaction) threadTransaction.get();

        try {
            threadTransaction.set(null);

            if ((tx != null) && !tx.wasCommitted() && !tx.wasRolledBack()) {
                Log log = LogFactory.getLog(HibernateUtil.class);
                log.debug(
                    "Tyring to rollback database transaction of this thread.");
                tx.rollback();
                log.debug("Database transaction rolled back.");
            }
        } catch ( RuntimeException ex ) {
            throw ex ;
        } finally {
            closeSession();
        }
    }

    private void closeSession() {
        
        Session s = (Session) threadSession.get();
        threadSession.set(null);

        if ((s != null) && s.isOpen()) {
            Log log = LogFactory.getLog(HibernateUtil.class);
            log.info("useThreadLocal: Closing Session of this thread.");
            s.close();
        }
    }
}                                       
