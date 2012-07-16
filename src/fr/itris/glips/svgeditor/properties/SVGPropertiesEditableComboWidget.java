/*
 * Created on 19 janv. 2005
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

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;


import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesEditableComboWidget extends SVGPropertiesWidget{

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesEditableComboWidget(SVGPropertyItem propertyItem) {

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
		
		LinkedList itemList=new LinkedList();
		SVGPropertiesComboItem item=null, selectedItem=null;
		String cur=null, propertyValue=propertyItem.getGeneralPropertyValue();
		
		//builds the array of items for the combo
		for(Iterator it=propertyItem.getPropertyValuesMap().keySet().iterator(); it.hasNext();){

			try{
				cur=(String)it.next();
				String value=(String)propertyItem.getPropertyValuesMap().get(cur);
				item=new SVGPropertiesComboItem(value, (String)propertyItem.getPropertyValuesLabelMap().get(cur));
				
				//checks if the current possible value is equals to the current value of the property
				if(value!=null && propertyValue!=null && value.equals(propertyValue)){
				    
				    selectedItem=item;
				}
				
			}catch (Exception ex){item=null;}
			
			if(item!=null){
			    
			    itemList.add(item);
			}
		}
			
		if(selectedItem==null && propertyValue!=null){
		    
			selectedItem=new SVGPropertiesComboItem(propertyValue, propertyValue);
			itemList.addFirst(selectedItem);
		}
		
		Object[] items=itemList.toArray();
		//the combo box
		final JComboBox combo=new JComboBox(items);
		combo.setEditable(true);
		
		//sets the selected item
		if(selectedItem!=null){
		    
		    combo.setSelectedItem(selectedItem);
		}
			
		final ActionListener comboListener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent evt) {
			    
				String value="";
				
				if(combo.getSelectedItem()!=null){
				    
					try{
						value=((SVGPropertiesComboItem)combo.getSelectedItem()).getValue();
					}catch (Exception ex){value="";}
						
					if(value==null || (value!=null && value.equals(""))){
					    
						value=combo.getSelectedItem().toString();
					}
				}
				
				//modifies the widgetValue of the property item
				if(value!=null && !value.equals("")){
				    
					propertyItem.changePropertyValue(value);
				}
			}
		};
			
		//adds a listener to the combo box
		combo.addActionListener(comboListener);
		
		//the panel that will be contained in the widget object
		JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(combo);
		
		component=panel;
			
		//creates the disposer
		disposer=new Runnable(){

            public void run() {

				combo.removeActionListener(comboListener);
            }
		};
	}
}
