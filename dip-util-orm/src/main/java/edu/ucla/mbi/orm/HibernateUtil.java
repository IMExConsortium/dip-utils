package edu.ucla.mbi.orm;

/*==============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *
 * HibernateUtil:
 *  - based on Hibernate in Action exampl
 *=========================================================================== */

import org.apache.commons.logging.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

import org.compass.core.*;

public class HibernateUtil implements HibernateOrmUtil {   
     
    protected Configuration configuration;
    protected static SessionFactory sessionFactory;

    public HibernateUtil () {
        Log log = LogFactory.getLog(HibernateUtil.class);
        log.info( " HibernateUtil constructor started..." );

        //** Create the initial SessionFactory from default configuration files
        try {

            configuration = new Configuration();

            //** Read not only hibernate.properties, but also hibernate.cfg.xml
            configuration.configure();
            
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

    public Configuration getConfiguration() {
        return configuration;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session getCurrentSession() {
        return getSessionFactory().openSession();
    }

    public CompassSession getCompassSession() {
        throw new UnsupportedOperationException (
                    "Warning: HibernateUtil class can't support compass query." ); 
    }
 
}
