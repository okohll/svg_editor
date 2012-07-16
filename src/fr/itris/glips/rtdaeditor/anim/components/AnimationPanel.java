package fr.itris.glips.rtdaeditor.anim.components;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.w3c.dom.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.toolkit.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the panel displaying the animations widgets
 * @author ITRIS, Jordi SUC
 */
public class AnimationPanel extends JPanel{

	/**
	 * the type of the handled objects
	 */
	private int objectType=ItemObject.ANIMATION;
	
	/**
	 * the panel containing all the widgets
	 */
	private JSplitPane allPanel=
		new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
	
	/**
	 * the right panel
	 */
	protected JPanel rightPanel=new JPanel();
	
	/**
	 * the jlabel for the no animations message
	 */
	private JLabel noAnimationMessageJLabel;
	
	/**
	 * the animations chooser panel
	 */
	private AnimationsChooser animationsChooserPanel=null;

	/**
	 * the properties panel
	 */
	private JPanel propertiesPanel=new JPanel();
	
	/**
	 * the panel of the widgets displaying information on the selected widget
	 */
	private JPanel childrenPanel=new JPanel();
	
	/**
	 * the properties table
	 */
	private AnimationTable propertiesTable=new AnimationTable(this, true);
	
	/**
	 * the children table
	 */
	private AnimationTable childrenTable=new AnimationTable(this, false);
	
	/**
	 * the labels
	 */
	private String propertiesLabel="", enumChildrenLabel="", 
		analogicChildrenLabel="", viewChildrenLabel="";
	
	/**
	 * the panel containing the buttons used to create or delete children
	 */
	private JPanel childrenButtonsPanel=new JPanel();
	
	/**
	 * whether the panel has been initialized
	 */
	private boolean initialized=false;
	
	/**
	 * the current animation object
	 */
	private AnimationObject currentAnimationObject;
	
	/**
	 * the jwidget manager
	 */
	private JWidgetManager jwidgetManager;
	
	/**
	 * the points painter
	 */
	private PointsPainter pointsPainter;
	
	/**
	 * the constructor of the class
	 * @param jwidgetManager the jwidget manager
	 * @param objectType the type of the objects that have to be handled
	 */
	public AnimationPanel(JWidgetManager jwidgetManager, int objectType){
		
		this.jwidgetManager=jwidgetManager;
		this.objectType=objectType;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		buildPanel();
		
		//creating the points painter that will paint some points 
		//defined by attributes on the canvas
		if(objectType==ItemObject.ANIMATION) {
			
			pointsPainter=new PointsPainter();
		}
	}

	/**
	 * stops all editing actions on the children table
	 */
	public void stopChildrenCellEditing() {
		
		childrenTable.getTableEditor().stopEditingCell();
	}
	
	/**
	 * stops all editing actions on the properties table
	 */
	public void stopPropertiesCellEditing() {
		
		propertiesTable.getTableEditor().stopEditingCell();
	}
	
	/**
	 * sets the animations objects that have to be handled
	 * @param animationObjects the list of the animation objects to be handled
	 */
	public void setAnimations(LinkedList<AnimationObject> animationObjects){

		removeAll();
		
		if(animationObjects!=null){
			
			add(allPanel);
			animationsChooserPanel.setAnimations(animationObjects);

		}else{

			propertiesTable.clean();
			childrenTable.clean();
			animationsChooserPanel.clean();
			currentAnimationObject=null;
		}
		
		if(pointsPainter!=null && 
			(animationObjects==null || animationObjects.size()==0)) {

			//setting the new animation object for the points painter
			pointsPainter.setCurrentAnimationObjet(null);
		}
	}
	
	/**
	 * sets the new animation object to be displayed
	 * @param animationObject an animation object
	 */
	public void setCurrentAnimation(final AnimationObject animationObject){

		currentAnimationObject=animationObject;
		rightPanel.removeAll();

		if(animationObject!=null){
			
			if(animationObject.hasChildren()){
				
				//setting the layout of the right panel and 
				//adding the properties and children panels
				GridBagLayout gridBagLayout=new GridBagLayout();
				rightPanel.setLayout(gridBagLayout);
				GridBagConstraints c=new GridBagConstraints();
				c.fill=GridBagConstraints.BOTH;
				c.weightx=2;
				c.weighty=2;
				
				c.gridwidth=GridBagConstraints.REMAINDER;
				gridBagLayout.setConstraints(propertiesPanel, c);
				rightPanel.add(propertiesPanel);

				gridBagLayout.setConstraints(childrenPanel, c);
				rightPanel.add(childrenPanel);

				//displays the child panel//
				if(! initialized){
					
					initialized=true;
				}
				
				if(animationObject.getType()==TagToolkit.ANALOGIC) {
					
					childrenButtonsPanel.setVisible(true);
					childrenTable.setRowSelectionAllowed(true);
					childrenPanel.setBorder(new TitledBorder(analogicChildrenLabel));
					
				}else if(animationObject.getType()==TagToolkit.NEED_CHILDREN) {
					
					//getting the label
					String nodeName=animationObject.getSpecChildElement().getAttribute("name");
					
					try{
						nodeName=nodeName.substring(Toolkit.rtdaPrefix.length(), nodeName.length());
					}catch (Exception ex){}
					
					String needChildrenLabel=ResourcesManager.bundle.getString("rtdaanim_"+nodeName);
					
					childrenButtonsPanel.setVisible(true);
					childrenTable.setRowSelectionAllowed(true);
					childrenPanel.setBorder(new TitledBorder(needChildrenLabel));
					
				}else if(animationObject.getType()==TagToolkit.VIEW) {
					
					childrenButtonsPanel.setVisible(false);
					childrenTable.setRowSelectionAllowed(false);
					childrenPanel.setBorder(new TitledBorder(viewChildrenLabel));
					
				}else {
					
					childrenButtonsPanel.setVisible(false);
					childrenTable.setRowSelectionAllowed(false);
					childrenPanel.setBorder(new TitledBorder(enumChildrenLabel));
				}
				
				childrenTable.getSelectionModel().setSelectionInterval(-1, -1);
				
				SwingUtilities.invokeLater(new Runnable() {
					
					public void run() {
					
						//setting the new animation object for the children table
						childrenTable.setAnimationObject(animationObject);
					}
				});

			}else{
				
				rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
				rightPanel.add(propertiesPanel);
			}
		
			propertiesTable.setAnimationObject(animationObject);
			
		}else {
			
			rightPanel.setLayout(new BorderLayout());
			rightPanel.add(noAnimationMessageJLabel, BorderLayout.CENTER);
		}

		if(pointsPainter!=null) {

			//setting the new animation object for the points painter
			pointsPainter.setCurrentAnimationObjet(animationObject);
		}

		rightPanel.revalidate();
		rightPanel.repaint();
		allPanel.revalidate();
	}
	
	/**
	 * @return the points painter
	 */
	public PointsPainter getPointsPainter() {
		
		return pointsPainter;
	}
	
	/**
	 * builds the panel displaying the animation widgets
	 */
	protected void buildPanel(){
		
		//creating the animations chooser
		animationsChooserPanel=new AnimationsChooser(
				this, objectType==ItemObject.ANIMATION?true:false, jwidgetManager);

		//getting the labels
		String noAnimationMessage="";
		
		try{
			
			if(objectType==ItemObject.ANIMATION){
				
				noAnimationMessage=ResourcesManager.bundle.getString("label_nortdaanimations");
				
			}else{
				
				noAnimationMessage=ResourcesManager.bundle.getString("label_nortdaactions");
			}

			propertiesLabel=ResourcesManager.bundle.getString("rtdaanim_propertyLabel");
			enumChildrenLabel=ResourcesManager.bundle.getString("rtdaanim_enumChildrenLabel");
			analogicChildrenLabel=ResourcesManager.bundle.getString("rtdaanim_analogicChildrenLabel");
			viewChildrenLabel=ResourcesManager.bundle.getString("rtdaanim_viewChildrenLabel");
		}catch (Exception ex){}
		
		noAnimationMessageJLabel=new JLabel(noAnimationMessage);
		noAnimationMessageJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		//handling the panel containing all the other widgets
		allPanel.setDividerLocation(130);
		allPanel.setDividerSize(3);
		allPanel.setOneTouchExpandable(false);
		allPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		//handling the right panel
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
		
		//adding the animations chooser panel
		allPanel.setLeftComponent(animationsChooserPanel);
		
		//the children panel
		childrenPanel.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollpane=new JScrollPane(childrenTable);
		scrollpane.getViewport().setBackground(childrenTable.getBackground());
		scrollpane.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollpane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		childrenPanel.add(scrollpane, BorderLayout.CENTER);
		
		//creating the buttons panel for the children table
		createChildrenButtonsPanel();
		childrenPanel.add(childrenButtonsPanel, BorderLayout.SOUTH);

		//creating the properties panel
		propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.X_AXIS));
		propertiesPanel.setBorder(new TitledBorder(propertiesLabel));
		
		scrollpane=new JScrollPane(propertiesTable);
		scrollpane.getViewport().setBackground(propertiesTable.getBackground());
		scrollpane.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollpane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		propertiesPanel.add(scrollpane, BorderLayout.CENTER);
		
		allPanel.setRightComponent(rightPanel);
		allPanel.setPreferredSize(new Dimension(640, 340));
	}
	
	/**
	 * creates the children buttons panel
	 */
	protected void createChildrenButtonsPanel() {

		//getting the icons
		ImageIcon	newIcon=ResourcesManager.getIcon("New", false),
		dnewIcon=ResourcesManager.getIcon("New", true),
		deleteIcon=ResourcesManager.getIcon("Delete", false),
		ddeleteIcon=ResourcesManager.getIcon("Delete", true);
		
		//getting the labels for the tool tip
		String newLabel="", deleteLabel="";
		
		try {
			newLabel=ResourcesManager.bundle.getString("labelnew");
			deleteLabel=ResourcesManager.bundle.getString("labeldelete");
		}catch (Exception ex) {}
		
		//creating the buttons
		final JButton newButton=new JButton(newIcon);
		newButton.setDisabledIcon(dnewIcon);
		newButton.setToolTipText(newLabel);
		newButton.setMargin(new Insets(1, 1, 1, 1));
		
		final JButton deleteButton=new JButton(deleteIcon);
		deleteButton.setDisabledIcon(ddeleteIcon);
		deleteButton.setToolTipText(deleteLabel);
		deleteButton.setMargin(new Insets(1, 1, 1, 1));
		
		//creating the listeners
		newButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				//creating a new child for the current animation object
				if(currentAnimationObject!=null) {
					
			        //creating the child dom element and appending it to the animation element
					final Element animationElement=currentAnimationObject.getAnimationElement();
					Document doc=animationElement.getOwnerDocument();
					Element specChildElement=currentAnimationObject.getSpecChildElement();
					final Element newChild=doc.createElementNS(animationElement.getNamespaceURI(), 
							specChildElement.getAttribute("name"));
					final SVGHandle handle=currentAnimationObject.getSVGHandle();
					
					//getting the currently selected element
					Set<Element> selectedElements=
						handle.getSelection().getSelectedElements();
					final Element selectedElement=selectedElements.iterator().next();
					
					//setting the default value of each attribute
					String name, defaultValue;
					Element el=null;
					
					for(Node node=specChildElement.getFirstChild(); 
						node!=null; node=node.getNextSibling()){
						
						if(node instanceof Element){
							
							el=(Element)node;
							name=el.getAttribute("name");
							defaultValue=el.getAttribute("defaultValue");
							
							if(defaultValue!=null && ! defaultValue.equals("")){
								
								newChild.setAttribute(name, defaultValue);
							}
						}
					}
					
			        Runnable executeRunnable=new Runnable() {
			        	
				        public void run() {

					        animationElement.appendChild(newChild);
					        handle.getSvgDOMListenerManager().
					        	fireStructureChanged(animationElement, animationElement);
							handle.getSelection().handleSelection(selectedElement, false, true);
				        }
			        };
			        
			        Runnable undoRunnable=new Runnable(){

			        	public void run() {
			        		
					        animationElement.removeChild(newChild);
					        handle.getSvgDOMListenerManager().
					        	fireStructureChanged(animationElement, animationElement);
					        handle.getSelection().handleSelection(selectedElement, false, true);
			        	}
			        };

			        currentAnimationObject.addUndoRedoAction(
			        	executeRunnable, undoRunnable, executeRunnable);
				}
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				if(currentAnimationObject!=null) {

					int row=childrenTable.getSelectedRow();

					if(row!=-1) {
						
						//getting the node that can be found after this node
						int nextRow=row+1;
						Element nextElement=null;
						
						if(nextRow<currentAnimationObject.getChildrenList().size()) {
							
							nextElement=currentAnimationObject.getChildrenList().
								get(nextRow).getChildAnimationElement();
						}
						
						final Element afterElement=nextElement;
						final Element removedElement=
							currentAnimationObject.getChildrenList().get(row).getChildAnimationElement();
						final Element animationElement=currentAnimationObject.getAnimationElement();
						final SVGHandle handle=currentAnimationObject.getSVGHandle();
						
						//getting the currently selected element
						Set<Element> selectedElements=
							handle.getSelection().getSelectedElements();
						final Element selectedElement=selectedElements.iterator().next();
						
						if(removedElement!=null) {
							
							Runnable executeRunnable=new Runnable() {

								public void run() {

									animationElement.removeChild(removedElement);
							        handle.getSvgDOMListenerManager().
							        	fireStructureChanged(animationElement, animationElement);
							        handle.getSelection().handleSelection(selectedElement, false, true);
								}
							};
							
							Runnable undoRunnable=new Runnable() {

								public void run() {

									if(afterElement!=null) {
										
										animationElement.insertBefore(removedElement, afterElement);
										
									}else {
										
										animationElement.appendChild(removedElement);
									}
									
							        handle.getSvgDOMListenerManager().
							        	fireStructureChanged(animationElement, animationElement);
							        handle.getSelection().handleSelection(selectedElement, false, true);
								}
							};
							
							currentAnimationObject.addUndoRedoAction(
									executeRunnable, undoRunnable, executeRunnable);
						}
					}
				}
			}
		});
		
		//adding a listener to the selection changes on the children table
		childrenTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent evt) {

				deleteButton.setEnabled(childrenTable.getSelectedRow()!=-1);
			}
		});
		
		//handling the children buttons panel
		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 0));
		
		buttonsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		buttonsPanel.add(newButton);
		buttonsPanel.add(deleteButton);
		
		childrenButtonsPanel.setLayout(new BorderLayout(0, 0));
		childrenButtonsPanel.add(new JSeparator(), BorderLayout.NORTH);
		childrenButtonsPanel.add(buttonsPanel, BorderLayout.CENTER);
	}
}
