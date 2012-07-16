/*
 * Created on 1 déc. 2004
 
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

package fr.itris.glips.svgeditor.actions.toolbar;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * @author ITRIS, Jordi SUC
 *
 * the class allowing to display a frame containing the components 
 * used for modifying the svg elements
 */
public class ToolsFrame{

    /**
     * the editor
     */
    private Editor editor;
    
    /**
     * the panel containing the widgets that will be displayed
     */
    protected JPanel toolPanel=new JPanel();
    
    /**
     * the frame into which the content of the tool frame will be displayed
     */
    private JInternalFrame internalFrame=new JInternalFrame();
    
    /**
     * the toggle button used to show or hide the frame, and that will be found in the tool bar
     */
    private JToggleButton frameButton=new JToggleButton();
    
    /**
     * the menu item used to handle the visibility of the frame
     */
    private JMenuItem frameMenuItem=new JMenuItem();
    
    /**
     * whether the frame has already been shown
     */
    private boolean hasBeenShown=false;
    
    /**
     * the runnable that should be executed when the visibility of the frame changes
     */
    private Runnable visibilityChangedRunnable=null;
    
	/**
	 * the id
	 */
	private String id="";
    
    /**
     * the labels
     */
    private String showFrameLabel="", hideFrameLabel="";
    
    /**
     * the constructor of the class
     *@param editor the editor
     *@param id the id of a module
     *@param label the label that will be displayed in the title
     *@param toolPanel the panel that will be displayed
     */
    public ToolsFrame(final Editor editor, String id, String label, final JPanel toolPanel){
        
        this.editor=editor;
        this.toolPanel=toolPanel;
        this.id=id;
    	
        //building the dialog//
        
        //setting the icons
        ImageIcon icon=ResourcesManager.getIcon(id, false);
        
        if(icon!=null){
        	
        	internalFrame.setFrameIcon(icon);
        	frameMenuItem.setIcon(icon);
        }
        
        //setting the properties for the dialog
    	internalFrame.setResizable(true);
    	internalFrame.setClosable(true);
    	internalFrame.setMaximizable(false);
    	internalFrame.setIconifiable(false);
    	internalFrame.setTitle(label);
    	toolPanel.setVisible(true);
		
    	JScrollPane scrollpane=new JScrollPane(toolPanel);
    	internalFrame.getContentPane().setLayout(new BoxLayout(
    			internalFrame.getContentPane(), BoxLayout.X_AXIS));
    	internalFrame.getContentPane().add(scrollpane);

		//adds a listener to the resizement of the desktop
		editor.getDesktop().addComponentListener(new ComponentAdapter(){

			@Override
            public void componentResized(ComponentEvent evt) {

                Rectangle dialogBounds=internalFrame.getBounds();
                Dimension parentSize=ToolsFrame.this.editor.getDesktop().getSize();
                
                if(dialogBounds.x+dialogBounds.width>parentSize.width){
                    
                    dialogBounds.x=parentSize.width-dialogBounds.width;
                }
                
                if(dialogBounds.y+dialogBounds.height>parentSize.height){
                    
                    dialogBounds.y=parentSize.height-dialogBounds.height;
                }
                
                if(dialogBounds.x<0){
                	
                	dialogBounds.x=0;
                }
                
                if(dialogBounds.y<0){
                	
                	dialogBounds.y=0;
                }
                
                internalFrame.setBounds(dialogBounds);
            }
		});
		
		//adds the dialog to the desktop
		editor.getDesktop().add(internalFrame);
		
        //setting its z-index on the desktop 
        editor.getDesktop().setLayer(internalFrame, 2, 2);
        
        //getting the labels
        if(ResourcesManager.bundle!=null){
        	
        	try{
        		showFrameLabel=ResourcesManager.bundle.getString("label_hidden_"+id.toLowerCase());
        		hideFrameLabel=ResourcesManager.bundle.getString("label_shown_"+id.toLowerCase());
        	}catch (Exception ex){ex.printStackTrace();}
        }
        
        //getting the icon for this tool frame
        ImageIcon toolFrameIcon=ResourcesManager.getIcon(id+"Window", false);
        
        //handling the menuitem and the toggle button used to show or hide this frame
        frameButton.setIcon(toolFrameIcon);
        frameButton.setToolTipText(showFrameLabel);
        frameMenuItem.setText(showFrameLabel);
        
        //the runnable used to place the frame
        final Runnable runnable=new Runnable(){
        	
        	public void run() {

    			//getting the location of the toggle button
    			Point location=SwingUtilities.convertPoint(frameButton.getParent(), frameButton.getLocation(), 
    																			editor.getDesktop());
    			
    			internalFrame.setLocation(location.x, 0);
    			hasBeenShown=true;
        	}
        };
        
        //adding the listener to the frame button
        frameButton.addActionListener(new ActionListener(){
        	
        	public void actionPerformed(ActionEvent evt) {

        		if(frameButton.isSelected()){
        			
        			frameMenuItem.setText(hideFrameLabel);
        	        frameButton.setToolTipText(hideFrameLabel);
        			
        		}else{
        			
        			frameMenuItem.setText(showFrameLabel);
        	        frameButton.setToolTipText(showFrameLabel);
        		}
        		
        		internalFrame.setVisible(frameButton.isSelected());
        		
        		if(visibilityChangedRunnable!=null){
        			
        			visibilityChangedRunnable.run();
        		}

        		internalFrame.pack();

        		if(! hasBeenShown){
        			
        			runnable.run();
        		}
        	}
        });
        
        //the listener to the menu item
        frameMenuItem.addActionListener(new ActionListener(){
        	
        	public void actionPerformed(ActionEvent evt) {

        		frameButton.doClick();
        	}
        });
        
        //setting the listener to the close event
        internalFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        internalFrame.addInternalFrameListener(new InternalFrameAdapter(){
        	
        	@Override
        	public void internalFrameClosing(InternalFrameEvent evt) {

        		frameButton.doClick();
        	}
        });
    }

    /**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
     * @return whether this tool frame is visible
     */
    public boolean isVisible(){
    	
    	return internalFrame.isVisible();
    }
    
    /**
     * sets whether the frame should be visible or not
     * @param visible
     */
    public void setVisible(boolean visible){
    	
    	internalFrame.setVisible(visible);
    }

	/**
	 * @return the tool bar button
	 */
	public JToggleButton getToolBarButton() {
		return frameButton;
	}

	/**
	 * @return the menu item t
	 */
	public JMenuItem getMenuItem() {
		return frameMenuItem;
	}

	/**
	 * @return Returns the editor.
	 */
	public Editor getSVGEditor() {
		return editor;
	}
	
	/**
	 * revalidates the tool frame
	 */
	public void revalidate(){
		
		internalFrame.pack();
	}

	/**
	 * @param visibilityChangedRunnable The visibilityChangedRunnable to set.
	 */
	public void setVisibilityChangedRunnable(Runnable visibilityChangedRunnable) {
		
		this.visibilityChangedRunnable=visibilityChangedRunnable;
	}

}
