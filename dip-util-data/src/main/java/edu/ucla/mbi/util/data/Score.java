package edu.ucla.mbi.util.data;

/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * Score - foating point score 
 *
 ======================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

public class Score extends AttachedDataItem {

    public Score() {}
    
    //--------------------------------------------------------------------------
    
    String name = "";
    
    public void setName( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
   
    //--------------------------------------------------------------------------

    float value = 0.0f;

    public void setValue( float value ){
        this.value = value;
    }

    public float getValue(){
        return value;
    }

}
