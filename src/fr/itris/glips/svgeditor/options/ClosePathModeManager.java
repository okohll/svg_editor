package fr.itris.glips.svgeditor.options;

import fr.itris.glips.library.*;

/**
 * the class handling the state of the close pathmode
 * @author Jordi SUC
 */
public class ClosePathModeManager {

	/**
	 * the id for the mode enablement preference
	 */
	private static final String MODE_PREF_ID="ShouldClosePath";
	
	/**
	 * the default value for the mode enabled boolean
	 */
	protected static final boolean defaultShouldClosePathEnabled=false;
	
	/**
	 * whether the all the drawn path should be closed
	 */
	private boolean shouldClosePath=false;
	
	/**
	 * the constructor of the class
	 */
	public ClosePathModeManager() {

		initializeParameters();
	}
	
	/**
	 * initializes the parameters
	 */
	protected void initializeParameters(){
		
		//getting the parameters from the preference store
		try{
			shouldClosePath=Boolean.parseBoolean(
					PreferencesStore.getPreference(null, MODE_PREF_ID));
		}catch (Exception ex){shouldClosePath=defaultShouldClosePathEnabled;}
	}
	
	/**
	 * updates the preferences values
	 */
	protected void updatePreferences(){

			PreferencesStore.setPreference(null, 
				 MODE_PREF_ID, Boolean.toString(shouldClosePath));
	}

	/**
	 * @return whether the all the drawn path should be closed
	 */
	public boolean shouldClosePath() {
		
		return shouldClosePath;
	}

	/**
	 * sets the new close path mode
	 * @param shouldClosePath whether the all the drawn path should be closed
	 */
	public void setShouldClosePath(boolean shouldClosePath) {
		
		this.shouldClosePath=shouldClosePath;
		updatePreferences();
	}
}
