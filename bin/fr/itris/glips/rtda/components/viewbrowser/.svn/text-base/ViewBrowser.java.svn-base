package fr.itris.glips.rtda.components.viewbrowser;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.resources.*;
import java.awt.event.*;

/**
 * the class of the view browser
 * @author ITRIS, Jordi SUC
 */
public class ViewBrowser extends JComponent {
	
	/**
	 * the resource bundle
	 */
	protected static ResourceBundle bundle=null;
	
	static {
		
        //getting the resource bundle
		try{
			bundle=ResourceBundle.getBundle("fr.itris.glips.rtda.resources.properties.strings");
		}catch (Exception ex){}
	}
	
	/**
	 * the font
	 */
	protected static final Font theFont=
		new Font("smallFont", Font.ROMAN_BASELINE, 12);
	
	/**
	 * the id of the view browser
	 */
	protected String viewBrowserId="";
	
	/**
	 * the main display
	 */
	protected MainDisplay mainDisplay;
	
	/**
	 * whether or not the header should be shown
	 */
	protected boolean showHeader=true;
	
	/**
	 * the buttons
	 */
	protected JButton previousButton, nextButton;
	
	/**
	 * the listeners to the buttons
	 */
	protected ActionListener previousButtonListener, nextButtonListener;
	
	/**
	 * the url text field
	 */
	protected JTextField urlTextField;
	
	/**
	 * the canvas container
	 */
	protected SVGCanvasContainer canvasContainer;
	
	/**
	 * the picture loader
	 */
	protected SVGPicturesLoader pictureLoader;
	
	/**
	 * the constructor of the class
	 * @param mainDisplay the main display
	 * @param viewBrowserId the id of the view browser
	 * @param showHeader whether the header should be shown in the view browser
	 */
	public ViewBrowser(MainDisplay mainDisplay, String viewBrowserId, boolean showHeader) {
		
		this.mainDisplay=mainDisplay;
		this.viewBrowserId=viewBrowserId;
		this.showHeader=showHeader;
		canvasContainer=new SVGCanvasContainer(this);
		pictureLoader=new SVGPicturesLoader(this);
		setOpaque(false);
		buildComponent();
	}
	
	/**
	 * builds the component
	 */
	protected void buildComponent() {
		
		//building the whole component
		setLayout(new BorderLayout());
		add(canvasContainer, BorderLayout.CENTER);
		
		if(showHeader) {
			
			//getting the labels
			String previousLabel="", nextLabel="";
			
			try {
				previousLabel=bundle.getString("viewchooser_previous");
				nextLabel=bundle.getString("viewchooser_next");
			}catch (Exception ex) {}
			
			//the header panel
			JMenuBar headerPanel=new JMenuBar();
			headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
			
			//building the buttons
			previousButton=new JButton();
			nextButton=new JButton();
			
			previousButton.setToolTipText(previousLabel);
			nextButton.setToolTipText(nextLabel);
			
			//getting the button's icons
			ImageIcon 	previousIcon=RtdaResources.getIcon("previousIcon", false),
								dpreviousIcon=RtdaResources.getIcon("previousIcon", true),
								nextIcon=RtdaResources.getIcon("nextIcon", false),
								dnextIcon=RtdaResources.getIcon("nextIcon", true);
			
			previousButton.setEnabled(false);
			nextButton.setEnabled(false);
			previousButton.setFont(theFont);
			nextButton.setFont(theFont);
			previousButton.setMargin(new Insets(1, 1, 1, 1));
			nextButton.setMargin(new Insets(1, 1, 1, 1));

			previousButton.setIcon(previousIcon);
			previousButton.setDisabledIcon(dpreviousIcon);
			nextButton.setIcon(nextIcon);
			nextButton.setDisabledIcon(dnextIcon);
			
			//adding the listeners to the buttons
			previousButtonListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {
					
					pictureLoader.returnToPreviousPicture();
				}
			};
			
			previousButton.addActionListener(previousButtonListener);
			
			nextButtonListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {
					
					pictureLoader.goToNextPicture();
				}
			};
			
			nextButton.addActionListener(nextButtonListener);
			
			headerPanel.add(previousButton);
			headerPanel.add(nextButton);
			
			//building the view chooser panel
			JPanel viewChooserPanel=new JPanel();
			viewChooserPanel.setLayout(new BorderLayout());
			add(headerPanel, BorderLayout.NORTH);
		}
	}
	
	/**
	 * @return the canvas container
	 */
	public SVGCanvasContainer getCanvasContainer() {
		
		return canvasContainer;
	}
	
	/**
	 * disposes all the pictures
	 */
	public void disposeAll() {
		
		clear();
		
		if(mainDisplay!=null){
			
			mainDisplay.reinitialize();
			
			//disposing the view browsers
			mainDisplay.getViewBrowsersManager().disposeAllViewBrowsers();
			
			//disposing the pictures
			mainDisplay.getPictureManager().disposeAllPictures();
	        mainDisplay.getAnimationsHandler().reinitialize();
		}
	}
	
	/**
	 * clears this view browser
	 */
	public void clear() {

		pictureLoader.clear();
		canvasContainer.setPicture(null);
	}
	
	/**
	 * disposing this view browser
	 */
	public void dispose(){
		
		if(previousButtonListener!=null){
			
			previousButton.removeActionListener(previousButtonListener);
		}
		
		if(nextButtonListener!=null){
			
			nextButton.removeActionListener(nextButtonListener);
		}
	}
    
    /**
     * handles the state of the buttons
     */
    protected void handleButtonsState(){

    	if(previousButton!=null){
    		
    		previousButton.setEnabled((pictureLoader.getHistory().size()>1 && 
    				pictureLoader.getCurrentHistoryPosition()>0));
    	}

    	if(nextButton!=null){
    		
    		nextButton.setEnabled(pictureLoader.getHistory().size()>1 && 
    				pictureLoader.getCurrentHistoryPosition()<pictureLoader.getHistory().size()-1);
    	}
    }

	/**
	 * @return the view browser id
	 */
	public String getViewBrowserId() {
		return viewBrowserId;
	}
	
	/**
	 * @return the picture loader
	 */
	public SVGPicturesLoader getPictureLoader() {
		return pictureLoader;
	}
}
