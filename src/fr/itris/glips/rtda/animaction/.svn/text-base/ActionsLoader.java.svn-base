package fr.itris.glips.rtda.animaction;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtda.database.*;

/**
 * the class of the loader of the actions for a project
 * @author ITRIS, Jordi SUC
 */
public class ActionsLoader {
	
	/**
	 * the name of the directory of the action classes that can be found in each project
	 */
	public static String actionClassesDirectoryName="actions";
	
	/**
	 * the suffix of each class
	 */
	public static String classSuffix=".class";
	
	/**
	 * the map associating the name of a project to the set of all the classes that can be found in this project
	 */
	public static Map<String, Set<Class<?>>> projectClasses=
		new ConcurrentHashMap<String, Set<Class<?>>>();
	
	/**
	 * removes the project denoted by the given name from the classes map
	 * @param projectName the name of a project
	 */
	public static void removeClasses(String projectName) {
		
		projectClasses.remove(projectName);
	}
	
	/**
	 * returns the list of the action class names that can be found in the project and its referenced projects
	 * @param superClass the super class of the classes whose name should be retrieved
	 * @param projectFile the project file
	 * @param isEditionMode whether the mode, into which the actions are retrieved, is the edition or runtime mode
	 * @return the list of the action class names
	 */
	public static LinkedList<String> getClassNames(Class<?> superClass, File projectFile, boolean isEditionMode){
		
		LinkedList<String> classNames=new LinkedList<String>();
		
		//getting the set of the available classes
		Set<Class<?>> classes=getClasses(superClass, projectFile, isEditionMode);

		if(classes!=null && classes.size()>0) {
			
			for(Class<?> foundClass : classes) {
				
				if(foundClass!=null) {
					
					classNames.add(foundClass.getName());
				}
			}
		}
		
		//sorting the list of the class names
		Collections.sort(classNames);
		
		return classNames;
	}
	
	/**
	 * return the class object corresponding to the given class name in the given project
	 * @param superClass the super class of the classes whose name should be retrieved
	 * @param projectFile a project file 
	 * @param className the name of a class
	 * @param isEditionMode  whether the mode, into which the actions are retrieved, is the edition or runtime mode
	 * @return the class object corresponding to the given class name in the given project
	 */
	public static Class<?> getClass(Class<?> superClass, File projectFile, String className, boolean isEditionMode) {
		
		Class<?> theClass=null;
		
		if(projectFile!=null && className!=null && ! className.equals("")) {
			
			//getting all the classes  that can be found in the given project
			Set<Class<?>> classes=getClasses(superClass, projectFile, isEditionMode);
			
			if(classes!=null) {
				
				for(Class<?> aClass : classes) {
					
					if(aClass.getName().equals(className)) {
						
						theClass=aClass;
						break;
					}
				}
			}
		}
		
		return theClass;
	}
	
	/**
	 * returns the list of the classes of the actions that can be found in the project and its referenced projects
	 * @param superClass the super class of the classes whose name should be retrieved
	 * @param projectFile a project file
	 * @param isEditionMode whether the mode, into which the actions are retrieved, is the edition or runtime mode
	 * @return the list of the classes of the actions
	 */
	public static Set<Class<?>> getClasses(Class<?> superClass, File projectFile, boolean isEditionMode){
		
		Set<Class<?>> classes=new HashSet<Class<?>>();
		
		if(projectFile!=null) {
		
			//getting the name of the project file
			String projectName=getProjectName(projectFile);
			
			if(projectClasses.containsKey(projectName)) {
				
				Set<Class<?>> classesSet=projectClasses.get(projectName);
				classes.addAll(classesSet);

			}else{
				
				if(isEditionMode) {

					//getting all the referenced project files//
					
					//getting the set of the names of all the referenced projects by the given project
					Set<String> allProjectNames=new HashSet<String>(
						DataBaseToolkit.getLibPaths(DataBaseToolkit.getLibPathsFile(
								projectFile.getParentFile().toURI().toASCIIString(), Toolkit.getFileName(projectFile))));
					allProjectNames.add(Toolkit.getFileName(projectFile));

					Set<File> allSuperProjects=new HashSet<File>();
					File workspace=projectFile.getParentFile();
					File[] workspaceChildren=workspace.listFiles();
					String theName="", theURI="";
					int pos=0;

					for(String name : allProjectNames) {
						
						if(name!=null && ! name.equals("")) {
							
							for(File file : workspaceChildren) {

								if(file!=null && file.isDirectory()) {
									
									theURI=file.toURI().toASCIIString();
									
									if(theURI.endsWith("/")){
                    					
										theURI=theURI.substring(0, theURI.length()-1);
                    				}
                    				
                    				pos=theURI.lastIndexOf("/");

                    				if(pos!=-1){
                    					
                    					theName=theURI.substring(pos+1, theURI.length());
                    					
                            			if(theName.equals(name)) {
                            				
                            				allSuperProjects.add(file);
                            				break;
                            			}
                    				}
								}
							}
						}
					}

					//getting the classes associated with each found project file
					Set<Class<?>> foundClasses=null;

					for(File file : allSuperProjects) {
						
						if(file!=null) {
							
							foundClasses=retrieveClasses(superClass, file);
							
							if(foundClasses!=null && foundClasses.size()>0) {
								
								classes.addAll(foundClasses);
							}
						}
					}

				}else {
					
					//getting all the action classes that can be found in the given project file
					Set<Class<?>> foundClasses=retrieveClasses(superClass, projectFile);
					
					if(foundClasses!=null && foundClasses.size()>0) {
						
						classes.addAll(foundClasses);
					}
				}
				
				//putting the classes into the map associating a project name to its set of classes
				Set<Class<?>> projectClassesSet=new CopyOnWriteArraySet<Class<?>>();
				projectClassesSet.addAll(classes);
				projectClasses.put(projectName, projectClassesSet);
			}
			
			//modifying the classes set, by only keeping those who extend the given super class
			Set<Class<?>> filteredClasses=new HashSet<Class<?>>();
			
			for(Class<?> aClass : classes) {
				
				if(! Modifier.isAbstract(aClass.getModifiers()) &&
					((Class<?>)superClass).isAssignableFrom(aClass)) {
					
					filteredClasses.add(aClass);
				}
				
				classes=filteredClasses;
			}
		}
		
		return classes;
	}
	
	/**
	 * computes the set of the classes specified in the project denoted by the given project file
	 * @param superClass the super class of the classes whose name should be retrieved
	 * @param projectFile a project file
	 * @return the set of the classes specified in the project denoted by the given project file
	 */
	protected static Set<Class<?>> retrieveClasses(Class<?> superClass, File projectFile){

		Set<Class<?>> classesSet=new CopyOnWriteArraySet<Class<?>>();
		
		if(projectFile!=null) {
			
			//getting the directory of the action classes in the project
			File[] children=projectFile.listFiles();
			File classesDirectory=null;
			
			if(children!=null) {
				
				for(File file : children) {
					
					if(file!=null && file.getName().equals(actionClassesDirectoryName)) {
						
						classesDirectory=file;
						break;
					}
				}
			}

			if(classesDirectory!=null) {
				
				//creating the class loader associated with the classes directory
				ActionURLClassLoader classLoader=null;
				
				try{
					classLoader=new ActionURLClassLoader(new URL[] {classesDirectory.toURI().toURL()});
				}catch (Exception ex) {}
				
				if(classLoader!=null) {
					
					//getting all the class files that can be found in this directory and its sub directories
					LinkedList<File> subFiles=new LinkedList<File>();
					LinkedList<File> classFiles=new LinkedList<File>();
					subFiles.add(classesDirectory);
					File[] files=null;
					File file=null, subFile=null;
					
					for(int i=0; i<subFiles.size(); i++) {
						
						file=subFiles.get(i);
						
						if(file!=null) {
							
							files=file.listFiles();
							
							if(files!=null) {
								
								//for each sub file
								for(int j=0; j<files.length; j++) {
									
									subFile=files[j];
									
									if(subFile!=null) {
										
										if(subFile.isDirectory()) {
											
											subFiles.add(subFile);
											
										}else if(subFile.getName().endsWith(classSuffix)){

											classFiles.add(subFile);
										}
									}
								}
							}
						}
					}

					//getting the classes corresponding to the class files
					Class<?> foundClass=null;
					InputStream in=null;
					ByteArrayOutputStream out=null;
					byte [] tab=new byte[1000];
					int nb=-1;

					for(File classFile : classFiles) {
						
						try {
							//getting the byte code of the file to create the class
							in=new BufferedInputStream(new FileInputStream(classFile));
							out=new ByteArrayOutputStream();
							
							while(in.available()>0) {
								
								nb=in.read(tab);
								
								if(nb>0) {
									
									out.write(tab, 0, nb);
								}
							}
							
							out.flush();
							out.close();
							in.close();
							foundClass=classLoader.defineNewClass(out.toByteArray());

							if(foundClass!=null) {
								
								classesSet.add(foundClass);
							}							
						}catch (Exception ex) {foundClass=null;ex.printStackTrace();}
					}
					
					projectClasses.put(getProjectName(projectFile), classesSet);
				}
			}
		}
		
		return classesSet;
	}
	
	/**
	 * returns the class name corresponding to the class file
	 * @param rootFile the file from which the class file name should be computed
	 * @param classFile the class file
	 * @return the class name corresponding to the class file
	 */
	protected static String pathToClassName(File rootFile, File classFile){
		
		String name="";
		
		if(rootFile!=null) {
			
			LinkedList<String> segments=new LinkedList<String>();
			
			while(! classFile.equals(rootFile)) {
				
				segments.add(classFile.getName());
				classFile=classFile.getParentFile();
			}
			
			//creating the class name
			for(String str : segments) {
				
				if(str!=null) {
					
					name=str+(name.equals("")?"":".")+name;
				}
			}
		}
		
		return name;
	}
	
    /**
     * returns the name of the project
     * @param projectFile the file of a project
     * @return the name of a project
     */
    public static String getProjectName(File projectFile){
        
        return Toolkit.getFileName(projectFile);
    }
	
	/**
	 * the class of a url class loader
	 * @author ITRIS, Jordi SUC
	 */
	protected static class ActionURLClassLoader extends URLClassLoader{
		
		/**
		 * the constructor of the class
		 * @param urls the array of the urls
		 */
		public ActionURLClassLoader(URL[] urls) {
			
			super(urls);
		}
		
		/**
		 * defines a new class
		 * @param content the array of bytes for this class
		 * @return the new class
		 */
		public Class<?> defineNewClass(byte[] content) {
			
			return defineClass(null, content, 0, content.length);
		}
	}
}
