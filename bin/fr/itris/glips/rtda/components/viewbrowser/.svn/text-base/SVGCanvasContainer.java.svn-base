/*
 * Created on 26 janv. 2005
 */
package fr.itris.glips.rtda.components.viewbrowser;

import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class displaying a svg picture
 * @author ITRIS, Jordi SUC
 */
public class SVGCanvasContainer extends JPanel{
    
	/**
	 * the view browser linked to the canvas container
	 */
	private ViewBrowser viewBrowser;
	
    /**
     * the current picture
     */
    private SVGPicture currentPicture;
    
    /**
     * the constructor of the class
     * @param viewBrowser the current view browser
     */
    public SVGCanvasContainer(ViewBrowser viewBrowser){
    	
    	this.viewBrowser=viewBrowser;
    }
    
    /**
     * sets a new picture
     * @param picture a picture
     */
    public void setPicture(SVGPicture picture){

    	removeAll();
    	
        //removing the last picture
        if(currentPicture!=null){

            currentPicture.getAnimActionsHandler().unregistersAnimationsActions();
            currentPicture.getCanvas().setVisible(false);
            currentPicture.setDisplayed(false);
            currentPicture=null;
        }
        
        if(picture!=null){
        	
            currentPicture=picture;
            currentPicture.setCurrentViewBrowser(viewBrowser);
            currentPicture.setDisplayed(true);

        	if(picture.getPictureComponent()!=null && 
        			picture.getPictureComponent().getParent()!=null) {
        		
    			picture.getPictureComponent().getParent().remove(picture.getPictureComponent());
        		picture.getAnimActionsHandler().unregistersAnimationsActions();
        	}
        	
        	boolean rendered=false;
        	
        	if(! picture.isRendered()){

        		picture.getPictureComponent().renderCanvas();
        		rendered=true;
        	}
            
           //adding the new picture
        	JScrollPane scrollPane=new JScrollPane(picture.getPictureComponent());
            scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
            scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
            add(scrollPane);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            
        	if(! rendered){

        		picture.getPictureComponent().renderCanvas();
        	}
        }

        revalidate();
        repaint();
    }

	/**
	 * @return the currentPicture
	 */
	public SVGPicture getCurrentPicture() {
		return currentPicture;
	}
}
