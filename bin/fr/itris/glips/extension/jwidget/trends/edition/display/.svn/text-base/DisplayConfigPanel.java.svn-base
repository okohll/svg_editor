package fr.itris.glips.extension.jwidget.trends.edition.display;

import java.awt.*;
import javax.swing.*;
import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.jwidget.*;

/**
 * the panel containing all the display widgets used to configure the trends
 * @author ITRIS, Jordi SUC
 */
public class DisplayConfigPanel extends JPanel{

	/**
	 * the jwidget edition object
	 */
	private JWidgetEdition jwidgetEdition;
	
	/**
	 * the style panel
	 */
	private StylePanel stylePanel;
	
	/**
	 * the curves panel
	 */
	private CurvesPanel curvesPanel;
	
	/**
	 * the cursor panel
	 */
	private CursorPanel cursorPanel;
	
	/**
	 * the grid panel
	 */
	private GridPanel gridPanel;
	
	/**
	 * the horizontal axis panel
	 */
	private HorizontalAxisPanel horizontalAxisPanel;
	
	/**
	 * the scroll bar panel
	 */
	private ScrollBarPanel scrollBarPanel;
	
	/**
	 * the values panel
	 */
	private ValuesPanel valuesPanel;
	
	/**
	 * the tool bar panel
	 */
	private ToolBarPanel toolBarPanel;

	/**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition object
	 */
	public DisplayConfigPanel(JWidgetEdition jwidgetEdition){
		
		this.jwidgetEdition=jwidgetEdition;
		build();
	}
	
	/**
	 * builds the panel
	 */
	protected void build(){
		
		//creating the style panel
		stylePanel=new StylePanel(jwidgetEdition);
		
		//creating the curves panel
		curvesPanel=new CurvesPanel(jwidgetEdition);
		
		//creating the cursor panel
		cursorPanel=new CursorPanel(jwidgetEdition);
		
		//creating the grid panel
		gridPanel=new GridPanel(jwidgetEdition);
		
		//creating the horizontal axis panel
		horizontalAxisPanel=new HorizontalAxisPanel(jwidgetEdition);
		
		//creating the scroll bar panel
		scrollBarPanel=new ScrollBarPanel(jwidgetEdition);
		
		//creating the values panel
		valuesPanel=new ValuesPanel(jwidgetEdition);
		
		//creating the toolbar panel
		toolBarPanel=new ToolBarPanel(jwidgetEdition);
		
		//filling the panel with the widgets
		GridBagLayout gridBag=new GridBagLayout();
		setLayout(gridBag);
		GridBagConstraints c=new GridBagConstraints();
		c.insets=new Insets(0, 0, 0, 0);
		c.fill=GridBagConstraints.BOTH;
		c.anchor=GridBagConstraints.NORTHWEST;
		
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=1;
		c.gridheight=3;
		gridBag.setConstraints(horizontalAxisPanel, c);
		add(horizontalAxisPanel);
		
		c.gridx=1;
		c.gridheight=2;
		gridBag.setConstraints(cursorPanel, c);
		add(cursorPanel);

		c.gridy=2;
		c.gridheight=1;
		gridBag.setConstraints(curvesPanel, c);
		add(curvesPanel);
		
		c.gridx=2;
		c.gridy=0;
		gridBag.setConstraints(toolBarPanel, c);
		add(toolBarPanel);
		
		c.gridx=2;
		c.gridy=1;
		c.gridheight=4;
		gridBag.setConstraints(gridPanel, c);
		add(gridPanel);
		
		c.gridx=0;
		c.gridy=3;
		c.gridheight=1;
		gridBag.setConstraints(stylePanel, c);
		add(stylePanel);
		
		c.gridx=1;
		gridBag.setConstraints(scrollBarPanel, c);
		add(scrollBarPanel);

		c.gridx=0;
		c.gridy=4;
		c.gridwidth=2;
		c.gridheight=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		gridBag.setConstraints(valuesPanel, c);
		add(valuesPanel);

	}
	
	/**
	 * initializes the panel
	 * @param element an element
	 */
	public void initialize(Element element) {
		
		stylePanel.initialize(element);
		curvesPanel.initialize(element);
		cursorPanel.initialize(element);
		gridPanel.initialize(element);
		horizontalAxisPanel.initialize(element);
		scrollBarPanel.initialize(element);
		valuesPanel.initialize(element);
		toolBarPanel.initialize(element);
	}
}
