package fr.itris.glips.rtda.action;

import java.awt.Container;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.test.*;
import fr.itris.glips.rtda.widget.*;

/**
 *the class used to change a value
 * @author ITRIS, Jordi SUC
 */
public abstract class AbstractSendMeasure extends fr.itris.glips.rtda.animaction.Action{

	/**
	 * the string names
	 */
	protected static String tagToWriteName="tagToWrite", 
			returnToInitialValueMethodName="returnToInitialValueMethod",
					autoName="auto";
	
    /**
     * the tag min and tag max
     */
	protected DataLimit tagMin, tagMax;

	/**
	 * the constructor of the class
	 * @param picture the svg picture this action is linked to
	 * @param projectName the name of the project this action is linked to
	 * @param parent the parent container
	 * @param component the component on which the action is registered
	 * @param actionObject the object to which the action applies, if it is not the provided component
	 * @param actionElement the dom element defining the action
	 * @param jwidgetRuntime the jwidget runtime object, if this action is linked to a jwidget
	 */
	public AbstractSendMeasure(SVGPicture picture, String projectName, Container parent, 
		JComponent component, Object actionObject, Element actionElement, 
			JWidgetRuntime jwidgetRuntime) {
		
		super(picture, projectName, parent, component, actionObject, 
				actionElement, jwidgetRuntime);
		computeRightsForTagsModif();
	}
	
	/**
	 * initializes the action
	 */
	protected void initializeAction() {
		
		//getting the tag names to handle
        dataNames.add(actionElement.getAttribute(tagToWriteName));

        //getting the tagMin and tagMax
        tagMin=new DataLimit(picture, actionElement.getAttribute(tagToWriteName), 
        		actionElement.getAttribute("tagMin"), true);
        tagMax=new DataLimit(picture, actionElement.getAttribute(tagToWriteName), 
        		actionElement.getAttribute("tagMax"), false);
        
        //getting the tag min and tag max values
        registersLimit(tagMin);
        registersLimit(tagMax);

        //if we are in the test version, we store information on the tags
        if(picture.getMainDisplay().isTestVersion()){
        	
        	//adding the information object for the test for the tag to write
            TestTagInformation info=new TestTagInformation(
            		picture, actionElement.getAttribute(tagToWriteName), null);
            dataNamesToTestTagInfo.put(actionElement.getAttribute(tagToWriteName), info);
            
            registersLimitForTest(tagMin);
            registersLimitForTest(tagMax);
        }
        
        initializeAuthorizationTag();
	}
	
	/**
	 * registers the limit
	 * @param limit a limit
	 */
	protected void registersLimit(DataLimit limit) {

        if(limit.isTag()){
            
        	dataNames.add(limit.getTag());
        }
	}
	
	/**
	 * registers the given limit for the test
	 * @param limit a limit
	 */
	protected void registersLimitForTest(DataLimit limit) {
		
        if(limit.isTag()){

        	TestTagInformation info=new TestTagInformation(
        			picture, limit.getTag(), null);
        	dataNamesToTestTagInfo.put(limit.getTag(), info);
        }
	}
	
	@Override
	public Runnable dataChanged(DataEvent evt) {
		
		super.dataChanged(evt);
		checkIsAuthorized();
        return null;
    }
    
    /**
     * returns the value of this limit
     * @param limit the limit
     * @return the limit value
     */
    protected double getLimitValue(DataLimit limit) {

    	double value=Double.NaN;

    	if(limit.isTag()){

    		if(dataNames.contains(limit.getTag())){

    			value=Toolkit.getNumber(getData(limit.getTag()));
    		}

    	}else if(limit.isAbsoluteValue()) {

    		value=limit.getAbsoluteValue();
    	}
    	
    	return value;
    }
    
	@Override
	public void performAction(Object evt) {
		
		if(isEntitled() && isAuthorized && showConfirmationDialog()){

			int returnValue=numberChooser.showNumberChooser(
					picture.getMainDisplay().getTopLevelFrame());
	    	
	    	if(returnValue==NumberChooser.OK_ACTION){
	    		
	    		double enteredNumber=numberChooser.getEnteredValue();

	    		if(! Double.isNaN(enteredNumber) && ! Double.isInfinite(enteredNumber)){

	            	//computing the limits
	            	double tagMinValue=getLimitValue(tagMin);
	            	double tagMaxValue=getLimitValue(tagMax);

                    if(! Double.isNaN(tagMinValue) && ! Double.isNaN(tagMaxValue) &&
                    		tagMaxValue>=tagMinValue && ! Double.isNaN(enteredNumber) && 
                    			enteredNumber>=tagMinValue && enteredNumber<=tagMaxValue){

            			//getting the old tag to write value
            			Object oldObj=getData(actionElement.getAttribute(tagToWriteName));
  
                		//setting the new value for the tag to write
                		putTagValue(actionElement.getAttribute(tagToWriteName), enteredNumber);
                		handleReturnToInitialValue(oldObj);
                    }
	    		}
	    	}
		}
	}
	
	/**
	 * handles the method for returning to the initial value
	 * @param oldValue an old value
	 */
	protected void handleReturnToInitialValue(final Object oldValue){
		
   		//handling the methods for returning to the initial value
		String returnToInitialValue=actionElement.getAttribute(returnToInitialValueMethodName);
		
		if(! returnToInitialValue.equals(autoName)) {
			
			//getting the value of the timer
			double timer=0;
			
        	try{
        		timer=Double.parseDouble(returnToInitialValue);
        	}catch (Exception ex){timer=Double.NaN;}
        	
        	if(! Double.isNaN(timer)) {
        		
            	final String tagToWrite=actionElement.getAttribute(tagToWriteName);
        		
            	//setting a timer            	
            	TimerTask timerTask=new TimerTask() {

            		@Override
            		public void run() {
            			
            			putTagValue(tagToWrite, oldValue);
            		}
            	};
            	
            	AnimationsHandler.getTimer().schedule(timerTask, (long)(timer*1000));
        	}
		}
	}
}
