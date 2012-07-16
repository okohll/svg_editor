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
import javax.swing.border.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;


import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesRenderChooserWidget extends SVGPropertiesWidget {

	/**
	 * the list of the resource tag names that are handled
	 */
	private static LinkedList<String> resourceTagNames=new LinkedList<String>();
	
	static {
		
        resourceTagNames.add("linearGradient");
        resourceTagNames.add("radialGradient");
        resourceTagNames.add("pattern");
	}
	
	/**
	 * the current color
	 */
	private Color currentColor=null;
	
    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesRenderChooserWidget(SVGPropertyItem propertyItem) {

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

	    //gets the initial value of the property
		String value=propertyItem.getGeneralPropertyValue();

		//the color represented by this string (if this string represents a color)
		currentColor=Editor.getColorChooser().getColor(handle, value);
		
		//finds the labels and values for the radio buttons
		final String[] values=new String[2];
		final String[] labels=new String[2];
			
		int selectedIndex=0;
			
		//the values
		if(currentColor!=null){
		    
		    values[0]=value;
		    values[1]="none";
		    selectedIndex=0;
		    
		}else{
		    
			currentColor=Color.black;
		    values[0]="#000000";
		    
		    if("none".equals(value)){
		        
		        values[1]="none";
		        
		    }else{
		        
			    values[1]=fr.itris.glips.library.Toolkit.toUnURLValue(value);
		    }

		    selectedIndex=1;
		}

		//the labels
		labels[0]="";
		labels[1]="";
		String noneLabel="", labelColorPicker="";
			
		if(bundle!=null){
		    
			try{
			    noneLabel=bundle.getString("item_none");
				labels[0]=bundle.getString("renderchooser_color");
				labels[1]=bundle.getString("renderchooser_uri");
				labelColorPicker=bundle.getString("labelcolorpicker");
			}catch (Exception ex){}
		}
		
		//the icons
		final ImageIcon 	colorPickerIcon=ResourcesManager.getIcon("ColorPickerSmall", false),
									colorPickerDisabledIcon=ResourcesManager.getIcon("ColorPickerSmall", true);
		
		//creates the panel that will contain the widgets
		final JPanel panel=new JPanel();
		
		//the button group
		final ButtonGroup group=new ButtonGroup();
			
		//creates the color chooser//
			
		//the button used to preview the color
		final JButton previewColor=new JButton();
	    Insets buttonInsets=new Insets(0, 0, 0, 0);
	    previewColor.setMargin(buttonInsets);
	    previewColor.setPreferredSize(new Dimension(22, 22));
			
		final JPanel colorPanel=new JPanel();
		colorPanel.setPreferredSize(new Dimension(18, 18));
		
		colorPanel.setBackground(currentColor);
		colorPanel.setBorder(new LineBorder(Color.white, 1));
		previewColor.add(colorPanel);
		
		final JToggleButton colorPickerButton=new JToggleButton(colorPickerIcon);
		colorPickerButton.setMargin(new Insets(0,0,0,0));
		colorPickerButton.setToolTipText(labelColorPicker);
		colorPickerButton.setPreferredSize(new Dimension(22, 22));

		//the list of the items that will be contained in the combo used to choose the id of a resource
		LinkedList<SVGComboResourceItem> items=new LinkedList<SVGComboResourceItem>();
		SVGComboResourceItem selectedItem=null;

	    //the map associating the id of a resource contained in the "defs" element to the resource node
		Document doc=handle.getScrollPane().getSVGCanvas().getDocument();
	    HashMap<String, Element> resources=
	    	handle.getSvgResourcesManager().getResourcesFromDefs(doc, resourceTagNames);

	    Element cur=null;
	    SVGComboResourceItem item=null;
	    
	    //the empty item
	    item=new SVGComboResourceItem(handle, "none", noneLabel, null);
	    
	    if(values[1].equals("none")){
	        
	        selectedItem=item;
	    }
	    
	    items.add(item);

	    //for each resource element contained in the map
	    for(String id : new LinkedList<String>(resources.keySet())){
	        
            cur=resources.get(id);
            
	        if(cur!=null && ! id.equals("")){
	            
	            item=new SVGComboResourceItem(handle, id, id, cur);
	            
	            if(id.equals(values[1])){
	                
	                selectedItem=item;
	            }
	            
	            items.add(item);
	        }
	    }
	    
		//creates the combo for the uri
		Object[] itemArray=items.toArray();
		
		//the combo box
		final JComboBox combo=new JComboBox(itemArray);
		
		//setting the properties for the combo
		combo.setPreferredSize(new Dimension(125, 25));
		combo.setRenderer(new SVGComboResourceCellRenderer());
			
		//sets the selected item
		if(selectedItem!=null){
		    
		    combo.setSelectedItem(selectedItem);						    
		}
	
		//creates the radio buttons
		final JRadioButton buttons[]=new JRadioButton[2];
		final ActionListener[] actionListeners=new ActionListener[2];

		for (int i=0;i<2;i++){
		    
			buttons[i]=new JRadioButton(labels[i]);

			group.add(buttons[i]);

			final int fi=i;
				
			actionListeners[i]=new ActionListener(){
			    
				public void actionPerformed(ActionEvent evt) {

					for(int j=0;j<2;j++){
					    
						//selects or deselects the radio buttons and enables or disables the associated widgets
						if(fi!=j){
						    
							if(j==0){
							    
								previewColor.setEnabled(false);
								colorPickerButton.setEnabled(false);
								colorPickerButton.setIcon(colorPickerDisabledIcon);
								
							}else if(j==1){
							    
								combo.setEnabled(false);
							}

						}else{
						    
							group.setSelected(buttons[j].getModel(), true);
							
							if(j==0){
							    
								previewColor.setEnabled(true);
								colorPickerButton.setEnabled(true);
								colorPickerButton.setIcon(colorPickerIcon);
								Editor.getSVGColorManager().setCurrentColor(colorPanel.getBackground());
								
							}else if(j==1){
							    
								combo.setEnabled(true);
							}
						}
					}

				    //unregisters the nodes with the last value that each of them had
				    String val=null;
				    
				    for(Element el : nodesList){

				        if(el!=null){
				            
				            val=propertyItem.getPropertyValue(el);
				            val=fr.itris.glips.library.Toolkit.toUnURLValue(val);
				            
				            if(val!=null && ! val.equals("") && ! val.equals("none")){
				                
				                handle.getSvgResourcesManager().
				                	removeNodeUsingResource(val, el);
				            }
				        }
				    }
					
					//changes the property value
					if(fi==1){
					    
						SVGComboResourceItem itm=(SVGComboResourceItem)combo.getSelectedItem();
					    
					    if(itm!=null){
					        
					        if(itm.getValue().equals("none")){
					            
					            propertyItem.changePropertyValue(itm.getValue());
					            
					        }else{
					            
								propertyItem.changePropertyValue(
										fr.itris.glips.library.Toolkit.toURLValue(itm.getValue()));
								
								//registers the nodes and the id of the resource
								if(! itm.equals("")){
								    
								    handle.getSvgResourcesManager().
								    	addNodesUsingResource(itm.getValue(), nodesList);
								}
					        }
					    }

					}else{
					    
						propertyItem.changePropertyValue(values[0]);
					}
				}
			};
			
			//adds the listeners
			buttons[i].addActionListener(actionListeners[i]);
		}

		//sets the current selection state
		buttons[selectedIndex].setSelected(true);
			
		if(selectedIndex==0){
		    
			previewColor.setEnabled(true);
			colorPickerButton.setEnabled(true);
			colorPickerButton.setIcon(colorPickerIcon);
			combo.setEnabled(false);
			
		}else if(selectedIndex==1){
		    
			previewColor.setEnabled(false);
			colorPickerButton.setEnabled(false);
			colorPickerButton.setIcon(colorPickerDisabledIcon);
			combo.setEnabled(true);
		}
		
		//the listener to the preview color button
		final ActionListener previewColorListener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent e) {

                Color col=Editor.getColorChooser().showColorChooserDialog(
                								currentColor==null?Color.BLACK:currentColor);
                
				if(col!=null){
				    
					String scl=Editor.getColorChooser().getColorString(col);
					propertyItem.changePropertyValue(scl);
					values[0]=scl;
					colorPanel.setBackground(col);
					currentColor=col;
					Editor.getSVGColorManager().setCurrentColor(col);
				}
			}
		};
			
		previewColor.addActionListener(previewColorListener);
		
		//the listener to the color picker button
		final ActionListener colorPickerListener=new ActionListener(){

            public void actionPerformed(ActionEvent evt){
                    
            	Editor.getEditor().getSelectionManager().setToNoneMode();
                
                //the listener to the events in the application
                AWTEventListener listener=new AWTEventListener(){

                    public void eventDispatched(AWTEvent event) {

                        if(event instanceof MouseEvent){

                            MouseEvent mevt=(MouseEvent)event;
                            mevt.consume();
                            
                            Point point=mevt.getPoint();
                            
                            if(mevt.getID()==MouseEvent.MOUSE_PRESSED){
                                
                                //converting the point
                                SwingUtilities.convertPointToScreen(point, (Component)mevt.getSource());
                                
                                //getting the color at the clicked point
								Color col=editor.getSVGToolkit().pickColor(point);
								
								if(col!=null){
								    
								    //setting the new color value
									String scl=Editor.getColorChooser().getColorString(col);
									propertyItem.changePropertyValue(scl);
									values[0]=scl;
									colorPanel.setBackground(col);
									Editor.getSVGColorManager().setCurrentColor(col);
								}
                                
                            }else if(mevt.getID()==MouseEvent.MOUSE_RELEASED){
                                
                                //remove this listener and set the default state of the editor
                                Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                                colorPickerButton.setSelected(false);
                                Editor.getEditor().getSelectionManager().setToRegularMode();
                            }
                        }
                    } 
                };
                
                //adding the listener
                Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK);
            }
		};
		
		//adds the listener to the colorPicker button
		colorPickerButton.addActionListener(colorPickerListener);
		
		//the listener to the combo box
		final ActionListener comboListener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent evt) {

			    //unregisters the nodes with the last value that each of them had
			    String val=null;
			    
			    for(Element current : nodesList){

			        if(current!=null){
			            
			            val=propertyItem.getPropertyValue(current);
			            val=fr.itris.glips.library.Toolkit.toUnURLValue(val);
			            
			            if(val!=null && ! val.equals("") && ! val.equals("none")){
			                
			                handle.getSvgResourcesManager().
			                	removeNodeUsingResource(val, current);
			            }
			        }
			    }
			    
			    //getting the new value
				String newValue="";
				
				if(combo.getSelectedItem()!=null){
				    
					newValue=((SVGComboResourceItem)combo.getSelectedItem()).getValue();
				}
						
				//modifies the widgetValue of the property item
				if(newValue!=null && ! newValue.equals("")){
				    
				    if(newValue.equals("none")){
				        
						propertyItem.changePropertyValue(newValue);

				    }else{
				        
						propertyItem.changePropertyValue(
								fr.itris.glips.library.Toolkit.toURLValue(newValue));
						
						//registers the nodes and the id of the resource
						handle.getSvgResourcesManager().
							addNodesUsingResource(newValue, nodesList);
				    }

					values[1]=newValue;
				}
			}
		};
			
		//adds a listener to the combo box
		combo.addActionListener(comboListener);
			
		//sets the layout
		GridBagLayout gridBag=new GridBagLayout();
		panel.setLayout(gridBag);
		GridBagConstraints c=new GridBagConstraints();
		c.anchor=GridBagConstraints.NORTHWEST;
		c.insets=new Insets(0,0,1,0);
		c.fill=GridBagConstraints.NONE;
		
		//the first button
		c.gridwidth=1;
		gridBag.setConstraints(buttons[1], c);
		panel.add(buttons[1]);
			
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=2.0;
		gridBag.setConstraints(combo, c);
		panel.add(combo);

		//the second button
		c.gridwidth=1;
		c.weightx=0;
		gridBag.setConstraints(buttons[0], c);
		panel.add(buttons[0]);

		gridBag.setConstraints(previewColor, c);
		panel.add(previewColor);
		
		c.insets=new Insets(0, 2, 0, 0);
		gridBag.setConstraints(colorPickerButton, c);
		panel.add(colorPickerButton);
		
		JPanel emptyPanel=new JPanel();
		c.insets=new Insets(0, 0, 0, 0);
		gridBag.setConstraints(emptyPanel, c);
		panel.add(emptyPanel);
		
		component=panel;

		//creating the runnable to dipose the widget
		disposer=new Runnable(){

            public void run() {
			    
				//removes the listeners//
                
				for (int i=0;i<2;i++){

					buttons[i].removeActionListener(actionListeners[i]);
				}
				
				previewColor.removeActionListener(previewColorListener);
				colorPickerButton.removeActionListener(colorPickerListener);
				combo.removeActionListener(comboListener);
            }
		};
	}
}
