package fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools;

import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.painters.*;
import java.awt.event.*;
import java.awt.*;

/**
 * the class of the tool used to modify the displayed time on the 
 * horizontal axis by means of the mouse
 * @author ITRIS, Jordi SUC
 */
public class MouseTimeZoomTool extends JButton{
	
	/**
	 * the  id
	 */
	public static String id="MouseTimeZoom";
	
	/**
	 * the listener to the button
	 */
	private ActionListener listener;
	
	/**
	 * the configuration object
	 */
	private TrendsConfiguration configuration;
	
	/**
	 * the components handler
	 */
	private ComponentsHandler componentsHandler;
	
	/**
	 * the listener to the curves component used to select the zone to zoom in time
	 */
	private MouseTimeZoomToolListener mouseTimeZoomToolListener;
	
	/**
	 * the painter of the first line of the time zone to zoom
	 */
	private AnyPainter firstLinePainter=new AnyPainter(Color.black, true);
	
	/**
	 * the painter of the second line of the time zone to zoom
	 */
	private AnyPainter secondLinePainter=new AnyPainter(Color.black, true);
	
	/**
	 * the constructor of the class
	 * @param toolBar the tool bar component
	 */
	public MouseTimeZoomTool(ToolBarComponent toolBar){

		this.configuration=toolBar.getComponentsHandler().
								getView().getController().getConfiguration();
		this.componentsHandler=toolBar.getComponentsHandler();
		build();
	}
	
	/**
	 * builds the button
	 */
	protected void build(){
		
		//getting the label
		String label=TrendsBundle.bundle.getString(id);
		
		//setting the properties of the button
		setToolTipText(label);
		
		//getting the icon
		setIcon(TrendsIcons.getIcon(id, false));
		
		//adding a listener to the button
		listener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				//handling the sub mode
				if(configuration.getCurrentMode()==TrendsConfiguration.REAL_TIME_MODE &&
						configuration.getCurrentSubMode()==TrendsConfiguration.UPDATE){
					
					configuration.setCurrentSubMode(TrendsConfiguration.SCROLL);
				}

				componentsHandler.setCurrentAction(id, true);
				
				componentsHandler.getCurvesComponent().
						addMouseListener(mouseTimeZoomToolListener);
				componentsHandler.getCurvesComponent().
						addMouseMotionListener(mouseTimeZoomToolListener);
			}
		};
		
		addActionListener(listener);
		
		//creating the listener to the curves component used to 
		//select the zone to zoom in time
		mouseTimeZoomToolListener=new MouseTimeZoomToolListener();
	}
	
	/**
	 * disposes the object
	 */
	public void dispose(){//TODO
		
		removeActionListener(listener);
		
		if(mouseTimeZoomToolListener!=null){
			
			mouseTimeZoomToolListener.removeListener();
		}
		
		listener=null;
		componentsHandler=null;
		configuration=null;
		mouseTimeZoomToolListener=null;
		firstLinePainter=null;
		secondLinePainter=null;
	}
	
	/**
	 * the class of the mouse time zoom tool
	 * @author ITRIS, Jordi SUC
	 */
	protected class MouseTimeZoomToolListener
				extends MouseAdapter implements MouseMotionListener{
		
		private Point firstPoint=null;

		@Override
		public void mousePressed(MouseEvent evt) {

			if(componentsHandler.getCurrentAction().equals(id)){

				//getting the bounds of the curves zone
				Rectangle curvesBounds=componentsHandler.
							getCurvesComponent().getCurvesZoneBounds();
				
				if(curvesBounds!=null && evt.getX()>=curvesBounds.x && 
						evt.getX()<=(curvesBounds.x+curvesBounds.width)){

					firstPoint=evt.getPoint();
					
					//creating the first line
					firstLinePainter.getPath().moveTo(firstPoint.x, curvesBounds.y);
					firstLinePainter.getPath().lineTo(firstPoint.x, curvesBounds.y+curvesBounds.height);
					firstLinePainter.validatePath();
					
					//painting the first line
					componentsHandler.getCurvesComponent().addAnyPainter(firstLinePainter);
					componentsHandler.getCurvesComponent().addAnyPainter(secondLinePainter);
					componentsHandler.getCurvesComponent().repaint(firstLinePainter.getBounds());
				}
				
			}else{
				
				removeListener();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent evt) {

			if(firstPoint!=null){
				
				//checking if the duration is longer than the delta between the first and the last date
				long newDuration=0;
				int x1=firstPoint.x;
				int x2=evt.getX();
				Rectangle curvesBounds=componentsHandler.getCurvesComponent().getCurvesZoneBounds();

				//getting the previous date
				long currentDuration=configuration.getHorizontalAxisDuration();
				long previousStartDate=componentsHandler.getView().getCurrentEndDate()-currentDuration;
				
				//computing the time for the both abscisses
				long firstClickedDate=previousStartDate+(long)Math.floor((x1-curvesBounds.x)*
														currentDuration/curvesBounds.width);
				
				long secondClickedDate=previousStartDate+(long)Math.floor((x2-curvesBounds.x)*
															currentDuration/curvesBounds.width);
				
				//computing the new duration
				newDuration=secondClickedDate-firstClickedDate;

				if(newDuration<2){
					
					newDuration=2;
				}
				
				//getting the bounds of the first line
				Rectangle firstLineBounds=firstLinePainter.getBounds();
				
				//getting the bounds of the first line
				Rectangle secondLineBounds=secondLinePainter.getBounds();
				
				//removing the painters
				componentsHandler.getCurvesComponent().removeAnyPainter(firstLinePainter);
				componentsHandler.getCurvesComponent().removeAnyPainter(secondLinePainter);
				
				//repainting the curves component
				componentsHandler.getCurvesComponent().repaint(firstLineBounds);
				componentsHandler.getCurvesComponent().repaint(secondLineBounds);
				
				//clearing the paths
				firstLinePainter.getPath().reset();
				secondLinePainter.getPath().reset();
				
				firstPoint=null;
				
				//setting the new start date
				componentsHandler.getView().setCurrentEndDate(secondClickedDate);
				
				//setting the new duration
				configuration.setHorizontalAxisDuration(newDuration);
			}
			
			componentsHandler.setCurrentAction("", true);
		}
		
		@SuppressWarnings(value="all")
		public void mouseDragged(MouseEvent evt) {

			//getting the bounds of the curves zone
			Rectangle curvesBounds=componentsHandler.
						getCurvesComponent().getCurvesZoneBounds();
			
			if(firstPoint!=null && curvesBounds!=null && evt.getX()>=curvesBounds.x && 
					evt.getX()<=(curvesBounds.x+curvesBounds.width)){
				
				//getting the previous bounds
				Rectangle previousBounds=new Rectangle(secondLinePainter.getBounds());
				
				//resetting the path
				secondLinePainter.getPath().reset();
				
				//add the new line
				secondLinePainter.getPath().moveTo(evt.getX(), curvesBounds.y);
				secondLinePainter.getPath().lineTo(evt.getX(), curvesBounds.y+curvesBounds.height);
				secondLinePainter.validatePath();
				
				//repainting the curves component
				componentsHandler.getCurvesComponent().repaint(
						previousBounds.union(secondLinePainter.getBounds()));
			}
		}
		
		@SuppressWarnings(value="all")
		public void mouseMoved(MouseEvent evt) {}
		
		/**
		 * removes the listener to the curves component
		 */
		protected void removeListener(){
			
			componentsHandler.getCurvesComponent().removeAnyPainter(firstLinePainter);
			componentsHandler.getCurvesComponent().removeAnyPainter(secondLinePainter);
			componentsHandler.getCurvesComponent().
					removeMouseListener(mouseTimeZoomToolListener);
			componentsHandler.getCurvesComponent().
					removeMouseMotionListener(mouseTimeZoomToolListener);
		}
	}
}
