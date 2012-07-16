package fr.itris.glips.rtdaeditor.anim.widgets;

import javax.swing.*;
import java.io.*;

import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.anim.action.*;
import fr.itris.glips.svgeditor.*;
import java.awt.event.*;
import java.util.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import java.awt.*;
import org.w3c.dom.*;

/**
 * the class of the action chooser
 * @author ITRIS, Jordi SUC
 */
public class ActionChooser extends Widget{

	/**
	 * the combo
	 */
	private JComboBox combo=new JComboBox();
	
	/**
	 * the button used to configure the action
	 */
	private JButton configureButton;
	
	/**
	 * the combo listener
	 */
	private ActionListener comboListener, configureButtonListener;
	
	/**
	 * the current class for the configurator
	 */
	private Class<?> currentClass=null;
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected ActionChooser(boolean isEditor){
		
		super(isEditor);
		buildWidget();
	}
	
	/**
	 * builds the widget
	 */
	protected void buildWidget(){
		
		//getting the labels
		String configureLabel="";
		
		try {
			configureLabel=ResourcesManager.bundle.getString("rtdaanim_configure");
		}catch (Exception ex) {}
		
		//creating the configure button
		configureButton=new JButton(configureLabel);
		configureButton.setEnabled(false);
		
		setLayout(new BorderLayout(1, 0));
		add(combo, BorderLayout.CENTER);
		add(configureButton, BorderLayout.EAST);

		if(isEditor) {
			
			//adding the listener to the combo
			comboListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {
					
					if(getItem()!=null && combo.getSelectedItem()!=null){
						
						ComboListItem comboItem=(ComboListItem)combo.getSelectedItem();
						
						if(comboItem!=null){
							
							getItem().setValue(comboItem.getValue().toString());
						}
					}
					
					//validating
					if(validateRunnable!=null) {
						
						validateRunnable.run();
					}
				}
			};
			
			configureButtonListener=new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					if(currentClass!=null) {
						
						//creating the action configuration object
						ActionConfigurator actionConfigurator=null;
						
						try {
							actionConfigurator=(ActionConfigurator)currentClass.newInstance();
						}catch (Exception ex) {}
						
						if(actionConfigurator!=null) {
							
							//displaying the action configurator
							Class<?>[] classParams={Element.class, JComponent.class};
							Object[] params={currentItemReference.get().getAnimationElement(), configureButton};
							
							//invoking the "configure" method
							try {
								currentClass.getMethod("configure", classParams).invoke(actionConfigurator, params);
							}catch (Exception ex) {}
						}
						
						currentClass=null;
					}
					
					//validating
					if(validateRunnable!=null) {
						
						validateRunnable.run();
					}
				}
			};
		}
	}
	
	@Override
	protected void setItem(EditableItem item, Runnable validateRunnable){

		super.setItem(item, validateRunnable);
		
		//disables the configure button
		configureButton.setEnabled(false);
		
		//getting the current handle and its project file
		SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		File projectFile=handle.getScrollPane().getSVGCanvas().getProjectFile();

		Class<?> selectedActionClass=null;
		
		if(isEditor) {
			
			combo.removeActionListener(comboListener);
			configureButton.removeActionListener(configureButtonListener);
			
			//clearing the combo box
			combo.removeAllItems();
			
			//getting the current value
			String valueToSelect=item.getValue();
			
			if(valueToSelect==null) {
				
				valueToSelect="";
			}

			//if no default value exists the empty item is used
			ComboListItem emptyItem=new ComboListItem("", "");
			
			if(valueToSelect.equals("")) {
				
				combo.addItem(emptyItem);
			}

			//getting the list of the available action classes
			if(projectFile!=null) {
				
				Set<Class<?>> classesSet=ActionsLoader.getClasses(CustomAction.class, projectFile, true);
				
				//creating the map associating a class name to its class and the sorted list of the class names
				Map<String, Class<?>> classes=new HashMap<String, Class<?>>();
				LinkedList<String> classNames=new LinkedList<String>();
				String name="";
				
				for(Class<?> theClass : classesSet) {
					
					name=theClass.getName();
					classes.put(name, theClass);
					classNames.add(name);
				}
				
				Collections.sort(classNames);
				
				//filling the combo
				ComboListItem comboItem=null, selectedComboItem=emptyItem;

				for(String className : classNames){
					
					comboItem=new ComboListItem(className, className);
					
					if(valueToSelect.equals(className)){
						
						selectedComboItem=comboItem;
						selectedActionClass=classes.get(className);
					}
					
					combo.addItem(comboItem);
				}
				
				//setting the new selected item
				combo.setSelectedItem(selectedComboItem);
			}

			combo.addActionListener(comboListener);
			configureButton.addActionListener(configureButtonListener);
			
		}else {
			
			//showing the value of the item in the combo
			combo.removeAllItems();
			
			String value=item.getValue();
			
			//getting the action class name corresponding to this value
			if(value!=null && ! value.equals("")) {
				
				selectedActionClass=ActionsLoader.getClass(CustomAction.class, projectFile, value, true);
			}
			
			combo.addItem(new ComboListItem(value, value));
		}
		
		if(selectedActionClass!=null) {
			
			//getting the name of the class used to configure the selected action
			String configuratorClassName=null;
			
			try {
				configuratorClassName=
						(String)selectedActionClass.getMethod("getConfiguratorClassName", new Class[0]).
										invoke(null, new Object[0]);
			}catch (Exception ex) {ex.printStackTrace();}
			
			if(configuratorClassName!=null && ! configuratorClassName.equals("")) {
				
				//getting the class corresponding to this class name and storing it
				Class<?> configuratorClass=ActionsLoader.getClass(	ActionConfigurator.class, projectFile, 
																							configuratorClassName, true);

				if(isEditor) {
					
					currentClass=configuratorClass;
				}
				
				//handling the state of the configure button
				configureButton.setEnabled(configuratorClass!=null);
			}
		}
	}

}
