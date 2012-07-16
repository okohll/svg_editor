package fr.itris.glips.rtdaeditor.anim.widgets;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import fr.itris.glips.library.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.dbchooser.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;
import java.awt.event.*;
import java.util.*;


/**
 * the class of the tag chooser
 * @author ITRIS, Jordi SUC
 */
public class ComplexTagChooser extends Widget{
	
	/**
	 * the label
	 */
	private final JLabel jlabel=new JLabel();
	
	/**
	 * the button used to launch the dialog
	 */
	private final JButton moreButton=new JButton();
	
	/**
	 * the tag chooser dialog
	 */
	private TagChooserDialog tagChooserDialog=null;
	
	/**
	 * the labels
	 */
	private String noneLabel="";
	
	/**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
	protected ComplexTagChooser(boolean isEditor){
		
		super(isEditor);	
		buildWidget();
	}
	
	/**
	 * builds this widget
	 */
	protected void buildWidget(){
		
		//getting the labels
		String tagChooserLabel="";
		ResourceBundle bundle=ResourcesManager.bundle;
		
		try{
			tagChooserLabel=bundle.getString("rtdaanim_tagchooserbutton");
			noneLabel="<"+bundle.getString("rtdaanim_none")+">";
		}catch (Exception ex){}
			
		moreButton.setText(tagChooserLabel);
		moreButton.setMargin(new Insets(1, 1, 1, 1));
		jlabel.setOpaque(false);
		
		setLayout(new BorderLayout(1, 0));
		add(jlabel, BorderLayout.CENTER);
		add(moreButton, BorderLayout.EAST);
		
		if(isEditor) {
			
			//creating the tag chooser dialog
			tagChooserDialog=new TagChooserDialog();
			
			//adding the listener to the button
			moreButton.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {

					int result=tagChooserDialog.showDialog(jlabel.getText());
					
					if(result==TagChooserDialog.OK){
						
						String newValue=tagChooserDialog.getValue();
						
						if(newValue==null){
							
							newValue="";
						}

						getItem().setValue(newValue);
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

		String value=item.getValue();
		
		if(value.equals("")){
			
			value=noneLabel;
		}
		
		jlabel.setText(value);
	}
	
	/**
	 * the class of the dialog used to choose a tag or a function
	 * @author ITRIS, Jordi SUC
	 */
	protected class TagChooserDialog{
		
		/**
		 * the ok constant
		 */
		public static final int OK=0;
		
		/**
		 * the cancel constant
		 */
		public static final int CANCEL=1;
		
		/**
		 * the dialog
		 */
		private JDialog dialog=null;
		
		/**
		 * the ok and cancel buttons
		 */
		private final JButton okButton=new JButton(), cancelButton=new JButton();
		
		/**
		 * the tag component
		 */
		private final TagComponent tagComponent=new TagComponent();
		
		/**
		 * the function component
		 */
		private final FunctionComponent functionComponent=new FunctionComponent();
		
		/**
		 * the radio buttons
		 */
		private JRadioButton tagRadioButton=new JRadioButton(), functionRadioButton=new JRadioButton();
		
		/**
		 * the value
		 */
		private String value="";
		
		/**
		 * the result
		 */
		private int currentResult=OK;
		
		/**
		 * the constructor of the class
		 */
		protected TagChooserDialog(){
			
			//creating the dialog
			if(Editor.getParent() instanceof Frame){
				
				dialog=new JDialog((Frame)Editor.getParent(), true);
				
			}else{
				
				dialog=new JDialog(new JFrame(), true);
			}
			
			buildDialog();
		}
		
		/**
		 * builds the dialog
		 */
		protected void buildDialog(){
			
			//the content panel
			JPanel contentPanel=new JPanel();
			
			//getting the labels
			String functionLabel="", tagLabel="", okLabel="", cancelLabel="";
			ResourceBundle bundle=ResourcesManager.bundle;
			
			try{
				okLabel=bundle.getString("labelok");
				cancelLabel=bundle.getString("labelcancel");
				tagLabel=bundle.getString("rtdaanim_tagbutton");
				functionLabel=bundle.getString("rtdaanim_functionbutton");
			}catch (Exception ex){}
			
			//handling the radio buttons
			tagRadioButton.setText(tagLabel);
			functionRadioButton.setText(functionLabel);
			ButtonGroup group=new ButtonGroup();
			group.add(tagRadioButton);
			group.add(functionRadioButton);
			
			//handling the buttons
			okButton.setText(okLabel);
			cancelButton.setText(cancelLabel);
			
			//setting the layout
			GridBagLayout gridBag=new GridBagLayout();
			GridBagConstraints c=new GridBagConstraints();
			contentPanel.setLayout(gridBag);
			c.fill=GridBagConstraints.HORIZONTAL;

			c.gridx=0;
			c.gridy=0;
			gridBag.setConstraints(tagRadioButton, c);
			contentPanel.add(tagRadioButton);

			c.gridx=1;
			gridBag.setConstraints(tagComponent.getComponent(), c);
			contentPanel.add(tagComponent.getComponent());
			
			c.gridx=0;
			c.gridy=1;
			gridBag.setConstraints(functionRadioButton, c);
			contentPanel.add(functionRadioButton);
			c.gridwidth=GridBagConstraints.REMAINDER;

			c.gridx=1;
			c.gridwidth=2;
			gridBag.setConstraints(functionComponent.getComponent(), c);
			contentPanel.add(functionComponent.getComponent());
			
			//the buttons panel
			JPanel buttonsPanel=new JPanel();
			buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			buttonsPanel.add(okButton);
			buttonsPanel.add(cancelButton);
			
			//adding the listener to the radio buttons
			ActionListener radioButtonListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {
					
					boolean enableTag=true;
					
					if(evt.getSource().equals(tagRadioButton)){
						
						value=tagComponent.getValue();
						
					}else{

						enableTag=false;
						value=functionComponent.getValue();
					}
					
					tagComponent.setEnabled(enableTag);
					functionComponent.setEnabled(! enableTag);
				}
			};
			
			tagRadioButton.addActionListener(radioButtonListener);
			functionRadioButton.addActionListener(radioButtonListener);
			
			//adding the listeners to the buttons
			ActionListener buttonListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {

					if(evt.getSource().equals(okButton)){
						
						currentResult=OK;
						
					}else{
						
						currentResult=CANCEL;
					}
					
					dialog.setVisible(false);
				}
			};
			
			okButton.addActionListener(buttonListener);
			cancelButton.addActionListener(buttonListener);
			
			//building the content pane
			Container contentPane=dialog.getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(contentPanel, BorderLayout.CENTER);
			contentPane.add(buttonsPanel, BorderLayout.SOUTH);
			dialog.pack();
			dialog.setLocationRelativeTo(moreButton);
		}
		
		/**
		 * shows the dialog 
		 * @param startValue the initial value
		 * @return whether the user has clicked 
		 */
		public int showDialog(String startValue){

			this.value=startValue;
			
			if(startValue.startsWith("function(")){

				functionRadioButton.setSelected(true);
				functionComponent.setEnabled(true);
				functionComponent.setValue(startValue);
				
			}else{
				
				tagRadioButton.setSelected(true);
				tagComponent.setEnabled(true);
				tagComponent.setValue(startValue);
			}
			
			dialog.setVisible(true);

			return currentResult;
		}

		/**
		 * @return the chosen value
		 */
		public String getValue() {
			return value;
		}
	}
	
	/**
	 * the components used for choosing the tag
	 * @author ITRIS, Jordi SUC
	 */
	protected class TagComponent{
		
		/**
		 * the container
		 */
		private JPanel panel=new JPanel();
		
		/**
		 * the text field
		 */
		private final JTextField tagTextField=new JTextField();
		
		/**
		 * the button used to launch the dialog
		 */
		private final JButton tagMoreButton=new JButton();
		
		/**
		 * the tag value
		 */
		private String tagValue="";
		
		/**
		 * the constructor of the class
		 */
		public TagComponent(){
			
			//getting the labels
			String tagChooserLabel="";
			ResourceBundle bundle=ResourcesManager.bundle;
			
			try{
				tagChooserLabel=bundle.getString("rtdaanim_tagchooserbutton");
			}catch (Exception ex){}
			
			//initializing the component
			tagMoreButton.setText(tagChooserLabel);
			tagTextField.setEditable(false);
			
			panel.setLayout(new BorderLayout(5, 0));
			panel.add(tagTextField);
			panel.add(tagMoreButton);
			
			//adding the listener to the button
			tagMoreButton.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent evt) {

					if(getItem()!=null){
						
						//creating the filter for choosing a tag
						DataBaseNodeFilter filter=new DataBaseNodeFilter(
								tagValue, 0, getItem().getTagType(), false, false, null);
						
						//getting the information object about the selected tag
						DataBaseNodeChooser nodeChooser=DataBaseNodeChooser.getDataBaseNodeChooser(
								Editor.getParent(), getItem().getDocument(), filter, false, false);
						nodeChooser.showDialog(((JComponent)evt.getSource()));
						nodeChooser.disposeDialog();
						DataBaseNodeInformation info=nodeChooser.getInfo();

						if(info!=null){
							
                            String displayedValue=info.getXmlPath();
                            
							tagTextField.setText(displayedValue);
							tagValue=displayedValue;
						}
					}
				}
			});
		}
		
		/**
		 * @return the panel
		 */
		public JPanel getComponent(){
			
			return panel;
		}
		
		/**
		 * @return the chosen value
		 */
		public String getValue(){
			
			return tagValue;
		}
		
		/**
		 * sets the original value before the user chooses another one
		 * @param val a tag value
		 */
		public void setValue(String val){
			
			this.tagValue=val;
			jlabel.setText(val);
		}
		
		/**
		 * enables or disables this component
		 * @param enable whether this component should be enabled
		 */
		public void setEnabled(boolean enable){
			
			tagTextField.setEnabled(enable);
			tagMoreButton.setEnabled(enable);
		}
	}
	
	/**
	 * the components used for the function
	 * @author ITRIS, Jordi SUC
	 */
	protected class FunctionComponent{
		
		/**
		 * the container
		 */
		private JPanel panel=new JPanel();
		
		/**
		 * the combo box
		 */
		private final JComboBox combo=new JComboBox();
		
		/**
		 * the spinners
		 */
		private JSpinner periodSpinner=new JSpinner(), initialValueSpinner=new JSpinner();
		
		/**
		 * the labels
		 */
		private JLabel functionLbl=new JLabel(""), 
								periodLbl=new JLabel(""),
								initialValueLbl=new JLabel("");
		
		/**
		 * the function value
		 */
		private String functionValue="";
		
		/**
		 * the constructor of the class
		 */
		public FunctionComponent(){
			
			//getting the labels
			String sinusLabel="", triangleLabel="", rampLabel="", periodLabel="", 
				initialValueLabel="", functionNameLabel="";
			
			ResourceBundle bundle=ResourcesManager.bundle;
			try{
				functionNameLabel=bundle.getString("rtdaanim_functionname");
				sinusLabel=bundle.getString("rtdaanim_function_sinus");
				triangleLabel=bundle.getString("rtdaanim_function_triangle");
				rampLabel=bundle.getString("rtdaanim_function_ramp");
				periodLabel=bundle.getString("rtdaanim_function_period");
				initialValueLabel=bundle.getString("rtdaanim_function_initialValue");
			}catch (Exception ex){}
			
			//setting the properties of the labels
			functionLbl.setText(functionNameLabel+" : ");
			functionLbl.setHorizontalAlignment(SwingConstants.RIGHT);
			periodLbl.setText(periodLabel+" : ");
			periodLbl.setHorizontalAlignment(SwingConstants.RIGHT);
			initialValueLbl.setText(initialValueLabel+" : ");
			initialValueLbl.setHorizontalAlignment(SwingConstants.RIGHT);
			
			//filling the combo
			combo.addItem(new ComboListItem("sin", sinusLabel));
			combo.addItem(new ComboListItem("triangle", triangleLabel));
			combo.addItem(new ComboListItem("ramp", rampLabel));
			
			//the two spinners
			final SpinnerNumberModel periodSpinnerModel=new SpinnerNumberModel(1.0, 0.1, 10000000, 0.1),
			initialValueSpinnerModel=new SpinnerNumberModel(0, 0, 10000000, 0.1);

			periodSpinner=new JSpinner(periodSpinnerModel);
			initialValueSpinner=new JSpinner(initialValueSpinnerModel);
			
			//setting the layout
			GridBagLayout gridBag=new GridBagLayout();
			panel.setLayout(gridBag);
			GridBagConstraints c=new GridBagConstraints();
			c.fill=GridBagConstraints.BOTH;
			c.insets=new Insets(1, 1, 0, 1);
			c.anchor=GridBagConstraints.EAST;
			
			//adding the combo
			c.gridx=0;
			c.gridy=0;
			gridBag.setConstraints(functionLbl, c);
			panel.add(functionLbl);
			
			c.gridx=1;
			gridBag.setConstraints(combo, c);
			panel.add(combo);
			
			//adding the period spinner
			c.gridx=0;
			c.gridy=1;
			gridBag.setConstraints(periodLbl, c);
			panel.add(periodLbl);
			
			c.gridx=1;
			gridBag.setConstraints(periodSpinner, c);
			panel.add(periodSpinner);
			
			//adding the initial value spinner
			c.gridx=0;
			c.gridy=2;
			gridBag.setConstraints(initialValueLbl, c);
			panel.add(initialValueLbl);
			
			c.gridx=1;
			gridBag.setConstraints(initialValueSpinner, c);
			panel.add(initialValueSpinner);
			
			//adding the listeners
			combo.addActionListener(new ActionListener(){
			
				public void actionPerformed(ActionEvent evt) {
					
					handleChanges();
				}
			});
			
			periodSpinner.addChangeListener(new ChangeListener(){
				
				public void stateChanged(ChangeEvent evt) {

					handleChanges();
				}
			});
			
			initialValueSpinner.addChangeListener(new ChangeListener(){
				
				public void stateChanged(ChangeEvent evt) {

					handleChanges();
				}
			});
		}
		
		/**
		 * returns the correct string to be displayed in the text field
		 * @param functionString a string representing a function
		 * @return the correct string to be displayed in the text field
		 */
		public String getNormalizedString(String functionString){
			
			if(functionString!=null && ! functionString.equals("")){
				
				String result=new String(functionString);
				
				try{
					result=result.replaceAll("\\s+", "");
					result=result.replaceAll("function[(]", "");
					result=result.replaceAll("[)]", "");
					result=result.replaceAll(",", " ");
				}catch (Exception ex){}
				
				return result;
			}
			
			return "";
		}

		/**
		 * handles the changes
		 */
		protected void handleChanges(){

			//creating the string representing the chosen function
			String name="", period="1", initialValue="0";
			
			if(combo.getSelectedItem()!=null){
				
				name=((ComboListItem)combo.getSelectedItem()).getValue().toString();
			}
			
			double dPeriod=1, dInitialValue=0;
			
			try{
				dPeriod=Double.parseDouble(periodSpinner.getValue().toString());
			}catch (Exception ex){dPeriod=1;}
			
			try{
				dInitialValue=Double.parseDouble(initialValueSpinner.getValue().toString());
			}catch (Exception ex){dInitialValue=0;}
			
			period=FormatStore.format(dPeriod);
			initialValue=FormatStore.format(dInitialValue);

			if(name!=null && ! name.equals("")){
				
				functionValue="function("+name+","+period+","+initialValue+")";
			}
		}
		
		/**
		 * @return the panel
		 */
		public JPanel getComponent(){
			
			return panel;
		}
		
		/**
		 * @return the chosen value
		 */
		public String getValue(){
			
			return functionValue;
		}
		
		/**
		 * sets the original value before the user chooses another one
		 * @param val a function value
		 */
		public void setValue(String val){
			
			this.functionValue=val;
			
			String value=getNormalizedString(val);
			
			//sets the accurate values for the function, period and initial values
			if(value!=null && ! value.equals("")){
				
				String[] values=value.split("\\s");
				ComboListItem item=null;
				
				for(int i=0; i<combo.getItemCount(); i++){
					
					item=(ComboListItem)combo.getItemAt(i);
					
					if(item.getValue().equals(values[0])){
						
						combo.setSelectedIndex(i);
						break;
					}
				}
				
				periodSpinner.setValue(new Double(Double.parseDouble(values[1])));
				initialValueSpinner.setValue(new Double(Double.parseDouble(values[2])));
			}
		}
		
		/**
		 * enables or disables this component
		 * @param enable whether this component should be enabled
		 */
		public void setEnabled(boolean enable){
			
			combo.setEnabled(enable);
			periodSpinner.setEnabled(enable);
			initialValueSpinner.setEnabled(enable);
			functionLbl.setEnabled(enable);
			periodLbl.setEnabled(enable);
			initialValueLbl.setEnabled(enable);
		}
	}
	
}
