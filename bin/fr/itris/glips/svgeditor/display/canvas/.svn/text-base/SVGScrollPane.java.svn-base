/*
 * Created on 23 mars 2004
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
package fr.itris.glips.svgeditor.display.canvas;

import java.awt.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.canvas.rulers.*;
import fr.itris.glips.svgeditor.display.handle.*;
import java.awt.event.*;

/**
 * @author ITRIS, Jordi SUC
 * the class of the scroll pane that will include the SVG canvas
 */
public class SVGScrollPane extends JPanel{
	
	/**
	 * the svg handle this scrollpane is associated to
	 */
	private SVGHandle handle;
	
	/**
	 * the canvas
	 */
	private SVGCanvas canvas;
	
	/**
	 * the inner scroll pane
	 */
	private JScrollPane innerScrollPane;
	
	/**
	 * the panel into which the svg image will be painted
	 */
	protected JPanel contentPanel;
	
	/**
	 * the panels containing the rulers
	 */
	protected JPanel northPanel, westPanel;
	
	/**
	 * the rulers
	 */
	protected Ruler horizontalRuler, verticalRuler;

	/**
	 * the corners
	 */
	protected Component northWestCornerBox, northEastCornerBox, southWestCornerBox;
	
	/**
	 * the scroll bars listener
	 */
	private AdjustmentListener adjustListener;
	
	/**
	 * the component listener
	 */
	private ComponentListener componentListener;
	
	/**
	 * the mouse wheel listener
	 */
	private MouseWheelListener theMouseWheelListener;
	
	/**
	 * whether the listener are disabled
	 */
	private int listenersDisabled=0;

	/**
	 * the constructor of the class
	 * @param handle the handle this scrollpane is linked to
	 */
	public SVGScrollPane(SVGHandle handle) {

		this.handle=handle;
		this.canvas=new SVGCanvas(this);
		setAutoscrolls(true);
		
		initializeScrollpane();
		innerScrollPane.setDoubleBuffered(true);
		innerScrollPane.setHorizontalScrollBarPolicy(
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		innerScrollPane.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	}
	
	/**
	 * the method used to initialize the scrollpane
	 */
	public void initializeScrollpane(){

		//canvasToolBar=new CanvasToolBar(this);
		
		//creates the content panel and setting the properties
		contentPanel=new JPanel();
		
		GridBagLayout gridBag=new GridBagLayout();
		contentPanel.setLayout(gridBag);
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.NONE;
		c.anchor=GridBagConstraints.CENTER;
		c.gridwidth=GridBagConstraints.REMAINDER;

		gridBag.setConstraints(canvas, c);
		contentPanel.add(canvas);

		//creates the scrollpane
		innerScrollPane=new JScrollPane(contentPanel);
		innerScrollPane.getHorizontalScrollBar().setUnitIncrement(
				innerScrollPane.getHorizontalScrollBar().getBlockIncrement());
		innerScrollPane.getVerticalScrollBar().setUnitIncrement(
				innerScrollPane.getVerticalScrollBar().getBlockIncrement());
		innerScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
		
		//creating the rulers
		horizontalRuler=new Ruler(this, true);
		verticalRuler=new Ruler(this, false);

		//creates the spacers and distributes the panels
		Dimension cornerSize=new Dimension(innerScrollPane.getVerticalScrollBar().getPreferredSize().width, 
				innerScrollPane.getVerticalScrollBar().getPreferredSize().width);
		
		northPanel=new JPanel(new BorderLayout(0, 0));
		northWestCornerBox=Box.createRigidArea(cornerSize);
		northPanel.add(northWestCornerBox, BorderLayout.LINE_START);
		northPanel.add(horizontalRuler, BorderLayout.CENTER);
		horizontalRuler.setPreferredSize(new Dimension(innerScrollPane.getWidth(), cornerSize.height));

		westPanel=new JPanel(new BorderLayout(0, 0));
		southWestCornerBox=Box.createRigidArea(cornerSize);
		westPanel.add(verticalRuler, BorderLayout.CENTER);
		verticalRuler.setPreferredSize(new Dimension(cornerSize.width, innerScrollPane.getHeight()));
		
		//building the svg scrollpane
		setLayout(new BorderLayout(0, 0));
		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.WEST, westPanel);
		add(BorderLayout.CENTER, innerScrollPane);
		innerScrollPane.setWheelScrollingEnabled(false);
		
		componentListener=new ComponentAdapter(){
			
			@Override
			public void componentResized(ComponentEvent evt) {

				if(listenersDisabled>=0){

					canvas.setRenderedRectangle(new Rectangle(
							innerScrollPane.getViewport().getViewRect()), true, false);
				}
			}
		};
		
		addComponentListener(componentListener);
		
		//adding the listener to the scrollbar changes
		adjustListener=new AdjustmentListener(){
			
			public void adjustmentValueChanged(AdjustmentEvent evt) {

				if(listenersDisabled>=0){
					
					refreshRulers();
					
					if(! evt.getValueIsAdjusting()){

						canvas.setRenderedRectangle(new Rectangle(
							innerScrollPane.getViewport().getViewRect()), false, false);
					}
				}
			}
		};
		
		innerScrollPane.getHorizontalScrollBar().addAdjustmentListener(adjustListener);
		innerScrollPane.getVerticalScrollBar().addAdjustmentListener(adjustListener);
		
		theMouseWheelListener=new MouseWheelListener(){
			
			public void mouseWheelMoved(MouseWheelEvent evt) {
	
				if(listenersDisabled>=0 && 
					evt.getScrollType()==MouseWheelEvent.WHEEL_UNIT_SCROLL){

					JScrollBar scrollBar=innerScrollPane.getVerticalScrollBar();
					scrollBar.setValue(scrollBar.getValue()+2*scrollBar.getBlockIncrement()*evt.getWheelRotation());
					refreshRulers();
					canvas.setRenderedRectangle(new Rectangle(
							innerScrollPane.getViewport().getViewRect()), false, false);
				}
			}
		};
		
		innerScrollPane.addMouseWheelListener(theMouseWheelListener);
	}
	
	/**
	 * disposes this scrollpane
	 */
	public void dispose(){

    	horizontalRuler.dispose();
    	verticalRuler.dispose();
		innerScrollPane.getHorizontalScrollBar().removeAdjustmentListener(adjustListener);
		innerScrollPane.getVerticalScrollBar().removeAdjustmentListener(adjustListener);
		innerScrollPane.removeMouseWheelListener(theMouseWheelListener);
		removeComponentListener(componentListener);
		canvas.dispose();
		removeAll();
		contentPanel.removeAll();
		contentPanel.setLayout(null);
		
		handle=null;
		canvas=null;
		innerScrollPane=null;
		contentPanel=null;
		northPanel=null;
		westPanel=null;
		horizontalRuler=null;
		verticalRuler=null;
		northWestCornerBox=null;
		northEastCornerBox=null;
		southWestCornerBox=null;
		adjustListener=null;
		componentListener=null;
	}

	/**
	 * @return the canvas
	 */
	public SVGCanvas getSVGCanvas() {
		return canvas;
	}

	/**
	 * @return the svg handle this scrollpane is associated to
	 */
	public SVGHandle getSVGHandle() {
		return handle;
	}
	
	/**
	 * updates the state of the rulers
	 */
	public void updateRulers(){
		
		//getting the rulers manager
		RulersParametersManager manager=
			Editor.getEditor().getHandlesManager().getRulersParametersHandler();
		boolean rulersVisible=manager.areRulersEnabled();
		
		horizontalRuler.setEnabled(rulersVisible);
		verticalRuler.setEnabled(rulersVisible);
		
		if(rulersVisible){
			
			add(BorderLayout.NORTH, northPanel);
			add(BorderLayout.WEST, westPanel);
			
		}else{
			
			remove(northPanel);
			remove(westPanel);
		}
		
		revalidate();
	}

	/**
	 * refreshes the rulers
	 */
	public void refreshRulers(){
		
		horizontalRuler.refreshRange();
		horizontalRuler.repaint();
		verticalRuler.refreshRange();
		verticalRuler.repaint();
	}

	/**
	 * @return the horizontal ruler range
	 */
	public double getHorizontalRulerRange() {
		return horizontalRuler.getRangeForAlignment();
	}
	
	/**
	 * @return the vertical ruler range
	 */
	public double getVerticalRulerRange() {
		return verticalRuler.getRangeForAlignment();
	}
	/**
	 * setting the scroll values
	 * @param scrollValues the scroll values
	 */
	public void setScrollValues(Dimension scrollValues){
		
		if(scrollValues!=null){

			innerScrollPane.getHorizontalScrollBar().setValue(scrollValues.width);
			innerScrollPane.getVerticalScrollBar().setValue(scrollValues.height);
		}
	}
	
	/**
	 * @return the scroll bar values
	 */
	public Dimension getScrollValues(){
		
		return new Dimension(innerScrollPane.getHorizontalScrollBar().getValue(), 
				innerScrollPane.getVerticalScrollBar().getValue());
	}

	/**
	 * @return the viewport
	 */
	public Rectangle getViewPortBounds(){
		
		return innerScrollPane.getViewport().getVisibleRect();
	}
	
	/**
	 * @return the scrollpane view port
	 */
	public JViewport getViewport(){
		
		return innerScrollPane.getViewport();
	}
	
	/**
	 * @return the canvas bounds
	 */
	public Rectangle getCanvasBounds(){
		
		Rectangle rect=new Rectangle();
		
		if(canvas.getBounds().width>=getViewPortBounds().width){
			
			rect.x=contentPanel.getX();
			
		}else{
			
			rect.x=canvas.getX();
		}
		
		if(canvas.getBounds().height>=getViewPortBounds().height){
			
			rect.y=contentPanel.getY();
			
		}else{
			
			rect.y=canvas.getY();
		}
		
		rect.width=canvas.getWidth();
		rect.height=canvas.getHeight();
		
		return rect;
	}
	
	/**
	 * @return the inner scrollpane
	 */
	public JScrollPane getInnerScrollpane(){
		
		return innerScrollPane;
	}
	
	/**
	 * sets whether the listeners should be enabled or not
	 * @param enabled whether the listeners should be enabled or not
	 */
	public void setListenersEnabled(boolean enabled){
		
		listenersDisabled+=(enabled?1:-1);
	}

}
