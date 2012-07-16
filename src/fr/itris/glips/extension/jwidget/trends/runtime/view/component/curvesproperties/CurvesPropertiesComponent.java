package fr.itris.glips.extension.jwidget.trends.runtime.view.component.curvesproperties;

import java.awt.*;
import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools.*;

/**
 * the class of the component used to display the widgets used 
 * to modify the curve's properties 
 * @author ITRIS, Jordi SUC
 */
public class CurvesPropertiesComponent{

	/**
	 * the internal frame
	 */
	private JInternalFrame internalFrame;
	
	/**
	 * the components handler
	 */
	private ComponentsHandler handler;
	
	/**
	 * the header panel
	 */
	private HeaderPanel headerPanel;
	
	/**
	 * the data panel
	 */
	private DataPanel dataPanel;
	
	/**
	 * the style panel
	 */
	private StylePanel stylePanel;
	
	/**
	 * the current trends curve configuration
	 */
	private TrendsCurveConfiguration currentTrendsCurveConfiguration;

	/**
	 * the constructor of the class
	 * @param componentsHandler the components handler
	 */
	public CurvesPropertiesComponent(ComponentsHandler componentsHandler) {
		
		this.handler=componentsHandler;
	}
	
	/**
	 * initializes this component
	 */
	public void initialize(){
		
		//setting the properties of the panel
		
		//getting the label for each panel
		String dataPanelLabel=TrendsBundle.bundle.getString("DataPanel");
		String stylePanelLabel=TrendsBundle.bundle.getString("StylePanel");
		
		//creating the content panel
		JPanel contentPanel=new JPanel();

		//creating the data panel
		dataPanel=new DataPanel(this);
		
		//creating the style panel
		stylePanel=new StylePanel();
		
		//creating the header panel
		headerPanel=new HeaderPanel(this);
		
		//creating the tabbed pane
		JTabbedPane tabbedPane=new JTabbedPane();
		tabbedPane.addTab(dataPanelLabel, dataPanel);
		tabbedPane.addTab(stylePanelLabel, stylePanel);

		//building the panel
		BorderLayout borderLayout=new BorderLayout();
		contentPanel.setLayout(borderLayout);

		contentPanel.add(headerPanel, BorderLayout.NORTH);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);
		
		//creating the internal frame
		internalFrame=new JInternalFrame();
		
		//setting the properties
		internalFrame.setFrameIcon(TrendsIcons.getIcon(DisplayCurvesPropertiesTool.id, false));
		internalFrame.setTitle(TrendsBundle.bundle.getString("CurveProperties"));
		
		internalFrame.getContentPane().add(contentPanel);
		internalFrame.setVisible(false);
		handler.getView().getController().getJwidgetRuntime().
			getPicture().getDesktop().add(internalFrame, new Integer(1000000));
	}
	
	/**
	 * sets whether the internal frame should be visible or not
	 * @param visible whether the internal frame should be visible or not
	 * @param location the location of the internal frame
	 */
	public void setVisible(boolean visible, Point location){
		
		if(visible){
			
			internalFrame.pack();
			
			if(location!=null){
				
				internalFrame.setLocation(location);
			}
		}

		internalFrame.setVisible(visible);
		
		if(visible){
			
			internalFrame.toFront();
		}
	}
	
	/**
	 * sets the new location for the internal frame
	 * @param point the new location
	 */
	public void setLocation(Point point){
		
		internalFrame.setLocation(point);
	}
	
	/**
	 * changes the curve for the curves properties
	 * @param trendsCurveConfiguration the trends curve configuration
	 */
	public void changeCurve(TrendsCurveConfiguration trendsCurveConfiguration){
		
		dataPanel.changeCurve(trendsCurveConfiguration);
		stylePanel.changeCurve(trendsCurveConfiguration);
	}
	
	/**
	 * sets the value of the given tag
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	public void setTagValue(String tagName, Object value){
		
		if(currentTrendsCurveConfiguration!=null && 
				currentTrendsCurveConfiguration.getTagName().equals(tagName)){
			
			dataPanel.setCurrentValue(value);
		}
	}
	
	/**
	 * disposes the component
	 */
	public void dispose(){//TODO
		
		headerPanel.dispose();
		stylePanel.dispose();
		internalFrame.setVisible(false);
		internalFrame.getParent().remove(internalFrame);
		internalFrame.dispose();
		
		
		internalFrame=null;
		handler=null;
		headerPanel=null;
		dataPanel=null;
		stylePanel=null;
		currentTrendsCurveConfiguration=null;
	}

	/**
	 * @return the component's handler
	 */
	public ComponentsHandler getComponentsHandler() {
		
		return handler;
	}
}
