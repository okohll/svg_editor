package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the simple tag chooser
 * @author ITRIS, Jordi SUC
 */
public class DirectoryChooser extends Widget{
	
	/**
	 * the label
	 */
	private final JLabel jlabel=new JLabel();
	
	/**
	 * the button used to launch the dialog
	 */
	private final JButton moreButton=new JButton();
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected DirectoryChooser(boolean isEditor){
		
		super(isEditor);
		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){
		
		//getting the labels
		String directoryChooserLabel="", workingDirectoryDescription="";
		ResourceBundle bundle=ResourcesManager.bundle;
		
		try{
			directoryChooserLabel=bundle.getString("rtdaanim_tagchooserbutton");
			workingDirectoryDescription=bundle.getString("rtdaanim_workingdirectorydescription");
		}catch (Exception ex){}
			
		moreButton.setText(directoryChooserLabel);
		moreButton.setMargin(new Insets(1, 1, 1, 1));
		jlabel.setOpaque(false);
		
		setLayout(new BorderLayout(1, 0));
		add(jlabel, BorderLayout.CENTER);
		add(moreButton, BorderLayout.EAST);
		
		if(isEditor) {
			
			final String fworkingDirectoryDescription=workingDirectoryDescription;
			
			//adding the listener to the button
			moreButton.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {
					
					//the file chooser
					JFileChooser fileChooser=new JFileChooser();
					
					//creating the file filter
					javax.swing.filechooser.FileFilter fileFilter=new javax.swing.filechooser.FileFilter(){
						
						@Override
						public boolean accept(File file) {
							
							if(file!=null && file.isDirectory()){
								
								return true;
							}
							
							return false;
						}
						
						@Override
						public String getDescription() {
							
							return fworkingDirectoryDescription;
						}
					};
					
					fileChooser.setFileFilter(fileFilter);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					
					if(Editor.getEditor().getResourcesManager().getCurrentDirectory()!=null){
						
						fileChooser.setCurrentDirectory(Editor.getEditor().getResourcesManager().getCurrentDirectory());
					}
					
					int returnVal=fileChooser.showOpenDialog(Editor.getParent());
					
					if(returnVal==JFileChooser.APPROVE_OPTION) {
						
						Editor.getEditor().getResourcesManager().setCurrentDirectory(fileChooser.getCurrentDirectory());
						String path=fileChooser.getSelectedFile().toURI().toASCIIString();
						getItem().setValue(path);
					}
					
					if(validateRunnable!=null) {
						
						validateRunnable.run();
					}
				}
			});
		}
	}
	
	@Override
	protected void setItem(EditableItem item, Runnable validateRunnable){

		super.setItem(item, validateRunnable);
		jlabel.setText(item.getValue());
	}
}
