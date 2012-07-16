/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.extension.jwidget.textfield.runtime.anim;

import javax.swing.*;
import org.w3c.dom.*;

import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.config.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 *  the listener to the changes of the user names
 * @author ITRIS, Jordi SUC
 */
public class TextFieldCurrentUser extends JWidgetAnimation{

	/**
	 * the default label
	 */
	protected String defaultLabel="";

    /**
     * the rights listener
     */
    protected RightsListener rightsListener;

    /**
     * the constructor of the class
     * @param jwidgetRuntime the associated jwidget runtime object
     * @param component the component to animate
     * @param animationElement the animation element
     */
    public TextFieldCurrentUser(JWidgetRuntime jwidgetRuntime, 
    		JComponent component, Element animationElement) {
    	
    	super(jwidgetRuntime, component, animationElement);

        defaultLabel=animationElement.getAttribute("defaultUserLabel");
        
        //creating the listener to the user name changes
        rightsListener=new RightsListener(){
        	
        	@Override
        	public void userNameChanged(final String newUserName) {
        		
				String name=newUserName;

				if(name==null || name.equals("")){
					
					name=defaultLabel;
				}
				
				((JTextField)TextFieldCurrentUser.this.component).setText(name);
        	}
        };
        
        jwidgetRuntime.getPicture().getMainDisplay().
        	getUserRights().addRightsListener(rightsListener);
    }
    
    @Override
    public void dispose() {
    	
    	super.dispose();
    	jwidgetRuntime.getPicture().getMainDisplay().
    		getUserRights().removeRightsListener(rightsListener);
    }
}

