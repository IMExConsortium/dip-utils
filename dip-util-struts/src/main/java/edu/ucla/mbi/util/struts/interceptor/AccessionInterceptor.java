package edu.ucla.mbi.util.struts.interceptor;

/* =========================================================================
 # $HeadURL::                                                              $
 # $Id::                                                                   $
 # Version: $Rev::                                                         $
 #==========================================================================
 #
 # AccessionInterceptor: prepreocesses namespace/accession pair
 #         
 #
 #======================================================================= */

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AccessionInterceptor implements Interceptor{

    public void destroy() { }

    public void init() { }

    public String intercept( ActionInvocation invocation ) 
	throws Exception {
	
	Log log = LogFactory.getLog( AccessionInterceptor.class );
	
	Class self = invocation.getAction().getClass();
	Class[] aint = self.getInterfaces();
	boolean skip = true;
	int cint = 0;
	
	while( skip && cint < aint.length ) {
	    Class cc = aint[cint];
	    if( cc.getCanonicalName()
                .equals( "edu.ucla.mbi.util.struts2.interceptor.AccessionAware" ) 
                ) {
		skip = false;
	    }
	    cint++;
	}

	if ( !skip ) {
	
	    AccessionAware action = (AccessionAware) invocation.getAction();
	    String namespace = action.getNs();
	    String accession = action.getAc();
	
	    // process
	    
	    log.info( "AccessionInterceptor: ns=" + namespace + " ac=" + accession );

	    if( accession != null && accession.length() > 0 ) {

		accession = accession.replaceAll( "\\s+", "" );
		
		if ( accession.matches( "^\\d+[NEXS]$" ) ) {
		    accession = "DIP-" + accession; 
		    if (namespace == null || namespace.length() == 0){
			namespace = "dip";
		    }
		} else if ( accession.toUpperCase().matches( "^DIP-\\d+[NEXS]$" ) ) {
		    accession = accession.toUpperCase();
		    if ( namespace == null || namespace.length() == 0){
			namespace = "dip";
		    }
		}
		
		if ( namespace == null || namespace.length() == 0 ) {
		    if ( accession.toLowerCase().startsWith( "pmid:" ) ) {
			accession = accession.toLowerCase().replaceFirst( "pmid:", "" );
			namespace = "pmid";
		    } else if ( accession.toLowerCase().startsWith( "uniprot:" ) ) {
			accession = accession.toLowerCase().replaceFirst( "uniprot:", "" );
			namespace = "uniprot";
		    } else if ( accession.toLowerCase().startsWith( "refseq:" ) ) {
			accession = accession.toLowerCase().replaceFirst( "refseq:", "" );
			namespace = "refseq";
		    }
		}
	    }

	    // done
	
	    action.setNs( namespace );
	    action.setAc( accession );
	    
	}
	return invocation.invoke();       
    }
}
