package fr.itris.glips.extension.jwidget.externaldatatable.runtime;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import org.w3c.dom.*;
import fr.itris.glips.rtda.jwidget.*;

/**
 * the class of the widget of the external widget
 * @author Jordi SUC
 */
public class ExternalDataTableWidget extends JPanel{
	
	/**
	 * the external data table runtime object
	 */
	protected ExternalDataTableRuntime externalDataTableRuntime;

	/**
	 * the svg element defining the jwidget
	 */
	protected Element element;

	/**
	 * the table
	 */
	protected JTable theTable;
	
	/**
	 * the button used to reload 
	 */
	protected JButton reloadButton; 
	
	/**
	 * the listener to the reload button
	 */
	protected ActionListener reloadButtonListener; 
	
	/**
	 * the table model
	 */
	protected TableModel model;
	
	/**
	 * the constructor of the class
	 * @param externalDataTableRuntime the external data table runtime
	 * @param element the svg element defining the jwidget
	 */
	public ExternalDataTableWidget(
			final ExternalDataTableRuntime externalDataTableRuntime, Element element){
		
		this.externalDataTableRuntime=externalDataTableRuntime;
		this.element=element;
		
		//creating the table
		theTable=new JTable();
		
    	theTable.setAutoCreateColumnsFromModel(true);
    	
       	//handling the look of the component
    	JWidgetToolkit.handleLook(element, theTable);
		JWidgetToolkit.handleLook(element, theTable.getTableHeader());
		JWidgetToolkit.handleBackgroundAndBorderLook(element, theTable);
		JWidgetToolkit.handleRowHeight(element, theTable);
		
		//whether the horizontal or vertical lines should be displayed
		boolean showHLines=element.getAttribute(
				"showHorizontalLines").equals(Boolean.toString(true));
		boolean showVLines=element.getAttribute(
				"showVerticalLines").equals(Boolean.toString(true));
		
		theTable.setShowHorizontalLines(showHLines);
		theTable.setShowVerticalLines(showVLines);
		
		theTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		theTable.getTableHeader().setReorderingAllowed(false);
		
		//creating the scrollpane for the table
		JScrollPane scrollpane=new JScrollPane(theTable);
		
		//filling the widget panel
		setOpaque(false);
		setLayout(new BorderLayout(0, 2));
		add(scrollpane, BorderLayout.CENTER);
		
		//handling the reload button//
		
		//checking whether the refresh button should be shown
		boolean showRefreshButton=element.getAttribute(
				"showReloadButton").equals(Boolean.toString(true));

		if(showRefreshButton){
			
			reloadButton=new JButton(
					element.getAttribute("reloadButtonLabel"));
			JWidgetToolkit.handleLook(element, 
					new String[]{"reloadButtonForegroundColor", "reloadButtonFontFamily", 
						"reloadButtonFontSize", "reloadButtonBold", "reloadButtonItalic"}, reloadButton);//TODO

			//the panel containing the table and the button
			JPanel refreshPanel=new JPanel();
			refreshPanel.setOpaque(false);
			refreshPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
			
			//adding the reload button
			refreshPanel.add(reloadButton);
			
			//adding the reload panel
			add(refreshPanel, BorderLayout.NORTH);
			
			//adding the listener to the button
			reloadButtonListener=new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {

					externalDataTableRuntime.reload();
				}
			};
			
			reloadButton.addActionListener(reloadButtonListener);
		}
	}
	
	/**
	 * initializes the widget
	 */
	public void initialize(){
		
    	//creating the table
    	theTable.setModel(model);
    	JWidgetToolkit.resizeColumns(theTable);
		validate();
	}
	
	/**
	 * sets the table model
	 * @param model the table model
	 */
	public void setTableModel(TableModel model){
		
		this.model=model;
	}
	
	/**
	 * @return the table
	 */
	public JTable getTable() {
		return theTable;
	}
	
	/**
	 * @return the reload button
	 */
	public JButton getReloadButton() {
		return reloadButton;
	}
	
	/**
	 * disposes the widget
	 */
	public void dispose(){
		
		if(reloadButton!=null){
			
			reloadButton.removeActionListener(reloadButtonListener);
		}
	}
}
