package fr.itris.glips.extension.jwidget.menubar.edition;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.base.edition.*;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtdaeditor.jwidget.*;

/**
 * the class of the widget of a button 
 * @author ITRIS, Jordi SUC
 */
public class MenuBarEdition extends MenuItemsContainerEdition { 
	
    /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
    public MenuBarEdition(JWidgetManager jwidgetManager, Frame mainFrame) {

    	super(jwidgetManager, mainFrame, MENU_BAR, "MenuBarWidget", 10);
    }

    @Override
    protected BufferedImage createImage(Element jwidgetElement, Dimension size) {
    	
		//creating the image
		BufferedImage image=new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		JMenuBar menuBar=new JMenuBar();
		JWidgetToolkit.handleLook(jwidgetElement, menuBar);
		
		//filling the menu bar with the menu items defined
		//by the children that are directly under the jwidget element
		Element childJWidgetElement=null;
		String label="";
		JMenu menu=null;
		Set<Component> menusSet=new HashSet<Component>();
		
		for(Node childJWidgetNode=jwidgetElement.getFirstChild(); 
				childJWidgetNode!=null; 
					childJWidgetNode=childJWidgetNode.getNextSibling()) {
			
			if(childJWidgetNode instanceof Element && 
				childJWidgetNode.getNodeName().equals(Toolkit.jwidgetElementName)) {
				
				childJWidgetElement=(Element)childJWidgetNode;
				label=childJWidgetElement.getAttribute(Toolkit.labelAttribute);
				label=(label==null)?" ":label;
				
				menu=new JMenu(label);
				menusSet.add(menu);
				menuBar.add(menu);
			}
		}
		
		//handling the look of each menu item
		JWidgetToolkit.handleLook(jwidgetElement, menusSet);

		JFrame frame=new JFrame();
		frame.setSize(size);
		menuBar.setSize(size);
		menuBar.setPreferredSize(size);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setJMenuBar(menuBar);
		frame.pack();  	
		menuBar.print(image.getGraphics());
		
		return image;
    }
}
