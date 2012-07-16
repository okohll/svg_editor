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
import java.util.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.event.*;
import fr.itris.glips.svgeditor.*;

import java.awt.*;
import java.awt.geom.*;


/**
 * the class of the widgets that will be displayed in the properties dialog, and enabling to modify the properties (with a combo)
 * of an attribute (or a group of attributes) of a resource node.
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourcePatternSizeChooserAttributeWidget extends SVGVisualResourceAttributeWidget{

	/**
	 * the constructor of the class
	 * @param resourceObjectAttribute the attribute object that will be modified
	 */
    public SVGVisualResourcePatternSizeChooserAttributeWidget(SVGVisualResourceObjectAttribute resourceObjectAttribute) {

        super(resourceObjectAttribute);
        
        buildComponent();
    }
    
    /**
     * builds the component to be displayed
     */
    protected void buildComponent(){
        
        if(resourceObjectAttribute!=null){
            
            final Editor svgEditor=resourceObjectAttribute.getModel().getVisualResources().getSVGEditor();
            
            //getting the attributes//
            SVGVisualResourceObjectAttribute 	xAtt=null, yAtt=null, wAtt=null, hAtt=null,
            														patternUnitsAtt=null, att=null;
            
            final String patternUnitsOther="userSpaceOnUse";

            String nm="";
            
            for(Iterator it=resourceObjectAttribute.getGroupAttributes().iterator();it.hasNext();){
                
                try{
                    att=(SVGVisualResourceObjectAttribute)it.next();
                }catch (Exception ex){att=null;}
                
                if(att!=null){
                    
                    nm=att.getModel().getName();
                    
                    if(nm.equals("x")){
                        
                        xAtt=att;
                        
                    }else if(nm.equals("y")){
                        
                        yAtt=att;
                        
                    }else if(nm.equals("width")){
                        
                        wAtt=att;
                        
                    }else if(nm.equals("height")){
                        
                        hAtt=att;
                        
                    }else if(nm.equals("patternUnits")){
                        
                        patternUnitsAtt=att;
                    }
                }
            }
            
            final SVGVisualResourceObjectAttribute 	fxAtt=xAtt, fyAtt=yAtt, fwAtt=wAtt, fhAtt=hAtt, 
																			fpatternUnitsAtt=patternUnitsAtt;
            
            //getting the bounds values//
            Rectangle2D.Double bounds=new Rectangle2D.Double(0, 0, 0, 0), 
            								defBounds=new Rectangle2D.Double(0, 0, 0, 0),
            								otherBounds=new Rectangle2D.Double(0, 0, 0, 0);

            //the object containing the values for this chooser
            SVGPatternSizeChooserValues patternChValues=null;
            
            if(patternUnitsAtt!=null && xAtt!=null && yAtt!=null && wAtt!=null && hAtt!=null){
                
                boolean isPatternUnitsDefault=(! patternUnitsAtt.getValue().equals(patternUnitsOther));
                
                bounds.x=svgEditor.getSVGToolkit().getDoubleValue(xAtt.getValue(), isPatternUnitsDefault);
                bounds.y=svgEditor.getSVGToolkit().getDoubleValue(yAtt.getValue(), isPatternUnitsDefault);
                bounds.width=svgEditor.getSVGToolkit().getDoubleValue(wAtt.getValue(), isPatternUnitsDefault);
                bounds.height=svgEditor.getSVGToolkit().getDoubleValue(hAtt.getValue(), isPatternUnitsDefault);

                if(isPatternUnitsDefault){
                    
                    defBounds=bounds;

                }else{
                    
                    otherBounds=bounds;
                    
                    defBounds=new Rectangle2D.Double(
                            svgEditor.getSVGToolkit().getDoubleValue(xAtt.getModel().getDefaultValue(), true),
                            svgEditor.getSVGToolkit().getDoubleValue(yAtt.getModel().getDefaultValue(), true),
                            svgEditor.getSVGToolkit().getDoubleValue(wAtt.getModel().getDefaultValue(), true),
                            svgEditor.getSVGToolkit().getDoubleValue(hAtt.getModel().getDefaultValue(), true));
                }
                
                patternChValues=new SVGPatternSizeChooserValues(defBounds, otherBounds, isPatternUnitsDefault);
            }
            
            //the object containing the values for this chooser
            final SVGPatternSizeChooserValues fpatternChValues=patternChValues;
            
            if(patternChValues!=null && xAtt!=null && yAtt!=null && wAtt!=null && hAtt!=null){
                
                //creating the combo used to choose the units//
                
                //the list of the combo items
                final LinkedList itmList=new LinkedList();

                //the map of the possible values
                final LinkedHashMap possibleValues=patternUnitsAtt.getModel().getPossibleValues();
                
                SVGComboItem itm=null, selectedItm=null;
                String val="", lbl="";
                
                //adding an item with an empty string
                itm=new SVGComboItem("","");
                itmList.add(itm);
                
                //builds the array of items for the combo
                for(Iterator it=possibleValues.keySet().iterator(); it.hasNext();){

                    try{
                        nm=(String)it.next();
                        val=(String)possibleValues.get(nm);
                        
                        //gets the label
                        lbl=nm;
                        
                        if(bundle!=null){
                            
                            lbl=bundle.getString(nm);
                        }
                        
                        itm=new SVGComboItem(val, lbl);
                        
                        //checks if the current possible value is equals to the current value of the property
                        if(val!=null && val.equals(patternUnitsAtt.getValue())){
                            
                            selectedItm=itm;
                        }
                        
                    }catch (Exception ex){itm=null; nm="";}
                    
                    //adds the item to the list
                    if(itm!=null){
                        
                        itmList.add(itm);
                    }
                }
                
                Object[] itms=itmList.toArray();
                
                //the combo box
                final JComboBox combo=new JComboBox(itms);

                //sets the selected item
                if(selectedItm!=null)combo.setSelectedItem(selectedItm);

                String patternUnitsLabel="";
                
                if(fpatternUnitsAtt!=null){
                    
                    patternUnitsLabel=fpatternUnitsAtt.getModel().getName();
                    
                    if(bundle!=null){
                        
                        try{
                            patternUnitsLabel=bundle.getString(fpatternUnitsAtt.getModel().getAbsoluteName());
                        }catch (Exception ex){}
                    }
                }
                
                //the label of the combo
                JLabel comboLabel=new JLabel(patternUnitsLabel.concat(" :"));

                //the panel containing the label and the combo box
                final JPanel comboPanel=new JPanel();
                comboPanel.setLayout(new BorderLayout(5, 5));
                
                //adding the elements
                comboPanel.add(comboLabel, BorderLayout.WEST);
                comboPanel.add(combo, BorderLayout.CENTER);
                comboPanel.add(new JSeparator(), BorderLayout.SOUTH);
                
                //the entries which will be used to manually set the coordinates of the vector
                final JTextField 	xTxt=new JTextField(format.format(bounds.x)), 
                						yTxt=new JTextField(format.format(bounds.y)),
                						wTxt=new JTextField(format.format(bounds.width)), 
                						hTxt=new JTextField(format.format(bounds.height));
                
                //getting the labels
                String 	strX=xAtt.getModel().getName(), 
                			strY=yAtt.getModel().getName(), 
                			strW=wAtt.getModel().getName(), 
                			strH=hAtt.getModel().getName();
                
                if(bundle!=null){
                    
                    try{
                        strX=bundle.getString("vresource_".concat(strX));
                        strY=bundle.getString("vresource_".concat(strY));
                        strW=bundle.getString("vresource_".concat(strW));
                        strH=bundle.getString("vresource_".concat(strH));
                    }catch (Exception ex){}
                }

                //the labels corresponding to the entries
                final JLabel 	xLbl=new JLabel(strX.concat("=")), 
                					yLbl=new JLabel(strY.concat("=")),
                					wLbl=new JLabel(strW.concat("=")), 
                					hLbl=new JLabel(strH.concat("="));
                
                //sets the size for the labels
                Dimension lblSize=new Dimension(70, 20);
                xLbl.setPreferredSize(lblSize);
                xLbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                yLbl.setPreferredSize(lblSize);
                yLbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                wLbl.setPreferredSize(lblSize);
                wLbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                hLbl.setPreferredSize(lblSize);
                hLbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                
                //the label for the units
                String unitSign=patternChValues.getUnitSign();

                final JLabel 	xUnitLbl=new JLabel(unitSign),
                					yUnitLbl=new JLabel(unitSign),
                					wUnitLbl=new JLabel(unitSign),
                					hUnitLbl=new JLabel(unitSign);
                
                //the listener to the textfields
                final CaretListener textfieldsListener=new CaretListener(){

                    public void caretUpdate(CaretEvent evt) {

                        Point point=new Point(0, 0);
                        double val=0;

                        //setting the attributes values
                        if(evt.getSource().equals(xTxt)){

                            if(fpatternChValues.isDefaultPatternUnits()){
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(xTxt.getText().concat("%"), fpatternChValues.isDefaultPatternUnits());
                                fpatternChValues.getDefBounds().x=val;
                                fxAtt.setValue(format.format(val)+"%");                         
                                
                            }else{
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(xTxt.getText(), fpatternChValues.isDefaultPatternUnits());
                                fxAtt.setValue(format.format(val));
                                fpatternChValues.getOtherBounds().x=val;                                  
                            }
                            
                        }else if(evt.getSource().equals(yTxt)){

                            if(fpatternChValues.isDefaultPatternUnits()){
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(yTxt.getText().concat("%"), fpatternChValues.isDefaultPatternUnits());
                                fpatternChValues.getDefBounds().y=val;
                                fyAtt.setValue(format.format(val)+"%");                         
                                
                            }else{
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(yTxt.getText(), fpatternChValues.isDefaultPatternUnits());
                                fyAtt.setValue(format.format(val));
                                fpatternChValues.getOtherBounds().y=val;                                  
                            }
                            
                        }else if(evt.getSource().equals(wTxt)){

                            if(fpatternChValues.isDefaultPatternUnits()){
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(wTxt.getText().concat("%"), fpatternChValues.isDefaultPatternUnits());
                                fpatternChValues.getDefBounds().width=val;
                                fwAtt.setValue(format.format(val)+"%");                         
                                
                            }else{
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(wTxt.getText(), fpatternChValues.isDefaultPatternUnits());
                                fwAtt.setValue(format.format(val));
                                fpatternChValues.getOtherBounds().width=val;                                  
                            }
                            
                        }else if(evt.getSource().equals(hTxt)){

                            if(fpatternChValues.isDefaultPatternUnits()){
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(hTxt.getText().concat("%"), fpatternChValues.isDefaultPatternUnits());
                                fpatternChValues.getDefBounds().height=val;
                                fhAtt.setValue(format.format(val)+"%");
                                
                            }else{
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(hTxt.getText(), fpatternChValues.isDefaultPatternUnits());
                                fhAtt.setValue(format.format(val));
                                fpatternChValues.getOtherBounds().height=val;                                  
                            }
                        }
                    }
                };
                
                //adds the listener to the textfields
                xTxt.addCaretListener(textfieldsListener);
                yTxt.addCaretListener(textfieldsListener);
                wTxt.addCaretListener(textfieldsListener);
                hTxt.addCaretListener(textfieldsListener);
    			
                //the panel containing the textfields
                final JPanel textfields=new JPanel();

                //sets the layout
                textfields.setLayout(new BoxLayout(textfields, BoxLayout.Y_AXIS));
                textfields.setBorder(new EmptyBorder(0, 0, 0, 5));

    			//adding the elements for x
                JPanel xPanel=new JPanel();
                xPanel.setLayout(new BorderLayout(2, 2));
                xPanel.add(xLbl, BorderLayout.WEST);
                xPanel.add(xTxt, BorderLayout.CENTER);
                xPanel.add(xUnitLbl, BorderLayout.EAST);
                textfields.add(xPanel);

    			//adding the elements for y
                JPanel yPanel=new JPanel();
                yPanel.setLayout(new BorderLayout(2, 2));
                yPanel.add(yLbl, BorderLayout.WEST);
                yPanel.add(yTxt, BorderLayout.CENTER);
                yPanel.add(yUnitLbl, BorderLayout.EAST);
                textfields.add(yPanel);
    			
    			//adding the elements for w
                JPanel wPanel=new JPanel();
                wPanel.setLayout(new BorderLayout(2, 2));
                wPanel.add(wLbl, BorderLayout.WEST);
                wPanel.add(wTxt, BorderLayout.CENTER);
                wPanel.add(wUnitLbl, BorderLayout.EAST);
                textfields.add(wPanel);
    			
    			//adding the elements for h
                JPanel hPanel=new JPanel();
                hPanel.setLayout(new BorderLayout(2, 2));
                hPanel.add(hLbl, BorderLayout.WEST);
                hPanel.add(hTxt, BorderLayout.CENTER);
                hPanel.add(hUnitLbl, BorderLayout.EAST);
                textfields.add(hPanel);

    			//the panel containing the vector panel and the textfields panel
    			final JPanel widgetPanel=new JPanel();
    			widgetPanel.setLayout(new BorderLayout(0, 5));
    			
    			//adds the combo box panel
    			widgetPanel.add(comboPanel, BorderLayout.NORTH);
    			
    			//adds the vector chooser panel
    			widgetPanel.add(textfields, BorderLayout.CENTER);

                //the listener to the combo box
                final ActionListener comboListener=new ActionListener(){
                    
                    public void actionPerformed(ActionEvent evt) {
                        
                        String val="";
                        
                        if(combo.getSelectedItem()!=null){
                            
                            val=((SVGComboItem)combo.getSelectedItem()).getValue();
                        }

                        //modifies the widgetValue of the property item
                        if(val!=null && ! val.equals("")){
                            
                            //saving the values for the current state
                            Rectangle2D.Double theBounds=new Rectangle2D.Double(0, 0, 0, 0);
                            
                            String 	xStr=xTxt.getText(), 
                            			yStr=yTxt.getText(),
                            			wStr=wTxt.getText(),
                            			hStr=hTxt.getText();
                            
                            if(fpatternChValues.isDefaultPatternUnits()){
                                
                                xStr=xStr.concat("%");
                                yStr=yStr.concat("%");
                                wStr=wStr.concat("%");
                                hStr=hStr.concat("%");
                            }

                            theBounds.x=svgEditor.getSVGToolkit().getDoubleValue(xStr, fpatternChValues.isDefaultPatternUnits());
                            theBounds.y=svgEditor.getSVGToolkit().getDoubleValue(yStr, fpatternChValues.isDefaultPatternUnits());
                            theBounds.width=svgEditor.getSVGToolkit().getDoubleValue(wStr, fpatternChValues.isDefaultPatternUnits());
                            theBounds.height=svgEditor.getSVGToolkit().getDoubleValue(hStr, fpatternChValues.isDefaultPatternUnits());

                            if(fpatternChValues.isDefaultPatternUnits()){
                                
                                fpatternChValues.setDefBounds(theBounds);

                            }else{
                                
                                fpatternChValues.setOtherBounds(theBounds);
                            }
                            
                            //setting the new attribute value
                            fpatternUnitsAtt.setValue(val);

                            fpatternChValues.setDefaultPatternUnits(! fpatternUnitsAtt.getValue().equals(patternUnitsOther));
                            
                            //sets the label for the units
                            String unitSign=fpatternChValues.getUnitSign();

                            xUnitLbl.setText(unitSign);
                            yUnitLbl.setText(unitSign);
                            wUnitLbl.setText(unitSign);
                            hUnitLbl.setText(unitSign);
                            
                            if(fpatternChValues.isDefaultPatternUnits()){

                                xStr=format.format(fpatternChValues.getDefBounds().x);
                                yStr=format.format(fpatternChValues.getDefBounds().y);
                                wStr=format.format(fpatternChValues.getDefBounds().width);
                                hStr=format.format(fpatternChValues.getDefBounds().height);

                                xTxt.setText(xStr);
                                yTxt.setText(yStr);
                                wTxt.setText(wStr);
                                hTxt.setText(hStr);

                                xStr=xStr.concat("%");
                                yStr=yStr.concat("%");
                                wStr=wStr.concat("%");
                                hStr=hStr.concat("%");

                            }else{
                                
                                xStr=format.format(fpatternChValues.getOtherBounds().x);
                                yStr=format.format(fpatternChValues.getOtherBounds().y);
                                wStr=format.format(fpatternChValues.getOtherBounds().width);
                                hStr=format.format(fpatternChValues.getOtherBounds().height);

                                xTxt.setText(xStr);
                                yTxt.setText(yStr);
                                wTxt.setText(wStr);
                                hTxt.setText(hStr);
                            }
                            
                            //setting the value of the attributes
                            fxAtt.setValue(xStr);
                            fyAtt.setValue(yStr);
                            fwAtt.setValue(wStr);
                            fhAtt.setValue(hStr);
                        }
                    }
                };
                
                //adds a listener to the combo box
                combo.addActionListener(comboListener);
    			
                component=widgetPanel;
                
                Runnable disposer=new Runnable(){

	                public void run() {

                        //removes the listener to the textfields
                        xTxt.removeCaretListener(textfieldsListener);
                        yTxt.removeCaretListener(textfieldsListener);
                        wTxt.removeCaretListener(textfieldsListener);
                        hTxt.removeCaretListener(textfieldsListener);
                        
                        //removes the listener from the combo box
                        combo.removeActionListener(comboListener);
                        
                        itmList.clear();
	                } 
                };
            }
        }
    }
    
    /**
     * the class used to store information for the pattern size chooser
     * 
     * @author ITRIS, Jordi SUC
     */
    protected class SVGPatternSizeChooserValues{

       /**
        * the default bounds
        */
        private Rectangle2D.Double defBounds=new Rectangle2D.Double(0, 0, 0, 0);
        
        /**
         * the other bounds
         */
        private Rectangle2D.Double otherBounds=new Rectangle2D.Double(0, 0, 0, 0);
        
        /**
         * the boolean telling if the default units are used or not
         */
        private boolean isDefaultPatternUnits=true;
        
        /**
         * the constructor of the class
         * @param defBounds the default bounds of the pattern
         * @param otherBounds the other bounds of the pattern
         * @param isDefaultPatternUnits the boolean telling if the default units are used or not
         */
        protected SVGPatternSizeChooserValues(Rectangle2D.Double defBounds, Rectangle2D.Double otherBounds,boolean isDefaultPatternUnits){
            
            if(defBounds!=null){
                
                this.defBounds=defBounds;
            }
            
            if(otherBounds!=null){
                
                this.otherBounds=otherBounds;
            }
            
            this.isDefaultPatternUnits=isDefaultPatternUnits;
        }
        
        /**
         * @return a unit sign
         */
        protected String getUnitSign(){
            
            String unitSign="";
            
            if(isDefaultPatternUnits()){
                
                unitSign="%";
                
            }else{
               
                unitSign="px";
            }
            
            return unitSign;
        }

	    protected Rectangle2D.Double getDefBounds() {
	        return defBounds;
	    }
	    
	    protected void setDefBounds(Rectangle2D.Double defBounds) {
	        this.defBounds=defBounds;
	    }
	    
        protected Rectangle2D.Double getOtherBounds() {
            return otherBounds;
        }
        
        protected void setOtherBounds(Rectangle2D.Double otherBounds) {
            this.otherBounds=otherBounds;
        }
        
        protected boolean isDefaultPatternUnits() {
            return isDefaultPatternUnits;
        }
        
        protected void setDefaultPatternUnits(boolean isDefaultPatternUnits) {
            this.isDefaultPatternUnits=isDefaultPatternUnits;
        }
    }
}
