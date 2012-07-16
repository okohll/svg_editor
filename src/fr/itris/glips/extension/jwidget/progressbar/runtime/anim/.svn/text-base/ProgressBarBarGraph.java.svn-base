package fr.itris.glips.extension.jwidget.progressbar.runtime.anim;

import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.test.*;

/**
 * the bar graph animation for the progress bar
 * @author Jordi SUC
 */
public class ProgressBarBarGraph extends JWidgetAnimation{
	
	/**
	 * the string names
	 */
	private static String tagAttributeName="tag", tagMinAttributeName="tagMin", 
		tagMaxAttributeName="tagMax";

	/**
     * the tag min and tag max
     */
    private DataLimit tagMin, tagMax;
    
	/**
	 * the computed value of the tag, tagMin and tagMax attributes
	 */
	private double tagValue=Double.NaN, tagMinValue=Double.NaN, tagMaxValue=Double.NaN;
	
    /**
     * the constructor of the class
     * @param jwidgetRuntime the associated jwidget runtime object
     * @param component the component to animate
     * @param animationElement the animation element
     */
    public ProgressBarBarGraph(JWidgetRuntime jwidgetRuntime, 
    		JComponent component, Element animationElement) {
    	
    	super(jwidgetRuntime, component, animationElement);
    	
   		//getting the tag name
		dataNames.add(animationElement.getAttribute(tagAttributeName));
		
	    //getting the tagMin and tagMax
        tagMin=new DataLimit(jwidgetRuntime.getPicture(), 
        		animationElement.getAttribute(tagAttributeName), 
        			animationElement.getAttribute(tagMinAttributeName), true);
        tagMax=new DataLimit(jwidgetRuntime.getPicture(), 
        		animationElement.getAttribute(tagAttributeName), 
        			animationElement.getAttribute(tagMaxAttributeName), false);
        
        //getting the tag min and tag max values
        registersLimit(tagMin);
        registersLimit(tagMax);

        //if we are in the test version, we store information on the tags
        if(jwidgetRuntime.getPicture().getMainDisplay().isTestVersion()){
        	
        	//adding the information object for the test for the tag to write
            TestTagInformation info=new TestTagInformation(
            		jwidgetRuntime.getPicture(), animationElement.getAttribute(tagAttributeName), null);
            dataNamesToInformation.put(
            		animationElement.getAttribute(tagAttributeName), info);
            registersLimitForTest(tagMin);
            registersLimitForTest(tagMax);
        }
    }
    
    /**
	 * registers the limit
	 * @param limit a limit
	 */
	protected void registersLimit(DataLimit limit) {
		
		if(limit.isAbsoluteValue()){
			
			if(limit.equals(tagMin)){
				
				tagMinValue=limit.getAbsoluteValue();
				
			}else{
				
				tagMaxValue=limit.getAbsoluteValue();
			}
		}
        
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
        			jwidgetRuntime.getPicture(), limit.getTag(), null);
        	dataNamesToInformation.put(limit.getTag(), info);
        }
	}
	
    @Override
    public Runnable dataChanged(DataEvent evt) {

		if(animationElement!=null){

			Map<String, Object> newMap=evt.getDataNameToValue();
			
			//computing the new values
			String tagAttributeValue=animationElement.getAttribute(tagAttributeName);
			
			//getting the value of the tag//
			if(newMap.containsKey(tagAttributeValue)){

				tagValue=Toolkit.getNumber(getData(tagAttributeValue));
			}

			if(! Double.isNaN(tagValue) && ! Double.isNaN(tagMaxValue) &&
				! Double.isNaN(tagMinValue) && tagMinValue<=tagMaxValue && 
				tagValue>=tagMinValue && tagValue<=tagMaxValue){
				
				double minValue=((JProgressBar)component).getMinimum();
				double maxValue=((JProgressBar)component).getMaximum();

				//computing the new value for the progress bar
				final double value=tagValue/(tagMaxValue-tagMinValue)*(maxValue-minValue);
				
				SwingUtilities.invokeLater(new Runnable(){
					
					public void run() {

						((JProgressBar)component).setValue((int)value);
					}
				});
			}
		}
		
        return null;
    }
}
