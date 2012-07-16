package fr.itris.glips.extension.jwidget.trends.edition;

import java.awt.*;
import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.edition.display.*;
import fr.itris.glips.extension.jwidget.trends.edition.general.*;
import fr.itris.glips.rtdaeditor.jwidget.*;

/**
 * the class of the configuration panel
 * @author ITRIS, Jordi SUC
 */
public class TrendsConfigurationPanel extends JWidgetConfigurationPanel{
	
	/**
	 * the trend jwidget edition object
	 */
	private JWidgetEdition jwidgetEdition;
	
	/**
	 * the tabbed pane used for switching from the general configuration pane to the
	 */
	private JTabbedPane tabbedPane;
	
	/**
	 * the general config panel
	 */
	private GeneralConfigPanel generalConfigPanel;
	
	/**
	 * the general config panel
	 */
	private DisplayConfigPanel displayConfigPanel;
	
	/**
	 * the constructor of the class
	 * @param jwidgetEdition the trend jwidget edition object
	 */
	public TrendsConfigurationPanel(JWidgetEdition jwidgetEdition) {
		
		this.jwidgetEdition=jwidgetEdition;
		buildPanel();
	}

	@Override
	public void initializePanel() {
		
		if(getElement()!=null) {
			
			generalConfigPanel.initialize(getElement());
			displayConfigPanel.initialize(getElement());
		}
	}
	
	@Override
	public void buildPanel() {
		
		//getting the labels
		String generalLabel="", displayLabel="";
		
		try{
			generalLabel=jwidgetEdition.getBundle().getString("generalTab");
			displayLabel=jwidgetEdition.getBundle().getString("displayTab");
		}catch (Exception ex){}
		
		
		//creating the general configuration panel
		generalConfigPanel=new GeneralConfigPanel(jwidgetEdition);
		JPanel generalConfigPanelWrapper=new JPanel();
		generalConfigPanelWrapper.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		generalConfigPanelWrapper.add(generalConfigPanel);
		
		//creating the display configuration panel
		displayConfigPanel=new DisplayConfigPanel(jwidgetEdition);
		JPanel displayConfigPanelWrapper=new JPanel();
		displayConfigPanelWrapper.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		displayConfigPanelWrapper.add(displayConfigPanel);
		
		//creating the tabbed pane
		tabbedPane=new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
		
		//adding the tabs to the tabbed pane
		tabbedPane.addTab(generalLabel, generalConfigPanelWrapper);
		tabbedPane.addTab(displayLabel, displayConfigPanelWrapper);
		//tabbedPane.addTab(curvesLabel, curvesConfigPanelWrapper);
		
		//adding the tabbed panel to this panel
		SpringLayout layout=new SpringLayout();
		setLayout(layout);
		layout.putConstraint(SpringLayout.NORTH, this, 0, SpringLayout.NORTH, tabbedPane);
		layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, tabbedPane);
		layout.putConstraint(SpringLayout.WEST, this, 0, SpringLayout.WEST, tabbedPane);
		layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, tabbedPane);
		add(tabbedPane);
	}
}
