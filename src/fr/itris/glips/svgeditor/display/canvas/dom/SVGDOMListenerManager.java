package fr.itris.glips.svgeditor.display.canvas.dom;

import java.util.*;
import java.util.concurrent.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;

/**
 * the class of the SVG DOM listeners manager
 * 
 * @author Jordi SUC
 */
public class SVGDOMListenerManager {

	/**
	 * the related svg handle
	 */
	private SVGHandle handle;
	
	/**
	 * the map associating a node to the set of the listeners to this node
	 */
	private ConcurrentHashMap<Node, Set<SVGDOMListener>> domListeners=
		new ConcurrentHashMap<Node, Set<SVGDOMListener>>();

	/**
	 * the constructor of the class
	 * @param handle a svg handle
	 */
	public SVGDOMListenerManager(SVGHandle handle) {

		this.handle=handle;
	}
	
	/**
	 * disposes this manager
	 */
	public void dispose(){
		
		//removing each dom listener
		Set<SVGDOMListener> domListenersSet=null;
		
		for(Node node : domListeners.keySet()) {
			
			if(node!=null) {
				
				domListenersSet=domListeners.get(node);
				
				for(SVGDOMListener listener : domListenersSet) {
					
					listener.removeListener();
				}
			}
		}
		
		domListeners.clear();
		handle=null;
		domListeners=null;
	}
	
	/**
	 * adding a listener to the svg dom of this handle
	 * (should be invoked in the AWT THREAD)
	 * @param listener a listener
	 */
	public void addDOMListener(SVGDOMListener listener){
		
		if(listener!=null){
			
			Set<SVGDOMListener> set=null;
			
			if(domListeners.containsKey(listener.getNode())) {
				
				set=domListeners.get(listener.getNode());
				
			}else {
				
				//creating and putting the new set into the map
				set=new CopyOnWriteArraySet<SVGDOMListener>();
				domListeners.put(listener.getNode(), set);
			}
			
			set.add(listener);
			listener.setHandle(handle);
		}
	}
	
	/**
	 * removing a listener from the svg dom of this handle
	 * (should be invoked in the AWT THREAD)
	 * @param listener a listener
	 */
	public void removeDOMListener(SVGDOMListener listener){

		if(listener!=null && listener.getNode()!=null && domListeners.containsKey(listener.getNode())){
			
			Set<SVGDOMListener> set=domListeners.get(listener.getNode());
			
			if(set!=null) {
				
				set.remove(listener);
				return;
			}
		}
	}
	
	/**
	 * fires that the given node has been modified ( one of its attributes has been modified)
	 * (should be invoked in the AWT THREAD)
	 * @param node a node
	 */
	public void fireNodeChanged(Node node){
		
		if(node!=null) {
			
			//getting the set associated with the given node
			Set<SVGDOMListener> set=domListeners.get(node);
			
			if(set!=null) {
				
				for(SVGDOMListener listener : set){
					
					if(listener!=null){
						
						//firing that the node has changed
						listener.nodeChanged();
					}
				}
			}
		}
	}
	
	/**
	 * fires that the given node has been removed 
	 * (should be invoked in the AWT THREAD)
	 * @param node a node
	 * @param removedNode the node that has been removed
	 */
	public void fireNodeRemoved(Node node, Node removedNode){
		
		if(node!=null && removedNode!=null) {
			
			//getting the set associated with the given node
			Set<SVGDOMListener> set=domListeners.get(node);
			
			if(set!=null) {
				
				for(SVGDOMListener listener : set){
					
					if(listener!=null){
						
						//firing that the node has changed
						listener.nodeRemoved(removedNode);
					}
				}
			}
		}
	}
	
	/**
	 * fires that the given node has been inserted
	 * (should be invoked in the AWT THREAD)
	 * @param node a node
	 * @param nodeInserted the node that has been inserted into the node children
	 */
	public void fireNodeInserted(Node node, Node nodeInserted){
		
		if(node!=null) {
			
			//getting the set associated with the given node
			Set<SVGDOMListener> set=domListeners.get(node);
			
			if(set!=null) {
				
				for(SVGDOMListener listener : set){
					
					if(listener!=null){
						
						//firing that the node has changed
						listener.nodeInserted(nodeInserted);
					}
				}
			}
		}
	}
	
	/**
	 * fires that the given sub tree has been modified
	 * (should be invoked in the AWT THREAD)
	 * @param rootNode a node
	 * @param lastModifiedNode the last node that has been modified
	 */
	public void fireStructureChanged(
			Node rootNode, Node lastModifiedNode){
		
		if(rootNode!=null) {
			
			//getting the set associated with the given node
			Set<SVGDOMListener> set=domListeners.get(rootNode);
			
			if(set!=null) {
				
				for(SVGDOMListener listener : set){
					
					if(listener!=null){
						
						//firing that the node has changed
						listener.structureChanged(lastModifiedNode);
					}
				}
			}
		}
	}
}
