/*
 * Created on 27 janv. 2005
 */
package fr.itris.glips.extension.jwidget.base.runtime.anim;

import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.test.*;

/**
 *  the animation used to modify the text on a 
 *  button according to an enumerated tag value
 * @author ITRIS, Jordi SUC
 */
public class AbstractButtonTextOnState extends JWidgetAnimation{

	/**
	 * attribute names
	 */
	private final static String tagAtt="tag", invalidTextAtt="invalidText", 
		defaultTextAtt="defaultText", textAtt="text", usedAtt="used", valueAtt="value";
	
    /**
     * the map of the text labels associating a possible value of a tag to its corresponding label
     */
    private Map<String, String> labelsMap=new HashMap<String, String>();

    /**
     * the constructor of the class
     * @param jwidgetRuntime the associated jwidget runtime object
     * @param component the component to animate
     * @param animationElement the animation element
     */
    public AbstractButtonTextOnState(JWidgetRuntime jwidgetRuntime, 
    		JComponent component, Element animationElement) {
    	
    	super(jwidgetRuntime, component, animationElement);

        //getting the name specified in the "tag" attribute 
    	//and adding it to the set of the data names
        dataNames.add(animationElement.getAttribute(tagAtt));
        
        //fills the maps with the text values
        String value="", text="";
        Element childElement;
        boolean isUsed=false;
        LinkedList<String> realValuesNames=new LinkedList<String>();

        for(Node cur=animationElement.getFirstChild(); 
        	cur!=null; cur=cur.getNextSibling()){
            
            if(cur instanceof Element){
            	
            	childElement=(Element)cur;
            	
                //whether the child should be used or not
        		isUsed=Boolean.parseBoolean(childElement.getAttribute(usedAtt));

                if(isUsed){
                    
                    //getting one of the possible values of the tag
                    value=childElement.getAttribute(valueAtt);
                    
                    if(! value.equals("")){
                        
                        //getting the text value for the given tag value
                        text=childElement.getAttribute(textAtt);
                        realValuesNames.add(value);
                        value=AnimationsToolkit.normalizeEnumeratedValue(value);
                        labelsMap.put(value, text);
                    }
                }
            }
        }
        
        //if we are in the test version, we store information on the tag
        if(jwidgetRuntime.getPicture().getMainDisplay().isTestVersion()){
            
            TestTagInformation info=new TestTagInformation(
            		jwidgetRuntime.getPicture(), animationElement.getAttribute(tagAtt), realValuesNames);
            
            dataNamesToInformation.put(
            		animationElement.getAttribute(tagAtt), info);
        }
    }
    
    @Override
    public Runnable dataChanged(DataEvent evt) {
        
        //getting the value of the tag
        String value=(String)getData(
        		animationElement.getAttribute(tagAtt));
        
        //the new text value
        String newTextValue="";
        
        if(value==null){
        	
            //the state of the tag is invalid//
            newTextValue=animationElement.getAttribute(invalidTextAtt);
            
        }else if(labelsMap.containsKey(value)){

            newTextValue=labelsMap.get(value);
            
        }else{
            
            newTextValue=animationElement.getAttribute(defaultTextAtt);
        }

        if(newTextValue!=null){
        	
            final String fnewTextValue=newTextValue;

            try{
                SwingUtilities.invokeAndWait(new Runnable(){
                	
                	public void run() {

                		((AbstractButton)component).setText(fnewTextValue);
                	}
                });
            }catch (Exception ex){}
        }

        return null;
    }
}

