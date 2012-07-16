package fr.itris.glips.compiler.rtdb;

import java.util.*;
import java.io.*;

/**
 * the class used to iterate over a file tree
 * @author ITRIS, Jordi SUC
 */
public class FileIterator implements Iterator<File>{

	/**
	 * the root file
	 */
	private File rootFile;
	
	/**
	 * the current file
	 */
	private File currentFile=null;
	
	/**
	 * the constructor of the class
	 * @param rootFile the root file
	 */
	public FileIterator(File rootFile) {
		
		this.rootFile=rootFile;
		this.currentFile=rootFile;
	}
	
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		
		boolean hasNext=false;
		File cFile=currentFile;
		
		if(cFile!=null) {
			
			//checking if the current file has children or a next sibling
			if(hasChildren(cFile) || getNextSibling(cFile)!=null) {
				
				hasNext=true;
				
			}else{
				
				//checking if one of the ancestors of the file has got a next sibling
				cFile=cFile.getParentFile();

				while(! cFile.equals(rootFile)) {
					
					if(getNextSibling(cFile)!=null) {
						
						hasNext=true;
						break;
					}
					
					cFile=cFile.getParentFile();
				}
			}
		}

		return hasNext;
	}
	
	/**
	 * @see java.util.Iterator#next()
	 */
	public File next() {
		
		File nextFile=null;
		
		if(currentFile!=null) {
			
			File cFile=currentFile;
			
			//checking if the current file has children
			if(hasChildren(cFile)) {
				
				//getting the first child file of the current file
				nextFile=getFirstChild(cFile);
				
			}else{
				
				//checking if the current file or one of its ancestors have got a next sibling
				File nextSibling=null;

				while(! cFile.equals(rootFile)) {
					
					//computing the next sibling
					nextSibling=getNextSibling(cFile);
					
					if(nextSibling!=null) {
						
						//as the next sibling exists, it is used
						nextFile=nextSibling;
						break;
					}
					
					cFile=cFile.getParentFile();
				}
			}
		}
		
		currentFile=nextFile;
		
		return nextFile;
	}
	
	/**
	 * returns the first child file of the given file
	 * @param file a file
	 * @return the first child file of the given file
	 */
	protected File getFirstChild(File file) {
		
		File firstChild=null;
		
		if(file!=null) {
			
			//getting the child files of the given file
			File[] children=file.listFiles();
			Arrays.sort(children);
			
			if(children!=null && children.length>0) {
				
				firstChild=children[0];
			}
		}
		
		return firstChild;
	}
	
	/**
	 * returns the next sibling of the given file
	 * @param file a file
	 * @return the next sibling of the given file
	 */
	protected File getNextSibling(File file) {
		
		File nextSibling=null;
		
		if(file!=null && file.getParentFile()!=null) {
			
			//getting the child files of the parent file of the given file
			//and retrieves the next sibling of this file
			File[] children=file.getParentFile().listFiles();
			Arrays.sort(children);
			
			if(children!=null) {
				
				for(int i=0; i<children.length; i++) {
					
					if(children[i].equals(file)) {
					
						//getting the file that is next the given file
						if((i+1)<children.length) {
							
							nextSibling=children[i+1];
						}
						
						break;
					}
				}
			}
		}
		
		return nextSibling;
	}
	
	/**
	 * checks whether the given file has got children
	 * @param file a file
	 * @return whether the given file has got children
	 */
	protected boolean hasChildren(File file) {
		
		if(file!=null) {
			
			File[] children=file.listFiles();
			
			if(children!=null && children.length>0) {
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {}
}
