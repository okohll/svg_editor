package fr.itris.glips.rtdaeditor.anim.widgets.tageventchooser;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.rtda.action.tagevent.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.anim.widgets.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the selector for the enumerated tag and its values
 * @author Jordi SUC
 */
public class EnumeratedTagAndValuesSelector extends JPanel{
	
	/**
	 * the tag event chooser component
	 */
	protected TagEventChooser tagEventChooser;
	
	/**
	 * the radio button
	 */
	protected JRadioButton radioButton;
	
	/**
	 * the tag chooser widget
	 */
	protected TagChooser tagChooserWidget;
	
	/**
	 * the list of the possible enumerated values for the tag
	 */
	protected JList enumeratedValuesList;
	
	/**
	 * the default list model
	 */
	private DefaultListModel listModel;
	
	/**
	 * the list selection model
	 */
	private DefaultListSelectionModel listSelectionModel;
	
	/**
	 * the jlabels
	 */
	private JLabel tagValuesLbl;
	
	/**
	 * the condition panel
	 */
	private JPanel conditionPanel;

	/**
	 * the constructor of the class
	 * @param tagEventChooser the tag event chooser
	 * @param radioButton the radio button
	 */
	public EnumeratedTagAndValuesSelector(
			TagEventChooser tagEventChooser, JRadioButton radioButton) {
		
		this.tagEventChooser=tagEventChooser;
		this.radioButton=radioButton;
		build();
	}
	
	/**
	 * builds the new component
	 */
	protected void build(){
		
		//getting the labels
		String conditionLabel=ResourcesManager.bundle.getString("rtdaanim_condition");
		String tagValuesLabel=ResourcesManager.bundle.getString("rtdaanim_tagValuesCondition");
		
		//creating the jlabels
		tagValuesLbl=new JLabel(tagValuesLabel+" : ");
		tagValuesLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//creating the tag chooser
		tagChooserWidget=new TagChooser(TagToolkit.ENUMERATED);
		
		//adding the listener to the tag chooser widget
		ActionListener tagChooserListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {

				initList(tagChooserWidget.getCurrentValue(), new LinkedList<String>());
			}
		};
		
		tagChooserWidget.addActionListener(tagChooserListener);
		
		//creating the list that will contain all the possible values of the selected tag
		enumeratedValuesList=new JList();
		listModel=new DefaultListModel();
		listSelectionModel=new DefaultListSelectionModel();
		enumeratedValuesList.setModel(listModel);
		enumeratedValuesList.setSelectionModel(listSelectionModel);
		enumeratedValuesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		JScrollPane listScrollpane=new JScrollPane(enumeratedValuesList);
		listScrollpane.setPreferredSize(new Dimension(200, 50));
		
		//creating the condition panel
		conditionPanel=new JPanel();
		conditionPanel.setBorder(new CompoundBorder(
				new EmptyBorder(0, 25, 0, 0), new TitledBorder(conditionLabel)));
		
		//filling the condition panel
		conditionPanel.setLayout(new BorderLayout(2, 0));
		conditionPanel.add(tagValuesLbl, BorderLayout.WEST);
		conditionPanel.add(listScrollpane, BorderLayout.CENTER);
		
		//building the component
		GridBagLayout layout=new GridBagLayout();
		setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();
		c.anchor=GridBagConstraints.WEST;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(1, 1, 1, 1);
		
		c.gridx=0;
		c.gridy=0;
		layout.setConstraints(radioButton, c);
		add(radioButton);
		
		c.gridx=1;
		c.gridy=0;
		c.weightx=50;
		layout.setConstraints(tagChooserWidget, c);
		add(tagChooserWidget);
		
		c.gridx=0;
		c.gridy=1;
		c.gridwidth=2;
		layout.setConstraints(conditionPanel, c);
		add(conditionPanel);
	}
	
	/**
	 * initializes the widgets with the new provided value
	 * @param newValue the new value
	 */
	protected void init(String newValue){
		
		//getting the tag name
		newValue=newValue.trim();
		String[] splitValues=newValue.split(TagEventsManager.separatorRegex);
		
		if(splitValues!=null && splitValues.length>1){

			//getting the tag name
			String tagName=splitValues[1];
			
			tagChooserWidget.init(tagName);

			//getting the selected values for the tag
			LinkedList<String> valuesList=new LinkedList<String>();
			
			if(splitValues.length>2){
				
				for(int i=2; i<splitValues.length; i++){
					
					valuesList.add(splitValues[i]);
				}
			}
			
			initList(tagName, valuesList);
			
		}else{
			
			tagChooserWidget.init("");
			initList("", new LinkedList<String>());
		}
	}
	
	/**
	 * inits the list with the new tag name and the currently selected values
	 * @param tagName a tag name
	 * @param values the list of the selected values of the tag
	 */
	protected void initList(String tagName, LinkedList<String> values){
		
		//clearing the list
		listModel.removeAllElements();
		listSelectionModel.clearSelection();
		
		if(tagName!=null && ! tagName.equals("")){

			//getting the map of the possible values
			//filling the map with the tag values
			LinkedList<String> tagValues=DataBaseNodeToolkit.
				getEnumeratedTagValues(tagEventChooser.getItem().getDocument(), tagName);
			
			if(tagValues!=null){
				
				//filling the list
				ComboListItem listItem=null;
				int i=0;
				
				for(String val : tagValues){

					listItem=new ComboListItem(val, val);
					listModel.addElement(listItem);
					
					if(values.contains(val)){

						listSelectionModel.addSelectionInterval(i, i);
					}
					
					i++;
				}
			}
		}
	}
	
	/**
	 * @return the current value 
	 */
	protected String getValue(){
		
		String value="";
		String tagValue=tagChooserWidget.getCurrentValue();
		
		if(tagValue==null){
			
			tagValue="";
		}
		
		value=TagEventsManager.separator+tagValue+TagEventsManager.separator;
		
		if(enumeratedValuesList.getSelectedValues()!=null){
			
			Object[] values=enumeratedValuesList.getSelectedValues();
			ComboListItem item=null;
			
			for(Object obj : values) {
				
				if(obj!=null) {
					
					item=(ComboListItem)obj;
					value+=item.getValue()+TagEventsManager.separator;
				}
			}
		}

		return value;
	}
	
	@Override
	public void setEnabled(boolean enable){
		
		tagChooserWidget.setEnabled(enable);
		enumeratedValuesList.setEnabled(enable);
		conditionPanel.setEnabled(enable);
		tagValuesLbl.setEnabled(enable);
		
		super.setEnabled(enable);
	}
}
