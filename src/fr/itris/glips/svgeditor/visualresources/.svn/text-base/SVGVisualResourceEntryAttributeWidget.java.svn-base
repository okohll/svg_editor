/*
 * Created on 26 août 2004
 * 
 =============================================
 GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
 GLIPS Graffiti Editor, a SVG Editor
 Copyright (C) 2003 Jordi SUC, Philippe Gil, SARL ITRIS
 
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
package fr.itris.glips.svgeditor.visualresources;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/**
 * the class of the widgets that will be displayed in the properties dialog, and enabling to modify the properties (with a entry)
 * of an attribute (or a group of attributes) of a resource node.
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceEntryAttributeWidget extends SVGVisualResourceAttributeWidget{
    
	/**
	 * the constructor of the class
	 * @param resourceObjectAttribute the attribute object that will be modified
	 */
    public SVGVisualResourceEntryAttributeWidget(SVGVisualResourceObjectAttribute resourceObjectAttribute) {

        super(resourceObjectAttribute);
        
        buildComponent();
    }
    
    /**
     * builds the component to be displayed
     */
    protected void buildComponent(){
    	
        //the text field in which the value will be entered
        final JTextField textField=new JTextField(resourceObjectAttribute.getValue(), 20);
        textField.moveCaretPosition(0);
        
        final CaretListener listener=new CaretListener(){

            public void caretUpdate(CaretEvent arg0) {

                //modifies the value
                resourceObjectAttribute.setValue(textField.getText());
            }
        };
        
        textField.addCaretListener(listener);

        //creates the component that will be returned
        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JPanel txtPanel=new JPanel();
        txtPanel.setLayout(new BoxLayout(txtPanel, BoxLayout.X_AXIS));
        txtPanel.setBorder(new EmptyBorder(0, 20, 0, 0));
        txtPanel.add(textField);
        
        panel.add(txtPanel);
        
        component=panel;
        
        disposer=new Runnable(){

	        public void run() {

                //removes the listener
                textField.removeCaretListener(listener);
	        } 
        };
    }

}

