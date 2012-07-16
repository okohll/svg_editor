package fr.itris.glips.svgeditor.actions.dom;

import java.awt.event.*;
import java.util.*;
import org.w3c.dom.*;

import fr.itris.glips.library.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to execute order actions on svg elements
 * @author Jordi SUC
 */
public class OrderModule extends DomActionsModule {
	
	/**
	 * the constants
	 */
	protected static final int ORDER_TOP=0, ORDER_UP=1, 
		ORDER_DOWN=2, ORDER_BOTTOM=3;

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public OrderModule(Editor editor) {
		
		//setting the id
		moduleId="Order";

		//filling the arrays of the types
		int[] types={ORDER_TOP, ORDER_UP, 
				ORDER_DOWN, ORDER_BOTTOM};
		actionsTypes=types;
		
		//filling the arrays of the ids
		String[] ids={"OrderTop", "OrderUp", "OrderDown", "OrderBottom"};
		actionsIds=ids;
		
		createItems();
	}
	
	@Override
	protected void doAction(
		SVGHandle handle, final Set<Element> elements, 
			int index, ActionEvent evt) {
		
		//getting the type of the action
		int type=actionsTypes[index];

		//getting the current parent node
		final Element parentElement=handle.getSelection().getParentElement();

		//computing the linkedlist of the child nodes
		final LinkedList<Element> initialChildNodesList=
			Toolkit.getChildrenElements(parentElement);

		//creating the initial map associating an element to its next element
		final Map<Element, Element> initialNextElementMap=
			new HashMap<Element, Element>();
		
		//creating the linkedlist of the elements to be handled
		final LinkedList<Element> elementsList=new LinkedList<Element>();
		
		for(Element element : initialChildNodesList){
			
			if(elements.contains(element)){
				
				elementsList.add(element);
				initialNextElementMap.put(element, 
						EditorToolkit.getNextElementSibling(element));
			}
		}
		
		//reverts this elements list to be handled
		final LinkedList<Element> reversedElementsList=
			new LinkedList<Element>(elementsList);
		Collections.reverse(reversedElementsList);
		
		//creating the new map associating an element to its 
		//next element, according to the applied action
		final Map<Element, Element> newNextElementMap=
			new HashMap<Element, Element>();
		
		//computing the new linkedlist of the nodes of the parent element 
		//once the action has been applied
		final LinkedList<Element> newChildNodesList=new LinkedList<Element>();
		
		switch (type){
			
			case ORDER_TOP :
				
				//adding all the elements that are not selected
				for(Element element : initialChildNodesList){
					
					if(! elementsList.contains(element)){
						
						newChildNodesList.add(element);
					}
				}
				
				//adding all the elements into the linked list
				newChildNodesList.addAll(elementsList);

				break;
				
			case ORDER_BOTTOM :

				//adding all the elements into the linked list
				newChildNodesList.addAll(elementsList);
				
				//adding all the remaining elements that are not selected
				for(Element element : initialChildNodesList){
					
					if(! newChildNodesList.contains(element)){
						
						newChildNodesList.add(element);
					}
				}
				
				break;
				
			case ORDER_UP :
				
				Element previousElement=null;
				
				//adding all the elements, moving those that are selected
				for(Element element : initialChildNodesList){

					if(elementsList.contains(element)){
						
						if(previousElement!=null){
							
							newChildNodesList.add(previousElement);
						}
						
						previousElement=element;

					}else{
						
						newChildNodesList.add(element);
						
						if(previousElement!=null){
							
							newChildNodesList.add(previousElement);
							previousElement=null;
						}
					}
				}
				
				break;
				
			case ORDER_DOWN :
				
				//adding all the elements, moving those that are selected
				Element lastElement=null;
				
				for(Element element : initialChildNodesList){
					
					if(elementsList.contains(element)){
						
						if(newChildNodesList.size()==0){
							
							newChildNodesList.add(element);
							
						}else{
							
							lastElement=newChildNodesList.removeLast();
							newChildNodesList.addLast(element);
							newChildNodesList.addLast(lastElement);
						}

					}else{
						
						newChildNodesList.addLast(element);
					}
				}
				
				break;
		}
		
		//filling the new map associating an element to its 
		//next element, according to the applied action
		int i=0;

		for(Element element : newChildNodesList){
			
			if(elements.contains(element)){

				if(i<newChildNodesList.size()-1){
					
					newNextElementMap.put(
							element, newChildNodesList.get(i+1));
					
				}else{
					
					newNextElementMap.put(element, null);
				}
			}
			
			i++;
		}

		//creating the execute runnable
		Runnable executeRunnable=new Runnable(){
			
			public void run() {

				Element nextElement=null;
				Element lastElement=null;
				
				for(Element element : reversedElementsList){
					
					nextElement=newNextElementMap.get(element);

					if(nextElement!=null){
						
						parentElement.insertBefore(element, nextElement);
						
					}else{
						
						if(lastElement!=null){
							
							parentElement.insertBefore(element, lastElement);
							
						}else{
							
							parentElement.removeChild(element);
							parentElement.appendChild(element);
						}
					}
					
					lastElement=element;
				}
			}
		};
		
		//creating the undo runnable
		Runnable undoRunnable=new Runnable(){
			
			public void run() {

				Element nextElement=null;
				Element lastElement=null;
				
				for(Element element : reversedElementsList){
					
					nextElement=initialNextElementMap.get(element);
					
					if(nextElement!=null){
						
						parentElement.insertBefore(element, nextElement);
						
					}else{
						
						if(lastElement!=null){
							
							parentElement.insertBefore(element, lastElement);
							
						}else{
							
							parentElement.removeChild(element);
							parentElement.appendChild(element);
						}
					}
					
					lastElement=element;
				}
			}
		};
		
		//adding the undo/redo action to the undo.redo stack
		addUndoRedoAction(handle, index, 
				executeRunnable, undoRunnable, elements);
	}

	@Override
	protected boolean selectionCorrect(int index, Set<Element> elements) {

		return selectionCorrectFirstType(elements);
	}
}
