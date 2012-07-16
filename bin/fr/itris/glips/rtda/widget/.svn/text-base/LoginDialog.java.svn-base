package fr.itris.glips.rtda.widget;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.widgets.*;

/**
 * the class of the dialog used to choose the login and 
 * password for the new user
 * @author ITRIS, Jordi SUC
 */
public class LoginDialog extends TitledDialog{
	
	/**
	 * the text fields
	 */
	private JTextField loginTxt;
	
	/**
	 * the password textfield
	 */
	private JPasswordField passwordTxt;
	
	/**
	 * whether the action succeeded
	 */
	private boolean succeeded=false;
	
	/**
	 * the constructor of the class
	 * @param parent the parent of the dialog
	 */
	public LoginDialog(Frame parent){
		
		super(parent, true, true);
	}
	
	/**
	 * the constructor of the class
	 * @param parent the parent of the dialog
	 */
	public LoginDialog(JDialog parent){
		
		super(parent, true);
	}
	
	@Override
	protected JPanel buildContentPanel(){
		
		//getting the labels
		ResourceBundle bundle=
			ResourceBundle.getBundle("fr.itris.glips.rtda.resources.properties.strings");
		String titleDialogLabel=bundle.getString("LoginDialogTitle");
		String titleDialogMessageLabel=bundle.getString("LoginDialogMessage");
		String loginLabel=bundle.getString("LoginLabel");
		String passwordLabel=bundle.getString("PasswordLabel");

		//setting the title
		setTitleMessage(titleDialogLabel);
		
		//setting the information message
		setMessage(titleDialogMessageLabel, INFORMATION_TYPE);
		
		//creating the jlabels for the textfield
		JLabel loginLbl=new JLabel(loginLabel+" : ");
		loginLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel passwordLbl=new JLabel(passwordLabel+" : ");
		passwordLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//creates the text field
		loginTxt=new JTextField(15);
		passwordTxt=new JPasswordField(15);
		
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
		layout.setConstraints(loginLbl, c);
		widgetsPanel.add(loginLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(loginTxt, c);
		widgetsPanel.add(loginTxt);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=1;
		c.weightx=0;
		layout.setConstraints(passwordLbl, c);
		widgetsPanel.add(passwordLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(passwordTxt, c);
		widgetsPanel.add(passwordTxt);
		
		//creating the ok and cancel listener
		ActionListener buttonsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				if(evt.getSource().equals(okButton)){
					
					succeeded=true;
				}
				
				setVisible(false);
			}
		};
		
		okButtonListener=buttonsListener;
		cancelButtonListener=buttonsListener;
		
		okButton.addActionListener(buttonsListener);
		cancelButton.addActionListener(buttonsListener);
		setMessage(titleDialogMessageLabel, TitledDialog.INFORMATION_TYPE);

		return widgetsPanel;
	}
	
	/**
	 * @return the login
	 */
	public String getLogin() {
		
		return loginTxt.getText();
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		
		return new String(passwordTxt.getPassword());
	}

	/**
	 * @return whether the action has succeeded
	 */
	public boolean hasSucceeded() {
		return succeeded;
	}

	@Override
	public void showDialog(JComponent relativeComponent) {

		loginTxt.grabFocus();
		super.showDialog(relativeComponent);
	}
}

