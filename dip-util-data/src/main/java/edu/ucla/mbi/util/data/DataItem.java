package edu.ucla.mbi.util.data;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *
 * DataItem - a traceable unit of data
 *
 *
 ============================================================================ */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

import edu.ucla.mbi.util.*;

public abstract class DataItem implements DataAclAware {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    //--------------------------------------------------------------------------

    User owner;
    
    public User getOwner() {
        return owner;
    }

    public void setOwner( User owner ) {
        this.owner = owner;
    }

    //--------------------------------------------------------------------------

    private GregorianCalendar creationTime;

    public void setCrt( GregorianCalendar  crt) {
        this.creationTime = crt;
    }

    public GregorianCalendar getCrt() {
        return this.creationTime;
    }

    public String getCreateDateString(){
        if( creationTime == null ) {
            return "----/--/--";
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append( creationTime.get(Calendar.YEAR) );
        sb.append( "/");

        sb.append( creationTime
                   .get( Calendar.MONTH ) < 9 ? "0" : "" ); 
        sb.append( creationTime.get( Calendar.MONTH ) + 1);
        sb.append( "/" );

        sb.append( creationTime
                   .get( Calendar.DAY_OF_MONTH ) < 10 ? "0" : "" );
        sb.append( creationTime.get( Calendar.DAY_OF_MONTH) );
        
        return sb.toString();
    }
    
    public String getCreateTimeString(){

        if( creationTime == null ) {
            return "--:--:--";
        } 

        StringBuffer sb = new StringBuffer();
        sb.append( creationTime
                   .get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "" );
        sb.append( creationTime.get(Calendar.HOUR_OF_DAY) );
        sb.append( ":");
        
        sb.append( creationTime
                   .get(Calendar.MINUTE) < 10 ? "0" : "" );
        sb.append( creationTime.get(Calendar.MINUTE) );
        sb.append( ":");
        
        sb.append( creationTime
                   .get(Calendar.SECOND) < 10 ? "0" : "" );
        sb.append( creationTime.get(Calendar.SECOND) );
        return sb.toString();
    }

    //--------------------------------------------------------------------------

    Set<User>  adminUsrSet;

    public Set<User> getAdminUsers() {
        if ( adminUsrSet == null ) {
            adminUsrSet = new TreeSet<User>();
        }
        return adminUsrSet;
    }

    public void setAdminUsers( Set<User> users ) {
        this.adminUsrSet = users;
    }

    public Set<String> getAdminUserNames(){
        
        Set<String> aul = new HashSet<String>();

        if( adminUsrSet != null ){
            for( Iterator<User> iu = adminUsrSet.iterator();
                     iu.hasNext(); ) {
                    aul.add( iu.next().getLogin() );
                }
            }
        return aul;
    }

    //--------------------------------------------------------------------------

    private Set<Group> adminGroupSet;

    public Set<Group> getAdminGroups() {
        if ( adminGroupSet == null ) {
            adminGroupSet = new TreeSet<Group>();
        }
        return adminGroupSet;
    }

    public void setAdminGroups( Set<Group> groups ) {
        this.adminGroupSet = groups;
    }

    public Set<String> getAdminGroupNames(){

        Set<String> agl = new HashSet<String>();

        if( adminGroupSet != null ){
            for( Iterator<Group> ig = adminGroupSet.iterator();
                 ig.hasNext(); ) {
                agl.add( ig.next().getName() );
            }
        }
        return agl;
    }

    //--------------------------------------------------------------------------

    DataState state;
    
    public DataState getState() {
        return state;
    }

    public void setState( DataState state ) {
        this.state = state;
    }

    //--------------------------------------------------------------------------

    boolean hidden = false;
    
    public boolean getHidden() {
        return hidden;
    }

    public boolean isHidden() {
        return hidden;
    }
    
    public void setHidden( boolean hidden ) {
        this.hidden = hidden;
    }

    //---------------------------------------------------------------------
    // DataAclAware
    //-------------
    
    public boolean testAcl( Set<String> ownerMatch, 
                            Set<String> adminUserMatch, 
                            Set<String> adminGroupMatch ) {
        try{
            Log log = LogFactory.getLog( this.getClass() );
            log.info( "ACL Test: owner= " + ownerMatch +
                      "\n           ausr= " + adminUserMatch +
                      "\n           agrp= " + adminGroupMatch );
            
            if ( ownerMatch == null && adminUserMatch == null && 
                 adminGroupMatch == null ) return true;
            
            // owner match
            //------------
            
            if ( ownerMatch != null ) {
                if ( ownerMatch.contains( getOwner().getLogin() ) ) {
                    log.info( "ACL Test: owner matched" );
                    return true;
                }
            }
            
            log.info( "ACL Test: no owner match");

            // admin user match
            //-----------------
            
            if ( adminUserMatch != null ) {
                for( Iterator<User> oi = getAdminUsers().iterator();
                     oi.hasNext(); ) {
                    
                    String usr = oi.next().getLogin();
                    if ( adminUserMatch.contains( usr.getLogin() ) ) {
                        log.info( "ACL Test: ausr matched" );
                        return true;
                    }
                }
            }
            log.info( "ACL Test: no ausr match");
            
            // admin group match
            //------------------

            if ( adminGroupMatch != null ) {
                
                for( Iterator<Group> gi = getAdminGroups().iterator();
                     gi.hasNext(); ) {
                    
                    String grp = gi.next().getLabel();
                    if ( adminGroupMatch.contains( grp ) ) {
                        log.info( "ACL Test: agrp matched" );
                        return true;
                    }
                }
            }
            
            log.info( "ACL Test: no agrp match");
            return false;
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //-------------------------------------------------------------------------------
    // equality
    //---------

    public int hashCode() {
        if( id != null ) {
            return id.intValue();
        } else {
            return 0;
        }
    }
    
    public boolean equals( Object obj ) {

        if( obj.getClass() != this.getClass() ){
            return false;
        }
        
        if( this.getId() == null || ((DataItem) obj).getId() == null ) {
                return false;
        }
        
        if( this.getId().intValue() == ((DataItem)obj).getId().intValue() ) {
            return true;
        }
        return false;
    }
}
