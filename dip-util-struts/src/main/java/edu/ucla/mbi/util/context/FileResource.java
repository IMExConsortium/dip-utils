package edu.ucla.mbi.util.context;

/* ========================================================================
 # $Id::                                                                  $
 # Version: $Rev::                                                        $
 #=========================================================================
 #                                                                        $
 # FilesystemResource: filesystem access                                  $
 #                                                                        $
 #     TO DO:                                                             $
 #                                                                        $
 #======================================================================= */

import java.io.*;

public interface FileResource {

    public InputStream getInputStream() throws IOException; 

}
