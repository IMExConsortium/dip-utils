package edu.ucla.mbi.util.data.dao;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # JournalDAO
 #
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.data.*;

public interface JournalDao {

    public Journal getJournal( int id );
    public Journal getJournal( String title );
    public Journal getJournalByNlmid( String nlmid );
    public List<Journal> getJournalList();
    public List<Journal> getJournalList( int firstRecord, int blockSize );
    public long getJournalCount();

    public void saveJournal( Journal journal );
    public void updateJournal( Journal journal );
    public void deleteJournal( Journal journal );

}


