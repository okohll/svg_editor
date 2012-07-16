package fr.itris.glips.rtda.action;

import java.awt.Container;
import java.io.*;
import java.net.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the abstract class of the run application action
 * @author ITRIS, Jordi SUC
 */
public abstract class AbstractRunApplication extends fr.itris.glips.rtda.animaction.Action{
	
	/**
	 * the string names
	 */
	protected static final String commandAttributeName="command", 
			workingDirectoryAttributeName="workingDirectory";
	
	/**
	 * the constructor of the class
	 * @param picture the svg picture this action is linked to
	 * @param projectName the name of the project this action is linked to
	 * @param parent the parent container
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
	 * @param jwidgetRuntime the jwidget runtime object, if this action is linked to a jwidget
	 */
	public AbstractRunApplication(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
		computeRightsForProgramLaunching();
	}
	
	/**
	 * initializes the action
	 */
	protected void initializeAction(){
		
		initializeAuthorizationTag();
	}
	
	@Override
	public Runnable dataChanged(DataEvent evt) {
		
		super.dataChanged(evt);
		checkIsAuthorized();
		return null;
	}

	@Override
	public void performAction(Object evt) {
		
		if(isEntitled() && isAuthorized && showConfirmationDialog()){

	        //getting the command
	        String command=actionElement.getAttribute(commandAttributeName);
	        
	        //getting the working directory
	        String workingDirectory=actionElement.getAttribute(workingDirectoryAttributeName);

	    	try{
	    	    
	    	    if(workingDirectory!=null && ! workingDirectory.equals("")){
	    	    	
	    	        File workingDirectoryFile=new File(new URI(workingDirectory));
	    	        Runtime.getRuntime().exec(command, null, workingDirectoryFile);
	    	        
	    	    }else{
	    	        
	    	       Runtime.getRuntime().exec(command);
	    	    }
	    	}catch (Exception ex){ex.printStackTrace();}
		}
	}
}
