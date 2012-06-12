package edu.ucla.mbi.util.data.dao;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # AdiDAO - AttachedDataItem DAO
 #     
 #======================================================================= */

import java.util.*;
import edu.ucla.mbi.util.data.*;

public interface AdiDAO {

    public AttachedDataItem getAdi( int id );
    public List<AttachedDataItem> getAdiListByRoot( DataItem root );
    public List<AttachedDataItem> getAdiListByParent( DataItem parent );
    
    public void saveAdi( AttachedDataItem  adi );
    public void updateAdi( AttachedDataItem adi );
    public void deleteAdi( AttachedDataItem adi );
    
}
