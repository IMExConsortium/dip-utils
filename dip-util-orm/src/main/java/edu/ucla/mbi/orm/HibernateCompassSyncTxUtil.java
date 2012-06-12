package edu.ucla.mbi.orm;

/*==============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *
 * HibernateCompassUtil:
 *  - based on Hibernate in Action exampl
 *  - compass integration of non-jndi based sessionfactories  
 *=========================================================================== */

import org.apache.commons.logging.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

import javax.naming.*;

import org.compass.core.*;
import org.compass.core.spi.InternalCompassSession;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.CompassConfigurationFactory;

import org.compass.gps.*;
import org.compass.gps.impl.*;
import org.compass.gps.device.*;
import org.compass.gps.device.hibernate.*;

import org.compass.gps.device.hibernate.*;
import org.compass.gps.device.hibernate.dep.*;
import org.compass.gps.device.hibernate.lifecycle.*;
import java.util.*;

public class HibernateCompassSyncTxUtil extends HibernateCSCUtil {   
     
    private HibernateSyncTransactionFactory hsTxFactory;

    private Compass compass = null; 
    private CompassSession compassSession = null;
    private boolean indexHibernateCompass = false;

    public HibernateCompassSyncTxUtil( boolean indexHibernateCompass ) {
        super();
        this.indexHibernateCompass = indexHibernateCompass;
        compassInit(indexHibernateCompass);
    }         


    public void beginTx(){

        Log log = LogFactory.getLog(HibernateCompassSyncTxUtil.class);
        log.info("beginTx: entering ...");
        super.beginTx();

        log.info("beginTx: compass=" + compass );
        if( compass != null && !compass.isClosed()){
            if( compassSession != null ){
                compassSession.close();
            }
        }else{
            compassInit(false);
            log.info("beginTx: after init compass=" + compass );
        }

        
        HibernateSyncTransaction hsTx = new HibernateSyncTransaction(
                        getSessionFactory(), true, hsTxFactory);

        /** 
          *  compass.openSession():
          *  If creating a new session, will try to automatically join an existing
          *  outer transaction.An outer transaction might be an already running 
          *  Compass local transaction, or an external transaciton (JTA or Spring 
          *  for example).In such cases, there is no need to perform any 
          *  transaction managment code (begin or commit/rollback transaction) or 
          *  closing the opened session. 
          **/
        compassSession = compass.openSession();

        log.info("beginTx: compassSession=" + compassSession );
    }


    private void compassInit(boolean indexHibernateCompass){
	
	    Log log = LogFactory.getLog(HibernateCompassSyncTxUtil.class);

	    // initialize compass
	    //-------------------

        if( compass != null && !compass.isClosed()){
            compass.close();
        }

	    CompassConfiguration cconf = CompassConfigurationFactory.newConfiguration();
           
        hsTxFactory = new HibernateSyncTransactionFactory();
        hsTxFactory.setSessionFactory(getSessionFactory());
    
        cconf.configure(); 
        //config.getProperty("compass-cfg","configure/compass.cfg.xml"));
        compass = cconf.buildCompass();
        
        log.info("compassInit: initializing compass.");
        
	    SingleCompassGps gps = new SingleCompassGps(compass);
	    CompassGpsDevice hibernateDevice = new Hibernate3GpsDevice(
                                "hibernate", getSessionFactory());
	    gps.addGpsDevice(hibernateDevice);
        gps.start();

        if(indexHibernateCompass){
            log.warn("compassInit: gps starts to index ... ");
            gps.index();
        }
        
    }

    public CompassSession getCompassSession(){
        Log log = LogFactory.getLog(HibernateCompassSyncTxUtil.class);
        
        if(compass == null || compassSession == null){
            log.warn("getCompassSearchSession: compass is null.");
            throw new IllegalStateException("Compass is not initialized.");            
	    }
        
        return compassSession;
    }
}
