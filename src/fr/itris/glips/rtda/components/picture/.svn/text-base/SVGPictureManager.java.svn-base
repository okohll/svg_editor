package fr.itris.glips.rtda.components.picture;

import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.config.*;
import fr.itris.glips.rtda.toolkit.*;

/**
 * the class of the manager of the svg pictures
 * @author ITRIS, Jordi SUC
 */
public class SVGPictureManager {

	/**
	 * the strings
	 */
	protected static String viewsTagName="views", viewTagName="view", 
		xmlPathAttribute="xmlPath", uriAttribute="uri";
	
	/**
	 * the map of all the loaded svg pictures, associating the id of a picture to this picture
	 */
	protected Map<String, SVGPicture> pictures=new HashMap<String, SVGPicture>();
	
	/**
	 * the map of all the xml configuration documents, associating a project name to its related 
	 * configuration document
	 */
	protected Map<String, ConfigurationDocument> configurationDocuments=
		new HashMap<String, ConfigurationDocument>();
	
	/**
	 * the map associating the name of a project to the map associating the xml path of a svg file to its uri
	 */
	protected Map<String, Map<String, String>> projectNameToViewPathsToUriMap=
			new HashMap<String, Map<String, String>>();
	
	/**
	 * the map associating the name of a project to the map associating the uri of a svg file to its xml path
	 */
	protected Map<String, Map<String, String>> projectNameToUriToXMLPathMap=
			new HashMap<String, Map<String, String>>();
	
	/**
	 * registers this svg picture
	 * @param picture a picture
	 */
	public void addPicture(SVGPicture picture) {
		
		pictures.put(picture.getUri(), picture);

		//getting the project name corresponding to the picture
		String projectName=picture.getCanvas().getProjectName();

		if(projectName!=null && ! projectName.equals("") && 
			! projectNameToViewPathsToUriMap.containsKey(projectName)) {
			
			//checking if the configuration document already exists for 
			//this project file, if not, it is created
			if(! configurationDocuments.containsKey(projectName)){
				
				ConfigurationDocument configurationDocument=new ConfigurationDocument(picture);
				configurationDocuments.put(projectName, configurationDocument);
			}
			
			//getting the map associating a view path to a svg file
			Map<String, String> map=getViewsPathToUriMap(configurationDocuments.get(projectName));

			//getting the absolute path of the container of the files for this project
			String startPath=picture.getCanvas().getProjectFile().toURI().toASCIIString();
			
			if(! startPath.equals("") && ! startPath.endsWith("/")) {
				
				startPath+="/";
			}
			
			//putting the uris into an absolute form
			Map<String, String> normalizedMap1=new HashMap<String, String>();
			Map<String, String> normalizedMap2=new HashMap<String, String>();
			String uri="", longUri="";
			
			for(String xmlPath : map.keySet()) {
				
				uri=map.get(xmlPath);
				
				if(uri.startsWith("/")) {
					
					uri=uri.substring(1, uri.length());
				}
				
				longUri=startPath+uri;
				normalizedMap1.put(xmlPath, longUri);
				normalizedMap2.put(uri, xmlPath);
			}

			projectNameToViewPathsToUriMap.put(projectName, normalizedMap1);
			projectNameToUriToXMLPathMap.put(projectName, normalizedMap2);
		}
	}
	
	/**
	 * unregisters this svg picture
	 * @param picture a picture
	 */
	public void removePicture(SVGPicture picture) {
		
		pictures.remove(picture.getUri());
		
		//checking if the map of the view path contains unused entries//
		
		//filling the set of all the used project names
		Set<String> usedProjectNames=new HashSet<String>();
		
		for(SVGPicture pict : pictures.values()) {
			
			if(pict!=null) {
				
				usedProjectNames.add(pict.getCanvas().getProjectName());
			}
		}
		
		//computing the unused project file names
		Set<String> unusedProjectNames=
			new HashSet<String>(projectNameToViewPathsToUriMap.keySet());
		unusedProjectNames.removeAll(usedProjectNames);
		
		if(unusedProjectNames.size()>0) {
			
			//removing from the views map all the unused project names
			for(String name : unusedProjectNames) {
				
				projectNameToViewPathsToUriMap.remove(name);
				projectNameToUriToXMLPathMap.remove(name);
				configurationDocuments.remove(name);
			}
		}
	}
	
    /**
     * disposes all the previously loaded canvases
     */
    public void disposeAllPictures(){

        //disposes all the stored pictures
        for(SVGPicture picture : new LinkedList<SVGPicture>(pictures.values())){

            if(picture!=null){
                
                picture.dispose();
            }
        }
    	
    	pictures.clear();
    	projectNameToViewPathsToUriMap.clear();
    	projectNameToUriToXMLPathMap.clear();
    	configurationDocuments.clear();
    }
	
    /**
     * returns the picture having the given uri
     * @param uri a picture uri
     * @return the picture having the given uri
     */
	public SVGPicture getPicture(String uri) {

		return pictures.get(uri);
	}
	
	/**
	 * @return the set of the registered pictures
	 */
	public Set<SVGPicture> getPictures(){
		
		return new HashSet<SVGPicture>(pictures.values());
	}
	
	/**
	 * returns the uri of the view corresponding to the given xml view path
	 * @param projectFileName the name of a project fike
	 * @param xmlViewPath a xml view path
	 * @return the uri of the view corresponding to the given xml view path
	 */
	public String getViewUri(String projectFileName, String xmlViewPath) {
		
		String viewUri="";
		
		if(projectFileName!=null && ! projectFileName.equals("") && 
			xmlViewPath!=null && ! xmlViewPath.equals("")){
			
			Map<String, String> map=projectNameToViewPathsToUriMap.get(projectFileName);
			
			if(map!=null) {
				
				viewUri=map.get(xmlViewPath);
			}
		}
		
		return viewUri;
	}
	
	/**
	 * returns the configuration document corresponding to the given feature
	 * @param picture a svg picture
	 * @return the configuration document corresponding to the given feature
	 */
	public ConfigurationDocument getConfigurationDocument(SVGPicture picture){
		
		return configurationDocuments.get(picture.getCanvas().getProjectName());
	}
	
	/**
	 * returns the uri of the view corresponding to the given xml view path
	 * @param projectFileName the name of a project fike
	 * @param uri the uri of a view
	 * @return the uri of the view corresponding to the given xml view path
	 */
	public String getViewXMLPath(String projectFileName, String uri) {

		String viewUri="";
		
		if(projectFileName!=null && ! projectFileName.equals("") && 
			uri!=null && ! uri.equals("")){
			
			int pos=uri.lastIndexOf("/");
			
			if(pos!=-1) {
				
				uri=uri.substring(pos+1, uri.length());
			}
			
			Map<String, String> map=projectNameToUriToXMLPathMap.get(projectFileName);
			
			if(map!=null) {
				
				viewUri=map.get(uri);
			}
		}
		
		return viewUri;
	}
	
	/**
	 * returns the map associating the xml path of a view to uri of the corresponding svg file
	 * @param confDoc the document of the xml data base
	 * @return the map of the xml path and uris
	 */
	public Map<String, String> getViewsPathToUriMap(ConfigurationDocument confDoc){

		Map<String, String> map=new HashMap<String, String>();
		
		if(confDoc!=null){
			
			NodeList allViewElements=confDoc.
				getRootElement().getElementsByTagName("view");
			
			//for each element, getting the view path and its location
			Node node=null;
			Element viewElement=null;
			String path="", location="";
			
			for(int i=0; i<allViewElements.getLength(); i++){
				
				node=allViewElements.item(i);
				
				if(node!=null && node instanceof Element){
					
					viewElement=(Element)node;
					
					//getting the path corresponding to the view element
					path=TagToolkit.getPath(viewElement);
					
					if(path!=null && ! path.equals("")){
						
						location=viewElement.getAttribute("location");
						map.put(path, location);
					}
				}
			}
		}

		return map;
	}
}
