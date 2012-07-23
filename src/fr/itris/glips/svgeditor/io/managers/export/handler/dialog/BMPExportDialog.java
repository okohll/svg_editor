package fr.itris.glips.svgeditor.io.managers.export.handler.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.MissingResourceException;

import javax.swing.*;
import javax.swing.border.*;

/**
 * the class of the dialog used to choose the parameters of the bmp export action
 * @author ITRIS, Jordi SUC
 * 
 * TODO: Oliver - delete this class
 */
public class BMPExportDialog extends ExportDialog{

	/**
	 * whether the bmp file should use indexed colors
	 */
	private boolean usePalette=false;

	/**
	 * whether the bmp file should be compressed or not
	 */
	private boolean compress=false;

	/**
	 * the constructor of the class
	 * @param parent the parent frame
	 */
	public BMPExportDialog(Frame parent) {

		super(parent);
		initialize();
	}

	/**
	 * the constructor of the class
	 * @param parent the parent dialog
	 */
	public BMPExportDialog(JDialog parent) {

		super(parent);
		initialize();
	}

	@Override
	protected void initialize() {

		super.initialize();

		String imageSettingsLabel="", usePaletteLabel="";

		if(bundle!=null){

			try{
				exportDialogTitle=bundle.getString("labelbmpexport");
				imageSettingsLabel=bundle.getString("labelimagesettings");
				usePaletteLabel=bundle.getString("labelexportpalette");
			}catch (MissingResourceException ex) {
				ex.printStackTrace();
			}
		}

		//creating the size chooser panel
		JPanel sizechooser=getSizeChooserPanel();

		//setting the title of the dialog
		setTitle(exportDialogTitle);

		//creating the check boxes to select the compression settings
		final JCheckBox usePaletteChk=new JCheckBox(usePaletteLabel);
		usePaletteChk.setHorizontalAlignment(SwingConstants.LEFT);
		usePaletteChk.setSelected(false);

		//adding the listener to the check boxes
		usePaletteChk.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				usePalette=usePaletteChk.isSelected();
			}
		});

		//the panel that will contain the check boxes to select the compression settings
		JPanel compressionSettingsPanel=new JPanel();
		compressionSettingsPanel.setLayout(new BoxLayout(compressionSettingsPanel, BoxLayout.Y_AXIS));

		//creating the layout and filling the panel
		GridBagLayout gridBag=new GridBagLayout();
		compressionSettingsPanel.setLayout(gridBag);
		GridBagConstraints c=new GridBagConstraints();

		c.anchor=GridBagConstraints.WEST;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridwidth=GridBagConstraints.REMAINDER;

		gridBag.setConstraints(usePaletteChk, c);
		compressionSettingsPanel.add(usePaletteChk);
		compressionSettingsPanel.setBorder(new TitledBorder(imageSettingsLabel));

		//handling the parameters panel
		parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));
		parametersPanel.add(sizechooser);
		parametersPanel.add(compressionSettingsPanel);
	}

	/**
	 * @return Returns the compress.
	 */
	public boolean isCompressed() {
		return compress;
	}

	/**
	 * @return Returns the usePalette.
	 */
	public boolean usePalette() {
		return usePalette;
	}
}
