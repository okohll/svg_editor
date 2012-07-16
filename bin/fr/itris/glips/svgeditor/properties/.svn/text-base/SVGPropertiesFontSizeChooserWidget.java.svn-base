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


/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesFontSizeChooserWidget extends SVGPropertiesWidget{

	/**
	 * the items of the font size chooser
	 */
	private static String[] items={"6","7","8","9","10","11","12","13","14","15","16","18","20","22","24","26",
		"28","32","36","40","44","48","54","60","66","72","80","88","96"};
	
    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesFontSizeChooserWidget(SVGPropertyItem propertyItem) {

		super(propertyItem);
		
		buildComponent();
	}
	
	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent(){

		//the value of the property
		String propertyValue=propertyItem.getGeneralPropertyValue();
		propertyValue=propertyValue.replaceAll("[pt]","");
		propertyValue=propertyValue.replaceAll("\\s","");
			
		//the list of the items that will be displayed in the combo box
		LinkedList itemList=new LinkedList();

		for(int i=0;i<items.length;i++){
		    
		    itemList.add(items[i]);
		}
		
		if(! itemList.contains(propertyValue)){
		    
		    itemList.add(propertyValue);
		}
		
		Collections.sort(itemList);

		//the combo box
		final JComboBox combo=new JComboBox(items);
		combo.setEditable(true);
		
		//sets the selected item
		combo.setSelectedItem(propertyValue);
		
		final ActionListener listener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent evt) {
			    
				String value="";
				
				if(combo.getSelectedItem()!=null){
				    
					try{
						value=(String)combo.getSelectedItem();
					}catch (Exception ex){value="";}
				}
					
				//modifies the widgetValue of the property item
				if(value!=null && !value.equals("")){
				    
					propertyItem.changePropertyValue(value.concat("pt"));
				}
			}		
		};
		
		//adds a listener to the combo box
		combo.addActionListener(listener);

		//the panel that will be contained in the widget object
		JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(combo);
		
		component=panel;

		//creates the disposer
		disposer=new Runnable(){

            public void run() {

				combo.removeActionListener(listener);
            }
		};
	}
}

