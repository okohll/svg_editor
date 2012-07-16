package fr.itris.glips.library.widgets;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.*;

/**
 * the class of a wait dialog
 * @author ITRIS, Jordi SUC
 */
public class WaitDialog extends JDialog{
	
	/**
	 * the banner image
	 */
	protected static Image bannerImage;
	
	/**
	 * the icons
	 */
	protected static ImageIcon informationIcon;
	
	static{
		
		//loading the images
		informationIcon=Icons.getIcon("Information", false);
		bannerImage=Icons.getIcon("WaitBanner", false).getImage();
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
	 * the progress panel
	 */
	protected JPanel progressPanel;
	
	/**
	 * the progress label
	 */
	private JLabel progressLabel;
	
	/**
	 * the progress bar
	 */
	protected JProgressBar progressBar;
	
	/**
	 * the error panel
	 */
	protected JPanel errorPanel;
	
	/**
	 * the error text area
	 */
	protected JEditorPane errorTextArea;
	
	/**
	 * the buttons panel
	 */
	protected JPanel buttonsPanel;
	
	/**
	 * the cancel button
	 */
	protected JButton cancelButton;
	
	/**
	 * the basic cancel button listener
	 */
	protected ActionListener basicCancelButtonListener;
	
	/**
	 * the cancel button listener
	 */
	protected ActionListener cancelButtonListener;

	/**
	 * a constructor of the class
	 * @param parent the parent frame
	 * @param cancelButtonListener the cancel button listener
	 */
	public WaitDialog(Frame parent, ActionListener cancelButtonListener) {

		super(parent, false);
		this.cancelButtonListener=cancelButtonListener;
		build();
	}
	
	/**
	 * a constructor of the class
	 * @param parent the parent dialog
	 * @param cancelButtonListener the cancel button listener
	 */
	public WaitDialog(JDialog parent, ActionListener cancelButtonListener) {

		super(parent, false);
		this.cancelButtonListener=cancelButtonListener;
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
		titleLbl.setBorder(new EmptyBorder(8, 8, 0, 0));
		titleLbl.setOpaque(false);
		
		messageIconLabel=new JLabel();
		messageIconLabel.setIcon(informationIcon);
		messageIconLabel.setOpaque(false);
		messageIconLabel.setBorder(new EmptyBorder(2, 0, 0, 5));
		
		messageLbl=new JTextArea(" ");
		messageLbl.setRows(2);
		messageLbl.setLineWrap(true);
		messageLbl.setOpaque(false);
		messageLbl.setEditable(false);
		messageLbl.setWrapStyleWord(true);
		
		//creating the message panel
		JPanel messagePanel=new JPanel();
		messagePanel.setBorder(new EmptyBorder(5, 10, 5, 80));
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
		
		//getting the label
		String cancelLabel=Bundle.bundle.getString("Cancel");

		//creating the cancel buttons
		cancelButton=new JButton(cancelLabel);
		
		//adding the basic cancel button listener
		basicCancelButtonListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
				disposeDialog();
			}
		};
		
		//adding the cancel button listener
		if(cancelButtonListener!=null){
			
			cancelButton.addActionListener(cancelButtonListener);
		}
		
		//creating and filling the buttons panel
		buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		buttonsPanel.add(cancelButton);
		
		//creating the south panel
		JPanel southPanel=new JPanel();
		southPanel.setLayout(new BorderLayout(0, 5));
		southPanel.add(new JSeparator(), BorderLayout.NORTH);
		southPanel.add(buttonsPanel, BorderLayout.CENTER);
		southPanel.setBorder(new EmptyBorder(0, 0, 5, 0));
		
		//creating the progress panel
		progressPanel=new JPanel();
		progressPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		//creating the progress bar label
		progressLabel=new JLabel(" ");
		progressLabel.setPreferredSize(new Dimension(400, 22));
		
		//filling the content panel
		progressBar=new JProgressBar();
		progressBar.setPreferredSize(new Dimension(400, 22));
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setIndeterminate(true);
		
		//adding the widgets to the progress panel
		GridBagLayout gridBagLayout=new GridBagLayout();
		progressPanel.setLayout(gridBagLayout);
		c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=50;
		c.anchor=GridBagConstraints.CENTER;
		c.insets=new Insets(1, 0, 1, 0);
		c.gridx=0;
		
		c.gridy=0;
		c.weighty=50;
		gridBagLayout.setConstraints(progressLabel, c);
		progressPanel.add(progressLabel);
		
		c.gridy=1;
		c.weighty=0;
		gridBagLayout.setConstraints(progressBar, c);
		progressPanel.add(progressBar);
		
		//creating the error panel
		errorPanel=new JPanel();
		
		//creating the error widgets//
		JLabel errorIconLabel=new JLabel(Icons.getIcon("ErrorLarge", false));
		
		//creating the text area
		errorTextArea=new JEditorPane(
				"text/html", "<html><body><br><br></body></html>");
		errorTextArea.setOpaque(false);
		errorTextArea.setPreferredSize(new Dimension(300, 60));
		
		//filling the error panel
		gridBagLayout=new GridBagLayout();
		errorPanel.setLayout(gridBagLayout);
		c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridy=0;
		
		c.gridx=0;
		c.insets=new Insets(0, 5, 2, 8);
		c.anchor=GridBagConstraints.NORTHWEST;
		gridBagLayout.setConstraints(errorIconLabel, c);
		errorPanel.add(errorIconLabel);
		
		c.gridx=1;
		c.weightx=50;
		c.insets=new Insets(2, 2, 2, 2);
		gridBagLayout.setConstraints(errorTextArea, c);
		errorPanel.add(errorTextArea);
		
		//filling the dialog
		getContentPane().setLayout(new BorderLayout(0, 5));
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		getContentPane().add(progressPanel, BorderLayout.CENTER);
		getContentPane().add(southPanel, BorderLayout.SOUTH);
		
		//packing
		pack();
	}
	
	/**
	 * sets the title message
	 * @param message the new title message
	 */
	public void setTitleMessage(String message){
		
		titleLbl.setText(message);
	}
	
	/**
	 * sets the message
	 * @param message a new message
	 */
	public void setMessage(String message){
		
		messageLbl.setText(message);
	}
	
	/**
	 * sets the progress message
	 * @param progressMessage the progress message
	 * @param value the current value of the progress bar
	 * @param min the min value of the progress bar
	 * @param max the max value of the progress bar
	 */
	public void setProgressMessage(
			String progressMessage, int value, int min, int max){
		
		if(progressBar.isIndeterminate()){
			
			progressBar.setIndeterminate(false);
		}
		
		progressLabel.setText(progressMessage);
		progressBar.setMinimum(min);
		progressBar.setMaximum(max);
		progressBar.setValue(value);
	}
	
	/**
	 * sets whether the progress bar should be indeterminate
	 * @param indeterminate whether the progress bar should be indeterminate
	 */
	public void setIndeterminate(boolean indeterminate){
		
		progressBar.setIndeterminate(indeterminate);
	}
	
	/**
	 * sets the progress message in the awt thread
	 * @param progressMessage the progress message
	 * @param value the current value of the progress bar
	 * @param min the min value of the progress bar
	 * @param max the max value of the progress bar
	 */
	public synchronized void setProgressMessageInAwtThread(
			final String progressMessage, final int value, final int min, final int max){
		
		SwingUtilities.invokeLater(new Runnable(){
			
			public void run() {
				
				setProgressMessage(progressMessage, value, min, max);
			}
		});
	}
	
	/**
	 * sets the error message
	 * @param errorMessage the error message
	 */
	public void setErrorMessage(String errorMessage){
		
		errorTextArea.setText(errorMessage);
	}
	
	/**
	 * sets the wait component in error
	 */
	public void setInError(){
		
		getContentPane().remove(progressPanel);
		getContentPane().remove(errorPanel);
		getContentPane().add(errorPanel, BorderLayout.CENTER);
		getContentPane().doLayout();
	}
	
	/**
	 * showing the dialog
	 * @param relativeComponent the component 
	 * relatively to which the dialog will be shown
	 * @param isInError whether the task of the monitor has failed
	 */
	public void showDialog(JComponent relativeComponent, boolean isInError){
		
		if(! isInError){
			
			//removing the error panel from the dialog
			getContentPane().remove(errorPanel);
			getContentPane().add(progressPanel, BorderLayout.CENTER);
		}
		
		if(relativeComponent!=null){
			
			setLocationRelativeTo(relativeComponent);
		}

		setVisible(true);
	}
	
	/**
	 * disposes this dialog
	 */
	public void disposeDialog(){

		try{
			cancelButton.removeActionListener(basicCancelButtonListener);
			
			if(cancelButtonListener!=null){
				
				cancelButton.removeActionListener(cancelButtonListener);
			}
			
			super.dispose();
			
			titleLbl=null;
			messageIconLabel=null;
			messageLbl=null;
			progressPanel=null;
			progressLabel=null;
			progressBar=null;
			errorPanel=null;
			errorTextArea=null;
			buttonsPanel=null;
			cancelButton=null;
			cancelButtonListener=null;
		}catch (Exception ex){}
	}
}
