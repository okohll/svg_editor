package fr.itris.glips.rtda.components.viewbrowser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import fr.itris.glips.rtda.*;

/**
 * the dialog that embeds a view browser in it
 * @author Jordi SUC
 */
public class ViewBrowserPopupDialog extends JDialog{

	/**
	 * the view browser
	 */
	private ViewBrowser viewBrowser;
	
	/**
	 * the main display
	 */
	private MainDisplay mainDisplay;
	
	/**
	 * the path of the view to be loaded at start
	 */
	private String viewPath;
	
	/**
	 * the name of the current project
	 */
	private String projectName;
	
	/**
	 * the window listener
	 */
	private WindowListener windowListener;
	
	/**
	 * the relative component for the dialog to display
	 */
	private JComponent relativeComponent;
	
	/**
	 * the constructor of the class
	 * @param mainDisplay the main display
	 * @param parentFrame the parent frame of the dialog
	 * @param viewPath the path of the view to display
	 * @param projectName the name of the current project
	 * @param relativeComponent the relative component for the dialog to display
	 */
	 public ViewBrowserPopupDialog(MainDisplay mainDisplay, 
			 Frame parentFrame, String viewPath, String projectName, 
			 	JComponent relativeComponent){
		 
		 super(parentFrame, true);
		 this.mainDisplay=mainDisplay;
		 this.viewPath=viewPath;
		 this.projectName=projectName;
		 this.relativeComponent=relativeComponent;
		 build();
	 }
	 
	/**
	 * the constructor of the class
	 * @param mainDisplay the main display
	 * @param parent the parent dialog
	 * @param viewPath the path of the view to display
	 * @param projectName the name of the current project
	 * @param relativeComponent the relative component for the dialog to display
	 */
	 public ViewBrowserPopupDialog(MainDisplay mainDisplay, 
			 JDialog parent, String viewPath, String projectName,
			 	JComponent relativeComponent){
		 
		 super(parent, true);
		 this.mainDisplay=mainDisplay;
		 this.viewPath=viewPath;
		 this.projectName=projectName;
		 this.relativeComponent=relativeComponent;
		 build();
	 }
	 
	 /**
	  * builds the dialog
	  */
	 protected void build(){

		 //adding the listener to the window
		 windowListener=new WindowAdapter(){
			 
			 @Override
			public void windowClosing(WindowEvent e) {

				dispose();
			} 
		 };
		 
		 setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		 addWindowListener(windowListener);
		 
		 //creating the view browser
		 viewBrowser=new ViewBrowser(mainDisplay, 
			 mainDisplay.getViewBrowsersManager().getUniqueIdForViewBrowser(), 
				 false);
		 
		 //adding the view browser to the dialog
		 getContentPane().setLayout(new BoxLayout(
				 getContentPane(), BoxLayout.X_AXIS));
		 getContentPane().add(viewBrowser);

		 //registering the view browser
		 mainDisplay.getViewBrowsersManager().
		 		registerViewBrowser(viewBrowser);

		 //getting the uri path of the initial view
		 if(viewPath!=null && ! viewPath.equals("")) {

			 String uri=mainDisplay.getPictureManager().getViewUri(
					 projectName, viewPath);

			 //setting the new picture for this view browser
			 viewBrowser.getPictureLoader().setCurrentPicture(uri, false);
		 }
	 }
	 
	 /**
	  * shows the dialog
	  */
	 public void showDialog(){

		 pack();
		 setLocationRelativeTo(relativeComponent);
		 setVisible(true);
	 }

	 @Override
	 public void dispose(){

		 setVisible(false);
		 
		 if(viewBrowser.getCanvasContainer().getCurrentPicture()!=null){

			 viewBrowser.getCanvasContainer().setPicture(null);
		 }
		 
    	//unregistering the view browser
    	mainDisplay.getViewBrowsersManager().
    		unregisterViewBrowser(viewBrowser);
		 viewBrowser.dispose();
		 viewBrowser.clear();
		 removeWindowListener(windowListener);
	 }
}
