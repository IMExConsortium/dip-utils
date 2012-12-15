package edu.ucla.mbi.util.struts.action;

/* =============================================================================
 * $HeadURL::                                                                  $
 * $Id::                                                                       $
 * Version: $Rev::                                                             $
 *==============================================================================
 *                                                                             $
 * ImageMgrSupport - image handling: upload/remove/etc                         $
 *                                                                             $
 *                                                                             $
 ============================================================================ */

import com.opensymphony.xwork2.ActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.PatternSyntaxException;

import java.io.*;
import org.json.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.struts.action.*;
import edu.ucla.mbi.util.struts.interceptor.*;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import javax.servlet.ServletContext;

public abstract class ImageMgrSupport extends ManagerSupport {

    private static final int BUFFER_SIZE = 4096;

    public static final String STORE ="STORE";
    public static final String PREVIEW ="preview";

    public static final String PAGE ="page";

    // configuration
    //--------------

    Map<String,Object> portalCnf;
    
    
    //--------------------------------------------------------------------------
    // operations
    //-----------

    public static final String IMAGE_STORE = "upload";
    public static final String IMAGE_DROP = "drop";
    public static final String IMAGE_VIEW = "view";

    
    //--------------------------------------------------------------------------
    // current page 
    //-------------
    
    private String pageId;

    public void setPageid( String pageId ) {
        this.pageId = pageId;
    }

    public String getPageid() {
        return pageId;
    }

    //--------------------------------------------------------------------------
    // Image Map
    //----------

    private Map<String,Object> imageMap;

    public Map<String,Object> getImageMap(){
        
        if( imageMap == null ){
            imageMap = new HashMap<String,Object>();
        }
        return imageMap;
    } 

    public void setImageMap( Map<String,Object> map ){
        this.imageMap = map;
    }

    //--------------------------------------------------------------------------
    // Image URL
    //----------

    String imageUrl = "";

    public String getImageUrl(){
        return this.imageUrl;
    }

    public void setImageUrl( String url ){
        this.imageUrl = url;
    }
    
    //--------------------------------------------------------------------------
    
    private File imageFile;
    private String ifContentType;
    private String imageFilename;

    public void setUpload( File file ){
        this.imageFile = file;
    }
    
    public void setUploadContentType( String contentType ){
        this.ifContentType = contentType;
    }
    
    public void setUploadFileName( String filename ){
        this.imageFilename = filename;
    }

    //--------------------------------------------------------------------------
    // initialization
    //---------------

    private void initialize(){
        
    }
    
    //--------------------------------------------------------------------------

    public String execute() throws Exception {

        initialize();
        
        // decode operation
        //-----------------

        String opcode = null;

        if( getOp() != null ) {
            
            for( Iterator<String> ii = getOp().keySet().iterator();
                 ii.hasNext(); ) {
                String key = ii.next();
                if ( key.equals( IMAGE_STORE ) ) {     
                    opcode = "img_store";
                    return imageStore();
                }

                if ( key.equals( IMAGE_DROP ) ) {     
                    opcode = "img_drop";
                    return imageDrop();
                }
                
                if ( key.equals( IMAGE_VIEW ) ) {     
                    opcode = "img_view";
                    return imageView();
                }
            }
        }
        buildImageMap();
        return "imap";
    }

    //--------------------------------------------------------------------------

    private String imageStore(){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "ImageSupport: imageStore" );

        if ( getOpp() == null ) return SUCCESS;

        String name = getOpp().get( "name" );
        if( name == null ) {
            name = "";
        } else {
            name = sanitizeFileName( name );
        }
        
        String portal = getOpp().get( "portal" );
        String skin = getOpp().get( "skin" );

        log.info( "pageid=" + getPageid() );
        log.info( "name=" + name + " portal=" + portal + " skin=" + skin);
        log.info( "  file=" + imageFile 
                  + " ifContentType=" + ifContentType 
                  + " imageFilename=" + imageFilename );
        
        if( ifContentType == null 
            || ( !ifContentType.equals("image/png") && 
                 !ifContentType.equals("image/gif") && 
                 !ifContentType.equals("image/jpeg") ) ) {
            return PAGE;                 // wrong file type: SKIP
        }

        if( name == null || name.equals("") ){
            name = sanitizeFileName( imageFilename );
        }
        if( name.equals("") ){
            return PAGE;                // wrong/empty file name: SKIP
        }

        if( ifContentType.equals("image/png") ){
            name = name + ".png";
        }

        if( ifContentType.equals("image/jpeg") ){
            name = name + ".jpg";
        }

        if( ifContentType.equals("image/gif") ){
            name = name + ".gif";
        }


        //----------------------------------------------------------------------
        // target file path
        //-----------------

        String imagePath = getPortalContext().getPortalImagePath();
        String skinPath = getPortalContext().getPortalSkinPath();
        String path = imagePath;
        
        if( portal != null && portal.equalsIgnoreCase( "true" ) ){
            // portal-wide
        }  else {
            path = "/" + getSite() + path;
        }
        
        if( skin != null && skin.equalsIgnoreCase( "true" ) ){
            if( portal != null && portal.equalsIgnoreCase( "true" ) ){
                
                // global/shared skin images
                //--------------------------
                
                path = skinPath + imagePath;
                
            } else {
                
                // specific skin images
                //---------------------
                
                path = skinPath + "/" + getSkn() + imagePath;
                
            }
        } else {
            if( portal != null && portal.equalsIgnoreCase( "true" ) ){
                
                // global image
                //-------------
                
                path = imagePath;

            } else {
                
                // site specific
                //--------------

                path = "/" + getSite() + imagePath; 

            }
        }
        
        String realPath =
            getServletContext().getRealPath( path );

        String realImagePath =
            getServletContext().getRealPath( path + "/" + name );
        
        log.info( "REALPATH=" + realImagePath);

        //----------------------------------------------------------------------
        // test if directory exists
        //-------------------------

        File pathFile = new File( realPath );
        boolean pathOK = false;

        try{
            pathOK = pathFile.isDirectory();
            if( !pathOK &&  !pathFile.exists() ){
                pathOK = pathFile.mkdirs();
            }
        } catch( SecurityException se ){
            log.info( "EXCEPTION: realPath(" 
                      + realPath + ") not accessible" );
        }
        
        if( !pathOK ){
            return PAGE; // skip
        }
        
        //----------------------------------------------------------------------
        // test if file exists
        //--------------------

        File pathImageFile = new File( realImagePath );
        boolean pathImageOK = false;
        
        try{
            pathImageOK = ! pathImageFile.exists();
            
            if( !pathImageOK ){
                log.info( "EXCEPTION: realImagePath("
                          + realImagePath + ") exists" );
            }
        } catch( SecurityException se ){
            log.info( "EXCEPTION: realImagePath("
                      + realImagePath + ") not accessible" );
        }

        if( !pathImageOK ){
            return PAGE; // skip
        }
        
        //----------------------------------------------------------------------
        // read/save
        //----------

        try{
            FileInputStream inFile = new FileInputStream( imageFile );
            
            FileOutputStream outFile = new FileOutputStream( pathImageFile );
            
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;

            while( (len = inFile.read( buffer )) > 0 ){
                outFile.write( buffer, 0, len ); 
            }

            inFile.close();
            outFile.close();

        } catch( IOException e ){
            // ignore for now
        }
        return PAGE;      
    }

    //--------------------------------------------------------------------------

    private String imageDrop(){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "ImageSupport: imageDrop" );

        if ( getOpp() == null ) return SUCCESS;

        String name = getOpp().get( "name" );
        String portal = getOpp().get( "portal" );
        String skin = getOpp().get( "skin" );
        
        log.info( "name=" + name + " portal=" + portal + " skin=" + skin);

        return PAGE;      
    }

    //--------------------------------------------------------------------------

    private String imageView(){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "ImageSupport: imageView" );

        if ( getOpp() == null ) return SUCCESS;

        String name = getOpp().get( "name" );
        log.info( "name=" + name );
        this.setImageUrl( name );
        return PREVIEW;      
    }

    //--------------------------------------------------------------------------

    private void buildImageMap(){
        imageMap = new HashMap<String,Object>();

        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "ImageSupport: buildImageMap" );

        // portal images
        //--------------
        
        getImageMap().put( "portal", new TreeMap<String,Object>() );
        String imagePath = getPortalContext().getPortalImagePath();
        
        if( imagePath != null ) {
            String realImagePath = 
                getServletContext().getRealPath( imagePath );
            
            log.debug( "ImageSupport: portal images = " 
                       + realImagePath );
            
            // get portal images
            //------------------

            File dir = new File( realImagePath );
            
            if( dir.exists()  && dir.isDirectory() ) {
                loadImageInfo( (Map) getImageMap().get( "portal" ), 
                               imagePath, dir );
            }
        } else {
            imagePath = "";
        }
        
        // skin images
        //------------
        
        getImageMap().put("skin", new HashMap<String,Object>());
        String skinPath = getPortalContext().getPortalSkinPath();
        
        String skinImagePath = null;
        
        if( skinPath != null ) {
            skinImagePath = skinPath + imagePath;
            
            String realSkinImagePath =
                getServletContext().getRealPath( skinImagePath );
            
            log.debug( "ImageSupport: skin images = " 
                       + realSkinImagePath );
            
            // get skin-global images
            //-----------------------

            File dir = new File( realSkinImagePath );
            if( dir.exists()  && dir.isDirectory() ) {
                loadImageInfo( (Map) getImageMap().get( "skin" ), 
                               skinImagePath, dir );
            }
        }
        
        // skin-current images
        //--------------------
        
        if( skinPath != null && getSkn() != null && getSkn().length() > 0 ){
            
            getImageMap().put( "skin-current", new HashMap<String,Object>());
            String skinCurrentImagePath 
                = getPortalContext().getPortalSkinPath() 
                + "/" + getSkn() + imagePath;

            String realSkinCurrentImagePath =
                getServletContext().getRealPath( skinCurrentImagePath );

            log.debug( "ImageSupport: skin-current images = "
                       + realSkinCurrentImagePath );

            // get skin-global images
            //-----------------------

            File dir = new File( realSkinCurrentImagePath );
            if( dir.exists()  && dir.isDirectory() ) {
                loadImageInfo( (Map) getImageMap().get( "skin-current" ), 
                               skinCurrentImagePath, dir );
            }
        }

        // site-current images
        //--------------------
        
        getImageMap().put( "site-current", new HashMap<String,Object>());
        
        if( getSite() != null && getSite().length() > 0 ){
            
            String siteImagePath = "/" + getSite() + imagePath;
            
            String realSiteImagePath =
                getServletContext().getRealPath( siteImagePath );

            log.debug( "ImageSupport: site-current images = "
                       + realSiteImagePath );

            // get skin-global images
            //-----------------------
            
            File dir = new File( realSiteImagePath );
            if( dir.exists()  && dir.isDirectory() ) {
                loadImageInfo( (Map) getImageMap().get( "site-current" ), 
                               siteImagePath, dir );
            }
        }
        
    }


    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    private void loadImageInfo( Map map, String path, File dir ){
    
        String [] files = dir.list();
        List<Object> ilist = new ArrayList<Object>();
        map.put( "path", path );
        map.put( "images", ilist );
        
        if( files != null && files.length > 0 ){
            
            Map<String,Object> imap = new TreeMap<String,Object>();
            for( int i=0; i<files.length; i++ ){
                String fun = files[i].toUpperCase();
                        
                if( fun.endsWith(".JPG") || fun.endsWith(".GIF") 
                    || fun.endsWith(".PNG") ){

                    Map<String,Object> cmap = new HashMap<String,Object>();
                    cmap.put( "name", files[i] );
                
                    imap.put( files[i], cmap );
                }
            }
            
            for( Iterator<String> i = imap.keySet().iterator(); i.hasNext(); ){
                ilist.add( imap.get( i.next() ) );
            }
        }
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------

    private String sanitizeFileName( String name ){

        if( name == null ) return "";
        
        try{
            name = name
                .replaceAll( "[^0-9a-zA-Z._-]", "" );    // non alpha-numerics
            name = name
                .replaceAll( "^\\.", "" );               // leading dot      
            name = name
                .replaceAll( "\\.[0-9a-zA-Z_-]+$", "" ); // extension
        } catch( PatternSyntaxException pse ){
            // should not happen
        }
        return name;
    }
}
