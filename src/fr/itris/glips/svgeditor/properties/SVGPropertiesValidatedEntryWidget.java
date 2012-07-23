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
import java.util.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;

import fr.itris.glips.svgeditor.resources.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesValidatedEntryWidget extends SVGPropertiesWidget{

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesValidatedEntryWidget(SVGPropertyItem propertyItem) {

		super(propertyItem);
		
		buildComponent();
	}
	
	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent(){

		final ResourceBundle bundle=ResourcesManager.bundle;

		//the text field in which the value will be entered
		final JTextField textField=new JTextField(propertyItem.getGeneralPropertyValue(), 8);
		textField.moveCaretPosition(0);
		
		//adds a key listener to the textfield
		final KeyAdapter keyListener=new KeyAdapter(){
		    
			public void keyPressed(KeyEvent evt) {
			    
				if(evt.getKeyCode()==KeyEvent.VK_ENTER){
				    
					//modifies the widgetValue of the property item
					propertyItem.changePropertyValue(textField.getText());
				}
			}
		};
		
		textField.addKeyListener(keyListener);
			
		final JButton okButton=new JButton();

	    Insets buttonInsets=new Insets(1, 1, 1, 1);
	    okButton.setMargin(buttonInsets);
		
		if(bundle!=null){
		    
			//try{
				okButton.setText(bundle.getString("labelok"));
			//}catch(Exception ex){}
		}
			
		final ActionListener listener=new ActionListener(){
		    
			public void actionPerformed(ActionEvent arg0) {
				//modifies the widgetValue of the property item
				propertyItem.changePropertyValue(textField.getText());
			}
		};
			
		//adds a listener to the button
		okButton.addActionListener(listener);
		
		//creates the component that will be returned
		JPanel validatedPanel=new JPanel();

		validatedPanel.setLayout(new BorderLayout());
		validatedPanel.add(textField, BorderLayout.CENTER);
		validatedPanel.add(okButton, BorderLayout.EAST);
		
		component=validatedPanel;

		//creates the disposer
		disposer=new Runnable(){

            public void run() {

				textField.removeKeyListener(keyListener);
				okButton.removeActionListener(listener);
            }
		};
	}
}

