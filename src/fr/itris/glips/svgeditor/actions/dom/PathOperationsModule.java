package fr.itris.glips.svgeditor.actions.dom;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.apache.batik.ext.awt.geom.*;
import org.w3c.dom.*;
import fr.itris.glips.library.geom.path.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class used to execute align actions on svg elements
 * @author Jordi SUC
 */
public class PathOperationsModule extends DomActionsModule {
	
	/**
	 * the constants
	 */
	protected static final int CONVERT_TO_PATH=0, UNION=1, 
		SUBTRACTION=2, INTERSECTION=3, CLOSE_PATH=4;
	
	/**
	 * node and attribute names
	 */
	protected static final String pathNodeName="path", dAtt="d";

	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public PathOperationsModule(Editor editor) {
		
		//setting the id
		moduleId="PathOperations";

		//filling the arrays of the types
		int[] types={CONVERT_TO_PATH, UNION, SUBTRACTION, 
				INTERSECTION, CLOSE_PATH};
		actionsTypes=types;
		
		//filling the arrays of the ids
		String[] ids={"ConvertToPath", "Union", "Subtraction", 
				"Intersection", "ClosePath"};
		actionsIds=ids;
		
		createItems();
	}
	
	@Override
	protected void doAction(
		final SVGHandle handle, Set<Element> elements, 
			int index, ActionEvent evt) {
		
		//getting the type of the action
		int type=actionsTypes[index];

		if(type==CONVERT_TO_PATH){
			
			doConvertToPathAction(handle, elements, index);
			
		}else if(type==CLOSE_PATH){

			doClosePathAction(handle, elements, index);
			
		}else{
			
			//the execute and undo runnables
			Runnable executeRunnable=null, undoRunnable=null;
			
			//getting the current parent node
			final Element parentElement=handle.getSelection().getParentElement();
			
			//getting the list of the elements, sorted 
			//according to their position in the document
			final LinkedList<Element> sortedElementsList=
				new LinkedList<Element>();

			NodeList childNodes=parentElement.getChildNodes();
			Node node=null;
			
			for(int i=0; i<childNodes.getLength(); i++){
				
				node=childNodes.item(i);
				
				if(node!=null && node instanceof Element && 
						elements.contains(node)){
					
					sortedElementsList.add((Element)node);
				}
			}

			//creating the map associating an element to its next element
			final Map<Element, Element> elementToNextMap=
				new HashMap<Element, Element>();
			
			//filling the map
			for(Element element : sortedElementsList){
				
				elementToNextMap.put(element, 
					EditorToolkit.getNextElementSibling(element));
			}
			
			//computing the reversed linked list of the elements
			final LinkedList<Element> reversedElementsList=
				new LinkedList<Element>(sortedElementsList);
			Collections.reverse(reversedElementsList);
			
			//creating the new element according to the current action
			Element newElement=null;
			
			switch (type){
				
				case UNION :
					
					newElement=doUnionAction(
						handle, parentElement, elements, index);
					break;
					
				case SUBTRACTION :
					
					newElement=doSubtractionAction(
						handle, parentElement, elements, index);
					break;
					
				case INTERSECTION :
					
					newElement=doIntersectionAction(
						handle, parentElement, elements, index);
					break;
			}
			
			final Element fnewElement=newElement;
			
			//creating the execute runnable
			executeRunnable=new Runnable(){
				
				public void run() {

					//removing all the elements that have been 
					//used to compute the action
					for(Element element : sortedElementsList){

						parentElement.removeChild(element);
					}
					
					//appending the new element
					parentElement.appendChild(fnewElement);
					
					//selecting the element
					handle.getSelection().clearSelection();
					handle.getSelection().handleSelection(
							fnewElement, false, true);
				}
			};
			
			//creating the execute runnable
			undoRunnable=new Runnable(){
				
				public void run() {

					//removing the appended new element
					parentElement.removeChild(fnewElement);
					
					//re-adding the elements that had been 
					//replaced by the new element
					Element nextElement=null;
					
					for(Element element : reversedElementsList){
						
						nextElement=elementToNextMap.get(element);
						
						if(nextElement!=null){
							
							parentElement.insertBefore(element, nextElement);
							
						}else{
							
							parentElement.appendChild(element);
						}
					}
				}
			};
			
			addUndoRedoAction(handle, index, 
					executeRunnable, undoRunnable, elements);
		}
	}

	/**
	 * executes the "convert to path" action
	 * @param handle a svg handle
	 * @param elements a set of elements to be modified
	 * @param index the index of the action
	 */
	protected void doConvertToPathAction(
		final SVGHandle handle, Set<Element> elements, int index) {
		
		//the execute and undo runnables
		Runnable executeRunnable=null, undoRunnable=null;
		
		//getting the current parent node
		final Element parentElement=handle.getSelection().getParentElement();
	
		//getting a map associating an element to its path
		Map<Element, GeneralPath> elementToPathMap=getPaths(handle, elements);
		
		//getting the map associating an element to the element it replaces
		final Map<Element, Element> newElementsMap=
			convertPathsToElement(handle, elementToPathMap);

		executeRunnable=new Runnable(){
			
			public void run() {

				//inserting the new elements and removing the older ones
				Element oldElement=null;
				
				for(Element newElement : newElementsMap.keySet()){
					
					oldElement=newElementsMap.get(newElement);
					parentElement.insertBefore(newElement, oldElement);
					
					if(oldElement!=null){
						
						parentElement.removeChild(oldElement);
					}
				}
				
				//selecting the new elements
				handle.getSelection().clearSelection();
				handle.getSelection().handleSelection(
						newElementsMap.keySet(), true, true);
			}
		};
		
		undoRunnable=new Runnable(){
			
			public void run() {

				//inserting the new elements and removing the older ones
				Element oldElement=null;
				
				for(Element newElement : newElementsMap.keySet()){
					
					oldElement=newElementsMap.get(newElement);
					
					parentElement.insertBefore(oldElement, newElement);
					
					if(oldElement!=null){
						
						parentElement.removeChild(newElement);
					}
				}
			}
		};
		
		addUndoRedoAction(handle, index, 
				executeRunnable, undoRunnable, elements);
	}
	
	/**
	 * executes the "convert to path" action
	 * @param handle a svg handle
	 * @param elements a set of elements to be modified
	 * @param index the index of the action
	 */
	protected void doClosePathAction(
		SVGHandle handle, Set<Element> elements, int index) {
		
		//the execute and undo runnables
		Runnable executeRunnable=null, undoRunnable=null;

		//getting a map associating an element to its path manager
		Map<Element, Path> elementToPathManagerMap=
			getPathManagers(handle, elements);
		
		//creating the initial and new maps assocating an 
		//element to its d attribute value
		final Map<Element, String> initialElementToDAttribute=
			new HashMap<Element, String>();
		final Map<Element, String> newElementToDAttribute=
			new HashMap<Element, String>();
		Path pathManager=null;
		String initDValue="", newDValue="";
		
		for(Element element : elementToPathManagerMap.keySet()){
			
			pathManager=elementToPathManagerMap.get(element);
			
			//getting the current d value
			initDValue=pathManager.toString();
			
			//cloning the path manager
			pathManager=new Path(pathManager);
			
			//closing the path manager
			pathManager.closePath();
			
			//getting the new d value
			newDValue=pathManager.toString();
			
			//putting the element and the its d attribute values into the maps
			initialElementToDAttribute.put(element, initDValue);
			newElementToDAttribute.put(element, newDValue);
		}

		//creating the execute runnable
		executeRunnable=new Runnable(){
			
			public void run() {

				String dValue="";
		
				for(Element element : newElementToDAttribute.keySet()){
					
					dValue=newElementToDAttribute.get(element);
					element.setAttribute(dAtt, dValue);
				}
			}
		};
		
		//creating the undo runnable
		undoRunnable=new Runnable(){
			
			public void run() {
				
				String dValue="";
		
				for(Element element : initialElementToDAttribute.keySet()){
					
					dValue=initialElementToDAttribute.get(element);
					element.setAttribute(dAtt, dValue);
				}
			}
		};
		
		addUndoRedoAction(handle, index, 
				executeRunnable, undoRunnable, elements);
	}
	
	/**
	 * executes the "union" action
	 * @param handle a svg handle
	 * @param parentElement the parent element of the element to be created
	 * @param elements a set of elements used to compute the union
	 * @param index the index of the action
	 * @return the new element computing from the union of the provided elements
	 */
	protected Element doUnionAction(
			SVGHandle handle, Element parentElement, Set<Element> elements, int index){
		
		Element newElement=null;
		
		//getting the map of the path associated with each element
		Map<Element, GeneralPath> paths=getPaths(handle, elements);
		
		//creating the union area
		GeneralPath path=null;
		Area area=null;
		
		for(Element element : elements){
			
			path=paths.get(element);
			path.closePath();
			
			if(area==null){
				
				area=new Area(path);
				
			}else{
				
				area.add(new Area(path));
			}
		}
		
		if(area!=null){
			
			//creating the union general path
			GeneralPath newPath=new GeneralPath(area);
			
			//creating the new path element and applying it the new path
			newElement=createElement(parentElement, 
					new Path(new ExtendedGeneralPath(newPath)));
		}

		return newElement;
	}
	
	/**
	 * executes the "intersect" action
	 * @param handle a svg handle
	 * @param parentElement the parent element of the element to be created
	 * @param elements a set of elements used to compute the intersection
	 * @param index the index of the action
	 * @return the new element computing from the intersection of the provided elements
	 */
	protected Element doSubtractionAction(
		SVGHandle handle, Element parentElement, 
			Set<Element> elements, int index){
		
		Element newElement=null;
		
		//getting the ordered list of the selected items
		LinkedList<Element> orderedSelectedElements=
			new LinkedList<Element>(
				handle.getSelection().getOrderedSelectedElements());
		
		//getting the map of the path associated with each element
		Map<Element, GeneralPath> paths=getPaths(handle, elements);
		
		//getting the first element
		Element firstElement=orderedSelectedElements.getFirst();
		
		//removing the first element from the list
		orderedSelectedElements.remove(firstElement);
		
		//creating the union area of all the elements but the first one
		GeneralPath path=null;
		Area area=null;
		
		for(Element element : orderedSelectedElements){
			
			path=paths.get(element);
			path.closePath();
			
			if(area==null){
				
				area=new Area(path);
				
			}else{
				
				area.add(new Area(path));
			}
		}
		
		if(area!=null){
			
			//creating the base area
			Area baseArea=new Area(paths.get(firstElement));
			
			//creating the subtraction area
			baseArea.subtract(area);
			
			//creating the intersection general path
			GeneralPath newPath=new GeneralPath(baseArea);
			
			//creating the new path element and applying it the new path
			newElement=createElement(parentElement, 
					new Path(new ExtendedGeneralPath(newPath)));
		}

		return newElement;
	}
	
	/**
	 * executes the "intersect" action
	 * @param handle a svg handle
	 * @param parentElement the parent element of the element to be created
	 * @param elements a set of elements used to compute the intersection
	 * @param index the index of the action
	 * @return the new element computing from the intersection of the provided elements
	 */
	protected Element doIntersectionAction(
			SVGHandle handle, Element parentElement, Set<Element> elements, int index){
		
		Element newElement=null;
		
		//getting the map of the path associated with each element
		Map<Element, GeneralPath> paths=getPaths(handle, elements);
		
		//creating the union area
		GeneralPath path=null;
		Area area=null;
		
		for(Element element : elements){
			
			path=paths.get(element);
			path.closePath();
			
			if(area==null){
				
				area=new Area(path);
				
			}else{
				
				area.intersect(new Area(path));
			}
		}
		
		if(area!=null){
			
			//creating the intersection general path
			GeneralPath newPath=new GeneralPath(area);
			
			//creating the new path element and applying it the new path
			newElement=createElement(parentElement, 
					new Path(new ExtendedGeneralPath(newPath)));
		}

		return newElement;
	}
	
	/**
	 * converts the given elements into general paths and returns the map associating 
	 * an element to the related general path
	 * @param handle a svg handle
	 * @param elements a set of elements
	 * @return the map associating an element to the related general path
	 */
	protected Map<Element, GeneralPath> getPaths(
			SVGHandle handle, Set<Element> elements){
		
		Map<Element, GeneralPath> paths=
			new HashMap<Element, GeneralPath>();
		
		GeneralPath path=null;
		Shape outline=null;
		
		for(Element element : elements){
			
			outline=handle.getSvgElementsManager().
				getOutline(element);
			
			if(outline!=null){
				
				path=new GeneralPath(outline);
				
				paths.put(element, path);
			}
		}
		
		return paths;
	}
	
	/**
	 * converts the given elements into path managers and returns the map associating 
	 * an element to the related path manager
	 * @param handle a svg handle
	 * @param elements a set of elements
	 * @return the map associating an element to the related path manager
	 */
	protected Map<Element, Path> getPathManagers(
			SVGHandle handle, Set<Element> elements){
		
		Map<Element, Path> pathManagers=
			new HashMap<Element, Path>();
		
		ExtendedGeneralPath path=null;
		Path pathManager=null;
		Shape outline=null;
		
		for(Element element : elements){
			
			outline=handle.getSvgElementsManager().
				getOutline(element);
			
			if(outline!=null){
				
				path=new ExtendedGeneralPath(outline);
				pathManager=new Path(path);
				pathManagers.put(element, pathManager);
			}
		}
		
		return pathManagers;
	}
	
	/**
	 * converts the given general paths into path svg elements
	 * @param handle a svg handle
	 * @param elementToPath an element to its path
	 * @return the map associating a paths element to the 
	 * element it should be inserted before
	 */
	protected Map<Element, Element> convertPathsToElement(
			SVGHandle handle, Map<Element, GeneralPath> elementToPath){
		
		//getting the map associating a general path to its element
		Map<GeneralPath, Element> pathToElementMap=
			new HashMap<GeneralPath, Element>();
		GeneralPath path=null;
		
		for(Element element : elementToPath.keySet()){
			
			path=elementToPath.get(element);
			
			if(path!=null){
				
				pathToElementMap.put(path, element);
			}
		}
		
		//getting the current parent element
		Element parentElement=handle.getSelection().getParentElement();
		
		//the map associating a path element to the 
		//element it should be inserted before
		Map<Element, Element> pathElements=
			new HashMap<Element, Element>();
		Element pathElement=null;
		Element element=null;
		
		for(GeneralPath thePath : pathToElementMap.keySet()){
			
			element=pathToElementMap.get(thePath);
			
			if(element!=null){
				
				pathElement=createElement(parentElement, 
						new Path(new ExtendedGeneralPath(thePath)));
				pathElements.put(pathElement, element);
			}
		}
		
		return pathElements;
	}
	
	/**
	 * creating a path element whose properties are defined 
	 * by the provided path manager
	 * @param parentElement the parent element
	 * @param pathManager the path manager
	 * @return  the created path element
	 */
	protected Element createElement(
			Element parentElement, Path pathManager){
		
		Element element=null;
		
		//getting the last color that has been used by the user
		String colorString=Editor.getColorChooser().getColorString(
			ColorManager.getCurrentColor());
		
		//getting the document
		Document doc=parentElement.getOwnerDocument();
		
		//creating a path manager
		pathManager=new Path(pathManager);
		
		if(Editor.getEditor().getClosePathModeManager().shouldClosePath()){
			
			pathManager.closePath();
		}

		//creating the path element
		element=doc.createElementNS(
			doc.getDocumentElement().getNamespaceURI(), 
				pathNodeName);
		element.setAttribute(dAtt, pathManager.toString());

		//setting the element style
		element.setAttributeNS(
				null, "style", "fill:"+colorString+";stroke:#000000;");
		
		return element;
	}

	@Override
	protected boolean selectionCorrect(int index, Set<Element> elements) {

		boolean selectionCorrect=false;
		
		if(index==0){
			
			selectionCorrect=selectionCorrectFirstType(elements);
			
		}else if(index==4){
			
			if(elements.size()>=1){
				
				//checking if the set only contains path elements
				boolean onlyPaths=true;
				
				for(Element element : elements){
					
					if(! element.getNodeName().equals(pathNodeName)){
						
						onlyPaths=false;
						break;
					}
				}
				
				selectionCorrect=onlyPaths;
			}
			
		}else{
			
			selectionCorrect=selectionCorrectSecondType(elements);
		}
		
		return selectionCorrect;
	}
}
