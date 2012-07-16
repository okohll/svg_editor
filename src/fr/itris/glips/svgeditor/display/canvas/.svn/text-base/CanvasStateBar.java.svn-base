package fr.itris.glips.svgeditor.display.canvas;

import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import fr.itris.glips.library.*;

/**
 * the class of the state bar used for displaying information on the canvas
 * @author ITRIS, Jordi SUC
 */
public class CanvasStateBar extends JLabel{
	
	/**
	 * the zoom factor
	 */
	private double zoomFactor=1.0;
	
	/**
	 * the current mouse position
	 */
	private Point2D mousePosition=null;
	
	/**
	 * the current zone size
	 */
	private Point2D zoneSize=null;
	
	/**
	 * the constructor of the class
	 */
	public CanvasStateBar(){

		build();
	}
	
	/**
	 * builds the state bar
	 */
	protected void build(){
		
		setBorder(new CompoundBorder(new EmptyBorder(2, 0, 0, 0), 
				new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED), 
						new EmptyBorder(2, 2, 2, 2))));
		updateBar();
	}
	
	/**
	 * updates the bar
	 */
	public void updateBar(){
		
		String newLabel="";
		
		newLabel=FormatStore.displayFormat.format(zoomFactor*100);
		newLabel+=" %  ";
		
		if(mousePosition!=null){
			
			newLabel+="("+FormatStore.displayFormat.format(mousePosition.getX())+", "+
				FormatStore.displayFormat.format(mousePosition.getY())+") ";
		}
		
		if(zoneSize!=null){
			
			newLabel+="("+FormatStore.displayFormat.format(zoneSize.getX())+", "+
				FormatStore.displayFormat.format(zoneSize.getY())+") ";
		}
		
		setText(newLabel);
	}

	/**
	 * sets the current mouse position
	 * @param mousePosition the current mouse position
	 */
	public void setMousePosition(Point2D mousePosition) {
		
		this.mousePosition=mousePosition;
		updateBar();
	}

	/**
	 * sets the current zone size
	 * @param zoneSize the current zone size
	 */
	public void setZoneSize(Point2D zoneSize) {
		
		this.zoneSize=zoneSize;
		updateBar();
	}

	/**
	 * setting the current zoom factor
	 * @param zoomFactor the new zoom factor
	 */
	public void setZoomFactor(double zoomFactor) {
		
		this.zoomFactor=zoomFactor;
		updateBar();
	}
}
