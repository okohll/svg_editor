package fr.itris.glips.rtda.jwidget;

import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.button.runtime.ButtonRuntime;
import fr.itris.glips.extension.jwidget.checkbox.runtime.CheckBoxRuntime;
import fr.itris.glips.extension.jwidget.combo.runtime.*;
import fr.itris.glips.extension.jwidget.externaldatatable.runtime.*;
import fr.itris.glips.extension.jwidget.list.runtime.*;
import fr.itris.glips.extension.jwidget.menubar.runtime.*;
import fr.itris.glips.extension.jwidget.popup.runtime.*;
import fr.itris.glips.extension.jwidget.progressbar.runtime.ProgressBarRuntime;
import fr.itris.glips.extension.jwidget.radiobuttonbar.runtime.RadioButtonBarRuntime;
import fr.itris.glips.extension.jwidget.slider.runtime.SliderRuntime;
import fr.itris.glips.extension.jwidget.spinner.runtime.SpinnerRuntime;
import fr.itris.glips.extension.jwidget.table.runtime.TableRuntime;
import fr.itris.glips.extension.jwidget.textfield.runtime.TextFieldRuntime;
import fr.itris.glips.extension.jwidget.togglebutton.runtime.ToggleButtonRuntime;
import fr.itris.glips.extension.jwidget.togglebuttonbar.runtime.ToggleButtonBarRuntime;
import fr.itris.glips.extension.jwidget.trends.runtime.TrendsRuntime;
import fr.itris.glips.extension.jwidget.viewbrowser.runtime.ViewBrowserRuntime;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.xml.parsers.*;

/**
 * the class of the builder of the jwidgets
 * @author ITRIS, Jordi SUC
 */
public class JWidgetRuntimeManager {

	/**
	 * strings
	 */
	protected static final String idSeparator="/";
	
	/**
	 * the map associating the id of a jwidget to its runtime class
	 */
	protected static final Map<String, Class<?>> jwidgetRuntimeClassesMap=
		new HashMap<String, Class<?>>();
	
	/**
	 * the map associating the id of a jwidget to its document
	 */
	protected static final Map<String, Document> jwidgetDocumentsMap=
		new HashMap<String, Document>();
	
	static {
		
		//loading each jwidget runtime class
		LinkedList<Class<?>> pluginClasses=PluginLoader.getPlugins(JWidgetRuntime.class);

		if(pluginClasses!=null) {
			
			//filling the map of the jwidget runtime classes
			String id="", jwidgetIdFieldName="jwidgetId";
			Field field=null;
			Object obj=null;
				
			for(Class<?> theClass : pluginClasses) {
				
				if(theClass!=null) {
					
					//getting the jwidget id field
					try{
						field=theClass.getField(jwidgetIdFieldName);
					}catch (Exception ex) {field=null;}
					
					if(field!=null) {
						
						try{
							obj=field.get(null);
						}catch (Exception ex) {obj=null;}
						
						if(obj!=null) {
							
							//getting the value of the id of the jwidget runtime class
							id=obj.toString();
							
							jwidgetRuntimeClassesMap.put(id, theClass);
						}
					}
				}
			}
		}
		
		//adding the inner jwidget classes to the list
		jwidgetRuntimeClassesMap.put(ButtonRuntime.jwidgetId, ButtonRuntime.class);
		jwidgetRuntimeClassesMap.put(CheckBoxRuntime.jwidgetId, CheckBoxRuntime.class);
		jwidgetRuntimeClassesMap.put(ToggleButtonRuntime.jwidgetId, ToggleButtonRuntime.class);
		jwidgetRuntimeClassesMap.put(ViewBrowserRuntime.jwidgetId, ViewBrowserRuntime.class);
		jwidgetRuntimeClassesMap.put(ComboRuntime.jwidgetId, ComboRuntime.class);
		jwidgetRuntimeClassesMap.put(ListRuntime.jwidgetId, ListRuntime.class);
		jwidgetRuntimeClassesMap.put(TextFieldRuntime.jwidgetId, TextFieldRuntime.class);
		jwidgetRuntimeClassesMap.put(SpinnerRuntime.jwidgetId, SpinnerRuntime.class);
		jwidgetRuntimeClassesMap.put(SliderRuntime.jwidgetId, SliderRuntime.class);
		jwidgetRuntimeClassesMap.put(TableRuntime.jwidgetId, TableRuntime.class);
		jwidgetRuntimeClassesMap.put(MenuBarRuntime.jwidgetId, MenuBarRuntime.class);
		jwidgetRuntimeClassesMap.put(PopUpRuntime.jwidgetId, PopUpRuntime.class);
		jwidgetRuntimeClassesMap.put(ProgressBarRuntime.jwidgetId, ProgressBarRuntime.class);
		jwidgetRuntimeClassesMap.put(ToggleButtonBarRuntime.jwidgetId, ToggleButtonBarRuntime.class);
		jwidgetRuntimeClassesMap.put(RadioButtonBarRuntime.jwidgetId, RadioButtonBarRuntime.class);
		jwidgetRuntimeClassesMap.put(TrendsRuntime.jwidgetId, TrendsRuntime.class);
		jwidgetRuntimeClassesMap.put(ExternalDataTableRuntime.jwidgetId, ExternalDataTableRuntime.class);
	}
	
	/**
	 * the set of the jwidget runtime objects
	 */
	protected Set<JWidgetRuntime> jwidgetRuntimeObjects=
		new HashSet<JWidgetRuntime>();
	
	/**
	 * creates a jwidget runtime object corresponding to the given jwidget element
	 * @param picture the picture to which the jwidget component is registered
	 * @param jwidgetElement a jwidget element
	 * @return a jwidget runtime object corresponding to the given jwidget element
	 */
	public JWidgetRuntime createJWidgetRuntime(
		SVGPicture picture, Element jwidgetElement) {
		
		JWidgetRuntime jwidgetRuntime=null;
		
		if(jwidgetElement!=null) {
			
			//getting the name of the jwidget that has to be created
			String name=jwidgetElement.getAttribute(JWidgetRuntime.jwidgetNameAttributeName);

			//checking if a plugin class matches the provided jwidget element//
			//getting the class corresponding to the name
			Class<?> jwidgetClass=jwidgetRuntimeClassesMap.get(name);
			
			if(jwidgetClass!=null) {
				
				//the array of the types of the constructor
				Class<?>[] types={SVGPicture.class, Element.class};
				
				try {
					
					//getting the constructor of this class
					Constructor<?> constructor=jwidgetClass.getConstructor(types);
					
					if(constructor!=null) {
						
						//creating the array of the parameters of the constructor
						Object[] parameters={picture, jwidgetElement};
						
						//creating the object
						jwidgetRuntime=(JWidgetRuntime)constructor.newInstance(parameters);
					}
					
				}catch (Exception ex) {}
			}
		}
		
		if(jwidgetRuntime!=null) {
			
			jwidgetRuntimeObjects.add(jwidgetRuntime);
		}
		
		return jwidgetRuntime;
	}
	
	/**
	 * creates the animation corresponding to the jwidget runtime object and to the animation element
	 * @param jwidgetRuntime a jwidget runtime object
	 * @param animationElement the animation element defining the action
	 * @return the animation corresponding to the jwidget runtime object and to the animation element
	 */ 
	public static JWidgetAnimation createAnimation(
			JWidgetRuntime jwidgetRuntime, Element animationElement) {
		
		JWidgetAnimation anim=null;
		
		if(jwidgetRuntime!=null && animationElement!=null) {
			
			anim=jwidgetRuntime.createAnimation(animationElement);
		}

		return anim;
	}
	
	/**
	 * creates the action corresponding to the jwidget runtime object and to the action element
	 * @param jwidgetRuntime a jwidget runtime object
	 * @param actionElement the element defining the action
	 * @return the action corresponding to the jwidget runtime object and to the action element
	 */ 
	public static fr.itris.glips.rtda.animaction.Action createAction(
			JWidgetRuntime jwidgetRuntime, Element actionElement) {
		
		fr.itris.glips.rtda.animaction.Action action=null;
		
		if(jwidgetRuntime!=null && actionElement!=null) {
			
			action=jwidgetRuntime.createAction(actionElement);
		}

		return action;
	}
	
	/**
	 * @return the jwidgetRuntimeObjects
	 */
	public Set<JWidgetRuntime> getJWidgetRuntimeObjects() {
		
		return new HashSet<JWidgetRuntime>(jwidgetRuntimeObjects);
	}
	
	/**
	 * removes the jwidget runtime object for the set of all of them
	 * @param jwidgetRuntimeObject a jwidget runtime object
	 */
	public void removeJWidgetRuntimeObject(JWidgetRuntime jwidgetRuntimeObject) {
		
		jwidgetRuntimeObjects.remove(jwidgetRuntimeObject);
	}
	
	/**
	 * returns the jwidget runtime object corresponding to the given document and id
	 * @param id the id of a jwidget runtime object
	 * @return the jwidget runtime object corresponding to the given document and id
	 */
	public JWidgetRuntime getJWidgetRuntimeObject(String id) {
		
		JWidgetRuntime jwidgetRuntime=null;
		
		//normalizing the id 
		int pos=id.indexOf(idSeparator);
		
		if(pos!=-1) {
			
			id=id.substring(0, pos);
		}
		
		//getting the corresponding jwidget runtime object
		for(JWidgetRuntime jwr : 
			new HashSet<JWidgetRuntime>(jwidgetRuntimeObjects)) {
			
			if(jwr!=null && id.equals(jwr.getId())) {
				
				jwidgetRuntime=jwr;
				break;
			}
		}
		
		return jwidgetRuntime;
	}
	
	/**
	 * loads all the jwidget xml descriptions
	 */
	public static void loadAllJWidgetXMLDescriptions(){
		
		for(String id : jwidgetRuntimeClassesMap.keySet()){
			
			getJWidgetXMLDescription(id);
		}
	}
	
	/**
	 * @return the map associating the id of a 
	 * jwidget to its description document
	 */
	public static Map<String, Document> getJWidgetDocumentsMap() {
		
		return jwidgetDocumentsMap;
	}
	
	/**
	 * @return the map associating the id of jwidet to its runtime class
	 */
	public static Map<String, Class<?>> getJWidgetRuntimeClassesMap() {
		return jwidgetRuntimeClassesMap;
	}
	
	/**
	 * returns the XML description of the jwidget denoted by the provided id
	 * @param id the id of a jwidget
	 * @return the XML description of the jwidget denoted by the provided id
	 */
	public static Document getJWidgetXMLDescription(String id){

		Document doc=jwidgetDocumentsMap.get(id);
		
		if(doc==null){
			
			//getting the jwidget runtime class corresponding to this id
			Class<?> jwidgetRuntimeClass=jwidgetRuntimeClassesMap.get(id);

			if(jwidgetRuntimeClass!=null){
				
				//getting the jwidget edition class corresponding to this jwidget runtime class
				Class<?> jwidgetEditionClass=null;
				
				try{
					Method method=
						jwidgetRuntimeClass.getMethod("getEditionClass", (Class<?>[])null);
					
					if(method!=null){
						
						jwidgetEditionClass=(Class<?>)method.invoke(null, (Object[])null);
					}
				}catch (Exception ex){}
			
				if(jwidgetEditionClass!=null){

					//retrieving the document
					InputStream inputStream=
						jwidgetEditionClass.getResourceAsStream("xml/"+id+".xml");

					if(inputStream!=null){
						
			        	DocumentBuilderFactory docBuildFactory=
			        		DocumentBuilderFactory.newInstance();

			            try{
			                //parses the XML file
			                DocumentBuilder docBuild=docBuildFactory.newDocumentBuilder();
			                doc=docBuild.parse(inputStream);
			            }catch (Exception ex){ex.printStackTrace();}
			            
			            if(doc!=null){
			            	
			            	jwidgetDocumentsMap.put(id, doc);
			            }
					}
				}
			}
		}
		
		return doc;
	}
}
