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
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.border.*;
import javax.swing.event.*;
import fr.itris.glips.svgeditor.*;

/**
 * the class of the widgets that will be displayed in the properties dialog, and enabling to modify the properties
 * of an attribute (or a group of attributes) of a resource node.
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceVectorCircleChooserAttributeWidget extends SVGVisualResourceAttributeWidget{

	/**
	 * the constructor of the class
	 * @param resourceObjectAttribute the attribute object that will be modified
	 */
    public SVGVisualResourceVectorCircleChooserAttributeWidget(SVGVisualResourceObjectAttribute resourceObjectAttribute) {

        super(resourceObjectAttribute);
        
        buildComponent();
    }
    
    /**
     * builds the component to be displayed
     */
    protected void buildComponent(){
        
        if(resourceObjectAttribute!=null){

            final Editor svgEditor=resourceObjectAttribute.getModel().getVisualResources().getSVGEditor();
            final String type=resourceObjectAttribute.getModel().getType();
            
            //getting the attributes//
            SVGVisualResourceObjectAttribute 	px1Att=null, py1Att=null, px2Att=null, py2Att=null, radiusAtt=null, 
            														gradientUnitsAtt=null, att=null;
            
            final String gradientUnitsOther="userSpaceOnUse";
            String nm="";
            
            for(Iterator it=resourceObjectAttribute.getGroupAttributes().iterator();it.hasNext();){
                
                try{
                    att=(SVGVisualResourceObjectAttribute)it.next();
                }catch (Exception ex){att=null;}
                
                if(att!=null){
                    
                    nm=att.getModel().getName();
                    
                    if(nm.equals("x1")){
                        
                        px1Att=att;
                        
                    }else if(nm.equals("cx")){
                        
                        px1Att=att;
                        
                    }else if(nm.equals("y1")){
                        
                        py1Att=att;
                        
                    }else if(nm.equals("cy")){
                        
                        py1Att=att;
                        
                    }else if(nm.equals("x2")){
                        
                        px2Att=att;
                        
                    }else if(nm.equals("fx")){
                        
                        px2Att=att;
                        
                    }else if(nm.equals("y2")){
                        
                        py2Att=att;
                        
                    }else if(nm.equals("fy")){
                        
                        py2Att=att;
                        
                    }else if(nm.equals("r")){
                        
                        radiusAtt=att;
                        
                    }else if(nm.equals("gradientUnits")){
                        
                        gradientUnitsAtt=att;
                    }
                }
            }
            
            final SVGVisualResourceObjectAttribute 	fpx1Att=px1Att, fpy1Att=py1Att, fpx2Att=px2Att, fpy2Att=py2Att, 
																			fradiusAtt=radiusAtt, fgradientUnitsAtt=gradientUnitsAtt;
            
            //getting the points value//
            Point2D.Double point1=new Point2D.Double(0, 0), point2=new Point2D.Double(0, 0);
            double radius=0;

            Point2D.Double defPoint1=new Point2D.Double(0, 0), defPoint2=new Point2D.Double(0, 0);
            double defRadius=0;
            
            Point2D.Double otherPoint1=new Point2D.Double(0, 0), otherPoint2=new Point2D.Double(0, 0);
            double otherRadius=0;

            //the object containing the values for this chooser
            SVGVectorCircleChooserValues vectChValues=null;
            
            if(gradientUnitsAtt!=null && px1Att!=null && py1Att!=null && px2Att!=null && py2Att!=null){
                
                boolean isGradientUnitsDefault=(! gradientUnitsAtt.getValue().equals(gradientUnitsOther));
                
                point1.x=svgEditor.getSVGToolkit().getDoubleValue(px1Att.getValue(), isGradientUnitsDefault);
                point1.y=svgEditor.getSVGToolkit().getDoubleValue(py1Att.getValue(), isGradientUnitsDefault);
                point2.x=svgEditor.getSVGToolkit().getDoubleValue(px2Att.getValue(), isGradientUnitsDefault);
                point2.y=svgEditor.getSVGToolkit().getDoubleValue(py2Att.getValue(), isGradientUnitsDefault);
                
                if(radiusAtt!=null){
                    
                    radius=svgEditor.getSVGToolkit().getDoubleValue(radiusAtt.getValue(), isGradientUnitsDefault);
                }

                if(isGradientUnitsDefault){
                    
                    defPoint1=point1;
                    defPoint2=point2;
                    defRadius=radius;

                }else{
                    
                    otherPoint1=point1;
                    otherPoint2=point2;
                    otherRadius=radius;
                    
                    defPoint1.x=svgEditor.getSVGToolkit().getDoubleValue(px1Att.getModel().getDefaultValue(), true);
                    defPoint1.y=svgEditor.getSVGToolkit().getDoubleValue(py1Att.getModel().getDefaultValue(), true);
                    defPoint2.x=svgEditor.getSVGToolkit().getDoubleValue(px2Att.getModel().getDefaultValue(), true);
                    defPoint2.y=svgEditor.getSVGToolkit().getDoubleValue(py2Att.getModel().getDefaultValue(), true);
                    
                    if(radiusAtt!=null){
                        
                        defRadius=svgEditor.getSVGToolkit().getDoubleValue(radiusAtt.getModel().getDefaultValue(), true);
                    }
                }
                
                vectChValues=new SVGVectorCircleChooserValues(defPoint1, defPoint2, defRadius, otherPoint1, otherPoint2, otherRadius, 
                																				isGradientUnitsDefault, type.equals("vectorchooser"));
            }
            
            //the object containing the values for this chooser
            final SVGVectorCircleChooserValues fvectChValues=vectChValues;
            
            if(vectChValues!=null && px1Att!=null && py1Att!=null && px2Att!=null && py2Att!=null){
                
                //creating the combo used to choose the units//
                
                //the list of the combo items
                final LinkedList itmList=new LinkedList();

                //the map of the possible values
                final LinkedHashMap possibleValues=gradientUnitsAtt.getModel().getPossibleValues();

                SVGComboItem itm=null, selectedItm=null;
                nm=""; 
                String val="", lbl="";
                
                //an item with an empty string
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
                        if(val!=null && val.equals(gradientUnitsAtt.getValue()))selectedItm=itm;
                        
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
                if(selectedItm!=null){
                    
                    combo.setSelectedItem(selectedItm);
                }

                String gradientUnitsLabel="";
                
                if(fgradientUnitsAtt!=null){
                    
                    gradientUnitsLabel=fgradientUnitsAtt.getModel().getName();
                    
                    if(bundle!=null){
                        
                        try{
                            gradientUnitsLabel=bundle.getString(fgradientUnitsAtt.getModel().getAbsoluteName());
                        }catch (Exception ex){}
                    }
                }
                
                //the label of the combo
                JLabel comboLabel=new JLabel(gradientUnitsLabel.concat(" :"));
                
                //the panel containing the label and the combo box
                final JPanel comboPanel=new JPanel();
                comboPanel.setLayout(new BorderLayout(5, 5));
                
                //adding the elements
                comboPanel.add(comboLabel, BorderLayout.WEST);
                comboPanel.add(combo, BorderLayout.CENTER);
                comboPanel.add(new JSeparator(), BorderLayout.SOUTH);

                //the vector chooser//

                //the size of the vector panel
                final Dimension vectorPanelSize=new Dimension(100, 100);
                
                //the object managing the vector displayed on the vector panel
                final SVGVectorCircleChooserItems items=
                    		new SVGVectorCircleChooserItems(	fvectChValues.getDefaultPoint1(), fvectChValues.getDefaultPoint2(), 
                        															fvectChValues.getDefaultRadius(), vectorPanelSize,
                        															fvectChValues.isVectorChooser(), vectChValues);

                //the panel used to choose the vector
                final JPanel vectorPanel=new JPanel(){

                    protected void paintComponent(Graphics g) {

                        super.paintComponent(g);
                        
                        //paints the vector
                        items.paintVectorOrCircle((Graphics2D)g);
                    }
                };
                
                //sets the properties of the panel
                vectorPanel.setPreferredSize(vectorPanelSize);
                vectorPanel.setBackground(Color.white);
                vectorPanel.setBorder(new LineBorder(Color.black, 1));
                
                //the entries which will be used to manually set the coordinates of the vector
                final JTextField px1Txt=new JTextField(format.format(point1.x)), 
                						py1Txt=new JTextField(format.format(point1.y)),
                						px2Txt=new JTextField(format.format(point2.x)), 
                						py2Txt=new JTextField(format.format(point2.y)),
                						radiusTxt=new JTextField(format.format(radius));
                
                //the labels corresponding to the entries
                final JLabel 	px1Lbl=new JLabel(px1Att.getModel().getName().concat("=")), 
                					py1Lbl=new JLabel(py1Att.getModel().getName().concat("=")),
                					px2Lbl=new JLabel(px2Att.getModel().getName().concat("=")), 
                					py2Lbl=new JLabel(py2Att.getModel().getName().concat("=")), 
                					radiusLbl=new JLabel();
                
                if(radiusAtt!=null){
                    
                    radiusLbl.setText(radiusAtt.getModel().getName().concat("="));
                }
                
                //sets the size for the labels
                px1Lbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                py1Lbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                px2Lbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                py2Lbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                radiusLbl.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                
                if(vectChValues.isDefaultGradientUnits()){
                    
                    //sets the color for the labels
                    px1Lbl.setForeground(vectChValues.getPoint1Color());
                    py1Lbl.setForeground(vectChValues.getPoint1Color());
                    px2Lbl.setForeground(vectChValues.getPoint2Color());
                    py2Lbl.setForeground(vectChValues.getPoint2Color());
                    radiusLbl.setForeground(vectChValues.getRadiusColor());
                }


                //the label for the units
                String unitSign=vectChValues.getUnitSign();

                final JLabel 	px1UnitLbl=new JLabel(unitSign),
                					py1UnitLbl=new JLabel(unitSign),
                					px2UnitLbl=new JLabel(unitSign),
                					py2UnitLbl=new JLabel(unitSign),
                					radiusUnitLbl=new JLabel(unitSign);
			    
                //the listener to the mouse events on the vector panel
                final SVGVectorCirclePanelListener vectorPanelListener=new SVGVectorCirclePanelListener(){
                    
                    /**
                     * the integer describing the point on which a click has been done
                     */
                    private int currentPoint=SVGVectorCircleChooserItems.NO_POINT;
                    
                    /**
                     * refreshes the display of the vector in the panel
                     */
                    public void refresh(){
                        
                        vectorPanel.repaint();
                    }
                    
                    public void mousePressed(MouseEvent e){
                        
                        currentPoint=items.hasHitAPoint(e.getPoint());
                    }
                    
                    public void mouseDragged(MouseEvent e){
                        
                        if(vectorPanelSize!=null){
                            
                            //if a point of the vector has been hit, sets the new value while dragging
                            if(		currentPoint==SVGVectorCircleChooserItems.POINT1 || 
                                    currentPoint==SVGVectorCircleChooserItems.POINT2 ||
                                    currentPoint==SVGVectorCircleChooserItems.CIRCLE){
                                
                                Point2D.Double point=new Point2D.Double(0, 0);
                                point.x=100*e.getPoint().x/vectorPanelSize.width;
                                point.y=100*e.getPoint().y/vectorPanelSize.height;

                                //sets the new values in the textfields
                                if(currentPoint==SVGVectorCircleChooserItems.POINT1){
                                    
                                    items.setPoint(point, currentPoint);
                                    
                                    Point2D.Double point1=items.getPoint1();
                                    
                                    px1Txt.setText(format.format(point1.x));
                                    py1Txt.setText(format.format(point1.y));
                                    
                                    fvectChValues.setDefaultPoint1(point1);

                                }else if(currentPoint==SVGVectorCircleChooserItems.POINT2){
                                    
                                    items.setPoint(point, currentPoint);
                                    
                                    Point2D.Double point2=items.getPoint2();
                                    
                                    px2Txt.setText(format.format(point2.x));
                                    py2Txt.setText(format.format(point2.y));
                                    
                                    fvectChValues.setDefaultPoint2(point2);
                                    
                                }else if(currentPoint==SVGVectorCircleChooserItems.CIRCLE){
                                    
                                    Point2D.Double point1=items.getPoint1();
                                    
                                    if(point1!=null){
                                        
                                        double cy=100*point.y/vectorPanelSize.height;
                                        double rad=Math.abs(point1.y-cy);
                                            
                                        items.setRadius(rad);
                                        
                                        radiusTxt.setText(format.format(items.getRadius()));
                                        fvectChValues.setDefaultRadius(items.getRadius());
                                    }
                                }

                                refresh();
                            }
                        }
                    }

                    public void mouseReleased(MouseEvent e) {
                        
                        //applies the changes
                        if(currentPoint==SVGVectorCircleChooserItems.POINT1 || 
                               currentPoint==SVGVectorCircleChooserItems.POINT2 ||
                                currentPoint==SVGVectorCircleChooserItems.CIRCLE){
                        
                            applyChanges();
                        }
                    }

                    public void mouseClicked(MouseEvent e) {
                    }

                    public void mouseEntered(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                    }

                    public void mouseMoved(MouseEvent e) {
                    }
                    
                    /**
                     * applies the changes made on the values of the attributes to the attributes
                     */
                    protected void applyChanges(){

                        if(items!=null && fvectChValues.isDefaultGradientUnits){
                            
                            Point2D.Double point1=items.getPoint1(), point2=items.getPoint2();
                            
                            fpx1Att.setValue(format.format(point1.x)+"%");
                            fpy1Att.setValue(format.format(point1.y)+"%");
                            fpx2Att.setValue(format.format(point2.x)+"%");
                            fpy2Att.setValue(format.format(point2.y)+"%");
                            
                            if(fradiusAtt!=null){
                                
                                fradiusAtt.setValue(format.format(items.getRadius())+"%");
                            }
                        }
                    }
                };
                
                //the listener to the textfields
                final CaretListener textfieldsListener=new CaretListener(){

					public void caretUpdate(CaretEvent evt) {
					
                        Point point=new Point(0, 0);
                        double val=0;

                        //setting the attributes values
                        if(evt.getSource().equals(px1Txt)){

                            if(fvectChValues.isDefaultGradientUnits()){
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(px1Txt.getText().concat("%"), fvectChValues.isDefaultGradientUnits());
                                fvectChValues.getDefaultPoint1().x=val;
                                items.setPoint(fvectChValues.getDefaultPoint1(), SVGVectorCircleChooserItems.POINT1);
                                fpx1Att.setValue(format.format(val)+"%");       
                                
                                vectorPanelListener.refresh();
                                
                            }else{
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(px1Txt.getText(), fvectChValues.isDefaultGradientUnits());
                                fpx1Att.setValue(format.format(val));
                                fvectChValues.getOtherPoint1().x=val;                                  
                            }
                            
                        }else if(evt.getSource().equals(py1Txt)){

                            if(fvectChValues.isDefaultGradientUnits()){
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(py1Txt.getText().concat("%"), fvectChValues.isDefaultGradientUnits());
                                fvectChValues.getDefaultPoint1().y=val;
                                items.setPoint(fvectChValues.getDefaultPoint1(), SVGVectorCircleChooserItems.POINT1);
                                fpy1Att.setValue(format.format(val)+"%");                         
                                vectorPanelListener.refresh();
                                
                            }else{
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(py1Txt.getText(), fvectChValues.isDefaultGradientUnits());
                                fpy1Att.setValue(format.format(val));
                                fvectChValues.getOtherPoint1().y=val;                                  
                            }
                            
                        }else if(evt.getSource().equals(px2Txt)){

                            if(fvectChValues.isDefaultGradientUnits()){
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(px2Txt.getText().concat("%"), fvectChValues.isDefaultGradientUnits());
                                fvectChValues.getDefaultPoint2().x=val;
                                items.setPoint(fvectChValues.getDefaultPoint2(), SVGVectorCircleChooserItems.POINT2);
                                fpx2Att.setValue(format.format(val)+"%");                         
                                vectorPanelListener.refresh();
                                
                            }else{
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(px2Txt.getText(), fvectChValues.isDefaultGradientUnits());
                                fpx2Att.setValue(format.format(val));
                                fvectChValues.getOtherPoint2().x=val;                                  
                            }
                            
                        }else if(evt.getSource().equals(py2Txt)){

                            if(fvectChValues.isDefaultGradientUnits()){
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(py2Txt.getText().concat("%"), fvectChValues.isDefaultGradientUnits());
                                fvectChValues.getDefaultPoint2().y=val;
                                items.setPoint(fvectChValues.getDefaultPoint2(), SVGVectorCircleChooserItems.POINT2);
                                fpy2Att.setValue(format.format(val)+"%");
                                
                                vectorPanelListener.refresh();
                                
                            }else{
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(py2Txt.getText(), fvectChValues.isDefaultGradientUnits());
                                fpy2Att.setValue(format.format(val));
                                fvectChValues.getOtherPoint2().y=val;                                  
                            }
                            
                        }else if(evt.getSource().equals(radiusTxt) && fradiusAtt!=null){

                            if(fvectChValues.isDefaultGradientUnits()){
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(radiusTxt.getText().concat("%"), fvectChValues.isDefaultGradientUnits());
                                fvectChValues.setDefaultRadius(val);
                                items.setRadius(fvectChValues.getDefaultRadius());
                                fradiusAtt.setValue(format.format(val)+"%"); 
                                vectorPanelListener.refresh();
                                
                            }else{
                                
                                val=svgEditor.getSVGToolkit().getDoubleValue(radiusTxt.getText(), fvectChValues.isDefaultGradientUnits());
                                fradiusAtt.setValue(format.format(val));
                                fvectChValues.setOtherRadius(val);                                  
                            }
                        }
					}
                };

                //adds the listener to the textfields
                px1Txt.addCaretListener(textfieldsListener);
                py1Txt.addCaretListener(textfieldsListener);
                px2Txt.addCaretListener(textfieldsListener);
                py2Txt.addCaretListener(textfieldsListener);
                radiusTxt.addCaretListener(textfieldsListener);

    			final JPanel vectChooserPanel=new JPanel();
    			vectChooserPanel.setLayout(new BoxLayout(vectChooserPanel, BoxLayout.X_AXIS));
    			vectChooserPanel.setBorder(new EmptyBorder(0, 2, 5, 0));
    			
                //the panel containing the textfields
                final JPanel textfields=new JPanel();

                //sets the layout
                textfields.setLayout(new BoxLayout(textfields, BoxLayout.Y_AXIS));
                textfields.setBorder(new EmptyBorder(0, 0, 0, 5));

    			//adding the elements for px1
                JPanel px1Panel=new JPanel();
                px1Panel.setLayout(new BorderLayout(2, 2));
                px1Panel.add(px1Lbl, BorderLayout.WEST);
                px1Panel.add(px1Txt, BorderLayout.CENTER);
                px1Panel.add(px1UnitLbl, BorderLayout.EAST);
                textfields.add(px1Panel);
    			
    			//adding the elements for py1
                JPanel py1Panel=new JPanel();
                py1Panel.setLayout(new BorderLayout(2, 2));
                py1Panel.add(py1Lbl, BorderLayout.WEST);
                py1Panel.add(py1Txt, BorderLayout.CENTER);
                py1Panel.add(py1UnitLbl, BorderLayout.EAST);
                textfields.add(py1Panel);
    			
    			//adding the elements for px2
                JPanel px2Panel=new JPanel();
                px2Panel.setLayout(new BorderLayout(2, 2));
                px2Panel.add(px2Lbl, BorderLayout.WEST);
                px2Panel.add(px2Txt, BorderLayout.CENTER);
                px2Panel.add(px2UnitLbl, BorderLayout.EAST);
                textfields.add(px2Panel);
    			
    			//adding the elements for py2
                JPanel py2Panel=new JPanel();
                py2Panel.setLayout(new BorderLayout(2, 2));
                py2Panel.add(py2Lbl, BorderLayout.WEST);
                py2Panel.add(py2Txt, BorderLayout.CENTER);
                py2Panel.add(py2UnitLbl, BorderLayout.EAST);
                textfields.add(py2Panel);
                
                if(! vectChValues.isVectorChooser()){
                    
        			//adding the elements for radius
                    JPanel radiusPanel=new JPanel();
                    radiusPanel.setLayout(new BorderLayout(2, 2));
                    radiusPanel.add(radiusLbl, BorderLayout.WEST);
                    radiusPanel.add(radiusTxt, BorderLayout.CENTER);
                    radiusPanel.add(radiusUnitLbl, BorderLayout.EAST);
                    textfields.add(radiusPanel);
                    
                }else{
                    
                    JPanel empty=new JPanel();
                    textfields.add(empty);
                }

                //adding the panel to the widget panel
                vectChooserPanel.add(textfields);
                vectChooserPanel.add(vectorPanel);

                if(vectChValues.isDefaultGradientUnits()){

                    //adds the listener to the vector panel
                    vectorPanel.addMouseListener(vectorPanelListener);
                    vectorPanel.addMouseMotionListener(vectorPanelListener);
                    
                }else{

                    //disables the vector chooser panel
                    items.setEnabled(false);
                    vectorPanel.setVisible(false);
                }
                
    			//the panel containing the vector panel and the textfields panel
    			final JPanel widgetPanel=new JPanel();
    			widgetPanel.setLayout(new BorderLayout(0, 5));
    			
    			//adds the combo box panel
    			widgetPanel.add(comboPanel, BorderLayout.NORTH);
    			
    			//adds the vector chooser panel
    			widgetPanel.add(vectChooserPanel, BorderLayout.CENTER);

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
                            Point2D.Double point1=new Point2D.Double(0, 0), point2=new Point2D.Double(0, 0);
                            double radius=0;
                            
                            String 	px1Str=px1Txt.getText(), 
                            			py1Str=py1Txt.getText(),
                            			px2Str=px2Txt.getText(),
                            			py2Str=py2Txt.getText(),
                            			radiusStr=radiusTxt.getText();
                            
                            if(fvectChValues.isDefaultGradientUnits()){
                                
                                px1Str=px1Str.concat("%");
                                py1Str=py1Str.concat("%");
                                px2Str=px2Str.concat("%");
                                py2Str=py2Str.concat("%");
                                radiusStr=py2Str.concat("%");
                            }

                            point1.x=svgEditor.getSVGToolkit().getDoubleValue(px1Str, fvectChValues.isDefaultGradientUnits());
                            point1.y=svgEditor.getSVGToolkit().getDoubleValue(py1Str, fvectChValues.isDefaultGradientUnits());
                            point2.x=svgEditor.getSVGToolkit().getDoubleValue(px2Str, fvectChValues.isDefaultGradientUnits());
                            point2.y=svgEditor.getSVGToolkit().getDoubleValue(py2Str, fvectChValues.isDefaultGradientUnits());
                            radius=svgEditor.getSVGToolkit().getDoubleValue(radiusStr, fvectChValues.isDefaultGradientUnits());

                            if(fvectChValues.isDefaultGradientUnits()){
                                
                                fvectChValues.setDefaultPoint1(point1);
                                fvectChValues.setDefaultPoint2(point2);
                                fvectChValues.setDefaultRadius(radius);

                            }else{
                                
                                fvectChValues.setOtherPoint1(point1);
                                fvectChValues.setOtherPoint2(point2);
                                fvectChValues.setOtherRadius(radius);
                            }
                            
                            //setting the new attribute value
                            fgradientUnitsAtt.setValue(val);

                            fvectChValues.setDefaultGradientUnits(! fgradientUnitsAtt.getValue().equals(gradientUnitsOther));
                            
                            //sets the label for the units
                            String unitSign=fvectChValues.getUnitSign();

                            px1UnitLbl.setText(unitSign);
                            py1UnitLbl.setText(unitSign);
                            px2UnitLbl.setText(unitSign);
                            py2UnitLbl.setText(unitSign);
                            radiusUnitLbl.setText(unitSign);
                            
                            if(fvectChValues.isDefaultGradientUnits()){

                                px1Str=format.format(fvectChValues.getDefaultPoint1().x);
                                py1Str=format.format(fvectChValues.getDefaultPoint1().y);
                                px2Str=format.format(fvectChValues.getDefaultPoint2().x);
                                py2Str=format.format(fvectChValues.getDefaultPoint2().y);
                                radiusStr=format.format(fvectChValues.getDefaultRadius());
                                
                                px1Txt.setText(px1Str);
                                py1Txt.setText(py1Str);
                                px2Txt.setText(px2Str);
                                py2Txt.setText(py2Str);
                                radiusTxt.setText(radiusStr);
                                
                                px1Str=px1Str.concat("%");
                                py1Str=py1Str.concat("%");
                                px2Str=px2Str.concat("%");
                                py2Str=py2Str.concat("%");
                                radiusStr=radiusStr.concat("%");
                                
                                //sets the color for the labels
                                px1Lbl.setForeground(fvectChValues.getPoint1Color());
                                py1Lbl.setForeground(fvectChValues.getPoint1Color());
                                px2Lbl.setForeground(fvectChValues.getPoint2Color());
                                py2Lbl.setForeground(fvectChValues.getPoint2Color());
                                radiusLbl.setForeground(fvectChValues.getRadiusColor());

                                //enables the vector chooser panel
                                items.setEnabled(true);
                                vectorPanel.setVisible(true);
                                
                                //adds the listener to the vector panel
                                vectorPanel.addMouseListener(vectorPanelListener);
                                vectorPanel.addMouseMotionListener(vectorPanelListener);

                            }else{
                                
                                px1Str=format.format(fvectChValues.getOtherPoint1().x);
                                py1Str=format.format(fvectChValues.getOtherPoint1().y);
                                px2Str=format.format(fvectChValues.getOtherPoint2().x);
                                py2Str=format.format(fvectChValues.getOtherPoint2().y);
                                radiusStr=format.format(fvectChValues.getOtherRadius());

                                px1Txt.setText(px1Str);
                                py1Txt.setText(py1Str);
                                px2Txt.setText(px2Str);
                                py2Txt.setText(py2Str);
                                radiusTxt.setText(radiusStr);
                                
                                //sets the color for the labels
                                px1Lbl.setForeground(Color.black);
                                py1Lbl.setForeground(Color.black);
                                px2Lbl.setForeground(Color.black);
                                py2Lbl.setForeground(Color.black);
                                radiusLbl.setForeground(Color.black);
                                
                                //disables the vector chooser panel
                                items.setEnabled(false);
                                vectorPanel.setVisible(false);
                                
                                //removes the listener to the vector panel
                                vectorPanel.removeMouseListener(vectorPanelListener);
                                vectorPanel.removeMouseMotionListener(vectorPanelListener);
                            }
                            
                            //setting the value of the attributes
                            fpx1Att.setValue(px1Str);
                            fpy1Att.setValue(py1Str);
                            fpx2Att.setValue(px2Str);
                            fpy2Att.setValue(py2Str);
                            
                            if(fradiusAtt!=null){
                                
                                fradiusAtt.setValue(radiusStr);
                            }
                        }
                    }
                };
                
                //adds a listener to the combo box
                combo.addActionListener(comboListener);
                
                component=widgetPanel;
    			
                disposer=new Runnable(){

	                public void run() {

                        //removes the mouse listener
                        vectorPanel.removeMouseListener(vectorPanelListener);
                        vectorPanel.removeMouseMotionListener(vectorPanelListener);
                        
                        //adds the listener to the textfields
                        px1Txt.removeCaretListener(textfieldsListener);
                        py1Txt.removeCaretListener(textfieldsListener);
                        px2Txt.removeCaretListener(textfieldsListener);
                        py2Txt.removeCaretListener(textfieldsListener);
                        radiusTxt.removeCaretListener(textfieldsListener);
                        
                        //removes the listener from the combo box
                        combo.removeActionListener(comboListener);
                        
                        itmList.clear();
                        resourceObjectAttribute=null;
	                } 
                };
            }
        }
    }
    
    /**
     * the interface used to listens to the mouse events on a vector panel
     * 
     * @author ITRIS, Jordi SUC
     *
     */
    protected interface SVGVectorCirclePanelListener extends MouseListener, MouseMotionListener{
        
        /**
         * refreshes the display of the vector in the panel
         */
        public void refresh();
    }
    
    /**
     * the class used to store information for the vector or circle chooser
     * 
     * @author ITRIS, Jordi SUC
     */
    protected class SVGVectorCircleChooserValues{
        
        /**
         * the colors 
         */
        private final Color point1Color=Color.blue;
        private final Color point2Color=new Color(0, 175, 0);
        private final Color radiusColor=Color.red;
        
        /**
         * the default points
         */
        private Point2D.Double defaultPoint1=null, defaultPoint2=null;
        
        /**
         * the other points
         */
        private Point2D.Double otherPoint1=null, otherPoint2=null;
        
        /**
         * the radius of the circle
         */
        private double defaultRadius=0, otherRadius=0;
        
        /**
         * the boolean telling if the default units are used or not
         */
        private boolean isDefaultGradientUnits=true;
        
        /**
         * the boolean telling if the vector chooser is used
         */
        private boolean isVectorChooser=true;
        
        /**
         * the constructor of the class
         * @param defaultPoint1 the default point1
         * @param defaultPoint2 the default point2
         * @param defaultRadius the default radius
         * @param otherPoint1 the other point1
         * @param otherPoint2 the other point2
         * @param otherRadius the other radius
         * @param isDefaultGradientUnits the boolean telling if the default units are used or not
         * @param isVectorChooser the boolean telling if the vector chooser is used
         */
        protected SVGVectorCircleChooserValues(	Point2D.Double defaultPoint1, Point2D.Double defaultPoint2, double defaultRadius, 
                															Point2D.Double otherPoint1, Point2D.Double otherPoint2, double otherRadius, 
                															boolean isDefaultGradientUnits, boolean isVectorChooser){
            
            this.defaultPoint1=defaultPoint1;
            this.defaultPoint2=defaultPoint2;
            this.defaultRadius=defaultRadius;
            this.otherPoint1=otherPoint1;
            this.otherPoint2=otherPoint2;
            this.otherRadius=otherRadius;
            this.isDefaultGradientUnits=isDefaultGradientUnits;
            this.isVectorChooser=isVectorChooser;
        }
        
        /**
         * @return a unit sign
         */
        protected String getUnitSign(){
            
            String unitSign="";
            
            if(isDefaultGradientUnits()){
                
                unitSign="%";
                
            }else{
               
                unitSign="px";
            }
            
            return unitSign;
        }

        protected Color getPoint1Color() {
            return point1Color;
        }
        
        protected Color getPoint2Color() {
            return point2Color;
        }
        
        protected Color getRadiusColor() {
            return radiusColor;
        }
        
        protected Point2D.Double getDefaultPoint1() {
            return defaultPoint1;
        }
        
        protected void setDefaultPoint1(Point2D.Double defaultPoint1) {
            this.defaultPoint1 = defaultPoint1;
        }
        
        protected Point2D.Double getDefaultPoint2() {
            return defaultPoint2;
        }

        protected void setDefaultPoint2(Point2D.Double defaultPoint2) {
            this.defaultPoint2 = defaultPoint2;
        }

        protected double getDefaultRadius() {
            return defaultRadius;
        }
        
        protected void setDefaultRadius(double defaultRadius) {
            this.defaultRadius = defaultRadius;
        }
        
        protected boolean isDefaultGradientUnits() {
            return isDefaultGradientUnits;
        }
        
        protected void setDefaultGradientUnits(boolean isDefaultGradientUnits) {
            this.isDefaultGradientUnits = isDefaultGradientUnits;
        }
        
        protected Point2D.Double getOtherPoint1() {
            return otherPoint1;
        }
        
        protected void setOtherPoint1(Point2D.Double otherPoint1) {
            this.otherPoint1 = otherPoint1;
        }
        
        protected Point2D.Double getOtherPoint2() {
            return otherPoint2;
        }
        
        protected void setOtherPoint2(Point2D.Double otherPoint2) {
            this.otherPoint2 = otherPoint2;
        }

        protected double getOtherRadius() {
            return otherRadius;
        }
        
        protected void setOtherRadius(double otherRadius) {
            this.otherRadius = otherRadius;
        }

        protected boolean isVectorChooser() {
            return isVectorChooser;
        }
        
        protected void setVectorChooser(boolean isVectorChooser) {
            this.isVectorChooser = isVectorChooser;
        }
    }
    
    /**
     * the class describing a point in a vector chooser widget
     * 
     * @author ITRIS, Jordi SUC
     *
     */
    public class SVGVectorCircleChooserItems{
        
        /**
         * the constant value for telling that no point has been hit
         */
        protected static final int NO_POINT=0;
        
        /**
         * the constant value for describing the first point
         */
        protected static final int POINT1=1;
        
        /**
         * the constant value for describing the second point
         */
        protected static final int POINT2=2;
        
        /**
         * the constant value for describing the circle
         */
        protected static final int CIRCLE=3;
        
        /**
         * the two points in percentage values
         */
        private Point2D.Double point1=new Point2D.Double(0, 0), point2=new Point2D.Double(0, 0);
        
        /**
         * the radius of the circle in percentage values
         */
        private double radius=0;
        
        /**
         * the size of the panel in which the vector will be displayed
         */
        private Dimension vectorPanelSize=null;
        
        /**
         * the average int
         */
        private int average=5;
        
        /**
         * the boolean teeling if the vector items are enabled or not
         */
        private boolean enabled=true;
        
        /**
         * the boolean telling the state of the items
         */
        private boolean isVectorChooser=true;
        
        /**
         * the object containing information on the vector and circle chooser
         */
        private SVGVectorCircleChooserValues vectCircleChooser=null;
        
        /**
         * the constructor of the class
         * @param point1 the first point in percentage values
         * @param point2 the second point in percentage values
         * @param radius the radius in percentage values
         * @param vectorPanelSize the size of the vector panel
         * @param isVectorChooser the boolean telling the state of the items
         * @param vectCircleChooser the object containing information on the vector and circle chooser
         */
        public SVGVectorCircleChooserItems(	Point2D.Double point1, Point2D.Double point2, double radius, 
        															Dimension vectorPanelSize, boolean isVectorChooser, 
        															SVGVectorCircleChooserValues vectCircleChooser){
            
            this.vectorPanelSize=vectorPanelSize;
            
            //the two points in percentage values
            if(point1!=null){
                
                this.point1=point1;
            }
            
            if(point2!=null){
                
                this.point2=point2;
            }
            
            this.radius=radius;
            this.isVectorChooser=isVectorChooser;
            this.vectCircleChooser=vectCircleChooser;
        }
        
        /**
         * @return the first point in the percentage format
         */
        protected Point2D.Double getPoint1(){
            
            return point1;
        }
        
        /**
         * @return the second point in the percentage format
         */
        protected Point2D.Double getPoint2(){
            
            return point2;
        }
        
        /**
         * @return the radius of the circle
         */
        protected double getRadius(){
            
            return radius;
        }
        
        /**
         * sets the radius
         * @param rad the radius
         */
        protected void setRadius(double rad){
            
            if(rad<0){
                
                rad=0;
            }
            
            if(rad>100){
                
                rad=100;
            }
            
            this.radius=rad;
        }
        
        /**
         * sets one of the points
         * @param point a point
         * @param type the type of the point (point1 or point2)
         */
        protected void setPoint(Point2D.Double point, int type){
            
            if(point!=null){

                if(point.x<0){
                    
                    point.x=0;
                }
                
                if(point.x>100){
                    
                    point.x=100;
                }
                
                if(point.y<0){
                    
                    point.y=0;
                }
                
                if(point.y>100){
                    
                    point.y=100;
                }

                //sets the point
                if(type==POINT1){
                    
                    point1.x=point.x;
                    point1.y=point.y;
                    
                }else if(type==POINT2){
                    
                    point2.x=point.x;
                    point2.y=point.y;
                }
            }
        }
        
        /**
         * @return the first point in the vector panel coordinates
         */
        protected Point2D.Double getPanelPoint1(){
            
            Point2D.Double point=new Point2D.Double(0, 0);
            
            if(point1!=null && vectorPanelSize!=null){
                
                point.x=point1.x*vectorPanelSize.width/100;
                point.y=point1.y*vectorPanelSize.height/100;
            }
            
            return point;
        }
        
        /**
         * @return the second point in the vector panel coordinates
         */
        protected Point2D.Double getPanelPoint2(){
            
            Point2D.Double point=new Point2D.Double(0, 0);
            
            if(point2!=null && vectorPanelSize!=null){
                
                point.x=point2.x*vectorPanelSize.width/100;
                point.y=point2.y*vectorPanelSize.height/100;
            }
            
            return point;          
        }
        
        /**
         * @return the radius of the circle in the vector panel coordinates
         */
        protected double getPanelRadius(){
            
            double rad=0;
            
            if(point2!=null && vectorPanelSize!=null){
                
                rad=radius*vectorPanelSize.width/100;
            }
            
            return rad;
        }
        
        /**
         * tells whether the given point corresponds to one of the vector's point
         * @param point a point
         * @return an int telling if it corresp
         */
        protected int hasHitAPoint(Point point){
            
            int res=NO_POINT;
            
            if(point!=null){
                
                Point2D.Double vPoint1=getPanelPoint1(), vPoint2=getPanelPoint2();
                double vRadius=getPanelRadius();
                Rectangle2D.Double 	rect1=new Rectangle2D.Double(vPoint1.x-average, vPoint1.y-average, 2*average, 2*average),
                								rect2=new Rectangle2D.Double(vPoint2.x-average, vPoint2.y-average, 2*average, 2*average),
                								rectRadius=new Rectangle2D.Double(vPoint1.x-average, vPoint1.y-vRadius-average, 2*average, 2*average);
              
                if(rectRadius.contains(point)){
                    
                    res=CIRCLE;
                    
                }else if(rect1.contains(point)){
                    
                    res=POINT1;
                    
                }else if(rect2.contains(point)){
                    
                    res=POINT2;
                }
            }
            
            return res;
        }
        
        /**
         * enables or disables the display
         * @param enabled
         */
        protected void setEnabled(boolean enabled){
            
            this.enabled=enabled;
        }
        
        /**
         * paints the vector
         * @param g a graphics object
         */
        protected void paintVectorOrCircle(Graphics2D g){
            
            if(g!=null){
                
                if(enabled){
                    
                    Point2D.Double vPoint1=getPanelPoint1(), vPoint2=getPanelPoint2();
                    
                    //the elements that will be painted in the vector chooser version
                    if(isVectorChooser){
                        
                        g.setColor(Color.darkGray);
                        g.drawLine((int)vPoint1.x, (int)vPoint1.y, (int)vPoint2.x, (int)vPoint2.y);
                        
                        g.setColor(vectCircleChooser.getPoint1Color());
                        g.drawLine((int)(vPoint1.x-average), (int)(vPoint1.y-average), (int)(vPoint1.x+average), (int)(vPoint1.y+average));
                        g.drawLine((int)(vPoint1.x+average), (int)(vPoint1.y-average), (int)(vPoint1.x-average), (int)(vPoint1.y+average));
                        
                        g.setColor(vectCircleChooser.getPoint2Color());
                        g.drawOval((int)(vPoint2.x-average), (int)(vPoint2.y-average), 2*average, 2*average);
                        
                    }else{
                        
                        //the elements that will be painted in the circle chooser version
                        double vRadius=getPanelRadius();

                        g.setColor(vectCircleChooser.getPoint2Color());
                        g.drawLine((int)(vPoint2.x-average), (int)(vPoint2.y-average), (int)(vPoint2.x+average), (int)(vPoint2.y+average));
                        g.drawLine((int)(vPoint2.x+average), (int)(vPoint2.y-average), (int)(vPoint2.x-average), (int)(vPoint2.y+average));
                        
                        g.setColor(vectCircleChooser.getPoint1Color());
                        g.drawLine((int)(vPoint1.x-average), (int)(vPoint1.y-average), (int)(vPoint1.x+average), (int)(vPoint1.y+average));
                        g.drawLine((int)(vPoint1.x+average), (int)(vPoint1.y-average), (int)(vPoint1.x-average), (int)(vPoint1.y+average));
                        
                        g.setColor(Color.darkGray);
                        g.drawOval((int)(vPoint1.x-vRadius), (int)(vPoint1.y-vRadius), (int)(2*vRadius), (int)(2*vRadius));

                        g.setColor(vectCircleChooser.getRadiusColor());
                        g.drawLine((int)vPoint1.x, (int)vPoint1.y, (int)vPoint1.x, (int)(vPoint1.y-vRadius));
                        g.drawLine((int)(vPoint1.x-average/2), (int)(vPoint1.y-vRadius-average/2), (int)(vPoint1.x+average/2), (int)(vPoint1.y-vRadius+average/2));
                        g.drawLine((int)(vPoint1.x+average/2), (int)(vPoint1.y-vRadius-average/2), (int)(vPoint1.x-average/2), (int)(vPoint1.y-vRadius+average/2));
                    }

                }else{
                    
                    if(vectorPanelSize!=null){
                        
                        g.setColor(Color.darkGray);
                        g.fillRect(0, 0, vectorPanelSize.width, vectorPanelSize.height);
                    }
                }
            }
        }
    }
}
