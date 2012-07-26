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

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import javax.swing.plaf.metal.*;
import org.w3c.dom.*;

/**
 * the class of the widget that will be displayed in the properties dialog, and enabling to modify the color values
 * of the children of a gradient resource node
 * 
 * @author ITRIS, Jordi SUC, Maciej Wojtkiewicz
 */
public class SVGVisualResourceGradientColorChooserChildWidget extends SVGVisualResourceChildWidget{
	
	/**
	 * the constructor of the class
	 * @param resourceObject the resource object whose children will be modified by this widget
	 */
    public SVGVisualResourceGradientColorChooserChildWidget(SVGVisualResourceObject resourceObject){
        
        super(resourceObject);
        this.resourceObject=resourceObject;
        buildComponent();
    }
    
    /**
     * builds the component to be displayed
     */
    protected void buildComponent(){
        
        if(resourceObject!=null && resourceObject.getChildren().getFirst()!=null){

            final SVGVisualResourceObject fresourceObject=resourceObject;
            final Editor svgEditor=resourceObject.getResourceModel().getVisualResources().getSVGEditor();
            SVGVisualResourceObjectChild childObject=(SVGVisualResourceObjectChild)resourceObject.getChildren().getFirst();
            
            //the labels
            String widgetLabel=childObject.getChildModel().getAbsoluteName();
            String 	stopProperties="", stopColor="", stopOpacity="", offset="", labelAddStop="", labelRemoveStop="";

            if(bundle!=null){
                
                try{
                    widgetLabel=bundle.getString(widgetLabel);
                    stopProperties=bundle.getString("vresource_stopproperties");
                    stopColor=bundle.getString("vresource_stop-color");
                    stopOpacity=bundle.getString("vresource_stop-opacity");
                    offset=bundle.getString("vresource_offset");
                    labelAddStop=bundle.getString("vresource_addstop");
                    labelRemoveStop=bundle.getString("vresource_removestop");
                }catch (MissingResourceException ex) {
                	ex.printStackTrace();
                }
            }
            
            this.label=widgetLabel;
            
            //the gradient items list
            final LinkedList gradientItems=new LinkedList();
            
            //the width of a gradient item
            final int itemWidth=12;
            
            //the panel containing all the elements
            JPanel content=new JPanel();
            
            //the panel containing the arrows
            final JPanel arrowsPanel=new JPanel(){
                
            	@Override
                protected void paintComponent(Graphics g) {
                    
                    super.paintComponent(g);
                    
                    //paints all the representations of the items
                    SVGGradientChooserItem item=null;
                    
                    for(Iterator it=gradientItems.iterator(); it.hasNext();){
                        
                            item=(SVGGradientChooserItem)it.next();
                        
                        if(item!=null){
                            
                            item.paintArrow((Graphics2D)g);
                        }
                    }
                }
            };
            
            //the dimension of the arrows panel
            final Dimension arrowsPanelSize=new Dimension(200+itemWidth, 20);
            arrowsPanel.setPreferredSize(arrowsPanelSize);
            
            //a comparator to sort the gradient items list
            final Comparator comparator=new Comparator(){
                
                public int compare(Object arg0, Object arg1){
                    
                    if(		arg0!=null && arg1!=null &&
                            arg0 instanceof SVGGradientChooserItem && arg1 instanceof SVGGradientChooserItem){
                        
                        SVGGradientChooserItem 	item0=(SVGGradientChooserItem)arg0, 
                        											item1=(SVGGradientChooserItem)arg1;
                        
                        return item0.getXPosition()-item1.getXPosition();
                    }
                    
                    return 0;
                } 
            };
            
            //the dimension of the arrows panel
            final Dimension gradientPanelSize=new Dimension(200+itemWidth, 42);

            //the panel containing the gradients
            final JPanel gradientPanel=new JPanel(){
                
            	@Override
                protected void paintComponent(Graphics g) {
                    
                    super.paintComponent(g);
                    
                    int 	sq=8, 
                    		lx=(gradientPanelSize.width-itemWidth)/sq, 
                    		ly=gradientPanelSize.height/sq;

                    //drawing the background that symbolizes transparency
                    for(int i=0; i<=lx; i++){
                        
                        for(int j=0; j<=ly; j++){
                            
                            g.setColor((i+j)%2==0?Color.gray:Color.darkGray);
                            g.fillRect(i*sq+itemWidth/2, j*sq, ((i*sq+itemWidth/2)>(gradientPanelSize.width-itemWidth)?sq-(i*sq+itemWidth/2):sq), sq);
                        }
                    }

                    //sorts the list of the gradient items
                    Collections.sort(gradientItems, comparator);
                    
                    //paints the gradients represented by each item
                    SVGGradientChooserItem item=null, previousItem=null;
                    
                    for(Iterator it=gradientItems.iterator(); it.hasNext();){
                        
                            item=(SVGGradientChooserItem)it.next();
                        
                        if(item!=null){
                            
                            //paints the gradient for the item
                            item.paintGradient((Graphics2D)g, previousItem, it.hasNext());
                            previousItem=item;
                        }
                    }
                    
                    //painting the border
                    g.setColor(Color.black);
                    g.drawRect(itemWidth/2, 0, gradientPanelSize.width-itemWidth, gradientPanelSize.height-1);
                }
            };
            
            gradientPanel.setPreferredSize(gradientPanelSize);
            
            //the labels for each widget
            final JLabel 	stopColorLbl=new JLabel(stopColor.concat(" :")),
            					stopOpacityLbl=new JLabel(stopOpacity.concat(" :")),
            					offsetLbl=new JLabel(offset.concat(" :"));
            
            //the panel containing the add and remove button
            JPanel buttonPanel=new JPanel();
            
            //the icons
            final ImageIcon 	addIcon=svgEditor.getResourcesManager().getIcon("New", false),
            							deleteIcon=svgEditor.getResourcesManager().getIcon("Delete", false),
            							deleteDisabledIcon=svgEditor.getResourcesManager().getIcon("Delete", true);

            //the add and remove buttons
            final JButton addBt=new JButton(addIcon), deleteBt=new JButton(deleteIcon);

            //dealing with the buttons' properties
            addBt.setToolTipText(labelAddStop);
            deleteBt.setToolTipText(labelRemoveStop);
            
            addBt.setMargin(new Insets(1,1,1,1));
            deleteBt.setMargin(new Insets(1,1,1,1));
            
            addBt.setPreferredSize(new Dimension(20, 20));
            deleteBt.setPreferredSize(new Dimension(20, 20));
            
            deleteBt.setEnabled(false);
            
            //adds the buttons to the button panel
            buttonPanel.setLayout(new GridLayout(2, 1, 0, 1));
            buttonPanel.add(addBt);
            buttonPanel.add(deleteBt);
            
            //the label displaying the position en percentage
            final JTextField positionValueTxt=new JTextField("", 4);
            positionValueTxt.setAlignmentX(JTextField.RIGHT_ALIGNMENT);
            positionValueTxt.setEditable(false);
            
            //the color button and its properties
            final JButton colorBt=new JButton();
            colorBt.setMargin(new Insets(1,1,1,1));
            colorBt.setPreferredSize(new Dimension(20, 15));
            colorBt.setEnabled(false);
            
            //the color panel that will be inserted in the color button
            final JPanel colorPanel=new JPanel();
            
            //the color for the disabled state of the color button
            final Color disabledColorPanelColor=new Color(200, 200, 200);
            colorPanel.setBackground(disabledColorPanelColor);
            colorBt.add(colorPanel);

            //the slider for choosing the opacity//
            
            //the panel that will contain the widgets
            final JPanel displayAndSlider=new JPanel();
            
            //the initial value
            int val=100;
            final JSlider slider=new JSlider(0, 100, val);
            
            final JLabel displayedValue=new JLabel(format.format(val)+" %");
            displayedValue.setPreferredSize(new Dimension(40, 20));
            slider.setPreferredSize(new Dimension(130, 25));
            
            //creates the panel that will be displayed
            displayAndSlider.setLayout(new BoxLayout(displayAndSlider, BoxLayout.X_AXIS));
            displayAndSlider.add(slider);
            displayAndSlider.add(displayedValue);
            
            //the listener managing the arrows on the arrows panel
            final SVGGradientChooserArrowsListener arrowsListener=new SVGGradientChooserArrowsListener(){
                
                /**
                 * tells whether the gradient items have been modified
                 */
                private boolean isModified=false;
                
                /**
                 * tells if the current arrow can be dragged or not
                 */
                private boolean canDrag=false;
                
                /**
                 * the currently selected item
                 */
                protected SVGGradientChooserItem currentItem=null;
                
                /**
                 * @return the currently selected item
                 */
                public SVGGradientChooserItem getCurrentItem(){
                    
                    return currentItem;
                }
                
                public void setCurrentItem(SVGGradientChooserItem item){
                    
                    currentItem=item;
                    
                    //sets if the delete button should be enabled or not
                    if(item!=null){

                        deleteBt.setEnabled(true);
                        deleteBt.setIcon(deleteIcon);
                        colorBt.setEnabled(true);
                        slider.setEnabled(true);
                        
                        if(item.getColor()!=null){

                            selectGradientChooserItem(item);
                            colorPanel.setBackground(item.getColor());
                            positionValueTxt.setText(item.getXPositionPercent()+"%");
                            displayedValue.setText((int)(item.getOpacity()*100)+"%");
                            slider.setValue((int)(item.getOpacity()*100));
                        }
                        
                    }else{
                        
                        deleteBt.setEnabled(false);
                        deleteBt.setIcon(deleteDisabledIcon);
                        colorBt.setEnabled(false);
                        colorPanel.setBackground(disabledColorPanelColor);
                        displayedValue.setText("0%");
                        slider.setEnabled(false);
                        positionValueTxt.setText("");
                    }
                    
                    refresh();
                }
                
                /**
                 * tells whether the gradient items have been modified
                 */
                public boolean isModified(){
                    
                    return isModified;
                }
                
                /**
                 * the method used to refresh the arrows
                 */
                public void refresh(){
                    
                    arrowsPanel.repaint();
                    gradientPanel.repaint();
                }
                
                /**
                 * creates and adds a gradient item to the list
                 * @param resChild a resource child
                 */
                public void createGradientItem(SVGVisualResourceObjectChild resChild){
                    
                    if(resChild!=null){
                        
                        SVGGradientChooserItem item=new SVGGradientChooserItem(resChild, arrowsPanelSize, gradientPanelSize);
                        
                        if(item!=null){
                            
                            gradientItems.add(item);
                            setCurrentItem(item);
                            
                            synchronized(this){isModified=true;}
                        }
                    }
                }
                
                /**
                 * removes a gradient item from the list
                 */
                public void removeCurrentGradientItem(){
                    
                    if(currentItem!=null){
                        
                        SVGVisualResourceObjectChild resChild=currentItem.getResourceObjectChild();
                        
                        if(resChild!=null){
                            
                            synchronized(this){isModified=true;}
                            
                            resourceObject.removeChildResource(resChild);
                        }
                        
                        gradientItems.remove(currentItem);
                        setCurrentItem(null);
                    }
                }
                
                public void mousePressed(MouseEvent e) {
                    
                    Point point=getNormalizedPoint(e.getPoint());
                    
                    if(point!=null){
                        
                        SVGGradientChooserItem item=getClickedItem(point);

                        if(item!=null){
                            
                            if(e.getClickCount()==2){
                                
                                setCurrentItem(item);
                                canDrag=false;
                                
                            }else if(e.getClickCount()==1){
                                
                                setCurrentItem(item);
                                canDrag=true;
                            }

                        }else{
                            
                            canDrag=false;
                            
                            //creating a new item
                            SVGVisualResourceObjectChild resChild=resourceObject.createNewChild();
                            
                            if(resChild!=null){
                                
                                item=new SVGGradientChooserItem(resChild, arrowsPanelSize, gradientPanelSize);
                                
                                if(item!=null){
                                    
                                    //sets the color and position for this item
                                    item.setColor(Color.black);
                                    item.setXPosition(e.getPoint().x);
                                    
                                    //adds the item to the gradient items list
                                    gradientItems.add(item);
                                    setCurrentItem(item);
                                    
                                    synchronized(this){isModified=true;}
                                }
                            }
                        }
                    }
                }
                
                public void mouseReleased(MouseEvent e) {
                    
                    if(currentItem!=null){
                        
                        if(e.getClickCount()==2){
 
                        	Color color=Editor.getColorChooser().showColorChooserDialog(colorPanel.getBackground());
                            
                            if(color!=null && currentItem!=null){
                                
                                colorPanel.setBackground(color);
                                currentItem.setColor(color);
                                Editor.getSVGColorManager().setCurrentColor(color);
                                currentItem.applyChanges();
                                refresh();
                            }
                        }
                        
                        synchronized(this){isModified=true;}
                        currentItem.applyChanges();
                    }
                }
                
                public void mouseDragged(MouseEvent e) {
                    
                    if(currentItem!=null){
                        
                        Point point=getNormalizedPoint(e.getPoint());
                        
                        if(point!=null && canDrag){
                            
                            currentItem.setXPosition(point.x);
                            positionValueTxt.setText(currentItem.getXPositionPercent()+"%");
                            refresh();
                        }
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
                 * normalizes the point taken from the event
                 * @param point the point passed by the event
                 * @return the normalized point
                 */
                protected Point getNormalizedPoint(Point point){
                    
                    Point pt=null;
                    
                    if(point!=null){
                        
                        int x=0, y=0;
                        
                        //sets the x value at the limits
                        if(point.x<0){
                            
                            x=0;
                            
                        }else if(point.x>arrowsPanelSize.width){
                            
                            x=(int)arrowsPanelSize.width;
                            
                        }else{
                            
                            x=point.x;
                        }
                        
                        y=point.y;
                        
                        pt=new Point(x, y);
                    }
                    
                    return pt;
                }
                
                /**
                 * @param point the position of the mouse
                 * @return the corresponding item
                 */
                protected SVGGradientChooserItem getClickedItem(Point point){
                    
                    if(point!=null){
                        
                        SVGGradientChooserItem item=null;
                        
                        for(int i=gradientItems.size()-1;i>=0;i--){
                            
                                item=(SVGGradientChooserItem)gradientItems.get(i);
                            
                            if(item!=null && item.belongsToThisItem(point)){
                                
                                return item;
                            }
                        }
                    }
                    
                    return null;
                }
                
                protected void selectGradientChooserItem(SVGGradientChooserItem gradientChooserItem){
                    
                    if(gradientChooserItem!=null){

                        SVGGradientChooserItem item=null;
                        
                        //deselects all the items
                        for(Iterator it=gradientItems.iterator(); it.hasNext();){
                            
                                item=(SVGGradientChooserItem)it.next();
                            
                            if(item!=null){
                                
                                item.setSelected(false);
                            }
                        }
                        
                        //selects the given item
                        gradientChooserItem.setSelected(true);
                    }
                }
                
                /**
                 * disposes the object
                 */
                public void dispose(){
                	
                	currentItem=null;
                }
            };
            
            //adds the listener to the mouse events on the arrows panel
            arrowsPanel.addMouseListener(arrowsListener);
            arrowsPanel.addMouseMotionListener(arrowsListener);
            
            //fills the gradient item list
            SVGVisualResourceObjectChild resChild=null;

            for(Iterator it=resourceObject.getChildren().iterator(); it.hasNext();){
                
                    resChild=(SVGVisualResourceObjectChild)it.next();
                
                if(resChild!=null && arrowsListener!=null){
                    
                    arrowsListener.createGradientItem(resChild);
                }
            }
            
            //the listeners to the add button
            final ActionListener addBtListener=new ActionListener(){
                
                public void actionPerformed(ActionEvent e) {
                    
                    SVGVisualResourceObjectChild resChild=resourceObject.createNewChild();
                    
                    if(resChild!=null && arrowsListener!=null){
                        
                        arrowsListener.createGradientItem(resChild);
                    }
                } 
            };
            
            addBt.addActionListener(addBtListener);
            
            //the listeners to the delete button
            final ActionListener deleteBtListener=new ActionListener(){
                
                public void actionPerformed(ActionEvent e) {
                    
                    if(arrowsListener!=null){
                        
                        arrowsListener.removeCurrentGradientItem();
                    }
                }
            };
            
            deleteBt.addActionListener(deleteBtListener);
            
            //the listener to the color button
            final ActionListener colorBtListener=new ActionListener(){
                
                public void actionPerformed(ActionEvent e) {
                    
                	Color color=Editor.getColorChooser().showColorChooserDialog(colorPanel.getBackground());
                    
                    if(color!=null && arrowsListener!=null && arrowsListener.getCurrentItem()!=null){
                        
                        colorPanel.setBackground(color);
                        arrowsListener.getCurrentItem().setColor(color);
                        Editor.getSVGColorManager().setCurrentColor(color);
                        arrowsListener.getCurrentItem().applyChanges();
                        arrowsListener.refresh();
                    }
                } 
            };
            
            colorBt.addActionListener(colorBtListener);

            //the listener to the slider
            final MouseAdapter sliderListener=new MouseAdapter(){
                
            	@Override
                public void mouseReleased(MouseEvent evt) {
                    
                    arrowsListener.getCurrentItem().setOpacity(((double)slider.getValue())/100);
                    arrowsListener.getCurrentItem().applyChanges();
                    gradientPanel.repaint();
                }
            };
            
            //adds a listener to the slider
            slider.addMouseListener(sliderListener);
            
            //a listener to modify the displayed value of the slider
            final ChangeListener sliderChangeListener=new ChangeListener(){
                
                public void stateChanged(ChangeEvent evt) {
                    
                    arrowsListener.getCurrentItem().setOpacity(((double)slider.getValue())/100);
                    displayedValue.setText(slider.getValue()+" %");
                    gradientPanel.repaint();
                }
            };
            
            //adds a listener to the slider
            slider.addChangeListener(sliderChangeListener);

            //dealing with the content panel//
            
            //the panel containing the widgets to modify the properties of an item
            JPanel propPanel=new JPanel();
            TitledBorder border=new TitledBorder(stopProperties);
            propPanel.setBorder(border);
            
            //sets the layout
            GridBagLayout gridBag0=new GridBagLayout();
            propPanel.setLayout(gridBag0);
            
            //sets the layout constraints
            GridBagConstraints c0=new GridBagConstraints();
            Insets insetsLbl0=new Insets(0, 0, 2, 2), insets0=new Insets(0, 2, 0, 2);
            
            //adding the offset widget
            c0.gridwidth=1;
            c0.insets=insetsLbl0;
            c0.anchor=GridBagConstraints.EAST;
            c0.fill=GridBagConstraints.NONE;
            gridBag0.setConstraints(offsetLbl, c0);
            propPanel.add(offsetLbl);
            
            c0.gridwidth=1;
            c0.insets=insets0;
            c0.anchor=GridBagConstraints.WEST;
            c0.fill=GridBagConstraints.NONE;
            gridBag0.setConstraints(positionValueTxt, c0);
            propPanel.add(positionValueTxt);
            
            //adding the color widget
            c0.gridwidth=1;
            c0.insets=insets0;
            c0.anchor=GridBagConstraints.EAST;
            c0.fill=GridBagConstraints.NONE;
            gridBag0.setConstraints(stopColorLbl, c0);
            propPanel.add(stopColorLbl);
            
            c0.gridwidth=GridBagConstraints.REMAINDER;
            c0.insets=insets0;
            c0.anchor=GridBagConstraints.WEST;
            c0.fill=GridBagConstraints.NONE;
            gridBag0.setConstraints(colorBt, c0);
            propPanel.add(colorBt);
            
            //adding the slider
            c0.gridwidth=1;
            c0.insets=insetsLbl0;
            c0.anchor=GridBagConstraints.EAST;
            c0.fill=GridBagConstraints.NONE;
            gridBag0.setConstraints(stopOpacityLbl, c0);
            propPanel.add(stopOpacityLbl);
            
            c0.gridwidth=GridBagConstraints.REMAINDER;
            c0.insets=insets0;
            c0.anchor=GridBagConstraints.WEST;
            c0.fill=GridBagConstraints.NONE;
            gridBag0.setConstraints(displayAndSlider, c0);
            propPanel.add(displayAndSlider);

            //building the main panel//
            
            //sets the content's layout
            GridBagLayout gridBag=new GridBagLayout();
            content.setLayout(gridBag);
            
            //sets the layout constraints
            GridBagConstraints c=new GridBagConstraints();
            c.fill=GridBagConstraints.NONE;
            
            //an empty  panel
            JPanel empty=new JPanel();
            c.gridwidth=1;
            gridBag.setConstraints(empty, c);
            content.add(empty);
            
            //the arrows panel
            c.gridwidth=GridBagConstraints.REMAINDER;
            c.anchor=GridBagConstraints.WEST;
            gridBag.setConstraints(arrowsPanel, c);
            content.add(arrowsPanel);
            
            //the buttons panel
            c.gridwidth=1;
            c.anchor=GridBagConstraints.EAST;
            gridBag.setConstraints(buttonPanel, c);
            content.add(buttonPanel);
            
            //the gradient panel
            c.gridwidth=GridBagConstraints.REMAINDER;
            c.anchor=GridBagConstraints.WEST;
            gridBag.setConstraints(gradientPanel, c);
            content.add(gradientPanel);
            
            //the properties panel
            c.gridwidth=GridBagConstraints.REMAINDER;
            c.anchor=GridBagConstraints.WEST;
            gridBag.setConstraints(propPanel, c);
            content.add(propPanel);
            
            //setting the component of the widget
            component=content;
            
            disposer=new Runnable(){

                public void run() {
                    
                    final SVGHandle handle=svgEditor.getHandlesManager().getCurrentHandle();
                    
                    //sorts the child nodes according to the "offset" attribute value
                    if(handle!=null && fresourceObject!=null && arrowsListener.isModified()){
                        
                        //sorts the gradient items list
                        Collections.sort(gradientItems, comparator);

                        //removes all the children of the resource node
                        Node cur=null;
                        
                        for(cur=fresourceObject.getResourceNode().getFirstChild(); cur!=null; cur=cur.getNextSibling()){
                            
                            fresourceObject.getResourceNode().removeChild(cur);
                        }
                        
                        //re-adds all the children in the correct order
                        SVGGradientChooserItem item=null;
                        SVGVisualResourceObjectChild resChild=null;

                        for(Iterator it=gradientItems.iterator(); it.hasNext();){
                            
                                item=(SVGGradientChooserItem)it.next();
                                resChild=item.getResourceChildObject();
                                cur=resChild.getChildElement();
                            
                            if(cur!=null){
                                
                                fresourceObject.getResourceNode().appendChild(cur);
                            }
                        }
                    }
                    
                    gradientItems.clear();
                    
                    //removes the listener to the mouse events on the arrows panel
                    arrowsPanel.removeMouseListener(arrowsListener);
                    arrowsPanel.removeMouseMotionListener(arrowsListener);
                    
                    //removes the listeners
                    addBt.removeActionListener(addBtListener);
                    deleteBt.removeActionListener(deleteBtListener);
                    colorBt.removeActionListener(colorBtListener);
                    slider.removeMouseListener(sliderListener);
                    slider.removeChangeListener(sliderChangeListener);
                    
                    arrowsListener.dispose();
                }
            };
        }
    }
    
    /**
     * the interface used to listens to the mouse events on an arrows panel
     * 
     * @author ITRIS, Jordi SUC
     */
    protected interface SVGGradientChooserArrowsListener extends MouseListener, MouseMotionListener{
        
        /**
         * @return the currently selected item
         */
        public SVGGradientChooserItem getCurrentItem();
        
        /**
         * the method used to refresh the display of the arrows and the gradient panel
         */
        public void refresh();
        
        /**
         * creates and adds a gradient item to the list
         * @param resChild a resource child
         */
        public void createGradientItem(SVGVisualResourceObjectChild resChild);
        
        /**
         * removes a gradient item from the list
         */
        public void removeCurrentGradientItem();
        
        /**
         * @return the boolean telling if the gradient items have been modified or not
         */
        public boolean isModified();
        
        /**
         * disposes the object
         */
        public void dispose();
    }
    
    
    /**
     * the items thatwill be displayed to build a gradient
     * 
     * @author ITRIS, Jordi SUC
     *
     */
    protected class SVGGradientChooserItem{
        
        /**
         * the colors used
         */
        private final Color 	ITEM_COLOR=Color.BLUE,
								        ITEM_FILL_COLOR=new Color(0, 0, 255, 50),
								        ITEM_SELECTED_COLOR=Color.RED,
								        ITEM_SELECTED_FILL_COLOR=new Color(255, 0, 0, 50),
								        BORDER_HIGHLIGHT_COLOR=MetalLookAndFeel.getSeparatorForeground(),
								        BORDER_SHADOW_COLOR=MetalLookAndFeel.getSeparatorBackground();
        
        /**
         * the resource child
         */
        private SVGVisualResourceObjectChild resChild;
        
        /**
         * the boolean telling if the item is selected
         */
        private boolean isSelected=false;
        
        /**
         * the width of a gradient item
         */
        private int itemWidth=12;
        
        /**
         * the position of the item along the y-axis
         */
        private int xPosition=0;
        
        /**
         * the position of the item along the y-axis
         */
        private int yPosition=0;
        
        /**
         * the color of the item
         */
        private Color color=null;
        
        /**
         * the opacity for the color of the item, a double in the [0..1] interval
         */
        private double opacity=1;

        /**
         * the resource object for the position of the color
         */
        private SVGVisualResourceObjectAttribute posResAttribute=null;
        
        /**
         * the resource object for the style of the item
         */
        private SVGVisualResourceObjectAttribute styleResAttribute=null;
        
        /**
         * the size of the arrows panel
         */
        private Dimension arrowsPanelSize=null;
        
        /**
         * the size of the gradient panel
         */
        private Dimension gradientPanelSize=null;
        
        /**
         * the path to be displayed
         */
        private GeneralPath path=null, colorRect=null;
        
        /**
         * the constructor of the class
         * @param resChild a resource child
         * @param arrowsPanelSize the size of of the arrows panel
         * @param gradientPanelSize the size of of the gradient panel 
         */
        protected SVGGradientChooserItem(SVGVisualResourceObjectChild resChild, Dimension arrowsPanelSize, Dimension gradientPanelSize){
            
            this.resChild=resChild;
            
            if(arrowsPanelSize!=null){
                
                this.yPosition=(int)arrowsPanelSize.height;
            }
            
            this.arrowsPanelSize=arrowsPanelSize;
            this.gradientPanelSize=gradientPanelSize;
            
            //sets the position of the item along the x-axis
            xPosition=itemWidth/2;
            
            if(resChild!=null){
                
                //getting the list of the attributes of the resource child
                LinkedList attributes=resChild.getAttributes();
                
                if(attributes!=null){

                    SVGVisualResourceObjectAttribute att=null;
                    
                    //for each attribute, checks if it has one of the following names and then stores it
                    for(int i=0;i<attributes.size();i++){
                        
                            att=(SVGVisualResourceObjectAttribute)attributes.get(i);
                        
                        if(att!=null){
                            
                            if(att.getModel().getName().equals("offset")){
                                
                                posResAttribute=att;
                                
                            }else if(att.getModel().getName().equals("style")){
                                
                                styleResAttribute=att;
                            }
                        }
                    }
                }
            }
            
            //getting the values of the item
            xPosition=computeXPosition();
            color=computeColor();
            opacity=computeOpacity();
            
            path=new GeneralPath();
            
            path.moveTo(0, 0);
            path.lineTo(-itemWidth/2, -itemWidth/2);
            path.lineTo(itemWidth/2, -itemWidth/2);
            path.lineTo(0, 0);
            
            colorRect=new GeneralPath();
            
            colorRect.moveTo(-itemWidth/2, -7);
            colorRect.lineTo(-itemWidth/2, -17);
            colorRect.lineTo(itemWidth/2, -17);
            colorRect.lineTo(itemWidth/2, -7);
            colorRect.closePath();
        }
        
        /**
         * @return the resource child object
         */
        protected SVGVisualResourceObjectChild getResourceChildObject(){
            
            return resChild;
        }
        
        /**
         * @return the offset of the color computed from the attribute value
         */
        protected int computeXPosition(){
            
            int pos=0;
            
            if(posResAttribute!=null && arrowsPanelSize!=null){
                
                String val=posResAttribute.getValue();
                
                //normalizing the string
                if(val==null){
                    
                    val="";
                }
                
                val=val.replaceAll("\\s+", "");
                
                 if(val.indexOf("%")!=-1) {
                     
                   val=val.replaceAll("%", "");
                   int pos2=(int)Double.parseDouble(val);
                   pos=(pos2*(arrowsPanelSize.width-itemWidth))/100+itemWidth/2;
                   
                 }else{
                     
                     double pos2=Double.parseDouble(val);
                     pos=(int)(pos2*(arrowsPanelSize.width-itemWidth))+itemWidth/2;
                 }
            }
            
            return pos;
        }
        
        /**
         * @return the offset of the color
         */
        protected int getXPosition(){
            
            return xPosition;
        }
        
        /**
         * @return the offset of the color in the percentage format
         */
        protected int getXPositionPercent(){
            
            int xPos=0;
            
            if(arrowsPanelSize!=null){
                
                double percent=100*(xPosition-itemWidth/2)/(arrowsPanelSize.width-itemWidth);
                xPos=(int)percent;
            }
            
            return xPos;
        }
        
        /**
         * sets the offset of the color
         * @param xPosition the x position
         */
        protected synchronized void setXPosition(int xPosition){
            
            if(xPosition<itemWidth/2){
                
                xPosition=itemWidth/2;
            }
            
            if(xPosition>(arrowsPanelSize.width-itemWidth/2)){
                
                xPosition=arrowsPanelSize.width-itemWidth/2;
            }
            
            this.xPosition=xPosition;
        }
        
        /**
         * sets the value of the offset in the attribute
         */
        protected void applyXPosition(){
            
            if(posResAttribute!=null && arrowsPanelSize!=null){
                
                double percent=100*(xPosition-itemWidth/2)/(arrowsPanelSize.width-itemWidth);
                
                posResAttribute.setValue(format.format((int)percent)+"%");
            }
        }
        
        /**
         * @return the color
         */
        protected Color getColor(){
            
            return color;
        }
        
        /**
         * @return the opacity a double in the [0..1] interval
         */
        protected double getOpacity(){
            
            return opacity;
        }
        
        /*** 
         * @return the color computed from the attribute value
         */
        protected Color computeColor(){
            
            Color color=Color.black;
            
            if(styleResAttribute!=null){
                
                String val=styleResAttribute.getValue();
                
                //normalizing the string
                if(val==null){
                    
                    val="";
                }
                
                val=val.replaceAll("\\s*[;]\\s*", ";");
                val=val.replaceAll("\\s*[:]\\s*", ":");
                
                String prop="stop-color";
                int ind=val.indexOf(prop);
                
                if(ind!=-1){
                    
                    //getting the value of the color among the style string
                    val=val.substring(ind, val.length());
                    val=val.substring(val.indexOf(":")+1, val.indexOf(";"));
                    
                    Editor editor=resChild.getChildModel().getVisualResources().getSVGEditor();
                    SVGHandle handle=editor.getHandlesManager().getCurrentHandle();
                    
                    color=editor.getColorChooser().getColor(handle, val);
                }
            }
            
            return color;
        }
        
        /*** 
         * @return the opacity of the color computed from the attribute value
         */
        protected double computeOpacity(){
            
            double op=1;
            
            if(styleResAttribute!=null){
                
                String val=styleResAttribute.getValue();
                
                //normalizing the string
                if(val==null){
                    
                    val="";
                }
                
                val=val.replaceAll("\\s*[;]\\s*", ";");
                val=val.replaceAll("\\s*[:]\\s*", ":");
                
                String prop="stop-opacity";
                int ind=val.indexOf(prop);
                
                if(ind!=-1){
                    
                    //getting the value of the color among the style string
                    val=val.substring(ind, val.length());
                    val=val.substring(val.indexOf(":")+1, val.indexOf(";"));
                    
                    op=resChild.getChildModel().getVisualResources().getSVGEditor().getSVGToolkit().getDoubleValue(val, true)/100;
                }
            }
            
            return op;
        }
        
        /**
         * sets the color of this item
         * @param color a color
         */
        protected synchronized void setColor(Color color){
            
            if(color==null){
                
                this.color=Color.black;
                
            }else{
                
                this.color=color;
            }
        }
        
        /**
         * sets the opacity of the color of this item
         * @param opacity the value for the opacity a double in the [0..1] interval
         */
        protected synchronized void setOpacity(double opacity){
            
            if(opacity<0){
                
                opacity=0;
            }
            
            if(opacity>1){
                
                opacity=1;
            }

            this.opacity=opacity;
        }
        
        /**
         * sets the value of the color in the style attribute
         */
        protected void applyColor(){
            
            if(color!=null && styleResAttribute!=null){
                
                String val=styleResAttribute.getValue();
                
                //normalizing the string
                if(val==null){
                    
                    val="";
                }
                
                val=val.replaceAll("\\s*[;]\\s*", ";");
                val=val.replaceAll("\\s*[:]\\s*", ":");
                
                String prop="stop-color";
                int ind=val.indexOf(prop);
                
                if(ind!=-1){
                    
                    //getting the value of the color among the style string
                    String val1=val1=val.substring(0, ind), val2=val.substring(ind, val.length());

                    //removes the previous value if it exists
                    int ind2=val2.indexOf(";");
                    
                    if(ind2!=-1){
                        
                        val2=val2.substring(ind2+1, val2.length());
                        val=val1.concat(val2);
                        
                        if(val.length()>0 && ! val.endsWith(";")){
                            
                            val=val.concat(";");
                        }
                        
                    }else{
                        
                        val=val1;
                    }
                }

                //creating the new value of the style attribute
                val=val.concat(prop.concat(":".concat(Editor.getColorChooser().getColorString(color)).concat(";")));
                
                //sets the attribute
                styleResAttribute.setValue(val);
            }	        
        }
        
        /**
         * sets the value of the opacity of the color in the style attribute
         */
        protected void applyOpacity(){
            
            if(styleResAttribute!=null){
                
                String val=styleResAttribute.getValue();
                
                //normalizing the string
                if(val==null){
                    
                    val="";
                }
                
                val=val.replaceAll("\\s*[;]\\s*", ";");
                val=val.replaceAll("\\s*[:]\\s*", ":");
                
                String prop="stop-opacity";
                int ind=val.indexOf(prop);
                
                if(ind!=-1){
                    
                    //getting the value of the opacity among the style string
                    String val1="", val2="";
                    val1=val.substring(0, ind);
                    val2=val.substring(ind, val.length());

                    //removes the previous value if it exists
                    int ind2=val2.indexOf(";");
                    
                    if(ind2!=-1){
                        
                        val2=val2.substring(ind2+1, val2.length());
                        val=val1.concat(val2);
                        
                        if(val.length()>0 && ! val.endsWith(";")){
                            
                            val=val.concat(";");
                        }
                        
                    }else{
                        
                        val=val1;
                    }
                }
                
                //creating the new value of the style attribute
                val=val.concat(prop.concat(":".concat(format.format(opacity)).concat(";")));
                
                //sets the attribute
                styleResAttribute.setValue(val);
            }	        
        }
        
        /**
         * applies the changes made on the offset and the color 
         */
        protected void applyChanges(){
            
            applyXPosition();
            applyColor();
            applyOpacity();
        }
        
        /**
         * @param point th position of the mouse
         * @return true if the x position of the mouse is contained in the item interval
         */
        protected boolean belongsToThisItem(Point point){
            
            if(point!=null){
                
                //computes the bounds of the arrow
                AffineTransform af=AffineTransform.getTranslateInstance(xPosition, yPosition-1);
                Shape shape=path.createTransformedShape(af);
                Shape rect=colorRect.createTransformedShape(af);
                Rectangle shapeBounds=shape.getBounds(), rectBounds=rect.getBounds(), 
                				allBounds=new Rectangle(shapeBounds.x, rectBounds.y, rectBounds.width, rectBounds.height+shapeBounds.height);

                //test if the point is contained in the bounds
                if(allBounds.contains(point.x, point.y)){
                    
                    return true;
                }
            }
            
            return false;
        }
        
        /**
         * @return true if the item is selected
         */
        protected boolean isSelected(){
            
            return isSelected;
        }
        
        /**
         * sets the item as selected or not selected
         * @param select true to select the item
         */
        protected synchronized void setSelected(boolean select){
            
            this.isSelected=select;
        }
        
        /**
         * paints the item with the gievn graphics
         * @param g a graphics
         */
        protected void paintArrow(Graphics2D g){
            
            //transforms the shapes
            AffineTransform af=AffineTransform.getTranslateInstance(xPosition, yPosition-1);
            Shape shape=path.createTransformedShape(af);
            Shape rect=colorRect.createTransformedShape(af);
            
            //sets the colors an draws the shape
            if(isSelected){
                
                g.setColor(ITEM_SELECTED_FILL_COLOR);
                
            }else{
                
                g.setColor(ITEM_FILL_COLOR);
            }
            
            g.fill(shape);
            
            if(isSelected){
                
                g.setColor(ITEM_SELECTED_COLOR);
                
            }else{
                
                g.setColor(ITEM_COLOR);
            }
            
            g.draw(shape);
            
            //getting the bounds of the color rectangle
            Rectangle bounds=rect.getBounds();
            
            //painting the color rectangle
            g.setColor(color);
            g.fillRect(bounds.x+2, bounds.y+2, 10, 8);

            //painting the border
            g.setColor(BORDER_SHADOW_COLOR);
            g.drawLine(bounds.x, bounds.y, bounds.x, bounds.y+bounds.height);
            g.drawLine(bounds.x, bounds.y, bounds.x+bounds.width, bounds.y);
            g.drawLine(bounds.x+bounds.width-1, bounds.y, bounds.x+bounds.width-1, bounds.y+bounds.height-1);
            g.drawLine(bounds.x+1, bounds.y+bounds.height-1, bounds.x+bounds.width-1, bounds.y+bounds.height-1);
            
            g.setColor(BORDER_HIGHLIGHT_COLOR);
            g.drawLine(bounds.x+1, bounds.y+1, bounds.x+1, bounds.y+bounds.height-1);
            g.drawLine(bounds.x+1, bounds.y+1, bounds.x+bounds.width-1, bounds.y+1);
            g.drawLine(bounds.x+bounds.width, bounds.y, bounds.x+bounds.width, bounds.y+bounds.height);
            g.drawLine(bounds.x, bounds.y+bounds.height, bounds.x+bounds.width, bounds.y+bounds.height);
        }
        
        /**
         * paints the item with the given graphics
         * @param g a graphics
         * @param previousItem the item previously displayed on the gradient panel
         * @param hasNextItem true if another item takes place after this current one in the gradient items list
         */
        protected void paintGradient(Graphics2D g, SVGGradientChooserItem previousItem, boolean hasNextItem){
            
            //getting the values of the gradient
            int preXPosition=itemWidth/2;
            Color preColor=null, tColor=null;
            
            if(color!=null){
                
                preColor=new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(opacity*255));
                tColor=new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(opacity*255));
            }

            if(previousItem!=null){
                
                preXPosition=previousItem.getXPosition();
                Color pc=previousItem.getColor();
                
                if(pc!=null){
                    
                    preColor=new Color(pc.getRed(), pc.getGreen(), pc.getBlue(), (int)(previousItem.getOpacity()*255));
                }
            }
            
            if(gradientPanelSize!=null && preColor!=null && tColor!=null){

                //drawing the gradient
                GradientPaint gradient=new GradientPaint(preXPosition, 0, preColor, xPosition, 0, tColor);
                g.setPaint(gradient);
                g.fillRect(preXPosition, 0, xPosition-preXPosition, gradientPanelSize.height);
                
                //if the current item is the last item in the gradient items list
                if(! hasNextItem){
                    
                    g.setColor(tColor);
                    g.fillRect(xPosition, 0, (gradientPanelSize.width-itemWidth)-xPosition+itemWidth/2, gradientPanelSize.height);
                }
            }
        }
        
        /**
         * @return the resource child
         */
        protected SVGVisualResourceObjectChild getResourceObjectChild(){
            
            return resChild;
        }
    }
}
