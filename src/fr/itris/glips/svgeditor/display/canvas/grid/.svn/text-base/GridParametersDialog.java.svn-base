package fr.itris.glips.svgeditor.display.canvas.grid;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.svgeditor.widgets.ColorChooserWidget;

/**
 * the class of the dialog used to select the grid parameters
 * @author Jordi SUC
 */
public class GridParametersDialog extends TitledDialog{
	
	/**
	 * the grid parameters handler
	 */
	private GridParametersManager gridParametersHandler;
	
	/**
	 * the horizontal distance spinner
	 */
	private DoubleSpinnerWidget horizontalSpinner;
	
	/**
	 * the vertical distance spinner
	 */
	private DoubleSpinnerWidget verticalSpinner;
	
	/**
	 * the color chooser
	 */
	private ColorChooserWidget colorChooserWidget;
	
	/**
	 * the dash chooser
	 */
	private DashChooserWidget dashChooserWidget;
	
	/**
	 * whether the entered values of the widgets can be used
	 */
	private boolean correctValues=false;
	
	/**
	 * a constructor of the class
	 * @param gridParametersHandler the grid parameters handler
	 * @param parent the parent frame
	 */
	public GridParametersDialog(
		GridParametersManager gridParametersHandler, Frame parent) {

		super(parent, true, true);
		this.gridParametersHandler=gridParametersHandler;
	}
	
	/**
	 * a constructor of the class
	 * @param gridParametersHandler the grid parameters handler
	 * @param parent the parent dialog
	 */
	public GridParametersDialog(
		GridParametersManager gridParametersHandler, JDialog parent) {

		super(parent, true);
		this.gridParametersHandler=gridParametersHandler;
	}
	
	@Override
	protected JPanel buildContentPanel() {
		
		//the panel that will contain all the widgets
		JPanel cntPanel=new JPanel();

		//getting the labels
		String gridDialogTitleLabel=ResourcesManager.bundle.getString("GridDialogTitle");
		String gridDialogMessageLabel=ResourcesManager.bundle.getString("GridDialogMessage"); 
		String gridColorLabel=ResourcesManager.bundle.getString("GridColor"); 
		String lineStyleLabel=ResourcesManager.bundle.getString("GridLineStyle"); 
		String distanceLabel=ResourcesManager.bundle.getString("GridDistance"); 
		String horizontalDistanceLabel=ResourcesManager.bundle.getString("GridHorizontalDistance"); 
		String verticalDistanceLabel=ResourcesManager.bundle.getString("GridVerticalDistance"); 
		String pxLabel=ResourcesManager.bundle.getString("GridPx"); 
		
		//setting the labels for the dialog
		setTitleMessage(gridDialogTitleLabel);
		setMessage(gridDialogMessageLabel, INFORMATION_TYPE);
		
		//creating the style panel
		JPanel stylePanel=new JPanel();
		
		//creating the jlabels
		JLabel gridColorLbl=new JLabel(gridColorLabel+" : ");
		gridColorLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel lineStyleLbl=new JLabel(lineStyleLabel+" : ");
		lineStyleLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//creating the widgets
		colorChooserWidget=new ColorChooserWidget();
		dashChooserWidget=new DashChooserWidget();
		
		//filling the style panel
		GridBagLayout gridBagLayout=new GridBagLayout();
		stylePanel.setLayout(gridBagLayout);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(2, 2, 2, 2);
		
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=1;
		gridBagLayout.setConstraints(gridColorLbl, c);
		stylePanel.add(gridColorLbl);
		
		c.gridx=1;
		c.weightx=50;
		gridBagLayout.setConstraints(colorChooserWidget, c);
		stylePanel.add(colorChooserWidget);
		
		c.gridx=0;
		c.gridy=1;
		c.weightx=0;
		gridBagLayout.setConstraints(lineStyleLbl, c);
		stylePanel.add(lineStyleLbl);
		
		c.gridx=1;
		c.weightx=50;
		gridBagLayout.setConstraints(dashChooserWidget, c);
		stylePanel.add(dashChooserWidget);
		
		//creating the distance panel
		JPanel distancePanel=new JPanel();
		TitledBorder border=new TitledBorder(distanceLabel);
		distancePanel.setBorder(border);
		
		//creating the jlabels
		JLabel horizontalDistanceLbl=
			new JLabel(horizontalDistanceLabel+" : ");
		horizontalDistanceLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel hPxLbl=new JLabel(pxLabel);
		JLabel verticalDistanceLbl=
			new JLabel(verticalDistanceLabel+" : ");
		verticalDistanceLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel vPxLbl=new JLabel(pxLabel);
		
		//creating the widgets
		horizontalSpinner=
			new DoubleSpinnerWidget(1, 1, 1000000000000D, 1, false);
		verticalSpinner=
			new DoubleSpinnerWidget(1, 1, 1000000000000D, 1, false);
		
		//filling the style panel
		gridBagLayout=new GridBagLayout();
		distancePanel.setLayout(gridBagLayout);
		c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(2, 2, 2, 2);
		
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=1;
		gridBagLayout.setConstraints(horizontalDistanceLbl, c);
		distancePanel.add(horizontalDistanceLbl);
		
		c.gridx=1;
		c.weightx=50;
		gridBagLayout.setConstraints(horizontalSpinner, c);
		distancePanel.add(horizontalSpinner);
		
		c.gridx=2;
		c.weightx=0;
		gridBagLayout.setConstraints(hPxLbl, c);
		distancePanel.add(hPxLbl);
		
		c.gridx=0;
		c.gridy=1;
		gridBagLayout.setConstraints(verticalDistanceLbl, c);
		distancePanel.add(verticalDistanceLbl);
		
		c.gridx=1;
		c.weightx=50;
		gridBagLayout.setConstraints(verticalSpinner, c);
		distancePanel.add(verticalSpinner);
		
		c.gridx=2;
		c.weightx=0;
		gridBagLayout.setConstraints(vPxLbl, c);
		distancePanel.add(vPxLbl);
		
		//building the panel that will be returned
		cntPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		cntPanel.setLayout(new BorderLayout(5, 5));
		cntPanel.add(stylePanel, BorderLayout.NORTH);
		cntPanel.add(distancePanel, BorderLayout.CENTER);
		
		//creating the ok and cancel button listener
		ActionListener buttonsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				correctValues=evt.getSource().equals(okButton);
				setVisible(false);
			}
		};
		
		okButtonListener=buttonsListener;
		cancelButtonListener=buttonsListener;
		okButton.addActionListener(okButtonListener);
		cancelButton.addActionListener(cancelButtonListener);
		
		return cntPanel;
	}
	
	/**
	 * @return whether the entered values of the widgets can be used
	 */
	public boolean isCorrectValues() {
		return correctValues;
	}
	
	/**
	 * @return the horizontal distance for the grid, as entered by the user
	 */
	public double getHorizontalDistance(){
		
		return horizontalSpinner.getWidgetValue();
	}
	
	/**
	 * @return the vertical distance for the grid, as entered by the user
	 */
	public double getVerticalDistance(){
		
		return verticalSpinner.getWidgetValue();
	}
	
	/**
	 * @return the color for the grid, as entered by the user
	 */
	public Color getColor(){
		
		return colorChooserWidget.getCurrentColor();
	}
	
	/**
	 * @return the dashes for the grid, as entered by the user
	 */
	public String getDashes(){
		
		return dashChooserWidget.getValue();
	}

	@Override
	public void showDialog(JComponent relativeComponent) {
		
		//initializing the value of each widget
		horizontalSpinner.init(gridParametersHandler.getHorizontalDistance());
		verticalSpinner.init(gridParametersHandler.getVerticalDistance());
		colorChooserWidget.init(gridParametersHandler.getGridColor());
		dashChooserWidget.init(gridParametersHandler.getStrokeDashesValues());
		horizontalSpinner.takeFocus();
		
		super.showDialog(relativeComponent);
	}
}
