/*
 * Created on 2 fevr. 2005
 */
package fr.itris.glips.rtdaeditor.test.display;

import javax.swing.*;
import org.w3c.dom.svg.*;
import java.awt.*;
import java.awt.event.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import java.util.*;
import fr.itris.glips.rtda.components.viewbrowser.*;

/**
 * the class displaying the dialog into which the test will be shown
 * 
 * @author ITRIS, Jordi SUC
 */
public class DialogTest{
    
	/**
	 * the main display
	 */
	private MainDisplay mainDisplay;
    
    /**
     * the split pane
     */
    private JSplitPane splitPane;

    /**
     * the canvas container
     */
    private SVGCanvasContainer canvasContainer;
    
    /**
     * the panel containing the controls 
     */
    private ControlsPanel controlsPanel;
    
    /**
     * the size of the screen
     */
    private Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    
    /**
     * the bounds when the dialog is in normal mode
     */
    private Rectangle normalBounds=new Rectangle();
    
    /**
     * whether the dialog is in full screen mode or not
     */
    private boolean isInFullScreenMode=false;

    /**
     * the normal dialog
     */
    private JFrame frame=new JFrame();
    
    /**
     * the constructor of the class
     */
    public DialogTest(){}
    
    /**
     * initializes the dialog
     * @param aMainDisplay the main display
     */
    public void initialize(MainDisplay aMainDisplay){
    	
    	this.mainDisplay=aMainDisplay;

        //getting the labels
        ResourceBundle bundle=ResourcesManager.bundle;
        String titleLabel=bundle.getString("rtdaanim_test_dialogTitle");
        
        //creating the canvas container
        canvasContainer=
        	mainDisplay.getMainViewBrowser().getCanvasContainer();
        
        //creating the controls panel
        controlsPanel=new ControlsPanel(this);
        
        //creating the split pane
        splitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(1);
        splitPane.setDividerLocation(0.8);
        splitPane.setOneTouchExpandable(true);
        splitPane.setTopComponent(canvasContainer);
        splitPane.setBottomComponent(controlsPanel);

        //the layout for this dialog content pane
        frame.getContentPane().setLayout(new BoxLayout(
        	frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setTitle(titleLabel);

		//the listener to the state of the window
        frame.addWindowListener(new WindowAdapter(){

        	@Override
        	public void windowClosing(WindowEvent evt) {
 
                refreshDialogState(false);
                frame.setVisible(false);
        	}
		});
        
        //packing the dialogs
        frame.getContentPane().add(splitPane);
        frame.pack();
        
        //setting the size for this dialog
        Rectangle bounds=new Rectangle();
        
        bounds.width=3*screenSize.width/4;
        bounds.height=3*screenSize.height/4;
        bounds.x=(screenSize.width-bounds.width)/2;
        bounds.y=(screenSize.height-bounds.height)/2;

        normalBounds=bounds;
        frame.setBounds(normalBounds);
    }
    
    /**
     * @return the main display
     */
    public MainDisplay getMainDisplay() {
		return mainDisplay;
	}
    
    /**
     * @return whether the dialog is in the full screen mode
     */
    public boolean isInFullScreenMode() {
		return isInFullScreenMode;
	}
    
    /**
     * handles the full screen mode
     */
    protected void handleFullScreenMode(){

        frame.setVisible(false);
    	
        if(! isInFullScreenMode){
            
        	frame.dispose();
        	frame.setUndecorated(true);
        	frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            splitPane.validate();

            isInFullScreenMode=true;
            
        }else{
            
        	frame.dispose();
        	frame.setUndecorated(false);
        	frame.setExtendedState(Frame.NORMAL);
            splitPane.validate();
            frame.setBounds(normalBounds);
            
            isInFullScreenMode=false;
        }
        
        frame.setVisible(true);
    }
    
    /**
     * refreshes the state of the dialog
     * @param visible whether the dialog will be visible
     */
    public void refreshDialogState(boolean visible){

    	mainDisplay.getAnimationsHandler().stop();
    	mainDisplay.getMainViewBrowser().disposeAll();
        controlsPanel.getTableBuilder().disposeTable();
        
        if(visible){

            SVGHandle handle=
            	Editor.getEditor().getHandlesManager().getCurrentHandle();
            
            if(handle!=null){
                
            	//setting the svg document to be displayed
            	mainDisplay.getMainViewBrowser().getPictureLoader().setCurrentPicture(
            			(SVGDocument)handle.getScrollPane().getSVGCanvas().getDocument(),
            				handle.getScrollPane().getSVGCanvas().getURI(), false);
                
                //refreshing the simulation values panel
            	controlsPanel.refreshSimulationValuesPanel();
                
                //setting the bounds
            	frame.setVisible(true);
            }
            
            mainDisplay.getAnimationsHandler().start();
            
        }else{
        	
        	frame.setVisible(false);
        }
    }
    
    /**
     * @return the normal dialog
     */
    public JFrame getFrame() {
		return frame;
	}
    
    /**
     * @return Returns the canvas container
     */
    public SVGCanvasContainer getCanvasContainer() {
    	
        return canvasContainer;
    }
    
    /**
     * refreshes the simulation values panel that will be displayed
     * to allow the users to specify the values they want for the simulation
     */
    public void refreshSimulationValuesPanel(){
    	
    	controlsPanel.refreshSimulationValuesPanel();
    }
}
