package fr.itris.glips.rtdaeditor.anim.components;

import javax.swing.*;
import javax.swing.event.*;
import java .awt.*;
import java.util.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import javax.swing.border.*;
import org.w3c.dom.*;
import java.awt.event.*;

/**
 * the class of the chooser of an animation among a list
 * @author ITRIS, Jordi SUC
 */
public class AnimationsChooser extends JPanel{

	/**
	 * the jwidget manager
	 */
	private JWidgetManager jwidgetManager;
	
	/**
	 * the handlers of the animations and actions creation menu, the first one handles the jwidget elements
	 * and the second one handles the svg elements
	 */
	private AnimationsAndActionsMenu animationsAndActionsMenu=null, 
		svgAnimationsAndActionsMenu=null;
	
	/**
	 * the animation list chooser
	 */
	private JList animationList=null;
	
	/**
	 * the list of the animation items
	 */
	private LinkedList<AnimationListItem> listItems=new LinkedList<AnimationListItem>();
	
	/**
	 * the animation list chooser scrollpane
	 */
	private JScrollPane listScrollPane=null;
	
	/**
	 * the list model
	 */
	private final DefaultListModel listModel=new DefaultListModel();
	
	/**
	 * the selected element
	 */
	private Element selectedElement;
	
	/**
	 * the source chooser panel
	 */
	private JPanel sourceChooserPanel=new JPanel();
	
	/**
	 * whether the currently selected node defines a jwidget component
	 */
	private boolean isEditingJWidgetComponent=false;
	
	/**
	 * whether this chooser is an animations chooser
	 */
	private boolean isAnimations=false;
	
	/**
	 * the icons for the buttons
	 */
	protected static ImageIcon	newIcon=ResourcesManager.getIcon("New", false),
												deleteIcon=ResourcesManager.getIcon("Delete", false),
												ddeleteIcon=ResourcesManager.getIcon("Delete", true),
												upButtonIcon=ResourcesManager.getIcon("ArrowUp", false),
												dupButtonIcon=ResourcesManager.getIcon("ArrowUp", true),
												downButtonIcon=ResourcesManager.getIcon("ArrowDown", false),
												ddownButtonIcon=ResourcesManager.getIcon("ArrowDown", true);
	
	/**
	 * the list selection listener
	 */
	private final ListSelectionListener listSelectionListener=new ListSelectionListener(){
		
		public void valueChanged(ListSelectionEvent evt) {
			
			handleListSelection();
		}
	};
	
	/**
	 * the buttons used in the list panel
	 */
	private JButton newButton, deleteButton, upButton, downButton;
	
	/**
	 * the animation panel 
	 */
	private AnimationPanel animationPanel;
	
	/**
	 * the current animation objects list
	 */
	private LinkedList<AnimationObject> currentAnimations=new LinkedList<AnimationObject>(), 
			allCurrentAnimations=new LinkedList<AnimationObject>();
	
	/**
	 * the constructor of the class
	 * @param animationPanel the animation panel
	 * @param isAnimations whether this chooser is used for handling animations or actions
	 * @param jwidgetManager the jwidget manager
	 */
	public AnimationsChooser(AnimationPanel animationPanel, boolean isAnimations, 
			JWidgetManager jwidgetManager){
		
		this.isAnimations=isAnimations;
		this.jwidgetManager=jwidgetManager;
		this.animationPanel=animationPanel;
		
		//creating the menu handlers
		animationsAndActionsMenu=new AnimationsAndActionsMenu(isAnimations);
		svgAnimationsAndActionsMenu=new AnimationsAndActionsMenu(isAnimations);
		
		buildPanel();
	}
	
	/**
	 * cleans the panel
	 */
	public void clean() {
		
		listModel.removeAllElements();
		selectedElement=null;
		currentAnimations.clear();
		allCurrentAnimations.clear();
		animationsAndActionsMenu.clean(true);
		svgAnimationsAndActionsMenu.clean(false);
	}
	
	/**
	 * builds this panel
	 */
	protected void buildPanel(){
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(150, 277));

		add(sourceChooserPanel, BorderLayout.NORTH);
		
		//the list 
		animationList=new JList(listModel);
		animationList.setBorder(new EmptyBorder(0, 0, 0, 0));
		listScrollPane=new JScrollPane(animationList);
		animationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		animationList.addListSelectionListener(listSelectionListener);
		
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
		
		newButton.setMargin(insets);
		newButton.setToolTipText(newLabel);
		deleteButton.setMargin(insets);
		deleteButton.setToolTipText(deleteLabel);
		upButton.setMargin(insets);
		downButton.setMargin(insets);
		
		JPanel buttons=new JPanel();
		buttons.setBorder(new EmptyBorder(2, 2, 2, 2));
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 0));
		buttons.add(newButton);
		buttons.add(deleteButton);
		buttons.add(upButton);
		buttons.add(downButton);
		
		deleteButton.setEnabled(false);
		upButton.setEnabled(false);
		downButton.setEnabled(false);
		
		//adding the listeners to the buttons
		newButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

                final Point mousePosition=newButton.getMousePosition();
                
                if(mousePosition!=null){

                	SwingUtilities.invokeLater(new Runnable(){
                		
                		public void run() {

                			showPopup(newButton, mousePosition);
                		}
                	});
                }
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

               //getting the current animation
				int selectedIndex=animationList.getSelectedIndex();
				deleteListItem(selectedIndex);
			}
		});
		
		//adding the listener to the up and down buttons
		ActionListener upDownListener=new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				//getting the currently selected element
				int selectedIndex=animationList.getSelectedIndex();
				
				if(animationList.getSelectedValue()!=null && 
						animationList.getSelectedValue() instanceof AnimationListItem) {
					
					if(e.getSource().equals(upButton)) {
						
						raiseItem(selectedIndex);

					}else if(e.getSource().equals(downButton)) {
						
						lowerItem(selectedIndex);
					}
				}
			}
		};
		
		upButton.addActionListener(upDownListener);
		downButton.addActionListener(upDownListener);
		
		//adding a listener to the popup actions on the list
		MouseListener listListener=new MouseAdapter(){
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(isPopUp(e)){

					showPopup(animationList, e.getPoint());
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
		
		animationList.addMouseListener(listListener);
		
		//filling this panel
		add(listScrollPane, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
		
		sourceChooserPanel.setLayout(
			new BoxLayout(sourceChooserPanel, BoxLayout.X_AXIS));
	}
	
	/**
	 * sets the new element to be the one whose animations are handled
	 * @param list a list of elements
	 */
	public void setAnimations(LinkedList<AnimationObject> list){
		
		boolean popupMenuCreated=false;
		
		//clearing the list of the previous animation items
		listItems.clear();
		
		//getting the list of the selected nodes
		SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		LinkedList<Element> selectedNodes=
				new LinkedList<Element>(handle.getSelection().getSelectedElements());

		if(selectedNodes.size()>=1) {
			
			selectedElement=selectedNodes.getFirst();
			
		}else{
			
			selectedElement=handle.getCanvas().getDocument().getDocumentElement();
		}

		currentAnimations.clear();
		allCurrentAnimations.clear();
		sourceChooserPanel.removeAll();
		revalidate();
		
		//getting the jwidget element if it exists
		Element jwidgetElement=null;
		JWidgetEdition jwidgetEdition=null;

		if(selectedElement!=null && 
			Toolkit.hasJWidgetChildElement(selectedElement)) {

			isEditingJWidgetComponent=true;
			jwidgetElement=JWidgetManager.getJWidgetElement(selectedElement);

			if(jwidgetElement!=null) {
				
				jwidgetEdition=jwidgetManager.getJWidgetEdition(
						jwidgetElement.getAttribute(fr.itris.glips.library.Toolkit.nameAttribute));
			}
			
		}else {
			
			isEditingJWidgetComponent=false;
		}

		if(list!=null && list.size()>0){

			allCurrentAnimations.addAll(list);
			
			if(jwidgetElement!=null && jwidgetEdition!=null && 
					jwidgetEdition.containsInnerComponents()) {

				handleSourceWidget(jwidgetEdition, jwidgetElement);

			}else {

				currentAnimations.addAll(list);
				handleList();
			}

		}else{

			animationList.removeListSelectionListener(listSelectionListener);
			listModel.removeAllElements();
			animationList.addListSelectionListener(listSelectionListener);
			handleListSelection();
			
			if(jwidgetElement!=null && jwidgetEdition!=null && 
					jwidgetEdition.containsInnerComponents()) {

				handleSourceWidget(jwidgetEdition, jwidgetElement);

			}
		}
		
		if(jwidgetElement!=null && jwidgetEdition!=null) {

			Document specDocument=
				jwidgetEdition.getAnimationsAndActionsDocument();
			
			animationsAndActionsMenu.createPopUpMenu(
					specDocument, jwidgetEdition.getBundle(), selectedElement);
			popupMenuCreated=true;
		}
		
		if(! popupMenuCreated){
			
			if(selectedElement.equals(
					handle.getCanvas().getDocument().getDocumentElement())){
				
				//creating the popup menu with the tag event animations and actions document
				svgAnimationsAndActionsMenu.createPopUpMenu(
					ItemObject.getTagEventRtdaAnimationsAndActionsDocument(), 
						null, selectedElement);
				
			}else{
				
				//creating the popup menu with the regular animations and actions document
				svgAnimationsAndActionsMenu.createPopUpMenu(
					ItemObject.getRegularRtdaAnimationsAndActionsDocument(), 
						null, selectedElement);
			}
		}
	}
	
	/**
	 * setting the current jwidget
	 *@param jwidgetEdition a jwidget edition object
	 *@param jwidgetElement a jwidget element
	 */
	public void handleSourceWidget(JWidgetEdition jwidgetEdition, Element jwidgetElement) {

		//adding the new jwidget source chooser
		AnimationChooserJWidgetSourceChooser sourceChooser=
			jwidgetEdition.getSourceChooser(isAnimations);
		
		if(sourceChooser!=null){
			
			sourceChooser.setInitialSource(this, jwidgetElement);
			sourceChooserPanel.add(sourceChooser);
			revalidate();
		}
	}
	
	/**
	 * sets the new source name to be edited
	 * @param sourceName the name of a source
	 */
	public void setNewSource(String sourceName) {

		currentAnimations.clear();
		animationsAndActionsMenu.setCurrentSourceName(sourceName);
		RtdaAnimationsAndActionsModule.getStateRecord().setSourceName(sourceName);
		
		if(sourceName!=null && ! sourceName.equals("")) {
			
			//creating the list of the animations or actions corresponding to the given source name
			LinkedList<AnimationObject> animationObjects=new LinkedList<AnimationObject>();
			
			for(AnimationObject animationObject : allCurrentAnimations) {
				
				if(sourceName.equals(animationObject.getJWidgetSource())) {
					
					animationObjects.add(animationObject);
				}
			}
			
			//setting the new list of the animations object
			currentAnimations.addAll(animationObjects);
			handleList();
			
		}else{

			handleListSelection();
		}
	}
	
	/**
	 * handles the list state for the current element
	 */
	protected void handleList(){

		animationList.removeListSelectionListener(listSelectionListener);
		listModel.removeAllElements();
		animationList.addListSelectionListener(listSelectionListener);
		
		if(currentAnimations.size()>0){

			animationList.removeListSelectionListener(listSelectionListener);
			
			for(AnimationObject animationObject : currentAnimations){

				listModel.addElement(new AnimationListItem(animationObject));
			}
			
			animationList.addListSelectionListener(listSelectionListener);
			
			int animationIndex=0;
			
			//setting the selected animation
			if(isAnimations) {

				animationIndex=RtdaAnimationsAndActionsModule.
					getStateRecord().getAnimationIndex();
				
			}else {

				animationIndex=RtdaAnimationsAndActionsModule.
					getStateRecord().getActionIndex();
			}
			
			if(animationIndex<0 || animationIndex>=currentAnimations.size()){
				
				animationIndex=0;
			}
			
			animationList.setSelectedIndex(animationIndex);
			animationPanel.setCurrentAnimation(
					currentAnimations.get(animationIndex));

		}else {

			animationPanel.setCurrentAnimation(null);
		}
	}
	
	/**
	 * handles the list selection changes
	 */
	protected void handleListSelection(){
		
		//storing the currently selected index
		if(isAnimations) {

			RtdaAnimationsAndActionsModule.getStateRecord().
				setAnimationIndex(animationList.getSelectedIndex());
			
		}else {

			RtdaAnimationsAndActionsModule.getStateRecord().
				setActionIndex(animationList.getSelectedIndex());
		}

		AnimationListItem currentSelection=
			(AnimationListItem)animationList.getSelectedValue();

		//disabling all the buttons
		deleteButton.setEnabled(false);
		upButton.setEnabled(false);
		downButton.setEnabled(false);
		
		if(currentSelection!=null){
			
			//setting the new animation to be displayed
			animationPanel.setCurrentAnimation(currentSelection.getAnimationObject());
			
			//handling the delete button state
			deleteButton.setEnabled(true);
			
			//handling the up and down buttons
			if(listModel.getSize()>1){
				
				if(animationList.getSelectedIndex()!=0){
					
					upButton.setEnabled(true);
				}
				
				if(animationList.getSelectedIndex()!=listModel.getSize()-1){
					
					downButton.setEnabled(true);
				}
			}

		}else{

			animationPanel.setCurrentAnimation(null);
		}
		
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {

				animationList.repaint();				
			}
		});
	}

	/**
	 * @return the animationsAndActionsMenu
	 */
	public AnimationsAndActionsMenu getAnimationsAndActionsMenu() {
		
		if(isEditingJWidgetComponent) {
			
			return animationsAndActionsMenu;
		}

		return svgAnimationsAndActionsMenu;
	}
	
	/**
	 * shows the popup create or execute action on the list items
	 * @param sourceComponent the source component where the action occured
	 * @param clickedPoint the clicked point
	 */
	protected void showPopup(
		JComponent sourceComponent, Point clickedPoint){//TODO
		
    	getAnimationsAndActionsMenu().showPopupMenu(
    			sourceComponent, clickedPoint);
	}
	
	/**
	 * deletes the list item denoted by the provided index
	 * @param index a list item index
	 */
	protected void deleteListItem(int index){//TODO
		
		if(index>=0) {
			
			AnimationListItem item=(AnimationListItem)
				animationList.getModel().getElementAt(index);
			
			if(item!=null) {
				
				AnimationObject animationObject=item.getAnimationObject();
				
				if(animationObject!=null) {
					
					//removing this animation object
					final SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
					final Element animationElement=animationObject.getAnimationElement();
					final Element parentElement=(Element)animationElement.getParentNode();
					Element sibling=null;
					
					//getting the animation that can be found after the selected animation
					AnimationListItem nextItem=null;
					
					if(index+1<animationList.getModel().getSize()) {
						
						nextItem=(AnimationListItem)animationList.getModel().getElementAt(index+1);
						
						if(nextItem!=null) {
							
							sibling=nextItem.getAnimationObject().getAnimationElement();
						}
					}

					final Element nextSibling=sibling;
					
					//the runnable used to execute the action
					Runnable executeRunnable=new Runnable() {

						public void run() {
							
							parentElement.removeChild(animationElement);
							handle.getSvgDOMListenerManager().
								fireNodeRemoved(parentElement, animationElement);
							handle.getSelection().handleSelection(parentElement, false, true);
						}
					};
					
					//the undo runnable
					Runnable undoRunnable=new Runnable() {

						public void run() {
							
							if(nextSibling!=null) {
								
								parentElement.insertBefore(animationElement, nextSibling);
								
							}else {
								
								parentElement.appendChild(animationElement);
							}

							handle.getSvgDOMListenerManager().
								fireNodeInserted(parentElement, animationElement);
							handle.getSelection().handleSelection(parentElement, false, true);
						}
					};
					
					//adding the undo/redo action
					getAnimationsAndActionsMenu().addUndoRedoAction(
							executeRunnable, undoRunnable, executeRunnable);
				}
			}
		}
	}
	
	/**
	 * raises the item at the procided index
	 * @param index the index of an item
	 */
	protected void raiseItem(int index){//TODO
		
		final SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		AnimationListItem listItem=(AnimationListItem)animationList.getSelectedValue();
		final Element currentElement=listItem.getAnimationObject().getAnimationElement();
		final Element parentElement=(Element)currentElement.getParentNode();
		Element nextElement=null;
		
		//getting the next element
		if(index+1<animationList.getModel().getSize()) {
			
			AnimationListItem nextListItem=(AnimationListItem)
				animationList.getModel().getElementAt(index+1);
			
			if(nextListItem!=null) {

				nextElement=nextListItem.getAnimationObject().getAnimationElement();
			}
		}
		
		final Element fnextElement=nextElement;
		Runnable executeRunnable=null, undoRunnable=null;
		
		Element previousElement=null;
		
		//getting the previous element
		if(index-1>=0) {
			
			AnimationListItem previousListItem=
				(AnimationListItem)animationList.getModel().getElementAt(index-1);
			
			if(previousListItem!=null) {

				previousElement=previousListItem.getAnimationObject().getAnimationElement();
			}
		}

		if(previousElement!=null) {
			
			final Element fpreviousElement=previousElement;
			
			//the runnable used to execute the action
			executeRunnable=new Runnable() {

				public void run() {
					
					//putting the current element before the previous element
					parentElement.insertBefore(currentElement, fpreviousElement);
					handle.getSvgDOMListenerManager().
						fireNodeInserted(parentElement, currentElement);
					handle.getSelection().handleSelection(parentElement, false, true);
				}
			};
			
			//the undo runnable
			undoRunnable=new Runnable() {

				public void run() {
					
					if(fnextElement!=null) {
						
						parentElement.insertBefore(currentElement, fnextElement);
						
					}else {
						
						parentElement.appendChild(currentElement);
					}

					handle.getSvgDOMListenerManager().
						fireNodeInserted(parentElement, currentElement);
					handle.getSelection().handleSelection(parentElement, false, true);
				}
			};
			
			//adding the undo/redo action
			getAnimationsAndActionsMenu().addUndoRedoAction(
					executeRunnable, undoRunnable, executeRunnable);
		}
	}
	
	/**
	 * lowers the item at the procided index
	 * @param index the index of an item
	 */
	protected void lowerItem(int index){//TODO
		
		final SVGHandle handle=Editor.getEditor().getHandlesManager().getCurrentHandle();
		AnimationListItem listItem=(AnimationListItem)animationList.getSelectedValue();
		final Element currentElement=listItem.getAnimationObject().getAnimationElement();
		final Element parentElement=(Element)currentElement.getParentNode();
		Element nextElement=null;
		
		//getting the next element
		if(index+1<animationList.getModel().getSize()) {
			
			AnimationListItem nextListItem=(AnimationListItem)
				animationList.getModel().getElementAt(index+1);
			
			if(nextListItem!=null) {

				nextElement=nextListItem.getAnimationObject().getAnimationElement();
			}
		}
		
		final Element fnextElement=nextElement;
		Runnable executeRunnable=null, undoRunnable=null;
		
		if(nextElement!=null) {
			
			//getting the next element of the next element
			Element nextNextElement=null;
			
			if(index+2<animationList.getModel().getSize()) {
				
				AnimationListItem nextNextListItem=
					(AnimationListItem)animationList.getModel().getElementAt(index+2);
				
				if(nextNextListItem!=null) {

					nextNextElement=
						nextNextListItem.getAnimationObject().getAnimationElement();
				}
			}

			final Element fnextNextElement=nextNextElement;
			
			//the runnable used to execute the action
			executeRunnable=new Runnable() {

				public void run() {
					
					if(fnextNextElement!=null) {
						
						//putting the current element before the next element of the next element
						parentElement.insertBefore(currentElement, fnextNextElement);
						
					}else {
						
						//putting the current element at the end of all the animation nodes
						parentElement.appendChild(currentElement);
					}

					handle.getSvgDOMListenerManager().
						fireNodeInserted(parentElement, currentElement);
					handle.getSelection().handleSelection(parentElement, false, true);
				}
			};
			
			//the undo runnable
			undoRunnable=new Runnable() {

				public void run() {
					
					//putting the current element before the next element
					parentElement.insertBefore(currentElement, fnextElement);
					handle.getSvgDOMListenerManager().
						fireNodeInserted(parentElement, currentElement);
					handle.getSelection().handleSelection(parentElement, false, true);
				}
			};
			
			//adding the undo/redo action
			getAnimationsAndActionsMenu().addUndoRedoAction(
					executeRunnable, undoRunnable, executeRunnable);
		}
	}

	/**
	 * the class of the list items
	 * @author ITRIS, Jordi SUC
	 */
	protected class AnimationListItem{
		
		/**
		 * the animation object
		 */
		private AnimationObject animationObject;
		
		/**
		 * the label for this animation
		 */
		private String label;
		
		/**
		 * the constructor of the class
		 * @param animationObject the animation object
		 */
		public AnimationListItem(AnimationObject animationObject){
			
			this.animationObject=animationObject;
			String name=animationObject.getName();
			
			try{
				label=animationObject.getLabel(
						"rtdaanim_"+name.substring(fr.itris.glips.library.Toolkit.rtdaPrefix.length(), name.length()));
			}catch (Exception ex){label=name;}
		}
		
		/**
		 * @return the animation object
		 */
		public AnimationObject getAnimationObject(){
			
			return animationObject;
		}
		
		@Override
		public String toString() {
			
			return label;
		}
	}
	
}
