/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.rtda.animations;

import org.w3c.dom.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.config.*;

/**
 * the listener to the changes of the user names
 * @author ITRIS, Jordi SUC
 */
public class CurrentUser extends DataChangedListener{

	/**
	 * the default label
	 */
	protected String defaultLabel="";

    /**
     * whether the listener is enabled or not
     */
    protected boolean isEnabled=true;
    
    /**
     * the rights listener
     */
    protected RightsListener rightsListener;

    /**
     * the constructor of the class
     * @param picture the svg picture to which this animation is registered
     * @param animationElement an animation node
     */
    public CurrentUser(SVGPicture picture, Element animationElement){

        super(picture, animationElement);

        defaultLabel=animationElement.getAttribute("defaultUserLabel");
        
        //creating the listener to the user name changes
        rightsListener=new RightsListener(){
        	
        	@Override
        	public void userNameChanged(final String newUserName) {
        		
        		CurrentUser.this.picture.enqueue(new Runnable(){
        			
        			public void run() {
        				
        				handleNewUser();
        			}
        		}, false);
        	}
        };
        
        picture.getMainDisplay().
        	getUserRights().addRightsListener(rightsListener);
        
        setInvalidTag(false);
        handleNewUser();
    }
    
    /**
     * handles the modification of a user name
     */
    protected void handleNewUser(){
    	
		String name=picture.getMainDisplay().getUserRights().getUserName();

		if(name==null || name.equals("")){
			
			name=defaultLabel;
		}
		
		AnimationsToolkit.setText(parentElement, name);
    }
    
    /**
     * the method called when the data to which the listener is registered, is modified
     * @param evt an event
     * @return the runnable that should be executed to apply the modifications
     */
    public Runnable dataChanged(DataEvent evt) {
 
        return null;
    }
    
    /**
     * @see fr.itris.glips.rtda.animaction.ListenerAction#dispose()
     */
    public void dispose() {
    	
        picture.getMainDisplay().
    		getUserRights().removeRightsListener(rightsListener);
    }
}

