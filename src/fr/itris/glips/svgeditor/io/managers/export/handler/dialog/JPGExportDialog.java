package fr.itris.glips.svgeditor.io.managers.export.handler.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.MissingResourceException;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * the class of the dialog used to choose the parameters of the jpg export action
 * 
 * @author ITRIS, Jordi SUC
 */
public class JPGExportDialog extends ExportDialog{

	/**
	 * the jpeg quality
	 */
	private float jpgQuality=1.0f;

	/**
	 * whether the Huffman tables are optimized
	 */
	private boolean isOptimized=true;

	/**
	 * whether the compression is progressive
	 */
	private boolean isProgressive=false;

	/**
	 * the constructor of the class
	 * @param parent the parent frame
	 */
	public JPGExportDialog(Frame parent) {

		super(parent);
		initialize();
	}

	/**
	 * the constructor of the class
	 * @param parent the parent dialog
	 */
	public JPGExportDialog(JDialog parent) {

		super(parent);
		initialize();
	}

	@Override
	protected void initialize() {

		super.initialize();

		//the labels
		String jpgCompressionSettingsLabel="", jpgQualityLabel="", optimizedLabel="", progressiveLabel="";

		if(bundle!=null){

			try{
				exportDialogTitle=bundle.getString("labeljpgexport");
				jpgCompressionSettingsLabel=bundle.getString("labeljpegcompressionsettings");
				jpgQualityLabel=bundle.getString("labeljpegquality");
				optimizedLabel=bundle.getString("labeljpegoptimized");
				progressiveLabel=bundle.getString("labeljpegprogressive");
			}catch (MissingResourceException ex) {
				ex.printStackTrace();
			}
		}

		//setting the title of the dialog
		setTitle(exportDialogTitle);

		//creating the size chooser panel
		JPanel sizechooser=getSizeChooserPanel();

		//the panel that will contain the slider
		JPanel sliderPanelAndLabel=new JPanel();
		sliderPanelAndLabel.setLayout(new BorderLayout());

		//creating the slider
		final JSlider jpgSlider=new JSlider(0, 100, 100);
		jpgSlider.setPreferredSize(new Dimension(100, 20));
		sliderPanelAndLabel.add(jpgSlider, BorderLayout.CENTER);

		//creating the label
		final JLabel valueLabel=new JLabel(jpgSlider.getValue()+" %");
		valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sliderPanelAndLabel.add(valueLabel, BorderLayout.NORTH);

		//the listener to the slider
		final MouseAdapter jpgSliderListener=new MouseAdapter(){

			@Override
			public void mouseReleased(MouseEvent evt) {

				jpgQuality=((float)(jpgSlider.getValue())/100);
			}
		};

		//adds a listener to the slider
		jpgSlider.addMouseListener(jpgSliderListener);

		ChangeListener jpgSliderChangeListener=new ChangeListener(){

			public void stateChanged(ChangeEvent evt) {

				//setting the label
				valueLabel.setText(jpgSlider.getValue()+" %");
			}
		};

		//adds a listener to the slider
		jpgSlider.addChangeListener(jpgSliderChangeListener);

		//the label for the widgets
		JLabel jpgLabel=new JLabel(jpgQualityLabel.concat(" :"));

		//the "optimized" option checkbox
		final JCheckBox optCheckBox=new JCheckBox(optimizedLabel);

		//adding the listener to the checkbox changes
		optCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				isOptimized=optCheckBox.isSelected();
			}
		});

		//the "progressive" option checkbox
		final JCheckBox progressiveCheckBox=new JCheckBox(progressiveLabel);

		//adding the listener to the checkbox changes
		progressiveCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				isProgressive=progressiveCheckBox.isSelected();
			}
		});

		//creating the panel containing the label and the widget panel
		JPanel qualityPanel=new JPanel();

		qualityPanel.setLayout(new BorderLayout(5, 0));
		qualityPanel.add(jpgLabel, BorderLayout.WEST);
		qualityPanel.add(sliderPanelAndLabel, BorderLayout.CENTER);

		//creating and filling the compression settings panel
		JPanel compressionSettingsPanel=new JPanel();
		compressionSettingsPanel.setBorder(new TitledBorder(jpgCompressionSettingsLabel));

		//creating the layout and filling the panel
		GridBagLayout gridBag=new GridBagLayout();
		compressionSettingsPanel.setLayout(gridBag);
		GridBagConstraints c=new GridBagConstraints();

		c.anchor=GridBagConstraints.WEST;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridwidth=GridBagConstraints.REMAINDER;

		gridBag.setConstraints(qualityPanel, c);
		compressionSettingsPanel.add(qualityPanel);
		gridBag.setConstraints(optCheckBox, c);
		compressionSettingsPanel.add(optCheckBox);
		gridBag.setConstraints(progressiveCheckBox, c);
		compressionSettingsPanel.add(progressiveCheckBox);

		//handling the parameters panel
		parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));
		parametersPanel.add(sizechooser);
		parametersPanel.add(compressionSettingsPanel);
	}

	/**
	 * @return Returns the jpgQuality.
	 */
	public float getJpgQuality() {
		return jpgQuality;
	}

	/**
	 * @return Returns the isOptimized.
	 */
	public boolean isOptimized() {
		return isOptimized;
	}

	/**
	 * @return Returns the isProgressive.
	 */
	public boolean isProgressive() {
		return isProgressive;
	}


}

