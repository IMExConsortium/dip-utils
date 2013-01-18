package edu.ucla.mbi.util.context;

/* =============================================================================
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #                                                                             $
 # ContextListener:                                                            $
 #                                                                             $
 #=========================================================================== */

/**
 * Interface implemented by classes interested in receiving context updates.
 **/

public interface ContextListener {
  
    /** 
     * The method called upon context updates.
     * @param  context the context being updated
     **/
    public void contextUpdate( JsonContext context );
       
}