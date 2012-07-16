package fr.itris.glips.rtda.action.tagevent;

import java.util.*;
import javax.swing.*;
import fr.itris.glips.library.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.animaction.Action;
import fr.itris.glips.rtda.animaction.eventsmanager.*;
import fr.itris.glips.rtda.test.*;

/**
 * the events manager for the actions triggered on the modification of tags value
 * @author Jordi SUC
 */
public class TagEventsManager extends EventsManager{
	
	/**
	 * the analogic keyword constant
	 */
	public final static String analogicKeyword="/*analogic*/";
	
	/**
	 * the string keyword constant
	 */
	public final static String stringKeyword="/*string*/";
	
	/**
	 * the separator constant
	 */
	public final static String separator="|";
	
	/**
	 * the regex separator constant
	 */
	public final static String separatorRegex="[|]";
	
	/**
	 * the tag condition handler
	 */
	private TagConditionHandler tagConditionHandler;

	/**
	 * the constructor of the class
	 * @param action the action this events manager monitors
	 */
	public TagEventsManager(Action action){
		
		this.action=action;
		registerTag(action.getActionElement().getAttribute("tagEvent"));
	}
	
	@Override
	public void dataChanged(DataEvent evt) {
		
		if(tagConditionHandler!=null){
			
			tagConditionHandler.dataChanged(evt);
		}
	}
	
	/**
	 * registers the tag that is found in the provided tagEvent attribute
	 * @param value the value of the tag event attribute
	 */
	protected void registerTag(String value){
		
		//splitting the value
		String[] splitValues=value.split(TagEventsManager.separatorRegex);
		
		if(splitValues!=null){
			
			if(splitValues.length==4 && 
					splitValues[0].equals(TagEventsManager.analogicKeyword)){

				tagConditionHandler=new AnalogicTagConditionHandler(splitValues);
				
			}else if(splitValues.length==3 && 
					splitValues[0].equals(TagEventsManager.stringKeyword)){
				
				tagConditionHandler=new StringTagConditionHandler(splitValues);
				
			}else if(splitValues.length>1 && ! splitValues[1].equals("")){
				
				tagConditionHandler=new EnumeratedTagConditionHandler(splitValues);
			}
		}
	}
	
	/**
	 * the class of the handler for the enumerated tag condition
	 * @author Jordi SUC
	 */
	protected class EnumeratedTagConditionHandler extends TagConditionHandler{
		
		/**
		 * the list of the possible values of the tags that match the condition
		 */
		protected Set<String> possibleValuesSet=
			new HashSet<String>();
		
		/**
		 * the constructor of the class
		 * @param splitValues the array of the segments 
		 * of the string defining the condition
		 */
		protected EnumeratedTagConditionHandler(String[] splitValues){
			
			//getting the parameters for the condition//
			
			//getting the tag name
			tagName=splitValues[1];
			action.addData(tagName);
			LinkedList<String> realValues=new LinkedList<String>();
			
			if(splitValues.length>2){
				
				for(int i=2; i<splitValues.length; i++){
					
					realValues.add(splitValues[i]);
					possibleValuesSet.add(
						AnimationsToolkit.normalizeEnumeratedValue(splitValues[i]));
				}
			}
			
			if(action.getPicture().getMainDisplay().isTestVersion()){
				
				//registering the tag
				TestTagInformation info=new TestTagInformation(action.getPicture(), 
					tagName, realValues);
				action.getDataNamesToInformation().put(tagName, info);
			}
		}
		
		@Override
	    protected void dataChanged(DataEvent evt){
	    	
	    	if(evt.getDataNameToValue().containsKey(tagName)){
	    		
	    		//getting the current value of the tag
	    		Object currentValue=action.getData(tagName);
	    		
	    		if(currentValue!=null && 
	    				possibleValuesSet.contains(currentValue)){
	    			
	    			try{
		    			SwingUtilities.invokeAndWait(new Runnable(){
		    				
		    				public void run() {

		    	    			action.performAction(null);
		    				}
		    			});
	    			}catch (Exception ex){ex.printStackTrace();}
	    		}
	    	}
	    }
	}
	
	/**
	 * the class of the handler for the analogic tag condition
	 * @author Jordi SUC
	 */
	protected class AnalogicTagConditionHandler extends TagConditionHandler{

		/**
		 * the equality mode
		 */
		protected String equalityMode;
		
		/**
		 * the value of the tag for the condition
		 */
		protected double definedValue;
		
		/**
		 * the constructor of the class
		 * @param splitValues the array of the segments 
		 * of the string defining the condition
		 */
		protected AnalogicTagConditionHandler(String[] splitValues){
			
			//getting the parameters for the condition//
			
			//getting the tag name
			tagName=splitValues[1];
			action.addData(tagName);
			
			//getting the equality mode 
			equalityMode=splitValues[2];
			
			//getting the tag value
			String tagValue=splitValues[3];

			//computing the double tag value corresponding to the string
			try{
				definedValue=Double.parseDouble(tagValue);
			}catch (Exception ex){definedValue=Double.NaN;}
			
			if(action.getPicture().getMainDisplay().isTestVersion()){
				
				//registering the tag
				TestTagInformation info=new TestTagInformation(action.getPicture(), 
					tagName, null);
				action.getDataNamesToInformation().put(tagName, info);
			}
		}
		
		@Override
	    protected void dataChanged(DataEvent evt){
	    	
	    	if(evt.getDataNameToValue().containsKey(tagName) && 
	    			! Double.isNaN(definedValue)){
	    		
	    		//getting the current value of the tag
	    		Object currentValue=action.getData(tagName);
	    		
	    		if(currentValue!=null){
	    			
					//computing the double tag value corresponding to the value
	    			double dValue=Toolkit.getNumber(currentValue);
					
					if(! Double.isNaN(dValue) && EqualityChooserWidget.checkCondition(
							dValue, definedValue, equalityMode)){
						
		    			try{
			    			SwingUtilities.invokeAndWait(new Runnable(){
			    				
			    				public void run() {

			    	    			action.performAction(null);
			    				}
			    			});
		    			}catch (Exception ex){ex.printStackTrace();}
					}
	    		}
	    	}
	    }
	}
	
	/**
	 * the class of the handler for the string tag condition
	 * @author Jordi SUC
	 */
	protected class StringTagConditionHandler extends TagConditionHandler{
		
		/**
		 * the tag value for the condition
		 */
		protected String definedTagValue;

		/**
		 * the constructor of the class
		 * @param splitValues the array of the segments 
		 * of the string defining the condition
		 */
		protected StringTagConditionHandler(String[] splitValues){
			
			//getting the parameters for the condition//

			//getting the tag name
			tagName=splitValues[1];
			action.addData(tagName);

			//getting the equality mode 
			definedTagValue=splitValues[2];
			
			if(action.getPicture().getMainDisplay().isTestVersion()){
				
				//registering the tag
				TestTagInformation info=new TestTagInformation(action.getPicture(), 
					tagName, null);
				action.getDataNamesToInformation().put(tagName, info);
			}
		}
		
		@Override
	    protected void dataChanged(DataEvent evt){
	    	
	    	if(evt.getDataNameToValue().containsKey(tagName)){
	    		
	    		//getting the current value of the tag
	    		Object currentValue=action.getData(tagName);
	    		
	    		if(currentValue!=null && definedTagValue.equals(currentValue)){
	    			
	    			try{
		    			SwingUtilities.invokeAndWait(new Runnable(){
		    				
		    				public void run() {

		    	    			action.performAction(null);
		    				}
		    			});
	    			}catch (Exception ex){ex.printStackTrace();}
	    		}
	    	}
	    }
	}
	
	/**
	 * the abstract class of every conditions handlers
	 * 
	 * @author Jordi SUC
	 */
	protected class TagConditionHandler{
		
		/**
		 * the tag name
		 */
		protected String tagName;
		
		/**
	     * the method called when the data to which the action is registered, is modified.
	     * @param evt an event
	     */
	    protected void dataChanged(DataEvent evt){}
	}
	
}
