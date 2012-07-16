package fr.itris.glips.rtdaeditor.anim.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.rtdaeditor.anim.widgets.*;

/**
 * the class of the renderer of the property table
 * @author ITRIS, Jordi SUC
 */
public class AnimationPropertyTableRenderer extends AnimationTableRenderer{
	
	/**
	 * the jlabel used to display the name of each attribute
	 */
	private JLabel jlabel=new JLabel();
	
	/**
	 * the constructor of the class
	 */
	public AnimationPropertyTableRenderer() {
		
		jlabel.setOpaque(true);
		jlabel.setHorizontalAlignment(SwingConstants.RIGHT);
		jlabel.setBorder(new EmptyBorder(0, 0, 0, 7));
	}
	
	/**
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(	JTable table, Object value, boolean isSelected, 
																						boolean hasFocus, int row, int column) {

		if(value!=null && value instanceof EditableItem){
			
			EditableItem item=(EditableItem)value;

			switch (column){
			
			case 0 :

				if(item.isGroup()){
					
					jlabel.setText(item.getGroupLabel()+" :");
					
				}else{
					
					jlabel.setText(item.getLabel()+" :");
				}
				
				jlabel.setBackground(table.getBackground());
				jlabel.setForeground(table.getForeground());

				return jlabel;
				
			case 1 :
				
				return Widget.getWidget(item, null, false, isSelected);
			}
		}

		return null;
	}
}
