package edu.ucla.mbi.util.struts2.action;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * TableMgrSupport - record->table map manager                                 $
 *                                                                             $
 *                                                                             $
 ============================================================================ */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

import java.util.*;
import java.util.regex.PatternSyntaxException;

import java.io.*;
import org.json.*;

import edu.ucla.mbi.util.*;
import edu.ucla.mbi.util.struts2.action.*;
import edu.ucla.mbi.util.struts2.interceptor.*;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import javax.servlet.ServletContext;

public abstract class TableMgrSupport extends ManagerSupport {

    //--------------------------------------------------------------------------
    // operations
    //-----------

    public static final String TABLE_ADD = "tadd";   // add table
    public static final String TABLE_DRP = "tdrp";   // drop table
    public static final String COLUMN_SVE = "csve";  // save current column
    public static final String COLUMN_ADD = "cadd";  // add column
    public static final String COLUMN_DRP = "cdrp";  // drop column
    public static final String COLUMN_MOV = "cmov";  // move column

    //--------------------------------------------------------------------------
    // return values
    //--------------

    public static final String JSON ="json";

    //--------------------------------------------------------------------------
    // configuration
    //--------------

    private TableViewContext tableContext;

    public void setTableContext( TableViewContext context ){
        this.tableContext = context;
    }

    public TableViewContext getTableContext() {
        return tableContext;
    }

    //--------------------------------------------------------------------------

    public Map getTables(){
        return getTableContext().getTables();
    }

    //--------------------------------------------------------------------------
    // current page
    //-------------

    private String pageId;

    public void setPageid( String pageId ) {
        this.pageId = pageId;
    }

    public String getPageid() {
        return pageId;
    }

    //--------------------------------------------------------------------------
    // initialize
    //-----------

    public void initialize(){
        initialize( false );
    }

    public void initialize( boolean force ){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "initializing: ActionContext=" + getContext() );

        // initialize tableContext
        //------------------------

        if( getTableContext() != null
            && ( getTableContext().getTables() == null || force ) ){

            // process json configuration
            //---------------------------

            if( getTableContext() instanceof TableViewJsonContext ){

                TableViewJsonContext tvgc
                    = (TableViewJsonContext) this.getTableContext();

                String jsonPath = (String) tvgc.getJsonContext()
                    .getConfig().get( "json-config" );

                log.info( " JsonTableDef=" + jsonPath );

                if ( jsonPath != null && jsonPath.length() > 0 ) {

                    String cpath = jsonPath.replaceAll("^\\s+","" );
                    cpath = jsonPath.replaceAll("\\s+$","" );

                    try {

                        ServletContext sc = getServletContext();
                        log.info( " ServletContext sc=" + sc );
                        InputStream is =
                            getServletContext().getResourceAsStream( cpath );

                        log.info( " JsonTableDef stream=" + is );

                        tvgc.jsonInitialize( is );
                        tvgc.initialize();

                    } catch ( Exception e ){
                        e.printStackTrace();
                        log.info( "JsonConfig reading error" );
                    }

                    tvgc.initialize();
                }
            }
        }
    }


    //--------------------------------------------------------------------------

    protected void saveTableContext(){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "storing: ActionContext=" + getContext() );

        if( getTableContext() != null
            && getTableContext() instanceof TableViewJsonContext ){

            JsonContext jsonContext =
                ((TableViewJsonContext) getTableContext()).getJsonContext();

            String jsonConfigFile =
                (String) jsonContext.getConfig().get( "json-config" );

            String srcPath =
                getServletContext().getRealPath( jsonConfigFile );
            log.info( " srcPath=" + srcPath );

            try{
                File sf = new File( srcPath );

                PrintWriter spw = new PrintWriter( sf );
                jsonContext.writeJsonConfigDef( spw );
                spw.close();

                getTableContext().initialize(); // reinitialize

            } catch( IOException ioe ){
                // NOTE: log me !!!!
            }
        }
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------

    public String execute() throws Exception {

        // force table initialization
        //---------------------------

        initialize( true );

        // decode operation
        //-----------------

        String opcode = null;

        if( getOp() != null ) {

            for( Iterator<String> ii = getOp().keySet().iterator();
                 ii.hasNext(); ) {
                String key = ii.next();
                if ( key.equals( TABLE_ADD ) ) {
                    return tableAdd();
                }

                if ( key.equals( TABLE_DRP ) ) {
                    return tableDrop();
                }

                if ( key.equals( COLUMN_SVE ) ) {
                    return columnSave();
                }

                if ( key.equals( COLUMN_ADD ) ) {
                    return columnAdd();
                }
                if ( key.equals( COLUMN_DRP ) ) {
                    return columnDrop();
                }
                if ( key.equals( COLUMN_MOV ) ) {
                    return columnMove();
                }

            }
            return JSON;
        }
        return SUCCESS;
    }

    //--------------------------------------------------------------------------

    private String tableAdd(){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( " OP: table add" );

        saveTableContext();
        return JSON;
    }

    //--------------------------------------------------------------------------

    private String tableDrop(){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( " OP: table drop" );

        saveTableContext();
        return JSON;
    }

    //--------------------------------------------------------------------------

    private String columnAdd(){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( " OP: column add" );

        saveTableContext();
        return JSON;
    }

    //--------------------------------------------------------------------------

    private String columnDrop(){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( " OP: column drop" );


        saveTableContext();
        return JSON;
    }

    //--------------------------------------------------------------------------

    private String columnSave(){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( " OP: column save" );

        // update JsonContext
        //-------------------

        if( getTableContext() != null
            && getTableContext() instanceof TableViewJsonContext ){

            JsonContext jsonContext =
                ((TableViewJsonContext) getTableContext()).getJsonContext();


            // modify column definition
            //-------------------------

            Map<String,Object> tables = jsonContext.getJsonConfig();



            // make changes permanent
            //-----------------------

            saveTableContext();


            // reinitialize
            //-------------

            initialize( true );
        }
        return JSON;
    }

    //--------------------------------------------------------------------------

    private String columnMove(){


        saveTableContext();
        return JSON;
    }


    //--------------------------------------------------------------------------
    // validation
    //-----------

    public void validate() {

    }
}
