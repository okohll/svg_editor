/*
 * Created on 10 déc. 2004
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

package fr.itris.glips.svgeditor;

import java.awt.event.*;
import java.util.*;
import org.apache.batik.apps.svgbrowser.DOMViewer;
import org.apache.batik.apps.svgbrowser.DOMViewerController;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * @author ITRIS, Jordi SUC
 *
 * the module displaying a dom viewer
 */
public class DOMViewerModule extends ModuleAdapter{
    
	/**
	 * the id
	 */
	private String idDOMViewer="DOMViewer";

	/**
	 * the labels
	 */
	private String labelDOMViewer="";
	
	/**
	 * the menu item
	 */
	private final JMenuItem domViewerMenuitem=new JMenuItem();
	
	/**
	 * the resource bundle
	 */
	private ResourceBundle bundle;
	
	private DOMViewerController controller;
	
	/**
	 * the dom viewer
	 */
	private SVGDOMViewer domViewerDialog=new SVGDOMViewer(controller);
	
	/**
	 * the editor
	 */
	private Editor editor;
	
	/**
	 * the currently edited handle
	 */
	private SVGHandle currentHandle;
    
    /**
     * the constructor of the class
	 * @param editor the editor
     */
    public DOMViewerModule(Editor editor) {

		this.editor=editor;
		
		//setting the properties of the domviewer
		domViewerDialog.setShowWhitespace(false);

		//the resource bundle
        bundle=ResourcesManager.bundle;
		
		if(bundle!=null){
		    
		    try{
		    	labelDOMViewer=bundle.getString("labeldomviewer");
		    }catch (MissingResourceException ex) {
		    	ex.printStackTrace();
		    }
		}
		
		//a listener that listens to the changes of the svg handles
		final HandlesListener svgHandlesListener=new HandlesListener(){

			@Override
			public void handleChanged(SVGHandle theCurrentHandle, Set<SVGHandle> handles) {

				DOMViewerModule.this.currentHandle=theCurrentHandle;
				
				if(domViewerDialog.isVisible()){

					if(theCurrentHandle!=null){
						
						//sets the document for the dom viewer
						domViewerDialog.setDocument(
								theCurrentHandle.getCanvas().getDocument());
						
					}else{
						
						domViewerDialog.setVisible(false);
					}
				}
			}
		};
		
		//adds the SVGFrame change listener
		editor.getHandlesManager().addHandlesListener(svgHandlesListener);
		
		//the menuitem
		domViewerMenuitem.setText(labelDOMViewer);
		
		domViewerMenuitem.addActionListener(
				
			new ActionListener(){
				
				public void actionPerformed(ActionEvent arg0) {
					
					if(! domViewerDialog.isVisible()){
						
						if(getSVGEditor().getHandlesManager().getCurrentHandle()!=null){
							
							//sets the document for the dom viewer
							domViewerDialog.setDocument(getSVGEditor().getHandlesManager().
									getCurrentHandle().getScrollPane().getSVGCanvas().getDocument());
						}
							
						//sets the location of the dialog box
						int x=(int)(Editor.getParent().getLocationOnScreen().getX()+
									Editor.getParent().getWidth()/2-domViewerDialog.getSize().getWidth()/2);
						int y=(int)(Editor.getParent().getLocationOnScreen().getY()+
								Editor.getParent().getHeight()/2-domViewerDialog.getSize().getHeight()/2);

						domViewerDialog.setLocation(x,y);
						domViewerDialog.setVisible(true);
					}
				}
			}
		);
    }
    
	/**
	 * @return the editor
	 */
	public Editor getSVGEditor(){
		return editor;
	}
    
	@Override
	public HashMap<String, JMenuItem> getMenuItems(){
		
		HashMap<String, JMenuItem> menuItems=new HashMap<String, JMenuItem>();
		menuItems.put(idDOMViewer, domViewerMenuitem);
		
		return menuItems;
	}
	
	/**
	 * the class of the dom viewer
	 * @author Jordi SUC
	 */
	protected class SVGDOMViewer extends DOMViewer {
		
		/**
		 * the constructor of the class
		 */
		public SVGDOMViewer(DOMViewerController controller) {
			super(controller);

			TreeNode root = new DefaultMutableTreeNode(resources.getString("EmptyDocument.text"));
			final JTree tree = new JTree(root);
			
			tree.addTreeSelectionListener(new TreeSelectionListener() {
			//panel.getTree().addTreeSelectionListener(new TreeSelectionListener(){

				public void valueChanged(TreeSelectionEvent ev) {

					if(currentHandle!=null){
						
						DefaultMutableTreeNode mtn=
							(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

						if(mtn==null) {

						    return;
						}
						
						Object nodeInfo=mtn.getUserObject();

						if (nodeInfo instanceof NodeInfo) {
							
						    Node node=((NodeInfo)nodeInfo).getNode();

						    if(node!=null && node instanceof Element &&
						    	EditorToolkit.isElementAShape((Element)node)){
						    	
						    	Element parentElement=currentHandle.getSelection().getParentElement();

						    	if(parentElement==null || parentElement.equals(node.getParentNode())){
						    		
							    	currentHandle.getSelection().handleSelection(
								    		(Element)node, false, true);
						    	}
						    }
						}
					}
				}
			});
		}
	}
}

