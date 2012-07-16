package fr.itris.glips.rtda.config;

import org.w3c.dom.*;

/**
 * the class storing information on a user
 * @author Jordi SUC
 */
public class User {

	/**
	 * the attributes names
	 */
	protected static final String idAtt="id", passwordAtt="password", 
		profileAtt="profile";

	/**
	 * the element containing information on the user
	 */
	protected Element element;
	
	/**
	 * the constructor of the class
	 * @param element the element describing the user
	 */
	public User(Element element){
		
		this.element=element;
	}
	
	/**
	 * @return the id
	 */
	public String getId(){
		
		return element.getAttribute(idAtt);
	}
	
	/**
	 * @return the password
	 */
	public String getPassword(){
		
		return element.getAttribute(passwordAtt);
	}
	
	/**
	 * @return the profile
	 */
	public String getProfile(){
		
		return element.getAttribute(profileAtt);
	}
}
