package edu.ucla.mbi.util.data;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *
 * AttachedDataItem - a unit of data attached to (annotatting) data items
 *
 ============================================================================ */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

import edu.ucla.mbi.util.*;

public abstract class AttachedDataItem extends DataItem {

    private DataItem root;
    private DataItem parent;

    public DataItem getParent() {
        return parent;
    }

    public void setParent( DataItem parent ){
        this.parent = parent;
    }

    public DataItem getRoot(){
        return root;
    }

    public void setRoot( DataItem root ){
        this.root = root;
    }
    
}
