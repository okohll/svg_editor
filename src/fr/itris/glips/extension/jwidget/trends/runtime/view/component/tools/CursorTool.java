package fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools;

import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.configuration.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.painters.*;
import java.awt.event.*;
import java.awt.*;
import java.text.*;
import java.util.*;

/**
 * the class of the tool used to select an horodate and a value on the trends component
 * @author ITRIS, Jordi SUC
 */
public class CursorTool extends JToggleButton{
	
	/**
	 * the line cursor id
	 */
	public static String lineId="LineCursor";
	
	/**
	 * the cross cursor id
	 */
	public static String crossId="CrossCursor";
	
	/**
	 * the current id
	 */
	private String id;
	
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
	 * the listener to the curves component
	 */
	private CursorToolListener cursorToolListener;
	
	/**
	 * the painter of the vertical line of the cursor
	 */
	private AnyPainter vlinePainter=new AnyPainter(Color.red, false);
	
	/**
	 * the painter of the horizontal line of the cursor
	 */
	private AnyPainter hlinePainter;
	
	/**
	 * the text cursor painter
	 */
	private TextCursorPainter textCursorPainter;
	
	/**
	 * whether the tool has been cleared
	 */
	private boolean cleared=true;
	
	/**
	 * whether this cursor tool is a cross tool
	 */
	private boolean isCross;
	
	/**
	 * the constructor of the class
	 * @param toolBar the tool bar component
	 * @param isCross whether this cursor tool is a cross tool
	 */
	public CursorTool(ToolBarComponent toolBar, boolean isCross){

		this.configuration=toolBar.getComponentsHandler().
				getView().getController().getConfiguration();
		this.componentsHandler=toolBar.getComponentsHandler();
		this.isCross=isCross;
		this.id=isCross?crossId:lineId;
		
		build();
	}
	
	/**
	 * builds the button
	 */
	protected void build(){
		
		//creating the horizontal line painter
		if(isCross){
			
			hlinePainter=new AnyPainter(Color.red, false);
		}
		
		//getting the label
		String label=TrendsBundle.bundle.getString(id);
		
		//setting the properties of the button
		setToolTipText(label);
		
		//getting the icon
		setIcon(TrendsIcons.getIcon(id, false));
		
		//adding a listener to the button
		listener=new ActionListener(){
			
			public void actionPerformed(ActionEvent evt) {
				
				if(isSelected()){
					
					//handling the sub mode
					if(configuration.getCurrentMode()==TrendsConfiguration.REAL_TIME_MODE &&
							configuration.getCurrentSubMode()==TrendsConfiguration.UPDATE){
						
						configuration.setCurrentSubMode(TrendsConfiguration.SCROLL);
					}
					
					componentsHandler.setCurrentExclusiveAction(id);
					componentsHandler.getCurvesComponent().
							addMouseListener(cursorToolListener);
					componentsHandler.getCurvesComponent().
						addMouseMotionListener(cursorToolListener);
				}
			}
		};
		
		addActionListener(listener);
		
		//creating the listener to the curves component used to 
		//select the zone to zoom in time
		cursorToolListener=new CursorToolListener();
		
		if(configuration.displayCursorHorodates()){
			
			//the text cursor painter
			textCursorPainter=new TextCursorPainter();
		}
	}
	
	/**
	 * sets the value of the given tag
	 * @param tagName the tag name
	 * @param value the tag value
	 */
	public void setTagValue(String tagName, Object value){

		if(configuration.displayCursorHorodates()){
			
			textCursorPainter.setValueString(value);
			textCursorPainter.validatePath();
			
			//repainting the curves component
			componentsHandler.getCurvesComponent().repaint(textCursorPainter.getBounds());
		}
	}
	
	/**
	 * disposes the object
	 */
	public void dispose(){

		clear();
		removeActionListener(listener);
		
		if(cursorToolListener!=null){
			
			cursorToolListener.removeListener();
		}
		
		listener=null;
		configuration=null;
		componentsHandler=null;
		cursorToolListener=null;
		vlinePainter=null;
		hlinePainter=null;
		textCursorPainter=null;
	}
	
	/**
	 * clears all the painted items
	 */
	public void clear(){
		
		if(! cleared){
			
			//removing the line painters
			componentsHandler.getCurvesComponent().removeAnyPainter(vlinePainter);
			
			if(isCross){
				
				componentsHandler.getCurvesComponent().removeAnyPainter(hlinePainter);
			}
			
			if(configuration.displayCursorHorodates()){
				
				componentsHandler.getCurvesComponent().removeAnyPainter(textCursorPainter);
			}
			
			//repainting the curves component
			componentsHandler.getCurvesComponent().repaint(vlinePainter.getBounds());
			
			if(isCross){
				
				componentsHandler.getCurvesComponent().repaint(hlinePainter.getBounds());
			}
			
			if(configuration.displayCursorHorodates()){
				
				//repainting the curves component
				componentsHandler.getCurvesComponent().repaint(textCursorPainter.getBounds());
			}
			
			cleared=true;
		}
	}
	
	/**
	 * the class of the cursor tool listener
	 * @author ITRIS, Jordi SUC
	 */
	protected class CursorToolListener extends MouseAdapter implements MouseMotionListener{
		
		@Override
		public void mouseReleased(MouseEvent evt) {
			
			boolean allowAction=componentsHandler.getCurrentExclusiveAction().equals(id);
			
			if(! allowAction){
				
				removeListener();
				
			}else{
				
				clear();
				
				//getting the bounds of the curves zone
				Rectangle curvesBounds=componentsHandler.
							getCurvesComponent().getCurvesZoneBounds();
				
				if(curvesBounds!=null && evt.getX()>=curvesBounds.x && 
						evt.getX()<=(curvesBounds.x+curvesBounds.width)){

					drawLine(evt.getPoint(), curvesBounds);
					long horodate=computeHorodate(evt.getPoint(), curvesBounds);
					drawHorodate(evt.getPoint(), horodate);
					requestTagValue(horodate);
				}
				
				cleared=false;
				allowAction=false;
			}
		}
		
		@SuppressWarnings(value="all")
		public void mouseMoved(MouseEvent evt) {

			boolean allowAction=componentsHandler.getCurrentExclusiveAction().equals(id);
			
			if(! allowAction){
				
				removeListener();
				
			}else{
				
				clear();
				cleared=false;
				
				//getting the bounds of the curves zone
				Rectangle curvesBounds=componentsHandler.
							getCurvesComponent().getCurvesZoneBounds();
				
				if(curvesBounds!=null && evt.getX()>=curvesBounds.x && 
						evt.getX()<=(curvesBounds.x+curvesBounds.width)){

					drawLine(evt.getPoint(), curvesBounds);
					resetTagValue();
					long horodate=computeHorodate(evt.getPoint(), curvesBounds);
					drawHorodate(evt.getPoint(), horodate);
				}
			}
		}
		
		@SuppressWarnings(value="all")
		public void mouseDragged(MouseEvent evt) {}

		/**
		 * draws the line corresponding to the given point
		 * @param point a point
		 * @param curvesBounds the bounds of the curves area
		 */
		protected void drawLine(Point point, Rectangle curvesBounds){
			
			if(point!=null){
				
				//creating the horizontal line
				vlinePainter.getPath().reset();
				vlinePainter.getPath().moveTo(point.x, 0);
				vlinePainter.getPath().lineTo(point.x, componentsHandler.getCurvesComponent().getHeight());
				vlinePainter.validatePath();
				componentsHandler.getCurvesComponent().addAnyPainter(vlinePainter);
				
				//painting the vertical line
				componentsHandler.getCurvesComponent().repaint(vlinePainter.getBounds());
				
				if(isCross){
					
					//creating the vertical line
					hlinePainter.getPath().reset();
					hlinePainter.getPath().moveTo(curvesBounds.x, point.y);
					hlinePainter.getPath().lineTo(curvesBounds.x+curvesBounds.width, point.y);
					hlinePainter.validatePath();
					componentsHandler.getCurvesComponent().addAnyPainter(hlinePainter);
					
					//painting the horizontal line
					componentsHandler.getCurvesComponent().repaint(hlinePainter.getBounds());
				}
			}
		}
		
		/**
		 * computes the horodate corresponding to the given point
		 * @param point a point
		 * @param curvesBounds the bounds of the curves panel
		 * @return the horodate corresponding to the given point
		 */
		protected long computeHorodate(Point point, Rectangle curvesBounds){
			
			long horodate=0;
			
			//computing the start date
			long currentDuration=configuration.getHorizontalAxisDuration();
			long startDate=componentsHandler.getView().getCurrentEndDate()-currentDuration;
			
			//getting the horodate corresponding to the point
			horodate=startDate+(long)Math.floor((point.x-curvesBounds.x)*
								currentDuration/curvesBounds.width);
			
			return horodate;
		}
		
		/**
		 * draws the horodate
		 * @param point the point
		 * @param horodate the horodate
		 */
		protected void drawHorodate(Point point, long horodate){
			
			if(configuration.displayCursorHorodates()){
				
				//clearing the text painter
				textCursorPainter.getPath().reset();
				
				//setting the new properties
				textCursorPainter.setHorodate(horodate);
				textCursorPainter.setLocation(point);
				textCursorPainter.validatePath();
				componentsHandler.getCurvesComponent().addAnyPainter(textCursorPainter);
				
				//repainting the curves component
				componentsHandler.getCurvesComponent().repaint(textCursorPainter.getBounds());
			}
		}
		
		/**
		 * resets the tag value
		 */
		protected void resetTagValue(){
			
			if(configuration.getTrendsCurveConfiguration()!=null){
				
				textCursorPainter.setValueString(null);
			}
		}
		
		/**
		 * requests the value of the current tag for the horodate
		 * @param horodate a horodate
		 */
		protected void requestTagValue(long horodate){
			
			//requesting the value of the tag corresponding to the horodate
			if(configuration.getTrendsCurveConfiguration()!=null){
				
				componentsHandler.getView().getController().getModel().
					requestTagValue(configuration.getTrendsCurveConfiguration().getTagName(), horodate);
			}
		}

		/**
		 * removes the listener to the curves component
		 */
		protected void removeListener(){
			
			clear();
			componentsHandler.getCurvesComponent().removeAnyPainter(vlinePainter);
			
			if(isCross){
				
				componentsHandler.getCurvesComponent().removeAnyPainter(hlinePainter);
			}
			
			componentsHandler.getCurvesComponent().
					removeMouseListener(cursorToolListener);
			componentsHandler.getCurvesComponent().
				removeMouseMotionListener(cursorToolListener);
		}
	}
	
	/**
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	protected class TextCursorPainter extends AnyPainter{
		
		/**
		 * the horodate string
		 */
		protected String horodateString="";
		
		/**
		 * the value string
		 */
		protected String valueString=null;
		
		/**
		 * the date format
		 */
		protected DateFormat dateFormat;
		
		/**
		 * the location of the rectangle that will be painted, i.e. the mouse point
		 */
		protected Point location=new Point();
		
		/**
		 * the location of the painted strings
		 */
		protected Point relativeHorodateLocation, relativeValueLocation;
		
		/**
		 * the font metrics
		 */
		protected FontMetrics fm;
		
		/**
		 * whether the value string should be drawn
		 */
		protected boolean drawVal=false;

		/**
		 * the constructor of the class
		 */
		public TextCursorPainter(){
			
			super(Color.darkGray, false);
			
			//creating the date format
			dateFormat=new SimpleDateFormat(configuration.getHorizontalAxisHorodatesFormat());
			
			//getting the font metrics
			fm=componentsHandler.getTrendsComponent().getGraphics().
				getFontMetrics(configuration.getFont());
		}
		
		/**
		 * sets the new horodate string
		 * @param horodate a horodate
		 */
		public void setHorodate(long horodate) {

			//creating a date
			Date currentDate=new Date(horodate);
			
			//getting the string representation of this date
			horodateString=dateFormat.format(currentDate);
		}

		/**
		 * sets the new value string
		 * @param value the new value
		 */
		public void setValueString(Object value) {

			valueString=null;
			
			if(value!=null){
				
				if(value instanceof String){
					
					valueString=(String)value;
					
				}else if(value instanceof Double){
					
					valueString=componentsHandler.getStringRepresentation((Double)value);
				}
			}
		}
		
		/**
		 * sets the location of the rectangle that will be painted, i.e. the mouse point
		 * @param location the new location
		 */
		public void setLocation(Point location) {
			
			this.location.x=location.x+5;
			this.location.y=location.y+5;
		}

		@Override
		public void validatePath(){
			
			//whether the value string should be drawn
			drawVal=(valueString!=null);
			
			//computing the rectangle into which the strings will be painted
			int width=10;
			int height=10;
			int hmargins=0;
			
			//computing the bounds of the rectangle and the location of the strings to be painted
			if(drawVal){

				width+=Math.max(fm.stringWidth(horodateString), fm.stringWidth(valueString));
				height+=2*(5+fm.getHeight());
				hmargins=5+fm.getHeight();
				
				relativeHorodateLocation=new Point(5, hmargins);
				hmargins=2*(5+fm.getHeight());
				relativeValueLocation=new Point(5, hmargins);
				
			}else{
				
				width+=fm.stringWidth(horodateString);
				height+=fm.getHeight();
				hmargins=5+fm.getHeight();
				relativeHorodateLocation=new Point(5, hmargins);
			}

			//creating the path
			path.reset();
			path.moveTo(location.x, location.y);
			path.lineTo(location.x+width, location.y);
			path.lineTo(location.x+width, location.y+height);
			path.lineTo(location.x, location.y+height);
			path.closePath();

			super.validatePath();
		}
		
		@Override
		public void paint(Graphics2D g){
			
			if(path!=null){
				
				g=(Graphics2D)g.create();
				
				//painting the path
				g.setColor(Color.white);
				g.fill(path);
				g.setColor(Color.darkGray);
				g.draw(path);
				
				//painting the strings
				g.setFont(configuration.getFont());
				g.drawString(horodateString, location.x+relativeHorodateLocation.x, 
						location.y+relativeHorodateLocation.y);
				
				if(drawVal){
					
					g.drawString(valueString, location.x+relativeValueLocation.x, 
							location.y+relativeValueLocation.y);
				}

				g.dispose();
			}
		}
	}
}
