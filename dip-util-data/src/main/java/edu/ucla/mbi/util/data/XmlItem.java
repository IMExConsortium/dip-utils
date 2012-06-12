package edu.ucla.mbi.util.data;

/* =========================================================================
 * $HeadURL::                                                              $
 * $Id::                                                                   $
 * Version: $Rev::                                                         $
 *==========================================================================
 *
 * XmlItem - xml document 
 *
 ======================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

public class XmlItem extends AttachedDataItem {
    
    public XmlItem() {}

    //--------------------------------------------------------------------------
    
    String label = "";
    
    public void setLabel( String label ) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    //--------------------------------------------------------------------------

    String xmlStr = "";

    public void setXml( String xml ) {
        this.xmlStr = xml;
    }

    public String getXml() {
        return xmlStr;
    }

}
