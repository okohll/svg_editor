package fr.itris.glips.extension.jwidget.trends.runtime;

import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;

import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import fr.itris.glips.extension.jwidget.trends.edition.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.controller.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.test.*;

/**
 * the class of the view browser runtime jwidget
 * @author ITRIS, Jordi SUC
 */
public class TrendsRuntime extends JWidgetRuntime{

	/**
	 * the jwidget id
	 */
	public static String jwidgetId="TrendsWidget";
	
	/**
	 * the controller
	 */
	private TrendsRuntimeController controller;
	
	/**
	 * whether the component has been initialized
	 */
	private boolean initialized=false;
	
    /**
     * the constructor of the class
     * @param picture the svg picture
     * @param element the svg element defining the jwidget
     */
    public TrendsRuntime(SVGPicture picture, Element element){
		
		super(picture, element);
	}
	
    @Override
    public void initialize() {

    	//creating the trends controller
    	controller=new TrendsRuntimeController(this);
    	JComponent trendsComponent=controller.getTrendsComponent();
    	component=trendsComponent;
    }
    
    @Override
    public void initializeWhenCanvasDisplayed() {

    	controller.initialize();
    	
    	if(controller.getConfiguration().getStartMode()==
    		TrendsConfiguration.HISTORY_MODE && 
    		! controller.getConfiguration().getStartDateTag().equals("") && 
    			! controller.getConfiguration().getEndDateTag().equals("")){
    		
    		//updating the date tags values
    		Map<String, Object> map=new HashMap<String, Object>();
    		map.put(controller.getConfiguration().getStartDateTag(), "");
    		map.put(controller.getConfiguration().getEndDateTag(), "");
    		controller.getModel().updateTags(map);
    	}

    	synchronized (this) {
    		initialized=true;
		}
    }
    
    @Override
    public Runnable dataChanged(DataEvent evt) {

    	if(initialized){

        	//updating the model
        	controller.getModel().updateTags(evt.getDataNameToValue());
    	}
    	
    	return null;
    }
    
	/**
	 * registers a new tag to that the trends widget receives updates from it
	 * @param tagName a tag name
	 * @param isEnumerated whether this tag is an enumerated one
	 */
	public void registerTag(String tagName, boolean isEnumerated){
		
		dataNames.add(tagName);
		
        //if we are in the test version, we store information on the tag
        if(picture.getMainDisplay().isTestVersion()){
        	
    		//creating the test tag information object
        	TestTagInformation info=null;
        	
            if(isEnumerated){
            	
            	//getting the trends curve configuration corresponding to the tag name
            	TrendsCurveConfiguration trendsCurveConfiguration=
            		controller.getConfiguration().getTrendsCurveConfiguration(tagName);
            	
            	if(trendsCurveConfiguration!=null){
            		
            		//creating the new test tag information object
                    info=new TestTagInformation(picture, tagName,
                    		new LinkedList<String>(trendsCurveConfiguration.getEnumeratedTagValues()));
                    dataNamesToTestTagInfo.put(tagName, info);
            	}

            }else{
            	
                info=new TestTagInformation(picture, tagName, null);
                dataNamesToTestTagInfo.put(tagName, info);
            }
        }
	}
	
    @Override
    public void refreshComponentState() {}
	
	/**
	 * returns the value of the given tag name
	 * @param tagName a tag name
	 * @return the value of the given tag name
	 */
	public Object getTagValue(String tagName){
		
		return getPicture().getMainDisplay().
			getAnimationsHandler().getData(tagName);
	}
	
	@Override
	public void dispose() {//TODO

		controller.dispose();
		super.dispose();
		
		controller=null;
	}
	
	@Override
	public void registerListeners() {}
	
	@Override
	public void unregisterListeners() {}
	
    /**
     * @return the jwidget edition class linked to this jwidget runtime class
     */
    public static Class<?> getEditionClass(){
    	
    	return TrendsEdition.class;
    }
}
