package edu.ucla.mbi.orm;

/*==============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *
 * AbstractDAO:
 *
 *=========================================================================== */

import org.hibernate.*;
import java.util.*;
import java.io.Serializable;
import org.compass.core.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractTxDAO extends AbstractDAO {
    
    protected AbstractTxDAO ( TxAware txAware ) {
        super( txAware );
    }

    public static HashSet<String>  dipRecordXrefSet = new HashSet<String>();
    public static HashMap<String, String> recordMap = new HashMap<String, String>();

    static {
        recordMap.put("Link", "DipLink");
        recordMap.put("Protein", "DipNode");
        recordMap.put("Message", "DipNode");
        recordMap.put("Gene", "DipNode");
        recordMap.put("Article", "DipSource");
        recordMap.put("Evidence", "DipEvidence");
        recordMap.put("Inference", "DipInference");
        recordMap.put("LNode", "LNode");
        recordMap.put("ENode", "ENode");

        dipRecordXrefSet.add( "DipLinkXref" );
        dipRecordXrefSet.add( "DipEvidenceXref" );
        dipRecordXrefSet.add( "DipInferenceXref" );
        dipRecordXrefSet.add( "DipSourceXref" );
        dipRecordXrefSet.add( "DipNodeXref" );
    }

    protected void save( Object obj ) throws DAOException {

        Session session = hibernateOrmUtil.getCurrentSession();
        try {
            session.save( obj );
            session.flush();
        } catch ( HibernateException e ) {
            handleTxException( e );
        }
    }
 
    protected void update( Object obj ) throws DAOException {

        Session session = hibernateOrmUtil.getCurrentSession();

        try {
            session.update( obj );
            session.flush();
        } catch ( HibernateException e ) {
            handleTxException( e );
        }
    }

    protected void saveOrUpdate( Object obj ) throws DAOException {

        Session session = hibernateOrmUtil.getCurrentSession();

        try {
            session.saveOrUpdate( obj );
            session.flush();
        } catch ( HibernateException e ) {
            handleTxException( e );
        }
    }
    
    protected void delete ( Object obj ) throws DAOException {

        Session session = hibernateOrmUtil.getCurrentSession();

        try {
            session.delete( obj );
            session.flush();
        } catch ( HibernateException e ) {
            handleTxException( e );
        }
    }
   
    public Object get ( Class clazz, int id ) throws DAOException {

        Object obj = null;
        Session session = hibernateOrmUtil.getCurrentSession();

        try {
            obj = session.get( clazz, id );
        }catch ( HibernateException e ) {
            handleException( e );
        }

        return obj;
    }

    protected List query ( Object object, String queriedClassName, 
                           String propertyName ) throws DAOException { 

        List obj = null;
        Session session = hibernateOrmUtil.getCurrentSession();

        try{
            Query query = session.createQuery(
                                        "from " + queriedClassName + " a where a." + 
                                        propertyName + " = :" + propertyName );
            query.setParameter( propertyName, object );
            obj = query.list();
        } catch (HibernateException e) {
            handleException(e);
        }
        return obj;
    }
 
    protected Object find ( Class clazz, Serializable id ) throws DAOException {

        Object obj = null;
        Session session = hibernateOrmUtil.getCurrentSession();

        try {
            obj = session.load( clazz, id );
        } catch ( HibernateException e ) {
            handleException( e );
        }
        return obj;
    }

    protected Object find ( Class clazz, int id ) throws DAOException {

        Object obj = null;
        Session session = hibernateOrmUtil.getCurrentSession();

        try {
            obj = session.load( clazz, id );
        } catch ( HibernateException e ) {
            handleException( e );
        }

        return obj;
    }
 
    protected Object find ( Class clazz, long id ) throws DAOException {

        Object obj = null;
        Session session = hibernateOrmUtil.getCurrentSession();

        try {
            obj = session.load( clazz, id );
        } catch ( HibernateException e ) {
            handleException(e);
        }
        return obj;
    }

    protected Object find ( Class clazz, String id ) throws DAOException {

        Object obj = null;
        Session session = hibernateOrmUtil.getCurrentSession();

        try {
            obj = session.load(clazz, id);
        } catch (HibernateException e) {
            handleException(e);
        }
        return obj;
    }
 
    protected List findAll ( Class clazz ) throws DAOException {

        List objects = null;
        Session session = hibernateOrmUtil.getCurrentSession();

        try {
            Query query = session.createQuery( "from " + clazz.getName());
            objects = query.list();
        } catch ( HibernateException e ) {
            handleException( e );
        }

        return objects;
    }

    protected List findAll ( Class clazz, String orderByPropertyName ) throws DAOException {

        List objects = null;
        Session session = hibernateOrmUtil.getCurrentSession();

        try {
            Query query = session.createQuery( "from " + clazz.getName() +
                                               " order by " + orderByPropertyName );
            objects = query.list();
        } catch ( HibernateException e ) {
            handleException( e );
        }
        return objects;
    }

    protected void handleTxException( HibernateException e ) throws DAOException {
        TxAware aware = ( TxAware) hibernateOrmUtil;
        aware.rollbackTx();
        super.handleException( e );
    }

    protected Resource[] compassQueryResource( String query )
                                               throws DAOException {

        Log log = LogFactory.getLog( AbstractTxDAO.class );
        CompassSession compassSession = null;

        
        try {
            log.info( "compassQueryResource: before getCompassSession()." );
            compassSession = hibernateOrmUtil.getCompassSession();
        } catch ( Exception e ) {
            log.info( "compassQueryResource: getCompassSession excepton: " + e.toString() );
            throw new DAOException ( e ); 
        }
        
        log.info( "compassQueryResource: after getCompassSession=" + compassSession );

        /* that used to remove duplicate hit */
        Set<Resource> resourceSet = new HashSet();
            
        try {
            CompassHits hits = compassSession.find(query);

            if( hits.length() >= 1 ) {
                for ( int i = 0; i < hits.length(); i++ ) {
                    resourceSet.add( hits.resource( i ) );
                }
                return resourceSet.toArray( new Resource[resourceSet.size()] );
            }
        } catch( CompassException e ) {
            e.printStackTrace();
            throw new DAOException( "Compass Exception: " + e.toString() );
        }

        return null;
        
    }     
}
