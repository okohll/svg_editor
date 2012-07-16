/*
 * Created on 27 juin 2005
 */
package fr.itris.glips.rtdaeditor.dbeditor;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.tree.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.svgeditor.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.*;

/**
 * the widget containing the tree displaying the widget database and the widgets to make the correspondance
 * between the widget data base and the view database
 * 
 * @author ITRIS, Jordi SUC
 */
public class InsertedWidgetDataBaseEditorPanel extends DataBaseEditorPanel{

    /**
     * the widget database editor 
     */
    private DataBaseEditorModule dbEditor=null;
    
    /**
     * the image node corresponding to the inserted widget
     */
    private Element widgetImage=null;
    
    /**
     * the tree that will display the nodes of the widget database
     */
    private JTree widgetTree=null;
    
    /**
     * the tree model
     */
    private InsertedWidgetDataBaseModel model=null;
    
    /**
     * the panel of the widgets that will be used to set the view tag values
     */
    private JPanel widgetsPanel=new JPanel();
    
    /**
     * the root of the widget data base
     */
    private Element widgetDataBaseRoot=null;
    
    /**
     * the runnable that will be used to dispose the widgets in this panel
     */
    private Runnable disposeRunnable=null;
    
    /**
     * the labels
     */
    protected String topDescriptionLabel="", chooserLabel="", associatedTagLabel="", associatedValueLabel="";
    
    /**
     * an instance of this class
     */
    protected InsertedWidgetDataBaseEditorPanel insertedWidgetPanel=this;
    
    /**
     * the constructor of the class
     * @param dbEditor the widget database editor 
     * @param widgetImage the image node corresponding to the inserted widget
     */
    public InsertedWidgetDataBaseEditorPanel(DataBaseEditorModule dbEditor, final Element widgetImage){
        
        this.dbEditor=dbEditor;
        this.widgetImage=widgetImage;
        
        //getting the root node of the database of the widget under the widget image node
        NodeList children=widgetImage.getElementsByTagName(
        		"rtda:"+EditorAnimationsToolkit.widgetDatabaseRootTagName);

        if(children.getLength()>0 && children.item(0)!=null){
            
            widgetDataBaseRoot=(Element)children.item(0);
        }
        
        if(widgetDataBaseRoot!=null){
            
            buildPanel();
        }
    }
    
    /**
     * building the panel
     */
    protected void buildPanel(){
        
        //getting the labels
        ResourceBundle bundle=dbEditor.getBundle();
        
        if(bundle!=null){
            
            try{
            	topDescriptionLabel=bundle.getString("rtdadb_widgetinsertiondatabase");
                chooserLabel=bundle.getString("rtdadb_tagchooser");
                associatedTagLabel=bundle.getString("rtdadb_associatedtag");
                associatedValueLabel=bundle.getString("rtdadb_associatedvalue");
            }catch (Exception ex){}
        }
        
        //creating the tree that will be displayed in the widget
        widgetTree=new JTree() {
            
            @Override
            protected TreeModelListener createTreeModelListener() {

                return new InsertedWidgetTreeModel();
            }
            
            final class InsertedWidgetTreeModel extends TreeModelHandler{
                
            	@Override
                public void treeStructureChanged(TreeModelEvent e) {

                }
            }
        };
        
        widgetTree.setScrollsOnExpand(false);
        widgetTree.setShowsRootHandles(true);
        widgetTree.setEditable(false);
        ToolTipManager.sharedInstance().registerComponent(widgetTree);

        //creating the model for the tree
        model=new InsertedWidgetDataBaseModel(dbEditor, widgetTree, widgetDataBaseRoot);
        widgetTree.setModel(model);
        widgetTree.setSelectionPath(((InsertedWidgetDataBaseEditorTreeNode)model.getRoot()).getTreePath());
        
        //the cell renderer
        TreeCellRenderer renderer=new InsertedWidgetDataBaseCellRenderer(widgetTree);
        widgetTree.setCellRenderer(renderer);

        //the selection model
        DefaultTreeSelectionModel selectionModel=new DefaultTreeSelectionModel();
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        widgetTree.setSelectionModel(selectionModel);
        
        //filling this panel
        setLayout(new BorderLayout());
        
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
        
        //creating the widgets enabling to link a widget tag to a view tag
        final JPanel tagChooserPanel=new JPanel();
        tagChooserPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        
        JLabel tagLbl=new JLabel(associatedTagLabel+" : ");

        final JTextField tagTextField=new JTextField(15);
        tagTextField.setEditable(false);

        final JButton chooserButton=new JButton(chooserLabel);

        tagChooserPanel.setLayout(new BorderLayout(2, 4));
        tagChooserPanel.add(tagLbl, BorderLayout.WEST);
        tagChooserPanel.add(tagTextField, BorderLayout.CENTER);
        tagChooserPanel.add(chooserButton, BorderLayout.EAST);
        
        //creating the listener to the button
        final ActionListener buttonListener=new ActionListener(){

	        public void actionPerformed(ActionEvent evt) {

	            //getting the path of the selected node
	            TreePath selectedPath=widgetTree.getSelectionPath();
	            
	            if(selectedPath!=null){
	                
	                //getting the currently selected node
	                InsertedWidgetDataBaseEditorTreeNode node=null;
	                node=(InsertedWidgetDataBaseEditorTreeNode)selectedPath.getLastPathComponent();
	                
	                if(node!=null){

	                    //if the node corresponds to a tag
	                    if(node.getType()==TagToolkit.ENUMERATED ||
	                    		node.getType()==TagToolkit.ANALOGIC_FLOAT ||
	                    			node.getType()==TagToolkit.ANALOGIC_INTEGER ||    
	                    				node.getType()==TagToolkit.STRING){

							//creating the filter for choosing a tag
							DataBaseNodeFilter filter=new DataBaseNodeFilter(node.getTagName(),
                                           node.getLevel(), node.getType(), true, false, null);
							
							//getting the information object about the selected tag
							DataBaseNodeChooser nodeChooser=DataBaseNodeChooser.getDataBaseNodeChooser(
									Editor.getParent(), widgetImage.getOwnerDocument(), filter, false, false);
							nodeChooser.showDialog(((JComponent)evt.getSource()));
							nodeChooser.disposeDialog();
							DataBaseNodeInformation info=nodeChooser.getInfo();

							if(info!=null){
								
		                        //retrieving and setting the new name for the tag
		                        String newTagName=info.getXmlPath();

		                        LinkedList<String> newValues=null;
		                        
		                        if(node.getType()==TagToolkit.ENUMERATED){
		                            
		                            newValues=info.getEnumeratedValues();
		                        }
		                        
		                        node.setCorrespondingValue(newTagName, newValues);
		                        
		                        String displayedValue=newTagName;
		                        displayedValue=EditorAnimationsToolkit.normalizePath(displayedValue);
		                        int pos=displayedValue.lastIndexOf("/");
		                        
		                        if(pos!=-1){
		                            
		                            displayedValue=displayedValue.substring(pos+1, displayedValue.length());
		                        }
                                
                                if(displayedValue==null || displayedValue.equals("")) {
                                    
                                    displayedValue="<"+TagToolkit.noneLabel+">";
                                }
		                        
		                        tagTextField.setText(displayedValue);
							}
	                    }
	                }
	            }
	        } 
        };
        
        chooserButton.addActionListener(buttonListener);
        
        //creating the panel used for selecting a value among several tag values
        final JPanel valueChooserPanel=new JPanel();
        valueChooserPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        
        JLabel valueLbl=new JLabel(associatedValueLabel+" : ");
        final JComboBox tagValuesCombo=new JComboBox();

        valueChooserPanel.setLayout(new BorderLayout(0, 4));
        valueChooserPanel.add(valueLbl, BorderLayout.WEST);
        valueChooserPanel.add(tagValuesCombo, BorderLayout.CENTER);
        
        //creating the listener to the combo box
        final ActionListener comboListener=new ActionListener(){

            public void actionPerformed(ActionEvent evt) {

                String value=(String)tagValuesCombo.getSelectedItem();
                
                if(value!=null){
                    
    	            //getting the path of the selected node
    	            TreePath selectedPath=widgetTree.getSelectionPath();
    	            
    	            if(selectedPath!=null){
    	                
    	                //getting the currently selected node
    	                InsertedWidgetDataBaseEditorTreeNode node=null;
    	                node=(InsertedWidgetDataBaseEditorTreeNode)selectedPath.getLastPathComponent();
    	                
    	                if(node!=null){
    	                    
    	                    node.setCorrespondingValue(value, null);
    	                }
    	            }
                }
            }
        };
        
        tagValuesCombo.addActionListener(comboListener);

        //setting the layout for the widgets panel
        widgetsPanel.setLayout(new BoxLayout(widgetsPanel, BoxLayout.X_AXIS));
        widgetsPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        
        //the JLabel containing the describing of the panel
        JLabel label=new JLabel("<html><body>"+topDescriptionLabel+" :</body></html>");
        JPanel labelPanel=new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(label);
        
        //adding the widgets to this panel
        add(labelPanel, BorderLayout.NORTH);
        add(scrollpane, BorderLayout.CENTER);
        add(widgetsPanel, BorderLayout.SOUTH);
        
        widgetTree.setVisible(true);
        
        //adding the tree selection listener
        final TreeSelectionListener selectionListener=new TreeSelectionListener(){
            
            public void valueChanged(TreeSelectionEvent evt){
  
                //clearing the widgets panel
                widgetsPanel.removeAll();
                
                //getting the newly selected node
                InsertedWidgetDataBaseEditorTreeNode node=null, parentNode=null;
                node=(InsertedWidgetDataBaseEditorTreeNode)evt.getPath().getLastPathComponent();
                
                if(node!=null){

                    parentNode=(InsertedWidgetDataBaseEditorTreeNode)node.getParent();

                    if(	node.getType()==TagToolkit.ENUMERATED ||
                        node.getType()==TagToolkit.ANALOGIC_FLOAT ||
                        node.getType()==TagToolkit.ANALOGIC_INTEGER ||    
                        node.getType()==TagToolkit.STRING){
                        
                        //the node corresponds to a tag, therefore, the widgets enabling to select a tag will be displayed
                        widgetsPanel.add(tagChooserPanel);
                        
                        //setting the text of the textfield with the current corresponding tag of the newly selected node
                        String displayedValue=node.getCorrespondingValue();
                            
                        if(displayedValue==null || displayedValue.equals("")) {
                            
                            displayedValue="<"+TagToolkit.noneLabel+">";
                        }
                        
                        tagTextField.setText(displayedValue);

                    }else if(node.getType()==TagToolkit.ENUMERATED_CHILD && parentNode!=null){
                        
                        //the node corresponds to the value of an enumerated tag//
                        
                        //removing the listener to the combo box
                        tagValuesCombo.removeActionListener(comboListener);
                        
                        //filling the the combo box enabling to select a tag value among all the available values
                        tagValuesCombo.removeAllItems();

                        LinkedList<String> values=DataBaseNodeToolkit.
                        		getEnumeratedTagValues(widgetImage.getOwnerDocument(), parentNode.getTagName());
                        
                        if(values!=null && values.size()>0){
                            
                            values.addFirst("<"+TagToolkit.noneLabel+">");

                            //filling the combo box with the combo items
                            String currentNodeValue=node.getCorrespondingValue();
                            int selectedIndex=0, i=0;
                            
                            for(String value : values){

                                if(value!=null){
                                    
                                    tagValuesCombo.addItem(value);
                                    
                                    if(value.equals(currentNodeValue)){
                                        
                                        selectedIndex=i;
                                    }
                                }
                                
                                i++;
                            }

                            //selecting an item
                            tagValuesCombo.setSelectedIndex(selectedIndex);
                            
                            //displaying the combo
                            widgetsPanel.add(valueChooserPanel);
                        }
                        
                        //adding the listener to the combo
                        tagValuesCombo.addActionListener(comboListener);
                    }
                }
                
                widgetsPanel.setVisible(false);
                widgetsPanel.setVisible(true);
            }
        };
        
        widgetTree.addTreeSelectionListener(selectionListener);
        
        //the dispose runnable
        disposeRunnable=new Runnable(){
            
            public void run() {

                ToolTipManager.sharedInstance().unregisterComponent(widgetTree);
                
                model.dispose();

                chooserButton.removeActionListener(buttonListener);
                tagValuesCombo.removeActionListener(comboListener);
                widgetTree.removeTreeSelectionListener(selectionListener);
                
                widgetsPanel.removeAll();
                removeAll();
            } 
        };
    }
    
    @Override
    public void dispose(){
        
        if(disposeRunnable!=null){
            
            disposeRunnable.run();
        }
    }
}
