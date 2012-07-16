package fr.itris.glips.extension.jwidget.base.edition;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.anim.components.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.rtdaeditor.widget.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import javax.swing.border.*;
import org.w3c.dom.*;

/**
 * the class of the widget of a menu items container
 * @author ITRIS, Jordi SUC
 */
public abstract class MenuItemsContainerEdition extends JWidgetEdition{
	
	/**
	 * the constant for the menu bar
	 */
	protected static final int MENU_BAR=0;
	
	/**
	 * the constant for the pop up menu
	 */
	protected static final int POP_UP=1;
	
	/**
	 * the constant for the menu
	 */
	protected static final int MENU=2;
	
	/**
	 * the constant for the menu item
	 */
	protected static final int MENU_ITEM=3;
	
	/**
	 * the constant for the menu bar
	 */
	protected static final String MENU_BAR_NAME="MenuBarWidget";
	
	/**
	 * the constant for the menu bar
	 */
	protected static final String POP_UP_NAME="PopUpMenuWidget";
	
	/**
	 * the constant for the menu bar
	 */
	protected static final String MENU_NAME="MenuWidget";
	
	/**
	 * the constant for the menu bar
	 */
	protected static final String MENU_ITEM_NAME="MenuItemWidget";
	
	/**
	 * the source separator
	 */
	protected static final String sourceSeparator="/";
	
	/**
	 * the main type
	 */
	protected int mainType=MENU_BAR;
	
	/**
	 * the icons that will be displayed in the tree
	 */
	protected static ImageIcon menuBarIcon, popUpMenuIcon, menuIcon, menuItemIcon;
	
	static {
		
		//loading the icons
		ImageIcon[] icons=getIcons(MenuItemsContainerEdition.class, "MenuBarWidget");
		
		if(icons!=null && icons.length>1) {
			
			menuBarIcon=icons[0];
		}
		
		icons=getIcons(MenuItemsContainerEdition.class, "PopUpMenuWidget");
		
		if(icons!=null && icons.length>1) {
			
			popUpMenuIcon=icons[0];
		}
		
		icons=getIcons(MenuItemsContainerEdition.class, "MenuWidget");
		
		if(icons!=null && icons.length>1) {
			
			menuIcon=icons[0];
		}
		
		icons=getIcons(MenuItemsContainerEdition.class, "MenuItemWidget");
		
		if(icons!=null && icons.length>1) {

			menuItemIcon=icons[0];
		}
	}
	
	/**
	 * the menu and menu item description
	 */
	protected String menuDescription="", menuItemDescription="";
	
	/**
	 * the constructor of the class
	 * @param jwidgetManager the jwidget manager
	 * @param mainFrame the main frame
	 * @param mainType the type of the jwidget
	 * @param id the id of the jwidget type
	 * @param position the position of the toggle button of the jwidget
	 */
	public MenuItemsContainerEdition(JWidgetManager jwidgetManager, 
			Frame mainFrame, int mainType, String id, int position) {
		
		super(jwidgetManager, mainFrame, id, position);
		this.mainType=mainType;
    	this.containsInnerComponents=true;
		
		//getting the descriptions
		try {
			menuDescription=bundle.getString("menuLabel");
			menuItemDescription=bundle.getString("menuItemLabel");
		}catch (Exception ex) {}
		
		//adding the properties and their default values
    	propertiesList.add("foregroundColor");
    	propertiesList.add("fontFamily");
    	propertiesList.add("fontSize");
    	propertiesList.add("bold");
    	propertiesList.add("italic");
    	
    	defaultValues.add("#000000");
    	defaultValues.add(FontFamilyChooserWidget.SANS_SERIF);
    	defaultValues.add("12");
    	defaultValues.add("false");
    	defaultValues.add("false");
		
		//building the configuration panel
		buildConfigurationPanel();
		
		//building the source choosers
    	buildSourceChoosers();
	}

	@Override
	public Element createJWidgetElement(Element parentElement) {
		
		Element jwidgetElement=super.createJWidgetElement(parentElement);
		jwidgetElement.setAttribute(fr.itris.glips.library.Toolkit.labelAttribute, description);
		
		return jwidgetElement;
	}

	@Override
	protected void buildConfigurationPanel() {
		
		configurationPanel=new ExtendedJWidgetConfigurationPanel();
	}
	
	/**
	 * builds the source choosers
	 */
	protected void buildSourceChoosers() {
		
		animationsSourceChooser=new TreeAnimationChooserJWidgetSourceChooser();
		actionsSourceChooser=new TreeAnimationChooserJWidgetSourceChooser();
	}
	
	/**
	 * the class of the configuration panel
	 * @author ITRIS, Jordi SUC
	 */
	protected class ExtendedJWidgetConfigurationPanel extends JWidgetConfigurationPanel{
		
		/**
		 * the tree displaying the menu items
		 */
		protected JTree tree;
		
		/**
		 * the tree model
		 */
		protected MenuItemTreeModel treeModel;
		
		/**
		 * the tree selection listener
		 */
		protected TreeSelectionListener treeSelectionListener;
		
		/**
		 * the new, delete, up and down buttons
		 */
		protected JButton newButton, deleteButton, upButton, downButton;
		
		/**
		 * the icons for the buttons
		 */
		private ImageIcon	newIcon=ResourcesManager.getIcon("New", false),
									deleteIcon=ResourcesManager.getIcon("Delete", false),
									ddeleteIcon=ResourcesManager.getIcon("Delete", true),
									upButtonIcon=ResourcesManager.getIcon("ArrowUp", false),
									dupButtonIcon=ResourcesManager.getIcon("ArrowUp", true),
									downButtonIcon=ResourcesManager.getIcon("ArrowDown", false),
									ddownButtonIcon=ResourcesManager.getIcon("ArrowDown", true);
		
		/**
		 * the pop up menu
		 */
		protected JPopupMenu popMenu=new JPopupMenu();
		
		/**
		 * the menu items used to create menu items or menus
		 */
		protected JMenuItem menuItemCreator, menuCreator;
		
		/**
		 * the font style chooser
		 */
		protected FontStyleChooser fontStyleChooser;
		
		/**
		 * the font style listener
		 */
		protected ActionListener fontStyleListener;
		
		/**
		 * the constructor of the class
		 */
		protected ExtendedJWidgetConfigurationPanel() {
			
			buildPanel();
		}

		@Override
		public void initializePanel() {

			//initializing the text
			tree.removeTreeSelectionListener(treeSelectionListener);
			treeModel.setCurrentTreeModel(tree, getElement());
			
			if(getElement()!=null) {
				
				tree.addTreeSelectionListener(treeSelectionListener);
				tree.setSelectionPath(new TreePath(treeModel.getRoot()));
				
				//getting the array of the values for the font style chooser
				String[] values=new String[5];
				
				for(int i=0; i<values.length; i++){
					
					values[i]=getProperty(getElement(), propertiesList.get(i));
				}

				fontStyleChooser.removeListener(fontStyleListener);
				fontStyleChooser.init(values);
				fontStyleChooser.addListener(fontStyleListener);
			}
		}
		
		@Override
		public void buildPanel() {

			//getting the label
			String configurationLabel="", treeLabel="";
			
			try {
				configurationLabel=bundle.getString("configurationPanelLabel");
				treeLabel=bundle.getString("treeLabel");
			}catch (Exception ex) {}
			
			JLabel configurationLbl=new JLabel(configurationLabel);
			JPanel configurationLabelPanel=new JPanel();
			configurationLabelPanel.setBorder(new EmptyBorder(0, 1, 1, 1));
			configurationLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			configurationLabelPanel.add(configurationLbl);
			
			//creating the tree
			tree=new JTree();
			tree.setRootVisible(true);
			tree.setShowsRootHandles(true);
			tree.setExpandsSelectedPaths(true);
			tree.setLargeModel(false);
			tree.setScrollsOnExpand(true);
	        ToolTipManager.sharedInstance().registerComponent(tree);
	        
			//the model
			treeModel=new MenuItemTreeModel();
			tree.setModel(treeModel);
			
			//the tree cell renderer
			MenuItemTreeCellRenderer renderer=new MenuItemTreeCellRenderer(tree);
			tree.setCellRenderer(renderer);
			
			//the tree cell editor
			MenuItemTreeCellEditor editor=new MenuItemTreeCellEditor(tree, renderer);
			tree.setCellEditor(editor);
			tree.setEditable(true);
			
			//the selection model
			DefaultTreeSelectionModel selectionModel=new DefaultTreeSelectionModel();
			selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			tree.setSelectionModel(selectionModel);
			
			//creating the widget to create, delete, move the columns//
			//the buttons panel
			newButton=new JButton(newIcon); 
			deleteButton=new JButton(deleteIcon);
			deleteButton.setDisabledIcon(ddeleteIcon);
			upButton=new JButton(upButtonIcon); 
			upButton.setDisabledIcon(dupButtonIcon);
			downButton=new JButton(downButtonIcon);
			downButton.setDisabledIcon(ddownButtonIcon);
			
			//getting the labels for the tool tip
			String newLabel="", deleteLabel="";
			
			try {
				newLabel=ResourcesManager.bundle.getString("labelnew");
				deleteLabel=ResourcesManager.bundle.getString("labeldelete");
			}catch (Exception ex) {}
			
			Insets insets=new Insets(1, 1, 1, 1);
			
			//setting the properties of the buttons
			newButton.setMargin(insets);
			newButton.setToolTipText(newLabel);
			deleteButton.setMargin(insets);
			deleteButton.setToolTipText(deleteLabel);
			upButton.setMargin(insets);
			downButton.setMargin(insets);
			
			//building the buttons panel
			JPanel buttons=new JPanel();
			buttons.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 0));
			buttons.add(newButton);
			buttons.add(deleteButton);
			buttons.add(upButton);
			buttons.add(downButton);
			buttons.setBorder(new EmptyBorder(2, 0, 2, 0));
			
			deleteButton.setEnabled(false);
			upButton.setEnabled(false);
			downButton.setEnabled(false);
			
			//building the popup menu
			buildPopUpMenu();
			
			//adding the listeners to the buttons
			newButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent evt) {

	                Point mousePosition=newButton.getMousePosition();
	                
	                if(mousePosition!=null){
	                	
						//getting the selected node
						TreePath path=tree.getSelectionPath();
						
						if(path!=null) {
							
							MenuItemTreeNode treeNode=(MenuItemTreeNode)path.getLastPathComponent();
							
		        			//handles the state of the menu item creator
		        			menuItemCreator.setEnabled(treeNode.getType()!=MENU_BAR);
						}

	                	popMenu.show(newButton, mousePosition.x, mousePosition.y);
	                }
				}
			});
			
			deleteButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent evt) {
					
					//getting the selected node
					TreePath path=tree.getSelectionPath();
					
					if(path!=null) {
						
						MenuItemTreeNode treeNode=(MenuItemTreeNode)path.getLastPathComponent();
						
						if(treeNode.getParent()!=null) {
							
							//removing the selected tree node
							((MenuItemTreeNode)treeNode.getParent()).removeChildNode(treeNode);
						}
					}
				}
			});
			
			upButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent evt) {
					
					//getting the selected node
					TreePath path=tree.getSelectionPath();
					
					if(path!=null) {
						
						MenuItemTreeNode treeNode=(MenuItemTreeNode)path.getLastPathComponent();
						
						//putting the selected tree node at a upper place
						treeNode.putUp();
					}
				}
			});
			
			downButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent evt) {
					
					//getting the selected node
					TreePath path=tree.getSelectionPath();
					
					if(path!=null) {
						
						MenuItemTreeNode treeNode=(MenuItemTreeNode)path.getLastPathComponent();
						
						//putting the selected tree node at a upper place
						treeNode.putDown();
					}
				}
			});
			
			//adding a listener to the tree selections
			treeSelectionListener=new TreeSelectionListener() {

				public void valueChanged(TreeSelectionEvent evt) {
					
					MenuItemTreeNode treeNode=null;
					
					if(evt.getPath()!=null) {
						
						treeNode=(MenuItemTreeNode)evt.getPath().getLastPathComponent();	
					}
					
					handleButtonsState(treeNode);
				}
			};

			//building the component
			JScrollPane scrollpane=new JScrollPane(tree);
			scrollpane.setPreferredSize(new Dimension(100, 225));
			
			//creating the tree panel
			JPanel treePanel=new JPanel();
			treePanel.setBorder(new TitledBorder(treeLabel));
			
			GridBagLayout gridBag=new GridBagLayout();
			treePanel.setLayout(gridBag);
			GridBagConstraints c=new GridBagConstraints();
			c.fill=GridBagConstraints.HORIZONTAL;
			
			c.gridx=0;
			c.gridy=0;
			gridBag.setConstraints(configurationLabelPanel, c);
			treePanel.add(configurationLabelPanel);
			
			c.gridy=1;
			c.weightx=50;
			gridBag.setConstraints(scrollpane, c);
			treePanel.add(scrollpane);
			
			c.gridy=2;
			c.weightx=0;
			gridBag.setConstraints(buttons, c);
			treePanel.add(buttons);
			
			//creating the font style chooser
			fontStyleChooser=new FontStyleChooser();
			
			//creating the font style listener
			fontStyleListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					
					//getting the array of the values
					String[] newValues=fontStyleChooser.getValues();
					
					//setting all the properties
					for(int i=0; i<newValues.length; i++){
						
						setProperty(getElement(), propertiesList.get(i), newValues[i], true);
					}
				}
			};
			
			//creating and filling the all panel
			JPanel allPanel=new JPanel();
			allPanel.setBorder(new EmptyBorder(7, 7, 20, 7));
			gridBag=new GridBagLayout();
			allPanel.setLayout(gridBag);
			c=new GridBagConstraints();
			c.fill=GridBagConstraints.HORIZONTAL;
			
			c.gridx=0;
			c.gridy=0;
			c.gridheight=2;
			c.weightx=2;
			gridBag.setConstraints(treePanel, c);
			allPanel.add(treePanel);
			
			c.gridx=1;
			c.gridheight=1;
			c.weightx=0;
			gridBag.setConstraints(fontStyleChooser, c);
			allPanel.add(fontStyleChooser);

			SpringLayout layout=new SpringLayout();
			setLayout(layout);
			layout.putConstraint(SpringLayout.NORTH, this, 0, SpringLayout.NORTH, allPanel);
			layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, allPanel);
			add(allPanel);
		}
		
		/**
		 * handles the buttons' state
		 * @param treeNode the currently selected tree node
		 */
		protected void handleButtonsState(MenuItemTreeNode treeNode) {
			
			//disabling the buttons
			 deleteButton.setEnabled(false);
			 upButton.setEnabled(false);
			 downButton.setEnabled(false);

			 //handling the state of the buttons
			 if(treeNode!=null) {
				 
				 if(treeNode.getParent()!=null) {
					 
					 deleteButton.setEnabled(true);
				 }
				 
				 if(treeNode.getPreviousSibling()!=null) {
					 
					 upButton.setEnabled(true);
				 }
				 
				 if(treeNode.getNextSibling()!=null) {
					 
					 downButton.setEnabled(true);
				 }
			 }
		}
		
		/**
		 * creates the pop up menu
		 */
		protected void buildPopUpMenu() {
			
			//getting the label of the menu items of the pop up menu
			String menuString="", menuItemString="";
			
			try {
				menuString=bundle.getString("labelMenuPopUp");
				menuItemString=bundle.getString("labelMenuItemPopUp");
			}catch (Exception ex) {}
			
			//creates the menu items and the pop up menus
			menuCreator=new JMenuItem(menuString);
			menuItemCreator=new JMenuItem(menuItemString);
			
			//adding the listener to the menu items
			menuCreator.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt) {

					//getting the selected tree node
					MenuItemTreeNode selectedTreeNode=getSelectedTreeNode();
					
					if(selectedTreeNode!=null) {
						
						selectedTreeNode.insertChildNode(MENU);
					}
				}
			});
			
			menuItemCreator.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt) {

					//getting the selected tree node
					MenuItemTreeNode selectedTreeNode=getSelectedTreeNode();
					
					if(selectedTreeNode!=null) {
						
						selectedTreeNode.insertChildNode(MENU_ITEM);
					}
				}
			});
			
			//building the pop up menu
			popMenu.add(menuItemCreator);
			popMenu.add(menuCreator);
			
			//adding the listener to the tree selection
			tree.addTreeSelectionListener(new TreeSelectionListener() {
				
				/**
				 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
				 */
				public void valueChanged(TreeSelectionEvent evt) {
					
					//getting the selected tree node
					MenuItemTreeNode selectedTreeNode=getSelectedTreeNode();
					
					//handling the state
					menuCreator.setEnabled(false);
					menuItemCreator.setEnabled(false);
					newButton.setEnabled(false);
					
					if(	selectedTreeNode!=null && 
						(selectedTreeNode.getType()==MENU || selectedTreeNode.getType()==MENU_BAR ||
						selectedTreeNode.getType()==POP_UP)) {

						menuCreator.setEnabled(true);
						menuItemCreator.setEnabled(true);
						newButton.setEnabled(true);
					}
				}
			});
		}
		
		 /**
		  * @return the selected element of the tree
		  */
		 protected  MenuItemTreeNode getSelectedTreeNode() {
			 
			MenuItemTreeNode treeNode=null;
			
			if(tree.getSelectionPath()!=null) {
				
				treeNode=(MenuItemTreeNode)tree.getSelectionPath().getLastPathComponent();
				
				return treeNode;
			}
			 
			 return null;
		 }
	}

	/**
	 * the class of the menu item nodes
	 * @author ITRIS, Jordi SUC
	 */
	protected class MenuItemTreeNode extends DefaultMutableTreeNode{
		
		/**
		 * the tree
		 */
		private JTree currentTree=null;
		
		/**
		 * the tree model
		 */
		protected MenuItemTreeModel model;
		
		/**
		 * the jwidget element
		 */
		private Element currentElement=null;
		
		/**
		 * the listener to the dom
		 */
		private SVGDOMListener domListener=null;
		
		/**
		 * the type of the node
		 */
		private int type=TagToolkit.NOT_A_TAG;
		
		/**
		 * the icon of the node
		 */
		private ImageIcon theIcon;
		
		/**
		 * the node label
		 */
		private String nodeLabel="";
		
		/**
		 * the set of the children of the element
		 */
		private HashSet<Element> childElements=new HashSet<Element>();
		
		/**
		 * the constructor of the class
		 * @param nodeTree the tree
		 * @param treeModel the tree model
		 * @param element the jwidget element
		 */
		public MenuItemTreeNode(JTree nodeTree, MenuItemTreeModel treeModel, Element element) {

			this.currentTree=nodeTree;
			this.model=treeModel;
			this.currentElement=element;
			SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
			
			//building the tree path
			LinkedList<DefaultMutableTreeNode> pathList=new LinkedList<DefaultMutableTreeNode>();
			pathList.add(this);
			
			setAllowsChildren(false);
			
			//getting the type of the element
			type=getType();
			
			//getting the tool tip label and icon for this node
			switch (type) {
			
				case MENU_BAR :
					
					theIcon=menuBarIcon;
					nodeLabel=description;
					break;
					
				case POP_UP :
					
					theIcon=popUpMenuIcon;
					nodeLabel=description;
					break;
					
				case MENU :
					
					theIcon=menuIcon;
					nodeLabel=menuDescription;
					break;
					
				case MENU_ITEM :
					
					theIcon=menuItemIcon;
					nodeLabel=menuItemDescription;
					break;
			}
			
			//checking if the tag can have child nodes
			setAllowsChildren(type!=MENU_ITEM);
			
			//creating the children
			createChildren();
			
			//adding a listener to the element so that the tree node can be notified when the element changes
			domListener=new SVGDOMListener(currentElement){
				
				@Override
				public void nodeChanged(){
					
					//checking if the given node equals this tree path element, and notifies the listener that this node
					//has been modified
					if(getNode()!=null && getNode() instanceof Element && getNode().equals(currentElement)){
						
						model.nodeChanged(MenuItemTreeNode.this);
						currentTree.setSelectionPath(MenuItemTreeNode.this.getTreePath());
					}
				}
				
				@Override
				public void nodeInserted(Node insertedNode) {
					
					//checking if the inserted node has not already been inserted
					if(! childElements.contains(insertedNode)){
						
						//creating and adding the new tree node
						MenuItemTreeNode childTreeNode=new MenuItemTreeNode(	currentTree, model, (Element)insertedNode);
						model.insertNodeInto(childTreeNode, MenuItemTreeNode.this, getChildCount());
						currentTree.setSelectionPath(MenuItemTreeNode.this.getTreePath());
						currentTree.makeVisible(childTreeNode.getTreePath());
					}
				}
				
				@Override
				public void nodeRemoved(Node removedNode){
					
					if(removedNode!=null){
						
						//getting and removing the tree node
						MenuItemTreeNode childTreeNode=getTreeNode((Element)removedNode);
						
						if(childTreeNode!=null){
							
							model.removeNodeFromParent(childTreeNode);
							
							if(getChildCount()>0){
								
								MenuItemTreeNode firstChild=((MenuItemTreeNode)getFirstChild());
								currentTree.scrollPathToVisible(firstChild.getTreePath());
								currentTree.setSelectionPath(firstChild.getTreePath());
								
							}else{

								currentTree.scrollPathToVisible(getTreePath());
								currentTree.setSelectionPath(getTreePath());
							}
						}
					}
				}
				
				@Override
				public void structureChanged(final Node lastModifiedNode) {

					//removing all the child node of this tree node and creating them
					createChildren();
					model.nodeStructureChanged(MenuItemTreeNode.this);
					
					//getting the tree node corresponding to the given node
					MenuItemTreeNode treeNode=getTreeNode((Element)lastModifiedNode);
					
					if(treeNode!=null) {

						currentTree.getSelectionModel().setSelectionPath(treeNode.getTreePath());
						currentTree.makeVisible(treeNode.getTreePath());
					}
				}
			};

			handle.getSvgDOMListenerManager().addDOMListener(domListener);
		}
		
		/**
		 * creates the children of this node
		 */
		protected void createChildren() {
			
			if(allowsChildren){
				
				if(children!=null) {
					
					MenuItemTreeNode treeNode=null;
					
					//disposing all the previous children
					for(Iterator<?> it=children.iterator(); it.hasNext();) {

						treeNode=(MenuItemTreeNode)it.next();
						
						if(treeNode!=null) {

							treeNode.dispose();
						}
					}
				}

				//removing all the previous children
				removeAllChildren();
				childElements.clear();

				//building the children nodes of this node
				if(currentElement!=null && currentElement.hasChildNodes()){
					
					Node child=null;
					NodeList childrenNodes=currentElement.getChildNodes();
					MenuItemTreeNode childTreeNode=null;
					
					for(int i=0; i<childrenNodes.getLength(); i++){
						
						child=childrenNodes.item(i);
						
						if(child!=null && child instanceof Element){

							childTreeNode=new MenuItemTreeNode(currentTree, model, (Element)child);
							add(childTreeNode);
						}
					}
				}
			}
		}
		
		/**
		 * disposes this node and its child nodes
		 */
		public void dispose(){

			if(domListener!=null){

				domListener.removeListener();
				domListener=null;
			}
			
			//disposes each child
			MenuItemTreeNode child=null;
			Enumeration<?> childrenEnum=children();
			
			while(childrenEnum.hasMoreElements()){
				
				child=(MenuItemTreeNode)childrenEnum.nextElement();
				
				if(child!=null){
					
					child.dispose();
				}
			}
			
			currentElement=null;
			childElements.clear();
		}
		
		/**
		 * computes and returns the type of the jwidget element
		 * @return the type of the jwidget element
		 */
		protected int getType() {
			
			int jwidgetType=MENU_BAR;
			
			if(currentElement!=null) {
				
				String name=currentElement.getAttribute(
					fr.itris.glips.library.Toolkit.nameAttribute);
				
				if(name.equals(POP_UP_NAME)) {
					
					jwidgetType=POP_UP;
					
				}else if(name.equals(MENU_NAME)) {
					
					jwidgetType=MENU;
					
				}else if(name.equals(MENU_ITEM_NAME)) {
					
					jwidgetType=MENU_ITEM;
				}
			}
			
			return jwidgetType;
		}
		
		@Override
		public void add(MutableTreeNode newTreeNode){
			
			super.add(newTreeNode);
			
			if(newTreeNode!=null && newTreeNode instanceof MenuItemTreeNode){
				
				childElements.add(((MenuItemTreeNode)newTreeNode).getJWidgetElement());
			}
		}
		
		@Override
		public void remove(MutableTreeNode oldTreeNode){
			
			super.remove(oldTreeNode);
			
			if(oldTreeNode instanceof MenuItemTreeNode){
				
				childElements.remove(((MenuItemTreeNode)oldTreeNode).getJWidgetElement());
			}
		}
		
		/**
		 * @return whether this node can be removed or not
		 */
		public boolean canRemoveNode(){
			
			return type==MENU || type==MENU_ITEM;
		}
		
		/**
		 * @return the set of the child elements of this node
		 */
		public HashSet<Element> getChildElements(){
			
			return childElements;
		}
		
		/**
		 * @return the jwidgetElement
		 */
		public Element getJWidgetElement() {
			return currentElement;
		}
		
		/**
		 * returns the tree node corresponding to the given element
		 * @param childElement an element
		 * @return the tree node corresponding to the given element
		 */
		protected MenuItemTreeNode getTreeNode(Element childElement){
			
			MenuItemTreeNode childTreeNode=null;
			
			if(childElement!=null){
				
				MenuItemTreeNode child=null;
				
				for(Iterator<?> it=children.iterator(); it.hasNext();){
					
					child=(MenuItemTreeNode)it.next();
					
					if(child!=null && child.getJWidgetElement().equals(childElement)){
						
						childTreeNode=child;
						break;
					}
				}
			}
			
			return childTreeNode;
		}
		
		/**
		 * @return the path in the tree
		 */
		public TreePath getTreePath() {
			
			return new TreePath(getPath());
		}
		
		/**
		 * @return the label
		 */
		public String getLabel() {
			
			return currentElement.getAttribute(
				fr.itris.glips.library.Toolkit.labelAttribute);
		}
		
		/**
		 * @return the id
		 */
		public String getNodeId() {
			
			return currentElement.getAttribute(
				fr.itris.glips.library.Toolkit.idAttribute);
		}
		
		/**
		 * @param parentElement the parent element
		 * @param element the element from which siblings should be found not
		 * @return the set of the labels of the jwidget element
		 */
		protected HashSet<String> getSiblingLabels(Element parentElement, Element element){
			
			//creating the set of the labels of the siblings of the current node
			HashSet<String> siblingLabels=new HashSet<String>();
			String currentLabel="";
			
			for(Node node=parentElement.getFirstChild(); node!=null; node=node.getNextSibling()){

				if(node instanceof Element && (element==null ||! node.equals(element))){
					
					currentLabel=((Element)node).getAttribute(
						fr.itris.glips.library.Toolkit.labelAttribute);
					
					if(currentLabel!=null && ! currentLabel.equals("")){
						
						siblingLabels.add(currentLabel);
					}
				}
			}

			return siblingLabels;
		}
		
		/**
		 * returns whether the given label is already used
		 * @param foundLabel a label
		 * @return  whether the given id is already used
		 */
		public boolean isDuplicatedLabel0(String foundLabel){
			
			if(foundLabel!=null && ! foundLabel.equals("") && ! isRoot()){
				
				return getSiblingLabels((Element)getJWidgetElement().getParentNode(), getJWidgetElement())
								.contains(foundLabel);
			}
			
			return false;
		}
		
		/**
		 * creates and returns a unique label in the application given a base string
		 * @param baseString the base string for the label
		 * @return a unique label in the application given a base string
		 */
		protected String getLabel(String baseString){
			
			String returnLabel=baseString;
			
			if(baseString!=null){
				
				//getting the set of the ids of the siblings of the current node
				HashSet<String> siblingLabels=getSiblingLabels(getJWidgetElement(), null);
				
				//computes the first id that is not already used
				for(int i=0; i<siblingLabels.size(); i++){
					
					returnLabel=baseString+(i+1);
					
					if(! siblingLabels.contains(returnLabel)){
						
						break;
					}
				}
			}
			
			return returnLabel;
		}
		
		/**
		 * removes the given node from the tree
		 * @param child
		 */
		public void removeChildNode(MenuItemTreeNode child){
			
			//computing the top level jwidget element of the given child node
			Element topLevelElement=getTopMostJWidgetElement(currentElement);
			
			if(topLevelElement!=null) {
				
				removeSubWidgetElement(	currentElement, child.getJWidgetElement(), 
															topLevelElement.equals(currentElement));
			}
		}
		
		/**
		 * inserts a new child node in the tree
		 * @param nodeType the type of the node to be created
		 */
		protected void insertChildNode(int nodeType){
			
			String name="", widgetLabel="";

			if(nodeType==MENU) {
				
				name=MENU_NAME;
				widgetLabel=getLabel(menuDescription);
				
			}else if(nodeType==MENU_ITEM){
				
				name=MENU_ITEM_NAME;
				widgetLabel=getLabel(menuItemDescription);
			}
			
			//computing the top level jwidget element of the given child node
			Element topLevelElement=getTopMostJWidgetElement(currentElement);
			
			if(topLevelElement!=null) {
				
				createSubWidgetElement(currentElement, name, widgetLabel, new HashMap<String, String>(),
														topLevelElement.equals(currentElement));
			}
		}
		
		/**
		 * puts this tree node at a upper place
		 */
		protected void putUp() {
			
			//computing the top level jwidget element of the given child node
			Element topLevelElement=getTopMostJWidgetElement(currentElement);
			
			if(topLevelElement!=null) {
				
				MenuItemsContainerEdition.this.putUp(currentElement, topLevelElement.equals(currentElement));
			}
		}
		
		/**
		 * puts this tree node at a lower place
		 */
		protected void putDown() {
			
			//computing the top level jwidget element of the given child node
			Element topLevelElement=getTopMostJWidgetElement(currentElement);
			
			if(topLevelElement!=null) {
				
				MenuItemsContainerEdition.this.putDown(currentElement, topLevelElement.equals(currentElement));
			}
		}
		
		/**
		 * sets the new label for this node
		 * @param newLabel the new label for this node
		 */
		protected void setLabel(String newLabel) {
			
			//computing the top level jwidget element of the given child node
			Element topLevelElement=getTopMostJWidgetElement(currentElement);
			
			if(topLevelElement!=null) {
				
				setProperty(currentElement, fr.itris.glips.library.Toolkit.labelAttribute, 
					newLabel, topLevelElement.equals(currentElement));
			}
		}
		
		@Override
		public String toString(){
			
			return currentElement.getAttribute(fr.itris.glips.library.Toolkit.labelAttribute);
		}
		
		/**
		 * @return the tool tip text for this node
		 */
		public String getToolTipText(){
			
			return nodeLabel;
		}

		/**
		 * @return the icon that will be displayed for this node
		 */
		public ImageIcon getIcon() {
			return theIcon;
		}
	}
	
	/**
	 * the model for the menu items tree
	 * @author ITRIS, Jordi SUC
	 */
	protected class MenuItemTreeModel extends DefaultTreeModel implements TreeModel{
		
		/**
		 * the root tree node
		 */
		private MenuItemTreeNode rootNode=null;
		
		/**
		 * the constructor of the class
		 */
		public MenuItemTreeModel(){
			
			super(null);
		}
		
		/**
		 * sets the current new tree model
		 * @param tree a tree
		 * @param jwidgetElement a jwidget element
		 */
		public void setCurrentTreeModel(JTree tree, Element jwidgetElement) {
			
			//disposing the previous nodes
			if(rootNode!=null) {
				
				rootNode.dispose();
			}
			
			if(jwidgetElement!=null) {
				
				//creating the root node that will be displayed in the JTree
				rootNode=new MenuItemTreeNode(tree, this, jwidgetElement);
				setRoot(rootNode);
				
			}else {
				
				rootNode=null;
				setRoot(null);
			}
		}
		
		@Override
		public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) {
			
			super.insertNodeInto(newChild, parent, index);
			
			if(parent!=null && parent instanceof MenuItemTreeNode && 
					newChild!=null && newChild instanceof MenuItemTreeNode){
				
				((MenuItemTreeNode)parent).getChildElements().add(((MenuItemTreeNode)newChild).getJWidgetElement());
			}
		}
		
		@Override
		public void removeNodeFromParent(MutableTreeNode childNode) {
			
			if(childNode!=null && childNode instanceof MenuItemTreeNode){
				
				((MenuItemTreeNode)childNode.getParent()).getChildElements().
				remove(((MenuItemTreeNode)childNode).getJWidgetElement());
			}
			
			super.removeNodeFromParent(childNode);
		}
		
		@Override
		public void valueForPathChanged(TreePath path, Object newValue){
			
			if(path!=null && newValue!=null && newValue instanceof String){
				
				MenuItemTreeNode node=(MenuItemTreeNode)path.getLastPathComponent();
				
				//if the node has changed, the modifications are flushed
				if(node!=null){
					
					/*if(node.isDuplicatedId(newValue.toString())){
						
						showDuplicatedIdDialog();
						
					}else if(newValue.toString()==null || (newValue.toString()!=null && newValue.toString().equals(""))){
						
						showMalformedIdDialog();
						
					}else{
						
						node.setId(newValue.toString());
					}*/
					
					node.setLabel(newValue.toString());
				}
			}
			
			super.valueForPathChanged(path, newValue);
		}

		@Override
		public void fireTreeStructureChanged(Object arg0, Object[] arg1, int[] arg2, Object[] arg3) {

			super.fireTreeStructureChanged(arg0, arg1, arg2, arg3);
		}
		
	    /**
	     * shows a dialog notifying that the id is duplicated
	     */
	    protected void showDuplicatedIdDialog(){
	    	
	        //getting the labels
	        ResourceBundle editorBundle=ResourcesManager.bundle;
        	String duplicatedIdLabel="", errorLabel="";
	        
	        if(editorBundle!=null){
	           
	            try{
	                errorLabel=editorBundle.getString("labelerror");
	            	duplicatedIdLabel=editorBundle.getString("rtdaanim_duplicatedId");
	            }catch (Exception ex){}
	        }
	    	
	        JOptionPane.showMessageDialog(	Editor.getParent(), 
																duplicatedIdLabel,
																errorLabel,
																JOptionPane.ERROR_MESSAGE);
	    }
	    
	    /**
	     * shows a dialog notifying that the id is malformed
	     */
	    protected void showMalformedIdDialog(){
	    	
	        //getting the labels
	        ResourceBundle editorBundle=ResourcesManager.bundle;
        	String malformedIdLabel="", errorLabel="";
	        
	        if(editorBundle!=null){
	           
	            try{
	                errorLabel=editorBundle.getString("labelerror");
	            	malformedIdLabel=editorBundle.getString("rtdaanim_malformedId");
	            }catch (Exception ex){}
	        }
	        
	        JOptionPane.showMessageDialog(
	        		Editor.getParent(), malformedIdLabel, errorLabel,
							JOptionPane.ERROR_MESSAGE);
	    }

	}
	
	/**
	 * the class of the tree cell renderer
	 * @author ITRIS, Jordi SUC
	 */
	public class MenuItemTreeCellRenderer extends DefaultTreeCellRenderer{

	    /**
	     * the colors
	     */
	    private Color textSelectionColor, textNonSelectionColor, 
	                         backgroundSelectionColor, backgroundNonSelectionColor,
	                         borderSelectionColor;
	    
	    /**
	     * the label that will be used for the rendering
	     */
	    private JLabel labelCmp;
	    
	    /**
	     * whether the cell is selected or not
	     */
	    protected boolean selected=false;
	    
	    /**
	     * whether the cell has focus or not
	     */
	    protected boolean hasFocus=false;
	    
	    /**
	     * the constructor of the class
	     * @param tree the tree that will use this renderer
	     */
	    public MenuItemTreeCellRenderer(JTree tree){

	        //getting the colors
	        textSelectionColor=UIManager.getColor("Tree.selectionForeground");
	        textNonSelectionColor=UIManager.getColor("Tree.textForeground");
	        backgroundSelectionColor=UIManager.getColor("Tree.selectionBackground");
	        backgroundNonSelectionColor=UIManager.getColor("Tree.textBackground");
	        borderSelectionColor=UIManager.getColor("Tree.selectionBorderColor");
	        
	        //creating the label that will be used for rendering
	        labelCmp=new JLabel() {

	        	@Override
	            protected void paintComponent(Graphics g) {

	                int paintedX=0;
	                
	                if(getIcon()!=null) {
	                	
	                	paintedX=getIcon().getIconWidth()+getIconTextGap();
	                }
	                
	                //setting the background
	                if(selected) {
	                    
	                    g.setColor(backgroundSelectionColor);

	                }else {
	                    
	                    g.setColor(backgroundNonSelectionColor);
	                }
	                
	                g.fillRect(paintedX, 0, getWidth()-paintedX-1, getHeight()-1);
	                super.paintComponent(g);
	                
	                //super.paintComponent(g);
	  
	                //painting the focus
	                if(hasFocus) {
	                    
	                    g.setColor(borderSelectionColor);
	                    g.drawRect(paintedX, 0, getWidth()-paintedX-1, getHeight()-1);
	                }
	            }
	        };
	        
	        labelCmp.setBorder(new EmptyBorder(2, 2, 2, 2));
	        labelCmp.setComponentOrientation(tree.getComponentOrientation());
	    }
	    
	    @Override
	    public Component getTreeCellRendererComponent(JTree theTree, Object value, 
	    		boolean sel, boolean expanded, boolean leaf, int row, boolean hasFcs){

	        if(value!=null && value instanceof MenuItemTreeNode){

	            this.selected=sel;
	            this.hasFocus=hasFcs;
	            
	            MenuItemTreeNode node=(MenuItemTreeNode)value;
	            
	            //setting the icon
	            ImageIcon currentIcon=getIcon(node);
	            labelCmp.setIcon(currentIcon);

	            //setting the text
	            labelCmp.setText(value.toString());
	            labelCmp.setToolTipText(node.getToolTipText());
	        }
	        
	        return labelCmp;
	    }
	    
	    /**
	     * returning the icon corresponding to the given node
	     * @param node a node
	     * @return the icon corresponding to the given node
	     */
	    protected ImageIcon getIcon(MenuItemTreeNode node){
	        
	        ImageIcon currentIcon=null;
	        
	        if(node!=null){
	            
	            currentIcon=node.getIcon();
	        }
	        
	        return currentIcon;
	    }

	    @Override
	    public Color getBackgroundNonSelectionColor() {
	        return backgroundNonSelectionColor;
	    }

	    @Override
	    public Color getBackgroundSelectionColor() {
	        return backgroundSelectionColor;
	    }

	    @Override
	    public Color getBorderSelectionColor() {
	        return borderSelectionColor;
	    }

	    @Override
	    public Color getTextNonSelectionColor() {
	        return textNonSelectionColor;
	    }

	    @Override
	    public Color getTextSelectionColor() {
	        return textSelectionColor;
	    }

	}
	
	/**
	 * @author ITRIS, Jordi SUC
	 */
	public class MenuItemTreeCellEditor extends AbstractCellEditor implements TreeCellEditor{

	    /**
	     * the tree associated with this cell editor
	     */
	    private JTree widgetTree;
	    
	    /**
	     * the panel displaying the icon and the textField
	     */
	    private JPanel labelAndTextField=new JPanel();
	    
	    /**
	     * the label that will contain the icon
	     */
	    private JLabel iconLabel=new JLabel();
	    
	    /**
	     * the textfield that will be displayed for the editor
	     */
	    private JTextField textField=new JTextField();
	    
	    /**
	     * the listener to the textfield
	     */
	    private ActionListener textFieldListener=null;
	    
	    /**
	     * the value for the editor
	     */
	    private Object newValue=null;
	    
	    /**
	     * the constructor of the class
	     * @param widgetTree a JTree object
	     * @param renderer a DefaultTreeCellRenderer object
	     */
	    public MenuItemTreeCellEditor(JTree widgetTree, MenuItemTreeCellRenderer renderer){

	        this.widgetTree=widgetTree;

	        //creating and adding the listener to the text field
	        textFieldListener=new ActionListener() {
	            
	           public void actionPerformed(ActionEvent evt) {

	               //retrieving the new value
	                newValue=textField.getText();
	                
	                fireEditingStopped();
	            } 
	        };
	        
	        textField.addActionListener(textFieldListener);

	        //building the panel containing the label and the icon
	        iconLabel.setBackground(renderer.getBackgroundNonSelectionColor());
	        labelAndTextField.setBackground(renderer.getBackgroundNonSelectionColor());
	        labelAndTextField.setLayout(new BorderLayout(renderer.getIconTextGap(), 0));
	        labelAndTextField.add(iconLabel, BorderLayout.WEST);
	        labelAndTextField.add(textField, BorderLayout.CENTER);
	    }

	    /**
	     * disposes this editor
	     */
	    public void dispose() {
	        
	        textField.removeActionListener(textFieldListener);
	    }
	    
	    /**
	     * @return the value for the editor
	     */
	    public Object getCellEditorValue() {

	    	Object obj=newValue;
	    	newValue=null;

	        return obj;
	    }
	    
	    @Override
	    public boolean isCellEditable(EventObject event) {

	        boolean isCellEditable=false;

	        if(event instanceof MouseEvent){
	 
	            MouseEvent evt=(MouseEvent)event;
	            
	            if(SwingUtilities.isLeftMouseButton(evt) && evt.getClickCount()==1) {
	                
	                //getting the currently selected tree path
	                TreePath selectedTreePath=widgetTree.getSelectionPath();
	                
	                //getting the tree path corresponding to the hit point
	                TreePath hitPath=widgetTree.getPathForLocation(evt.getX(), evt.getY());
	                
	                if(	selectedTreePath!=null && hitPath!=null && selectedTreePath.equals(hitPath)) {
	                    
	                    //if the path is already selected, it can be edited
	                    isCellEditable=true;
	                }
	            }
	         }

	        return isCellEditable;
	    }
	    
	    /**
	     * @see javax.swing.tree.TreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	     */
	    public Component getTreeCellEditorComponent(
	    		JTree theTree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row){

	        if(value!=null && value instanceof MenuItemTreeNode){
	            
	            //the node that will be edited
	        	MenuItemTreeNode node=(MenuItemTreeNode)value;
	            
	            //the icon associated with the node
	           	ImageIcon editingIcon=getIcon(node);
	            
	            //setting the new values for the component
	            iconLabel.setIcon(editingIcon);
	            textField.setText(node.getLabel());
	        }

	    	return labelAndTextField;
	    }
	 
	    /**
	     * returning the icon corresponding to the given node
	     * @param node a node
	     * @return the icon corresponding to the given node
	     */
	    protected ImageIcon getIcon(MenuItemTreeNode node){
	        
	        ImageIcon currentIcon=null;
	        
	        if(node!=null){
	            
	            currentIcon=node.getIcon();
	        }
	        
	        return currentIcon;
	    }
	}
	
	/**
	 * the class of the source chooser
	 * @author ITRIS, Jordi SUC
	 */
	protected class TreeAnimationChooserJWidgetSourceChooser 
						extends AnimationChooserJWidgetSourceChooser{

		/**
		 * the tree
		 */
		protected JTree tree;
		
		/**
		 * the tree model
		 */
		protected MenuItemTreeModel treeModel;
		
		/**
		 * the tree selection listener
		 */
		protected TreeSelectionListener treeSelectionListener;
		
		/**
		 * the constructor of the class
		 */
		protected TreeAnimationChooserJWidgetSourceChooser() {
			
			buildWidget();
		}
		
		@Override
		protected void buildWidget() {
			
			String sourceLabel="";
			
			try {
				sourceLabel=bundle.getString("nodeChooserLabel");
			}catch (Exception ex) {}
			
			//the source label
			JLabel sourceLbl=new JLabel(sourceLabel);
			sourceLbl.setBorder(new EmptyBorder(2, 2, 2, 2));

			//creating the tree
			tree=new JTree();
			tree.setRootVisible(true);
			tree.setShowsRootHandles(true);
	        ToolTipManager.sharedInstance().registerComponent(tree);
			
			//the model
			treeModel=new MenuItemTreeModel();
			tree.setModel(treeModel);
			
			//the tree cell renderer
			MenuItemTreeCellRenderer renderer=new MenuItemTreeCellRenderer(tree);
			tree.setCellRenderer(renderer);
			
			//the selection model
			DefaultTreeSelectionModel selectionModel=new DefaultTreeSelectionModel();
			selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			tree.setSelectionModel(selectionModel);

			//adding the listener to the tree selections
			treeSelectionListener=new TreeSelectionListener() {

				public void valueChanged(TreeSelectionEvent evt) {
					
					//getting the selected node
					if(evt.getPath()!=null) {
						
						MenuItemTreeNode selectedNode=(MenuItemTreeNode)evt.getPath().getLastPathComponent();
						
						//getting the source id of the selected node
						String sourceId=getSourceId(selectedNode.getJWidgetElement());
						
						//setting the new source to edit
						setSource(sourceId);
					}
				}
			};
			
			//building the component
			JScrollPane scrollPane=new JScrollPane(tree);
			scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(25, 10));
			scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 25));
			scrollPane.setPreferredSize(new Dimension(80, 80));
			setLayout(new BorderLayout());
			add(sourceLbl, BorderLayout.NORTH);
			add(scrollPane, BorderLayout.CENTER);
		}
		
		/**
		 * returns the source id
		 * @param node a jwidget element
		 * @return the source id of the given jwidget element
		 */
		protected String getSourceId(Element node) {
			
			String sourceId="";
			
			if(node!=null) {
				
				//building the path of the given element
				Element element=node;
				
				while(fr.itris.glips.library.Toolkit.isJWidgetElement(element)) {
					
					sourceId=sourceSeparator+element.getAttribute(
						fr.itris.glips.library.Toolkit.idAttribute)+sourceId;
					element=(Element)element.getParentNode();
				}
			}

			return sourceId;
		}
		
		/**
		 * returns the jwidget element corresponding to the given source id
		 * @param rootNode the root tree node
		 * @param sourceId the source id
		 * @return the jwidget element corresponding to the given source id
		 */
		protected TreePath getTreePathFromSourceId(MenuItemTreeNode rootNode, String sourceId) {
			
			//checking if the source id is not empty
			//if the source is an empty string, the id of the root not is taken
			if(sourceId==null || sourceId.equals("")) {
				
				sourceId=rootNode.getNodeId();
			}

			//creating the list of the ids of the given source id
			LinkedList<String> ids=new LinkedList<String>();
			Scanner scanner=new Scanner(sourceId);
			scanner.useDelimiter(sourceSeparator);
			String str="";
			
			while(scanner.hasNext()) {
				
				str=scanner.next();
				
				if(! str.equals("")) {
					
					ids.add(str);
				}
			}
			
			//gets the node corresponding to each id in the tree
			MenuItemTreeNode treeNode=rootNode;
			LinkedList<TreeNode> treeNodes=new LinkedList<TreeNode>();
			treeNodes.add(rootNode);
			String nodeId="";
			
			for(int i=1; i<ids.size(); i++) {
				
				nodeId=ids.get(i);
				treeNode=getTreeNodeFromId(treeNode, nodeId);
				
				if(treeNode!=null) {
					
					treeNodes.add(treeNode);
				}
			}
			
			//returning the tree path
			return new TreePath(treeNodes.toArray());
		}
		
		/**
		 * returns the tree node corresponding to the given id and that is a child of the given parent node
		 * @param treeNode the parent tree node
		 * @param elementId the id of the element to be found
		 * @return the tree node corresponding to the given id and that is a child of the given parent node
		 */
		protected MenuItemTreeNode getTreeNodeFromId(MenuItemTreeNode treeNode, String elementId) {

			try{
				for(	MenuItemTreeNode node=(MenuItemTreeNode)treeNode.getFirstChild(); 
				node!=null; 
				node=(MenuItemTreeNode)node.getNextSibling()) {
			
					if(elementId.equals(node.getNodeId())) {
		
						return node;
					}
				}
			}catch (Exception ex){}

			return null;
		}
		
		@Override
		protected void updateWidgets() {
			
			tree.removeTreeSelectionListener(treeSelectionListener);
			
			//setting the new root element for the tree
			treeModel.setCurrentTreeModel(tree, getJwidgetElement());
			
			//initializing the component
			if(getJwidgetElement()!=null) {

				//setting the new source for the chooser
				if(RtdaAnimationsAndActionsModule.getStateRecord().getSourceName().equals("")) {
					
					setSource(getSourceId(getJwidgetElement()));
					
					//selecting the default items
					tree.setSelectionPath(new TreePath(getJwidgetElement()));
					
				}else{
	
					String source=RtdaAnimationsAndActionsModule.getStateRecord().getSourceName();
					setSource(source);

					//getting the tree path corresponding to the source id
					TreePath treePath=getTreePathFromSourceId((MenuItemTreeNode)treeModel.getRoot(), source);
					
					//sets the new selection for the tree
					tree.setSelectionPath(treePath);
				}
			}
			
			tree.addTreeSelectionListener(treeSelectionListener);
		}

		@Override
		public void clean() {
			
			super.clean();
			updateWidgets();
		}
	}
	
}
