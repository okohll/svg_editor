/*
 * Created on 19 janv. 2005
 * 
 =============================================
                   GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
GLIPS Graffiti Editor, a SVG Editor
Copyright (C) 2004 Jordi SUC, Philippe Gil, SARL ITRIS

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact : jordi.suc@itris.fr; philippe.gil@itris.fr

 =============================================
 */
package fr.itris.glips.svgeditor.properties;

import java.awt.*;
import javax.swing.*;

/**
 * the renderer for the combo box contained in the render chooser
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGComboResourceCellRenderer implements ListCellRenderer{
    
    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        JPanel panel=new JPanel();
        
        SVGComboResourceItem item=null;

        if(value instanceof SVGComboResourceItem){
            item=(SVGComboResourceItem)value;
            
            panel.setLayout(new BorderLayout(0, 0));
            //getting the resource image panel
            JComponent resourceImage=item.getResourceImage();
            
            if(resourceImage!=null){
            	
                panel.add(resourceImage, BorderLayout.WEST);
            }

            JLabel label=new JLabel(item.toString());
            label.setFont(list.getFont());
            panel.add(label, BorderLayout.CENTER);
            
            if(isSelected){
                
                if(resourceImage!=null){
                	
                	resourceImage.setBackground(list.getSelectionBackground());
                }
                
                panel.setBackground(list.getSelectionBackground());
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
                
            }else{
                
                if(resourceImage!=null){
                	
                	resourceImage.setBackground(list.getBackground());
                }
                
                panel.setBackground(list.getBackground());
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
        }

        return panel;
    }
}
