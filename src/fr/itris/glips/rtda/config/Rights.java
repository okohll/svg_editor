package fr.itris.glips.rtda.config;

import java.util.*;
import org.w3c.dom.*;

/**
 * the class provided information on rights
 * @author Jordi SUC
 */
public class Rights {
	
	/**
	 * the element names
	 */
	protected static final String rightsName="rights", profileName="profile", 
		userName="user", tagForUserAttName="tagForUser";

	/**
	 * the map associating the id of a profile to this profile
	 */
	private Map<String, Profile> profiles=
		new HashMap<String, Profile>();
	
	/**
	 * the map associating the id of a user to this user
	 */
	private Map<String, User> users=
		new HashMap<String, User>();
	
	/**
	 * the tag name used to store the current user
	 */
	private String userTagName="";
	
	/**
	 * the constructor of the class
	 * @param rootElement the root element of the database
	 */
	public Rights(Element rootElement){

		if(rootElement!=null){
			
			//getting the element describing the rights
			NodeList rightsElements=
				rootElement.getElementsByTagName(rightsName);

			if(rightsElements!=null && rightsElements.getLength()>0){
				
				Element rightsElement=(Element)rightsElements.item(0);

				if(rightsElement!=null){
					
					//getting the tag for storing the user name
					userTagName=rightsElement.getAttribute(tagForUserAttName);
					
					//creating the profile and user managers
					NodeList profileElements=
						rootElement.getElementsByTagName(profileName);
					Element element=null;
					Profile userProfile=null;
					
					for(int i=0; i<profileElements.getLength(); i++){
						
						element=(Element)profileElements.item(i);
						userProfile=new Profile(element);
						profiles.put(userProfile.getId(), userProfile);
					}
					
					NodeList userElements=
						rootElement.getElementsByTagName(userName);
					User user=null;
					
					for(int i=0; i<userElements.getLength(); i++){
						
						element=(Element)userElements.item(i);
						user=new User(element);
						users.put(user.getId(), user);
					}
				}
			}
		}
	}
	
	/**
	 * returns the user corresponding to the provided login
	 * @param login a login
	 * @return the object giving information on the user
	 */
	public User getUser(String login){
		
		return users.get(login);
	}
	
	/**
	 * returns the profile corresponding to the provided user
	 * @param user a user
	 * @return the object giving information on the profile of a user
	 */
	public Profile getProfile(User user){
		
		return profiles.get(user.getProfile());
	}
	
	/**
	 * @return the tag name used to store the current user
	 */
	public String getUserTagName() {
		return userTagName;
	}
}
