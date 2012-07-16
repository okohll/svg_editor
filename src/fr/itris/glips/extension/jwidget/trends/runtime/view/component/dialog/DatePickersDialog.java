package fr.itris.glips.extension.jwidget.trends.runtime.view.component.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.library.widgets.*;

/**
 * the class of the dialog used to pick dates
 * @author ITRIS, Jordi SUC
 */
public class DatePickersDialog extends TitledDialog{
	
	/**
	 * the trends runtime view
	 */
	private TrendsRuntimeView view;
	
	/**
	 * the start date picker
	 */
	private DateAndTimeChooserWidget startDateAndTimeChooserWidget;
	
	/**
	 * the end date picker
	 */
	private DateAndTimeChooserWidget endDateAndTimeChooserWidget;
	
	/**
	 * the constructor of the class
	 * @param view the trends runtime view
	 * @param parent the parent of the dialog
	 */
	public DatePickersDialog(TrendsRuntimeView view, Frame parent){
		
		super(parent, true, true);
		this.view=view;
	}
	
	/**
	 * the constructor of the class
	 * @param view the trends runtime view
	 * @param parent the parent of the dialog
	 */
	public DatePickersDialog(TrendsRuntimeView view, JDialog parent){
		
		super(parent, true);
		this.view=view;
	}
	
	@Override
	protected JPanel buildContentPanel(){
		
		//getting the labels
		String label=TrendsBundle.bundle.getString("DatePickersDialog");
		String informationMessage=TrendsBundle.bundle.getString("DatePickersDialogMessage");
		String startDateLabel=TrendsBundle.bundle.getString("StartDate");
		String endDateLabel=TrendsBundle.bundle.getString("EndDate");
		final String startHigherThanEndDateLabel=
			TrendsBundle.bundle.getString("StartHigherThanEndDate");
		final String timeZoneTooLargeLabel=
			TrendsBundle.bundle.getString("TimeZoneTooLarge");

		//setting the title
		setTitleMessage(label);
		
		//setting the information message
		setMessage(informationMessage, INFORMATION_TYPE);
		
		//creating the date pickers
		startDateAndTimeChooserWidget=new DateAndTimeChooserWidget();
		endDateAndTimeChooserWidget=new DateAndTimeChooserWidget();
		
		//creating the listener to the widgets
		ActionListener widgetsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				//getting the start and end dates
				long startDate=startDateAndTimeChooserWidget.getDate();
				long endDate=endDateAndTimeChooserWidget.getDate();

				//disabling the ok button
				okButton.setEnabled(false);
				
				//disabling the ok button
				if(startDate>endDate){
					
					setMessage(startHigherThanEndDateLabel, ERROR_TYPE);
					
				}else if(Math.abs(endDate-startDate)>
					view.getController().getConfiguration().getMaxDisplayableTime()){
					
					setMessage(timeZoneTooLargeLabel, ERROR_TYPE);
					
				}else{
					
					setMessage("", INFORMATION_TYPE);
					okButton.setEnabled(true);
				}
			}
		};
		
		//adding the listener to the widgets
		startDateAndTimeChooserWidget.addListener(widgetsListener);
		endDateAndTimeChooserWidget.addListener(widgetsListener);
		
		//creating the listeners to the buttons
		okButtonListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				setVisible(false);
				
				//showing the wait dialog
				view.getComponentsHandler().getWaitDialog().
					showDialog(view.getTrendsComponent(), false);
				
				final long startDate=startDateAndTimeChooserWidget.getDate();
				final long endDate=endDateAndTimeChooserWidget.getDate();
				
				Thread thread=new Thread(){
					
					@Override
					public void run() {

						view.getController().getModel().requestFromDataBase(
								startDate, endDate);
					}
				};

				thread.start();
			}
		};
		
		cancelButtonListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				setVisible(false);
			}
		};

		okButton.addActionListener(okButtonListener);
		cancelButton.addActionListener(cancelButtonListener);
		
		//creating the start date panel
		JPanel startDatePanel=new JPanel();
		startDatePanel.setLayout(new BoxLayout(startDatePanel, BoxLayout.X_AXIS));
		
		//setting the border
		TitledBorder border=new TitledBorder(startDateLabel);
		startDatePanel.setBorder(border);
		
		//adding the date picker to the panel
		startDatePanel.add(startDateAndTimeChooserWidget);
		
		//creating the end date panel
		JPanel endDatePanel=new JPanel();
		endDatePanel.setLayout(new BoxLayout(endDatePanel, BoxLayout.X_AXIS));
		
		//setting the border
		border=new TitledBorder(endDateLabel);
		endDatePanel.setBorder(border);
		
		//adding the date picker to the panel
		endDatePanel.add(endDateAndTimeChooserWidget);
	
		//creating the date pickers panel
		JPanel datePickersPanel=new JPanel();
		
		//setting the layout
		GridLayout gridLayout=new GridLayout(1, 2);
		datePickersPanel.setLayout(gridLayout);
		datePickersPanel.add(startDatePanel);
		datePickersPanel.add(endDatePanel);
		
		//initializing the choosers
		long time=System.currentTimeMillis();
		Calendar cl=Calendar.getInstance();
		cl.setTimeInMillis(time);
		cl.set(Calendar.MILLISECOND, 0);
		
		long endDate=cl.getTimeInMillis();
		long startDate=endDate-1000;
		
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(startDate);

		startDateAndTimeChooserWidget.init(startDate);
		endDateAndTimeChooserWidget.init(endDate);

		return datePickersPanel;
	}
	
	@Override
	public void showDialog(JComponent relativeComponent) {

		startDateAndTimeChooserWidget.takeFocus();
		super.showDialog(relativeComponent);
	}
	
	@Override
	public void disposeDialog() {//TODO
		
		startDateAndTimeChooserWidget.dispose();
		endDateAndTimeChooserWidget.dispose();
		
		super.disposeDialog();
		
		
		view=null;
		startDateAndTimeChooserWidget=null;
		endDateAndTimeChooserWidget=null;
	}
}
