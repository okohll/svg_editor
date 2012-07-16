package fr.itris.glips.rtdaeditor.test.display;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.test.*;
import fr.itris.glips.rtdaeditor.test.display.table.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the controls panel
 * @author Jordi SUC
 */
public class ControlsPanel extends JPanel{

	/**
	 * the test dialog
	 */
	private DialogTest dialogTest;
	
	/**
	 * the main display
	 */
	private MainDisplay mainDisplay;
    
    /**
     * the builder for the table allowing to set the 
     * values of the test simulator
     */
    private TestTableBuilder tableBuilder;
	
    /**
     * the toolbar containing the buttons
     */
    private JToolBar controlsToolbar;
    
    /**
     * the panel containing the table used to enter 
     * the values for the simulation
     */
    private JPanel simulationValuesPanel;
	
	/**
	 * the constructor of the class
	 * @param dialogTest the test dialog
	 */
	public ControlsPanel(DialogTest dialogTest){
		
		this.dialogTest=dialogTest;
		this.mainDisplay=dialogTest.getMainDisplay();
		this.tableBuilder=new TestTableBuilder();
		build();
	}
	
	/**
	 * builds the controls panel
	 */
	protected void build(){
		
		createToolbar();
		createSimulationValuesPanel();
		
		//filling the panel
		setLayout(new BorderLayout());
		add(controlsToolbar, BorderLayout.WEST);
		add(simulationValuesPanel, BorderLayout.CENTER);
	}
	
	/**
	 * creates the toolbar
	 */
	protected void createToolbar(){
		
        //getting the labels
        ResourceBundle bundle=ResourcesManager.bundle;
        
        String playLabel=bundle.getString("rtdaanim_test_play");
        String pauseLabel=bundle.getString("rtdaanim_test_pause");
        String stopLabel=bundle.getString("rtdaanim_test_stop");
        String exitLabel=bundle.getString("rtdaanim_test_exit");
        final String displayInvalidMarkersLabel=bundle.getString("rtdaanim_test_displayInvalidMarkers");
        final String hideInvalidMarkersLabel=bundle.getString("rtdaanim_test_hideInvalidMarkers");
        final String displayFullScreenMode=bundle.getString("rtdaanim_test_fullscreen");
        final String hideFullScreenMode=bundle.getString("rtdaanim_test_unfullscreen");

        //the control buttons
        final JButton playButton=new JButton();
        final JButton pauseButton=new JButton();
        final JButton stopButton=new JButton();
        final JButton exitButton=new JButton();
        
        final JToggleButton displayFullScreenModeButton=new JToggleButton();
        final JToggleButton displayInvalidMarkersButton=new JToggleButton();
 
		//the ok action name
		String actionName="f11Action";
		
		//registering the ok action
		Action f11Action=new AbstractAction(actionName){
			
			public void actionPerformed(ActionEvent e) {

                if(dialogTest.getFrame().isVisible()){

                    displayFullScreenModeButton.doClick();
                }
			}
		};
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), actionName);
		getActionMap().put(actionName, f11Action);

        //getting the icons for the buttons
        ImageIcon playIcon=ResourcesManager.getIcon("Play", false);
        ImageIcon pauseIcon=ResourcesManager.getIcon("Pause", false);
        ImageIcon stopIcon=ResourcesManager.getIcon("Stop", false);
        ImageIcon exitIcon=ResourcesManager.getIcon("Exit", false);
        ImageIcon fullScreenIcon=ResourcesManager.getIcon("FullScreen", false);
        ImageIcon displayInvalidMarkersIcon=ResourcesManager.getIcon("SmallInvalid", false);
        ImageIcon displayInvalidMarkersIconDisabled=ResourcesManager.getIcon("SmallInvalid", true);
        
        //setting the properties for the buttons
        playButton.setIcon(playIcon);
        pauseButton.setIcon(pauseIcon);
        stopButton.setIcon(stopIcon);
        exitButton.setIcon(exitIcon);
        displayFullScreenModeButton.setIcon(fullScreenIcon);
        displayInvalidMarkersButton.setIcon(displayInvalidMarkersIcon);
        displayInvalidMarkersButton.setDisabledIcon(displayInvalidMarkersIconDisabled);
        displayInvalidMarkersButton.setIcon(displayInvalidMarkersIcon);
        
        playButton.setToolTipText(playLabel);
        pauseButton.setToolTipText(pauseLabel);
        stopButton.setToolTipText(stopLabel);
        exitButton.setToolTipText(exitLabel);
        displayFullScreenModeButton.setToolTipText(displayFullScreenMode);
        
        //handles the state of the invalid marker button
        if(mainDisplay.getAnimationsHandler().
        			getInvalidityNotifier().isInvalidMarkersEnabled()){
            
            displayInvalidMarkersButton.setSelected(true);
            displayInvalidMarkersButton.setToolTipText(hideInvalidMarkersLabel);
            
        }else{
            
            displayInvalidMarkersButton.setSelected(false);
            displayInvalidMarkersButton.setToolTipText(displayInvalidMarkersLabel);
        }
        
        //handles the state of the full screen button
        displayFullScreenModeButton.setSelected(dialogTest.isInFullScreenMode());

        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        
        //setting the listeners to the buttons
        playButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {
                
                if(mainDisplay.getAnimationsHandler().isStopped()){

                	mainDisplay.refresh(null);
                	mainDisplay.getAnimationsHandler().start();
                    
                }else if(mainDisplay.getAnimationsHandler().isPaused()){
                    
                	mainDisplay.getAnimationsHandler().resume();
                }
            }
        });
        
        pauseButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {
                
                if(! mainDisplay.getAnimationsHandler().isStopped() && 
                		! mainDisplay.getAnimationsHandler().isPaused()){
                    
                	mainDisplay.getAnimationsHandler().pause();
                }
            }
        });
        
        stopButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {
                
            	mainDisplay.getAnimationsHandler().stop();
                refreshSimulationValuesPanel();
            }
        });
        
        exitButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {
                
                dialogTest.refreshDialogState(false);
            }
        });
        
        displayFullScreenModeButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {

                if(! dialogTest.isInFullScreenMode()){
                    
                    displayFullScreenModeButton.setToolTipText(hideFullScreenMode);

                }else{
                    
                    displayFullScreenModeButton.setToolTipText(displayFullScreenMode);
                }
                
                dialogTest.handleFullScreenMode();
            }
        });
        
        //the listener to the display invalid marker button
        displayInvalidMarkersButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent evt) {

                //handles the state of the invalid marker button
                if(displayInvalidMarkersButton.isSelected()){

                    displayInvalidMarkersButton.setToolTipText(hideInvalidMarkersLabel);
                    
                }else{

                    displayInvalidMarkersButton.setToolTipText(displayInvalidMarkersLabel);
                }
                
                mainDisplay.getAnimationsHandler().getInvalidityNotifier().
                		setInvalidMarkersEnabled(displayInvalidMarkersButton.isSelected());
            }
        });
        
		//the listener to the changes of the animations handler
        mainDisplay.getAnimationsHandler().addAnimationsStateListener(
        		new AnimationsStateListener(){

			@Override
	        public void animationsExistBeforeStarting() {
	        	
        		playButton.setEnabled(true);
        		pauseButton.setEnabled(false);
        		stopButton.setEnabled(false);
	        }

			@Override
	        public void noMoreAnimations() {
	        	
        		playButton.setEnabled(false);
        		pauseButton.setEnabled(false);
        		stopButton.setEnabled(false);
	        }

			@Override
	        public void animationsStarted() {

        		playButton.setEnabled(false);
        		pauseButton.setEnabled(true);
        		stopButton.setEnabled(true);
	        }

			@Override
	        public void animationsPaused() {

        		playButton.setEnabled(true);
        		pauseButton.setEnabled(false);
        		stopButton.setEnabled(true);
	        }

			@Override
	        public void animationsResumed() {

        		playButton.setEnabled(false);
        		pauseButton.setEnabled(true);
        		stopButton.setEnabled(true);
	        }

			@Override
	        public void animationsStopped() {

        		playButton.setEnabled(true);
        		pauseButton.setEnabled(false);
        		stopButton.setEnabled(false);
	        }
		});
		
        //creating the controls toolbar
        controlsToolbar=new JToolBar(SwingConstants.VERTICAL);
        controlsToolbar.setFloatable(false);
        controlsToolbar.setBorderPainted(false);

        controlsToolbar.add(playButton);
        controlsToolbar.add(pauseButton);
        controlsToolbar.add(stopButton);
        controlsToolbar.addSeparator();
        controlsToolbar.add(displayInvalidMarkersButton);
        controlsToolbar.addSeparator();
        controlsToolbar.add(displayFullScreenModeButton);
        controlsToolbar.add(exitButton);
	}
	
	/**
	 * creates the simulation values panel
	 */
	protected void createSimulationValuesPanel(){
		
		simulationValuesPanel=tableBuilder.getComponent();
	}
	
    /**
     * refreshes the simulation values panel that will be displayed
     * to allow the users to specify the values they want for the simulation
     */
    public void refreshSimulationValuesPanel(){
        
        tableBuilder.disposeTable();
        
        if(mainDisplay.getAnimationsHandler().getRtdpTestSimulator()!=null && 
        		dialogTest.getFrame().isVisible()){
            
            //the objects describing the variables that should be modified to simulate the animations
            LinkedList<TestSimulationTagValues> testSimulationValues=
            	mainDisplay.getAnimationsHandler().getRtdpTestSimulator().
            		refreshSimulationValuesSpecifiers();
            mainDisplay.getAnimationsHandler().getRtdpTestSimulator().setTestInteractor(tableBuilder);
            
            if(testSimulationValues!=null && testSimulationValues.size()>0){
                
                tableBuilder.refreshTestTable(testSimulationValues);
            }
        }
    }
	
	/**
	 * @return the table builder
	 */
	public TestTableBuilder getTableBuilder() {
		return tableBuilder;
	}
}
