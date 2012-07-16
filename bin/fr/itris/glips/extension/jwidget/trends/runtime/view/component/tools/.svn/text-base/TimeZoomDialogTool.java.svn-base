package fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools;

import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import fr.itris.glips.library.widgets.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class of the tool used to choose the displayed time on the horizontal axis
 * @author ITRIS, Jordi SUC
 */
public class TimeZoomDialogTool extends JButton{
	
	/**
	 * the id
	 */
	private String id="TimeZoomDialog";
	
	/**
	 * the components handler
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the listener to the button
	 */
	private ActionListener listener;
	
	/**
	 * the configuration object
	 */
	private TrendsConfiguration configuration;
	
	/**
	 * the dialog used to choose a duration
	 */
	private DurationChooserDialog dialog;
	
	/**
	 * the constructor of the class
	 * @param toolBar the tool bar component
	 */
	public TimeZoomDialogTool(ToolBarComponent toolBar){

		this.componentsHandler=toolBar.getComponentsHandler();
		this.configuration=componentsHandler.getView().getController().getConfiguration();
	}
	
	/**
	 * initializes the button
	 */
	public void initialize(){
		
		//creating the dialog
		Container topLevelAncestor=getTopLevelAncestor();
		
		if(topLevelAncestor instanceof Frame){
			
			dialog=new DurationChooserDialog((Frame)topLevelAncestor);
			
		}else if(topLevelAncestor instanceof JApplet){
			
			dialog=new DurationChooserDialog(componentsHandler.getView().
				getController().getJwidgetRuntime().getPicture().getCanvas().
					getPicture().getMainDisplay().getTopLevelFrame());
			
		}else {
			
			dialog=new DurationChooserDialog((JDialog)topLevelAncestor);
		}
		
		dialog.pack();

		//setting the properties of the button
		setToolTipText(TrendsBundle.bundle.getString(id));
		
		//setting the icon
		setIcon(TrendsIcons.getIcon(id, false));
		
		//adding a listener to the button
		listener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				componentsHandler.setCurrentAction(id, false);
				dialog.showDialog(TimeZoomDialogTool.this);
			}
		};
		
		addActionListener(listener);
	}
	
	/**
	 * disposes the object
	 */
	public void dispose(){
		
		removeActionListener(listener);
		dialog.disposeDialog();
		
		
		listener=null;
		componentsHandler=null;
		configuration=null;
		dialog=null;
	}
	
	/**
	 * the class of the dialog used to chooser a duration
	 * @author ITRIS, Jordi SUC
	 */
	protected class DurationChooserDialog extends TitledDialog{
		
		/**
		 * the time chooser widget
		 */
		private TimeChooserWidget timeChooserWidget;
		
		/**
		 * the constructor of the class
		 * @param parentFrame the parent frame
		 */
		public DurationChooserDialog(Frame parentFrame) {

			super(parentFrame, true, true);
		}
		
		/**
		 * the constructor of the class
		 * @param parentDialog the parent dialog
		 */
		public DurationChooserDialog(JDialog parentDialog) {

			super(parentDialog, true);
		}
		
		@Override
		protected JPanel buildContentPanel() {
			
			//getting the labels
			String title=TrendsBundle.bundle.getString("TimeZoomDialogWidget");
			String informationMessage=TrendsBundle.bundle.getString("TimeZoomDialogWidgetMessage");
			String durationLabel=TrendsBundle.bundle.getString("TimeZoomDialogWidgetTime");
			final String tooLongLabel=TrendsBundle.bundle.getString("TimeZoomDialogWidgetTimeTooLong");
			
			//setting the messages
			setTitleMessage(title);
			setMessage(informationMessage, INFORMATION_TYPE);
			
			//creating the jlabel for the widget
			JLabel widgetJLabel=new JLabel(durationLabel+" :");
			
			//creating the time chooser widget
			timeChooserWidget=new TimeChooserWidget();
			
			//adding a listener to the time chooser widget
			ActionListener widgetListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {

					long chosenTime=TrendsConfiguration.getTime(timeChooserWidget.getValue());
					
					if(chosenTime>configuration.getMaxDisplayableTime()){
						
						okButton.setEnabled(false);
						setMessage(tooLongLabel, ERROR_TYPE);
						
					}else{
						
						okButton.setEnabled(true);
						setMessage("", INFORMATION_TYPE);
					}
				}
			};
			
			timeChooserWidget.addListener(widgetListener);
			
			//creating the panel for the widgets
			JPanel widgetsPanel=new JPanel();
			widgetsPanel.setLayout(new BorderLayout(5, 0));
			widgetsPanel.add(widgetJLabel, BorderLayout.WEST);
			widgetsPanel.add(timeChooserWidget, BorderLayout.CENTER);
			
			//creating the buttons listener
			ActionListener buttonsListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {

					if(evt.getSource().equals(okButton)){
						
						configuration.setHorizontalAxisDuration(timeChooserWidget.getValue());	
					}
					
					setVisible(false);
				}
			};
			
			okButtonListener=buttonsListener;
			cancelButtonListener=buttonsListener;
			
			okButton.addActionListener(buttonsListener);
			cancelButton.addActionListener(buttonsListener);
			
			//creating and filling the content pane
			JPanel contentPane=new JPanel();
			contentPane.setBorder(new EmptyBorder(0, 30, 0, 30));
			contentPane.add(widgetsPanel, BorderLayout.CENTER);
			
			//initializing the time chooser widget
			timeChooserWidget.init("1 min");
			
			return contentPane;
		}
		
		@Override
		public void showDialog(JComponent relativeComponent) {

			timeChooserWidget.takeFocus();
			super.showDialog(relativeComponent);
		}

		@Override
		public void disposeDialog(){
			
			timeChooserWidget.dispose();

			super.disposeDialog();
		}
	}
}
