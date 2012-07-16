package fr.itris.glips.svgeditor.display.handle.manager;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.display.handle.*;


/**
 * the class used to handle the resources 
 * and the elements using them
 * @author Jordi SUC
 */
public class SVGResourcesManager {

	/**
	 * the related svg handle
	 */
	private SVGHandle handle;
	
	/**
	 * the map associating the id of a resource to the list of the nodes using this resource
	 */
	private HashMap<String, LinkedList<Element>> usedResources=
		new HashMap<String, LinkedList<Element>>();
	
	/**
	 * the constructor of the class
	 * @param handle a svg handle
	 */
	public SVGResourcesManager(SVGHandle handle) {

		this.handle=handle;
	}
	
	/**
	 * disposes this manager
	 */
	public void dispose(){
		
		//removing all the used resources
		usedResources.clear();
		handle=null;
		usedResources=null;
	}
	
	/**
	 * adds the given id of a resource and its associated node 
	 * to the map of the nodes using a resource
	 * @param resourceId the id of a resource
	 * @param node the node using the given resource
	 */
	public void addNodeUsingResource(String resourceId, Node node){
		
		if(resourceId!=null && ! resourceId.equals("") && node!=null){
			
			LinkedList<Element> nodesList=null;
			
			//checking if the id of the resource is contained in the map
			if(usedResources.containsKey(resourceId)){
				
				try{
					//getting the associated list of nodes
					nodesList=usedResources.get(resourceId); 
				}catch (Exception ex){}
			}
			
			if(nodesList==null){
				
				//if the id was not contained in the map, creates a new list of nodes
				nodesList=new LinkedList<Element>();
				usedResources.put(resourceId, nodesList);
			}
			
			//adding the node to the list
			nodesList.add((Element)node);
		}
	}
	
	/**
	 * adds the given id of a resource and its associated node to the map of the nodes using a resource
	 * @param resourceId the id of a resource
	 * @param list the nodes using the given resource
	 */
	public void addNodesUsingResource(String resourceId, LinkedList<Element> list){
		
		if(resourceId!=null && ! resourceId.equals("") && list!=null){
			
			LinkedList<Element> nodesList=null;

			//checking if the id of the resource is contained in the map
			if(usedResources.containsKey(resourceId)){
				
				try{
					//getting the associated list of nodes
					nodesList=usedResources.get(resourceId); 
				}catch (Exception ex){}
			}
			
			if(nodesList==null){
				
				//if the id was not contained in the map, creates a new list of nodes
				nodesList=new LinkedList<Element>();
				usedResources.put(resourceId, nodesList);
			}

			for(Element cur : new HashSet<Element>(list)){
				
				if(cur!=null){
					
					//adding the node to the list
					nodesList.add(cur);
				}
			}
		}
	}
	
	/**
	 * removes the given id of a resource and its associated node to the map of the nodes using a resource
	 * @param resourceId the id of a resource
	 * @param node the node using the given resource
	 */
	public void removeNodeUsingResource(String resourceId, Node node){
		
		if(resourceId!=null && ! resourceId.equals("") && node!=null){
			
			LinkedList<Element> nodesList=null;
			
			//checking if the id of the resource is contained in the map
			if(usedResources.containsKey(resourceId)){
				
				try{
					//getting the associated list of nodes
					nodesList=usedResources.get(resourceId); 
				}catch (Exception ex){}
			}
			
			if(nodesList!=null && nodesList.contains(node)){
				
				//removing the node from the list
				nodesList.remove(node);
			}
		}
	}
	
	/**
	 * adds the given id of a resource and its associated node to the map of the nodes using a resource
	 * @param resourceId the id of a resource
	 * @param list the nodes using the given resource
	 */
	public void removeNodesUsingResource(String resourceId, LinkedList<Element> list){
		
		if(resourceId!=null && ! resourceId.equals("") && list!=null){
			
			LinkedList<Element> nodesList=null;
			
			//checking if the id of the resource is contained in the map
			if(usedResources.containsKey(resourceId)){
				
				try{
					//getting the associated list of nodes
					nodesList=usedResources.get(resourceId); 
				}catch (Exception ex){}
			}
			
			if(nodesList!=null){
				
				for(Element cur : list){
					
					if(cur!=null){
						
						//removing the node from the list
						nodesList.remove(cur);
					}
				}
			}
		}
	}
	
	/**
	 * returns all the nodes that use the resource denoted by the provided id
	 * @param id a resource id
	 * @return all the nodes that use the resource denoted by the provided id
	 */
	public Set<Element> getNodeUsingResource(String id){
		
		LinkedList<Element> resourcesList=usedResources.get(id);
		
		if(resourcesList!=null){
			
			return new HashSet<Element>(resourcesList);
		}
		
		return new HashSet<Element>();
	}
	
	/**
	 * redraws the node that use the resource of the given id
	 * @param id the id of a resource
	 */
	public void refreshNodesUsingResource(String id){
		
		if(id!=null && ! id.equals("")){
			
			LinkedList<Element> list=null;
			
			try{
				list=usedResources.get(id);
			}catch (Exception ex){}

			if(list!=null){

				for(Element cur : list){

					if(cur!=null){
						
						cur.setAttribute("display", "none");
						cur.removeAttribute("display");
					}
				}
			}
		}
	}
	
	/**
	 * modifies the id of a resource in the nodes that are using this resource
	 * @param newId the new id for the resource
	 * @param lastId the previous id for the resource
	 */
	public void modifyResourceId(String newId, String lastId){
		
		if(newId!=null && ! newId.equals("") && lastId!=null && ! lastId.equals("") && 
			! lastId.equals(newId) && usedResources.containsKey(lastId)){
			
			//getting the list of the nodes using the resource
			LinkedList<Element> nodesList=null;
			
			try{
				nodesList=usedResources.get(lastId);
			}catch (Exception ex){}
			
			if(nodesList!=null){
				
				String style="";
				
				//for each node, replaces the last id of the resource with the new id
				for(Element cur : nodesList){
					
					if(cur!=null){
						
						style=cur.getAttribute("style");
						
						if(style!=null && ! style.equals("")){
							
							style=style.replaceAll("#"+lastId+"[)]", "#"+newId+")");
							cur.setAttribute("style", style);
						}
					}
				}
				
				//modifies the map of the used resources
				usedResources.remove(lastId);
				usedResources.put(newId, nodesList);
			}
		}
	}
	
	/**
	 * checks if the resource denoted by the given id is used or not
	 * @param resourceId the id of a resource
	 * @return true if the resource is used
	 */
	public boolean isResourceUsed(String resourceId){
		
		boolean isUsed=false;
		
		if(resourceId!=null && ! resourceId.equals("")){
			
			LinkedList<Element> list=null;
			
			try{
				list=usedResources.get(resourceId);
			}catch (Exception ex){}
			
			if(list!=null && list.size()>0){
				
				isUsed=true;
			}
		}
		
		return isUsed;
	}
	
	/**
	 * checks if the given node is using a resource and registers it on the canvas if this is such a case
	 * @param node a node
	 */
	public void registerUsedResource(Node node){
		
		if(node!=null && node instanceof Element){
			
			//the map of the used resources
			Map<String, LinkedList<Element>> usedResourcesMap=getUsedResources();
			
			//getting the style attribute
			String style=((Element)node).getAttribute("style");
			
			if(style!=null && ! style.equals("")){
				
				LinkedList<Element> nodesList=null;
				
				//for each resource id, checks if it is contained in the style attribute
				for(String id : usedResourcesMap.keySet()){
					
					try{
						nodesList=usedResourcesMap.get(id);
					}catch (Exception ex){id=null; nodesList=null;}
					
					//adds the node in the used resource map
					if(id!=null && ! id.equals("") && style.indexOf("#".concat(id))!=-1 && 
							nodesList!=null && ! nodesList.contains(node)){
						
						addNodeUsingResource(id, node);
					}
				}
			}
			
			//registers all the used resources that could be found in the children nodes of this node
			for(Node cur=node.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
				
				if(cur instanceof Element){
					
					registerUsedResource(cur);
				}
			}
		}
	}
	
	/**
	 * checks if the given node is using a resource and unregisters it in the canvas if this is such a case
	 * @param node a node
	 */
	public void unregisterAllUsedResource(Node node){
		
		if(node!=null && node instanceof Element){
			
			//the map of the used resources
			Map<String, LinkedList<Element>> usedResourcesMap=getUsedResources();
			
			//getting the style attribute
			String style=((Element)node).getAttribute("style");
			
			if(style!=null && ! style.equals("")){
				
				LinkedList<Element> nodesList=null;
				
				//for each resource id, checks if it is contained in the style attribute
				for(String id : usedResourcesMap.keySet()){
					
					try{
						nodesList=usedResourcesMap.get(id);
					}catch (Exception ex){id=null; nodesList=null;}
					
					//removes the node from the used resource map
					if(id!=null && ! id.equals("") && style.indexOf("#".concat(id))!=-1 && 
						nodesList!=null && nodesList.contains(node)){
						
						removeNodeUsingResource(id, node);
					}
				}
			}
			
			//unregisters all the used resources that could be found in the children nodes of this node
			for(Node cur=node.getFirstChild(); cur!=null; cur=cur.getNextSibling()){
				
				if(cur instanceof Element){
					
					unregisterAllUsedResource(cur);
				}
			}
		}
	}
	
	/**
	 * returns the map associating an id to a resource node
	 * @param doc the document
	 * @param resourceTagNames the list of the tag names of the 
	 * resources that should appear in the returned list
	 * @return the map associating an id to a resource node
	 */
	public HashMap<String, Element> getResourcesFromDefs(
			Document doc, LinkedList<String> resourceTagNames){
		
		HashMap<String, Element> idResources=new HashMap<String, Element>();

		//getting the list of the resources that can be found in the defs elements
		if(doc!=null && resourceTagNames!=null && resourceTagNames.size()>0){

			//the list of the available defs nodes that can be found in the document
			final NodeList defsNodes=doc.getElementsByTagName("defs");

			if(defsNodes!=null && defsNodes.getLength()>0){
				
				Element defs=null, el=null;
				Node cur=null;
				String id="";
				
				for(int i=0; i<defsNodes.getLength(); i++) {
					
					defs=(Element)defsNodes.item(i);
					
					if(defs!=null) {

						//for each child of the "defs" element, adds its id to the map
						for(cur=defs.getFirstChild(); cur!=null; cur=cur.getNextSibling()) {

							if(cur instanceof Element && resourceTagNames.contains(cur.getNodeName())){
								
								el=(Element)cur;
								id=el.getAttribute("id");
								
								if(id!=null && ! id.equals("")){
									
									idResources.put(id, el);
								}
							}
						}
					}
				}
			}
		}
		
		return idResources;
	}
	
	/**
	 * @return the defs element of the document
	 */
	public Element getDefsElement(){
		
		//the element that will be returned
		Element defs=null;
		
		//getting the list of the defs nodes in the document
		NodeList defsNodes=
			handle.getCanvas().getDocument().getElementsByTagName("defs");
		
		if(defsNodes.getLength()>0){
			
			defs=(Element)defsNodes.item(0);
		}
		
		return defs;
	}

	/**
	 * @return the map associating an id to the linkedlist 
	 * of the elements associated with it
	 */
	public HashMap<String, LinkedList<Element>> getUsedResources() {
		
		return usedResources;
	}
}
