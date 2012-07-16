/*
 * Created on 26 janv. 2005
 */
package fr.itris.glips.rtda.components.picture;

import java.awt.*;
import java.io.*;
import org.apache.batik.bridge.*;
import org.apache.batik.swing.*;
import org.apache.batik.swing.svg.*;
import org.w3c.dom.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * the class of the canvas
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGCanvas extends JSVGCanvas {

	/**
	 * the svg picture
	 */
	private SVGPicture picture;
	
	/**
	 * the painters
	 */
	private CopyOnWriteArrayList<Painter> painters=new CopyOnWriteArrayList<Painter>();
	
    /**
     * the constructor of the class
     * @param picture the svg picture
     */
    public SVGCanvas(SVGPicture picture) {

		this.picture=picture;

		removeKeyListener(listener);
		removeMouseMotionListener(listener);
		removeMouseListener(listener);
		//userAgent=new SVGCanvasUserAgent(userAgent);

		//setting the properties for the canvas
		setBackground(Color.white);
		setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);
		doubleBufferedRendering=true;
		disableInteractions=true;
		selectableText=false;
    }

    /**
     * @return the svg picture
     */
    public SVGPicture getPicture() {
		return picture;
	}

	@Override
    public void paintComponent(Graphics g) {

    	super.paintComponent(g);
    	
    	for(Painter painter : new LinkedList<Painter>(painters)) {
    		
    		painter.paint((Graphics2D)g);
    	}
    }
    
	/**
	 * return the canvas' size
	 * @param doc the canvas document
	 * @return the canvas' size
	 */
	public Dimension getCanvasSize(Document doc){
		
		if(doc!=null){
		    
			Element root=doc.getDocumentElement();
										
			if(root!=null){

				int w=(int)getPixelledNumber(root.getAttributeNS(null, "width"));
				int h=(int)getPixelledNumber(root.getAttributeNS(null, "height"));
				
				return new Dimension(w, h);
			}
		}
		
		return new Dimension(0,0);
	}

	@Override
	public String getURI() {
		return uri;
	}
	
	@Override
	public void setURI(String uri) {
		
		this.uri=uri;
	}
	
    /**
     * @return Returns the projectFile.
     */
    public File getProjectFile() {

        return picture.getAnimActionsHandler().getProjectFile();
    }
    
    /**
     * @return the project name
     */
    public String getProjectName() {

    	return picture.getAnimActionsHandler().getProjectName();
    }
    
    /**
     * adding new painters to the canvas
     * @param newPainters a list of painters
     */
    public void addPainters(Collection<Painter> newPainters) {
    	
    	painters.addAll(newPainters);
    }
    
    /**
     * sets the new painter
	 * @param painter the new painter
	 */
	public void addPainter(Painter painter) {
		
		painters.add(painter);
	}
	
	/**
	 * clears the painter list
	 */
	public void clearPainters() {
		
		painters.clear();
	}
	
	@Override
	public void dispose() {

		super.dispose();
	}
	
	/**
	 * computed the number corresponding to this string in pixel
	 * @param str
	 * @return the number corresponding to this string in pixel
	 */
	public double getPixelledNumber(String str){
	    
	    double i=0;
	    
	    if(str!=null && ! str.equals("")){
	        
	        if(! Character.isDigit(str.charAt(str.length()-1))){
	            
	            String unit=str.substring(str.length()-2, str.length());
	            String nb=str.substring(0, str.length()-2);
	            
	            try{
	                i=Double.parseDouble(nb);
	            }catch (Exception ex){}
	            
	            if(unit.equals("cm")){
	                
	                i=i*28.340080972;

	            }else if(unit.equals("mm")){
	                
	                i=i*2.830188679;
	            }
	                
	        }else{
	            
	            try{
	                i=Double.parseDouble(str);
	            }catch (Exception ex){}
	        }
	    }
	  
	    return i;
	}
	
	@Override
	protected void installKeyboardActions() {}

	 /**
	  * @author ITRIS, Jordi SUC
	  * the class defining  a modified implementation of a user agent
	  */
	 protected class SVGCanvasUserAgent extends JSVGComponent.BridgeUserAgentWrapper{
		
		 /**
		  * the constructor of the class
		  * @param userAgent the user agent
		  */
		 protected  SVGCanvasUserAgent(UserAgent userAgent){
		     
			 super(userAgent);
			 SVGCanvas.this.userAgent=userAgent;
		 }
		
			@Override
		 public void setSVGCursor(Cursor cursor) {
				
		 }
	 }
}
