package fr.itris.glips.rtda.config;

import org.w3c.dom.*;

/**
 * the class storing information on a user profile
 * @author Jordi SUC
 */
public class Profile {

	/**
	 * the attributes names
	 */
	protected static final String idAtt="id", rightForTagModificationAtt="rightForTagModification", 
		rightForViewLoadingAtt="rightForViewLoading", rightForRecipeLoadingAtt="rightForRecipeLoading",
		rightForRecipeModificationAtt="rightForRecipeModificationAtt", 
		rightForProgramLaunchingAtt="rightForProgramLaunching", timeOutAtt="timeout";

	/**
	 * the id
	 */
	private String id="";
	
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
	 * the time out before which the logged 
	 * user will be disconnected
	 */
	private int timeOut=0;
	
	/**
	 * the constructor of the class
	 * @param element the element describing the profile
	 */
	public Profile(Element element){
		
		id=element.getAttribute(idAtt);
		
		try{
			rightForTagModification=Integer.parseInt(
				element.getAttribute(rightForTagModificationAtt));
		}catch (Exception ex){}
		
		try{
			rightForViewLoading=Integer.parseInt(
				element.getAttribute(rightForViewLoadingAtt));
		}catch (Exception ex){}
		
		try{
			rightForRecipeLoading=Integer.parseInt(
				element.getAttribute(rightForRecipeLoadingAtt));
		}catch (Exception ex){}
		
		try{
			rightForRecipeModification=Integer.parseInt(
				element.getAttribute(rightForRecipeModificationAtt));
		}catch (Exception ex){}
		
		try{
			rightForProgramLaunching=Integer.parseInt(
				element.getAttribute(rightForProgramLaunchingAtt));
		}catch (Exception ex){}
		
		try{
			timeOut=Integer.parseInt(
				element.getAttribute(timeOutAtt));
		}catch (Exception ex){}
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
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
	 * @return the time out before which the logged 
	 * user will be disconnected
	 */
	public int getTimeOut() {
		
		return timeOut;
	}
}
