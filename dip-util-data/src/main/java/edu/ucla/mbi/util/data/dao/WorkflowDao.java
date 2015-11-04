package edu.ucla.mbi.util.data.dao;

/* =============================================================================
 # $HeadURL::                                                                  $
 # $Id::                                                                       $
 # Version: $Rev::                                                             $
 #==============================================================================
 #
 # WorkflowDAO - workflow control persistence
 #
 #=========================================================================== */

import java.util.*;
import edu.ucla.mbi.util.data.*;

public interface WorkflowDao {

    public DataState getDataState( int id );
    public DataState getDataState( String title );
    public List<DataState> getDataStateList();
    public long getDataStateCount();

    public DataState getDataStage( int id );
    public DataState getDataStage( String title );

    public void saveDataState( DataState state );
    public void updateDataState( DataState state );
    public void deleteDataState( DataState state );

    public Transition getTrans( int id );
    public Transition getTrans( String name );
    public List<Transition> getTransList();
    public List<Transition> getAllowedTransList( DataState state );
    public long getTransCount();

    public void saveTrans( Transition trans );
    public void updateTrans( Transition trans );
    public void deleteTrans( Transition trans );

}
