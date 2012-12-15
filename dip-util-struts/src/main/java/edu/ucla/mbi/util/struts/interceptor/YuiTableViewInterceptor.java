package edu.ucla.mbi.util.struts.interceptor;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * YuiTableViewInterceptor: generates YUI-compatible data for classes          $
 *  implementing TableViewAware interface                                      $
 *                                                                             $
 *=========================================================================== */

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.regex.PatternSyntaxException;

import org.json.*;

public class YuiTableViewInterceptor extends TableViewInterceptor {

    protected void build( ActionInvocation invocation ) {
        
        Log log = LogFactory.getLog( this.getClass() );
        ValueStack stack = invocation.getStack();
        
        TableViewAware action = (TableViewAware) invocation.getAction();
        
        //TableViewContext tableContext = action.getTableContext();        
        //String table = action.getTableName();
        //List tableData = action.getTableData();

        action.setModelView( buildModelView( action.getViewDef() ) );

        action.setModelData( buildModelData( stack,
                                             action.getTableContext(),
                                             action.getTableName(),
                                             action.getFr(), 
                                             action.getMr(),
                                             action.getTr(),
                                             action.getFlt(),
                                             action.getTableData() ) );
        
        if ( action.getCvl() != null ) {
            action.setModelData( buildValues( stack,
                                              action.getTableContext(),
                                              action.getTableName(),
                                              action.getModelData(),
                                              action.getCvl(),
                                              action.getKnownData() ) );
        }
    }

    //--------------------------------------------------------------------------    

    private Map buildValues( ValueStack stack, 
                             TableViewContext tblContext,
                             String tblName,
                             Map modelData,
                             String cvl, List knownData ) {
        
        if ( modelData == null ) {
            modelData = new HashMap();
        }
        
        Map values = new HashMap();
        modelData.put( "colValue", values );

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "YuiTableView: buildValues:" + tblName );
        
        String[] fcol;
        
        try{
            fcol = cvl.split( ":" );
        } catch ( PatternSyntaxException pse ) {
            return modelData; // cvl not defined
        }

        if ( tblName != null && knownData != null ) {
            
            List<String> tblValue = tblContext.getValue( tblName );
            List tblLayout = tblContext.getLayout( tblName );
            
            for( int i = 0; i < fcol.length; i ++ ){
                String fld = fcol[i];
                log.info( "fld=" + fld );

                int cind = -1;
                for ( int fi = 0 ; fi <  tblLayout.size(); fi++ ) {
                    if ( fld.equals( ((Map) tblLayout.get(fi)).get("name") ) 
                         ) {
                        cind = fi;
                        break;
                    }
                }
                
                log.info( "fld index=" + cind);

                if ( cind < 0 || 
                     ((Map) tblLayout.get( cind ))
                     .get( "filter-type" ) == null ) break;
                
                String valOGNL = (String) 
                    ((Map) tblLayout.get( cind )).get( "filter-value" );
                if ( valOGNL == null ) {
                    valOGNL = tblValue.get( cind );
                }
                String lblOGNL = (String) 
                    ((Map) tblLayout.get( cind )).get( "filter-label" );

                log.info("valOGNL="+valOGNL+" lblOGNL="+lblOGNL);
                
                TreeMap<String,String> tm =  new TreeMap<String,String>();
                for ( int r = 0; r < knownData.size(); r++ ) {
                    log.info( "r=" + r ); 
                    
                    String vexpr = "knownData[" + r + "]." + valOGNL;
                    String val = stack.findString( vexpr );
                    
                    if ( val != null && val.length() > 0 ) {
                        String lbl = val;
                        if ( lblOGNL != null ){ 
                            String lexpr = "knownData[" + r + "]." + lblOGNL;
                            lbl = stack.findString( lexpr );
                        }
                        
                        log.info( "val=" + val + " lbl=" + lbl );
                        
                        if ( lbl != null && lbl.length() > 0) {
                            tm.put( lbl, val );
                        }
                    }
                }
                
                List<Map<String,String>> 
                    vl = new ArrayList<Map<String,String>>();
                
                for( Iterator<String> ii = tm.navigableKeySet().iterator();
                     ii.hasNext(); ){
                    String lbl = ii.next();
                    String val = tm.get( lbl );
                    Map<String,String> e = new HashMap<String,String>();
                    e.put("label",lbl);
                    e.put("value",val);
                    vl.add(e);
                }
                values.put(fld,vl);                                   
            }
        }
        return modelData;
    }
    
    //--------------------------------------------------------------------------    
   
    private Map buildModelView( Map viewDef ) {
        
        // prepare and return table/grid layout info
        //------------------------------------------
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "view def=" + viewDef );

        Map mv = new HashMap();
        List cm = new ArrayList();
        mv.put("colModel",cm);
        if ( viewDef == null ) return mv;
        for(Iterator ii = ((List)viewDef.get("colModel")).iterator();
            ii.hasNext(); ) {
            
            Map col = (Map) ii.next();
            Map newCol = new HashMap();
            cm.add( newCol );
            
            for( Iterator i = col.keySet().iterator(); i.hasNext(); ){

                String key = (String) i.next();
                Object  value =  col.get( key );

                if( key.equals("width") ){
                    String svalue = (String) value;
                    try {
                        svalue = svalue.replaceAll("[^0-9]","");
                    } catch ( PatternSyntaxException pse ) {
                        // should not happen
                    }
                    
                    if ( svalue.length() > 0 ) {
                        try {
                            int width = Integer.parseInt( svalue );
                            newCol.put( "width", width );
                        } catch (NumberFormatException  nfe ){
                            // ignore
                        }
                    }
                } else {
                    if( !key.equals( "name") ){
                        newCol.put( key, value );
                    } else {
                        newCol.put( "key", value );
                    }
                }
            }
            /*
            if ( col.get( "name" ) != null ) {
                newCol.put( "key", col.get( "name" ) );
            }

            
            if ( col.get( "label" ) != null ) {
                newCol.put( "label", col.get( "label" ) );
            }

            if ( col.get( "width" ) != null ) {
                String sWidth = (String) col.get( "width" );
                try {
                    sWidth = sWidth.replaceAll("[^0-9]","");
                } catch ( PatternSyntaxException pse ) {
                    // should not happen
                }
                
                if ( sWidth.length() > 0 ) {
                    try {
                        int width = Integer.parseInt( sWidth ); 
                        newCol.put( "width", width );
                    } catch (NumberFormatException  nfe ){
                        // ignore
                    }
                }
            }
            
            if ( col.get( "resizeable" ) != null ) {
                newCol.put( "resizeable", col.get( "resizeable" ) );
            }
            if ( col.get( "sortable" ) != null ) {
                newCol.put( "sortable", col.get( "sortable" ) );
            }
            if ( col.get( "hidden" ) != null ) {
                newCol.put( "hidden", col.get( "hidden" ) );
            }
            if ( col.get( "filter" ) != null ) {
                newCol.put( "filter", col.get( "filter" ) );
            }
            */
        }

        return mv;
    }
   

    //---------------------------------------------------------------------
    
    private Map buildModelData( ValueStack stack,
                                TableViewContext tblContext, 
                                String tblName,  
                                int firstRecord, 
                                int maxRecord, 
                                int totalRecord,
                                String filter,
                                List tblData ) {
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "\n\nYuiTableView: tableName=" + tblName + " filter=" + filter );

        List tblLayout = tblContext.getLayout( tblName );
        TableFilter tflt = null; 
        if ( filter != null ) {
            tflt = new TableFilter( tblLayout, filter );
        }

        Map modelData = new HashMap();
        List data = new ArrayList();
        
        if ( tblName != null && tblData != null ) {

            log.info( "tblData size=" + tblData.size() );
            
            int fltRecord = totalRecord;
            
            List<String> tblValue = tblContext.getValue( tblName );
            List<String> tblList = tblContext.getList( tblName );
            List<String> tblUrl = tblContext.getUrl( tblName );
            List<String> tblUrlvalue = tblContext.getUrlvalue( tblName );
            List<String> tblType = tblContext.getType( tblName );
            List<String> tblField = tblContext.getField( tblName );
            
            // go over items/rows
            // -------------------
            
            for ( int r = 0; r < tblData.size(); r++ ) {
                
                Map dataRow = new HashMap();
                Map dataVal = new HashMap();
                
                // go over columns
                // ----------------
                
                int ii = 0;
                
                for ( Iterator<String> i = tblValue.iterator(); 
                      i.hasNext(); ) {

                    String valuePattern = i.next();
                    
                    if ( tblList.get( ii ) == null ) {
                        
                        if ( valuePattern.length() > 0 ) {
                            
                            String expr = "tableData[" + r + "]."
                                + valuePattern;
                            String val = stack.findString( expr );
                            dataRow.put( tblField.get(ii), val );
                            
                            if ( tblType.get( ii ).equalsIgnoreCase( "url" ) ) {
                                String uval = val;
                                if ( tblUrl.get(ii) != null && val != null
                                     && tblUrl.get(ii).length() > 0 ) {
                                    
                                    String urlTemplate = tblUrl.get( ii );
                                    String urlValue = val;
                                    if ( tblUrlvalue != null ) {
                                        urlValue = tblUrlvalue.get( ii );
                                        if ( urlValue != null
                                             && urlValue.length() > 0 ) {
                                            expr = "tableData[" + r + "]."
                                                + urlValue;
                                            urlValue = stack.findString( expr );
                                        }
                                        if ( urlValue == null
                                             || urlValue.length() == 0 ) {
                                            urlValue = val;
                                        }
                                    }
                                    
                                    urlTemplate = 
                                        urlTemplate.replaceAll( "%VAL%", val);
                                    
                                    uval = "<a href=\"" + urlTemplate + "\">"
                                        + urlValue + "</a>";
                                }
                                dataRow.put( tblField.get( ii ), 
                                             uval == null ? "" : uval );
                            } else {
                                dataRow.put( tblField.get(ii), 
                                             val == null ? "" : val );
                            }
                        } else {
                            dataRow.put(ii, "");
                        }
                    } else {
                        
                        log.info( "list=" + tblList.get( ii ) + " val= " + valuePattern );
                        String lexpr = "tableData[" + r + "]."+ tblList.get( ii );

                        List ol = (List) stack.findValue( lexpr );
                        log.info( "list=" + ol );

                        String lval = "";
                        if ( ol != null ) {
                            for( Iterator li = ol.iterator(); 
                                 li.hasNext(); ) {
                                
                                Object o = li.next();
                                stack.push(o);
                                String val =  stack.findString( valuePattern );
                                log.info( "item val=" + val);

                                if ( tblUrl.get( ii ) != null 
                                     && tblUrl.get( ii ).length() > 0 ) {
                                    String uval = val;
                                    if ( val != null ) {
                                        String urlTemplate = tblUrl.get( ii );
                                        String urlValue = val;
                                        
                                        if ( tblUrlvalue != null ) {
                                            urlValue = tblUrlvalue.get( ii );
                                            if ( urlValue != null
                                                 && urlValue.length() > 0 ) {
                                                //uexpr = urlValue;
                                                urlValue = stack.findString( urlValue );
                                            }
                                            if ( urlValue == null
                                                 || urlValue.length() == 0 ) {
                                                urlValue = val;
                                            }
                                        }

                                        urlTemplate =
                                            urlTemplate.replaceAll( "%VAL%", val);

                                        uval = "<a href=\"" + urlTemplate + "\">"
                                            + urlValue + "</a>";
                                        
                                        lval = lval + uval + ", ";
                                        
                                    }
                                } else {
                                    lval = lval + val + ", ";
                                }
                                stack.pop();                                
                            }

                            if ( lval.length() > 0 )  {
                                lval = lval.substring(0,lval.length()-2);
                            }
                            log.info( "lval=" + lval );
                            dataRow.put( tblField.get(ii), lval );
                        }

                        if ( valuePattern.length() > 0 ) {
                            
                            String expr = "tableData[" + r + "]."
                                + valuePattern;
                            String val = stack.findString( expr );
                            //LS: dataRow.put( tblField.get(ii), val );
                            
                            if ( tblType.get( ii ).equalsIgnoreCase( "url" ) ) {
                                String uval = val;
                                if ( tblUrl.get(ii) != null && val != null
                                     && tblUrl.get(ii).length() > 0 ) {
                                    
                                    String urlTemplate = tblUrl.get( ii );
                                    String urlValue = val;
                                    if ( tblUrlvalue != null ) {
                                        urlValue = tblUrlvalue.get( ii );
                                        if ( urlValue != null
                                             && urlValue.length() > 0 ) {
                                            expr = "tableData[" + r + "]."
                                                + urlValue;
                                            urlValue = stack.findString( expr );
                                        }
                                        if ( urlValue == null
                                             || urlValue.length() == 0 ) {
                                            urlValue = val;
                                        }
                                    }
                                    
                                    urlTemplate = 
                                        urlTemplate.replaceAll( "%VAL%", val);
                                    
                                    uval = "<a href=\"" + urlTemplate + "\">"
                                        + urlValue + "</a>";
                                }
                                //dataRow.put( tblField.get( ii ), 
                                //             uval == null ? "" : uval );
                            } else {
                                //dataRow.put( tblField.get(ii), 
                                //             val == null ? "" : val );
                            }
                        } else {
                            //dataRow.put(ii, "");
                        }
                    }
                    ii++;
                }
                
                if ( tflt == null ) {  // no filter                
                    data.add( dataRow );
                    //log.info( " added #" + data.size() + "\n\n");
                } else {               // apply filter
                    if ( tflt.match( stack, r ) ) {
                        data.add( dataRow );
                        //log.info( " added #" + data.size() + "\n\n");
                    } else {
                        //log.info( " skipped\n\n");
                    }
                }
            }
            
            fltRecord = data.size();
            

            if ( filter != null ) {
                try {
                    if( firstRecord > 0 ){
                        data = data.subList( firstRecord, data.size() );                         
                    } 
                    if ( maxRecord > 0  && maxRecord < data.size() ) {
                        data = data.subList( 0, maxRecord );
                    }    
                } catch( Exception e ) {
                    e.printStackTrace();
                }
            }
            
            modelData.put( "data", data );

            if ( tflt == null ) {
                modelData.put( "total", totalRecord );                
            } else {
                modelData.put( "total", fltRecord );
            }
             modelData.put( "noflt", totalRecord );
             modelData.put( "first", firstRecord );
             modelData.put( "max", maxRecord );
        }
        
        log.info("\n\nmodelData=" + modelData + "\n\n");
        return modelData;
    }
}
