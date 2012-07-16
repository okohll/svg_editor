/*
 * Created on 5 mars 2005
 */
package fr.itris.glips.rtda.widget;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * the dialog enabling to choose a numeric value
 * 
 * @author ITRIS, Jordi SUC
 */
public class NumberChooser{
	
	/**
	 * the constant describing the ok action
	 */
	public static int OK_ACTION=0;
	
	/**
	 * the constant describing the cancel action
	 */
	public static int CANCEL_ACTION=1;

	/**
	 * the size of each button
	 */
	private static Dimension buttonDimension=new Dimension(35, 35);
	
	/**
	 * whether the last display of the number chooser ends with a OK or a CANCEL action
	 */
	private int returnedInt=OK_ACTION;
	
	/**
	 * the string that will be displayed in the display panel
	 */
	private StringBuffer displayString=new StringBuffer("0");
	
	/**
	 * the current double value
	 */
	private double currentValue=Double.NaN;
	
	/**
	 * the dispose runnable
	 */
	private Runnable disposeRunnable=null;
	
	/**
	 * the labels
	 */
	private String okLabel="", cancelLabel="", clearLabel="", titleLabel="", errorTitle="", errorMessage="";
	
	/**
	 * the label displaying the number
	 */
	private final JLabel displayLabel=new JLabel(displayString.toString());
	
	/**
	 * the dialog that will be displayed
	 */
	private JDialog dialog=null;
	
	/**
	 * the panel containing all the widgets used to choose a number
	 */
	private JPanel contentPanel=new JPanel();
	
	/**
	 * the constructor of the class
	 * @param bundle a resource bundle
	 */
	public NumberChooser(ResourceBundle bundle){
		
		//getting the labels
		if(bundle!=null){
			
			try{
				okLabel=bundle.getString("labelok");
				cancelLabel=bundle.getString("labelcancel");
				clearLabel=bundle.getString("labelclear");
				titleLabel=bundle.getString("numberchoosertitle");
				errorTitle=bundle.getString("errorTitle");
				errorMessage=bundle.getString("errorMessageNumberChooser");
			}catch (Exception ex){}
		}
		
		//building the panel//

		//the display panel
		final JPanel displayPanel=new JPanel();
		displayPanel.setBorder(new CompoundBorder(
			new EmptyBorder(4, 1, 1, 1), new EtchedBorder(EtchedBorder.LOWERED)));
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));
		
		//the label displaying the number
		displayLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		
		JPanel middlePanel=new JPanel();
		middlePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		middlePanel.setBackground(Color.white);
		displayLabel.setBorder(new EmptyBorder(0, 0, 2, 0));
		middlePanel.add(displayLabel);
		displayPanel.add(middlePanel);

		//the numbers (0, 1, 2, 3, 4, 5, 6, 7, 8, 9) panel
		JPanel numberPanel=new JPanel();
		numberPanel.setLayout(new GridLayout(4, 3, 2, 2));
		
		//creating the buttons
		final JButton 	button0=new JButton("0"), button1=new JButton("1"), button2=new JButton("2"),
								button3=new JButton("3"), button4=new JButton("4"), button5=new JButton("5"),
								button6=new JButton("6"), button7=new JButton("7"), button8=new JButton("8"),
								button9=new JButton("9");
		
		//setting the properties for the buttons
		button0.setPreferredSize(buttonDimension);
		button1.setPreferredSize(buttonDimension);
		button2.setPreferredSize(buttonDimension);
		button3.setPreferredSize(buttonDimension);
		button4.setPreferredSize(buttonDimension);
		button5.setPreferredSize(buttonDimension);
		button6.setPreferredSize(buttonDimension);
		button7.setPreferredSize(buttonDimension);
		button8.setPreferredSize(buttonDimension);
		button9.setPreferredSize(buttonDimension);
		
		Insets margin=new Insets(1, 1, 1, 1);
		
		button0.setMargin(margin);
		button1.setMargin(margin);
		button2.setMargin(margin);
		button3.setMargin(margin);
		button4.setMargin(margin);
		button5.setMargin(margin);
		button6.setMargin(margin);
		button7.setMargin(margin);
		button8.setMargin(margin);
		button9.setMargin(margin);

		//adding the buttons to the panel
		numberPanel.add(button7);
		numberPanel.add(button8);
		numberPanel.add(button9);
		numberPanel.add(button4);
		numberPanel.add(button5);
		numberPanel.add(button6);
		numberPanel.add(button1);
		numberPanel.add(button2);
		numberPanel.add(button3);
		numberPanel.add(button0);
		
		//the panel of the other buttons
		JPanel otherButtonsPanel=new JPanel();
		otherButtonsPanel.setLayout(new GridLayout(4, 1, 2, 2));
		
		//creating the buttons
		final JButton buttonE=new JButton("E"), buttonDot=new JButton("."), buttonMinus=new JButton("+/-");
		
		//setting the properties for the buttons
		buttonE.setPreferredSize(buttonDimension);
		buttonDot.setPreferredSize(buttonDimension);
		buttonMinus.setPreferredSize(buttonDimension);
		
		buttonE.setMargin(margin);
		buttonDot.setMargin(margin);
		buttonMinus.setMargin(margin);
		
		//adding the buttons to the panel
		otherButtonsPanel.add(buttonE);
		otherButtonsPanel.add(buttonDot);
		otherButtonsPanel.add(buttonMinus);
		
		//the buttons panel
		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.setBorder(new CompoundBorder(
			new EmptyBorder(1, 1, 1, 1), new EtchedBorder(EtchedBorder.LOWERED)));
		
		buttonsPanel.add(numberPanel);
		buttonsPanel.add(otherButtonsPanel);

		//the ok and cancel panel
		JPanel okCancelClearPanel=new JPanel();

		//the ok and cancel buttons
		final JButton okButton=new JButton(okLabel), 
			cancelButton=new JButton(cancelLabel), 
			clearButton=new JButton(clearLabel);
		
		//filling the ok and cancel panel
		okCancelClearPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		okCancelClearPanel.add(okButton);
		okCancelClearPanel.add(cancelButton);
		okCancelClearPanel.add(clearButton);

		//creating the listener to the buttons
		final ActionListener buttonsListener=new ActionListener(){

			public void actionPerformed(ActionEvent evt) {

				String stringToAdd="";
				
				//removing the '0' characters at the beginning of the string buffer
				while(displayString.length()>0 && displayString.charAt(0)=='0'){
					
					displayString.deleteCharAt(0);
				}
				
				//if the number of entered characters is inferior to 20
				if(displayString.length()<=20){
					
					//adding the new character
					if(evt.getSource().equals(button0)){
						
						stringToAdd="0";
						
					}else if(evt.getSource().equals(button1)){
						
						stringToAdd="1";
						
					}else if(evt.getSource().equals(button2)){
						
						stringToAdd="2";
						
					}else if(evt.getSource().equals(button3)){
						
						stringToAdd="3";
						
					}else if(evt.getSource().equals(button4)){
						
						stringToAdd="4";
						
					}else if(evt.getSource().equals(button5)){
						
						stringToAdd="5";
						
					}else if(evt.getSource().equals(button6)){
						
						stringToAdd="6";
						
					}else if(evt.getSource().equals(button7)){
						
						stringToAdd="7";
						
					}else if(evt.getSource().equals(button8)){
						
						stringToAdd="8";
						
					}else if(evt.getSource().equals(button9)){

						stringToAdd="9";
						
					}else if(evt.getSource().equals(buttonE)){
						
						//if this character is not already used
						if(displayString.length()>0 && displayString.indexOf("E")==-1 && displayString.charAt(displayString.length()-1)!='.'){
							
							stringToAdd="E";
						}
						
					}else if(evt.getSource().equals(buttonDot)){

						//checks if the '.' character can be added
						if(displayString.length()>0){
							
							if(displayString.indexOf(".")!=-1){
								
								int ind=displayString.indexOf("E");
								
								if(ind!=-1 && ind<displayString.length()-1 && displayString.lastIndexOf(".")<ind){
									
									stringToAdd=".";
								}
								
							}else{
								
								stringToAdd=".";
							}
						}
					}
					
					//appends the accurate button
					displayString.append(stringToAdd);
				}

				if(evt.getSource().equals(clearButton)){
					
					displayString=new StringBuffer("0");
					
				}else if(evt.getSource().equals(buttonMinus) && displayString.length()>0){
					
					//computes the accurate sign
					if(displayString.charAt(0)!='-'){
						
						displayString.insert(0, "-");
						
					}else{
						
						displayString.deleteCharAt(0);
					}
				}
				
				//checks the display string consistency
				if(displayString.length()==0){
					
					displayString=new StringBuffer("0");
				}
				
				//displays the new string
				displayLabel.setText(displayString.toString());
			}
		};
		
		//adding the listener to the buttons
		button0.addActionListener(buttonsListener);
		button1.addActionListener(buttonsListener);
		button2.addActionListener(buttonsListener);
		button3.addActionListener(buttonsListener);
		button4.addActionListener(buttonsListener);
		button5.addActionListener(buttonsListener);
		button6.addActionListener(buttonsListener);
		button7.addActionListener(buttonsListener);
		button8.addActionListener(buttonsListener);
		button9.addActionListener(buttonsListener);
		buttonE.addActionListener(buttonsListener);
		buttonDot.addActionListener(buttonsListener);
		buttonMinus.addActionListener(buttonsListener);
		clearButton.addActionListener(buttonsListener);
		
		//the listener to the ok and cancel buttons
		final ActionListener okCancelButtonsListener=new ActionListener(){

			public void actionPerformed(ActionEvent evt) {

				if(dialog!=null){
					
					
					if(! evt.getSource().equals(okButton)){

						//the cancel button has been pressed//

						returnedInt=CANCEL_ACTION;
						dialog.setVisible(false);
						
					}else{
						
						//the ok button has been pressed//
						
						//computes the entered value
						computeCurrentValue();
						
						//checks the entered value consistency
						if(! Double.isNaN(currentValue) && ! Double.isInfinite(currentValue)){
							
							returnedInt=OK_ACTION;
							dialog.setVisible(false);
							
						}else{
							
							//shows a dialog informing the user that the entered value is wrong
							JOptionPane.showMessageDialog(dialog.getOwner(), errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		};
		
		okButton.addActionListener(okCancelButtonsListener);
		cancelButton.addActionListener(okCancelButtonsListener);

		disposeRunnable=new Runnable(){

			public void run() {

				button0.removeActionListener(buttonsListener);
				button1.removeActionListener(buttonsListener);
				button2.removeActionListener(buttonsListener);
				button3.removeActionListener(buttonsListener);
				button4.removeActionListener(buttonsListener);
				button5.removeActionListener(buttonsListener);
				button6.removeActionListener(buttonsListener);
				button7.removeActionListener(buttonsListener);
				button8.removeActionListener(buttonsListener);
				button9.removeActionListener(buttonsListener);
				buttonE.removeActionListener(buttonsListener);
				buttonDot.removeActionListener(buttonsListener);
				buttonMinus.removeActionListener(buttonsListener);
				
				okButton.removeActionListener(okCancelButtonsListener);
				cancelButton.removeActionListener(okCancelButtonsListener);
				clearButton.removeActionListener(buttonsListener);
			}
		};
		
		//build the dialog content pane
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(displayPanel, BorderLayout.NORTH);
		contentPanel.add(buttonsPanel, BorderLayout.CENTER);
		contentPanel.add(okCancelClearPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * shows the number chooser
	 * @param parent the parent container
	 * @return whether the display of the number chooser ends with a OK or a CANCEL action
	 */
	public int showNumberChooser(Container parent){
		
		if(parent!=null){
			
			if(! (parent instanceof Frame) && ! (parent instanceof JDialog) && 
					parent instanceof JComponent){
				
				//getting the container into which the number chooser will be displayed
				parent=((JComponent)parent).getTopLevelAncestor();
			}

			if(parent!=null){

				//creates the dialog
				if(parent instanceof Dialog){
					
					dialog=new JDialog((Dialog)parent, true);
					
				}else if(parent instanceof Frame){
					
					dialog=new JDialog((Frame)parent, true);
				}
				
				if(dialog!=null){
					
					dialog.setTitle(titleLabel);
					dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.X_AXIS));
					dialog.getContentPane().add(contentPanel);
					dialog.pack();
					
					//sets the location of the dialog box
					int 	x=(int)(parent.getLocationOnScreen().getX()+parent.getWidth()/2-dialog.getSize().getWidth()/2), 
							y=(int)(parent.getLocationOnScreen().getY()+parent.getHeight()/2-dialog.getSize().getHeight()/2);
					
					dialog.setLocation(x,y);
					dialog.setVisible(true);

					dialog.getContentPane().removeAll();
					dialog.dispose();
					
					//sets the initial value for the display 
					displayString=new StringBuffer("0");
					displayLabel.setText(displayString.toString());
				}
			}
		}

		return returnedInt;
	}
	
	/**
	 * disposes the widget
	 */
	public void dispose(){
		
		if(disposeRunnable!=null){
			
			disposeRunnable.run();
		}
	}
	
	/**
	 * computes the number entered in this chooser
	 */
	protected void computeCurrentValue(){
		
		String returnedString=displayString.toString();
		
		String[] values=returnedString.split(("E"));
		double returnedValue=Double.NaN;
		
		//computing the double value
		if(values.length==1){
			
			try{
				returnedValue=Double.parseDouble(values[0]);
			}catch (Exception ex){returnedValue=Double.NaN;}
			
		}else if(values.length==2){
			
			double nb=Double.NaN, exp=Double.NaN;
			
			try{
				nb=Double.parseDouble(values[0]);
			}catch (Exception ex){nb=Double.NaN;}
			
			try{
				exp=Double.parseDouble(values[1]);
			}catch (Exception ex){exp=Double.NaN;}
			
			if(! Double.isNaN(nb) && ! Double.isNaN(exp)){
				
				returnedValue=nb*Math.pow(10, exp);
			}
		}
		
		//sets the new current value
		currentValue=returnedValue;
	}
	
	/**
	 * @return the current value that has been entered
	 */
	public double getEnteredValue(){

		return currentValue;
	}
}
