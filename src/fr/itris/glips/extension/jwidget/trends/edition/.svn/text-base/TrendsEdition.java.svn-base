package fr.itris.glips.extension.jwidget.trends.edition;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.rtda.colorsblinkings.*;
import fr.itris.glips.rtdaeditor.jwidget.*;

/**
 * the class of the editor of the properties of a trend widget
 * @author ITRIS, Jordi SUC
 */
public class TrendsEdition extends JWidgetEdition {
	
	/**
	 * the spinners format
	 */
	public static String spinnersFormat="##########";
	
	/**
	 * the maximum number of curves on the trends panel
	 */
	public static final int curvesNumber=8;
	
    /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
	public TrendsEdition(JWidgetManager jwidgetManager, Frame mainFrame) {
		
		super(jwidgetManager, mainFrame, "TrendsWidget", 17);
		
		//filling the list of the property names
    	propertiesList.add("duration");
    	propertiesList.add("displayTimeGrad");
    	propertiesList.add("displayHorizHorodate");
    	propertiesList.add("horizHorodateFormat");
    	
    	propertiesList.add("displayCursorHorodate");
    	propertiesList.add("cursorHorodateFormat");
    	
    	propertiesList.add("refreshPeriod");
    	propertiesList.add("pointsNumber");
    	propertiesList.add("displayCurvesLegend");
    	
    	propertiesList.add("startMode");
    	
    	propertiesList.add("backgroundColor");
    	propertiesList.add("fontSize");
    	
    	propertiesList.add("showGrid");
    	
    	propertiesList.add("verticalLinesDivisionAutomaticPeriod");
    	propertiesList.add("verticalLinesDivisionDuration");
    	propertiesList.add("verticalLinesDivisionColor");
    	propertiesList.add("verticalLinesDivisionDash");
    	propertiesList.add("verticalLinesSubDivisionDuration");
    	propertiesList.add("verticalLinesSubDivisionColor");
    	propertiesList.add("verticalLinesSubDivisionDash");
    	
    	propertiesList.add("keepTagRecordInHistory");
    	
    	propertiesList.add("interpolationWithPeriod");
    	
    	propertiesList.add("maxDisplayableTime");
    	
    	propertiesList.add("displayScrollBar");
    	
    	propertiesList.add("decimalDigitsNumber");
    	propertiesList.add("higherLimit");
    	propertiesList.add("lowerLimit");
    	
    	propertiesList.add("verticalLinesSubDivisionAutomaticPeriod");
    	
    	propertiesList.add("tag1");
    	propertiesList.add("tag2");
    	
    	propertiesList.add("displayToolBar");//30
    	
    	defaultValues.add("1 min");
    	defaultValues.add(Boolean.toString(true));
    	defaultValues.add(Boolean.toString(true));
    	defaultValues.add("HH:mm:ss");
    	
    	defaultValues.add(Boolean.toString(true));
    	defaultValues.add("HH:mm:ss");
    	
    	defaultValues.add("1 sec");
    	defaultValues.add("1000");
    	defaultValues.add(Boolean.toString(false));
    	
    	defaultValues.add("realTime");
    	
    	defaultValues.add("#000000");
    	defaultValues.add("9");
    	
    	defaultValues.add(Boolean.toString(true));
    	defaultValues.add(Boolean.toString(true));
    	defaultValues.add("10 sec");
    	defaultValues.add("#666666");
    	defaultValues.add("");
    	defaultValues.add("1 sec");
    	defaultValues.add("#333333");
    	defaultValues.add("");
    	
    	defaultValues.add(Boolean.toString(true));
    	defaultValues.add(Boolean.toString(true));
    	
    	defaultValues.add("1 hour");
    	
    	defaultValues.add(Boolean.toString(true));
    	
    	defaultValues.add("3");
    	defaultValues.add("100000");
    	defaultValues.add("0.0001");
    	
    	defaultValues.add(Boolean.toString(true));
    	
    	defaultValues.add("");
    	defaultValues.add("");
    	
    	defaultValues.add(Boolean.toString(true));
		
		//building the configuration panel
		buildConfigurationPanel();
	}

	@Override
	protected BufferedImage createImage(Element jwidgetElement, Dimension size) {
		
		//creating the image
    	BufferedImage image=
    		new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    	
    	//creating the panel of the trends
    	JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		
		boolean displayToolBar=Boolean.parseBoolean(
				getProperty(jwidgetElement, propertiesList.get(30)));
    	
		if(displayToolBar){
			
	    	//creating the toolbar
			JToolBar toolBar=new JToolBar();
			
			//creating the buttons for the tool bar
			JToggleButton switchButton=
				new JToggleButton(TrendsIcons.getIcon("RealTimeMode", false));
			JToggleButton playButton=
				new JToggleButton(TrendsIcons.getIcon("Play", false));
			JToggleButton pauseButton=
				new JToggleButton(TrendsIcons.getIcon("Pause", false));
			
			JButton datePickersDialogButton=
				new JButton(TrendsIcons.getIcon("DatePickersDialog", false));
			
			JToggleButton cursorButton=
				new JToggleButton(TrendsIcons.getIcon("Regular", false));
			JToggleButton lineCursorButton=
				new JToggleButton(TrendsIcons.getIcon("LineCursor", false));
			JToggleButton crossCursorButton=
				new JToggleButton(TrendsIcons.getIcon("CrossCursor", false));
			
			JButton timeZoomInitialButton=
				new JButton(TrendsIcons.getIcon("TimeZoomInitial", false));
			JButton timeZoomInButton=
				new JButton(TrendsIcons.getIcon("TimeZoomIn", false));
			JButton timeZoomOutButton=
				new JButton(TrendsIcons.getIcon("TimeZoomOut", false));
			JButton timeZoomDialogButton=
				new JButton(TrendsIcons.getIcon("TimeZoomDialog", false));
			JButton mouseTimeZoomButton=
				new JButton(TrendsIcons.getIcon("MouseTimeZoom", false));
			JButton verticalZoomInitialButton=
				new JButton(TrendsIcons.getIcon("VerticalZoomInitial", false));
			JButton mouseZoomButton=
				new JButton(TrendsIcons.getIcon("MouseZoom", false));
			
			JButton displayCurvesPropertiesButton=
				new JButton(TrendsIcons.getIcon("DisplayCurvesProperties", false));
			
			//setting the properties of the buttons
			switchButton.setSelected(true);
			playButton.setSelected(true);
			pauseButton.setSelected(false);
			datePickersDialogButton.setEnabled(false);
			cursorButton.setSelected(true);
			lineCursorButton.setSelected(false);
			crossCursorButton.setSelected(false);

			//filling the tool bar
			toolBar.add(switchButton);
			toolBar.addSeparator();
			toolBar.add(playButton);
			toolBar.add(pauseButton);
			toolBar.addSeparator();
			toolBar.add(datePickersDialogButton);
			toolBar.addSeparator();
			toolBar.add(cursorButton);
			toolBar.add(lineCursorButton);
			toolBar.add(crossCursorButton);
			toolBar.addSeparator();
			toolBar.add(timeZoomInitialButton);
			toolBar.add(timeZoomInButton);
			toolBar.add(timeZoomOutButton);
			toolBar.add(timeZoomDialogButton);
			toolBar.add(mouseTimeZoomButton);
			toolBar.add(verticalZoomInitialButton);
			toolBar.add(mouseZoomButton);
			toolBar.addSeparator();
			toolBar.add(displayCurvesPropertiesButton);
			
    		panel.add(toolBar, BorderLayout.NORTH);
		}
		
		boolean displayScrollBar=Boolean.parseBoolean(
				getProperty(jwidgetElement, propertiesList.get(23)));
		
		if(displayScrollBar){
			
			//creating the scroll pane
			JScrollBar scrollBar=new JScrollBar(JScrollBar.HORIZONTAL);
			scrollBar.setMinimum(0);
			scrollBar.setMaximum(100);
			scrollBar.setValue(10);
			scrollBar.setVisibleAmount(30);
			
			panel.add(scrollBar, BorderLayout.SOUTH);
		}
		
		//getting the color of the curves panel
    	String colorString=getProperty(jwidgetElement, propertiesList.get(10));
    	Color curvesPanelColor=ColorsAndBlinkingsToolkit.getColor(colorString);
    	
		//creating the curves panel
		JPanel curvesPanel=new JPanel();
		curvesPanel.setBackground(curvesPanelColor);

		//adding the components to the panel
		panel.add(curvesPanel, BorderLayout.CENTER);
    	panel.setPreferredSize(size);
		
    	JFrame frame=new JFrame();
    	frame.getContentPane().add(panel);
    	frame.pack();  	
    	panel.setSize(size);
    
		//printing the image
    	panel.print(image.getGraphics());
		
		return image;
	}

	@Override
	protected void buildConfigurationPanel() {

		configurationPanel=new TrendsConfigurationPanel(this);
	}
}
