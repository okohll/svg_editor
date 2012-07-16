package fr.itris.glips.extension.jwidget.popup.edition;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.image.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.base.edition.MenuItemsContainerEdition;
import fr.itris.glips.rtdaeditor.jwidget.*;

/**
 * the class of the widget of a button 
 * @author ITRIS, Jordi SUC
 */
public class PopUpEdition extends MenuItemsContainerEdition{ 
	
    /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
    public PopUpEdition(JWidgetManager jwidgetManager, Frame mainFrame) {

    	super(jwidgetManager, mainFrame, POP_UP, "PopUpMenuWidget", 11);
    }

    @Override
    protected BufferedImage createImage(Element jwidgetElement, Dimension size) {
    	
		//creating the image
		BufferedImage image=new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		
		String zoneLabel="";
		
		try {
			zoneLabel=bundle.getString("zoneLabel");
		}catch (Exception ex) {}
		
		JLabel label=new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText(zoneLabel);
		label.setForeground(Color.lightGray);
		label.setBorder(new LineBorder(Color.lightGray));
		label.setSize(size);
		
		label.print(image.getGraphics());
		
		return image;
    }
}
