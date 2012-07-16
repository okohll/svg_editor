/*
 * Created on 18 févr. 2005
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
package fr.itris.glips.svgeditor.colorchooser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;

import fr.itris.glips.library.color.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

import java.util.*;

/**
 * the panel enabling to choose a svg standard w3c color 
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGW3CColorChooserPanel extends AbstractColorChooserPanel{

    /**
     * the label of the panel
     */
    private String label="";
    
    /**
     * the labels
     */
    private String memoryLabel="";

    /**
     * the bundle used to get labels
     */
    private ResourceBundle bundle=null;

    /**
     * the constructor of the class 
     * @param editor the editor
     */
    public SVGW3CColorChooserPanel(Editor editor){
        
        //gets the labels from the resources
        bundle=ResourcesManager.bundle;
        
        if(bundle!=null){
            
            try{
                label=bundle.getString("svgColorChooserPanelLabel");
                memoryLabel=bundle.getString("colorChooserMemoryLabel");
            }catch (Exception ex){}
        }
    }

    @Override
    protected void buildChooser() {

        //the panel containing the color and memory panels
        JPanel colorsAndMemoryPanel=new JPanel();
        
        //the panel containing the panels displaying the colors
        JPanel colorsPanel=new JPanel();
        int colorsNbPerLine=25;
        
        //the elements for the last colors panel functionnality
        final int memoryNb=35, memoryRowNb=7;
        
        //creating the memory panel
        final JPanel memoryPanel=new JPanel();
        
        //creating the list of the panels that will be contained in the memory panel
        final LinkedList<JPanel> lastColorPanels=new LinkedList<JPanel>();
        
        //creating the list of the lastly selected colors
        final LinkedList<Color> lastSelectedColors=new LinkedList<Color>();
        
        //the label panel displaying the name and the corresponding rgb values of a color
        final JLabel colorLabel=new JLabel("", JLabel.CENTER);
        
        //the list of the colors
        java.util.List<SVGW3CColor> colorsList=SVGColorsManager.getW3CColors();
        
        colorsPanel.setLayout(new GridLayout((int)(Math.floor(
        		colorsList.size()/colorsNbPerLine)+1), colorsNbPerLine, 1, 1));
        JPanel panel=null;
        
        for(SVGW3CColor color : colorsList){

            if(color!=null){
                
                panel=new JPanel();
                
                //setting the properties of the panel
                panel.setBorder(new LineBorder(Color.black, 1));
                panel.setBackground(color);
                panel.setPreferredSize(new Dimension(15, 15));
                panel.setToolTipText(color.getStringRepresentation());
                
                final SVGW3CColor fcolor=color;
                
                //adding a listener to the clicks on the color panels
                panel.addMouseListener(new MouseAdapter(){

                	@Override
                    public void mouseClicked(MouseEvent evt) {

                        SVGW3CColor theColor=null, selectedColor=fcolor;
                        JPanel thePanel=null;
                        
                        //sets the new selected color
                        getColorSelectionModel().setSelectedColor(selectedColor);
                        
                        //removes the last color of the last selected colors, if the list is full
                        if(lastSelectedColors.size()>0 && lastSelectedColors.size()>=memoryNb){
                            
                            lastSelectedColors.removeLast();
                        }
                        
                        //adds the new selected color to the list
                        lastSelectedColors.addFirst(selectedColor);

                        //for each panel contained in the memory, sets its new color and tooltip
                        for(int i=0; i<lastColorPanels.size(); i++){
                            
                            thePanel=lastColorPanels.get(i);
                            
                            if(thePanel!=null){
                                
                                if(i<lastSelectedColors.size()){
                                    
                                    theColor=(SVGW3CColor)lastSelectedColors.get(i);
                                    
                                    if(theColor!=null){
                                        
                                        thePanel.setBorder(new LineBorder(Color.black, 1));
                                        thePanel.setBackground(theColor);
                                        thePanel.setToolTipText(theColor.getStringRepresentation());
                                    }
                                    
                                }else{
                                    
                                    thePanel.setBorder(new LineBorder(Color.lightGray, 1));
                                    thePanel.setBackground(getParent().getBackground());
                                    thePanel.setToolTipText(null);
                                }
                            }
                        }
                        
                        memoryPanel.repaint();
                    }

                	@Override
                    public void mouseEntered(MouseEvent arg0){

                        colorLabel.setText(fcolor.getStringRepresentation());
                    }

                	@Override
                    public void mouseExited(MouseEvent arg0){

                        colorLabel.setText("");
                    }
                });
                
                colorsPanel.add(panel);
            }
        }
        
        //filling the memory panel
        memoryPanel.setLayout(new GridLayout(memoryRowNb, (int)(Math.floor(memoryNb/memoryRowNb))+1, 1, 1));
        memoryPanel.setBorder(new TitledBorder(memoryLabel));
        
        JPanel memPanel=null;
        
        for(int i=0; i<memoryNb; i++){
            
            memPanel=new JPanel();
            memPanel.setBorder(new LineBorder(Color.lightGray, 1));
            memPanel.setPreferredSize(new Dimension(15, 15));
            memPanel.setBackground(getParent().getBackground());
            
            lastColorPanels.add(memPanel);
            memoryPanel.add(memPanel);
            
            final int fi=i;

            //adding a mouse listener to the panel
            memPanel.addMouseListener(new MouseAdapter(){
 
            	@Override
				public void mouseClicked(MouseEvent evt) {

				    SVGW3CColor theColor=null;
				    
				    if(fi<lastSelectedColors.size()){
				        
				        theColor=(SVGW3CColor)lastSelectedColors.get(fi);
				        
				        if(theColor!=null){
				            
				            getColorSelectionModel().setSelectedColor(theColor);
				        }
				    }
				}
				
            	@Override
                public void mouseEntered(MouseEvent arg0){
                    
				    SVGW3CColor theColor=null;
				    
				    if(fi<lastSelectedColors.size()){
				        
				        theColor=(SVGW3CColor)lastSelectedColors.get(fi);
				        
				        if(theColor!=null){
				            
		                    colorLabel.setText(theColor.getStringRepresentation());
				        }
				    }
                }

            	@Override
                public void mouseExited(MouseEvent arg0){

				    SVGW3CColor theColor=null;
				    
				    if(fi<lastSelectedColors.size()){
				        
				        theColor=(SVGW3CColor)lastSelectedColors.get(fi);
				        
				        if(theColor!=null){
				            
		                    colorLabel.setText("");
				        }
				    }
                }
            });
        }
        
        //adding the two panels
        colorsAndMemoryPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        colorsAndMemoryPanel.add(colorsPanel);
        colorsAndMemoryPanel.add(memoryPanel);
        
        //adding the colors and memory panel and the color label widget to the color chooser panel
        setLayout(new BorderLayout(10, 10));
        add(colorsAndMemoryPanel, BorderLayout.CENTER);
        
        //the panel containing the label
        JPanel colorLabelPanel=new JPanel();
        colorLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        colorLabelPanel.add(colorLabel);
        colorLabelPanel.setPreferredSize(new Dimension(25, 30));
        colorLabelPanel.setBorder(new CompoundBorder(new EmptyBorder(1, 10, 1, 10), new SoftBevelBorder(SoftBevelBorder.LOWERED)));
        
        add(colorLabelPanel, BorderLayout.SOUTH);
    }
    
    @Override
    public String getDisplayName() {

        return label;
    }
    
    @Override
    public Icon getLargeDisplayIcon() {

        return null;
    }
    
    @Override
    public Icon getSmallDisplayIcon() {

        return null;
    }
    
    @Override
    public void updateChooser() {}
}
