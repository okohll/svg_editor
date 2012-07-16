package fr.itris.glips.rtda.animaction;

import java.awt.Container;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the class that is the super class of all the custom actions
 * @author ITRIS, Jordi SUC
 */
public abstract class CustomAction extends Action {
	
	/**
	 * the class name attribute
	 */
	public static String classAttributeName="class";
	
	/**
	 * the constructor of the class
	 * @param picture the svg picture this action is linked to
	 * @param projectName the name of the project this action is linked to
	 * @param parent the top level container
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
	 * @param jwidgetRuntime the jwidget runtime object, if this action is linked to a jwidget
	 */
	public CustomAction(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
	}

	/**
	 * @return the name of the class that will be used to create the 
	 * 					dialog used to configure this action or <b>null</b> if no 
	 * 					configuration dialog should be displayed
	 */
	public static String getConfiguratorClassName() {
		
		return null;
	}
}
