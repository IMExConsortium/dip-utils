package edu.ucla.mbi.util.struts.interceptor;

/* ========================================================================
 * $HeadURL::                                                             $
 * $Id::                                                                  $
 * Version: $Rev::                                                        $
 *=========================================================================
 *                                                                        $
 * TableFilter: filter applied to table contents                          $
 *                                                                        $
 *====================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.util.ValueStack;

import java.util.*;
import java.util.regex.PatternSyntaxException;

import org.json.*;

public class TableFilter {

    Map<String,String> flt; 
    Map<String,String> fltOGNL;

    private TableFilter(){};

    public TableFilter( List tableLayout, String filter) {

        Log log = LogFactory.getLog( this.getClass() );
        
        flt = new HashMap<String,String>();
        fltOGNL = new HashMap<String,String>();                       
        JSONObject jflt = null;
        
        try {
            if ( filter != null ) {
                jflt = new JSONObject( filter );
                String[] names = JSONObject.getNames( jflt );
                for( int ni = 0; ni < names.length; ni++ ) {
                    String cid = names[ni];
                    String cval = jflt.getString( cid );
                    flt.put( cid,cval );
                    log.info(" col: " + cid + " val: " + cval );
                }
            }

            // get filter OGML
            //----------------

            for( Iterator<String> fi = flt.keySet().iterator();
                 fi.hasNext(); ) {
                
                String cfld = fi.next();    // filter field/name

                int cind = -1;
                for ( int ci = 0 ; ci <  tableLayout.size(); ci++ ) {
                    if ( cfld.equals( ((Map) tableLayout.get(ci)).get("name") ) 
                         ) {
                        cind = ci;
                        break;
                    }
                }

                if ( cind < 0 || 
                     ((Map) tableLayout.get( cind ))
                     .get( "filter-type" ) == null ) break;
                
                String valOGNL = (String) 
                    ((Map) tableLayout.get( cind )).get( "filter-value" );
                
                fltOGNL.put( cfld, valOGNL );
                
                log.info(" Filter: field=" + cfld + 
                         " val=" + flt.get(cfld) +
                         " ognl=" + fltOGNL.get(cfld) );
            }
            
        } catch ( JSONException je ) {
            log.info( "YuiTableView: filter JSON exception" );
            je.printStackTrace();
        }
    }

    public boolean match( ValueStack stack, int dataIndex ) {

        Log log = LogFactory.getLog( this.getClass() );
        boolean pass = false;
                    
        for( Iterator<String> fi = flt.keySet().iterator(); 
             fi.hasNext(); ) {
                        
            String cfld = fi.next();       // field
            log.info( "\n\ncfld=" + cfld );
                        
            String cval = flt.get( cfld );  // filter-value to match
            String cOGNL = fltOGNL.get( cfld );  // filter-value to match
                        
            String vexpr = "tableData[" + dataIndex + "]." + cOGNL;
            String dval = stack.findString( vexpr );

            log.info( "ognl=" + vexpr + " found=" + dval + " search=" + cval);
            
            try { 
                if ( cval == null ) { 
                    log.info( "cval == null....");
                    pass = true; 
                } else {
                    if ( cval.equals( dval) ) {
                        log.info( "cval == dval");
                        pass = true;
                    } else {
                        pass = false;
                    }
                }
            } catch( Exception e ) {
                pass = false;
                e.printStackTrace();
            }
            
            if ( ! pass ) { break; }
        }                    
        return pass;
    }
}
