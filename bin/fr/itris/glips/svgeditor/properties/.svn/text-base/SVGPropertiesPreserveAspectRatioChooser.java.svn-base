/*
 * Created on 4 juin 2005
 * 
 =============================================
                   GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
GLIPS Graffiti Editor, a SVG Editor
Copyright (C) 2004 Jordi SUC, Philippe Gil, SARL ITRIS

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact : jordi.suc@itris.fr; philippe.gil@itris.fr

 =============================================
 */
package fr.itris.glips.svgeditor.properties;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;


import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.visualresources.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesPreserveAspectRatioChooser extends SVGPropertiesWidget{

	/**
	 * the runnable used for configuring the widgets
	 */
	protected Runnable configure=null;
	
	 /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesPreserveAspectRatioChooser(SVGPropertyItem propertyItem) {

		super(propertyItem);
		
		buildComponent();
	}
	
	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent(){

		final Editor editor=propertyItem.getProperties().getSVGEditor();
		final ResourceBundle bundle=ResourcesManager.bundle;
		final SVGHandle handle=editor.getHandlesManager().getCurrentHandle();

		//getting the labels
		String meetOrSliceLabel="", alignLabel="";
		
		try{
			meetOrSliceLabel=bundle.getString("property_meetOrSlice");
			alignLabel=bundle.getString("property_preserveAspectRatioAlign");
		}catch (Exception ex){}
		
		//creating the label for the combo box
		final JLabel alignLbl=new JLabel(alignLabel+" : ");

		//creating the array of the values of the align field
		final String[] alignValues=new String[10];
		
		alignValues[0]="none";
		alignValues[1]="xMinYMin";
		alignValues[2]="xMidYMin";
		alignValues[3]="xMaxYMin";
		alignValues[4]="xMinYMid";
		alignValues[5]="xMidYMid";
		alignValues[6]="xMaxYMid";
		alignValues[7]="xMinYMax";
		alignValues[8]="xMidYMax";
		alignValues[9]="xMaxYMax";

		//creating the combo items
		SVGComboItem[] items=new SVGComboItem[alignValues.length];
		String label="";
		int selectedIndex=-1;
		
		for(int i=0; i<alignValues.length; i++){
			
			//getting the label for this item and creating the item
			try{
				label=bundle.getString("item_"+alignValues[i]);
			}catch (Exception ex){label=alignValues[i];}
			
			if(label!=null){
				
				items[i]=new SVGComboItem(alignValues[i], label);
			}
		}
		
		//creating the combo box that will be used to modify the align value
		final JComboBox combo=new JComboBox(items);
		
		//creating the check box for the meet or slice
		final JCheckBox meetOrSliceCheckBox=new JCheckBox(meetOrSliceLabel);

		//creating and adding the listener to the combo box
		final ActionListener comboAndMeetOrSliceListener=new ActionListener(){

			public void actionPerformed(ActionEvent evt) {

				if(combo.getSelectedItem()!=null){
					
					String meetOrSlice="meet";
					
					if(! meetOrSliceCheckBox.isSelected()){
						
						meetOrSlice="slice";
					}

					propertyItem.changePropertyValue(((SVGComboItem)combo.getSelectedItem()).getValue()+" "+meetOrSlice);
				}
			}
		};
		
		combo.addActionListener(comboAndMeetOrSliceListener);
		meetOrSliceCheckBox.addActionListener(comboAndMeetOrSliceListener);
		
		//the runnable allowing to configure the widgets
		configure=new Runnable(){

			public void run() {

				String value=propertyItem.getGeneralPropertyValue();
        		combo.removeActionListener(comboAndMeetOrSliceListener);
        		meetOrSliceCheckBox.removeActionListener(comboAndMeetOrSliceListener);

				String align="", meetOrSlice="";

				if(value!=null){
					
					//getting the align value and the meet or slice value
					value=value.trim();
					value=value.replaceAll("\\s+", " ");
					
					String[] splitValue=value.split(" ");
					
					for(int i=0; i<splitValue.length; i++){
					
						if(splitValue[i].equals("meet") || splitValue[i].equals("slice")){
							
							meetOrSlice=splitValue[i];
							
						}else if(! splitValue[i].equals("defer")){
							
							align=splitValue[i];
						}
					}
				}
				
				//setting the default values
				if(align.equals("")){
					
					align="none";
				}
				
				if(meetOrSlice.equals("")){
					
					meetOrSlice="meet";
				}
				
				//enabling the widgets and setting their values
				combo.setEnabled(true);
				alignLbl.setEnabled(true);
				meetOrSliceCheckBox.setEnabled(true);
				
				//getting the selected index for the combo
				int selectedIndex=0;
				
				for(int i=0; i<alignValues.length; i++){
					
					if(align.equals(alignValues[i])){
						
						selectedIndex=i;
					}
				}
				
				//setting the selected index
				combo.setSelectedIndex(selectedIndex);
				
				//handles the meet or slice checkbox state
				meetOrSliceCheckBox.setSelected(meetOrSlice.equals("meet"));

        		combo.addActionListener(comboAndMeetOrSliceListener);
        		meetOrSliceCheckBox.addActionListener(comboAndMeetOrSliceListener);
			}
		};

		//creating and filling the panel that will be returned
		JPanel panel=new JPanel();
		GridBagLayout gridBag=new GridBagLayout();
		panel.setLayout(gridBag);
		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.anchor=GridBagConstraints.WEST;

		c.gridwidth=1;
		gridBag.setConstraints(alignLbl, c);
		panel.add(alignLbl);

		c.gridwidth=GridBagConstraints.REMAINDER;
		gridBag.setConstraints(combo, c);
		panel.add(combo);

		gridBag.setConstraints(meetOrSliceCheckBox, c);
		panel.add(meetOrSliceCheckBox);
		
		//initializing the widgets
		configure.run();
		
		component=panel;

		//creates the disposer
		disposer=new Runnable(){

            public void run() {

        		combo.removeActionListener(comboAndMeetOrSliceListener);
        		meetOrSliceCheckBox.removeActionListener(comboAndMeetOrSliceListener);
            }
		};
	}
}
