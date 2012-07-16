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

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.toolbar.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class allowing to display a database in a tree
 * 
 * @author ITRIS, Jordi SUC
 */
public class DataBaseEditorModule extends ModuleAdapter{
    
    /**
     * the constant for unknown state
     */
    public static final int UNKNOWN_STATE=-1;
    
    /**
     * the constant for the widget database
     */
    public static final int WIDGET_DATABASE=0;
    
    /**
     * the constant for the view database
     */
    public static final int VIEW_DATABASE=1;
    
    /**
     * the constant for the conversion between the widget database and the view database
     */
    public static final int WIDGET_INSERTION_DATABASE=2;
    
    /**
     * the ids of the module
     */
    final private String idrtdaDataBase="RtdaDataBaseEditor";
    
    /**
     * the labels
     */
    protected String 	labelRtdaDatabaseEditor="",
									labelRtdaDatabaseEditorMenuitemHidden="", labelRtdaDatabaseEditorMenuitemShown="", 
									noFrameMessage="", noWidgetNodeSelected="";
    
    /**
     * the undo/redo labels
     */
    protected String labelRtdaDatabaseEditorUndoRedo="";
    
    /**
     * the editor
     */
    private Editor editor=null;
    
    /**
     * a small font
     */
    public final Font smallFont=new Font("smallFont", Font.ROMAN_BASELINE, 9);
    
    /**
     * the font
     */
    public final Font theFont=new Font("theFont", Font.ROMAN_BASELINE, 10);
    
    /**
     * the bundle used to get labels
     */
    private ResourceBundle bundle=null;
    
    /**
     * the nodes that are currently selected
     */
    private LinkedList<Element> selectedNodes=new LinkedList<Element>();
    
    /**
     * the bounds of the tool frame
     */
    private Rectangle frameBounds=null;
    
    /**
     * the frame into which the database editor panel will be inserted
     */
    private ToolsFrame rtdaDataBaseEditorFrame;
    
    /**
     * the panel into which the panel of the editor will be inserted
     */
    private JPanel rtdaDataBaseEditorPanel=new JPanel();
    
    /**
     * the panel containing the widgets for the editor
     */
    private DataBaseEditorPanel editorPanel=null;

    /**
     * the current type of the editor among the values : UNKNOWN_STATE, 
     * WIDGET_DATABASE, VIEW_DATABASE, WIDGET_INSERTION_DATABASE
     */
    private int currentType=UNKNOWN_STATE;
    
	/**
	 * whether the graphic components of the dialog have already been reinitialized
	 */
	private boolean hasBeenReinitialized=false;
    
    /**
     * the constructor of the class
     * @param editor the editor
     */
    public DataBaseEditorModule(Editor editor){
        
        this.editor=editor;
        
        //gets the labels from the resources
        bundle=ResourcesManager.bundle;
        
        if(bundle!=null){
            
            try{
                labelRtdaDatabaseEditor=bundle.getString("label_rtdadatabaseeditor");
                labelRtdaDatabaseEditorUndoRedo=bundle.getString("labelrtdadatabaseeditorundoredo");
                noFrameMessage=bundle.getString("rtdaanim_empty_dialog_noframe");
                noWidgetNodeSelected=bundle.getString("rtdaanim_empty_dialog_nowidgetnode");
            }catch (Exception ex){ex.printStackTrace();}
        }

        //a listener that listens to the changes of the svg handles
        HandlesListener svgHandleListener=new HandlesListener(){

            /**
             * a listener to the selection changes
             */
            private SelectionChangedListener selectionListener;
            
			/**
			 * the last handle
			 */
			private SVGHandle lastHandle;
            
            /**
             * the current selection manager
             */
            private Selection selection;
            
            @Override
        	public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
        		
				if(lastHandle!=null && selectionListener!=null && lastHandle.getSelection()!=null){
					
					//if a selection listener is already registered on a selection module, it is removed	
					lastHandle.getSelection().removeSelectionChangedListener(selectionListener);
				}

                //clears the list of the selected items
                selectedNodes.clear();
                
                final SVGHandle handle=getSVGEditor().getHandlesManager().getCurrentHandle();
                
                //gets the current selection module
                if(handle!=null){
                    
                    selection=handle.getSelection();
                    
                    if(selection!=null){

                        //the listener of the selection changes
                        selectionListener=new SelectionChangedListener(){
                            
                            @Override
                            public void selectionChanged(Set<Element> selectedElements) {
                            	
                            	manageSelection(selectedElements);
                            }
                        };
                    }
                }

                int oldType=currentType;
                
                if(handle!=null){
                    
                    //computing the state
                    if(Toolkit.isDocumentAView(handle.getScrollPane().getSVGCanvas().getDocument())){
                        
                        currentType=VIEW_DATABASE;
                        manageSelection(new HashSet<Element>(handle.getSelection().getSelectedElements()));
                        
                    }else{
                        
                        currentType=WIDGET_DATABASE;
                        
                        if(oldType!=currentType) {
                            
                            rtdaDataBaseEditor();
                        }
                    }
                    
                }else{
                    
                	selectionListener=null;
                	selection=null;
                    currentType=UNKNOWN_STATE;
                    rtdaDataBaseEditor();
                }
                
                lastHandle=currentHandle;
                
                SwingUtilities.invokeLater(new Runnable() {
                    
                    public void run() {

                        //adds the selection listener
                        if(selectionListener!=null){
                            
                            selection.addSelectionChangedListener(selectionListener);
                        }
                    }
                });
            }	
            
            /**
             * updates the selected items and the state of the menu items
             * @param selectedElements the set of the selected elements
             */
            protected void manageSelection(Set<Element> selectedElements){

                if(currentType==VIEW_DATABASE || currentType==WIDGET_INSERTION_DATABASE){
                    
                    currentType=VIEW_DATABASE;
                    selectedNodes.clear();
                    
                    //refresh the selected nodes list
                    if(selectedElements!=null){
                        
                        selectedNodes.addAll(selectedElements);
                    }
                    
                    if(selectedNodes.size()==1 && rtdaDataBaseEditorPanel.isVisible()){
                        
                        Node node=null;
                        try{node=selectedNodes.getFirst();}catch(Exception ex){}
                        
                        if( node!=null && node.getNodeName().equals("image") && 
                                node instanceof Element && ((Element)node).hasAttribute(Toolkit.widgetAttribute)){
                            
                            currentType=WIDGET_INSERTION_DATABASE;

                            rtdaDataBaseEditor();
                            return;
                        }
                    }
                    
                    rtdaDataBaseEditor();
                }
            }
        };
        
        //adds the svg handle change listener
        editor.getHandlesManager().addHandlesListener(svgHandleListener);
        
        //setting the layout for the visual resources panel
        rtdaDataBaseEditorPanel.setLayout(new BoxLayout(rtdaDataBaseEditorPanel, BoxLayout.X_AXIS));
        
        //getting the preferred bounds for the container panel
        frameBounds=editor.getPreferredWidgetBounds("rtdadatabaseeditor");
        
        //creating the internal frame containing the tree panel
        rtdaDataBaseEditorFrame=new ToolsFrame(editor, idrtdaDataBase, 
        		labelRtdaDatabaseEditor, rtdaDataBaseEditorPanel);
        
		//setting the visibility changes handler
		Runnable visibilityRunnable=new Runnable(){
			
			public void run() {
				
                if(currentType==VIEW_DATABASE || currentType==WIDGET_INSERTION_DATABASE){
                    
                    currentType=VIEW_DATABASE;
                    
                    if(selectedNodes.size()==1){
                        
                        Node node=null;
                        try{node=selectedNodes.getFirst();}catch(Exception ex){}
                        
                        if(	node!=null && node.getNodeName().equals("image") && 
                                node instanceof Element && ((Element)node).hasAttribute(Toolkit.widgetAttribute)){
                            
                            currentType=WIDGET_INSERTION_DATABASE;

                            rtdaDataBaseEditor();
                            return;
                        }
                    }
                }

                rtdaDataBaseEditor();
			}
		};
		
		this.rtdaDataBaseEditorFrame.setVisibilityChangedRunnable(visibilityRunnable);
    }
    
    /**
     * @return the editor
     */
    public Editor getSVGEditor(){
        return this.editor;
    }

    @Override
    public HashMap<String, JMenuItem> getMenuItems() {

        HashMap<String, JMenuItem> menuItems=new HashMap<String, JMenuItem>();
        menuItems.put("ToolFrame_"+this.idrtdaDataBase, rtdaDataBaseEditorFrame.getMenuItem());
        
        return menuItems;
    }
    
	@Override
	public HashMap<String, AbstractButton> getToolItems() {

        HashMap<String, AbstractButton> map=new HashMap<String, AbstractButton>();
        map.put(idrtdaDataBase, rtdaDataBaseEditorFrame.getToolBarButton());
        return map;
	}
    
    /**
     * @return Returns the bundle.
     */
    public ResourceBundle getBundle() {
        return bundle;
    }

	/**
	 * @return Returns the rtdaDataBaseEditorFrame.
	 */
	public ToolsFrame getRtdaDataBaseEditorFrame() {
		return rtdaDataBaseEditorFrame;
	}
	
    /**
     * the method that shows the database editor
     */
    public void rtdaDataBaseEditor(){

        //getting the current handle
        SVGHandle handle=editor.getHandlesManager().getCurrentHandle();
        
        if(hasBeenReinitialized && (! rtdaDataBaseEditorFrame.isVisible() || handle==null)) {
        	
        	return;
        }
        
        if(! rtdaDataBaseEditorFrame.isVisible()) {
        	
        	handle=null;
        }
        
        hasBeenReinitialized=(handle==null);
        
        //removes all the widgets from the main panel
        rtdaDataBaseEditorPanel.removeAll();
        
        if(editorPanel!=null){
            
            editorPanel.dispose();
        }
        
        if(handle!=null && currentType!=UNKNOWN_STATE && currentType!=VIEW_DATABASE){
            
            if(currentType==WIDGET_DATABASE){
                
                //creating and adding the panel editor containing the tree widget displaying the widget database
                editorPanel=new WidgetDataBaseEditorPanel(this, handle.getScrollPane().getSVGCanvas().getDocument());
                
            }else if(currentType==WIDGET_INSERTION_DATABASE){
                
                //creating and adding the panel editor containing the tree widget and the swing widgets
                //enabling the user to make the correspondance between the widget database and the view database
                editorPanel=new InsertedWidgetDataBaseEditorPanel(this, selectedNodes.getFirst());
                
            }else if(currentType==VIEW_DATABASE){
                
            	editorPanel=new DataBaseEditorPanel(){

            		@Override
					public void dispose() {}
            	};
            }

            //the editor panel is added to the main panel
            if(editorPanel!=null){
                
                rtdaDataBaseEditorPanel.setPreferredSize(new Dimension(frameBounds.width, frameBounds.height));
                rtdaDataBaseEditorPanel.add(editorPanel);
            }
            
            rtdaDataBaseEditorFrame.revalidate();
            
        }else{
            
            String message="";
            
            //if we are in an unknow state or in a widget or in a view and no widget image is edited
            if(currentType==VIEW_DATABASE){
                
            	message=noWidgetNodeSelected;
                
            }else{
            	
            	message=noFrameMessage;
            }
            
            message="<html><body align=\"center\">"+message+"</body></html>";

            JLabel label=new JLabel(message);
            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setBorder(new EmptyBorder(5, 5, 5, 5));

            rtdaDataBaseEditorPanel.removeAll();
            rtdaDataBaseEditorPanel.add(label);
            rtdaDataBaseEditorPanel.setPreferredSize(null);
            rtdaDataBaseEditorFrame.revalidate();
            editorPanel=null;
        }
    }
    
}
