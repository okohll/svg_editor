package fr.itris.glips.svgeditor.options;

import fr.itris.glips.library.*;

/**
 * the class handling the state of the square mode
 * @author Jordi SUC
 */
public class SquareModeManager {
	
	/**
	 * the id for the mode enablement preference
	 */
	private static final String MODE_PREF_ID="SquareMode";
	
	/**
	 * the default value for the mode enabled boolean
	 */
	protected static final boolean defaultSquareModeEnabled=false;

	/**
	 * whether the square mode is activated
	 */
	private boolean isSquareMode=false;
	
	/**
	 * the constructor of the class
	 */
	public SquareModeManager() {

		initializeParameters();
	}
	
	/**
	 * initializes the parameters
	 */
	protected void initializeParameters(){

		//getting the parameters from the preference store
			isSquareMode=Boolean.parseBoolean(
					PreferencesStore.getPreference(null, MODE_PREF_ID));
	}
	
	/**
	 * updates the preferences values
	 */
	protected void updatePreferences(){

		PreferencesStore.setPreference(null, 
			 MODE_PREF_ID, Boolean.toString(isSquareMode));
	}

	/**
	 * @return whether the square mode is activated
	 */
	public boolean isSquareMode() {
		return isSquareMode;
	}

	/**
	 * sets the new square mode
	 * @param isSquareMode the new square mode
	 */
	public void setSquareMode(boolean isSquareMode) {
		
		this.isSquareMode=isSquareMode;
		updatePreferences();
	}
}
