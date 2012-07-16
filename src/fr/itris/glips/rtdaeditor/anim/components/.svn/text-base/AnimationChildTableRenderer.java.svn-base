package fr.itris.glips.rtdaeditor.anim.components;

import java.awt.*;
import javax.swing.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.anim.widgets.*;

/**
 * the class of the renderer of the property table
 * @author ITRIS, Jordi SUC
 */
public class AnimationChildTableRenderer extends AnimationTableRenderer{
	
	/**
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(	JTable table, Object value, boolean isSelected, 
																						boolean hasFocus, int row, int column) {

		if(value!=null && value instanceof EditableItem){

			return Widget.getWidget((EditableItem)value, null, false, isSelected);
		}

		return null;
	}
}
