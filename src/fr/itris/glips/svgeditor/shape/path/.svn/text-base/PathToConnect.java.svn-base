package fr.itris.glips.svgeditor.shape.path;

import org.w3c.dom.*;
import fr.itris.glips.library.geom.path.*;

/**
 * the class defining the element to which a new path 
 * should be connected
 * @author Jordi SUC
 */
public class PathToConnect {

	/**
	 * the element
	 */
	protected Element element;
	
	/**
	 * the path
	 */
	protected Path path;
	
	/**
	 * whether a new path should be inserted before or after this one
	 */
	protected boolean insertBefore=true;
	
	/**
	 * the constructor of the class
	 * @param element the element
	 * @param itemIndex the index of the item defining 
	 * where the new path should be inserted
	 */
	public PathToConnect(Element element, int itemIndex){
		
		this.element=element;
		
		//creating the path of the element
		path=new Path(element.getAttribute(PathShape.dAtt));
		
		//getting the position where the new path should be inserted
		insertBefore=(itemIndex==0);
	}
	
	/**
	 * @return whether another path can be connected to this path
	 */
	public boolean isCorrectPath(){
		
		return ! path.isArc();
	}
	
	/**
	 * @return whether the action this object denotes consists 
	 * in inserting a path before the current one or after the current one
	 */
	public boolean isInsertBeforeAction() {
		return insertBefore;
	}

	/**
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}

	/**
	 * @return the path
	 */
	public Path getPath() {
		return path;
	}
}
