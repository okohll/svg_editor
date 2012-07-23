/*
 * Created on 19 janv. 2005
 * 
 =============================================
                   GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
GLIPS Graffiti Editor, a SVG Editor
Copyright (C) 2004 Jordi SUC, Philippe Gil, SARL ITRIS

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact : jordi.suc@itris.fr; philippe.gil@itris.fr

 =============================================
 */
package fr.itris.glips.svgeditor.properties;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesFontChooserWidget extends SVGPropertiesWidget{

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesFontChooserWidget(SVGPropertyItem propertyItem) {

		super(propertyItem);
		
		buildComponent();
	}
	
	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent(){
		
		//the value of the property
		String propertyValue=propertyItem.getGeneralPropertyValue();
			
		//builds the array of Integer that will be inserted into the combo box
		Integer[] obj=new Integer[fontList.size()];
		
		for(int i=0;i<fontList.size();i++){
		    
			obj[i]=new Integer(i);
		}
			
		//creates the combo box
		final JComboBox combo=new JComboBox(obj);

		//creates and sets the renderer
		FontFamilyRenderer renderer=new FontFamilyRenderer();
		combo.setRenderer(renderer);

		//sets the selected item
		combo.setSelectedItem(fontFamilyList.contains(propertyValue)?new Integer(fontFamilyList.indexOf(propertyValue)):new Integer(0));
			
		final ActionListener listener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent evt) {
			    
				String value="";
				
				if(combo.getSelectedItem()!=null){
				    
					try{
						value=(String)(fontFamilyList.get(((Integer)combo.getSelectedItem()).intValue()));
					}catch (IndexOutOfBoundsException ex) {
						ex.printStackTrace();
						value="";
					}
				}
						
				//modifies the widgetValue of the property item
				if(value!=null && !value.equals("")){
				    
					propertyItem.changePropertyValue(value);
				}
			}	
		};
			
		//adds a listener to the combo box
		combo.addActionListener(listener);
			
		//the panel that will be contained in the widget object
		JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(combo);
		
		component=panel;
	
		//creates the disposer
		disposer=new Runnable(){

            public void run() {

				combo.removeActionListener(listener);
            }
		};
	}
	
	/**
	 *  the class allowing to display a formatted text in a combo box
	 * @author ITRIS, Jordi SUC
	 */
	protected class FontFamilyRenderer extends JLabel implements ListCellRenderer {
		
		/**
		 * the constructor of the class
		 */
		 protected FontFamilyRenderer() {
		     
			 setOpaque(true);
			 setHorizontalAlignment(LEFT);
			 setVerticalAlignment(CENTER);
		 }

		 /**
		  * returns the component that will be displayed in a combo box
		  * @param list 
		  * @param value
		  * @param index
		  * @param isSelected
		  * @param cellHasFocus
		  * @return the component that will be displayed in a combo box
		  */
		 public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			
			//gets the selected index
			int ind= ((Integer)value).intValue();

			setBackground(list.getBackground());
			setForeground(list.getForeground());
			setText((String)fontFamilyList.get(ind));
			setFont((Font)fontList.get(ind));

			return this;
		 }
	}
}

