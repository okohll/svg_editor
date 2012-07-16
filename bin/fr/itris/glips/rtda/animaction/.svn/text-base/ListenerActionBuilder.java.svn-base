package fr.itris.glips.rtda.animaction;

import java.awt.Container;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.action.svg.*;
import fr.itris.glips.rtda.action.tagevent.*;
import fr.itris.glips.rtda.animations.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.resources.*;

/**
 * the listeners and actions builder
 * @author ITRIS, Jordi SUC
 */
public class ListenerActionBuilder {

	/**
	 * the animations and actions document
	 */
	protected static Document rtdaAnimationsAndActionsDocument=
				RtdaResources.getXMLDocument("rtdaAnimationsAndActions.xml");
    
    /**
     * the set containing the name of the supported animation nodes
     */
    protected static Set<String> animationNames=new HashSet<String>();
    
    /**
     * the set containing the name of the supported action nodes
     */
    protected static Set<String> actionNames=new HashSet<String>();
    
    static{
        
    	//getting all the animation and actions nodes in the document and computing their names
    	NodeList animations=rtdaAnimationsAndActionsDocument.getElementsByTagName("animation");
    	NodeList actions=rtdaAnimationsAndActionsDocument.getElementsByTagName("action");

    	for(int i=0; i<animations.getLength(); i++) {

    		animationNames.add(((Element)animations.item(i)).getAttribute("name"));
    	}
    	
    	String name="";
    	
    	for(int i=0; i<actions.getLength(); i++) {

    		name=((Element)actions.item(i)).getAttribute("name");
    		actionNames.add(name);
    	}
    }
    
    /**
     * returns whether the given node name is the name of an animation or an action name
     * @param nodeName the name of an animation or an action
     * @return whether the given node name is the name of an animation or an action name
     */
    public static boolean isAnimationNodeSupported(String nodeName){
        
        return animationNames.contains(nodeName);
    }
    
    /**
     * returns whether the node name corresponds to an action or not
     * @param nodeName the name of a node
     * @return whether the node name corresponds to an action or not
     */
    public static boolean isActionNode(String nodeName){
        
        return actionNames.contains(nodeName);
    }
    
    /**
     * creates a data changed listener
     * @param picture a svg picture
     * @param animationElement the animation element
     * @return the creates data changed listener
     */
    public static DataChangedListener createDataChangedListener(
    		SVGPicture picture, Element animationElement){
        
        DataChangedListener dataChangedListener=null;
        
        if(picture!=null && animationElement!=null){

        	String nodeName=animationElement.getNodeName();
        	
            if(nodeName.equals("rtda:colorOnState")){
                
                dataChangedListener=new ColorOnState(picture, animationElement);

            }else  if(nodeName.equals("rtda:colorOnMeasure")){
                
                dataChangedListener=new ColorOnMeasure(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:bargraph")){
                
                dataChangedListener=new BarGraph(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:attributeOnState")){
                
                dataChangedListener=new AttributeOnState(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:attributeOnInterval")){
                
                dataChangedListener=new AttributeOnInterval(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:attributeOnMeasure")){
                
                dataChangedListener=new AttributeOnMeasure(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:translation")){
                
                dataChangedListener=new Translation(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:rotation")){
                
                dataChangedListener=new Rotation(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:scale")){
                
                dataChangedListener=new Scale(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:text")){
                
                dataChangedListener=new fr.itris.glips.rtda.animations.Text(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:label")){
                
                dataChangedListener=new Label(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:measureText")){
                
                dataChangedListener=new MeasureText(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:stateSymbol")){
                
                dataChangedListener=new StateSymbol(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:measureSymbol")){
                
                dataChangedListener=new MeasureSymbol(picture, animationElement);
                
            }else  if(nodeName.equals("rtda:user")){
                
                dataChangedListener=new CurrentUser(picture, animationElement);
            }
        }
        
        return dataChangedListener;
    }
    
    /**
     * creates an returns the action corresponding to the given action element
     * @param picture the svg picture this action is linked to
     * @param projectName the name of the project this action is linked to
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
     * @return the action corresponding to the given action element
     */
    public static Action createAction(SVGPicture picture, String projectName, JComponent component, 
    			Object actionObject, Element actionElement) {
    	
    	Action action=null;
    	
    	if(component!=null && actionElement!=null) {
    		
    		String nodeName=actionElement.getNodeName();
    		
    		//checking if the action denotes a tag event action
    		Element parentElement=(Element)actionElement.getParentNode();
    		boolean isTagEvent=parentElement.getNodeName().equals(Toolkit.svgNodeName);

    		if(nodeName.equals("rtda:simpleSendCommand")) {
    			
    			if(isTagEvent){
    				
        			action=new TagEventSimpleSendCommand(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    				
    			}else{
    				
        			action=new SimpleSendCommand(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    			}
    		
    		}else if(nodeName.equals("rtda:sendCommand")) {
    			
    			if(isTagEvent){
    				
        			action=new TagEventSendCommand(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    				
    			}else{
    				
        			action=new SendCommand(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    			}
    			
    		}else if(nodeName.equals("rtda:sendMeasure")) {
    			
    			if(isTagEvent){
    				
        			action=new TagEventSendMeasure(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    				
    			}else{
    				
        			action=new SendMeasure(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    			}

    		}else if(nodeName.equals("rtda:loadView")) {
    			
    			if(isTagEvent){
    				
        			action=new TagEventLoadView(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    				
    			}else{
    				
        			action=new LoadView(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    			}
    			
    		}else if(nodeName.equals("rtda:runApplication")) {
    			
    			if(isTagEvent){
    				
        			action=new TagEventRunApplication(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    				
    			}else{
    				
        			action=new RunApplication(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    			}
    			
    		}else if(nodeName.equals("rtda:login")) {
    			
    			if(isTagEvent){
    				
        			action=new TagEventLogin(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    				
    			}else{
    				
        			action=new Login(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    			}
    			
    		}else if(nodeName.equals("rtda:disconnect")) {
    			
    			if(isTagEvent){
    				
        			action=new TagEventDisconnect(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    				
    			}else{
    				
        			action=new Disconnect(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    			}
    			
    		}else if(nodeName.equals("rtda:loadData")) {
    			
    			if(isTagEvent){
    				
        			action=new TagEventLoadData(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    				
    			}else{
    				
        			action=new LoadData(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    			}
    			
    		}else if(nodeName.equals("rtda:recordData")) {
    			
    			if(isTagEvent){
    				
        			action=new TagEventRecordData(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    				
    			}else{
    				
        			action=new RecordData(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    			}
    			
    		}else if(nodeName.equals("rtda:writeDataToFile")) {
    			
    			if(isTagEvent){
    				
        			action=new TagEventWriteDataToFile(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    				
    			}else{
    				
        			action=new WriteDataToFile(picture, projectName, component.getTopLevelAncestor(), 
            				component, actionObject, actionElement, null);
    			}

    		}else if(nodeName.equals("rtda:class")){
    			
    			//retrieving the class name in the action element
    			String className=actionElement.getAttribute(CustomAction.classAttributeName);
    			
    			//getting the class corresponding to the given class name
    			Class<?> theClass=ActionsLoader.getClass(CustomAction.class, 
    					picture.getCanvas().getProjectFile(), className, false);
    			
    			if(theClass!=null) {

    				//creating an instance of the class
    				Class<?>[] classParam={SVGPicture.class, String.class, Container.class, 
    													JComponent.class, Object.class, Element.class};
    				Object[] params={picture, projectName, component.getTopLevelAncestor(), 
    												component, actionObject, actionElement};
    				
    				try {
    					action=(fr.itris.glips.rtda.animaction.Action)
    						theClass.getConstructor(classParam).newInstance(params);
    				}catch (Exception ex) {}
    			}
    		}
    	}
    	
    	return action;
    }
}
