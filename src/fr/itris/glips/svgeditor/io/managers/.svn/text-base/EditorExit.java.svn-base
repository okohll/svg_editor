package fr.itris.glips.svgeditor.io.managers;

import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the manager enabling to exit the editor
 * @author Jordi SUC
 */
public class EditorExit {

	/**
	 * the labels
	 */
	private String warningTitle="", warningMessage="";
	
	/**
	 * the io manager
	 */
	private IOManager ioManager;
	
	/**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
	public EditorExit(IOManager ioManager){
		
		this.ioManager=ioManager;
		warningTitle=ResourcesManager.bundle.getString("EditorExitWarningTitle");
		warningMessage=ResourcesManager.bundle.getString("EditorExitWarningMessage");
	}
	
	/**
     * exits the editor
     */
    public void exit(){
        
    	//saving the current state of the editor
        Editor.getEditor().getResourcesManager().saveEditorsCurrentState();
        
        if(Editor.getEditor().isQuitActionDisabled()) {
            
        	//closing all the files that are currently being edited
        	ioManager.getFileCloseManager().closeAll();
        	
            //hiding the editor's frame
            Editor.getEditor().setVisible(false);
            
        }else {
            
            //quitting the editor
            //checks if some svg documents have been modified
            boolean displayDialog=false;
            Collection<SVGHandle> handles=
            	new LinkedList<SVGHandle>(
            		Editor.getEditor().getHandlesManager().getHandles());
            
            if(handles.size()>0){
                
                for(SVGHandle hnd : handles){

                    if(hnd!=null && hnd.isModified()){
                        
                        displayDialog=true;
                        break;
                    }
                }   
            }
            
            boolean canExitEditor=Editor.getEditor().canExitFromJVM();
            
            if(displayDialog){
            	
                //if svg documents have not been save, an alert dialog is displayed
                int returnVal=JOptionPane.showConfirmDialog(
                	Editor.getParent(), warningMessage, 
                		warningTitle, JOptionPane.YES_NO_OPTION); 
                
                canExitEditor=(canExitEditor && returnVal==JOptionPane.YES_OPTION);
            }

            if(canExitEditor) {
                
            	Editor.getEditor().dispose();
                System.exit(0);
            }
        }
    }
    
}