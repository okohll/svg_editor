package fr.itris.glips.view;

import java.awt.*;
import javax.swing.*;

/**
 * the class of the applet for GLIPS View
 * @author Jordi SUC
 */
public class ViewApplet extends JApplet{
	
	/**
	 * the name of the parameter used to specify whether to show the menu bar
	 */
	private static final String showMenubarParam="showMenuBar";
	
	/**
	 * the name of the parameter used to specify the 
	 * uri of the file to be loaded at startup
	 */
	private static final String fileURIParam="fileURI";
	
	/**
	 * the svg view
	 */
	private View svgView;
	
    /**
     * the regular frame
     */
    private Frame regularFrame;
	
	@Override
	public void init() {

	    try {
	        SwingUtilities.invokeAndWait(new Runnable() {
	        	
	            public void run() {
	            	
	                createGUI();
	            }
	        });
	    } catch (Exception e) {}
	}

	/**
	 * creates the GUI
	 */
	private void createGUI() {
		
		//getting the parameters
		boolean showMenuBar=false;
		
		try{
			showMenuBar=Boolean.parseBoolean(
				getParameter(showMenubarParam));
		}catch (Exception ex){}
		
		String fileURI=getParameter(fileURIParam);

		//getting the main frame
		regularFrame=
			(Frame)SwingUtilities.getAncestorOfClass(Frame.class, this);
		
		//creating the view manager
    	svgView=new View(regularFrame, this, false, false, false);
    	
    	//creating the menu bar
    	if(showMenuBar){
    		
    		setJMenuBar(svgView.buildMenubar());
    	}
        
    	//building the content pane
        getContentPane().setLayout(new BoxLayout(
        		getContentPane(), BoxLayout.Y_AXIS));
        
        getContentPane().add(svgView.getCanvasContainer());
        getContentPane().doLayout();
        
        //setting the initial svg file
        if(! fileURI.equals("")) {
        	
        	if(fileURI.startsWith("/")){
        		
        		fileURI=fileURI.substring(1, fileURI.length());
        	}
        	
        	if(fileURI.indexOf(":")==-1){
        		
        		try{
        			fileURI=getCodeBase().toURI().toASCIIString()+fileURI;
        		}catch (Exception ex){}
        	}
        	
            svgView.getMainDisplay().getMainViewBrowser().getPictureLoader().
            	setCurrentPicture(fileURI, true);
        }
	}
	
	@Override
	public void destroy() {

		super.destroy();
	}
}
