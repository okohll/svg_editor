package fr.itris.glips.rtdaeditor.anim;

import java.util.*;
import org.w3c.dom.*;

/**
 * the class handling a child element in a rtda animation
 * @author ITRIS, Jordi SUC
 */
public class ChildObject extends ItemObject{

	/**
	 * the child animation element
	 */
	private Element childAnimationElement=null;
	
    /**
     * the list of the attributes
     */
    private LinkedList<AttributeObject> attributesList=new LinkedList<AttributeObject>();
    
    /**
     * the constructor of the class
     * @param anim the animation object that contains this child object
     * @param childAnimationElement the element of an animation child
     * @param specChildAnimationElement the specification of the child animation element
     */
    public ChildObject(AnimationObject anim, Element childAnimationElement, Element specChildAnimationElement) {
        
    	this.childAnimationElement=childAnimationElement;
    	
        //creating the attribute objects
    	NodeList attributes=specChildAnimationElement.getElementsByTagName("attribute");
    	Element cur=null;
    	LinkedList<Element> groupElements=null;
    	String groupValue="", currentGroup="", name="";
    	LinkedList<String> attributeNames=new LinkedList<String>();
    	Map<String, AttributeObject> tmpAttributes=new HashMap<String, AttributeObject>();
    	
    	for(int i=0; i<attributes.getLength(); i++){
    		
    		cur=(Element)attributes.item(i);
    		
      		if(cur!=null){

        		if(cur.hasAttribute("group")){
        			
        			groupValue=cur.getAttribute("group");
        			
        			//the attribute belongs to a group//
        			if(groupElements!=null && groupElements.size()>0 && 
        				currentGroup!=null &&! currentGroup.equals(groupValue)){
        				
        				//handles the nodes of the previous group
        				tmpAttributes.put(currentGroup, 
        							new AttributeObject(anim, childAnimationElement, groupElements));
        				groupElements=null;
        				currentGroup=null;
        			}
        			
    				//if a new group starts
    				if(groupElements==null){
    					
    					groupElements=new LinkedList<Element>();
    					currentGroup=groupValue;
    					attributeNames.add(currentGroup);
    				}
    				
    				//adding the element to the list of the elements being in the same group
    				groupElements.add(cur);
        			
        		}else{

        			name=cur.getAttribute("name");
        			attributeNames.add(name);
        			tmpAttributes.put(name, new AttributeObject(anim, childAnimationElement, cur));
        		}
    		}
    	}
    	
    	//validating the remaining group attributes if they exist
		if(groupElements!=null && groupElements.size()>0){
			
			//handles the nodes of the previous group
			tmpAttributes.put(currentGroup, new AttributeObject(anim, childAnimationElement, groupElements));
			attributeNames.add(currentGroup);
		}
		
		//filling the list of the attribute objects
		AttributeObject attObj=null;
		
		for(String attName : attributeNames){
			
			attObj=tmpAttributes.get(attName);
			
			if(attObj!=null){
				
				attributesList.add(attObj);
			}
		}
    }
    
    /**
     * sets the value of the tag attribute
     * @param value a value
     */
    public void setTagValue(String value){
    	
    	if(value==null){
    		
    		value="";
    	}

    	for(AttributeObject attributeObject : attributesList){

    		if(attributeObject.getConstraint().equals("tagvalue")){
    			
    			attributeObject.setValue(value);
    			attributeObject.validateChanges();

    			break;
    		}
    	}
    }

    /**
     * @return Returns the attributesList.
     */
    public LinkedList<AttributeObject> getAttributesList() {
        return attributesList;
    }

	/**
	 * @return the childAnimationElement
	 */
	public Element getChildAnimationElement() {
		return childAnimationElement;
	}

}
