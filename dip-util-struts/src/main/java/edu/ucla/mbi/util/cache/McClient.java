package edu.ucla.mbi.util.cache;

/* =============================================================================
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * McClient: memcached-based CacheClient implementation                        $
 *                                                                             $
 *                                                                             $
 *=========================================================================== */

import net.spy.memcached.spring.MemcachedClientFactoryBean;
import net.spy.memcached.MemcachedClient;

public class McClient implements CacheClient {

    // configuration properties
    //-------------------------

    private MemcachedClient mcf = null;    
    private int ttl = 0;
    private String prefix;

    //*** setter
    public void setMcf( MemcachedClient mcf ) {
        this.mcf = mcf;
    }
    
    public void setTtl( int timeToLive ) {
        this.ttl = timeToLive;
    }

    public void setPrefix( String prefix ) {
        this.prefix = prefix;
    }

    // CacheClient implementation
    //---------------------------

    public Object fetch( String id ) throws Exception {
        return mcf.get( prefix + id );
    }
    
    public void store( String id, Object obj ) throws Exception {
        mcf.set( prefix + id, ttl, obj );
    }

    public void shutdown() {
        mcf.shutdown();
    }    
}

