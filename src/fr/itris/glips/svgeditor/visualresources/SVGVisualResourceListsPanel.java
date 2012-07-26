/*
 * Created on 26 août 2004
 * 
 =============================================
 GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
 GLIPS Graffiti Editor, a SVG Editor
 Copyright (C) 2003 Jordi SUC, Philippe Gil, SARL ITRIS
 
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
package fr.itris.glips.svgeditor.visualresources;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the panel displayed in the dialog
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceListsPanel extends JPanel{
	
	/**
	 * the constant describing the creation of a new resource
	 */
	private static final int RESOURCE_NEW=0;
	
	/**
	 * the constant describing the modification of a resource
	 */
	private static final int RESOURCE_MODIFIED=1;
	
	/**
	 * the constant describing the suppression of a resource
	 */
	private static final int RESOURCE_DELETED=2;
    
    /**
     * the map associating the name of a model to a visual resource models
     */
    private final Map vresourceModels=Collections.synchronizedMap(new LinkedHashMap());
    
    /**
     * the tab panel
     */
    private JTabbedPane tabPanel=null;
    
    /**
     * a small font
     */
    private static final Font smallFont=new Font("smallFont", Font.ROMAN_BASELINE, 9);
    
    /**
     * the font
     */
    private static final Font theFont=new Font("theFont", Font.ROMAN_BASELINE, 10);
    
    /**
     * the runnables enabling to dispose this panel
     */
    private final LinkedList<Runnable> disposers=new LinkedList<Runnable>();
    
    /**
     * the listener to the changes of the selection of the tabs
     */
    private ChangeListener changeListener=null;
    
    /**
     * the visual resources module
     */
    private SVGVisualResources visualResources=null;
    
    /**
     * the map associating the type of a resource to a JList
     */
    private Map listMap=Collections.synchronizedMap(new HashMap());
    
    /**
     * the map associating the id of a resource to its list item
     */
    private Map itemListMap=Collections.synchronizedMap(new HashMap());
    
    /**
     * the cell renderer
     */
    private ListCellRenderer listCellRenderer=new SVGVisualResourcesListCellRenderer();
    
    /**
     * the resource state object
     */
    private SVGVisualResourceState resourceState=null;
    
    /**
     * the map associating the id of a resource to the panel representing this resource in the list
     */
    private Map<SVGVisualResourceObject, JComponent> cellPanels=Collections.synchronizedMap(
    																						new HashMap<SVGVisualResourceObject, JComponent>());
    
    /**
     * the current svg handle
     */
    private SVGHandle svgHandle=null;
    
    /**
     * the constructor of the class
     * @param visualResources the visual resources module
     * @param vresourceModels the list of the visual resource items
     */
    public SVGVisualResourceListsPanel(SVGVisualResources visualResources, LinkedList vresourceModels){
        
        this.visualResources=visualResources;
        this.svgHandle=visualResources.getSVGEditor().getHandlesManager().getCurrentHandle();
        this.resourceState=visualResources.getResourceState(svgHandle);
        
        //fills the map of the resource models
        if(vresourceModels!=null){
        	
        	SVGVisualResourceModel model=null;
        	
        	for(Iterator it=vresourceModels.iterator(); it.hasNext();){
        		
        		model=(SVGVisualResourceModel)it.next();
        		
        		if(model!=null){
        			
        			this.vresourceModels.put(model.getName(), model);
        		}
        	}
        }
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        //getting the tab panel
        tabPanel=createTabbedPane();

        if(tabPanel!=null){
            
        	//the listener to the changes of the tab
            changeListener=new ChangeListener(){
                
                public void stateChanged(ChangeEvent arg0) {
                    
                    if(tabPanel!=null){
                        
                        resourceState.setSelectedTabId(tabPanel.getTitleAt(tabPanel.getSelectedIndex()));
                    }
                }
            };
            
            tabPanel.addChangeListener(changeListener);
            
            if(resourceState!=null) {
            	
            	setSelectedTab(resourceState.getSelectedTabId());
            }
            
            add(tabPanel);
        }
        
        setBorder(new EmptyBorder(0, 0, 1, 0));
    }

    /**
     * @return Returns the visualResources.
     */
    protected SVGVisualResources getVisualResources() {
        
        return visualResources;
    }
    
    /**
     * removes all the listeners
     */
    public void dispose(){

        if(tabPanel!=null && changeListener!=null){
            
            //removes the change listener
            tabPanel.removeChangeListener(changeListener);
            
            //runs all the disposers
            Runnable disposer=null;
            
            for(Iterator it=disposers.iterator(); it.hasNext();){
                
                disposer=(Runnable)it.next();
                
                if(disposer!=null){
                    
                    disposer.run();
                }
            }
            
            //clears the maps
            vresourceModels.clear();
            disposers.clear();
            listMap.clear();
            itemListMap.clear();
            
            for(SVGVisualResourceObject obj : cellPanels.keySet()){
            	
            	JComponent cmp=cellPanels.get(obj);
            	cmp.removeAll();
            }
            
            cellPanels.clear();
            
            tabPanel.removeAll();
            svgHandle=null;
            removeAll();
        }
    }
    
    /**
     * refreshes the resource objects and the lists containing them
     * @param type the type of the action that has triggered this action to be called
     * @param resourceType the type of the resource
     * @param currentElement the resource element that is currently used
     * @param oldElement a resource element that has been removed or modified
     */
    public void refreshResources(int type, String resourceType, Element currentElement, Element oldElement){

    	if(resourceType!=null && ! resourceType.equals("")){
    		
    	    //getting the map associating the id of a resource to the resource node
        	LinkedList resourcesNames=new LinkedList();
        	resourcesNames.add(resourceType);
    		String resourceId="", oldResourceId="";
    		
    		SVGVisualResourceModel model=(SVGVisualResourceModel)vresourceModels.get(resourceType);
    		ResourceImageManager resImgManager=Editor.getEditor().getResourceImageManager();
    		SVGVisualResourceObject resObj=null;
    		
    		if(model!=null){
    			
    			//retrieving the list corresponding to the model
    			JList jList=(JList)listMap.get(model.getName());
    			
    			DefaultListModel listModel=null;
    			
    			if(jList!=null){
    				
    				listModel=((DefaultListModel)jList.getModel());
    			}
    			
    			if(listModel!=null){
    				
        	    	if(type==RESOURCE_NEW && currentElement!=null){
        	    		
        	    		resourceId=currentElement.getAttribute("id");

        	    		if(! resourceId.equals("")){
        	    			
        	    			//creates a new resource object
        	    			resObj=model.createVisualResourceObject(currentElement);
        	    			
        	    			//creates the list item
        	    			SVGVisualResourceListItem item=new SVGVisualResourceListItem(svgHandle, resObj);
        	    			itemListMap.put(resourceId, item);
        	    			
        	    			//adding the item to the list
        	    			listModel.insertElementAt(item, 0);
        	    			jList.setSelectedIndex(0);
        	    		}

        	    	}else if(type==RESOURCE_DELETED && oldElement!=null){

        	    		SVGVisualResourceListItem item=null;
        	    		
        	    		//invalidating the old resource representation
        	    		oldResourceId=oldElement.getAttribute("id");
        	    		
        	    		if(oldResourceId!=null && ! oldResourceId.equals("")){
        	    			
        	        		resImgManager.invalidateResourceRepresentation(svgHandle, oldResourceId);
        	    		}
        	    		
        	    		//removes the resource object linked with the old element
        	    		model.removeVisualResourceObject(oldResourceId);
        	    		
        	    		//getting the list item linked with the resource element
        	    		item=(SVGVisualResourceListItem)itemListMap.get(oldResourceId);
        	    		
        	    		if(item!=null){
        	    			
        	    			//removes the item for the JList
        	    			listModel.removeElement(item);
        	    		}
        	    		
        	    		//removes the item 
        	    		itemListMap.remove(oldResourceId);
        	    		
        	    		if(listModel.size()>0){
        	    			
        	    			jList.setSelectedIndex(0);
        	    		}
        	    		
        	    	}else if(type==RESOURCE_MODIFIED && currentElement!=null && oldElement!=null){
        	    		
        	    		int itemIndex=0;
        	    		
        	    		SVGVisualResourceListItem item=null;
        	    		
        	    		//invalidating the old resource representation
        	    		oldResourceId=oldElement.getAttribute("id");
        	    		
        	    		if(oldResourceId!=null && ! oldResourceId.equals("")){
        	    			
        	        		resImgManager.invalidateResourceRepresentation(svgHandle, oldResourceId);
        	    		}
        	    		
        	    		//removes the resource object linked with the old element
        	    		model.removeVisualResourceObject(oldResourceId);
        	    		
        	    		//getting the old list item linked with the resource element
        	    		item=(SVGVisualResourceListItem)itemListMap.get(oldResourceId);
        	    		
        	    		if(item!=null){
        	    			
        	    			//retrieving the index of the list item of the old element that will be removed
        	    			itemIndex=listModel.indexOf(item);
        	    			
        	    			//removes the old item for the JList
        	    			listModel.removeElement(item);
        	    		}
        	    		
        	    		//removes the old item 
        	    		itemListMap.remove(oldResourceId);
        	    		
        	    		//the new resource id
        	    		resourceId=currentElement.getAttribute("id");

        	    		if(! resourceId.equals("")){
        	    			
        	    			//creates a new resource object
        	    			resObj=model.createVisualResourceObject(currentElement);
        	    			
        	    			//creates the list item
        	    			item=new SVGVisualResourceListItem(svgHandle, resObj);
        	    			itemListMap.put(resourceId, item);
        	    			
        	    			//adding the item to the list
        	    			listModel.insertElementAt(item, itemIndex);
        	    			jList.setSelectedIndex(itemIndex);
        	    		}
        	    	}
    			}
    		}
    	}
		
    	//TODO visualResources.getSVGEditor().getSVGToolkit().forceReselection();
    }
    
    /**
     * removes the cell panel representing the given resource object
     * @param resObj a resource object
     */
    protected void removeCellPanel(SVGVisualResourceObject resObj){
    	
    	if(resObj!=null){
    		
    		JPanel panel=(JPanel)cellPanels.get(resObj);
    		
    		if(panel!=null){
    			
    			if(panel.getParent()!=null){
    				
    				panel.getParent().remove(panel);
    			}
    			
    			panel.removeAll();
    		}
    		
    		cellPanels.remove(resObj);
    	}
    }
    
    /**
     * removes all the cell panels and disposes them
     */
    protected void removeAllCellPanels(){
    	
    	JPanel panel=null;
    	
    	for(Iterator it=new LinkedList(cellPanels.values()).iterator(); it.hasNext();){
    		
    		panel=(JPanel)it.next();
    		
    		if(panel!=null){
    			
    			if(panel.getParent()!=null){
    				
    				panel.getParent().remove(panel);
    			}
    			
    			panel.removeAll();
    		}
    	}
    	
    	cellPanels.clear();
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
     * @return a JTabbedPane containing the widgets allowing to change the values of the visual resources
     */
    protected JTabbedPane createTabbedPane(){
        
        JTabbedPane tabbedPanel=new JTabbedPane();

        //creates the tabs that will be contained in the tabbed panel
        SVGVisualResourceModel model=null;
        JPanel panel=null;
        Iterator it;
        
        for(it=new LinkedList(vresourceModels.values()).iterator(); it.hasNext();){
            
                model=(SVGVisualResourceModel)it.next();
            
            if(model!=null){
                
                panel=createTabPanel(model);
                tabbedPanel.addTab(model.getTabName(), panel);
            }
        }
        
        return tabbedPanel;
    }
    
    /**
     * @param vresourceModel a visual resource model
     * @return a JPanel containing the widgets allowing to modify the visual resources
     */
    protected JPanel createTabPanel(SVGVisualResourceModel vresourceModel){

        final Element defs=getVisualResources().getDefs(svgHandle);
        
        //the finalised variable
        final SVGVisualResourceModel fvresourceModel=vresourceModel;
        
        //the panel containing the scrollpane
        JPanel panel=new JPanel();
        panel.setLayout(new BorderLayout());
        
        //creates the list of items that will be displayed in the list widget
        final DefaultListModel listModel=new DefaultListModel();
        SVGVisualResourceObject resObj=null;
        LinkedList resourceObjects=vresourceModel.getResourceObjectsList();
        SVGVisualResourceListItem item=null;
        int i=0, selectedIndex=-1;
        
        for(Iterator it=resourceObjects.iterator(); it.hasNext();){
            
            //gets the id and the index for the list item
                resObj=(SVGVisualResourceObject)it.next();
            
            if(resObj!=null){
            	
            	item=new SVGVisualResourceListItem(svgHandle, resObj);
            	itemListMap.put(resObj.getResourceId(), item);
            	listModel.addElement(item);
            }
        }
        
        //the list and the scrollpane
        final JList list=new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(8);
        
        //adding the list to the map
        listMap.put(vresourceModel.getName(), list);
 
        /*********************adding the drag and drop support**************************/

		/*//adding a drag gesture listener to the color panel
		final DragGestureListener dragGestureListener=new DragGestureListener(){

	        public void dragGestureRecognized(DragGestureEvent evt) {
	            
	        	int selectedIndex=list.locationToIndex(evt.getDragOrigin());
                final Object obj=list.getModel().getElementAt(selectedIndex);
                list.setSelectedIndex(selectedIndex);
                
                if(obj!=null){
                	
                	final String resourceId=((SVGVisualResourceListItem)obj).getVisualResourceObject().getResourceId();
                    
            		//the transferable
                	final Transferable transferable=new Transferable(){

                         public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
                             
                             if(df!=null && df.isMimeTypeEqual(DataFlavor.stringFlavor)){

                                 return resourceId;
                             }
                             
                             return null;
                         }

                         public DataFlavor[] getTransferDataFlavors() {

                             DataFlavor[] dfs=new DataFlavor[1];
                             dfs[0]=DataFlavor.stringFlavor;
                             
                             return dfs;
                         }

                         public boolean isDataFlavorSupported(DataFlavor df) {

                             if(df!=null && df.isMimeTypeEqual(DataFlavor.stringFlavor)){
                                 
                                 return true;
                             }
                             
                             return false;
                         }
             		};

                    SVGVisualResourceObject res=((SVGVisualResourceListItem)obj).getVisualResourceObject();
                    
                    if(res.canBeModified()){
                        
                    	Image cursorImage=getVisualResources().getSVGEditor().getResourceImageManager().getImage(frame, res.getResourceId(), false);
                    	DragSource dragSource=SVGEditor.getSVGEditor().getDragSource();
                    	Cursor cursor=SVGEditor.getSVGEditor().getSVGToolkit().createCursorFromImage(cursorImage);

                    	dragSource.startDrag(evt, cursor, transferable, null);
                    	evt.getTriggerEvent().consume();
                    }
                }
	        }
		};

		//adding a drag gesture tokenizer to this component
		final DragGestureRecognizer dragGestureRecognizer=getVisualResources().getSVGEditor().getDragSource().
									createDefaultDragGestureRecognizer(list, DnDConstants.ACTION_COPY, dragGestureListener);
		
		disposers.add(new Runnable(){

            public void run() {

                dragGestureRecognizer.removeDragGestureListener(dragGestureListener);
                dragGestureRecognizer.setComponent(null);
                list.setDropTarget(null);
            }
		});*/
		
        /******************************************************************************/

        //setting the list cell renderer
        list.setCellRenderer(listCellRenderer);
        
        //the scroll pane in which the list will be inserted
        JScrollPane scpanel=new JScrollPane(list);

        //the labels for the buttons
        String 	newLabel="", duplicateLabel="", deleteLabel="", importLabel="", propLabel="", 
        			deleteMessage="", deleteTitle="", errorTitle="", cantDeleteMessage="";
        
        ResourceBundle bundle=ResourcesManager.bundle;
        
        if(bundle!=null){
            
            newLabel=bundle.getString("labelnew");
            duplicateLabel=bundle.getString("labelduplicate");
            deleteLabel=bundle.getString("labeldelete");
            importLabel=bundle.getString("labelimport");
            propLabel=bundle.getString("labelproperties");
            deleteMessage=bundle.getString("vresource_deleteresourcemessage");
            deleteTitle=bundle.getString("labeldelete");
            errorTitle=bundle.getString("labelerror");
            cantDeleteMessage=bundle.getString("vresource_cantdeleteresource");
        }
        
        final String ferrorTitle=errorTitle, fcantDeleteMessage=cantDeleteMessage;
        
        final ImageIcon 	newIcon=ResourcesManager.getIcon("New", false), 
	            					duplIcon=ResourcesManager.getIcon("Duplicate", false), 
	            					duplDisIcon=ResourcesManager.getIcon("Duplicate", true), 
	            					delIcon=ResourcesManager.getIcon("Delete", false),  
	            					delDisIcon=ResourcesManager.getIcon("Delete", true), 
	            					impIcon=ResourcesManager.getIcon("Import", false), 
	            					impDisIcon=ResourcesManager.getIcon("Import", true), 
	            					propIcon=ResourcesManager.getIcon("Properties", false), 
	            					propDisIcon=ResourcesManager.getIcon("Properties", true);

        //the buttons used to make actions on the resources contained in the tab
        final JButton newBt=new JButton(newIcon), 
        duplicateBt=new JButton(duplDisIcon),
        deleteBt=new JButton(delDisIcon),
        importBt=new JButton(impDisIcon),
        propBt=new JButton(propDisIcon);
        
        //the tool tips
        newBt.setToolTipText(newLabel);
        duplicateBt.setToolTipText(duplicateLabel);
        deleteBt.setToolTipText(deleteLabel);
        importBt.setToolTipText(importLabel);
        propBt.setToolTipText(propLabel);
        
        Insets buttonInsets=new Insets(1, 1, 1, 1);
        
        newBt.setMargin(buttonInsets);
        duplicateBt.setMargin(buttonInsets);
        deleteBt.setMargin(buttonInsets);
        importBt.setMargin(buttonInsets);
        propBt.setMargin(buttonInsets);
        
        duplicateBt.setEnabled(false);
        deleteBt.setEnabled(false);
        importBt.setEnabled(false);
        propBt.setEnabled(false);
        
        //the panel containing the buttons
        JPanel buttons=new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(newBt);
        buttons.add(duplicateBt);
        buttons.add(propBt);
        buttons.add(importBt);
        buttons.add(deleteBt);
        
        //the selection listener
        final ListSelectionListener listSelectionListener=new ListSelectionListener(){
            
            public void valueChanged(ListSelectionEvent e) {
            	
                SVGVisualResourceListItem item=null;
                SVGVisualResourceObject resObj=null;
                
                //getting the list item
                if(list.getModel().getSize()>0){
                    
                        item=(SVGVisualResourceListItem)list.getSelectedValue();
                }
                
                //enables or disables the buttons
                if(item!=null){
                    
                    //sets the new current item name
                    if(resObj!=null){
                        
                    	resourceState.setSelectedItemId(resObj.getResourceModel().getName(), resObj.getResourceId());
                    }
                    
                    duplicateBt.setEnabled(false);
                    duplicateBt.setIcon(duplDisIcon);
                    deleteBt.setEnabled(false);
                    deleteBt.setIcon(delDisIcon);
                    importBt.setEnabled(false);
                    importBt.setIcon(impDisIcon);
                    propBt.setEnabled(false);
                    propBt.setIcon(propDisIcon);
                    
                    resObj=item.getVisualResourceObject();
                    
                    if(resObj!=null && resObj.canBeModified()){
                        
                        duplicateBt.setEnabled(true);
                        duplicateBt.setIcon(duplIcon);
                        deleteBt.setEnabled(true);
                        deleteBt.setIcon(delIcon);
                        propBt.setEnabled(true);
                        propBt.setIcon(propIcon);
                        
                    }else if(resObj!=null && ! resObj.canBeModified()){
                        
                        importBt.setEnabled(true);
                        importBt.setIcon(impIcon);
                    }
                }
            } 
        };
        
        //the listener to the new button
        final ActionListener newBtListener=new ActionListener(){
            
            public void actionPerformed(ActionEvent e) {
                
            	createNewResource(defs, fvresourceModel);
            }
        };
        
        //the listener to the duplicate button
        final ActionListener duplicateBtListener=new ActionListener(){
            
            public void actionPerformed(ActionEvent e) {
                
                SVGVisualResourceListItem item=(SVGVisualResourceListItem)list.getSelectedValue();
                
                if(item!=null){

                    duplicateResource(item.getVisualResourceObject());
                }
            }
        };
        
        final String fdeleteMessage=deleteMessage, fdeleteTitle=deleteTitle;

        //the listener to the delete button
       final  ActionListener deleteBtListener=new ActionListener(){
            
            public void actionPerformed(ActionEvent e) {
                
                SVGVisualResourceListItem item=(SVGVisualResourceListItem)list.getSelectedValue();
                SVGVisualResourceObject resObj= item.getVisualResourceObject();
				
                //if the resource is not used by shapes on the canvas
            	if(resObj!=null && 
            			! svgHandle.getSvgResourcesManager().isResourceUsed(
            					resObj.getResourceId())) {
            		
                    int returnVal=JOptionPane.showConfirmDialog(
                    		Editor.getParent(), fdeleteMessage, fdeleteTitle, JOptionPane.YES_NO_OPTION); 
                    
                    if(returnVal==JOptionPane.YES_OPTION){

                    	removeResource(resObj);
                    }
            		
            	}else{
            		
            		//if the resource is used by other elements
            		JOptionPane.showMessageDialog(
            				Editor.getParent(), fcantDeleteMessage, ferrorTitle, JOptionPane.ERROR_MESSAGE);
            	}
            }
        };
        
        //the listener to the import button
        final ActionListener importBtListener=new ActionListener(){
            
            public void actionPerformed(ActionEvent e) {
                
            	SVGVisualResourceListItem item=(SVGVisualResourceListItem)list.getSelectedValue();
                SVGVisualResourceObject resObj=null;
                
                if(svgHandle!=null && defs!=null && item!=null){
                    
                    resObj=item.getVisualResourceObject();
                    
                    //exports the resource node to the "defs" node if it is contained in the resource store
                    if(resObj!=null){
                        
                        importResource(defs, resObj);
                    }
                }
            }
        };
        
        //the listener to the properties button
        final ActionListener propBtListener=new ActionListener(){
            
            public void actionPerformed(ActionEvent e) {
                
            	SVGVisualResourceListItem item=(SVGVisualResourceListItem)list.getSelectedValue();
                SVGVisualResourceObject resObj=null;
                
                if(item!=null){
                    
                	modifyProperties(item.getVisualResourceObject());
                }
            }
        };
        
        //a mouse listener to hande double clicks
        final MouseListener doubleClickOnListListener=new MouseAdapter() {
            
            public void mouseClicked(MouseEvent e) {
                
                if (e.getClickCount()==2) {
                    
                	propBt.doClick();
                 }
            }
        };
        
        list.addMouseListener(doubleClickOnListListener);
        
        //adds the listener to the "new" button
        newBt.addActionListener(newBtListener);

        //adds the listener to the "duplicate" button
        duplicateBt.addActionListener(duplicateBtListener);
        
        //adds the listener to the "delete" button
        deleteBt.addActionListener(deleteBtListener);
        
        //adds the listener to the "import" button
        importBt.addActionListener(importBtListener);
        
        //adds the listener to the "properties" button
        propBt.addActionListener(propBtListener);
        
        //adds the listener to the list selection
        list.addListSelectionListener(listSelectionListener);
        
		disposers.add(new Runnable(){

            public void run() {

                newBt.removeActionListener(newBtListener);
                duplicateBt.removeActionListener(duplicateBtListener);
                deleteBt.removeActionListener(deleteBtListener);
                importBt.removeActionListener(importBtListener);
                propBt.removeActionListener(propBtListener);
                list.removeListSelectionListener(listSelectionListener);
                list.removeMouseListener(doubleClickOnListListener);
            }
		});
        
        //sets the selected index for the list
        if(selectedIndex>=0){
            
            list.setSelectedIndex(selectedIndex);
            
        }else{
            
            list.setSelectedIndex(0);
        }
        
        //adds the widgets
        panel.add(scpanel, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * creates a new resource node
     * @param parentElement the parent of the created resource
     * @param vresourceModel a resource model
     */
    protected void createNewResource(Element parentElement, SVGVisualResourceModel vresourceModel){
    	
        if(svgHandle!=null && parentElement!=null && vresourceModel!=null){

        	final Element fparentElement=parentElement;
        	final String name=vresourceModel.getName();
        	
        	String shapeId="";
        	
        	//if the resource contains shape nodes as children, the id of the shape node is returned by a chooser
        	if(vresourceModel.isShapedResource()){
        		
        		shapeId=SVGVisualResourceShapeIdChooser.showShapeChooserIdDialog(svgHandle);
        	}
        	
        	if(! vresourceModel.isShapedResource() || 
        			(vresourceModel.isShapedResource() && shapeId!=null && ! shapeId.equals(""))){

        		final String fshapeId=shapeId;
        		
				//creating the new resource node
            	final Element newElement=
            		getVisualResources().getVisualResourcesToolkit().createVisualResource(
            			svgHandle, fparentElement, name, fshapeId);
 
	        	if(newElement!=null){
	        		
		        	final String id=newElement.getAttribute("id");
	        		
	        		Runnable executeRunnable=new Runnable(){
	        			
	        			public void run() {

							//appending the resource node
							getVisualResources().getVisualResourcesToolkit().
								appendVisualResource(svgHandle, fparentElement, newElement);
							resourceState.setSelectedItemId(name, id);
							refreshResources(RESOURCE_NEW, name, newElement, null);
	        			}
	        		};
	        		
	        		Runnable undoRunnable=new Runnable(){
	        			
	        			public void run() {
	        			
							getVisualResources().getVisualResourcesToolkit().removeVisualResource(svgHandle, newElement);
							resourceState.setSelectedItemId(name, "");
							refreshResources(RESOURCE_DELETED, name, null, newElement);
	        			}
	        		};
	        		
					//create the undo/redo action and insert it into the undo/redo stack
					UndoRedoAction action=new UndoRedoAction(
						getVisualResources().undoredoresourcesnew, executeRunnable, 
								undoRunnable, executeRunnable, new HashSet<Element>());
			
					UndoRedoActionList actionlist=new UndoRedoActionList(action.getName(), false);
					actionlist.add(action);
					svgHandle.getUndoRedo().addActionList(actionlist, true);
	        	}
        	}
        }
    }
    
    /**
     * duplicate the resource node corresponding to the given resource object
     * @param resourceObject a resource object
     */
    protected void duplicateResource(SVGVisualResourceObject resourceObject){
    	
    	if(svgHandle!=null && resourceObject!=null && 
    			resourceObject.canBeModified() && resourceObject.getParentNode()!=null){

        	final Element fparentElement=(Element)resourceObject.getParentNode();
        	final Element resourceNode=resourceObject.getInitialNode();
        	final String name=resourceObject.getResourceModel().getName();
        	
			//creating the new resource node
        	final Element newElement=getVisualResources().getVisualResourcesToolkit().
        		duplicateVisualResource(svgHandle, resourceNode);

        	if(newElement!=null){
        		
	        	final String id=newElement.getAttribute("id");
        		
        		Runnable executeRunnable=new Runnable(){
        			
        			public void run() {

						//appending the resource node
						getVisualResources().getVisualResourcesToolkit().
							appendVisualResource(svgHandle, fparentElement, newElement);
						resourceState.setSelectedItemId(name, id);
						refreshResources(RESOURCE_NEW, name, newElement, null);
        			}
        		};
        		
        		Runnable undoRunnable=new Runnable(){
        			
        			public void run() {
        			
						getVisualResources().getVisualResourcesToolkit().
							removeVisualResource(svgHandle, newElement);
						resourceState.setSelectedItemId(name, "");
						refreshResources(RESOURCE_DELETED, name, null, newElement);
        			}
        		};
        		
				//create the undo/redo action and insert it into the undo/redo stack
				UndoRedoAction action=new UndoRedoAction(
					getVisualResources().undoredoresources, executeRunnable, 
							undoRunnable, executeRunnable, new HashSet<Element>());
		
				UndoRedoActionList actionlist=new UndoRedoActionList(action.getName(), false);
				actionlist.add(action);
				svgHandle.getUndoRedo().addActionList(actionlist, true);
        	}
    	}
    }
    
    /**
     * duplicate the resource node corresponding to the given resource object
     * @param resourceObject a resource object
     */
    protected void removeResource(SVGVisualResourceObject resourceObject){
    	
    	if(svgHandle!=null && resourceObject!=null && resourceObject.canBeModified() && resourceObject.getParentNode()!=null){

        	final Element fparentElement=(Element)resourceObject.getParentNode();
        	final Element resourceElement=resourceObject.getInitialNode();
        	final String name=resourceObject.getResourceModel().getName();
        	final String id=resourceObject.getResourceId();
    		
    		Runnable executeRunnable=new Runnable(){
    			
    			public void run() {

					//removing the resource node
					getVisualResources().getVisualResourcesToolkit().
						removeVisualResource(svgHandle, resourceElement);
					resourceState.setSelectedItemId(name, "");
					refreshResources(RESOURCE_DELETED, name, null, resourceElement);
    			}
    		};
    		
    		Runnable undoRunnable=new Runnable(){
    			
    			public void run() {
    			
					getVisualResources().getVisualResourcesToolkit().
						appendVisualResource(svgHandle, fparentElement, resourceElement);
					resourceState.setSelectedItemId(name, id);
					refreshResources(RESOURCE_NEW, name, resourceElement, null);
    			}
    		};
    		
			//create the undo/redo action and insert it into the undo/redo stack
			UndoRedoAction action=new UndoRedoAction(
				getVisualResources().undoredoresourcesremove, executeRunnable, 
						undoRunnable, executeRunnable, new HashSet<Element>());
	
			UndoRedoActionList actionlist=new UndoRedoActionList(action.getName(), false);
			actionlist.add(action);
			svgHandle.getUndoRedo().addActionList(actionlist, true);
    	}
    }
    
    /**
     * imports a resource node from the visual resource store
     * @param parentElement the parent element
     * @param resourceObject a resource object
     */
    protected void importResource(Element parentElement, SVGVisualResourceObject resourceObject){
    	
    	if(svgHandle!=null && resourceObject!=null && 
    			! resourceObject.canBeModified() && parentElement!=null){

        	final Element fparentElement=parentElement, resourceNode=resourceObject.getInitialNode();
        	final String name=resourceObject.getResourceModel().getName();
        	
			//creating the new resource node
        	final Element newElement=getVisualResources().getVisualResourcesToolkit().
    			importVisualResource(svgHandle, fparentElement, resourceNode);

        	if(newElement!=null){
        		
	        	final String id=newElement.getAttribute("id");
        		
        		Runnable executeRunnable=new Runnable(){
        			
        			public void run() {

						//appending the resource node
        				getVisualResources().getVisualResourcesToolkit().
        					appendVisualResource(svgHandle, fparentElement, newElement);
						resourceState.setSelectedItemId(name, id);
						refreshResources(RESOURCE_NEW, name, null, newElement);
        			}
        		};
        		
        		Runnable undoRunnable=new Runnable(){
        			
        			public void run() {
        			
        				getVisualResources().getVisualResourcesToolkit().
        					removeVisualResource(svgHandle, newElement);
						resourceState.setSelectedItemId(name, "");
						refreshResources(RESOURCE_DELETED, name, null, newElement);
        			}
        		};
        		
				//create the undo/redo action and insert it into the undo/redo stack
				UndoRedoAction action=new UndoRedoAction(
					getVisualResources().undoredoresources, executeRunnable, 
							undoRunnable, executeRunnable, new HashSet<Element>());
		
				UndoRedoActionList actionlist=new UndoRedoActionList(action.getName(), false);
				actionlist.add(action);
				svgHandle.getUndoRedo().addActionList(actionlist, true);
        	}
    	}
    }
    
    /**
     * modifies the properties of the given resource
     * @param resourceObject a resource object
     */
    protected void modifyProperties(SVGVisualResourceObject resourceObject){

    	if(svgHandle!=null && resourceObject!=null && resourceObject.canBeModified()){

        	final SVGVisualResourceObject fresourceObject=resourceObject;
        	final Element oldElement=fresourceObject.getInitialNode();
        	final Element newElement=fresourceObject.getResourceNode();
        	final Element parentElement=(Element)resourceObject.getParentNode();
        	final String name=resourceObject.getResourceModel().getName();
        	
			//displays the dialog 
		    int returnValue=SVGVisualResourceResourceModifier.showPropertyModifierDialog(fresourceObject);

		    if(returnValue==SVGVisualResourceResourceModifier.OK){
		        
			    //applying the modifications
				fresourceObject.applyChanges();

				//getting the ids
				final String oldId=oldElement.getAttribute("id"), newId=fresourceObject.getResourceId();
				
				//getting all the elements that used the resource
				Set<Element> elements=
					svgHandle.getSvgResourcesManager().getNodeUsingResource(oldId);
				
	    		Runnable executeRunnable=new Runnable(){
	    			
	    			public void run() {

						//removing the initial node and adding the modified node
						getVisualResources().getVisualResourcesToolkit().
							removeVisualResource(svgHandle, oldElement);
						getVisualResources().getVisualResourcesToolkit().
							appendVisualResource(svgHandle, parentElement, newElement);
						
						if(! newId.equals(oldId)){
						    
							//modifying the id of the resource in the map of the resources in the canvas
							svgHandle.getSvgResourcesManager().modifyResourceId(newId, oldId);
						}
						
						//refreshing the nodes that use the resource
						svgHandle.getSvgResourcesManager().refreshNodesUsingResource(newId);
						
						//refreshing the resources lists
						resourceState.setSelectedItemId(name, newId);
						refreshResources(RESOURCE_MODIFIED, name, newElement, oldElement);
	    			}
	    		};
	    		
	    		Runnable undoRunnable=new Runnable(){
	    			
	    			public void run() {
	    			
						//removing the modified node and adding the initial node
						getVisualResources().getVisualResourcesToolkit().
							removeVisualResource(svgHandle, newElement);
						getVisualResources().getVisualResourcesToolkit().
							appendVisualResource(svgHandle, parentElement, oldElement);

						//modifying the id of the resource in the map of the resources in the canvas
						if(! newId.equals(oldId)){
						    
						    svgHandle.getSvgResourcesManager().modifyResourceId(oldId, newId);
						}
						
						//refreshing the nodes that use the resource
						svgHandle.getSvgResourcesManager().refreshNodesUsingResource(oldId);
						
						//refreshing the resources lists
						resourceState.setSelectedItemId(name, oldId);
						refreshResources(RESOURCE_MODIFIED, name, oldElement, newElement);
	    			}
	    		};
	    		
				//create the undo/redo action and insert it into the undo/redo stack
				UndoRedoAction action=new UndoRedoAction(
					getVisualResources().undoredoresources, executeRunnable, 
							undoRunnable, executeRunnable, elements);
		
				UndoRedoActionList actionlist=new UndoRedoActionList(action.getName(), false);
				actionlist.add(action);
				svgHandle.getUndoRedo().addActionList(actionlist, true);
				
		    }else{
		    	
		        //removes this resource object
		        fresourceObject.getResourceModel().getResourceObjectsList().remove(fresourceObject);
		        
		        refreshResources(RESOURCE_MODIFIED, name, oldElement, fresourceObject.getResourceNode());
		    }
    	}
    }
    
    /**
     * the list cell renderer
     * 
     * @author ITRIS, Jordi SUC 
     */
    protected class SVGVisualResourcesListCellRenderer implements ListCellRenderer{

        /**
         * @param list
         * @param item
         * @param index
         * @param isSelected
         * @param cellHasFocus
         * @return the component representing an item in a list
         */
        public Component getListCellRendererComponent(JList list, Object item, 
        		int index, boolean isSelected, boolean cellHasFocus) {

            JPanel panel=new JPanel();

            if(item!=null){
                
                panel.setLayout(new BorderLayout(5, 0));

                JComponent resourceComponent=((SVGVisualResourceListItem)item).
                	getResourceRepresentation(list);//TODO
                SVGVisualResourceObject resObj=((SVGVisualResourceListItem)item).getVisualResourceObject();
                panel.add(resourceComponent, BorderLayout.WEST);
                
                JLabel label=new JLabel(((SVGVisualResourceListItem)item).getVisualResourceObject().getResourceId());
                panel.add(label, BorderLayout.CENTER);
                
                if(isSelected){
                    
                    if(((SVGVisualResourceListItem)item).getVisualResourceObject().canBeModified()){
                        
                        panel.setBackground(list.getSelectionBackground());
                        label.setBackground(list.getSelectionBackground());
                        label.setForeground(list.getSelectionForeground());
                        
                    }else{
                        
                        panel.setBackground(MetalLookAndFeel.getPrimaryControlDarkShadow());
                        label.setBackground(MetalLookAndFeel.getPrimaryControlDarkShadow());
                        label.setForeground(list.getSelectionForeground());
                    }

                }else{
                    
                    if(((SVGVisualResourceListItem)item).getVisualResourceObject().canBeModified()){
                        
                        panel.setBackground(list.getBackground());
                        label.setBackground(list.getBackground());
                        label.setForeground(list.getForeground());
                        
                    }else{
                        
                        panel.setBackground(MetalLookAndFeel.getPrimaryControlShadow());
                        label.setBackground(MetalLookAndFeel.getPrimaryControlShadow());
                        label.setForeground(list.getForeground());
                    }
                }
                
                //adds the cell panel to the list of the cell panels
                if(panel!=null){
                	
                	cellPanels.put(resObj, panel);
                }
            }

            return panel;
        }
    }
}
