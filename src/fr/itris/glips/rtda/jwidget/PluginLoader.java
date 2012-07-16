/*
 * Created on 28 avr. 2005
 * 
 =============================================
 GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
 GLIPS Graffiti Editor, a SVG Editor
 Copyright (C) 2003 Jordi SUC, Philippe Gil, SARL ITRIS
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
 Contact : jordi.suc@itris.fr; philippe.gil@itris.fr
 
 =============================================
 */
package fr.itris.glips.rtda.jwidget;

import java.io.*;
import java.util.jar.*;
import java.util.*;
import java.net.*;
import java.lang.reflect.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * the class enabling to load the plugins
 * 
 * @author ITRIS, Jordi SUC
 */
public class PluginLoader {
    
	/**
	 * the class files extension
	 */
	protected static String classExtension=".class";
	
	/**
	 * the xml files extension
	 */
	protected static String xmlExtension=".xml";
	
	/**
	 * the jar files extension
	 */
	protected static String jarExtension=".jar";
	
	/**
	 * the resource separator
	 */
	protected static String resourceSep="/";
	
	/**
	 * the class separator
	 */
	protected static String classSep=".";
	
	/**
	 * the url class loader
	 */
	public static PluginURLClassLoader urlClassLoader=new PluginURLClassLoader();
	
    /**
     * the file of the location of the application
     */
    private static File applicationLocationFile=null;
    
    /**
     * the set of the jars files that are contained in the plugin directory
     */
    private static final Set<File> foundJars=new HashSet<File>();
    
    /**
     * the map associating an abstract class to the map associating the id of a plugin to this plugin class
     */
    private static final Map<Class<?>, LinkedHashMap<String, Class<?>>> abstractClassToPlugins=
                 new HashMap<Class<?>, LinkedHashMap<String, Class<?>>>();
    
    /**
     * the map associating an id to a document
     */
    private static final Map<String, Document> jwidgetIdToDoc=
    	new HashMap<String, Document>();
    
    /**
     * whether the jwidget documents are loaded or not
     */
    private static boolean jwidgetDocumentsLoaded=false;
    
    static{
        
        //computing the location of the application
        applicationLocationFile=new File(System.getProperty("user.dir"));
        
        //getting the list of the plugins path
        if(applicationLocationFile!=null && applicationLocationFile.exists()){
            
            //the directory of the plugins
            File pluginDirectory=null;
            File[] children=applicationLocationFile.listFiles();
            int i;
            
            if(children!=null){
                
                //for each children of the application directory, retrieves the "plugins" directory
                for(i=0; i<children.length; i++){
                    
                    if(children[i]!=null && children[i].getName().equals("GLIPSSVGPlugins")){
                        
                        pluginDirectory=children[i];
                    }
                }
                
                if(pluginDirectory!=null){
                    
                    //the children of the plugin directory
                    children=pluginDirectory.listFiles();
                    
                    if(children!=null){
                        
                        //for each child of the plugin directory, if this child is a ".jar" file,
                        //it is added to the list of the plugins
                        for(i=0; i<children.length; i++){
                            
                            if(children[i]!=null && children[i].getName().endsWith(jarExtension)){
                                
                                foundJars.add(children[i]);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * returns the plugin class corresponding to the given id
     * @param relatedAbstractClass an abstract class
     * @return the plugin class corresponding to the given id
     * @param id a plugin id
     */
    public static Class<?> getPlugin(Class<?> relatedAbstractClass, String id){
        
        Class<?> pluginClass=null;
        
        if(id!=null && ! id.equals("")){
            
            //checks if the abstract class has already been handled, if not, the plugins associated with 
        	//this abstract class will be loaded
            if(! abstractClassToPlugins.containsKey(relatedAbstractClass)){
                
                getPlugins(relatedAbstractClass);
            }
            
            Map<String, Class<?>> map=abstractClassToPlugins.get(relatedAbstractClass);
            
            if(map!=null){
                
                pluginClass=map.get(id);
            }
        }
        
        return pluginClass;
    }

    /**
     * returns the list of the classes contained in the plugin directory and that implements the given abstract class
     * @param relatedAbstractClass an abstract class
     * @return the list of the classes contained in the plugin directory and that implements the given abstract class
     */
    public static LinkedList<Class<?>> getPlugins(Class<?> relatedAbstractClass){
        
        LinkedList<Class<?>> pluginClasses=null;
        LinkedHashMap<String, Class<?>> pluginClassesMap=null;
        
        if(relatedAbstractClass!=null && foundJars.size()>0 && 
        		abstractClassToPlugins.containsKey(relatedAbstractClass)){
            
            //retrieving the map of the plugins that have already been computed
            pluginClassesMap=abstractClassToPlugins.get(relatedAbstractClass);
            
            if(pluginClassesMap!=null){
                
                pluginClasses=new LinkedList<Class<?>>();
                pluginClasses.addAll(pluginClassesMap.values());
            }
            
        }else if(relatedAbstractClass!=null && foundJars.size()>0){

            pluginClassesMap=new LinkedHashMap<String, Class<?>>();
            String id=null;
            Set<Class<?>> foundClasses=null;
            
            //for each jar file, retrieves the class that implements the given abstract class
            for(File file : foundJars){

                if(file!=null){
                    
                    //getting the class contained in the jar that implements the given abstract class
                    try{foundClasses=getClasses(file, relatedAbstractClass);}catch (Exception ex){foundClasses=null;}
                    
                    if(foundClasses!=null){
                    	
                    	for(Class<?> pluginClass : foundClasses) {
                    		
                            id=pluginClass.getName();
                            
                            if(id!=null && ! id.equals("")){

                                pluginClassesMap.put(id, pluginClass);
                            }
                    	}
                    }
                }
            }
            
            //storing the found plugin classes
            abstractClassToPlugins.put(relatedAbstractClass, pluginClassesMap);
            
            pluginClasses=new LinkedList<Class<?>>(pluginClassesMap.values());
        }
        
        return pluginClasses;
    }
    
    /**
     * returns the classes contained in the given file and that implements the given abstract class
     * @param file a jar file
     * @param relatedAbstractClass an abstract class
     * @return the classes contained in the given file and that implements the given abstract class
     */
    protected static HashSet<Class<?>> getClasses(File file, Class<?> relatedAbstractClass){
        
        HashSet<Class<?>> returnClasses=new HashSet<Class<?>>();
        
        if(file!=null && relatedAbstractClass!=null){
            
            JarFile jarFile =null;
            
            //creating the jar file
            try{jarFile=new JarFile(file);}catch (Exception ex){}
            
            //creating the url of the jar file
            URL url=null;
            try{url=file.toURI().toURL();}catch (Exception ex){}

            if(jarFile!=null && url!=null){
                
                //creating the class loader
                try{
                	urlClassLoader.addURL(url);
                }catch (Exception ex){}
                
                if(urlClassLoader!=null){
                    
                    //getting the enumeration of the files in the jar
                    Enumeration<JarEntry> enumeration=jarFile.entries();
                    
                    JarEntry entry=null;
                    String name="";
                    Class<?> loadedClass=null;
                        
                    //for each file in the jar, checks if it is a class and then, loads it
                    while(enumeration.hasMoreElements()){
                        
                        entry=enumeration.nextElement();
                        
                        if(entry!=null){
                            
                            name=entry.getName();
                            
                            if(name!=null && name.endsWith(classExtension)){
                                
                                name=pathToClassName(name);
                                
                                if(name!=null && ! name.equals("")){
                                    
                                    try{loadedClass=urlClassLoader.loadClass(name);}catch (Exception ex){loadedClass=null;}
 
                                    if(loadedClass!=null && ! Modifier.isAbstract(loadedClass.getModifiers()) && 
                                    	relatedAbstractClass.isAssignableFrom(loadedClass)){
                                        
                                        //the loaded class extends the abstract class
                                        returnClasses.add(loadedClass);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return returnClasses;
    }
    
    /**
     * returns the class name corresponding to the given path
     * @param path a path
     * @return the class name corresponding to the given path
     */
    protected static String pathToClassName(String path){
        
        String name="";
        
        if(path!=null && ! path.equals("") && path.endsWith(classExtension)){
            
            name=path.replace(resourceSep, classSep);
            name=name.substring(0, name.indexOf(classExtension));
        }
        
        return name;
    }
    
    /**
     * @return the map of the jwidget documents that can be found in the plugins
     */
    public static Map<String, Document> getJWidgetDocuments(){
    	
    	if(! jwidgetDocumentsLoaded) {
    		
			//loading all the jwidget documents that can be found in the jar plugins
			Map<String, Document> documents=loadJWidgetDocuments();

			if(documents!=null) {
				
				jwidgetIdToDoc.putAll(documents);
			}
    	}
    	
    	return jwidgetIdToDoc;
    }
    
    /**
     * returns the document associated with the given jwidget plugin id
     * @param id the id of a jwidget plugin
     * @return the document associated with the given jwidget plugin id
     */
    public static Document getJWidgetDocument(String id) {
    	
    	Document doc=null;
    	
    	if(id!=null && ! id.equals("")) {
    		
    		doc=getJWidgetDocuments().get(id);
    	}
    	
    	return doc;
    }
    
    /**
     * loads and returns the jwidget documents that can be found in the plugins
     * @return the map of the jwidget documents that can be found in the plugins
     */
    protected static Map<String, Document> loadJWidgetDocuments(){
    	
    	Map<String, Document> documents=new HashMap<String, Document>();
    	Map<String, Document> fileDocuments=null;

        for(File file : foundJars){

            if(file!=null){

            	fileDocuments=getJWidgetDocument(file);
            	
            	if(fileDocuments!=null && fileDocuments.size()>0) {
            		
            		documents.putAll(fileDocuments);
            	}
            }
        }

        jwidgetDocumentsLoaded=true;
    	return documents;
    }
    
    /**
     * returns the map associating the id of a jwidget plugin to its document
     * @param file a jar file
     * @return the map associating the id of a jwidget plugin to its document
     */
    protected static Map<String, Document> getJWidgetDocument(File file){
    	
    	Map<String, Document> documents=new HashMap<String, Document>();
    	
       if(file!=null){
    	   
    	   DocumentBuilder documentBuilder=null;
    	   
    	   try{
    		   documentBuilder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
    	   }catch (Exception ex) {}
            
            JarFile jarFile =null;
            
            //creating the jar file
            try{jarFile=new JarFile(file);}catch (Exception ex){}

            if(jarFile!=null) {
            	
            	//getting the enumeration of the files in the jar
                Enumeration<JarEntry> enumeration=jarFile.entries();
                
                JarEntry entry=null;
                String name="", id="";
                Document doc=null;
                int pos=-1;
                    
                //for each file in the jar, checks if it is a class and then, loads it
                while(enumeration.hasMoreElements()){
                    
                    entry=enumeration.nextElement();
                    
                    if(entry!=null){
                        
                        name=entry.getName();

                        if(name!=null && name.endsWith(xmlExtension)){
                            
                        	//getting the id 
                            pos=name.lastIndexOf(resourceSep);
                            
                            if(pos!=-1) {
                            
                                id=name.substring(pos+resourceSep.length(), name.length()-xmlExtension.length());
                            	
                            }else {
                            	
                                id=name.substring(0, name.length()-xmlExtension.length());
                            }
                            
                            if(id!=null && ! id.equals("") && documentBuilder!=null) {
                            	
                                //creating the document
                            	try{
                            		doc=documentBuilder.parse(jarFile.getInputStream(entry));
                            	}catch (Exception ex) {doc=null;}
                            	
                            	if(doc!=null) {
                            		
                            		documents.put(id, doc);
                            	}
                            }
                        }
                    }
                }
            }
        }
    	
    	return documents;
    }
    
    /**
     * the class of a url class loader
     *
     * @author ITRIS, Jordi SUC
     */
    protected static class PluginURLClassLoader extends URLClassLoader{
		
    	/**
    	 * the constructor of the class
    	 */
    	public PluginURLClassLoader() {
    		
    		super(new URL[] {});
    	}
    	
		@Override
		public void addURL(URL url) {

			super.addURL(url);
		}
	}
}
