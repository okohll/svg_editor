package fr.itris.glips.svgeditor.options;

import fr.itris.glips.library.*;

/**
 * the class handling the state of the constraint lines mode
 * @author Jordi SUC
 */
public class ConstraintLinesModeManager {
	
	/**
	 * the id for the mode enablement preference
	 */
	private static final String MODE_PREF_ID="ConstraintLines";
	
	/**
	 * the default value for the mode enabled boolean
	 */
	protected static final boolean defaultConstraintLinesEnabled=false;

	/**
	 * whether all the drawn lines should be constrained
	 */
	private boolean constraintLines=false;
	
	/**
	 * the constructor of the class
	 */
	public ConstraintLinesModeManager() {
		
		initializeParameters();
	}
	
	/**
	 * initializes the parameters
	 */
	protected void initializeParameters(){
		
		//getting the parameters from the preference store
		try{
			constraintLines=Boolean.parseBoolean(
					PreferencesStore.getPreference(null, MODE_PREF_ID));
		}catch (Exception ex){constraintLines=defaultConstraintLinesEnabled;}
	}
	
	/**
	 * updates the preferences values
	 */
	protected void updatePreferences(){

		PreferencesStore.setPreference(null, 
				MODE_PREF_ID, Boolean.toString(constraintLines));
	}
	
	/**
	 * @return whether all the drawn lines should be constrained
	 */
	public boolean constraintLines() {
		
		return constraintLines;
	}

	/**
	 * sets whether all the drawn lines should be constrained
	 * @param constraintLines whether all the drawn lines should be constrained
	 */
	public void setConstraintLines(boolean constraintLines) {
		
		this.constraintLines=constraintLines;
		updatePreferences();
	}
}
