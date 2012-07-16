package fr.itris.glips.extension.jwidget.trends.runtime.controller;

import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.model.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the class that is the link between the model and the view parts
 * and which provides methods for interacting with the trends widgets
 * @author ITRIS, Jordi SUC
 */
public class TrendsRuntimeController {

	/**
	 * the jwidget runtime object that has created the trends widget
	 */
	private TrendsRuntime jwidgetRuntime;
	
	/**
	 * the object that stores all the configuration properties values
	 */
	private TrendsConfiguration configuration;
	
	/**
	 * the trends runtime model
	 */
	private TrendsRuntimeModel model;
	
	/**
	 * the trends runtime view
	 */
	private TrendsRuntimeView view;
	
	/**
	 * whether the jwidget is disposed or not
	 */
	private boolean isJWidgetDisposed=false;
	
	/**
	 * the constructor of the class
	 * @param jwidgetRuntime the jwidget runtime object that has created the trends widget
	 */
	public TrendsRuntimeController(TrendsRuntime jwidgetRuntime){
		
		this.jwidgetRuntime=jwidgetRuntime;
		
		//creating the object that stores all the configuration properties values
		configuration=new TrendsConfiguration(this, jwidgetRuntime.getJWidgetElement());
		
		//creating the trends model
		model=new TrendsRuntimeModel(this);
		
		//creating the trends view part
		view=new TrendsRuntimeView(this);
	}
	
	/**
	 * initializing the trends widget
	 */
	public void initialize(){
		
		model.initialize();
		view.initialize();
	}
	
	/**
	 * @return whether the jwidget is disposed or not
	 */
	public boolean isJWidgetDisposed() {
		return isJWidgetDisposed;
	}

	/**
	 * disposes the component
	 */
	public void dispose(){//TODO
		
		synchronized(this){
			
			isJWidgetDisposed=true;
		}
		
		view.dispose();
		model.dispose();
		
		
		jwidgetRuntime=null;
		configuration=null;
		model=null;
		view=null;
	}

	/**
	 * registers a new tag to that the trends widget receives updates from it
	 * @param tagName a tag name
	 * @param isEnumerated whether this tag is an enumerated one
	 */
	public void registerTag(String tagName, boolean isEnumerated){
		
		if(tagName!=null && ! tagName.equals("")){
			
			jwidgetRuntime.registerTag(tagName, isEnumerated);
		}
	}
	
	/**
	 * returns the value of a tag given its name 
	 * @param tagName a tag name
	 * @return the value of a tag given its name 
	 */
	public Object getTagValue(String tagName){

		return jwidgetRuntime.getTagValue(tagName);
	}

	/**
	 * @return the object that stores all the configuration properties values
	 */
	public TrendsConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @return the jwidget runtime object that has created the trends widget
	 */
	public JWidgetRuntime getJwidgetRuntime() {
		return jwidgetRuntime;
	}

	/**
	 * @return the trends runtime model
	 */
	public TrendsRuntimeModel getModel() {
		return model;
	}
	
	/**
	 * @return the trends runtime view part
	 */
	public TrendsRuntimeView getView() {
		return view;
	}
	
	/**
	 * @return the trends component
	 */
	public JComponent getTrendsComponent(){
		
		return view.getTrendsComponent();
	}
}
