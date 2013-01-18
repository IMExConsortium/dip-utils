package edu.ucla.mbi.fault;

/*===========================================================================
 * $HeadURL::                                                               $
 * $Id::                                                                    $
 * Version: $Rev::                                                          $
 *===========================================================================
 *
 * FaultDef:
 *
 *    fault definitions
 *
 *========================================================================= */

import java.util.Map;
import java.util.HashMap;

public class Fault {

    public static final int MISSING_ID = 2;
    public static final int INVALID_ID = 3;
    public static final int UNSUPPORTED_OP = 4;
    public static final int NO_RECORD = 5;
    public static final int MARSHAL = 6;
    public static final int TRANSFORM = 7;
    public static final int QUERY_TYPE = 8;
    public static final int OP_NOT_ALLOWED = 9;

    public static final int REMOTE_TIMEOUT = 12;
    public static final int REMOTE_FAULT = 13;

    public static final int DUPLICATE_ENTRY = 22;
    public static final int TRANSACTION = 23;
    public static final int FORMAT = 24;
    public static final int REMOTE_VALIDATION = 25;
    public static final int VALIDATION_ERROR = 26;
    public static final int JSON_CONFIGURATION = 27;
    public static final int CACHE_FAULT = 29;

    public static final int AUTH = 98;
    public static final int UNKNOWN = 99;

    public static Map<Integer, String> fault;
    
    static {

        fault = new HashMap<Integer, String>();
        fault.put(  2, "missing identifier" );
        fault.put(  3, "invalid identifier" );
        fault.put(  4, "unsupported operation" );
        fault.put(  5, "no record found" );
        fault.put(  6, "marshaling error" );
        fault.put(  7, "transformation error" );
        fault.put(  8, "invalid query type" );
        fault.put(  9, "operation not allowed" );

        fault.put( 12, "remote server timeout" );
        fault.put( 13, "remote server fault" );
        
        fault.put( 22, "duplicate entry" );
        fault.put( 23, "transaction error" );
        fault.put( 24, "format error" );
        fault.put( 25, "remote validation" );
        fault.put( 26, "validation error" );
        fault.put( 27, "json configuration" );
        fault.put( 29, "memcache client fault" );

        fault.put( AUTH, "authentication error" );
        fault.put( UNKNOWN, "unrecognized error" );
    }
}
