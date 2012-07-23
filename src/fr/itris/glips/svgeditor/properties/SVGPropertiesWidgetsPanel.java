/*
 * Created on 2 juin 2004
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
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

	/**
	 * the panel that will be displayed in the properties dialog
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	public class SVGPropertiesWidgetsPanel extends JPanel{
		
		/**
		 * the map associating a tab id to its label
		 */
		private LinkedHashMap tabMap;
		
		/**
		 * the map associating a tab name to a list of SVGPropertiesItems objects
		 */
		private LinkedHashMap propertyItemsMap;
		
		/**
		 * the tab panel
		 */
		private JTabbedPane tabPanel=null;
		
		/**
		 * the list of the widgets
		 */
		private LinkedList<SVGPropertiesWidget> widgets=new LinkedList<SVGPropertiesWidget>();
		
		/**
		 * the listener to the changes of the selection of the tabs
		 */
		private ChangeListener changeListener=null;
		
		/**
		 * the properties object
		 */
		private SVGProperties properties=null;
		
		/**
		 * an instance of this class
		 */
		private SVGPropertiesWidgetsPanel widgetsPanel=this;

		/**
		 * the constructor of the class
		 * @param properties the properties object
		 * @param tabMap the map associating a tab id to its label
		 * @param propertyItemsMap the map associating a tab name to a list (LinkedList) of SVGPropertiesItems objects
		 */
		public SVGPropertiesWidgetsPanel(SVGProperties properties, LinkedHashMap tabMap, LinkedHashMap propertyItemsMap){

			this.properties=properties;
			this.tabMap=tabMap;
			this.propertyItemsMap=propertyItemsMap;
			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			tabPanel=getTabbedPane();

			if(tabPanel!=null){
				
				//setting the listener for the changing of the tabs
				changeListener=new ChangeListener(){
				    
					public void stateChanged(ChangeEvent arg0) {
					    
						if(tabPanel!=null){
						    
							widgetsPanel.properties.setTabId(tabPanel.getTitleAt(tabPanel.getSelectedIndex()));
						}
					}
				};
				
				tabPanel.addChangeListener(changeListener);
				
				setSelectedTab(properties.getTabId());
				add(tabPanel);
			}
			
			setBorder(new EmptyBorder(0, 0, 1, 0));
		}
		
		/**
		 * removes all the listeners
		 */
		public void dispose(){
		    
			if(tabPanel!=null && changeListener!=null){
			    
				tabPanel.removeChangeListener(changeListener);
				SVGPropertiesWidget widget=null;
				
				//disposes the widgets
				for(Iterator it=widgets.iterator(); it.hasNext();){
				    
				    widget=(SVGPropertiesWidget)it.next();
				    if(widget!=null){
				        
				        widget.dispose();
				    }
				}

				//clears the maps
				tabMap.clear();
				propertyItemsMap.clear();
				widgets.clear();
				tabPanel.removeAll();
				tabPanel=null;
			}
		}
		
		/**
		 * sets the selected tab
		 * @param name the tab to be selected
		 */
		protected void setSelectedTab(String name){
		    
			if(tabPanel!=null){

				for(int i=0;i<tabPanel.getTabCount();i++){
				    
					if(name!=null && name.equals(tabPanel.getTitleAt(i))){
					    
						tabPanel.setSelectedIndex(i);
					}
				}
			}
		}
		
		/**
		 * @return a JTabbedPane containing the widgets allowing to change the properties of the node
		 */
		protected JTabbedPane getTabbedPane(){
			
			if(propertyItemsMap!=null && propertyItemsMap.size()>0 && tabMap!=null){
			    
				//the tabbed pane
				JTabbedPane tabbedPane=new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
				
				String cur=null, label=null;
				LinkedList list=null;
				JPanel panel=null, tpanel=null;
				
				//for each tab
				for(Iterator it=new LinkedList(propertyItemsMap.keySet()).iterator(); it.hasNext();){
				    
						//gets the name and the label of the tab, and the list of the property items linked with it
						cur=(String)it.next();
						list=(LinkedList)propertyItemsMap.get(cur);
						label=(String)tabMap.get(cur);
					
					if(label==null || (label!=null && label.equals(""))){
					    
					    label=cur;
					}
					
					//creates the panel for the current tab and adds it to the tabbed pane
					if(cur!=null && list!=null && label!=null){
					    
						panel=getTabPanel(list);

						if(panel!=null){

						    tpanel=new JPanel();
						    tpanel.setLayout(new BorderLayout());
						    tpanel.add(panel, BorderLayout.NORTH);
						    tpanel.setBorder(new EmptyBorder(2, 5, 0, 5));
							tabbedPane.add(label, tpanel);
						}
					}
				}

				return tabbedPane;
			}
			
			return null;
		}
		
		/**
		 * @param propertyItemsList the list of the property items
		 * @return a JPanel containing the widgets allowing to modify the property values
		 */
		protected JPanel getTabPanel(LinkedList propertyItemsList){
		
			if(propertyItemsList!=null){
				
				//the panel linked with a tab
				JPanel panelInTab=new JPanel();
				panelInTab.setBorder(new EmptyBorder(0, 0, 20, 0));

				panelInTab.setLayout(new BoxLayout(panelInTab, BoxLayout.Y_AXIS));
				SVGPropertyItem cur=null;
				SVGPropertiesWidget widget=null;
				TitledBorder border=null;
				JPanel container=null;
				
				//for each property item, the linked panel is added to the tab panel
				for(Iterator it=propertyItemsList.iterator(); it.hasNext();){
				    
					cur=(SVGPropertyItem)it.next();
					
					if(cur!=null){
					    
						widget=getPropertyComponents(cur);
						
						if(widget!=null){
						    
						    widgets.add(widget);
						}
						
						if(widget!=null && widget.getComponent()!=null && widget.getLabel()!=null){
							
						    container=new JPanel();
						    border=new TitledBorder(widget.getLabel());
						    container.setBorder(border);
						    container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
						    widget.getComponent().setBorder(new EmptyBorder(0, 15, 0, 0));

						    container.add(widget.getComponent());
						    panelInTab.add(container);
						}
					}
				}

				return panelInTab;
			}
			
			return null;
		}
		
		/**
		 * @param propertyItem a property item object
		 * @return an array containing a label and the widget allowing to change the property of the given node
		 */
		protected SVGPropertiesWidget getPropertyComponents(SVGPropertyItem propertyItem){
			
			//the object that contains the widget and the label to be displayed
			SVGPropertiesWidget widget=null;

			if(propertyItem!=null){

				//the type of the widget that will be used
				String valueType=propertyItem.getPropertyValueType();

				if(valueType!=null && ! valueType.equals("")){
						
					//a color chooser widget
					if(valueType.equals("renderchooser")){

						widget=new SVGPropertiesRenderChooserWidget(propertyItem);
						
					//a combo box widget
					}else if(valueType.equals("combo")){
							
						widget=new SVGPropertiesComboWidget(propertyItem);
						
						//a combo box widget
					}else if(valueType.equals("editablecombo")){
							
						widget=new SVGPropertiesEditableComboWidget(propertyItem);
						
						//a combo box widget
					}else if(valueType.equals("markerchooser")){

						widget=new SVGPropertiesMarkerChooserWidget(propertyItem);
													
					//a slider widget
					}else if(valueType.equals("slider")){
						
						widget=new SVGPropertiesSliderWidget(propertyItem);

					//a radio button widget
					}else if(valueType.equals("tworadiobuttons")){

						widget=new SVGPropertiesTwoRadioButtonsWidget(propertyItem);
						
					//an entry widget
					}else if(valueType.equals("entry")){

						widget=new SVGPropertiesEntryWidget(propertyItem);
							
					//an entry widget with an ok button
					}else if(valueType.equals("validatedentry")){
						
						widget=new SVGPropertiesValidatedEntryWidget(propertyItem);
						
					//an entry widget with an ok button to modify an id
					}else if(valueType.equals("idmodifier")){
					
						widget=new SVGPropertiesIdModifierWidget(propertyItem);
							
					//a widget enabling to choose a font family
					}else if(valueType.equals("fontchooser")){
							
						widget=new SVGPropertiesFontChooserWidget(propertyItem);
							
					//a widget used to choose font size
					}else if(valueType.equals("fontsizechooser")){

						widget=new SVGPropertiesFontSizeChooserWidget(propertyItem);
						
					}else if(valueType.equals("positivenumberchooser") || valueType.equals("numberchooser")){
					    
						widget=new SVGPropertiesNumberChooserWidget(propertyItem);
						
						//a widget used to choose the preserve aspect ratio attribute
					}else if(valueType.equals("preserveaspectratiochooser")){

						widget=new SVGPropertiesPreserveAspectRatioChooser(propertyItem);
					}
				}
			}
				
			return widget;
		}
	}
