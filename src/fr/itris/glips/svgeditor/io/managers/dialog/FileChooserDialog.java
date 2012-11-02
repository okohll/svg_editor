package fr.itris.glips.svgeditor.io.managers.dialog;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import libraries.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of a file chooser dialog
 * @author Jordi SUC
 */
public class FileChooserDialog extends TitledDialog {
	
	/**
	 * the constant for the open file mode
	 */
	public static final int OPEN_FILE_MODE=0;
	
	/**
	 * the constant for the save file mode
	 */
	public static final int SAVE_FILE_MODE=1;
	
	/**
	 * the constant for the export file mode
	 */
	public static final int EXPORT_FILE_MODE=2;
	
	/**
	 * the file chooser used to select a file
	 */
	private JFileChooser fileChooser;
	
	/**
	 * the mode of the file chooser
	 */
	private final int mode;
	
	/**
	 * the labels
	 */
	private String titleLabel="", messageLabel="", 
		fileMissingErrorLabel="", wrongTypeErrorLabel="", 
			fileWillBeErasedWarningLabel="", fileNotExistError="";
	
	/**
	 * the selected file
	 */
	private File selectedFile;
	
	/**
	 * the selected files
	 */
	private File[] selectedFiles;
	
	/**
	 * the file filter
	 */
	private AbstractFileFilter fileFilter;

	/**
	 * a constructor of the class
	 * @param parent the parent frame
	 * @param mode the mode of the file chooser
	 */
	public FileChooserDialog(Frame parent, int mode) {
		
		super(parent, true, true);
		this.mode=mode;
		initialize();
	}
	
	/**
	 * a constructor of the class
	 * @param parent the parent dialog
	 * @param mode the mode of the file chooser
	 */
	public FileChooserDialog(JDialog parent, int mode) {
		
		super(parent, true);
		this.mode=mode;
		initialize();
	}
	
	/**
	 * initializes the dialog 
	 */
	protected void initialize(){
		//setting the properties to the file chooser
		if (mode == SAVE_FILE_MODE) {
			fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		}

		fileChooser.setMultiSelectionEnabled(
				mode==OPEN_FILE_MODE);
			
		//getting the label
		String radix=getRadix();
		titleLabel=ResourcesManager.bundle.getString(radix+"Title");
		messageLabel=ResourcesManager.bundle.getString(radix+"Message");
		fileMissingErrorLabel=
			ResourcesManager.bundle.getString(radix+"MissingError");
		wrongTypeErrorLabel=
			ResourcesManager.bundle.getString(radix+"WrongTypeError");
		
		if(mode!=OPEN_FILE_MODE ){
			
			fileWillBeErasedWarningLabel=
				ResourcesManager.bundle.getString(radix+"WillBeErasedWarning");
			
		}else{
			
			fileNotExistError=ResourcesManager.bundle.getString(radix+"NotExistError");
		}
		
		//setting the title and the message
		setTitleMessage(titleLabel);
		setMessage(messageLabel, INFORMATION_TYPE);
		
		//adding the listener to the window closing event
		WindowAdapter windowsListener=new WindowAdapter(){
			
			@Override
			public void windowClosing(WindowEvent e) {

				selectedFile=null;
				selectedFiles=null;
				setVisible(false);
			}
		};
		
		addWindowListener(windowsListener);
	}
	
	/**
	 * @return the file selected by the user
	 */
	public File getSelectedFile() {
		return selectedFile;
	}
	
	/**
	 * @return the array of the selected files by the user
	 */
	public File[] getSelectedFiles() {
		return selectedFiles;
	}
	
	@Override
	protected JPanel buildContentPanel() {
		System.out.println("Building content panel with mode " + mode);
		//creating the file chooser
		fileChooser=new JFileChooser();
		fileChooser.setControlButtonsAreShown(false);
		fileChooser.setFileHidingEnabled(true);
		fileChooser.resetKeyboardActions();
		final JTextField textField=getTextField(fileChooser);
		
		textField.addCaretListener(new CaretListener(){
			
			public void caretUpdate(CaretEvent e) {
				
				//computing the file corresponding to the text field content
				String content=URIEncoderDecoder.encode(textField.getText());
				System.out.println("Selected file string: " + content);
				
				if(content!=null && ! content.equals("")){
					
					if(mode==OPEN_FILE_MODE){
						
						checkFilesConsistency(getFiles(
							fileChooser.getCurrentDirectory(), content));

					}else{
						
						File file=null;
						
							try {
								file=new File(new URI(
								fileChooser.getCurrentDirectory().toURI().toASCIIString()+content));
							} catch (URISyntaxException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
						if(file!=null){

				            checkFileConsistency(file);
						}
					}
				}
			}
		}) ;

		//creating the content pane
		JPanel contentPane=new JPanel();
		contentPane.setLayout(
			new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.add(fileChooser);
		fileChooser.getInputMap().clear();
		
		//adding a listener to the properties changes on the file chooser
		PropertyChangeListener propertyChangeListener=new PropertyChangeListener(){
			
			public void propertyChange(PropertyChangeEvent evt) {

		        String prop=evt.getPropertyName();

		        //the directory changed
		        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {

		        	//a file has been selected
		            checkFileConsistency((File)evt.getNewValue());
		        	
		        }else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
		        	
		        	//a file has been selected
		            checkFileConsistency((File)evt.getNewValue());

		        }else if (JFileChooser.SELECTED_FILES_CHANGED_PROPERTY.equals(prop)) {
		        	
		        	//many files have been selected
		        	checkFilesConsistency((File[])evt.getNewValue());
		        }
			}
		};
		
		fileChooser.addPropertyChangeListener(propertyChangeListener);
		
		//disabling the ok button
		okButton.setEnabled(false);
		
		//adding the listener to the ok button
		ActionListener okListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
	
				//storing the filechooser's current directory
				Editor.getEditor().getResourcesManager().setCurrentDirectory(
						fileChooser.getCurrentDirectory());
				setVisible(false);
			}
		};
		
		//adding the listener to the cancel button
		ActionListener cancelListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
	
				selectedFile=null;
				selectedFiles=null;
				fileChooser.setSelectedFile(null);
				fileChooser.setSelectedFiles(null);
				setVisible(false);
			}
		};
		
		okButtonListener=okListener;
		cancelButtonListener=cancelListener;
		
		okButton.addActionListener(okButtonListener);
		cancelButton.addActionListener(cancelButtonListener);
		
		return contentPane;
	}
	
	/**
	 * sets the current file filter for the file chooser
	 * @param fileFilter a file filter
	 */
	public void setFileFilter(AbstractFileFilter fileFilter){
		
		this.fileFilter=fileFilter;
        fileChooser.setFileFilter(fileFilter);
	}
	
	@Override
	public void showDialog(final JComponent relativeComponent) {

		//setting the current directory to the file chooser
		fileChooser.setCurrentDirectory(
				Editor.getEditor().getResourcesManager().getCurrentDirectory());
		fileChooser.rescanCurrentDirectory();
		
		okButton.requestFocusInWindow();
		
		FileChooserDialog.super.showDialog(/*relativeComponent*/null);
	}
	
	/**
	 * checks if the provided file is consistent for the save action
	 * @param file a file
	 */
	protected void checkFileConsistency(File file){
		
		String message=messageLabel;
		
		if(file!=null && ! file.isDirectory()){
			
			if(isFileConsistent(file)){
				
				//if the file has no extension, one is added
				if(file.getName().indexOf(".")==-1){
					
						try {
							file=new File(new URI(
								file.toURI().toASCIIString()+fileFilter.getDefaultExtension()));
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				
				if(file.exists()){
					
					//a warning message is displayed since the file will be erased
					setMessage(fileWillBeErasedWarningLabel, ERROR_TYPE);
					
				}else{
					
					setMessage(messageLabel, INFORMATION_TYPE);
				}
				
				//the file is stored
				selectedFile=file;
				
				//enabling the ok button
				okButton.setEnabled(true);
				return;
				
			}else{
				
				//the selected file is wrong
				message=wrongTypeErrorLabel;
			}
			
		}else{
			
			//no file has been selected
			message=fileMissingErrorLabel;
		}
		
		setMessage(message, ERROR_TYPE);
		okButton.setEnabled(false);
	}
	
	/**
	 * checks if the provided array of files is 
	 * consistent for the open action
	 * @param files a file
	 */
	protected void checkFilesConsistency(File[] files){
		
		String message=messageLabel;
		System.out.println("Checking files for open: " + files + " files");
		if(files!=null && files.length>0){
			
			boolean wrongFileExists=false;
			boolean fileNotExist=false;
			
			for(File file : files){
				
				System.out.println("Checking file " + file);
				
				if(file==null || !file.exists()){
					System.out.println("File " + file.getAbsolutePath() + " not found");
					fileNotExist=true;
					break;
				}
				
				if(!isFileConsistent(file)){
					
					wrongFileExists=true;
					break;
				}
			}
			
			if(fileNotExist){
				
				//one of the selected file does not exist
				message=fileNotExistError;
			
			}else if(wrongFileExists){
				
				//one of the selected file is wrong
				message=wrongTypeErrorLabel;
					
			}else{
				
				//the files are stored
				selectedFiles=files;
				System.out.println("Selected files are " + selectedFiles);
				//enabling the ok button
				okButton.setEnabled(true);
				setMessage(messageLabel, INFORMATION_TYPE);
				return;
			}
			
		}else{
			
			//no file has been selected
			message=fileMissingErrorLabel;
		}
		
		setMessage(message, ERROR_TYPE);
		okButton.setEnabled(false);
	}
	
	/**
	 * returns whether the provided file is consistent
	 * @param file a file
	 * @return whether the provided file is consistent
	 */
	protected boolean isFileConsistent(File file){
		
		String fileName=file.getName();
		
		if(fileFilter!=null){
			
			return fileName.indexOf(".")==-1 || fileFilter.acceptFile(file);
		}
		
		return false;
	}
	
	/**
	 * @return the radix corresponding to the mode
	 */
	protected String getRadix(){
		
		switch (mode){
			
			case OPEN_FILE_MODE :
				
				return "FileOpen";
				
			case SAVE_FILE_MODE :
				
				return "FileSave";
				
			case EXPORT_FILE_MODE :
				
				return "FileExport";
		}
		
		return "";
	}
	
	/**
	 * returns the text field that could be found into the container
	 * @param c a container
	 * @return the text field that could be found in the provided container
	 */
	protected JTextField getTextField(Container c){
		
		JTextField textField=null;
		
		for(int i=0; i<c.getComponentCount(); i++){
			
			Component cnt=c.getComponent( i );
			
			if(cnt instanceof JTextField){
				
				return (JTextField)cnt ;
			}

			if(cnt instanceof Container){
				
				textField=getTextField((Container)cnt) ;
				
				if(textField!=null){
					
					return textField;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * returns the files given the directory and the 
	 * content string that defines file names
	 * @param baseDirectory the base directory
	 * @param content the content from which file names should be retrieved
	 * @return the related array of files
	 */
	protected File[] getFiles(File baseDirectory, String content){
		
		File[] computedFiles=null;
		
		//splitting the content string
		String[] splitContent=content.split("\"");
		
		//computing the new files
		File file=null;
		String baseURI=baseDirectory.toURI().toASCIIString();
		Set<File> filesSet=new HashSet<File>();
		
		for(String name : splitContent){
			
			name=name.trim();
			
			if(! name.equals("")){
				
				// Alteration by Oliver: only add files which actually exist to the fileSet
				try {
					file=new File(new URI(baseURI+name));
					if (file.exists()) {
						filesSet.add(file);
					} else {
						// Dialog may not have reported file extension, try with .svg
						file=new File(new URI(baseURI+name+".svg"));
						if (file.exists()) {
							filesSet.add(file);
						}
					}
				} catch (URISyntaxException urisex) {
					System.out.println("URI error: " + urisex);
				}
				
			}
		}
		
		//creating the array of files
		computedFiles=new File[filesSet.size()];
		int i=0;
		
		for(File theFile : filesSet){
			
			computedFiles[i]=theFile;
			i++;
		}
		
		return computedFiles;
	}
}
