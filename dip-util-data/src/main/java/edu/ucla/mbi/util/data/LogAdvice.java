package edu.ucla.mbi.util.data;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 * =============================================================================
 *
 * LogAdvice - super AOP logger
 *                 
 * ========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import edu.ucla.mbi.dip.*;
import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.dip.dbservice.dxf.DipDbFault;

public abstract class LogAdvice {

    
    public LogAdvice() {
        org.apache.commons.logging.Log
            logFile = LogFactory.getLog( LogAdvice.class );
	    logFile.info( "LogAdvice: creating log manager" );
    }

    public abstract void createDipRecordOperation( DipRecord dipRecord, 
                                                   String user,
                                                   DipRecord rDipRecord );

    public abstract void updateDipRecordOperation( DipRecord dipRecord,
                                                   String user,
                                                   DipRecord rDipRecord );
        
    public abstract void createCvtermOperation( CVTerm cvterm,
                                                String user,
                                                CVTerm rCvterm );

    public abstract void beforeTopNodeOperation ( NodeType node,
                                                  String createIn,
                                                  String updateIn,
                                                  String userIn
                                                  );


    public abstract void afterTopNodeOperation ( NodeType node,
                                                 String createIn,
                                                 String updateIn,
                                                 String userIn
                                                 ); 
        
    public abstract void afterThrowing ( NodeType node,
                                         String createIn,
                                         String updateIn,
                                         String userIn,
                                         DipDbFault fault );
}
