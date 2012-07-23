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
import javax.swing.event.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesSliderWidget extends SVGPropertiesWidget{

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesSliderWidget(SVGPropertyItem propertyItem) {

		super(propertyItem);
		
		buildComponent();
	}
	
	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent(){

		//the panel that will contain the widgets
		final JPanel displayAndSlider=new JPanel();
			
		//the initial value
		String value=propertyItem.getGeneralPropertyValue();
		int val=100;
			
		try{val=(int)(Double.parseDouble(value)*100);}catch (NumberFormatException ex) {
			ex.printStackTrace();
			val=100;
		}
			
		final JSlider slider=new JSlider(0, 100, val);
		slider.setPreferredSize(new Dimension(100, 19));
		final JTextField textField=new JTextField(val+"", 5);
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		final JLabel displayedValue=new JLabel("%");
		
		JPanel textAndLabel=new JPanel();
		textAndLabel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		textAndLabel.add(textField);
		textAndLabel.add(displayedValue);
		
		//adds a listener to the slider
		final MouseAdapter sliderListener=new MouseAdapter(){
		    
			@Override
			public void mouseReleased(MouseEvent evt) {
			    
				//modifies the value of the property item
				String newValue=format.format(((double)slider.getValue())/100);

				propertyItem.changePropertyValue(newValue);
				textField.setText(slider.getValue()+"");
			}
		};

		slider.addMouseListener(sliderListener);
		
		//adding the listener to the textfield
		final CaretListener textFieldListener=new CaretListener(){
			
			public void caretUpdate(CaretEvent evt) {

				double newVal=Double.NaN;
				
				try{
					newVal=Double.parseDouble(textField.getText());
				}catch (NumberFormatException ex) {
					ex.printStackTrace();
					newVal=Double.NaN;
				}
				
				if(! Double.isNaN(newVal)){
					
					if(newVal>100){
						
						newVal=100;

						final int fnewVal=(int)newVal;
						final CaretListener thisListener=this;
						
						SwingUtilities.invokeLater(new Runnable(){
							
							public void run() {

								textField.removeCaretListener(thisListener);
								textField.setText(fnewVal+"");
								textField.addCaretListener(thisListener);
							}
						});
					}
					
					if(newVal<0){
						
						newVal=0;
						
						final int fnewVal=(int)newVal;
						final CaretListener thisListener=this;
						
						SwingUtilities.invokeLater(new Runnable(){
							
							public void run() {

								textField.removeCaretListener(thisListener);
								textField.setText(fnewVal+"");
								textField.addCaretListener(thisListener);
							}
						});
					}

					String newValue=format.format(newVal/100);
					propertyItem.changePropertyValue(newValue);
					slider.setValue((int)newVal);
				}
			}
		};
		
		textField.addCaretListener(textFieldListener);
		
		//adds a listener to the slider
		final ChangeListener sliderChangeListener=new ChangeListener(){
		    
			public void stateChanged(ChangeEvent evt) {
			    
				SwingUtilities.invokeLater(new Runnable() {
					
					public void run() {
						
						textField.removeCaretListener(textFieldListener);
						textField.setText(slider.getValue()+"");
						textField.addCaretListener(textFieldListener);
					}
				});
			}
		};
		
		slider.addChangeListener(sliderChangeListener);

		displayAndSlider.setLayout(new BorderLayout(0, 0));
		displayAndSlider.add(slider, BorderLayout.CENTER);
		displayAndSlider.add(textAndLabel, BorderLayout.NORTH);
		
		component=displayAndSlider;

		//creates the disposer
		disposer=new Runnable(){

            public void run() {

				slider.removeMouseListener(sliderListener);
				slider.removeChangeListener(sliderChangeListener);
				textField.removeCaretListener(textFieldListener);
            }
		};
	}
}
