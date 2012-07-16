package fr.itris.glips.svgeditor.selection;

import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.selection.*;
import java.util.*;
import javax.swing.*;

/**
 * the selection manager storing information for all the svg documents about the selections
 * @author ITRIS, Jordi SUC
 */
public class SelectionInfoManager {

	/**
	 * nothing should be done by the selection managers when in this mode
	 */
	public static final int NONE_MODE=0;
	
	/**
	 * the regular selection type
	 */
	public static final int REGULAR_MODE=1;
	
	/**
	 * the zone selection type
	 */
	public static final int ZONE_MODE=2;
	
	/**
	 * the zoom selection type
	 */
	public static final int ZOOM_MODE=3;
	
	/**
	 * the drawing selection type
	 */
	public static final int DRAWING_MODE=4;
	
	/**
	 * the selection items action mode, i.e. the mode that enables to 
	 * execute actions above selection items
	 */
	public static final int ITEMS_ACTION_MODE=5;
	
	/**
	 * the selection mode
	 */
	private int selectionMode=REGULAR_MODE;
	
	/**
	 * the tool button corresponding to the regular mode and the regular sub mode
	 */
	private JToggleButton regularButton;
	
	/**
	 * the current shape manager for the drawing mode
	 */
	private fr.itris.glips.svgeditor.shape.AbstractShape drawingShape;
	
	/**
	 * sets the tool button corresponding to the regular mode and the regular sub mode
	 * @param regularButton a tool button
	 */
	public void setRegularButton(JToggleButton regularButton){
		
		this.regularButton=regularButton;
	}
	
	/**
	 * sets the new selection mode
	 * @param selectionMode the selection mode
	 * @param drawingShape shape module or null if the mode is not the drawing mode
	 */
	public void setSelectionMode(int selectionMode, 
			fr.itris.glips.svgeditor.shape.AbstractShape drawingShape) {

		this.selectionMode=selectionMode;
		this.drawingShape=drawingShape;
		
		//setting the sub mode of each svg selection object to the regular sub mode
		Set<SVGHandle> handles=new HashSet<SVGHandle>(
				Editor.getEditor().getHandlesManager().getHandles());
		
		for(SVGHandle handle : handles){
			
			handle.getSelection().selectionModeChanged();
			handle.getSelection().setSelectionSubMode(Selection.REGULAR_SUB_MODE);
		}
	}
	
	/**
	 * @return the current shape module, used to draw shapes
	 */
	public fr.itris.glips.svgeditor.shape.AbstractShape getDrawingShape() {
		return drawingShape;
	}
	
	/**
	 * @return the current selection mode
	 */
	public int getSelectionMode() {
		return selectionMode;
	}
	
	/**
	 * sets the selection mode to none
	 */
	public void setToNoneMode(){
		
		//setting the mode to NONE and recording the cancel runnable
		this.selectionMode=NONE_MODE;
		this.drawingShape=null;
		
		//setting the sub mode of each svg selection object to the regular sub mode
		Set<SVGHandle> handles=new HashSet<SVGHandle>(Editor.getEditor().getHandlesManager().getHandles());
		
		for(SVGHandle handle : handles){
			
			handle.getSelection().setSelectionSubMode(Selection.REGULAR_SUB_MODE);
		}
	}
	
	/**
	 * sets the new mode to regular
	 */
	public void setToRegularMode(){
		
		//setting the mode to REGULAR and removing the cancel runnable
		this.selectionMode=REGULAR_MODE;
		this.drawingShape=null;
		
		//setting the sub mode of each svg selection object to the regular sub mode
		Set<SVGHandle> handles=new HashSet<SVGHandle>(Editor.getEditor().getHandlesManager().getHandles());
		
		for(SVGHandle handle : handles){
			
			handle.getSelection().setSelectionSubMode(Selection.REGULAR_SUB_MODE);
		}
		
		regularButton.setSelected(true);
	}
}
