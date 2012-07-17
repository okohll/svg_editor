package fr.itris.glips.library;

import java.util.prefs.*;

/**
 * the class that manages the preferences
 * @author Jordi SUC
 */
public class PreferencesStore {

    /**
     * a user preference node
     */
    private static Preferences preferences;
    
    static{
    	
        //getting the preference node
        //try{
        	preferences=Preferences.userNodeForPackage(
        			PreferencesStore.class);
        //}catch(Exception ex){}
    }
    
    /**
     * returns the preference value corresponding to this id
     * @param id the id of a preference
     * @return the value corresponding to this id
     */
    public static Preferences getPreferenceNode(String id){
    	
    	if(preferences!=null){
    		
    		return preferences.node(id);
    	}
    	
    	return null;
    }
    
    /**
     * returns the preference value corresponding to this id
     * @param nodeId the id of the node on which the preference should be retrieved
     * (if null or empty the default node will be taken)
     * @param id the id of a preference
     * @return the value corresponding to this id
     */
    public static String getPreference(String nodeId, String id){
    	
    	//try{
        	Preferences node=null;
        	
        	if(nodeId==null || nodeId.equals("")){
        		
        		node=preferences;
        		
        	}else{
        		
        		node=preferences.node(nodeId);
        	}
        	
        	return node.get(id, null);
        	
    	//}catch (Exception ex){}

    //	return null;
    }
    
    /**
     * set the preference value corresponding to this id
     * @param nodeId the id of the node on which the preference should be set
     * (if null or empty the default node will be taken)
     * @param id the id of a preference
     * @param value the new value of a preference
     */
    public static void setPreference(String nodeId, String id, String value){
    	
    	try{
        	Preferences node=null;
        	
        	if(nodeId==null || nodeId.equals("")){
        		
        		node=preferences;
        		
        	}else{
        		
        		node=preferences.node(nodeId);
        	}
        	
        	node.put(id, value);
        	node.flush();
        	
    	}catch (Exception ex){ex.printStackTrace();}
    }
}
