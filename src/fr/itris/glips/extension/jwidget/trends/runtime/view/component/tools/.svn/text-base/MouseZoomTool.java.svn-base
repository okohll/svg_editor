package fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools;

import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.painters.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * the class of the tool used to modify the displayed time on the 
 * horizontal axis and the vertical scale by means of the mouse
 * @author ITRIS, Jordi SUC
 */
public class MouseZoomTool extends JButton{
	
	/**
	 * the id
	 */
	public static String id="MouseZoom";
	
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
	 * the listener to the curves component used to select the zone to zoom in
	 */
	private MouseZoomToolListener mouseZoomToolListener;
	
	/**
	 * the painter of the first line of the zone to zoom
	 */
	private AnyPainter firstLinePainter=new AnyPainter(Color.black, true);
	
	/**
	 * the painter of the second line of the zone to zoom
	 */
	private AnyPainter secondLinePainter=new AnyPainter(Color.black, true);
	
	/**
	 * the painter of the third line of the zone to zoom
	 */
	private AnyPainter thirdLinePainter=new AnyPainter(Color.black, true);
	
	/**
	 * the painter of the fourth line of the zone to zoom
	 */
	private AnyPainter fourthLinePainter=new AnyPainter(Color.black, true);
	
	/**
	 * the constructor of the class
	 * @param toolBar the tool bar component
	 */
	public MouseZoomTool(ToolBarComponent toolBar){

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
		
		//setting the icon
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
						addMouseListener(mouseZoomToolListener);
				componentsHandler.getCurvesComponent().
						addMouseMotionListener(mouseZoomToolListener);
			}
		};
		
		addActionListener(listener);
		
		//creating the listener to the curves component used to 
		//select the zone to zoom in
		mouseZoomToolListener=new MouseZoomToolListener();
	}
	
	/**
	 * disposes the object
	 */
	public void dispose(){
		
		removeActionListener(listener);
		
		if(mouseZoomToolListener!=null){
			
			mouseZoomToolListener.removeListener();
		}
		
		
		
		listener=null;
		configuration=null;
		componentsHandler=null;
		mouseZoomToolListener=null;
		firstLinePainter=null;
		secondLinePainter=null;
		thirdLinePainter=null;
		fourthLinePainter=null;
	}
	
	/**
	 * the class of the mouse zoom tool
	 * @author ITRIS, Jordi SUC
	 */
	protected class MouseZoomToolListener
				extends MouseAdapter implements MouseMotionListener{
		
		private Point currentFirstPoint=null;

		@Override
		public void mousePressed(MouseEvent evt) {

			if(componentsHandler.getCurrentAction().equals(id)){

				//getting the bounds of the curves zone
				Rectangle curvesBounds=componentsHandler.
							getCurvesComponent().getCurvesZoneBounds();
				
				//normalizing the value of the coordinates
				currentFirstPoint=getNormalizedPoint(evt.getPoint(), curvesBounds);
				
				componentsHandler.getCurvesComponent().addAnyPainter(firstLinePainter);
				componentsHandler.getCurvesComponent().addAnyPainter(secondLinePainter);
				componentsHandler.getCurvesComponent().addAnyPainter(thirdLinePainter);
				componentsHandler.getCurvesComponent().addAnyPainter(fourthLinePainter);
				
			}else{
				
				currentFirstPoint=null;
				removeListener();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent evt) {//TODO

			if(currentFirstPoint!=null){
				
				//checking if the duration is longer than the delta between the first and the last date
				long newDuration=0;
				int x1=currentFirstPoint.x;
				Rectangle curvesBounds=componentsHandler.getCurvesComponent().getCurvesZoneBounds();
				
				//normalizing the value of the coordinates of the current point
				Point point=getNormalizedPoint(evt.getPoint(), curvesBounds);
				int x2=point.x;
				
				//getting the previous date
				long currentDuration=configuration.getHorizontalAxisDuration();
				long previousStartDate=componentsHandler.getView().getCurrentEndDate()-currentDuration;
				
				Date date=new Date(previousStartDate);
				Calendar cal=Calendar.getInstance();
				cal.setTime(date);
				
				if(componentsHandler.getHorizontalBarComponent()!=null){
					
					date=new Date(componentsHandler.getHorizontalBarComponent().getStartDate());
					
				}else{
					
					date=new Date(componentsHandler.getView().getController().getModel().getFirstDate());
				}

				cal=Calendar.getInstance();
				cal.setTime(date);

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
				
				//computing the vertical zoom data//
				
				//converting the two points into the 1.0 scale factor
				double scaledYFirstPoint=(currentFirstPoint.y-curvesBounds.y)/
																configuration.getVerticalZoomFactor()+
																	configuration.getVerticalZoomOrigin();
				
				double scaledYSecondPoint=(point.y-curvesBounds.y)/
																configuration.getVerticalZoomFactor()+
																	configuration.getVerticalZoomOrigin();
				
				//computing the new zoom data
				double verticalZoomFactor=
						curvesBounds.height/Math.abs(scaledYSecondPoint-scaledYFirstPoint);
				double verticalZoomOrigin=
					scaledYFirstPoint<scaledYSecondPoint?scaledYFirstPoint:scaledYSecondPoint;
				
				//clearing the paintings//
				
				//getting the bounds of the first line
				Rectangle firstLineBounds=firstLinePainter.getBounds();
				
				//getting the bounds of the second line
				Rectangle secondLineBounds=secondLinePainter.getBounds();
				
				//getting the bounds of the third line
				Rectangle thirdLineBounds=thirdLinePainter.getBounds();
				
				//getting the bounds of the fourth line
				Rectangle fourthLineBounds=fourthLinePainter.getBounds();
				
				//removing the painters
				componentsHandler.getCurvesComponent().removeAnyPainter(firstLinePainter);
				componentsHandler.getCurvesComponent().removeAnyPainter(secondLinePainter);
				componentsHandler.getCurvesComponent().removeAnyPainter(thirdLinePainter);
				componentsHandler.getCurvesComponent().removeAnyPainter(fourthLinePainter);
				
				//repainting the curves component
				componentsHandler.getCurvesComponent().repaint(firstLineBounds);
				componentsHandler.getCurvesComponent().repaint(secondLineBounds);
				componentsHandler.getCurvesComponent().repaint(thirdLineBounds);
				componentsHandler.getCurvesComponent().repaint(fourthLineBounds);
				
				//clearing the paths
				firstLinePainter.getPath().reset();
				secondLinePainter.getPath().reset();
				thirdLinePainter.getPath().reset();
				fourthLinePainter.getPath().reset();
				
				currentFirstPoint=null;
				
				if(verticalZoomFactor>1000){
					
					verticalZoomFactor=1000;
				}
				
				//setting the new zoom factors
				configuration.setVerticalZoomFactor(verticalZoomFactor, false);
				configuration.setVerticalZoomOrigin(verticalZoomOrigin);
				
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
			
			//normalizing the value of the coordinates
			Point point=getNormalizedPoint(evt.getPoint(), curvesBounds);

			if(currentFirstPoint!=null){
				
				//drawing the first lines
				drawLine(firstLinePainter, currentFirstPoint, new Point(currentFirstPoint.x, point.y));
				drawLine(secondLinePainter, new Point(currentFirstPoint.x, point.y), point);
				drawLine(thirdLinePainter, point, new Point(point.x, currentFirstPoint.y));
				drawLine(fourthLinePainter, new Point(point.x, currentFirstPoint.y), currentFirstPoint);
			}
		}
		
		/**
		 * returns the point corresponding to the given point but that 
		 * is bounded by the given rectangle
		 * @param point a point
		 * @param bounds the bounds
		 * @return the point corresponding to the given point but that 
		 * is bounded by the given rectangle
		 */
		protected Point getNormalizedPoint(Point point, Rectangle bounds){

			int x=point.x;
			int y=point.y;
			
			if(x<bounds.x){
				
				x=bounds.x;
			}
			
			if(x>bounds.x+bounds.width){
				
				x=bounds.x+bounds.width;
			}
			
			if(y<bounds.y){
				
				y=bounds.y;
			}
			
			if(y>bounds.y+bounds.height){
				
				y=bounds.y+bounds.height;
			}
			
			return new Point(x, y);
		}
		
		/**
		 * draws a line
		 * @param anyPainter a painter
		 * @param firstPoint the first point
		 * @param secondPoint the second point
		 */
		protected void drawLine(AnyPainter anyPainter, Point firstPoint, Point secondPoint){
			
			//resetting the path
			anyPainter.getPath().reset();
			
			//getting the previous bounds
			Rectangle previousBounds=new Rectangle(anyPainter.getBounds());
			
			//resetting the path
			anyPainter.getPath().reset();
			
			//add the new line
			anyPainter.getPath().moveTo(firstPoint.x, firstPoint.y);
			anyPainter.getPath().lineTo(secondPoint.x, secondPoint.y);
			anyPainter.validatePath();
			
			//repainting the curves component
			componentsHandler.getCurvesComponent().repaint(
					previousBounds.union(anyPainter.getBounds()));
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
					removeMouseListener(this);
			componentsHandler.getCurvesComponent().
					removeMouseMotionListener(this);
		}
	}
}
