/*
 * Created on 9 juin 2005
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
package fr.itris.glips.rtdaeditor.dbeditor;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.tree.*;
import org.w3c.dom.*;
import java.awt.*;
import fr.itris.glips.rtda.database.*;
import fr.itris.glips.rtda.toolkit.*;
import java.util.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the widget containing the tree displaying the widget database nodes
 * and the buttons for creating this database
 * 
 * @author ITRIS, Jordi SUC
 */
public class WidgetDataBaseEditorPanel extends DataBaseEditorPanel{
    
    /**
     * the widget database editor 
     */
    private DataBaseEditorModule dbEditor=null;
    
    /**
     * the svg document of the widget whose database is to be edited
     */
    private Document doc=null;
    
    /**
     * the tree that will display the nodes of the widget database
     */
    private JTree widgetTree=null;
    
    /**
     * the tree model
     */
    private WidgetDataBaseModel model=null;
    
    /**
     * the cell editor
     */
    private WidgetDataBaseCellEditor cellEditor=null;
    
    /**
     * the buttons panel
     */
    private JPanel buttonsPanel=new JPanel();
    
    /**
     * the runnable that will be used to dispose the widgets in this panel
     */
    private Runnable disposeRunnable=null;
    
    /**
     * the labels
     */
    protected String topDescriptionLabel="", insertLabel="", removeLabel="", errorLabel="",
    							cannotRemoveTagNodeLabel="", cannotRemoveModuleNodeLabel="", 
    							cannotRemoveValueNodeLabel="", malformedIdLabel="", duplicatedIdLabel="";
    
    /**
     * the popup menu
     */
    protected JPopupMenu popupMenu;
    
    /**
     * the menu items for the popup menu
     */
    protected JMenuItem moduleItem, floatTagItem, integerTagItem, 
    	enumeratedTagItem, stringTagItem, enumeratedValueItem, deleteItem;
    
    /**
     * the currently selected node
     */
    private WidgetDataBaseEditorTreeNode currentNode;
    
    /**
     * the constructor of the class
     * @param dbEditor the widget database editor 
     * @param doc the svg document of the widget whose database is to be edited
     */
    public WidgetDataBaseEditorPanel(DataBaseEditorModule dbEditor, Document doc){
        
        this.dbEditor=dbEditor;
        this.doc=doc;
        
        buildPanel();
    }
    
    /**
     * building the panel
     */
    protected void buildPanel(){
        
        //getting the labels
        ResourceBundle bundle=ResourcesManager.bundle;
        
        if(bundle!=null){
            
            try{
            	topDescriptionLabel=bundle.getString("rtdadb_widgetdatabasecreation");
                insertLabel=bundle.getString("rtdadb_insert");
                removeLabel=bundle.getString("rtdadb_remove");
                errorLabel=bundle.getString("labelerror");
                cannotRemoveTagNodeLabel=bundle.getString("rtdadb_cannotdeletetag");
                cannotRemoveModuleNodeLabel=bundle.getString("rtdadb_cannotdeletemodule");
                cannotRemoveValueNodeLabel=bundle.getString("rtdadb_cannotdeletevalue");
            	duplicatedIdLabel=bundle.getString("rtdadb_duplicatedId");
            	malformedIdLabel=bundle.getString("rtdadb_malformedId");
            }catch (Exception ex){}
        }
        
        //creating the tree that will be displayed in the widget
        widgetTree=new JTree();
        widgetTree.setScrollsOnExpand(true);
        widgetTree.setShowsRootHandles(true);
        widgetTree.setEditable(true);
        
        //setting the temporary model
        widgetTree.setModel(new TreeModel(){
        	
        	public void addTreeModelListener(TreeModelListener evt) {}

			public Object getChild(Object arg0, int arg1) {return null;}

			public int getChildCount(Object arg0) {return 0;}

			public int getIndexOfChild(Object arg0, Object arg1) {return 0;}

			public Object getRoot() {return null;}

			public boolean isLeaf(Object arg0) {return false;}

			public void removeTreeModelListener(TreeModelListener arg0) {}

			public void valueForPathChanged(TreePath arg0, Object arg1) {}
        });
        
        ToolTipManager.sharedInstance().registerComponent(widgetTree);

        //creating the buttons enabling to handle actions on the tree nodes
        final JButton insertButton=new JButton(), removeButton=new JButton();
        
        //getting and setting the icons
        ImageIcon insertIcon=ResourcesManager.getIcon("New", false);
        ImageIcon dinsertIcon=ResourcesManager.getIcon("New", true);
        ImageIcon removeIcon=ResourcesManager.getIcon("Delete", false);
        ImageIcon dremoveIcon=ResourcesManager.getIcon("Delete", true);

        insertButton.setIcon(insertIcon);
        insertButton.setDisabledIcon(dinsertIcon);
        removeButton.setIcon(removeIcon);
        removeButton.setDisabledIcon(dremoveIcon);
        
        //setting the tooltips
        insertButton.setToolTipText(insertLabel);
        removeButton.setToolTipText(removeLabel);
        
        //setting the margins
        Insets buttonInsets=new Insets(1, 1, 1, 1);
        insertButton.setMargin(buttonInsets);
        removeButton.setMargin(buttonInsets);
        
    	//creating the popup menu
    	popupMenu=new JPopupMenu();
        
        //getting the popup items icons
        ImageIcon moduleItemIcon=ResourcesManager.getIcon(
        		TagToolkit.getIconName(
        				TagToolkit.NOT_A_TAG), false);
        
        ImageIcon floatItemIcon=ResourcesManager.getIcon(
        		TagToolkit.getIconName(
        				TagToolkit.ANALOGIC_FLOAT), false);

        ImageIcon integerItemIcon=ResourcesManager.getIcon(
        		TagToolkit.getIconName(
        				TagToolkit.ANALOGIC_INTEGER), false);
        
        ImageIcon enumeratedItemIcon=ResourcesManager.getIcon(
        		TagToolkit.getIconName(
        				TagToolkit.ENUMERATED), false);
        
        ImageIcon stringItemIcon=ResourcesManager.getIcon(
        		TagToolkit.getIconName(
        				TagToolkit.STRING), false);
        
        ImageIcon enumeratedValueItemIcon=ResourcesManager.getIcon(
        		TagToolkit.getIconName(
        				TagToolkit.ENUMERATED_CHILD), false);
    	
        //creating the popup menu items
        moduleItem=new JMenuItem(TagToolkit.moduleLabel, moduleItemIcon);
        floatTagItem=new JMenuItem(TagToolkit.floatTagLabel, floatItemIcon);
		integerTagItem=new JMenuItem(TagToolkit.integerTagLabel, integerItemIcon);
		enumeratedTagItem=new JMenuItem(TagToolkit.enumeratedTagLabel, enumeratedItemIcon);
		stringTagItem=new JMenuItem(TagToolkit.stringTagLabel, stringItemIcon);
		enumeratedValueItem=new JMenuItem(TagToolkit.enumeratedValueLabel, enumeratedValueItemIcon);
		deleteItem=new JMenuItem(removeLabel, removeIcon);

        //disabling the buttons
        insertButton.setEnabled(false);
        removeButton.setEnabled(false);
        
        //adding the tree selection listener
        final TreeSelectionListener selectionListener=new TreeSelectionListener(){
            
            public void valueChanged(TreeSelectionEvent evt){

            	currentNode=null;
                insertButton.setEnabled(false);
                removeButton.setEnabled(true);

                TreePath selectedPath=evt.getPath();
                
                if(selectedPath!=null){
                    
                    WidgetDataBaseEditorTreeNode node=
                    	(WidgetDataBaseEditorTreeNode)selectedPath.getLastPathComponent();
                    
                    if(node!=null){
                    	
                    	currentNode=node;
                    	handlePopup(node, false);

                        //handles the state of the menuitems and the buttons
                        if(node.getType()==TagToolkit.ENUMERATED){
                            
                            insertButton.setEnabled(true);
                            
                        }else if(node.getType()==TagToolkit.NOT_A_TAG){
                            
                            insertButton.setEnabled(true);

                            if(node.isRoot()){
                                
                                removeButton.setEnabled(false);
                            }
                        }
                    }

                }else{
                    
                    removeButton.setEnabled(false);
                }
            }
        };
        
        widgetTree.addTreeSelectionListener(selectionListener);

        //creating the listener to the insert button
        final ActionListener insertButtonListener=new ActionListener(){
            
            public void actionPerformed(ActionEvent evt) {
                
                Point mousePosition=insertButton.getMousePosition();
                
                if(mousePosition!=null){
 
                    popupMenu.show(insertButton, mousePosition.x, mousePosition.y);
                }
            }
        };
        
        insertButton.addActionListener(insertButtonListener);
        
        //adding a mouse listener on the tree
        final MouseListener mouseListener=new MouseAdapter(){
        	
        	@Override
        	public void mousePressed(final MouseEvent evt) {

        		if(isPopUp(evt)){
        			
        			currentNode=null;
        			
        			//getting the node corresponding to the clicked point
        			TreePath path=widgetTree.getClosestPathForLocation(
        					evt.getX(), evt.getY());
        			
                    if(path!=null){
                        
                        WidgetDataBaseEditorTreeNode node=
                        	(WidgetDataBaseEditorTreeNode)path.getLastPathComponent();
                        
                        if(node!=null){
                        	
                        	currentNode=node;

                			widgetTree.setSelectionRow(
                    				widgetTree.getRowForLocation(evt.getX(), evt.getY()));
                        	
                           	SwingUtilities.invokeLater(new Runnable(){
                        		
                        		public void run() {
                        			
                        			handlePopup(currentNode, true);
                        			
                                	if(popupMenu.isEnabled()){
                                		
                                		popupMenu.show(widgetTree, evt.getX(), evt.getY());
                                	}
                        		}
                        	});
                        }
                    }  
        		}
        	}
        	
        	/**
        	 * checks whether this mouse event denotes a popup trigger event or not
        	 * @param evt an event
        	 * @return whether this mouse event denotes a popup trigger event or not
        	 */
        	protected boolean isPopUp(MouseEvent evt) {
        		
        		return evt.isPopupTrigger() || SwingUtilities.isRightMouseButton(evt);
        	}
        };
        
        widgetTree.addMouseListener(mouseListener);

        //creating the listener to the remove button
        final ActionListener removeButtonListener=new ActionListener(){
            
            public void actionPerformed(ActionEvent evt) {
                
                if(currentNode!=null && currentNode.getParent()!=null && 
                		! currentNode.isRoot()){
                    
                    if(currentNode.canRemoveNode()){
                        
                        //removes the node from the tree
                        ((WidgetDataBaseEditorTreeNode)currentNode.getParent()).
                        	removeChildNode(currentNode);
                        
                    }else{
                        
                        String message="";
                        
                        if(currentNode.getType()==TagToolkit.NOT_A_TAG){
                            
                            message=cannotRemoveModuleNodeLabel;
                            
                        }else if(currentNode.getType()==TagToolkit.ENUMERATED_CHILD){
                            
                            message=cannotRemoveValueNodeLabel;
                            
                        }else{
                            
                            message=cannotRemoveTagNodeLabel;
                        }
                        
                        JOptionPane.showMessageDialog(	
                        	Editor.getParent(), message, errorLabel,
								JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        
        removeButton.addActionListener(removeButtonListener);
        
        deleteItem.addActionListener(removeButtonListener);
        
        //creating the listeners to the menu items
        final ActionListener menuItemListener=new ActionListener(){
            
            public void actionPerformed(ActionEvent evt) {
                
                if(currentNode!=null){
                    
                    //inserting a new node
                    if(evt.getSource().equals(moduleItem)){
                        
                    	currentNode.insertChildNode(TagToolkit.NOT_A_TAG);
                        
                    }else if(evt.getSource().equals(enumeratedTagItem)){
                        
                    	currentNode.insertChildNode(TagToolkit.ENUMERATED);
                        
                    }else if(evt.getSource().equals(enumeratedValueItem)){
                        
                    	currentNode.insertChildNode(TagToolkit.ENUMERATED_CHILD);
                        
                    }else if(evt.getSource().equals(floatTagItem)){
                        
                    	currentNode.insertChildNode(TagToolkit.ANALOGIC_FLOAT);
                        
                    }else if(evt.getSource().equals(integerTagItem)){
                        
                    	currentNode.insertChildNode(TagToolkit.ANALOGIC_INTEGER);
                        
                    }else if(evt.getSource().equals(stringTagItem)){
                        
                    	currentNode.insertChildNode(TagToolkit.STRING);
                    }
                }
            } 
        };
        
        moduleItem.addActionListener(menuItemListener);
        enumeratedTagItem.addActionListener(menuItemListener);
        enumeratedValueItem.addActionListener(menuItemListener);
        floatTagItem.addActionListener(menuItemListener);
        integerTagItem.addActionListener(menuItemListener);
        stringTagItem.addActionListener(menuItemListener);
        
        //filling the panel of the buttons
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(insertButton);
        buttonsPanel.add(removeButton);
        
        //filling this panel
        setLayout(new BorderLayout(0, 5));
        
        //the panel containing the tree
        JPanel treePanel=new JPanel();
        treePanel.setLayout(new BorderLayout());
        treePanel.add(widgetTree, BorderLayout.NORTH);
        treePanel.setBackground(Color.white);

        //the scrollpane 
        JScrollPane scrollpane=new JScrollPane(treePanel);
        scrollpane.setWheelScrollingEnabled(true);
        scrollpane.setDoubleBuffered(true);
        widgetTree.setDoubleBuffered(true);
        
        //the JLabel containing the describing of the panel
        JLabel label=new JLabel("<html><body>"+topDescriptionLabel+" :</body></html>");
        JPanel labelPanel=new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(label);
        
        //adding the widgets to this panel
        add(labelPanel, BorderLayout.NORTH);
        add(scrollpane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
        
        //getting the current handle
        SVGHandle handle=dbEditor.getSVGEditor().getHandlesManager().getCurrentHandle();
        
        if(handle!=null){
            
            //creating the model
            SwingUtilities.invokeLater(new Runnable(){

                public void run() {

                    //getting the root node of the widget database
                    Element widgetDataBaseRoot=
                    	DataBaseToolkit.getRtdaWidgetDataBase(doc);

                    //creating the model for the tree
                    model=new WidgetDataBaseModel(
                    	dbEditor, WidgetDataBaseEditorPanel.this, widgetTree, widgetDataBaseRoot);
                    widgetTree.setModel(model);
                    
                    //the cell renderer
                    WidgetDataBaseCellRenderer renderer=
                    	new WidgetDataBaseCellRenderer(widgetTree);
                    widgetTree.setCellRenderer(renderer);
                    
                    //the cell editor
                    cellEditor=new WidgetDataBaseCellEditor(widgetTree, renderer);
                    widgetTree.setCellEditor(cellEditor);
                    
                    //the selection model
                    DefaultTreeSelectionModel selectionModel=
                    	new DefaultTreeSelectionModel();
                    selectionModel.setSelectionMode(
                    		TreeSelectionModel.SINGLE_TREE_SELECTION);
                    widgetTree.setSelectionModel(selectionModel);
                    
                    widgetTree.setSelectionPath(
                    	((WidgetDataBaseEditorTreeNode)model.getRoot()).getTreePath());
                    widgetTree.setVisible(true);
                    dbEditor.getRtdaDataBaseEditorFrame().revalidate();
                }
            });
        }

        //the dispose runnable
        disposeRunnable=new Runnable(){
            
            public void run() {

            	ToolTipManager.sharedInstance().unregisterComponent(widgetTree);
            	widgetTree.removeTreeSelectionListener(selectionListener);
            	widgetTree.removeMouseListener(mouseListener);
                
                if(model!=null){
                    
                    model.dispose();
                }
                
                if(cellEditor!=null) {
                    
                    cellEditor.dispose();
                }

                insertButton.removeActionListener(insertButtonListener);
                removeButton.removeActionListener(removeButtonListener);
                deleteItem.removeActionListener(removeButtonListener);
                
                moduleItem.removeActionListener(menuItemListener);
                enumeratedTagItem.removeActionListener(menuItemListener);
                enumeratedValueItem.removeActionListener(menuItemListener);
                floatTagItem.removeActionListener(menuItemListener);
                integerTagItem.removeActionListener(menuItemListener);
                stringTagItem.removeActionListener(menuItemListener);
                
                buttonsPanel.removeAll();
                removeAll();
            } 
        };
    }
    
	/**
	 * handles the content of the popup given a node
	 * @param node a node in the tree
	 * @param isPopupEvent whether the event that triggered the call of this method
	 * is a popup event
	 */
	protected void handlePopup(WidgetDataBaseEditorTreeNode node, 
			boolean isPopupEvent){//TODO
		
    	popupMenu.setEnabled(false);
    	
        //removing all the items
        popupMenu.removeAll();
        
        //handles the state of the menuitems and the buttons
        if(node.getType()==TagToolkit.ENUMERATED){

            popupMenu.setEnabled(true);
            popupMenu.add(enumeratedValueItem);
            
        }else if(node.getType()==TagToolkit.NOT_A_TAG){

            popupMenu.setEnabled(true);
            popupMenu.add(moduleItem);
            popupMenu.addSeparator();
            popupMenu.add(floatTagItem);
            popupMenu.add(integerTagItem);
            popupMenu.add(enumeratedTagItem);
            popupMenu.add(stringTagItem);
        }
        
        if(isPopupEvent && ! node.isRoot()){
            
        	popupMenu.setEnabled(true);
        	
        	if(popupMenu.getComponentCount()>0){
        		
        		popupMenu.addSeparator();
        	}
        	
        	popupMenu.add(deleteItem);
        }
	}
    
    /**
     * shows a dialog notifying that the id is duplicated
     */
    protected void showDuplicatedIdDialog(){
    	
        JOptionPane.showMessageDialog(
        	Editor.getParent(), duplicatedIdLabel,
				errorLabel, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * shows a dialog notifying that the id is malformed
     */
    protected void showMalformedIdDialog(){
    	
        JOptionPane.showMessageDialog(	Editor.getParent(), 
															malformedIdLabel,
															errorLabel,
															JOptionPane.ERROR_MESSAGE);
    }
	
    @Override
    public void dispose(){
        
        if(disposeRunnable!=null){
            
            disposeRunnable.run();
        }
    }
    
}
