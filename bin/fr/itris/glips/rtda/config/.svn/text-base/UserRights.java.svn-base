package fr.itris.glips.rtda.config;

import java.util.*;
import java.util.Timer;
import javax.swing.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the class used to store the current user rights
 * @author Jordi SUC
 */
public class UserRights {
	
	/**
	 * the RIGHT_FOR_TAG_MODIFICATION constant
	 */
	public static final int RIGHT_FOR_TAG_MODIFICATION=0;
	
	/**
	 * the RIGHT_FOR_VIEW_LOADING constant
	 */
	public static final int RIGHT_FOR_VIEW_LOADING=1;
	
	/**
	 * the RIGHT_FOR_RECIPE_LOADING constant
	 */
	public static final int RIGHT_FOR_RECIPE_LOADING=2;
	
	/**
	 * the RIGHT_FOR_RECIPE_MODIFICATION constant
	 */
	public static final int RIGHT_FOR_RECIPE_MODIFICATION=3;
	
	/**
	 * the RIGHT_FOR_PROGRAM_LAUNCHING constant
	 */
	public static final int RIGHT_FOR_PROGRAM_LAUNCHING=4;
	
	/**
	 * the resource bundle
	 */
	private ResourceBundle bundle;
	
	/**
	 * the main display
	 */
	private MainDisplay mainDisplay;
	
	/**
	 * the set of the rights listener
	 */
	private Set<RightsListener> rightsListenerSet=
		new HashSet<RightsListener>();
	
	/**
	 * the current picture
	 */
	private SVGPicture picture;
	
	/**
	 * the main picture
	 */
	private SVGPicture mainPicture;
	
	/**
	 * the current configuration document
	 */
	private ConfigurationDocument configDoc;
	
	/**
	 * the right level for tag modifications
	 */
	private int rightForTagModification=0;
	
	/**
	 * the right level for view loadings 
	 */
	private int rightForViewLoading=0;
	
	/**
	 * the right level for recipe loadings 
	 */
	private int rightForRecipeLoading=0;

	/**
	 * the right level for recipe modifications
	 */
	private int rightForRecipeModification=0;
	
	/**
	 * the right level for program launchings
	 */
	private int rightForProgramLaunching=0;
	
	/**
	 * the current user name
	 */
	private String userName="";
	
	/**
	 * the timer manager used for the timeout
	 */
	private Timer timer=new Timer();
	
	/**
	 * the timer task
	 */
	private TimerTask timerTask;

	/**
	 * the constructor of the class
	 * @param mainDisplay the main display
	 */
	public UserRights(MainDisplay mainDisplay){
		
		this.mainDisplay=mainDisplay;
		
        //getting the resource bundle
		try{
			bundle=ResourceBundle.getBundle(
				"fr.itris.glips.rtda.resources.properties.strings");
		}catch (Exception ex){}
	}
	
	/**
	 * sets the current picture
	 * @param newPicture a new picture
	 */
	public void setCurrentPicture(SVGPicture newPicture){
		
		if(newPicture!=null && (picture==null || 
				! newPicture.getCanvas().getProjectFile().equals(
						picture.getCanvas().getProjectFile()))){
			
			this.picture=newPicture;
			this.configDoc=mainDisplay.getPictureManager().
				getConfigurationDocument(picture);
			
			if(mainPicture==null){
				
				mainPicture=picture;
			}
			
			initRights(true);
		}
	}
	
	/**
	 * sets the new user if the login and password are OK
	 * @param relativeComponent the component relatively to 
	 * which the options dialog should be displayed
	 * @param login the login of the new user
	 * @param password the password of the new user
	 * @return whether the login succeeded
	 */
	public boolean setCurrentUser(
			JComponent relativeComponent, String login, String password){
		
		boolean success=false;
		
		//disabling the current timer task
		if(timerTask!=null){
			
			timerTask.cancel();
			timer.purge();
			timerTask=null;
		}

		synchronized(this){
			
			if(configDoc!=null){
				
				//getting the user corresponding to the login
				User user=configDoc.getRights().getUser(login);
				
				if(user==null){
					
					showWrongLoginDialog(relativeComponent);
					
				}else if(user.getPassword().equals(password)){
					
					Profile profile=configDoc.getRights().getProfile(user);
					
					if(profile!=null){
						
						rightForTagModification=profile.getRightForTagModification();
						rightForViewLoading=profile.getRightForViewLoading();
						rightForRecipeLoading=profile.getRightForRecipeLoading();
						rightForRecipeModification=profile.getRightForRecipeModification();
						rightForProgramLaunching=profile.getRightForProgramLaunching();
						userName=user.getId();
						notifyUserNameChanged();
						refreshJWidgets();
						
						if(profile.getTimeOut()!=0){
							
							final SVGPicture fmainPicture=mainPicture;
							final SVGPicture fpicture=picture;
							
							timerTask=new TimerTask(){
								
								@Override
								public void run() {
		
									initRights(true);
									
									if(fmainPicture!=null && fpicture!=mainPicture){
										
										mainDisplay.getMainViewBrowser().getPictureLoader().
											setCurrentPicture(fmainPicture);
									}
									
									refreshJWidgets();
								}
							};
							
							timer.schedule(timerTask, profile.getTimeOut()*60000);
						}
						
						success=true;
					}
					
				}else{
					
					showWrongPasswordDialog(relativeComponent);
				}
				
			}else{
				
				initRights(true);
				refreshJWidgets();
			}
		}

		return success;
	}
	
	/**
	 * disconnects the current user
	 */
	public void disconnect(){
		
		initRights(true);
		refreshJWidgets();
	}
	
	/**
	 * initializes the rights
	 * @param notify whether to notify that the rights have changed
	 */
	protected synchronized void initRights(boolean notify){
		
		rightForTagModification=0;
		rightForViewLoading=0;
		rightForRecipeLoading=0;
		rightForRecipeModification=0;
		rightForProgramLaunching=0;
		userName="";
		
		if(notify){
			
			notifyUserNameChanged();
		}
	}

	/**
	 * @return the right level for program launchings
	 */
	public int getRightForProgramLaunching() {
		return rightForProgramLaunching;
	}

	/**
	 * @return the right level for recipe loadings 
	 */
	public int getRightForRecipeLoading() {
		return rightForRecipeLoading;
	}

	/**
	 * @return the right level for recipe modifications
	 */
	public int getRightForRecipeModification() {
		return rightForRecipeModification;
	}

	/**
	 * @return the right level for tag modifications
	 */
	public int getRightForTagModification() {
		return rightForTagModification;
	}

	/**
	 * @return the right level for view loadings 
	 */
	public int getRightForViewLoading() {
		return rightForViewLoading;
	}
	
	/**
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * @return the configuration document
	 */
	public ConfigurationDocument getConfigDoc() {
		return configDoc;
	}
	
	/**
	 * adds a new rights listener
	 * @param listener a rights listener
	 */
	public void addRightsListener(RightsListener listener){
		
		if(listener!=null){
			
			rightsListenerSet.add(listener);
		}
	}
	
	/**
	 * removes a rights listener
	 * @param listener a rights listener
	 */
	public void removeRightsListener(RightsListener listener){
		
		if(listener!=null){
			
			rightsListenerSet.remove(listener);
		}
	}
	
	/**
	 * notifies that the user name has changed
	 */
	protected void notifyUserNameChanged(){
		
		for(RightsListener listener : 
			new HashSet<RightsListener>(rightsListenerSet)){
			
			listener.userNameChanged(userName);
		}
		
		mainDisplay.getAnimationsHandler().setDataValue(
			picture.getAnimActionsHandler().getServerIPAddress(),
				configDoc.getRights().getUserTagName(), userName);
	}
	
	/**
	 * reinitializes the object
	 */
	public void reinitialize(){
		
		picture=null;
		configDoc=null;
		mainPicture=null;
		rightsListenerSet.clear();
		initRights(false);
	}
	
	/**
	 * refreshes all the jwidgets
	 */
	protected void refreshJWidgets(){
		
		Set<JWidgetRuntime> set=new HashSet<JWidgetRuntime>(
			mainDisplay.getJwidgetRuntimeManager().getJWidgetRuntimeObjects());
		
		for(JWidgetRuntime jwidgetRuntime : set){
			
			if(jwidgetRuntime!=null){
				
				jwidgetRuntime.refreshComponentState();
			}
		}
	}
	
	/**
	 * shows the wrong login dialog
	 * @param relativeComponent the component relatively to 
	 * which the options dialog should be displayed
	 */
	protected void showWrongLoginDialog(JComponent relativeComponent){
		
		String errorLabel=bundle.getString("ErrorLabel");
		String wrongLoginLabel=bundle.getString("WrongLoginLabel");
		
		JOptionPane.showMessageDialog(relativeComponent, 
				wrongLoginLabel, errorLabel, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * shows the wrong password dialog
	 * @param relativeComponent the component relatively to 
	 * which the options dialog should be displayed
	 */
	protected void showWrongPasswordDialog(JComponent relativeComponent){
		
		String errorLabel=bundle.getString("ErrorLabel");
		String wrongPasswordLabel=bundle.getString("WrongPasswordLabel");
		
		JOptionPane.showMessageDialog(relativeComponent, 
				wrongPasswordLabel, errorLabel, JOptionPane.ERROR_MESSAGE);
	}
}
