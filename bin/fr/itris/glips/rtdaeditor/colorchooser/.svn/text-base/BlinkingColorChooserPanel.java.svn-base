/*
 * Created on 21 févr. 2005
 */
package fr.itris.glips.rtdaeditor.colorchooser;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;

import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.rtda.colorsblinkings.*;

/**
 * the panel enabling to choose a blinking color 
 * 
 * @author ITRIS, Jordi SUC
 */
public class BlinkingColorChooserPanel extends AbstractColorChooserPanel{

	/**
	 * the label of the panel
	 */
	private String label="";
	
	/**
	 * the labels
	 */
	private String memoryLabel="", noColorAvailableLabel="";
	
	/**
	 * the bundle used to get labels
	 */
	private ResourceBundle bundle=null;
	
	/**
	 * the color chooser
	 */
	private RtdaColorChooserModule colorChooser=null;
	
	/**
	 * the list of the runnables used to clear the color chooser panel from its previous components
	 */
	private LinkedList<Runnable> disposeRunnables=new LinkedList<Runnable>();
	
	/**
	 * the constructor of the class 
	 * @param colorChooser the color chooser
	 */
	public BlinkingColorChooserPanel(RtdaColorChooserModule colorChooser){
	    
	    this.colorChooser=colorChooser;
	    
	    //gets the labels from the resources
        bundle=ResourcesManager.bundle;
        
	    if(bundle!=null){
	        
	        try{
	            label=bundle.getString("blinkingColorChooserPanelLabel");
	            memoryLabel=bundle.getString("colorChooserMemoryLabel");
	            noColorAvailableLabel=bundle.getString("noColorAvailableLabel");
	        }catch (Exception ex){}
	    }
	    
        //a listener that listens to the changes of the svg handles
        HandlesListener svgHandleListener=new HandlesListener(){

        	@Override
        	public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {

                buildChooser();
            }
        };
        
        //adds the svg handles change listener
        colorChooser.getSVGEditor().getHandlesManager().addHandlesListener(svgHandleListener);
	}
    
	@Override
	protected void buildChooser() {
	    
	    //clears the color chooser panel before adding new widgets into it, by calling all the dispose runnables
	    for(Runnable runnable : disposeRunnables){
	        
	        if(runnable!=null){
	            
	            runnable.run();
	        }
	    }
	    
	    //clears the list of the dispose runnables
	    disposeRunnables.clear();
	    
	    //removes all the components of the panel
	    removeAll();
	    
	    //retrieving the current handle
	    SVGHandle handle=colorChooser.getSVGEditor().getHandlesManager().getCurrentHandle();
	    Collection<Object> colorsList=null;
	        
	    if(handle!=null){
            
	        colorsList=new LinkedList<Object>(colorChooser.getColorsAndBlinkingsToolkit().getBlinkingColorsMap(
	        		handle.getScrollPane().getSVGCanvas().getProjectFile()).values());
	    }
	        
	    if(colorsList!=null && colorsList.size()>0){

		    //the panel containing the color and memory panels
		    JPanel colorsAndMemoryPanel=new JPanel();
		    
		    //the panel containing the panels displaying the colors
		    JPanel colorsPanel=new JPanel();
		    int colorsNbPerLine=25, minRowNb=6;
		    
		    //the elements for the last colors panel functionnality
		    final int memoryNb=35, memoryRowNb=7;
		    
		    //creating the memory panel
		    final JPanel memoryPanel=new JPanel();
		    
		    //creating the list of the panels that will be contained in the memory panel
		    final LinkedList<JPanel> lastColorPanels=new LinkedList<JPanel>();
		    
		    //creating the list of the lastly selected colors
		    final LinkedList<Color> lastSelectedColors=new LinkedList<Color>();
		    
		    //the label panel displaying the name and the corresponding rgb values of a color
		    final JLabel colorLabel=new JLabel("", SwingConstants.CENTER);
		    
		    int rowNb=(int)(Math.floor(colorsList.size()/colorsNbPerLine)+1);
		    
		    colorsPanel.setLayout(new GridLayout((rowNb>minRowNb)?rowNb:minRowNb, colorsNbPerLine, 1, 1));
		    BlinkingColor color=null;
		    JPanel panel=null;
		    MouseListener mouseListener=null;
		    int count=0;
		    final Dimension panelsSize=new Dimension(15, 15);
		    
		    for(Iterator<Object> it=colorsList.iterator(); it.hasNext();){
		        
		        color=(BlinkingColor)it.next();
		        
		        if(color!=null){
		            
		            final BlinkingColor fcolor=color;
		            
		            //the panel that will show a representation of a color
		            panel=new JPanel(){

		            	@Override
                        protected void paintComponent(Graphics g) {

                            super.paintComponent(g);
                            
                            //draws two colored rectangles representing the two colors of the blinking color
                            g.setColor(fcolor);
                            g.fillRect(0, 0, panelsSize.width/2, panelsSize.height);
                            
                            g.setColor(fcolor.getColor2());
                            g.fillRect(panelsSize.width/2, 0, panelsSize.width/2, panelsSize.height);
                        }
		            };
		            
		            //setting the properties of the panel
		            panel.setBorder(new LineBorder(Color.black, 1));
		            panel.setBackground(color);
		            panel.setPreferredSize(panelsSize);
		            panel.setToolTipText(color.getStringRepresentation());
		            
		            final JPanel fpanel=panel;
		            
		            //creating and adding a listener to the clicks on the color panels
		            mouseListener=new MouseAdapter(){
		        		
		            	@Override
		                public void mouseClicked(MouseEvent evt) {
		
		                    BlinkingColor theColor=null, selectedColor=fcolor;
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
		                                
		                                theColor=(BlinkingColor)lastSelectedColors.get(i);
		                                
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
		                public void mouseEntered(MouseEvent evt){
		
		                    colorLabel.setText(fcolor.getStringRepresentation());
		                }
		
		            	@Override
		                public void mouseExited(MouseEvent evt){
		
		                    colorLabel.setText("");
		                }
		            };
		            
		            panel.addMouseListener(mouseListener);

		            //adding the runnable used to dispose the current panel
		            final MouseListener fmouseListener=mouseListener;
		            
		            disposeRunnables.add(new Runnable(){

                        public void run() {
                            
                            fpanel.removeMouseListener(fmouseListener);
                        }
		            });
		            
		            colorsPanel.add(panel);
		        }
		        
		        count++;
		    }
		    
		    //adds other panels to fully fills the colors panels
		    if(count<minRowNb*colorsNbPerLine){
		        
		        for(int i=count; i<minRowNb*colorsNbPerLine; i++){
		            
		            colorsPanel.add(new JPanel());
		        }
		    }
		    
		    //filling the memory panel
		    memoryPanel.setLayout(new GridLayout(memoryRowNb, (int)(Math.floor(memoryNb/memoryRowNb))+1, 1, 1));
		    memoryPanel.setBorder(new TitledBorder(memoryLabel));
		    
		    JPanel memPanel=null;
		    
		    for(int i=0; i<memoryNb; i++){
		        
		        final int fi=i;
		        
		        //the panel that will show a representation of a color
	            memPanel=new JPanel(){

	            	@Override
                    protected void paintComponent(Graphics g) {

                        super.paintComponent(g);
                        
   					    BlinkingColor bColor=null;
   					    
   					    if(fi<lastSelectedColors.size()){
   					        
   					        bColor=(BlinkingColor)lastSelectedColors.get(fi);
   					        
   					        if(bColor!=null){
   					            
   		                        //draws two colored rectangles representing the two colors of the blinking color
   		                        g.setColor(bColor);
   		                        g.fillRect(0, 0, panelsSize.width/2, panelsSize.height);
   		                        
   		                        g.setColor(bColor.getColor2());
   		                        g.fillRect(panelsSize.width/2, 0, panelsSize.width/2, panelsSize.height);
   					        }
   					    }
                    }
	            };

	            memPanel.setBorder(new LineBorder(Color.lightGray, 1));
		        memPanel.setPreferredSize(new Dimension(15, 15));
		        memPanel.setBackground(getParent().getBackground());
		        
		        lastColorPanels.add(memPanel);
		        memoryPanel.add(memPanel);

		        //creating and adding a mouse listener to the panel
		        mouseListener=new MouseAdapter(){
		       	 
		        	@Override
   					public void mouseClicked(MouseEvent evt) {
   	
   					    BlinkingColor bColor=null;
   					    
   					    if(fi<lastSelectedColors.size()){
   					        
   					        bColor=(BlinkingColor)lastSelectedColors.get(fi);
   					        
   					        if(bColor!=null){
   					            
   					            getColorSelectionModel().setSelectedColor(bColor);
   					        }
   					    }
   					}
   					
		        	@Override
   	                public void mouseEntered(MouseEvent evt){
   	                    
   	                    BlinkingColor bColor=null;
   					    
   					    if(fi<lastSelectedColors.size()){
   					        
   					        bColor=(BlinkingColor)lastSelectedColors.get(fi);
   					        
   					        if(bColor!=null){
   					            
   			                    colorLabel.setText(bColor.getStringRepresentation());
   					        }
   					    }
   	                }
   	
		        	@Override
   	                public void mouseExited(MouseEvent evt){
   	
   	                    BlinkingColor bColor=null;
   					    
   					    if(fi<lastSelectedColors.size()){
   					        
   					        bColor=(BlinkingColor)lastSelectedColors.get(fi);
   					        
   					        if(bColor!=null){
   					            
   			                    colorLabel.setText("");
   					        }
   					    }
   		            }
   		        };
		        
	            memPanel.addMouseListener(mouseListener);
		            
	            //adding the runnable used to dispose the current panel
	            final MouseListener fmouseListener=mouseListener;
	            final JPanel fmemPanel=memPanel;
	            
	            disposeRunnables.add(new Runnable(){

                    public void run() {
                        
                        fmemPanel.removeMouseListener(fmouseListener);
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
	        colorLabelPanel.setBorder(new CompoundBorder(	new EmptyBorder(1, 10, 1, 10), 
	        																				new SoftBevelBorder(BevelBorder.LOWERED)));
	        
	        add(colorLabelPanel, BorderLayout.SOUTH);
		    
	    }else{
	        
	        //adds a label for indicating that no color is available
	        JLabel noColorLbl=new JLabel(noColorAvailableLabel);

	        setLayout(new FlowLayout(FlowLayout.CENTER));
	        add(noColorLbl);
	    }
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