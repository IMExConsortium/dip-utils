package edu.ucla.mbi.util.graphviz;

/* =========================================================================
 # $HeadURL:: https://lukasz@imex.mbi.ucla.edu/svn/dip-ws/trunk/dip-portal#$
 # $Id:: DipRole.java 441 2009-08-16 16:42:04Z lukasz                      $
 # Version: $Rev:: 441                                                     $
 #==========================================================================
 #
 #  GraphViz - Java interface to http://www.graphviz.org/ 
 #    Loosely based on 
 #        http://www.loria.fr/~szathmar/off/projects/java/GraphVizAPI
 #     
 #======================================================================= */

import java.io.*;

public class GraphViz {

    private static String tempDir = "";
    private static String dotBin = "";	

    private StringBuilder graph = new StringBuilder();
    
    public GraphViz() {}
    
    public static void setTempDir( String dir ){
        tempDir = dir;
    }

    public static void setDotBin( String dot ){
        dotBin = dot;
    }

    public byte[] getGraph( String src, String layout, String type ){
        File dot = null;
        try{
            dot = writeSrcToFile( src );
            if( dot != null ){
                byte[] image = getImage( dot, layout, type );
                return image;
            }
        } catch( Exception e){
            e.printStackTrace();
        } finally{
            try{
                if( dot!= null){ 
                    dot.delete();
                }
            } catch( Exception ee){
                
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------
    
    private byte[] getImage( File dot, String layout, String type ) 
        throws IOException, InterruptedException{
        
        File iFile = File.createTempFile( "graph_", "." + type, 
                                          new File( GraphViz.tempDir ) );
        Runtime rt = Runtime.getRuntime();
        
        String[] args = { dotBin, 
                          "-K" + layout,
                          "-T" + type, dot.getAbsolutePath(), 
                          "-o", iFile.getAbsolutePath() };
        Process p = rt.exec( args );
        p.waitFor();
        
        FileInputStream in = new FileInputStream( iFile.getAbsolutePath() );
        byte[] iData = new byte[ in.available() ];
        in.read( iData );
        if( in != null ) in.close();
        iFile.delete();
        
        return iData;
    }
    
    //--------------------------------------------------------------------------
    
    private File writeSrcToFile( String str ) throws java.io.IOException{
        
        System.out.println( "GraphViz.tempDir=" + GraphViz.tempDir );

        File temp  = File.createTempFile( "graph_", ".dot.tmp", 
                                          new File( GraphViz.tempDir ) );
        System.out.println("tmp="+ temp.getAbsolutePath());
        
        FileWriter fout = new FileWriter( temp );
        fout.write( str );
        fout.close();


        return temp;
    }      
}
