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

import javax.swing.*;
import javax.swing.event.*;

/**
 * @author ITRIS, Jordi SUC
 */
public class SVGPropertiesNumberChooserWidget extends SVGPropertiesWidget{

    /**
     * the constructor of the class
     * @param propertyItem a property item
     */
	public SVGPropertiesNumberChooserWidget(SVGPropertyItem propertyItem) {

		super(propertyItem);
		
		buildComponent();
	}
	
	/**
	 * builds the component that will be displayed
	 */
	protected void buildComponent(){

		String valueType=propertyItem.getPropertyValueType();

	    SpinnerNumberModel spinnerModel=null;
	    
	    double val=0;
	    
	    try{
	        val=Double.parseDouble(propertyItem.getGeneralPropertyValue());
	    }catch(NumberFormatException ex) {
	    		ex.printStackTrace();
	    		val=0;
	    	}
	    
	    if(valueType.equals("positivenumberchooser")){
	        
	        if(val<0){
	            
	            val=0;
	        }
	        
	        spinnerModel=new SpinnerNumberModel(val, 0, 10000000000000D, 1);
	        
	    }else{
	        
	        spinnerModel=new SpinnerNumberModel(val, -10000000000000D, 10000000000000D, 1);
	    }
	    
	    //the spinner
	    final JSpinner spinner=new JSpinner(spinnerModel);
	    
	    //the listener to the changes
	    final ChangeListener changeListener=new ChangeListener(){

            public void stateChanged(ChangeEvent evt) {
                
				propertyItem.changePropertyValue(spinner.getModel().getValue()+"");
            } 
	    };
	    
	    //adding the listener
	    spinner.addChangeListener(changeListener);
	    
	    //the panel that will be returned
	    JPanel panel=new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	    panel.add(spinner);
	    
		component=panel;

		//creates the disposer
		disposer=new Runnable(){

            public void run() {

			    spinner.removeChangeListener(changeListener);
            }
		};
	}
}

