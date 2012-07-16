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

import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.actions.toolbar.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;

/**
 * the class allowing to change the properties of a node by
 * modifying its attributes and style elements
 * @author ITRIS, Jordi SUC
 */
public class SVGProperties extends ModuleAdapter{
	
	/**
	 * the ids of the module
	 */
	final private String idproperties="Properties";
	
	/**
	 * the labels
	 */
	private String labelproperties="";

	/**
	 * the editor
	 */
	private Editor editor=null;
	
	/**
	 * the document of the properties
	 */
	private Document docProperties=null;

	/**
	 * the font
	 */
	public final Font theFont=new Font("theFont", Font.ROMAN_BASELINE, 10);
	
	/**
	 * the nodes that are currently selected
	 */
	private final LinkedList selectedNodes=new LinkedList();
	
	/**
	 * the bundle used to get labels
	 */
	private ResourceBundle bundle=null;
	
	/**
	 * the panel in which the widgets panel will be inserted
	 */
	private JPanel propertiesPanel=new JPanel();
	
	/**
	 * the panel displaying the properties
	 */
	private SVGPropertiesWidgetsPanel widgetsPanel=null;
	
	/**
	 * the frame into which the properties panel will be inserted
	 */
	private ToolsFrame propertiesFrame;
	
	/**
	 * the name of the last selected tab
	 */
	private String tabId="";
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public SVGProperties(Editor editor) {
		
		this.editor=editor;
		
		//gets the labels from the resources
        bundle=ResourcesManager.bundle;
		
		if(bundle!=null){
		    
			try{
				labelproperties=bundle.getString("label_properties");
			}catch (Exception ex){}
		}

		//a listener that listens to the changes of the svg handles
		final HandlesListener svgHandleListener=new HandlesListener(){
			
			/**
			 * a listener on the selection changes
			 */
			private SelectionChangedListener selectionListener=null;
			
			/**
			 * the last handle
			 */
			private SVGHandle lastHandle=null;
			
			public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {

				if(lastHandle!=null && selectionListener!=null && lastHandle.getSelection()!=null){
					
					//if a selection listener is already registered on a selection module, it is removed	
					lastHandle.getSelection().removeSelectionChangedListener(selectionListener);
				}
				
				//removes the widgets panel
				if(widgetsPanel!=null){

				    propertiesPanel.removeAll();
				    widgetsPanel.dispose();
				    widgetsPanel=null;
				}
				
				//clears the list of the selected items
				selectedNodes.clear();
				selectionListener=null;
				
				final SVGHandle handle=getSVGEditor().getHandlesManager().getCurrentHandle();
				
				if(handle!=null && handle.getSelection()!=null){

					manageSelection(new HashSet<Element>(
							handle.getSelection().getSelectedElements()));
					
					//the listener of the selection changes
					selectionListener=new SelectionChangedListener(){

						@Override
						public void selectionChanged(Set<Element> selectedElements) {
							
							manageSelection(selectedElements);
						}
					};
					
					//adds the selection listener
					handle.getSelection().addSelectionChangedListener(selectionListener);
					
				}else if(propertiesPanel.isVisible()){

					handleProperties(null);
				}
				
				lastHandle=currentHandle;
			}
			
			/**
			 * updates the selected items and the state of the menu items
			 * @param selectedElements the set of the selected elements
			 */
			protected void manageSelection(Set<Element> selectedElements){

				selectedNodes.clear();
				
				//refresh the selected nodes list
				if(selectedElements!=null){
				    
				    selectedNodes.addAll(selectedElements);
				}
				
				if(selectedNodes.size()>=1){
				    
					if(propertiesPanel.isVisible()){
					    
						handleProperties(selectedNodes);
					}
					
				}else if(propertiesPanel.isVisible()){
				    
					handleProperties(null);		
				}
			}
		};
		
		//adds the svg handle change listener
		editor.getHandlesManager().addHandlesListener(svgHandleListener);
		
		//setting the layout for the properties panel
		propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.X_AXIS));

		//creating the document
		docProperties=ResourcesManager.getXMLDocument("properties.xml");
		docProperties=normalizeXMLProperties(docProperties);

		//creating the internal frame containing the properties panel
		propertiesFrame=new ToolsFrame(editor, idproperties, labelproperties, propertiesPanel);
		
		//setting the visibility changes handler
		Runnable visibilityRunnable=new Runnable(){
			
			public void run() {

				if(! propertiesFrame.isVisible() || selectedNodes.size()==0) {
					
    				handleProperties(null);
					
				}else {
					
    				handleProperties(selectedNodes);
				}
			}
		};
		
		this.propertiesFrame.setVisibilityChangedRunnable(visibilityRunnable);
	}
	
	/**
	 * @return the editor
	 */
	public Editor getSVGEditor(){
		return editor;
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

        HashMap<String, JMenuItem> menuItems=new HashMap<String, JMenuItem>();
        menuItems.put("ToolFrame_"+this.idproperties, propertiesFrame.getMenuItem());
        return menuItems;
	}
	
	@Override
	public HashMap<String, AbstractButton> getToolItems() {

        HashMap<String, AbstractButton> map=new HashMap<String, AbstractButton>();
        map.put(idproperties, propertiesFrame.getToolBarButton());
        return map;
	}

	/**
	 * @return Returns the selectedNodes.
	 */
	protected LinkedList getSelectedNodes() {
		return selectedNodes;
	}

	/**
	 * @return Returns the tabId.
	 */
	protected String getTabId() {
		return tabId;
	}
	
	/**
	 * @param tabId The tabId to set.
	 */
	protected void setTabId(String tabId) {
		this.tabId = tabId;
	}
	
	/**
	 * the method that manages the changes of the properties for the given nodes
	 * @param list a list of nodes
	 */
	protected void handleProperties(LinkedList list){

		//removes the widgets panel
		if(widgetsPanel!=null){

		    propertiesPanel.removeAll();
		    widgetsPanel.dispose();
		    widgetsPanel=null;
		}
		
		final SVGHandle handle=editor.getHandlesManager().getCurrentHandle();

		if(handle!=null && list!=null && list.size()>0){
		    
			LinkedList snodes=new LinkedList(list);

			//gets the accurate subtree given the list of nodes
			Node treeProperties=getXMLProperties(snodes);
			
			if(treeProperties!=null){
			    
				//the map associating a tab to its label and the map associating a tab to a list of property item objects
				LinkedHashMap tabMap=new LinkedHashMap(), propertyItemsMap=new LinkedHashMap();
		
				if(treeProperties!=null){

					String name="", label="";
					LinkedList propertyItemsList=null;
					
					//builds the list of the tabs 
					for(Node cur=treeProperties.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
					    
						if(cur.getNodeName().equals("tab")){
						    
							name=((Element)cur).getAttribute("name");
					
							if(name!=null && ! name.equals("")){
				
								//gets the label of the tab and puts it into the map
								if(bundle!=null){
								    
									try{label=bundle.getString(name);}catch (Exception ex){label=null;}
								}
								
								if(label==null || (label!=null && label.equals(""))){
								    
								    label=name;
								}
						
								tabMap.put(name, label);
				
								//gets the property items list and puts it into the map
								propertyItemsList=getPropertyList(snodes, cur);
								propertyItemsMap.put(name, propertyItemsList);
							}
						}		
					}
		
					//creates the panel
					if(tabMap!=null && propertyItemsMap!=null && tabMap.size()>0 && propertyItemsMap.size()>0){
					    
						if(widgetsPanel!=null){

						    widgetsPanel.dispose();
						}

						widgetsPanel=new SVGPropertiesWidgetsPanel(this, tabMap, propertyItemsMap);
					} 
					
					//adds the property panel into the container and displays it
					if(propertiesPanel!=null && widgetsPanel!=null){

					    propertiesPanel.removeAll();
					    propertiesPanel.add(widgetsPanel);
					    propertiesFrame.revalidate();
					    
						return;
					}
				}
			}
		}
		
		if(bundle!=null){

			//initializes the value of the last selected tab
			tabId="";
			String message="";
			
			try{
				if(selectedNodes.size()<1){
				    
				    message=bundle.getString("property_empty_dialog_none");
				    
				}else if(selectedNodes.size()==1){
				    
				    message=bundle.getString("property_empty_dialog_one");
				    
				}else if(selectedNodes.size()>1){
				    
				    message=bundle.getString("property_empty_dialog_many");
				}
			}catch (Exception ex){}

			JLabel label=new JLabel(message);
            label.setBorder(new EmptyBorder(5, 5, 5, 5));

			propertiesPanel.removeAll();
			propertiesPanel.add(label);
			propertiesFrame.revalidate();
		}
	}
	
	/**
	 * normalizes the xml document
	 * @param doc the raw xml document
	 * @return the normalized document
	 */
	public Document normalizeXMLProperties(Document doc){
		
		//modifies the document to resolve the links within the dom
		//each tab node is appended to the node which have declared having a child whose type is like one of the predefined tabs
		if(doc!=null){
		    
			Node root=doc.getDocumentElement();
		
			if(root!=null){
			    
				//the map associating the name attributes of a tab to its tab node
				HashMap tabMap=new HashMap();
			
				//the list of the "define" nodes
				LinkedList defineList=new LinkedList();
			
				//finds the node that contains the predefined node tabs, and adds all its children to the tab map
				Node cur=null, tab=null;
				String name="";

				for(cur=root.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
				
					if(cur.getNodeName().equals("define")){
					    
						//adds the "define" node to the list
						defineList.add(cur);
					
						//adds all the predefined "tab" nodes to the map
						for(tab=cur.getFirstChild(); tab!=null; tab=tab.getNextSibling()){
						    
							if(tab.getNodeName().equals("tab")){

								try{name=((Element)tab).getAttribute("name");}catch (Exception ex){name="";}
							
								if(name!=null && ! name.equals("")){
								    
								    tabMap.put(name, tab.cloneNode(true));
								}
							}
						}
					}
				}
			
				//removes all the "define" nodes from the root node
				for(Iterator it=defineList.iterator(); it.hasNext();){
				    
					try{root.removeChild((Node)it.next());}catch (Exception ex){}
				}
				
				defineList.clear();

				//the list of the "tab" nodes to add to the "module" node
				LinkedList tabList=new LinkedList();
				Node use=null;
			
				//appends the tab nodes to the node that define a link to a predefined tab
				for(cur=root.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
				
					if(cur.getNodeName().equals("module")){
					
						//gets the "use" nodes defined in the "module" node and the "tab" nodes that will be used to replace them
						for(use=cur.getFirstChild(); use!=null; use=use.getNextSibling()){
						    
							if(use.getNodeName().equals("use")){
							    
								try{name=((Element)use).getAttribute("name");}catch(Exception ex){name="";}
							
								//adds the tab node to the list
								if(name!=null && !name.equals("")){
								    
									tab=(Node)tabMap.get(name);
									
									if(tab!=null){
									    
									    tabList.add(tab.cloneNode(true));
									}
								}
								
							}else if(use.getNodeName().equals("tab")){
							    
								tabList.add(use.cloneNode(true));
							}
						}
					
						//removes all the child nodes from the "module" node
						for(tab=cur.getFirstChild(); tab!=null; tab=cur.getFirstChild()){
						    
							cur.removeChild(tab);
						}
						
						//appends all the "tab" nodes to the "module" node
						
						for(Iterator it=tabList.iterator(); it.hasNext();){
						    
							try{cur.appendChild((Node)it.next());}catch (Exception ex){}
						}
					}
					
					//initializes the list of "tab" nodes
					tabList.clear();
				}
			}
		}

		return doc;
	}
	
	/**
	 *  gets the subtree describing the properties that can be changed for the given list of nodes
	 * @param list a list of nodes
	 * @return the subtree
	 */
	protected Node getXMLProperties(LinkedList list){
		
		Node subTree=null;
		
		if(list!=null && list.size()>0){

			Node current=null;
			String name="";
			Iterator it=list.iterator();
			
			try{
				current=(Node)it.next();
				subTree=getXMLProperties(current);
				name=current.getNodeName();
			}catch (Exception ex){return null;}
			
			while(it.hasNext()){
			    
				try{
					current=(Node)it.next();
				}catch (Exception ex){current=null;}
				
				if(current!=null){
					
					if(name!=null && ! name.equals(current.getNodeName())){
					    
					    subTree=intersectXMLProperties(subTree, getXMLProperties(current));
					}
					
					name=current.getNodeName();
				}
			}
		}
		
		return subTree;
	}
	
	/**
	 * gets a subtree that contains the nodes that can be found in the node1 subtree AND in the node2 subtree
	 * @param node1 a subtree
	 * @param node2 a subtree
	 * @return a subtree that contains the nodes that can be found in the node1 subtree AND in the node2 subtree
	 */
	protected Node intersectXMLProperties(Node node1, Node node2){
	    
		Node node=null;
		
		if(node1!=null && node2!=null){
		    
			//clones the node
			node=node1.cloneNode(false);
			
			//removes all of its children
			while(node.hasChildNodes()){
			    
			    node.removeChild(node.getFirstChild());
			}
			
			Node 	tab=null, ctab=null, tab2=null, 
						property=null, cproperty=null, property2=null;
			String name=null;
			
			for(tab=node1.getFirstChild(); tab!=null; tab=tab.getNextSibling()){

				if(tab.getNodeName().equals("tab")){
				    
					//for each tab in node1, tests if it is in node2
					if((tab2=getTab(node2, tab))!=null){
					    
						//clones the node
						ctab=tab.cloneNode(false);
						
						//removes all of its children
						while(ctab.hasChildNodes()){
						    
						    ctab.removeChild(ctab.getFirstChild());
						}
						
						node.appendChild(ctab);
						
						//for each property in the current tab in node1, tests if it is in node2
						for(property=tab.getFirstChild(); property!=null;property=property.getNextSibling()){
							
							if((property2=getProperty(tab2, property))!=null){
							    
								//clones the node
								cproperty=property.cloneNode(true);
								//appends it to the tab
								ctab.appendChild(cproperty);	
							}
						}
					}
				}
			}
		}
		
		return node;
	}
	
	/**
	 * 
	 * @param node a subtree
	 * @param tab a node whose name is "tab"
	 * @return the tab node whose attribute name is equal to the attribute name of the given tab or null if the node does not contain the tab
	 */
	protected Node getTab(Node node, Node tab){
		
		if(node!=null && tab!=null && tab.getNodeName().equals("tab")){
		    
			Node tab2=null;
			String tabName="";
			
			//gets the value of the name attribute of the tab
			try{
				Element element=(Element)tab;
				tabName=element.getAttribute("name");
			}catch (Exception ex){tabName="";}
			
			if(tabName!=null && ! tabName.equals("")){
				
				String name="";

				//for each tab in node2, test if it has the same name as the given tab
				for(tab2=node.getFirstChild(); tab2!=null; tab2=tab2.getNextSibling()){
					
					if(tab2.getNodeName().equals("tab")){
					    
						//gets the value of the name attribute of the tab
						try{
							Element element=(Element)tab2;
							name=element.getAttribute("name");
						}catch (Exception ex){name="";}
						
						if(name!=null && name.equals(tabName)){
						    
						    return tab2;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * @param tab the tab in which the property is contained
	 * @param property the property node
	 * @return the property node whose attribute name is equal to the attribute name of the given property 
	 * or null if the node does not contain the property
	 */
	protected Node getProperty(Node tab, Node property){
		
		if(tab!=null && property!=null && property instanceof Element){
			
			Node prop=null;
			String propertyName="", propertyType="", propertyName2="", propertyType2="";
			
			//gets the name and type of the given property
			try{
				Element element=(Element)property;
				propertyName=element.getAttribute("name");
				propertyType=element.getAttribute("type");
			}catch (Exception ex){return null;}
			
			if(propertyName!=null && ! propertyName.equals("") && propertyType!=null && ! propertyType.equals("")){

				for(prop=tab.getFirstChild(); prop!=null; prop=prop.getNextSibling()){
				    
					//if the node is a property node
					if(prop.getNodeName().equals("property")){
						//gets the name and type of the current node
						try{
							Element element=(Element)prop;
							propertyName2=element.getAttribute("name");
							propertyType2=element.getAttribute("type");
						}catch (Exception ex){propertyName2=null; propertyType2=null;}
						
						//tests if the current property name and type are equal to the gievn property name and type
						if(propertyName2!=null && propertyType2!=null 
							&& propertyName2.equals(propertyName) && propertyType2.equals(propertyType)){
						    
								return prop;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * gets the subtree describing the properties that can be changed for the type of the given node
	 * @param node a node
	 * @return the subtree
	 */
	protected Node getXMLProperties(Node node){
		
		if(docProperties!=null && node!=null){
		    
			String name=node.getNodeName();
			
			if(name!=null && ! name.equals("")){
			    
				Document docPrp=(Document)docProperties.cloneNode(true);
				Element root=docPrp.getDocumentElement();
				
				if(root!=null){
				    
				    Node current=null;
					String cname="";
					
					//for each root child, searches the one whom value of the "name" attribute is the type of the given node
					for(current=root.getFirstChild(); 
						current!=null; current=current.getNextSibling()){
					    
						if(current.getNodeName().equals("module")){
						    
							cname=((Element)current).getAttribute("name");
							
							if(cname!=null && cname.equals(name)){
							    
							    break;
							}
						}
					}
					
					if(current!=null){
					    
						return current;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * @param nodelist the list of the selected nodes
	 * @param subTree a node 
	 * @return the list of the property item objects
	 */
	protected LinkedList getPropertyList(LinkedList nodelist, Node subTree){
		
		LinkedList list=new LinkedList();
		
		if(subTree!=null){
		    
			Node cur=null;
			SVGPropertyItem item=null;

			//for each property node
			for(cur=subTree.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
			    
				//get the property item object
				if(cur.getNodeName().equals("property")){
				    
					//get the property item object
					item=getProperty(nodelist, cur);
					
					//adds it to the list
					if(item!=null)list.add(item);
				}
			}
		}
		
		if(list!=null && list.size()>0){
		    
		    return list;
		}
		
		return null;
	}
	
	/**
	 * creates the property item object given a property node
	 * @param nodelist the list of the selected nodes
	 * @param property a property node
	 * @return a SVGPropertyItem object
	 */
	protected SVGPropertyItem getProperty(LinkedList nodelist, Node property){
		
		SVGPropertyItem propertyItem=null;
		
		if(property!=null){
			
			String type="", name="", valueType="", defaultValue="", constraint="";
			
			//the attributes of the property node
			type=((Element)property).getAttribute("type");
			name=((Element)property).getAttribute("name");
			valueType=((Element)property).getAttribute("valuetype");
			defaultValue=((Element)property).getAttribute("defaultvalue");
			constraint=((Element)property).getAttribute("constraint");
			
			LinkedHashMap values=null;
			
			if(property.hasChildNodes()){
			    
				//fills the map with the attributes of each possible value for the property item
				values=new LinkedHashMap();
				String itemName="", itemValue=""; 
				Node cur;
				
				for(cur=property.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
				    
					if(cur.getNodeName().equals("item")){
					    
						//the attributes of the item
						itemName=((Element)cur).getAttribute("name");
						itemValue=((Element)cur).getAttribute("value");
						
						if(itemName!=null && ! itemName.equals("") && itemValue!=null && !itemValue.equals("")){
						    
							values.put(itemName, itemValue);
						}
					}
				}
				
				//if the map is empty, it is set to null
				if(values.size()<=0){
				    
				    values=null;
				}
			}
			
			//creates the property item object
			if(type!=null && name!=null && valueType!=null && ! type.equals("") && ! name.equals("") && ! valueType.equals("")){
				
			    propertyItem=new SVGPropertyItem(this, nodelist, type, name, valueType, defaultValue, constraint, values);
			}
		}

		return propertyItem;
	}
	
	/**
	 * gets the name of the module
	 * @return the name of the module
	 */
	public String getName(){
	    
		return idproperties;
	}
}
