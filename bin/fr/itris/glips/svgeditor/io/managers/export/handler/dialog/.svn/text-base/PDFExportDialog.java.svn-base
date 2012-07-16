package fr.itris.glips.svgeditor.io.managers.export.handler.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import com.lowagie.text.*;
import java.util.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the dialog used to choose the parameters of the pdf export action
 * 
 * @author ITRIS, Jordi SUC
 */
public class PDFExportDialog extends ExportDialog{

	/**
	 * the size of the pages
	 */
	protected com.lowagie.text.Rectangle pageSize=null;

	/**
	 * whether the orientation is "portrait"
	 */
	protected boolean isPortrait=true;

	/**
	 * the margins
	 */
	protected Insets margins=new Insets(0, 0, 0, 0);

	/**
	 * information on the pdf file
	 */
	protected String title="", author="", subject="", keywords="", creator="";

	/**
	 * the constructor of the class
	 * @param parent the parent frame
	 */
	public PDFExportDialog(Frame parent) {

		super(parent);
		initialize();
	}

	/**
	 * the constructor of the class
	 * @param parent the parent dialog
	 */
	public PDFExportDialog(JDialog parent) {

		super(parent);
		initialize();
	}

	@Override
	protected void initialize() {

		super.initialize();

		if(bundle!=null){

			try{
				exportDialogTitle=bundle.getString("labelpdfexport");
			}catch (Exception ex){ex.printStackTrace();}
		}

		//setting the title of the dialog
		setTitle(exportDialogTitle);

		//handling the parameters panel
		parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));

		//getting the page parameters panel
		parametersPanel.add(getTabbedPane());
	}

	protected JTabbedPane getTabbedPane() {

		JTabbedPane tabbedPane=new JTabbedPane();

		//getting the labels
		String pageTabLabel="", infoTabLabel="";

		if(bundle!=null){

			try{
				pageTabLabel=bundle.getString("labelexportpdftabpage");
				infoTabLabel=bundle.getString("labelexportpdftabinfo");
			}catch (Exception ex){}
		}

		//adding the tabs
		JPanel pageTab=getPageParametersPanel();
		tabbedPane.addTab(pageTabLabel, pageTab);

		JPanel infoTab=getPageInformationPanel();
		tabbedPane.addTab(infoTabLabel, infoTab);

		return tabbedPane;
	}

	/**
	 * @return Returns the author.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @return Returns the creator.
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @return Returns the isPortrait.
	 */
	public boolean isPortrait() {
		return isPortrait;
	}

	/**
	 * @return Returns the keywords.
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @return Returns the margins.
	 */
	public Insets getMargins() {
		return margins;
	}

	/**
	 * @return Returns the pageSize.
	 */
	public com.lowagie.text.Rectangle getPageSize() {
		return pageSize;
	}

	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return subject;
	}

	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * @return the panel containing the widgets used to modify the parameters of the page
	 */
	protected JPanel getPageParametersPanel() {

		/***********creating the size chooser panel***************/

		//getting the labels
		String exportSizeLabel="", predefLabel="", customLabel="", widthLabel="", heightLabel="";

		if(bundle!=null){

			try{
				predefLabel=bundle.getString("labelexportpredefinedsize");
				customLabel=bundle.getString("labelexportcustomsize");
				exportSizeLabel=bundle.getString("labelexportsize");
				widthLabel=bundle.getString("labelwidth");
				heightLabel=bundle.getString("labelheight");
			}catch (Exception ex){}
		}

		//creating the spinners that will be used to choose the custom size chooser
		final JSpinner widthSpinner=new JSpinner(), heightSpinner=new JSpinner();

		SpinnerNumberModel widthSpinnerModel=new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1);
		SpinnerNumberModel heightSpinnerModel=new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1);

		widthSpinner.setModel(widthSpinnerModel);
		heightSpinner.setModel(heightSpinnerModel);

		((JSpinner.DefaultEditor)widthSpinner.getEditor()).getTextField().setColumns(5);
		((JSpinner.DefaultEditor)heightSpinner.getEditor()).getTextField().setColumns(5);

		//setting the listeners to the spinners
		widthSpinner.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent evt) {

				if(pageSize==null) {

					pageSize=new com.lowagie.text.Rectangle((float)((Double)widthSpinner.getValue()).doubleValue(), 0.0f);

				}else {

					pageSize=new com.lowagie.text.Rectangle((float)((Double)widthSpinner.getValue()).doubleValue(), pageSize.height());
				}
			}
		});

		heightSpinner.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent evt) {

				if(pageSize==null) {

					pageSize=new com.lowagie.text.Rectangle(0.0f, (float)((Double)heightSpinner.getValue()).doubleValue());

				}else {

					pageSize=new com.lowagie.text.Rectangle(pageSize.width(), (float)((Double)heightSpinner.getValue()).doubleValue());
				}
			}
		});

		//creating the labels
		JLabel  lbw=new JLabel(widthLabel.concat(" : ")),
		lbh=new JLabel(heightLabel.concat(" : ")),
		pxw=new JLabel("px"),
		pxh=new JLabel("px");

		lbw.setHorizontalAlignment(SwingConstants.RIGHT);
		lbh.setHorizontalAlignment(SwingConstants.RIGHT);

		//getting the items for the combo of the predefined sizes
		PredefinedPageSizeItem[] items=PredefinedPageSizeItem.getItems();

		//creating the combo box that will display the predefined types
		final JComboBox combo=new JComboBox(items);

		//adding a listener to the combo
		combo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				if(combo.getSelectedItem()!=null) {

					//setting the new size
					try {
						pageSize=((PredefinedPageSizeItem)combo.getSelectedItem()).getSize();
					}catch (Exception ex) {}
				}
			}
		});

		//creating the radio buttons used to choose between a predefined and a personnalized size chooser
		final JRadioButton    predefinedRadioButton=new JRadioButton(predefLabel),
		customRadioButton=new JRadioButton(customLabel);

		ButtonGroup buttonGroup=new ButtonGroup();
		buttonGroup.add(predefinedRadioButton);
		buttonGroup.add(customRadioButton);

		//adding the listeners to the radio buttons
		ChangeListener radioButtonsListener=new ChangeListener() {

			/**
			 * the last selected button
			 */
			private Object lastSelectedButton=null;

			public void stateChanged(ChangeEvent evt) {

				if(lastSelectedButton==null || (lastSelectedButton!=null && ! lastSelectedButton.equals(evt.getSource()))) {

					if(evt.getSource().equals(predefinedRadioButton) && predefinedRadioButton.isSelected()) {

						//enabling the combo box and disabling the spinners
						combo.setEnabled(true);
						widthSpinner.setEnabled(false);
						heightSpinner.setEnabled(false);

						try {
							//setting the new value for the combo
							if(combo.getSelectedItem()!=null) {

								combo.setSelectedItem(combo.getSelectedItem());

							}else {

								combo.setSelectedItem(PredefinedPageSizeItem.getItem(PageSize.A4));
							}

							//setting the new size
							pageSize=((PredefinedPageSizeItem)combo.getSelectedItem()).getSize();

						}catch (Exception ex) {}

					}else if(evt.getSource().equals(customRadioButton) && customRadioButton.isSelected()){

						//disabling the combo box and enabling the spinners
						combo.setEnabled(false);
						widthSpinner.setEnabled(true);
						heightSpinner.setEnabled(true);

						//setting the new value for the spinners
						pageSize=new com.lowagie.text.Rectangle(    (float)((Double)widthSpinner.getValue()).doubleValue(), 
								(float)((Double)heightSpinner.getValue()).doubleValue());
					}

					lastSelectedButton=evt.getSource();
				}
			}
		};

		//adding the runnable to initialize the size chooser panel
		initializers.add(new Runnable() {

			public void run() {

				if(pageSize!=null) {

					//getting the combo item corresponding to the page size
					PredefinedPageSizeItem item=PredefinedPageSizeItem.getItem(pageSize);

					if(item!=null) {

						//enabling the combo box and disabling the spinners
						predefinedRadioButton.setSelected(true);

						//setting the new value for the combo
						combo.setSelectedItem(combo.getSelectedItem());

						return;
					}
				}

				//disabling the combo box and enabling the spinners
				customRadioButton.setSelected(true);

				widthSpinner.setValue(exportSize.x);
				heightSpinner.setValue(exportSize.y);
			}
		});

		//adding the listener to the radio button
		predefinedRadioButton.addChangeListener(radioButtonsListener);
		customRadioButton.addChangeListener(radioButtonsListener);

		//creating and filling the panel of the size chooser
		JPanel sizeChooserPanel=new JPanel();

		JPanel spinnerWidthPanel=new JPanel();
		spinnerWidthPanel.setLayout(new BorderLayout(2, 0));
		spinnerWidthPanel.add(widthSpinner, BorderLayout.CENTER);
		spinnerWidthPanel.add(pxw, BorderLayout.EAST);

		JPanel spinnerHeightPanel=new JPanel();
		spinnerHeightPanel.setLayout(new BorderLayout(2, 0));
		spinnerHeightPanel.add(heightSpinner, BorderLayout.CENTER);
		spinnerHeightPanel.add(pxh, BorderLayout.EAST);

		//setting the layout
		GridBagLayout gridBag=new GridBagLayout();
		GridBagConstraints c=new GridBagConstraints();
		sizeChooserPanel.setLayout(gridBag);
		c.fill=GridBagConstraints.HORIZONTAL;

		//adding the radio buttons 
		c.insets=new Insets(0, 0, 0, 10);
		c.anchor=GridBagConstraints.WEST;
		c.gridwidth=2;
		gridBag.setConstraints(customRadioButton, c);
		sizeChooserPanel.add(customRadioButton);

		c.insets=new Insets(0, 10, 0, 0);
		c.anchor=GridBagConstraints.WEST;
		c.gridwidth=GridBagConstraints.REMAINDER;
		gridBag.setConstraints(predefinedRadioButton, c);
		sizeChooserPanel.add(predefinedRadioButton);

		//adding the custom size chooser widgets
		c.insets=new Insets(0, 25, 0, 0);
		c.anchor=GridBagConstraints.EAST;
		c.gridwidth=1;
		gridBag.setConstraints(lbw, c);
		sizeChooserPanel.add(lbw);

		c.insets=new Insets(0, 0, 0, 0);
		c.anchor=GridBagConstraints.WEST;
		c.gridwidth=1;
		gridBag.setConstraints(spinnerWidthPanel, c);
		sizeChooserPanel.add(spinnerWidthPanel);

		//adding the combo box
		c.insets=new Insets(0, 35, 0, 0);
		c.gridwidth=GridBagConstraints.REMAINDER;
		gridBag.setConstraints(combo, c);
		sizeChooserPanel.add(combo);

		//adding the predefined size chooser widgets
		c.insets=new Insets(0, 25, 0, 0);
		c.anchor=GridBagConstraints.EAST;
		c.gridwidth=1;
		gridBag.setConstraints(lbh, c);
		sizeChooserPanel.add(lbh);

		c.insets=new Insets(0, 0, 0, 0);
		c.anchor=GridBagConstraints.WEST;
		c.gridwidth=1;
		gridBag.setConstraints(spinnerHeightPanel, c);
		sizeChooserPanel.add(spinnerHeightPanel);

		//adding an empty panel
		JPanel emptyPanel=new JPanel();
		c.insets=new Insets(0, 35, 0, 0);
		c.gridwidth=GridBagConstraints.REMAINDER;
		gridBag.setConstraints(emptyPanel, c);
		sizeChooserPanel.add(emptyPanel);

		//setting the border of the size chooser panel
		sizeChooserPanel.setBorder(new CompoundBorder(
				new TitledBorder(exportSizeLabel), new EmptyBorder(0, 0, 4, 0)));

		/***********creating the orientation panel***************/

		//getting the labels
		String orientationLabel="", portraitLabel="", landscapeLabel="";

		if(bundle!=null){

			try{
				orientationLabel=bundle.getString("labelexportorientation");
				portraitLabel=bundle.getString("labelexportportrait");
				landscapeLabel=bundle.getString("labelexportlandscape");
			}catch (Exception ex){}
		}

		//creating the radio buttons
		ButtonGroup orientButtonGroup=new ButtonGroup();

		final JRadioButton   portraitRadioButton=new JRadioButton(portraitLabel), 
		landscapeRadioButton=new JRadioButton(landscapeLabel);

		orientButtonGroup.add(portraitRadioButton);
		orientButtonGroup.add(landscapeRadioButton);

		portraitRadioButton.setSelected(true);

		//adding the listener to the radio buttons
		ActionListener orientationRadioButtonListener=new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				isPortrait=evt.equals(portraitRadioButton);
			}
		};

		portraitRadioButton.addActionListener(orientationRadioButtonListener);
		landscapeRadioButton.addActionListener(orientationRadioButtonListener);

		//creating the orientation panel
		JPanel orientationPanel=new JPanel();
		orientationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		//the panel containing the radio buttons
		JPanel radioButtonsPanel=new JPanel();
		radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
		radioButtonsPanel.add(portraitRadioButton);
		radioButtonsPanel.add(landscapeRadioButton);

		orientationPanel.add(radioButtonsPanel);

		//setting the border of the orientation panel
		orientationPanel.setBorder(new TitledBorder(orientationLabel));

		/***********creating the margin panel***************/

		//getting the labels
		String marginLabel="", topLabel="", bottomLabel="", leftLabel="", rightLabel="";

		if(bundle!=null){

			try{
				marginLabel=bundle.getString("labelexportmargin");
				topLabel=bundle.getString("labelexportmargintop");
				bottomLabel=bundle.getString("labelexportmarginbottom");
				leftLabel=bundle.getString("labelexportmarginleft");
				rightLabel=bundle.getString("labelexportmarginright");
			}catch (Exception ex){}
		}

		//creating the spinners that will be used to choose the margins
		final JSpinner  topSpinner=new JSpinner(), bottomSpinner=new JSpinner(),
		leftSpinner=new JSpinner(), rightSpinner=new JSpinner();

		SpinnerNumberModel  topSpinnerModel=new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1),
		bottomSpinnerModel=new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1),
		leftSpinnerModel=new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1),
		rightSpinnerModel=new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1);

		topSpinner.setModel(topSpinnerModel);
		bottomSpinner.setModel(bottomSpinnerModel);
		leftSpinner.setModel(leftSpinnerModel);
		rightSpinner.setModel(rightSpinnerModel);

		((JSpinner.DefaultEditor)topSpinner.getEditor()).getTextField().setColumns(3);
		((JSpinner.DefaultEditor)bottomSpinner.getEditor()).getTextField().setColumns(3);
		((JSpinner.DefaultEditor)leftSpinner.getEditor()).getTextField().setColumns(3);
		((JSpinner.DefaultEditor)rightSpinner.getEditor()).getTextField().setColumns(3);

		//setting the listeners to the spinners
		ChangeListener changeListener=new ChangeListener(){

			public void stateChanged(ChangeEvent evt) {

				int value=(int)((Double)widthSpinner.getValue()).doubleValue();

				if(evt.getSource().equals(topSpinner)) {

					margins.top=value;

				}else if(evt.getSource().equals(bottomSpinner)) {

					margins.bottom=value;

				}else if(evt.getSource().equals(leftSpinner)) {

					margins.left=value;

				}else if(evt.getSource().equals(rightSpinner)) {

					margins.right=value;
				}
			}
		};

		topSpinner.addChangeListener(changeListener);
		bottomSpinner.addChangeListener(changeListener);
		leftSpinner.addChangeListener(changeListener);
		rightSpinner.addChangeListener(changeListener);

		//creating the jlabels for each spinner
		JLabel  topLbl=new JLabel(topLabel+" : "), bottomLbl=new JLabel(bottomLabel+" : "), 
		leftLbl=new JLabel(leftLabel+" : "), rightLbl=new JLabel(rightLabel+" : "),
		topPx=new JLabel("px"), bottomPx=new JLabel("px"),
		leftPx=new JLabel("px"), rightPx=new JLabel("px");

		topLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		bottomLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		leftLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		rightLbl.setHorizontalAlignment(SwingConstants.RIGHT);

		//creating the margins panel
		JPanel marginsPanel=new JPanel();

		GridBagLayout mgGridBag=new GridBagLayout();
		marginsPanel.setLayout(mgGridBag);
		GridBagConstraints cs=new GridBagConstraints();
		cs.fill=GridBagConstraints.HORIZONTAL;
		cs.gridwidth=1;
		cs.insets=new Insets(0, 0, 2, 0);

		cs.anchor=GridBagConstraints.EAST;
		mgGridBag.setConstraints(topLbl, cs);
		marginsPanel.add(topLbl);

		cs.anchor=GridBagConstraints.WEST;
		mgGridBag.setConstraints(topSpinner, cs);
		marginsPanel.add(topSpinner);

		cs.anchor=GridBagConstraints.WEST;
		cs.insets=new Insets(0, 2, 2, 5);
		mgGridBag.setConstraints(topPx, cs);
		marginsPanel.add(topPx);

		cs.anchor=GridBagConstraints.EAST;
		cs.insets=new Insets(0, 5, 2, 0);
		mgGridBag.setConstraints(bottomLbl, cs);
		marginsPanel.add(bottomLbl);

		cs.anchor=GridBagConstraints.WEST;
		cs.insets=new Insets(0, 0, 2, 0);
		mgGridBag.setConstraints(bottomSpinner, cs);
		marginsPanel.add(bottomSpinner);

		cs.gridwidth=GridBagConstraints.REMAINDER;
		cs.insets=new Insets(0, 2, 2, 0);
		cs.anchor=GridBagConstraints.WEST;
		mgGridBag.setConstraints(bottomPx, cs);
		marginsPanel.add(bottomPx);

		cs.gridwidth=1;
		cs.insets=new Insets(2, 0, 0, 0);
		cs.anchor=GridBagConstraints.EAST;
		mgGridBag.setConstraints(leftLbl, cs);
		marginsPanel.add(leftLbl);

		cs.anchor=GridBagConstraints.WEST;
		mgGridBag.setConstraints(leftSpinner, cs);
		marginsPanel.add(leftSpinner);

		cs.anchor=GridBagConstraints.WEST;
		cs.insets=new Insets(2, 2, 0, 5);
		mgGridBag.setConstraints(leftPx, cs);
		marginsPanel.add(leftPx);

		cs.anchor=GridBagConstraints.EAST;
		cs.insets=new Insets(2, 5, 0, 0);
		mgGridBag.setConstraints(rightLbl, cs);
		marginsPanel.add(rightLbl);

		cs.anchor=GridBagConstraints.WEST;
		cs.insets=new Insets(2, 0, 0, 0);
		mgGridBag.setConstraints(rightSpinner, cs);
		marginsPanel.add(rightSpinner);

		cs.gridwidth=GridBagConstraints.REMAINDER;
		cs.insets=new Insets(2, 0, 0, 0);
		cs.anchor=GridBagConstraints.WEST;
		mgGridBag.setConstraints(rightPx, cs);
		marginsPanel.add(rightPx);

		//setting the border to the margins panel
		marginsPanel.setBorder(new TitledBorder(marginLabel));

		//the panel that will be returned
		JPanel pagePanel=new JPanel();

		GridBagLayout pageGridBag=new GridBagLayout();
		pagePanel.setLayout(pageGridBag);
		GridBagConstraints pc=new GridBagConstraints();

		pc.fill=GridBagConstraints.BOTH;
		pc.insets=new Insets(3, 3, 3, 3);

		pc.gridwidth=GridBagConstraints.REMAINDER;
		pageGridBag.setConstraints(sizeChooserPanel, pc);
		pagePanel.add(sizeChooserPanel);

		pc.gridwidth=1;
		pageGridBag.setConstraints(orientationPanel, pc);
		pagePanel.add(orientationPanel);

		pc.gridwidth=GridBagConstraints.REMAINDER;
		pageGridBag.setConstraints(marginsPanel, pc);
		pagePanel.add(marginsPanel);

		return pagePanel;
	}

	/**
	 * @return the panel containing the widgets for entering information on the page
	 */
	protected JPanel getPageInformationPanel() {

		JPanel pageInfoPanel=new JPanel();

		//getting the labels
		String titleLabel="", authorLabel="", subjectLabel="", keywordsLabel="", creatorLabel="";

		if(bundle!=null){

			try{
				titleLabel=bundle.getString("labelexporttitle");
				authorLabel=bundle.getString("labelexportauthor");
				subjectLabel=bundle.getString("labelexportsubject");
				keywordsLabel=bundle.getString("labelexportkeywords");
				creatorLabel=bundle.getString("labelexportcreator");
			}catch (Exception ex){}
		}

		//creating the jlabels
		JLabel  titleLbl=new JLabel(titleLabel+" : "), authorLbl=new JLabel(authorLabel+" : "), 
		subjectLbl=new JLabel(subjectLabel+" : "), keywordsLbl=new JLabel(keywordsLabel+" : "), 
		creatorLbl=new JLabel(creatorLabel+" : ");

		titleLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		authorLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		subjectLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		keywordsLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		creatorLbl.setHorizontalAlignment(SwingConstants.RIGHT);

		//creating the textfields
		final JTextField titleTxt=new JTextField(25), authorTxt=new JTextField(25), 
		subjectTxt=new JTextField(25), keywordsTxt=new JTextField(25), 
		creatorTxt=new JTextField(25);

		//adding the listener to the textfields
		CaretListener caretListener=new CaretListener() {

			public void caretUpdate(CaretEvent evt) {

				if(evt.getSource().equals(titleTxt)) {

					title=titleTxt.getText();

				}else if(evt.getSource().equals(authorTxt)) {

					author=authorTxt.getText();

				}else if(evt.getSource().equals(subjectTxt)) {

					subject=subjectTxt.getText();

				}else if(evt.getSource().equals(keywordsTxt)) {

					keywords=keywordsTxt.getText();

				}else if(evt.getSource().equals(creatorTxt)) {

					creator=creatorTxt.getText();
				}
			}
		};

		titleTxt.addCaretListener(caretListener);
		authorTxt.addCaretListener(caretListener);
		subjectTxt.addCaretListener(caretListener);
		keywordsTxt.addCaretListener(caretListener);
		creatorTxt.addCaretListener(caretListener);

		//adding the widgets to the panel
		GridBagLayout gridBag=new GridBagLayout();
		pageInfoPanel.setLayout(gridBag);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;

		c.anchor=GridBagConstraints.EAST;
		c.gridwidth=1;
		c.insets=new Insets(2, 4, 2, 0);
		gridBag.setConstraints(titleLbl, c);
		pageInfoPanel.add(titleLbl);

		c.anchor=GridBagConstraints.WEST;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.insets=new Insets(2, 0, 2, 4);
		gridBag.setConstraints(titleTxt, c);
		pageInfoPanel.add(titleTxt);

		c.anchor=GridBagConstraints.EAST;
		c.gridwidth=1;
		c.insets=new Insets(2, 4, 2, 0);
		gridBag.setConstraints(authorLbl, c);
		pageInfoPanel.add(authorLbl);

		c.anchor=GridBagConstraints.WEST;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.insets=new Insets(2, 0, 2, 4);
		gridBag.setConstraints(authorTxt, c);
		pageInfoPanel.add(authorTxt);

		c.anchor=GridBagConstraints.EAST;
		c.gridwidth=1;
		c.insets=new Insets(2, 4, 2, 0);
		gridBag.setConstraints(subjectLbl, c);
		pageInfoPanel.add(subjectLbl);

		c.anchor=GridBagConstraints.WEST;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.insets=new Insets(2, 0, 2, 4);
		gridBag.setConstraints(subjectTxt, c);
		pageInfoPanel.add(subjectTxt);

		c.anchor=GridBagConstraints.EAST;
		c.gridwidth=1;
		c.insets=new Insets(2, 4, 2, 0);
		gridBag.setConstraints(keywordsLbl, c);
		pageInfoPanel.add(keywordsLbl);

		c.anchor=GridBagConstraints.WEST;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.insets=new Insets(2, 0, 2, 4);
		gridBag.setConstraints(keywordsTxt, c);
		pageInfoPanel.add(keywordsTxt);

		c.anchor=GridBagConstraints.EAST;
		c.gridwidth=1;
		c.insets=new Insets(2, 4, 2, 0);
		gridBag.setConstraints(creatorLbl, c);
		pageInfoPanel.add(creatorLbl);

		c.anchor=GridBagConstraints.WEST;
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.insets=new Insets(2, 0, 2, 4);
		gridBag.setConstraints(creatorTxt, c);
		pageInfoPanel.add(creatorTxt);

		return pageInfoPanel;
	}

	/**
	 * the class of the items of the combo box used for choosing a predefined size
	 */
	protected static class PredefinedPageSizeItem{

		/**
		 * the map associating a size object to the corresponding label
		 */
		protected static LinkedHashMap<com.lowagie.text.Rectangle, String> sizeToLabel=
			new LinkedHashMap<com.lowagie.text.Rectangle, String>();

		/**
		 * the labels of the predefined sizes
		 */
		protected static String label_11X17="", labelA0="", labelA1="", labelA10="", labelA2="", 
		labelA3="", labelA4="", labelA5="", labelA6="", labelA7="", labelA8="", 
		labelA9="", labelARCH_A="", labelARCH_B="", labelARCH_C="", labelARCH_D="", 
		labelARCH_E="", labelB0="", labelB1="", labelB2="", labelB3="", labelB4="", 
		labelB5="", labelFLSA="", labelFLSE="", labelHALFLETTER="", labelLEDGER="", 
		labelLEGAL="", labelLETTER="", labelNOTE="";

		/**
		 * the array of the combo items
		 */
		protected static PredefinedPageSizeItem[] items=null;

		static {

			//getting the labels
			ResourceBundle bundle=ResourcesManager.bundle;

			if(bundle!=null){

				try{
					label_11X17=bundle.getString("labelexportpdf_11X17"); 
					labelA0=bundle.getString("labelexportpdfA0"); 
					labelA1=bundle.getString("labelexportpdfA1"); 
					labelA10=bundle.getString("labelexportpdfA10"); 
					labelA2=bundle.getString("labelexportpdfA2"); 
					labelA3=bundle.getString("labelexportpdfA3"); 
					labelA4=bundle.getString("labelexportpdfA4"); 
					labelA5=bundle.getString("labelexportpdfA5");
					labelA6=bundle.getString("labelexportpdfA6"); 
					labelA7=bundle.getString("labelexportpdfA7");
					labelA8=bundle.getString("labelexportpdfA8"); 
					labelA9=bundle.getString("labelexportpdfA9"); 
					labelARCH_A=bundle.getString("labelexportpdfARCH_A"); 
					labelARCH_B=bundle.getString("labelexportpdfARCH_B");
					labelARCH_C=bundle.getString("labelexportpdfARCH_C");
					labelARCH_D=bundle.getString("labelexportpdfARCH_D");
					labelARCH_E=bundle.getString("labelexportpdfARCH_E");
					labelB0=bundle.getString("labelexportpdfB0");
					labelB1=bundle.getString("labelexportpdfB1"); 
					labelB2=bundle.getString("labelexportpdfB2");
					labelB3=bundle.getString("labelexportpdfB3"); 
					labelB4=bundle.getString("labelexportpdfB4"); 
					labelB5=bundle.getString("labelexportpdfB5"); 
					labelFLSA=bundle.getString("labelexportpdfFLSA"); 
					labelFLSE=bundle.getString("labelexportpdfFLSE"); 
					labelHALFLETTER=bundle.getString("labelexportpdfHALFLETTER"); 
					labelLEDGER=bundle.getString("labelexportpdfLEDGER"); 
					labelLEGAL=bundle.getString("labelexportpdfLEGAL"); 
					labelLETTER=bundle.getString("labelexportpdfLETTER"); 
					labelNOTE=bundle.getString("labelexportpdfNOTE");
				}catch (Exception ex){}
			}

			//filling the map of the sizes
			sizeToLabel.put(PageSize._11X17, label_11X17);
			sizeToLabel.put(PageSize.A0, labelA0);
			sizeToLabel.put(PageSize.A1, labelA1);
			sizeToLabel.put(PageSize.A10, labelA10);
			sizeToLabel.put(PageSize.A2, labelA2);
			sizeToLabel.put(PageSize.A3, labelA3);
			sizeToLabel.put(PageSize.A4, labelA4);
			sizeToLabel.put(PageSize.A5, labelA5);
			sizeToLabel.put(PageSize.A6, labelA6);
			sizeToLabel.put(PageSize.A7, labelA7);
			sizeToLabel.put(PageSize.A8, labelA8);
			sizeToLabel.put(PageSize.A9, labelA9);
			sizeToLabel.put(PageSize.ARCH_A, labelARCH_A);
			sizeToLabel.put(PageSize.ARCH_B, labelARCH_B);
			sizeToLabel.put(PageSize.ARCH_C, labelARCH_C);
			sizeToLabel.put(PageSize.ARCH_D, labelARCH_D);
			sizeToLabel.put(PageSize.ARCH_E, labelARCH_E);
			sizeToLabel.put(PageSize.B0, labelB0);
			sizeToLabel.put(PageSize.B1, labelB1);
			sizeToLabel.put(PageSize.B2, labelB2);
			sizeToLabel.put(PageSize.B3, labelB3);
			sizeToLabel.put(PageSize.B4, labelB4);
			sizeToLabel.put(PageSize.B5, labelB5);
			sizeToLabel.put(PageSize.FLSA, labelFLSA);
			sizeToLabel.put(PageSize.FLSE, labelFLSE);
			sizeToLabel.put(PageSize.HALFLETTER, labelHALFLETTER);
			sizeToLabel.put(PageSize.LEDGER, labelLEDGER);
			sizeToLabel.put(PageSize.LEGAL, labelLEGAL);
			sizeToLabel.put(PageSize.LETTER, labelLETTER);
			sizeToLabel.put(PageSize.NOTE, labelNOTE);

			items=new PredefinedPageSizeItem[sizeToLabel.size()];
			com.lowagie.text.Rectangle itemSize=null;
			LinkedList<com.lowagie.text.Rectangle> sizes=
				new LinkedList<com.lowagie.text.Rectangle>(sizeToLabel.keySet());

			//creating all the items according to the size objects that can be found in the map
			for(int i=0; i<sizes.size(); i++) {

				itemSize=sizes.get(i);

				//creating the new combo item
				items[i]=new PredefinedPageSizeItem(itemSize);
			}
		}

		/**
		 * the size associated with the item
		 */
		protected com.lowagie.text.Rectangle size=PageSize.A4;

		/**
		 * the label of this item
		 */
		protected String label="";

		/**
		 * the constructor of the class
		 * @param size the size for the item
		 */
		private PredefinedPageSizeItem(com.lowagie.text.Rectangle size) {

			this.size=size;
			this.label=sizeToLabel.get(size);
		}

		/**
		 * @return Returns the size.
		 */
		public com.lowagie.text.Rectangle getSize() {
			return size;
		}

		@Override
		public String toString() {

			return label;
		}

		/**
		 * @return Returns the sizeToLabel.
		 */
		protected static LinkedHashMap<com.lowagie.text.Rectangle, String> getPageSizeToLabel() {
			return sizeToLabel;
		}

		/**
		 * returns the combo item corresponding to the given page size
		 * @param pageSize a page size
		 * @return the combo item corresponding to the given page size
		 */
		protected static PredefinedPageSizeItem getItem(com.lowagie.text.Rectangle pageSize) {

			PredefinedPageSizeItem item=null;

			if(pageSize!=null) {

				//getting the item that has got the given page size for its size
				for(int i=0; i<items.length; i++) {

					if(items[i]!=null && items[i].getSize()!=null && items[i].getSize().equals(pageSize)) {

						item=items[i];
						break;
					}
				}
			}

			return item;
		}

		/**
		 * @return the array of the available predefined page size items
		 */
		protected static PredefinedPageSizeItem[] getItems() {

			return items;
		}
	}
}
