package fr.itris.glips.extension.jwidget.trends.runtime.view.component.curvesproperties;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.library.widgets.*;

/**
 * the class of the panel displaying the widgets used to display 
 * and modify the curves style properties
 * @author ITRIS, Jordi SUC
 */
public class StylePanel extends JPanel{
	
	/**
	 * the curve configuration object
	 */
	private TrendsCurveConfiguration curveConfiguration;
	
	/**
	 * the listener to the changes on the curves configuration
	 */
	private CurvesConfigurationChangeListener listener;
	
	/**
	 * the checkbox used to chooser whether to display the scale or not
	 */
	private JCheckBox displayScaleCheckBox;
	
	/**
	 * the listener to the checkbox
	 */
	private ActionListener displayScaleCheckBoxListener;
	
	/**
	 * the color chooser widget
	 */
	private ColorChooserWidget colorChooserWidget;
	
	/**
	 * the listener to the color chooser
	 */
	private ActionListener colorChooserWidgetListener;
	
	/**
	 * the dash chooser
	 */
	private DashChooserWidget dashChooser;
	
	/**
	 * the dash chooser listener
	 */
	private ActionListener dashChooserListener;
	
	/**
	 * the thickness chooser
	 */
	private IntegerSpinnerWidget thicknessChooser;
	
	/**
	 * the thickness chooser listener
	 */
	private ActionListener thicknessChooserListener;
	
	/**
	 * the line interpolation chooser
	 */
	private LineInterpolationChooserWidget lineInterpolationChooser;
	
	/**
	 * the line interpolation chooser listener
	 */
	private ActionListener lineInterpolationChooserListener;

	/**
	 * the point style chooser
	 */
	private PointStyleChooserWidget pointStyleChooserWidget;
	
	/**
	 * the point style chooser listener
	 */
	private ActionListener pointStyleChooserWidgetListener;

	/**
	 * the constructor of the class
	 */
	public StylePanel() {

		build();
	}
	
	/**
	 * builds the panel
	 */
	protected void build(){
	
		//creating the listeners to the curves properties
		listener=new CurvesConfigurationChangeListener(){
			
			@Override
			public void curveScaleChanged() {

				updateScaleValue();
			}
			
			@Override
			public void curveStyleChanged() {

				updateStyleValues();
			}
		};
		
		//getting the labels
		String displayScaleLabel=TrendsBundle.bundle.getString("DisplayScale");
		String curveColorLabel=TrendsBundle.bundle.getString("CurveColor");
		String curveDashesLabel=TrendsBundle.bundle.getString("CurveDashes");
		String curveThicknessLabel=TrendsBundle.bundle.getString("CurveThickness");
		String curveInterpolationLabel=TrendsBundle.bundle.getString("CurveInterpolation");
		String curvePointsLabel=TrendsBundle.bundle.getString("CurvePoints");

		//creating the jlabels
		JLabel curveColorJLabel=new JLabel(curveColorLabel+" : ");
		curveColorJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel curveDashesJLabel=new JLabel(curveDashesLabel+" : ");
		curveDashesJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel curveThicknessJLabel=new JLabel(curveThicknessLabel+" : ");
		curveThicknessJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel curveInterpolationJLabel=new JLabel(curveInterpolationLabel+" : ");
		curveInterpolationJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel curvePointsJLabel=new JLabel(curvePointsLabel+" : ");
		curvePointsJLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		//creating the widgets//
		
		//the display scale widget
		displayScaleCheckBox=new JCheckBox(displayScaleLabel);
		
		displayScaleCheckBoxListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				if(curveConfiguration!=null){
					
					curveConfiguration.setDisplayScale(displayScaleCheckBox.isSelected());
				}
			}
		};
		
		//the color chooser widget
		colorChooserWidget=new ColorChooserWidget();
		
		colorChooserWidgetListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				if(curveConfiguration!=null){
					
					curveConfiguration.setColor(colorChooserWidget.getCurrentColor());
				}
			}
		};
		
		//the dash chooser
		dashChooser=new DashChooserWidget();
		
		dashChooserListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				if(curveConfiguration!=null){
					
					curveConfiguration.setDash(dashChooser.getValue());
				}
			}
		};
		
		//the thickness chooser
		thicknessChooser=new IntegerSpinnerWidget(1, 1, 1000, 1);
		
		thicknessChooserListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				if(curveConfiguration!=null){
					
					curveConfiguration.setThickness(thicknessChooser.getWidgetValue());
				}
			}			
		};
		
		//the line interpolation chooser
		lineInterpolationChooser=new LineInterpolationChooserWidget();
		
		lineInterpolationChooserListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				if(curveConfiguration!=null){
					
					curveConfiguration.setInterpolationType(lineInterpolationChooser.getValue());
				}
			}
		};
		
		//the point style chooser
		pointStyleChooserWidget=new PointStyleChooserWidget();
		
		pointStyleChooserWidgetListener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {

				if(curveConfiguration!=null){
					
					curveConfiguration.setPoint(pointStyleChooserWidget.getValue());
				}
			}
		};
		
		//creating and filling the panel
		GridBagLayout gridBagLayout=new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(1, 1, 1, 1);
		
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=2;
		c.anchor=GridBagConstraints.WEST;
		gridBagLayout.setConstraints(displayScaleCheckBox, c);
		add(displayScaleCheckBox);
		
		c.gridx=0;
		c.gridy=1;
		c.gridwidth=1;
		c.anchor=GridBagConstraints.EAST;
		gridBagLayout.setConstraints(curveColorJLabel, c);
		add(curveColorJLabel);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.WEST;
		gridBagLayout.setConstraints(colorChooserWidget, c);
		add(colorChooserWidget);
		
		c.gridx=0;
		c.gridy=2;
		c.anchor=GridBagConstraints.EAST;
		gridBagLayout.setConstraints(curveDashesJLabel, c);
		add(curveDashesJLabel);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.WEST;
		c.weightx=50;
		gridBagLayout.setConstraints(dashChooser, c);
		add(dashChooser);

		c.gridx=0;
		c.gridy=3;
		c.weightx=0;
		c.anchor=GridBagConstraints.EAST;
		gridBagLayout.setConstraints(curveThicknessJLabel, c);
		add(curveThicknessJLabel);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.WEST;
		gridBagLayout.setConstraints(thicknessChooser, c);
		add(thicknessChooser);
		
		c.gridx=0;
		c.gridy=4;
		c.anchor=GridBagConstraints.EAST;
		gridBagLayout.setConstraints(curveInterpolationJLabel, c);
		add(curveInterpolationJLabel);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.WEST;
		gridBagLayout.setConstraints(lineInterpolationChooser, c);
		add(lineInterpolationChooser);
		
		c.gridx=0;
		c.gridy=5;
		c.anchor=GridBagConstraints.EAST;
		gridBagLayout.setConstraints(curvePointsJLabel, c);
		add(curvePointsJLabel);
		
		c.gridx=1;
		c.anchor=GridBagConstraints.WEST;
		gridBagLayout.setConstraints(pointStyleChooserWidget, c);
		add(pointStyleChooserWidget);
	}
	
	/**
	 * changes the curve for the curves properties
	 * @param trendsCurveConfiguration the trends curve configuration
	 */
	public void changeCurve(TrendsCurveConfiguration trendsCurveConfiguration){
		
		if(curveConfiguration!=null){
			
			//removing the previous listener
			curveConfiguration.removeCurvesConfigurationChangeListener(listener);
		}
		
		this.curveConfiguration=trendsCurveConfiguration;

		if(curveConfiguration!=null){
			
			//adding the listener to the new curve
			curveConfiguration.addCurvesConfigurationChangeListener(listener);
		}
		
		updateScaleValue();
		updateStyleValues();
	}
	
	/**
	 * updates the values of the widgets
	 */
	protected void updateScaleValue(){
		
		//removing all the listeners
		displayScaleCheckBox.removeActionListener(displayScaleCheckBoxListener);
		
		//initializing the widgets
		displayScaleCheckBox.setSelected(curveConfiguration.displayScale());
		
		//adding all the listeners
		displayScaleCheckBox.addActionListener(displayScaleCheckBoxListener);
	}
	
	/**
	 * updates the values of the widgets
	 */
	protected void updateStyleValues(){
		
		if(curveConfiguration!=null){
			
			//removing all the listeners
			colorChooserWidget.removeListener(colorChooserWidgetListener);
			dashChooser.removeListener(dashChooserListener);
			thicknessChooser.removeListener(thicknessChooserListener);
			lineInterpolationChooser.removeListener(lineInterpolationChooserListener);
			pointStyleChooserWidget.removeListener(pointStyleChooserWidgetListener);
			
			//initializing the widgets
			colorChooserWidget.init(curveConfiguration.getColor());
			dashChooser.init(curveConfiguration.getDash());
			thicknessChooser.init(curveConfiguration.getThickness()+"");
			lineInterpolationChooser.init(curveConfiguration.getInterpolation());
			pointStyleChooserWidget.init(curveConfiguration.getPoint());
			
			//adding all the listeners
			colorChooserWidget.addListener(colorChooserWidgetListener);
			dashChooser.addListener(dashChooserListener);
			thicknessChooser.addListener(thicknessChooserListener);
			lineInterpolationChooser.addListener(lineInterpolationChooserListener);
			pointStyleChooserWidget.addListener(pointStyleChooserWidgetListener);
		}
	}
	
	/**
	 * disposes the panel
	 */
	public void dispose(){//TODO
		
		displayScaleCheckBox.removeActionListener(displayScaleCheckBoxListener);
		colorChooserWidget.dispose();
		dashChooser.dispose();
		thicknessChooser.dispose();
		lineInterpolationChooser.dispose();
		pointStyleChooserWidget.dispose();
		
		
		
		curveConfiguration=null;
		listener=null;
		displayScaleCheckBox=null;
		displayScaleCheckBoxListener=null;
		colorChooserWidget=null;
		colorChooserWidgetListener=null;
		dashChooser=null;
		dashChooserListener=null;
		thicknessChooser=null;
		thicknessChooserListener=null;
		lineInterpolationChooser=null;
		lineInterpolationChooserListener=null;
		pointStyleChooserWidget=null;
		pointStyleChooserWidgetListener=null;
	}
}
