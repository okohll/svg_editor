package fr.itris.glips.rtdaeditor.dbchooser;

import java.awt.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the panel containing the components used to choose a data base node
 * @author Jordi SUC
 */
public class DataBaseNodeChooserPanel extends JPanel{
	
	/**
	 * the map of the history of the selected tag names in the chooser.
	 * the map associates the type of a tag to its name
	 */
	public static final ConcurrentHashMap<Integer, String> tagNamesHistory=
		new ConcurrentHashMap<Integer, String>();

	/**
	 * the listeners to the database node chooser
	 */
	protected Set<DataBaseNodeChooserStateListener> listeners=
		new CopyOnWriteArraySet<DataBaseNodeChooserStateListener>();
	
	/**
	 * the document of the svg the tag chooser is required for
	 */
	protected Document document;
	
	/**
	 *  the filter for displaying the data base nodes
	 */
	protected DataBaseNodeFilter filter;
	
	/**
	 * whether to show the nodes only used for the view
	 */
	protected  boolean showViewNodes;
	
	/**
	 * whether to show the node for the "close popup dialog" option
	 */
	protected  boolean showClosePopupDialogNode;
	
	/**
	 * the tree containing the DB nodes
	 */
	protected JTree dataBaseTree=new JTree();
	
	/**
	 * the tree containing the other nodes
	 */
	protected JTree otherValuesTree=new JTree();
	
	/**
	 * the none node
	 */
	protected DefaultMutableTreeNode noneNode;
	
	/**
	 * the back node
	 */
	protected DefaultMutableTreeNode backNode;
	
	/**
	 * the quit node
	 */
	protected DefaultMutableTreeNode quitNode;
	
	/**
	 * the none node
	 */
	protected DefaultMutableTreeNode closePopupDialogNode;
	
	/**
	 * the tree selection listener
	 */
	protected TreeSelectionListener treeSelectionListener;
	
	/**
	 * whether the selection is correct
	 */
	protected boolean isCorrect=false;
	
	/**
	 * the constructor of the class
	 * @param document the document of the svg the tag chooser is required for
     * @param filter the filter for displaying the data base nodes
     * @param showViewNodes shows the nodes only used for the view
     * @param showClosePopupDialogNode shows the node for the "close popup dialog" option
	 */
	public DataBaseNodeChooserPanel(Document document, 
			DataBaseNodeFilter filter, boolean showViewNodes, 
			boolean showClosePopupDialogNode){

		this.document=document;
		this.filter=filter;
		this.showViewNodes=showViewNodes;
		this.showClosePopupDialogNode=showClosePopupDialogNode;
		
		//handling the tag name in the filter
		if(filter.getTagName().equals("")){
			
			//getting the last tag name selected in the chooser that
			//has the same type as the one defined in the filter
			String recordedTagName=tagNamesHistory.get(filter.getTagType());
			
			if(recordedTagName!=null && ! recordedTagName.equals("")){
				
				//modifying the tag name in the filter
				filter.setTagName(recordedTagName);
			}
		}

		build();
	}
	
	/**
	 * builds the panel
	 */
	protected void build(){
		
		//getting the data base root node corresponding  to the given document
		Element rootDataBaseElement=
			DataBaseNodeToolkit.getRootDataBaseElement(document);

		if(rootDataBaseElement!=null) {
			
	        //getting the labels
			ResourceBundle bundle=ResourcesManager.bundle;
	        String backLabel="<"+bundle.getString("rtdaanim_back")+">";
	        String closePopupLabel="<"+bundle.getString("rtdaanim_closePopupDialog")+">";
	        String quitLabel="<"+bundle.getString("rtdaanim_quit")+">";
	        String noneLabel="<"+bundle.getString("rtdaanim_none")+">";
	        
		    //handling the properties of the data base tree//
		    dataBaseTree.getSelectionModel().setSelectionMode(
		    		TreeSelectionModel.SINGLE_TREE_SELECTION);	        
		    dataBaseTree.setEditable(false);
		    dataBaseTree.setShowsRootHandles(true);
	        
	        //adding the tree renderer
	        DataBaseTreeCellRenderer renderer=
	        	new DataBaseTreeCellRenderer(dataBaseTree);
	        dataBaseTree.setCellRenderer(renderer);
		    
	        //handling the properties of the "none" tree
		    otherValuesTree.getSelectionModel().setSelectionMode(
		    		TreeSelectionModel.SINGLE_TREE_SELECTION);	        
		    otherValuesTree.setEditable(false);
		    otherValuesTree.setShowsRootHandles(false);
		    
		    //the root node of the tree displaying the other values
		    DefaultMutableTreeNode rootNode=
				new DefaultMutableTreeNode("", true);
	        
	        //creating the other nodes
		    noneNode=new DefaultMutableTreeNode(noneLabel, false);
		    backNode=new DefaultMutableTreeNode(backLabel, false);
		   	closePopupDialogNode=new DefaultMutableTreeNode(closePopupLabel, false);
		    quitNode=new DefaultMutableTreeNode(quitLabel, false);

	        otherValuesTree.setShowsRootHandles(false);
	        otherValuesTree.setRootVisible(false);

	        //creating and filling the trees panel
	        JPanel treesPanel=new JPanel();
	        treesPanel.setBackground(dataBaseTree.getBackground());
	        
	        GridBagLayout gridBag=new GridBagLayout();
	        treesPanel.setLayout(gridBag);
	        GridBagConstraints c=new GridBagConstraints();
	        
	        c.gridwidth=GridBagConstraints.REMAINDER;
	        c.weightx=50;
	        c.anchor=GridBagConstraints.NORTHWEST;
	        
	        gridBag.setConstraints(dataBaseTree, c);
	        treesPanel.add(dataBaseTree);
	        
	        c.insets=new Insets(5, 0, 0, 0);
	        gridBag.setConstraints(otherValuesTree, c);
	        treesPanel.add(otherValuesTree);

	        //the empty panel
	        JPanel emptyPanel=new JPanel();
	        emptyPanel.setBackground(dataBaseTree.getBackground());
	        c.weighty=50;
	        c.insets=new Insets(0, 0, 0, 0);
	        gridBag.setConstraints(emptyPanel, c);
	        treesPanel.add(emptyPanel);
	        
	        //creating the scrollpane containing the trees
	        JScrollPane scrollpane=new JScrollPane(treesPanel);
	        
	        //filling the content panel
	        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	        add(scrollpane);
	        
	        //creating and adding the listener to the tree selection changes
	        treeSelectionListener=new TreeSelectionListener(){
	        	
	        	public void valueChanged(TreeSelectionEvent evt) {

	        		notifySelectionCorrect(false);
	        		DataBaseTreeNode selectedNode=null;
	        		
	        		if(evt.getSource().equals(dataBaseTree) && 
	        				dataBaseTree.getLastSelectedPathComponent()!=null){
	        			
	    				//the selected node
	        			selectedNode=(DataBaseTreeNode)dataBaseTree.getLastSelectedPathComponent();
	        			
	        			//removing the tree selection listener from the none tree
	        			otherValuesTree.removeTreeSelectionListener(this);
	        			
	        			//removing the selection of the none tree
	        			otherValuesTree.setSelectionPath(null);

	        			if(selectedNode!=null && selectedNode.canBeSelected()){

	    	        		notifySelectionCorrect(true);
	        			}
	        			
	        			//adding the tree selection listener from the none tree
	        			otherValuesTree.addTreeSelectionListener(this);

	        		}else if(evt.getSource().equals(otherValuesTree)){
	        			
	        			//removing the tree selection listener from the none tree
	        			dataBaseTree.removeTreeSelectionListener(this);
	        			
	        			//removing the selection of the data base tree
	        			dataBaseTree.setSelectionPath(null);
	        			
	        			if(otherValuesTree.getSelectionPath()!=null){
	        				
	        				//the none node has been selected
	        				notifySelectionCorrect(true);
	        			}
	        			
	        			//adding the tree selection listener from the none tree
	        			dataBaseTree.addTreeSelectionListener(this);
	        		}
	        	}
	        };
	        
	        dataBaseTree.addTreeSelectionListener(treeSelectionListener);
	        otherValuesTree.addTreeSelectionListener(treeSelectionListener);
	        
	        //adding the nodes to the root nodes
	        if(showViewNodes){
	        	
	        	rootNode.add(backNode);
	        }
	        
	        if(showClosePopupDialogNode){
	        	
	        	rootNode.add(closePopupDialogNode);
	        }
	        
	        if(showViewNodes){
	        	
	            rootNode.add(quitNode);
	        }

	        rootNode.add(noneNode);
	        
	        //creating the default tree model
	        DefaultTreeModel treeModel=new DefaultTreeModel(rootNode);
	        otherValuesTree.setModel(treeModel);

	        //checking if another value than a node that is in the DB should be selected
            boolean isAnotherValue=false;
            
            if(showViewNodes && filter.getTagName().equals(DataBaseNodeToolkit.backValue)){
            	
            	otherValuesTree.setSelectionPath(new TreePath(new Object[]{rootNode, backNode}));
            	isAnotherValue=true;
            	
            }else if(showViewNodes && filter.getTagName().equals(DataBaseNodeToolkit.quitValue)){
            	
            	otherValuesTree.setSelectionPath(new TreePath(new Object[]{rootNode, quitNode}));
            	isAnotherValue=true;
            	
            }else if(showViewNodes && showClosePopupDialogNode && 
            		filter.getTagName().equals(DataBaseNodeToolkit.closePopupDialogValue)){
            	
            	otherValuesTree.setSelectionPath(
            			new TreePath(new Object[]{rootNode, closePopupDialogNode}));
            	isAnotherValue=true;
            	
            }else if(filter.getTagName().equals("")) {
            	
                //the none node of the none tree will be selected
            	otherValuesTree.setSelectionPath(new TreePath(new Object[]{rootNode, noneNode}));
            	isAnotherValue=true;
            }
			
			//creating the root node for the data base tree
			DataBaseTreeNode rootTreeNode=
				new DataBaseTreeNode(rootDataBaseElement, filter, rootDataBaseElement);
			
            if(rootTreeNode.getChildCount()>0) {
                
                //setting the new tree model for the data base tree
                DefaultTreeModel model=new DefaultTreeModel(rootTreeNode);
                dataBaseTree.setModel(model);
                
                //handling the state of the data base tree
                if(rootTreeNode.getChildCount()==0) {
                    
                    dataBaseTree.setVisible(false);
                    
                }else {
                    
                    dataBaseTree.setVisible(true);
                }
                
                dataBaseTree.setVisible(true);

                if(! isAnotherValue){
                	
                    //getting the element corresponding to the tag name
                    Element tagElement=DataBaseNodeToolkit.getElementForTag(
                    		rootDataBaseElement, filter.getTagName());
                    
                    //getting the corresponding tree node
                    DataBaseTreeNode selectedTreeNode=
                    	rootTreeNode.getTreeNodeForElement(tagElement);
                    
                    if(selectedTreeNode!=null) {
                        
                        //the tree node will be selected
                        dataBaseTree.setSelectionPath(
                        		new TreePath(selectedTreeNode.getPath()));
                        
                    }else{
                        
                        //the none node of the none tree will be selected
                    	otherValuesTree.setSelectionPath(
                    			new TreePath(new Object[]{rootNode, noneNode}));
                    }
                }

            }else {
                
                //hiding the database tree
                dataBaseTree.setVisible(false);
            }
		}
	}
	
	/**
	 * @return the information object that is computed
	 */
	public DataBaseNodeInformation getInfo(){
		
		DataBaseNodeInformation info=null;
		
		if(isCorrect){
			
			//getting the selected node in data base tree
			DataBaseTreeNode selectedNode=null;
			
			if(dataBaseTree.getLastSelectedPathComponent()!=null){

				selectedNode=(DataBaseTreeNode)
					dataBaseTree.getLastSelectedPathComponent();
			}
			
			if(selectedNode==null) {
				
				//getting the selected node in the other values tree
				DefaultMutableTreeNode node=(DefaultMutableTreeNode)
					otherValuesTree.getSelectionPath().getLastPathComponent();
				String value="";

				if(node.equals(backNode)){
					
					value=DataBaseNodeToolkit.backValue;
					
				}else if(node.equals(closePopupDialogNode)){

					value=DataBaseNodeToolkit.closePopupDialogValue;
					
				}else if(node.equals(quitNode)){

					value=DataBaseNodeToolkit.quitValue;
				}
				
				//recording the current value
				tagNamesHistory.put(filter.getTagType(), value);
				
				//the none node has been selected, the information object that will be returned is created
				info=new DataBaseNodeInformation(
						TagToolkit.NOT_A_TAG, value, new LinkedList<String>(), "");
				
			}else {
				
				//recording the current value
				tagNamesHistory.put(filter.getTagType(), selectedNode.getTagName());
				
				//a data base node has been selected, the information object that will be returned is created
				info=new DataBaseNodeInformation(
						selectedNode.getNodeType(), selectedNode.getTagName(), 
								selectedNode.getEnumeratedValues(), selectedNode.getLocation());
			}
		}

		return info;
	}
	
	/**
	 * notifies that the selection is correct
	 * @param correct whether the selection is correct
	 */
	protected void notifySelectionCorrect(boolean correct){
		
		isCorrect=correct;
		
		for(DataBaseNodeChooserStateListener listener : listeners){
			
			listener.notifySelectionCorrect(correct);
		}
	}
	
	/**
	 * adds a new listener
	 * @param listener a listener
	 */
	public void addSelectionStateListener(
			DataBaseNodeChooserStateListener listener){
		
		if(listener!=null){
			
			listeners.add(listener);
		}
	}
	
	/**
	 * removes the listener
	 * @param listener a listener
	 */
	public void removeSelectionStateListener(
			DataBaseNodeChooserStateListener listener){
		
		if(listener!=null){
			
			listeners.remove(listener);
		}
	}
	
	/**
	 * disposes the panel
	 */
	public void dispose(){
		
        dataBaseTree.removeTreeSelectionListener(treeSelectionListener);
        otherValuesTree.removeTreeSelectionListener(treeSelectionListener);
        listeners.clear();
	}
	
}
