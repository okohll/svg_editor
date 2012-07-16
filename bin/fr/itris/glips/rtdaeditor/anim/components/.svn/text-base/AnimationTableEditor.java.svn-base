package fr.itris.glips.rtdaeditor.anim.components;

import javax.swing.*;
import javax.swing.table.*;

/**
 * the super class of the table editors
 * @author ITRIS, Jordi SUC
 */
public abstract class AnimationTableEditor extends AbstractCellEditor implements TableCellEditor{
	
	/**
	 * the table
	 */
	protected AnimationTable animationTable;
	
	/**
	 * the constructor of the class
	 * @param animationTable the table
	 */
	protected AnimationTableEditor(AnimationTable animationTable) {
		
		this.animationTable=animationTable;
	}
	
	/**
	 * stops any cell editing
	 */
	public void stopEditingCell() {
		
		fireEditingStopped();
	}
}
