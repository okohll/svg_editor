package fr.itris.glips.svgeditor.actions.dom;

import java.awt.event.*;
import java.awt.geom.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.popup.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.shape.*;

/**
 * the superclass of all action modules
 * @author Jordi SUC
 */
public abstract class DomActionsModule extends ModuleAdapter {
	
	/**
	 * the id of the module
	 */
	protected String moduleId="";
	
	/**
	 * the labels
	 */
	protected String moduleLabel="";
	
	/**
	 * the module icon
	 */
	protected Icon moduleIcon;
	
	/**
	 * the menu
	 */
	protected JMenu moduleMenu;
	
	/**
	 * the types of the align actions
	 */
	protected int[] actionsTypes;
	
	/**
	 * the ids of the align actions
	 */
	protected String[] actionsIds;
	
	/**
	 * the labels of the align actions
	 */
	protected String[] actionsLabels;
	
	/**
	 * the undo/redo labels of the align actions
	 */
	protected String[] undoRedoActionsLabels;
	
	/**
	 * the icons of the align actions
	 */
	protected Icon[] actionsIcons;
	
	/**
	 * the disabled icons of the align actions
	 */
	protected Icon[] actionsDisabledIcons;
	
	/**
	 * the disabled icons of the align actions
	 */
	protected JMenuItem[] actionsMenuItems;
	
	/**
	 * creates the menu items
	 */
	protected void createItems() {
		
		//getting the menu resources
		moduleLabel=ResourcesManager.bundle.getString(moduleId+"ItemLabel");
		moduleIcon=ResourcesManager.getIcon(moduleId, false);
		
		//creating the menu
		moduleMenu=new JMenu(moduleLabel);
		moduleMenu.setIcon(moduleIcon);
		
		//creating the listener to the menu items
		ActionListener listener=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {

				for(int i=0; i<actionsMenuItems.length; i++){
					
					if(e.getSource().equals(actionsMenuItems[i])){
						
						//getting the current handle
						SVGHandle handle=
							Editor.getEditor().getHandlesManager().getCurrentHandle();
						
						if(handle!=null){
							
							doAction(handle, new HashSet<Element>(
								handle.getSelection().getSelectedElements()), 
									actionsTypes[i], e);
						}

						break;
					}
				}
			}
		};
		
		//creating the arrays
		actionsLabels=new String[actionsIds.length];
		undoRedoActionsLabels=new String[actionsIds.length];
		actionsIcons=new Icon[actionsIds.length];
		actionsDisabledIcons=new Icon[actionsIds.length];
		actionsMenuItems=new JMenuItem[actionsIds.length];
		
		//filling the arrays
		for(int i=0; i<actionsIds.length; i++){
			
			//getting the labels
			actionsLabels[i]=
				ResourcesManager.bundle.getString(actionsIds[i]+"ItemLabel");
			undoRedoActionsLabels[i]=
				ResourcesManager.bundle.getString(actionsIds[i]+"UndoRedoLabel");
			
			//getting the icons
			actionsIcons[i]=
				ResourcesManager.getIcon(actionsIds[i], false);
			actionsDisabledIcons[i]=
				ResourcesManager.getIcon(actionsIds[i], true);
			
			//creating the menu item
			actionsMenuItems[i]=new JMenuItem(actionsLabels[i]);
			actionsMenuItems[i].setIcon(actionsIcons[i]);
			actionsMenuItems[i].setDisabledIcon(actionsDisabledIcons[i]);
			actionsMenuItems[i].setEnabled(false);
			
			//adding the listener
			actionsMenuItems[i].addActionListener(listener);
			
			//adding the menu item to the menu
			moduleMenu.add(actionsMenuItems[i]);
		}
	}
	
	@Override
	public void initialize() {

		//creating the listener to the property changes of the menu items
		PropertyChangeListener changeListener=new PropertyChangeListener(){
			
			public void propertyChange(PropertyChangeEvent evt) {

				updateMenuItems();
			}
		};
		
		moduleMenu.getParent().addPropertyChangeListener(changeListener);
	}
	
	/**
	 * updates all the menu items
	 */
	protected void updateMenuItems(){
		
		final SVGHandle handle=
			Editor.getEditor().getHandlesManager().getCurrentHandle();
		
		if(handle==null || handle.getSelection()==null || 
			(handle.getSelection()!=null && 
				handle.getSelection().isSelectionLocked())){
			
			setItemsEnabled(false);
			
		}else{
			
			//getting the selected elements
			Set<Element> selectedElements=
				handle.getSelection().getSelectedElements();
			handleItemsState(handle, selectedElements);
		}
	}
	
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> map=new HashMap<String, JMenuItem>();
		map.put(moduleId, moduleMenu);
		
		return map;
	}
	
	@Override
	public Collection<PopupItem> getPopupItems() {
		
		//creating the popup item for the menu
		PopupSubMenu subMenu=new PopupSubMenu(
			Editor.getEditor(), moduleId, moduleLabel, moduleId);
		
		//creating the popup items that will be added to the popup sub menu
		PopupItem popupItem=null;
		
		for(int i=0; i<actionsIds.length; i++){
			
			final int index=i;
			
			popupItem=new PopupItem(
					Editor.getEditor(), actionsIds[i], actionsLabels[i], actionsIds[i]){
				
				@Override
				public JMenuItem getPopupItem(LinkedList<Element> nodes) {
					
					menuItem.setEnabled(false);
					final Set<Element> elementsSet=new HashSet<Element>(nodes);
					
					//getting the current handle
					final SVGHandle currentHandle=
						Editor.getEditor().getHandlesManager().getCurrentHandle();
					
					if(currentHandle!=null && selectionCorrect(index, elementsSet)){
						
						//creating the listener to the menu item
						ActionListener actionListener=new ActionListener(){
							
							public void actionPerformed(ActionEvent evt) {
								
								doAction(currentHandle, elementsSet, index, evt);
							}
						};
						
						menuItem.addActionListener(actionListener);
						menuItem.setEnabled(true);
					}
					
					return super.getPopupItem(nodes);
				}
			};
			
			subMenu.addPopupItem(popupItem);
		}

		//the collection of the popup items to be returned
		Collection<PopupItem> popupItems=new HashSet<PopupItem>();
		popupItems.add(subMenu);
		
		return popupItems;
	}
	
	/**
	 * executes the action the module handles
	 * @param handle a svg handle
	 * @param elements a set of elements to be modified
	 * @param index the index of the action
	 * @param evt the event that triggered the action
	 */
	protected abstract void doAction(
		SVGHandle handle, Set<Element> elements, 
			int index, ActionEvent evt);
	
	/**
	 * handles the state of the items
	 * @param handle a svg handle
	 * @param selectedElements the set of the selected elements
	 */
	protected void handleItemsState(
			SVGHandle handle, Set<Element> selectedElements){

		for(int i=0; i<actionsMenuItems.length; i++){
			
			actionsMenuItems[i].setEnabled(
					selectionCorrect(i, selectedElements));
		}
	}
	
	/**
	 * whether the provided elements (that are the currently selected elements), denote
	 * a correct selection for the module
	 * @param index the index defining the type of the action
	 * @param elements  the set of the currently selected elements
	 * @return whether the provided elements denote
	 * a correct selection for the module
	 */
	protected abstract boolean selectionCorrect(int index, Set<Element> elements);
	
	/**
	 * whether the provided elements (that are the currently selected elements), denote
	 * a correct selection for the module, FIRST TYPE IMPLEMENTATION
	 * @param elements  the set of the currently selected elements
	 * @return whether the provided elements denote
	 * a correct selection for the module
	 */
	protected boolean selectionCorrectFirstType(Set<Element> elements){
		
		return elements!=null && elements.size()>=1;
	}
	
	/**
	 * whether the provided elements (that are the currently selected elements), denote
	 * a correct selection for the module, SECOND TYPE IMPLEMENTATION
	 * @param elements  the set of the currently selected elements
	 * @return whether the provided elements denote
	 * a correct selection for the module
	 */
	protected boolean selectionCorrectSecondType(Set<Element> elements){
		
		return elements!=null && elements.size()>=2;
	}
	
	/**
	 * whether the provided elements (that are the currently selected elements), denote
	 * a correct selection for the module, THIRD TYPE IMPLEMENTATION
	 * @param elements  the set of the currently selected elements
	 * @return whether the provided elements denote
	 * a correct selection for the module
	 */
	protected boolean selectionCorrectThirdType(Set<Element> elements){
		
		return elements!=null && elements.size()>=3;
	}
	
	/**
	 * applies the provided translation factors to the provided elements
	 * @param handle the current svg handle
	 * @param index the index of the type of the action
	 * @param elementsToTranslationFactors the map associating 
	 * an element to its translation factors
	 */
	protected void applyTranslateTransform(
			SVGHandle handle, int index, 
			Map<Element, Point2D> elementsToTranslationFactors){
		
		//applying the transform to the elements
		AbstractShape shapeModule=null;
		
		//the set of the undo/redo actions that are created for each element
		final Set<UndoRedoAction> undoRedoActions=
			new HashSet<UndoRedoAction>();
		UndoRedoAction undoRedoAction;
		Point2D translationFactors=null;
		Set<Element> elements=null;
		
		for(Element element : elementsToTranslationFactors.keySet()){
			
			shapeModule=ShapeToolkit.getShapeModule(element);
			
			if(shapeModule!=null){
				
				translationFactors=elementsToTranslationFactors.get(element);
				
				if(translationFactors!=null){
					
					elements=new HashSet<Element>();
					elements.add(element);
					
					undoRedoAction=shapeModule.translate(
							handle, elements, translationFactors);
					
					if(undoRedoAction!=null){
						
						undoRedoActions.add(undoRedoAction);
					}
				}
			}
		}
		
		if(undoRedoActions.size()>0){
			
			//creating the execute runnable
			Runnable executeRunnable=new Runnable(){
				
				public void run() {
			
					for(UndoRedoAction action : undoRedoActions){
						
						action.execute();
					}
				}
			};
			
			//creating the undo runnable
			Runnable undoRunnable=new Runnable(){
				
				public void run() {
			
					for(UndoRedoAction action : undoRedoActions){
						
						action.undo();
					}
				}
			};
			
			addUndoRedoAction(handle, index, 
				executeRunnable, undoRunnable, 
					elementsToTranslationFactors.keySet());
		}
	}
	
	/**
	 * applies the provided resize transform to the provided elements
	 * @param handle the current svg handle
	 * @param index the index of the type of the action
	 * @param elementsToTransform the map associating 
	 * an element to its transform
	 */
	protected void applyResizeTransform(
			SVGHandle handle, int index, 
			Map<Element, AffineTransform> elementsToTransform){
		
		//applying the transform to the elements
		AbstractShape shapeModule=null;
		
		//the set of the undo/redo actions that are created for each element
		final Set<UndoRedoAction> undoRedoActions=
			new HashSet<UndoRedoAction>();
		UndoRedoAction undoRedoAction;
		AffineTransform transform=null;
		Set<Element> elements=null;
		
		for(Element element : elementsToTransform.keySet()){
			
			shapeModule=ShapeToolkit.getShapeModule(element);
			
			if(shapeModule!=null){
				
				transform=elementsToTransform.get(element);
				
				if(transform!=null){
					
					elements=new HashSet<Element>();
					elements.add(element);
					
					undoRedoAction=shapeModule.resize(
							handle, elements, transform);
					
					if(undoRedoAction!=null){
						
						undoRedoActions.add(undoRedoAction);
					}
				}
			}
		}
		
		if(undoRedoActions.size()>0){
			
			//creating the execute runnable
			Runnable executeRunnable=new Runnable(){
				
				public void run() {
			
					for(UndoRedoAction action : undoRedoActions){
						
						action.execute();
					}
				}
			};
			
			//creating the undo runnable
			Runnable undoRunnable=new Runnable(){
				
				public void run() {
			
					for(UndoRedoAction action : undoRedoActions){
						
						action.undo();
					}
				}
			};
			
			addUndoRedoAction(handle, index, 
				executeRunnable, undoRunnable, 
					elementsToTransform.keySet());
		}
	}
	
	/**
	 * applies the rotate transform denoted by the provided 
	 * parameters to the provided elements
	 * @param handle the current svg handle
	 * @param index the index of the type of the action
	 * @param elementsToCenter the map associating 
	 * an element to its center point for the rotation
	 * @param elementsToAngle the map associating 
	 * an element to its angle for the rotation
	 */
	protected void applyRotateTransform(
			SVGHandle handle, int index, 
				Map<Element, Point2D> elementsToCenter,
					Map<Element, Double> elementsToAngle){
		
		//applying the transform to the elements
		AbstractShape shapeModule=null;
		
		//the set of the undo/redo actions that are created for each element
		final Set<UndoRedoAction> undoRedoActions=
			new HashSet<UndoRedoAction>();
		UndoRedoAction undoRedoAction;
		Point2D centerPoint;
		double angle=0;
		Set<Element> elements=null;
		
		for(Element element : elementsToCenter.keySet()){
			
			shapeModule=ShapeToolkit.getShapeModule(element);
			
			if(shapeModule!=null){
				
				centerPoint=elementsToCenter.get(element);
				angle=elementsToAngle.get(element).doubleValue();
				
				if(centerPoint!=null){
					
					elements=new HashSet<Element>();
					elements.add(element);
					
					undoRedoAction=shapeModule.rotate(
							handle, elements, centerPoint, angle);
					
					if(undoRedoAction!=null){
						
						undoRedoActions.add(undoRedoAction);
					}
				}
			}
		}
		
		if(undoRedoActions.size()>0){
			
			//creating the execute runnable
			Runnable executeRunnable=new Runnable(){
				
				public void run() {
			
					for(UndoRedoAction action : undoRedoActions){
						
						action.execute();
					}
				}
			};
			
			//creating the undo runnable
			Runnable undoRunnable=new Runnable(){
				
				public void run() {
			
					for(UndoRedoAction action : undoRedoActions){
						
						action.undo();
					}
				}
			};
			
			addUndoRedoAction(handle, index, executeRunnable, 
					undoRunnable, elementsToAngle.keySet());
		}
	}
	
	/**
	 * adds a undo/redo action to the undo/redo stack
	 * @param handle a svg handle
	 * @param index the index of the type of the action
	 * @param executeRunnable the runnable for the execution
	 * @param undoRunnable the runnable for undoing the action
	 * @param elements the set of the elements to be modified
	 */
	protected void addUndoRedoAction(SVGHandle handle, int index, 
			Runnable executeRunnable, Runnable undoRunnable, Set<Element> elements){

		//creating the undo/redo action and insert it into the undo/redo stack
		UndoRedoAction action=new UndoRedoAction(undoRedoActionsLabels[index], 
				executeRunnable, undoRunnable, executeRunnable, elements);

		UndoRedoActionList actionlist=
			new UndoRedoActionList(undoRedoActionsLabels[index], false);
		actionlist.add(action);

		handle.getUndoRedo().addActionList(actionlist, true);
	}
	
	/**
	 * sets whether the items should be enabled
	 * @param enable whether the items should be enabled
	 */
	protected void setItemsEnabled(boolean enable){
		
		for(int i=0; i<actionsMenuItems.length; i++){
			
			actionsMenuItems[i].setEnabled(enable);
		}
	}
	
	/**
	 * returns the map associating an element to its bounds
	 * @param handle a svg handle 
	 * @param elements a set of elements
	 * @return the map associating an element to its bounds
	 */
	protected Map<Element, Rectangle2D> getBounds(
			SVGHandle handle, Set<Element> elements){
		
		//getting the bounds of each element
		Map<Element, Rectangle2D> elementsToBounds=
			new HashMap<Element, Rectangle2D>();
		
		Rectangle2D bounds=null;
		
		for(Element element : elements){
			
			bounds=handle.getSvgElementsManager().
				getSensitiveBounds(element);
			
			if(bounds!=null){
				
				elementsToBounds.put(element, bounds);
			}
		}
		
		return elementsToBounds;
	}
	
	/**
	 * returns the union of all the rectangles of the set
	 * @param rectSet a set of rectangle
	 * @return the union of all the rectangles of the set
	 */
	protected Rectangle2D union(Set<Rectangle2D> rectSet){
		
		Rectangle2D unionRect=null;
		
		for(Rectangle2D rect : rectSet){
			
			if(unionRect==null){
				
				unionRect=new Rectangle2D.Double(
						rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
				
			}else{
				
				unionRect.add(rect);
			}
		}
		
		return unionRect;
	}
	
	/**
	 * the class of the comparator used to compare two
	 * elements according to their bounds
	 * @author Jordi SUC
	 */
	protected class ElementComparator implements Comparator<Element>{
		
		/**
		 * the map associating an element to its bounds
		 */
		private Map<Element, Rectangle2D> elementToBoundsMap;
		
		/**
		 * 
		 */
		private boolean isHorizontal=false;
		
		/**
		 * the constructor of the class
		 * @param elementToBoundsMap the map associating an element to its bounds
		 * @param isHorizontal whether the elements should be compared according to 
		 * 									their abscisses or according to their ordinate
		 */
		protected ElementComparator(
				Map<Element, Rectangle2D> elementToBoundsMap, boolean isHorizontal){
			
			this.elementToBoundsMap=elementToBoundsMap;
			this.isHorizontal=isHorizontal;
		}
		
		public int compare(Element e1, Element e2) {
			
			int result=0;
			
			if(e1!=null && e2!=null){
				
				//getting the bounds of the elements
				Rectangle2D bounds1=elementToBoundsMap.get(e1);
				Rectangle2D bounds2=elementToBoundsMap.get(e2);
				
				if(isHorizontal){
					
					result=(int)(bounds1.getX()-bounds2.getX());
					
				}else{
					
					result=(int)(bounds1.getY()-bounds2.getY());
				}
			}
			
			return result;
		}
	}
}
