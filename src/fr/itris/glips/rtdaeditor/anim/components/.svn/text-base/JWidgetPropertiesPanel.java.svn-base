package fr.itris.glips.rtdaeditor.anim.components;

import java.awt.*;
import org.w3c.dom.*;
import javax.swing.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the panel used to modify the properties of a jwidget
 * @author ITRIS, Jordi SUC
 */
public class JWidgetPropertiesPanel extends JPanel{

	/**
	 * the jwidget manager
	 */
	private JWidgetManager jwigdetManager=null;
	
	/**
	 * the panel used to specify that no jwidget node is selected
	 */
	private JPanel noWidgetPanel=new JPanel();

	/**
	 * the constructor of the class
	 * @param jwigdetManager the jwidget manager
	 */
	public JWidgetPropertiesPanel(JWidgetManager jwigdetManager) {
		
		this.jwigdetManager=jwigdetManager;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//getting the label
		String noWidgetLabel="";
		
		try {
			noWidgetLabel=ResourcesManager.bundle.getString("label_nojwidget");
		}catch(Exception ex) {}
		
		 JLabel noWidgetLbl=new JLabel(noWidgetLabel);
		 noWidgetPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		 noWidgetPanel.add(noWidgetLbl);
		 
		 add(noWidgetPanel);
	}
	
	/**
	 * sets the selected element and checks whether it is a jwidget element
	 * @param element an element
	 */
	public void setElement(Element element) {
		
		removeAll();

		if(element!=null && fr.itris.glips.library.Toolkit.hasJWidgetChildElement(element)) {
			
			Element jwidgetElement=JWidgetManager.getJWidgetElement(element);
			String name=jwidgetElement.getAttribute(fr.itris.glips.library.Toolkit.nameAttribute);

			//getting the jwidget edition object corresponding to this jwidget element
			JWidgetEdition jwidgetEdition=jwigdetManager.getJWidgetEdition(name);

			if(jwidgetEdition!=null) {
				
				JPanel configurationPanel=jwidgetEdition.handleConfigurationPane(jwidgetElement);
				add(configurationPanel);
				return;
			}
			
		}else {
			
			//cleans the jwidget edition objects
			for(JWidgetEdition jwidgetEdition : jwigdetManager.getJWidgets()) {
				
				jwidgetEdition.handleConfigurationPane(null);
			}
		}
		
		 add(noWidgetPanel);
	}	
	
}
