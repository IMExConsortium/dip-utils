package edu.ucla.mbi.util.struts.action;

/*==============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version:: $Rev::                                                            $
 *==============================================================================
 *
 * JsonContextConfigure Action:
 *
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.io.*;

import org.json.JSONObject;

import edu.ucla.mbi.util.JsonContext;

public abstract class JsonContextConfigSupport extends ManagerSupport {

    private Log log = LogFactory.getLog( JsonContextConfigSupport.class );
    
    private final String JSON = "json";

    //*** spring injection
    private JsonContext jsonContext;
    private String contextTop;
    private int contextDepth = 1;

    protected Map<String, Object> contextMap; 

    //*** setter
    public void setJsonContext( JsonContext context ) {
        this.jsonContext = context;
    }

    public void setContextTop( String top ) {
        contextTop = top;
    }

    public void setContextDepth( int depth ) {
        contextDepth = depth;
    }
 
    //*** getter    

    //*** used for json return 
    public Map<String, Object> getContextMap() {
        return contextMap;
    } 

    public String getContextTop() {
        return contextTop;
    }

    public int getContextDepth() {
        return contextDepth;
    }

    //---------------------------------------------------------------------

    public String execute() throws Exception {

        log.info( " JsonContextConfigAction execute..." );
        
        //*** read json file
        String jsonConfigFile =
            (String) jsonContext.getConfig().get( "json-config" );
        String srcPath = getServletContext().getRealPath( jsonConfigFile );

        try {
            jsonContext.readJsonConfigDef( srcPath );
        } catch ( Exception ex ) {
            throw ex;
        }
        
        contextMap = jsonContext.getJsonConfig();

        log.info( "execute: contextMap=" + contextMap );
        log.info( "contextDepth=" + contextDepth );
        
        if( getOp() == null ) {
            log.info( "execute: enter op=view.");
            return SUCCESS; 
        } 

        for( String opKey:getOp().keySet() ) {
            
            String opVal = getOp().get(opKey);
            
            log.info(  "op=" + opKey + "  and val=" + opVal );

            if ( opVal != null && opVal.length() > 0 ) {
                return operationAction ( opKey, opVal );
            } 
        }

        log.info( "execute: return fault.");

        return ERROR;
    }
  
    private String operationAction ( String opKey, String opVal ) 
        throws Exception {
        
        if( opKey.equals( "show" ) ) {
            log.info( "execute: op.show hit. " );
            return JSON;
        }

        String newKey = null;
        String newVal = null;

        String[] levelArrayT  = null;
        String[] levelArrayI = null;
        int levelDepth = 0;

        //*** fill levelArrayI using oppVal
        for( String oppKey:getOpp().keySet() ) {
            String oppVal = getOpp().get( oppKey ); 
            log.info( "oppKey=" + oppKey + " and oppVal=" + oppVal );

            if( oppKey.startsWith( "path" ) ) {
                levelArrayI = oppVal.split("\\|");
                levelDepth = levelArrayI.length;

                if( levelDepth > contextDepth ) {
                    log.warn( "opAction: opp(" + opKey + "=" + opVal + ") " +
                              "level should not be greater than contextDepth. " );
                    return ERROR; 
                }

                levelArrayT = new String[levelDepth]; 
                
                for( int i = 0; i < levelDepth; i++ ) {
                    if( levelArrayI[i].startsWith( "^" ) ) {
                        levelArrayT[i] = "l";
                        levelArrayI[i] = levelArrayI[i].substring( 1 ); //remove ^ char
                    } else {
                        levelArrayT[i] = "m";
                    }
                }

            } 
                 
            if ( oppKey.equals( "key" ) ) {
                newKey = oppVal;
            } 

            if ( oppKey.equals( "value" ) ) {
                newVal = oppVal;
            }
        }
        
        //*** validate levelArray
        boolean pathOk = false;
        for( int i = levelDepth; i > 0; i-- ) {
            if( pathOk ) {
                if( levelArrayT[i-1] == null || levelArrayI[i-1] == null ) {
                    pathOk = false;
                    break;
                }
            } else {
                if( levelArrayT[i-1] != null || levelArrayI[i-1] != null ) {
                    pathOk = true;
                }
            }
        }

        if( !pathOk && levelDepth > 0 ) {
            log.warn( "There is a wrong level path in the url request. " );
            return ERROR;
        }

        Object currentObj = contextMap.get( contextTop );
        boolean updateJson = false;

        /** Comment:
         * currentObj: not null collection to perform operation upon
         * opKey:  add|set|drop
         * opVal:  map|list|value 
         * opp.path format: keyOfMap|^indexOfList|keyOfMap|
         * newKey: opp.key 
         * newVal: opp.value
         **/

        for( int i = 0; i < levelDepth; i++ ) {
        
            if( levelArrayT[i].equals("m") ) {
                try {
                    currentObj = ( (Map)currentObj).get( levelArrayI[i] );
                } catch ( Exception ex ) {
                    log.warn( "The map level" + i + " =" + levelArrayI[i] +
                              " does not match with the json file. " );
                    return ERROR;
                }

                if( currentObj == null ) {
                    log.warn( "The map level" + i + " =" + levelArrayI[i] +
                              " does not match with the json file. " );
                    return ERROR;
                }
            }

            if( levelArrayT[i].equals("l") ) {
                try {
                    int index = Integer.valueOf( levelArrayI[i] );
                    currentObj = ((List)currentObj).get( index );
                } catch( Exception ex ) {
                    log.warn( "The list level" + i + " =" + levelArrayI[i] +
                              " does not match with the json file. " ); 
                    return ERROR;
                }
            }
        }
  
        if( opKey.equals("add") ) {

            Object nextObj = null;

            if( opVal.equals("map") ) {
                nextObj = new HashMap();
            }

            if( opVal.equals("list") ) {
                nextObj = new ArrayList();
            }
            
            if( currentObj instanceof Map ) {
                log.info( "currentObj is Map, and newKey=" + newKey );
                ((Map)currentObj).put( newKey, nextObj );
                log.info( "currentObj after put is " + currentObj );
                updateJson = true;
            } 
            
            if( currentObj instanceof List ) {
                log.info( "currentObj is List. " );
                if( newKey != null && newKey.matches("([0]|[1-9][0-9]*)" ) ) {
                    int index = Integer.valueOf( newKey );
                    for( int i=((List)currentObj).size(); i<=index; i++ ){
                        ((List)currentObj).add( JSONObject.NULL );
                    }
                    ((List)currentObj).set( index, nextObj );
                } else {
                    ((List)currentObj).add( nextObj ); // only add list at the end of the parent list
                }
                updateJson = true;
            } 
        } else {
            
            Object co = null;

            if( currentObj instanceof Map ) {
                co = ((Map)currentObj).get( newKey );                
            }

            if( currentObj instanceof List ) {
                int index = Integer.valueOf( newKey );
                log.info( "index=" + index + " and list size=" + 
                          ((List)currentObj).size() );
                if( index < ((List)currentObj).size() ) {
                    try {
                        co = ((List)currentObj).get( index );
                    } catch( Exception ex ) {
                        log.warn( "The newKey=" + newKey + " for the list " +
                                  "index does not match with the json file. " );
                        return ERROR;
                    }
                }
            }
            
            if( opKey.equals("set") ) {

                if( co == JSONObject.NULL || co == null 
                    || co instanceof String ) {

                    if( currentObj instanceof Map ) {
                        ((Map)currentObj).put( newKey, newVal );
                        updateJson = true;
                    }
            
                    if( currentObj instanceof List ) {
                        log.info( "currentObj is List. " );
                        try {
                            int index = Integer.valueOf( newKey );
                            for( int i=((List)currentObj).size();
                                 i<=index; i++ )  {

                                ((List)currentObj).add( JSONObject.NULL );
                            }
                            ((List)currentObj).set( index, newVal );
                            updateJson = true;
                        } catch( Exception ex ) {
                            log.warn( "The newKey=" + newKey + "for the list " +   
                                      "index does not match with the json file. " );
                            return ERROR;
                        }       
                    }
                }
            }
            
            if( opKey.equals("drop") ) {
                log.info( "op is drop. " );                
                if( ( co instanceof Map && !opVal.equals("map") )
                    ||( co instanceof List && !opVal.equals("list") ) ) {

                    log.warn( "The operation drop " + opVal + " does not " +
                              "match with the json file. " );

                    return ERROR;
                }

                if( currentObj instanceof Map ) {
                    ((Map)currentObj).remove( newKey );
                    updateJson = true;
                }

                if( currentObj instanceof List ) {
                    int index = Integer.valueOf( newKey );
                    log.info( "drop index=" + Integer.valueOf( newKey ) );
                    ((List)currentObj).remove( index );
                    log.info( "after remove index. " );
                    updateJson = true;
                }
            }
        }

        if( updateJson ) {
            saveJsonContext();
        } 
            
        return SUCCESS;
        
    }

    private void saveJsonContext() throws Exception {
            
        String jsonConfigFile = (String) jsonContext.getConfig()
            .get( "json-config" );
        
        log.info( "saveJsonContext: jsonConfigFile=" + jsonConfigFile );

        String srcPath =
            getServletContext().getRealPath( jsonConfigFile );
        
        log.info( "saveJsonContext:  srcPath=" + srcPath );
        
        try { 
            jsonContext.writeJsonConfigDef( srcPath  );
        } catch ( Exception ex ) {
            throw ex;         
        }

        log.info( "saveNativeServerConfigure: after writing to json file. " ); 
    }
        
}
    
