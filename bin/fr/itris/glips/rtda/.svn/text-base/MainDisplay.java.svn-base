package fr.itris.glips.rtda;

import java.awt.*;
import fr.itris.glips.rtda.colorsblinkings.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.components.viewbrowser.*;
import fr.itris.glips.rtda.config.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the manager of the animations
 * 
 * @author ITRIS, Jordi SUC
 */
public class MainDisplay {
	
	/**
	 * the main display toolkit
	 */
	protected MainDisplayToolkit mainDisplayToolkit;
	
    /**
     * whether the class is created in a test version or in a runtime version
     */
    protected boolean isTestVersion=false;
    
    /**
     * the animations handler
     */
    protected AnimationsHandler animationsHandler;
    
    /**
     * the main view browser
     */
    protected ViewBrowser mainViewBrowser;
    
    /**
     * the colors and blinkings toolkit
     */
    protected ColorsAndBlinkingsToolkit colorsAndBlinkingsToolkit;
    
    /**
     * the view browser manager
     */
    protected ViewBrowsersManager viewBrowsersManager;
    
    /**
     * the svg picture manager
     */
    protected SVGPictureManager svgPictureManager;
    
    /**
     * the jwidget runtime manager
     */
    protected JWidgetRuntimeManager jwidgetRuntimeManager;
    
    /**
     * the configuration document
     */
    protected ConfigurationDocument configurationDocument;
    
    /**
     * the object handling the user rights
     */
    protected UserRights userRights;
    
    /**
     * the constructor of the class
     * @param mainDisplayToolkit the main display toolkit
     * @param isTestVersion whether the currently running version is a test version
     * @param isDebugMode whether we are in debug mode
     */
    public MainDisplay(MainDisplayToolkit mainDisplayToolkit, 
    		boolean isTestVersion, boolean isDebugMode) {

    	this.mainDisplayToolkit=mainDisplayToolkit;
    	this.isTestVersion=isTestVersion;
    	animationsHandler=new AnimationsHandler(this, isTestVersion, isDebugMode);
    	colorsAndBlinkingsToolkit=new ColorsAndBlinkingsToolkit(this);
    	viewBrowsersManager=new ViewBrowsersManager(this);
    	jwidgetRuntimeManager=new JWidgetRuntimeManager();
    	svgPictureManager=new SVGPictureManager();
    	userRights=new UserRights(this);
        
        //creating the main view browser
        mainViewBrowser=new ViewBrowser(this, "", false);
        
        //adds a listener to the requests of the display of views
        animationsHandler.addRequireViewDisplayListener(
        	new RequireViewDisplayListener(){

        	@Override
        	public void displayView(String id, String uri) {

        		//getting the view browser corresponding to the given id
        		ViewBrowser viewBrowser=viewBrowsersManager.getViewBrowser(id);

        		if(viewBrowser!=null) {
        			
        			viewBrowser.getPictureLoader().setCurrentPicture(uri, false);
        		}
        	}
        	
        	@Override
        	public void displayPreviousView(String id) {

        		//getting the view browser corresponding to the given id
        		ViewBrowser viewBrowser=viewBrowsersManager.getViewBrowser(id);

        		if(viewBrowser!=null) {
        			
        			viewBrowser.getPictureLoader().returnToPreviousPicture();
        		}
        	}

        	@Override
			public void quitProgram() {

				quit();				
			}
        });
    }
    
    /**
     * @return the animations handler
     */
    public AnimationsHandler getAnimationsHandler(){
    	
    	return animationsHandler;
    }
    
    /**
     * @return the colors and blinkings toolkit
     */
    public ColorsAndBlinkingsToolkit getColorsAndBlinkingsToolkit() {
		return colorsAndBlinkingsToolkit;
	}
    
    /**
     * @return the view browser manager
     */
    public ViewBrowsersManager getViewBrowsersManager(){
    	
    	return viewBrowsersManager;
    }
    
    /**
     * @return the svg picture manager
     */
    public SVGPictureManager getPictureManager(){
    	
    	return svgPictureManager;
    }
    
    /**
	 * @return the main view browser
	 */
	public ViewBrowser getMainViewBrowser() {
		return mainViewBrowser;
	}
	
	/**
	 * @return the jwidget runtime manager
	 */
	public JWidgetRuntimeManager getJwidgetRuntimeManager() {
		return jwidgetRuntimeManager;
	}
    
    /**
     * the method used when the user wants to quit the program
     */
    public void quit() {
    	
    	mainDisplayToolkit.quitProgram();
    }
    
    /**
     * refreshes some elements
     * @param currentPicture the current picture
     */
    public void refresh(SVGPicture currentPicture) {
    	
    	mainDisplayToolkit.refresh(currentPicture);
    	userRights.setCurrentPicture(currentPicture);
    }
    
    /**
     * @return the top level frame of this application
     */
    public Frame getTopLevelFrame() {
    	
    	return mainDisplayToolkit.getTopLevelFrame();
    }
    
    /**
     * reinitializes the main display
     */
    public void reinitialize(){

    	userRights.reinitialize();
    	configurationDocument=null;
    }

    /**
     * @return Returns the isTestVersion.
     */
    public boolean isTestVersion() {
    	
        return isTestVersion;
    }
    
    /**
     * @return the manager of the user rights
     */
    public UserRights getUserRights() {
		return userRights;
	}
}
