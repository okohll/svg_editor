/*
 * Created on 7 juin 2005
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

import org.w3c.dom.*;
import javax.swing.tree.*;
import javax.swing.*;

/**
 * the model for the widget data base 
 * 
 * @author ITRIS, Jordi SUC
 */
public class WidgetDataBaseModel extends DefaultTreeModel implements TreeModel{

    /**
     * the database editor
     */
    private DataBaseEditorModule dbEditor=null;
    
    /**
     * the editor panel
     */
    private WidgetDataBaseEditorPanel editorPanel;
    
	/**
	 * the tree corresponding to the model
	 */
	private JTree tree=null;
    
    /**
     * the root node of the JTree
     */
    private WidgetDataBaseEditorTreeNode rootNode=null;
    
    /**
     * the constructor of the class
     * @param dbEditor the database editor
     * @param editorPanel the editor panel
     * @param tree the tree corresponding to the model
     * @param rootModel the root element of the subtree of the widget data base
     */
    public WidgetDataBaseModel(DataBaseEditorModule dbEditor, 
    		WidgetDataBaseEditorPanel editorPanel, JTree tree, Element rootModel){
        
    	super(null);
    	
    	this.dbEditor=dbEditor;
    	this.editorPanel=editorPanel;
    	this.tree=tree;
        
        //creating the root node that will be displayed in the JTree
        if(rootModel!=null) {
        	
            rootNode=new WidgetDataBaseEditorTreeNode(dbEditor,  this, rootModel);
            setRoot(rootNode);
        }
    }
    
    /**
     * disposes the model
     */
    public void dispose(){
        
        rootNode.dispose();
    }

    @Override
    public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) {

        super.insertNodeInto(newChild, parent, index);

        if(parent!=null && parent instanceof WidgetDataBaseEditorTreeNode && 
                newChild!=null && newChild instanceof WidgetDataBaseEditorTreeNode){
            
            ((WidgetDataBaseEditorTreeNode)parent).getChildElements().add(
            		((WidgetDataBaseEditorTreeNode)newChild).getElement());
        }
    }
    
    @Override
    public void removeNodeFromParent(MutableTreeNode childNode) {

        if(childNode!=null && childNode instanceof WidgetDataBaseEditorTreeNode){
            
            ((WidgetDataBaseEditorTreeNode)childNode.getParent()).getChildElements().
            					remove(((WidgetDataBaseEditorTreeNode)childNode).getElement());
        }
        
        super.removeNodeFromParent(childNode);
    }
    
    @Override
    public void valueForPathChanged(TreePath path, Object newValue){

        if(path!=null && newValue!=null && newValue instanceof String){
        	
        	WidgetDataBaseEditorTreeNode node=
        		(WidgetDataBaseEditorTreeNode)path.getLastPathComponent();
        	
        	//if the node has changed, the modifications are flushed
        	if(node!=null){

        		if(node.isDuplicatedId(newValue.toString())){
        			
        			editorPanel.showDuplicatedIdDialog();
        			
        		}else if(newValue.toString()==null || (newValue.toString()!=null && 
        				newValue.toString().equals(""))){
        			
        			editorPanel.showMalformedIdDialog();
        			
        		}else{
        			
            		node.setId(newValue.toString());
        		}
        	}
        }
        
        super.valueForPathChanged(path, newValue);
    }

    /**
     * @return Returns the tree.
     */
    public JTree getTree() {
        return tree;
    }

    /**
     * @return Returns the dbEditor.
     */
    public DataBaseEditorModule getDbEditor() {
        return dbEditor;
    }
}
