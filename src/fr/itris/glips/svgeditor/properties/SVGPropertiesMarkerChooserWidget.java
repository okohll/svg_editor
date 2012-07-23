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
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;



/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesMarkerChooserWidget extends SVGPropertiesWidget{

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesMarkerChooserWidget(SVGPropertyItem propertyItem) {

		super(propertyItem);
		
		buildComponent();
	}
	
	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent(){
		
		final Editor editor=propertyItem.getProperties().getSVGEditor();
		final SVGHandle handle=editor.getHandlesManager().getCurrentHandle();
	    String propertyValue=propertyItem.getGeneralPropertyValue();
	    
	    if(propertyValue!=null && ! propertyValue.equals("") && ! propertyValue.equals("none")){
	        
	        propertyValue=fr.itris.glips.library.Toolkit.toUnURLValue(propertyValue);
	    }
	    
	    //the map associating the id of a resource contained in the "defs" element 
	    //of the svg document of the handle to this resource
	    HashMap resources=null;
	    
	    if(handle!=null){
	        
	        //the list of types of resources that should appear in the combo
	        LinkedList resourceTagNames=new LinkedList();
	        resourceTagNames.add("marker");
	        
	        Document doc=handle.getScrollPane().getSVGCanvas().getDocument();
	        resources=handle.getSvgResourcesManager().
	        	getResourcesFromDefs(doc, resourceTagNames);
	    }

		LinkedList itemList=new LinkedList();
		Map values=new LinkedHashMap(propertyItem.getPropertyValuesMap());
		Map labels=new LinkedHashMap(propertyItem.getPropertyValuesLabelMap());
		String cur=null, value="";
		SVGComboResourceItem item=null, selectedItem=null;
		Element resource=null;
		
		if(resources!=null){
		    
			//fills the map with the values of the resource ids
			for(Iterator it=resources.keySet().iterator(); it.hasNext();){
			    
			        cur=(String)it.next();
			    
			    if(cur!=null){
			        
			        values.put(cur, cur);
			        labels.put(cur, cur);
			    }
			}
		}

		//an item with an empty string
		item=new SVGComboResourceItem(handle,  "","", null);
		itemList.add(item);
			
		//builds the array of items for the combo
		for(Iterator it=values.keySet().iterator(); it.hasNext();){
			
				cur=(String)it.next();
				value=(String)values.get(cur);
				resource=(Element)resources.get(cur);
				item=new SVGComboResourceItem(handle, value, (String)labels.get(cur), resource);
					
				//checks if the current possible value is equals to the current value of the property
				if(value!=null && propertyValue!=null && value.equals(propertyValue)){
				    
				    selectedItem=item;
				}
				
			if(item!=null){
			    
			    itemList.add(item);
			}
		}
			
		Object[] items=itemList.toArray();
		
		//the combo box
		final JComboBox combo=new JComboBox(items);
		combo.setRenderer(new SVGComboResourceCellRenderer());
		combo.setPreferredSize(new Dimension(combo.getWidth(), 24));
		
		if(selectedItem!=null)combo.setSelectedItem(selectedItem);
			
		final ActionListener comboListener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent evt) {
			    
			    //unregisters the nodes with the last value that each of them had
			    Node cur=null;
			    String val=null;
			    
			    for(Iterator it=nodesList.iterator(); it.hasNext();){
			        
			            cur=(Node)it.next();
			        
			        if(cur!=null){
			            
			            val=propertyItem.getPropertyValue(cur);
			            val=fr.itris.glips.library.Toolkit.toUnURLValue(val);
			            
			            if(val!=null && ! val.equals("") && ! val.equals("none")){
			                
			                handle.getSvgResourcesManager().
			                	removeNodeUsingResource(val, cur);
			            }
			        }
			    }

			    //gets the new value and adds it, and registers the node to the resource manager
				String value="";
				
				if(combo.getSelectedItem()!=null){
				    
					value=((SVGComboResourceItem)combo.getSelectedItem()).getValue();
				}
				
				//modifies the widgetValue of the property item
				if(value!=null && ! value.equals("")){
				    
				    if(! value.equals("none")){
				        
				        //registers the id for the list of nodes
						handle.getSvgResourcesManager().
							addNodesUsingResource(value, nodesList);
				        
				        value=fr.itris.glips.library.Toolkit.toURLValue(value);
				    }
				    
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

