package fr.itris.glips.svgeditor.io.managers.creation;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the dialog used to choose the parameters 
 * used to create a new svg file
 * @author ITRIS, Jordi SUC
 */
public class NewDialog extends TitledDialog{

	/**
	 * the file creation manager
	 */
	private FileNew fileCreationManager;
	
	/**
	 * the width spinner widget
	 */
	private IntegerSpinnerWidget widthSpinner;
	
	/**
	 * the height spinner widget
	 */
	private IntegerSpinnerWidget heightSpinner;
	
	/**
	 * the constructor of the class
	 * @param fileCreationManager the file creation manager
	 * @param parent the parent of the dialog
	 */
	public NewDialog(FileNew fileCreationManager, Frame parent){
		
		super(parent, true, true);
		this.fileCreationManager=fileCreationManager;
	}
	
	/**
	 * the constructor of the class
	 * @param fileCreationManager the file creation manager
	 * @param parent the parent of the dialog
	 */
	public NewDialog(FileNew fileCreationManager, JDialog parent){
		
		super(parent, true);
		this.fileCreationManager=fileCreationManager;
	}
	
	@Override
	protected JPanel buildContentPanel(){
		
		//getting the labels
		ResourceBundle bundle=ResourcesManager.bundle;
		String widthLabel=bundle.getString("labelwidth");
		String heightLabel=bundle.getString("labelheight");
		String titleDialogLabel=bundle.getString("FileNewTitle");
		String titleDialogMessageLabel=bundle.getString("FileNewMessage");

		//setting the title
		setTitleMessage(titleDialogLabel);
		
		//setting the information message
		setMessage(titleDialogMessageLabel, INFORMATION_TYPE);
		
		//creating the jlabels for the spinners
		JLabel widthLbl=new JLabel(widthLabel+" : ");
		widthLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel heightLbl=new JLabel(heightLabel+" : ");
		heightLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel widthPxLbl=new JLabel("px");
		JLabel heightPxLbl=new JLabel("px");
		
		//creates the spinners
		widthSpinner=new IntegerSpinnerWidget(1, 1, Integer.MAX_VALUE-1, 1);
		heightSpinner=new IntegerSpinnerWidget(1, 1, Integer.MAX_VALUE-1, 1);
		
		//initializing the spinners
		widthSpinner.init(600);
		heightSpinner.init(400);
		
		//creating and filling the panel that will contain the widgets
		JPanel widgetsPanel=new JPanel();
		widgetsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout layout=new GridBagLayout();
		widgetsPanel.setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(2, 2, 2, 2);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=0;
		layout.setConstraints(widthLbl, c);
		widgetsPanel.add(widthLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(widthSpinner, c);
		widgetsPanel.add(widthSpinner);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=2;
		c.weightx=0;
		layout.setConstraints(widthPxLbl, c);
		widgetsPanel.add(widthPxLbl);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=1;
		layout.setConstraints(heightLbl, c);
		widgetsPanel.add(heightLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(heightSpinner, c);
		widgetsPanel.add(heightSpinner);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=2;
		c.weightx=0;
		layout.setConstraints(heightPxLbl, c);
		widgetsPanel.add(heightPxLbl);
		
		//creating the ok and cancel listener
		ActionListener buttonsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
	
				if(evt.getSource().equals(okButton)){
					
					fileCreationManager.createNewDocument(
						widthSpinner.getWidgetValue()+"", heightSpinner.getWidgetValue()+"");
				}
				
				setVisible(false);
			}
		};
		
		okButtonListener=buttonsListener;
		cancelButtonListener=buttonsListener;
		
		okButton.addActionListener(buttonsListener);
		cancelButton.addActionListener(buttonsListener);

		return widgetsPanel;
	}
	
	@Override
	public void showDialog(JComponent relativeComponent) {

		widthSpinner.takeFocus();
		super.showDialog(relativeComponent);
	}
	
	@Override
	public void disposeDialog() {
		
		widthSpinner.dispose();
		heightSpinner.dispose();
		
		super.disposeDialog();
	}
}
