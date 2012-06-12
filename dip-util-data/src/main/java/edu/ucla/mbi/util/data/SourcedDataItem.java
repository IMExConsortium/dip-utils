package edu.ucla.mbi.util.data;

/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * DataItem - a traceable unit of data
 *
 *
 ======================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

import edu.ucla.mbi.util.*;

public abstract class SourcedDataItem extends DataItem {

    private DataSource source;

    public DataSource getSource() {
        return source;
    }

    public void setSource( DataSource source ) {
        this.source = source;
    }
    
}