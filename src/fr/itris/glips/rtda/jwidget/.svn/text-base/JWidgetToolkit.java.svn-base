package fr.itris.glips.rtda.jwidget;

import java.awt.*;
import java.awt.geom.*;
import org.w3c.dom.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtda.*;
import fr.itris.glips.rtda.colorsblinkings.*;
import fr.itris.glips.rtda.components.picture.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * the abstract class of all the jwidget toolkits, handling features shared by the edition
 * and runtime jwidget objects
 * @author ITRIS, Jordi SUC
 */
public abstract class JWidgetToolkit {
	
	/**
	 * the font
	 */
	public static Font theFont=new Font("theFont", Font.ROMAN_BASELINE, 12);
	
	/**
	 * the background color attribute
	 */
	public static String backgroundColorName="backgroundColor";
	
	/**
	 * the isOpaque attribute
	 */
	public static String isOpaqueName="isOpaque";
	
	/**
	 * the sameSizeForButtons attribute
	 */
	public static String sameSizeForButtonsName="sameSizeForButtons";
	
	/**
	 * the display border attribute
	 */
	public static String displayBorderName="displayBorder";
	
	/**
	 * the alignment attribute
	 */
	public static String alignmentName="alignment";
	
	/**
	 * the rowHeight attribute
	 */
	public static String rowHeightName="rowHeight";
	
	/**
	 * the array of the font attribute names
	 */
	public static String[] fontAttributesNames={
		"foregroundColor", "fontFamily", "fontSize", "bold", "italic"};
	
	/**
     * computes the bounds corresponding to the given jwidget element
     * @param picture a svg picture
     * @param jwidgetElement a jwidget element
     * @return the bounds corresponding to the given jwidget element
     */
	public static Rectangle computeBounds(SVGPicture picture, Element jwidgetElement) {

    	Rectangle2D bounds=AnimationsToolkit.getNodeBounds(
    			picture, jwidgetElement.getParentNode());

    	Rectangle componentBounds=new Rectangle((int)bounds.getX(), (int)bounds.getY(), 
    			(int)bounds.getWidth(), (int)bounds.getHeight());
    	
    	return componentBounds;
    }
	
	 /**
     * handles the look attributes of the given jwidget elements
     * @param jwidgetElement a jwidget element
     * @param attributeNames the array of the attributes for the look
     * @param component the component whose look has to be handled
     */
    public static void handleLook(Element jwidgetElement, 
    		String[] attributeNames, Component component) {
    	
    	Set<Component> cmpSet=new HashSet<Component>();
    	cmpSet.add(component);
    	handleLook(jwidgetElement, attributeNames, cmpSet);
    }
	
	 /**
     * handles the look attributes of the given jwidget elements
     * @param jwidgetElement a jwidget element
     * @param component the component whose look has to be handled
     */
    public static void handleLook(Element jwidgetElement, Component component) {
    	
    	Set<Component> cmpSet=new HashSet<Component>();
    	cmpSet.add(component);
    	handleLook(jwidgetElement, fontAttributesNames, cmpSet);
    }
    
    /**
     * handles the foreground color for the component
     * @param jwidgetElement a jwidget element
     * @param attributeName the name of the attribute defining the color
     * @param component the component whose font should be modified
     */
    public static void handleForegroundColor(Element jwidgetElement, 
    		String attributeName, Component component){
    	
    	Set<Component> cmpSet=new HashSet<Component>();
    	cmpSet.add(component);
    	handleLook(jwidgetElement, new String[]{attributeName, "", "", "", ""}, cmpSet);
    }
    
	 /**
     * handles the background and border look attributes of the given jwidget elements
     * @param jwidgetElement a jwidget element
     * @param component the component whose look has to be handled
     */
    public static void handleBackgroundAndBorderLook(
    		Element jwidgetElement, Component component) {
    	
    	//getting the background color for the component
    	String colorString=jwidgetElement.getAttribute(backgroundColorName);
    	Color color=ColorsAndBlinkingsToolkit.getColor(colorString);
    	
    	if(color!=null) {
    		
    		component.setBackground(color);
    	}
    	
    	//getting whether the component should be opaque
    	boolean isOpaque=true;
    	
    	try{
    		isOpaque=Boolean.parseBoolean(
    				jwidgetElement.getAttribute(isOpaqueName));
    	}catch (Exception ex){}
    	
    	((JComponent)component).setOpaque(isOpaque);
    	
       	//getting whether to display the component
    	boolean displayBorder=true;
    	
    	try{
    		displayBorder=Boolean.parseBoolean(
    				jwidgetElement.getAttribute(displayBorderName));
    	}catch (Exception ex){}
    	
    	if(! displayBorder){
    		
    		if(component instanceof JScrollPane){
    			
            	((JScrollPane)component).setViewportBorder(new EmptyBorder(0, 0, 0, 0));
    		}
    		
    		if(! (component instanceof JViewport)){
    			
            	((JComponent)component).setBorder(new EmptyBorder(0, 0, 0, 0));
    		}
    	}
    }
    
	 /**
     * handles the alignment of the given jwidget element
     * @param jwidgetElement a jwidget element
     * @param textField the textField whose alignment has to be handled
     */
    public static void handleAlignment(
    		Element jwidgetElement, JTextField textField) {
    	
    	//getting the alignment for the component
    	int alignment=SwingConstants.CENTER;
    	String alignmentVal=jwidgetElement.getAttribute(alignmentName);
    	
    	if(alignmentVal.equals(AlignmentChooserWidget.alignments[0])){
    		
    		alignment=SwingConstants.LEFT;
    		
    	}else if(alignmentVal.equals(AlignmentChooserWidget.alignments[2])){
    		
    		alignment=SwingConstants.RIGHT;
    	}
    	
		textField.setHorizontalAlignment(alignment);
    }
    
	 /**
     * handles the row height of the provided table
     * @param jwidgetElement a jwidget element
     * @param table the table whose row height should be set
     */
    public static void handleRowHeight(Element jwidgetElement, JTable table) {
    	
    	int rowHeight=-1;
    	
    	try{
    		rowHeight=Integer.parseInt(
    				jwidgetElement.getAttribute(rowHeightName));
    	}catch (Exception ex){}
    	
    	if(rowHeight!=-1){
    		
    		table.setRowHeight(rowHeight);
    	}
    }
    
	 /**
     * handles the look attributes of the given jwidget elements
     * @param jwidgetElement a jwidget element
     * @param components the components whose look has to be handled
     */
    public static void handleLook(Element jwidgetElement, 
    		Set<Component> components) {
    	
    	handleLook(jwidgetElement, fontAttributesNames, components);
    }
    
	 /**
     * handles the look attributes of the given jwidget elements
     * @param jwidgetElement a jwidget element
     * @param attributeNames the array of the attributes for the look
     * @param components the components whose look has to be handled
     */
    public static void handleLook(Element jwidgetElement, 
    		String[] attributeNames, Set<Component> components) {
    	
    	//getting the foreground color for the button
    	String colorString=jwidgetElement.getAttribute(attributeNames[0]);
    	Color color=ColorsAndBlinkingsToolkit.getColor(colorString);
    	
    	if(color!=null) {
    		
    		for(Component component : components) {
    			
        		component.setForeground(color);
    		}
    	}
    	
    	//getting the parameters for the font
    	String name=FontFamilyChooserWidget.SANS_SERIF;
    	int style=Font.PLAIN;
    	int size=12;
    	
    	//getting the font family
    	String nameVal=jwidgetElement.getAttribute(attributeNames[1]);
    	
    	if(! nameVal.equals("")){
    		
    		name=nameVal;
    	}
    	
    	//getting the font size for the button
    	String fontSizeStr=jwidgetElement.getAttribute(attributeNames[2]);
    	
    	try {
    		size=Integer.parseInt(fontSizeStr);
    	}catch (Exception ex) {}
    	
    	if(size<2 && size>1000) {
    		
    		size=12;
    	}
    	
    	//getting the style for the font
    	boolean isBold=false;
    	
    	try{
    		isBold=Boolean.parseBoolean(
    				jwidgetElement.getAttribute(attributeNames[3]));
    	}catch (Exception ex){}

    	boolean isItalic=false;
    	
    	try{
    		isItalic=Boolean.parseBoolean(
    				jwidgetElement.getAttribute(attributeNames[4]));
    	}catch (Exception ex){}
    	
    	//computing the style
    	if(isBold){
    		
    		style=Font.BOLD;
    	}
    	
    	if(isItalic){
    		
    		if(style==Font.BOLD){
    			
    			style=style | Font.ITALIC;
    			
    		}else{
    			
    			style=Font.ITALIC;
    		}
    	}
    	
    	//creating the font
    	Font font=new Font(name, style, size);
    	
		for(Component component : components) {
			
	    	component.setFont(font);
		}
    }
    
	/**
	 * computes and sets the width of each column of the provided table
	 * @param table a table
	 */
	public static void computeColumnsWidth(JTable table) {
		
		Graphics g=table.getGraphics();
		
		if(g!=null) {

			int parentWidth=table.getParent().getWidth();
			int totalCharSize=0, totalCharNumber=0;
			double remainingSpaceForEachColumn=0;
			int[] textWidths=new int[table.getColumnCount()];
			char[] chars=null;
			int textWidth=0;
			
			//the number of enabled columns
			int columnsCount=0;
			
			//getting the length of each column text
			for(int i=0; i<table.getColumnCount(); i++) {

				textWidth=g.getFontMetrics().stringWidth(
						table.getColumnName(i));
				chars=table.getColumnName(i).toCharArray();

				textWidths[i]=textWidth;
				totalCharSize+=textWidth;
				
				totalCharNumber+=chars.length;
				columnsCount++;
			}
			
			//computing the margin size that can be set for each column
			if(totalCharSize<parentWidth && columnsCount>0) {
				
				remainingSpaceForEachColumn=(parentWidth-totalCharSize)/columnsCount;
			}
			
			TableColumn column=null;
			int width=0;
			
			for(int i=0; i<table.getColumnCount(); i++) {

				column=table.getColumnModel().getColumn(i);
				width=textWidths[i];
				width=(int)Math.round(width+remainingSpaceForEachColumn);
				column.setPreferredWidth(width);
			}
		}
	}
	
	/**
	 * resizes all the columns of the provided table 
	 * @param table a table
	 */
	public static void resizeColumns(JTable table){

		computeColumnsWidth(table);
		table.getTableHeader().resizeAndRepaint();
	}
}
