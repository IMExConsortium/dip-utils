package edu.ucla.mbi.orm;

/*==============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *
 * HibernateOrmUtil interface: 
 *
 *=========================================================================== */

import org.hibernate.*;
import org.compass.core.*;

public interface HibernateOrmUtil {
    Session getCurrentSession();
    CompassSession getCompassSession();
}
