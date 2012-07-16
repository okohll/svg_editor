package fr.itris.glips.rtdaeditor.anim.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.rtdaeditor.anim.widgets.*;

/**
 * the class of the table used for modifying the properties of an animation
 * @author ITRIS, Jordi SUC
 */
public class AnimationTable extends JTable{
	
	/**
	 * the animation panel
	 */
	private AnimationPanel animationPanel=null;
	
	/**
	 * the model
	 */
	private AnimationTableModel tableModel=null;
	
	/**
	 * the renderer
	 */
	private AnimationTableRenderer tableRenderer=null;
	
	/**
	 * the cell editor
	 */
	private AnimationTableEditor tableEditor=null;
	
	/**
	 * whether this table is a property table
	 */
	private boolean isPropertyTable=false;
	
	/**
	 * the constructor of the class
	 * @param animationPanel the animation panel
	 * @param isPropertyTable whether the model should be for the properties or for the children objects
	 */
	public AnimationTable(AnimationPanel animationPanel, boolean isPropertyTable){
		
		this.animationPanel=animationPanel;
		this.isPropertyTable=isPropertyTable;
		
		//creating the model
		if(isPropertyTable){
			
			tableModel=new AnimationPropertyTableModel(this);
			tableRenderer=new AnimationPropertyTableRenderer();
			tableEditor=new AnimationPropertyTableEditor(this);
			
		}else{
			
			tableModel=new AnimationChildTableModel(this);
			tableRenderer=new AnimationChildTableRenderer();
			tableEditor=new AnimationChildTableEditor(this);
		}
		
		setModel(tableModel);
		setAutoCreateColumnsFromModel(true);

		if(tableEditor!=null) {

			setDefaultEditor(Object.class, tableEditor);
		}

		setAutoResizeMode(isPropertyTable?
			JTable.AUTO_RESIZE_ALL_COLUMNS:
				JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		setIntercellSpacing(new Dimension(1, 2));
		getTableHeader().setReorderingAllowed(false);
		setRowHeight(20);
		setBackground(Color.white);

		if(isPropertyTable) {
			
			setRowSelectionAllowed(false);
			setShowVerticalLines(false);
		}

		//setting the selection colors for the widgets
		Widget.setSelectionBackground(selectionBackground);
		Widget.setSelectionForeground(selectionForeground);
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int col) {
		
		if(tableRenderer!=null && row<getRowCount() && col<getColumnCount()){
			
			return tableRenderer;
		}
		
		return super.getCellRenderer(row, col);
	}
	
	/**
	 * sets the new animation object
	 * @param animationObject the new animation object
	 */
	public void setAnimationObject(AnimationObject animationObject){
		
		if(animationObject!=null){
			
			tableModel.setAnimationObject(animationObject);
			tableRenderer.setAnimationObject(animationObject);
			tableModel.fireTableStructureChanged();
		}
	}
	
	/**
	 * computes the columns width
	 */
	public void computeColumnsWidth() {
		
		String useLabel="", boundsLabel="";
		
		try {
			useLabel=ResourcesManager.bundle.getString("rtdaanim_used");
			boundsLabel=ResourcesManager.bundle.getString("rtdaanim_equal");
		}catch (Exception ex) {}
		
		Graphics g=getGraphics();
		
		if(g!=null) {

			int parentWidth=getParent().getWidth();
			int totalCharSize=0, totalCharNumber=0;
			double remainingSpaceForEachColumn=0;
			int[] textWidths=new int[getColumnCount()];
			char[] chars=null;
			int textWidth=0;
			
			//the number of enabled columns
			int columnsCount=0;
			
			//getting the length of each column text
			for(int i=0; i<getColumnCount(); i++) {

				textWidth=0;
				chars=getColumnName(i).toCharArray();
				
				for(int j=0; j<chars.length; j++) {
					
					textWidth+=g.getFontMetrics().charWidth(chars[j])+1;
				}

				textWidths[i]=textWidth;
				totalCharSize+=textWidth;
				
				if(! getColumnName(i).equals(useLabel) && ! getColumnName(i).equals(boundsLabel)) {
					
					totalCharNumber+=chars.length;
					columnsCount++;
				}
			}
			
			//computing the margin size that can be set for each column
			if(totalCharSize<parentWidth) {
				
				remainingSpaceForEachColumn=(parentWidth-totalCharSize)/columnsCount;
			}
			
			TableColumn column=null;
			int width=0;
			
			for(int i=0; i<getColumnCount(); i++) {

				column=getColumnModel().getColumn(i);
				width=textWidths[i];
				
				if(! getColumnName(i).equals(useLabel) && ! getColumnName(i).equals(boundsLabel)) {
					
					width=(int)Math.round(width+remainingSpaceForEachColumn);
				}
				
				column.setPreferredWidth(width);
			}
		}
	}
	
	/**
	 * resize all the columns
	 */
	public void resizeColumns(){

		if(! isPropertyTable) {
			
			computeColumnsWidth();
			resizeAndRepaint();
		}
	}

	/**
	 * @return the tableEditor
	 */
	public AnimationTableEditor getTableEditor() {
		return tableEditor;
	}
	
	/**
	 * cancels the editing on other tables
	 */
	public void cancelOtherEditings() {
		
		if(! isPropertyTable) {
			
			animationPanel.stopPropertiesCellEditing();
			
		}else {
			
			animationPanel.stopChildrenCellEditing();
		}
	}
	
	/**
	 * cleans this object from the unused listeners
	 */
	public void clean() {
		
		tableModel.clean();
		tableRenderer.clean();
	}

}
