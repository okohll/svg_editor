package fr.itris.glips.svgeditor.actions.dom;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the dialog used to choose the parameters 
 * used to create a new svg file
 * @author ITRIS, Jordi SUC
 */
public class CanvasSizeDialog extends TitledDialog{

	/**
	 * the SVGCanvasModifier module object
	 */
	private CanvasSizeModule svgCanvasModifier;
	
	/**
	 * the width spinner widget
	 */
	private DoubleSpinnerWidget widthSpinner;
	
	/**
	 * the height spinner widget
	 */
	private DoubleSpinnerWidget heightSpinner;
	
	/**
	 * the constructor of the class
	 * @param svgCanvasModifier the SVGCanvasModifier module object
	 * @param parent the parent of the dialog
	 */
	public CanvasSizeDialog(CanvasSizeModule svgCanvasModifier, Frame parent){
		
		super(parent, true, true);
		this.svgCanvasModifier=svgCanvasModifier;
	}
	
	/**
	 * the constructor of the class
	 * @param svgCanvasModifier the SVGCanvasModifier module object
	 * @param parent the parent of the dialog
	 */
	public CanvasSizeDialog(CanvasSizeModule svgCanvasModifier, JDialog parent){
		
		super(parent, true);
		this.svgCanvasModifier=svgCanvasModifier;
	}
	
	@Override
	protected JPanel buildContentPanel(){
		
		//getting the labels
		ResourceBundle bundle=ResourcesManager.bundle;
		String widthLabel=bundle.getString("labelwidth");
		String heightLabel=bundle.getString("labelheight");
		String titleDialogLabel=bundle.getString("CanvasSizeModifierDialogTitle");
		String titleDialogMessageLabel=bundle.getString("CanvasSizeModifierDialogTitleMessage");

		//setting the title
		setTitleMessage(titleDialogLabel);
		
		//setting the information message
		setMessage(titleDialogMessageLabel, INFORMATION_TYPE);
		
		//creating the jlabels for the spinners
		JLabel widthLbl=new JLabel(widthLabel+" : ");
		JLabel heightLbl=new JLabel(heightLabel+" : ");
		JLabel widthPxLbl=new JLabel("px");
		JLabel heightPxLbl=new JLabel("px");
		
		//creates the spinners
		widthSpinner=new DoubleSpinnerWidget(1, 1, 1000000, 1, false);
		heightSpinner=new DoubleSpinnerWidget(1, 1, 1000000, 1, false);
		
		//initializing the spinners
		widthSpinner.init(2000);
		heightSpinner.init(1000);
		
		//creating and filling the panel that will contain the widgets
		JPanel widgetsPanel=new JPanel();
		widgetsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout layout=new GridBagLayout();
		widgetsPanel.setLayout(layout);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(2, 2, 2, 2);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=0;
		layout.setConstraints(widthLbl, c);
		widgetsPanel.add(widthLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(widthSpinner, c);
		widgetsPanel.add(widthSpinner);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=2;
		c.weightx=0;
		layout.setConstraints(widthPxLbl, c);
		widgetsPanel.add(widthPxLbl);
		
		c.anchor=GridBagConstraints.EAST;
		c.gridx=0;
		c.gridy=1;
		layout.setConstraints(heightLbl, c);
		widgetsPanel.add(heightLbl);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=1;
		c.weightx=50;
		layout.setConstraints(heightSpinner, c);
		widgetsPanel.add(heightSpinner);
		
		c.anchor=GridBagConstraints.WEST;
		c.gridx=2;
		c.weightx=0;
		layout.setConstraints(heightPxLbl, c);
		widgetsPanel.add(heightPxLbl);
		
		//creating the ok and cancel listener
		ActionListener buttonsListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
	
				if(evt.getSource().equals(okButton)){
					
					svgCanvasModifier.changeCanvasSize(new Point2D.Double(
							widthSpinner.getWidgetValue(), heightSpinner.getWidgetValue()));
				}
				
				setVisible(false);
			}
		};
		
		okButtonListener=buttonsListener;
		cancelButtonListener=buttonsListener;
		
		okButton.addActionListener(buttonsListener);
		cancelButton.addActionListener(buttonsListener);

		return widgetsPanel;
	}
	
	/**
	 * showing the dialog
	 * @param relativeComponent the component relatively to which the dialog will be shown
	 * @param currentSize the current size of the canvas
	 */
	public void showDialog(JComponent relativeComponent, Point2D currentSize) {
		
		widthSpinner.init(currentSize.getX());
		heightSpinner.init(currentSize.getY());
		widthSpinner.grabFocus();
		super.showDialog(relativeComponent);
	}
	
	@Override
	public void disposeDialog() {
		
		widthSpinner.dispose();
		heightSpinner.dispose();
		
		super.disposeDialog();
	}
}
