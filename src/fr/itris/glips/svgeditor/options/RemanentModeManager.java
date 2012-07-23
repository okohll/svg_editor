package fr.itris.glips.svgeditor.options;

import fr.itris.glips.library.*;

/**
 * the class handling the state of the remanent mode
 * @author Jordi SUC
 */
public class RemanentModeManager {

	/**
	 * the id for the mode enablement preference
	 */
	private static final String MODE_PREF_ID="Remanent";
	
	/**
	 * the default value for the mode enabled boolean
	 */
	protected static final boolean defaultIsRemanentEnabled=false;
	
	/**
	 * whether the remanent mode is activated
	 */
	private boolean isRemanentMode=false;

	/**
	 * the constructor of the class
	 */
	public RemanentModeManager() {

		initializeParameters();
	}
	
	/**
	 * initializes the parameters
	 */
	protected void initializeParameters(){
	
		//getting the parameters from the preference store
			isRemanentMode=Boolean.parseBoolean(
					PreferencesStore.getPreference(null, MODE_PREF_ID));
	}
	
	/**
	 * updates the preferences values
	 */
	protected void updatePreferences(){

		PreferencesStore.setPreference(null, 
			 MODE_PREF_ID, Boolean.toString(isRemanentMode));
	}
	
	/**
	 * @return whether the remanent mode is activated
	 */
	public boolean isRemanentMode() {
		
		return isRemanentMode;
	}

	/**
	 * sets the new remanent mode
	 * @param isRemanentMode the new remanent mode
	 */
	public void setRemanentMode(boolean isRemanentMode) {
		
		this.isRemanentMode=isRemanentMode;
		updatePreferences();
	}
}
