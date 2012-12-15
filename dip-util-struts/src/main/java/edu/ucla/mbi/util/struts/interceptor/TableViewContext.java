package edu.ucla.mbi.util.struts.interceptor;

/* =========================================================================
   # $HeadURL:: https://lukasz@imex.mbi.ucla.edu/svn/dip-ws/dip-util/trunk/d#$
   # $Id:: TableViewContext.java 638 2009-10-19 00:17:18Z lukasz             $
   # Version: $Rev:: 638                                                     $
   #==========================================================================
   #
   # TableViewContext: generates json table configuration information 
   #         utilized by classes implementing TableViewAware interface 
   #         intercepted by TableViewInterceptor
   #
   #======================================================================= */

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TableViewContext {

    Map tableConfig;
    Map tables = new HashMap();

    public Map getTables() {
        return tableConfig;
    }

    public void setTables( Map tables ) {
        this.tableConfig = tables;
    }

    public String getIdentifier( String table ) {
        if ( tables.containsKey( table ) ) {
            String id
                = (String) ((Map) tables
                            .get( table )).get( "identifier" );
            return id;
        } else {
            return null;
        }
    }

    public String getLabel( String table ) {
        if ( tables.containsKey( table ) ) {
            String id
                = (String) ((Map) tables
                            .get( table )).get( "label" );
            return id;
        } else {
            return null;
        }
    }

    public List getLayout( String table ) {
        if ( tables.containsKey( table ) ) {
            return ((Map<String,List>) tables
                    .get( table )).get( "layout" );
        }
        return null;
    }
    /*
    public List<Map> getFilter( String table ) {
        if ( tables.containsKey( table ) ) {
            return ((Map<String,List<Map>>) tables
                    .get( table )).get( "filter" );
        }
        return null;
    }
    */
    public List<String> getField(String table){
        if(tables.containsKey(table)){
            return ((Map<String,List<String>>)tables
                    .get(table)).get("field");
        } 
        return null;
    }

    public List<String> getValue( String table ) {
        if( tables.containsKey( table ) ) {
            return ((Map<String,List<String>>) tables
                    .get( table )).get( "value" );
        }
        return null;
    }

    public List<String> getList( String table ) {
        if( tables.containsKey( table ) ) {
            return ((Map<String,List<String>>) tables
                    .get( table )).get( "list" );
        }
        return null;
    }
    
    public List<String> getUrl( String table ) {
        if ( tables.containsKey( table ) ) {
            return ((Map<String,List<String>>) tables
                    .get( table )).get( "url" );
        }
        return null;
    }
    
    public List<String> getUrlvalue( String table ) {
        if ( tables.containsKey( table ) ) {
            return ((Map<String,List<String>>) tables
                    .get( table )).get( "urlvalue" );
        }
        return null;
    }

    public List<String> getType( String table ) {
        if ( tables.containsKey( table ) ) {
            return ((Map<String,List<String>>) tables
                    .get( table )).get( "type" );
        }
        return null;
    }
    
    public Map<String,Boolean> getSelect( String table ) {
        if ( tables.containsKey( table ) ) {
            return ((Map<String,Map<String,Boolean>>) tables
                    .get( table )).get( "select" );
        }
        return null;
    }

    public void initialize() {
                                                                           
        // use config structure to build individual layout information

        if ( tableConfig == null ) {
            return;
        }
        Set tableKeys = tableConfig.keySet();
 
        for ( Iterator i = tableKeys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            Map tableDef = (Map) tableConfig.get( key );
            this.addLayout( key, tableDef );
        }
    }

    private void addLayout( String key, Map tableDef ) {

        Log log =
            LogFactory.getLog( this.getClass() );
        log.info( "TableViewContext: adding table->" + key );
 
        if ( key == null ) {
            return;
        }
 
        Map<String,Object> table = new HashMap<String,Object>();

 
        // item identifier
        //----------------

        String id   = (String) tableDef.get( "idfield" );

        if( id != null && id.length() > 0 ) {
            table.put( "identifier", id );
        }
        
        // item label
        //-----------

        String label = (String) tableDef.get( "labelfield" );
        if ( label != null && label.length() > 0 ) {
            table.put( "label", id );
        }
 
        // lists of properties (one/column)
        //---------------------------------
        List columns = new ArrayList();
 
        List<String> fields = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        List<String> lists = new ArrayList<String>();
        List<String> types = new ArrayList<String>();
        Map<String,Boolean> sortFlags
            = new HashMap<String,Boolean>();
        Map<String,Boolean> selFlags 
            = new HashMap<String,Boolean>();
        List<String> urls = new ArrayList<String>();
        List<String> urlvals = new ArrayList<String>();

        //List<Map> filters = new ArrayList<Map>();
 
        table.put( "layout", columns );
        table.put( "field", fields );
        table.put( "value", values );
        table.put( "type", types );
        table.put( "list", lists );
        table.put( "sort", sortFlags );
        table.put( "select", selFlags );
        table.put( "url", urls );
        table.put( "urlvalue", urlvals );
        //table.put( "filter",filters );
 
        /// process definition
        //--------------------
        List colDefList = (List) tableDef.get("fields");

        if ( colDefList != null ) {
            int cid = 0;

            for ( Iterator i = colDefList.iterator(); 
                  i.hasNext(); ){
  
                Map colDef = (Map) i.next(); 
                String field = (String) colDef.get( "field" );
                String name  = (String) colDef.get( "name" );
                String value = (String) colDef.get( "value" );
                String type  = (String) colDef.get( "type" );
                String list  = (String) colDef.get( "list" );
                String formatter  = (String) colDef.get( "formatter" );
                String show  = (String) colDef.get( "show" );
                String width = (String) colDef.get( "width" );
                String minWidth = (String) colDef.get( "min-width" );
                String maxAutoWidth = (String) colDef.get( "max-auto-width" );
                String resize = (String) colDef.get( "resize" );
                String hidden  = (String) colDef.get( "hidden" );
                String sort  = (String) colDef.get( "sort" );
                String select= (String) colDef.get( "select" );

                String filter= (String) colDef.get( "filter" );
                String filterType= (String) colDef.get( "filter-type" );
                String filterLabel= (String) colDef.get( "filter-label" );
                String filterValue= (String) colDef.get( "filter-value" );
                
                String url   = (String) colDef.get( "url" );
                String urlvalue   = (String) colDef.get( "urlvalue" );
                
  
                // default field: column number  
                if ( field == null || field.length() == 0 ) {
                    field = String.valueOf( cid );
                }
                // default type: text (url pattern overrides to url)
                if ( type == null || type.length() == 0 ) {

                    type = "text";
                
                    if ( url != null && url.length() > 0 ) {
                        type = "url";
                    }
                }
                // default show: true
                if ( show == null ) {
                    show = "true";
                }
                // default hide: false
                if ( hidden == null ) {
                    hidden = "false";
                }
                // default sort: false
                if ( sort == null ) {
                    sort = "false";
                }
                // default select: true
                if ( select == null ) {
                    select = "false";
                }
  
                if ( show.equalsIgnoreCase( "true" ) ) {  
                    
                    // only fields to be shown
                    Map column = new HashMap();
                    Map fpar = new HashMap();

                    column.put( "name", field ) ; //String.valueOf( cid-1 ));
                    column.put( "label", name );
                    
                    if ( width != null && width.length() > 0 ) {
                        column.put( "width", width );
                    }
                    
                    if ( minWidth != null && minWidth.length() > 0 ) {
                        column.put( "min-width", minWidth );
                    }
                    if ( maxAutoWidth != null && 
                         maxAutoWidth.length() > 0 ) {
                        column.put( "max-auto-width", maxAutoWidth );
                    }
                    
                    if ( resize != null && 
                         resize.equalsIgnoreCase( "true" ) ) {
                        column.put( "resizeable", 
                                    resize.equalsIgnoreCase( "true" ) );
                    }
                    if ( sort != null && 
                         sort.equalsIgnoreCase( "true" ) ) {
                        column.put( "sortable", 
                                    sort.equalsIgnoreCase( "true" ) );
                    }
                    
                    if ( formatter != null && formatter.length() > 0 ) {
                        column.put( "formatter", formatter );
                    }
    
                    if ( hidden != null && 
                         hidden.equalsIgnoreCase( "true" ) ) {
                        column.put( "hidden", 
                                    hidden.equalsIgnoreCase( "true" ) );
                    }
                    
                    if ( filter != null && 
                         filter.equalsIgnoreCase( "true" ) ) {
                        
                        column.put( "filter",
                                    filter.equalsIgnoreCase( "true" ) );
                        
                        if ( filterType != null && 
                             filterType.length() > 0 ) {
                            column.put( "filter-type", filterType );
                        }
                        if ( filterLabel != null && 
                             filterLabel.length() > 0 ) {
                            column.put( "filter-label", filterLabel );
                        }
                        if ( filterValue != null && 
                             filterValue.length() > 0 ) {
                            column.put( "filter-value", filterValue );
                        }
                    } 
                    
                    columns.add( column );
                    //filters.add( fpar );

                    sortFlags.put( field,
                                   sort.equalsIgnoreCase("true") );
                    selFlags.put( field,
                                  select.equalsIgnoreCase( "true" ) );
                } 
      
                
                fields.add( field );
                values.add( value );
                types.add( type );
                lists.add( list );
                urls.add( url );
                urlvals.add(urlvalue);
                cid++;
            }
        }
        tables.put( key, table );
    }

    public Map getViewInfo( String viewType, String viewName ) {

        // NOTE: viewType is dummy for now. willbe used when 
        //       view types different then table/grid are added

        //colNames:['Inv No','Date', 'Amount','Tax','Total','Notes'], 
        //colModel :[ {name:'invid', index:'invid', width:55}, 
        //            {name:'invdate', index:'invdate', width:90}, 
        //            ...
        //          ]

        Map res = new HashMap();

        List cName = new ArrayList();
        List cModel = new ArrayList();

        res.put("colNames",cName );
        res.put("colModel",cModel );

        Map table = (Map) tables.get( viewName );
 
        cModel.addAll( (List) table.get( "layout" ) );

        for( Iterator i = ((List) table.get( "layout" )).iterator(); 
             i.hasNext(); ) {

            Map col = (Map) i.next();
            String name = (String) col.get( "label" );
            cName.add( name ) ;
        }
        return res;
    }
}
