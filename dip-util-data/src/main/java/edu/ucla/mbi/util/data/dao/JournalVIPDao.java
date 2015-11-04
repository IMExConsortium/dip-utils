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

public interface JournalVIPDao extends JournalDao{

    public Journal getJournal( int id );
    public Journal getJournal( String title );
    public Journal getJournalByNlmid( String nlmid );
    public List<Journal> getJournalList();
    public List<Journal> getJournalList( int firstRecord, int blockSize );
    
    public List<Journal> getJournalList( int firstRecord,
                                         int blockSize,
                                         String sortKey, boolean asc );

    public List<Journal> getJournalList( int firstRecord,
                                         int blockSize,
                                         String sortKey, boolean asc,
                                         Map<String,String> flt );
    
    
    public long getJournalCount( Map<String,String> flt );
    public long getJournalCount();

    public void saveJournal( Journal journal );
    public void updateJournal( Journal journal );
    public void deleteJournal( Journal journal );

    public String getJournalYear( Journal journal, boolean first );
    public List<String> getJournalYearList( Journal journal, boolean first );

    public String getJournalVolume( Journal journal, boolean first,
                                    String year );
    public List<String> getJournalVolumeList( Journal journal, boolean first,
                                              String year );

    public String getJournalIssue( Journal journal, boolean first,
                                   String year, String volume );
    public List<String> getJournalIssueList( Journal journal, boolean first,
                                             String year, String volume  );

    public Map<Journal,List> getJournalListStats( List<Journal> jlist );

    public List<Group> getAdminGroups( String query );

}


