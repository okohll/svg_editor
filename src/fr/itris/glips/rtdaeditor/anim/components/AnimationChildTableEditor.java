package fr.itris.glips.rtdaeditor.anim.components;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.anim.widgets.*;

/**
 * the class of the editor of the children table
 * @author ITRIS, Jordi SUC
 */
public class AnimationChildTableEditor extends AnimationTableEditor{

	/**
	 * the current item
	 */
	protected EditableItem currentItem=null;
	
	/**
	 * the selected row when the editing action began
	 */
	protected int lastSelectedRow=-1;
	
	/**
	 * the constructor of the class
	 * @param animationTable the table
	 */
	protected AnimationChildTableEditor(AnimationTable animationTable) {
		
		super(animationTable);
	}

	/**
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

		((AnimationTable)table).cancelOtherEditings();
		lastSelectedRow=row;
		
		if(value!=null && value instanceof EditableItem){
			
			final EditableItem item=(EditableItem)value;
			currentItem=item;

			Runnable validateRunnable=new Runnable() {
				
				public void run() {

					fireEditingStopped();
				}
			};
			
			return Widget.getWidget(item, validateRunnable, true, true);
		}

		return null;
	}
	
	@Override
	protected void fireEditingStopped() {

		super.fireEditingStopped();
		animationTable.getSelectionModel().
			setSelectionInterval(lastSelectedRow, lastSelectedRow);
	}
	
	/**
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {

		Object returnedValue=currentItem;
		currentItem=null;
		
		return returnedValue;
	}

	@Override
	public boolean isCellEditable(EventObject evt) {

		return true;
	}
}
