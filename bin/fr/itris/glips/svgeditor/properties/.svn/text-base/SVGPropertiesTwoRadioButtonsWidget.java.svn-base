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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;


/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesTwoRadioButtonsWidget extends SVGPropertiesWidget{

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesTwoRadioButtonsWidget(SVGPropertyItem propertyItem) {

		super(propertyItem);
		
		buildComponent();
	}
	
	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent(){

		//the panel that contains the radio buttons
		final JPanel radioPanel=new JPanel();
		final ButtonGroup group=new ButtonGroup();

		//gets the initial value of the property
		String value=propertyItem.getGeneralPropertyValue();
			
		if(propertyItem.getPropertyValuesMap().size()==2){

			//finds the labels and values for the radio buttons
			final String[] values=new String[2];
			final String[] labels=new String[2];
			String key="";
			int i=0;
			
			for(Iterator it=propertyItem.getPropertyValuesMap().keySet().iterator(); it.hasNext();){
			    
				try{
					key=(String)it.next();
					values[i]=(String)propertyItem.getPropertyValuesMap().get(key);
					labels[i]=(String)propertyItem.getPropertyValuesLabelMap().get(key);
					i++;
				}catch (Exception ex){break;}
			}

			if(values[0]!=null && labels[0]!=null && values[1]!=null && labels[1]!=null){
			    
				//creates the radio buttons
				final JRadioButton buttons[]=new JRadioButton[2];
				final ActionListener[] actionListeners=new ActionListener[2];
				
				for (i=0;i<2;i++){
				    
					buttons[i]=new JRadioButton(labels[i]);
					group.add(buttons[i]);
					
					if(value!=null && values[i].equals(value)){
					    
					    group.setSelected(buttons[i].getModel(), true);
					}
						
					final int fi=i;
						
					actionListeners[i]=new ActionListener(){
					    
						public void actionPerformed(ActionEvent evt) {
						    
							group.setSelected(buttons[fi].getModel(), true);
							propertyItem.changePropertyValue(values[fi]);
						}
					};
						
					//adds the listeners
					buttons[i].addActionListener(actionListeners[i]);
				}
					
				//creates the component that will be returned
				radioPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
				
				for(i=0;i<2;i++){
				    
				    radioPanel.add(buttons[i]);
				}
				
				component=radioPanel;

				//creates the disposer
				disposer=new Runnable(){

		            public void run() {

						for(int i=0;i<2;i++){
						    
							buttons[i].removeActionListener(actionListeners[i]);
						}
		            }
				};
			}
		}
	}
}

