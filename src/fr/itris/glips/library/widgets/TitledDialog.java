package fr.itris.glips.library.widgets;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.*;

/**
 * the class of a titled dialog
 * @author ITRIS, Jordi SUC
 */
public abstract class TitledDialog extends JDialog {
	
	/**
	 * the INFORMATION type
	 */
	public static final  int INFORMATION_TYPE=0;
	
	/**
	 * the WARNING type
	 */
	public static final int WARNING_TYPE=1;
	
	/**
	 * the ERROR type
	 */
	public static final int ERROR_TYPE=2;
	
	/**
	 * the icons
	 */
	protected static ImageIcon informationIcon, warningIcon, errorIcon; 
	
	/**
	 * the banner image
	 */
	protected static Image bannerImage;
	
	static{
		
		//loading the images
		informationIcon=Icons.getIcon("Information", false);
		warningIcon=Icons.getIcon("Warning", false);
		errorIcon=Icons.getIcon("Error", false);
		bannerImage=Icons.getIcon("Banner", false).getImage();
	}
	
	/**
	 * the title jlabel
	 */
	protected JLabel titleLbl;
	
	/**
	 * the message icon jlabel
	 */
	private JLabel messageIconLabel;
	
	/**
	 * the message label
	 */
	private JTextArea messageLbl;
	
	/**
	 * the content panel
	 */
	protected JPanel contentPanel=new JPanel();
	
	/**
	 * the buttons panel
	 */
	protected JPanel buttonsPanel;
	
	/**
	 * the ok and cancel buttons
	 */
	protected JButton okButton, cancelButton;
	
	/**
	 * the ok and cancel button listeners
	 */
	protected ActionListener okButtonListener, cancelButtonListener;
	
	/**
	 * the initial information message
	 */
	private String initialInformationMessage="";
	
	/**
	 * whether the controls should be created
	 */
	private boolean createControls;

	/**
	 * a constructor of the class
	 * @param parent the parent frame
	 * @param modal whether the dialog is modal
	 * @param createControls whether the controls should be created
	 */
	public TitledDialog(Frame parent, boolean modal, boolean createControls) {

		super(parent, modal);
		this.createControls=createControls;
		build();
	}
	
	/**
	 * a constructor of the class
	 * @param parent the parent dialog
	 * @param modal whether the dialog is modal
	 */
	public TitledDialog(JDialog parent, boolean modal) {

		super(parent, modal);
		build();
	}

	/**
	 * builds the dialog
	 */
	protected void build(){
		
		//building the title pane
		JPanel titlePanel=new JPanel(){
			
			@Override
			protected void paintComponent(Graphics g) {

				super.paintComponent(g);
				
				if(bannerImage!=null){
					
					g.drawImage(bannerImage, getWidth()-bannerImage.getWidth(this), 
							getHeight()-bannerImage.getHeight(this), this);
				}
			}
		};
		
		titlePanel.setBackground(Color.white);
		
		//creating the jlabels
		titleLbl=new JLabel(" ");
		titleLbl.setHorizontalAlignment(SwingConstants.LEFT);
		titleLbl.setFont(titleLbl.getFont().deriveFont(Font.BOLD));
		titleLbl.setBorder(new EmptyBorder(8, 8, 0, 5));
		titleLbl.setOpaque(false);
		
		messageIconLabel=new JLabel();
		messageIconLabel.setIcon(informationIcon);
		messageIconLabel.setOpaque(false);
		messageIconLabel.setBorder(new EmptyBorder(2, 0, 0, 5));
		
		messageLbl=new JTextArea(" ");
		messageLbl.setRows(3);
		messageLbl.setLineWrap(true);
		messageLbl.setOpaque(false);
		messageLbl.setEditable(false);
		messageLbl.setWrapStyleWord(true);
		
		//creating the message panel
		JPanel messagePanel=new JPanel();
		messagePanel.setBorder(new EmptyBorder(5, 10, 5, 5));
		messagePanel.setOpaque(false);
		
		GridBagLayout layout=new GridBagLayout();
		messagePanel.setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.anchor=GridBagConstraints.WEST;
		
		c.gridx=0;
		c.gridy=0;
		layout.setConstraints(messageIconLabel, c);
		messagePanel.add(messageIconLabel);
		
		JPanel emptyPanel=new JPanel();
		emptyPanel.setOpaque(false);
		c.gridy=1;
		layout.setConstraints(emptyPanel, c);
		messagePanel.add(emptyPanel);
		
		c.gridx=1;
		c.gridy=0;
		c.gridheight=2;
		c.weightx=50;
		c.weighty=50;
		
		layout.setConstraints(messageLbl, c);
		messagePanel.add(messageLbl);
		
		//creating the labels panel
		JPanel labelsPanel=new JPanel();
		labelsPanel.setOpaque(false);
		
		//setting the layout
		labelsPanel.setLayout(new BorderLayout(0, 0));
		labelsPanel.add(titleLbl, BorderLayout.NORTH);
		labelsPanel.add(messagePanel, BorderLayout.CENTER);
		
		//filling the title panel
		titlePanel.setLayout(new BorderLayout());
		titlePanel.add(labelsPanel, BorderLayout.CENTER);
		titlePanel.add(new JSeparator(), BorderLayout.SOUTH);
		
		JPanel southPanel=null;
		
		if(createControls){
			
			//getting the labels
			String okLabel=Bundle.bundle.getString("OK");
			String cancelLabel=Bundle.bundle.getString("Cancel");

			//creating the ok and cancel buttons
			okButton=new JButton(okLabel);
			cancelButton=new JButton(cancelLabel);
			
			//the ok action name
			String actionName="okAction";
			
			//registering the ok action
			Action okAction=new AbstractAction(actionName){
				
				public void actionPerformed(ActionEvent e) {

					okButtonListener.actionPerformed(e);
				}
			};
			
			okButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
					KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), actionName);
			okButton.getActionMap().put(actionName, okAction);
			
			//the cancel action name
			actionName="cancelAction";
			
			//registering the cancel action
			Action cancelAction=new AbstractAction(actionName){
				
				public void actionPerformed(ActionEvent e) {
					
					cancelButtonListener.actionPerformed(e);
				}
			};
			
			cancelButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
					KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), actionName);
			cancelButton.getActionMap().put(actionName, cancelAction);

			//creating and filling the buttons panel
			buttonsPanel=new JPanel();
			buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
			buttonsPanel.add(okButton);
			buttonsPanel.add(cancelButton);
			
			//creating the south panel
			southPanel=new JPanel();
			southPanel.setLayout(new BorderLayout(0, 5));
			southPanel.add(new JSeparator(), BorderLayout.NORTH);
			southPanel.add(buttonsPanel, BorderLayout.CENTER);
			southPanel.setBorder(new EmptyBorder(0, 0, 5, 0));
		}

		//creating the content panel
		contentPanel=buildContentPanel();
		
		//filling the dialog
		getContentPane().setLayout(new BorderLayout(0, 10));
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		if(southPanel!=null){
			
			getContentPane().add(southPanel, BorderLayout.SOUTH);
		}

		//packing
		pack();
	}
	
	/**
	 * sets the title message
	 * @param titleMessage a new title message
	 */
	protected void setTitleMessage(String titleMessage){
		
		titleLbl.setText(titleMessage);
	}
	
	/**
	 * sets the message
	 * @param message a new message
	 * @param type the type of the message
	 */
	public void setMessage(String message, int type){
		
		messageIconLabel.setIcon(null);
		
		switch (type){
		
			case INFORMATION_TYPE :
				
				if(initialInformationMessage.equals("")){
					
					initialInformationMessage=message;
					
				}else if(message.equals("")){
					
					message=initialInformationMessage;
				}
				
				messageIconLabel.setIcon(informationIcon);
				break;
				
			case WARNING_TYPE :
				
				messageIconLabel.setIcon(warningIcon);
				break;
				
			case ERROR_TYPE :
				
				messageIconLabel.setIcon(errorIcon);
				break;
		}
		
		messageLbl.setText(message);
	}
	
	/**
	 * builds the content panel and returns it
	 * @return the content panel
	 */
	protected abstract JPanel buildContentPanel();
	
	/**
	 * shows the dialog
	 * @param relativeComponent the component relatively 
	 * to which the dialog will be shown
	 */
	public void showDialog(JComponent relativeComponent){
		
		setLocationRelativeTo(relativeComponent);
		setVisible(true);
	}
	
	/**
	 * @return the ok button
	 */
	public JButton getOkButton() {
		return okButton;
	}
	
	/**
	 * @return the cancel button
	 */
	public JButton getCancelButton() {
		return okButton;
	}
	
	/**
	 * sets the ok button
	 * @param okButton the ok button
	 */
	public void setOkButton(JButton okButton) {
		this.okButton = okButton;
	}
	
	/**
	 * sets the cancel button
	 * @param cancelButton the cancel button
	 */
	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}
	
	/**
	 * disposes this dialog
	 */
	public void disposeDialog(){
		
		if(okButtonListener!=null){
			
			okButton.removeActionListener(okButtonListener);
			okButton.getActionMap().clear();
		}
		
		if(cancelButtonListener!=null){
			
			cancelButton.removeActionListener(cancelButtonListener);
			cancelButton.getActionMap().clear();
		}
		
		super.dispose();
	}
}
