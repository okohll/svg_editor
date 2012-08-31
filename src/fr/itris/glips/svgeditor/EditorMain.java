package fr.itris.glips.svgeditor;

import java.awt.event.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the enter point of the application 
 * @author ITRIS, Jordi SUC
 */
public class EditorMain {

	/**
	 * the constructor of the class
	 *@param fileName the name of a svg file
	 */
	public EditorMain(String fileName){

		//creating the editor object
		final Editor editor=new Editor();
		
		//creating the parent frame of the editor
		JFrame mainFrame=new JFrame();
		mainFrame.setTitle("GLIPS Graffiti Editor");
		
		//handling the close case
		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(new WindowAdapter(){
			
			@Override
			public void windowClosing(WindowEvent evt) {

				editor.exit();
			}
		});
		
		//setting the icon
		ImageIcon icon2=ResourcesManager.getIcon("Editor", false);
		
		if(icon2!=null && icon2.getImage()!=null){
			
			mainFrame.setIconImage(icon2.getImage());
		}

		//intializing the editor
		editor.init(mainFrame, fileName, true, false, true, null);
	}
	
	/**
	 * the main method
	 * @param args the array of arguments
	 */
	public static void main(String[] args) {
		
		String fileName="";
		
		if(args!=null && args.length>0){
			
			fileName=args[0];
		}
		
		final String ffileName=fileName;
		
		SwingUtilities.invokeLater(new Runnable(){
			
			public void run() {

				new EditorMain(ffileName);
			}
		});
	}
}
