package fr.itris.glips.extension.jwidget.radiobuttonbar.edition;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.base.edition.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import java.awt.image.*;
import java.util.*;

/**
 * the class of the widget of a button 
 * @author ITRIS, Jordi SUC
 */
public class RadioButtonBarEdition extends ButtonGroupEdition {
	
    /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
    public RadioButtonBarEdition(JWidgetManager jwidgetManager, Frame mainFrame) {

    	super(jwidgetManager, mainFrame, RADIO_BUTTON_BAR, "RadioButtonBarWidget", 13);
    }

    @Override
    protected BufferedImage createImage(Element jwidgetElement, Dimension size) {

    	//creating the image
    	BufferedImage image=new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    	JToolBar toolBar=new JToolBar();
    	
    	//setting the properties of the panel
    	toolBar.setOpaque(false);
    	toolBar.setFloatable(false);
    	toolBar.setBorder(new EmptyBorder(0, 0, 0, 0));

    	//getting the orientation of the panel
    	String orientationStr=jwidgetElement.getAttribute("orientation");
    	int orientation=orientationStr.equals("vertical")?SwingConstants.VERTICAL:SwingConstants.HORIZONTAL;
    	toolBar.setLayout(new BoxLayout(	toolBar, 
    														(orientation==SwingConstants.VERTICAL?BoxLayout.Y_AXIS:BoxLayout.X_AXIS)));
    	
		//creating all the child nodes components
		Element childJWidgetElement=null;
		String label="";
		JRadioButton button=null;
		
		Set<Component> components=new HashSet<Component>();
		
		for(Node childJWidgetNode=jwidgetElement.getFirstChild(); 
				childJWidgetNode!=null; 
				childJWidgetNode=childJWidgetNode.getNextSibling()) {
			
			if(childJWidgetNode instanceof Element && 
				childJWidgetNode.getNodeName().equals(
						fr.itris.glips.library.Toolkit.jwidgetElementName)) {
				
				childJWidgetElement=(Element)childJWidgetNode;
				label=childJWidgetElement.getAttribute(
					fr.itris.glips.library.Toolkit.labelAttribute);
				label=(label==null)?" ":label;
				
				button=new JRadioButton(label);
				button.setOpaque(false);
				toolBar.add(button);
				components.add(button);
			}
		}
    	
    	//handling the look for the buttons
		JWidgetToolkit.handleLook(jwidgetElement, components);

		//setting the size of the toolbar
		toolBar.setBounds(0, 0, size.width, size.height);
		toolBar.setPreferredSize(size);
		toolBar.doLayout();
		toolBar.print(image.getGraphics());
		
    	return image;
    }
}
