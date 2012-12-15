package edu.ucla.mbi.util.struts.interceptor;

/* ========================================================================
 # $HeadURL::                                                             $
 # $Id::                                                                  $
 # Version: $Rev::                                                        $
 #=========================================================================
 #                                                                        $
 # TableViewAware interface: must be implemented by classes interceptable $
 #           by TableViewInterceptor                                      $
 #                                                                        $
 #====================================================================== */

import java.io.InputStream;
import java.util.Map;

public interface ExportAware extends TableViewAware {

    //---------------------------------------------------------------------
    // ExportAware
    //----------------------------------------

    public void setNb( String neighbours );
    public String getNb();

    public void setExport( String export );
    public String getExport();

    public void setXf( Map format );
    public Map getXf();


    // file export/download
    //---------------------
    
    public String getContentType();
    public void setContentType( String contentType );
    
    public String getContentDisposition();
    public void setContentDisposition( String contentDisposition );

    public InputStream getExportStream() ;
    public void setExportStream( InputStream is) ;
    
}

