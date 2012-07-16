package fr.itris.glips.svgeditor.io.managers.monitor;

import java.awt.*;
import javax.swing.*;
import fr.itris.glips.library.monitor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the monitor used for a save action
 * @author Jordi SUC
 */
public class SaveMonitor extends Monitor{

	/**
	 * the labels
	 */
	private String titleLabel="", messageLabel="", 
		xmlGenerationLabel="", writingFileLabel="";
	
	/**
	 * the constructor of the class
	 * @param parent the parent component used to 
	 * display the progress dialog
	 * @param relativeComponent the component relatively to 
	 * which the dialog will be shown
	 * @param min the min value of the progress
	 * @param max the max value of the progress
	 */
	public SaveMonitor(Component parent, 
			JComponent relativeComponent, int min, int max){

		super(parent, relativeComponent, min, max);
		
		//initializing the monitor
		initialize();
	}
	
	@Override
	protected void initialize() {

		//getting the labels
		titleLabel=ResourcesManager.bundle.getString("SaveMonitorTitle");
		messageLabel=ResourcesManager.bundle.getString("SaveMonitorMessage");
		xmlGenerationLabel=ResourcesManager.bundle.getString("SaveMonitorXMLGenerationMessage");
		writingFileLabel=ResourcesManager.bundle.getString("SaveMonitorWritingFileMessage");

		//setting the labels for the dialog
		waitDialog.setTitleMessage(titleLabel);
		waitDialog.setMessage(messageLabel);
	}
	
	@Override
	public void setIndeterminate(final boolean indeterminate) {
		
		setProgressMessage(writingFileLabel);
		super.setIndeterminate(indeterminate);
	}
	
	@Override
	public void start() {

		setProgressMessage(xmlGenerationLabel);
		super.start();
	}
}
