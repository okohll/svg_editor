package fr.itris.glips.svgeditor;

import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

/**
 * the class of the applet for the editor
 * @author Jordi SUC
 */
public class EditorApplet extends JApplet{

	/**
	 * the editor
	 */
	private Editor editor=null;
	
	@Override
	public void init() {
		
	    try {
	        SwingUtilities.invokeAndWait(new Runnable() {
	        	
	            public void run() {

	                createGUI();
	            }
	        });
	    } catch (InterruptedException e) {
	    		Thread.currentThread().interrupt();
	    	} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * creates the GUI
	 */
	private void createGUI() {

		//creating the editor object
		editor=new Editor();
		
		//intializing the editor
		editor.init(this, "", false, false, true, false, null);
	}
	
	@Override
	public void destroy() {

		if(editor!=null){

			editor.dispose();
		}
		
		super.destroy();
	}
}
