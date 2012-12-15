package edu.ucla.mbi.util.struts.interceptor;

/* ========================================================================
 # $HeadURL:: https://lukasz@imex.mbi.ucla.edu/svn/dip-ws/dip-util/trunk/#$
 # $Id:: TableViewAware.java 1304 2010-11-06 19:10:51Z lukasz             $
 # Version: $Rev:: 1304                                                   $
 #=========================================================================
 #                                                                        $
 # TableViewAware interface: must be implemented by classes interceptable $
 #           by TableViewInterceptor                                      $
 #                                                                        $
 #====================================================================== */

import java.util.*;

public interface TableViewAware {
    
    public void initialize();
    
    public void setTableContext( TableViewContext context );
    public TableViewContext getTableContext();

    public String getTableName();
    public void setTableName( String table );
    
    public Map getTableMeta();
    public void setTableMeta( Map meta );
    
    public List getTableData();
    public void setTableData( List items );

    public List getKnownData();
    public void setKnownData( List items );

    public List<String> getJqModelList();
    public void setJqModelList( List<String> items );

    public List<String> getJqModelType();
    public void setJqModelType( List<String> items );
    
    public Map getJqModelView();
    public void setJqModelView( Map model );
    
    public List getJqCountList();
    public void setJqCountList( List items );

    public List getJqData();
    public void setJqData( List items );

    public Map getJqGridData();
    public void setJqGridData( Map items );
    
    public List getRows();
    public void setRows( List items );
    
    public void setTotal( int total );
    public int getTotal();

    public void setFiltered( int filtered );
    public int getFiltered();
    
    public void setPage( int page );
    public int getPage();
   
    public void setRecords( int records ); 
    public int getRecords();

    //---------------------------------------------------------------------
    // TableViewAware: library neutral version
    //----------------------------------------

    public void setFr( int firstRecord );
    public int getFr();

    public void setMr( int maxRecord );
    public int getMr();

    public void setTr( int totalRecord );
    public int getTr();

    public void setCvl( String cvl );
    public String getCvl();

    public void setFlt( String filter );
    public String getFlt();

    public void setSrt( String sort );
    public String getSrt();

    public List<Map<String,String>> getModelList();
    public void setModelList( List<Map<String,String>> models );

    public void setModelCountList( List<Long> counts );
    public List<Long> getModelCountList();

    public String getViewType();
    public void setViewType( String type );

    public Map getViewDef();
    public void setViewDef( Map def );
    
    public Map getModelView();
    public void setModelView( Map model );

    public Map getModelData();
    public void setModelData( Map model );

}

