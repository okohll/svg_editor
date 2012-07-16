package fr.itris.glips.rtdaeditor.anim.widgets.tageventchooser;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the components used to choose a tag
 * @author Jordi SUC
 */
public class TagChooser extends JPanel{

	/**
	 * the list of the listeners to the tag chooser
	 */
	protected LinkedList<ActionListener> listeners=
		new LinkedList<ActionListener>();
	
	/**
	 * the textfield
	 */
	protected final JTextField textfield=new JTextField(10);
	
	/**
	 * the button used to launch the dialog
	 */
	protected final JButton selectButton=new JButton();
	
	/**
	 * the labels
	 */
	protected String noneLabel="";
	
	/**
	 * the current value for the chooser
	 */
	protected String currentValue="";
	
	/**
	 * the tag type
	 */
	protected int tagType=0;
	
	/**
	 * the constructor of the class
	 * @param tagType the type of the tag to be selected
	 */
	public TagChooser(int tagType){

		this.tagType=tagType;
		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){
		
		//getting the labels
		String tagChooserLabel="";
		ResourceBundle bundle=ResourcesManager.bundle;
		
		try{
			tagChooserLabel=bundle.getString("rtdaanim_tagchooserbutton");
			noneLabel="<"+bundle.getString("rtdaanim_none")+">";
		}catch (Exception ex){}
			
		selectButton.setText(tagChooserLabel);
		selectButton.setMargin(new Insets(1, 1, 1, 1));
		
		//setting the properties of the textfield
		textfield.setEditable(false);
		
		setLayout(new BorderLayout(1, 0));
		add(textfield, BorderLayout.CENTER);
		add(selectButton, BorderLayout.EAST);
		
		//adding the listener to the button
		selectButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				//getting the current handle
				SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
				
				if(handle!=null){
					
					//creating the filter for choosing a tag
					DataBaseNodeFilter filter=new DataBaseNodeFilter(
						currentValue, 0, tagType, false, false, null);
					
					//getting the information object about the selected tag
					DataBaseNodeChooser nodeChooser=DataBaseNodeChooser.getDataBaseNodeChooser(
							Editor.getParent(), handle.getCanvas().getDocument(), filter, false, false);
					nodeChooser.showDialog(((JComponent)evt.getSource()));
					nodeChooser.disposeDialog();
					DataBaseNodeInformation info=nodeChooser.getInfo();

					if(info!=null){
						
						String newValue=info.getXmlPath();
						
						if(newValue==null){
							
							newValue="";
						}
						
						if(newValue.equals("")){
							
							textfield.setText(noneLabel);
							
						}else{
							
							textfield.setText(newValue);
						}

						currentValue=newValue;
						notifyListeners();
					}
				}
			}
		});
	}
	
	/**
	 * inits the widget with the provided value
	 * @param value a value
	 */
	public void init(String value){
		
		currentValue=value;
		
		if(value.equals("")){
			
			value=noneLabel;
		}
		
		textfield.setText(value);
	}                             
	
	/**
	 * adds a new action listener to the widget
	 * @param listener a listener to the widget
	 */
	public void addActionListener(ActionListener listener){
		
		listeners.add(listener);
	}
	
	/**
	 * notifies that an event occured
	 */
	protected void notifyListeners(){
		
		for(ActionListener listener : listeners){
			
			listener.actionPerformed(null);
		}
	}

	/**
	 * @return the current value
	 */
	public String getCurrentValue() {
		return currentValue;
	}
	
	@Override
	public void setEnabled(boolean enabled) {

		textfield.setEnabled(enabled);
		selectButton.setEnabled(enabled);
		super.setEnabled(enabled);
	}
}
