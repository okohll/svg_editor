package fr.itris.glips.library.widgets;

import javax.swing.*;
import javax.swing.border.*;
import org.jdesktop.swingx.*;
import fr.itris.glips.library.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

/**
 * the widget used to chose a duration
 * @author ITRIS, Jordi SUC
 */
public class DateAndTimeChooserWidget extends Widget{

	/**
	 * the prefix string for all the labels
	 */
	protected static String labelPrefix="JWidgetTimeChooser_";
	
	/**
	 * the date picker
	 */
	private JXDatePicker datePicker;
	
	/**
	 * the spinner used to choose a number
	 */
	private IntegerSpinnerWidget hourSpinner, minSpinner, secSpinner;
	
	/**
	 * the current date
	 */
	private long date=0;
	
	/**
	 * the listener to the widgets
	 */
	private ActionListener listener;
	
	/**
	 * the constructor of the class
	 */
	public DateAndTimeChooserWidget(){
		
		build();
	}

	/**
	 * initializes the widget with the given value
	 * @param newDate a new value
	 */
	public void init(long newDate){
		
		//removing the listeners to the widgets
		datePicker.removeActionListener(listener);
		hourSpinner.removeListener(listener);
		minSpinner.removeListener(listener);
		secSpinner.removeListener(listener);
		
		if(newDate>0){

			datePicker.setDate(new Date(newDate));
			Calendar cal=Calendar.getInstance();
			cal.setTimeInMillis(newDate);
			date=newDate;
			
			hourSpinner.init(cal.get(Calendar.HOUR_OF_DAY));
			minSpinner.init(cal.get(Calendar.MINUTE));
			secSpinner.init(cal.get(Calendar.SECOND));
		}
		
		//adding the listeners to the widgets
		datePicker.addActionListener(listener);
		hourSpinner.addListener(listener);
		minSpinner.addListener(listener);
		secSpinner.addListener(listener);
	}
	
	@Override
	public void dispose(){
		
		datePicker.removeActionListener(listener);
		hourSpinner.removeListener(listener);
		minSpinner.removeListener(listener);
		secSpinner.removeListener(listener);
		super.dispose();
		
		datePicker=null;
		hourSpinner=null;
		minSpinner=null;
		secSpinner=null;
		listener=null;
	}
	
	@Override
	protected void build(){
		
		//getting the labels
		String dateLabel=Bundle.bundle.getString("DateAndTimeChooser_date");
		String timeLabel=Bundle.bundle.getString("DateAndTimeChooser_time");
		String hourLabel=Bundle.bundle.getString("DateAndTimeChooser_hour");
		String minLabel=Bundle.bundle.getString("DateAndTimeChooser_min");
		String secLabel=Bundle.bundle.getString("DateAndTimeChooser_sec");
		
		//creating the jlabels
		JLabel dateLbl=new JLabel(dateLabel+" : ");
		dateLbl.setFont(FontStore.smallFont);
		dateLbl.setHorizontalAlignment(JLabel.RIGHT);
		JLabel hourLbl=new JLabel(hourLabel+" : ");
		hourLbl.setFont(FontStore.smallFont);
		hourLbl.setHorizontalAlignment(JLabel.RIGHT);
		JLabel minLbl=new JLabel(minLabel+" : ");
		minLbl.setFont(FontStore.smallFont);
		minLbl.setHorizontalAlignment(JLabel.RIGHT);
		JLabel secLbl=new JLabel(secLabel+" : ");
		secLbl.setFont(FontStore.smallFont);
		secLbl.setHorizontalAlignment(JLabel.RIGHT);
		
		//creating the date picker
		datePicker=new JXDatePicker();
		
		//creating the spinners
		hourSpinner=new IntegerSpinnerWidget(0, 0, 23, 1);
		minSpinner=new IntegerSpinnerWidget(0, 0, 59, 1);
		secSpinner=new IntegerSpinnerWidget(0, 0, 59, 1);
		
		//creating the listener to the widgets
		listener=new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {

				//getting the time from the calendar
				Calendar cal=Calendar.getInstance();
				cal.setTime(datePicker.getDate());
				
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);

				//computing the time entered by the user
				date=cal.getTimeInMillis()+
					hourSpinner.getWidgetValue()*3600000L+
						minSpinner.getWidgetValue()*60000L+
							secSpinner.getWidgetValue()*1000L;
				notifyListeners();
			}
		};

		//creating the panel for the date picker
		JPanel datePickerPanel=new JPanel();
		datePickerPanel.setLayout(new BorderLayout(2, 0));
		datePickerPanel.add(dateLbl, BorderLayout.WEST);
		datePickerPanel.add(datePicker, BorderLayout.CENTER);
		
		//creating the panel for the time widgets
		JPanel timePanel=new JPanel();
		TitledBorder border=new TitledBorder(timeLabel);
		border.setTitleFont(FontStore.smallFont);
		timePanel.setBorder(border);
		
		//adding the time widgets
		GridBagLayout layout=new GridBagLayout();
		timePanel.setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(1, 1, 1, 1);
		
		c.gridx=0;
		c.gridy=0;
		c.anchor=GridBagConstraints.WEST;
		layout.setConstraints(hourLbl, c);
		timePanel.add(hourLbl);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.EAST;
		c.weightx=50;
		layout.setConstraints(hourSpinner, c);
		timePanel.add(hourSpinner);
		
		c.gridx=0;
		c.gridy=1;
		c.weightx=1;
		c.anchor=GridBagConstraints.WEST;
		layout.setConstraints(minLbl, c);
		timePanel.add(minLbl);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.EAST;
		c.weightx=50;
		layout.setConstraints(minSpinner, c);
		timePanel.add(minSpinner);
		
		c.gridx=0;
		c.gridy=2;
		c.weightx=1;
		c.anchor=GridBagConstraints.WEST;
		layout.setConstraints(secLbl, c);
		timePanel.add(secLbl);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.EAST;
		c.weightx=50;
		layout.setConstraints(secSpinner, c);
		timePanel.add(secSpinner);

		//adding the widgets to the time chooser widget
		setLayout(new BorderLayout(0, 5));
		add(datePickerPanel, BorderLayout.NORTH);
		add(timePanel, BorderLayout.CENTER);
	}
	
	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
		
		datePicker.setEnabled(enable);
		hourSpinner.setEnabled(enable);
		minSpinner.setEnabled(enable);
		secSpinner.setEnabled(enable);
	}
	
	/**
	 * @return the selected date in milliseconds
	 */
	public long getDate(){
		
		return date;
	}
	
	@Override
	public void takeFocus() {

		hourSpinner.takeFocus();
	}
}
