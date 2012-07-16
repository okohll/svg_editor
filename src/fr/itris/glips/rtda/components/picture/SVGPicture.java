/*
 * Created on 15 avr. 2005
 */
package fr.itris.glips.rtda.components.picture;

import java.awt.*;
import org.apache.batik.util.*;
import org.w3c.dom.svg.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.components.viewbrowser.*;
import javax.swing.*;

/**
 * the class of the object that will display a svg document
 * @author ITRIS, Jordi SUC
 */
public class SVGPicture {
	
	/**
	 * the listener to the animations state
	 */
	private AnimationsStateListener animationsStateListener;
	
	/**
	 * whether the picture has been disposed or not
	 */
	protected boolean isDisposed=false;
	
	/**
	 * the main display
	 */
	protected MainDisplay mainDisplay;
	
	/**
	 * the related view browser
	 */
	protected ViewBrowser viewBrowser;
	
	/**
	 * the document of the canvas
	 */
	protected SVGDocument doc;
	
	/**
	 * the uri of the svg file
	 */
	protected String uri;
	
	/**
	 * the animation and actions handler
	 */
	protected SVGPictureAnimActionsHandler animActionsHandler;
	
	/**
	 * the picture component
	 */
	protected SVGPictureComponent pictureComponent;
	
	/**
	 * whether the component is displayed
	 */
	private boolean isDisplayed=false;
	
	/**
	 * a constructor of the class
	 * @param mainDisplay the main display
	 * @param doc a svg document
	 * @param uri the uri of the canvas
	 */
	public SVGPicture(MainDisplay mainDisplay, SVGDocument doc, String uri){

		this.mainDisplay=mainDisplay;
		this.doc=doc;
		this.uri=uri;
		
		initialize();
	}
	
	/**
	 * sets the current view browser
	 * @param viewBrowser the current view browser
	 */
	public void setCurrentViewBrowser(ViewBrowser viewBrowser){
		
		this.viewBrowser=viewBrowser;
	}
	
	/**
	 * initializes the canvas and the scrollpane
	 */
	protected void initialize(){

		//creating the animations and actions handler
		animActionsHandler=new SVGPictureAnimActionsHandler(this);
		
		//creating the picture component
		pictureComponent=new SVGPictureComponent(this);
		
		//registers this picture 
		mainDisplay.getPictureManager().addPicture(this);
		
		animActionsHandler.initialize();

		//creates and adds a state listener to the animations handler
		animationsStateListener=new AnimationsStateListener(){
			
			@Override
			public void animationsPaused() {
				
				getCanvas().setCursor(Cursor.getDefaultCursor());
			}
			
			@Override
			public void animationsStopped() {
				
				getCanvas().setCursor(Cursor.getDefaultCursor());
			}
		};
		
		mainDisplay.getAnimationsHandler().
			addAnimationsStateListener(animationsStateListener);
	}
	
	/**
	 * @return whether this picture is displayed or not
	 */
	public boolean isDisplayed() {
		
		return isDisplayed;
	}

	/**
	 * sets whether the picture is displayed
	 * @param isDisplayed whether the picture is displayed
	 */
	public void setDisplayed(boolean isDisplayed) {
		this.isDisplayed=isDisplayed;
	}

	/**
	 * @return whether the picture is disposed
	 */
	public boolean isDisposed() {
		return isDisposed;
	}
	
	/**
	 * disposes this picture
	 */
	public void dispose(){

		if(! isDisposed){
			
			animActionsHandler.dispose();
			pictureComponent.dispose();
			mainDisplay.getAnimationsHandler().
				removeAnimationsStateListener(animationsStateListener);
			
			//unregisters this picture 
			mainDisplay.getPictureManager().removePicture(this);

			isDisposed=true;
		}
	}
	
	/**
	 * @return the document of the canvas
	 */
	public SVGDocument getDocument() {
		return doc;
	}
	
	/**
	 * enqueues the provided runnable so that it 
	 * is executed into the AWT thread
	 * @param runnable a runnable
	 * @param wait whether to wait in the calling 
	 * thread while executing the runnable
	 */
	public void enqueue(final Runnable runnable, boolean wait){
		
		if(runnable!=null && pictureComponent!=null && 
				getCanvas()!=null && getCanvas().getUpdateManager()!=null){
			
			final RunnableQueue queue=getCanvas().getUpdateManager().
				getUpdateRunnableQueue();
			
			if(queue!=null){
				
				if(wait){
					
					try{
						if(queue.getRunHandler()!=null){
							
							queue.getRunHandler().executionResumed(queue);
							queue.getRunHandler().runnableStart(queue, runnable);
							SwingUtilities.invokeAndWait(runnable);
							queue.getRunHandler().runnableInvoked(queue, runnable);
						}
					}catch (Exception ex){ex.printStackTrace();}
					
				}else{
					
					queue.invokeLater(runnable);
				}
			}
		}
	}
	
	/**
	 * @return Returns the canvas.
	 */
	public SVGCanvas getCanvas() {
		
		if(pictureComponent!=null){
			
			return pictureComponent.getCanvas();
		}
		
		return null;
	}
	
	/**
	 * @return Returns the isRendered.
	 */
	public boolean isRendered() {
		return pictureComponent.isRendered();
	}
	
	/**
	 * @return Returns the uri.
	 */
	public String getUri() {
		return uri;
	}
	
	/**
	 * @return the component displaying the SVG picture
	 */
	public JDesktopPane getDesktop(){
		
		return pictureComponent.getDesktopPane();
	}
	
	/**
	 * @return the animations, actions and jwidget handler for the picture
	 */
	public SVGPictureAnimActionsHandler getAnimActionsHandler() {
		return animActionsHandler;
	}
	
	/**
	 * @return the component corresponding to this picture
	 */
	public SVGPictureComponent getPictureComponent() {
		return pictureComponent;
	}
	
	/**
	 * @return the main display
	 */
	public MainDisplay getMainDisplay() {
		return mainDisplay;
	}
	
	/**
	 * @return the view browser into which the picture is displayed
	 */
	public ViewBrowser getViewBrowser() {
		return viewBrowser;
	}
}
